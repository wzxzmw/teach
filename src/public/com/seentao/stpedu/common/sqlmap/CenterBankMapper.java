package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import com.seentao.stpedu.common.entity.CenterBank;
import com.seentao.stpedu.persionalcenter.model.AccountTypes;

public interface CenterBankMapper {

	public abstract void insertCenterBank(CenterBank centerBank);
	
	public abstract void deleteCenterBank(CenterBank centerBank);
	
	public abstract void updateCenterBankByKey(CenterBank centerBank);
	
	public abstract List<CenterBank> selectSingleCenterBank(CenterBank centerBank);
	
	public abstract List<CenterBank> selectAllCenterBank();
	
	public abstract List<AccountTypes> selectAllAccountTypes();
	
}