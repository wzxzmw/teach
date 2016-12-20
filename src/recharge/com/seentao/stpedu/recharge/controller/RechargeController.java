package com.seentao.stpedu.recharge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.recharge.service.BusinessRechargeService;

/** 
* @author yy
* @date 2016年6月27日 下午5:38:46 
*/
@Controller
public class RechargeController implements IRechargeController {
	
	@Autowired
	private BusinessRechargeService businessRechargeService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.recharge.controller.IRechargeController#submitDeposit(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Float, java.lang.String, java.lang.Integer, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitDeposit")
	public String submitDeposit(Integer userType,String userName,String userId,String userToken,
			Integer depositObjectType,Float cash,String remark,Integer depositType,String clubId
			){
		return businessRechargeService.submitDeposit(userType,userName,userId,remark,depositObjectType,cash,depositType,clubId);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.recharge.controller.IRechargeController#exchangeAccount(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Float, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/exchangeAccount")
	public String exchangeAccount(Integer userType,String userName,String userId,String userToken,
			Integer virtualType,Float fLevelAccount,String remark
			){
		return businessRechargeService.exchangeAccount(userType,userName,userId,virtualType,fLevelAccount,remark);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.recharge.controller.IRechargeController#checkPayment(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/checkPayment")
	public String checkPayment(Integer userType,String userName,String userId,String userToken,
			Integer depositType,String depositId){
		return businessRechargeService.checkPayment(userType,userName,userId,depositType,depositId);
	}
	
}


