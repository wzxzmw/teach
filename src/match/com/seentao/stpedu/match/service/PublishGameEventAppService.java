package com.seentao.stpedu.match.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.service.ArenaCompetitionService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class PublishGameEventAppService {

	
	
	@Autowired
	private ArenaCompetitionService service;
	@Autowired
	private ClubMemberService clubMemberService;
	
	/**
	 * 提交赛事信息
	 * @param userId					用户id
	 * @param gameEventId				赛事id
	 * @param gameEventTitle			赛事标标题
	 * @param gameEventLinkId			赛事宣传图片id
	 * @param gameEventStartDate		赛事开始时间
	 * @param gameEventEndDate			赛事结束时间
	 * @param gameEventIntroduce		赛事介绍
	 * @param actionType				操作提交类型
	 * @return
	 * @author 							lw
	 * @date							2016年6月25日  上午9:26:28
	 */
	public String submitGameEvent(Integer userId, Integer gameEventId, String gameEventTitle, Integer gameEventLinkId,
			Integer gameEventStartDate, Integer gameEventEndDate, String gameEventIntroduce, Integer actionType) {
		
		//	1. 获取用户信息
		String redisTmp = RedisComponent.findRedisObject(userId,CenterUser.class);
		if(StringUtil.isEmpty(redisTmp)){
			LogUtil.error(this.getClass(), "createArenaCompetition", AppErrorCode.ERROR_GAME_EVENT_USER);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_USER).toJSONString();
		}
		CenterUser user = JSONObject.parseObject(redisTmp, CenterUser.class);
		
		//	2. 判断有没加入俱乐部
		if(user.getClubId() == null){
			LogUtil.error(this.getClass(), "createArenaCompetition", AppErrorCode.ERROR_GAME_EVENT_OUT_CLUB);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_OUT_CLUB).toJSONString();
		}
		
		//  获取俱乐部
		redisTmp = RedisComponent.findRedisObject(user.getClubId(),ClubMain.class);
		if(StringUtil.isEmpty(redisTmp)){
			LogUtil.error(this.getClass(), "createArenaCompetition", AppErrorCode.ERROR_GAME_EVENT_OUT_CLUB);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_OUT_CLUB).toJSONString();
		}
		ClubMain club = JSONObject.parseObject(redisTmp, ClubMain.class);
		
		//  校验是否是赛事创建者
		if(club.getCreateUserId().intValue() != userId){
			LogUtil.error(this.getClass(), "createArenaCompetition", AppErrorCode.ERROR_GAME_EVENT_OUT_PRESIDENT_OR_CREATECOMP);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_OUT_PRESIDENT_OR_CREATECOMP).toJSONString();
		}
		
		//	3. 创建赛事
		if(gameEventId == null || gameEventId <= 0 ){
			
			//	3.1 创建赛事提交内容校验
			if( gameEventTitle == null || (gameEventTitle = gameEventTitle.trim()).length() <= 0 || gameEventIntroduce == null || gameEventStartDate == null || gameEventEndDate == null){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_INSERT_NULL);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_INSERT_NULL).toJSONString();
			}
			
			//	3.1.2 创建赛事时间校验
			if(gameEventEndDate <= gameEventStartDate){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_TIME);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_TIME).toJSONString();
			}
			
			//	3.1.3 创建赛事内容校验
			if(gameEventIntroduce.length() > 5000){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.CONTENT_LENGTH_TO_LONG);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CONTENT_LENGTH_TO_LONG).toJSONString();
			}
			
			// 校验赛事创建标题长度
			if(gameEventTitle.length() > 30){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_TITLE);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_TITLE).toJSONString();
			}
			
			//	3.1.4 判断用户身份 ： 会长 并且没有创建赛事
			ClubMember member = new ClubMember();
			member.setClubId(user.getClubId());
			member.setUserId(userId);
			member.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
			member.setLevel(GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT);
			member.setStart(0);
			member = clubMemberService.checkPresidentAndNoArenaCompetition(member);
			if(member == null ){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_EVENT_OUT_PRESIDENT_OR_CREATECOMP);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_OUT_PRESIDENT_OR_CREATECOMP).toJSONString();
			}
			
			//	3.1.5 查询俱乐部对象
			String clubRedis = RedisComponent.findRedisObject(member.getClubId(), ClubMain.class);
			if(StringUtil.isEmpty(clubRedis)){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_EVENT_CLUB_BEAN);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_CLUB_BEAN).toJSONString();
			}
			ClubMain cub = JSONObject.parseObject(clubRedis, ClubMain.class);
			
			//	3.1.6 检验是否购买赛事运营权
			if(GameConstants.CLUB_BUY_COMPETITION != cub.getIsBuyCompetition()){
				LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_EVENT_NOT_BUY);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_NOT_BUY).toJSONString();
			}
			
			//	3.1.7 创建赛事
			return service.createArenaCompetition(userId, gameEventTitle, gameEventLinkId, gameEventStartDate, 
					gameEventEndDate, gameEventIntroduce, member.getClubId()).toJSONString();
			
		}else{
			
			//	3.2.1 修改赛事信息
			return service.updateArenaCompetitionInfo(gameEventId, actionType, gameEventLinkId, gameEventEndDate, 
					gameEventStartDate, gameEventTitle, gameEventIntroduce);
			
		}
		
		
	}
	
	
	
	
	
	
}
