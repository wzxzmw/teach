package com.seentao.stpedu.club.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubNotice;
import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.ClubRelRedPacketMemberService;
import com.seentao.stpedu.common.service.ClubRelRemindMemberService;
import com.seentao.stpedu.common.service.TeachCourseDiscussService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.user.service.BusinessUserService;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

/**
 * getDynamics
 * 获取动态信息
 * 单独把没每一种动态查询出来并返回统一json
 * 供pc端使用
 */
@Service
public class BusinessClubService {
	
	@Autowired
	private ClubRelRedPacketMemberService clubRelRedPacketMemberService;
	
	@Autowired
	private ClubRelRemindMemberService clubRelRemindMemberService;
	
	@Autowired
	private TeachCourseDiscussService teachCourseDiscussService;
	
	@Autowired
	private TeachQuestionService teachQuestionService;
	
	@Autowired
	private CenterCompanyService centerCompanyService;
	
	@Autowired
	private CenterAttentionDao centerAttentionDao;
	
	protected static JSONObject commonReturnJsonObjects(
			Integer dynamicId,Integer dynamicType,Integer dmCreaterId
			,Integer dmCreateDate,Integer dynamicMainId,Integer isTop){
		JSONObject json = new JSONObject();
		String userRedis = RedisComponent.findRedisObject(dmCreaterId, CenterUser.class);
		CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
		json.put("dynamicId", String.valueOf(dynamicId));
		json.put("dynamicType", String.valueOf(dynamicType));
		if(user!=null){
			String msg = Common.getImgUrl(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			json.put("dmCreaterHeadLink",Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg );
			json.put("dmCreaterId", String.valueOf(dmCreaterId));
			json.put("dmCreaterName", user.getRealName());
			json.put("dmCreaterNickname", user.getNickName());
		}
		json.put("isTop", isTop);
		json.put("dynamicMainId", String.valueOf(dynamicMainId));
		json.put("dmCreateDate", String.valueOf(dmCreateDate));
		return json;
		
	}
	
	/**
	 * 最终返回结果
	 * @param returnCount
	 * @param allPage
	 * @param currentPage
	 * @param dynamics
	 * @return
	 */
	protected static String commonReturnMap(Integer returnCount,Integer allPage,Integer currentPage,JSONArray dynamics){
		Map<String, Object> backParam = new HashMap<String, Object>();
		backParam.put("returnCount",returnCount);
		backParam.put("allPage",allPage);
		backParam.put("currentPage", currentPage);
		backParam.put("dynamics", dynamics);
		return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
	}
	
	/**
	 * 1:俱乐部话题；
	 * @return
	 */
	public JSONObject getDynamics_1(CenterLive liveBean){
		String topicRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubTopic.class);
		if(topicRedis==null){
			return null;
		}
		ClubTopic topic = JSONObject.parseObject(topicRedis, ClubTopic.class);
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				topic.getCreateUserId(), topic.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle", topic.getTitle());
		json.put("dynamicMainContent", RichTextUtil.delHTMLTag(topic.getContent()));
		json.put("dmSupportCount", topic.getPraiseNum());//动态主体的点赞数量
		json.put("dmCommentCount", topic.getCommentNum());//动态主体的评论数量
		json.put("clubId",topic.getClubId());
		return json;
	}
	/**
	 * 2:俱乐部擂台；
	 * @return
	 */
	public JSONObject getDynamics_2(CenterLive liveBean){
		//游戏平台返回对象
		JSONObject gameObj = null;
		JSONObject json = null;
		IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
		String arenaRedis = game.getBaseMatchInfoByMatchId(liveBean.getLiveMainId());
		if(!StringUtil.isEmpty(arenaRedis)){
			JSONObject gameJson = JSONObject.parseObject(arenaRedis);
			int code = gameJson.getInteger("code");
			if(code == 0){
				arenaRedis = gameJson.getString("result");
				gameObj = JSONObject.parseObject(arenaRedis);
				if(null != gameObj){
					json = commonReturnJsonObjects(
							liveBean.getLiveId(),liveBean.getLiveType(),
							gameObj.getInteger("creatorId"),gameObj.getInteger("createTime"),
							liveBean.getLiveMainId(), liveBean.getIsTop());
					json.put("dynamicMainTitle",gameObj.getString("matchName"));
				}
			}
		}
		return json;
	}
	/**
	 * 3:俱乐部培训；
	 * @return
	 */
	public JSONObject getDynamics_3(CenterLive liveBean){
		String trainingClassRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubTrainingClass.class);
		if(trainingClassRedis==null){
			return null;
		}
		ClubTrainingClass trainingClass = JSONObject.parseObject(trainingClassRedis, ClubTrainingClass.class);
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				trainingClass.getCreateUserId(), trainingClass.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle", trainingClass.getTitle());
		json.put("dynamicMainContent",trainingClass.getIntroduce());
		return json;
	}
	/**
	 * 4:俱乐部提醒；
	 * @return
	 */
	public JSONObject getDynamics_4(CenterLive liveBean){
		JSONObject json = null;
		//获取俱乐部提醒
		String remindRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubRemind.class);
		ClubRemind remind = JSONObject.parseObject(remindRedis, ClubRemind.class);
		if(null != remind){
			//获取提醒关系信息
			List<ClubRelRemindMember> relReminds = clubRelRemindMemberService.getClubRelRemindMemberByRemindId(remind.getRemindId());
			String ids = "";	//接收者id
			String names = "";	//接收者名称
			String nicks = "";	//接收者昵称
			if(null != relReminds && relReminds.size()>0){
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
				json = commonReturnJsonObjects(
						liveBean.getLiveId(),liveBean.getLiveType(),
						remind.getCreateUserId(), remind.getCreateTime(),
						liveBean.getLiveMainId(), liveBean.getIsTop());
				json.put("dmReceiverId", ids);
				json.put("dmReceiverName", names);
				json.put("dmReceiverNickname", nicks);
				json.put("dynamicMainContent", remind.getContent());
			}
		}
		return json;
	}
	/**
	 * 5:俱乐部通知
	 * @return
	 */
	public JSONObject getDynamics_5(CenterLive liveBean){
		String noticeRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubNotice.class);
		if(noticeRedis==null){
			return null;
		}
		ClubNotice notice = JSONObject.parseObject(noticeRedis, ClubNotice.class);
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				notice.getCreateUserId(), notice.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle", notice.getTitle());
		json.put("dynamicMainContent", notice.getContent());
		return json;
	}
	/**
	 * 6:俱乐部红包
	 * @return
	 */
	public JSONObject getDynamics_6(CenterLive liveBean){
		String redPacketRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubRedPacket.class);
		if(redPacketRedis==null){
			return null;
		}
		ClubRedPacket  redPacket = JSONObject.parseObject(redPacketRedis, ClubRedPacket.class);
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				redPacket.getCreateUserId(), redPacket.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
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
		json.put("dmReceiverId", ids);
		json.put("dmReceiverName", names);
		json.put("dmReceiverNickname", nicks);
		return json;
	}
	/**
	 * 7:俱乐部邀请答疑；
	 * @return
	 */
	public JSONObject getDynamics_7(CenterLive liveBean){
		//7俱乐部答疑  9教学答疑
		int liveMainType = liveBean.getLiveMainType();
		JSONObject json = null;
		if(liveMainType==7){
			String questionRedis = RedisComponent.findRedisObject(liveBean.getLiveMainId(), ClubTrainingQuestion.class);
			if(null == questionRedis){
				return null;
			}
			ClubTrainingQuestion trainingQuestion = JSONObject.parseObject(questionRedis, ClubTrainingQuestion.class);
			json = commonReturnJsonObjects(
					liveBean.getLiveId(),liveBean.getLiveType(),
					trainingQuestion.getCreateUserId(), trainingQuestion.getCreateTime(),
					liveBean.getLiveMainId(), liveBean.getIsTop());
			json.put("dynamicMainTitle", trainingQuestion.getTitle());
			json.put("dynamicMainContent", trainingQuestion.getContent());
			json.put("dmSupportCount", trainingQuestion.getPraiseNum());
			json.put("classId", String.valueOf(trainingQuestion.getClassId()));
			json.put("classType", GameConstants.CLASS_TYPE_CLUB);
		}else{
			String teachquestion = RedisComponent.findRedisObject(liveBean.getLiveMainId(), TeachQuestion.class);
			if(null == teachquestion){
				return null;
			}
			TeachQuestion teachquestions = JSONObject.parseObject(teachquestion, TeachQuestion.class);
			json = commonReturnJsonObjects(
					liveBean.getLiveId(),liveBean.getLiveType(),
					teachquestions.getCreateUserId(), teachquestions.getCreateTime(),
					liveBean.getLiveMainId(), liveBean.getIsTop());
			json.put("dynamicMainTitle", teachquestions.getTitle());
			json.put("dynamicMainContent", teachquestions.getContent());
			json.put("dmSupportCount", teachquestions.getPraiseNum());
			json.put("classId", String.valueOf(teachquestions.getClassId()));
			json.put("classType", GameConstants.CLASS_TYPE_TEACHING);
		}
		return json;
	}
	/**
	 * 8:教学通知；
	 * @return
	 */
	public JSONObject getDynamics_8(CenterLive liveBean){
		TeachCourseDiscuss teachCourseDiscuss = new TeachCourseDiscuss();
		teachCourseDiscuss.setDiscussId(liveBean.getLiveMainId());
		teachCourseDiscuss = teachCourseDiscussService.getTeachCourseDiscuss(teachCourseDiscuss);
		if(null == teachCourseDiscuss){
			return null;
		}
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				teachCourseDiscuss.getCreateUserId(), teachCourseDiscuss.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle", teachCourseDiscuss.getContent());
		json.put("dynamicMainContent", teachCourseDiscuss.getContent());
		json.put("dmSupportCount", teachCourseDiscuss.getPraiseNum());
		json.put("classId", String.valueOf(teachCourseDiscuss.getClassId()));
		json.put("classType", GameConstants.CLASS_TYPE_CLUB);
		return json;
	}
	/**
	 * 9:教学答疑；
	 * @return
	 */
	public JSONObject getDynamics_9(CenterLive liveBean){
		//获取教学答疑
		TeachQuestion question = new TeachQuestion();
		question.setQuestionId(liveBean.getLiveMainId());
		TeachQuestion resquestion = teachQuestionService.getTeachQuestion(question);
		if(null == resquestion){
			return null;
		}
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				resquestion.getCreateUserId(), resquestion.getCreateTime(),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle", resquestion.getTitle());
		json.put("dynamicMainContent", resquestion.getContent());
		json.put("dmSupportCount", resquestion.getPraiseNum());
		json.put("classId", String.valueOf(resquestion.getClassId()));
		json.put("classType", GameConstants.CLASS_TYPE_CLUB);
		return json;
	}
	/**
	 * 10:教学实训；
	 * @return
	 */
	public JSONObject getDynamics_10(CenterLive liveBean){
		IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
		String resarenaRedis = game.getBaseMatchInfoByMatchId(liveBean.getLiveMainId());
		if(StringUtil.isEmpty(resarenaRedis)){
			return null;
		}
		JSONObject gameJson = JSONObject.parseObject(resarenaRedis);
		int code = gameJson.getInteger("code");
		if(code != 0){
			return null;
		}
		String arenaRedis = gameJson.getString("result");
		JSONObject obj = JSONObject.parseObject(arenaRedis);
		if(null == obj){
			return null;
		}
		JSONObject json = commonReturnJsonObjects(
				liveBean.getLiveId(),liveBean.getLiveType(),
				obj.getInteger("creatorId"),obj.getInteger("createTime"),
				liveBean.getLiveMainId(), liveBean.getIsTop());
		json.put("dynamicMainTitle",obj.getString("matchName"));
		json.put("classId",obj.getString("classId"));
		return json;
	}
	/**
	 * 11:用户关注职位动态；
	 * @return
	 */
	public JSONObject getDynamics_11(CenterLive live,Integer userId){
		String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
		if(attentionRedis == null){
			return null;
		}
		CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
		String dataStr = TimeUtil.getCurrentTime();
		String sign = MD5Utils.getMD5Str(dataStr);
		String param = "entIds="+centerAttention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
		JSONObject companyPacket = BusinessUserService.getCompanyPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
		JSONArray companys = companyPacket.getJSONArray("ents");
		if(companys==null || companys.size()==0){
			return null;
		}
		JSONObject obj = companys.getJSONObject(0);
		CenterCompany centerCompany = new CenterCompany();
		centerCompany.setCompanyId(obj.getString("companyId"));
		CenterCompany rescenterCompany = centerCompanyService.getCenterCompany(centerCompany);
		int supportCount = rescenterCompany == null?0:rescenterCompany.getPraiseNum();
		JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				centerAttention.getCreateUserId(),centerAttention.getCreateTime(),
				null, live.getIsTop());
		json.put("dynamicMainTitle",obj.getString("companyName"));
		String imgLink = obj.getString("companyLogoLink");
		if(StringUtil.isEmpty(imgLink)){
			imgLink = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_ENTERPRISE);
			json.put("companyLogoLink",imgLink);
		}else{
			json.put("companyLogoLink",Common.checkPic(imgLink) == true ? imgLink+ActiveUrl.HEAD_MAP:SystemConfig.getString("COMPANY_LOGO_LINK")+imgLink);
		}
		json.put("postCount", obj.getInteger("postCount"));
		json.put("postTypeCount", obj.getInteger("postTypeCount"));
		json.put("supportCount", supportCount);
		json.put("companyDetailLink", obj.getString("companyDetailLink"));
		json.put("dynamicMainId", centerAttention.getRelObjetId());
		//通过企业id和userId查询关注表判断当前登录人是否关注了该职位
				int hasBeenAttention = 0;
				if(centerAttention.getRelObjetId() != null){
					// 校验当前登录人是否关注过当前企业
					CenterAttention attention = centerAttentionDao.selectSingleCenterAttention(new CenterAttention(userId, 3,centerAttention.getRelObjetId()) );
					if(attention != null){
						hasBeenAttention =1;
					}else {
						hasBeenAttention = 0;
					}
				}
				json.put("hasBeenAttention", hasBeenAttention);
		return json;
	}
	/**
	 * 12:用户关注俱乐部动态；
	 * @return
	 */
	public JSONObject getDynamics_12(CenterLive live){
		String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
		if(attentionRedis == null){
			return null;
		}
		CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
		//获取俱乐部信息
		String clubMainRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), ClubMain.class);
		if(null == clubMainRedis){
			return null;
		}
		ClubMain main = JSONObject.parseObject(clubMainRedis, ClubMain.class);
		JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				centerAttention.getCreateUserId(),centerAttention.getCreateTime(),
				main.getClubId(), live.getIsTop());
		json.put("dynamicMainTitle",main.getClubName());
		return json;
	}
	/**
	 * 13:用户关注人动态；
	 * @return
	 */
	public JSONObject getDynamics_13(CenterLive live){
		String attentionRedis = RedisComponent.findRedisObject(live.getLiveMainId(), CenterAttention.class);
		if(attentionRedis == null){
			return null;
		}
		CenterAttention centerAttention = JSONObject.parseObject(attentionRedis, CenterAttention.class);
		JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				centerAttention.getCreateUserId(),centerAttention.getCreateTime(),
				live.getLiveMainId(), live.getIsTop());
		//我关注的人的信息
		String otherUserRedis = RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), CenterUser.class);
		if(null == otherUserRedis){
			return null;
		}
		CenterUser otherUser = JSONObject.parseObject(otherUserRedis, CenterUser.class);
		String msg = Common.getImgUrl(otherUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
		json.put("dmCreaterHeadLink", Common.checkPic(msg)== true ? msg+ActiveUrl.HEAD_MAP:msg);
		json.put("dmReceiverName", otherUser.getRealName());
		json.put("dmReceiverNickname", otherUser.getNickName());
		json.put("dmReceiverId", otherUser.getUserId());
		json.put("dmSupportCount", otherUser.getPraiseNum());
		return json;
	}
	/**
	 * 14:用户加入俱乐部动态；
	 * @return
	 */
	public JSONObject getDynamics_14(CenterLive live){
		String clubRedis = RedisComponent.findRedisObject(live.getLiveMainId(), ClubMain.class);
		ClubMain main = JSONObject.parseObject(clubRedis, ClubMain.class);
		if(null == main){
			return null;
		}
		JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				live.getMainObjetId(),live.getCreateTime(),
				live.getLiveMainId(), live.getIsTop());
		json.put("dynamicMainTitle", main.getClubName());
		return json;
	}
	/**
	 * 15:用户参加比赛动态；
	 * @return
	 */
	public JSONObject getDynamics_15(CenterLive live){
		IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
		String arenaRedis = game.getBaseMatchInfoByMatchId(live.getLiveMainId());
		if(StringUtil.isEmpty(arenaRedis)){
			return null;
		}
		JSONObject gameJson = JSONObject.parseObject(arenaRedis);
		String result = gameJson.getString("result");
		JSONObject obj = JSONObject.parseObject(result);
		if(null == obj){
			return null;
		}
		/*JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				obj.getInteger("creatorId"),obj.getInteger("createTime"),
				live.getLiveMainId(), live.getIsTop());*/
		JSONObject json = commonReturnJsonObjects(
				live.getLiveId(),live.getLiveType(),
				live.getMainObjetId(),obj.getInteger("createTime"),
				live.getLiveMainId(), live.getIsTop());
		json.put("dynamicMainTitle", obj.getString("matchName"));
		return json;
	}
	
}
