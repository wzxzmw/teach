package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.sqlmap.CenterAccountMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class CenterAccountDao {

	@Autowired
	private CenterAccountMapper centerAccountMapper;


	public void insertCenterAccount(CenterAccount centerAccount)throws InsertObjectException{
		try{
			centerAccountMapper.insertCenterAccount(centerAccount);
		}catch(InsertObjectException e){
			LogUtil.error(this.getClass(), "insertCenterAccount", "centerAccountMapper is error"+e);
			throw new InsertObjectException("insert object is error",e);
		}
	}

	public void deleteCenterAccount(CenterAccount centerAccount){
		centerAccountMapper .deleteCenterAccount(centerAccount);
	}

	public void updateCenterAccountByKey(CenterAccount centerAccount){
		centerAccountMapper .updateCenterAccountByKey(centerAccount);
	}

	public List<CenterAccount> selectSingleCenterAccount(CenterAccount centerAccount){
		return centerAccountMapper .selectSingleCenterAccount(centerAccount);
	}

	public CenterAccount selectCenterAccount(CenterAccount centerAccount){
		List<CenterAccount> centerAccountList = centerAccountMapper .selectSingleCenterAccount(centerAccount);
		if(centerAccountList == null || centerAccountList .size() == 0){
			return null;
		}
		return centerAccountList .get(0);
	}

	public List<CenterAccount> selectAllCenterAccount(){
		return centerAccountMapper .selectAllCenterAccount();
	}

	public CenterAccount selectCenterAccountType(CenterAccount centerAccount) {
		List<CenterAccount> centerAccountList = centerAccountMapper .selectCenterAccountType(centerAccount);
		if(centerAccountList == null || centerAccountList .size() == 0){
			return null;
		}
		return centerAccountList .get(0);
	}

	public CenterAccount selectCenterAType(CenterAccount centerAccount){
		List<CenterAccount> centerAccountList = centerAccountMapper .selectCenterAType(centerAccount);
		if(centerAccountList == null || centerAccountList .size() == 0){
			return null;
		}
		return centerAccountList .get(0);
	}
	public List<CenterAccount> getMaxBalanceList(CenterAccount account) {
		return centerAccountMapper.getMaxBalanceList( account);
	}

	public Integer getMaxBalanceCount(CenterAccount account) {
		return centerAccountMapper.getMaxBalanceCount( account) ;
	}

	public CenterAccount selectCenterOneMoney(CenterAccount centerAccount) {
		List<CenterAccount> centerAccountList = centerAccountMapper .selectCenterOneMoney(centerAccount);
		if(centerAccountList == null || centerAccountList .size() == 0){
			return null;
		}
		return centerAccountList .get(0);
	}
	
	public List<CenterAccount> findAllGuessResultAccount(CenterAccount tmp) {
		return centerAccountMapper.findAllGuessResultAccount(tmp);
	}

	public void executeGuessAccountIncome(List<CenterAccount> account) {
		centerAccountMapper.executeGuessAccountIncome(account);
	}


	public List<CenterAccount> getCenterAccountList(Map<String, Object> param) {
		return centerAccountMapper.getCenterAccountList(param);
	}

	public void batchUpdateByUserIdAndAccountType(List<CenterAccount> list1) {
		centerAccountMapper.batchUpdateByUserIdAndAccountType(list1);
	}

}