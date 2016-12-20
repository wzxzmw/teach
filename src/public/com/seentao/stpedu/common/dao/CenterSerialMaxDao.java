package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.CenterSerialMax;
import com.seentao.stpedu.common.sqlmap.CenterSerialMaxMapper;


@Repository
public class CenterSerialMaxDao {

	@Autowired
	private CenterSerialMaxMapper centerSerialMaxMapper;
	
	
	public void insertCenterSerialMax(CenterSerialMax centerSerialMax){
		centerSerialMaxMapper .insertCenterSerialMax(centerSerialMax);
	}
	
	public void deleteCenterSerialMax(CenterSerialMax centerSerialMax){
		centerSerialMaxMapper .deleteCenterSerialMax(centerSerialMax);
	}
	
	public void updateCenterSerialMaxByKey(CenterSerialMax centerSerialMax){
		centerSerialMaxMapper .updateCenterSerialMaxByKey(centerSerialMax);
	}
	
	public List<CenterSerialMax> selectSingleCenterSerialMax(CenterSerialMax centerSerialMax){
		return centerSerialMaxMapper .selectSingleCenterSerialMax(centerSerialMax);
	}
	
	public CenterSerialMax selectCenterSerialMax(CenterSerialMax centerSerialMax){
		List<CenterSerialMax> centerSerialMaxList = centerSerialMaxMapper .selectSingleCenterSerialMax(centerSerialMax);
		if(centerSerialMaxList == null || centerSerialMaxList .size() == 0){
			return null;
		}
		return centerSerialMaxList .get(0);
	}
	
	public List<CenterSerialMax> selectAllCenterSerialMax(){
		return centerSerialMaxMapper .selectAllCenterSerialMax();
	}
}