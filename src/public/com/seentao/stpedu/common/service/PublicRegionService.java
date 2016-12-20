package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.PublicRegionDao;
import com.seentao.stpedu.common.entity.PublicRegion;

@Service
public class PublicRegionService{
	
	@Autowired
	private PublicRegionDao publicRegionDao;
	
	public PublicRegion getPublicRegion(PublicRegion publicRegion) {
		List<PublicRegion> publicRegionList = publicRegionDao .selectSinglePublicRegion(publicRegion);
		if(publicRegionList == null || publicRegionList .size() <= 0){
			return null;
		}
		
		return publicRegionList.get(0);
	}
	
	public List<PublicRegion> getPublicRegionList(PublicRegion publicRegion) {
		List<PublicRegion> publicRegionList = publicRegionDao .selectSinglePublicRegion(publicRegion);
		if(publicRegionList == null || publicRegionList .size() <= 0){
			return null;
		}
		
		return publicRegionList;
	}
	
	/**
	 * 根据市id 查询出市名称以及上一级名称
	 * @param id
	 * @return 
	 */
	public List<PublicRegion> getPublicRegionByCityId(Integer cityId) {
		PublicRegion publicRegion = new PublicRegion();
		publicRegion.setRegionId(cityId);
		List<PublicRegion> publicRegionList = publicRegionDao.selectPublicRegionByCityId(publicRegion);
		if(publicRegionList == null || publicRegionList .size() <= 0){
			return null;
		}
		return publicRegionList;
	}
}