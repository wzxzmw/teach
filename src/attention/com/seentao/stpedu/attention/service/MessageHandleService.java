package com.seentao.stpedu.attention.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.chainsaw.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterLiveDao;
import com.seentao.stpedu.common.dao.TeachClassDao;
import com.seentao.stpedu.common.entity.ArenaCompetitionDiscuss;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.common.entity.CenterAttitude;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterPhoto;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubMemberMood;
import com.seentao.stpedu.common.entity.ClubNotice;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTopicComment;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.entity.TeachQuestionAnswer;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.common.service.ArenaCompetitionDiscussService;
import com.seentao.stpedu.common.service.BaseTeachCourseService;
import com.seentao.stpedu.common.service.BaseTeachStudentHomeworkService;
import com.seentao.stpedu.common.service.CenterAlbumService;
import com.seentao.stpedu.common.service.CenterAttitudeService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterNewsService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterPhotoService;
import com.seentao.stpedu.common.service.CenterPrivateMessageService;
import com.seentao.stpedu.common.service.CenterPrivateMessageTalkService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMemberMoodService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubNoticeService;
import com.seentao.stpedu.common.service.ClubTopicCommentService;
import com.seentao.stpedu.common.service.ClubTopicService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.ClubTrainingCourseService;
import com.seentao.stpedu.common.service.ClubTrainingDiscussService;
import com.seentao.stpedu.common.service.ClubTrainingQuestionAnswerService;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseDiscussService;
import com.seentao.stpedu.common.service.TeachPlanService;
import com.seentao.stpedu.common.service.TeachPlanSignService;
import com.seentao.stpedu.common.service.TeachQuestionAnswerService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

import redis.clients.jedis.Jedis;

/**
 * 对信息主体操作
 * <pre>项目名称：stpedu    
 * 类名称：MessageHandleService    
 */
@Service
public class MessageHandleService {
	
	@Autowired
	private TeachQuestionService teachQuestionService;//问题表
	
	@Autowired
	private CenterAttitudeService centerAttitudeService;//态度表
	
	@Autowired
	private ClubTrainingQuestionService  clubTrainingQuestionService;//培训班问题表
	
	@Autowired
	private TeachQuestionAnswerService teachQuestionAnswerService;//培训班回答表
	
	@Autowired
	private  ClubTrainingQuestionAnswerService clubTrainingQuestionAnswerService;//培训班问题回答表
	
	@Autowired
	private TeachPlanService teachPlanService;//计划表
	
	@Autowired
	private TeachPlanSignService teachPlanSignService;//计划签到表
	
	@Autowired
	private TeachCourseDiscussService teachCourseDiscussService;//课程讨论表
	
	@Autowired
	private ArenaCompetitionDiscussService  arenaCompetitionDiscussService;//赛事讨论表
	
	@Autowired
	private ClubTrainingDiscussService clubTrainingDiscussService;//培训班讨论表
	
	@Autowired
	private ClubNoticeService  clubNoticeService;//俱乐部通知表
	
	@Autowired
	private CenterUserService centerUserService;//用户表
	
	@Autowired
	private ClubMemberService clubMemberService;//俱乐部会员表
	
	@Autowired
	private ClubTopicService clubTopicService;//俱乐部话题表
	
	@Autowired
	private ClubMemberMoodService clubMemberMoodService;//俱乐部会员心情表
	
	@Autowired
	private CenterCompanyService centerCompanyService;//企业表
	
	@Autowired
	private CenterPhotoService centerPhotoService;//照片表
	
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;
	
	@Autowired
	private TeachClassDao teachClassDao;//班级表
	
	@Autowired
	private ClubTrainingCourseService clubTrainingCourseService;
	
	@Autowired
	private BaseTeachCourseService teachCourseService;
	
	@Autowired
	private BaseTeachStudentHomeworkService teachStudentHomeworkService;
	
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	
	@Autowired
	private CenterAlbumService centerAlbumService;
	
	@Autowired
	private ClubTopicCommentService clubTopicCommentService;
	
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;
	
	@Autowired
	private CenterNewsService centerNewsService;
	
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	
	@Autowired
	private CenterLiveService centerLiveService;//动态
	
