package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.sqlmap.CenterMoneyChangeMapper;


@Repository
public class CenterMoneyChangeDao {

	@Autowired
	private CenterMoneyChangeMapper centerMoneyChangeMapper;
	
	
	public void insertCenterMoneyChange(CenterMoneyChange centerMoneyChange){
		centerMoneyChangeMapper .insertCenterMoneyChange(centerMoneyChange);
	}
	
	public void deleteCenterMoneyChange(CenterMoneyChange centerMoneyChange){
		centerMoneyChangeMapper .deleteCenterMoneyChange(centerMoneyChange);
	}
	
	public void updateCenterMoneyChangeByKey(CenterMoneyChange centerMoneyChange){
		centerMoneyChangeMapper .updateCenterMoneyChangeByKey(centerMoneyChange);
	}
	
	public List<CenterMoneyChange> selectSingleCenterMoneyChange(CenterMoneyChange centerMoneyChange){
		return centerMoneyChangeMapper .selectSingleCenterMoneyChange(centerMoneyChange);
	}
	
	public CenterMoneyChange selectCenterMoneyChange(CenterMoneyChange centerMoneyChange){
		List<CenterMoneyChange> centerMoneyChangeList = centerMoneyChangeMapper .selectSingleCenterMoneyChange(centerMoneyChange);
		if(centerMoneyChangeList == null || centerMoneyChangeList .size() == 0){
			return null;
		}
		return centerMoneyChangeList .get(0);
	}
	
	public List<CenterMoneyChange> selectAllCenterMoneyChange(){
		return centerMoneyChangeMapper .selectAllCenterMoneyChange();
	}

	public void insertCenterMoneyChange(List<CenterMoneyChange> centerMoneyChange) {
		centerMoneyChangeMapper.insertCenterMoneyChangeList(centerMoneyChange);
		
	}
	
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerMoneyChangeMapper.countCenterMoneyChangeByCondition(paramMap);
	}
	
	public List<CenterMoneyChange> queryByPage(Map<String, Object> paramMap) {
		List<CenterMoneyChange> centerMoneyChangeList = centerMoneyChangeMapper .selectCenterMoneyChangeByCondition(paramMap);
		if(centerMoneyChangeList == null || centerMoneyChangeList .size() == 0){
			return null;
		}
		return centerMoneyChangeList;
	}
	
	
	
}