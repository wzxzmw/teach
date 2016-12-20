package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterConvert;
import java.util.List;
import java.util.Map;

public interface CenterConvertMapper {

	public abstract void insertCenterConvert(CenterConvert centerConvert);
	
	public abstract void deleteCenterConvert(CenterConvert centerConvert);
	
	public abstract void updateCenterConvertByKey(CenterConvert centerConvert);
	
	public abstract List<CenterConvert> selectSingleCenterConvert(CenterConvert centerConvert);
	
	public abstract List<CenterConvert> selectAllCenterConvert();
	
	public abstract Integer countCenterConvertListByCondition(Map<String, Object> paramMap);
	
	public abstract  List<CenterConvert> selectCenterConvertListByCondition(Map<String, Object> paramMap);
	
}