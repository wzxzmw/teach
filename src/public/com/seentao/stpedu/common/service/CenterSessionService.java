package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.dao.CenterSessionDao;
import com.seentao.stpedu.common.entity.CenterSession;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class CenterSessionService{
	
	@Autowired
	private CenterSessionDao centerSessionDao;
	
	public List<CenterSession> getCenterSession(CenterSession centerSession) {
		List<CenterSession> centerSessionList = centerSessionDao .selectSingleCenterSession(centerSession);
		if(centerSessionList == null || centerSessionList .size() <= 0){
			return null;
		}
		return centerSessionList;
	}

	public void insertCenterSession(CenterSession centerSession)throws InsertObjectException {
		try {
			centerSessionDao.insertCenterSession(centerSession);
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "insert obj", "insert obj is error"+e);
			throw new InsertObjectException("insert obj is error",e);
		}
	}

	public void deleteCenterSessionByUserId(Integer userId,Integer clientType) {
		CenterSession c = new CenterSession();
		c.setUserId(userId);
		c.setClientType(clientType);
		centerSessionDao.deleteCenterSession(c);
	}
	
	/**
	 * 根据用户ID和用户名牌查询
	 * @param userId 用户ID
	 * @param userToken 用户TOken
	 * @return
	 * @author chengshx
	 */
	public CenterSession getCenterSession(Integer userId, String userToken){
		CenterSession centerSession = new CenterSession();
		centerSession.setUserId(userId);
		centerSession.setUserToken(userToken);
		return centerSessionDao.selectCenterSession(centerSession);
	}
	
	public CenterSession getCenterSessionOne(CenterSession centerSession) {
		List<CenterSession> centerSessionList = centerSessionDao .selectSingleCenterSession(centerSession);
		if(centerSessionList == null || centerSessionList .size() <= 0){
			return null;
		}
		return centerSessionList.get(0);
	}

	public void updateCenterSession(CenterSession centerSession) {
		centerSessionDao.updateCenterSessionByKey(centerSession);
	}
	
}