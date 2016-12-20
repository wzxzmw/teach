package com.seentao.stpedu.attention.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.ClubJoinTrainingDao;
import com.seentao.stpedu.common.entity.ArenaCompetitionDiscuss;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTopicComment;
import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.service.ArenaCompetitionDiscussService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubTopicCommentService;
import com.seentao.stpedu.common.service.ClubTopicService;
import com.seentao.stpedu.common.service.ClubTrainingDiscussService;
import com.seentao.stpedu.common.service.TeachCourseDiscussService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

/**
 * 评论信息
 * <pre>项目名称：stpedu    
 * 类名称：GroupCommentService    
 */
@Service
public class GroupCommentService {

	@Autowired
	private CenterUserService centerUserService;//用户表
	
	@Autowired
	private TeachCourseDiscussService teachCourseDiscussService;
	
	@Autowired
	private ArenaCompetitionDiscussService arenaCompetitionDiscussService;
	
	@Autowired
	private ClubJoinTrainingDao clubJoinTrainingDao;
	
	
	@Autowired
	private ClubTrainingDiscussService clubTrainingDiscussService;
	
	@Autowired
	private ClubTopicCommentService clubTopicCommentService;
	
	@Autowired
	private CenterLiveService centerLiveService;
	
	@Autowired
	private ClubTopicService clubTopicService;
	
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;
	/**
	 * getComments(获取群组评论信息)
	 * @param classId 班级id
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id
	 * @param topicId 话题id
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @param start 起始页 从0开始
	 * @param limit 每页数量 
	 * @param inquireType
	 * @author ligs
	 * @param userId 用户ID
	 * @date 2016年6月28日 下午3:43:04
	 * @return
	 */
	public JSONObject getComments(Integer classId, Integer classType, Integer gameEventId, Integer topicId,Integer commentType, Integer start, Integer limit, Integer inquireType, Integer userId) {
		switch (inquireType) {
		case 1:/**
				*教学模块，获取群组讨论列表；
				*俱乐部模块，获取培训班级下的群组讨论列表；
				*动态模块，通过教学通知类型的动态，进入教学模块的群组页；
			    */
			if(classType == GameConstants.TEACHING_CLASS){
				//教学班
				return this.pcGetCommentOneClasses(userId, classId, commentType, limit, start);
			}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
				//俱乐部培训班
				return this.pcGetCommentOneClub(classId, limit, start);
			}
		break;
		case 2:
			//竞技场模块，通过赛事ID查询赛事讨论表获得赛事交流列表
			return this.getCommonArena(gameEventId, limit, start);
		case 3://通过话题ID查询俱乐部话题评论表获得数据
			//获取培训班讨论列表
			Map<String ,Object> paramMaps = new HashMap<String ,Object>();
			paramMaps.put("topicId", topicId);
			//调用分页组件
			QueryPage<ClubTopicComment> appQueryPages = QueryPageComponent.queryPage(limit, start, paramMaps, ClubTopicComment.class);
			//msg错误码
			if(!appQueryPages.getState()){
				LogUtil.error(this.getClass(), "getComments", "获取话题交流列表失败");
				appQueryPages.error(AppErrorCode.TOPIC_DISCUSSION);
			}
			for(ClubTopicComment clubTopicComment:appQueryPages.getList()){
				//评论发布者姓名 昵称
				if(clubTopicComment.getCreateUserId() != null){
					String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), clubTopicComment.getCreateUserId().toString());
					if(userMessage != null && !"".equals(userMessage)){
						CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
						clubTopicComment.setCreaterName(centerUsers.getRealName());//评论发布者姓名
						clubTopicComment.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
					}
				}
				CenterUser getcenterUser = new CenterUser();
				getcenterUser.setUserId(clubTopicComment.getCreateUserId());
				CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					/*
					 * 压缩图片
					 */
					clubTopicComment.setCreaterHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				clubTopicComment.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
				clubTopicComment.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
				clubTopicComment.setCommentId(clubTopicComment.getCommentId());//群组评论信息id
				clubTopicComment.setCreateTime(clubTopicComment.getCreateTime());//群组评论发布时间的时间戳
			}
			LogUtil.info(this.getClass(), "getComments", "成功");
			return appQueryPages.getMessageJSONObject("comments");
		}
		LogUtil.error(this.getClass(), "getCommentsForMobile", "请求参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	
	/**
	 * submitComment(提交群组评论信息)   
	 * @param userId 用户ID
	 * @param commentModule 群组评论功能所在模块 1:教学的群组模块；2:竞技场的赛事交流模块；3:俱乐部的培训班中的群组模块；4:俱乐部的话题模块
	 * @param classId 班级id 当 群组评论功能所在模块 为1和3时，会传递该值
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id 当 群组评论功能所在模块 为2时，会传递该值
	 * @param topicId 话题id 当 群组评论功能所在模块 为4时，会传递该值
	 * @param commentBody 群组评论内容
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @author ligs
	 * @date 2016年6月28日 下午8:32:50
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public JSONObject submitComment(Integer userId, Integer commentModule, Integer classId, Integer classType,Integer gameEventId, Integer topicId, String commentBody, Integer commentType) {
		switch (commentModule) {
		case GameConstants.TEACHING_GROUP://1:教学的群组模块
			if(commentType == GameConstants.MESSAGE_DISCUSSION){//1：讨论
				//通过用户ID查询【用户表】判断是否为该班级的学生或教师
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(userId);
				CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
				if(isCenterUser.getUserType() == 1){//如果为教师
					TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
					teachRelTeacherClass.setTeacherId(userId);
					teachRelTeacherClass.setClassId(classId);
					TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					if(isTeachRelTeacherClass == null){
						LogUtil.error(this.getClass(), "submitComment", "教师不属于该班级");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
					}
				}
				//校验群组评论内容字数
				boolean valueLength = Common.isValid(commentBody, 2, 150);
				if(valueLength == false){
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INPUT_ONE_HUNDRED_FIFTY);
				}
				//时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "submitComment", "日期转换异常");
				}
			    try {
					int dateTime = (int) (parses.getTime() / 1000);
					TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
					teachCourseDiscuss.setClassId(classId);
					teachCourseDiscuss.setType(GameConstants.MESSAGE_DISCUSSION);
					teachCourseDiscuss.setCreateUserId(userId);
					teachCourseDiscuss.setCreateTime(dateTime);
					teachCourseDiscuss.setPraiseNum(0);
					teachCourseDiscuss.setContent(commentBody);
					teachCourseDiscuss.setIsDelete(GameConstants.NO_DEL);
					teachCourseDiscussService.insertTeachCourseDiscuss(teachCourseDiscuss);
				} catch (Exception e) {
					LogUtil.info(this.getClass(), "submitComment", "失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO);
				}
				LogUtil.info(this.getClass(), "submitComment", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(commentType == GameConstants.MESSAGE_INFORM){//2：通知
				//通过用户ID查询【用户表】判断是否为该班级的学生或教师
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(userId);
				CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
				if(isCenterUser.getUserType() == 1){//如果为教师
					TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
					teachRelTeacherClass.setTeacherId(userId);
					teachRelTeacherClass.setClassId(classId);
					TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					if(isTeachRelTeacherClass == null){
						LogUtil.error(this.getClass(), "submitComment", "教师不属于该班级");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
					}
				}else if(isCenterUser.getUserType() == 2){//如果为学生
						LogUtil.error(this.getClass(), "submitComment", "不是该班级的教师");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLASS);
				}else if(isCenterUser.getUserType() == 3){//如果为用户
						LogUtil.error(this.getClass(), "submitComment", "不是该班级的教师");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLASS);
				}
				boolean valueLength = Common.isValid(commentBody, 2, 150);
				if(valueLength == false){
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INPUT_ONE_HUNDRED_FIFTY);
				}
				//时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "submitComment", "日期转换异常");
				}
			    try {
					int dateTime = (int) (parses.getTime() / 1000);
					TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
					teachCourseDiscuss.setClassId(classId);
					teachCourseDiscuss.setType(GameConstants.MESSAGE_INFORM);
					teachCourseDiscuss.setCreateUserId(userId);
					teachCourseDiscuss.setCreateTime(dateTime);
					teachCourseDiscuss.setPraiseNum(0);
					teachCourseDiscuss.setContent(commentBody);
					teachCourseDiscuss.setIsDelete(GameConstants.NO_DEL);
					@SuppressWarnings("unused")
					Integer mainId = teachCourseDiscussService.returninsertTeachCourseDiscuss(teachCourseDiscuss);
					//添加数据到动态表
					CenterLive centerLive = new CenterLive();
					centerLive.setLiveType(8);//动态类型 教学通知
					centerLive.setLiveMainType(8);//动态主题类型 教学通知
					centerLive.setLiveMainId(teachCourseDiscuss.getDiscussId());//动态主体ID
					centerLive.setIsTop(0);//是否置顶
					centerLive.setCreateTime(dateTime);
					centerLive.setIsDelete(GameConstants.NO_DEL);//是否删除
					centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_CLASS);
					centerLive.setMainObjetId(classId);
					centerLiveService.insertCenterLive(centerLive);
				} catch (Exception e) {
					LogUtil.info(this.getClass(), "submitComment", "失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO);
				}
				LogUtil.info(this.getClass(), "submitComment", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
			break;
		case GameConstants.ARENA_COMPETITION://2:竞技场的赛事交流模块
			boolean valueLength = Common.isValid(commentBody, 2, 150);
			if(valueLength == false){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INPUT_ONE_HUNDRED_FIFTY);
			}
			//时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
			}
		    int dateTime = (int)(parses.getTime()/1000);
			
		    ArenaCompetitionDiscuss arenaCompetitionDiscuss = new ArenaCompetitionDiscuss();
			arenaCompetitionDiscuss.setCompId(gameEventId);
			arenaCompetitionDiscuss.setCreateUserId(userId);
			arenaCompetitionDiscuss.setCreateTime(dateTime);
			arenaCompetitionDiscuss.setPraiseNum(0);
			arenaCompetitionDiscuss.setContent(commentBody);
			arenaCompetitionDiscuss.setIsDelete(GameConstants.NO_DEL);
			arenaCompetitionDiscussService.insertArenaCompetitionDiscuss(arenaCompetitionDiscuss);
			LogUtil.info(this.getClass(), "submitComment", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		case GameConstants.CLUB_CLASS_GROUP://3:俱乐部的培训班中的群组模块
			boolean valueLengths = Common.isValid(commentBody, 2, 150);
			if(valueLengths == false){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INPUT_ONE_HUNDRED_FIFTY);
			}
			//通过用户ID查询【参加培训人员表】判断该用户是否属于该班级
			ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
			clubJoinTraining.setUserId(userId);
			clubJoinTraining.setClassId(classId);
			clubJoinTraining.setIsDelete(GameConstants.NO_DEL);
			ClubJoinTraining isClubJoinTraining = clubJoinTrainingDao.selectClubJoinTraining(clubJoinTraining);
			if(isClubJoinTraining != null){
				//时间戳精确到秒
				SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdff.format(new Date());
			    Date parse = null;
				try {
					parse = sdff.parse(date);
				} catch (ParseException e) {
				}
			    int dateTimes = (int)(parse.getTime()/1000);
			    //保存数据到培训班讨论表
				ClubTrainingDiscuss clubTrainingDiscuss = new ClubTrainingDiscuss();
				clubTrainingDiscuss.setClassId(classId);
				clubTrainingDiscuss.setContent(commentBody);
				clubTrainingDiscuss.setCreateTime(dateTimes);
				clubTrainingDiscuss.setCreateUserId(userId);
				clubTrainingDiscuss.setPraiseNum(0);
				clubTrainingDiscuss.setIsDelete(GameConstants.NO_DEL);
				clubTrainingDiscussService.insertClubTrainingDiscuss(clubTrainingDiscuss);
				LogUtil.info(this.getClass(), "submitComment", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else {
				LogUtil.error(this.getClass(), "submitComment", "您不属于该培训班");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS);
			}
		case GameConstants.CLUB_TOPIC_MODULE://4:俱乐部的话题模块
			boolean isvalueLength = Common.isValid(commentBody, 2, 150);
			if(isvalueLength == false){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_INPUT_ONE_HUNDRED_FIFTY);
			}
			//时间戳精确到秒
			SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdff.format(new Date());
		    Date parse = null;
			try {
				parse = sdff.parse(date);
			} catch (ParseException e) {
			}
		    int dateTimes = (int)(parse.getTime()/1000);
		    //保存数据到俱乐部话题评论表
		    ClubTopicComment clubTopicComment = new ClubTopicComment();
		    clubTopicComment.setTopicId(topicId);
			clubTopicComment.setContent(commentBody);
		    clubTopicComment.setCreateUserId(userId);
		    clubTopicComment.setPraiseNum(0);
		    clubTopicComment.setCreateTime(dateTimes);
		    clubTopicComment.setIsFrozen(0);
		    clubTopicCommentService.insertClubTopicComment(clubTopicComment);
		    //增加俱乐部话题表评论数量
		    ClubTopic clubTopic = new ClubTopic();
		    clubTopic.setTopicId(topicId);
		    ClubTopic isClubTopic = clubTopicService.selectSingleClubTopic(clubTopic);
		    if(isClubTopic != null && isClubTopic.getCommentNum() != null) {
		    	 ClubTopic clubTopicup = new ClubTopic();
		    	 clubTopicup.setTopicId(topicId);
		    	 clubTopicup.setCommentNum(isClubTopic.getCommentNum() + 1);
		    	 clubTopicService.updateClubTopicByKey(clubTopicup);
		    }
		    JedisCache.delRedisVal(ClubTopic.class.getSimpleName(), topicId.toString());
		    LogUtil.info(this.getClass(), "submitComment", "成功");
		    return Common.getReturn(AppErrorCode.SUCCESS, "");
		}
		return null;
	}

	
	
	
	/**
	 * getCommentsForMobile(移动端：获取群组评论信息)
	 * @param userId 用户ID
	 * @param classId 班级id
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id
	 * @param topicId 话题id
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @param start 起始页 从0开始
	 * @param limit 每页数量 
	 * @param sortType 获取信息顺序   1:最新的；2:历史信息；
	 * @param newCommentId 最新的群组评论信息id
	 * @param oldCommentId 最早的群组评论信息id
	 * @param inquireType
	 * @author ligs
	 * @date 2016年7月19日 下午20:36:04
	 * @return
	 */
	public JSONObject getCommentsForMobile(Integer userId, Integer classId, Integer classType, Integer gameEventId,Integer topicId, Integer commentType, Integer start, Integer limit, Integer sortType, Integer newCommentId,Integer oldCommentId, Integer inquireType) {
		if(inquireType ==1){
			//commentType固定传1:讨论
			newCommentId = newCommentId == null ? 0 : newCommentId;
			oldCommentId = oldCommentId == null ? 0 : oldCommentId;
			
			if(newCommentId == 0 &&  oldCommentId == 0){//第一次请求返回最早和最新
				if(classType == 1){//教学班
					//通过用户ID查询【用户表】判断是否为该班级的学生或教师
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(userId);
					CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
					if(isCenterUser.getUserType() == 1){//如果为教师
						TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
						teachRelTeacherClass.setTeacherId(userId);
						teachRelTeacherClass.setClassId(classId);
						TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
						if(isTeachRelTeacherClass == null){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "教师不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 2){//如果为学生
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "学生不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 3){//如果为用户
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "用户不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}
						Map<String ,Object> paramMap = new HashMap<String ,Object>();
						paramMap.put("classId", classId);//班级ID
						paramMap.put("type",commentType);//讨论类型
						paramMap.put("isDelete", GameConstants.NO_DEL);
						if(start == null){
							start = 0;
						}
						//调用分页组件
						QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachCourseDiscuss.class);
						//msg错误码
						if(!appQueryPage.getState()){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
							appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
						}
						if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
							TeachCourseDiscuss newest= appQueryPage.getList().get(0);//最新的
							TeachCourseDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
							Map<String , Object> newOal = new HashMap<String,Object>();
							newOal.put("newCommentId", newest.getDiscussId());//最新评论ID
							newOal.put("oldCommentId", earliest.getDiscussId());//最早评论ID
							//反转List
							Collections.reverse(appQueryPage.getList());
						for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
							//评论发布者姓名
							if(getCourseMessage.getCreateUserId() != null){
								String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
								if(userMessage != null && !"".equals(userMessage)){
									CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
									getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
									getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
								}
							}
							CenterUser getcenterUser = new CenterUser();
							getcenterUser.setUserId(getCourseMessage.getCreateUserId());
							CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
							//通过教师头像id获得头像图片地址
							String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
							if(!StringUtil.isEmpty(imgUrl)){
								getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
							}
							getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));//群组评论信息id
							getCourseMessage.getContent();//群组评论信息内容
							getCourseMessage.setCreaterId(String.valueOf(getCourseMessage.getCreateUserId()));//评论发布者id
							getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
							getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
							getCourseMessage.getType();//群组评论类型
							getCourseMessage.getPraiseNum();//点赞数量
							getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
						}
						LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
						return appQueryPage.getMessageJSONObject("comments" , newOal);
					}else {
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId", "");//最新评论ID
						newOal.put("oldCommentId", "");//最新评论ID
						return appQueryPage.getMessageJSONObject("comments" , newOal);
					}
				}else if(classType == 2){//俱乐部培训班
					//获取培训班讨论列表
					Map<String ,Object> paramMap = new HashMap<String ,Object>();
					paramMap.put("classId", classId);
					paramMap.put("isDelete", GameConstants.NO_DEL);
					if(start == null){
						start = 0;
					}
					//调用分页组件
					QueryPage<ClubTrainingDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, ClubTrainingDiscuss.class);
					//msg错误码
					if(!appQueryPage.getState()){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
						appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
					}
					if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
						ClubTrainingDiscuss newest= appQueryPage.getList().get(0);//最新的
						ClubTrainingDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId",newest.getDiscussId() );//最新评论ID
						newOal.put("oldCommentId",earliest.getDiscussId() );//最早评论ID
						//反转List
						Collections.reverse(appQueryPage.getList());
					for(ClubTrainingDiscuss returnClubDiscuss:appQueryPage.getList()){
						//评论发布者姓名 昵称
						if( returnClubDiscuss.getCreateUserId() != null){
							String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnClubDiscuss.getCreateUserId().toString());
							if(userMessage != null && !"".equals(userMessage)){
								CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
								returnClubDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
								returnClubDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
							}
						}
						CenterUser getcenterUser = new CenterUser();
						getcenterUser.setUserId(returnClubDiscuss.getCreateUserId());
						CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
						//通过教师头像id获得头像图片地址
						String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(imgUrl)){
							returnClubDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
						}
						returnClubDiscuss.setCommentId(String.valueOf(returnClubDiscuss.getDiscussId()));
						returnClubDiscuss.setType(1);//俱乐部培训班评论类型固定传1
						returnClubDiscuss.setCreaterId(String.valueOf(returnClubDiscuss.getCreateUserId()));
						returnClubDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
						returnClubDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
						returnClubDiscuss.setCreateTime(returnClubDiscuss.getCreateTime());//群组评论发布时间的时间戳
					}
					LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
					return appQueryPage.getMessageJSONObject("comments" , newOal);
				}else {
					Map<String , Object> newOal = new HashMap<String,Object>();
					newOal.put("newCommentId", "");//最新评论ID
					newOal.put("oldCommentId", "");//最新评论ID
					return appQueryPage.getMessageJSONObject("comments" , newOal);
				}
			}
			}else if(sortType == 1){//最新的
				if(classType == 1){//教学班
					//通过用户ID查询【用户表】判断是否为该班级的学生或教师
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(userId);
					CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
					if(isCenterUser.getUserType() == 1){//如果为教师
						TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
						teachRelTeacherClass.setTeacherId(userId);
						teachRelTeacherClass.setClassId(classId);
						TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
						if(isTeachRelTeacherClass == null){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "教师不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 2){//如果为学生
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "学生不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 3){//如果为用户
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "用户不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}
					
						Map<String ,Object> paramMap = new HashMap<String ,Object>();
						paramMap.put("classId", classId);//班级ID
						paramMap.put("type",commentType);//讨论类型
						paramMap.put("isDelete", GameConstants.NO_DEL);
						paramMap.put("newCommentId", newCommentId);//最新的ID
						//调用分页组件
						if(start == null)
							start=0;
						QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachCourseDiscuss.class,"queryNewestByCount","queryNewestByPage");
						//msg错误码
						if(!appQueryPage.getState()){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
							appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
						}
						if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
							TeachCourseDiscuss newest= appQueryPage.getList().get(0);//最新的
							TeachCourseDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
							Map<String , Object> newOal = new HashMap<String,Object>();
							newOal.put("newCommentId", newest.getDiscussId());//最新评论ID
							newOal.put("oldCommentId", earliest.getDiscussId());//最早评论ID
							//反转List
							Collections.reverse(appQueryPage.getList());
						for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
							//评论发布者姓名
							if(getCourseMessage.getCreateUserId() != null){
								String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
								if(userMessage != null && !"".equals(userMessage)){
									CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
									getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
									getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
								}
							}
							CenterUser getcenterUser = new CenterUser();
							getcenterUser.setUserId(getCourseMessage.getCreateUserId());
							CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
							//通过教师头像id获得头像图片地址
							String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
							if(!StringUtil.isEmpty(imgUrl)){
								getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
							}
							getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));//群组评论信息id
							getCourseMessage.getContent();//群组评论信息内容
							getCourseMessage.setCreaterId(String.valueOf(getCourseMessage.getCreateUserId()));//评论发布者id
							getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
							getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
							getCourseMessage.getType();//群组评论类型
							getCourseMessage.getPraiseNum();//点赞数量
							getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
						}
						LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
						return appQueryPage.getMessageJSONObject("comments" , newOal);
						}else {
							Map<String , Object> newOal = new HashMap<String,Object>();
							newOal.put("newCommentId", newCommentId);//最新评论ID
							return appQueryPage.getMessageJSONObject("comments" , newOal);
						}
				}else if(classType == 2){//俱乐部培训班
					//获取培训班讨论列表
					Map<String ,Object> paramMap = new HashMap<String ,Object>();
					paramMap.put("classId", classId);
					paramMap.put("isDelete", GameConstants.NO_DEL);
					paramMap.put("newCommentId", newCommentId);//最新的ID
					if(start == null){
						start = 0;
					}
					//调用分页组件
					QueryPage<ClubTrainingDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubTrainingDiscuss.class,"queryByCountnew","queryByPagenew");
					//msg错误码
					if(!appQueryPage.getState()){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
						appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
					}
					if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
						ClubTrainingDiscuss newest= appQueryPage.getList().get(0);//最新的
						ClubTrainingDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
						
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId",newest.getDiscussId());//最新评论ID
						newOal.put("oldCommentId", earliest.getDiscussId());//最早评论ID
						//反转List
						Collections.reverse(appQueryPage.getList());
						for(ClubTrainingDiscuss returnClubDiscuss:appQueryPage.getList()){
							//评论发布者姓名 昵称
							if( returnClubDiscuss.getCreateUserId() != null){
								String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnClubDiscuss.getCreateUserId().toString());
								if(userMessage != null && !"".equals(userMessage)){
									CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
									returnClubDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
									returnClubDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
								}
							}
							CenterUser getcenterUser = new CenterUser();
							getcenterUser.setUserId(returnClubDiscuss.getCreateUserId());
							CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
							//通过教师头像id获得头像图片地址
							String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
							if(!StringUtil.isEmpty(imgUrl)){
								returnClubDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
							}
							returnClubDiscuss.setCommentId(String.valueOf(returnClubDiscuss.getDiscussId()));
							returnClubDiscuss.setType(1);//俱乐部培训班评论类型固定传1
							returnClubDiscuss.setCreaterId(String.valueOf(returnClubDiscuss.getCreateUserId()));//评论发布者id
							returnClubDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
							returnClubDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
							returnClubDiscuss.setCreateTime(returnClubDiscuss.getCreateTime());//群组评论发布时间的时间戳
						}
						LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
						return appQueryPage.getMessageJSONObject("comments" , newOal);
					}else {
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId", newCommentId);//最早评论ID
						return appQueryPage.getMessageJSONObject("comments" , newOal);
					}
					
				}
			}else if(sortType == 2){//2:历史信息；最早的
				if(classType ==1){//教学班
					//通过用户ID查询【用户表】判断是否为该班级的学生或教师
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(userId);
					CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
					if(isCenterUser.getUserType() == 1){//如果为教师
						TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
						teachRelTeacherClass.setTeacherId(userId);
						teachRelTeacherClass.setClassId(classId);
						TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
						if(isTeachRelTeacherClass == null){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "教师不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 2){//如果为学生
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "学生不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}else if(isCenterUser.getUserType() == 3){//如果为用户
						if(!isCenterUser.getClassId().equals(classId)){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "用户不属于该班级");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
						}
					}
					
						Map<String ,Object> paramMap = new HashMap<String ,Object>();
						paramMap.put("classId", classId);//班级ID
						paramMap.put("type",commentType);//讨论类型
						paramMap.put("isDelete", GameConstants.NO_DEL);
						paramMap.put("oldCommentId", oldCommentId);//最早的ID
						//调用分页组件
						if(start == null)
							start =0;
						QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachCourseDiscuss.class,"queryOldByCount","queryOldByPage");
						//msg错误码
						if(!appQueryPage.getState()){
							LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
							appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
						}
						if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
							TeachCourseDiscuss newest= appQueryPage.getList().get(0);//最新的
							TeachCourseDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
							
							Map<String , Object> newOal = new HashMap<String,Object>();
							newOal.put("newCommentId", newest.getDiscussId() > newCommentId ? newest.getDiscussId() : newCommentId);//最新评论ID
							newOal.put("oldCommentId", earliest.getDiscussId() < oldCommentId ? earliest.getDiscussId() : oldCommentId);//最早评论ID
							//反转List
							Collections.reverse(appQueryPage.getList());
							for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
								//评论发布者姓名
								if(getCourseMessage.getCreateUserId() != null){
									String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
									if(userMessage != null && !"".equals(userMessage)){
										CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
										getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
										getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
									}
								}
								CenterUser getcenterUser = new CenterUser();
								getcenterUser.setUserId(getCourseMessage.getCreateUserId());
								CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
								//通过教师头像id获得头像图片地址
								String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
								if(!StringUtil.isEmpty(imgUrl)){
									getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
								}
								getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));//群组评论信息id
								getCourseMessage.getContent();//群组评论信息内容
								getCourseMessage.setCreaterId(String.valueOf(getCourseMessage.getCreateUserId()));//评论发布者id
								getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
								getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
								getCourseMessage.getType();//群组评论类型
								getCourseMessage.getPraiseNum();//点赞数量
								getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
							}
							LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
							return appQueryPage.getMessageJSONObject("comments" , newOal);
						}else {
							Map<String , Object> newOal = new HashMap<String,Object>();
							newOal.put("oldCommentId", oldCommentId);//最早评论ID
							return appQueryPage.getMessageJSONObject("comments" , newOal);
						}
				}else if(classType ==2){//培训班
					//获取培训班讨论列表
					Map<String ,Object> paramMap = new HashMap<String ,Object>();
					paramMap.put("classId", classId);
					paramMap.put("isDelete", GameConstants.NO_DEL);
					paramMap.put("oldCommentId", oldCommentId);
					if(start == null){
						start = 0;
					}
					//调用分页组件
					QueryPage<ClubTrainingDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubTrainingDiscuss.class,"queryByCountOld","queryByPageOld");
					//msg错误码
					if(!appQueryPage.getState()){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
						appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
					}
					if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
					ClubTrainingDiscuss newest= appQueryPage.getList().get(0);//最新的
					ClubTrainingDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
					
					Map<String , Object> newOal = new HashMap<String,Object>();
					newOal.put("newCommentId", newest.getDiscussId());//最新评论ID
					newOal.put("oldCommentId", earliest.getDiscussId());//最早评论ID
					//反转List
					Collections.reverse(appQueryPage.getList());
					for(ClubTrainingDiscuss returnClubDiscuss:appQueryPage.getList()){
						//评论发布者姓名 昵称
						if( returnClubDiscuss.getCreateUserId() != null){
							String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnClubDiscuss.getCreateUserId().toString());
							if(userMessage != null && !"".equals(userMessage)){
								CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
								returnClubDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
								returnClubDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
							}
						}
						CenterUser getcenterUser = new CenterUser();
						getcenterUser.setUserId(returnClubDiscuss.getCreateUserId());
						CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
						//通过教师头像id获得头像图片地址
						String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(imgUrl)){
							returnClubDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
						}
						returnClubDiscuss.setCommentId(String.valueOf(returnClubDiscuss.getDiscussId()));
						returnClubDiscuss.setType(1);//俱乐部培训班评论类型固定传1
						returnClubDiscuss.setCreaterId(String.valueOf(returnClubDiscuss.getCreateUserId()));//评论发布者id
						returnClubDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
						returnClubDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
						returnClubDiscuss.setCreateTime(returnClubDiscuss.getCreateTime());//群组评论发布时间的时间戳
					}
					LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
					return appQueryPage.getMessageJSONObject("comments" , newOal);
				}else {
					Map<String , Object> newOal = new HashMap<String,Object>();
					newOal.put("oldCommentId", oldCommentId);//最早评论ID
					return appQueryPage.getMessageJSONObject("comments" , newOal);
				}
			}
		}
		}else if(inquireType ==2){
			//竞技场赛事交流模块
			if(newCommentId != null && oldCommentId != null &&newCommentId == 0 && oldCommentId == 0){
				//获取培训班讨论列表
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("compId", gameEventId);
				paramMap.put("isDelete", GameConstants.NO_DEL);
				//调用分页组件
				if(start == null){
					start = 0;
				}
				QueryPage<ArenaCompetitionDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, ArenaCompetitionDiscuss.class);
				//msg错误码
				if(!appQueryPage.getState()){
					LogUtil.error(this.getClass(), "getCommentsForMobile", "获取赛事交流列表失败");
					appQueryPage.error(AppErrorCode.COMPETITION_DISCUSSION);
				}
					if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
						ArenaCompetitionDiscuss newest= appQueryPage.getList().get(0);//最新的
						ArenaCompetitionDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId", newest.getDiscussId());//最新评论ID
						newOal.put("oldCommentId",earliest.getDiscussId());//最早评论ID
						//反转List
						Collections.reverse(appQueryPage.getList());
					for(ArenaCompetitionDiscuss returnCompetitionDiscuss:appQueryPage.getList()){
						//评论发布者姓名 昵称
						if(returnCompetitionDiscuss.getCreateUserId() != null){
							String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnCompetitionDiscuss.getCreateUserId().toString());
							if(userMessage != null && !"".equals(userMessage)){
								CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
								returnCompetitionDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
								returnCompetitionDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
							}
						}
						CenterUser getcenterUser = new CenterUser();
						getcenterUser.setUserId(returnCompetitionDiscuss.getCreateUserId());
						CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
						//通过教师头像id获得头像图片地址
						String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(imgUrl)){
							returnCompetitionDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
						}
						returnCompetitionDiscuss.setType(1);//俱乐部培训班评论类型固定传1
						returnCompetitionDiscuss.setCreaterId(String.valueOf(returnCompetitionDiscuss.getCreateUserId()));//评论发布者id
						returnCompetitionDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
						returnCompetitionDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
						returnCompetitionDiscuss.setCommentId(String.valueOf(returnCompetitionDiscuss.getDiscussId()));//群组评论信息id
						returnCompetitionDiscuss.setCreateTime(returnCompetitionDiscuss.getCreateTime());//群组评论发布时间的时间戳
					}
					LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
					return appQueryPage.getMessageJSONObject("comments",newOal);
				}else {
						Map<String , Object> newOal = new HashMap<String,Object>();
						newOal.put("newCommentId", "");//最早评论ID
						newOal.put("oldCommentId", "");//最早评论ID
						return appQueryPage.getMessageJSONObject("comments" , newOal);
				}
			}else if(sortType == 1){//最新的
				//获取培训班讨论列表
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("compId", gameEventId);
				paramMap.put("isDelete", GameConstants.NO_DEL);
				paramMap.put("newCommentId",newCommentId);//最新的ID
				if(start == null){
					start = 0;
				}
				//调用分页组件
				QueryPage<ArenaCompetitionDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ArenaCompetitionDiscuss.class,"queryCompNewByCount","queryCompNewByAge");
				//msg错误码
				if(!appQueryPage.getState()){
					LogUtil.error(this.getClass(), "getCommentsForMobile", "获取赛事交流列表失败");
					appQueryPage.error(AppErrorCode.COMPETITION_DISCUSSION);
				}
				if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
				ArenaCompetitionDiscuss newest= appQueryPage.getList().get(0);//最早的
				ArenaCompetitionDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最新的
				Map<String , Object> newOal = new HashMap<String,Object>();
