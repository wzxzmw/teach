package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterSmsVerification;
import java.util.List;
import java.util.Map;

public interface CenterSmsVerificationMapper {

	public abstract Integer insertCenterSmsVerification(CenterSmsVerification centerSmsVerification);
	
	public abstract void deleteCenterSmsVerification(CenterSmsVerification centerSmsVerification);
	
	public abstract void updateCenterSmsVerificationByKey(CenterSmsVerification centerSmsVerification);
	
	public abstract List<CenterSmsVerification> selectSingleCenterSmsVerification(CenterSmsVerification centerSmsVerification);
	
	public abstract List<CenterSmsVerification> selectAllCenterSmsVerification();
	
	public abstract Integer selectCenterSmsVerificationByConditon(Map<String,Object> condition);
	
}