package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterMoneyLock;
import com.seentao.stpedu.common.sqlmap.CenterMoneyLockMapper;


@Repository
public class CenterMoneyLockDao {

	@Autowired
	private CenterMoneyLockMapper centerMoneyLockMapper;
	
	
	public int insertCenterMoneyLock(CenterMoneyLock centerMoneyLock){
		centerMoneyLockMapper .insertCenterMoneyLock(centerMoneyLock);
		return centerMoneyLock.getLockId();
	}
	
	public void deleteCenterMoneyLock(CenterMoneyLock centerMoneyLock){
		centerMoneyLockMapper .deleteCenterMoneyLock(centerMoneyLock);
	}
	
	public void updateCenterMoneyLockByKey(CenterMoneyLock centerMoneyLock){
		centerMoneyLockMapper .updateCenterMoneyLockByKey(centerMoneyLock);
	}
	
	public List<CenterMoneyLock> selectSingleCenterMoneyLock(CenterMoneyLock centerMoneyLock){
		return centerMoneyLockMapper .selectSingleCenterMoneyLock(centerMoneyLock);
	}
	
	public CenterMoneyLock selectCenterMoneyLock(CenterMoneyLock centerMoneyLock){
		List<CenterMoneyLock> centerMoneyLockList = centerMoneyLockMapper .selectSingleCenterMoneyLock(centerMoneyLock);
		if(centerMoneyLockList == null || centerMoneyLockList .size() == 0){
			return null;
		}
		return centerMoneyLockList .get(0);
	}
	
	public List<CenterMoneyLock> selectAllCenterMoneyLock(){
		return centerMoneyLockMapper .selectAllCenterMoneyLock();
	}

	/**
	 * 批量查询现金锁定表
	 * @param lockIds	现金锁定表ids
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午9:22:35
	 */
	public List<CenterMoneyLock> findGuessLock(String lockIds) {
		return centerMoneyLockMapper.findGuessLock(lockIds);
	}

	
	/**
	 * 批量修改现金锁定表
	 * @param lock
	 * @author 			lw
	 * @date			2016年7月3日  下午3:03:34
	 */
	public void executeGuessResult(CenterMoneyLock lock) {
		centerMoneyLockMapper.executeGuessResult(lock);
	}

	

	
}