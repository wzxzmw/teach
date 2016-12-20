package com.seentao.stpedu.recharge.controller;

/** 
* @author yy
* @date 2016年7月5日 下午9:16:12 
*/
public interface IRechargeController {

	/**
	 * 充值
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param depositObjectType 充值对象类型(1:个人；2:俱乐部；)
	 * @param clubId 俱乐部id
	 * @param cash 充值金额
	 * @param depositType 充值类型标识(1:支付宝；2:微信；)
	 * @param remark 备注
	 */
	String submitDeposit(Integer userType, String userName, String userId, String userToken, Integer depositObjectType,
			Float cash, String remark, Integer depositType, String clubId);

	/**
	 * 一级货币兑换虚拟物品
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param virtualType 兑换的虚拟物品类型(1:鲜花;)
	 * @param fLevelAccount 金额
	 * @param remark 备注
	 */
	String exchangeAccount(Integer userType, String userName, String userId, String userToken, Integer virtualType,
			Float fLevelAccount, String remark);

	/**
	 * 校验支付是否成功
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param depositType 充值类型标识(1:支付宝；2:微信；)
	 * @param depositId 充值id
	 */
	String checkPayment(Integer userType, String userName, String userId, String userToken, Integer depositType,
			String depositId);

}
