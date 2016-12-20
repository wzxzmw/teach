package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.CenterAttitudeDao;
import com.seentao.stpedu.common.entity.CenterAttitude;

@Service
public class CenterAttitudeService{
	
	@Autowired
	private CenterAttitudeDao centerAttitudeDao;
	
	public CenterAttitude getCenterAttitude(CenterAttitude centerAttitude) {
		List<CenterAttitude> centerAttitudeList = centerAttitudeDao .selectSingleCenterAttitude(centerAttitude);
		if(centerAttitudeList == null || centerAttitudeList .size() <= 0){
			return null;
		}
		
		return centerAttitudeList.get(0);
	}
	
	/**
	 * 查询点赞的数量
	 * @param centerAttitude
	 * @return
	 */
	public Integer getCenterAttitudeCount(CenterAttitude centerAttitude) {
		return centerAttitudeDao .selectCenterAttitudeCount(centerAttitude);
	}
	/**
	 * insertCenterAttitude(添加态度表数据)   
	 * @author ligs
	 * @date 2016年6月27日 下午3:19:27
	 * @return
	 */
	public void insertCenterAttitude(CenterAttitude centerAttitude){
		centerAttitudeDao.insertCenterAttitude(centerAttitude);
	}
}