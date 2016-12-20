package com.seentao.stpedu.persionalcenter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterPrivateMessageDao;
import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.dao.ClubRelClassCourseDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubRelClassCourse;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.ClubTrainingCourseService;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.PublicPictureService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.common.service.TeachSchoolService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.persionalcenter.model.Messages;
import com.seentao.stpedu.persionalcenter.model.MessagesMobile;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;


@Service
public class ClubCenterService {
	
	@Autowired
	private ClubMainDao clubMainDao;
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;
	@Autowired
	private ClubRelClassCourseDao clubRelClassCourseDao;
	@Autowired
	private PublicPictureService publicPictureService;
	@Autowired
	private CenterPrivateMessageDao centerPrivateMessageDao;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	@Autowired
	private ClubMainService clubMainService;
	@Autowired
	private TeachSchoolService teachSchoolService;
	@Autowired
	private ClubTrainingCourseService clubTrainingCourseService;
	@Autowired
	private CenterAccountService centerAccountService;
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	@Autowired
	private TeachQuestionService teachQuestionService;
	@Autowired
	private ClubTrainingQuestionService clubTrainingQuestionService;
	

	/**
	 * 获取消息信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public String getMessages(int userId,int messageType,int limit,int start){
			LogUtil.info(this.getClass(), "getMessages", "获取消息信息参数：【userId："+userId+"】，【messageType："+messageType+"】，【start："+start+"】");
			JSONObject jsonObject=new JSONObject();
			 CenterNewsStatus centerNewsStatus=centerNewsStatusService.queryCenterNewsStatusByUserId(Integer.valueOf(userId));			
			//设置isnewRemaind为0
			switch(messageType){
				//私信
				case GameConstants.GET_MESSAGE_ONE:
					jsonObject=getMessageTypeOne(jsonObject,userId,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_ONE, centerNewsStatus);
					}
		        break;
		        //关注
				case GameConstants.GET_MESSAGE_TWO:
					jsonObject=getMessageTypeTwo(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_TWO, centerNewsStatus);
					}
			    break;
			    //邀请
				case GameConstants.GET_MESSAGE_THREE:
					jsonObject=getMessageTypeThree(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_THREE, centerNewsStatus);
					}
			    break;
			    //比赛
				case GameConstants.GET_MESSAGE_FOUR:
					jsonObject=getMessageTypeFour(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_FOUR, centerNewsStatus);
					}
			    break;
			    //系统
				case GameConstants.GET_MESSAGE_FIVE:
					jsonObject=getMessageTypeFive(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_ATTITUDE_FIVE, centerNewsStatus);
					}
			    break;
			}
			LogUtil.info(this.getClass(), "getMessages", "获取消息信息参数成功");
			return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_MESSAGE_SUCCESS, jsonObject).toJSONString();
	} 
	
	/**
	 * 获取消息信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public String getMessagesMobile(int userId,int messageType,int limit,int start){
			LogUtil.info(this.getClass(), "getMessages", "获取消息信息参数：【userId："+userId+"】，【messageType："+messageType+"】，【start："+start+"】");
			JSONObject jsonObject=new JSONObject();
			 CenterNewsStatus centerNewsStatus=centerNewsStatusService.queryCenterNewsStatusByUserId(Integer.valueOf(userId));			
			//设置isnewRemaind为0
			switch(messageType){
				//私信
				case GameConstants.GET_MESSAGE_ONE:
					jsonObject=getMessageTypeOneMobile(jsonObject,userId,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_ONE, centerNewsStatus);
					}
		        break;
		        //关注
				case GameConstants.GET_MESSAGE_TWO:
					jsonObject=getMessageTypeTwoMobile(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_TWO, centerNewsStatus);
					}
			    break;
			    //邀请
				case GameConstants.GET_MESSAGE_THREE:
					jsonObject=getMessageTypeThreeMobile(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_THREE, centerNewsStatus);
					}
					
			    break;
			    //比赛
				case GameConstants.GET_MESSAGE_FOUR:
					jsonObject=getMessageTypeFour(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_FOUR, centerNewsStatus);
					}
			    break;
			    //系统
				case GameConstants.GET_MESSAGE_FIVE:
					jsonObject=getMessageTypeFive(jsonObject,userId,messageType,limit,start);
					if(centerNewsStatus != null){
						this.queryCenterNewStatusByStatus(GameConstants.GET_MESSAGE_FIVE, centerNewsStatus);
					}
			    break;
			}
			LogUtil.info(this.getClass(), "getMessages", "获取消息信息参数成功");
			return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_MESSAGE_SUCCESS, jsonObject).toJSONString();
	} 
	
	
	public void queryCenterNewStatusByStatus(Integer actionType,CenterNewsStatus centerNewsStatus){
		
		if(GameConstants.GET_MESSAGE_ONE == actionType){
			centerNewsStatus.setIsNewPrivateMessage(GameConstants.CLUB_RIGHT_ZERO);
			
		}if(GameConstants.GET_MESSAGE_TWO == actionType){
			centerNewsStatus.setIsNewAttention(GameConstants.CLUB_RIGHT_ZERO);
		}if(GameConstants.GET_MESSAGE_THREE == actionType){
			centerNewsStatus.setIsNewQuestionInvite(GameConstants.CLUB_RIGHT_ZERO);
		}if(GameConstants.GET_MESSAGE_FOUR == actionType){
			centerNewsStatus.setIsNewGameInvite(GameConstants.CLUB_RIGHT_ZERO);
		}if(GameConstants.GET_MESSAGE_FIVE == actionType){
			centerNewsStatus.setIsNewSysNews(GameConstants.CLUB_RIGHT_ZERO);
		}
		//将用户消息表的私信状态表值置为
		try {
			centerNewsStatusService.updateCenterNewsStatusByKey(centerNewsStatus);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "updateCenterNewsStatusByKey", "updateCenterNewsStatusByKey is error",e);
		}
	}
	/**
	 * 获取私信信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeOne(JSONObject jsonObject,int userId,int limit,int start){
		//根据私信对话表的接受者的id获取私信表对话表中的数据 
		//分页查询
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		//调用分页组件userId
		QueryPage<CenterPrivateMessageTalk> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterPrivateMessageTalk.class);
		List<CenterPrivateMessageTalk> centerPrivateMessageTalkList= appQueryPage.getList();
		//返回对象封装
		List<Messages> retMessageList=new ArrayList<Messages>();
		for(CenterPrivateMessageTalk temp:centerPrivateMessageTalkList){
			//根据私信对话表消息发布者id,获取用户表的数据,并查询头像id,再查询图片表
			//消息主体的标题【私信的内容】,消息主体发布时间的时间戳,消息主体的对话数量【根据消息对话表的对话id查询私信表】
			//yy修改 （获取的用户信息是对方的信息）
			Integer nowUserId = null;
			if(temp.getSendUserId().equals(userId)){
				nowUserId = temp.getReceiveUserId();
			}else{
				nowUserId = temp.getSendUserId();
			}
			String sendRedis=RedisComponent.findRedisObject(nowUserId,CenterUser.class);
			CenterUser sendUser=JSONObject.parseObject(sendRedis, CenterUser.class);
			Messages messages=new Messages();
			//消息id
			messages.setMessageId(temp.getTalkId());
			//消息类型
			messages.setMessageType("1");
			//消息主体的发布者id
			messages.setMmCreaterId(String.valueOf(nowUserId));
			//消息主体的发布者姓名
			messages.setMmCreaterName(sendUser==null?"":Common.null2Str(sendUser.getUserName()));
			//消息主体的发布者昵称
			messages.setMmCreaterNickname(sendUser==null?"":Common.null2Str(sendUser.getNickName()));
			//消息主体的发布者头像链接
			String msg =Common.getImgUrl(sendUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			messages.setMmCreaterHeadLink(sendUser==null?"":Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			
			CenterPrivateMessage centerPrivateMessage=new CenterPrivateMessage();
			centerPrivateMessage.setPrivateMessageId(temp.getLastPrivateMessageId());
			centerPrivateMessage=centerPrivateMessageDao.selectCenterPrivateMessage(centerPrivateMessage);
			//消息主体的标题
			messages.setMessageMainTitle(Common.null2Str(centerPrivateMessage.getContent()));
			//消息主体的对话数量
			messages.setMmTalkCount(temp.getPrivateMessageNum());
			//消息主体发布时间的时间戳
			messages.setMmCreateDate(temp.getCreateTime());
			//消息主体的id
			messages.setMessageMainId(String.valueOf(centerPrivateMessage.getPrivateMessageId()));
			retMessageList.add(messages);
		}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
	}
	
	/**
	 * 获取私信信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeOneMobile(JSONObject jsonObject,int userId,int limit,int start){
		//根据私信对话表的接受者的id获取私信表对话表中的数据 
		//分页查询
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		//调用分页组件userId
		QueryPage<CenterPrivateMessageTalk> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterPrivateMessageTalk.class);
		List<CenterPrivateMessageTalk> centerPrivateMessageTalkList= appQueryPage.getList();
		//返回对象封装
		List<MessagesMobile> retMessageList=new ArrayList<MessagesMobile>();
		for(CenterPrivateMessageTalk temp:centerPrivateMessageTalkList){
			//根据私信对话表消息发布者id,获取用户表的数据,并查询头像id,再查询图片表
			//消息主体的标题【私信的内容】,消息主体发布时间的时间戳,消息主体的对话数量【根据消息对话表的对话id查询私信表】
			//从redis中获取用户
			//String sendRedis=RedisComponent.findRedisObject(temp.getSendUserId(),CenterUser.class);
			//CenterUser sendUser=JSONObject.parseObject(sendRedis, CenterUser.class);
			MessagesMobile messages=new MessagesMobile();
			//消息id
			messages.setMessageId(String.valueOf(temp.getTalkId()));
			//消息类型
			messages.setMessageType("1");
			
			CenterPrivateMessage centerPrivateMessage=new CenterPrivateMessage();
			centerPrivateMessage.setPrivateMessageId(temp.getLastPrivateMessageId());
			centerPrivateMessage=centerPrivateMessageDao.selectCenterPrivateMessage(centerPrivateMessage);
			//消息主体的标题
			messages.setMessageMainTitle(Common.null2Str(centerPrivateMessage.getContent()));
			//消息主体的对话数量
			messages.setMmTalkCount(temp.getPrivateMessageNum());
			//消息主体发布时间的时间戳
			messages.setMmCreateDate(String.valueOf(temp.getCreateTime()));
			//消息主体的id
			messages.setMessageMainId(String.valueOf(centerPrivateMessage.getPrivateMessageId()));
			
			
			int pmObjectId=0;
			//私信对象id
			if(centerPrivateMessage.getReceiveUserId()==userId){
				pmObjectId=centerPrivateMessage.getSendUserId();
			}else if(centerPrivateMessage.getSendUserId()==userId){
				pmObjectId=centerPrivateMessage.getReceiveUserId();
			}
			CenterUser user = JSONObject.parseObject(RedisComponent.findRedisObject(pmObjectId,CenterUser.class), CenterUser.class);
			//保存私信对象【手机端】
			messages.setPmObjectId(String.valueOf(pmObjectId));
			//消息主体的发布者id
			messages.setMmCreaterId(String.valueOf(pmObjectId));
			//返回昵称
			if(null!=temp){
				//消息主体的发布者姓名
				messages.setMmCreaterName(Common.null2Str(user.getRealName()));
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(Common.null2Str(user.getNickName()));
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				messages.setPmObjectNickname(JSONObject.parseObject(RedisComponent.findRedisObject(pmObjectId,CenterUser.class), CenterUser.class).getNickName());
			}else{
				messages.setPmObjectNickname("");
			}
			messages.setClassType(0);
			retMessageList.add(messages);
		}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
	}
	
	/**
	 * 获取关注信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeTwo(JSONObject jsonObject,int userId,int messageType,int limit,int start){	
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
		//调用分页组件
		
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<Messages> retMessageList=new ArrayList<Messages>();
		for(CenterNews temp:retList){
				//从redis中获取用户
//				String userRedis = RedisComponent.findRedisObject(temp.getUserId(),CenterUser.class);
//				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				
				
				String reObjRedis= RedisComponent.findRedisObject(temp.getRelObjectId(),CenterUser.class);
				CenterUser retCenterUser = JSONObject.parseObject(reObjRedis, CenterUser.class);
				
				Messages messages=new Messages();
				//消息id
				messages.setMessageId(temp.getNewsId());
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(retCenterUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(retCenterUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(retCenterUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(retCenterUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
			
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle("");
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(temp.getCreateTime());
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
		
	}
	
	/**
	 * 获取关注信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeTwoMobile(JSONObject jsonObject,int userId,int messageType,int limit,int start){	
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
		//调用分页组件
		
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		
		//返回对象封装
		List<MessagesMobile> retMessageList=new ArrayList<MessagesMobile>();
		for(CenterNews temp:retList){
				//从redis中获取用户
//				String userRedis = RedisComponent.findRedisObject(temp.getUserId(),CenterUser.class);
//				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				
				String reObjRedis= RedisComponent.findRedisObject(temp.getRelObjectId(),CenterUser.class);
				CenterUser retCenterUser = JSONObject.parseObject(reObjRedis, CenterUser.class);
				
				MessagesMobile messages=new MessagesMobile();
				//消息id
				messages.setMessageId(String.valueOf(temp.getNewsId()));
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(retCenterUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(retCenterUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(retCenterUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(retCenterUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
			
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle("");
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(String.valueOf(temp.getCreateTime()));
				messages.setClassType(0);
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
		
	}
	
	/**
	 * 获取邀请信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeThree(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
				
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<Messages> retMessageList=new ArrayList<Messages>();
		for(CenterNews temp:retList){
				//获取问题表的创建者
			TeachQuestion teachQuestion=new TeachQuestion();
			ClubTrainingQuestion clubTrainingQuestion=new ClubTrainingQuestion();
			int creatId=0;
			//根据RelObjectType判断是教学班问题或是问题表问题
			if(temp.getRelObjectType()==2){
				//教学班问题表
				teachQuestion.setQuestionId(temp.getRelObjectId());
				teachQuestion=teachQuestionService.getTeachQuestion(teachQuestion);
				creatId=teachQuestion.getCreateUserId();
			}else if(temp.getRelObjectType()==3){
				//问题表
				clubTrainingQuestion.setQuestionId(temp.getRelObjectId());
				clubTrainingQuestion=clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
				creatId=clubTrainingQuestion.getCreateUserId();
			}
			    //从redis中获取用户
				String userRedis = RedisComponent.findRedisObject(creatId,CenterUser.class);
				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				Messages messages=new Messages();
				//消息id
				messages.setMessageId(temp.getNewsId());
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(centerUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(centerUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(centerUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent().substring(temp.getContent().indexOf("【")+1, temp.getContent().indexOf("】")));
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(temp.getCreateTime());
				//消息主体的id
				messages.setMessageMainId(String.valueOf(temp.getRelObjectId()));
				//返回班级类型
				Integer classType=temp.getRelObjectType();
				if(2==classType){
					//教学班
					classType=1;
				}else{
					//俱乐部培训班
					classType=2;
				}
				//返回班级类型
				messages.setClassType(classType == null?0:classType);
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
			return jsonObject;
	}
	
	/**
	 * 获取邀请信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeThreeMobile(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
				
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<MessagesMobile> retMessageList=new ArrayList<MessagesMobile>();
		for(CenterNews temp:retList){
				//获取问题表的创建者
			TeachQuestion teachQuestion=new TeachQuestion();
			ClubTrainingQuestion clubTrainingQuestion=new ClubTrainingQuestion();
			int creatId=0;
			//根据RelObjectType判断是教学班问题或是问题表问题
			if(temp.getRelObjectType()==2){
				//教学班问题表
				teachQuestion.setQuestionId(temp.getRelObjectId());
				teachQuestion=teachQuestionService.getTeachQuestion(teachQuestion);
				creatId=temp.getCreateUserId();
			}else if(temp.getRelObjectType()==3){
				//问题表
				clubTrainingQuestion.setQuestionId(temp.getRelObjectId());
				clubTrainingQuestion=clubTrainingQuestionService.getClubTrainingQuestion(clubTrainingQuestion);
				creatId=temp.getCreateUserId();
			}
			    //从redis中获取用户
				String userRedis = RedisComponent.findRedisObject(creatId,CenterUser.class);
				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				MessagesMobile messages=new MessagesMobile();
				//消息id
				messages.setMessageId(String.valueOf(temp.getNewsId()));
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(centerUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(centerUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(centerUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg +ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent().substring(temp.getContent().indexOf("【")+1, temp.getContent().indexOf("】")));
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(String.valueOf(temp.getCreateTime()));
				//消息主体的id
				messages.setMessageMainId(String.valueOf(temp.getRelObjectId()));
				//返回班级类型
				Integer classType=temp.getRelObjectType();
				if(2==classType){
					//教学班
					classType=1;
				}else{
					//俱乐部培训班
					classType=2;
				}
				//返回班级类型
				messages.setClassType(classType==null?0:classType);
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
			return jsonObject;
	}
	/**
	 * 获取比赛信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeFour(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		String userRedis = RedisComponent.findRedisObject(userId,CenterUser.class);
		CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		//只对会长进行邀请
		conditionMap.put("userId", centerUser.getUserId());
		conditionMap.put("messageType", messageType-1);
						
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<Messages> retMessageList=new ArrayList<Messages>();
		for(CenterNews temp:retList){
				//从redis中获取用户
				
				Messages messages=new Messages();
				messages.setClassType(0);
				//消息id
				messages.setMessageId(temp.getNewsId());
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent().substring(temp.getContent().indexOf("【")+1, temp.getContent().indexOf("】")));
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(temp.getCreateTime());
				//消息主体的id
				messages.setMessageMainId(String.valueOf(temp.getRelObjectId()));
				//根据 关联对象id【比赛id】 调用chunlei封装接口 获取发布者，比赛报名分组模式，邀请备注，并通过发布者查询用户表获取-俱乐部ID，学校ID
				//根据比赛id返回比赛对象
				//type 1 邀请对象
				//[{"enrollType":3,"inviteDesc":"邀请俱乐部会员","creatorId":2}]
				IGameInterfaceService iGameInterfaceService= HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				//4是俱乐部邀请
				 ClubMain clubMain=null;
				if(4==temp.getRelObjectType()){
					String tempStrClub= iGameInterfaceService.getMatchInviteObjectByMatchId(temp.getRelObjectId(),1,centerUser.getClubId());
					  //返回结果
				    JSONObject jsonObjClub= JSONObject.parseObject(JSONObject.parseObject(tempStrClub).get("result").toString());
				 	String creatorId = RedisComponent.findRedisObject(jsonObjClub.getIntValue("creatorId"),CenterUser.class);
					CenterUser creator = JSONObject.parseObject(creatorId, CenterUser.class);
					//获取俱乐部
					if(null!=creator){
						clubMain=clubMainService.getClubMainByClubId(String.valueOf(creator.getClubId()));
						messages.setMmCreaterId(String.valueOf(creator.getUserId()));
						//消息主体的发布者姓名
						messages.setMmCreaterName(creator.getUserName());
						//消息主体的发布者昵称
						messages.setMmCreaterNickname(creator.getNickName());
						messages.setEnrollType(jsonObjClub.getIntValue("enrollType"));
						messages.setInviteDesc(jsonObjClub.getString("inviteDesc"));
						
						messages.setMmClubId(clubMain.getClubId().toString());
						messages.setMmClubName(clubMain.getClubName());
						messages.setMmSchoolId(String.valueOf(creator.getSchoolId()));
						TeachSchool school = teachSchoolService.getTeachSchool(creator.getSchoolId());
						messages.setMmSchoolName(school==null?"":school.getSchoolName());
						school=null;
					}else{
						messages.setMmCreaterId("");
						//消息主体的发布者姓名
						messages.setMmCreaterName("");
						//消息主体的发布者昵称
						messages.setMmCreaterNickname("");
						messages.setInviteDesc("");
						messages.setMmClubId("");
						messages.setMmClubName("");
						messages.setMmSchoolId("");
						messages.setMmSchoolName("");
						messages.setEnrollType(0);
					}
					
					
				}
				//5是学校邀请
				if(5==temp.getRelObjectType()){
				  String tempStrSchool= iGameInterfaceService.getMatchInviteObjectByMatchId(temp.getRelObjectId(),2,centerUser.getUserId());
				  //返回结果
				  JSONObject jsonObjClub= JSONObject.parseObject(JSONObject.parseObject(tempStrSchool).get("result").toString());
				  String creatorId = RedisComponent.findRedisObject(jsonObjClub.getIntValue("creatorId"),CenterUser.class);
				  CenterUser creator = JSONObject.parseObject(creatorId, CenterUser.class);
				 
				 //获取俱乐部
				 if(null!=creator){
					messages.setMmCreaterId(String.valueOf(creator.getUserId()));
					// 消息主体的发布者姓名
					messages.setMmCreaterName(creator.getUserName());
					// 消息主体的发布者昵称
					messages.setMmCreaterNickname(creator.getNickName());
					messages.setEnrollType(jsonObjClub.getIntValue("enrollType"));
					messages.setInviteDesc(jsonObjClub.getString("inviteDesc"));
					clubMain = clubMainService.getClubMainByClubId(String.valueOf(creator.getClubId()));
					messages.setMmClubId(clubMain.getClubId().toString());
					messages.setMmClubName(clubMain.getClubName());
					messages.setMmSchoolId(String.valueOf(creator.getSchoolId()));
					TeachSchool school = teachSchoolService.getTeachSchool(creator.getSchoolId());
					messages.setMmSchoolName(school==null? "" :school.getSchoolName());
				} else {
					messages.setMmCreaterId("");
					// 消息主体的发布者姓名
					messages.setMmCreaterName("");
					// 消息主体的发布者昵称
					messages.setMmCreaterNickname("");
					messages.setEnrollType(jsonObjClub.getIntValue("enrollType"));
					messages.setInviteDesc(jsonObjClub.getString("inviteDesc"));
					messages.setMmClubId("");
					messages.setMmClubName("");
					messages.setMmSchoolId("");
					messages.setMmSchoolName("");
				}
			}
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
		
	}
	
	/**
	 * 获取比赛信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeFourMobile(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		String userRedis = RedisComponent.findRedisObject(userId,CenterUser.class);
		CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		//只对会长进行邀请
		conditionMap.put("userId", centerUser.getUserId());
		conditionMap.put("messageType", messageType-1);
						
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<MessagesMobile> retMessageList=new ArrayList<MessagesMobile>();
		for(CenterNews temp:retList){
				//从redis中获取用户
				
			MessagesMobile messages=new MessagesMobile();
				//消息id
				messages.setMessageId(String.valueOf(temp.getNewsId()));
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg)== true ? msg+ActiveUrl.HEAD_MAP:msg);
/*				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent().substring(temp.getContent().indexOf("【")+1, temp.getContent().indexOf("】")));
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(String.valueOf(temp.getCreateTime()));
				//消息主体的id
				messages.setMessageMainId(String.valueOf(temp.getRelObjectId()));
				//根据 关联对象id【比赛id】 调用chunlei封装接口 获取发布者，比赛报名分组模式，邀请备注，并通过发布者查询用户表获取-俱乐部ID，学校ID
				//根据比赛id返回比赛对象
				//type 1 邀请对象
				//[{"enrollType":3,"inviteDesc":"邀请俱乐部会员","creatorId":2}]
				IGameInterfaceService iGameInterfaceService= HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				//4是俱乐部邀请
				 ClubMain clubMain=null;
				if(4==temp.getRelObjectType()){
					String tempStrClub= iGameInterfaceService.getMatchInviteObjectByMatchId(temp.getRelObjectId(),1,centerUser.getClubId());
					  //返回结果
				    JSONObject jsonObjClub= JSONObject.parseObject(JSONObject.parseObject(tempStrClub).get("result").toString());
				 	String creatorId = RedisComponent.findRedisObject(jsonObjClub.getIntValue("creatorId"),CenterUser.class);
					CenterUser creator = JSONObject.parseObject(creatorId, CenterUser.class);
					//获取俱乐部
					if(null!=creator){
						clubMain=clubMainService.getClubMainByClubId(String.valueOf(creator.getClubId()));
						messages.setMmCreaterId(String.valueOf(creator.getUserId()));
						//消息主体的发布者姓名
						messages.setMmCreaterName(creator.getUserName());
						//消息主体的发布者昵称
						messages.setMmCreaterNickname(creator.getNickName());
						messages.setEnrollType(jsonObjClub.getIntValue("enrollType"));
						messages.setInviteDesc(jsonObjClub.getString("inviteDesc"));
						
						messages.setMmClubId(clubMain.getClubId().toString());
						messages.setMmClubName(clubMain.getClubName());
						messages.setMmSchoolId(String.valueOf(creator.getSchoolId()));
						messages.setMmSchoolName(teachSchoolService.getTeachSchool(creator.getSchoolId()).getSchoolName());
					}else{
						messages.setMmCreaterId("");
						//消息主体的发布者姓名
						messages.setMmCreaterName("");
						//消息主体的发布者昵称
						messages.setMmCreaterNickname("");
						messages.setInviteDesc("");
						messages.setMmClubId("");
						messages.setMmClubName("");
						messages.setMmSchoolId("");
						messages.setMmSchoolName("");
					}
					
					
				}
				//5是学校邀请
				if(5==temp.getRelObjectType()){
					String tempStrSchool = iGameInterfaceService.getMatchInviteObjectByMatchId(temp.getRelObjectId(), 2,
							centerUser.getUserId());
					// 返回结果
					JSONObject jsonObjClub = JSONObject
							.parseObject(JSONObject.parseObject(tempStrSchool).get("result").toString());
					String creatorId = RedisComponent.findRedisObject(jsonObjClub.getIntValue("creatorId"),
							CenterUser.class);
					CenterUser creator = JSONObject.parseObject(creatorId, CenterUser.class);
					messages.setMmCreaterId(String.valueOf(creator.getUserId()));
					// 消息主体的发布者姓名
					messages.setMmCreaterName(creator.getUserName());
					// 消息主体的发布者昵称
					messages.setMmCreaterNickname(creator.getNickName());
					messages.setEnrollType(jsonObjClub.getIntValue("enrollType"));
					messages.setInviteDesc(jsonObjClub.getString("inviteDesc"));
					clubMain = clubMainService.getClubMainByClubId(String.valueOf(creator.getClubId()));
					messages.setMmClubId(clubMain.getClubId().toString());
					messages.setMmClubName(clubMain.getClubName());
					messages.setMmSchoolId(String.valueOf(creator.getSchoolId()));
					messages.setMmSchoolName(teachSchoolService.getTeachSchool(creator.getSchoolId()).getSchoolName());
				}
				
			
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		return jsonObject;
		
	}
	
	/**
	 * 获取系统信息
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeFive(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<Messages> retMessageList=new ArrayList<Messages>();
		for(CenterNews temp:retList){
				//获取问题表的创建者
			    //从redis中获取用户
				String userRedis = RedisComponent.findRedisObject(userId,CenterUser.class);
				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				Messages messages=new Messages();
				messages.setClassType(0);
				//消息id
				messages.setMessageId(temp.getNewsId());
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(centerUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(centerUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(centerUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*
				if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent());
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(temp.getCreateTime());
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
			return jsonObject;
	}
	
	/**
	 * 获取系统信息【手机】
	 * @author  lijin
	 * @date    2016年6月30日 下午9:54:25
	 */
	public JSONObject getMessageTypeFiveMobile(JSONObject jsonObject,int userId,int messageType,int limit,int start){
		//消息用户id和消息类型获取消息表的记录
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("messageType", messageType-1);
		//调用分页组件
		QueryPage<CenterNews> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterNews.class);
		List<CenterNews> retList= appQueryPage.getList();
		//返回对象封装
		List<MessagesMobile> retMessageList=new ArrayList<MessagesMobile>();
		for(CenterNews temp:retList){
				//获取问题表的创建者
			    //从redis中获取用户
				String userRedis = RedisComponent.findRedisObject(userId,CenterUser.class);
				CenterUser centerUser = JSONObject.parseObject(userRedis, CenterUser.class);
				MessagesMobile messages=new MessagesMobile();
				//消息id
				messages.setMessageId(String.valueOf(temp.getNewsId()));
				//消息类型
				messages.setMessageType(String.valueOf(messageType));
				//消息主体的发布者id
				messages.setMmCreaterId(String.valueOf(centerUser.getUserId()));
				//根据消息用户id查询用户表，获取姓名，昵称，查询头像id，查询图片表返回头像url
				//消息主体的发布者姓名
				messages.setMmCreaterName(centerUser.getUserName());
				//消息主体的发布者昵称
				messages.setMmCreaterNickname(centerUser.getNickName());
				//消息主体的发布者头像链接
				String msg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				messages.setMmCreaterHeadLink(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				/*if(null!=publicPictureService.getPublicPicture(centerUser.getHeadImgId())){
					messages.setMmCreaterHeadLink(publicPictureService.getPublicPicture(centerUser.getHeadImgId()).getDownloadUrl());
				}else{
					messages.setMmCreaterHeadLink("");
				}*/
				//消息主体的标题【私信的内容】，消息主体发布时间的时间戳，消息主体的对话数量【消息没有对话数量】
				//消息主体的标题
				messages.setMessageMainTitle(temp.getContent());
				//消息主体发布时间的时间戳
				messages.setMmCreateDate(String.valueOf(temp.getCreateTime()));
				retMessageList.add(messages);
			}
		jsonObject.put("messages", retMessageList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
			return jsonObject;
	}
	
	/**
	 * 获取俱乐部权益信息
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	public String getClubRights(int clubId,int userId){
		LogUtil.info(this.getClass(), "getClubRights", "获取俱乐部权益信息参数：【userId："+userId+"】，【clubId："+clubId+"】");
		JSONObject jsonObject = new JSONObject();
		// 根据俱乐部id获取俱乐部对象
		ClubMain clubMain = new ClubMain(clubId);
		//通过构造器传参，
		//clubMain.setClubId(clubId);
		clubMain = clubMainDao.selectClubMainEntity(clubMain);
		int buyClubOperateRights = 0, 
			buyGameEventOperateRights = 0;
		//购买赛事运营权需要的一级货币
		double georNeedFLevelAccount = Double.parseDouble(SystemConfig.getString("GAME_RIGHT_GEOR_NEED_FLEVEL_ACCOUNT").toString());
		//购买俱乐部运营权需要的一级货币
		double corNeedFLevelAccount  = Double.parseDouble("0");
		if(null!=clubMain){
			//当会员人数小于200人，则需要用新道币购买,如果该用户退出俱乐部,也不需要购买
			if(GameConstants.CLUB_NUM > clubMain.getMemberNum())
				corNeedFLevelAccount = Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_COR_NEED_FLEVEL_ACCOUNT").toString());
			// 获取是否已购买俱乐部运营权 1、表示已经购买,0、表示没有购买
			if (GameConstants.CLUB_RIGHT_ONE == clubMain.getIsBuyClubVip())
				// 是否已购买俱乐部运营权
				buyClubOperateRights = GameConstants.CLUB_RIGHT_ONE;
			// 获取是否购买赛事运营权限
			if (GameConstants.CLUB_RIGHT_ONE == clubMain.getIsBuyCompetition())
				buyGameEventOperateRights = GameConstants.CLUB_RIGHT_ONE;
		}
		// 是否已购买俱乐部运营权
		jsonObject.put("buyClubOperateRights", buyClubOperateRights);
		// 购买俱乐部运营权所需要的一级货币
		jsonObject.put("corNeedFLevelAccount", corNeedFLevelAccount);
		// 可创建会员加入方式为收费的俱乐部
		jsonObject.put("chargeClub", buyClubOperateRights);
		// 可创建报名收费的擂台赛
		jsonObject.put("chargeGame", buyClubOperateRights);
		// 可创建收费的培训班
		jsonObject.put("chargeTraining", buyClubOperateRights);
		// 官方大赛收费
		jsonObject.put("chargeOfficialGame", buyGameEventOperateRights);
		// 是否已购买赛事运营权
		jsonObject.put("buyGameEventOperateRights", buyGameEventOperateRights);
		// 购买赛事运营权所需要的一级货币
		jsonObject.put("georNeedFLevelAccount", georNeedFLevelAccount);
		LogUtil.info(this.getClass(), "getClubRights", "获取俱乐部权益信息成功");
		return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_CLUB_RIGHTS_SUCCESS, jsonObject).toJSONString();
			
	} 
	
	
	/**
	 * 购买俱乐部权益
	 * @author  lijin
	 * @date    2016年6月28日 下午21:54:25
	 */
	public String buyClubRights(String clubId,int userId, int actionType,String classId,String classType){
		if(clubMemberService.validateUserAndClubAndLevel(userId, GameConstants.CLUB_PRESIDENT_COACH_MEMBER)){
			LogUtil.info(this.getClass(), "buyClubRights", "购买俱乐部权益参数：【userId："+userId+"】，【clubId："+clubId+"】，【actionType："+actionType+"】，【classId："+classId+"】，【classType："+classType+"】");
			ClubMain clubMain=new ClubMain();
			CenterAccount centerAccount=new CenterAccount();
			/**
			 *  actionType =1 购买俱乐部运营权 ,
			 *  actionType =2 购买赛事运营权
			 */
			if(actionType==1||actionType==2){
				centerAccount.setUserId(Integer.parseInt(clubId));
				centerAccount.setUserType(GameConstants.CLUB_USER);
			}else{
				centerAccount.setUserId(userId);
				centerAccount.setUserType(GameConstants.INDIVIDUAL_USER);
			}
			
			centerAccount.setAccountType(GameConstants.STAIR_ONE);
			centerAccount=centerAccountService.getCenterAccount(centerAccount);
			double uMoney =0;
			if(null!=centerAccount){
				double userMoney = centerAccount.getBalance() == null ? 0 : centerAccount.getBalance();
				double lockAmount = centerAccount.getLockAmount() == null ? 0 : centerAccount.getLockAmount();
				uMoney = userMoney - lockAmount;
			}else{
				//没有账户存在
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.USER_NOT_ACCOUNT).toJSONString();
			}
			
			switch(actionType){
				//购买俱乐部运营权
				case GameConstants.BUY_CLUB_RIGHT_ONE:
					//
					//判断俱乐部运营权是否购买
					clubMain.setClubId(Integer.parseInt(clubId));
					clubMain=clubMainDao.selectClubMainEntity(clubMain);
					if(clubMain.getIsBuyClubVip().intValue()==GameConstants.CLUB_RIGHT_ONE){
						return  Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.BUY_CLUB_MATCH_IS_BUY).toJSONString();
					}
					//如果人数大于200人则不用花钱进行购买俱乐部权益
					if(clubMain.getMemberNum() >= BusinessConstant.PERSON_NUM){
						//修改该俱乐部(是否购买权益字段)为已购买
						getActionTypeOne(Integer.parseInt(clubId));
						break;
					}
					//判断当前俱乐部账户金额充足与否
					double clubRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_COR_NEED_FLEVEL_ACCOUNT").toString());
					//判断虚拟币是否足够
					if(clubRightFee > uMoney){
						LogUtil.error(this.getClass(), "decrCurrency", "创业宝数量不足，请进入【我的账务】购买");
						return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "创业宝数量不足，请进入【我的账务】购买").toJSONString();
					}else{
						getActionTypeOne(Integer.parseInt(clubId));
						String accountId= centerAccountService.decrCurrencyClubUser(Integer.parseInt(clubId), clubRightFee,GameConstants.STAIR_ONE,2);
						centerMoneyChangeService.addCenterMoneyChange(Integer.parseInt(accountId), clubRightFee, clubId,GameConstants.MONEY_CHANGE_LINK_TYPE_BUY,GameConstants.SPENDING);
					}
					
		        break;
		        //购买赛事运营权
				case GameConstants.BUY_CLUB_RIGHT_TWO:
					//判断赛事运营权是否购买
					clubMain.setClubId(Integer.parseInt(clubId));
					//clubMain.setCreateUserId(userId);
					clubMain=clubMainDao.selectClubMainEntity(clubMain);
					if(clubMain.getIsBuyCompetition().intValue()==GameConstants.CLUB_RIGHT_ONE){
						return  Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.BUY_CLUB_MATCH_IS_BUY).toJSONString();
					}
					//判断当前俱乐部账户金额充足与否
					double gameRightFee =Double.parseDouble(SystemConfig.getString("GAME_RIGHT_GEOR_NEED_FLEVEL_ACCOUNT").toString());
					
