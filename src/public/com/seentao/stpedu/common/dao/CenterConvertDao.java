package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterConvert;
import com.seentao.stpedu.common.entity.CenterRecharge;
import com.seentao.stpedu.common.sqlmap.CenterConvertMapper;


@Repository
public class CenterConvertDao {

	@Autowired
	private CenterConvertMapper centerConvertMapper;
	
	
	public void insertCenterConvert(CenterConvert centerConvert){
		centerConvertMapper .insertCenterConvert(centerConvert);
	}
	
	public void deleteCenterConvert(CenterConvert centerConvert){
		centerConvertMapper .deleteCenterConvert(centerConvert);
	}
	
	public void updateCenterConvertByKey(CenterConvert centerConvert){
		centerConvertMapper .updateCenterConvertByKey(centerConvert);
	}
	
	public List<CenterConvert> selectSingleCenterConvert(CenterConvert centerConvert){
		return centerConvertMapper .selectSingleCenterConvert(centerConvert);
	}
	
	public CenterConvert selectCenterConvert(CenterConvert centerConvert){
		List<CenterConvert> centerConvertList = centerConvertMapper .selectSingleCenterConvert(centerConvert);
		if(centerConvertList == null || centerConvertList .size() == 0){
			return null;
		}
		return centerConvertList .get(0);
	}
	
	public List<CenterConvert> selectAllCenterConvert(){
		return centerConvertMapper .selectAllCenterConvert();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerConvertMapper.countCenterConvertListByCondition(paramMap);
	}
	
	
	public List<CenterConvert> queryByPage(Map<String, Object> paramMap) {
		List<CenterConvert> centerConvertList = centerConvertMapper .selectCenterConvertListByCondition(paramMap);
		if(centerConvertList == null || centerConvertList .size() == 0){
			return null;
		}
		return centerConvertList;
	}
	
}