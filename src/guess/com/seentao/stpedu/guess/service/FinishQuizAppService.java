package com.seentao.stpedu.guess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.ArenaGuessTopicService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class FinishQuizAppService {

	
	@Autowired
	private ArenaGuessTopicService guessTopicService;
	
	@Autowired
	private ArenaGuessService guessService;
	
	
	
	/**
	 * 	结束竞猜话题或竞猜
	 * @param userId			用户id
	 * @param actionType		操作类型
	 * @param actionObjectId	操作对象id
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  上午11:33:29
	 */
	public String finishQuiz(Integer userId, Integer actionObjectId, Integer actionType) {
		
		//	1. 结束竞猜话题
		if(GameConstants.FINISHQUIZ_GUESS_TOPIC == actionType){
			return guessTopicService.closeArenaGuessTopic(actionObjectId,userId).toJSONString();
			
		//	2. 结束竞猜
		}else if(GameConstants.FINISHQUIZ_GUESS == actionType){
			return guessService.closeArenaGuess(actionObjectId, userId).toJSONString();
		}
		
		LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM).toJSONString();
	}
	
	
	
	

}
