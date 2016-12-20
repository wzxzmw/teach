package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterCompany;
import java.util.List;

public interface CenterCompanyMapper {

	public abstract Integer insertCenterCompany(CenterCompany centerCompany);
	
	public abstract void deleteCenterCompany(CenterCompany centerCompany);
	
	public abstract void updateCenterCompanyByKey(CenterCompany centerCompany);
	
	public abstract List<CenterCompany> selectSingleCenterCompany(CenterCompany centerCompany);
	
	public abstract List<CenterCompany> selectAllCenterCompany();
	
}