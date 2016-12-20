package com.seentao.stpedu.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.sqlmap.CenterNewsStatusMapper;


@Repository
public class CenterNewsStatusDao {

	@Autowired
	private CenterNewsStatusMapper centerNewsStatusMapper;
	
	/**
	 * 添加消息状态
	 * @param centerNewsStatus
	 * @throws RuntimeException
	 */
	public void insertCenterNewsStatus(CenterNewsStatus centerNewsStatus)throws RuntimeException{
		try {
			centerNewsStatusMapper.insertCenterNewsStatus(centerNewsStatus);
			/**
			 * 抛该异常为控制事务回滚
			 */
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 根据statuId删除状态消息
	 * @param centerNewsStatus
	 */
	public void deleteCenterNewsStatus(CenterNewsStatus centerNewsStatus)throws RuntimeException{
		try {
			centerNewsStatusMapper.deleteCenterNewsStatus(centerNewsStatus);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 根据状态statusId更新状态
	 * @param centerNewsStatus
	 * @throws RuntimeException
	 */
	public void updateCenterNewsStatusByKey(CenterNewsStatus centerNewsStatus)throws RuntimeException{
		try {
			
			centerNewsStatusMapper.updateCenterNewsStatusByKey(centerNewsStatus);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 根据用户id查询
	 * @return
	 */
	public CenterNewsStatus queryCenterNewsStatusByUserId(Integer userId){
		
		return centerNewsStatusMapper.queryCenterNewsStatusByUserId(userId);
	}

}