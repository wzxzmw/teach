package com.seentao.stpedu.common.sqlmap;


import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterCashOut;

public interface CenterCashOutMapper {

	public abstract void insertCenterCashOut(CenterCashOut centerCashOut);
	
	public abstract void deleteCenterCashOut(CenterCashOut centerCashOut);
	
	public abstract void updateCenterCashOutByKey(CenterCashOut centerCashOut);
	
	public abstract List<CenterCashOut> selectSingleCenterCashOut(CenterCashOut centerCashOut);
	
	public abstract List<CenterCashOut> selectAllCenterCashOut();
	
	public abstract int countCenterCashOutListByCondition(Map<String, Object> paramMap);
	
	public abstract List<CenterCashOut> selectCenterCashOutListByCondition(Map<String, Object> paramMap);
}