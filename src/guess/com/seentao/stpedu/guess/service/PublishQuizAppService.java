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
import com.seentao.stpedu.common.service.CenterMoneyLockService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class PublishQuizAppService {

	
	@Autowired
	private ArenaGuessService arenaGuessService;
	@Autowired
	private ArenaGuessTopicService guessTopicService;
	@Autowired
	private CenterMoneyLockService moneyLockService;

	
	
	
	/**
	 * 创建竞猜
	 * @param userId					用户id
	 * @param quizTopicId				竞猜话题id
	 * @param quizType					竞猜类型					1:普通竞猜；2:坐庄竞猜；
	 * @param quizTitle					竞猜主题
	 * @param quizEndDate				竞猜结束时间的时间戳
	 * @param bankerBettingObject		庄家押哪方					1:能；2:不能； 		只有竞猜类型为坐庄竞猜时传该字段
	 * @param odds						赔率
	 * @param bankerBettingCount		庄家投注数量(虚拟物品的数量)		底金
	 * @return
	 * @author 							lw
	 * @date							2016年6月24日  下午9:48:16
	 * 
	 * 公布结果时间，在公布结果的时候 才修改出现。
	 * 正方金额 ： 所有压正方的金钱总额
	 * 反方金额：所有压反方金额的总额
	 */
	@Transactional
	public String submitQuiz(Integer userId, Integer quizTopicId, Integer quizType, String quizTitle,
			Integer quizEndDate, Integer bankerBettingObject, Float odds, Double bankerBettingCount) {
		
		//	1. 获取竞猜主题
		String guessRedis = RedisComponent.findRedisObject(quizTopicId, ArenaGuessTopic.class);
		if(StringUtil.isEmpty(guessRedis)){
			LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZ_REQUEST_PARAM).toJSONString();
			
		}
		ArenaGuessTopic guessTopic = JSONObject.parseObject(guessRedis, ArenaGuessTopic.class);
		
		//	2. 判断竞猜结束时间
		if(guessTopic.getCreateTime() > quizEndDate){
			LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_SUBMIT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZ_SUBMIT).toJSONString();
		}
		
		//	3. 判断竞猜话题是否进行中
		if(guessTopic.getStatus() == GameConstants.GUESS_START_TOPIC ){
			
			//	3. 判断竞猜类型 , 如果竞猜类型不是 坐庄，或者是普通
			if(quizType == GameConstants.GUESS_ORDINARY || 		//普通竞猜
					(quizType == GameConstants.GUESS_LANDLORD &&  (bankerBettingObject == GameConstants.GUESS_CAN || bankerBettingObject == GameConstants.GUESS_CANNOT) )){
				
					//	4. 校验坐庄 最低押金 和  赔率范围
					if(quizType == GameConstants.GUESS_LANDLORD &&  (bankerBettingCount < GameConstants.GUESS_MONEY_MIN 
							|| odds < GameConstants.GUESS_ODDS_MIN || odds > GameConstants.GUESS_ODDS_MAX)){
						LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_MONEY_ODDS);
						return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZ_MONEY_ODDS).toJSONString();
					}
				
					//	5. 竞猜基本参数组装
					ArenaGuess  guess = new ArenaGuess();
					guess.initAmount();
					guess.setGuessTitle(quizTitle);
					guess.setEndTime(quizEndDate);
					guess.setGuessType(quizType);
					guess.setTopicId(quizTopicId);
					guess.setMaxAmount(0d);
					guess.setMaxUserId(0);
					guess.setJoinUserNum(0);
					guess.setCreateUserId(userId);
					guess.setCreateTime(TimeUtil.getCurrentTimestamp());
					guess.setStatus(GameConstants.GUESS_START);
					guess.setResult(GameConstants.GUESS_RESULT_NOT);
					
					//	6. 坐庄参数组装： 保存  方向、押金、赔率
					if(GameConstants.GUESS_LANDLORD == quizType){
						
						//竞猜对象组装
						String msg = guess.addAmount(bankerBettingObject, bankerBettingCount);
						if(msg != null){
							LogUtil.error(this.getClass(), "submitQuiz", msg);
							return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, msg).toJSONString();
						}
						guess.setBankerPosition(bankerBettingObject);
						guess.setBankerAmount(bankerBettingCount);
						guess.setOdds(Double.parseDouble(odds+""));
						
					}
					
					try {
						
						//	7.1 庄家 下注 信息保存 	扣钱、锁定资金、下注表添加、参加竞猜人数+1，计算最大投注者
						if(GameConstants.GUESS_LANDLORD == quizType){
							JSONObject msg = moneyLockService.submitMoneyLock(userId, guess.getBankerPosition(), guess.getBankerAmount(),guess);
							
							//	7.1.2 下注失败返回
							if(msg.get(GameConstants.CODE)!= null && Integer.valueOf(msg.get(GameConstants.CODE).toString()) != GameConstants.RESULT_SUCCESS){
								return msg.toJSONString();
							}
							
						//	7.2 普通竞猜直接保存
						}else{
							arenaGuessService.insertArenaGuess(guess);
						}
						
						//	8. 更新 竞猜话题 中竞猜数量,(如果是坐庄：竞猜话题总人数量+1)
						guessTopicService.addOneGuessNum(quizTopicId,GameConstants.GUESS_LANDLORD == quizType? true : false);
						LogUtil.info(this.getClass(), "submitQuiz", GameConstants.SUCCESS_INSERT);
						return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
						
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_INSERT, e);
						return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_QUIZ_INSERT).toJSONString();
					}
				
			}else{
				
				LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_TYPE);
				return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_QUIZ_TYPE).toJSONString();
			}
		}
		
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GUESS_END_TOPIC).toJSONString();
	}

	

}
