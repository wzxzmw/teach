package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterMoneyChange;

public interface CenterMoneyChangeMapper {

	public abstract void insertCenterMoneyChange(CenterMoneyChange centerMoneyChange);
	
	public abstract void deleteCenterMoneyChange(CenterMoneyChange centerMoneyChange);
	
	public abstract void updateCenterMoneyChangeByKey(CenterMoneyChange centerMoneyChange);
	
	public abstract List<CenterMoneyChange> selectSingleCenterMoneyChange(CenterMoneyChange centerMoneyChange);
	
	public abstract List<CenterMoneyChange> selectAllCenterMoneyChange();

	public abstract void insertCenterMoneyChangeList(List<CenterMoneyChange> centerMoneyChange);
	
	public abstract List<CenterMoneyChange> selectCenterMoneyChangeByCondition(Map<String,Object> conditons);
	
	public abstract int countCenterMoneyChangeByCondition(Map<String,Object> conditons);
}