//				newCommentId = earliest.getDiscussId() > newCommentId ? earliest.getDiscussId() : newCommentId;
				newOal.put("newCommentId", earliest.getDiscussId());//最新评论ID
				newOal.put("oldCommentId", newest.getDiscussId());//最早评论ID
				//反转List
				Collections.reverse(appQueryPage.getList());
				for(ArenaCompetitionDiscuss returnCompetitionDiscuss:appQueryPage.getList()){
					//评论发布者姓名 昵称
					if(returnCompetitionDiscuss.getCreateUserId() != null){
						String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnCompetitionDiscuss.getCreateUserId().toString());
						if(userMessage != null && !"".equals(userMessage)){
							CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
							returnCompetitionDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
							returnCompetitionDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
						}
					}
					CenterUser getcenterUser = new CenterUser();
					getcenterUser.setUserId(returnCompetitionDiscuss.getCreateUserId());
					CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						returnCompetitionDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					returnCompetitionDiscuss.setType(1);//俱乐部培训班评论类型固定传1
					returnCompetitionDiscuss.setCreaterId(String.valueOf(returnCompetitionDiscuss.getCreateUserId()));//评论发布者id
					returnCompetitionDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
					returnCompetitionDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
					returnCompetitionDiscuss.setCommentId(String.valueOf(returnCompetitionDiscuss.getDiscussId()));//群组评论信息id
					returnCompetitionDiscuss.setCreateTime(returnCompetitionDiscuss.getCreateTime());//群组评论发布时间的时间戳
				}
				LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
				return appQueryPage.getMessageJSONObject("comments",newOal);
			}else {
				Map<String , Object> newOal = new HashMap<String,Object>();
				newOal.put("newCommentId", newCommentId);//最新评论ID
				return appQueryPage.getMessageJSONObject("comments" , newOal);
			}
			}else if(sortType == 2){//最早的
				//获取培训班讨论列表
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("compId", gameEventId);
				paramMap.put("isDelete", GameConstants.NO_DEL);
				paramMap.put("oldCommentId",oldCommentId);//最新的ID
				if(start == null){
					start = 0;
				}
				//调用分页组件
				QueryPage<ArenaCompetitionDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ArenaCompetitionDiscuss.class,"queryCompoldByCount","queryCompoldByAge");
				//msg错误码
				if(!appQueryPage.getState()){
					LogUtil.error(this.getClass(), "getCommentsForMobile", "获取赛事交流列表失败");
					appQueryPage.error(AppErrorCode.COMPETITION_DISCUSSION);
				}
				if(appQueryPage.getList().size() > 0 && appQueryPage.getList() != null){
				ArenaCompetitionDiscuss newest= appQueryPage.getList().get(0);//最新的
				ArenaCompetitionDiscuss earliest= appQueryPage.getList().get(appQueryPage.getList().size() - 1);//最早的
				Map<String , Object> newOal = new HashMap<String,Object>();
//				oldCommentId = oldCommentId > earliest.getDiscussId() ? earliest.getDiscussId() : oldCommentId;
				newOal.put("newCommentId", newest.getDiscussId());//最新评论ID
				newOal.put("oldCommentId", earliest.getDiscussId() );//最早评论ID
				//反转List
				Collections.reverse(appQueryPage.getList());
				for(ArenaCompetitionDiscuss returnCompetitionDiscuss:appQueryPage.getList()){
						//评论发布者姓名 昵称
						if(returnCompetitionDiscuss.getCreateUserId() != null){
							CenterUser getcenterUser = new CenterUser();
							getcenterUser.setUserId(returnCompetitionDiscuss.getCreateUserId());
							CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
							if(getcenterUserAll != null){
									
								//通过教师头像id获得头像图片地址
								String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
								if(!StringUtil.isEmpty(imgUrl)){
									returnCompetitionDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
								}
									returnCompetitionDiscuss.setType(1);//俱乐部培训班评论类型固定传1
									returnCompetitionDiscuss.setCreaterId(String.valueOf(returnCompetitionDiscuss.getCreateUserId()));//评论发布者id
									returnCompetitionDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
									returnCompetitionDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
									returnCompetitionDiscuss.setCommentId(String.valueOf(returnCompetitionDiscuss.getDiscussId()));//群组评论信息id
									returnCompetitionDiscuss.setCreateTime(returnCompetitionDiscuss.getCreateTime());//群组评论发布时间的时间戳
			
									String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnCompetitionDiscuss.getCreateUserId().toString());
								if(userMessage != null && !"".equals(userMessage)){
									CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
									returnCompetitionDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
									returnCompetitionDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
								}
						}
					}
				}
				LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
				return appQueryPage.getMessageJSONObject("comments",newOal);
			}else {
				Map<String , Object> newOal = new HashMap<String,Object>();
				newOal.put("oldCommentId", oldCommentId);//最早评论ID
				return appQueryPage.getMessageJSONObject("comments" , newOal);
			}
		}
		}else if(inquireType == 3){
			//返回消息
			JSONObject msg = null;
			//俱乐部模块，从我的俱乐部主页下的话题列表进入话题详情页时，获取该话题的评论列表信息
			Map<String ,Object> paramMaps = new HashMap<String ,Object>();
			paramMaps.put("topicId", topicId);
			if(start == null){
				start = 0;
			}
			//调用分页组件
			QueryPage<ClubTopicComment> appQueryPages = QueryPageComponent.queryPage(limit, start, paramMaps, ClubTopicComment.class);
			//msg错误码
			if(!appQueryPages.getState()){
				LogUtil.error(this.getClass(), "getCommentsForMobile", "获取话题交流列表失败");
				appQueryPages.error(AppErrorCode.TOPIC_DISCUSSION);
			}
			if(appQueryPages.getList().size() > 0 ){
				
				for(ClubTopicComment clubTopicComment:appQueryPages.getList()){
					//评论发布者姓名 昵称
					if(clubTopicComment.getCreateUserId() != null){
						String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), clubTopicComment.getCreateUserId().toString());
						if(userMessage != null && !"".equals(userMessage)){
							CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
							clubTopicComment.setCreaterName(centerUsers.getRealName());//评论发布者姓名
							clubTopicComment.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
						}
					}
					CenterUser getcenterUser = new CenterUser();
					getcenterUser.setUserId(clubTopicComment.getCreateUserId());
					CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						/*
						 * 压缩图片
						 */
						clubTopicComment.setCreaterHeadLink(Common.checkPic(imgUrl)== true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					clubTopicComment.setCreaterId(String.valueOf(clubTopicComment.getCreateUserId()));//评论发布者id
					clubTopicComment.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
					clubTopicComment.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
					clubTopicComment.setCommentId(clubTopicComment.getCommentId());//群组评论信息id
					clubTopicComment.setCreateTime(clubTopicComment.getCreateTime());//群组评论发布时间的时间戳
					clubTopicComment.setType(1);//评论类型   俱乐部培训为固定1
				}
				
				
				msg = appQueryPages.resultListReverse().getMessageJSONObject("comments");
				Integer newId = appQueryPages.getList().get(appQueryPages.getList().size()-1).getCommentId();
				Integer oldId = appQueryPages.getList().get(0).getCommentId();
				
				msg.getJSONObject(GameConstants.RESULT).put("newCommentId", newId > newCommentId ? newId : newCommentId);
				msg.getJSONObject(GameConstants.RESULT).put("oldCommentId", oldCommentId <= 0 ? oldId : oldId < oldCommentId ? oldId : oldCommentId);
			}else{
				msg = appQueryPages.getMessageJSONObject("comments");
				msg.getJSONObject(GameConstants.RESULT).put("newCommentId", newCommentId );
				msg.getJSONObject(GameConstants.RESULT).put("oldCommentId", oldCommentId );
			}
			
			LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
			
			return msg;
		}else if(inquireType ==4){
			if(classType == 1){//教学班
				//通过用户ID查询【用户表】判断是否为该班级的学生或教师
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(userId);
				CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
				if(isCenterUser.getUserType() == 1){//如果为教师
					TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
					teachRelTeacherClass.setTeacherId(userId);
					teachRelTeacherClass.setClassId(classId);
					TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					if(isTeachRelTeacherClass == null){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "教师不属于该班级");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
					}
				}else if(isCenterUser.getUserType() == 2){//如果为学生
					if(!isCenterUser.getClassId().equals(classId)){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "学生不属于该班级");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
					}
				}else if(isCenterUser.getUserType() == 3){//如果为用户
					if(!isCenterUser.getClassId().equals(classId)){
						LogUtil.error(this.getClass(), "getCommentsForMobile", "用户不属于该班级");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
					}
				}
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("classId", classId);
				paramMap.put("type",GameConstants.MESSAGE_INFORM);
				paramMap.put("isDelete", GameConstants.NO_DEL);
				if(start == null){
					start = 0;
				}
				//调用分页组件
				QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachCourseDiscuss.class);
				//msg错误码
				if(!appQueryPage.getState()){
					LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
					appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
				}
				
				for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
					//评论发布者姓名 昵称
					if(getCourseMessage.getCreateUserId() != null){
						String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
						if(userMessage != null && !"".equals(userMessage)){
							CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
							getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
							getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
						}
					}
					CenterUser getcenterUser = new CenterUser();
					getcenterUser.setUserId(getCourseMessage.getCreateUserId());
					CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));
					getCourseMessage.setCreaterId(String.valueOf(getCourseMessage.getCreateUserId()));//评论发布者id
					getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
					getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
					getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
				}
				LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
				return appQueryPage.getMessageJSONObject("comments");
			}else if(classType == 2){//俱乐部培训班
				//获取培训班讨论列表
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("classId", classId);
				paramMap.put("isDelete", GameConstants.NO_DEL);
				if(start == null){
					start = 0;
				}
				//调用分页组件
				QueryPage<ClubTrainingDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, ClubTrainingDiscuss.class);
				//msg错误码
				if(!appQueryPage.getState()){
					LogUtil.error(this.getClass(), "getCommentsForMobile", "获取评论列表失败");
					appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
				}
				for(ClubTrainingDiscuss returnClubDiscuss:appQueryPage.getList()){
					//评论发布者姓名 昵称
					if( returnClubDiscuss.getCreateUserId() != null){
						String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnClubDiscuss.getCreateUserId().toString());
						if(userMessage != null && !"".equals(userMessage)){
							CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
							returnClubDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
							returnClubDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
						}
					}
					CenterUser getcenterUser = new CenterUser();
					getcenterUser.setUserId(returnClubDiscuss.getCreateUserId());
					CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						returnClubDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					returnClubDiscuss.setCommentId(String.valueOf(returnClubDiscuss.getDiscussId()));
					returnClubDiscuss.setType(1);//俱乐部培训班评论类型固定传1
					returnClubDiscuss.setCreaterId(String.valueOf(returnClubDiscuss.getCreateUserId()));//评论发布者id
					returnClubDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
					returnClubDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
					returnClubDiscuss.setCreateTime(returnClubDiscuss.getCreateTime());//群组评论发布时间的时间戳
				}
				LogUtil.info(this.getClass(), "getCommentsForMobile", "成功");
				return appQueryPage.getMessageJSONObject("comments");
			}
		}
		LogUtil.error(this.getClass(), "getCommentsForMobile", "请求参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	/**
	 * PC:获取群组评论inquireType：1
	 * 教学班
	 * @return
	 */
	public JSONObject pcGetCommentOneClasses(Integer userId,Integer classId,Integer commentType,Integer limit,Integer start){
		//通过用户ID查询【用户表】判断是否为该班级的学生或教师
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
		if(isCenterUser.getUserType() == 1){//如果为教师
			TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
			teachRelTeacherClass.setTeacherId(userId);
			teachRelTeacherClass.setClassId(classId);
			TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
			if(isTeachRelTeacherClass == null){
				LogUtil.error(this.getClass(), "submitComment", "教师不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}else if(isCenterUser.getUserType() == 2){//如果为学生
			if(!isCenterUser.getClassId().equals(classId)){
				LogUtil.error(this.getClass(), "submitComment", "学生不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}else if(isCenterUser.getUserType() == 3){//如果为用户
			if(!isCenterUser.getClassId().equals(classId)){
				LogUtil.error(this.getClass(), "submitComment", "用户不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}
		if(commentType == GameConstants.MESSAGE_DISCUSSION){//讨论
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("classId", classId);
			paramMap.put("type",GameConstants.MESSAGE_DISCUSSION);
			paramMap.put("isDelete", GameConstants.NO_DEL);
			//调用分页组件
			QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachCourseDiscuss.class);
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getComments", "获取评论列表失败");
				appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
			}
			
			for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
				//评论发布者姓名
				if(getCourseMessage.getCreateUserId() != null){
					String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
					if(userMessage != null && !"".equals(userMessage)){
						CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
						getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
						getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
					}
				}
				CenterUser getcenterUser = new CenterUser();
				getcenterUser.setUserId(getCourseMessage.getCreateUserId());
				CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));//群组评论信息id
				getCourseMessage.getContent();//群组评论信息内容
				getCourseMessage.getCreateUserId();//评论发布者id
				getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
				getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
				
				getCourseMessage.getType();//群组评论类型
				getCourseMessage.getPraiseNum();//点赞数量
				getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
			}
			LogUtil.info(this.getClass(), "getComments", "成功");
			return appQueryPage.getMessageJSONObject("comments");
		}else if(commentType == GameConstants.MESSAGE_INFORM){//通知
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("classId", classId);
			paramMap.put("type",GameConstants.MESSAGE_INFORM);
			paramMap.put("isDelete", GameConstants.NO_DEL);
			//调用分页组件
			QueryPage<TeachCourseDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachCourseDiscuss.class);
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getComments", "获取评论列表失败");
				appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
			}
			
			for (TeachCourseDiscuss getCourseMessage : appQueryPage.getList()) {
				//评论发布者姓名 昵称
				if(getCourseMessage.getCreateUserId() != null){
					String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), getCourseMessage.getCreateUserId().toString());
					if(userMessage != null && !"".equals(userMessage)){
						CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
						getCourseMessage.setCreaterName(centerUsers.getRealName());//评论发布者姓名
						getCourseMessage.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
					}
				}
				CenterUser getcenterUser = new CenterUser();
				getcenterUser.setUserId(getCourseMessage.getCreateUserId());
				CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					getCourseMessage.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				getCourseMessage.setCommentId(String.valueOf(getCourseMessage.getDiscussId()));//群组评论信息id
				getCourseMessage.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
				getCourseMessage.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
				getCourseMessage.setCreateTime(getCourseMessage.getCreateTime());//群组评论发布时间的时间戳
			}
			LogUtil.info(this.getClass(), "getComments", "成功");
			return appQueryPage.getMessageJSONObject("comments");
		}
		LogUtil.error(this.getClass(), "getCommentsForMobile", "请求参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	
	/**
	 * PC:获取群组评论inquireType：1
	 * 俱乐部培训班
	 * @return
	 */
	public JSONObject pcGetCommentOneClub(Integer classId,Integer limit,Integer start){
		//获取培训班讨论列表
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("classId", classId);
		paramMap.put("isDelete", GameConstants.NO_DEL);
		//调用分页组件
		QueryPage<ClubTrainingDiscuss> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubTrainingDiscuss.class,"queryCountOne","queryPageOne");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getComments", "获取评论列表失败");
			appQueryPage.error(AppErrorCode.GET_COMMENT_ERROR);
		}
		for(ClubTrainingDiscuss returnClubDiscuss:appQueryPage.getList()){
			//评论发布者姓名 昵称
			if( returnClubDiscuss.getCreateUserId() != null){
				String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnClubDiscuss.getCreateUserId().toString());
				if(userMessage != null && !"".equals(userMessage)){
					CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
					returnClubDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
					returnClubDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
				}
			}
			CenterUser getcenterUser = new CenterUser();
			getcenterUser.setUserId(returnClubDiscuss.getCreateUserId());
			CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				returnClubDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			returnClubDiscuss.setCommentId(String.valueOf(returnClubDiscuss.getDiscussId()));//群组评论信息id
			returnClubDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
			returnClubDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
			returnClubDiscuss.setCreateTime(returnClubDiscuss.getCreateTime());//群组评论发布时间的时间戳
		}
		LogUtil.info(this.getClass(), "getComments", "成功");
		return appQueryPage.getMessageJSONObject("comments");
	}
	
	
	/**
	 * PC:获取群组评论inquireType：2
	 * 竞技场模块，通过赛事ID查询赛事讨论表获得赛事交流列表
	 * @return
	 */
	public JSONObject getCommonArena(Integer gameEventId,Integer limit,Integer start){
		//获取培训班讨论列表
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("compId", gameEventId);
		paramMap.put("isDelete", GameConstants.NO_DEL);
		//调用分页组件
		QueryPage<ArenaCompetitionDiscuss> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, ArenaCompetitionDiscuss.class);
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getComments", "获取赛事交流列表失败");
			appQueryPage.error(AppErrorCode.COMPETITION_DISCUSSION);
		}
		for(ArenaCompetitionDiscuss returnCompetitionDiscuss:appQueryPage.getList()){
			//评论发布者姓名 昵称
			if(returnCompetitionDiscuss.getCreateUserId() != null){
				String userMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), returnCompetitionDiscuss.getCreateUserId().toString());
				if(userMessage != null && !"".equals(userMessage)){
					CenterUser centerUsers = JSONObject.parseObject(userMessage, CenterUser.class);
					returnCompetitionDiscuss.setCreaterName(centerUsers.getRealName());//评论发布者姓名
					returnCompetitionDiscuss.setCreaterNickname(centerUsers.getNickName());//评论发布者昵称
				}
			}
			CenterUser getcenterUser = new CenterUser();
			getcenterUser.setUserId(returnCompetitionDiscuss.getCreateUserId());
			CenterUser getcenterUserAll = centerUserService.selectCenterUser(getcenterUser);
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(getcenterUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				returnCompetitionDiscuss.setCreaterHeadLink(Common.checkPic(imgUrl)== true? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			returnCompetitionDiscuss.setCreaterName(getcenterUserAll.getRealName());//评论发布者姓名
			returnCompetitionDiscuss.setCreaterNickname(getcenterUserAll.getNickName());//评论发布者昵称
			returnCompetitionDiscuss.setCommentId(String.valueOf(returnCompetitionDiscuss.getDiscussId()));//群组评论信息id
			returnCompetitionDiscuss.setCreateTime(returnCompetitionDiscuss.getCreateTime());//群组评论发布时间的时间戳
		}
		LogUtil.info(this.getClass(), "getComments", "成功");
		return appQueryPage.getMessageJSONObject("comments");
	}
}
