package com.seentao.stpedu.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.account.service.SubmitQuizBettingService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Controller
public class SubmitQuizBettingController implements ISubmitQuizBettingController {

	@Autowired
	private SubmitQuizBettingService submitQuizBettingService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.account.controller.ISubmitQuizBettingController#submitQuizBetting(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Double)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "submitQuizBetting")
	public String submitQuizBetting(Integer userId,Integer quizId,Integer bettingObject,Double bettingCount ){
		
		//校验下注金额
		if(bettingObject == null || bettingObject <= 0 ){
			LogUtil.error(this.getClass(), "submitQuizBetting", String.valueOf(AppErrorCode.ERROR_QUIZRESULT_BET_MONEY));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZRESULT_BET_MONEY).toJSONString();
		}
		
		return submitQuizBettingService.submitQuizB(userId,quizId,bettingObject,bettingCount);
		
	}
}
