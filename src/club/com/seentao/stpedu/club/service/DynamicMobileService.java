
package com.seentao.stpedu.club.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.attention.service.CenterAttentionService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterPrivateMessageTalkDao;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterNewsService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.TeachCourseDiscussService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.HttpRequest;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;
import com.seentao.stpedu.votes.service.GetVotesService;


@Service
public class DynamicMobileService {
	/** 
	* @Fields centerNewsService : 消息服务
	*/ 
	@Autowired
	private CenterNewsService centerNewsService;
	/** 
	* @Fields centerNewsStatusService : 消息状态服务 
	*/ 
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
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
	* @Fields clubMemberService : 俱乐部会员服务
	*/ 
	@Autowired
	private ClubMemberService clubMemberService;
	/** 
	* @Fields centerLiveService : 动态服务
	*/ 
	@Autowired
	private CenterLiveService centerLiveService;
	/** 
	* @Fields centerAttentionService : 关注服务
	*/ 
	@Autowired
	private CenterAttentionService centerAttentionService;
	@Autowired
	private ArenaGuessService arenaGuessService;
	@Autowired
	private CenterPrivateMessageTalkDao centerPrivateMessageTalkDao;
	@Autowired
	private GetVotesService getVotesService;
	@Autowired
	private DynamicTrainingService dynamicTrainingService;
	
	/*
	 * 获取当前人的相关动态信息
	 */
	public String getDynamicEntryForMobile(Integer userId){
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
		CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
		/*
		 * 添加消息动态
		 */
			JSONObject cenJosn = getCenterMessages(userId);
			if( cenJosn != null)
			array.add(cenJosn);
		/*
		 * 添加评选快捷入口
		 */
		JSONObject voteJson = getVotesSome();
		if( voteJson != null)
		array.add(voteJson);
		/*
		 * 班级群(群组讨论)
		 */
		JSONObject teachJson = getTeachCourseDiscuss(user);
		if( teachJson != null)
		array.add(teachJson);
		/*
		 * 获取答疑
		 */
		JSONObject questJson = getTeachQuestion(user);
		if( questJson != null)
		array.add(questJson);
		/*
		 * 实训
		 */
		JSONObject tellJson = getTellMePracticalTraining(user.getClassId());
		if(tellJson != null)
			array.add(tellJson);
		/*
		 * 俱乐部
		 */
		JSONObject clubJson = getClubMember(user) ;
		if(clubJson != null)
		array.add(clubJson);
		/*
		 * 推荐
		 */
		JSONObject recommend  = getRecommend();
		if( recommend!= null)
		array.add(recommend);
		/*
		 * 竞猜
		 */
		JSONObject arendJson = getArenaGuess();
		if( arendJson != null)
		array.add(arendJson);
		/*
		 * 人脉
		 */
		JSONObject attenJson = getCenterAttention(user);
		if( attenJson != null)
		array.add(attenJson);
		obj.put("returnCount", array.size());
		obj.put("allPage", 1);
		obj.put("currentPage", 1);
		obj.put("dynamicEntries", array);
		return Common.getReturn(AppErrorCode.SUCCESS, "", obj).toJSONString();
	}
	