	@Autowired
	private CenterLiveDao centerLiveDao;//动态
	/**
	 * submitAttitude(对内容主体进行点赞、加精等态度操作) 
	 * @param userId 用户ID
	 * @param attiMainType 态度主体类型 1:答疑的问题；2:答疑的回答；3:计划任务；4:讨论(群组)；5:通知；6:话题；7:心情；8:企业；9:人员；10:相册图片；11:话题的评论
	 * @param attiMainId 态度主体id
	 * @param platformModule 平台模块 1:教学；2:竞技场；3:俱乐部；4:个人中心；当态度主体类型为1:答疑的问题；2:答疑的回答；4:讨论(群组)的时候传递该参数
	 * @param actionType 操作类型  1:加精；2:点赞；3:签到；4:置顶；5:取消置顶；6:点踩；
	 * @author ligs
	 * @date 2016年6月26日 下午10:23:35
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public JSONObject submitAttitude(Integer userId, Integer attiMainType, String attiMainId, Integer platformModule,Integer actionType)  {
		//判断态度主体类型
		switch (attiMainType) {
		case GameConstants.MANNER_ANSWER_ISSUE:
			// 1:答疑的问题
			return this.submitAttitudeOne(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_QUESTIONS_ANSWER:
			// 2:答疑的回答 点赞
			return this.submitAttitudeTwo(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_SCHEDULED_TASK:
			// 3:计划任务
			return this.submitAttitudeThree(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_DISCUSSION_GROUP:
			// 4:讨论(群组)
			return this.submitAttitudeFour(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_INFORM:
			// 5:通知 置顶  取消置顶
			return this.submitAttitudeFive(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_GAMBIT:
			// 6:话题
			return this.submitAttitudeSix(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_MOOD:
			// 7:心情
			return this.submitAttitudeSeven(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_ENTERPRISE:
			// 8:企业   企业表
			return this.submitAttitudeEight(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_USER:
			// 9:人员
			return this.submitAttitudeNine(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_PHOTO_ALBUM:
			// 10:相册图片
			return this.submitAttitudeTen(platformModule, actionType, attiMainId, userId);
		case GameConstants.MANNER_TOPIC:
			// 11:话题的评论
			return this.submitAttitudEeleven(platformModule, actionType, attiMainId, userId);
		}
		LogUtil.error(this.getClass(), "submitAttitude", "态度主体类型参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PARAMETER_ERROR);
	}
	
	
	
	/**
	 * submitDelete(对信息主体的删除操作)
	 * @param userId 用户ID
	 * @param actionObjectType 操作对象类型 1:答疑的问题；2:答疑的回答；3:计划任务；4:讨论(群组)；5:班级；6:通知(俱乐部的)；7:话题(俱乐部的)；8:课程；9:学生提交的作业；10:相册；11:相册图片；12:私信   
	 * @param platformModule 平台模块 1:教学；2:竞技场；3:俱乐部；4:个人中心；当操作对象类型为1:答疑的问题；2:答疑的回答；4:讨论(群组)；8:课程的时候传递该参数
	 * @param actionObjectId 操作对象id 多个id以逗号分隔 
	 * @author ligs 
	 * @date 2016年7月4日 上午9:22:16
	 * @return
	 */
	@Transactional
	public JSONObject submitDelete(Integer userId, Integer actionObjectType, Integer platformModule,String actionObjectId) {
		switch (actionObjectType) {
		case GameConstants.DEL_ANSWER_QUESTIONS_ISSUE://答疑的问题
			if(platformModule == GameConstants.MANNER_TEACHING){//教学课程答疑问题
				//判断用户是否为教师
				TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
				teachRelTeacherClass.setTeacherId(userId);
				TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
				//根据信息ID查询问题表判断信息主体是否存在  是否为该信息主体的创建人
					TeachQuestion teachQuestion = new TeachQuestion();
					teachQuestion.setQuestionId(Integer.valueOf(actionObjectId));
					teachQuestion.setIsDelete(GameConstants.NO_DEL);
					TeachQuestion isTeachQuestion =teachQuestionService.getTeachQuestion(teachQuestion);
					if(isTeachQuestion == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}else {
						//判断用户是教师还是学生
						CenterUser centerUser = new CenterUser();
						centerUser.setUserId(userId);
						CenterUser iscenterUser =  centerUserService.selectCenterUser(centerUser);
						if(iscenterUser.getUserType() == GameConstants.USER_TYPE_TEAHCER){//教师
							if(isteachRelTeacherClass  == null){
								LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
							}
						}else if(iscenterUser.getUserType() ==  GameConstants.USER_TYPE_STUDENT){//学生
							if( !isTeachQuestion.getCreateUserId().equals(userId)){
								LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
							}
						}else {
							LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
						}
				}
				//通过问题ID逻辑删除问题表对应内容 
				List<TeachQuestion> delQuestionList = new ArrayList<TeachQuestion>();
				TeachQuestion teachQuestions = new TeachQuestion();
				teachQuestions.setQuestionId(Integer.valueOf(actionObjectId));
				teachQuestions.setIsDelete(GameConstants.YES_DEL);
				delQuestionList.add(teachQuestions);
				//删除该问题所有的邀请消息  【消息表】
				CenterNews centerNews = new CenterNews();
				centerNews.setNewsType(2);
				centerNews.setRelObjectType(2);
				centerNews.setRelObjectId(Integer.valueOf(actionObjectId));
				centerNewsService.deleteCenterNews(centerNews);
				//删除redis
				JedisCache.delRedisVal(TeachQuestion.class.getSimpleName(),actionObjectId);
				teachQuestionService.delTeachQuestionAll(delQuestionList);
				
				LogUtil.info(this.getClass(), "submitDelete", "删除课程答疑问题成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(platformModule == GameConstants.MANNER_CLUB){//培训班答疑问题
				//查询培训班表判断是否为培训班的创建者
				ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
				clubTrainingClass.setCreateUserId(userId);
				ClubTrainingClass isclubTrainingClass =clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
				//根据信息ID查询问题表判断信息主体是否存在  是否为该信息主体的创建人
					String[] messageId = actionObjectId.split(",");
					for (int i = 0;i< messageId.length ;i++) {
						ClubTrainingQuestion clubTrainingQuestion = new ClubTrainingQuestion();
						clubTrainingQuestion.setQuestionId(Integer.valueOf(messageId[i]));
						clubTrainingQuestion.setIsDelete(GameConstants.NO_DEL);
						ClubTrainingQuestion isclubTrainingQuestion =clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
						if(isclubTrainingQuestion == null){
							LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
						}else {
							if(isclubTrainingClass != null){
								//通过问题ID逻辑删除问题表对应内容 
								List<ClubTrainingQuestion> delClubTrainingQuestion = new ArrayList<ClubTrainingQuestion>();
									ClubTrainingQuestion clubTrainingQuestions = new ClubTrainingQuestion();
									clubTrainingQuestions.setQuestionId(Integer.valueOf(actionObjectId));
									clubTrainingQuestions.setIsDelete(GameConstants.YES_DEL);
									delClubTrainingQuestion.add(clubTrainingQuestions);
									//删除该问题所有的邀请消息  【消息表】
									CenterNews centerNews = new CenterNews();
									centerNews.setNewsType(2);
									centerNews.setRelObjectType(3);
									centerNews.setRelObjectId(Integer.valueOf(actionObjectId));
									centerNewsService.deleteCenterNews(centerNews);
									//删除redis
									JedisCache.delRedisVal(ClubTrainingQuestion.class.getSimpleName(),actionObjectId);
								clubTrainingQuestionService.delClubTrainingQuestionAll(delClubTrainingQuestion);
								
								LogUtil.info(this.getClass(), "submitDelete", "删除培训班答疑问题成功");
								return Common.getReturn(AppErrorCode.SUCCESS, "");
							}else {
								LogUtil.error(this.getClass(), "submitDelete", "不是培训班的创建人");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_CLUBCLASS_SIGN_IN);
							}
						}
					}
			}
			break;
		case GameConstants.DEL_ANSWER_QUESTIONS_ANSWER://答疑的回答
			if(platformModule == GameConstants.MANNER_TEACHING){//课程答疑回答
				//判断用户是否为教师
				TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
				teachRelTeacherClass.setTeacherId(userId);
				TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
				if(isteachRelTeacherClass == null){}
				//根据信息ID查询问题表判断信息主体是否存在  是否为该信息主体的创建人
				String[] messageId = actionObjectId.split(",");
				TeachQuestionAnswer isTeachQuestionAnswer = null;
				for (int i = 0;i< messageId.length ;i++) {
					TeachQuestionAnswer teachQuestionAnswer = new TeachQuestionAnswer();
					teachQuestionAnswer.setAnswerId(Integer.valueOf(messageId[i]));
					teachQuestionAnswer.setIsDelete(GameConstants.NO_DEL);
					 isTeachQuestionAnswer =teachQuestionAnswerService.getTeachQuestionAll(teachQuestionAnswer);
					if(isTeachQuestionAnswer == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}else {
						//判断用户是教师还是学生
						CenterUser centerUser = new CenterUser();
						centerUser.setUserId(userId);
						CenterUser iscenterUser =  centerUserService.selectCenterUser(centerUser);
						if(iscenterUser.getUserType() == GameConstants.USER_TYPE_TEAHCER){//教师
							if(isteachRelTeacherClass  == null){
								LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
							}
						}else if(iscenterUser.getUserType() ==GameConstants.USER_TYPE_STUDENT){//学生
							if( !isTeachQuestionAnswer.getCreateUserId().equals(userId)){
								LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
							}
						}else {
							LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人或教师");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE_TEACHER);
						}
					}
				}
				//通过问题ID逻辑删除问题表对应内容 
				List<TeachQuestionAnswer> delTeachQuestionAnswer = new ArrayList<TeachQuestionAnswer>();
				String[] messageIds = actionObjectId.split(",");
				for (int i = 0;i< messageIds.length ;i++) {
					TeachQuestionAnswer teachQuestionAnswer = new TeachQuestionAnswer();
					teachQuestionAnswer.setAnswerId(Integer.valueOf(messageIds[i]));
					teachQuestionAnswer.setIsDelete(GameConstants.YES_DEL);
					delTeachQuestionAnswer.add(teachQuestionAnswer);
					//删除redis
					JedisCache.delRedisVal(TeachQuestionAnswer.class.getSimpleName(),messageIds[i]);
				}
				teachQuestionAnswerService.delTeachQuestionAnswerAll(delTeachQuestionAnswer);
				//减去课程答疑问题回答的数量
				TeachQuestion teachQuestion = new TeachQuestion();
				teachQuestion.setQuestionId(isTeachQuestionAnswer.getQuestionId());
				TeachQuestion isteachQuestion =teachQuestionService.getTeachQuestion(teachQuestion);
				if(isteachQuestion != null){
					if(isteachQuestion.getAnswerNum() != null){
						TeachQuestion teachQuestions = new TeachQuestion();
						teachQuestions.setQuestionId(isteachQuestion.getQuestionId());
						teachQuestions.setAnswerNum(isteachQuestion.getAnswerNum() - 1 );
						teachQuestionService.updateTeachQuestionByKey(teachQuestions);
					}
				}
				JedisCache.delRedisVal(TeachQuestion.class.getSimpleName(), isTeachQuestionAnswer.getQuestionId().toString());
				LogUtil.info(this.getClass(), "submitDelete", "删除课程答疑回答成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(platformModule == GameConstants.MANNER_CLUB){//培训班的答疑回答
				//判断是否为培训班的教练
				ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
				clubTrainingClass.setCreateUserId(userId);
				ClubTrainingClass isclubTrainingClass =clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
				//根据信息ID查询问题表判断信息主体是否存在  是否为该信息主体的创建人
				ClubTrainingQuestionAnswer isClubTrainingQuestionAnswer = null;
				ClubTrainingQuestionAnswer clubTrainingQuestionAnswer = new ClubTrainingQuestionAnswer();
				clubTrainingQuestionAnswer.setAnswerId(Integer.valueOf(actionObjectId));
				clubTrainingQuestionAnswer.setIsDelete(GameConstants.NO_DEL);
				 isClubTrainingQuestionAnswer =clubTrainingQuestionAnswerService.getClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
				if(isClubTrainingQuestionAnswer == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}else {
					if(isclubTrainingClass != null){
						//通过问题ID逻辑删除问题表对应内容 
						List<ClubTrainingQuestionAnswer> delClubTrainingQuestionAnswer = new ArrayList<ClubTrainingQuestionAnswer>();
						String[] messageIds = actionObjectId.split(",");
						for (int q = 0;q< messageIds.length ;q++) {
							ClubTrainingQuestionAnswer clubTrainingQuestionAnswers = new ClubTrainingQuestionAnswer();
							clubTrainingQuestionAnswers.setAnswerId(Integer.valueOf(messageIds[q]));
							clubTrainingQuestionAnswers.setIsDelete(GameConstants.YES_DEL);
							delClubTrainingQuestionAnswer.add(clubTrainingQuestionAnswers);
							//删除redis
							JedisCache.delRedisVal(ClubTrainingQuestionAnswer.class.getSimpleName(),messageIds[q]);
						}
						clubTrainingQuestionAnswerService.delClubTrainingQuestionAnswerAll(delClubTrainingQuestionAnswer);
						//减去培训班答疑回答的数量
						ClubTrainingQuestion clubTrainingQuestion = new ClubTrainingQuestion();
						clubTrainingQuestion.setQuestionId(isClubTrainingQuestionAnswer.getQuestionId());
						clubTrainingQuestion.setIsDelete(GameConstants.NO_DEL);
						ClubTrainingQuestion isclubTrainingQuestion = clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
						if(isclubTrainingQuestion != null){
							ClubTrainingQuestion clubTrainingQuestions = new ClubTrainingQuestion();
							clubTrainingQuestions.setQuestionId(isclubTrainingQuestion.getQuestionId());
							clubTrainingQuestions.setAnswerNum(isclubTrainingQuestion.getAnswerNum() -1);
							clubTrainingQuestionService.updateClubTrainingQuestionByKey(clubTrainingQuestions);
						}
						JedisCache.delRedisVal(ClubTrainingQuestion.class.getSimpleName(), isClubTrainingQuestionAnswer.getQuestionId().toString());
						LogUtil.info(this.getClass(), "submitDelete", "删除培训班的答疑回答成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}else {
						LogUtil.error(this.getClass(), "submitDelete", "不是培训班的创建人");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_CLUBCLASS_SIGN_IN);
					}
				}
			}
			break;
		case GameConstants.DEL_PLAN_TASK://计划任务
			//通过计划ID以及用户ID查询计划表判断是否为计划的创建人 
				TeachPlan teachPlan = new TeachPlan();
				teachPlan.setPlanId(Integer.valueOf(actionObjectId));
				teachPlan.setIsDelete(GameConstants.NO_DEL);
				TeachPlan isTeachPlan =teachPlanService.selectTeachPlan(teachPlan);
				if(isTeachPlan == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}
				//通过班级ID和教师ID查询教师班级关系表判断是否为教师
				TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
				teachRelTeacherClass.setClassId(isTeachPlan.getClassId());
				teachRelTeacherClass.setTeacherId(userId);
				TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
				if(isteachRelTeacherClass == null){
					LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
				}
				//判断是否已经有人签到  有人签到则不能删除计划任务
				if(isTeachPlan.getActualSignNum() != null){
					if(isTeachPlan.getActualSignNum() > 0 ){
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_MESSAGE_SIGN_IN);
					}
				} 
			//通过问题ID逻辑删除对应内容 
			List<TeachPlan> delTeachPlan = new ArrayList<TeachPlan>();
			TeachPlan teachPlans = new TeachPlan();
			teachPlans.setPlanId(Integer.valueOf(actionObjectId));
			teachPlans.setIsDelete(GameConstants.YES_DEL);
			delTeachPlan.add(teachPlans);
			//删除redis
			JedisCache.delRedisVal(TeachPlan.class.getSimpleName(),actionObjectId);
			teachPlanService.delTeachPlanAll(delTeachPlan);
			LogUtil.info(this.getClass(), "submitDelete", "删除计划任务成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		case GameConstants.DEL_DISCUSSION_GROUP://讨论群组
			if(platformModule == GameConstants.MANNER_TEACHING){//教学课程讨论
				//根据信息ID查询课程讨论表判断讨论主体是否存在  是否为该讨论主体的创建人
				String[] mesageId = actionObjectId.split(",");
				for (int i = 0;i< mesageId.length ;i++) {
					TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
					teachCourseDiscuss.setDiscussId(Integer.valueOf(mesageId[i]));
					teachCourseDiscuss.setIsDelete(GameConstants.NO_DEL);
					TeachCourseDiscuss isTeachCourseDiscuss =teachCourseDiscussService.getTeachCourseDiscuss(teachCourseDiscuss);
					if(isTeachCourseDiscuss == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}
				}
				//讨论表数据进行逻辑删除 
				//通过问题ID逻辑删除对应内容 
				List<TeachCourseDiscuss> delTeachCourseDiscuss = new ArrayList<TeachCourseDiscuss>();
				for (int i = 0;i< mesageId.length ;i++) {
					TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
					teachCourseDiscuss.setDiscussId(Integer.valueOf(mesageId[i]));
					teachCourseDiscuss.setIsDelete(GameConstants.YES_DEL);
					delTeachCourseDiscuss.add(teachCourseDiscuss);
					//删除redis
					JedisCache.delRedisVal(TeachCourseDiscuss.class.getSimpleName(),mesageId[i]);
				}
				teachCourseDiscussService.delTeachCourseDiscussAll(delTeachCourseDiscuss);
				
				LogUtil.info(this.getClass(), "submitDelete", "删除教学课程讨论成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(platformModule == GameConstants.MANNER_ARENA){//比赛讨论
				//根据信息ID查询课程讨论表判断讨论主体是否存在  是否为该讨论主体的创建人
				String[] mesageId = actionObjectId.split(",");
				for (int i = 0;i< mesageId.length ;i++) {
					ArenaCompetitionDiscuss arenaCompetitionDiscuss = new ArenaCompetitionDiscuss();
					arenaCompetitionDiscuss.setDiscussId(Integer.valueOf(mesageId[i]));
					arenaCompetitionDiscuss.setIsDelete(GameConstants.NO_DEL);
					ArenaCompetitionDiscuss isArenaCompetitionDiscuss =arenaCompetitionDiscussService.getArenaCompetitionDiscuss(arenaCompetitionDiscuss);
					if(isArenaCompetitionDiscuss == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}else if(!isArenaCompetitionDiscuss.getCreateUserId().equals(userId)){
						LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
					}
				}
				//讨论表数据进行逻辑删除 
				//通过问题ID逻辑删除对应内容 
				List<ArenaCompetitionDiscuss> delArenaCompetitionDiscuss = new ArrayList<ArenaCompetitionDiscuss>();
				for (int i = 0;i< mesageId.length ;i++) {
					ArenaCompetitionDiscuss arenaCompetitionDiscuss = new ArenaCompetitionDiscuss();
					arenaCompetitionDiscuss.setDiscussId(Integer.valueOf(mesageId[i]));
					arenaCompetitionDiscuss.setIsDelete(GameConstants.YES_DEL);
					delArenaCompetitionDiscuss.add(arenaCompetitionDiscuss);
					//删除redis
					JedisCache.delRedisVal(ArenaCompetitionDiscuss.class.getSimpleName(),mesageId[i]);
				}
				arenaCompetitionDiscussService.delArenaCompetitionDiscussAll(delArenaCompetitionDiscuss);
				
				LogUtil.info(this.getClass(), "submitDelete", "删除比赛讨论成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(platformModule == GameConstants.MANNER_CLUB){//培训班讨论
				//根据信息ID查询培训班讨论表判断讨论主体是否存在  是否为该讨论主体的创建人
				String[] mesageId = actionObjectId.split(",");
				for (int i = 0;i< mesageId.length ;i++) {
					ClubTrainingDiscuss clubTrainingDiscuss = new ClubTrainingDiscuss();
					clubTrainingDiscuss.setDiscussId(Integer.valueOf(mesageId[i]));
					clubTrainingDiscuss.setIsDelete(GameConstants.NO_DEL);
					ClubTrainingDiscuss isClubTrainingDiscuss =clubTrainingDiscussService.getClubTrainingDiscuss(clubTrainingDiscuss);
					if(isClubTrainingDiscuss == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}else{
						//判断是否为教练
						ClubMember clubMember = new ClubMember();
						clubMember.setUserId(userId);
						ClubMember isClubMember =clubMemberService.getClubMemberOne(clubMember);
						if(isClubMember.getLevel() == 3){
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, "没有删除权限");
						}
						//讨论表数据进行逻辑删除 
						//通过问题ID逻辑删除对应内容 
						List<ClubTrainingDiscuss> delClubTrainingDiscuss = new ArrayList<ClubTrainingDiscuss>();
						for (int a = 0;a< mesageId.length ;a++) {
							ClubTrainingDiscuss aclubTrainingDiscuss = new ClubTrainingDiscuss();
							aclubTrainingDiscuss.setDiscussId(Integer.valueOf(mesageId[a]));
							aclubTrainingDiscuss.setIsDelete(GameConstants.YES_DEL);
							delClubTrainingDiscuss.add(aclubTrainingDiscuss);
							//删除redis
							JedisCache.delRedisVal(ArenaCompetitionDiscuss.class.getSimpleName(),mesageId[a]);
						}
						clubTrainingDiscussService.delClubTrainingDiscussAll(delClubTrainingDiscuss);
						
						LogUtil.info(this.getClass(), "submitDelete", "删除培训班讨论成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}
				}
			}
			break;
		case GameConstants.DEL_CLASS://班级
			if(platformModule == GameConstants.MANNER_TEACHING){//教学班
				//通过班级信息主体id（班级ID）查询班级是否存在  班级是否有成员
				TeachClass teachClass = new TeachClass();
				teachClass.setClassId(Integer.valueOf(actionObjectId));
				TeachClass isTeachClass =teachClassDao.selectTeachClass(teachClass);
				if(isTeachClass == null){
					LogUtil.error(this.getClass(), "submitDelete", "班级不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_CLASS_NOT);
				}else if(isTeachClass.getStudentsNum() > 0){
					LogUtil.error(this.getClass(), "submitDelete", "班级中存在学员不可删除");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_CLASS_USER);
				}
				//判断是否为班级的教师
				TeachRelTeacherClass teachRelTeacherClasses = new TeachRelTeacherClass();
				teachRelTeacherClasses.setClassId(Integer.valueOf(actionObjectId));
				teachRelTeacherClasses.setTeacherId(userId);
				TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClasses);
				if(isTeachRelTeacherClass == null){
					LogUtil.error(this.getClass(), "submitDelete", "不是该班级的教师");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.USER_NOT_CLASS_TEACH);
				}else {
					//通过班级ID逻辑删除班级
					TeachClass delTeachClass = new TeachClass();
					delTeachClass.setClassId(Integer.valueOf(actionObjectId));
					delTeachClass.setIsDelete(GameConstants.YES_DEL);
					try {
						teachClassDao.updateTeachClassByKey(delTeachClass);
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "submitDelete", "删除班级失败");
					}
					//删除教师班级关系表
					TeachRelTeacherClass delteachRelTeacherClass = new TeachRelTeacherClass();
					delteachRelTeacherClass.setClassId(Integer.valueOf(actionObjectId));
					delteachRelTeacherClass.setTeacherId(userId);
					teachRelTeacherClassService.deleteTeachRelTeacherClass(delteachRelTeacherClass);
					LogUtil.info(this.getClass(), "submitDelete", "删除教学班成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				}
			}//删除培训班本期不做
			
			break;
		case GameConstants.DEL_INFORM://俱乐部通知
			//通过通知信息主体ID查询俱乐部通知表判断通知信息主体是否存在
				ClubNotice clubNotice = new ClubNotice();
				clubNotice.setNoticeId(Integer.valueOf(actionObjectId));
				clubNotice.setIsDelete(GameConstants.NO_DEL);
				ClubNotice isClubNotice =clubNoticeService.getClubNotice(clubNotice);
				if(isClubNotice == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}
				
				//通过俱乐部ID和用户ID查询俱乐部会员表判断是否为俱乐部的会长
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(isClubNotice.getClubId());
				clubMember.setUserId(userId);
				ClubMember isClubMember =clubMemberService.getClubMemberOne(clubMember);
				if(isClubMember.getLevel() == GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT || isClubNotice.getCreateUserId().equals(userId)){
					List<ClubNotice> delClubNotice = new ArrayList<ClubNotice>();
					ClubNotice delclubNotice = new ClubNotice();
					delclubNotice.setNoticeId(Integer.valueOf(actionObjectId));
					delclubNotice.setIsDelete(GameConstants.YES_DEL);
					delClubNotice.add(delclubNotice);
					//删除redis
					JedisCache.delRedisVal(ClubNotice.class.getSimpleName(),actionObjectId);
					clubNoticeService.delClubNoticeAll(delClubNotice);
					
					LogUtil.info(this.getClass(), "submitDelete", "删除俱乐部通知成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				}else {
					LogUtil.error(this.getClass(), "submitDelete", "没有删除权限");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_DEL_JURISDICTION);
				}
		case GameConstants.DEL_TOPIC://话题
			//通过话题信息主体ID查询俱乐部通知表判断话题信息主体是否存在  
				ClubTopic clubTopic = new ClubTopic();
				clubTopic.setTopicId(Integer.valueOf(actionObjectId));
				clubTopic.setIsDelete(GameConstants.NO_DEL);
				ClubTopic isClubTopic =clubTopicService.selectSingleClubTopic(clubTopic);
				if(isClubTopic == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}
				//判断删除话题的人是否为会长    只有会长可以删除
				ClubMember clubMembers = new ClubMember();
				clubMembers.setClubId(isClubTopic.getClubId());
				clubMembers.setUserId(userId);
				ClubMember isClubMembers =clubMemberService.getClubMemberOne(clubMembers);
				if(isClubMembers.getLevel()  != GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT){
					LogUtil.error(this.getClass(), "submitDelete", "没有删除权限");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_DEL_JURISDICTION);
				}
			//逻辑删除话题
			List<ClubTopic> delClubTopic = new ArrayList<ClubTopic>();
			ClubTopic clubTopics = new ClubTopic();
			clubTopics.setTopicId(Integer.valueOf(actionObjectId));
			clubTopics.setIsDelete(GameConstants.YES_DEL);
			delClubTopic.add(clubTopics);
			//删除redis
			JedisCache.delRedisVal(ClubTopic.class.getSimpleName(),actionObjectId);
			clubTopicService.delClubTopicAll(delClubTopic);
			
			LogUtil.info(this.getClass(), "submitDelete", "删除话题成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		case GameConstants.DEL_COURSE://课程
			if(platformModule == GameConstants.MANNER_TEACHING){//教学班课程
				//通过课程id查询课程是否存在   是否是自定义课程
				TeachCourse teachCourse = new TeachCourse();
				teachCourse.setCourseId(Integer.valueOf(actionObjectId));
				teachCourse.setIsDelete(GameConstants.NO_DEL);
				TeachCourse isteachCourse = teachCourseService.getTeachCourse(teachCourse);
				if(isteachCourse == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}else if(!isteachCourse.getTeacherId().equals(userId)){
					LogUtil.error(this.getClass(), "submitDelete", "没有删除权限");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_DEL_JURISDICTION);
				}else if(isteachCourse.getCourseType() == 1){
					LogUtil.error(this.getClass(), "submitDelete", "官方课程不能删除");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_OFFICIAL_ERROR);
				}
				//逻辑删除自定义课程
				List<TeachCourse> delTeachCourse = new ArrayList<TeachCourse>();
				TeachCourse teachCourses = new TeachCourse();
				teachCourses.setCourseId(Integer.valueOf(actionObjectId));
				teachCourses.setIsDelete(GameConstants.YES_DEL);
				delTeachCourse.add(teachCourses);
				//删除redis
				JedisCache.delRedisVal(TeachCourse.class.getSimpleName(),actionObjectId);
				teachCourseService.delTeachCourseAll(delTeachCourse);
				
				LogUtil.info(this.getClass(), "submitDelete", "删除教学班课程成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(platformModule == GameConstants.MANNER_CLUB){//培训班课程
				//通过课程id查询课程是否存在   是否是自定义课程
				ClubTrainingCourse clubTrainingCourse = new ClubTrainingCourse();
				clubTrainingCourse.setCourseId(Integer.valueOf(actionObjectId));
				clubTrainingCourse.setIsDelete(GameConstants.NO_DEL);
				ClubTrainingCourse isClubTrainingCourse =clubTrainingCourseService.getClubTrainingCourse(clubTrainingCourse);
				if(isClubTrainingCourse == null){
					LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
				}else if(isClubTrainingCourse.getCreateUserId().equals(userId)){
					LogUtil.error(this.getClass(), "submitDelete", "没有删除权限");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_DEL_JURISDICTION);
				}else if(isClubTrainingCourse.getCourseType() == 1){
					LogUtil.error(this.getClass(), "submitDelete", "官方课程不能删除");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_OFFICIAL_ERROR);
				}
				//逻辑删除自定义课程
				List<ClubTrainingCourse> delClubTrainingCourse = new ArrayList<ClubTrainingCourse>();
				ClubTrainingCourse clubTrainingCourses = new ClubTrainingCourse();
				clubTrainingCourses.setCourseId(Integer.valueOf(actionObjectId));
				clubTrainingCourses.setIsDelete(GameConstants.YES_DEL);
				delClubTrainingCourse.add(clubTrainingCourses);
				//删除redis
				JedisCache.delRedisVal(ClubTrainingCourse.class.getSimpleName(),actionObjectId);
				clubTrainingCourseService.delClubTrainingCourseAll(delClubTrainingCourse);
				
				LogUtil.info(this.getClass(), "submitDelete", "删除培训班课程成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
			break;
		case GameConstants.DEL_SUTDENT_JOB://学生提交的作业
			//根据作业id查询【学生作业表】判断作业是否存在    是否为自己的作业
			TeachStudentHomework teachStudentHomework = new TeachStudentHomework();
			teachStudentHomework.setHomeworkId(Integer.valueOf(actionObjectId));
			teachStudentHomework.setIsDelete(GameConstants.NO_DEL);
			TeachStudentHomework isTeachStudentHomework = teachStudentHomeworkService.getTeachStudentHomework(teachStudentHomework);
			if(isTeachStudentHomework == null){
				LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
			}else if(!isTeachStudentHomework.getStudentId().equals(userId)){
				LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
			}
			//时间戳精确到秒
			SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdff.format(new Date());
		    Date parse = null;
			try {
				parse = sdff.parse(date);
			} catch (ParseException e) {
			}
		    long dateTimes = (int)(parse.getTime()/1000);
			//判断提交的作业是否在课程卡的时间之内  通过课程ID查询课程卡课程关系表获得课程卡ID
			TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
			teachRelCardCourse.setCourseId(isTeachStudentHomework.getCourseId());
			TeachRelCardCourse isTeachRelCardCourse =teachRelCardCourseService.selectTeachRelCardCourse(teachRelCardCourse);
			//根据课程卡ID查询课程卡表
			TeachCourseCard teachCourseCard =teachCourseCardService.getTeachCourseCardOne(isTeachRelCardCourse.getCardId());
			if(dateTimes >= teachCourseCard.getStartTime() & dateTimes <= teachCourseCard.getEndTime()){
				//逻辑删除作业
				TeachStudentHomework delteachStudentHomework = new TeachStudentHomework();
				delteachStudentHomework.setHomeworkId(Integer.valueOf(actionObjectId));
				delteachStudentHomework.setIsDelete(GameConstants.YES_DEL);
				teachStudentHomeworkService.updateTeachStudentHomeworkByKey(delteachStudentHomework);
				//删除redis
				JedisCache.delRedisVal(TeachStudentHomework.class.getSimpleName(),actionObjectId);
				//课程关系表减去已交文本作业人数
				TeachRelCardCourse teachRelCardCourses = new TeachRelCardCourse();
				teachRelCardCourses.setRelId(isTeachRelCardCourse.getRelId());
				teachRelCardCourses.setActualNum(isTeachRelCardCourse.getActualNum() - 1);
				teachRelCardCourseService.updateTeachRelCardCourse(teachRelCardCourses);
				LogUtil.info(this.getClass(), "submitDelete", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else {
				LogUtil.error(this.getClass(), "submitDelete", "课程已结束不能删除作业");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.COURSE_ERROR_TIME);
			}
		/*case GameConstants.DEL_PHOTO://删除 相册 本期不做
			//查询相册是否存在   是否为创建者  以及相册里面是否有照片
			CenterAlbum centerAlbum = new CenterAlbum();
			centerAlbum.setAlbumId(Integer.valueOf(actionObjectId));
			centerAlbum.setIsDelete(GameConstants.NO_DEL);
			CenterAlbum isCenterAlbum =centerAlbumService.getCenterAlbum(centerAlbum);
			if(isCenterAlbum == null){
				LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
			}else if(isCenterAlbum.getCreateUserId() != userId){
				LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
			}else if(isCenterAlbum.getPhotoNum() > 0){
				LogUtil.error(this.getClass(), "submitDelete", "相册里有照片不能删除");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PHOTO_NOT_DEL);
			}else {
				//逻辑删除相册
				CenterAlbum delcenterAlbum = new CenterAlbum();
				delcenterAlbum.setAlbumId(Integer.valueOf(actionObjectId));
				delcenterAlbum.setIsDelete(GameConstants.YES_DEL);
				centerAlbumService.updateCenterAlbumByKeyAll(delcenterAlbum);
				//删除redis
				JedisCache.delRedisVal(CenterAlbum.class.getSimpleName(),actionObjectId);
				LogUtil.info(this.getClass(), "submitDelete", "删除相册成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}*/
		case GameConstants.DEL_PICTURE://相册图片
				//根据照片ID查询照片表判断照片是否存在    是否为创建者
				CenterPhoto isCenterPhoto = null;
					CenterPhoto centerPhoto = new CenterPhoto();
					centerPhoto.setPhotoId(Integer.valueOf(actionObjectId));
					centerPhoto.setIsDelete(GameConstants.NO_DEL);
					isCenterPhoto =centerPhotoService.getCenterPhoto(centerPhoto);
					if(isCenterPhoto == null){
						LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
					}else if( (int)userId != isCenterPhoto.getCreateUserId()){
						LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
					}else if(isCenterPhoto.getAlbumId() != null){
						//判断要删除的照片是否为封面图片   如果是封面图片删除相册表的封面图片ID
						CenterAlbum centerAlbum = new CenterAlbum();
						centerAlbum.setAlbumId(isCenterPhoto.getAlbumId());
						centerAlbum.setIsDelete(GameConstants.NO_DEL);
						CenterAlbum isCenterAlbum =centerAlbumService.getCenterAlbum(centerAlbum);
						if(isCenterAlbum != null && isCenterAlbum.getCoverPhotoId() != null){
							//if(isCenterAlbum.getCoverPhotoId().equals(Integer.valueOf(actionObjectId))){
								CenterAlbum centerAlbums = new CenterAlbum();
								centerAlbums.setAlbumId(isCenterAlbum.getAlbumId());
								centerAlbums.setCoverPhotoId(0);
								centerAlbumService.updateCoverPhotoId(centerAlbums);
							//}
						}
				}
				//逻辑删除照片
				List<CenterPhoto> delCenterPhoto = new ArrayList<CenterPhoto>();
				CenterPhoto centerPhotos = new CenterPhoto();
				centerPhotos.setPhotoId(Integer.valueOf(actionObjectId));
				centerPhotos.setIsDelete(GameConstants.YES_DEL);
				delCenterPhoto.add(centerPhotos);
				//删除redis
				JedisCache.delRedisVal(CenterPhoto.class.getSimpleName(),actionObjectId);
				centerPhotoService.delCenterPhotoAll(delCenterPhoto);
				//照片表数量减一
				CenterAlbum centerAlbum = new CenterAlbum();
				centerAlbum.setAlbumId(isCenterPhoto.getAlbumId());
				centerAlbum.setPhotoNum(1);
				centerAlbumService.updatePhotoNum(centerAlbum);
				//删除redis
				JedisCache.delRedisVal(CenterAlbum.class.getSimpleName(),isCenterPhoto.getAlbumId().toString());
				LogUtil.info(this.getClass(), "submitDelete", "删除相册图片成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
		/*case GameConstants.DEL_PRIVATE_LETTER://私信本期不做
			//根据私信ID判断私信是否存在   以及是否为创建人
			CenterPrivateMessage centerPrivateMessage = new CenterPrivateMessage();
			centerPrivateMessage.setPrivateMessageId(Integer.valueOf(actionObjectId));
			CenterPrivateMessage isCenterPrivateMessage =centerPrivateMessageService.getCenterPrivateMessage(centerPrivateMessage);
			if(isCenterPrivateMessage == null){
				LogUtil.error(this.getClass(), "submitDelete", "要删除的信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.DEL_MESSAGE_NOT);
			}else if(isCenterPrivateMessage.getSendUserId() != userId){
				LogUtil.error(this.getClass(), "submitDelete", "不是该信息主体的创建人");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_MESSAGE_CREATE);
			}
			//删除私信
			CenterPrivateMessage delcenterPrivateMessage = new CenterPrivateMessage();
			delcenterPrivateMessage.setPrivateMessageId(Integer.valueOf(actionObjectId));
			centerPrivateMessageService.deleteCenterPrivateMessage(delcenterPrivateMessage);
			//根据对话ID查询私信对话表 判断是否为最后一条私信  私信数量是否为0
			CenterPrivateMessageTalk centerPrivateMessageTalk = new CenterPrivateMessageTalk();
			centerPrivateMessageTalk.setTalkId(isCenterPrivateMessage.getTalkId());
			CenterPrivateMessageTalk isCenterPrivateMessageTalk =centerPrivateMessageTalkService.getCenterPrivateMessageTalk(centerPrivateMessageTalk);
			if(isCenterPrivateMessageTalk.getPrivateMessageNum() != 0){
				//私信对话表私信数量减一
				CenterPrivateMessageTalk upcenterPrivateMessageTalk = new CenterPrivateMessageTalk();
				upcenterPrivateMessageTalk.setTalkId(isCenterPrivateMessage.getTalkId());
				upcenterPrivateMessageTalk.setPrivateMessageNum(isCenterPrivateMessageTalk.getPrivateMessageNum() - 1);
				centerPrivateMessageTalkService.updateCenterPrivateMessageTalkByKey(upcenterPrivateMessageTalk);
			}
			if(isCenterPrivateMessageTalk.getPrivateMessageNum() == 0){
				//删除私信对话
				CenterPrivateMessageTalk delcenterPrivateMessageTalk = new CenterPrivateMessageTalk();
				delcenterPrivateMessageTalk.setTalkId(isCenterPrivateMessageTalk.getTalkId());
				centerPrivateMessageTalkService.deleteCenterPrivateMessageTalk(delcenterPrivateMessageTalk);
				LogUtil.info(this.getClass(), "submitDelete", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(isCenterPrivateMessageTalk.getLastPrivateMessageId() == isCenterPrivateMessage.getPrivateMessageId()){
				//通过对话ID查询最后一条私信
				CenterPrivateMessage selcenterPrivateMessage = new CenterPrivateMessage();
				selcenterPrivateMessage.setTalkId(isCenterPrivateMessage.getTalkId());
				CenterPrivateMessage isSelcenterPrivateMessage =centerPrivateMessageService.selectCenterPrivateMessageLast(selcenterPrivateMessage);
				//最后一条私信添加到对话表中
				CenterPrivateMessageTalk upCenterPrivateMessageTalk = new CenterPrivateMessageTalk();
				upCenterPrivateMessageTalk.setTalkId(isCenterPrivateMessage.getTalkId());
				upCenterPrivateMessageTalk.setLastPrivateMessageId(isSelcenterPrivateMessage.getPrivateMessageId());
				centerPrivateMessageTalkService.updateCenterPrivateMessageTalkByKey(upCenterPrivateMessageTalk);
			}
			LogUtil.info(this.getClass(), "submitDelete", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");*/
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
		
	}
	
	
	/**
	 *  答疑的问题
	 */
	public JSONObject  submitAttitudeOne(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		if(platformModule == GameConstants.MANNER_TEACHING){//教学 课程 
			//根据课程问题信息主体id查询【问题表】判断信息主体是否存在
			TeachQuestion teachQuestion = new TeachQuestion();//问题表
			teachQuestion.setQuestionId(Integer.valueOf(attiMainId));
			teachQuestion.setIsDelete(GameConstants.NO_DEL);
			TeachQuestion isTeachQuestion =teachQuestionService.getTeachQuestion(teachQuestion);
			if(isTeachQuestion == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			if(actionType == GameConstants.MANNER_ADD_CREAM){//1：加精
				if(isTeachQuestion.getIsEssence() == GameConstants.IS_CREAM){//判断是否已经加精
					LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过加精");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ALREADY_DIGEST );
				}
				//根据问题主体ID修改问题表为加精
				TeachQuestion teachQuestionAll = new TeachQuestion();//问题表
				teachQuestionAll.setQuestionId(isTeachQuestion.getQuestionId());
				teachQuestionAll.setIsEssence(GameConstants.IS_CREAM);
				teachQuestionService.updateTeachQuestionByKey(teachQuestionAll);
				JedisCache.delRedisVal(TeachQuestion.class.getSimpleName(),attiMainId);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(actionType == GameConstants.MANNER_THUMB_UP){//2:点赞
				//根据用户id和信息主体id以及关联对象类型查询【态度表】判读用户是否已经对问题信息主体进行过操作
				CenterAttitude centerAttitude = new CenterAttitude();//态度表
				centerAttitude.setCreateUserId(userId);
				centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				centerAttitude.setRelObjetType(GameConstants.COURSE_ISSUE);
				CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
				if(iscenterAttitude != null){
					LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
				}else{
					//时间时间戳精确到秒
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dates = sdf.format(new Date());
				    Date parses = null;
					try {
						parses = sdf.parse(dates);
					} catch (ParseException e) {
					}
				    int dateTime = (int)(parses.getTime()/1000);
					//添加一条数据到态度表
					CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
					addCenterAttitude.setCreateUserId(userId);
					addCenterAttitude.setCreateTime(dateTime);
					addCenterAttitude.setAttiType(GameConstants.PRAISE);
					addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
					addCenterAttitude.setRelObjetType(GameConstants.COURSE_ISSUE);
					centerAttitudeService.insertCenterAttitude(addCenterAttitude);
					//根据问题主体ID修改问题表点赞数量
					TeachQuestion teachQuestionAll = new TeachQuestion();//问题表
					teachQuestionAll.setQuestionId(Integer.valueOf(attiMainId));
					TeachQuestion teachQuestions = teachQuestionService.getTeachQuestion(teachQuestionAll);
					TeachQuestion updateteachQuestion = new TeachQuestion();//问题表
					updateteachQuestion.setQuestionId(Integer.valueOf(attiMainId));
					updateteachQuestion.setPraiseNum(teachQuestions.getPraiseNum()+1);
					teachQuestionService.updateTeachQuestionByKey(updateteachQuestion);
					JedisCache.delRedisVal(TeachQuestion.class.getSimpleName(),attiMainId.toString());
					
					Map<String , Object> support = new HashMap<String,Object>();
					support.put("supportCount", teachQuestions.getPraiseNum()+1);
					LogUtil.info(this.getClass(), "submitAttitude", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
				}
				
			}
		}else if(platformModule == GameConstants.MANNER_CLUB){//俱乐部  培训班问题
			//根据培训班问题主体ID查询【培训班问题表】判断培训班问题信息主体是否存在
			ClubTrainingQuestion clubTrainingQuestion = new ClubTrainingQuestion();
			clubTrainingQuestion.setQuestionId(Integer.valueOf(attiMainId));
			clubTrainingQuestion.setIsDelete(GameConstants.NO_DEL);
			ClubTrainingQuestion isclubTrainingQuestion =clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
			if(isclubTrainingQuestion == null ){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			if(actionType == GameConstants.MANNER_ADD_CREAM){//1：加精
				if(isclubTrainingQuestion.getIsEssence() == GameConstants.IS_CREAM){//判断是否已经加精
					LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过加精");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ALREADY_DIGEST );
				}else {
					//根据问题主体ID修改问题表为加精
					ClubTrainingQuestion upClubTrainingQuestion = new ClubTrainingQuestion();
					upClubTrainingQuestion.setQuestionId(Integer.valueOf(attiMainId));
					upClubTrainingQuestion.setIsEssence(GameConstants.IS_CREAM);
					clubTrainingQuestionService.updateClubTrainingQuestionByKey(upClubTrainingQuestion);
					JedisCache.delRedisVal(ClubTrainingQuestion.class.getSimpleName(),attiMainId);
					LogUtil.info(this.getClass(), "submitAttitude", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				}
			}else if(actionType == GameConstants.MANNER_THUMB_UP){//2:点赞
				//根据用户id和信息主体id以及关联对象类型查询【态度表】判读用户是否已经对问题信息主体进行过操作
				CenterAttitude centerAttitude = new CenterAttitude();//态度表
				centerAttitude.setCreateUserId(userId);
				centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				centerAttitude.setRelObjetType(GameConstants.TRAIN_ISSUE);
				CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
				if(iscenterAttitude != null){
					LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
				}
				//时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.TRAIN_ISSUE);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//根据问题主体ID修改培训班问题表点赞数量
				ClubTrainingQuestion upclubTrainingQuestion = new ClubTrainingQuestion();
				upclubTrainingQuestion.setQuestionId(Integer.valueOf(attiMainId));
				upclubTrainingQuestion.setPraiseNum(isclubTrainingQuestion.getPraiseNum()+1);
				clubTrainingQuestionService.updateClubTrainingQuestionByKey(upclubTrainingQuestion);
				JedisCache.delRedisVal(ClubTrainingQuestion.class.getSimpleName(),attiMainId);
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isclubTrainingQuestion.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	/**
	 * 答疑的回答
	 * @return
	 */
	public JSONObject submitAttitudeTwo(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		if(platformModule == GameConstants.MANNER_TEACHING){//教学
			//根据课程回答信息主体id查询【问题回答表】判断回答信息  主体是否存在
			TeachQuestionAnswer teachQuestionAnswer = new TeachQuestionAnswer();
			teachQuestionAnswer.setAnswerId(Integer.valueOf(attiMainId));
			teachQuestionAnswer.setIsDelete(GameConstants.NO_DEL);
			TeachQuestionAnswer isteachQuestionAnswer = teachQuestionAnswerService.getTeachQuestionAll(teachQuestionAnswer);
			if(isteachQuestionAnswer == null ){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对回答信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.COURSE_ANSWER);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.COURSE_ANSWER);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新回答表赞数量
				TeachQuestionAnswer updateQuestionAnswer = new TeachQuestionAnswer();
				updateQuestionAnswer.setAnswerId(Integer.valueOf(attiMainId));
				updateQuestionAnswer.setPraiseNum(isteachQuestionAnswer.getPraiseNum()+1);
				teachQuestionAnswerService.updateTeachQuestionAnswerByKey(updateQuestionAnswer);
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isteachQuestionAnswer.getPraiseNum()+1);
				JedisCache.delRedisVal(TeachQuestionAnswer.class.getSimpleName(),attiMainId);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "",support);
			}
		}else if(platformModule == GameConstants.MANNER_CLUB){//培训班问题回答表  点赞
			//根据培训班回答信息主体ID查询【培训班问题回答表】判断培训班回答信息主体是否存在
			ClubTrainingQuestionAnswer clubTrainingQuestionAnswer = new ClubTrainingQuestionAnswer();
			clubTrainingQuestionAnswer.setAnswerId(Integer.valueOf(attiMainId));
			clubTrainingQuestionAnswer.setIsDelete(GameConstants.NO_DEL);
			ClubTrainingQuestionAnswer isClubTrainingQuestionAnswer =clubTrainingQuestionAnswerService.getClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
			if(isClubTrainingQuestionAnswer == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对回答信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.TRAIN_ANSWER);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.TRAIN_ANSWER);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新培训班回答表赞数量
				ClubTrainingQuestionAnswer upClubTrainingQuestionAnswer = new ClubTrainingQuestionAnswer();
				upClubTrainingQuestionAnswer.setAnswerId(Integer.valueOf(attiMainId));
				upClubTrainingQuestionAnswer.setPraiseNum(isClubTrainingQuestionAnswer.getPraiseNum()+1);
				clubTrainingQuestionAnswerService.updateClubTrainingQuestionAnswerByKey(upClubTrainingQuestionAnswer);
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isClubTrainingQuestionAnswer.getPraiseNum()+1);
				//删除培训班回答表redis
				JedisCache.delRedisVal(ClubTrainingQuestionAnswer.class.getSimpleName(),attiMainId);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "",support);
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	/**
	 * 计划任务
	 * @return
	 */
	public JSONObject submitAttitudeThree(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据计划任务信息id查询【计划表】判断计划任务是否存在
		TeachPlan teachPlan = new TeachPlan();//计划表
		teachPlan.setPlanId(Integer.valueOf(attiMainId));
		teachPlan.setIsDelete(GameConstants.NO_DEL);
		TeachPlan isteachPlan = teachPlanService.selectTeachPlan(teachPlan);
		if(isteachPlan == null){
			LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
		}
		//判断是否已启用签到 
		if(isteachPlan.getIsSign() == 1){
			//判断是否已经签到
			TeachPlanSign iteachPlanSign = new TeachPlanSign();//计划签到表
			iteachPlanSign.setStudentId(userId);//学生ID
			iteachPlanSign.setPlanId(Integer.valueOf(attiMainId));//计划ID
			TeachPlanSign isteachPlanSign =teachPlanSignService.getTeachPlanSigns(iteachPlanSign);
			if(isteachPlanSign == null){
				TeachPlanSign iteachPlanSigns = new TeachPlanSign();//计划签到表
				iteachPlanSigns.setPlanId(Integer.valueOf(attiMainId));
				iteachPlanSigns.setStudentId(userId);
				iteachPlanSigns.setIsDelete(GameConstants.NO_DEL);
				teachPlanSignService.insertTeachPlanSign(iteachPlanSigns);
				
				TeachPlanSign iteachPlanSignc = new TeachPlanSign();//计划签到表
				iteachPlanSignc.setStudentId(userId);//学生ID
				iteachPlanSignc.setPlanId(Integer.valueOf(attiMainId));//计划ID
				TeachPlanSign isteachPlanSigns =teachPlanSignService.getTeachPlanSigns(iteachPlanSignc);
				if(isteachPlanSigns.getSignTime() == null){
					//获取系统时间与【计划表】的计划日期对比在同一天才可签到 
					//获取系统时间戳到天
					SimpleDateFormat sip = new SimpleDateFormat("yyyyMMdd");
				    String date = sip.format(new Date());
				    Date parse = null;
					try {
						parse = sip.parse(date);
					} catch (ParseException e) {
					}
				    int dateTime = (int)(parse.getTime()/1000);
					if(dateTime == isteachPlan.getPlanDate()){
						//签到时间时间戳精确到秒
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dates = sdf.format(new Date());
					    Date parses = null;
						try {
							parses = sdf.parse(dates);
						} catch (ParseException e) {
						}
					    int dateTimes = (int)(parses.getTime()/1000);
						
					    //查询计划签到表查看计划签到表是否有该学生信息
					    TeachPlanSign teachPlanSigns = new TeachPlanSign();//计划签到表
						teachPlanSigns.setStudentId(userId);//学生ID
						teachPlanSigns.setPlanId(Integer.valueOf(attiMainId));//计划ID
						TeachPlanSign isTeachPlanSigns =teachPlanSignService.getTeachPlanSigns(teachPlanSigns);
						if(isTeachPlanSigns == null){
							//根据用户ID更新【计划签到表】签到时间
							TeachPlanSign teachPlanSign = new TeachPlanSign();//计划签到表
							teachPlanSign.setStudentId(userId);//学生ID
							teachPlanSign.setPlanId(Integer.valueOf(attiMainId));//计划ID
							teachPlanSign.setSignTime(dateTimes);
							teachPlanSign.setIsDelete(GameConstants.NO_DEL);
							teachPlanSignService.insertTeachPlanSign(teachPlanSign);
							//增加【计划表】已签到人数 
							TeachPlan teachPlans = new TeachPlan();//计划表
							teachPlans.setPlanId(Integer.valueOf(attiMainId));
							teachPlans.setActualSignNum(isteachPlan.getActualSignNum()+1); 
							teachPlanService.updateTeachPlanByKey(teachPlans);
							//删除计划表redis
							JedisCache.delRedisVal(TeachPlan.class.getSimpleName(),attiMainId.toString());
							LogUtil.info(this.getClass(), "submitAttitude", "签到成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "");
						}else {
							//根据用户ID更新【计划签到表】签到时间
							TeachPlanSign teachPlanSign = new TeachPlanSign();//计划签到表
							teachPlanSign.setStudentId(userId);//学生ID
							teachPlanSign.setPlanId(Integer.valueOf(attiMainId));//计划ID
							teachPlanSign.setSignTime(dateTimes);
							teachPlanSignService.updateTeachPlanSignByKey(teachPlanSign);
							//增加【计划表】已签到人数 
							TeachPlan teachPlans = new TeachPlan();//计划表
							teachPlans.setPlanId(Integer.valueOf(attiMainId));
							teachPlans.setActualSignNum(isteachPlan.getActualSignNum()+1); 
							teachPlanService.updateTeachPlanByKey(teachPlans);
							//删除计划表redis
							JedisCache.delRedisVal(TeachPlan.class.getSimpleName(),attiMainId.toString());
							LogUtil.info(this.getClass(), "submitAttitude", "签到成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "");
						}
					}else if(dateTime < isteachPlan.getPlanDate()){
						LogUtil.error(this.getClass(), "submitAttitude", "未到计划签到日期");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_SIGN_IN);
					}else if(dateTime > isteachPlan.getPlanDate()){
						LogUtil.error(this.getClass(), "submitAttitude", "已过计划签到日期");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_SIGN_FORMERLY);
					}
				}else{
					LogUtil.error(this.getClass(), "submitAttitude", "已经签到");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_SIGN_IN);
				}
			}else {
				if(isteachPlanSign.getSignTime() == null){
					//获取系统时间与【计划表】的计划日期对比在同一天才可签到 
					//获取系统时间戳到天
					SimpleDateFormat sip = new SimpleDateFormat("yyyyMMdd");
				    String date = sip.format(new Date());
				    Date parse = null;
					try {
						parse = sip.parse(date);
					} catch (ParseException e) {
					}
				    int dateTime = (int)(parse.getTime()/1000);
					if(dateTime == isteachPlan.getPlanDate()){
						//签到时间时间戳精确到秒
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dates = sdf.format(new Date());
					    Date parses = null;
						try {
							parses = sdf.parse(dates);
						} catch (ParseException e) {
						}
					    int dateTimes = (int)(parses.getTime()/1000);
						
					    //查询计划签到表查看计划签到表是否有该学生信息
					    TeachPlanSign teachPlanSigns = new TeachPlanSign();//计划签到表
						teachPlanSigns.setStudentId(userId);//学生ID
						teachPlanSigns.setPlanId(Integer.valueOf(attiMainId));//计划ID
						TeachPlanSign isTeachPlanSigns =teachPlanSignService.getTeachPlanSigns(teachPlanSigns);
						if(isTeachPlanSigns == null){
							//根据用户ID更新【计划签到表】签到时间
							TeachPlanSign teachPlanSign = new TeachPlanSign();//计划签到表
							teachPlanSign.setStudentId(userId);//学生ID
							teachPlanSign.setPlanId(Integer.valueOf(attiMainId));//计划ID
							teachPlanSign.setSignTime(dateTimes);
							teachPlanSign.setIsDelete(GameConstants.NO_DEL);
							teachPlanSignService.insertTeachPlanSign(teachPlanSign);
							//增加【计划表】已签到人数 
							TeachPlan teachPlans = new TeachPlan();//计划表
							teachPlans.setPlanId(Integer.valueOf(attiMainId));
							teachPlans.setActualSignNum(isteachPlan.getActualSignNum()+1); 
							teachPlanService.updateTeachPlanByKey(teachPlans);
							//删除计划表redis
							JedisCache.delRedisVal(TeachPlan.class.getSimpleName(),attiMainId.toString());
							LogUtil.info(this.getClass(), "submitAttitude", "签到成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "");
						}else {
							//根据用户ID更新【计划签到表】签到时间
							TeachPlanSign teachPlanSign = new TeachPlanSign();//计划签到表
							teachPlanSign.setStudentId(userId);//学生ID
							teachPlanSign.setPlanId(Integer.valueOf(attiMainId));//计划ID
							teachPlanSign.setSignTime(dateTimes);
							teachPlanSignService.updateTeachPlanSignByKey(teachPlanSign);
							//增加【计划表】已签到人数 
							TeachPlan teachPlans = new TeachPlan();//计划表
							teachPlans.setPlanId(Integer.valueOf(attiMainId));
							teachPlans.setActualSignNum(isteachPlan.getActualSignNum()+1); 
							teachPlanService.updateTeachPlanByKey(teachPlans);
							//删除计划表redis
							JedisCache.delRedisVal(TeachPlan.class.getSimpleName(),attiMainId.toString());
							LogUtil.info(this.getClass(), "submitAttitude", "签到成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "");
						}
					}else if(dateTime < isteachPlan.getPlanDate()){
						LogUtil.error(this.getClass(), "submitAttitude", "未到计划签到日期");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_SIGN_IN);
					}else if(dateTime > isteachPlan.getPlanDate()){
						LogUtil.error(this.getClass(), "submitAttitude", "已过计划签到日期");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_SIGN_FORMERLY);
					}
				}else{
					LogUtil.error(this.getClass(), "submitAttitude", "已经签到");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_SIGN_IN);
				}
			}
		}else{
			LogUtil.error(this.getClass(), "submitAttitude", "未开启签到功能");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_OPEN_SIGN);
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	/**
	 * 
	 * @return
	 */
	public JSONObject submitAttitudeFour(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		if(platformModule == GameConstants.MANNER_TEACHING){//教学 课程讨论表
			//根据讨论信息主体id判断对应讨论信息主体是否存在
			TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
			teachCourseDiscuss.setDiscussId(Integer.valueOf(attiMainId));
			teachCourseDiscuss.setIsDelete(GameConstants.NO_DEL);
			TeachCourseDiscuss isteachCourseDiscuss = teachCourseDiscussService.getTeachCourseDiscuss(teachCourseDiscuss);
			if(isteachCourseDiscuss == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//根据用户id和信息主体id以及关联对象类型查询【态度表】判读用户是否已经对讨论信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.COURSE_DISCUSSION);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.COURSE_DISCUSSION);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//对应【讨论表】增加赞数量 删除讨论表redis
				TeachCourseDiscuss addteachCourseDiscuss = new TeachCourseDiscuss();
				addteachCourseDiscuss.setDiscussId(Integer.valueOf(attiMainId));
				addteachCourseDiscuss.setPraiseNum(isteachCourseDiscuss.getPraiseNum()+1);
				teachCourseDiscussService.updateTeachCourseDiscussByKey(addteachCourseDiscuss);
				JedisCache.delRedisVal(TeachCourseDiscuss.class.getSimpleName(),attiMainId);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}else if(platformModule == GameConstants.MANNER_ARENA){//竞技场  赛事讨论表
			//根据讨论信息主体id判断对应讨论信息主体是否存在
			ArenaCompetitionDiscuss arenaCompetitionDiscuss = new ArenaCompetitionDiscuss();
			arenaCompetitionDiscuss.setDiscussId(Integer.valueOf(attiMainId));
			arenaCompetitionDiscuss.setIsDelete(GameConstants.NO_DEL);
			ArenaCompetitionDiscuss isarenaCompetitionDiscuss =arenaCompetitionDiscussService.getArenaCompetitionDiscuss(arenaCompetitionDiscuss);
			if(isarenaCompetitionDiscuss == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//根据用户id和信息主体id以及关联对象类型查询【态度表】判读用户是否已经对讨论信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.COMPETITION_DISCUSSION);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.COMPETITION_DISCUSSION);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//对应【讨论表】增加赞数量 删除讨论表redis
				ArenaCompetitionDiscuss uparenaCompetitionDiscuss = new ArenaCompetitionDiscuss();
				uparenaCompetitionDiscuss.setDiscussId(Integer.valueOf(attiMainId));
				uparenaCompetitionDiscuss.setPraiseNum(isarenaCompetitionDiscuss.getPraiseNum()+1);
				arenaCompetitionDiscussService.updateArenaCompetitionDiscussByKey(uparenaCompetitionDiscuss);
				JedisCache.delRedisVal(ArenaCompetitionDiscuss.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isarenaCompetitionDiscuss.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}else if(platformModule == GameConstants.MANNER_CLUB){//俱乐部  培训班讨论表
			//根据讨论信息主体id判断对应讨论信息主体是否存在
			ClubTrainingDiscuss clubTrainingDiscuss = new ClubTrainingDiscuss();
			clubTrainingDiscuss.setDiscussId(Integer.valueOf(attiMainId));
			clubTrainingDiscuss.setIsDelete(GameConstants.NO_DEL);
			ClubTrainingDiscuss isclubTrainingDiscuss = clubTrainingDiscussService.getClubTrainingDiscuss(clubTrainingDiscuss);
			if(isclubTrainingDiscuss == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//根据用户id和信息主体id以及关联对象类型查询【态度表】判读用户是否已经对讨论信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.TRAIN_DISCUSSION);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.TRAIN_DISCUSSION);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//对应【讨论表】增加赞数量 删除讨论表redis
				ClubTrainingDiscuss upclubTrainingDiscuss = new ClubTrainingDiscuss();
				upclubTrainingDiscuss.setDiscussId(Integer.valueOf(attiMainId));
				upclubTrainingDiscuss.setPraiseNum(isclubTrainingDiscuss.getPraiseNum()+1);
				clubTrainingDiscussService.updateClubTrainingDiscussByKey(upclubTrainingDiscuss);
				JedisCache.delRedisVal(ClubTrainingDiscuss.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isclubTrainingDiscuss.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	/**
	 * 5:通知
	 * @return
	 */
	public JSONObject submitAttitudeFive(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		if(actionType == GameConstants.MANNER_STICK){//置顶
			//通过通知信息主体ID判断信息主体是否存在
			ClubNotice clubNotice = new ClubNotice();
			clubNotice.setNoticeId(Integer.valueOf(attiMainId));
			clubNotice.setIsDelete(GameConstants.NO_DEL);
			ClubNotice isclubNotice = clubNoticeService.getClubNotice(clubNotice);
			if(isclubNotice == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//通过用户Id查询【用户表】获得俱乐部Id 通过通知信息主体Id查询通知表获得俱乐部Id 判断是否为一个俱乐部
			CenterUser iscenterUser =centerUserService.getCenterUserById(userId);
			if(!iscenterUser.getClubId().equals(isclubNotice.getClubId())){
				LogUtil.error(this.getClass(), "submitAttitude", "请操作自己的俱乐部");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ONESELF_CLUB);
			}
			
			//通过俱乐部ID和用户ID查询【俱乐部会员表】判断用户是否为俱乐部会长   仅会长可置顶
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(isclubNotice.getClubId());
			clubMember.setUserId(userId);
			ClubMember isClubMember =clubMemberService.getClubMemberOne(clubMember);
			if(isClubMember.getLevel() != GameConstants.PRESIDENT){
				LogUtil.error(this.getClass(), "submitAttitude", "仅会长可以置顶");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PRESIDENT_STICK);
			}
			//查询俱乐部通知表判断是否有置顶通知  
			ClubNotice selectclubNotices = new ClubNotice();
			selectclubNotices.setClubId(isclubNotice.getClubId());
			selectclubNotices.setIsTop(1);
			selectclubNotices.setIsDelete(0);
			ClubNotice isselectclubNotices =clubNoticeService.getClubNotice(selectclubNotices);
			if(null != isselectclubNotices){
				//删除通知表置顶通知的缓存
				JedisCache.delRedisVal(ClubNotice.class.getSimpleName(),isselectclubNotices.getNoticeId().toString());
			}
			//取消其它置顶
			ClubNotice clubNotices = new ClubNotice();
			clubNotices.setClubId(isclubNotice.getClubId());
			clubNotices.setIsTop(0);
			clubNoticeService.updateClubNoticeByKeyAll(clubNotices);
			JedisCache.delRedisVal(ClubNotice.class.getSimpleName(),isclubNotice.getNoticeId().toString());
			//动态表的俱乐部通知置顶也相应取消
			CenterLive centerLive =new CenterLive();
			centerLive.setLiveType(GameConstants.LIVE_TYPE_NOTICE);//俱乐部通知
			centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_NOTICE);//俱乐部通知
			centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);//俱乐部动态
			centerLive.setMainObjetId(isclubNotice.getClubId());//俱乐部id
			centerLive.setIsDelete(GameConstants.NO_DEL);
			centerLive.setIsTop(1);
			centerLive = centerLiveService.getCenterLive(centerLive);
			if(null != centerLive){
				centerLive.setIsTop(0);
				centerLiveService.updateCenterLiveByKeyIsTop(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), centerLive.getLiveId().toString());
			}
			//置顶通知
			ClubNotice clubNoticeTop = new ClubNotice();
			clubNoticeTop.setNoticeId(Integer.valueOf(attiMainId));
			clubNoticeTop.setIsTop(1);
			clubNoticeService.updateClubNoticeByKey(clubNoticeTop);
			JedisCache.delRedisVal(ClubNotice.class.getSimpleName(),attiMainId.toString());
			//置顶俱乐部动态表话题
			CenterLive ce =new CenterLive();
			ce.setLiveType(GameConstants.LIVE_TYPE_NOTICE);//俱乐部通知
			ce.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_NOTICE);//俱乐部通知
			ce.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);//俱乐部动态
			ce.setMainObjetId(isclubNotice.getClubId());//俱乐部id
			ce.setIsDelete(GameConstants.NO_DEL);
			ce.setLiveMainId(Integer.valueOf(attiMainId));//置顶的话题id
			//获取动态表主键用于删除redis
			ce = centerLiveService.getCenterLive(ce);
			//修改动态表为置顶
			if(null != ce){
				ce.setIsTop(1);
				centerLiveService.updateCenterLiveByKeyIsTop(ce);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), ce.getLiveId().toString());
			}
			LogUtil.info(this.getClass(), "submitAttitude", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}else if(actionType == GameConstants.MANNER_CANCEL_STICK){//取消置顶
			//通过通知信息主体ID判断信息主体是否存在
			ClubNotice clubNotice = new ClubNotice();
			clubNotice.setNoticeId(Integer.valueOf(attiMainId));
			clubNotice.setIsDelete(GameConstants.NO_DEL);
			ClubNotice isclubNotice = clubNoticeService.getClubNotice(clubNotice);
			if(isclubNotice == null){
				LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
			}
			//通过用户Id查询【用户表】获得俱乐部Id 通过通知信息主体Id查询通知表获得俱乐部Id 判断是否为同一个俱乐部
			CenterUser iscenterUser =centerUserService.getCenterUserById(userId);
			if(!iscenterUser.getClubId().equals(isclubNotice.getClubId())){
				LogUtil.error(this.getClass(), "submitAttitude", "请操作自己的俱乐部信息");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ONESELF_CLUB);
			}
			
			//通过俱乐部ID和用户ID查询【俱乐部会员表】判断用户是否为俱乐部会长   仅会长可取消置顶
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(isclubNotice.getClubId());
			clubMember.setUserId(userId);
			ClubMember isClubMember =clubMemberService.getClubMemberOne(clubMember);
			if(isClubMember.getLevel() != GameConstants.PRESIDENT){
				LogUtil.error(this.getClass(), "submitAttitude", "仅会长可以取消置顶");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PRESIDENT_CANCEL_STICK);
			}
			//取消置顶
			ClubNotice clubNotices = new ClubNotice();
			clubNotices.setNoticeId(Integer.valueOf(attiMainId));
			clubNotices.setIsTop(0);
			clubNoticeService.updateClubNoticeByKey(clubNotices);
			JedisCache.delRedisVal(ClubNotice.class.getSimpleName(),attiMainId.toString());
			//动态表的俱乐部通知置顶也相应取消
			CenterLive centerLive =new CenterLive();
			centerLive.setLiveType(GameConstants.LIVE_TYPE_NOTICE);//俱乐部通知
			centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_NOTICE);//俱乐部通知
			centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);//俱乐部动态
			centerLive.setMainObjetId(isclubNotice.getClubId());//俱乐部id
			centerLive.setIsDelete(GameConstants.NO_DEL);
			centerLive.setIsTop(1);
			centerLive = centerLiveService.getCenterLive(centerLive);
			if(null != centerLive){
				centerLive.setIsTop(0);
				centerLiveService.updateCenterLiveByKeyIsTop(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), centerLive.getLiveId().toString());
			}
			LogUtil.info(this.getClass(), "submitAttitude", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	
	/**
	 * 6:话题
	 */
	public JSONObject submitAttitudeSix(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据话题信息主体id查询【俱乐部话题表】判断对应话题是否存在 
		ClubTopic clubTopic = new ClubTopic();
		clubTopic.setTopicId(Integer.valueOf(attiMainId));
		clubTopic.setIsDelete(GameConstants.NO_DEL);
		clubTopic.setIsFrozen(0);
		ClubTopic isClubTopic =clubTopicService.selectSingleClubTopic(clubTopic);
		if(isClubTopic == null){
			LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在或已冻结");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
		}
		if(actionType == GameConstants.MANNER_THUMB_UP){//点赞
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对回答信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setRelObjetType(GameConstants.CLUB_TOPIC);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "submitAttitude", "日期转换异常");
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.CLUB_TOPIC);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新话题表赞数量
				ClubTopic clubTopics = new ClubTopic();
				clubTopics.setTopicId(Integer.valueOf(attiMainId));
				clubTopics.setPraiseNum(isClubTopic.getPraiseNum()+1);
				clubTopicService.updateClubTopicByKey(clubTopics);
				JedisCache.delRedisVal(ClubTopic.class.getSimpleName(), attiMainId.toString());
				//增加返回赞数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isClubTopic.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}else if(actionType == GameConstants.MANNER_STICK){//置顶
			//通过用户Id查询【用户表】获得俱乐部Id 通过通知信息主体Id查询通知表获得俱乐部Id 判断是否为一个俱乐部
			CenterUser iscenterUser =centerUserService.getCenterUserById(userId);
			if(!iscenterUser.getClubId().equals(isClubTopic.getClubId())){
				LogUtil.error(this.getClass(), "submitAttitude", "请操作自己的俱乐部信息");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ONESELF_CLUB);
			}else {
				//通过俱乐部ID和用户ID查询【俱乐部会员表】判断用户是否为俱乐部会长   仅会长可置顶
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(isClubTopic.getClubId());
				clubMember.setUserId(userId);
				ClubMember isClubMember =clubMemberService.getClubMemberOne(clubMember);
				if(isClubMember.getLevel() != GameConstants.PRESIDENT){
					LogUtil.error(this.getClass(), "submitAttitude", "仅会长可以置顶");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PRESIDENT_STICK);
				}else {
					//取消其它置顶
					ClubTopic clubTopics = new ClubTopic();
					clubTopics.setClubId(isClubTopic.getClubId());
					clubTopics.setIsTop(0);
					clubTopicService.updateClubTopicByKeyAll(clubTopics);
					JedisCache.delRedisVal(ClubTopic.class.getSimpleName(), isClubTopic.getTopicId().toString());
					//取消动态表话题的置顶
					CenterLive centerLive =new CenterLive();
					centerLive.setLiveType(GameConstants.LIVE_TYPE_TOPIC);//俱乐部话题
					centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_TOPIC);//俱乐部话题
					centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);//俱乐部动态
					centerLive.setMainObjetId(isClubTopic.getClubId());//俱乐部id
					centerLive.setIsDelete(GameConstants.NO_DEL);
					centerLive.setIsTop(1);
					centerLive = centerLiveService.getCenterLive(centerLive);
					if(null != centerLive){
						centerLive.setIsTop(0);
						centerLiveService.updateCenterLiveByKeyIsTop(centerLive);
						JedisCache.delRedisVal(CenterLive.class.getSimpleName(), centerLive.getLiveId().toString());
					}
					//置顶话题
					ClubTopic upClubTopic = new ClubTopic();
					upClubTopic.setTopicId(Integer.valueOf(attiMainId));
					upClubTopic.setIsTop(1);
					clubTopicService.updateClubTopicByKey(upClubTopic);
					JedisCache.delRedisVal(ClubTopic.class.getSimpleName(), attiMainId.toString());
					//置顶俱乐部动态表话题
					CenterLive ce =new CenterLive();
					ce.setLiveType(1);//俱乐部话题
					ce.setLiveMainType(1);//俱乐部话题
					ce.setMainObjetType(2);//俱乐部动态
					ce.setMainObjetId(isClubTopic.getClubId());//俱乐部id
					ce.setIsDelete(GameConstants.NO_DEL);
					ce.setLiveMainId(Integer.valueOf(attiMainId));//置顶的话题id
					ce = centerLiveService.getCenterLive(ce);
					if(null != ce){
						ce.setIsTop(1);
						centerLiveService.updateCenterLiveByKeyIsTop(ce);
						JedisCache.delRedisVal(CenterLive.class.getSimpleName(), ce.getLiveId().toString());
					}
					LogUtil.info(this.getClass(), "submitAttitude", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
					}
				}
			}
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
		}
	
