package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class ClubMemberService{

	@Autowired
	private ClubMemberDao clubMemberDao;

	public List<ClubMember> getClubMember(ClubMember clubMember) {
		List<ClubMember> clubMemberList = clubMemberDao .selectSingleClubMember(clubMember);
		if(clubMemberList == null || clubMemberList .size() <= 0){
			return null;
		}

		return clubMemberList;
	}

	public ClubMember getClubMemberOne(ClubMember clubMember) {
		List<ClubMember> clubMemberList = clubMemberDao .selectSingleClubMember(clubMember);
		if(clubMemberList == null || clubMemberList .size() <= 0){
			return null;
		}

		return clubMemberList.get(0);
	}

	public Integer queryCount(ClubMember c) {
		return clubMemberDao.queryCount( c);
	}

	public ClubMember selectClubMember(ClubMember clubMember){
		return  clubMemberDao .selectClubMember(clubMember);
	}
	// 根据俱乐部clubId 和用户id ,以及1,2状态查询
	public ClubMember queryClubMemberByClubIdAndUserId(Integer userId,Integer clubId) {
		return clubMemberDao.queryClubMemberByClubIdAndUserId(userId,clubId);	
	}

	/**clubMemberDao.queryClubMemberByClubIdAndUserId(Integer.valueOf(userId), Integer.valueOf(clubId));
	 * 传入 俱乐部 会员对象  join 关联查询 赛事对象 
	 * 	校验 俱乐部是否创建赛事
	 * @param member
	 * @return
	 * @author 			lw
	 * @date			2016年6月25日  上午10:14:32
	 */
	public ClubMember checkPresidentAndArenaCompetition(ClubMember member) {
		return clubMemberDao.checkPresidentAndArenaCompetition(member);
	}


	/**
	 * 传入 俱乐部 会员对象  join 关联查询 赛事对象 
	 * 	校验 俱乐部是否创建赛事
	 * @param member
	 * @return
	 * @author 			lijin
	 * @date			2016年6月29日  上午10:14:32
	 */
	public boolean validatePresidentAndArenaCompetition(Map<String,Object> conditionMap) {
		return clubMemberDao.validatePresidentAndArenaCompetition(conditionMap);
	}



	/**
	 * 校验用户是否是俱乐部会长，并没有创建赛事
	 * @param member
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午3:00:35
	 */
	public ClubMember checkPresidentAndNoArenaCompetition(ClubMember member) {
		return clubMemberDao.checkPresidentAndArenaCompetition(member);
	}


	/**
	 * 通过用户id 校验权限
	 * @param userId
	 * @param clubMemberLevelPresident
	 * @return
	 * @author 			lijin
	 * @date			2016年6月20日  上午11:57:02
	 */
	public boolean validateUserAndClubAndLevel(Integer userId, String levels) {
		//获取用户信息
		String userRedis = RedisComponent.findRedisObject(userId,CenterUser.class);
		if(StringUtil.isEmpty(userRedis)){
			LogUtil.error(this.getClass(), "validateUserAndClubAndLevel", AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM);
			return false;
		}

		CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
		if(user.getClubId() == null){
			LogUtil.error(this.getClass(), "validateUserAndClubAndLevel", AppErrorCode.ERROR_GAME_EVENT_OUT_CLUB);
			return false;
		}

		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("userId", userId);
		condition.put("clubId", user.getClubId());
		condition.put("levels", levels);

		return this.validatePresidentAndArenaCompetition(condition);
	}

	/** 
	 * @Title: selectClubMemberByClubId 
	 * @Description: 根据俱乐部ID获得除userId以外的所有会员ID列表
	 * @param userId 用户ID
	 * @param clubId 俱乐部ID
	 * @return List<ClubMember>
	 * @author liulin
	 * @date 2016年6月29日 下午8:10:07
	 */
	public List<ClubMember> selectClubMemberByClubId(Integer userId, Integer clubId) {
		ClubMember member = new ClubMember();
		member.setUserId(userId);
		member.setClubId(clubId);
		member.setMemberStatus(1);
		return clubMemberDao.selectClubMemberByClubId(member);
	}

	/** 
	 * @Title: updateClubMemberByMemberId 
	 * @Description: 根据会员ID更改是否有新通知为1
	 * @param members  参数说明 
	 * @return void
	 * @author liulin
	 * @date 2016年6月29日 下午8:25:33
	 */
	public void updateClubMemberByMemberId(List<ClubMember> members) {
		for(ClubMember member : members){
			member.setIsNewNotice(1);
			clubMemberDao.updateClubMemberByKey(member);
			JedisCache.delRedisVal(ClubMember.class.getSimpleName(), String.valueOf(member.getMemberId()));
		}
	}
	/** 
	 * @Title: updateClubMemberByMemberId 
	 * @Description: 根据会员ID更改是否有新通知为1
	 * @param members  参数说明 
	 * @return void
	 * @author liulin
	 * @date 2016年6月29日 下午8:25:33
	 */
	public void updateIsNewRemindByMemberId(List<ClubMember> members) {
		for(ClubMember member : members){
			member.setIsNewRemind(1);
			clubMemberDao.updateClubMemberByKey(member);
			JedisCache.delRedisVal(ClubMember.class.getSimpleName(), String.valueOf(member.getMemberId()));
		}
	}
	/** 
	 * @Description:判断是否有待审核的会员
	 * @param members  参数说明 
	 * @return void
	 * @author lijin
	 * @date 2016年6月30日 下午8:25:33
	 */
	public int checkClubMemberStatuts(ClubMember ClubMember){
		return clubMemberDao.checkClubMemberStatuts(ClubMember);
	}

	public List<ClubMember> getClubMemberList(ClubMember param) {
		return clubMemberDao.getClubMemberList(param) ;
	}

	public Integer getClubMemberCount(ClubMember param) {
		return clubMemberDao. getClubMemberCount( param) ;
	}

	public void batchUpdateByUserIdAndClubId(List<ClubMember> list) {
		clubMemberDao.batchUpdateByUserIdAndClubId(list);
	}

	public void updateByUserIdAndClubId(ClubMember club) {
		clubMemberDao.updateByUserIdAndClubId(club);
	}

	/**
	 * 加入俱乐部会员（审核）
	 * @param userId2
	 * @param clubId
	 * @author cxw
	 */
	public int addClubMember(String userId,String clubId,String applicationContent){
		ClubMember clubMember = new ClubMember();
		clubMember.setUserId(Integer.valueOf(userId));
		clubMember.setClubId(Integer.valueOf(clubId));
		clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_SH);
		clubMember.setLevel(GameConstants.MEMBER);
		clubMember.setRpSendNum(0);
		clubMember.setRpReceiveNum(0);
		clubMember.setTotalReceiveRp1(0);
		clubMember.setTotalReceiveRp2(0);
		clubMember.setApplyExplain(applicationContent);
		clubMember.setIsNewRemind(0);
		clubMember.setIsNewNotice(0);
		clubMember.setIsNewRedPacket(0);
		int member = clubMemberDao.insertClubMember(clubMember);
		return member;

	}

	/***
	 * 修改所加入俱乐部会员信息(审核)
	 * @param userId2
	 * @param clubId
	 * @param memberId
	 * @author cxw
	 */
	public void updateClubMInfo(String userId,String clubId,Integer intMemberId,String applicationContent){
		try {
			ClubMember clubMember = new ClubMember();
			clubMember.setMemberId(intMemberId);
			clubMember.setUserId(Integer.valueOf(userId));
			clubMember.setClubId(Integer.valueOf(clubId));
			clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_SH);
			clubMember.setLevel(GameConstants.MEMBER);
			clubMember.setRpSendNum(0);
			clubMember.setRpReceiveNum(0);
			clubMember.setTotalReceiveRp1(0);
			clubMember.setTotalReceiveRp2(0);
			clubMember.setApplyExplain(applicationContent);
			clubMember.setIsNewRemind(0);
			clubMember.setIsNewNotice(0);
			clubMember.setIsNewRedPacket(0);
			clubMemberDao.updateClubMemberByKey(clubMember);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加入俱乐部会员表
	 * @param userId2
	 * @param clubId
	 * @author cxw
	 */
	public int addClubMemberType(String userId,String clubId){
		ClubMember clubMember = new ClubMember();
		clubMember.setUserId(Integer.valueOf(userId));
		clubMember.setClubId(Integer.valueOf(clubId));
		clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
		clubMember.setLevel(GameConstants.MEMBER);
		clubMember.setRpSendNum(0);
		clubMember.setRpReceiveNum(0);
		clubMember.setTotalReceiveRp1(0);
		clubMember.setTotalReceiveRp2(0);
		clubMember.setApplyExplain(null);
		clubMember.setIsNewRemind(0);
		clubMember.setIsNewNotice(0);
		clubMember.setIsNewRedPacket(0);
		int member = clubMemberDao.insertClubMember(clubMember);
		return member;

	}
	/***
	 * 修改所加入俱乐部会员信息
	 * @param userId2
	 * @param clubId
	 * @param memberId
	 * @author cxw
	 */
	public void updateClubMemberInfo(String userId,String clubId,Integer intMemberId){
		try {
			ClubMember clubMember = new ClubMember();
			clubMember.setMemberId(intMemberId);
			clubMember.setUserId(Integer.valueOf(userId));
			clubMember.setClubId(Integer.valueOf(clubId));
			clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
			clubMember.setLevel(GameConstants.MEMBER);
			clubMember.setRpSendNum(0);
			clubMember.setRpReceiveNum(0);
			clubMember.setTotalReceiveRp1(0);
			clubMember.setTotalReceiveRp2(0);
			clubMember.setApplyExplain(null);
			clubMember.setIsNewRemind(0);
			clubMember.setIsNewNotice(0);
			clubMember.setIsNewRedPacket(0);
			clubMemberDao.updateClubMemberByKey(clubMember);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<ClubMember> getClubMemberByStatus(ClubMember c) {
		return clubMemberDao.getClubMemberByStatus(c);
	}

	public Integer queryCountByStatus(ClubMember c) {
		return clubMemberDao.queryCountByStatus( c) ;
	}

	public void updateClubMemberByKey(ClubMember newMember) {
		clubMemberDao.updateClubMemberByKey(newMember);
	}

	public void updateClubMember(Integer memberId){
		ClubMember clubMember = new ClubMember();
		clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_OUT);
		clubMember.setMemberId(memberId);
		//clubMember.setClubId(GameConstants.NO);
		clubMemberDao.updateClubMemberByKey(clubMember);
	}
	
	public void updateClubmemerIsremoind(Integer ClubmerId){
		ClubMember clubMember = new ClubMember();
		clubMember.setMemberId(ClubmerId);
		clubMember.setIsNewRemind(1);
		clubMemberDao.updateClubmemerIsremoind(clubMember);
		
	}

	/** 
	* @Title: selectClubMemberByUserIds 
	* @Description: 根据用户ids和俱乐部id获取会员列表
	* @param remindUserIds	用户ids
	* @param clubId	俱乐部id
	* @return List<ClubMember>
	* @author liulin
	* @date 2016年8月9日 上午4:40:23
	*/
	public List<ClubMember> selectClubMemberByUserIds(String remindUserIds, Integer clubId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clubId", clubId);
		paramMap.put("userIds", remindUserIds);
		return clubMemberDao.selectClubMemberByUserIds(paramMap);
	}

	/** 
	* @Title: updateIsNewRemindByUserIds 
	* @Description: 批量修改会员提醒为有新提醒1
	* @param remindUserIds	用户ids
	* @param clubId  俱乐部id
	* @return void
	* @author liulin
	* @date 2016年8月9日 上午4:41:43
	*/
	public void updateIsNewRemindByUserIds(String remindUserIds, Integer clubId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clubId", clubId);
		paramMap.put("userIds", remindUserIds);
		paramMap.put("isNewRemind", 1);
		clubMemberDao.updateIsNewRemindByUserIds(paramMap);
	}

	/** 
	* @Title: updateIsNewRemindByMemberIds 
	* @Description: 批量修改会员提醒为有新提醒1
	* @param memberIds  会员ids
	* @return void
	* @author liulin
	* @date 2016年8月9日 上午4:42:57
	*/
	public void updateIsNewRemindByMemberIds(String memberIds) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberIds", memberIds);
		paramMap.put("isNewRemind", 1);
		clubMemberDao.updateIsNewRemindByMemberIds(paramMap);
	}

	/** 
	* @Title: updateIsNewNoticeByMemberIds 
	* @Description: 批量修改会员通知为有新通知1
	* @param memberIds  会员ids
	* @return void
	* @author liulin
	* @date 2016年8月9日 上午4:43:47
	*/
	public void updateIsNewNoticeByMemberIds(String memberIds) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberIds", memberIds);
		paramMap.put("isNewNotice", 1);
		clubMemberDao.updateIsNewNoticeByMemberIds(paramMap);
	}
}