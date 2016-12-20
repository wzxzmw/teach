package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.CenterSession;
import com.seentao.stpedu.common.sqlmap.CenterSessionMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class CenterSessionDao {

	@Autowired
	private CenterSessionMapper centerSessionMapper;
	
	
	public void insertCenterSession(CenterSession centerSession)throws InsertObjectException{
		try {
			centerSessionMapper.insertCenterSession(centerSession);
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "insertCenterSession", "insertCenterSession is error"+e);
			throw new InsertObjectException("insert obj is error",e);
		}
	}
	
	public void deleteCenterSession(CenterSession centerSession){
		centerSessionMapper .deleteCenterSession(centerSession);
	}
	
	public void updateCenterSessionByKey(CenterSession centerSession){
		centerSessionMapper .updateCenterSessionByKey(centerSession);
	}
	
	public List<CenterSession> selectSingleCenterSession(CenterSession centerSession){
		return centerSessionMapper .selectSingleCenterSession(centerSession);
	}
	
	public CenterSession selectCenterSession(CenterSession centerSession){
		List<CenterSession> centerSessionList = centerSessionMapper .selectSingleCenterSession(centerSession);
		if(centerSessionList == null || centerSessionList .size() == 0){
			return null;
		}
		return centerSessionList .get(0);
	}
	
	public List<CenterSession> selectAllCenterSession(){
		return centerSessionMapper .selectAllCenterSession();
	}
}