package com.seentao.stpedu.guess.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessBet;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterMoneyLock;
import com.seentao.stpedu.common.service.ArenaGuessBetService;
import com.seentao.stpedu.common.service.ArenaGuessService;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterMoneyLockService;
import com.seentao.stpedu.common.service.CenterSerialMaxService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;


/**
 * 竞猜结果 执行生成器
 * @author 	lw
 * @date	2016年6月26日  下午5:04:18
 *
 */
@Component
@Scope("prototype")
public class QuizResultsLogic{

	//执行是否成功
	private boolean isSuccess;
	//执行消息
	private JSONObject msg;
	
	//赢方向
	private int state = -1;
	
	//竞猜对象
	private ArenaGuess guess;
	
	//	竞猜下注 输赢	对象
	private List<ArenaGuessBet> winBet = new ArrayList<ArenaGuessBet>();
	private List<ArenaGuessBet> failBet = new ArrayList<ArenaGuessBet>();
	
	//	货币锁定  输赢	对象
	private List<CenterMoneyLock> winLock = new ArrayList<CenterMoneyLock>();
	private List<CenterMoneyLock> failLock = new ArrayList<CenterMoneyLock>();

	
	//	账户  对象 ids  
	private String accountIds = null;
	
	
	//临时容器对象
	private StringBuffer sb = new StringBuffer();
	 
	 
	 //赢方总金额
	private double wmoney = 0d;
	 //赢方押注总金额
	private double money = 0d;
	
	
	
