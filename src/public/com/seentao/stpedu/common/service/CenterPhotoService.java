package com.seentao.stpedu.common.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.CenterPhotoDao;
import com.seentao.stpedu.common.entity.CenterPhoto;

@Service
public class CenterPhotoService{
	
	@Autowired
	private CenterPhotoDao centerPhotoDao;
	
	public CenterPhoto getCenterPhoto(CenterPhoto centerPhoto) {
		List<CenterPhoto> centerPhotoList = centerPhotoDao .selectSingleCenterPhoto(centerPhoto);
		if(centerPhotoList == null || centerPhotoList .size() <= 0){
			return null;
		}
		
		return centerPhotoList.get(0);
	}
	
	public void updateCenterPhotoByKey(CenterPhoto centerPhoto){
		centerPhotoDao.updateCenterPhotoByKey(centerPhoto);
	}

	public void delCenterPhotoAll(List<CenterPhoto> delCenterPhoto) {
		centerPhotoDao.delCenterPhotoAll(delCenterPhoto);
	}
}