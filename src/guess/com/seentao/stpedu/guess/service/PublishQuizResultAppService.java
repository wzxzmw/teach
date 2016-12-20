package com.seentao.stpedu.guess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.ArenaGuessTopicService;
import com.seentao.stpedu.common.threads.PulblicRunnableThreadPool;
import com.seentao.stpedu.common.threads.RuessResultRunnable;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class PublishQuizResultAppService {

	//竞猜
	@Autowired
	private ArenaGuessService 			guessService;
	@Autowired
	private RuessResultRunnable 		ruessResultRunnable;
	@Autowired
	private ArenaGuessTopicService 		guessTopicService;
	
	
	/**
	 * 竞猜公布结果
	 * 	如果坐庄竞猜 ： 庄家 投入 50000  赔率是 2  对方投入 10000。庄家输，这时候是否要返还庄家30000
	 * @param userId			用户id
	 * @param quizId			竞猜id
	 * @param quizWinner		竞猜获胜方
	 * @return
	 * @author 					lw
	 * @date					2016年6月26日  下午2:20:14
	 */
	
	@Transactional
	public String submitQuizResult(Integer userId, Integer quizId, Integer quizWinner) {
		
		
		//	1. 获取竞猜
		String redisData = RedisComponent.findRedisObject(quizId, ArenaGuess.class);
		if(StringUtil.isEmpty(redisData)){
			LogUtil.error(this.getClass(), "submitQuizResult", AppErrorCode.ERROR_QUIZRESULT_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZRESULT_REQUEST_PARAM).toJSONString();
			
		}
		ArenaGuess guess = JSONObject.parseObject(redisData, ArenaGuess.class);
		
		
		//		2. 获取 竞猜话题
		redisData = RedisComponent.findRedisObject(guess.getTopicId(), ArenaGuessTopic.class);
		if(StringUtil.isEmpty(redisData)){
			LogUtil.error(this.getClass(), "finishQuiz",AppErrorCode.ERROR_FINISHQUIZ_GUESSTOPIC);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_GUESSTOPIC).toJSONString();
		}
		ArenaGuessTopic topic = JSONObject.parseObject(redisData, ArenaGuessTopic.class);
		
		//	3. 校验是否是俱乐部会长
		if(guessTopicService.checkClubPresident(topic.getCompId(),userId)){
			LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT).toJSONString();
		}
		
		//	2. 校验竞猜未公布结果、竞猜已经结束
		if(guess.getResult() == GameConstants.GUESS_RESULT_NOT && GameConstants.GUESS_END == guess.getStatus()){
			
			//	2.1 改变竞猜结果
			guess.setResult(quizWinner);
			
			//	2.2 一个小时之后公布结果
			guess.setResultTime(TimeUtil.getCurrentTimestamp() + GameConstants.GUESS_RESULT_TIME);
			//guess.setResultTime(0);
			//	2.3 竞猜对象加入多线程延迟执行结果集
			ruessResultRunnable.setGuess(guess);
			
			//	2.4 执行多线程
			PulblicRunnableThreadPool.submit(ruessResultRunnable);
			
			//	2.5 持久化竞猜对象
			guessService.updateArenaGuessByKey(guess);
			
			//	2.6 删除竞猜缓存
			JedisCache.delRedisVal(ArenaGuess.class.getSimpleName(), String.valueOf(guess.getGuessId()));
			LogUtil.info(this.getClass(), "submitQuizResult", String.valueOf(AppErrorCode.SUCCESS));
			return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
			
		}
		
		LogUtil.error(this.getClass(), "submitQuizResult", AppErrorCode.ERROR_QUIZRESULT_GUESS_NOT_END);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_THREE, AppErrorCode.ERROR_QUIZRESULT_GUESS_NOT_END).toJSONString();
		
	}

}
