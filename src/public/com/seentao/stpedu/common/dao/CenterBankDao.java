package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterBank;
import com.seentao.stpedu.common.sqlmap.CenterBankMapper;
import com.seentao.stpedu.persionalcenter.model.AccountTypes;


@Repository
public class CenterBankDao {

	@Autowired
	private CenterBankMapper centerBankMapper;
	
	
	public void insertCenterBank(CenterBank centerBank){
		centerBankMapper .insertCenterBank(centerBank);
	}
	
	public void deleteCenterBank(CenterBank centerBank){
		centerBankMapper .deleteCenterBank(centerBank);
	}
	
	public void updateCenterBankByKey(CenterBank centerBank){
		centerBankMapper .updateCenterBankByKey(centerBank);
	}
	
	public List<CenterBank> selectSingleCenterBank(CenterBank centerBank){
		return centerBankMapper .selectSingleCenterBank(centerBank);
	}
	
	public CenterBank selectCenterBank(CenterBank centerBank){
		List<CenterBank> centerBankList = centerBankMapper .selectSingleCenterBank(centerBank);
		if(centerBankList == null || centerBankList .size() == 0){
			return null;
		}
		return centerBankList .get(0);
	}
	
	public List<CenterBank> selectAllCenterBank(){
		return centerBankMapper .selectAllCenterBank();
	}
	public List<AccountTypes> selectAllAccountTypes(){
		return centerBankMapper .selectAllAccountTypes();
	}
	
	
}