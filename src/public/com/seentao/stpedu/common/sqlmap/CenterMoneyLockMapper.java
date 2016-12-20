package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import com.seentao.stpedu.common.entity.CenterMoneyLock;

public interface CenterMoneyLockMapper {

	public abstract int insertCenterMoneyLock(CenterMoneyLock centerMoneyLock);
	
	public abstract void deleteCenterMoneyLock(CenterMoneyLock centerMoneyLock);
	
	public abstract void updateCenterMoneyLockByKey(CenterMoneyLock centerMoneyLock);
	
	public abstract List<CenterMoneyLock> selectSingleCenterMoneyLock(CenterMoneyLock centerMoneyLock);
	
	public abstract List<CenterMoneyLock> selectAllCenterMoneyLock();

	public abstract List<CenterMoneyLock> findGuessLock(String lockIds);

	public abstract void executeGuessResult(CenterMoneyLock lock);

	
}