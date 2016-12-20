package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterAttitude;
import java.util.List;

public interface CenterAttitudeMapper {

	public abstract void insertCenterAttitude(CenterAttitude centerAttitude);
	
	public abstract void deleteCenterAttitude(CenterAttitude centerAttitude);
	
	public abstract void updateCenterAttitudeByKey(CenterAttitude centerAttitude);
	
	public abstract List<CenterAttitude> selectSingleCenterAttitude(CenterAttitude centerAttitude);
	
	public abstract List<CenterAttitude> selectAllCenterAttitude();

	public abstract Integer selectCenterAttitudeCount(CenterAttitude centerAttitude);
	
}