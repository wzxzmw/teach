package com.seentao.stpedu.game.service;

public interface IGameService {
	
	
	/**
	 * 根据班级id和章节id获取实训比赛id服务
	 * @author  lijin
	 * @date    2016年7月12日 下午9:54:25
	 */
	String getTeamIdByClassIdAndChapterId(String classId, String champterId);

	/**
	 * 生成创建擂台比赛动态
	 * @param gameId 比赛ID
	 * @param gameName 比赛名称
	 * @param clubId 俱乐部ID
	 * @return
	 * @author chengshx
	 */   
	String addArenaGameLive(Integer gameId, Integer clubId);

	/**
	 * 生成创建教学实训动态
	 * @param gameId 比赛ID
	 * @param gameName 比赛名称
	 * @param classId 班级ID或者课程卡ID
	 * @param matchType 1：班级ID，2：课程卡ID
	 * @param startYear 初始年
	 * @return
	 * @author chengshx
	 */
	String addTeachingPracticeLive(Integer gameId, Integer classId, Integer matchType, Integer startYear);

	/**
	 * 生成用户参加比赛动态
	 * @param gameId 比赛ID
	 * @param userId 用户ID
	 * @return
	 * @author chengshx
	 */
	String addUserJoinGameLive(Integer gameId, Integer userId);

	/**
	 * 根据用户ID获取所属俱乐部信息
	 * @param userId
	 * @param loginUserId
	 * @return
	 * @author chengshx
	 */
	String getClubInfo(Integer userId, Integer loginUserId);

	/**
	 * 根据用户ID返回用户信息
	 * @param userId 用户ID
	 * @param loginUserId 登陆用户ID
	 * @return
	 * @author chengshx
	 */
	String getUserInfo(Integer userId, Integer loginUserId);

	/**
	 * 根据比赛ID、用户ID和消息ID 获取该登录用户是否有添加队员权限
	 * @param gameId 比赛ID
	 * @param userId 用户ID
	 * @param newsId 消息ID
	 * @return
	 * @author chengshx
	 */
	String hasAuthorityAddUser(Integer gameId, Integer userId, Integer newsId);

	/**
	 * 判断用户货币余额是否充足
	 * @param userId 用户ID
	 * @param amount 金额
	 * @return
	 * @author chengshx
	 */
	String isMoneyEnough(Integer userId, Double amount);

	/**
	 * 扣减用户账户金额
	 * @param userId 用户ID
	 * @param amount 金额
	 * @return
	 * @author chengshx
	 */
	String decrUserMoney(Integer userId, Double amount);

	/**
	 * 添加赛事俱乐部记录
	 * @param gameId 比赛ID
	 * @param userIds 用户ID串，逗号分隔
	 * @return
	 * @author chengshx
	 */
	String addArenaJoinClub(Integer gameId, String userIds);

	/**
	 * 根据附件ID获取附件的OSSurl地址
	 * @param attaId 附件ID
	 * @return
	 * @author chengshx
	 */
	String getOSSurl(Integer attaId);

	/**
	 * 验证用户Token是否有效
	 * @param userId 用户ID
	 * @param userToken 用户Token
	 * @return
	 * @author chengshx
	 */
	String isUserTokenValid(Integer userId, String userToken);
	
	/**
	 * type为1时判断俱乐部是否有增值权，为2时判断俱乐部是否有赛事运营权
	 * @param clubId 俱乐部ID或俱乐部ID
	 * @param type 类型（1：ID为俱乐部ID，2：赛事ID）
	 * @return
	 * @author chengshx
	 */
	String hasGameRight(Integer clubId, Integer type);
	
	/**
	 * 生成邀请参加比赛消息
	 * @param gameId 比赛ID
	 * @param gameName 比赛名称
	 * @param idStr 用户ID或俱乐部ID，逗号分隔
	 * @param type 类型（1：俱乐部邀请，2：学校邀请）
	 * @return
	 * @author chengshx
	 */
	String addGameNews(Integer gameId, String gameName, String idStr, Integer type);

	/**
	 * 获取用户在俱乐部等级
	 * @param userId 用户ID
	 * @param clubId 俱乐部Id
	 * @return 1:会长 2:教练 3： 会员
	 * @author cxw
	 */
	String getClubUserLevel(Integer clubId, Integer userId);
	
	/**
	 * 根据班级id获取该班级所有的课程卡id
	 * @param classId
	 * @return
	 * @author 			lw
	 * @date			2016年7月28日  下午4:51:25
	 */
	String getCardIdsByClassId(Integer classId);

	/**
	 * 统计班级累计实训人次
	 * @param cardId 课程卡ID
	 * @return
	 * @author chengshx
	 */
	String addClassTrainNum(Integer cardId);
	
	/**
	 * 创建比赛时：赛事未开始比赛数量+1
	 * @param CompId	赛事id
	 * @return
	 * @author 			lw
	 * @date			2016年8月24日  下午4:02:55
	 */
	String addCreateGame(Integer CompId);
	
	/**
	 * 比赛进行时：赛事未开始比赛-1、进行中的比赛数据量+1
	 * @param CompId	赛事id
	 * @return
	 * @author 			lw
	 * @date			2016年8月24日  下午5:25:44
	 */
	String addStartGame(Integer CompId);
	
	/**
	 * 比赛结束：赛事进行中的比赛-1
	 * @param CompId	赛事id
	 * @return
	 * @author 			lw
	 * @date			2016年8月24日  下午5:27:08
	 */
	String addEndGme(Integer CompId);
	
	/**
	 * 指派队长：赛事队伍数量+1
	 * @param CompId	赛事id
	 * @return
	 * @author 			lw
	 * @date			2016年8月24日  下午5:28:52
	 */
	String appointCaptain(Integer CompId);
	
	/**
	 * 取消队长：参赛队伍数量-1
	 * @param CompId	赛事id
	 * @param num		队长数量（队伍数量）
	 * @return
	 * @author 			lw
	 * @date			2016年8月24日  下午5:29:48
	 */
	String cancelCaptain(Integer CompId,Integer num);
	
	/**
	 * 通过用户id 获取 用户所在学校
	 * @param userId
	 * @return
	 * @author 			lw
	 * @date			2016年9月19日  上午9:51:55
	 */
	String findSchoolInfoByUserId(Integer userId);
	
}