package com.seentao.stpedu.common.dao;


import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterCashOut;
import com.seentao.stpedu.common.entity.CenterConvert;
import com.seentao.stpedu.common.sqlmap.CenterCashOutMapper;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;


@Repository
public class CenterCashOutDao {

	@Autowired
	private CenterCashOutMapper centerCashOutMapper;
	
	
	public void insertCenterCashOut(CenterCashOut centerCashOut){
		centerCashOutMapper .insertCenterCashOut(centerCashOut);
	}
	
	public void deleteCenterCashOut(CenterCashOut centerCashOut){
		centerCashOutMapper .deleteCenterCashOut(centerCashOut);
	}
	
	public void updateCenterCashOutByKey(CenterCashOut centerCashOut){
		centerCashOutMapper .updateCenterCashOutByKey(centerCashOut);
	}
	
	public List<CenterCashOut> selectSingleCenterCashOut(CenterCashOut centerCashOut){
		return centerCashOutMapper .selectSingleCenterCashOut(centerCashOut);
	}
	
	public CenterCashOut selectCenterCashOut(CenterCashOut centerCashOut){
		List<CenterCashOut> centerCashOutList = centerCashOutMapper .selectSingleCenterCashOut(centerCashOut);
		if(centerCashOutList == null || centerCashOutList .size() == 0){
			return null;
		}
		return centerCashOutList .get(0);
	}
	
	public List<CenterCashOut> selectAllCenterCashOut(){
		return centerCashOutMapper .selectAllCenterCashOut();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerCashOutMapper.countCenterCashOutListByCondition(paramMap);
	}
	
	
	public List<CenterCashOut> queryByPage(Map<String, Object> paramMap) {
		List<CenterCashOut> centerCashOutList = centerCashOutMapper .selectCenterCashOutListByCondition(paramMap);
		if(centerCashOutList == null || centerCashOutList .size() == 0){
			return null;
		}
		return centerCashOutList;
	}
	
}