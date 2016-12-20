package com.seentao.stpedu.persionalcenter.controller;


public interface IClubCenterController {

	/**
	 * 获取消息信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param messageType		消息类型
	 * @param start		            起始页
	 * @param limit             每页数量	
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getMessages(String userName,String userId,String userType,String userToken,String messageType,String start,String limit);

	
	/**
	 * 获取消息信息【手机版】
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param messageType		消息类型
	 * @param start		            起始页
	 * @param limit             每页数量	
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getMessagesForMobile(String userName,String userId,String userType,String userToken,String messageType,String start,String limit);
	
	/**
	 * 获取俱乐部权益信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部Id
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getClubRights(String userName,String userId,String userType,String userToken,String clubId);

	/**
	 * 购买俱乐部权益
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部Id
	 * @param actionType         操作类型
	 * @param classId         班级id
	 * @param classType         班级类型
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String buyClubRights(String userName,String userId,String userType,String userToken,String actionType,String clubId,String classId,String classType);

	/**
	 * 获取俱乐部或个人的账务收支信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部Id
	 * @param incomeAndExpenses        收支标识
	 * @param accountType         账务类型
	 * @param startDate         查询开始时间的时间戳
	 * @param endDate        查询结束时间的时间戳
	 * @param start        起始数
	 * @param limit        每页数量
	 * @param inquireType        查询类型
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getIncomeAndExpenses(String userName,String userId,String userType,String userToken,String clubId ,String incomeAndExpenses,String accountType,String startDate,String endDate,String start,String limit,String inquireType);

	/**
	 * 获取提现信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部Id
	 * @param startDate       查询开始时间的时间戳
	 * @param endDate         查询结束时间的时间戳
	 * @param start        起始数
	 * @param limit        每页数量
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getCashing(String userName,String userId,String userType ,String userToken,String clubId,String startDate,String endDate,String start,String limit);
	/**
	 * 提交提现申请
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部Id
	 * @param cash       查询开始时间的时间戳
	 * @param accountType         账户类型标识
	 * @param accountNum        账户号码
	 * @param applyContent         提现申请内容
	 * @param iCode         验证码
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String applyCashing(String userName,String userId,String userType,String userToken ,String clubId,String cash,String accountType,String accountNum,String applyContent,String iCode);

	/**
	 * 获取账户类型标识信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	String getAccountTypes(String userName ,String userId,String userType,String userToken);

}