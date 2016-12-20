package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterRecharge;
import com.seentao.stpedu.common.sqlmap.CenterRechargeMapper;


@Repository
public class CenterRechargeDao {

	@Autowired
	private CenterRechargeMapper centerRechargeMapper;
	
	
	public void insertCenterRecharge(CenterRecharge centerRecharge){
		centerRechargeMapper .insertCenterRecharge(centerRecharge);
	}
	
	public void deleteCenterRecharge(CenterRecharge centerRecharge){
		centerRechargeMapper .deleteCenterRecharge(centerRecharge);
	}
	
	public void updateCenterRechargeByKey(CenterRecharge centerRecharge){
		centerRechargeMapper .updateCenterRechargeByKey(centerRecharge);
	}
	
	public List<CenterRecharge> selectSingleCenterRecharge(CenterRecharge centerRecharge){
		return centerRechargeMapper .selectSingleCenterRecharge(centerRecharge);
	}
	
	public CenterRecharge selectCenterRecharge(CenterRecharge centerRecharge){
		List<CenterRecharge> centerRechargeList = centerRechargeMapper .selectSingleCenterRecharge(centerRecharge);
		if(centerRechargeList == null || centerRechargeList .size() == 0){
			return null;
		}
		return centerRechargeList .get(0);
	}
	
	public List<CenterRecharge> selectAllCenterRecharge(){
		return centerRechargeMapper .selectAllCenterRecharge();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerRechargeMapper.countCenterRechargeByCondition(paramMap);
	}
	
	
	public List<CenterRecharge> queryByPage(Map<String, Object> paramMap) {
		List<CenterRecharge> centerRechargeList = centerRechargeMapper .selectCenterRechargeByCondition(paramMap);
		if(centerRechargeList == null || centerRechargeList .size() == 0){
			return null;
		}
		return centerRechargeList;
	}
}