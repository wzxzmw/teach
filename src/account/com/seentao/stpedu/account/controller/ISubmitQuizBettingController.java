package com.seentao.stpedu.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitQuizBettingController {

	/**
	 * 竞猜下注
	 * @param userId 
	 * @param bettingObject 投注哪方(1:能；2:不能；)
	 * @param bettingCount  投注数量(虚拟物品的数量)
	 * @param quizId        竞猜id
	 * @return
	 */
	String submitQuizBetting(Integer userId, Integer quizId, Integer bettingObject, Double bettingCount);

}