	/**
	 * 7:心情
	 */
	public JSONObject submitAttitudeSeven(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据心情信息主体ID查询【俱乐部会员心情表】判断心情信息是否存在
		ClubMemberMood clubMemberMood = new ClubMemberMood();
		clubMemberMood.setMoodId(Integer.valueOf(attiMainId));
		clubMemberMood.setIsFrozen(0);
		ClubMemberMood isClubMemberMood = clubMemberMoodService.getClubMemberMood(clubMemberMood);
		if(isClubMemberMood == null){
			LogUtil.error(this.getClass(), "submitAttitude", "信息主体不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
		}
		if(actionType == GameConstants.MANNER_THUMB_UP){//2:点赞；
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对回答信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setAttiType(GameConstants.PRAISE);
			centerAttitude.setRelObjetType(GameConstants.PERSONAL_MOOD);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.PERSONAL_MOOD);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新赞数量
				ClubMemberMood upclubMemberMood = new ClubMemberMood();
				upclubMemberMood.setMoodId(Integer.valueOf(attiMainId));
				upclubMemberMood.setPraiseNum(isClubMemberMood.getPraiseNum()+1);
				clubMemberMoodService.updateClubMemberMoodByKey(upclubMemberMood);
				JedisCache.delRedisVal(ClubMemberMood.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isClubMemberMood.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}else if(actionType == GameConstants.MANNER_DOT_TRAMPLE){//6:点踩；
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对心情信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setAttiType(GameConstants.TRAMPLE);
			centerAttitude.setRelObjetType(GameConstants.PERSONAL_MOOD);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点踩");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_TRAMPLE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.TRAMPLE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.PERSONAL_MOOD);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新踩数量
				ClubMemberMood upclubMemberMood = new ClubMemberMood();
				upclubMemberMood.setMoodId(Integer.valueOf(attiMainId));
				upclubMemberMood.setAgainstNum(isClubMemberMood.getAgainstNum()+1);
				clubMemberMoodService.updateClubMemberMoodByKey(upclubMemberMood);
				JedisCache.delRedisVal(ClubMemberMood.class.getSimpleName(),attiMainId.toString());
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAMETER_ERROR);
	}
	
	/**
	 * 8:企业
	 * @return
	 */
	public JSONObject submitAttitudeEight(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据信息主体ID查询企业表判断企业是否存在
		CenterCompany centerCompany = new CenterCompany();
		centerCompany.setCompanyId(String.valueOf(attiMainId));
		CenterCompany isCenterCompany =centerCompanyService.getCenterCompany(centerCompany);
		if(isCenterCompany == null){
			//如果企业不存在添加一条记录
			CenterCompany centerCompanys = new CenterCompany();
			centerCompanys.setCompanyId(attiMainId);
			centerCompanys.setPraiseNum(0);
			Integer companyId = centerCompanyService.insertCenterCompany(centerCompanys);
			
			//签到时间时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
			}
		    int dateTime = (int)(parses.getTime()/1000);
			//添加一条数据到态度表
			CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
			addCenterAttitude.setCreateUserId(userId);
			addCenterAttitude.setCreateTime(dateTime);
			addCenterAttitude.setAttiType(GameConstants.PRAISE);
			addCenterAttitude.setRelObjetId(centerCompanys.getDataId());
			addCenterAttitude.setRelObjetType(GameConstants.ENTERPRISE);
			centerAttitudeService.insertCenterAttitude(addCenterAttitude);
			//更新企业表赞数量
			CenterCompany upCenterCompany = new CenterCompany();
			upCenterCompany.setDataId(centerCompanys.getDataId());
			upCenterCompany.setPraiseNum(1);
			centerCompanyService.updateCenterCompanyByKey(upCenterCompany);
			JedisCache.delRedisVal(CenterCompany.class.getSimpleName(),attiMainId.toString());
			//返回点赞总数量
			Map<String , Object> support = new HashMap<String,Object>();
			support.put("supportCount", centerCompanys.getPraiseNum() + 1);
			LogUtil.info(this.getClass(), "submitAttitude", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
		}else {
			//根据用户id和回答信息主体id以及关联对象类型查询【态度表】判读用户是否已经对回答信息主体进行过操作
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(isCenterCompany.getDataId());
			centerAttitude.setAttiType(GameConstants.PRAISE);
			centerAttitude.setRelObjetType(GameConstants.ENTERPRISE);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对信息主体进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ALREADY_LIKE);
			}else {
				
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(isCenterCompany.getDataId());
				addCenterAttitude.setRelObjetType(GameConstants.ENTERPRISE);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新企业表赞数量
				CenterCompany upCenterCompany = new CenterCompany();
				upCenterCompany.setDataId(isCenterCompany.getDataId());
				upCenterCompany.setPraiseNum(1);
				centerCompanyService.updateCenterCompanyByKey(upCenterCompany);
				JedisCache.delRedisVal(CenterCompany.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isCenterCompany.getPraiseNum() + 1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
	}
	
	/**
	 * 9:人员
	 * @return
	 */
	public JSONObject submitAttitudeNine(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据人员信息主体id查询【人员表】判断人员是否存在
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(Integer.valueOf(attiMainId));
		CenterUser isCenterUser =centerUserService.selectCenterUser(centerUser);
		if(isCenterUser == null ){
			LogUtil.error(this.getClass(), "submitAttitude", "用户不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.USER_ERROR);
		}else{
			//根据用户id和人员信息主体id以及关联对象类型查询【态度表】判断是否已经对人员进行过点赞
			CenterAttitude centerAttitude = new CenterAttitude();//态度表
			centerAttitude.setCreateUserId(userId);
			centerAttitude.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitude.setAttiType(GameConstants.PRAISE);
			centerAttitude.setRelObjetType(GameConstants.PERSONNEL);
			CenterAttitude iscenterAttitude =centerAttitudeService.getCenterAttitude(centerAttitude);
			if(iscenterAttitude != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对人员进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_USER_LIKE);
			}else {
				//时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.PERSONNEL);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				//更新人员表赞数量
				CenterUser upCenterUser = new CenterUser();
				upCenterUser.setUserId(Integer.valueOf(attiMainId));
				if(isCenterUser.getPraiseNum() == null){
					upCenterUser.setPraiseNum(0+1);
					support.put("supportCount",1);
				}else {
					upCenterUser.setPraiseNum(isCenterUser.getPraiseNum()+1);
					support.put("supportCount",isCenterUser.getPraiseNum()+1);
				}
				try {
					centerUserService.updateCenterUserByKey(upCenterUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(),attiMainId.toString());
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
	}
	
	/**
	 * 10:相册图片
	 * @return
	 */
	public JSONObject submitAttitudeTen(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//根据相册图片信息主体id查询【照片表】判断照片是否存在
		CenterPhoto centerPhoto = new CenterPhoto();
		centerPhoto.setPhotoId(Integer.valueOf(attiMainId));
		centerPhoto.setIsDelete(GameConstants.NO_DEL);
		CenterPhoto isCenterPhoto = centerPhotoService.getCenterPhoto(centerPhoto);
		if(isCenterPhoto == null){
			LogUtil.error(this.getClass(), "submitAttitude", "照片不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.PHOTO_NOT);
		}else {
			//根据用户id和人员信息主体id以及关联对象类型查询【态度表】判断是否已经对人员进行过点赞
			CenterAttitude centerAttitudes = new CenterAttitude();//态度表
			centerAttitudes.setCreateUserId(userId);
			centerAttitudes.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitudes.setAttiType(GameConstants.PRAISE);
			centerAttitudes.setRelObjetType(GameConstants.PHOTO_ALBUM);
			CenterAttitude iscenterAttitudes =centerAttitudeService.getCenterAttitude(centerAttitudes);
			if(iscenterAttitudes != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对照片进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_PHOTO_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.PHOTO_ALBUM);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新照片赞数量
				CenterPhoto upCenterPhoto = new CenterPhoto();
				upCenterPhoto.setPhotoId(Integer.valueOf(attiMainId));
				upCenterPhoto.setPraiseNum(isCenterPhoto.getPraiseNum()+1);
				centerPhotoService.updateCenterPhotoByKey(upCenterPhoto);
				JedisCache.delRedisVal(CenterPhoto.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount", isCenterPhoto.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
	}
	
	/**
	 * 11:话题的评论
	 * @return
	 */
	public JSONObject submitAttitudEeleven(Integer platformModule,Integer actionType,String attiMainId,Integer userId){
		//查询俱乐部话题评论表判断评论是否存在
		ClubTopicComment clubTopicComment = new ClubTopicComment();
		clubTopicComment.setCommentId(Integer.valueOf(attiMainId));
		clubTopicComment.setIsFrozen(0);
		ClubTopicComment isClubTopicComment =clubTopicCommentService.getClubTopicComment(clubTopicComment);
		if(isClubTopicComment == null){
			LogUtil.error(this.getClass(), "submitAttitude", "话题评论信息不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.INFORMATION_NOT);
		}else {
			//根据用户id和人员信息主体id以及关联对象类型查询【态度表】判断是否已经对话题内容进行过点赞
			CenterAttitude centerAttitudes = new CenterAttitude();//态度表
			centerAttitudes.setCreateUserId(userId);
			centerAttitudes.setRelObjetId(Integer.valueOf(attiMainId));
			centerAttitudes.setAttiType(GameConstants.PRAISE);
			centerAttitudes.setRelObjetType(GameConstants.TOPICCONTENT);
			CenterAttitude iscenterAttitudes =centerAttitudeService.getCenterAttitude(centerAttitudes);
			if(iscenterAttitudes != null){
				LogUtil.error(this.getClass(), "submitAttitude", "已经对话题评论进行过点赞");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_TOPIC_LIKE);
			}else {
				//签到时间时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//添加一条数据到态度表
				CenterAttitude addCenterAttitude = new CenterAttitude();//态度表
				addCenterAttitude.setCreateUserId(userId);
				addCenterAttitude.setCreateTime(dateTime);
				addCenterAttitude.setAttiType(GameConstants.PRAISE);
				addCenterAttitude.setRelObjetId(Integer.valueOf(attiMainId));
				addCenterAttitude.setRelObjetType(GameConstants.TOPICCONTENT);
				centerAttitudeService.insertCenterAttitude(addCenterAttitude);
				//更新话题评论内容赞数量
				ClubTopicComment upclubTopicComment = new ClubTopicComment();
				upclubTopicComment.setCommentId(Integer.valueOf(attiMainId));
				upclubTopicComment.setPraiseNum(isClubTopicComment.getPraiseNum()+1);
				clubTopicCommentService.updateClubTopicCommentByKey(upclubTopicComment);
				JedisCache.delRedisVal(ClubTopicComment.class.getSimpleName(),attiMainId.toString());
				//返回点赞总数量
				Map<String , Object> support = new HashMap<String,Object>();
				support.put("supportCount",isClubTopicComment.getPraiseNum()+1);
				LogUtil.info(this.getClass(), "submitAttitude", "对话题内容点赞成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "" ,support);
			}
		}
	}
	
	
	
	
	
}
