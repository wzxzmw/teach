package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAccountDao;
import com.seentao.stpedu.common.dao.CenterMoneyLockDao;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterMoneyLock;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class CenterMoneyLockService{

	@Autowired
	private ArenaGuessBetService arenaGuessBetService;
	@Autowired
	private CenterMoneyLockDao centerMoneyLockDao;
	@Autowired
	private CenterAccountDao centerAccountDao;
	@Autowired
	private CenterAccountDao accountDao;
	@Autowired
	private ArenaGuessService arenaGuessService;
	//货币变动	对象
	@Autowired
	private CenterMoneyChangeService	changeService;
	//流水号生成器
	@Autowired
	private CenterSerialMaxService		centerSerialMaxService;
	
	public String getCenterMoneyLock(Integer id) {
		CenterMoneyLock centerMoneyLock = new CenterMoneyLock();
		List<CenterMoneyLock> centerMoneyLockList = centerMoneyLockDao .selectSingleCenterMoneyLock(centerMoneyLock);
		if(centerMoneyLockList == null || centerMoneyLockList .size() <= 0){
			return null;
		}

		return JSONArray.toJSONString(centerMoneyLockList);
	}

	/**
	 * 竞猜下注
	 * @param userId  用户id
	 * @param bettingObject 投注哪方
	 * @param bettingCount  投注数量(虚拟物品的数量)
	 * @author cxw
	 * @param guess 
	 * @return
	 */
	public JSONObject submitMoneyLock(Integer userId,int bettingObject,Double bettingCount, ArenaGuess guess){
		try {
			
			//	1. 查询用户账户
			CenterAccount centerAccount = new CenterAccount();
			centerAccount.setUserId(userId);
			centerAccount.setAccountType(GameConstants.STAIR_TWO);
			CenterAccount account = centerAccountDao.selectCenterAccount(centerAccount);
			if(account !=null){
				Double MoneyCount = account.getBalance();
				if(null == MoneyCount){
					LogUtil.error(this.getClass(), "submitQuizB", "虚拟币不足");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.DEFICIENCY_OF_VIRTUAL_CURRENCY);
				}
				
				//	2. 账户余额比较
				if(bettingCount <= (MoneyCount - account.getLockAmount())){
					
					//	3. 锁定投注金额保存用户账户
					Double lockAmount = account.getLockAmount()+bettingCount;
					centerAccount.setLockAmount(lockAmount);
					centerAccount.setAccountId(account.getAccountId());
					// wangzx
					//centerAccount.setBalance(account.getBalance()-bettingCount);
					accountDao.updateCenterAccountByKey(centerAccount);
					
					//	4. 竞猜总参与人数加+1；并校验是否是最大投注者。
					arenaGuessService.addGuessNumAndIsMaxAmount(guess,bettingCount,userId);
					
					//	5. 下注金额锁定
					CenterMoneyLock centerMoneyLock = new CenterMoneyLock();
					centerMoneyLock.setUserId(userId);
					centerMoneyLock.setAccountId(account.getAccountId());
					centerMoneyLock.setCreateTime(TimeUtil.getCurrentTimestamp());
					centerMoneyLock.setRelObjetId(bettingObject);
					centerMoneyLock.setLockStatus(Integer.valueOf(GameConstants.CENTER_MONEY_LOCK_ONE));
					centerMoneyLock.setRelObjetType(Integer.valueOf(GameConstants.CENTER_MONEY_LOCK_T));
					centerMoneyLock.setRelObjetId(guess.getGuessId());
					centerMoneyLockDao.insertCenterMoneyLock(centerMoneyLock);
					
					//	6. 生成流水记录
					changeService.insertCenterMoneyChange(new CenterMoneyChange(account.getAccountId()
															, TimeUtil.getCurrentTimestamp()
															, centerMoneyLock.getLockId()
															, bettingCount
															, GameConstants.MONEY_CHANGE_STATE_SUCCESS
															, GameConstants.MONEY_CHANGE_EXPENDITURE
															, guess.getGuessId()
															, GameConstants.MONEY_CHANGE_LINK_TYPE_GUESS 
															, centerSerialMaxService.getCenterSerialMaxByNowDate(2)));
					//	7. 添加竞猜下注表
					arenaGuessBetService.submitQuizBett(userId,guess.getGuessId(),bettingObject,bettingCount,centerMoneyLock.getLockId());
					
					LogUtil.info(this.getClass(), "submitQuizB", String.valueOf(AppErrorCode.SUCCESS));
					return Common.getReturn(AppErrorCode.SUCCESS, String.valueOf(AppErrorCode.SUCCESS));
				}
			}
			
			//	8. 虚拟币不足
			LogUtil.error(this.getClass(), "submitQuizB", String.valueOf(AppErrorCode.DEFICIENCY_OF_VIRTUAL_CURRENCY));
			return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.DEFICIENCY_OF_VIRTUAL_CURRENCY);
			 
		} catch (Exception e) {
			e.printStackTrace();
			 LogUtil.error(this.getClass(), "submitQuizB", String.valueOf(AppErrorCode.ERROR_INSERT),e);
			return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, String.valueOf(AppErrorCode.ERROR_INSERT));
		}
	}

	/**
	 * 批量查询 现金锁定表
	 * @param lockIds	现金锁定表ids
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午9:21:30
	 */
	public List<CenterMoneyLock> findGuessLock(String lockIds) {
		if(!StringUtil.isEmpty(lockIds)){
			return centerMoneyLockDao.findGuessLock(lockIds);
		}
		return new ArrayList<CenterMoneyLock>();
	}



	public void executeGuessResult(CenterMoneyLock lock) {
		centerMoneyLockDao.executeGuessResult(lock);
	}
									


}