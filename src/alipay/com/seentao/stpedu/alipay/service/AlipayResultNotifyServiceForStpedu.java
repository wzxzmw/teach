package com.seentao.stpedu.alipay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterRecharge;
import com.seentao.stpedu.common.interfaces.IAlipayServiceInterface;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterRechargeService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class AlipayResultNotifyServiceForStpedu implements IAlipayResultNotifyServiceForStpedu {
	
	/** 
	* @Fields centerRechargeService : 充值表Service 
	*/ 
	@Autowired
	private CenterRechargeService centerRechargeService; 
	
	/** 
	* @Fields centerMoneyChangeService : 货币变动表Service
	*/ 
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService; 
	
	/** 
	* @Fields centerAccountService : 用户账户表Service 
	*/ 
	@Autowired
	private CenterAccountService centerAccountService;
	
	
	/** 
	* 往充值表中插入记录，返回客户端需要发给支付宝的信息
	* @author W.jx
	* @date 2016年6月28日 下午9:11:16 
	* @param outTradeNo 流水号
	* @param rechargeAmount 订单金额
	* @return
	*/
	public JSONArray subOrder(String outTradeNo, Double rechargeAmount) {
		//把请求参数打包成数组
		JSONArray result = new JSONArray();
		IAlipayServiceInterface alipayService = HproseRPC.getRomoteClass(ActiveUrl.ALIPAY_SERVER_INTERFACE, IAlipayServiceInterface.class);
		result = alipayService.subOrder(outTradeNo, rechargeAmount);
		return result;
	}

	
	/** 
	* 支付宝同步回调
	* @author W.jx
	* @date 2016年7月27日 下午7:19:50 
	* @param out_trade_no
	* @param trade_no
	* @param trade_status
	* @param seller_id
	* @param total_fee
	* @param buyer_email
	* @param notify_time
	* @return
	* @throws 
	*/
	@Override
	@Transactional
	public String zfbCallBack(String out_trade_no, String trade_no, String trade_status, String seller_id, String total_fee,
			String buyer_email, String notify_time) {
		JSONObject resultJSON = new JSONObject();
		String result = "";
		int user_type = 1;
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
			//交易成功且结束，即不可再做任何操作
			if(trade_status.equals("TRADE_FINISHED")){
				//交易结束
			} else if (trade_status.equals("TRADE_SUCCESS")){//交易成功，且可对该交易做操作，如：多级分润、退款等。
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				CenterRecharge centerRecharge = centerRechargeService.getCenterRechargeBySerialNumber(out_trade_no);
				//获取账户信息
				CenterAccount centerAccount = new CenterAccount();
				centerAccount.setAccountId(centerRecharge.getAccountId());
				centerAccount = centerAccountService.getCenterAccount(centerAccount);
				user_type = centerAccount.getUserType();
				resultJSON.put("user_type", user_type);
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				//如果有做过处理，不执行商户的业务程序
				if(centerRecharge != null && (Double.compare(Double.valueOf(total_fee), centerRecharge.getAmount()) == 0) && centerRecharge.getStatus() != GameConstants.RECHARGE_SUCCESS){
					//1、充值表修改状态
					centerRecharge.setStatus(GameConstants.RECHARGE_SUCCESS);
					centerRecharge.setPayOrderNumber(trade_no);
					centerRecharge.setPayAccount(buyer_email);
					centerRecharge.setPayAmount(centerRecharge.getAmount());
					centerRecharge.setPayTime(notify_time);
					//修改充值表的map.xml
					centerRechargeService.updateCenterRecharge(centerRecharge);
					
					//2、变动表添加记录
					CenterMoneyChange centerMoneyChange = new CenterMoneyChange(); 
					centerMoneyChange.setAccountId(centerRecharge.getAccountId());
					centerMoneyChange.setSerialNumber(centerRecharge.getSerialNumber());
					centerMoneyChange.setChangeTime(TimeUtil.getCurrentTimestamp());
					centerMoneyChange.setAmount(centerRecharge.getVirtualAmount());
					centerMoneyChange.setChangeType(GameConstants.INCOME);
					centerMoneyChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_RECH);
					centerMoneyChange.setRelObjetId(centerRecharge.getRechargeId());
					centerMoneyChange.setStatus(GameConstants.MONEY_CHANGE_STATE_SUCCESS);
					centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChange);
					
					//3、用户账户表 修改余额
					if(centerAccount != null && centerAccount.getAccountId() > 0){
						centerAccount.setBalance(centerAccount.getBalance() + centerRecharge.getVirtualAmount());
						centerAccountService.updateCenterAccountByKey(centerAccount);
					}
					LogUtil.info(this.getClass(), "zfbCallBack", "该订单处理成功");
				}else{
					result = "fail";
					LogUtil.error(this.getClass(), "zfbCallBack", "该订单已经处理过，或者金额错误");
				}
			}
			
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
			result = "success";	//请不要修改或删除
			resultJSON.put("result", result);
			//////////////////////////////////////////////////////////////////////////////////////////
		return resultJSON.toJSONString();
	}
	
	

	/** 
	* 异步回调
	* @author W.jx
	* @date 2016年7月27日 下午7:19:50 
	* @param out_trade_no
	* @param trade_no
	* @param trade_status
	* @param seller_id
	* @param total_fee
	* @param buyer_email
	* @param notify_time
	* @return
	* @throws 
	*/
	@Override
	@Transactional
	public String zfbAsynchronizationCallBack(String out_trade_no, String trade_no, String trade_status, String seller_id, String total_fee,
			String buyer_email, String notify_time) {
		String result = "";
		/*String trade_status = "";
		String out_trade_no = "";
		String seller_id = "";
		String total_fee = "";
		String trade_no = "";
		String buyer_email = "";
		String notify_time = "";*/
		/*try {
			//商户订单号
			out_trade_no = new String(params.get("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			trade_no = new String(params.get("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			trade_status = new String(params.get("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//卖家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
			seller_id = new String(params.get("seller_id").getBytes("ISO-8859-1"),"UTF-8");
			//交易金额
			total_fee = new String(params.get("total_fee").getBytes("ISO-8859-1"),"UTF-8");
			//买家支付宝账号  买家支付宝账号，可以是Email或手机号码。
			buyer_email = new String(params.get("buyer_email").getBytes("ISO-8859-1"),"UTF-8");
			//通知时间
			notify_time = new String(params.get("notify_time").getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.error(this.getClass(), "zfbCallBack", "参数获取失败！\r" + e);
			return "fail";
		}*/
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		//boolean verify_result = AlipayNotify.verify(params);
		
		//if(verify_result){//验证成功
			//交易成功且结束，即不可再做任何操作
			if(trade_status.equals("TRADE_FINISHED")){
				//交易结束
			} else if (trade_status.equals("TRADE_SUCCESS")){//交易成功，且可对该交易做操作，如：多级分润、退款等。
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				CenterRecharge centerRecharge = centerRechargeService.getCenterRechargeBySerialNumber(out_trade_no);
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				//如果有做过处理，不执行商户的业务程序
				if((Double.compare(Double.valueOf(total_fee), centerRecharge.getAmount()) == 0) && centerRecharge.getStatus() != GameConstants.RECHARGE_SUCCESS){
					//1、充值表修改状态
					centerRecharge.setStatus(GameConstants.RECHARGE_SUCCESS);
					centerRecharge.setPayOrderNumber(trade_no);
					centerRecharge.setPayAccount(buyer_email);
					centerRecharge.setPayAmount(centerRecharge.getAmount());
					centerRecharge.setPayTime(notify_time);
					centerRechargeService.updateCenterRecharge(centerRecharge);
					
					//2、变动表添加记录
					CenterMoneyChange centerMoneyChange = new CenterMoneyChange(); 
					centerMoneyChange.setAccountId(centerRecharge.getAccountId());
					centerMoneyChange.setSerialNumber(centerRecharge.getSerialNumber());
					centerMoneyChange.setChangeTime(TimeUtil.getCurrentTimestamp());
					centerMoneyChange.setAmount(centerRecharge.getVirtualAmount());
					centerMoneyChange.setChangeType(GameConstants.INCOME);
					centerMoneyChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_RECH);
					centerMoneyChange.setRelObjetId(centerRecharge.getRechargeId());
					centerMoneyChange.setStatus(GameConstants.MONEY_CHANGE_STATE_SUCCESS);
					centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChange);
					
					//3、用户账户表 修改余额
					CenterAccount centerAccount = new CenterAccount();
					centerAccount.setAccountId(centerRecharge.getAccountId());
					centerAccount = centerAccountService.getCenterAccount(centerAccount);
					if(centerAccount != null && centerAccount.getAccountId() > 0){
						centerAccount.setBalance(centerAccount.getBalance() + centerRecharge.getVirtualAmount());
						centerAccountService.updateCenterAccountByKey(centerAccount);
					}
					LogUtil.info(this.getClass(), "zfbAsynchronizationCallBack", "该订单处理成功");
				}else{
					LogUtil.error(this.getClass(), "zfbAsynchronizationCallBack", "该订单已经处理过，或者金额错误");
				}
			}
			
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
			result = "success";	//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		//}else{//验证失败
		//	result = "fail";
		//	LogUtil.error(this.getClass(), "zfbCallBack", "验证失败！");
		//}
		return result;
	}
	
	
	
}