	//竞猜下注 	对象
	@Autowired
	private ArenaGuessBetService 		betService;
	//货币锁定 	对象
	@Autowired
	private CenterMoneyLockService  	lockService;
	//账户	对象
	@Autowired
	private CenterAccountService		accountService;
	//货币变动	对象
	@Autowired
	private CenterMoneyChangeService	changeService;
	//流水号生成器
	@Autowired
	private CenterSerialMaxService		centerSerialMaxService;
	@Autowired
	private ArenaGuessService 			arenaGuessService;
	
	
	/**
	 * 总执行方法
	 * 
	 * @author 			lw
	 * @date			2016年6月26日  下午5:21:14
	 */
	@Transactional
	public QuizResultsLogic comeExecute(ArenaGuess guess){
		
		//	1. 初始化竞猜结果消息
		this.isSuccess = false;
		
		//	2. 竞猜执行对象校验
		if(guess == null){
			LogUtil.error(this.getClass(), "comeExecute", AppErrorCode.ERROR_QUIZRESULT_GUESS_AFTER_EXE);
			return this;
		}
		this.guess = guess;
		
		//	3. 检查是否已经分配结果
		ArenaGuess arenaGuess = new ArenaGuess();
		arenaGuess.setGuessId(guess.getGuessId());
		arenaGuess = arenaGuessService.selectArenaGuess(arenaGuess);
		if(arenaGuess.getStatus() == GameConstants.GUESS_FINISH){
			LogUtil.error(this.getClass(), "comeExecute", AppErrorCode.ERROR_QUIZRESULT_GUESS_FINISH);
			return this;
		}
		
		
		//	4. 获取分配奖金
		this.sumWinMoney();
		
		/*
		 * 	4. 查询所有的 竞猜下注集合。
		 * 循环
		 *   获取所有的金钱锁定id
		 *   配好 赢方 和 输方对象
		 */
		if(this.findAllGuessBet()){
			
			/*
			 * 	6.获取 输赢放货币锁定表 信息
			 * 整理出 输赢方 ids 准备 修改数据库账户信息
			 */
			if(this.findAllGuessLock()){
				
				/*
				 * 7.修改 个人钱包 
				 * 并生 货币变动表记录
				 */
				if(this.saveGuessResult()){
					this.isSuccess = true;
					return this;
				}
			}
		}
		
		return this;
	}
	
	
	/**
	 * 保存
	 * 
	 * @author 			lw
	 * @date			2016年6月26日  下午10:09:09
	 */
	private boolean saveGuessResult() {
		
		//	1. 查询输赢方账户信息
		List<CenterAccount> account = accountService.findAllGuessResultAccount(accountIds);
		
		if(CollectionUtils.isEmpty(account)){
			LogUtil.error(this.getClass(), "saveGuessResult", AppErrorCode.ERROR_QUIZRESULT_ACCOUNT);
			return false;
		}
		
		//	2. 货币变动持久化容器
		List<CenterMoneyChange> createChangeList = new ArrayList<CenterMoneyChange>();
		
		//	3. 庄家输多少钱临时变量
		Double failBankerMoney = null;
		
		/*
		 * 4. 循环下注表 计算 账户改变值
		 * 如果没有输方投入金钱，还给本钱.
		 * 如果有收益，扣除收益5%续费
		 * 
		 */
		
		//	赢家逻辑
		for(ArenaGuessBet bet : winBet){
			for(CenterAccount acc : account){
				
				//	4.1 判断用户账户
				if(bet.getUserId().equals(acc.getUserId())){
					
					//	4.2 回本钱
					acc.setLockAmount(acc.getLockAmount() - bet.getAmount());
					//	4.2.1 本钱流水更变
					createChangeList.add(
							new CenterMoneyChange(acc.getAccountId()
												, TimeUtil.getCurrentTimestamp()
												, bet.getLockId()
												, bet.getAmount()
												, GameConstants.MONEY_CHANGE_STATE_SUCCESS
												, GameConstants.MONEY_CHANGE_INCOME
												, guess.getGuessId()
												, GameConstants.MONEY_CHANGE_LINK_TYPE_GUESS 
												, centerSerialMaxService.getCenterSerialMaxByNowDate(2)));
					
					if(bet.getBonus().intValue() > 0){
						//	4.3 加上奖金
						acc.setBalance(acc.getBalance() + bet.getBonus());
						
						//	4.4 加入资金变动容器
						createChangeList.add(
								new CenterMoneyChange(acc.getAccountId()
										, TimeUtil.getCurrentTimestamp()
										, bet.getLockId()
										, bet.getBonus()
										, GameConstants.MONEY_CHANGE_STATE_SUCCESS
										, GameConstants.MONEY_CHANGE_INCOME
										, guess.getGuessId()
										, GameConstants.MONEY_CHANGE_LINK_TYPE_GUESS 
										, centerSerialMaxService.getCenterSerialMaxByNowDate(2)));
					}
					break;
				}
			}
		}
		
		//	输家逻辑
		for(ArenaGuessBet bet : failBet){
			for(CenterAccount acc : account){
				
				//	4.11 判断用户账户
				if(bet.getUserId().equals(acc.getUserId())){
					
					//	4.12 庄家输了逻辑
					if(GameConstants.GUESS_LANDLORD == guess.getGuessType() 
							&& GameConstants.GUESS_RESULT_INVALID != guess.getResult()
							&& guess.getResult().intValue() != guess.getBankerPosition() 
							&& (guess.getBankerPosition() == GameConstants.GUESS_CAN || guess.getBankerPosition() == GameConstants.GUESS_CANNOT)){
						
						//	4.12.1 计算庄家共赔金钱
						failBankerMoney = getBackMoney(guess.getBankerPosition())*guess.getOdds();
						
						//庄家总投入减去扣除金额
						failBankerMoney = bet.getAmount() - failBankerMoney;
						failBankerMoney =  failBankerMoney > 0d ? failBankerMoney : 0d;
						
						if(failBankerMoney > 0){
							
							createChangeList.add(
									new CenterMoneyChange(acc.getAccountId()
											, TimeUtil.getCurrentTimestamp()
											, bet.getLockId()
											, failBankerMoney
											, GameConstants.MONEY_CHANGE_STATE_SUCCESS
											, GameConstants.MONEY_CHANGE_INCOME
											, guess.getGuessId()
											, GameConstants.MONEY_CHANGE_LINK_TYPE_GUESS 
											, centerSerialMaxService.getCenterSerialMaxByNowDate(2)));
							
							//	4.12.2  增加未扣完的庄家金钱
							acc.setBalance( acc.getBalance() + failBankerMoney);
						}
						
					
					//	4.13 来宾输了逻辑
					}else{
						
						//	4.13.1 扣除余额
						acc.setBalance(acc.getBalance() - bet.getAmount());
					}
					
					//	4.13 扣除锁定资金
					acc.setLockAmount(acc.getLockAmount() - bet.getAmount());
					
					failBankerMoney = null;
					break;
				}
			}
		}
		
		
		try {
			//	5. 更新账户余额 和 锁定资金
			if(CollectionUtils.isEmpty(account)){
				LogUtil.error(this.getClass(), "saveGuessResult", AppErrorCode.ERROR_QUIZRESULT_RESULT_ACCOUNT);
				return false;
			}
			accountService.executeGuessAccount(account);
			
			//	6. 批量插入 货币变动表
			if(CollectionUtils.isEmpty(createChangeList)){
				LogUtil.error(this.getClass(), "saveGuessResult", AppErrorCode.ERROR_QUIZRESULT_RESULT_MONGEY_CHANGE);
				return false;
			}
			changeService.insertCenterMoneyChange(createChangeList);
			
			//	7. 获取锁定表的ids (输赢放)
			String winLockIds = this.getLockObjIds(winLock);
			String failLockIds = this.getLockObjIds(failLock);
			if(failLockIds == null && winLockIds == null){
				LogUtil.error(this.getClass(), "saveGuessResult", AppErrorCode.ERROR_QUIZRESULT_RESULT_LOCK);
				return false;
			}
			CenterMoneyLock lock = new CenterMoneyLock();
			
			//	8.1 批量修改赢放  货币锁定表  锁定状态
			if(winLockIds != null){
				lock.setLockIds(winLockIds);
				lock.setLockStatus(Integer.valueOf(GameConstants.CENTER_MONEY_LOCK_TWO));
				lockService.executeGuessResult(lock);
			}
			
			//	8.2 批量修改赢放  货币锁定表  锁定状态
			if(failLockIds != null){
				lock = new CenterMoneyLock();
				lock.setLockIds(failLockIds);
				lock.setLockStatus(Integer.valueOf(GameConstants.CENTER_MONEY_LOCK_THREE));
				lockService.executeGuessResult(lock);
			}
			
			//	9. 批量修改 竞猜下注表   下注结果  和 奖金
			betService.executeGuessResult(winBet, failBet);

			//	10. 修改分配完成 
			this.guess.setStatus(GameConstants.GUESS_FINISH);
			arenaGuessService.updateArenaGuessByKey(this.guess);
			
			//	11. 删除缓存
			JedisCache.delRedisVal(ArenaGuess.class.getSimpleName(), String.valueOf(this.guess.getGuessId()));
			LogUtil.info(this.getClass(), "saveGuessResult", String.valueOf(AppErrorCode.SUCCESS));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "saveGuessResult", AppErrorCode.ERROR_QUIZRESULT_TO_DB, e);
			return false;
		}
		
	}

	/**
	 * 获取锁定表的ids
	 * @param winLock2
	 * @return
	 * @author 			lw
	 * @date			2016年7月3日  下午2:05:31
	 */
	private String getLockObjIds(List<CenterMoneyLock> lockList) {
		
		//	1. 校验集合
		if(CollectionUtils.isEmpty(lockList)){
			return null;
		}
		
		//	2. ids拼接容器
		StringBuffer sb = new StringBuffer();
		
		//	3. 拼接获取 所有的lockId
		for(CenterMoneyLock en : lockList){
			sb.append(en.getLockId()).append(",");
		}
		
		String ids = sb.toString();
		return ids.substring(0, ids.length()-1);
	}


	/**
	 * 分类查询数据库 货币锁定表
	 * @author 			lw
	 * @date			2016年6月26日  下午8:38:38
	 */
	private boolean findAllGuessLock() {

		//	1. 查询数据库 货币锁定表
		winLock = lockService.findGuessLock(this.getLockIds(winBet));
		failLock = lockService.findGuessLock(this.getLockIds(failBet));
		
		//	2. 获取输赢 账户ids
		if(CollectionUtils.isEmpty(winLock) && CollectionUtils.isEmpty(failLock)){
			 error(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.ERROR_QUIZRESULT_GUESS_BET_FINISH);
			return false;
		}else{
			
			//	2.2 合并下注用户对象
			winLock.addAll(failLock);
			
			//	2.3 获取查询的所有账户ids
			accountIds = this.getAccountIds(winLock);
		}
		
		return true;
	}



	/**
	 * 获取账户ids
	 * @param value
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午9:42:17
	 */
	private String getAccountIds(List<CenterMoneyLock> value) {
		 
		//	1. 校验账户集合
		 if(CollectionUtils.isEmpty(value)){
			 return null;
		 }
		 
		 //	2. 去重临时容器
		 List<Integer> tmp = new ArrayList<Integer>();
		 
		 //	3. 初始化临时字符串拼接容器
		 sb.delete(0, sb.length());
		 
		 //	4. 循环账户锁定表对象获取账户id
		 for(CenterMoneyLock en : value){
			 
			 //	4.1 如果去重容器已经拥有账户id 不执行
			 if(tmp.contains(en.getAccountId())){
				 break;
			 }
			 
			 //	4.2 账户id加入去重容器
			 tmp.add(en.getAccountId());
			 
			 //	4.3 把账户id加入拼接返回结果
			 sb.append(en.getAccountId()).append(",");
		 }
		 
		 String ids = sb.toString();
		return ids.length() > 0 ? ids.substring(0, ids.length()-1) : null;
	}


	/**
	 * 拼接查询 货币锁定表ids
	 * @param bets
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午9:16:16
	 */
	private String getLockIds(List<ArenaGuessBet> bets) {
		
		 //	1. 校验集合
		 if(CollectionUtils.isEmpty(bets)){
			 error(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.ERROR_QUIZRESULT_LOCK_PERSON_NUM);
			 return null;
		 }
		 
		sb.delete(0, sb.length());
		
		//	2. 循环拼接 lockId
		for(ArenaGuessBet en : bets){
			sb.append(en.getLockId()).append(",");
		}
		String ids = sb.toString();
		return ids.length() > 0 ? ids.substring(0, ids.length()-1) : null ;
	}


	/**
	 * 计算分配金额。
	 * 
	 * @author 			lw
	 * @date			2016年6月26日  下午7:43:49
	 */
	private void sumWinMoney(){
		
		//	1. 获取赢家金钱
		this.getMoney(guess.getResult());
		
		//	2. 金钱初始化
		if(wmoney > 0 ){
			
			//	2.1 初始化赢家总金钱
			money = wmoney; 
			
			/*
			 * 2.2 
			 * 如果是坐庄，兵器是客人赢 ，计算分配金额。
			 * 其他方式分配金额直接取对方压的金额计算
			 */
			if(GameConstants.GUESS_LANDLORD == guess.getGuessType()){
				
				//如果是客人赢
				if(guess.getResult() != guess.getBankerPosition()){
					
					//取客人押金
					wmoney = this.getPositionMoney(guess.getResult());
					
					if(wmoney > 0){
						
						money = wmoney; 
						
						wmoney *= guess.getOdds();
						
					}else{
						money = wmoney = 0d; 
					}
					
				}
			}
			
			//	2.3 初始化总奖金金额
			wmoney *= (1 - GameConstants.GUESS_CUT);
		}
		
		
		//	3. 获取赢方标记
		state = guess.getResult();
	}

	/**
	 * 查询所有的竞猜下注对象
	 * 
	 * @author 			lw
	 * @date			2016年6月26日  下午7:47:09
	 */
	private boolean findAllGuessBet(){
		
		//	1. 获取竞猜所有的下注金额
		List<ArenaGuessBet> betList = betService.findAllGuessBet(guess.getGuessId());
		if(CollectionUtils.isEmpty(betList)){
			error(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZRESULT_BET);
			return false;
		}
		
		//	2. 按照输赢整理  竞猜下注 对象
		for(ArenaGuessBet en : betList){
			if(en.getPosition() == GameConstants.GUESS_CAN || en.getPosition() == GameConstants.GUESS_CANNOT){
				
				//	2.1 计算个人应该奖金
				personBonuses(en);
				
				//	2.2 输赢列队
				if(state == en.getPosition()){
					winBet.add(en);
				}else{
					failBet.add(en);
				}
			}
		}
		
		return true;
	}



	/**
	 * 计算个人应发奖金
	 * @param en
	 * @author 			lw
	 * @date			2016年6月26日  下午8:13:00
	 */
	private void personBonuses(ArenaGuessBet en) {
		
		 if(en.getPosition() == guess.getResult()){
			 
			 //	计算奖金
			 en.setBonus(money == 0 ? 0 : Math.floor((en.getAmount()/this.getPositionMoney(en.getPosition()))*wmoney*1000)/1000);
			 en.setBetResult(GameConstants.GUESS_RESULT_WIN);
		 }else{
			 en.setBetResult(GameConstants.GUESS_RESULT_FAIL);
		 }
		 
	}





	/**
	 * 获取赢家金钱(取反)
	 * @param bankerPosition
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午5:56:01
	 */
	private void getMoney(int bankerPosition) {
		wmoney = this.getBackMoney(bankerPosition);
	}
	
	/**
	 * 获取赢家金钱(取反)
	 * @param bankerPosition
	 * @return
	 * @author 			lw
	 * @date			2016年7月3日  下午4:07:57
	 */
	private Double getBackMoney(int bankerPosition){
		if(GameConstants.GUESS_CAN == bankerPosition){
			return guess.getNegativeAmount();
		}else if(GameConstants.GUESS_CANNOT == bankerPosition){
			return guess.getSureAmount();
		}
		return 0d;
	}
	
	/**
	 * 获取正反方金钱总额(取正)
	 * @param bankerPosition
	 * @return
	 * @author 			lw
	 * @date			2016年7月3日  下午1:10:15
	 */
	public Double getPositionMoney(int bankerPosition){
		if(GameConstants.GUESS_CAN == bankerPosition){
			return guess.getSureAmount();
		}else if(GameConstants.GUESS_CANNOT == bankerPosition){
			return  guess.getNegativeAmount();
		}
		return 0d;
	}
	
	
	
	
	
	
	/**
	 * 错误信息保存
	 * @param errorTypeTwo	错误类型
	 * @param errorCode		错误信息
	 * @author 				lw
	 * @date				2016年6月26日  下午7:52:16
	 */
	private void error(int errorTypeTwo,String errorCode){
		this.isSuccess = false;
		this.msg = new JSONObject();
		this.msg.put(GameConstants.CODE, errorTypeTwo);
		this.msg.put(GameConstants.MSG, errorCode);
	}
	
	/**
	 * 是否成功
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  上午11:00:15
	 */
	public boolean isSuccess(){
		return isSuccess;
	}
	
	/**
	 * 获取错误 信息
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  上午11:00:26
	 */
	public JSONObject getErrorMessage(){
		return msg;
	}
	
}