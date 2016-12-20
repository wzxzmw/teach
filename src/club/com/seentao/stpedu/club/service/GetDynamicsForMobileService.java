
package com.seentao.stpedu.club.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.attention.service.CenterAttentionService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubNotice;
import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.ClubJoinTrainingService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubRelRedPacketMemberService;
import com.seentao.stpedu.common.service.ClubRelRemindMemberService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.HttpRequest;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class GetDynamicsForMobileService {
	/** 
	* @Fields centerLiveService : 动态服务
	*/ 
	@Autowired
	private CenterLiveService centerLiveService;
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;
	/** 
	* @Fields clubRelRemindMemberService : 俱乐部提醒会员关系服务
	*/ 
	@Autowired
	private ClubRelRemindMemberService clubRelRemindMemberService;
	/** 
	* @Fields clubRelRedPacketMemberService : 红包会员关系列表
	*/ 
	@Autowired
	private ClubRelRedPacketMemberService clubRelRedPacketMemberService;
	/** 
	* @Fields centerAttentionService : 关注服务
	*/ 
	@Autowired
	private CenterAttentionService centerAttentionService;
	/** 
	* @Fields centerLiveService : 企业服务
	*/ 
	@Autowired
	private CenterCompanyService centerCompanyService;
	@Autowired
	private CenterAttentionDao centerAttentionDao;
	/** 
	* @Fields clubMemberService : 俱乐部会员服务
	*/ 
	@Autowired
	private ClubMemberService clubMemberService;
	/*
	 * 获取动态信息
	 */
	public String getDynamicsSomes(Integer userId,Integer clubId,Integer start,Integer limit,Integer inquireType){
		switch (inquireType) {
		case 1:
			/*
			 * 我的俱乐部主页，获取俱乐部的动态
			 */
			return Common.getReturn(AppErrorCode.SUCCESS, "", getClubDynamic(userId, clubId, start, limit)).toJSONString();
		case 2:
			/*
			 * 动态模块获取关注的人的动态
			 */
			return Common.getReturn(AppErrorCode.SUCCESS, "", getUserDynamic(userId, start, limit)).toJSONString();
		default:
			return null;
		}
	}
	/*
	 * 我的俱乐部主页，获取俱乐部的动态
	 */
	protected JSONObject getClubDynamic(Integer userId,Integer clubId,Integer start,Integer limit){
		JSONArray array = new JSONArray();
		QueryPage<CenterLive> queryPage = centerLiveService.getClubCenterLive(clubId, start, limit);
		/*
		 * 获取俱乐部
		 */
		StringBuffer gameIds = new StringBuffer();
		List<CenterLive> centers = new LinkedList<>();
		for(CenterLive centerLive :queryPage.getList()){
			String redisLive = RedisComponent.findRedisObject(centerLive.getLiveId(), CenterLive.class);
			centerLive = JSONObject.parseObject(redisLive, CenterLive.class);
			centers.add(centerLive);
			/*
			 * 动态类型
			 */
			int liveType = centerLive.getLiveType().intValue();
			switch (liveType) {
			case 1:
				JSONObject obj = getClubTopic(centerLive,clubId);
				if(obj != null)
					array.add(obj);
				break;
			case 2:
				gameIds.append(centerLive.getLiveMainId()+",");
				break;
			case 3:
				//俱乐部培训
				JSONObject trainObj = getClubTraining(centerLive, clubId, userId);
				if(trainObj != null)
					array.add(trainObj);
				break;
			case 4:
				//俱乐部提醒
				JSONObject remindObj = getRemind(centerLive, clubId, userId);
				if(remindObj != null)
					array.add(remindObj);
				break;
			case 5:
				//俱乐部通知
				JSONObject noteObj = getClubNotice(centerLive, clubId, userId);
				if(noteObj != null)
					array.add(noteObj);
				break;
			case 6:
				//俱乐部红包
				JSONObject clubRedObj = getClubRedEnvelopes(centerLive, clubId, userId);
				if(clubRedObj != null)
					array.add(clubRedObj);
				break;
			case 7:
				JSONObject answerObj = getClubInviteAnswer(centerLive, clubId, userId);
				if(answerObj != null)
					array.add(answerObj);
				break;
			default:
				break;
			}
		}
		if(gameIds.length() !=0){
			getClubChallenge(centers,array,gameIds.toString().substring(0, gameIds.length()-1));
		}
			
		JSONObject json = new JSONObject();
		json.put("returnCount", queryPage.getCount());
		json.put("allPage", queryPage.getAllPage());
		json.put("currentPage", queryPage.getCurrentPage());
		json.put("dynamics", array);
		return json;
	}
	/*
	 * 动态模块获取关注的人的动态
	 */
	protected JSONObject getUserDynamic(Integer userId,Integer start,Integer limit){
		JSONArray array = new JSONArray();
		/*
		 * 获取我关注人的动态
		 */
		List<CenterAttention> attentions = centerAttentionService.getCenterAttentionByCreateUserId(new CenterAttention(userId, 1) );
		if(CollectionUtils.isEmpty(attentions)){
			JSONObject json = new JSONObject();
			json.put("returnCount", 0);
			json.put("allPage", 0);
			json.put("currentPage", 0);
			json.put("dynamics", new JSONArray());
			return json;
		}
		StringBuffer buffer = new StringBuffer();
		for(CenterAttention centerAttention :attentions){
			buffer.append(centerAttention.getRelObjetId()+",");
		}
		QueryPage<CenterLive> centerLives = centerLiveService.getAttentionUserCenterLive(buffer.substring(0, buffer.length()-1), start, limit);
		StringBuffer buffers = new StringBuffer();
		List<CenterLive> centers = new LinkedList<>();
		for(CenterLive centerLive : centerLives.getList()){
			String redisLive = RedisComponent.findRedisObject(centerLive.getLiveId(), CenterLive.class);
			centerLive = JSONObject.parseObject(redisLive, CenterLive.class);
			centers.add(centerLive);
			int liveType = centerLive.getLiveType().intValue();
			CenterAttention centerAttention = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), CenterAttention.class), CenterAttention.class);
			switch (liveType) {
			case 11:
				// 对企业的动态
				JSONObject attenObj = getUserAttentionPosition(centerLive, centerAttention, userId);
				if(attenObj != null)
					array.add(attenObj);
				break;
			case 12:
				JSONObject attJoinClub = getUserAttentionClub(centerLive, centerAttention, userId);
				if(attJoinClub != null)
					array.add(attJoinClub);
				break;
			case 13:
				JSONObject attenUser = getUserAttentionUser(centerLive, centerAttention, userId);
				if(attenUser != null)
					array.add(attenUser);
				break;
			case 14:
				JSONObject userJoinJson = getUserJoinClub(centerLive, userId);
				if(userJoinJson != null)
					array.add(userJoinJson);
				break;
			case 15:
				buffers.append(centerLive.getLiveMainId()+",");
				break;
			default:
				break;
			}
		}
		if(buffers.length() !=0){
			commonGame(buffers.toString().substring(0, buffers.length()-1),centers, array);
		}
		JSONObject json = new JSONObject();
		json.put("returnCount", centerLives.getCount());
		json.put("allPage", centerLives.getAllPage());
		json.put("currentPage", centerLives.getCurrentPage());
		json.put("dynamics", array);
		return json;
	}
	/*
	 * 1、俱乐部话题
	 */
	protected JSONObject getClubTopic(CenterLive centerLive,Integer clubId){
		/*
		 * 获取俱乐部话题
		 */
		ClubTopic  clubTopic = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTopic.class), ClubTopic.class);
		
		/*
		 * 当俱乐部话题为空
		 */
		if(clubTopic == null)
			return null;
		
			CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubTopic.getCreateUserId(), CenterUser.class), CenterUser.class);
			JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, RichTextUtil.delHTMLTag(clubTopic.getContent()), clubTopic.getTitle(), clubTopic.getCreateTime(), clubTopic.getTopicId());
			obj.put("clubId", clubId);
			obj.put("dmSupportCount", clubTopic.getPraiseNum());
			obj.put("dmCommentCount", clubTopic.getCommentNum());
			/*
			 * 当动态类型为1:俱乐部话题的时候返回该字段	
			 */
			return obj;
	}
	/*
	 *2、、俱乐部擂台 
	 */
	protected void getClubChallenge(List<CenterLive> centerLives,JSONArray array,String gameIds){
		commonGame(gameIds, centerLives, array);
	}
	/*
	 *3、:俱乐部培训
	 */
	protected JSONObject getClubTraining(CenterLive centerLive,Integer clubId,Integer userId){
		/*
		 * 获取俱乐部培训
		 */
		ClubTrainingClass clubTrainingClass = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTrainingClass.class), ClubTrainingClass.class);
		if(clubTrainingClass == null)
			return null;
		
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubTrainingClass.getCreateUserId(), CenterUser.class), CenterUser.class);
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, clubTrainingClass.getIntroduce(), clubTrainingClass.getTitle(), clubTrainingClass.getCreateTime(), clubTrainingClass.getClassId());
		/*
		 * 1:是；0:否；默认是0；
		当动态类型为3：俱乐部培训的时候返回该字段	
		 */
		ClubJoinTraining res = clubJoinTrainingService.getClubJoinTrainingAll(new ClubJoinTraining(0, userId, clubTrainingClass.getClassId()));
		if(res == null){
			obj.put("hasJoinClass", 0);
		}else{
			// 如果不为空则返回为1
			obj.put("hasJoinClass", 1);
			
		}
		/*
		 * 当动态类型为3:培训的时候返回该字段
		 */
		obj.put("className", clubTrainingClass.getTitle());
		return obj;
	}
	/*
	 * 4、俱乐部提醒
	 */
	protected JSONObject getRemind(CenterLive centerLive,Integer clubId,Integer userId){
		// 俱乐部提醒
		ClubRemind clubRemind = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubRemind.class), ClubRemind.class);
		if(clubRemind == null)
			return null;
		/*
		 * 获取动态主体接受者
		 */
		List<ClubRelRemindMember> relReminds = clubRelRemindMemberService.getClubRelRemindMemberByRemindId(clubRemind.getRemindId());
		if(CollectionUtils.isEmpty(relReminds))
			return null;
		/*
		 * 动态主体接受者id
		 */
		StringBuffer receiverIds= new StringBuffer();
		/*
		 * 动态主体接受者姓名
		 */
		StringBuffer receiverNames = new StringBuffer();
		/*
		 * 动态主体接受者昵称
		 */
		StringBuffer receiverNicknames = new StringBuffer();
		for(ClubRelRemindMember clubRelRemindMember :relReminds){
			receiverIds.append(clubRelRemindMember.getUserId()+",");
			CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubRelRemindMember.getUserId(), CenterUser.class), CenterUser.class);
			receiverNames.append(centerUser.getRealName()+",");
			receiverNicknames.append(centerUser.getNickName()+",");
		}
		//获取俱乐部提醒发布者,
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubRemind.getCreateUserId(), CenterUser.class), CenterUser.class);
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser,clubRemind.getContent(),"", clubRemind.getCreateTime(), clubRemind.getRemindId());
		CommonMobileParamters.buildCommonParameters(obj, receiverIds.toString(), receiverNames.toString(), receiverNicknames.toString());
		return obj;
	}
	/*
	 *5、俱乐部通知
	 */
	protected JSONObject getClubNotice(CenterLive centerLive,Integer clubId,Integer userId){
		//获取俱乐部通知
		ClubNotice clubNotice = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubNotice.class), ClubNotice.class);
			if(clubNotice == null)
				return null;
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubNotice.getCreateUserId(), CenterUser.class), CenterUser.class);
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, clubNotice.getContent(), clubNotice.getTitle(),clubNotice.getCreateTime(), clubNotice.getNoticeId());
		return obj;
	}
	/*
	 * 6:俱乐部红包，
	 */
	protected JSONObject getClubRedEnvelopes(CenterLive centerLive,Integer clubId,Integer userId){
		ClubRedPacket clubRedPacket = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubRedPacket.class), ClubRedPacket.class);
		if(clubRedPacket == null)
			return null;
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubRedPacket.getCreateUserId(), CenterUser.class), CenterUser.class);
		List<ClubRelRedPacketMember> relRedPackets = clubRelRedPacketMemberService.getClubRelRemindMemberByRedPackId(clubRedPacket.getRedPacketId());
		if(CollectionUtils.isEmpty(relRedPackets))
			return null;
		/*
		 * 动态主体接受者id
		 */
		StringBuffer receiverIds= new StringBuffer();
		/*
		 * 动态主体接受者姓名
		 */
		StringBuffer receiverNames = new StringBuffer();
		/*
		 * 动态主体接受者昵称
		 */
		StringBuffer receiverNicknames = new StringBuffer();
		for(ClubRelRedPacketMember clubRelRedPacketMember:relRedPackets){
			receiverIds.append(clubRelRedPacketMember.getUserId()+",");
			CenterUser center = JSONObject.parseObject(RedisComponent.findRedisObject(clubRelRedPacketMember.getUserId(), CenterUser.class), CenterUser.class);
			receiverNames.append(center.getRealName()+",");
			receiverNicknames.append(center.getNickName()+",");
		}
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, "", "", clubRedPacket.getCreateTime(), clubRedPacket.getRedPacketId());
		CommonMobileParamters.buildCommonParameters(obj, receiverIds.toString(), receiverNames.toString(), receiverNicknames.toString());
		return obj;
	}
	/*
	 * 7:俱乐部邀请答疑
	 */
	protected JSONObject getClubInviteAnswer(CenterLive centerLive,Integer clubId,Integer userId){
		ClubTrainingQuestion clubTrainingQuestion = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubTrainingQuestion.class), ClubTrainingQuestion.class);
		if(clubTrainingQuestion == null)
			return null;
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(clubTrainingQuestion.getCreateUserId(), CenterUser.class), CenterUser.class);
		if(centerUser == null)
			return null;
		//获得班级
		TeachClass teachClass = JSONObject.parseObject(RedisComponent.findRedisObject(clubTrainingQuestion.getClassId(), TeachClass.class), TeachClass.class);
		if(teachClass == null)
			return null;
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, clubTrainingQuestion.getContent(), clubTrainingQuestion.getTitle(), clubTrainingQuestion.getCreateTime(), clubTrainingQuestion.getQuestionId());
		/*
		 * classId
		 */
		obj.put("classId", teachClass.getClassId());
		/*
		 * classType
		 */
		obj.put("classType", teachClass.getClassType());
		return obj;
	}
	/*
	 * 8:教学通知
	 */
	protected JSONObject getTeachingNotice(){
		JSONObject obj = CommonMobileParamters.buildMobileParameters(null, null, "", "", null, null);
		/*
		 * classId
		 */
		obj.put("classId", null);
		/*
		 * classType
		 */
		obj.put("classType", null);
		return obj;
	}
	/*
	 * 9:教学答疑，
	 */
	protected JSONObject getTeachingAnswer(){
		JSONObject obj = CommonMobileParamters.buildMobileParameters(null, null, "", "", null, null);
		/*
		 * classId
		 */
		obj.put("classId", null);
		/*
		 * classType
		 */
		obj.put("classType", null);
		return obj;
	}
	/*
	 * 10:教学实训
	 */
	protected JSONObject getTeachingTraining(){
		JSONObject obj = CommonMobileParamters.buildMobileParameters(null, null, "", "", null, null);
		/*
		 * classId
		 */
		obj.put("classId", null);
		return obj;
	}
	/*
	 * 11:用户关注职位动态
	 */
	//TODO
	protected JSONObject getUserAttentionPosition(CenterLive centerLive,CenterAttention centerAttention,Integer userId){
		if(centerAttention == null)
			return null;
		/**判断当前登录者他是否是企业的动态*/
		if(centerAttention.getRelObjetType() != 3)
			return null;
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class), CenterUser.class);
		String dataStr = TimeUtil.getCurrentTime();
		String sign = MD5Utils.getMD5Str(dataStr);
		String param = "entIds="+centerAttention.getRelObjetId()+"&sign="+sign+"&dataStr="+dataStr;
		JSONArray jsonArray = JSONObject.parseObject(HttpRequest.sendPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param)).getJSONArray("ents");
		int supportCount = 0;
		int hasBeenAttention = 0;
		for(Object object : jsonArray){
			JSONObject obj = (JSONObject)object;
			CenterCompany centerCompany = centerCompanyService.getCenterCompany(new CenterCompany(obj.getString("companyId")));
			if(centerCompany != null){
				supportCount = centerCompany.getPraiseNum();
			}else{
				supportCount =0;
			}
			// 校验当前登录人是否关注过当前企业
			CenterAttention attention = centerAttentionDao.selectSingleCenterAttention(new CenterAttention(userId, 3, obj.getString("companyId")) );
			if(attention != null){
				hasBeenAttention =1;
			}else {
				hasBeenAttention = 0;
			}
			JSONObject json = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, "", obj.getString("companyName"), centerAttention.getCreateTime(), centerLive.getLiveMainId());
			String _pic = RedisComponent.findRedisObjectPublicPicture(null, BusinessConstant.DEFAULT_IMG_ENTERPRISE);
			PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
			//企业Logo链接地址
			json.put("companyLogoLink", obj.getString("companyLogoLink") == null ?pic.getFilePath()+ActiveUrl.HEAD_MAP:Common.checkPic(obj.getString("companyLogoLink")) == true ?obj.getString("companyLogoLink")+ActiveUrl.HEAD_MAP:obj.getString("companyLogoLink"));
			//在招岗位数量
			json.put("postCount", obj.getInteger("postCount") == null ? 0:obj.getInteger("postCount"));
			//岗位类别数量
			json.put("postTypeCount", 0);
			//点赞数量
			json.put("supportCount", supportCount);
			//当前登录者是否关注
			json.put("hasBeenAttention", hasBeenAttention);
			//
			json.put("companyDetailLink", obj.getString("companyDetailLink"));
			return json;
			
		}
		return null;
	}
	/*
	 * 12:用户关注俱乐部动态，
	 */
	protected JSONObject getUserAttentionClub(CenterLive centerLive,CenterAttention centerAttention,Integer userId){
		if(centerAttention == null)
			return null;
		if(centerAttention.getRelObjetType() !=2)
			return null;
		ClubMain clubMain  = JSONObject.parseObject(RedisComponent.findRedisObject(Integer.parseInt(centerAttention.getRelObjetId()), ClubMain.class), ClubMain.class);
		if(clubMain == null)
			return null;
		//获取关注的创建者
		CenterUser centerUser  = JSONObject.parseObject(RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class), CenterUser.class);
		
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, "", clubMain.getClubName(), centerAttention.getCreateTime(), clubMain.getClubId());
		/*
		 * hasJoinClub 当前登录者是否已加入该俱乐部
		 */
		ClubMember clubMember = clubMemberService.queryClubMemberByClubIdAndUserId(userId, clubMain.getClubId());
		if(clubMember == null){
			obj.put("hasJoinClub", 0);
		}else{
			obj.put("hasJoinClub", 1);
		}
		/*
		 * clubName
		 */
		obj.put("clubName", clubMain.getClubName());
		return obj;
	}
	/*
	 * 13:用户关注人动态
	 */
	protected JSONObject getUserAttentionUser(CenterLive centerLive,CenterAttention centerAttention,Integer userId){
		if(centerAttention == null)
			return null;
		if(centerAttention.getRelObjetType() != 1)
			return null;
		/*
		 * 当前登录者关注人的动态 为动态主体的发布者
		 */
		CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(centerAttention.getCreateUserId(), CenterUser.class), CenterUser.class);
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, "", "", centerAttention.getCreateTime(), centerUser.getUserId());
		//动态主体的接收者id
		obj.put("dmReceiverId", centerLive.getLiveMainId());
		CenterUser us = JSONObject.parseObject(RedisComponent.findRedisObject(Integer.valueOf(centerAttention.getRelObjetId()), CenterUser.class), CenterUser.class);;
		// 动态主体的接收者姓名
		obj.put("dmReceiverName", us==null ?"" :us.getRealName());
		//动态主体的接收者昵称
		obj.put("dmReceiverNickname", us==null ?"":us.getNickName());
		return obj;
	}
	/*
	 * 14:用户加入俱乐部动态
	 */
	protected JSONObject getUserJoinClub(CenterLive centerLive,Integer userId){
		ClubMain main = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getLiveMainId(), ClubMain.class), ClubMain.class);
		CenterUser centerUser  = JSONObject.parseObject(RedisComponent.findRedisObject(centerLive.getMainObjetId(), CenterUser.class), CenterUser.class);
		JSONObject obj = CommonMobileParamters.buildMobileParameters(centerLive, centerUser, "", main.getClubName(), centerLive.getCreateTime(),main.getClubId());
		/*
		 * hasJoinClub 当前登录者是否已加入该俱乐部
		 */
		ClubMember clubMember = clubMemberService.queryClubMemberByClubIdAndUserId(userId, main.getClubId());
		if(clubMember == null){
			obj.put("hasJoinClub", 0);
		}else{
			obj.put("hasJoinClub", 1);
		}
		/*
		 * clubName
		 */
		obj.put("clubName", main.getClubName());
		return obj;
	}
	/*
	 * 15:用户参加比赛动态。
	 */
	protected void  getUserJoinGame(String gameIds,List<CenterLive> centerLives,JSONArray array){
		commonGame(gameIds, centerLives, array);
	}
	/*
	 * 比赛公共
	 */
	protected void commonGame(String gameIds,List<CenterLive> centerLives,JSONArray array){
		IGameInterfaceService  game= HproseRPC.getRomoteClass(ActiveUrl.REQUESTMATCHINFO_URL, IGameInterfaceService.class);
		String games = game.getBaseMatchInfoByMatchIds(gameIds);
		JSONObject gameJson = JSONObject.parseObject(games);
		if(gameJson != null){
			int code = gameJson.getInteger("code");
			if(code == 0){
			String gameResult =   gameJson.getString("result");
			JSONArray jsonArray = JSONArray.parseArray(gameResult);
				for(Object object:jsonArray){
					for(CenterLive centerLive :centerLives){
						JSONObject obj = (JSONObject)object;
						if(obj.getInteger("matchId").equals(centerLive.getLiveMainId())){
							CenterUser centerUser = JSONObject.parseObject(RedisComponent.findRedisObject(obj.getInteger("creatorId"), CenterUser.class), CenterUser.class);
							JSONObject json = CommonMobileParamters.buildMobileParameters(centerLive, centerUser,"", obj==null?"":obj.getString("matchName"), obj.getInteger("createTime"), centerLive.getLiveMainId());
							int matchType = obj.getInteger("matchType");
							if(matchType == 3){
								json.put("gameType", 4);
							}else{
								json.put("gameType", 3);
							}
							array.add(json);
							/*
							 * 当比赛id和动态liveMainId相等则结束内部循环
							 */
							break;
						}
					}
				}
			}
		}
		
	}
}
