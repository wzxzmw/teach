package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterSerialMax;
import java.util.List;

public interface CenterSerialMaxMapper {

	public abstract void insertCenterSerialMax(CenterSerialMax centerSerialMax);
	
	public abstract void deleteCenterSerialMax(CenterSerialMax centerSerialMax);
	
	public abstract void updateCenterSerialMaxByKey(CenterSerialMax centerSerialMax);
	
	public abstract List<CenterSerialMax> selectSingleCenterSerialMax(CenterSerialMax centerSerialMax);
	
	public abstract List<CenterSerialMax> selectAllCenterSerialMax();
	
}