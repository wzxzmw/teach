package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.dao.CenterAccountDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class CenterAccountService{
	
	@Autowired
	private CenterAccountDao centerAccountDao;
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	
	public List<CenterAccount> getCenterAccountList(CenterAccount centerAccount) {
		List<CenterAccount> centerAccountList = centerAccountDao .selectSingleCenterAccount(centerAccount);
		if(centerAccountList == null || centerAccountList .size() <= 0){
			return null;
		}
		return centerAccountList;
	}
	
	public CenterAccount getCenterAccount(CenterAccount centerAccount) {
		List<CenterAccount> centerAccountList = centerAccountDao .selectSingleCenterAccount(centerAccount);
		if(centerAccountList == null || centerAccountList .size() <= 0){
			return null;
		}
		
		return centerAccountList.get(0);
	}
	/**
	 * 
	 * @param userId
	 * @param bettingCount 虚拟币数量
	 * @return
	 * @author chaixw
	 */
	public String decrCurrency(Integer userId, double bettingCount) {
		CenterAccount centerAccount = new CenterAccount();
		centerAccount.setUserId(Integer.valueOf(userId));
		centerAccount.setUserType(GameConstants.INDIVIDUAL_USER);
		centerAccount.setAccountType(GameConstants.STAIR_ONE);
		CenterAccount account = centerAccountDao.selectCenterAccount(centerAccount);
		if(account == null){
			LogUtil.error(this.getClass(), "decrCurrency", "没有获取到相应用户的虚拟币");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, "没有获取到相应用户的虚拟币").toJSONString();
		}
		
		double userMoney = account.getBalance() == null ? 0 : account.getBalance();
		double lockAmount = account.getLockAmount() == null ? 0 : account.getLockAmount();
		double uMoney = userMoney - lockAmount;
		//判断虚拟币是否足够
		if(bettingCount > uMoney){
			LogUtil.error(this.getClass(), "decrCurrency", "虚拟币不足");
			return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "虚拟币不足").toJSONString();
		}
		account.setBalance(uMoney - bettingCount);
		centerAccountDao.updateCenterAccountByKey(account);
		//add by lijin 2016/07/31
		//货币变动修改
		centerMoneyChangeService.addCenterMoneyChange(account.getAccountId(), bettingCount, String.valueOf(account.getUserId()),GameConstants.MONEY_CHANGE_LINK_TYPE_BUY,GameConstants.SPENDING);
		return null;
		 
	}
	
	/**
	 * 
	 * @param userId
	 * @param bettingCount 虚拟币数量
	 * @return
	 * @author chaixw
	 */
	public String decrCurrencyClubUser(Integer userId, double bettingCount,int accountType,int userType ) {
		CenterAccount centerAccount = new CenterAccount();
		centerAccount.setUserId(Integer.valueOf(userId));
		centerAccount.setUserType(userType);
		centerAccount.setAccountType(accountType);
		CenterAccount account = centerAccountDao.selectCenterAccount(centerAccount);
		if(account == null){
			LogUtil.error(this.getClass(), "decrCurrency", "没有获取到相应用户的虚拟币");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, "没有获取到相应用户的虚拟币").toJSONString();
		}
		
		double userMoney = account.getBalance() == null ? 0 : account.getBalance();
		double lockAmount = account.getLockAmount() == null ? 0 : account.getLockAmount();
		double uMoney = userMoney - lockAmount;
		//判断虚拟币是否足够
		if(bettingCount > uMoney){
			LogUtil.error(this.getClass(), "decrCurrency", "虚拟币不足");
			return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "虚拟币不足").toJSONString();
		}
		account.setBalance(uMoney - bettingCount);
		centerAccountDao.updateCenterAccountByKey(account);
		return String.valueOf(account.getAccountId());
		 
	}
	
	
	public List<CenterAccount> getMaxBalanceList(CenterAccount account) {
		return centerAccountDao.getMaxBalanceList( account);
	}

	public Integer getMaxBalanceCount(CenterAccount account) {
		return centerAccountDao.getMaxBalanceCount( account);
	}
	
	/**
	 * updateCenterAccountByKey(加入班级时用户账户表减去对应金额)   
	 * @author ligs
	 * @date 2016年6月26日 下午8:02:29
	 * @return
	 */
	public void updateCenterAccountByKey(CenterAccount centerAccount){
		centerAccountDao.updateCenterAccountByKey(centerAccount);
	}
	
	/**
	 * 批量获取用户账户信息
	 * @param winAccountIds		用户账户ids
	 * @return
	 * @author 					lw
	 * @date					2016年6月27日  上午9:15:40
	 */
	public List<CenterAccount> findAllGuessResultAccount(String accountIds) {
		if(accountIds != null){
			CenterAccount tmp = new CenterAccount();
			tmp.setIds(accountIds);
			tmp.setAccountType(GameConstants.STAIR_TWO);
			return centerAccountDao.findAllGuessResultAccount(tmp);
		}
		return new ArrayList<CenterAccount>();
	}
	
	/**
	 * 竞猜 ,更新账户余额
	 * @param winAccount		更新对象
	 * @author 					lw
	 * @date					2016年6月27日  上午9:40:03
	 */
	public void executeGuessAccount(List<CenterAccount> account) {
		centerAccountDao.executeGuessAccountIncome(account);
	}
	
	
	public void insertCenterAccount(CenterAccount oneLevel)throws InsertObjectException {
		try{
			centerAccountDao.insertCenterAccount(oneLevel);
		}catch(InsertObjectException e){
			LogUtil.error(this.getClass(), "insertCenterAccount", "insertCenterAccount is error", e);
			throw new InsertObjectException("insert obj is error",e);
		}
	}
	
	/**
	 * 判断用户账户金额是否充足
	 * @param userId 用户ID
	 * @param amount 金额
	 * @return
	 * @author chengshx
	 */
	public Boolean isMoneyEnough(Integer userId, Double amount){
		CenterAccount centerAccount = new CenterAccount();
		centerAccount.setUserId(userId);
		centerAccount.setUserType(GameConstants.INDIVIDUAL_USER);
		centerAccount.setAccountType(GameConstants.STAIR_ONE);
		CenterAccount account = centerAccountDao.selectCenterAccount(centerAccount);
		//用户不存在账户
		if(account!=null){
			double balance = account.getBalance() == null ? 0 : account.getBalance();
			double lockAmount = account.getLockAmount() == null ? 0 : account.getLockAmount();
			double userMoney = balance - lockAmount;
			//判断虚拟币是否足够
			if(amount > userMoney){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	public List<CenterAccount> getCenterAccountList(List<Integer> sendUserIds, Integer redPacketType) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account_type",redPacketType);
		param.put("ids", sendUserIds);
		return centerAccountDao.getCenterAccountList(param) ;
	}
	
	public void batchUpdateByUserIdAndAccountType(List<CenterAccount> list1) {
		centerAccountDao.batchUpdateByUserIdAndAccountType(list1);
	}
	
	/**
	 * 更新俱乐部账户余额
	 * @param addAmount
	 * @param clubId
	 */
	public void updateClubCenterAccount(Double addAmount ,String clubId){
		try {
			CenterAccount account = null;
			account = new CenterAccount();
			account.setUserId(Integer.valueOf(clubId));
			CenterAccount account2 = centerAccountDao.selectCenterAccount(account);
			Double balance = account2.getBalance();
			account = new CenterAccount();
			account.setAccountId(account2.getAccountId());
			account.setBalance(balance+addAmount);
			centerAccountDao.updateCenterAccountByKey(account);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 更新俱乐部账户余额
	 * @param addAmount
	 * @param clubId
	 */
	public void updateClubCenterAccountAndChange(Double addAmount ,String clubId){
		try {
			CenterAccount account = null;
			account = new CenterAccount();
			account.setUserId(Integer.valueOf(clubId));
			CenterAccount account2 = centerAccountDao.selectCenterAccount(account);
			
			account = new CenterAccount();
			account.setAccountId(account2.getAccountId());
			account.setBalance(addAmount);
			centerAccountDao.updateCenterAccountByKey(account);
			centerMoneyChangeService.addCenterMoneyChange(account2.getAccountId(), addAmount, String.valueOf(account.getUserId()),GameConstants.MONEY_CHANGE_LINK_TYPE_BUY,GameConstants.INCOME);
			
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}