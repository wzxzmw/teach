package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.CenterAccount;

public interface CenterAccountMapper {

	public abstract void insertCenterAccount(CenterAccount centerAccount)throws InsertObjectException;
	
	public abstract void deleteCenterAccount(CenterAccount centerAccount);
	
	public abstract void updateCenterAccountByKey(CenterAccount centerAccount);
	
	public abstract List<CenterAccount> selectSingleCenterAccount(CenterAccount centerAccount);
	
	public abstract List<CenterAccount> selectAllCenterAccount();

	public abstract List<CenterAccount> selectCenterAccountType(CenterAccount centerAccount);

	public abstract List<CenterAccount> selectCenterAType(CenterAccount centerAccount);
	
	public abstract List<CenterAccount> getMaxBalanceList(CenterAccount account);

	public abstract Integer getMaxBalanceCount(CenterAccount account);

	public abstract List<CenterAccount> selectCenterOneMoney(CenterAccount centerAccount);

	public abstract List<CenterAccount> findAllGuessResultAccount(CenterAccount tmp);

	public abstract void executeGuessAccountIncome(List<CenterAccount> account);

	public abstract List<CenterAccount> getCenterAccountList(Map<String, Object> param);

	public abstract void batchUpdateByUserIdAndAccountType(List<CenterAccount> list1);
	
}