package com.seentao.stpedu.common.interfaces;

public interface IGameInterfaceService {

	/**
	 * 根据赛事ID获取赛事下未开始比赛的数量(status=1)，正在进行的比赛数量(status=1) 调用方：平台系统
	 * @param competitionId 赛事比赛ID
	 * @author zhengchunlei
	 * @return
	 */
	String getNumMatchInfoByCompetitionId(Integer competitionId);

	/**
	 * 获取参加一场比赛的全部用户ID，包括所有场地 调用方：平台系统
	 * @param matchId 平台比赛ID
	 * @author zhengchunlei
	 * @return
	 */
	String getUsersByMatchId(Integer matchId);

	/**
	 * 获取比赛所属赛事ID 调用方：平台系统
	 * @param matchId 平台比赛ID
	 * @author zhengchunlei
	 * @return
	 */
	String getCompetitionIdByMatchId(Integer matchId);

	/**
	 * 根据赛事ID获取赛事下全部比赛ID 调用方：平台系统
	 * @param competitionId 赛事ID
	 * @author zhengchunlei
	 * @return
	 */
	String getMatchIdsByCompetitionId(Integer competitionId);

	/**
	 * 根据比赛ID获取比赛邀请信息 调用方：平台系统
	 * @param matchId 平台比赛ID 
	 * @author zhengchunlei
	 * @return
	 */
	String getMatchInviteObjectByMatchId(Integer matchId,int type,int userId);

	/**
	 * 通过平台比赛ID获取比赛信息
	 * @param matchId
	 * @author zhengchunlei
	 * @return
	 */
	String getBaseMatchInfoByMatchId(Integer matchId);
	
	/*
	 * 批量查询通过比赛ids查询比赛信息
	 */
	String getBaseMatchInfoByMatchIds(String matchIds);

	/**
	 * 通过俱乐部ID和用户Id 获取比赛状态信息
	 * @param clubId 俱乐部ID
	 * @param userId 用户Id
	 * @author zhengchunlei
	 * @return obj.put("matchStatus", ); matchStatus -1：没有返回值；1:比赛未开始；4:比赛进行中；5:比赛结束
	 */
	String getMatchStatusByClubId(Integer clubId, Integer userId);
	/**
	 * 根据用户Id判断是否正在参赛
	 * @param userId 用户Id
	 * @author zhengchunlei
	 * @return obj.put("isAuth", ); isAuth 0：没有删除权限  正在参赛；1:可以删除   没有参赛
	 */
	String getIsPlayMatchByuserId(Integer userId);
	
	/**
	 * 参加的比赛数量(是俱乐部内部的比赛和外部比赛的数量和)
	 * 参加的实训作业数量
	 * @param userId
	 * @return
	 */
	String getWebGameCountByUserId(Integer userId,String type,String status);
	
	/**
	 * 获取某场比赛的场地及队伍信息
	 * @author cuijing
	 * @param matchId
	 * @return 
	 */
	String getTeamsByMatchId(String matchId);
	
	/**
	 * 根据课程卡集合获取教师最近创建的比赛
	 * @param cardIds 课程卡ID集合
	 * @return
	 * @author zhengchunlei 2016.9.20(廖伟用)
	 */
	String getTeacherCreateMatchInfoByCardIds(String cardIds);

}