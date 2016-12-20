package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ArenaGuessDao;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ArenaGuessService{
	
	@Autowired
	private ArenaGuessDao arenaGuessDao;
	@Autowired
	private ArenaGuessTopicService topicService;
	
	public String getArenaGuess(Integer id) {
		ArenaGuess arenaGuess = new ArenaGuess();
		List<ArenaGuess> arenaGuessList = arenaGuessDao .selectSingleArenaGuess(arenaGuess);
		if(arenaGuessList == null || arenaGuessList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(arenaGuessList);
	}
	
	public int insertArenaGuess(ArenaGuess arenaGuess){
		return arenaGuessDao .insertArenaGuess(arenaGuess);
	}
	
	public ArenaGuess selectArenaGuess(ArenaGuess arenaGuess){
		return arenaGuessDao.selectArenaGuess(arenaGuess);
	}
	
	
	public void updateArenaGuessByKey(ArenaGuess arenaGuess){
		arenaGuessDao .updateArenaGuessByKey(arenaGuess);
	}

	/**
	 * 关闭竞猜
	 * @param guessEnd
	 * @author 			lw
	 * @return 
	 * @date			2016年6月28日  上午10:38:14
	 */
	public JSONObject closeArenaGuess(int actionObjectId, int userId) {
		//	1. 获取 竞猜对象
		String redisData = RedisComponent.findRedisObject(actionObjectId, ArenaGuess.class);
		if(StringUtil.isEmpty(redisData)){
			LogUtil.error(this.getClass(), "finishQuiz",AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
		}
		ArenaGuess guess = JSONObject.parseObject(redisData, ArenaGuess.class);
		
		//	2. 获取 竞猜话题
		redisData = RedisComponent.findRedisObject(guess.getTopicId(), ArenaGuessTopic.class);
		if(StringUtil.isEmpty(redisData)){
			LogUtil.error(this.getClass(), "finishQuiz",AppErrorCode.ERROR_FINISHQUIZ_GUESSTOPIC);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_GUESSTOPIC);
		}
		ArenaGuessTopic topic = JSONObject.parseObject(redisData, ArenaGuessTopic.class);
		
		//	3. 校验是否是俱乐部会长
		if(topicService.checkClubPresident(topic.getCompId(),userId)){
			LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT);
		}
		
		// 3. 保存竞猜
		try {
			guess.setStatus(GameConstants.GUESS_END);
			guess.setEndTime(TimeUtil.getCurrentTimestamp());
			arenaGuessDao.closeArenaGuess(guess);
			
			// 4. 删除缓存
			JedisCache.delRedisVal(ArenaGuess.class.getSimpleName(), guess.getGuessId().toString());
			LogUtil.info(this.getClass(), "finishQuiz", GameConstants.SUCCESS_UPDATE);
			return Common.getReturn(AppErrorCode.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_TO_DB);
			return Common.getReturn(AppErrorCode.ERROR_UPDATE, String.valueOf(AppErrorCode.ERROR_FINISHQUIZ_TO_DB));
		}
		
		
	}

	/**
	 * 更新 竞猜表 ，判断是否是最大投注者，并且竞猜人数加一
	 * @param guess
	 * @param bettingCount
	 * @author 			lw
	 * @param userId 
	 * @date			2016年6月28日  下午4:35:13
	 */
	public void addGuessNumAndIsMaxAmount(ArenaGuess guess, double bettingCount, Integer userId) {
		guess.setJoinUserNum(guess.getJoinUserNum()+1);
		if(guess.getMaxAmount() < bettingCount){
			guess.setMaxAmount(bettingCount);
			guess.setMaxUserId(userId);
		}
		if(guess.getGuessId() == null){
			this.insertArenaGuess(guess);
		}else{
			this.updateArenaGuessByKey(guess);
			JedisCache.delRedisVal(ArenaGuess.class.getSimpleName(), String.valueOf(guess.getGuessId()));
		}
	}

	
	/**
	 * 获取最大投注者的 id 、昵称、金钱
	 * @param en		
	 * @author 			lw
	 * @date			2016年7月1日  下午3:22:16
	 */
	public List<ArenaGuess> findMaxGuess(String string) {
		return  arenaGuessDao.findMaxAmountGuess(string);
	
	}

	/** 
	* @Title: getMostArenaGuess 
	* @Description: 获取参与人数最多的竟猜
	* @return ArenaGuess
	* @author liulin
	* @date 2016年7月24日 下午3:48:36
	*/
	public ArenaGuess getMostArenaGuess() {
		return arenaGuessDao.getMostArenaGuess();
	}

	/**
	 * 根据话题id获取改话题总金额
	 * @param topicId
	 * @return
	 * @author 			lw
	 * @date			2016年7月30日  上午11:14:01
	 */
	public List<ArenaGuess> findAllAcountByGuessTopicId(String topicId) {
		return arenaGuessDao.findAllAcountByGuessTopicId(topicId);
	}


	
	
	
	
	
	
	
	
	
	
	
	
}