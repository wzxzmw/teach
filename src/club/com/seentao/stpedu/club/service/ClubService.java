package com.seentao.stpedu.club.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.attention.service.CenterAttentionService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubMemberMood;
import com.seentao.stpedu.common.entity.ClubNotice;
import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterNewsService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterPrivateMessageService;
import com.seentao.stpedu.common.service.ClubJoinTrainingService;
import com.seentao.stpedu.common.service.ClubMemberMoodService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubNoticeService;
import com.seentao.stpedu.common.service.ClubRelRedPacketMemberService;
import com.seentao.stpedu.common.service.ClubRelRemindMemberService;
import com.seentao.stpedu.common.service.ClubRemindService;
import com.seentao.stpedu.common.service.ClubTopicService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseChapterService;
import com.seentao.stpedu.common.service.TeachCourseDiscussService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.user.service.BusinessUserService;
import com.seentao.stpedu.utils.HttpRequest;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;
import com.seentao.stpedu.votes.service.GetVotesService;

/** 
* @ClassName: ClubService 
* @Description: 俱乐部业务处理类
* @author liulin
* @date 2016年6月26日 下午8:44:04 
*/
@Service
public class ClubService {
	
	/** 
	* @Fields clubTopicService : 俱乐部话题服务
	*/ 
	@Autowired
	private ClubTopicService clubTopicService;
	
	/** 
	* @Fields clubRemindService : 俱乐部提醒服务
	*/ 
	@Autowired
	private ClubRemindService clubRemindService;
	
	/** 
	* @Fields clubRelRemindMemberService : 俱乐部提醒会员关系服务
	*/ 
	@Autowired
	private ClubRelRemindMemberService clubRelRemindMemberService;
	
	/** 
	* @Fields clubNoticeService : 俱乐部通知提醒
	*/ 
	@Autowired
	private ClubNoticeService clubNoticeService;
	
	/** 
	* @Fields clubMemberMoodService : 俱乐部会员心情服务
	*/ 
	@Autowired
	private ClubMemberMoodService clubMemberMoodService;
	
	/** 
	* @Fields clubMemberService : 俱乐部会员服务
	*/ 
	@Autowired
	private ClubMemberService clubMemberService;
	/** 
	* @Fields centerAttentionService : 关注服务
	*/ 
	@Autowired
	private CenterAttentionService centerAttentionService;
	/** 
	* @Fields centerLiveService : 动态服务
	*/ 
	@Autowired
	private CenterLiveService centerLiveService;
	/** 
	* @Fields centerLiveService : 企业服务
	*/ 
	@Autowired
	private CenterCompanyService centerCompanyService;
	
	/** 
	* @Fields centerNewsService : 消息服务
	*/ 
	@Autowired
	private CenterNewsService centerNewsService;
	
	/** 
	* @Fields clubRelRedPacketMemberService : 红包会员关系列表
	*/ 
	@Autowired
	private ClubRelRedPacketMemberService clubRelRedPacketMemberService;
	
	/** 
	* @Fields teachCourseDiscussService : 课程讨论服务
	*/ 
	@Autowired
	private TeachCourseDiscussService teachCourseDiscussService;
	/** 
	* @Fields teachQuestionService : 问题服务
	*/ 
	@Autowired
	private TeachQuestionService teachQuestionService;
	
	/** 
	* @Fields teachCourseChapterService : 课程章节服务
	*/ 
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	
	/** 
	* @Fields teachCourseCardService : 课程卡服务
	*/ 
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	/** 
	* @Fields teachRelCardCourseService : 课程卡课程关系服务
	*/ 
	@Autowired
	private ArenaGuessService arenaGuessService;
	/** 
	* @Fields centerPrivateMessageService : 私信服务
	*/ 
	@Autowired
	private CenterPrivateMessageService centerPrivateMessageService;
	/** 
	* @Fields centerNewsStatusService : 消息状态服务 
	*/ 
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;
	
	@Autowired
	private CenterAttentionDao centerAttentionDao;
	
	@Autowired
	private GetVotesService getVotesService;
	
