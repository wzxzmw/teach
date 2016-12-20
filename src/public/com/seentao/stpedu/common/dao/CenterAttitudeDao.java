package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.CenterAttitude;
import com.seentao.stpedu.common.sqlmap.CenterAttitudeMapper;


@Repository
public class CenterAttitudeDao {

	@Autowired
	private CenterAttitudeMapper centerAttitudeMapper;
	
	
	public void insertCenterAttitude(CenterAttitude centerAttitude){
		centerAttitudeMapper .insertCenterAttitude(centerAttitude);
	}
	
	public void deleteCenterAttitude(CenterAttitude centerAttitude){
		centerAttitudeMapper .deleteCenterAttitude(centerAttitude);
	}
	
	public void updateCenterAttitudeByKey(CenterAttitude centerAttitude){
		centerAttitudeMapper .updateCenterAttitudeByKey(centerAttitude);
	}
	
	public List<CenterAttitude> selectSingleCenterAttitude(CenterAttitude centerAttitude){
		return centerAttitudeMapper .selectSingleCenterAttitude(centerAttitude);
	}
	
	public CenterAttitude selectCenterAttitude(CenterAttitude centerAttitude){
		List<CenterAttitude> centerAttitudeList = centerAttitudeMapper .selectSingleCenterAttitude(centerAttitude);
		if(centerAttitudeList == null || centerAttitudeList .size() == 0){
			return null;
		}
		return centerAttitudeList .get(0);
	}
	
	public List<CenterAttitude> selectAllCenterAttitude(){
		return centerAttitudeMapper .selectAllCenterAttitude();
	}

	public Integer selectCenterAttitudeCount(CenterAttitude centerAttitude) {
		return centerAttitudeMapper.selectCenterAttitudeCount( centerAttitude);
	}
}