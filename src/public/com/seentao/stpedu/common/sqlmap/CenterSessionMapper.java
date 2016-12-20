package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.CenterSession;
import java.util.List;

public interface CenterSessionMapper {

	public abstract void insertCenterSession(CenterSession centerSession)throws InsertObjectException;
	
	public abstract void deleteCenterSession(CenterSession centerSession);
	
	public abstract void updateCenterSessionByKey(CenterSession centerSession);
	
	public abstract List<CenterSession> selectSingleCenterSession(CenterSession centerSession);
	
	public abstract List<CenterSession> selectAllCenterSession();
	
}