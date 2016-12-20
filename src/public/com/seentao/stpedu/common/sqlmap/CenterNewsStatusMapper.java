package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterNewsStatus;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CenterNewsStatusMapper {
	/**
	 * 添加消息状态
	 * @param centerNewsStatus
	 * @throws RuntimeException
	 */
	public abstract void insertCenterNewsStatus(CenterNewsStatus centerNewsStatus)throws RuntimeException;
	/**
	 * 删除消息状态
	 * @param centerNewsStatus
	 */
	public abstract void deleteCenterNewsStatus(CenterNewsStatus centerNewsStatus)throws RuntimeException;
	/**
	 * 根据statusId更新状态信息
	 * @param centerNewsStatus
	 * @throws RuntimeException
	 */
	public abstract void updateCenterNewsStatusByKey(CenterNewsStatus centerNewsStatus)throws RuntimeException;
	
	public abstract List<CenterNewsStatus> selectSingleCenterNewsStatus(CenterNewsStatus centerNewsStatus);
	
	public abstract List<CenterNewsStatus> selectAllCenterNewsStatus();
	
	public abstract List<CenterNewsStatus> getCenterNewsStatusRedDotByUserId(int userId);

	public abstract void updateCenterNewsStatusByKeyAll(CenterNewsStatus centerStatus)throws RuntimeException;

	public abstract void updateCenterNewsStatusByKeyAttention(CenterNewsStatus centerNewsStatus) throws RuntimeException;
	/**
	 * 根据用户id查询红点
	 */
	public abstract CenterNewsStatus queryCenterNewsStatusByUserId(@Param("userId") Integer userId);
	
}