package com.seentao.stpedu.user.controller;

/** 
* @author yy
* @date 2016年7月5日 下午5:26:18 
*/
public interface IUserController {

	/**
	 * 获取人员信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param memberId	人员id
	 * @param shcoolId	学校id
	 * @param classId 班级id
	 * @param clubId	俱乐部id
	 * @param gameId 比赛id
	 * @param gameFieldId	场地id
	 * @param platformModule 平台模块
	 * @param searchWord 搜索词
	 * @param gameEventId 赛事id
	 * @param clubApplyStatus 该人员申请当前俱乐部的状态
	 * @param taskId 计划任务id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param hasQualification 查询类型
	 * @param inquireType 是否有认证资格
	 * @param classType 班级类型
	 */
	String getUsers(Integer userType, String userName, String userId, String userToken, Integer platformModule,
			Integer start, Integer limit, Integer inquireType, Integer hasQualification, Integer clubApplyStatus,
			String memberId, String schoolId, String classId, String clubId, String gameId, String gameFieldId,
			String searchWord, String gameEventId, String taskId, Integer classType,String quizId);

	/**
	 * 登录接口
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param sourceID 来源id
	 * @param pushToken	推送唯一标示
	 * @param password 密码
	 */
	String login(String userName, String password, String pushToken, String sourceID);
	String loginForMobile(String userName, String password, String pushToken, String sourceID);

	/**
	 * 注销登录接口
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 */
	String logout(String userId, Integer userType, String userName, String userToken);
	
	/**
	 * 注销登录接口(app)
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 */
	String logoutForMobile(String userId, Integer userType, String userName, String userToken);

	/**
	 * 提交注册或密码找回信息
	 * @param phoneNumber	手机号
	 * @param iCode	验证码
	 * @param password	密码
	 * @param invitationCode 邀请码
	 * @param registType 注册类型 1新注册 2密码找回
	 */
	String regist(String phoneNumber, String iCode, String password, String invitationCode, Integer registType,
			String nickName);

	/**
	 * 密码重置
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 */
	String resetPassword(String userId, Integer userType, String userName, String userToken, String oldPassword,
			String newPassword);

	/**
	 * 获取登录用户的信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 * @param inquireType 查询类型
	 */
	String getLoginUser(String userId, Integer userType, String userName, String userToken, Integer inquireType);

	/** 
	* @Title: submitUserDesc 
	* @Description: 提交个人签名
	* @param userId	用户ID
	* @param desc	个人签名
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午3:20:37
	*/
	String submitUserDesc(Integer userId, String desc);

	/** 
	* @Title: submitBindingNumber 
	* @Description: 绑定手机号
	* @param userId	用户ID
	* @param oldBindingNumber	旧手机号
	* @param newBindingNumber	新手机号
	* @param verifyPictureNumber	图片验证码
	* @param verifyNumber	手机验证码
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午3:52:45
	*/
	String submitBindingNumber(Integer userId, String oldBindingNumber, String newBindingNumber,
			String verifyPictureNumber, String verifyNumber);

	/** 
	* @Title: submitFeedback 
	* @Description: 提交意见反馈
	* @param userId	用户ID
	* @param content	意见内容
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午7:15:52
	*/
	String submitFeedback(Integer userId, String content);

	/** 
	* @Title: getProtocol 
	* @Description: 获取协议内容
	* @param userId	用户ID
	* @param protocolType	协议内型
	* @return  参数说明 
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午8:21:09
	*/
	String getProtocol(Integer userId, Integer protocolType);

	/** 
	* @Title: getCompanies 
	* @Description: 获取企业信息列表
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param start	起始位
	* @param limit	每页显示数
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午9:01:56
	*/
	String getCompanies(Integer userId, Integer memberId, Integer start, Integer limit, Integer inquireType);

	/** 
	* @Title: getVerifyPicture 
	* @Description: 申请获取图片验证码
	* @param userId	用户ID
	* @return String
	* @author liulin
	* @date 2016年7月5日 下午4:07:59
	*/
	String getVerifyPicture(Integer userId);
	
	/**
	 * 获取登录用户的信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 * @param inquireType 查询类型
	 */
	String getLoginUserForMobile(String userId, Integer userType, String userName, String userToken, Integer inquireType);
	
	/** 
	* @Title: getCompaniesForMobile (移动端)
	* @Description: 获取企业信息列表
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param start	起始位
	* @param limit	每页显示数
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午9:01:56
	*/
	String getCompaniesForMobile(Integer userId, Integer memberId, Integer start, Integer limit, Integer inquireType);
	
	/**
	 *  验证手机号码是否存在
	 * @param userId 用户id 预留
	 * @param phoneNumber 验证的手机号
	 * @return
	 */
	String verifyPhoneNumberExist(String userId,String phoneNumber);

}
