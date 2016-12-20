package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterPhoto;
import com.seentao.stpedu.common.sqlmap.CenterPhotoMapper;


@Repository
public class CenterPhotoDao {

	@Autowired
	private CenterPhotoMapper centerPhotoMapper;
	
	
	public void insertCenterPhoto(CenterPhoto centerPhoto){
		centerPhotoMapper .insertCenterPhoto(centerPhoto);
	}
	
	public void deleteCenterPhoto(CenterPhoto centerPhoto){
		centerPhotoMapper .deleteCenterPhoto(centerPhoto);
	}
	
	public void updateCenterPhotoByKey(CenterPhoto centerPhoto){
		centerPhotoMapper .updateCenterPhotoByKey(centerPhoto);
	}
	
	public List<CenterPhoto> selectSingleCenterPhoto(CenterPhoto centerPhoto){
		return centerPhotoMapper .selectSingleCenterPhoto(centerPhoto);
	}
	
	public CenterPhoto selectCenterPhoto(CenterPhoto centerPhoto){
		List<CenterPhoto> centerPhotoList = centerPhotoMapper .selectSingleCenterPhoto(centerPhoto);
		if(centerPhotoList == null || centerPhotoList .size() == 0){
			return null;
		}
		return centerPhotoList .get(0);
	}
	
	public List<CenterPhoto> selectAllCenterPhoto(){
		return centerPhotoMapper .selectAllCenterPhoto();
	}

	public CenterPhoto selectCenterPhotoId(CenterPhoto centerPhoto){
		List<CenterPhoto> centerPhotoList = centerPhotoMapper .selectCenterPhotoId(centerPhoto);
		if(centerPhotoList == null || centerPhotoList .size() == 0){
			return null;
		}
		return centerPhotoList .get(0);
	}

	public void delCenterPhotoAll(List<CenterPhoto> delCenterPhoto) {
		centerPhotoMapper.delCenterPhotoAll(delCenterPhoto);
	}
	
	/**
	 * 通过相册id获取该相册下的图片信息
	 * @param paramMap
	 * @author cxw
	 */
	public Integer queryCountalbumInfo(Map<String, Object> paramMap) {
		return centerPhotoMapper.queryCountalbumInfo(paramMap);
	}

	public List<CenterPhoto> queryByCenteralbumInfo(Map<String, Object> paramMap) {
		return centerPhotoMapper.queryByCenteralbumInfo(paramMap);
	}

	public CenterPhoto selectCenterPhotoInfo(CenterPhoto centerPhoto) {
		return centerPhotoMapper.selectCenterPhotoInfo(centerPhoto);
	}

	/**
	 * 获取相册下的最新图片信息
	 * @param paramMap
	 * @author cxw
	 */
	public Integer queryCountalNewbum(Map<String, Object> paramMap) {
		return centerPhotoMapper.queryCountalNewbum(paramMap);
	}

	public List<CenterPhoto> queryByNewCenteralbum(Map<String, Object> paramMap) {
		return centerPhotoMapper.queryByNewCenteralbum(paramMap);
	}

	public CenterPhoto selectCenterPDesc(CenterPhoto centerPhoto){
		List<CenterPhoto> centerPhotoList = centerPhotoMapper .selectCenterPDesc(centerPhoto);
		if(centerPhotoList == null || centerPhotoList .size() == 0){
			return null;
		}
		return centerPhotoList .get(0);
	}
}