	@Autowired
	private BusinessClubService businessClubService;
	/** 
	* @Title: getTopics 
	* @Description: 获取话题信息
	* @param userId		用户ID
	* @param memberId	人员ID
	* @param clubId		俱乐部id
	* @param topicId	话题id
	* @param startDate	查询开始时间的时间戳
	* @param endDate	查询结束时间的时间戳
	* @param searchWord	搜索词
	* @param start		起始数
	* @param limit		每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月28日 上午10:04:21
	*/
	public String getTopics(Integer userId, Integer memberId, Integer clubId, Integer topicId, Integer startDate, Integer endDate, String searchWord, Integer start, Integer limit, Integer inquireType) {
		if(inquireType == GameConstants.CLUB_TOPIC_TYPE_HOT){
			try{
				//分页查询话题
				QueryPage<ClubTopic> queryPage = clubTopicService.getHotClubTopic(clubId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("topics").toJSONString();
				}
				//获得话题ID列表
				List<ClubTopic> topics = queryPage.getList();
				//获得话题集
				JSONArray array = getTopicArray(topics);
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("topics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getTopics", "获取俱乐部最热话题失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
			}
		}else if(inquireType == GameConstants.CLUB_TOPIC_TYPE_NEW){
			try{
				//分页查询话题
				QueryPage<ClubTopic> queryPage = clubTopicService.getNewClubTopic(clubId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("topics").toJSONString();
				}
				List<ClubTopic> topics = queryPage.getList();
				JSONArray array = getTopicArray(topics);
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("topics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getTopics", "获取俱乐部最新话题失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
			}
		}else if(inquireType == GameConstants.CLUB_TOPIC_TYPE_MY){
			try{
				/*//获得当前用户信息
				String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
				CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
				//获得话题信息
				int clubID = -1;
				if(null != user){
					clubID = user.getClubId()==null?-1:user.getClubId();
				}*/
				String topicRedis = RedisComponent.findRedisObject(topicId, ClubTopic.class);
				ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
				//判断如果是此话题俱乐部的成员
				//if(clubID == topic.getClubId().intValue()){
					JSONArray array = new JSONArray();
					Integer createUserId = topic.getCreateUserId();
					String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
					CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
					//组装数据
					JSONObject obj = new JSONObject();
					if(null != user){
						obj.put("tCreaterName", user.getRealName());
						obj.put("tCreaterNickname", user.getNickName());
					}
					obj.put("topicId", topic.getTopicId());
					obj.put("tCreaterId", topic.getCreateUserId());
					obj.put("topicTitle", topic.getTitle());
					obj.put("topicContent", topic.getContent());
					obj.put("tCreateDate", topic.getCreateTime());
					obj.put("supportCount", topic.getPraiseNum());
					obj.put("commentCount", topic.getCommentNum());
					obj.put("isTop", topic.getIsTop());
					//移动端使用yy修改
					obj.put("topicContentForM", topic.getContent());
					JSONObject rich = RichTextUtil.parseRichText(topic.getContent());
					obj.put("links",rich.getJSONArray("links"));
					obj.put("imgs", rich.getJSONArray("imgs"));
					array.add(obj);
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", 1);
					backParam.put("allPage", 1);
					backParam.put("currentPage", 1);
					backParam.put("topics", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				/*}else{
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", 0);
					backParam.put("allPage", 0);
					backParam.put("currentPage", 0);
					backParam.put("topics", JSONArray.parse("[]"));
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}*/
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getTopics", "获取唯一一条话题信息失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
			}
		}else if(inquireType == GameConstants.CLUB_TOPIC_TYPE_INFO){
			try{
				//获得用户信息
				String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
				CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
				//判断如果是俱乐部成员
				if(user.getClubId().intValue()==clubId){
					Map<String, Object> paramMap = new HashMap<String, Object>();
					//获得会员信息
					ClubMember member = new ClubMember();
					member.setUserId(userId);
					member = clubMemberService.selectClubMember(member);
					//String memberRedis = RedisComponent.findRedisObject(userId, ClubMember.class);
					//ClubMember member = JSONObject.parseObject(memberRedis, ClubMember.class);
					int level = member.getLevel().intValue();
					//如是不是会长
					if(level != GameConstants.PRESIDENT){
						paramMap.put("createUserId", userId);
					}
					//判断是否传入了搜索词
					if(!StringUtil.isEmpty(searchWord)){
						paramMap.put("title", "%"+searchWord+"%");
					}
					if(startDate == null || endDate == null || startDate == 0 || endDate == 0){
						return  Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
					}
					if(startDate != -1 && endDate != -1){
						paramMap.put("startDate", startDate);
						paramMap.put("endDate", endDate);
					}
					paramMap.put("clubId", clubId);
					paramMap.put("isDelete", 0);
					//分页查询话题
					QueryPage<ClubTopic> queryPage = clubTopicService.getBackClubTopic(paramMap, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("topics").toJSONString();
					}
					List<ClubTopic> topics = queryPage.getList();
					//获得话题集合
					JSONArray array = getTopicArray(topics);
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", queryPage.getCount());
					backParam.put("allPage", queryPage.getAllPage());
					backParam.put("currentPage", queryPage.getCurrentPage());
					backParam.put("topics", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getTopics", "获取后台管理中的话题管理列表失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
			}
		}else if(inquireType == GameConstants.CENTER_TOPIC_TYPE_INFO){
			try{
				//分页查询话题
				QueryPage<ClubTopic> queryPage = clubTopicService.getCenterClubTopic(memberId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("topics").toJSONString();
				}
				List<ClubTopic> topics = queryPage.getList();
				JSONArray array = getTopicArray(topics);
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("topics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getTopics", "获取台管理中的话题管理列表失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TOPICS).toJSONString();
			}
		}
		return null;
	}

	/** 
	* @Title: getTopicArray 
	* @Description: 组装话题数据
	* @param topics
	* @return JSONArray
	* @author liulin
	* @date 2016年6月28日 下午4:43:25
	*/
	private JSONArray getTopicArray(List<ClubTopic> topics) {
		//返回的话题集合
		JSONArray array = new JSONArray();
		for(ClubTopic topic : topics){
			JSONObject obj = new JSONObject();
			Integer tId = topic.getTopicId();
			String topicRedis = RedisComponent.findRedisObject(tId, ClubTopic.class);
			topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
			String userRedis =  RedisComponent.findRedisObject(topic.getCreateUserId(), CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//组装数据
			obj.put("topicId", topic.getTopicId());
			obj.put("tCreaterId", topic.getCreateUserId());
			obj.put("tCreaterName", user.getRealName());
			obj.put("tCreaterNickname", user.getNickName());
			obj.put("topicTitle", topic.getTitle());
			obj.put("topicContent", topic.getContent());
			obj.put("tCreateDate", topic.getCreateTime());
			obj.put("supportCount", topic.getPraiseNum());
			obj.put("commentCount", topic.getCommentNum());
			obj.put("isTop", topic.getIsTop());
			obj.put("topicContentFilterHtml", RichTextUtil.delHTMLTag(topic.getContent()));
			JSONArray nullArray = new JSONArray();
			obj.put("links",nullArray);
			obj.put("imgs", nullArray);
			array.add(obj);
		}
		return array;
	}


	/** 
	* @Title: addTopic 
	* @Description: 添加话题
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param topicTitle	话题标题
	* @param topicContent	话题内容
	* @param isTop	是否置顶
	* @return String
	* @author liulin
	* @date 2016年6月28日 下午5:43:32
	*/
	public String addTopic(Integer userId, Integer clubId, String topicTitle, String topicContent, Integer isTop) {
		try{
			//获得用户信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//判断此用户如果是此俱乐部的成员
			if(user.getClubId().intValue() == clubId){
				if(isTop == 1){
					ClubTopic oldTopic = new ClubTopic();
					oldTopic.setIsTop(isTop);
					oldTopic.setClubId(clubId);
					oldTopic.setIsDelete(0);
					oldTopic = clubTopicService.selectSingleClubTopic(oldTopic);
					if(oldTopic != null){
						ClubTopic newTopic = new ClubTopic();
						newTopic.setTopicId(oldTopic.getTopicId());
						newTopic.setIsTop(0);
						clubTopicService.updateClubTopicByKey(newTopic);
						JedisCache.delRedisVal(ClubTopic.class.getSimpleName(), oldTopic.getTopicId().toString());
					}
				}
				//添回话题并返回ID
				Integer topicid = clubTopicService.addTopic(userId, clubId, topicTitle, topicContent, isTop);
				//添加俱乐部动态
				centerLiveService.addTopicClubCenterLive(topicid, clubId, isTop);
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}else{
				LogUtil.error(this.getClass(), "addTopic", "您没有发话题的权限");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_TOPIC_PERN).toJSONString();
			}
			
		}catch(Exception e){
			LogUtil.error(this.getClass(), "addTopic", "发布话题失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_TOPIC).toJSONString();
		}
	}

	/** 
	* @Title: getReminds 
	* @Description: 获取提醒 
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param remindShowType	提醒的显示分类
	* @param start	起始数
	* @param limit	每页数量
	* @return String
	* @author liulin
	* @date 2016年6月28日 下午9:04:02
	*/
	public String getReminds(Integer userId, Integer clubId, Integer remindShowType, Integer start, Integer limit) {
		try{
			//获得用户信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			if(user.getClubId() == null){
				return Common.getReturn(AppErrorCode.SUCCESS, " ", "[]").toJSONString();
			}
			//判断此用户是此俱乐部的成员
			if(user.getClubId().intValue() == clubId){
				//大家的提醒
				if(remindShowType == GameConstants.CLUB_REMIND_TYPE_EVERYONE){
					//分页关联查询提醒表及提醒会员关系表
					QueryPage<ClubRelRemindMember> queryPage = clubRemindService.getEveryOneClubReminds(userId, clubId, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("reminds").toJSONString();
					}
					//获得提醒关系信息列表
					List<ClubRelRemindMember> clubRelRemindMembers = queryPage.getList();
					JSONArray array = getRemindArray(clubRelRemindMembers);
					//获得此会员信息
					ClubMember oldMember = new ClubMember();
					oldMember.setClubId(clubId);
					oldMember.setUserId(userId);
					oldMember = clubMemberService.queryClubMemberByClubIdAndUserId(userId,clubId);
					//修改会员没有新提醒并删除缓存
					if(null != oldMember){
						ClubMember newMember = new ClubMember();
						newMember.setMemberId(oldMember.getMemberId());
						newMember.setIsNewRemind(0);
						clubMemberService.updateClubMemberByKey(newMember);
						JedisCache.delRedisVal(ClubMember.class.getSimpleName(), newMember.getMemberId().toString());
					}
					Map<String, Object> backParam = getBackParam(queryPage);
					backParam.put("reminds", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}else if(remindShowType == GameConstants.CLUB_REMIND_TYPE_MY){
				//提醒我的
					//分页关联查询提醒表及提醒会员关系表
					QueryPage<ClubRelRemindMember> queryPage = clubRemindService.getMyClubReminds(userId, clubId, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("reminds").toJSONString();
					}
					//获得提醒关系信息列表
					List<ClubRelRemindMember> clubRelRemindMembers = queryPage.getList();
					JSONArray array = getRemindArray(clubRelRemindMembers);
					Map<String, Object> backParam = getBackParam(queryPage);
					backParam.put("reminds", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}else if(remindShowType == GameConstants.CLUB_REMIND_TYPE_ME){
				//我的提醒
					//分页关联查询提醒表及提醒会员关系表
					QueryPage<ClubRelRemindMember> queryPage = clubRemindService.getMeClubReminds(userId, clubId, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("reminds").toJSONString();
					}
					//获得提醒关系信息列表
					List<ClubRelRemindMember> clubRelRemindMembers = queryPage.getList();
					JSONArray array = getRemindArray(clubRelRemindMembers);
					Map<String, Object> backParam = getBackParam(queryPage);
					backParam.put("reminds", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "getReminds", "获取提醒失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_REMIND).toJSONString();
		}
		return null;
	}

	/** 
	* @Title: getBackParam 
	* @Description: 组装返回参数数据
	* @param queryPage
	* @return Map<String,Object>
	* @author liulin
	* @date 2016年6月29日 上午11:44:42
	*/
	private Map<String, Object> getBackParam(QueryPage<ClubRelRemindMember> queryPage) {
		Map<String, Object> backParam = new HashMap<String, Object>();
		backParam.put("returnCount", queryPage.getCount());
		backParam.put("allPage", queryPage.getAllPage());
		backParam.put("currentPage", queryPage.getCurrentPage());
		return backParam;
	}

	/** 
	* @Title: getRemindArray 
	* @Description: 组装提醒数据
	* @param clubRelRemindMembers
	* @return  参数说明 
	* @return JSONArray
	* @author liulin
	* @date 2016年6月29日 上午11:41:06
	*/
	private JSONArray getRemindArray(List<ClubRelRemindMember> clubRelRemindMembers) {
		String userRedis;
		CenterUser user;
		//返回对象
		JSONArray array = new JSONArray();
		for(ClubRelRemindMember member : clubRelRemindMembers){
			//组装数据
			JSONObject json = new JSONObject();
			//获得提醒信息
			String remindRedis = RedisComponent.findRedisObject(member.getRemindId(), ClubRemind.class);
			ClubRemind remind = JSONObject.parseObject(remindRedis, ClubRemind.class);
			//获得提醒发布者信息
			userRedis = RedisComponent.findRedisObject(remind.getCreateUserId(), CenterUser.class);
			user = JSONObject.parseObject(userRedis,CenterUser.class);
			//获得被提醒者信息
			String userRedis1 = RedisComponent.findRedisObject(member.getUserId(), CenterUser.class);
			CenterUser user1 = JSONObject.parseObject(userRedis1, CenterUser.class);
			json.put("remindId", member.getRemindId());
			json.put("rCreaterId", remind.getCreateUserId());
			json.put("rCreaterName", user.getRealName());
			json.put("rCreaterNickname", user.getNickName());
			json.put("remindContent", remind.getContent());
			json.put("rCreateDate", remind.getCreateTime());
			json.put("rReceiverId", user1.getUserId());
			json.put("rReceiverName", user1.getRealName());
			json.put("rReceiverNickname", user1.getNickName());
			array.add(json);
		}
		return array;
	}

	/** 
	* @Title: addRemind 
	* @Description: 添加提醒
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param remindContent	提醒内容
	* @param remindObject	提醒对象
	* @param remindUserIds	提醒人员的id
	* @return String
	* @author liulin
	* @date 2016年6月29日 上午11:56:29
	*/
	public String addRemind(Integer userId, Integer clubId, String remindContent, Integer remindObject, String remindUserIds) {
		try{
			if(remindContent.equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_STR).toJSONString();
			}
			if(!Common.isValid(remindContent, 2, 70)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_STR).toJSONString();
			}
			//获得用户信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//判断此用户如果是此俱乐部的成员
			if(user.getClubId().intValue() == clubId){
				//添加提醒并返回提醒ID
				Integer remindId = clubRemindService.addRemind(userId, clubId, remindContent);
				//添加提醒会员关系并修改被提醒用户的会员有新提醒
				clubRelRemindMemberService.addRemindMember(remindId, userId, clubId, remindObject, remindUserIds);
				//生成俱乐部提醒动态
				centerLiveService.addRemindClubCenterLive(remindId, clubId);
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}else{
				LogUtil.error(this.getClass(), "addRemind", "您没有发布提醒的权限");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_REMIND_PERN).toJSONString();
			}
			
		}catch(Exception e){
			LogUtil.error(this.getClass(), "addRemind", "发布提醒失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_REMIND).toJSONString();
		}
	}

	/** 
	* @Title: getWebNotices 
	* @Description: 获取通知
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午4:53:26
	*/
	public String getWebNotices(Integer userId, Integer clubId, Integer start, Integer limit, Integer inquireType) {
		try{
			//获得用户信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//判断此用户是此俱乐部的成员
			if(user.getClubId().intValue() == clubId){
				//我的俱乐部主页，获取通知信息列表
				if(inquireType.intValue() == GameConstants.CLUB_NOTICE_TYPE_INFO){
				//根据俱乐部ID获得通知ID列表
					//分页关联查询提醒表及提醒会员关系表
					QueryPage<ClubNotice> queryPage = clubNoticeService.getClubNoticesInfo(userId, clubId, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("notices").toJSONString();
					}
					//获得俱乐部ID列表
					List<ClubNotice> notices = queryPage.getList();
					//返回的通知集合
					JSONArray array = new JSONArray();
					//获得此会员信息
					ClubMember oldMember = new ClubMember();
					oldMember.setClubId(clubId);
					oldMember.setUserId(userId);
					oldMember = clubMemberService.queryClubMemberByClubIdAndUserId(userId, clubId);
					//修改会员没有新通知并删除缓存
					if(null != oldMember){
						ClubMember newMember = new ClubMember();
						newMember.setMemberId(oldMember.getMemberId());
						newMember.setIsNewNotice(0);
						clubMemberService.updateClubMemberByKey(newMember);
						JedisCache.delRedisVal(ClubMember.class.getSimpleName(), newMember.getMemberId().toString());
					}
					for(ClubNotice notice : notices){
						//获得通知信息
						String noticeRedis = RedisComponent.findRedisObject(notice.getNoticeId(), ClubNotice.class);
						notice = JSONObject.parseObject(noticeRedis, ClubNotice.class);
						//根据通知的创建者ID获得用户信息
						userRedis = RedisComponent.findRedisObject(notice.getCreateUserId(), CenterUser.class);
						user = JSONObject.parseObject(userRedis, CenterUser.class);
						//组装数据
						JSONObject json = new JSONObject();
						json.put("noticeId", notice.getNoticeId());
						json.put("nCreaterId", notice.getCreateUserId());
						json.put("nCreaterName", user.getRealName());
						json.put("nCreaterNickname", user.getNickName());
						json.put("noticeTitle", notice.getTitle());
						json.put("noticeContent", notice.getContent());
						json.put("nCreateDate", notice.getCreateTime());
						json.put("isTop", notice.getIsTop());
						array.add(json);
					}
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", queryPage.getCount());
					backParam.put("allPage", queryPage.getAllPage());
					backParam.put("currentPage", queryPage.getCurrentPage());
					backParam.put("notices", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "getWebNotices", "获取通知失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_NOTICE).toJSONString();
		}
		return null;
	}

	/** 
	* @Title: addNotice 
	* @Description: 发送通知 
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param noticeTitle	通知标题
	* @param noticeContent	通知内容
	* @param isTop	是否置顶
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午7:20:39
	*/
	public String addNotice(Integer userId, Integer clubId, String noticeTitle, String noticeContent, Integer isTop) {
		try{
			//获得用户信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//判断此用户如果是此俱乐部的成员
			if(user.getClubId().intValue() == clubId){
				if(isTop == 1){
					ClubNotice oldNotice = new ClubNotice();
					oldNotice.setIsTop(isTop);
					oldNotice.setClubId(clubId);
					oldNotice.setIsDelete(0);
					oldNotice = clubNoticeService.getClubNotice(oldNotice);
					if(oldNotice != null){
						ClubNotice newNotice = new ClubNotice();
						newNotice.setNoticeId(oldNotice.getNoticeId());
						newNotice.setIsTop(0);
						clubNoticeService.updateClubNoticeByKey(newNotice);
						JedisCache.delRedisVal(ClubNotice.class.getSimpleName(), oldNotice.getNoticeId().toString());
					}
				}
				//添加通知并返回通知ID
				Integer noticeId = clubNoticeService.addNotice(userId, clubId, noticeTitle, noticeContent, isTop);
				//根据clubId获得除userId以外的所有会员的是否有新通知置为1.
				List<ClubMember> members = clubMemberService.selectClubMemberByClubId(userId, clubId);
				//获得会员ids
				String memberIds = "";
				//删除缓存
				for(ClubMember clubMember : members){
					memberIds = memberIds + clubMember.getMemberId() + ",";
					JedisCache.delRedisVal(ClubMember.class.getSimpleName(), String.valueOf(clubMember.getMemberId()));
				}
				//批量更改会员通知为有新通知1
				if(!StringUtil.isEmpty(memberIds)){
					memberIds = memberIds.substring(0, memberIds.length()-1);
					clubMemberService.updateIsNewNoticeByMemberIds(memberIds);
				}
				//clubMemberService.updateClubMemberByMemberId(members);
				//生成俱乐部通知动态
				centerLiveService.addNoticeClubCenterLive(noticeId, clubId, isTop);
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}else{
				LogUtil.error(this.getClass(), "addRemind", "您没有发通知的权限");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_NOTICE_PERN).toJSONString();
			}
			
		}catch(Exception e){
			LogUtil.error(this.getClass(), "addRemind", "发布通知失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_NOTICE).toJSONString();
		}
	}

	/** 
	* @Title: getMoods 
	* @Description: 获取心情
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param clubId	俱乐部ID
	* @param timeRange 查询的时间范围
	* @param start	起始数
	* @param limit	每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午9:39:53
	*/
	public String getMoods(Integer userId, Integer memberId, Integer clubId, Integer timeRange, Integer start,
			Integer limit, Integer inquireType) {
		//初始化时间
		TimeUtil.initTime(timeRange);
		int startTime = (int) (TimeUtil.getStartTime()/1000);
		int endTime = (int) (TimeUtil.getEndTime()/1000);
		if(inquireType == GameConstants.CLUB_MOOD_TYPE_EVERYONE){
			try{
				//获得用户信息
				String redisUser = RedisComponent.findRedisObject(userId, CenterUser.class);
				CenterUser centerUser = JSONObject.parseObject(redisUser, CenterUser.class);
				//判断此用户如果是此俱乐部的成员
				if(centerUser.getClubId().intValue() == clubId){
					//分页查询心情
					QueryPage<ClubMemberMood> queryPage = clubMemberMoodService.getEveryOneClubMemberMood(clubId, userId, startTime, endTime, start, limit);
					if(CollectionUtils.isEmpty(queryPage.getList())){
						return queryPage.getMessageJSONObject("moods").toJSONString();
					}
					//获得心情ID列表
					List<ClubMemberMood> moods = queryPage.getList();
					JSONArray array = getMoodArray(moods);
					Map<String, Object> backParam = getBackMoodParam(queryPage);
					backParam.put("moods", array);
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getMoods", "获取俱乐部大家的心情失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_MOOD).toJSONString();
			}
		}else if(inquireType == GameConstants.CLUB_MOOD_TYPE_MY){
			try{
				//分页查询心情
				QueryPage<ClubMemberMood> queryPage = clubMemberMoodService.getMyClubMemberMood(clubId, userId, startTime, endTime, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("moods").toJSONString();
				}
				//获得心情ID列表
				List<ClubMemberMood> moods = queryPage.getList();
				JSONArray array = getMoodArray(moods);
				Map<String, Object> backParam = getBackMoodParam(queryPage);
				backParam.put("moods", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getMoods", "获取俱乐部登录者的心情失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_MOOD).toJSONString();
			}
		}else if(inquireType == GameConstants.CLUB_MOOD_TYPE_ME){
			try{
				//分页查询心情
				QueryPage<ClubMemberMood> queryPage = clubMemberMoodService.getMeClubMemberMood(userId, memberId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("moods").toJSONString();
				}
				//获得心情ID列表
				List<ClubMemberMood> moods = queryPage.getList();
				JSONArray array = getMoodArray(moods);
				Map<String, Object> backParam = getBackMoodParam(queryPage);
				backParam.put("moods", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}catch(Exception e){
				LogUtil.error(this.getClass(), "getMoods", "获取俱乐部大家的心情失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_MOOD).toJSONString();
			}
		}
		return null;
	}

	/** 
	* @Title: getBackMoodParam 
	* @Description: 获得返回心情参数
	* @param queryPage	分页参数对象
	* @return Map<String,Object>
	* @author liulin
	* @date 2016年6月30日 下午2:58:10
	*/
	private Map<String, Object> getBackMoodParam(QueryPage<ClubMemberMood> queryPage) {
		Map<String, Object> backParam = new HashMap<String, Object>();
		backParam.put("returnCount", queryPage.getCount());
		backParam.put("allPage", queryPage.getAllPage());
		backParam.put("currentPage", queryPage.getCurrentPage());
		return backParam;
	}

	/** 
	* @Title: getMoodArray 
	* @Description: 组装数据
	* @param moods	组装的数据列表
	* @return JSONArray
	* @author liulin
	* @date 2016年6月30日 下午2:56:05
	*/
	private JSONArray getMoodArray(List<ClubMemberMood> moods) {
		//返回的心情集合
		JSONArray array = new JSONArray();
		for(ClubMemberMood mood : moods){
			//获得心情对象
			String moodRedis = RedisComponent.findRedisObject(mood.getMoodId(), ClubMemberMood.class);
			mood = JSONObject.parseObject(moodRedis, ClubMemberMood.class);
			//获得心情的发布者信息
			String userRedis = RedisComponent.findRedisObject(mood.getCreateUserId(), CenterUser.class);
			if(null != userRedis){
				CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
				//获得会员信息
				ClubMember member = new ClubMember();
				member.setUserId(user.getUserId());
				member = clubMemberService.selectClubMember(member);
				//组装数据
				JSONObject json = new JSONObject();
				String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				json.put("mCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
				json.put("moodId", mood.getMoodId());
				json.put("mCreaterId", mood.getCreateUserId());
				json.put("mCreaterName", user.getRealName());
				json.put("mCreaterNickname", user.getNickName());
				json.put("clubRole", member.getLevel());
				json.put("moodContent", mood.getContent());
				json.put("mCreateDate", mood.getCreateTime());
				json.put("supportCount", mood.getPraiseNum());
				json.put("againstCount", mood.getAgainstNum());
				array.add(json);
			}
		}
		return array;
	}

	/** 
	* @Title: getDynamics 
	* @Description: 获取动态
	* @param userId	用户ID
	* @param memberId	会员ID
	* @param classId	班级ID
	* @param classType	班级类型
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页显示数
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年7月4日 上午11:44:31
	*/
	public String getDynamics(Integer userId, Integer memberId, Integer classId, Integer classType, Integer clubId,
			Integer start, Integer limit, Integer inquireType) {
		try{
			if(inquireType == GameConstants.GET_ATTITUDE_ONE){
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getClubCenterLive(clubId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String topicRedis = null;	//俱乐部话题
				String arenaRedis = null; //俱乐部擂台
				String trainingClassRedis = null;	//俱乐部培训班
				String remindRedis = null;	//俱乐部提醒
				String noticeRedis = null;	//俱乐部通知
				String redPacketRedis = null;	//俱乐部红包
				String questionRedis = null;	//邀请答疑
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					if(liveType == 1){
						//获取话题
						topicRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTopic.class);
						if(null == topicRedis){
							continue;
						}
						ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
						//获取话题发布者
						String userRedis = RedisComponent.findRedisObject(topic.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonOne(live, json, topic, user);
					}else if(liveType == 2){//调用春磊的接口 TODO
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null != obj){
							Integer createUserId = obj.getInteger("creatorId");
							//获取平台比赛的创建人
							String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							constructorJsonTwo(live, obj, json, createUserId, user);
						}else{
							continue;
						}
					}}
					}else if(liveType == 3){
						//获取俱乐部培训
						trainingClassRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingClass.class);
						ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
						if(null == trainingClass){
							continue;
						}
						//获得俱乐部培训创建者
						String userRedis = RedisComponent.findRedisObject(trainingClass.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonThree(live, json, trainingClass, user);
					}else if(liveType == 4){
						//获取俱乐部提醒
						remindRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubRemind.class);
						ClubRemind remind = JSONObject.parseObject(remindRedis, ClubRemind.class);
						if(null == remind){
							continue;
						}
						//获取提醒关系信息
						List<ClubRelRemindMember> relReminds = clubRelRemindMemberService.getClubRelRemindMemberByRemindId(remind.getRemindId());
						String ids = "";	//接收者id
						String names = "";	//接收者名称
						String nicks = "";	//接收者昵称
						if(null != relReminds){
							for(ClubRelRemindMember relRemind : relReminds){
								ids = ids+relRemind.getUserId()+",";
								String receiverRedis = RedisComponent.findRedisObject(relRemind.getUserId(), CenterUser.class);
								CenterUser receiver = JSONObject.parseObject(receiverRedis, CenterUser.class);
								names = names+receiver.getRealName()+",";
								nicks = nicks+receiver.getNickName()+",";
							}
							ids = ids.substring(0,ids.length()-1);
							names = names.substring(0,names.length()-1);
							nicks = nicks.substring(0,nicks.length()-1);
						}
						//获取俱乐部提醒发布者
						String userRedis = RedisComponent.findRedisObject(remind.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonFour(live, json, remind, ids, names, nicks, user);
					}else if(liveType == 5){
						//获取通知
						noticeRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubNotice.class);
						ClubNotice notice = JSONObject.parseObject(noticeRedis, ClubNotice.class);
						if(null == notice){
							continue;
						}
						//获取通知发布者信息
						String userRedis = RedisComponent.findRedisObject(notice.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonFive(live, json, notice, user);
					}else if(liveType == 6){
						//获取俱乐部红包
						redPacketRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubRedPacket.class);
						ClubRedPacket  redPacket = JSONObject.parseObject(redPacketRedis, ClubRedPacket.class);
						if(null == redPacket){
							continue;
						}
						//获取俱乐部红包的创建者
						String userRedis = RedisComponent.findRedisObject(redPacket.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonSix(live, json, redPacket, user);
					}else if(liveType == 7){
						if(live.getLiveMainType() == 7){
							//获取俱乐部邀请答疑
							questionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingQuestion.class);
							if(null == questionRedis){
								continue;
							}
							ClubTrainingQuestion trainingQuestion = JSONObject.parseObject(questionRedis, ClubTrainingQuestion.class);
							//获取邀请答疑创建者
							String userRedis = RedisComponent.findRedisObject(trainingQuestion.getCreateUserId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							constructorJsonSeven(live, json, trainingQuestion, user);
						}else if(live.getLiveMainType() == 9){
							//获取班级邀请答疑
							questionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), TeachQuestion.class);
							if(null == questionRedis){
								continue;
							}
							TeachQuestion trainingQuestion = JSONObject.parseObject(questionRedis, TeachQuestion.class);
							//获取邀请答疑创建者
							String userRedis = RedisComponent.findRedisObject(trainingQuestion.getCreateUserId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							constructorJsonSeven1(live, json, trainingQuestion, user);
						}
						
					}
					array.add(json);
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_TWO){
				//获取人员信息
				String userRedis1 = RedisComponent.findRedisObject(memberId, CenterUser.class);
				//如果没有此用户返回空
				if(null == userRedis1){
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount",0);
					backParam.put("allPage", 0);
					backParam.put("currentPage", 0);
					backParam.put("dynamics", JSONArray.parseArray("[]"));
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
				CenterUser user1 = JSONObject.parseObject(userRedis1, CenterUser.class);
				//获得人员所在的班级ID,俱乐部ID
				Integer clubId1 = user1.getClubId();
				Integer classId1 = user1.getClassId();
				//定义参数map
				Map<String, Object> paramMap = new HashMap<String, Object>();
				if(clubId1 != null){
					paramMap.put("clubId", clubId1);
				}
				if(classId1 != null){
					paramMap.put("classId", classId1);
				}
				paramMap.put("userId", user1.getUserId());
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getMemberCenterLive(paramMap, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String topicRedis = null;	//俱乐部话题
				String arenaRedis = null; //俱乐部擂台
				String trainingClassRedis = null;	//俱乐部培训班
				String teachCourseDiscussRedis = null;	//教学通知
				String teachQuestionRedis = null;	//教学答颖
				String teacharenaRedis = null;	//教学实训
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					if(liveType == 1){
						ClubTopic topic = new ClubTopic();
						topic.setTopicId(live.getLiveMainId());
						topic = clubTopicService.selectSingleClubTopic(topic);
						if(null == topic){
							continue;
						}
						//获取话题发布者
						String userRedis = RedisComponent.findRedisObject(topic.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toTwoJsonOne(live, json, topic, user);
					}else if(liveType == 2){//调用春磊的接口 TODO
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null != obj){
							Integer createUserId = obj.getInteger("creatorId");
							//获取平台比赛的创建人
							String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							constructorJsonTwo(live, obj, json, createUserId, user);
						}else{
							continue;
						}
					}
					}
					}else if(liveType == 3){
						//获取俱乐部培训
						trainingClassRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingClass.class);
						if(null != trainingClassRedis){
							ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
							//获得俱乐部培训创建者
							String userRedis = RedisComponent.findRedisObject(trainingClass.getCreateUserId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							constructorJsonThree(live, json, trainingClass, user);
						}else{
							continue;
						}
					}else if(liveType == 8){
						//获取教学通知
						TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
						teachCourseDiscuss.setDiscussId(live.getLiveMainId());
						teachCourseDiscuss = teachCourseDiscussService.getTeachCourseDiscuss(teachCourseDiscuss);
						if(null != teachCourseDiscuss){
							//获取教学通知的创建者
							String userRedis = RedisComponent.findRedisObject(teachCourseDiscuss.getCreateUserId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							toTwoJsonEight(live, json, teachCourseDiscuss, user);
						}else{
							continue;
						}
						
					}else if(liveType == 9){
						//获取教学答疑
						TeachQuestion question = new TeachQuestion();
						question.setQuestionId(live.getLiveMainId());
						question = teachQuestionService.getTeachQuestion(question);
						if(null != question){
							//获取教学答疑创建者
							String userRedis = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							toTwoJsonNine(live, json, question, user);
						}else{
							continue;
						}
					}else if(liveType == 10){//春磊比赛接口
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null != obj){
							Integer createUserId = obj.getInteger("creatorId");
							//获取平台比赛的创建人
							String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							toTwoJsonTen(live, obj, json, createUserId, user);
						}else{
							continue;
						}
					}
					}
					}else if(liveType == 11){//TODO:温家鑫的接口
						//获得关注职位动态
						String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
						if(null == attentionRedis){
							continue;
						}
						CenterAttention attention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
						String dataStr = TimeUtil.getCurrentTime();
						String sign = MD5Utils.getMD5Str(dataStr);
						String param = "entIds="+attention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
						String companyRedis = HttpRequest.sendPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
						JSONObject companyPacket = JSONObject.parseObject(companyRedis);
						JSONArray companys = companyPacket.getJSONArray("ents");
						if(companys.size()>0){
							obj = companys.getJSONObject(0);
							if(null == obj){
								continue;
							}
							CenterCompany centerCompany = new CenterCompany();
							centerCompany.setCompanyId(obj.getString("companyId"));
							centerCompany = centerCompanyService.getCenterCompany(centerCompany);
							int supportCount = 0;
							if(centerCompany == null){
								supportCount = 0;
							}else{
								supportCount = centerCompany.getPraiseNum();
							}
							
							toFiveJsonEleven(live, obj, json, attention, supportCount , null);
						}else{
							continue;
						}
					}else if(liveType == 12){
						//获得关注职位动态
						String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
						if(null == attentionRedis){
							continue;
						}
						CenterAttention attention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
						//获取俱乐部信息
						String clubMainRedis = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), ClubMain.class);
						ClubMain main = JSONObject.parseObject(clubMainRedis, ClubMain.class);
						if(null == main){
							continue;
						}
						//获取俱乐部的创建者
						String userRedis = RedisComponent.findRedisObject(attention.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toFiveJsonTwelve(live, json, attention, main, user);
					}else if(liveType == 13){
						//获得关注职位动态
						String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
						if(null == attentionRedis){
							continue;
						}
						CenterAttention attention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
						//获取用户信息
						String centerUserRedis = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), CenterUser.class);
						if(null == centerUserRedis){
							continue;
						}
						CenterUser user = JSONObject.parseObject(centerUserRedis, CenterUser.class);
						toFiveJsonThirteen(live, json, attention, user);
					}else if(liveType == 14){
						//获取俱乐部信息
						String clubRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubMain.class);
						ClubMain main = JSONObject.parseObject(clubRedis, ClubMain.class);
						if(null == main){
							continue;
						}
						//获取俱乐部的创建者
						String userRedis = RedisComponent.findRedisObject(main.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toFiveJsonFourteen(live, json, main, user);
					}else if(liveType == 15){//调用春磊的接口 TODO
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null == obj){
							continue;
						}
						Integer createUserId = obj.getInteger("creatorId");
						//获取平台比赛的创建人
						String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						constructorJsonTwo(live, obj, json, createUserId, user);
					}
					}
					array.add(json);
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_THREE ){
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getClassCenterLive(classId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String teachCourseDiscussRedis = null;	//教学通知
				String teachQuestionRedis = null;	//教学答颖
				String arenaRedis = null;	//教学实训
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					if(liveType == 8){
						//获取教学通知
						teachCourseDiscussRedis = RedisComponent.findRedisObject(live.getLiveMainId(), TeachCourseDiscuss.class);
						TeachCourseDiscuss teachCourseDiscuss = JSONObject.parseObject(teachCourseDiscussRedis, TeachCourseDiscuss.class);
						if(null == teachCourseDiscuss){
							continue;
						}
						//获取教学通知的创建者
						String userRedis = RedisComponent.findRedisObject(teachCourseDiscuss.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toTwoJsonEight(live, json, teachCourseDiscuss, user);
					}else if(liveType == 9){
						//获取教学答疑
						teachQuestionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), TeachQuestion.class);
						TeachQuestion question = JSONObject.parseObject(teachQuestionRedis, TeachQuestion.class);
						if(null == question){
							continue;
						}
						//获取教学答疑创建者
						String userRedis = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toThreeJsonNine(live, json, question, user);
					}else if(liveType == 10){//春磊比赛接口
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null == obj){
							continue;
						}
						Integer createUserId = obj.getInteger("creatorId");
						//获取平台比赛的创建人
						String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						toTwoJsonTen(live, obj, json, createUserId, user);
					}
					}
					}
					array.add(json);
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_FOUR){
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getLiveClubCenterLive(clubId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String topicRedis = null;	//俱乐部话题
				String arenaRedis = null; //俱乐部擂台
				String trainingClassRedis = null;	//俱乐部培训班
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					if(liveType == 1){
						//获取话题
						topicRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTopic.class);
						if(null == topicRedis){
							continue;
						}
						ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
						//获取话题发布者
						String userRedis = RedisComponent.findRedisObject(topic.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						if(user != null){
							toFourJsonOne(live, json, topic, user);
						}
					}else if(liveType == 2){//调用春磊的接口 TODO
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null == obj){
							continue;
						}
						Integer createUserId = obj.getInteger("creatorId");
						//获取平台比赛的创建人
						String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						if(user != null){
							constructorJsonTwo(live, obj, json, createUserId, user);
						}
					}
					}
					}else if(liveType == 3){
						//获取俱乐部培训
						trainingClassRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingClass.class);
						ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
						if(null == trainingClass){
							continue;
						}
						//获得俱乐部培训创建者
						String userRedis = RedisComponent.findRedisObject(trainingClass.getCreateUserId(), CenterUser.class);
						CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
						if(user != null){
							constructorJsonThree(live, json, trainingClass, user);
						}
					}
					if(!json.isEmpty()){
						array.add(json);
					}
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_FIVE){
				//获取关注的用户ID列表
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(userId);
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_USER);
				List<CenterAttention> attentions = centerAttentionService.getCenterAttentionByCreateUserId(attention);
				if(null == attentions || attentions.size() < 1){
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", 0);
					backParam.put("allPage", 0);
					backParam.put("currentPage", 0);
					backParam.put("dynamics", JSONArray.parseArray("[]"));
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
				//获得关注人的id,多个以逗号分割(1,2,3,4)
				String ids = "";
				for(CenterAttention can : attentions){
					ids = ids+can.getRelObjetId()+",";
				}
				ids = ids.substring(0, ids.length()-1);
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getAttentionUserCenterLive(ids, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String companyRedis = null;	//用户关注职业动态
				String clubMainRedis = null; //用户关注俱乐部动态
				String centerUserRedis = null;	//用户关注人动态
				String clubRedis = null;	//用户加入俱乐部动态
				String arenaRedis = null;	//用户参加比赛动态
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
						if(liveType == 11){//TODO:温家鑫的接口
							if(null == attentionRedis){
								continue;
							}
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							String dataStr = TimeUtil.getCurrentTime();
							String sign = MD5Utils.getMD5Str(dataStr);
							String param = "entIds="+centerAttention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
							JSONObject companyPacket = BusinessUserService.getCompanyPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
							JSONArray companys = companyPacket.getJSONArray("ents");
							if(companys.size()>0){
								obj = companys.getJSONObject(0);
								CenterCompany centerCompany = new CenterCompany();
								centerCompany.setCompanyId(obj.getString("companyId"));
								centerCompany = centerCompanyService.getCenterCompany(centerCompany);
								int supportCount = 0;
								if(centerCompany == null){
									supportCount = 0;
								}else{
									supportCount = centerCompany.getPraiseNum();
								}
								//根据用户id和企业id查询关注表判断当前登录用户是否关注了该企业
								CenterAttention centerAttentions = new CenterAttention();
								centerAttentions.setAtteId(live.getLiveMainId());
								centerAttentions = centerAttentionDao.selectSingleCenterAttention(centerAttentions);
								if(centerAttentions != null){
									centerAttentions.setAtteId(null);
									centerAttentions.setCreateTime(null);
									centerAttentions.setRelObjetId(centerAttentions.getRelObjetId());
									centerAttentions.setRelObjetType(3);
									centerAttentions.setCreateUserId(userId);
									centerAttentions = centerAttentionDao.selectSingleCenterAttention(centerAttentions);

								}
								Integer isAttention = null;
								if(null == centerAttentions){
									isAttention = 0;
								}else {
									isAttention = 1;
								}
								toFiveJsonEleven(live, obj, json, centerAttention, supportCount , isAttention);
							}else{
								continue;
							}
						}else if(liveType == 12){
							if(null == attentionRedis){
								continue;
							}
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							//获取俱乐部信息
							clubMainRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), ClubMain.class);
							ClubMain main = JSONObject.parseObject(clubMainRedis, ClubMain.class);
							if(null == main){
								continue;
							}
							CenterUser user = null;
							if(centerAttention!=null && centerAttention.getRelObjetId()!=null){
								String userRedis = RedisComponent.findRedisObject(Integer.valueOf(centerAttention.getRelObjetId()), CenterUser.class);
								user = JSONObject.parseObject(userRedis, CenterUser.class);
								//获取俱乐部的创建者
								if(user != null){
									toFiveJsonTwelve(live, json, centerAttention, main, user);
								}
							}
						}else if(liveType == 13){
							if(null == attentionRedis){
								continue;
							}
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							//获取用户信息
							centerUserRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), CenterUser.class);
							if(null == centerUserRedis){
								continue;
							}
							CenterUser user = JSONObject.parseObject(centerUserRedis, CenterUser.class);
							if(user != null){
								toFiveJsonThirteen(live, json, centerAttention, user);
							}
						}else if(liveType == 14){
							//获取俱乐部信息
							clubRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubMain.class);
							ClubMain main = JSONObject.parseObject(clubRedis, ClubMain.class);
							if(null == main){
								continue;
							}
							//获取俱乐部的创建者
							String userRedis = RedisComponent.findRedisObject(live.getMainObjetId(), CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							if(user != null)
							toFiveJsonFourteen(live, json, main, user);
						}else if(liveType == 15){//调用春磊的接口 TODO
							IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
							arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
							if(!StringUtil.isEmpty(arenaRedis)){
								JSONObject gameJson = JSONObject.parseObject(arenaRedis);
								arenaRedis = gameJson.getString("result");
							obj = JSONObject.parseObject(arenaRedis);
							if(null == obj){
								continue;
							}
							Integer createUserId = obj.getInteger("creatorId");
							//获取平台比赛的创建人
							String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
							CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
							if(user != null)
							constructorJsonTwo(live, obj, json, createUserId, user);
						}
						}
					
				if(!json.isEmpty()){
					array.add(json);
				}
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_SIX){
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getClubCenterLive(clubId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				//获取俱乐部
				String clubRedis = RedisComponent.findRedisObject(clubId, ClubMain.class);
				ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
				String clubName = clubMain.getClubName();
				List<CenterLive> lives = queryPage.getList();
				String topicRedis = null;	//俱乐部话题
				String arenaRedis = null; //俱乐部擂台
				String trainingClassRedis = null;	//俱乐部培训班
				String remindRedis = null;	//俱乐部提醒
				String noticeRedis = null;	//俱乐部通知
				String redPacketRedis = null;	//俱乐部红包
				String questionRedis = null;	//邀请答疑
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = new JSONObject();
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					if(liveType == 1){
						ClubTopic topic = null;
						CenterUser user = null;
						//获取话题
						topicRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTopic.class);
						topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
						if(topic!=null){
							String userRedis = RedisComponent.findRedisObject(topic.getCreateUserId(), CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
							//获取话题发布者
							toSixJsonOne(live, json, topic, user, clubName, null);
						}
					}else if(liveType == 2){//调用春磊的接口 TODO
						Integer createUserId = 0;
						CenterUser user = null;
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
							arenaRedis = gameJson.getString("result");
						obj = JSONObject.parseObject(arenaRedis);
						if(null != obj){
							createUserId = obj.getInteger("creatorId");
							String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
							//Integer createUserId = obj.getInteger("creatorId");
							//获取平台比赛的创建人
							toSixJsonTwo(live, obj, json, createUserId, user, clubName, null);
						}
					}}
					}else if(liveType == 3){
						ClubTrainingClass trainingClass = null;
						String userRedis = null;
						CenterUser user = null;
						//获取俱乐部培训
						trainingClassRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingClass.class);
						trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
						if(null!=trainingClass){
							userRedis = RedisComponent.findRedisObject(trainingClass.getCreateUserId(), CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
							//获得俱乐部培训创建者
							userRedis = toSixJsonThree(userId, live, json, trainingClass, user, clubName, null);
						}
					}else if(liveType == 4){
						ClubRemind remind = null;
						List<ClubRelRemindMember> relReminds = null;
						//获取提醒关系信息
						String ids = "";	//接收者id
						String names = "";	//接收者名称
						String nicks = "";	//接收者昵称
						CenterUser user = null;
						//获取俱乐部提醒
						remindRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubRemind.class);
						remind = JSONObject.parseObject(remindRedis, ClubRemind.class);
						if(null!=remind){
							relReminds = clubRelRemindMemberService.getClubRelRemindMemberByRemindId(remind.getRemindId());
							//获取俱乐部提醒发布者
							String userRedis = RedisComponent.findRedisObject(remind.getCreateUserId(), CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
						}
						if(null != relReminds){
							for(ClubRelRemindMember relRemind : relReminds){
								ids = ids+relRemind.getUserId()+",";
								String receiverRedis = RedisComponent.findRedisObject(relRemind.getUserId(), CenterUser.class);
								CenterUser receiver = JSONObject.parseObject(receiverRedis, CenterUser.class);
								names = names+receiver.getRealName()+",";
								nicks = nicks+receiver.getNickName()+",";
							}
							ids = ids.substring(0,ids.length()-1);
							names = names.substring(0,names.length()-1);
							nicks = nicks.substring(0,nicks.length()-1);
							toSixJsonFour(live, json, remind, ids, names, nicks, user, clubName, null);
						}
					}else if(liveType == 5){
						CenterUser user = null;
						//获取通知
						noticeRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubNotice.class);
						ClubNotice notice = JSONObject.parseObject(noticeRedis, ClubNotice.class);
						if(null != notice){
							String userRedis = RedisComponent.findRedisObject(notice.getCreateUserId(), CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
							//获取通知发布者信息
							toSixJsonFive(live, json, notice, user, clubName, null);
						}
					}else if(liveType == 6){
						ClubRedPacket  redPacket = null;
						CenterUser user = null;
						//获取俱乐部红包
						redPacketRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubRedPacket.class);
						redPacket = JSONObject.parseObject(redPacketRedis, ClubRedPacket.class);
						if(null != redPacket){
							//获取俱乐部红包的创建者
							String userRedis = RedisComponent.findRedisObject(redPacket.getCreateUserId(), CenterUser.class);
							user = JSONObject.parseObject(userRedis, CenterUser.class);
							toSixJsonSix(live, json, redPacket, user, clubName, null);
						}
					}else if(liveType == 7){
						if(live.getLiveMainType() == 7){
							CenterUser user = null;
							String className = null;
							//获取邀请答疑
							questionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubTrainingQuestion.class);
							ClubTrainingQuestion trainingQuestion = JSONObject.parseObject(questionRedis, ClubTrainingQuestion.class);
							if(trainingQuestion!=null){
								//获取邀请答疑创建者
								String userRedis = RedisComponent.findRedisObject(trainingQuestion.getCreateUserId(), CenterUser.class);
								user = JSONObject.parseObject(userRedis, CenterUser.class);
								//获得班级
								String classRedis = RedisComponent.findRedisObject(trainingQuestion.getClassId(), TeachClass.class);
								if(null != classRedis){
									TeachClass teachClass = JSONObject.parseObject(classRedis, TeachClass.class);
									className = teachClass.getClassName();
								}
								toSixJsonSeven(live, json, trainingQuestion, user, null, className);
							}
						}else if(live.getLiveMainType() == 9){
							CenterUser user = null;
							String className = null;
							//获取邀请答疑
							questionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), TeachQuestion.class);
							TeachQuestion trainingQuestion = JSONObject.parseObject(questionRedis, TeachQuestion.class);
							if(null != trainingQuestion){
								//获取邀请答疑创建者
								String userRedis = RedisComponent.findRedisObject(trainingQuestion.getCreateUserId(), CenterUser.class);
								user = JSONObject.parseObject(userRedis, CenterUser.class);
								//获得班级
								String classRedis = RedisComponent.findRedisObject(trainingQuestion.getClassId(), TeachClass.class);
								
								if(null != classRedis){
									TeachClass teachClass = JSONObject.parseObject(classRedis, TeachClass.class);
									className = teachClass.getClassName();
								}
								toSixJsonSeven1(live, json, trainingQuestion, user, null, className);
							}
						}
					}
					if(!json.isEmpty()){
						array.add(json);
					}
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType == GameConstants.GET_ATTITUDE_SEVEN){
				//获取关注的用户ID列表
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(userId);
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_USER);
				List<CenterAttention> attentions = centerAttentionService.getCenterAttentionByCreateUserId(attention);
				if(null == attentions || attentions.size() < 1){
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", 0);
					backParam.put("allPage", 0);
					backParam.put("currentPage", 0);
					backParam.put("dynamics", JSONArray.parseArray("[]"));
					return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
				}
				//获得关注人的id,多个以逗号分割(1,2,3,4)
				String ids = "";
				for(CenterAttention can : attentions){
					ids = ids+can.getRelObjetId()+",";
				}
				ids = ids.substring(0, ids.length()-1);
				//分页查询俱乐部动态
				QueryPage<CenterLive> queryPage = centerLiveService.getAttentionUserCenterLive(ids, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("dynamics").toJSONString();
				}
				List<CenterLive> lives = queryPage.getList();
				String companyRedis = null;	//用户关注职业动态
				String clubMainRedis = null; //用户关注俱乐部动态
				String centerUserRedis = null;	//用户关注人动态
				String clubRedis = null;	//用户加入俱乐部动态
				String arenaRedis = null;	//用户参加比赛动态
				JSONArray array = new JSONArray();	//返回的集合
				for(CenterLive live : lives){
					JSONObject obj = null;
					JSONObject json = new JSONObject();
					String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
					live = JSONObject.parseObject(redisLive, CenterLive.class);
					int liveType = live.getLiveType().intValue();
					String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
						if(liveType == 11){//TODO:温家鑫的接口
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							int supportCount = 0;
							if(centerAttention!=null){
								obj = new JSONObject();
								String dataStr = TimeUtil.getCurrentTime();
								String sign = MD5Utils.getMD5Str(dataStr);
								String param = "entIds="+centerAttention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
								companyRedis = HttpRequest.sendPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
								JSONObject companyPacket = JSONObject.parseObject(companyRedis);
								JSONArray companys = companyPacket.getJSONArray("ents");
								if(companys.size()>0){
									obj = companys.getJSONObject(0);
									CenterCompany centerCompany = new CenterCompany();
									centerCompany.setCompanyId(obj.getString("companyId"));
									centerCompany = centerCompanyService.getCenterCompany(centerCompany);
									if(centerCompany == null){
										supportCount = 0;
									}else{
										supportCount = centerCompany.getPraiseNum();
									}
								}
							}
							//根据用户id和企业id查询关注表判断当前登录用户是否关注了该企业
							CenterAttention centerAttentions = new CenterAttention();
							centerAttentions.setAtteId(live.getLiveMainId());
							centerAttentions = centerAttentionDao.selectSingleCenterAttention(centerAttentions);
							if(centerAttentions != null){
								centerAttentions.setAtteId(null);
								centerAttentions.setCreateTime(null);
								centerAttentions.setRelObjetId(centerAttentions.getRelObjetId());
								centerAttentions.setRelObjetType(3);
								centerAttentions.setCreateUserId(userId);
								centerAttentions = centerAttentionDao.selectSingleCenterAttention(centerAttentions);
								Integer isAttention = null;
								if(null == centerAttentions){
									isAttention = 0;
								}else {
									isAttention = 1;
								}
								toSevenJsonEleven(live, obj, json, centerAttention, supportCount,isAttention);

							}
						}else if(liveType == 12){
							ClubMain main = null;
							CenterUser user = null;
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							if(centerAttention!=null){
								//获取俱乐部信息
								clubMainRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), ClubMain.class);
								main = JSONObject.parseObject(clubMainRedis, ClubMain.class);
								//获取关注的创建者
								String userRedis = RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class);
								user = JSONObject.parseObject(userRedis, CenterUser.class);
								toSevenJsonTwelve(userId, live, json, centerAttention, main, user);
							}
						}else if(liveType == 13){
							CenterUser user = null;
							CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
							if(centerAttention!=null){
								//获取用户信息
								centerUserRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), CenterUser.class);
								user = JSONObject.parseObject(centerUserRedis, CenterUser.class);
								toSevenJsonThirteen(live, json, centerAttention, user);
							}
						}else if(liveType == 14){
							CenterUser user = null;
							//获取俱乐部信息
							clubRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubMain.class);
							ClubMain main = JSONObject.parseObject(clubRedis, ClubMain.class);
							if(main!=null){
								//获取俱乐部的创建者
								String userRedis = RedisComponent.findRedisObject(main.getCreateUserId(), CenterUser.class);
								user = JSONObject.parseObject(userRedis, CenterUser.class);
								toSevenJsonFourteen(userId, live, json, main, user);
							}
						}else if(liveType == 15){//调用春磊的接口 TODO
							IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
							arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
							Integer createUserId = 0;
							CenterUser user = null;
							if(!StringUtil.isEmpty(arenaRedis)){
								JSONObject gameJson = JSONObject.parseObject(arenaRedis);
								arenaRedis = gameJson.getString("result");
								obj = JSONObject.parseObject(arenaRedis);
								if(null != obj){
									createUserId = obj.getInteger("creatorId");
									//获取平台比赛的创建人
									String userRedis = RedisComponent.findRedisObject(createUserId, CenterUser.class);
									user = JSONObject.parseObject(userRedis, CenterUser.class);
									toSixJsonTwo(live, obj, json, createUserId, user, null, null);
								}
							}
						}
						if(!json.isEmpty()){
							array.add(json);
						}
				}
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", queryPage.getCount());
				backParam.put("allPage", queryPage.getAllPage());
				backParam.put("currentPage", queryPage.getCurrentPage());
				backParam.put("dynamics", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "getDynamics", "获得动态失败", e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_ATTITUDE).toJSONString();
		}
		return null;
	}

	private void toFiveJsonEleven(CenterLive live, JSONObject obj, JSONObject json, CenterAttention attention, int supportCount ,Integer isAttention) {
		String userRedis = RedisComponent.findRedisObject(attention.getCreateUserId(), CenterUser.class);
		if(null != userRedis){
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
			json.put("dmCreaterId", user.getUserId());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", obj.getString("companyName"));
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", live.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", attention.getRelObjetId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", obj.getString("companyLogoLink") == null ?"" :Common.checkPic(obj.getString("companyLogoLink")) == true ? obj.getString("companyLogoLink")+ActiveUrl.HEAD_MAP:obj.getString("companyLogoLink"));
		json.put("postCount", obj.getInteger("postCount"));
		json.put("postTypeCount", obj.getInteger("postTypeCount"));
		json.put("hasBeenAttention", isAttention);
		json.put("supportCount", supportCount);
		json.put("companyDetailLink", obj.getString("companyDetailLink"));
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void toSevenJsonFourteen(Integer userId, CenterLive live, JSONObject json, ClubMain main, CenterUser user) {
		String userRedis;
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(main!=null){
			json.put("dmCreaterId", String.valueOf(main.getCreateUserId()));
			json.put("dynamicMainTitle", main.getClubName());
			json.put("dmCreateDate", main.getCreateTime());
			json.put("clubName", main.getClubName());
		}else{
			json.put("dmCreaterId", "");
			json.put("dynamicMainTitle","");
			json.put("dmCreateDate", "");
			json.put("clubName", "");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainContent", null);
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("className", null);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		if(main!=null && user!=null){
			//是否已加入俱乐部
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(main.getClubId());
			clubMember.setUserId(userId);
			clubMember.setMemberStatus(1);//已加入
			ClubMember resclubMember = clubMemberService.selectClubMember(clubMember);
			//是否已加入俱乐部
			if(resclubMember!=null){
				json.put("hasJoinClub", 1);
			}else{
				json.put("hasJoinClub", 0);
			}
		}else{
			json.put("hasJoinClub", 0);
		}
		json.put("gameType", 0);
	}

	private void toSevenJsonThirteen(CenterLive live, JSONObject json, CenterAttention centerAttention,
			CenterUser user) {
		if(null != user){
			json.put("dmReceiverId", String.valueOf(user.getUserId()));
			json.put("dmReceiverName", user.getRealName());
			json.put("dmReceiverNickname", user.getNickName());
			json.put("dmSupportCount", StringUtil.nullToInt(user.getPraiseNum()));
		}/*else{
			json.put("dmReceiverId", null);
			json.put("dmReceiverName", null);
			json.put("dmReceiverNickname", null);
			json.put("dmSupportCount", 0);
		}*/
		Integer userId = centerAttention == null ? 0 : centerAttention.getCreateUserId();
		String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
		if(null != userRedis){
			CenterUser createUser = JSONObject.parseObject(userRedis, CenterUser.class);
			json.put("dmCreaterId", String.valueOf(createUser.getUserId()));
			json.put("dmCreaterName", createUser.getRealName());
			json.put("dmCreaterNickname", createUser.getNickName());
			String msg = Common.getImgUrl(createUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dynamicMainTitle", null);
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", centerAttention==null?"":centerAttention.getCreateTime());
		json.put("dmCommentCount", 0);
		json.put("dmSupportCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", null);
		json.put("className", null);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toSevenJsonTwelve(Integer userId, CenterLive live, JSONObject json, CenterAttention centerAttention,
			ClubMain main, CenterUser user) {
		String userRedis;
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterId", String.valueOf(user.getUserId()));
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", main==null?"":main.getClubName());
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", centerAttention==null?"":centerAttention.getCreateTime());
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", main==null?0:main.getClubId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", main==null?"":main.getClubName());
		json.put("className", null);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		if(main!=null && user!=null){
			//是否已加入俱乐部
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(main.getClubId());
			clubMember.setUserId(userId);
			clubMember.setMemberStatus(1);//已加入
			ClubMember resclubMember = clubMemberService.selectClubMember(clubMember);
			//是否已加入俱乐部
			if(resclubMember!=null){
				json.put("hasJoinClub", 1);
			}else{
				json.put("hasJoinClub", 0);
			}
		}else{
			json.put("hasJoinClub", 0);
		}
		json.put("gameType", 0);
	}

	private void toSevenJsonEleven(CenterLive live, JSONObject obj, JSONObject json, CenterAttention centerAttention,
			int supportCount ,Integer isAttention) {
		String userRedis = null;
		if(centerAttention!=null){
			userRedis = RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class);
			json.put("dmCreateDate", centerAttention.getCreateTime());
			json.put("dynamicMainId", centerAttention.getRelObjetId());
		}else{
			json.put("dmCreateDate", "");
			json.put("dynamicMainId","");
		}
		if(null != userRedis){
			CenterUser createUser = JSONObject.parseObject(userRedis, CenterUser.class);
			json.put("dmCreaterId", String.valueOf(createUser.getUserId()));
			json.put("dmCreaterName", createUser.getRealName());
			json.put("dmCreaterNickname", createUser.getNickName());
			String msg = Common.getImgUrl(createUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}else{
			//获取平台比赛的创建人的头像
			String ms = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(ms)==true ? ms+ActiveUrl.HEAD_MAP:ms);
		}
		if(obj!=null){
			json.put("dynamicMainTitle", obj.getString("companyName"));
			json.put("companyLogoLink", obj.getString("companyLogoLink") == null ? "" :Common.checkPic(obj.getString("companyLogoLink")) == true ?obj.getString("companyLogoLink")+ActiveUrl.HEAD_MAP:obj.getString("companyLogoLink"));
			json.put("postCount", StringUtil.nullToInt(obj.getInteger("postCount")));
			json.put("postTypeCount", StringUtil.nullToInt(obj.getInteger("postTypeCount")));
			json.put("companyDetailLink", obj.getString("companyDetailLink"));
		}else{
			json.put("dynamicMainTitle", "");
			json.put("companyLogoLink", "");
			json.put("postCount", 0);
			json.put("postTypeCount", 0);
			json.put("companyDetailLink","");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainContent", null);
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("hasBeenAttention", isAttention);
		json.put("supportCount", supportCount);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", null);
		json.put("className", null);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toSixJsonSeven(CenterLive live, JSONObject json, ClubTrainingQuestion trainingQuestion,
			CenterUser user, String clubName, String className) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ?msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(trainingQuestion!=null){
			json.put("dynamicMainTitle", trainingQuestion.getTitle());
			json.put("dynamicMainContent", RichTextUtil.delHTMLTag(trainingQuestion.getContent()));
			json.put("dmCreateDate", trainingQuestion.getCreateTime());
			json.put("dmSupportCount", StringUtil.nullToInt(trainingQuestion.getPraiseNum()));
			json.put("dmCreaterId", String.valueOf(trainingQuestion.getCreateUserId()));
			json.put("classId", trainingQuestion.getClassId().toString());
		}else{
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent","");
			json.put("dmCreateDate", "");
			json.put("dmSupportCount", 0);
			json.put("dmCreaterId", "");
			json.put("classId","");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", GameConstants.CLASS_TYPE_CLUB);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}
	
	private void toSixJsonSeven1(CenterLive live, JSONObject json, TeachQuestion trainingQuestion,
			CenterUser user, String clubName, String className) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(trainingQuestion!=null){
			json.put("dynamicMainTitle", trainingQuestion.getTitle());
			json.put("dynamicMainContent", RichTextUtil.delHTMLTag(trainingQuestion.getContent()));
			json.put("dmCreateDate", trainingQuestion.getCreateTime());
			json.put("dmSupportCount", StringUtil.nullToInt(trainingQuestion.getPraiseNum()));
			json.put("dmCreaterId", String.valueOf(trainingQuestion.getCreateUserId()));
			json.put("classId", trainingQuestion.getClassId().toString());
		}else{
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent","");
			json.put("dmCreateDate",0);
			json.put("dmSupportCount", 0);
			json.put("dmCreaterId", "");
			json.put("classId", "");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toSixJsonSix(CenterLive live, JSONObject json, ClubRedPacket redPacket, CenterUser user, String clubName, String className) {
		if(null != user){
			//获取红包发布者头像	
			String msg =  Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg +ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		String ids = "";	//接收者id
		String names = "";	//接收者名称
		String nicks = "";	//接收者昵称
		if(redPacket!=null){
			//获取红包会员关系信息
			List<ClubRelRedPacketMember> relRedPackets = clubRelRedPacketMemberService.getClubRelRemindMemberByRedPackId(redPacket.getRedPacketId());
			if(null != relRedPackets){
				for(ClubRelRedPacketMember relRedPacket : relRedPackets){
					ids = ids+relRedPacket.getUserId()+",";
					String receiverRedis = RedisComponent.findRedisObject(relRedPacket.getUserId(), CenterUser.class);
					CenterUser receiver = JSONObject.parseObject(receiverRedis, CenterUser.class);
					names = names+receiver.getRealName()+",";
					nicks = nicks+receiver.getNickName()+",";
				}
				ids = ids.substring(0,ids.length()-1);
				names = names.substring(0,names.length()-1);
				nicks = nicks.substring(0,nicks.length()-1);
			}
			json.put("dmCreaterId", String.valueOf(redPacket.getCreateUserId()));
			json.put("dmCreateDate", redPacket.getCreateTime());
		}else{
			json.put("dmCreaterId", "");
			json.put("dmCreateDate",0);
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", String.valueOf(redPacket.getCreateUserId()));
		json.put("dmReceiverId", ids);
		json.put("dmReceiverName", names);
		json.put("dmReceiverNickname", nicks);
		json.put("dynamicMainTitle", "发给："+nicks);
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", redPacket.getCreateTime());
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toSixJsonFive(CenterLive live, JSONObject json, ClubNotice notice, CenterUser user, String clubName, String className) {
		if(null != user){
			//获取通知发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) ==true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(notice!=null){
			json.put("dynamicMainTitle", notice.getTitle());
			json.put("dynamicMainContent", RichTextUtil.delHTMLTag(notice.getContent()));
			json.put("dmCreateDate", notice.getCreateTime());
			json.put("dmCreaterId", String.valueOf(notice.getCreateUserId()));
		}else{
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent", "");
			json.put("dmCreateDate",0);
			json.put("dmCreaterId", "");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toSixJsonFour(CenterLive live, JSONObject json, ClubRemind remind, String ids, String names,
			String nicks, CenterUser user, String clubName, String className) {
		if(null != user){
			//获取提醒发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) ==true ? msg+ActiveUrl.HEAD_MAP:msg);			
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(null!=remind){
			json.put("dynamicMainTitle", remind.getContent());
			json.put("dynamicMainContent", remind.getContent());
			json.put("dmCreateDate", remind.getCreateTime());
			json.put("dmCreaterId", String.valueOf(remind.getCreateUserId()));
		}else{
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent", "");
			json.put("dmCreateDate", 0);
			json.put("dmCreaterId", "");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", ids);
		json.put("dmReceiverName", names);
		json.put("dmReceiverNickname", nicks);
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private String toSixJsonThree(Integer userId, CenterLive live, JSONObject json, ClubTrainingClass trainingClass,
			CenterUser user, String clubName, String className) {
		String userRedis;
		if(null != user){
			//获取培训发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(trainingClass!=null){
			json.put("dmCreaterId", String.valueOf(trainingClass.getCreateUserId()));
			json.put("dynamicMainTitle", trainingClass.getTitle());
			json.put("dynamicMainContent", trainingClass.getIntroduce());
			json.put("dmCreateDate", trainingClass.getCreateTime());
			//yy(修改用户是否已加入俱乐部某个培训班)
			if(trainingClass.getClassId()!=null && userId!=null ){
				ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
				clubJoinTraining.setIsDelete(0);
				clubJoinTraining.setClassId(trainingClass.getClassId());
				clubJoinTraining.setUserId(userId);
				ClubJoinTraining res = clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
				if(null != res){
					json.put("hasJoinClass", 1);
				}else{
					json.put("hasJoinClass", 0);
				}
			}else{
				json.put("hasJoinClass", 0);
			}
		}else{
			json.put("dmCreaterId","");
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent", 0);
			json.put("dmCreateDate", 0);
			json.put("hasJoinClass", 0);
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", String.valueOf(trainingClass.getCreateUserId()));
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", trainingClass.getTitle());
		json.put("dynamicMainContent", trainingClass.getIntroduce());
		json.put("dmCreateDate", trainingClass.getCreateTime());
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
		return userRedis;
	}

	private void toSixJsonTwo(CenterLive live, JSONObject obj, JSONObject json, Integer createUserId, CenterUser user, String clubName, String className) {
		if(null != user){
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", String.valueOf(createUserId));
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", obj==null?"":obj.getString("matchName"));
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", obj==null?"":obj.getInteger("createTime"));
		json.put("dmSupportCount", 0);
		json.put("dmCommentCount", 0);
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("companyLogoLink", null);
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		int matchType = obj==null?0:obj.getInteger("matchType");
		if(matchType == 3){
			matchType = 4;
		}else if(matchType == 4){
			matchType = 3;
		}
		json.put("gameType", matchType);
	}

	private void toSixJsonOne(CenterLive live, JSONObject json, ClubTopic topic, CenterUser user, String clubName, String className) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			//获取话题发布者头像
			String msg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
		}
		if(topic!=null){
			json.put("dmCreaterId", String.valueOf(topic.getCreateUserId()));
			json.put("dynamicMainTitle", topic.getTitle());
			json.put("dynamicMainContent", RichTextUtil.delHTMLTag(topic.getContent()));
			json.put("dmCreateDate", topic.getCreateTime());
			json.put("dmSupportCount", StringUtil.nullToInt(topic.getPraiseNum()));
			json.put("dmCommentCount", StringUtil.nullToInt(topic.getCommentNum()));
			json.put("clubId", String.valueOf(topic.getClubId()));
		}else{
			json.put("dmCreaterId", "");
			json.put("dynamicMainTitle", "");
			json.put("dynamicMainContent", "");
			json.put("dmCreateDate", 0);
			json.put("dmSupportCount", 0);
			json.put("dmCommentCount", 0);
			json.put("clubId", "");
		}
		json.put("dynamicId", String.valueOf(live.getLiveId()));
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dynamicMainId", String.valueOf(live.getLiveMainId()));
		json.put("isTop", StringUtil.nullToInt(live.getIsTop()));
		json.put("postCount", 0);
		json.put("postTypeCount", 0);
		json.put("hasBeenAttention", 0);
		json.put("supportCount", 0);
		json.put("companyDetailLink", null);
		json.put("classId", null);
		json.put("clubName", clubName);
		json.put("className", className);
		json.put("classType", 0);
		json.put("hasJoinClass", 0);
		json.put("hasJoinClub", 0);
		json.put("gameType", 0);
	}

	private void toFiveJsonFourteen(CenterLive live, JSONObject json, ClubMain main, CenterUser user) {
		if(null != user){
			//获取俱乐部的创建者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)== true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterId", user.getUserId());
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", main.getClubName());
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", live.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void toFiveJsonThirteen(CenterLive live, JSONObject json, CenterAttention centerAttention,
			CenterUser user) {
		if(null != user){
			//获取俱乐部的创建者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)== true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmReceiverName", user.getRealName());
			json.put("dmReceiverNickname", user.getNickName());
			json.put("dynamicMainTitle", user.getNickName());
		}
		
		String userRedis = RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class);
		if(null != userRedis){
			CenterUser createUser = JSONObject.parseObject(userRedis, CenterUser.class);
			json.put("dmCreaterId", createUser.getUserId());
			json.put("dmCreaterName", createUser.getRealName());
			json.put("dmCreaterNickname", createUser.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", user.getUserId());
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", centerAttention.getCreateTime());
		json.put("dmSupportCount", user.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void toFiveJsonTwelve(CenterLive live, JSONObject json, CenterAttention centerAttention, ClubMain main,
			CenterUser user) {
		if(null != user){
			//获取俱乐部的创建者头像
			String msg  = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
			json.put("dmCreaterId", user.getUserId());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", main.getClubName());
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", centerAttention.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
//		json.put("dynamicMainId", live.getLiveMainId());
		json.put("dynamicMainId", main.getClubId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void toFourJsonOne(CenterLive live, JSONObject json, ClubTopic topic, CenterUser user) {
		if(null != user){
			//获取话题发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			json.put("dmCreaterName", null);
			json.put("dmCreaterNickname", null);
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", topic.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", topic.getTitle());
		json.put("dynamicMainContent", RichTextUtil.delHTMLTag(topic.getContent()));
		json.put("dmCreateDate", topic.getCreateTime());
		json.put("dmSupportCount", topic.getPraiseNum());
		json.put("dmCommentCount", topic.getCommentNum());
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", topic.getClubId().toString());
		json.put("classId", null);
		json.put("classType", null);
	}

	private void toThreeJsonNine(CenterLive live, JSONObject json, TeachQuestion question, CenterUser user) {
		if(null != user){
			//获取教学答疑发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg)== true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", question.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", question.getTitle());
		json.put("dynamicMainContent", question.getContent());
		json.put("dmCreateDate", question.getCreateTime());
		json.put("dmSupportCount", question.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", question.getClassId().toString());
		json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
	}

	private void toTwoJsonTen(CenterLive live, JSONObject obj, JSONObject json, Integer createUserId, CenterUser user) {
		if(null != user){
			//获取平台比赛的创建人的头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", createUserId);
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", obj.getString("matchName"));
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", obj.getInteger("createTime"));
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", obj.getString("classId"));
		json.put("classType", null);
	}

	private void toTwoJsonNine(CenterLive live, JSONObject json, TeachQuestion question, CenterUser user) {
		if(null != user){
			//获取教学答疑发布者头像
			String msg =Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink", Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", question.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", question.getTitle());
		json.put("dynamicMainContent", question.getContent());
		json.put("dmCreateDate", question.getCreateTime());
		json.put("dmSupportCount", question.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", question.getClassId().toString());
		json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
	}

	private void toTwoJsonEight(CenterLive live, JSONObject json, TeachCourseDiscuss teachCourseDiscuss,
			CenterUser user) {
		if(null != user){
			//获取教学通知发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", teachCourseDiscuss.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", teachCourseDiscuss.getContent());
		json.put("dynamicMainContent", teachCourseDiscuss.getContent());
		json.put("dmCreateDate", teachCourseDiscuss.getCreateTime());
		json.put("dmSupportCount", teachCourseDiscuss.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", teachCourseDiscuss.getClassId().toString());
		json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
	}

	private void toTwoJsonOne(CenterLive live, JSONObject json, ClubTopic topic, CenterUser user) {
		if(null != user){
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
			//获取话题发布者头像
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			
		}else{
			json.put("dmCreaterName", null);
			json.put("dmCreaterNickname", null);
		}
		
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", topic.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", topic.getTitle());
		json.put("dynamicMainContent", RichTextUtil.delHTMLTag(topic.getContent()));
		json.put("dmCreateDate", topic.getCreateTime());
		json.put("dmSupportCount", topic.getPraiseNum());
		json.put("dmCommentCount", topic.getCommentNum());
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", topic.getClubId().toString());
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonSeven(CenterLive live, JSONObject json, ClubTrainingQuestion trainingQuestion,
			CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			json.put("dmCreaterName", null);
			json.put("dmCreaterNickname", null);
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", trainingQuestion.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", trainingQuestion.getTitle());
		json.put("dynamicMainContent", trainingQuestion.getContent());
		json.put("dmCreateDate", trainingQuestion.getCreateTime());
		json.put("dmSupportCount", trainingQuestion.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", trainingQuestion.getClassId().toString());
		json.put("classType", GameConstants.CLASS_TYPE_CLUB);
	}
	private void constructorJsonSeven1(CenterLive live, JSONObject json, TeachQuestion trainingQuestion,
			CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			json.put("dmCreaterName", null);
			json.put("dmCreaterNickname", null);
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", trainingQuestion.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", trainingQuestion.getTitle());
		json.put("dynamicMainContent", trainingQuestion.getContent());
		json.put("dmCreateDate", trainingQuestion.getCreateTime());
		json.put("dmSupportCount", trainingQuestion.getPraiseNum());
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", trainingQuestion.getClassId().toString());
		json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
	}

	private void constructorJsonSix(CenterLive live, JSONObject json, ClubRedPacket redPacket, CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		
		//获取红包会员关系信息
		List<ClubRelRedPacketMember> relRedPackets = clubRelRedPacketMemberService.getClubRelRemindMemberByRedPackId(redPacket.getRedPacketId());
		String ids = "";	//接收者id
		String names = "";	//接收者名称
		String nicks = "";	//接收者昵称
		if(null != relRedPackets){
			for(ClubRelRedPacketMember relRedPacket : relRedPackets){
				ids = ids+relRedPacket.getUserId()+",";
				String receiverRedis = RedisComponent.findRedisObject(relRedPacket.getUserId(), CenterUser.class);
				CenterUser receiver = JSONObject.parseObject(receiverRedis, CenterUser.class);
				names = names+receiver.getRealName()+",";
				nicks = nicks+receiver.getNickName()+",";
			}
			ids = ids.substring(0,ids.length()-1);
			names = names.substring(0,names.length()-1);
			nicks = nicks.substring(0,nicks.length()-1);
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", redPacket.getCreateUserId());
		json.put("dmReceiverId", ids);
		json.put("dmReceiverName", names);
		json.put("dmReceiverNickname", nicks);
		json.put("dynamicMainTitle", null);
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", redPacket.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonFive(CenterLive live, JSONObject json, ClubNotice notice, CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", notice.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", notice.getTitle());
		json.put("dynamicMainContent", notice.getContent());
		json.put("dmCreateDate", notice.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonFour(CenterLive live, JSONObject json, ClubRemind remind, String ids, String names,
			String nicks, CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", remind.getCreateUserId());
		json.put("dmReceiverId", ids);
		json.put("dmReceiverName", names);
		json.put("dmReceiverNickname", nicks);
		json.put("dynamicMainTitle", null);
		json.put("dynamicMainContent", remind.getContent());
		json.put("dmCreateDate", remind.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonThree(CenterLive live, JSONObject json, ClubTrainingClass trainingClass,
			CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", trainingClass.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", trainingClass.getTitle());
		json.put("dynamicMainContent", trainingClass.getIntroduce());
		json.put("dmCreateDate", trainingClass.getCreateTime());
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonTwo(CenterLive live, JSONObject obj, JSONObject json, Integer createUserId,
			CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", createUserId);
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", obj.getString("matchName"));
		json.put("dynamicMainContent", null);
		json.put("dmCreateDate", obj.getInteger("createTime"));
		json.put("dmSupportCount", null);
		json.put("dmCommentCount", null);
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", null);
		json.put("classId", null);
		json.put("classType", null);
	}

	private void constructorJsonOne(CenterLive live, JSONObject json, ClubTopic topic, CenterUser user) {
		if(null != user){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}else{
			json.put("dmCreaterName", null);
			json.put("dmCreaterNickname", null);
		}
		json.put("dynamicId", live.getLiveId());
		json.put("dynamicType", live.getLiveType().toString());
		json.put("dmCreaterId", topic.getCreateUserId());
		json.put("dmReceiverId", null);
		json.put("dmReceiverName", null);
		json.put("dmReceiverNickname", null);
		json.put("dynamicMainTitle", topic.getTitle());
		//yy修改  过滤html标签
		json.put("dynamicMainContent", RichTextUtil.delHTMLTag(topic.getContent()));
		json.put("dmCreateDate", topic.getCreateTime());
		json.put("dmSupportCount", topic.getPraiseNum());
		json.put("dmCommentCount", topic.getCommentNum());
		json.put("dynamicMainId", live.getLiveMainId());
		json.put("isTop", live.getIsTop());
		json.put("companyLogoLink", null);
		json.put("postCount", null);
		json.put("postTypeCount", null);
		json.put("hasBeenAttention", null);
		json.put("supportCount", null);
		json.put("companyDetailLink", null);
		json.put("clubId", topic.getClubId());
		json.put("classId", null);
		json.put("classType", null);
	}

	/** 
	* @Title: getDynamicEntryForMobile 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param userId
	* @return  参数说明 
	* @return String
	* @author liulin
	* @date 2016年7月24日 上午11:52:49
	*/
	public String getDynamicEntryForMobile(Integer userId) {
		try{
			JSONArray array = new JSONArray();
			//1获取最后一条消息ID
			CenterNews centerNews = centerNewsService.getCenterNews(userId);
			//获取该用户最新一条私信信息(不管是发送的还是接收的)---yy修改
			CenterPrivateMessage message = null;
			Map<String,Object> conditionMap = new HashMap<String,Object>();
			conditionMap.put("userId", userId);
			//调用分页组件userId
			QueryPage<CenterPrivateMessageTalk> appQueryPage = QueryPageComponent.queryPage(1, 0, conditionMap,CenterPrivateMessageTalk.class);
			List<CenterPrivateMessageTalk> centerPrivateMessageTalkList= appQueryPage.getList();
			if(null!=centerPrivateMessageTalkList && centerPrivateMessageTalkList.size()==1){
				CenterPrivateMessageTalk centerprivatemessagetalk = centerPrivateMessageTalkList.get(0);
				CenterPrivateMessage centerPrivateMessage = new CenterPrivateMessage();
				centerPrivateMessage.setPrivateMessageId(centerprivatemessagetalk.getLastPrivateMessageId());
				message = centerPrivateMessageService.getCenterPrivateMessage(centerPrivateMessage);
				centerPrivateMessage = null;
				conditionMap = null;
			}
			//获取用户消息状态
			CenterNewsStatus status = centerNewsStatusService.getCenterNewsStatusByUserId(userId);
			JSONObject json = null;
			if(centerNews != null){
				json = new JSONObject();
				//封装返回数据对象
				json.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_ONE);
				//获取最后一条消息
				String centerNewsRedis = RedisComponent.findRedisObject(centerNews.getNewsId(), CenterNews.class);
				centerNews = JSONObject.parseObject(centerNewsRedis, CenterNews.class);
				if(message != null){
					//获取私信对象
					String messageRedis = RedisComponent.findRedisObject(message.getPrivateMessageId(), CenterPrivateMessage.class);
					message = JSONObject.parseObject(messageRedis, CenterPrivateMessage.class);
					//获取消息和私信对象创建时间
					int messageTime = message.getCreateTime();
					int newsTime = centerNews.getCreateTime();
					if(newsTime >= messageTime){//最后一条为消息对象
						json.put("dynamicEntryTitle", centerNews.getContent());
						json.put("dynamicEntryMainId", null);
						json.put("chapterId", null);
						json.put("gameId", null);
						json.put("gameName", null);
						json.put("clubName", null);
						json.put("gameEventTitle", null);
						//消息类型。1:关注消息，2:邀请回答问题，3:邀请比赛，4：系统消息,5:私信
						if(null != status){
							int newsType = centerNews.getNewsType();
							if(newsType == 1){
								json.put("messageType", GameConstants.GET_MESSAGE_TWO);
								json.put("hasNewMsg", status.getIsNewAttention());
							}else if(newsType == 2){
								json.put("messageType", GameConstants.GET_MESSAGE_THREE);
								json.put("hasNewMsg", status.getIsNewQuestionInvite());
							}else if(newsType == 3){
								json.put("messageType", GameConstants.GET_MESSAGE_FOUR);
								json.put("hasNewMsg", status.getIsNewGameInvite());
							}else if(newsType == 4){
								json.put("messageType", GameConstants.GET_MESSAGE_FIVE);
								json.put("hasNewMsg", status.getIsNewSysNews());
							}
						}else{
							json.put("hasNewMsg", 0);
						}
						
					}else{//最后一条为私信对象
						json.put("dynamicEntryTitle", message.getContent());
						json.put("dynamicEntryMainId", null);
						json.put("chapterId", null);
						json.put("gameId", null);
						json.put("gameName", null);
						json.put("messageType", GameConstants.GET_MESSAGE_ONE);
						json.put("clubName", null);
						json.put("gameEventTitle", null);
						if(null != status){
							json.put("hasNewMsg", status.getIsNewPrivateMessage());
						}else{
							json.put("hasNewMsg", 0);
						}
					}
				}else{//如果私信为空
					json.put("dynamicEntryTitle", centerNews.getContent());
					json.put("dynamicEntryMainId", null);
					json.put("chapterId", null);
					json.put("gameId", null);
					json.put("gameName", null);
					json.put("messageType", centerNews.getNewsType());
					json.put("clubName", null);
					json.put("gameEventTitle", null);
					//消息类型。1:关注消息，2:邀请回答问题，3:邀请比赛，4：系统消息,5:私信
					if(null != status){
						int newsType = centerNews.getNewsType();
						if(newsType == 1){
							json.put("messageType", GameConstants.GET_MESSAGE_TWO);
							json.put("hasNewMsg", status.getIsNewAttention());
						}else if(newsType == 2){
							json.put("messageType", GameConstants.GET_MESSAGE_THREE);
							json.put("hasNewMsg", status.getIsNewQuestionInvite());
						}else if(newsType == 3){
							json.put("messageType", GameConstants.GET_MESSAGE_FOUR);
							json.put("hasNewMsg", status.getIsNewGameInvite());
						}else if(newsType == 4){
							json.put("messageType", GameConstants.GET_MESSAGE_FIVE);
							json.put("hasNewMsg", status.getIsNewSysNews());
						}
					}else{
						json.put("hasNewMsg", 0);
					}
				}
				array.add(json);
			}
			if(json == null){
				//获取私信对象
				json = new JSONObject();
				json.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_ONE);
				json.put("dynamicEntryTitle", message == null ? "" : message.getContent());
				json.put("dynamicEntryMainId", null);
				json.put("chapterId", null);
				json.put("gameId", null);
				json.put("gameName", null);
				json.put("messageType", GameConstants.GET_MESSAGE_ONE);
				json.put("clubName", null);
				json.put("gameEventTitle", null);
				if(null != status){
					json.put("hasNewMsg", status.getIsNewPrivateMessage());
				}else{
					json.put("hasNewMsg", 0);
				}
				array.add(json);
			}
			/*
			 * 新增评选功能(yy)增加评选快捷入口
			 */
			String voteJson = getVotesService.getVotesSomes(null, null, 0, 1, 3);
			JSONObject resVoteJson = JSONObject.parseObject(voteJson);
			if("0".equals(resVoteJson.get("code").toString())){
				JSONArray bsVoteItems = resVoteJson.getJSONObject("result").getJSONArray("votes");
				if(bsVoteItems!=null && bsVoteItems.size()>0){
					JSONObject b = (JSONObject)bsVoteItems.get(0);
					json = new JSONObject();
					json.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_NINE);
					json.put("dynamicEntryTitle", b.get("voteTitle") == null ? "" : b.get("voteTitle"));
					json.put("dynamicEntryMainId", b.get("voteId"));
					json.put("chapterId", null);
					json.put("gameId", null);
					json.put("gameName", null);
					json.put("messageType",null);
					json.put("clubName", null);
					json.put("gameEventTitle", null);
					json.put("hasNewMsg",null);
					array.add(json);
				}
			}
			//2获得用户所在班级信息
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			//定义班级群
			TeachCourseDiscuss teachCourseDiscuss = null;
			//定义答颖
			TeachQuestion teachQuestion = null;
			//定义俱乐部动态
			CenterLive centerLive = null;
			//定义竟猜对象
			ArenaGuess arenaGuess = null;
			//定义我关注的人的动态
			CenterLive userCenterLive = null;
			if(null != user && null != user.getClassId()){
				JSONObject discussJson = new JSONObject();
				JSONObject courseJson = new JSONObject();
				//获得课程讨论表
					teachCourseDiscuss = new TeachCourseDiscuss();
					teachCourseDiscuss.setClassId(user.getClassId());
					teachCourseDiscuss.setIsDelete(0);
					//最后一条的课程讨论ID
					teachCourseDiscuss = teachCourseDiscussService.getNewTeachCourseDiscuss(teachCourseDiscuss);
					if(null != teachCourseDiscuss){
						String disCussRedis = RedisComponent.findRedisObject(teachCourseDiscuss.getDiscussId(), TeachCourseDiscuss.class);
						teachCourseDiscuss = JSONObject.parseObject(disCussRedis, TeachCourseDiscuss.class);
					}
					discussJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_TWO);
					discussJson.put("dynamicEntryTitle", teachCourseDiscuss != null ? ("通知："+teachCourseDiscuss.getContent()) : "");
					discussJson.put("dynamicEntryMainId", teachCourseDiscuss != null ? teachCourseDiscuss.getClassId() : 0);
					discussJson.put("chapterId", null);
					discussJson.put("gameId", null);
					discussJson.put("gameName", null);
					discussJson.put("messageType", null);
					discussJson.put("hasNewMsg", 0);
					discussJson.put("clubName", null);
					discussJson.put("gameEventTitle", null);
					array.add(discussJson);
					
					//获得最后一条答疑
					teachQuestion = new TeachQuestion();
					teachQuestion.setClassId(user.getClassId());
					teachQuestion.setIsDelete(0);
					teachQuestion = teachQuestionService.getNewTeachQuestion(teachQuestion);
					if(null != teachQuestion){
						String questionRedis = RedisComponent.findRedisObject(teachQuestion.getQuestionId(), TeachQuestion.class);
						teachQuestion = JSONObject.parseObject(questionRedis, TeachQuestion.class);
						
					}
					courseJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_THREE);
					courseJson.put("dynamicEntryTitle", teachQuestion != null ? ("答疑："+teachQuestion.getTitle()) : "");
					courseJson.put("dynamicEntryMainId", teachQuestion != null ? teachQuestion.getClassId() : 0);
					courseJson.put("chapterId", null);
					courseJson.put("gameId", null);
					courseJson.put("gameName", null);
					courseJson.put("messageType", null);
					courseJson.put("hasNewMsg", 0);
					courseJson.put("clubName", null);
					courseJson.put("gameEventTitle", null);
					array.add(courseJson);
					

					//获取动态：最近创建的实训课程
					JSONObject training = this.tellMePracticalTraining(user.getClassId());
					if(training != null){
						array.add(training);
					}
			}
			
			//3用户所在的俱乐部信息
			if(null != user.getClubId()){
				//获得会员信息
				ClubMember member = new ClubMember();
				member.setUserId(userId);
				member = clubMemberService.selectClubMember(member);
				//如果会员状态是在审核中就不显示
				if(member.getMemberStatus() != GameConstants.CLUB_MEMBER_STATE_SH){
				//String memberRedis = RedisComponent.findRedisObject(, t)
				//定义俱乐部动态	JSONObject
				JSONObject clubJson = new JSONObject();
				//获取俱乐部下最后的动态
				centerLive = centerLiveService.getLastCenterLiveByClubId(user.getClubId());
				//获取俱乐部名称
				String clubRedis = RedisComponent.findRedisObject(user.getClubId(), ClubMain.class);
				ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
				String clubName = clubMain.getClubName();
				if(null != centerLive){
					String centerLiveRedis = RedisComponent.findRedisObject(centerLive.getLiveId(), CenterLive.class);
					centerLive = JSONObject.parseObject(centerLiveRedis, CenterLive.class);
					clubJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_FIVE);
					clubJson.put("dynamicEntryMainId", centerLive.getMainObjetId());
					clubJson.put("chapterId", null);
					clubJson.put("gameId", null);
					clubJson.put("gameName", null);
					clubJson.put("messageType", null);
					clubJson.put("hasNewMsg", 0);
					clubJson.put("clubName", clubName);
					clubJson.put("gameEventTitle", null);
					int liveType = centerLive.getLiveType();
					if(liveType == 1){
						String topicRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTopic.class);
						if(null != topicRedis){
							ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
							clubJson.put("dynamicEntryTitle", "话题 ："+topic.getTitle());
						}
					}else if(liveType == 2){
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						String arenaRedis = game.getBaseMatchInfoByMatchId(centerLive.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
								arenaRedis = gameJson.getString("result");
								JSONObject obj = JSONObject.parseObject(arenaRedis);
								clubJson.put("dynamicEntryTitle", "擂台 ："+obj.getString("matchName"));
							}
						}
					}else if(liveType == 3){
						//获取俱乐部培训
						String trainingClassRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTrainingClass.class);
						ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
						if(null != trainingClass){
							clubJson.put("dynamicEntryTitle", "培训 ："+trainingClass.getTitle());
						}
					}/*else if(liveType == 4){
						String remindRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubRemind.class);
						if(null != remindRedis){
							ClubRemind clubRemind = JSONObject.parseObject(remindRedis, ClubRemind.class);
							clubJson.put("dynamicEntryTitle", "提醒 ："+clubRemind.getContent());
						}
					}else if(liveType == 5){
						String noticeRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubNotice.class);
						if(null != noticeRedis){
							ClubNotice clubNotice = JSONObject.parseObject(noticeRedis, ClubNotice.class);
							clubJson.put("dynamicEntryTitle", "通知 ："+clubNotice.getContent());
						}
					}else if(liveType == 6){
						String redPacketRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubRedPacket.class);
						if(null != redPacketRedis){
							ClubRedPacket redPacket = JSONObject.parseObject(redPacketRedis, ClubRedPacket.class);
							clubJson.put("dynamicEntryTitle", "红包 ："+redPacket.getMessage());
						}
					}else if(liveType == 7){
						if(centerLive.getLiveMainType() == 7){
							String questionRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTrainingQuestion.class);
							if(null != questionRedis){
								ClubTrainingQuestion trainingQuestion = JSONObject.parseObject(questionRedis, ClubTrainingQuestion.class);
								clubJson.put("dynamicEntryTitle", "邀请答疑 ："+trainingQuestion.getTitle());
							}
						}else if(centerLive.getLiveMainType() == 9){
							String questionRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), TeachQuestion.class);
							if(null != questionRedis){
								TeachQuestion trainingQuestion = JSONObject.parseObject(questionRedis, TeachQuestion.class);
								clubJson.put("dynamicEntryTitle", "邀请答疑 ："+trainingQuestion.getTitle());
							}
						}
						
					}*/
					array.add(clubJson);
				}
				}
			}
			String recommend = "俱乐部、精英、赛事推荐";
			JSONObject recommendJson = new JSONObject();
			recommendJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_SIX);
			recommendJson.put("dynamicEntryMainId", null);
			recommendJson.put("dynamicEntryTitle", recommend);
			recommendJson.put("chapterId", null);
			recommendJson.put("gameId", null);
			recommendJson.put("gameName", null);
			recommendJson.put("messageType", null);
			recommendJson.put("hasNewMsg", 0);
			recommendJson.put("clubName", null);
			recommendJson.put("gameEventTitle", null);
			array.add(recommendJson);
			//获取竟猜参与人数最多的记录
			arenaGuess = arenaGuessService.getMostArenaGuess();
			JSONObject arenaGuessJson = new JSONObject();
			if(null != arenaGuess){
				String topicRedis = RedisComponent.findRedisObject(arenaGuess.getTopicId(), ArenaGuessTopic.class);
				if(null != topicRedis){
					ArenaGuessTopic topic = JSONObject.parseObject(topicRedis, ArenaGuessTopic.class);
					arenaGuessJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_SEVEN);
					arenaGuessJson.put("dynamicEntryMainId", topic.getCompId());
					arenaGuessJson.put("dynamicEntryTitle", "竟猜话题："+ arenaGuess.getGuessTitle());
					arenaGuessJson.put("chapterId", null);
					//获得比赛名称
					IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
					String arenaRedis = game.getBaseMatchInfoByMatchId(topic.getMatchId());
					if(!StringUtil.isEmpty(arenaRedis)){
						JSONObject gameJson = JSONObject.parseObject(arenaRedis);
						int code = gameJson.getInteger("code");
						if(code == 0){
							arenaRedis = gameJson.getString("result");
							JSONObject obj = JSONObject.parseObject(arenaRedis);
							arenaGuessJson.put("gameName", obj.getString("matchName"));
						}
					}
					if(null != topic.getCompId()){
						String compRedis = RedisComponent.findRedisObject(topic.getCompId(), ArenaCompetition.class);
						if(null != compRedis){
							ArenaCompetition arenaCompetition = JSONObject.parseObject(compRedis, ArenaCompetition.class);
							arenaGuessJson.put("gameEventTitle", arenaCompetition.getTitle());
						}
					}
					arenaGuessJson.put("gameId", topic.getMatchId());
					arenaGuessJson.put("messageType", null);
					arenaGuessJson.put("hasNewMsg", 0);
					arenaGuessJson.put("clubName", 0);
				}
				array.add(arenaGuessJson);
			}
			//获取我关注的人
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setCreateUserId(userId);
			centerAttention.setRelObjetType(GameConstants.USER_OBJ);
			//获得我关注的人的ID
			String ids = centerAttentionService.getCenterAttentionForMyUser(centerAttention);
			JSONObject attentionJson = null;
			
			if(null != ids){
				userCenterLive = centerLiveService.getLastCenterLiveByIds(ids);
				if(null != userCenterLive){
					String liveRedis = RedisComponent.findRedisObject(userCenterLive.getLiveId(), CenterLive.class);
					userCenterLive = JSONObject.parseObject(liveRedis, CenterLive.class);
					int liveType = userCenterLive.getLiveType();
					String userRediss = RedisComponent.findRedisObject(userCenterLive.getMainObjetId(), CenterUser.class);
					if(null != userRediss){
						String attentionRedis = RedisComponent.findRedisObject(userCenterLive.getLiveMainId(), CenterAttention.class);
						CenterAttention attention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
						CenterUser liveUser = JSONObject.parseObject(userRediss, CenterUser.class);
						String userName = liveUser.getNickName();
						if(liveType == 11 && null != attention){
							attentionJson = new JSONObject();
							attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
							attentionJson.put("dynamicEntryMainId", null);
							attentionJson.put("chapterId", null);
							attentionJson.put("gameId", null);
							attentionJson.put("gameName", null);
							attentionJson.put("messageType", null);
							attentionJson.put("hasNewMsg", 0);
							attentionJson.put("clubName", null);
							attentionJson.put("gameEventTitle", null);
							//关注了企业
							String dataStr = TimeUtil.getCurrentTime();
							String sign = MD5Utils.getMD5Str(dataStr);
							String param = "entIds="+attention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
							String companyRedis = HttpRequest.sendPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
							if(null != companyRedis){
								JSONObject companyPacket = JSONObject.parseObject(companyRedis);
								JSONArray companys = companyPacket.getJSONArray("ents");
								if(companys.size() > 0){
									JSONObject obj = companys.getJSONObject(0);
									String companyName = obj.getString("companyName");
									attentionJson.put("dynamicEntryTitle", userName+"关注了"+companyName+"企业的职位");
								}
							}
							array.add(attentionJson);
						}else if(liveType == 12 && null != attention){
							attentionJson = new JSONObject();
							attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
							attentionJson.put("dynamicEntryMainId", null);
							attentionJson.put("chapterId", null);
							attentionJson.put("gameId", null);
							attentionJson.put("gameName", null);
							attentionJson.put("messageType", null);
							attentionJson.put("hasNewMsg", 0);
							attentionJson.put("clubName", null);
							attentionJson.put("gameEventTitle", null);
							//关注俱乐部
							String clubRedis = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), ClubMain.class);
							if(null != clubRedis){
								ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
								attentionJson.put("dynamicEntryTitle", userName+"关注了"+clubMain.getClubName());
							}
							array.add(attentionJson);
						}else if(liveType == 13 && null != attention){
							attentionJson = new JSONObject();
							attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
							attentionJson.put("dynamicEntryMainId", null);
							attentionJson.put("chapterId", null);
							attentionJson.put("gameId", null);
							attentionJson.put("gameName", null);
							attentionJson.put("messageType", null);
							attentionJson.put("hasNewMsg", 0);
							attentionJson.put("clubName", null);
							attentionJson.put("gameEventTitle", null);
							//关注人
							String userRedis1 = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), CenterUser.class);
							if(null != userRedis1){
								CenterUser user1 = JSONObject.parseObject(userRedis1, CenterUser.class);
								attentionJson.put("dynamicEntryTitle", userName+"关注了"+user1.getNickName());
							}
							array.add(attentionJson);
						}else if(liveType == 14){
							attentionJson = new JSONObject();
							attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
							attentionJson.put("dynamicEntryMainId", null);
							attentionJson.put("chapterId", null);
							attentionJson.put("gameId", null);
							attentionJson.put("gameName", null);
							attentionJson.put("messageType", null);
							attentionJson.put("hasNewMsg", 0);
							attentionJson.put("clubName", null);
							attentionJson.put("gameEventTitle", null);
							//加入俱乐部
							String clubRedis = RedisComponent.findRedisObject(userCenterLive.getLiveMainId(), ClubMain.class);
							if(null != clubRedis){
								ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
								attentionJson.put("dynamicEntryTitle", userName+"加入了"+clubMain.getClubName());
							}
							array.add(attentionJson);
						}else if(liveType == 15){
							attentionJson = new JSONObject();
							attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
							attentionJson.put("dynamicEntryMainId", null);
							attentionJson.put("chapterId", null);
							attentionJson.put("gameId", null);
							attentionJson.put("gameName", null);
							attentionJson.put("messageType", null);
							attentionJson.put("hasNewMsg", 0);
							attentionJson.put("clubName", null);
							attentionJson.put("gameEventTitle", null);
							//参加比赛
							IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
							String arenaRedis = game.getBaseMatchInfoByMatchId(userCenterLive.getLiveMainId());
							if(!StringUtil.isEmpty(arenaRedis)){
								JSONObject gameJson = JSONObject.parseObject(arenaRedis);
								int code = gameJson.getInteger("code");
								if(code == 0){
									arenaRedis = gameJson.getString("result");
									JSONObject obj = JSONObject.parseObject(arenaRedis);
									attentionJson.put("dynamicEntryTitle", userName+"参加了"+obj.getString("matchName"));
								}
							}
							array.add(attentionJson);
						}
						
					}
				}
			}
			if(attentionJson == null){
				attentionJson = new JSONObject();
				attentionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_EIGHT);
				attentionJson.put("dynamicEntryMainId", null);
				attentionJson.put("chapterId", null);
				attentionJson.put("gameId", null);
				attentionJson.put("gameName", null);
				attentionJson.put("messageType", null);
				attentionJson.put("hasNewMsg", 0);
				attentionJson.put("clubName", null);
				attentionJson.put("gameEventTitle", null);
				//加入俱乐部
				attentionJson.put("dynamicEntryTitle", "");
				array.add(attentionJson);
			}
			Map<String, Object> backParam = new HashMap<String, Object>();
			backParam.put("returnCount", array.size());
			backParam.put("allPage", 1);
			backParam.put("currentPage", 1);
			backParam.put("dynamicEntries", array);
			return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
		}catch(Exception e){
			LogUtil.error(this.getClass(), "getDynamics", "获得动态失败", e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_ATTITUDE).toJSONString();
		}
	}
	
	

	/**
	 * 获取动态：实训
	 * @param classId
	 * @return
	 * @author 			lw
	 * @date			2016年9月20日  下午3:01:19
	 */
	private JSONObject tellMePracticalTraining(Integer classId) {
		List<Integer> chapetIds = teachCourseChapterService.getAllChapetIdByClass(classId);
		if(!CollectionUtils.isEmpty(chapetIds)){
			List<TeachCourseCard> cardList = teachCourseCardService.selectByChapterIds(StringUtil.ListToString(chapetIds, ","));
			if(!CollectionUtils.isEmpty(cardList)){
				
				//游戏端需要通过所有课程卡IDS查询最近创建的实训课程
				StringBuffer sb = new StringBuffer();
				for(TeachCourseCard en : cardList){
					sb.append(en.getCardId()).append(",");
				}
				if(sb.length() > 2){
					
					String cardIds = sb.substring(0, sb.length()-1).toString();
					
					IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);  
					/*
					 * 通过cardId远程调用游戏端获取教师最近一次创建的实训课程。
					 */
					String gameData = game.getTeacherCreateMatchInfoByCardIds(cardIds);
					if(gameData != null){
						JSONObject gameJson = JSONObject.parseObject(gameData);
						if(gameJson.getInteger(GameConstants.CODE) == 0){
							JSONObject match = gameJson.getJSONObject(GameConstants.RESULT);
							if(match != null && match.size() > 0){
								JSONObject questionJson = new JSONObject();
								for(TeachCourseCard en :cardList){
									if(en.getCardId().equals(match.getInteger("cardId"))){
										questionJson.put("chapterId", en.getChapterId());
									}
								}
								if(questionJson.get("chapterId") == null){
									questionJson.put("chapterId", null);
								}
								questionJson.put("dynamicEntryType", GameConstants.DYNAMIC_ENTRY_TYPE_FOUR);
								questionJson.put("dynamicEntryTitle", "实训："+match.getString("matchName"));
								questionJson.put("dynamicEntryMainId", classId);
								questionJson.put("gameId", null);
								questionJson.put("gameName", null);
								questionJson.put("messageType", null);
								questionJson.put("hasNewMsg", 0);
								questionJson.put("clubName", null);
								questionJson.put("gameEventTitle", null);
								return questionJson;
							}
						}
						
					}
				}
			}
			
		}
		return null;
	}
	/**
	 * 获取动态
	 * @param userId
	 * @param memberId
	 * @param classId
	 * @param classType
	 * @param clubId
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @return
	 */
	public String getDynamicsUpdate(Integer userId, Integer memberId, Integer classId, Integer classType,
			Integer clubId, Integer start, Integer limit, Integer inquireType) {
		String result = null;
		if(inquireType == GameConstants.GET_ATTITUDE_ONE){//我的俱乐部动态
			JSONArray arr = new JSONArray();
			//分页查询俱乐部动态
			QueryPage<CenterLive> queryPage = centerLiveService.getClubCenterLive(clubId, start, limit);
			//无数据返回
			if(CollectionUtils.isEmpty(queryPage.getList())){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			List<CenterLive> lives = queryPage.getList();
			for (CenterLive live : lives) {//1，2，3，4，5，6，7
				String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
				CenterLive liveBean = JSONObject.parseObject(redisLive, CenterLive.class);
				//动态类型
				int liveType = liveBean.getLiveType().intValue();
				if(liveType == 1){//获取话题
					JSONObject jsonobj = businessClubService.getDynamics_1(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 2){// 2:俱乐部擂台；
					JSONObject jsonobj = businessClubService.getDynamics_2(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 3){// 3:俱乐部培训；
					JSONObject jsonobj = businessClubService.getDynamics_3(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 4){//4:俱乐部提醒；
					JSONObject jsonobj = businessClubService.getDynamics_4(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 5){//5:俱乐部通知
					JSONObject jsonobj = businessClubService.getDynamics_5(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 6){//获取俱乐部红包
					JSONObject jsonobj = businessClubService.getDynamics_6(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 7){//获取俱乐部邀请答疑
					JSONObject jsonobj = businessClubService.getDynamics_7(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}
			}
			result = businessClubService.commonReturnMap(
					 queryPage.getCount(), queryPage.getAllPage(),
					 queryPage.getCurrentPage(), arr);
		}else if(inquireType == GameConstants.GET_ATTITUDE_TWO){//个人中心动态
			JSONArray arr = new JSONArray();
			//获取人员信息
			String userRedis = RedisComponent.findRedisObject(memberId, CenterUser.class);
			//如果没有此用户返回空
			if(null == userRedis){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			CenterUser user = JSONObject.parseObject(userRedis,CenterUser.class);
			//定义参数map
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("clubId", user.getClubId()==null?null:user.getClubId());
			paramMap.put("classId", user.getClassId()==null?null:user.getClassId());
			paramMap.put("userId",memberId==null?null:memberId);
			//分页查询俱乐部动态
			QueryPage<CenterLive> queryPage = centerLiveService.getMemberCenterLive(paramMap, start, limit);
			if(CollectionUtils.isEmpty(queryPage.getList())){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			List<CenterLive> lives = queryPage.getList();
			for(CenterLive live : lives){//1，2，3，8，9，10
				String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
				CenterLive liveBean = JSONObject.parseObject(redisLive, CenterLive.class);
				int liveType = liveBean.getLiveType().intValue();
				if(liveType == 1){//获取话题
					JSONObject jsonobj = businessClubService.getDynamics_1(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 2){// 2:俱乐部擂台；
					JSONObject jsonobj = businessClubService.getDynamics_2(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 3){// 3:俱乐部培训；
					JSONObject jsonobj = businessClubService.getDynamics_3(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 8){
					JSONObject jsonobj = businessClubService.getDynamics_8(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 9){
					JSONObject jsonobj = businessClubService.getDynamics_9(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 10){
					JSONObject jsonobj = businessClubService.getDynamics_10(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}
			}
			result = businessClubService.commonReturnMap(
					 queryPage.getCount(), queryPage.getAllPage(),
					 queryPage.getCurrentPage(), arr);
		}else if(inquireType == GameConstants.GET_ATTITUDE_THREE){//班级动态
			JSONArray arr = new JSONArray();
			//分页查询俱乐部动态
			QueryPage<CenterLive> queryPage = centerLiveService.getClassCenterLive(classId,start, limit);
			if(CollectionUtils.isEmpty(queryPage.getList())){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			List<CenterLive> lives = queryPage.getList();
			for(CenterLive live : lives){//8，9，10
				String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
				CenterLive liveBean = JSONObject.parseObject(redisLive, CenterLive.class);
				int liveType = liveBean.getLiveType().intValue();
				if(liveType == 8){
					JSONObject jsonobj = businessClubService.getDynamics_8(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 9){
					JSONObject jsonobj = businessClubService.getDynamics_9(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 10){
					JSONObject jsonobj = businessClubService.getDynamics_10(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}
			}
			result = businessClubService.commonReturnMap(
					 queryPage.getCount(), queryPage.getAllPage(),
					 queryPage.getCurrentPage(), arr);
		}else if(inquireType == GameConstants.GET_ATTITUDE_FOUR){//俱乐部动态
			JSONArray arr = new JSONArray();
			//分页查询俱乐部动态
			QueryPage<CenterLive> queryPage = centerLiveService.getClubCenterLive(clubId, start, limit);
			//无数据返回
			if(CollectionUtils.isEmpty(queryPage.getList())){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			List<CenterLive> lives = queryPage.getList();
			for (CenterLive live : lives) {//1，2，3，4，5，6，7
				String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
				CenterLive liveBean = JSONObject.parseObject(redisLive, CenterLive.class);
				//动态类型
				int liveType = liveBean.getLiveType().intValue();
				if(liveType == 1){//获取话题
					JSONObject jsonobj = businessClubService.getDynamics_1(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 2){// 2:俱乐部擂台；
					JSONObject jsonobj = businessClubService.getDynamics_2(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 3){// 3:俱乐部培训；
					JSONObject jsonobj = businessClubService.getDynamics_3(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}
			}
			result = businessClubService.commonReturnMap(
					 queryPage.getCount(), queryPage.getAllPage(),
					 queryPage.getCurrentPage(), arr);
		}else if(inquireType == GameConstants.GET_ATTITUDE_FIVE){//获取关注人的动态
			//11，12，13，14，15
			JSONArray arr = new JSONArray();
			//获取关注的用户ID列表
			CenterAttention attention = new CenterAttention();
			attention.setCreateUserId(userId);
			attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_USER);
			List<CenterAttention> attentions = centerAttentionService.getCenterAttentionByCreateUserId(attention);
			if(CollectionUtils.isEmpty(attentions)){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			//获得我关注人的id,多个以逗号分割(1,2,3,4)
			String ids = "";
			for(CenterAttention can : attentions){
				ids = ids+can.getRelObjetId()+",";
			}
			//分页查询俱乐部动态
			QueryPage<CenterLive> queryPage = centerLiveService.getAttentionUserCenterLive(ids.substring(0, ids.length()-1), start, limit);
			if(CollectionUtils.isEmpty(queryPage.getList())){
				return businessClubService.commonReturnMap(0,0,0, arr);
			}
			List<CenterLive> lives = queryPage.getList();
			for(CenterLive live : lives){
				String redisLive = RedisComponent.findRedisObject(live.getLiveId(), CenterLive.class);
				CenterLive liveBean = JSONObject.parseObject(redisLive, CenterLive.class);
				int liveType = liveBean.getLiveType().intValue();
				if(liveType == 11){//用户关注职位动态；
					JSONObject jsonobj = businessClubService.getDynamics_11(liveBean,userId);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 12){//用户关注俱乐部动态；
					JSONObject jsonobj = businessClubService.getDynamics_12(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 13){//用户关注人动态；
					JSONObject jsonobj = businessClubService.getDynamics_13(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 14){//用户加入俱乐部动态；
					JSONObject jsonobj = businessClubService.getDynamics_14(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}else if(liveType == 15){//用户参加比赛动态；
					JSONObject jsonobj = businessClubService.getDynamics_15(liveBean);
					if(null != jsonobj){
						arr.add(jsonobj);
					}
				}
			}
			result = businessClubService.commonReturnMap(
					 queryPage.getCount(), queryPage.getAllPage(),
					 queryPage.getCurrentPage(), arr);
		}
		return result;
	}

}
