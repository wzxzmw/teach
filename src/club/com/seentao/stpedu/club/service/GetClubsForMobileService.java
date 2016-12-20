package com.seentao.stpedu.club.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ArenaCompetitionDao;
import com.seentao.stpedu.common.dao.ArenaJoinClubDao;
import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.dao.CenterUserDao;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.PublicRegion;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;
/**
 * @author cxw
 */
@Service
public class GetClubsForMobileService {

	@Autowired
	private ClubMemberDao clubMemberDao;
	@Autowired
	private CenterAttentionDao attentionDao;
	@Autowired
	private ArenaJoinClubDao arenaJoinClubDao;
	@Autowired
	private CenterUserDao centerUserDao;
	@Autowired
	private ArenaCompetitionDao arenaCompetitionDao;
    @Transactional
	public String getClubsForMobileInfo(String userId, Integer userType, String memberId,int hasConcernTheClub, String searchWord, String clubId, int start, int limit, int recordingActivity, int inquireType){

		if(inquireType == GameConstants.GET_CLUB_INFORMATION){
			if(hasConcernTheClub == GameConstants.REL_OBJECT_NOT_CONCERNED || hasConcernTheClub == GameConstants.REL_OBJECT_CLUB_ALL){
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("clubName", searchWord);
				//调用分页组件
				QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryCountclubmain","queryByAgeclubmain");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
				}

				for (ClubMain clubMain : appQueryPage.getList()) {
					ClubMember clubMember = new ClubMember();
					clubMember.setClubId(clubMain.getClubId());
					//取会员状态字段（俱乐部模块角色）
					ClubMember list = clubMemberDao.selectClubMember(clubMember);
					if(null == list){
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}else{
						if(list.getUserId()== Integer.parseInt(userId)){
							clubMain.setClubRole(list.getLevel());
						}else{
							clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
						}
					}
					//查询是否已关注俱乐部
					CenterAttention attention = new CenterAttention();
					attention.setCreateUserId(Integer.valueOf(userId));
					attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
					//是否被关注
					List<CenterAttention> centerAttention = attentionDao.selectCenterAfollow(attention);
					if(centerAttention.size()>=0){
						boolean a = false;
						for (CenterAttention centerAttention2 : centerAttention) {
							if(Integer.valueOf(centerAttention2.getRelObjetId())==clubMain.getClubId()){
								a= true;
								break;
							}
						}
						if(a){
							clubMain.setHasBeenAttention(GameConstants.YES);
						}else{
							clubMain.setHasBeenAttention(GameConstants.NO);
						}
					}else{
						clubMain.setHasBeenAttention(GameConstants.NO);
					}

					//根据logoid查询logo链接地址
					//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
					String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
					PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
					/*
					 * 压缩图片
					 */
					clubMain.setClubLogoLink(Common.checkPic(pic.getFilePath()) == true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
				}
				return appQueryPage.getMessageJSONObject("clubs").toJSONString();
			}else if(hasConcernTheClub == GameConstants.REL_OBJECT_ALREADY_CONCERNED){
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(Integer.valueOf(userId));
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				List<CenterAttention> list = attentionDao.selectCenterAttention(attention);
				if(null == list || list.size()<=0){
					LogUtil.error(this.getClass(), "getClubsInfo", "memberId关注的俱乐部信息为空");
					JSONObject json = new JSONObject();
					json.put("clubs", new JSONArray());
					return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
				}
				StringBuffer sb = new StringBuffer();
				for(CenterAttention en : list){
					sb.append(en.getRelObjetId()).append(",");
				}
				String ids = sb.toString();
				ids = ids.substring(0, ids.length()-1);

				//根据已关注俱乐部id查询俱乐部信息
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("clubId", ids);
				//调用分页组件
				QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryCountclubfollow","queryByclubmainfollow");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
				}
				for (ClubMain clubMain : appQueryPage.getList()) {
					ClubMember clubMember = new ClubMember();
					clubMember.setClubId(clubMain.getClubId());
					//取会员状态字段（俱乐部模块角色）
					ClubMember list1 = clubMemberDao.selectClubMember(clubMember);
					if(null == list1){
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}else{
						if(list1.getUserId()== Integer.parseInt(userId)){
							clubMain.setClubRole(list1.getLevel());
						}else{
							clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
						}
					}

					//根据logoid查询logo链接地址
					//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
					String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
					PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
					/*
					 * 图片压缩
					 */
					clubMain.setClubLogoLink(Common.checkPic(pic.getFilePath()) ==true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					clubMain.setHasBeenAttention(GameConstants.YES);
				}
				return appQueryPage.getMessageJSONObject("clubs").toJSONString();
			}
		}else if(inquireType == GameConstants.GET_CLUB_INFORMATION_FORMOBILE){
			//根据userid查询俱乐部会员表获得俱乐部id
			//以俱乐部id和userid为条件查询关注表判断是否已关注
			//2：根据userid查询关注表获取关注的俱乐部id
			if(memberId==null){
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(Integer.valueOf(memberId));
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				List<CenterAttention> list = attentionDao.selectCenterAttention(attention);
				if(null == list || list.size()<=0){
					LogUtil.error(this.getClass(), "getClubsInfo", "memberId关注的俱乐部信息为空");
					JSONObject json = new JSONObject();
					json.put("clubs", new JSONArray());
					json.put("returnCount", 0);
					return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
				}
				StringBuffer sb = new StringBuffer();
				for(CenterAttention en : list){
					sb.append(en.getRelObjetId()).append(",");
				}
				String ids = sb.toString();
				ids = ids.substring(0, ids.length()-1);

				//根据已关注俱乐部id查询俱乐部信息
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("clubId", ids);
				//调用分页组件
				QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryCountclubfollow","queryByclubmainfollow");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
				}
				for (ClubMain clubMain : appQueryPage.getList()) {
					ClubMember clubMember = new ClubMember();
					clubMember.setClubId(clubMain.getClubId());
					//取会员状态字段（俱乐部模块角色）
					ClubMember list1 = clubMemberDao.selectClubMember(clubMember);
					if(null == list1){
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}else{
						if(list1.getUserId()== Integer.parseInt(userId)){
							clubMain.setClubRole(list1.getLevel());
						}else{
							clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
						}
					}

					//根据logoid查询logo链接地址
					//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
					String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
					PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
					clubMain.setClubLogoLink(pic==null ?"":Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					clubMain.setHasBeenAttention(GameConstants.YES);
				}
				return appQueryPage.getMessageJSONObject("clubs").toJSONString();
			}else{
				CenterAttention attention = null;
				attention = new CenterAttention();
				attention.setCreateUserId(Integer.valueOf(memberId));
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				List<CenterAttention> list = attentionDao.selectCenterAttention(attention);
				if(null == list || list.size()<=0){
					LogUtil.error(this.getClass(), "getClubsInfo", "memberId关注的俱乐部信息为空");
					JSONObject json = new JSONObject();
					json.put("clubs", new JSONArray());
					json.put("returnCount", 0);
					return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
				}
				StringBuffer sb = new StringBuffer();
				for(CenterAttention en : list){
					sb.append(en.getRelObjetId()).append(",");
				}
				String ids = sb.toString();
				ids = ids.substring(0, ids.length()-1);

				//根据已关注俱乐部id查询俱乐部信息
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("clubId", ids);
				//调用分页组件
				QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryCountclubfollow","queryByclubmainfollow");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
				}
				for (ClubMain clubMain : appQueryPage.getList()) {
					ClubMember clubMember = new ClubMember();
					clubMember.setClubId(clubMain.getClubId());
					//取会员状态字段（俱乐部模块角色）
					ClubMember list1 = clubMemberDao.selectClubMember(clubMember);
					if(null == list1){
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}else{
						if(list1.getUserId()== Integer.parseInt(userId)){
							clubMain.setClubRole(list1.getLevel());
						}else{
							clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
						}
					}

					//根据logoid查询logo链接地址
					String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
					PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
					/*
					 * 压缩图片
					 */
					
					clubMain.setClubLogoLink(Common.checkPic(pic.getFilePath()) ==true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
					//根据userid获取俱乐部会员表获取俱乐部id
					attention = new CenterAttention();
					attention.setCreateUserId(Integer.valueOf(memberId));
					attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
					List<CenterAttention> Centerlist = attentionDao.selectCenterAttention(attention);

					if(Centerlist.size() > 0){
						clubMain.setHasBeenAttention(GameConstants.YES);
					}else{
						clubMain.setHasBeenAttention(GameConstants.NO);
					}

				}
				return appQueryPage.getMessageJSONObject("clubs").toJSONString();
			}

		}else if(inquireType == GameConstants.GET_HOT_CLUB_INFORMATION_FORMOBILE){
			//查询最热俱乐部信息，根据人员数量
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			//调用分页组件
			QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryClubCount","queryClubCountInfo");
			//msg错误码
			if(!appQueryPage.getState()){
				appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
			} 
			for (ClubMain clubMain : appQueryPage.getList()) {
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(clubMain.getClubId());
				//取会员状态字段（俱乐部模块角色）
				ClubMember list1 = clubMemberDao.selectClubMember(clubMember);
				if(null == list1){
					clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
				}else{
					if(list1.getUserId()== Integer.parseInt(userId)){
						clubMain.setClubRole(list1.getLevel());
					}else{
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}
				}
				//查询是否已关注俱乐部
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(Integer.valueOf(userId));
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				//是否被关注
				List<CenterAttention> centerAttention = attentionDao.selectCenterAfollow(attention);
				if(centerAttention.size()>=0){
					boolean a = false;
					for (CenterAttention centerAttention2 : centerAttention) {
						if(Integer.parseInt(centerAttention2.getRelObjetId())==clubMain.getClubId()){
							a= true;
							break;
						}
					}
					if(a){
						clubMain.setHasBeenAttention(GameConstants.YES);
					}else{
						clubMain.setHasBeenAttention(GameConstants.NO);
					}
				}else{
					clubMain.setHasBeenAttention(GameConstants.NO);
				}

				//根据logoid查询logo链接地址
				//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
				String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
				PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
				clubMain.setClubLogoLink(pic==null ?"":Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			}

			return appQueryPage.getMessageJSONObject("clubs").toJSONString();
		}else if(inquireType == GameConstants.GET_NEW_CLUB_INFORMATION_FORMOBILE){
			//查询最新俱乐部
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			//调用分页组件
			QueryPage<ClubMain> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, ClubMain.class,"queryClubNewCount","queryClubCountNewInfo");
			//msg错误码
			if(!appQueryPage.getState()){
				appQueryPage.error(AppErrorCode.ERROR_CLUBMAIN);
			}
			for (ClubMain clubMain : appQueryPage.getList()) {
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(clubMain.getClubId());
				//取会员状态字段（俱乐部模块角色）
				ClubMember list1 = clubMemberDao.selectClubMember(clubMember);
				if(null == list1){
					clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
				}else{
					if(list1.getUserId()== Integer.parseInt(userId)){
						clubMain.setClubRole(list1.getLevel());
					}else{
						clubMain.setClubRole(GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					}
				}
				//查询是否已关注俱乐部
				CenterAttention attention = new CenterAttention();
				attention.setCreateUserId(Integer.valueOf(userId));
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				//是否被关注
				List<CenterAttention> centerAttention = attentionDao.selectCenterAfollow(attention);
				if(centerAttention.size()>=0){
					boolean a = false;
					for (CenterAttention centerAttention2 : centerAttention) {
						if(Integer.parseInt(centerAttention2.getRelObjetId())==clubMain.getClubId()){
							a= true;
							break;
						}
					}
					if(a){
						clubMain.setHasBeenAttention(GameConstants.YES);
					}else{
						clubMain.setHasBeenAttention(GameConstants.NO);
					}
				}else{
					clubMain.setHasBeenAttention(GameConstants.NO);
				}

				//根据logoid查询logo链接地址
				//clubMain.setClubLogoLink(Common.getImgUrl(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB));
				String _pic = RedisComponent.findRedisObjectPublicPicture(clubMain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
				PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
				clubMain.setClubLogoLink(pic==null ?"":Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			}

			return appQueryPage.getMessageJSONObject("clubs").toJSONString();
		}else if(inquireType == GameConstants.GET_CLUB_INFORMATION_FIVE){
			//我的俱乐部页面，获取我的俱乐部信息;把这种场景单独分出来为了后台统计活跃度使用；
			JSONArray arry = new JSONArray();
			JSONObject json= null;
			try {
				json = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				//获得当前用户信息
				String clubRedis = RedisComponent.findRedisObject(Integer.valueOf(clubId), ClubMain.class);
				if(StringUtils.isEmpty(clubRedis)){
					jsonObject.put("clubs", arry);
					return Common.getReturn(AppErrorCode.SUCCESS, "", jsonObject).toJSONString();
				}
				// 根据俱乐部id查询俱乐部相关信息
				ClubMain clubmaim = JSONObject.parseObject(clubRedis, ClubMain.class);
				//获取昵称用户名
				String centeruserRedis = RedisComponent.findRedisObject(clubmaim.getCreateUserId(), CenterUser.class);
				CenterUser centeruser = JSONObject.parseObject(centeruserRedis, CenterUser.class);
				//根据学校id查询学校表获得学校名称
				String schoolRedis = RedisComponent.findRedisObject(clubmaim.getSchoolId(), TeachSchool.class);
				TeachSchool school = JSONObject.parseObject(schoolRedis, TeachSchool.class);
				//获取粉丝关注人数
				CenterAttention attention = new CenterAttention();
				attention.setRelObjetId(clubId);
				attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_CLUB);
				//查询关注的人数
				Integer fansCountInfo = attentionDao.getFansCountInfo(attention);
				//根据用户id查询关注表是否关注俱乐部
				CenterAttention centerAttention = attentionDao.queryCenterAttentionByUserIdAndClubId(Integer.valueOf(userId), GameConstants.REL_OBJECT_TYPE_CLUB, String.valueOf(clubmaim.getClubId()));
				//俱乐部clubId
				json.put("clubId", clubmaim.getClubId());
				//俱乐部名称
				json.put("clubName", clubmaim.getClubName());
				//俱乐部类型 1、官方,2、非官方
				json.put("clubType", clubmaim.getClubType());
				//俱乐部logo图片id
				json.put("logoId", clubmaim.getLogoId());
				//俱乐部图片链接的地址
				String _pic = RedisComponent.findRedisObjectPublicPicture(clubmaim.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
				PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
				json.put("clubLogoLink",Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath() );
				//俱乐部介绍
				json.put("clubExplain", clubmaim.getClubExplain());
				//当前登录则是否已经关注
				if( null == centerAttention){
					json.put("hasBeenAttention", GameConstants.NO);
				}else{
					json.put("hasBeenAttention", GameConstants.YES);

				}
				//俱乐部模块角色,根据clubId和用户id查询当前人在俱乐部中的状态
				ClubMember me = clubMemberDao.queryClubMemberByClubIdAndUserId(Integer.valueOf(userId), Integer.valueOf(clubId));
				//json.put("clubRole", me.getLevel());
				//俱乐部状态
				json.put("status", clubmaim.getStatus());
				//加入俱乐部的人数
				json.put("memberNum", clubmaim.getMemberNum());
				//俱乐部创建人的id 
				json.put("createUserId", clubmaim.getCreateUserId());
				//俱乐部创建人姓名
				json.put("realName", centeruser.getUserName()==null ?centeruser.getNickName():centeruser.getUserName());
				//俱乐部创建人昵称
				json.put("nickName", centeruser.getNickName());
				// 粉丝人数
				json.put("fansNum", fansCountInfo);
				//当前登录用户申请该俱乐部的状态
				if(null == me ){
					json.put("clubRole", GameConstants.CLUB_MEMBER_STATE_NOJOIN);
					json.put("clubApplyStatus", GameConstants.NO);
				}else{
					json.put("clubRole", me.getLevel());
					if(me.getMemberStatus()==1){
						json.put("clubApplyStatus",2);
					}else if(me.getMemberStatus()==2){
						json.put("clubApplyStatus",1);
					}else if(me.getMemberStatus()==3){
						json.put("clubApplyStatus",3);
					}else if(me.getMemberStatus()==4){
						json.put("clubApplyStatus",0);
					}
				}
				//擂台海报id
				json.put("gameBannerId", clubmaim.getGameBannerId());
				//擂台海报url
				String pc = RedisComponent.findRedisObjectPublicPicture(clubmaim.getGameBannerId(), 0);
				PublicPicture picture = JSONObject.parseObject(pc, PublicPicture.class);
				if(picture == null){
					json.put("gameBannerLink", "");
				}else{
					json.put("gameBannerLink", picture.getFilePath());
				}
				//培训海报id
				json.put("teachBannerId", clubmaim.getTeachBannerId());
				String te = RedisComponent.findRedisObjectPublicPicture(clubmaim.getTeachBannerId(),0);
				PublicPicture ps = JSONObject.parseObject(te, PublicPicture.class);
				if(ps == null){
					json.put("teachingBannerLink", "");
				}else{
					json.put("teachingBannerLink", ps.getFilePath());
				}
				//风格海报id
				json.put("styleBannerId", clubmaim.getStyleBannerId());
				String ts = RedisComponent.findRedisObjectPublicPicture(clubmaim.getStyleBannerId(), 0);
				PublicPicture pr = JSONObject.parseObject(ts, PublicPicture.class);
				if(pr == null){
					json.put("styleBannerLink", "");
				}else{
					json.put("styleBannerLink", pr.getFilePath());
				}
				json.put("bgColorId", String.valueOf(clubmaim.getBgColorId()));
				//String clo = RedisComponent.findRedisObject(clubmaim.getBgColorId(), ClubBackgroundColor.class);
				//ClubBackgroundColor cl = JSONObject.parseObject(clo, ClubBackgroundColor.class);
				json.put("colorValue", clubmaim== null ? "" :String.valueOf(clubmaim.getBgColorId()));
				//会员的加入方式
				json.put("addMemberType", clubmaim.getAddMemberType());
				// 收取的一级货币
				json.put("addAmount", clubmaim.getAddAmount());
				/*// 判断是否有赛事
				ArenaJoinClub arenaJoinClub = new ArenaJoinClub();
				arenaJoinClub.setClubId(Integer.valueOf(clubId));
				ArenaJoinClub joinClub = arenaJoinClubDao.selectArenaJoinClub(arenaJoinClub);*/
				// 判断是否有赛事
				ArenaCompetition joinClub = arenaCompetitionDao.queryArenaCompetitionByClubId(Integer.valueOf(clubId));
				if(joinClub == null){
					json.put("gameEventId","");
				}else{
					json.put("gameEventId", joinClub.getCompId().toString());
				}
				if(joinClub == null){
					json.put("gameEventId","");
				}else{
					json.put("gameEventId", joinClub.getCompId().toString());
				}
				//获取学校名称
				json.put("schoolName", school == null ?"":school.getSchoolName());
				//俱乐部学校所属城市	
				if(school == null){
					json.put("cityId","");
					json.put("provinceId","");
					json.put("schoolId","");
				}else{
					String clubRegionRedis = RedisComponent.findRedisObject(school.getRegionId(), PublicRegion.class);
					PublicRegion clubRegion = JSONObject.parseObject(clubRegionRedis, PublicRegion.class);
					json.put("cityId", clubRegion == null ?"" :clubRegion.getRegionId().toString());
					//俱乐部所属省
					json.put("provinceId", clubRegion == null ?"":clubRegion.getParentId().toString());
					json.put("schoolId",school.getSchoolId());
				}
				//查询俱乐部所在地区
				String clubRegionRedis = RedisComponent.findRedisObject(clubmaim.getRegionId(), PublicRegion.class);
				if(StringUtil.isEmpty(clubRegionRedis)){
					json.put("clubCityId", "");
					json.put("clubProvinceId","");
				}else{
					PublicRegion clubRegion = JSONObject.parseObject(clubRegionRedis, PublicRegion.class);
					json.put("clubCityId", clubRegion==null ?"" :clubRegion.getRegionId().toString());
					json.put("clubProvinceId",clubRegion==null ?"": clubRegion.getParentId().toString());
				}				//俱乐部活跃度+，每天最多+5
				CenterUser centerUser = null;
				centerUser= new CenterUser();
				centerUser.setUserId(Integer.valueOf(userId));
				CenterUser userOne = centerUserDao.selectCenterUserOne(centerUser);
				Integer num = userOne.getClubActiveNum();
				Integer logins = userOne.getLogins();
				Integer todayClubActiveNum = userOne.getTodayClubActiveNum() == null ? 0 : userOne.getTodayClubActiveNum();
				if(todayClubActiveNum<5){
					centerUser.setClubActiveNum(num+1);
					centerUser.setUserId(Integer.valueOf(userId));
					centerUser.setLastLoginTime(TimeUtil.getCurrentTimestamp());
					centerUser.setLogins(logins+1);
					centerUser.setTodayClubActiveNum(todayClubActiveNum+1);
					centerUserDao.updateCenterUserByKey(centerUser);

				}else{

					//解析时间戳
					SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Long dtime=new Long(userOne.getLastLoginTime());  
					//Date datetime = new Date(dtime*1000);
					String time = form.format(dtime*1000);

					//获取时间戳
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dates = sdf.format(new Date());
					//Date parses = sdf.parse(dates);

					int lasttimed = Integer.parseInt(time.substring(8, 10));
					int lasttimem = Integer.parseInt(time.substring(5, 7));
					int noewtimed = Integer.parseInt(dates.substring(8, 10));
					int noewtimem = Integer.parseInt(dates.substring(5, 7));
					//判断时间是否在同一天
					if(noewtimem > lasttimem || noewtimed > lasttimed){
						//清空 当天俱乐部活跃度
						centerUser = new CenterUser();
						centerUser.setUserId(Integer.valueOf(userId));
						centerUser.setTodayClubActiveNum(0);
						centerUserDao.updateCenterUserByKey(centerUser);

						centerUser = new CenterUser();
						centerUser.setUserId(Integer.valueOf(userId));
						CenterUser user = centerUserDao.selectCenterUserTodayNum(centerUser);
						//执行俱乐部+操作
						centerUser = new CenterUser();
						centerUser.setClubActiveNum(num+1);
						centerUser.setUserId(Integer.valueOf(userId));
						centerUser.setLastLoginTime(TimeUtil.getCurrentTimestamp());
						centerUser.setLogins(logins+1);
						centerUser.setTodayClubActiveNum(user.getTodayClubActiveNum()+1);
						centerUserDao.updateCenterUserByKey(centerUser);

					}else{
						centerUser.setUserId(Integer.valueOf(userId));
						centerUser.setLastLoginTime(TimeUtil.getCurrentTimestamp());
						centerUser.setLogins(logins+1);
						centerUserDao.updateCenterUserByKey(centerUser);
					}
				}

				arry.add(json);
				jsonObject.put("clubs", arry);
				return Common.getReturn(AppErrorCode.SUCCESS, "", jsonObject).toJSONString();


			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "getClubsInfo", "获取俱乐部信息失败");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.GET_CLUB_INFORMATION_FAILED).toJSONString();
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
	}
}