	/*				
	 *消息 
	 */
	protected JSONObject getCenterMessages(Integer userId){
		//1获取最后一条消息ID
		CenterNews centerNews = centerNewsService.getCenterNews(userId);
		if(centerNews == null)
			return CommonParameter.buildParameter(null, null, 1, "", null, 0);
		String centerNewsRedis = RedisComponent.findRedisObject(centerNews.getNewsId(), CenterNews.class);
		centerNews = JSONObject.parseObject(centerNewsRedis, CenterNews.class);
		//获取该用户最新一条私信信息(不管是发送的还是接收的)---yy修改
		CenterPrivateMessage message = null;
		//根据当前用户userId查询最后一条私信id
		CenterPrivateMessageTalk talk = centerPrivateMessageTalkDao.queryCenterPrivateMessageTalkNew(userId);
		if(talk != null)
			message =JSONObject.parseObject(RedisComponent.findRedisObject(talk.getLastPrivateMessageId(), CenterPrivateMessage.class), CenterPrivateMessage.class) ;
		//获取用户消息状态
		CenterNewsStatus status = centerNewsStatusService.getCenterNewsStatusByUserId(userId);
		/*if(centerNews == null){
			return CommonParameter.buildParameter(status, null, 1, message == null ? "" : message.getContent(), null, null);
		}*/
		/*
		 * 如果私信不为空
		 */
		if(message != null){
			/*
			 * 获取消息和私信对象创建时间
			 */
			int messageTime = message.getCreateTime();
			int newsTime = centerNews.getCreateTime();
			/*
			 * 如果最新消息时间大于私信时间则为消息封装
			 */
			if(newsTime >= messageTime){
				/*
				 * 获取消息类型 1:关注消息，2:邀请回答问题，3:邀请比赛，4：系统消息,5:私信
				 */
				return CommonParameter.buildParameter(status, centerNews, 1, centerNews.getContent(), null, null);
			}else{
				/*
				 * 如果最后为私信
				 */
				
				return CommonParameter.buildParameter(status, centerNews, 1, message.getContent(), null, null);
			}
		}
		/*
		 * 否则私信为空
		 */
		return CommonParameter.buildParameter(status, centerNews, 1, centerNews.getContent(), null,null);
	}
	/*
	 * 新增评选功能(yy)增加评选快捷入口
	 */
	protected JSONObject getVotesSome(){
		String voteJson = getVotesService.getVotesSomes(null, null, 0, 1, 3);
		JSONObject resVoteJson = JSONObject.parseObject(voteJson);
		if("0".equals(resVoteJson.get("code").toString())){
			JSONArray bsVoteItems = resVoteJson.getJSONObject("result").getJSONArray("votes");
			if(bsVoteItems!=null && bsVoteItems.size()>0){
				JSONObject b = (JSONObject)bsVoteItems.get(0);
				return CommonParameter.buildParameter(null, null, 9, b.get("voteTitle") == null ? "" : String.valueOf(b.get("voteTitle")), Integer.valueOf((String)b.get("voteId")), 0);
			}
		}
		return CommonParameter.buildParameter(null, null, 9, "", null, 0);
	}
	/*
	 * 班级群(群组讨论)
	 */
	protected JSONObject getTeachCourseDiscuss(CenterUser user){
		//2获得用户所在班级信息
		
		if(user.getClassId() != null){
			Integer disId = teachCourseDiscussService.getNewTeachCourseDiscussMax(user.getClassId());
			if(null != disId){
				String disCussRedis = RedisComponent.findRedisObject(disId, TeachCourseDiscuss.class);
				TeachCourseDiscuss teachCourseDiscuss = JSONObject.parseObject(disCussRedis, TeachCourseDiscuss.class);
				return CommonParameter.buildParameter(null, null, 2, teachCourseDiscuss != null ? ("通知："+teachCourseDiscuss.getContent()) : "", teachCourseDiscuss != null ? teachCourseDiscuss.getClassId():0, 0);
			}
			return null;
		}
		return null;
	}
	/*
	 * 获得最后一条答疑
	 */
	protected JSONObject getTeachQuestion(CenterUser user){
		if(user.getClassId() != null){
			TeachQuestion teachQuestion = teachQuestionService.getNewTeachQuestionMax(user.getClassId());
			if(teachQuestion != null){
				String questionRedis = RedisComponent.findRedisObject(teachQuestion.getQuestionId(), TeachQuestion.class);
				teachQuestion = JSONObject.parseObject(questionRedis, TeachQuestion.class);
				return CommonParameter.buildParameter(null, null, 3, teachQuestion != null ? ("答疑："+teachQuestion.getTitle()) : "", teachQuestion != null ? teachQuestion.getClassId() : 0, 0);
			}
			return null;
		}
		return null;
	}
	/*
	 * 获取实训
	 */
	protected JSONObject getTellMePracticalTraining(Integer classId){
		if(classId == null)
			return null;
		return  dynamicTrainingService.tellMePracticalTraining(classId); 
		
	}
	/*
	 * 获取俱乐部动态
	 */
	protected  JSONObject getClubMember(CenterUser user){
		if(user.getClubId() != null){
			ClubMember clubMember = clubMemberService.queryClubMemberByClubIdAndUserId(user.getUserId(), user.getClubId());
			if(clubMember != null){
				/*
				 * 俱乐部的动态
				 */
				CenterLive centerLive = centerLiveService.getLastCenterLiveByClubId(user.getClubId());
				/*
				 * 获取俱乐部名称
				 */
				String clubRedis = RedisComponent.findRedisObject(user.getClubId(), ClubMain.class);
				ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
				if(centerLive != null){
					/*
					 * 获取动态
					 */
					String centerLiveRedis = RedisComponent.findRedisObject(centerLive.getLiveId(), CenterLive.class);
					centerLive = JSONObject.parseObject(centerLiveRedis, CenterLive.class);
					int type = centerLive.getLiveType();
					switch (type) {
					case 1:
						String topicRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTopic.class);
						if(null != topicRedis){
							ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
							JSONObject obj = CommonParameter.buildParameter(null, null, 5, "话题 ："+topic.getTitle(), centerLive.getMainObjetId(), 0);
							obj.put("clubName", clubMain.getClubName());
							return obj;
						}
					case 2:
						IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
						String arenaRedis = game.getBaseMatchInfoByMatchId(centerLive.getLiveMainId());
						if(!StringUtil.isEmpty(arenaRedis)){
							JSONObject gameJson = JSONObject.parseObject(arenaRedis);
							int code = gameJson.getInteger("code");
							if(code == 0){
								arenaRedis = gameJson.getString("result");
								JSONObject obj = JSONObject.parseObject(arenaRedis);
								JSONObject jsonObject = CommonParameter.buildParameter(null, null, 5, "擂台 ："+obj.getString("matchName"), centerLive.getMainObjetId(), 0);
								jsonObject.put("clubName", clubMain.getClubName());
								return jsonObject;
							}
						}
					case 3:
						//获取俱乐部培训
						String trainingClassRedis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTrainingClass.class);
						ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
						if(null != trainingClass){
							JSONObject object= CommonParameter.buildParameter(null, null, 5, "培训 ："+trainingClass.getTitle(), centerLive.getMainObjetId(), 0);
							object.put("clubName", clubMain.getClubName());
							return object;
						}
					default:
						return null;
					}
					
				}
			}
			return null;
		}
		return null;
	}
	/*
	 * 推荐
	 */
	protected JSONObject getRecommend(){
		return CommonParameter.buildParameter(null, null, 6, "俱乐部、精英、赛事推荐", null, 0);
	}
	/*
	 * 竞猜
	 */
	protected JSONObject getArenaGuess(){
		ArenaGuess arenaGuess = arenaGuessService.getMostArenaGuess();
		if(arenaGuess != null){
			String topicRedis = RedisComponent.findRedisObject(arenaGuess.getTopicId(), ArenaGuessTopic.class);
			String gameName = "";
			String gameEventTitle ="";
			if(StringUtils.isNoneEmpty(topicRedis)){
				ArenaGuessTopic topic = JSONObject.parseObject(topicRedis, ArenaGuessTopic.class);
				IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
				String arenaRedis = game.getBaseMatchInfoByMatchId(topic.getMatchId());
				if(!StringUtil.isEmpty(arenaRedis)){
					JSONObject gameJson = JSONObject.parseObject(arenaRedis);
					int code = gameJson.getInteger("code");
					if(code == 0){
						arenaRedis = gameJson.getString("result");
						JSONObject obj = JSONObject.parseObject(arenaRedis);
						gameName = obj.getString("matchName");
					}
				}
				if(null != topic.getCompId()){
					String compRedis = RedisComponent.findRedisObject(topic.getCompId(), ArenaCompetition.class);
					if(null != compRedis){
						ArenaCompetition arenaCompetition = JSONObject.parseObject(compRedis, ArenaCompetition.class);
						gameEventTitle = arenaCompetition.getTitle();
					}
				}
				JSONObject json = CommonParameter.buildParameter(null, null, 7, "竟猜话题："+ arenaGuess.getGuessTitle(), topic.getCompId(), 0);
				json.put("gameName", gameName);
				json.put("gameEventTitle", gameEventTitle);
				json.put("gameId", topic.getMatchId());
				return json;
			}
		}
		return CommonParameter.buildParameter(null, null, 7, "", null, 0);
	}
	/*
	 * 人脉
	 */
	protected JSONObject getCenterAttention(CenterUser user){
		/*
		 * 1 、rel_object_id 人脉
		 * 2、1 表示只获取关注人的动态
		 */
		String ids = centerAttentionService.getCenterAttentionForMyUser(new CenterAttention(user.getUserId(),1));
		if(StringUtils.isNoneBlank(ids)){
			/*
			 * 表示我关注产生的动态信息
			 */
			CenterLive centerLive = centerLiveService.getLastCenterLiveByIds(ids);
			if(centerLive != null){
				String liveRedis = RedisComponent.findRedisObject(centerLive.getLiveId(), CenterLive.class);
				centerLive = JSONObject.parseObject(liveRedis, CenterLive.class);
				/*
				 * 表示别人关注我的
				 */
				String redis = RedisComponent.findRedisObject(centerLive.getLiveMainId(), CenterAttention.class);
				CenterAttention attention = JSONObject.parseObject(redis, CenterAttention.class);
				/*
				 * 查询关注的用户信息
				 */
				String attUser = RedisComponent.findRedisObject(centerLive.getMainObjetId(), CenterUser.class);
				CenterUser us = JSONObject.parseObject(attUser, CenterUser.class);
				
				int liveType = centerLive.getLiveType();
				switch (liveType) {
				case 11:
						/*
						 * 关注了企业
						 */
					return getCompany(us, attention);
				case 12:
						/*
						 * 关注俱乐部
						 */
					if(attention == null)
//						return null;
						return CommonParameter.buildParameter(null, null, 8, "", null, 0);
						String clubRedis = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), ClubMain.class);
						ClubMain clubMain = JSONObject.parseObject(clubRedis, ClubMain.class);
						if(null != clubMain){
							return CommonParameter.buildParameter(null, null, 8, us.getNickName()+"关注了"+clubMain.getClubName(), null, 0);
						}
				case 13:
						/*
						 * 如果当前没有用户关注你则返回为空
						 */
					if(attention == null)
//						return null;
						return CommonParameter.buildParameter(null, null, 8, "", null, 0);
						//关注人
						String userRedis1 = RedisComponent.findRedisObject(Integer.parseInt(attention.getRelObjetId()), CenterUser.class);
						CenterUser center = JSONObject.parseObject(userRedis1, CenterUser.class);
						if(null != center){
							return CommonParameter.buildParameter(null, null, 8, us.getNickName()+"关注了"+center.getNickName(), null, 0);
							
							}
						return CommonParameter.buildParameter(null, null, 8, null, null, 0);
				case 14:
					String joinClub = RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubMain.class);
					ClubMain main = JSONObject.parseObject(joinClub, ClubMain.class);
					if(StringUtils.isEmpty(redis))
//						return null;
						return CommonParameter.buildParameter(null, null, 8, "", null, 0);
					
						return CommonParameter.buildParameter(null, null, 8, us.getNickName()+"加入了"+main.getClubName(), null, 0);
				case 15:
					IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
					String arenaRedis = game.getBaseMatchInfoByMatchId(centerLive.getLiveMainId());
					if(StringUtil.isEmpty(arenaRedis))
						return CommonParameter.buildParameter(null, null, 8, "", null, 0);
						JSONObject gameJson = JSONObject.parseObject(arenaRedis);
						int code = gameJson.getInteger("code");
						if(code == 0){
							arenaRedis = gameJson.getString("result");
							JSONObject obj = JSONObject.parseObject(arenaRedis);
							return CommonParameter.buildParameter(null, null, 8, us.getNickName()+"参加了"+obj.getString("matchName"), null, 0);
						}
				default:
					return CommonParameter.buildParameter(null, null, 8, "", null, 0);
				}
			}
			return CommonParameter.buildParameter(null, null, 8, "", null, 0);
		}
		return CommonParameter.buildParameter(null, null, 8, "", null, 0);
	}
	/*
	 * 关注企业
	 */
	protected JSONObject getCompany(CenterUser us,CenterAttention attention){
		if(attention == null)
			return null;
			String dataStr = TimeUtil.getCurrentTime();
			String sign = MD5Utils.getMD5Str(dataStr);
			String param = "entIds="+attention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
			String companyRedis = HttpRequest.sendPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
				if(StringUtils.isEmpty(companyRedis))
					return CommonParameter.buildParameter(null, null, 8, "", null, 0);
				JSONObject companyPacket = JSONObject.parseObject(companyRedis);
				JSONArray companys = companyPacket.getJSONArray("ents");
				if(companys.size() > 0){
					JSONObject obj = companys.getJSONObject(0);
					String companyName = obj.getString("companyName");
					return CommonParameter.buildParameter(null, null, 8,companyName != null ? us.getNickName()+"关注了"+companyName+"企业的职位":null, null, 0);
				}
				return CommonParameter.buildParameter(null, null, 8, "", null, 0);
	}
}
