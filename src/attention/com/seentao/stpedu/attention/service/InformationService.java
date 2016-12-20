package com.seentao.stpedu.attention.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterNewsService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

/**
 * <pre>项目名称：stpedu    
 * 类名称：InformationService    
 */
@Service
public class InformationService {

	@Autowired
	private CenterNewsService centerNewsService;//消息表
	
	@Autowired
	private TeachQuestionService teachQuestionService;
	
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	
	@Autowired
	private CenterLiveService centerLiveService;
	
	@Autowired
	private ClubTrainingQuestionService clubTrainingQuestionService;
	
	@Autowired
	private CenterUserService centerUserService;
	/**
	 * submitInvitation(邀请其他人或推送俱乐部回答问题操作)   
	 * @param userId 用户ID
	 * @param invitationType  邀请对象类型    1:邀请人；2:推送俱乐部；
	 * @param invitationObjectId 邀请对象id 人员id或俱乐部id
	 * @param questionId 问题id
	 * @author ligs
	 * @param classType 班级类型
	 * @param classId 问题所属班级ID
	 * @date 2016年6月28日 上午11:46:16
	 * @return
	 * @throws ParseException 
	 */
	public JSONObject submitInvitation(Integer userId, Integer invitationType, Integer invitationObjectId,Integer questionId, Integer classId, Integer classType)  {
		if(questionId == null || questionId == 0){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_PARAMETER_ERROR);
		}
		if(invitationType == GameConstants.INVITATION_PEOPLE){//邀请人
			if(classType == GameConstants.TEACHING_CLASS){
				//教学班
				return this.teachingInvite(userId, invitationObjectId, questionId);
			}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
				//俱乐部培训班
				return this.trainInvite(invitationObjectId, questionId, userId);
			}
		}else if(invitationType == GameConstants.PUSH_CLUB){//推送俱乐部
			if(classType == 1){
				//教学班
				return this.pushTeaching(questionId, invitationObjectId);
			}else if(classType == 2){
				//培训班
				return this.pushTrain(questionId, invitationObjectId);
			}	
		}
		return null;
	}
	/**
	 * 教学班邀请
	 * @return
	 */
	public JSONObject teachingInvite(Integer userId,Integer invitationObjectId,Integer questionId){ 
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		CenterUser iscenterUser = centerUserService.selectAllCenterUsers(centerUser);
		//根据人员ID和问题ID和消息类型查询【消息表】判断是否已经邀请过该人员，
		CenterNews centerNews = new CenterNews();
		centerNews.setUserId(invitationObjectId);
		centerNews.setRelObjectId(questionId);
		centerNews.setRelObjectType(2);//教学
		centerNews.setNewsType(GameConstants.INVITATION_PROBLEM);
		centerNews.setCreateUserId(userId);
		CenterNews isCenterNews =centerNewsService.getCenterNews(centerNews);
		if(isCenterNews != null){
			LogUtil.error(this.getClass(), "submitInvitation", "已经邀请过该用户回答问题");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_ANSWER_QUESTIONS);
		}else {
			//根据问题ID查询【问题表】得到问题标题
		    TeachQuestion teachQuestion = new TeachQuestion();
		    teachQuestion.setQuestionId(questionId);
		    TeachQuestion isTeachQuestion =teachQuestionService.getTeachQuestion(teachQuestion);
		   //时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
				LogUtil.error(this.getClass(), "submitInvitation","时间转换异常");
			}
		    try {
				int dateTime = (int) (parses.getTime() / 1000);
				//保存标题以及人员ID到【消息表】
				CenterNews insertCenterNews = new CenterNews();
				insertCenterNews.setCreateTime(dateTime);
				insertCenterNews.setUserId(invitationObjectId);
				insertCenterNews.setRelObjectId(questionId);
				insertCenterNews.setRelObjectType(GameConstants.TEACHING_PROBLEMS);
				insertCenterNews.setContent(iscenterUser.getNickName() + "邀请你回答教学班问题【" + isTeachQuestion.getTitle() + "】");
				insertCenterNews.setNewsType(GameConstants.INVITATION_PROBLEM);
				insertCenterNews.setCreateUserId(userId);
				centerNewsService.insertCenterNews(insertCenterNews);
				//用户消息状态表增加数据   先判断用户状态表是否有该用户存在
				/*CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
				centerNewsStatus.setUserId(invitationObjectId);
				CenterNewsStatus isCenterNewsStatus = centerNewsStatusService.getCenterNewsStatus(centerNewsStatus);
				if (isCenterNewsStatus == null) {
					//新建用户状态表
					CenterNewsStatus centerStatus = new CenterNewsStatus();
					centerStatus.setUserId(invitationObjectId);
					centerStatus.setIsNewQuestionInvite(GameConstants.ARE);
					centerStatus.setIsNewAttention(0);
					centerStatus.setIsNewGameInvite(0);
					centerStatus.setIsNewPrivateMessage(0);
					centerStatus.setIsNewSysNews(0);
					centerNewsStatusService.insertCenterNewsStatus(centerStatus);
					LogUtil.info(this.getClass(), "submitInvitation", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
				} else {
					//修改用户状态表
					CenterNewsStatus centerStatus = new CenterNewsStatus();
					centerStatus.setUserId(invitationObjectId);
					centerStatus.setIsNewQuestionInvite(GameConstants.ARE);
					centerNewsStatusService.updateCenterNewsStatusByKeyAll(centerStatus);
					LogUtil.info(this.getClass(), "submitInvitation", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
				} */
				centerNewsStatusService.submitOrUpdateSome(invitationObjectId, 2);
				LogUtil.info(this.getClass(), "submitInvitation", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
				
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "submitInvitation", "邀请失败");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INVITE_ERROR);
			}
		}
	}
	
	/**
	 * 俱乐部培训班邀请
	 * @return
	 */
	public JSONObject trainInvite(Integer invitationObjectId,Integer questionId,Integer userId){
		//根据人员ID和问题ID和消息类型查询【消息表】判断是否已经邀请过该人员，
		CenterNews centerNews = new CenterNews();
		centerNews.setUserId(invitationObjectId);
		centerNews.setRelObjectId(questionId);
		centerNews.setRelObjectType(3);//培训班
		centerNews.setNewsType(GameConstants.INVITATION_PROBLEM);
		centerNews.setCreateUserId(userId);
		CenterNews isCenterNews =centerNewsService.getCenterNews(centerNews);
		if(isCenterNews != null){
			LogUtil.error(this.getClass(), "submitInvitation", "已经邀请过该用户回答问题");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_ANSWER_QUESTIONS);
		}else {
			//根据问题ID查询【培训班问题表】得到问题标题
			ClubTrainingQuestion clubTrainingQuestion = new ClubTrainingQuestion();
			clubTrainingQuestion.setQuestionId(questionId);
			ClubTrainingQuestion getclubTrainingQuestion =clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
			//时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
				LogUtil.error(this.getClass(), "submitInvitation","时间转换异常");
			}
		    int dateTime = (int)(parses.getTime()/1000);
			//保存标题以及人员ID到【消息表】
			CenterUser centerUsers = centerUserService.getCenterUserById(userId);

		    CenterNews insertCenterNews = new CenterNews();
		    insertCenterNews.setCreateTime(dateTime);
		    insertCenterNews.setUserId(invitationObjectId);
		    insertCenterNews.setRelObjectId(questionId);
		    insertCenterNews.setRelObjectType(GameConstants.TRAINING_THE_PROBLEM);
		    insertCenterNews.setContent(centerUsers.getNickName()+"邀请你回答培训班问题【"+getclubTrainingQuestion.getTitle()+"】");
		    insertCenterNews.setNewsType(GameConstants.INVITATION_PROBLEM);
		    insertCenterNews.setCreateUserId(userId);
		    centerNewsService.insertCenterNews(insertCenterNews);
		    //用户消息状态表增加数据   先判断用户状态表是否有该用户存在
		   /* CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
		    centerNewsStatus.setUserId(invitationObjectId);
		    CenterNewsStatus isCenterNewsStatus =   centerNewsStatusService.getCenterNewsStatus(centerNewsStatus);
		    if(isCenterNewsStatus == null ){
		    	//新建用户状态表
		    	CenterNewsStatus centerStatus = new CenterNewsStatus();
		    	centerStatus.setUserId(invitationObjectId);
		    	centerStatus.setIsNewQuestionInvite(GameConstants.ARE);
		    	centerStatus.setIsNewAttention(0);
		    	centerStatus.setIsNewGameInvite(0);
		    	centerStatus.setIsNewPrivateMessage(0);
		    	centerStatus.setIsNewSysNews(0);
		    	centerNewsStatusService.insertCenterNewsStatus(centerStatus);
		    	LogUtil.info(this.getClass(), "submitInvitation", "成功");
		    	return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
		    }else {
		    	//修改用户状态表
		    	CenterNewsStatus centerStatus = new CenterNewsStatus();
		    	centerStatus.setUserId(invitationObjectId);
		    	centerStatus.setIsNewQuestionInvite(GameConstants.ARE);
		    	centerNewsStatusService.updateCenterNewsStatusByKeyAll(centerStatus);
		    	LogUtil.info(this.getClass(), "submitInvitation", "成功");
		    	return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
		    }*/
		    try {
		    	centerNewsStatusService.submitOrUpdateSome(invitationObjectId, 2);
		    	LogUtil.info(this.getClass(), "submitInvitation", "成功");
		    	return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_INVITE_SUCCESS);
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "submitInvitation", "get centerNewsStatus is error", e);
				return Common.getReturn(AppErrorCode.DEFAULT, AppErrorCode.IS_INVITE_ERROR);
			}
		}
	}
	
	
	
	
	/**
	 * 教学班推送
	 * @return
	 */
	public JSONObject pushTeaching(Integer questionId,Integer invitationObjectId){
		//通过问题id和关联对象类型和关联对象ID查询动态表判断是否已经推送
		CenterLive centerLive = new CenterLive();
		centerLive.setLiveType(7);//动态类型
		centerLive.setLiveMainType(9);//动态主题类型
		centerLive.setLiveMainId(questionId);
		centerLive.setMainObjetType(2);//动态用户类型
		centerLive.setMainObjetId(invitationObjectId);
		centerLive.setIsDelete(GameConstants.NO_DEL);
		CenterLive iscenterLive =centerLiveService.getCenterLive(centerLive);
		if(iscenterLive != null){
			LogUtil.error(this.getClass(), "submitInvitation", "该问题已经推送过俱乐部");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLUB);
		}
		//时间戳精确到秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(new Date());
	    Date parses = null;
		try {
			parses = sdf.parse(dates);
		} catch (ParseException e) {
			LogUtil.error(this.getClass(), "submitInvitation","时间转换异常");
		}
	    int dateTime = (int)(parses.getTime()/1000);
		//保存信息到动态表
		CenterLive addcenterLive = new CenterLive();
		addcenterLive.setLiveType(7);//动态类型
		addcenterLive.setLiveMainType(9);//动态主题类型
		addcenterLive.setLiveMainId(questionId);//动态主题ID
		addcenterLive.setMainObjetType(2);//动态用户类型  俱乐部动态
		addcenterLive.setCreateTime(dateTime);//创建时间戳
		addcenterLive.setIsDelete(GameConstants.NO_DEL);
		addcenterLive.setIsTop(0);
		addcenterLive.setMainObjetId(invitationObjectId);
		centerLiveService.insertCenterLive(addcenterLive);
		LogUtil.info(this.getClass(), "submitInvitation", "成功");
		return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.IS_PUSH_SUCCESS);
	}
	/**
	 * 俱乐部培训推送
	 * @return
	 */
	public JSONObject pushTrain(Integer questionId,Integer invitationObjectId){
		//通过问题id和关联对象类型和关联对象ID查询动态表判断是否已经推送，
		CenterLive centerLive = new CenterLive();
		centerLive.setLiveType(7);//动态类型
		centerLive.setLiveMainType(7);//动态主题类型
		centerLive.setLiveMainId(questionId);
		centerLive.setMainObjetType(2);//动态用户类型
		centerLive.setMainObjetId(invitationObjectId);
		centerLive.setIsDelete(GameConstants.NO_DEL);
		CenterLive iscenterLive =centerLiveService.getCenterLive(centerLive);
		if(iscenterLive != null){
			LogUtil.error(this.getClass(), "submitInvitation", "该问题已经推送过俱乐部");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLUB);
		}
		//时间戳精确到秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(new Date());
	    Date parses = null;
		try {
			parses = sdf.parse(dates);
		} catch (ParseException e) {
			LogUtil.error(this.getClass(), "submitInvitation","时间转换异常");
		}
	    int dateTime = (int)(parses.getTime()/1000);
		//保存信息到动态表
		CenterLive addcenterLive = new CenterLive();
		addcenterLive.setLiveType(7);//动态类型
		addcenterLive.setLiveMainType(7);//动态主题类型
		addcenterLive.setLiveMainId(questionId);//动态主题ID
		addcenterLive.setMainObjetType(2);//动态用户类型  俱乐部动态
		addcenterLive.setCreateTime(dateTime);//创建时间戳
		addcenterLive.setIsDelete(GameConstants.NO_DEL);
		addcenterLive.setIsTop(0);
		addcenterLive.setMainObjetId(invitationObjectId);
		centerLiveService.insertCenterLive(addcenterLive);
		LogUtil.info(this.getClass(), "submitInvitation", "成功");
		return Common.getReturn(AppErrorCode.SUCCESS,  AppErrorCode.IS_PUSH_SUCCESS);
	}
}
