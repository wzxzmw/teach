package com.seentao.stpedu.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.CenterCompanyDao;
import com.seentao.stpedu.common.entity.CenterCompany;

@Service
public class CenterCompanyService{
	
	@Autowired
	private CenterCompanyDao centerCompanyDao;
	
	public CenterCompany getCenterCompany(CenterCompany centerCompany) {
		return centerCompanyDao.selectSingleCenterCompany(centerCompany);
	} 
	
	public void updateCenterCompanyByKey(CenterCompany centerCompany){
		centerCompanyDao.updateCenterCompanyByKey(centerCompany);
	}
	
	
	public Integer insertCenterCompany(CenterCompany centerCompany){
		return centerCompanyDao.insertCenterCompany(centerCompany);
	}
}