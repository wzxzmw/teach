package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.CenterSmsVerificationDao;
import com.seentao.stpedu.common.entity.CenterSmsVerification;

@Service
public class CenterSmsVerificationService{
	
	@Autowired
	private CenterSmsVerificationDao centerSmsVerificationDao;
	
	public String getCenterSmsVerification(CenterSmsVerification centerSmsVerification) {
		List<CenterSmsVerification> centerSmsVerificationList = centerSmsVerificationDao .selectSingleCenterSmsVerification(centerSmsVerification);
		if(centerSmsVerificationList == null || centerSmsVerificationList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(centerSmsVerificationList);
	}
	
	public CenterSmsVerification getCenterSmsVerificationOne(CenterSmsVerification centerSmsVerification) {
		List<CenterSmsVerification> centerSmsVerificationList = centerSmsVerificationDao .selectSingleCenterSmsVerification(centerSmsVerification);
		if(centerSmsVerificationList == null || centerSmsVerificationList .size() <= 0){
			return null;
		}
		return centerSmsVerificationList.get(0);
	}

	public void insertCenterSmsVerification(CenterSmsVerification centerSmsVerification) {
		centerSmsVerificationDao.insertCenterSmsVerification(centerSmsVerification);
	}

	/** 
	* @Title: compareNumber 
	* @Description: 比较验证码
	* @param newBindingNumber
	* @param verifyNumber
	* @return boolean
	* @author liulin
	* @date 2016年6月30日 下午4:14:15
	*/
	public boolean compareNumber(String newBindingNumber, String verifyNumber) {
		CenterSmsVerification smsVerification = new CenterSmsVerification();
		smsVerification.setPhone(newBindingNumber);
		smsVerification.setVerificationCode(verifyNumber);
		smsVerification = centerSmsVerificationDao.selectCenterSmsVerification(smsVerification);
		if(null!=smsVerification && verifyNumber.equalsIgnoreCase(smsVerification.getVerificationCode())){
			return true;
		}else{
			return false;
		}
	}

	public void updateCenterSmsVerification(CenterSmsVerification centerSmsVerification) {
		centerSmsVerificationDao.updateCenterSmsVerificationByKey(centerSmsVerification);
	}
}