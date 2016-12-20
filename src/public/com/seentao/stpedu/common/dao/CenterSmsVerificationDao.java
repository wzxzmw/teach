package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterSmsVerification;
import com.seentao.stpedu.common.sqlmap.CenterSmsVerificationMapper;


@Repository
public class CenterSmsVerificationDao {

	@Autowired
	private CenterSmsVerificationMapper centerSmsVerificationMapper;
	
	
	public void insertCenterSmsVerification(CenterSmsVerification centerSmsVerification){
		centerSmsVerificationMapper .insertCenterSmsVerification(centerSmsVerification);
	}
	
	public void deleteCenterSmsVerification(CenterSmsVerification centerSmsVerification){
		centerSmsVerificationMapper .deleteCenterSmsVerification(centerSmsVerification);
	}
	
	public void updateCenterSmsVerificationByKey(CenterSmsVerification centerSmsVerification){
		centerSmsVerificationMapper .updateCenterSmsVerificationByKey(centerSmsVerification);
	}
	
	public List<CenterSmsVerification> selectSingleCenterSmsVerification(CenterSmsVerification centerSmsVerification){
		return centerSmsVerificationMapper .selectSingleCenterSmsVerification(centerSmsVerification);
	}
	
	public CenterSmsVerification selectCenterSmsVerification(CenterSmsVerification centerSmsVerification){
		List<CenterSmsVerification> centerSmsVerificationList = centerSmsVerificationMapper .selectSingleCenterSmsVerification(centerSmsVerification);
		if(centerSmsVerificationList == null || centerSmsVerificationList .size() == 0){
			return null;
		}
		return centerSmsVerificationList .get(0);
	}
	
	public List<CenterSmsVerification> selectAllCenterSmsVerification(){
		return centerSmsVerificationMapper .selectAllCenterSmsVerification();
	}
	
	
	public boolean selectCenterSmsVerificationByConditon(Map<String,Object> condition){
		if(0== centerSmsVerificationMapper.selectCenterSmsVerificationByConditon(condition)){
			return false;
		}else{
			return true;
		}
	}
	
	
}