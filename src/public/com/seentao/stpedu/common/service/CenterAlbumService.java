package com.seentao.stpedu.common.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.entity.CenterAlbum;

@Service
public class CenterAlbumService{

	@Autowired
	private CenterAlbumDao centerAlbumDao;

	public CenterAlbum getCenterAlbum(CenterAlbum centerAlbum) {
		List<CenterAlbum> centerAlbumList = centerAlbumDao .selectSingleCenterAlbum(centerAlbum);
		if(centerAlbumList == null || centerAlbumList .size() <= 0){
			return null;
		}

		return centerAlbumList.get(0);
	}


	/**
	 * updateCenterAlbumByKey(逻辑删除相册)   
	 * @author ligs
	 * @date 2016年7月4日 下午8:03:49
	 * @return
	 */
	public void updateCenterAlbumByKey(CenterAlbum centerAlbum){
		centerAlbumDao.selectSingleCenterAlbum(centerAlbum);
	}

	/**
	 * addAlbumPhontNum(提交相册信息增加相册照片数量)
	 * @param albumId
	 * @author cxw
	 */
	public void addAlbumPhontNum(String albumId){
		try {
			CenterAlbum album = null;
			album = new CenterAlbum();
			album.setAlbumId(Integer.valueOf(albumId));
			CenterAlbum album2 = centerAlbumDao.selectCenterAlbum(album);
			Integer photoNum = album2.getPhotoNum();
			album = new CenterAlbum();
			album.setPhotoNum(photoNum+1);
			album.setAlbumId(Integer.valueOf(albumId));
			centerAlbumDao.updateCenterAlbumByKey(album);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateCenterAlbumByKeyAll(CenterAlbum delcenterAlbum) {
		centerAlbumDao.updateCenterAlbumByKeyAll(delcenterAlbum);
	}


	public void updatePhotoNum(CenterAlbum centerAlbum) {
		centerAlbumDao.updatePhotoNum(centerAlbum);
	}


	public void updateCoverPhotoId(CenterAlbum centerAlbums) {
		centerAlbumDao.updateCoverPhotoId(centerAlbums);
	}
}