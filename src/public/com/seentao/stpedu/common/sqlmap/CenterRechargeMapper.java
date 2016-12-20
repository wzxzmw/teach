package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterRecharge;
import java.util.List;
import java.util.Map;

public interface CenterRechargeMapper {

	public abstract void insertCenterRecharge(CenterRecharge centerRecharge);
	
	public abstract void deleteCenterRecharge(CenterRecharge centerRecharge);
	
	public abstract void updateCenterRechargeByKey(CenterRecharge centerRecharge);
	
	public abstract List<CenterRecharge> selectSingleCenterRecharge(CenterRecharge centerRecharge);
	
	public abstract List<CenterRecharge> selectAllCenterRecharge();
	
	public abstract Integer countCenterRechargeByCondition(Map<String, Object> paramMap);
	
	public abstract List<CenterRecharge>  selectCenterRechargeByCondition(Map<String, Object> paramMap);
	
}