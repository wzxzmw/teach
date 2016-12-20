package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterPhoto;
import java.util.List;
import java.util.Map;

public interface CenterPhotoMapper {

	public abstract void insertCenterPhoto(CenterPhoto centerPhoto);
	
	public abstract void deleteCenterPhoto(CenterPhoto centerPhoto);
	
	public abstract void updateCenterPhotoByKey(CenterPhoto centerPhoto);
	
	public abstract List<CenterPhoto> selectSingleCenterPhoto(CenterPhoto centerPhoto);
	
	public abstract List<CenterPhoto> selectAllCenterPhoto();

	public abstract List<CenterPhoto> selectCenterPhotoId(CenterPhoto centerPhoto);

	public abstract void delCenterPhotoAll(List<CenterPhoto> delCenterPhoto);

	public abstract Integer queryCountalbumInfo(Map<String, Object> paramMap);

	public abstract List<CenterPhoto> queryByCenteralbumInfo(Map<String, Object> paramMap);

	public abstract CenterPhoto selectCenterPhotoInfo(CenterPhoto centerPhoto);

	public abstract Integer queryCountalNewbum(Map<String, Object> paramMap);

	public abstract List<CenterPhoto> queryByNewCenteralbum(Map<String, Object> paramMap);

	public abstract List<CenterPhoto> selectCenterPDesc(CenterPhoto centerPhoto);
	
}