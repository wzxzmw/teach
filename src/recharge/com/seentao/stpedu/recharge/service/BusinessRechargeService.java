package com.seentao.stpedu.recharge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.alipay.service.AlipayResultNotifyServiceForStpedu;
import com.seentao.stpedu.alipay.service.IAlipayResultNotifyServiceForStpedu;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterConvert;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterRecharge;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterConvertService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterRechargeService;
import com.seentao.stpedu.common.service.CenterSerialMaxService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.DoubleUtil;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author yy
* @date 2016年6月27日 下午5:40:02 
*/
@Service
public class BusinessRechargeService {
	@Autowired
	private CenterAccountService centerAccountService;
	@Autowired
	private CenterConvertService centerConvertService;
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	@Autowired
	private CenterSerialMaxService centerSerialMaxService;
	@Autowired
	private CenterRechargeService centerRechargeService;
	@Autowired
	private IAlipayResultNotifyServiceForStpedu alipayService;
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
	@Transactional
	public String exchangeAccount(Integer userType, String userName, String userId, Integer virtualType,
			Float fLevelAccount, String remark) {
		LogUtil.info(this.getClass(), "exchangeAccount", "开始调用,userId="+userId+",virtualType="+virtualType);
		JSONObject json = new JSONObject();
		try {
			//一级账户
			CenterAccount oneLevelParam = new CenterAccount();
			oneLevelParam.setUserId(Integer.valueOf(userId));
			oneLevelParam.setAccountType(GameConstants.STAIR_ONE);//一级货币账户
			oneLevelParam.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//用户类型。1:个人用户，2:俱乐部用户。
			CenterAccount resOneLeveCount = centerAccountService.getCenterAccount(oneLevelParam);
			//该用户没有账户
			if(resOneLeveCount!=null){
				//一级账户余额
				Double balance = resOneLeveCount.getBalance();
				if(fLevelAccount <= balance){
					//二级账户
					CenterAccount twoLevelParam = new CenterAccount();
					twoLevelParam.setUserId(Integer.valueOf(userId));
					twoLevelParam.setAccountType(GameConstants.STAIR_TWO);//二级货币账户
					twoLevelParam.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//用户类型。1:个人用户，2:俱乐部用户。
					CenterAccount resTwoLeveCount = centerAccountService.getCenterAccount(twoLevelParam);
					//流水号
					String max = centerSerialMaxService.getCenterSerialMaxByNowDate(GameConstants.STAIR_ONE);
					//兑换表新增数据
					CenterConvert centerConvert = new CenterConvert();
					centerConvert.setConvertType(virtualType);//兑换类型
					centerConvert.setCreateTime(TimeUtil.getCurrentTimestamp());//兑换时间
					centerConvert.setCreateUserId(Integer.valueOf(userId));//兑换用户id
					centerConvert.setRemarks(remark);//兑换备注
					centerConvert.setVirtualAmount((double)fLevelAccount*BusinessConstant.RECHARGE_RATIO_VIRTUAL);//一级货币数量
					centerConvert.setSerialNumber(max);//流水号
					centerConvertService.insertCenterConvert(centerConvert);
					//账户表一级账户较少金额
					CenterAccount oneLevelAccount = new CenterAccount();
					oneLevelAccount.setAccountId(resOneLeveCount.getAccountId());
					oneLevelAccount.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//用户类型。1:个人用户，2:俱乐部用户。
					oneLevelAccount.setUserId(Integer.valueOf(userId));
					oneLevelAccount.setAccountType(GameConstants.STAIR_ONE);//一级货币账户
					oneLevelAccount.setBalance(balance-fLevelAccount);//余额减少
					centerAccountService.updateCenterAccountByKey(oneLevelAccount);
					//账户表二级账户增加金额
					CenterAccount twoLevelAccount = new CenterAccount();
					twoLevelAccount.setAccountId(resTwoLeveCount.getAccountId());
					twoLevelAccount.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//用户类型。1:个人用户，2:俱乐部用户。
					twoLevelAccount.setUserId(Integer.valueOf(userId));
					twoLevelAccount.setAccountType(GameConstants.STAIR_TWO);//二级货币账户
					twoLevelAccount.setBalance(resTwoLeveCount.getBalance()+(fLevelAccount*BusinessConstant.RECHARGE_RATIO_VIRTUAL));
					centerAccountService.updateCenterAccountByKey(twoLevelAccount);
					//变动表二级货币增加记录
					CenterMoneyChange oneLeveChange = new CenterMoneyChange();
					oneLeveChange.setAccountId(resTwoLeveCount.getAccountId());//账户id
					oneLeveChange.setSerialNumber(max);//流水号
					oneLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
					oneLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
					oneLeveChange.setAmount(fLevelAccount*BusinessConstant.RECHARGE_RATIO_VIRTUAL);//变动金额
					oneLeveChange.setChangeType(GameConstants.INCOME);//变动类型(1收入，2支出,3提现)
					oneLeveChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_EXC);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
					oneLeveChange.setRelObjetId(centerConvert.getConvertId());//关联id
					oneLeveChange.setStatus(GameConstants.SUCCESSFUL);//状态(状态。1:成功，2:失败，3:过程中。)
					centerMoneyChangeService.insertCenterMoneyChange(oneLeveChange);
					//变动表一级货币增加记录
					CenterMoneyChange twoLeveChange = new CenterMoneyChange();
					twoLeveChange.setAccountId(resOneLeveCount.getAccountId());//账户id
					twoLeveChange.setSerialNumber(max);//流水号
					twoLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
					twoLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
					twoLeveChange.setAmount((double)fLevelAccount);//变动金额
					twoLeveChange.setChangeType(GameConstants.SPENDING);//变动类型(1收入，2支出,3提现)
					twoLeveChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_EXC);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
					twoLeveChange.setRelObjetId(centerConvert.getConvertId());//关联id
					twoLeveChange.setStatus(GameConstants.SUCCESSFUL);//状态(状态。1:成功，2:失败，3:过程中。)
					centerMoneyChangeService.insertCenterMoneyChange(twoLeveChange);
					LogUtil.info(this.getClass(), "exchangeAccount", "兑换成功");
					json.put(GameConstants.CODE, AppErrorCode.SUCCESS);
				}else{
					//余额不足
					json.put(GameConstants.CODE, AppErrorCode.ERROR_COUNT_NOT);
					json.put(GameConstants.MSG, BusinessConstant.USER_ACCOUNT_LACK);
				}
			}else{
				//用户没有账户
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				json.put(GameConstants.MSG, BusinessConstant.USER_NOT_ACCOUNT);
			}
		} catch (Exception e) {
			LogUtil.error(BusinessRechargeService.class, "exchangeAccount", "兑换虚拟货币异常", e);
			json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
			json.put(GameConstants.MSG, BusinessConstant.EXCHANGE_EXCEPTION);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return json.toJSONString();
	}
	
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
	@Transactional
	public String submitDeposit(Integer userType, String userName, String userId, String remark,
			Integer depositObjectType, Float cash, Integer depositType, String clubId) {
		LogUtil.info(this.getClass(), "submitDeposit", "开始调用userId="+userId+",depositObjectType="
			+depositObjectType+",cash="+cash);
		JSONObject json = new JSONObject();
		if(depositObjectType==BusinessConstant.USER_ACCOUNT_TYPE_1){//个人充值
			json = this.addCenterRecharge(userId,depositType,remark,cash,depositObjectType,clubId);
			LogUtil.info(this.getClass(), "submitDeposit", "(个人充值)插入充值信息成功");
		}else if(depositObjectType==BusinessConstant.USER_ACCOUNT_TYPE_2){//为俱乐部充值
			//判断俱乐部是否一致
			String cacheUser = RedisComponent.findRedisObject(Integer.valueOf(userId), CenterUser.class);
			CenterUser centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
			Integer club = Integer.valueOf(clubId);
			if(club.equals(centeruser.getClubId())){
				json = this.addCenterRecharge(userId,depositType,remark,cash,depositObjectType,clubId);
				LogUtil.info(this.getClass(), "submitDeposit", "(俱乐部充值)插入充值信息成功");
			}else{
				//俱乐部不一致
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				json.put(GameConstants.MSG, BusinessConstant.ERROR_CLUB_DIFFERING);
			}
		}else{
			//充值对象错误
			json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			json.put(GameConstants.MSG, BusinessConstant.ERROR_TYPE_CODE);
		}
		return json.toJSONString();
	}

	private JSONObject addCenterRecharge(String userId, Integer depositType, String remark, Float cash, Integer depositObjectType,String clubId) {
		JSONObject json = new JSONObject();
		try {
			CenterAccount centerAccount = new CenterAccount();
			if(depositObjectType==BusinessConstant.USER_ACCOUNT_TYPE_1){//个人充值
				centerAccount.setUserId(Integer.valueOf(userId));
				centerAccount.setAccountType(GameConstants.STAIR_ONE);//一级货币账户
				centerAccount.setUserType(depositObjectType);//用户类型。1:个人用户，2:俱乐部用户。
			}else if(depositObjectType==BusinessConstant.USER_ACCOUNT_TYPE_2){//给俱乐部充值
				centerAccount.setUserId(Integer.valueOf(clubId));
				centerAccount.setAccountType(GameConstants.STAIR_ONE);//一级货币账户
				centerAccount.setUserType(depositObjectType);
			}
			CenterAccount resCount = centerAccountService.getCenterAccount(centerAccount);
			if(resCount!=null){
				CenterRecharge centerRecharge = new CenterRecharge();
				centerRecharge.setAccountId(resCount.getAccountId());//账户id
				centerRecharge.setAmount((double)cash);//充值金额
				centerRecharge.setCreateTime(TimeUtil.getCurrentTimestamp());//充值时间
				centerRecharge.setCreateUserId(Integer.valueOf(userId));//创建人
				centerRecharge.setRechargeType(depositType);//充值类型(支付宝，微信)
				//流水号
				String max = centerSerialMaxService.getCenterSerialMaxByNowDate(GameConstants.STAIR_ONE);//1：一级虚拟货币，2:二级虚拟货币
				centerRecharge.setSerialNumber(max);//流水号
				centerRecharge.setRemarks(remark);//备注
				centerRecharge.setVirtualAmount((double)cash);//一级货币数量
				centerRecharge.setStatus(GameConstants.RECHARGE_WAIT);//充值状态(1:支付成功，2:支付失败，3:等待支付，4:取消充值。)
				centerRechargeService.insertCenterRecharge(centerRecharge);
				//updatedate: 2016-07-10 17:27  修改人：温家鑫   修改返回值
				
				JSONArray paramArray = alipayService.subOrder(max,DoubleUtil.formatDouble((double)cash));
				JSONObject res = new JSONObject();
				res.put("alipayParams", paramArray);
				res.put("depositId",centerRecharge.getRechargeId());
				json.put(GameConstants.CODE, AppErrorCode.SUCCESS);
				json.put("result", res);
			}else{
				//用户没有账户
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				json.put(GameConstants.MSG, BusinessConstant.USER_NOT_ACCOUNT);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LogUtil.error(BusinessRechargeService.class, "addCenterRecharge", "插入充值记录异常", e);
			json.put(GameConstants.CODE, AppErrorCode.ERROR_INSERT);
			json.put(GameConstants.MSG, BusinessConstant.ERROR_RECHARGE_CODE);
		}
		return json;
	}
	
	/**
	 * 校验支付是否成功
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param depositType 充值类型标识(1:支付宝；2:微信；)
	 * @param depositId 充值id
	 */
	public String checkPayment(Integer userType, String userName, String userId, Integer depositType,
			String depositId) {
		LogUtil.info(this.getClass(), "checkPayment", "开始调用,userId="+userId+",depositId="+depositId);
		JSONObject json = new JSONObject();
		CenterRecharge centerRecharge = new CenterRecharge();
		centerRecharge.setRechargeId(Integer.valueOf(depositId));//充值id
		CenterRecharge res = centerRechargeService.getCenterRecharge(centerRecharge);
		if(res==null){
			LogUtil.error(this.getClass(), "checkPayment", "没有充值信息");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.NOT_RECHARGE_BY_ID).toJSONString();
		}else{
			Integer status = res.getStatus();
			if(status==BusinessConstant.RECHARGE_TYPE_1){
				json.put("paymentStatus",BusinessConstant.RECHARGE_TYPE_1);//成功
			}else{
				json.put("paymentStatus",BusinessConstant.RECHARGE_TYPE_2);//支付中
			}
			LogUtil.info(this.getClass(), "checkPayment", "获取校验支付结果成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", json).toJSONString();
		}
	}

}