					//判断虚拟币是否足够
					if(gameRightFee > uMoney){
						LogUtil.error(this.getClass(), "decrCurrency", "创业宝数量不足，请进入【我的账务】购买");
						return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "创业宝数量不足，请进入【我的账务】购买").toJSONString();
					}else{
						getActionTypeTwo(Integer.parseInt(clubId));
						String accountId=centerAccountService.decrCurrencyClubUser(Integer.parseInt(clubId), gameRightFee, GameConstants.STAIR_ONE,2);
						centerMoneyChangeService.addCenterMoneyChange(Integer.parseInt(accountId), gameRightFee, clubId,GameConstants.MONEY_CHANGE_LINK_TYPE_BUY,GameConstants.SPENDING);
					}
		        break;
		        //购买官方课程包
				case GameConstants.BUY_CLUB_RIGHT_THREE:
					//判断权益是否已经购买
					ClubTrainingClass clubTrainingClass=new ClubTrainingClass();
					clubTrainingClass.setClassId(Integer.parseInt(classId));
					//clubTrainingClass.setCreateUserId(userId);
					ClubTrainingClass temp= clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
					//已经购买返回
					if(Common.null2Int(temp.getIsBuyOfficial())==GameConstants.CLUB_RIGHT_ONE){
						return  Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.BUY_CLUB_TRAN_IS_BUY).toJSONString();
					}
					//判断当前俱乐部账户金额充足与否
					double classFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
					if(classFee > uMoney){
						LogUtil.error(this.getClass(), "decrCurrency", "创业宝数量不足，请进入【我的账务】购买");
						return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "创业宝数量不足，请进入【我的账务】购买").toJSONString();
					}else{
						//购买权益
						int count= getActionTypeThree(classId,classType);
						//修改个人账户的金额
						String accountId=centerAccountService.decrCurrencyClubUser(userId, classFee, GameConstants.STAIR_ONE,1);
						//TODO:进入默认账户
						//修改现金记录表
						centerMoneyChangeService.addCenterMoneyChange(Integer.parseInt(accountId), classFee, String.valueOf(userId),GameConstants.MONEY_CHANGE_LINK_TYPE_BUY,GameConstants.SPENDING);
					
						//修改数量
						//更新课程数量
						ClubTrainingClass updateClubTrainingClass = new ClubTrainingClass();
						updateClubTrainingClass.setClassId(Integer.parseInt(classId));
						updateClubTrainingClass.setCourseNum(count);
						clubTrainingClassService.updateClubTrainingClassNumByKey(updateClubTrainingClass);
						//删除缓存
						//删除用户表缓存信息
						JedisCache.removeRedisVal(null, ClubTrainingClass.class.getSimpleName(), String.valueOf(classId));
						
					}
		        break;
			}
			LogUtil.info(this.getClass(), "buyClubRights", "购买俱乐部权益成功");
			return  Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,AppErrorCode.BUY_CLUB_NOT_MEMBER).toJSONString();
		}
	} 
	
	/**
	 *购买俱乐部运营权,购买赛事运营权
	 * @author  lijin
	 * @date    2016年6月28日 下午21:54:25
	 */
	public void getActionTypeOne(int clubId){
		//根据俱乐部id，跟新是否购买运营增值权限
		 ClubMain clubMain=new ClubMain();
		 clubMain.setClubId(clubId);
		 clubMain.setIsBuyClubVip(GameConstants.CLUB_RIGHT_ONE);
		 clubMainDao.updateClubMainByKey(clubMain);
		 JedisCache.delRedisVal( ClubMain.class.getSimpleName(), String.valueOf(clubId));
	}
	
	/**
	 *购买赛事运营权
	 * @author  lijin
	 * @date    2016年6月28日 下午21:54:25
	 */
	public void getActionTypeTwo(int clubId){
		//根据俱乐部id，跟新是否购买赛事运营权限
		 ClubMain clubMain=new ClubMain();
		 clubMain.setClubId(clubId);
		 clubMain.setIsBuyCompetition(GameConstants.CLUB_RIGHT_ONE);
		 clubMainDao.updateClubMainByKey(clubMain);
		 JedisCache.delRedisVal( ClubMain.class.getSimpleName(), String.valueOf(clubId));
	}
	
	/**
	 *购买官方课程包
	 * @author  lijin
	 * @date    2016年6月28日 下午21:54:25
	 */
	public int getActionTypeThree(String classId,String classType){
		
		//根据班级id，获取俱乐部培训班表中记录，修改是否购买官方课程标识为是;
		ClubTrainingClass clubTrainingClass=new ClubTrainingClass();
		clubTrainingClass.setClassId(Integer.parseInt(classId));
		clubTrainingClass.setIsBuyOfficial(GameConstants.CLUB_RIGHT_ONE);
		clubTrainingClassService.updateClubTrainingClassByKey(clubTrainingClass);
		 JedisCache.delRedisVal( ClubTrainingClass.class.getSimpleName(), classId);
	    //获取培训课程表，课程类型为官方课程的数据;
		
		 ClubTrainingCourse clubTrainingCourse=new ClubTrainingCourse();
		 clubTrainingCourse.setCourseType(1);
		 List<ClubTrainingCourse> courseList= clubTrainingCourseService.getClubTrainingCourseList(clubTrainingCourse);
		 //List<Integer> courses= clubTrainingClassService.getClassByCourseType(1);
	   //根据班级id和课程id，添加培训班级课程关系表;
		List<ClubRelClassCourse>  clubRelClassCourses=new ArrayList<ClubRelClassCourse>();
		
		//封装培训班课程关系表对象集合
		int count=0;
		for(ClubTrainingCourse tempClubTrainingCourse:courseList){
			ClubRelClassCourse clubRelClassCourse=new ClubRelClassCourse();
			clubRelClassCourse.setClassId(Integer.parseInt(classId));
			clubRelClassCourse.setCourseId(tempClubTrainingCourse.getCourseId());
			clubRelClassCourse.setCreateTime( Common.getCurrentCreateTime());
			clubRelClassCourse.setIsShow(4);//yy修改 默认显示
			clubRelClassCourse.setTotalStudyNum(0);
			clubRelClassCourses.add(clubRelClassCourse);
			count++;
		}
		//添加培训班级课程关系表
		clubRelClassCourseDao.insertClubRelClassCourses(clubRelClassCourses);
		
		return count;
	}
}
