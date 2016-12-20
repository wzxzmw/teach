package com.seentao.stpedu.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.ArenaGuessTopicService;
import com.seentao.stpedu.common.service.CenterMoneyLockService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class SubmitQuizBettingService {


	@Autowired
	private CenterMoneyLockService centerMoneyLockService;

	@Autowired
	private ArenaGuessService arenaGuessService;

	@Autowired
	private ArenaGuessTopicService arenaGuessTopicService;
	

	/**
	 * 竞猜下注
	 * @param userId  用户id
	 * @param quizId  竞猜id
	 * @param bettingObject 投注哪方
	 * @param bettingCount  投注数量(虚拟物品的数量)
	 * @author cxw
	 * @return
	 */
	@Transactional
	public String submitQuizB(Integer userId,Integer quizId, int bettingObject, Double bettingCount) {

		//查询 竞猜 是否存在， 并校验是否是坐庄
		ArenaGuess guess = new ArenaGuess();
		guess.setGuessId(quizId);
		guess = arenaGuessService.selectArenaGuess(guess);
		if(guess == null){
			LogUtil.error(this.getClass(), "submitQuizB", AppErrorCode.ERROR_DO_GUESS_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_DO_GUESS_REQUEST_PARAM).toJSONString();
		}
		
		if(guess.getEndTime() <= TimeUtil.getCurrentTimestamp()){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_DO_GUESS_TIME_END).toJSONString();
		}
		
		/*
		 * 1.如果是坐庄竞猜
		 * 2.下注人是庄家，则不能下注
		 */
		if(guess.getGuessType() == GameConstants.GUESS_LANDLORD && guess.getCreateUserId().intValue() == userId){
			LogUtil.error(this.getClass(), "submitQuizB", AppErrorCode.ERROR_DO_GUESS_LANDLORD);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_DO_GUESS_LANDLORD).toJSONString();
		}

		/* 如果是坐庄竞猜，
		 * 	1.并且投入资金大于 可投金额
		 * 	2.可投金额  <= 可投金额 - 已投总资金
			2.并且投注方向	跟	庄家方向一样
			返回错误
		 */
		String msg = guess.addAmount(bettingObject, bettingCount);
		if(msg !=null){
			LogUtil.error(this.getClass(), "submitQuizB", msg);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, msg).toJSONString();
		}

		//调用下注货币锁定方法,并产生流水记录
		centerMoneyLockService.submitMoneyLock(userId,bettingObject,bettingCount,guess);
		//竞猜话题参与人数 + 1
		arenaGuessTopicService.addOneGuessNum(guess.getTopicId(), null);
		LogUtil.info(this.getClass(), "submitQuizB", "下注成功");
		return Common.getReturn(AppErrorCode.SUCCESS,"").toJSONString();


	}


}
