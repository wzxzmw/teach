package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.common.sqlmap.CenterAlbumMapper;
import com.seentao.stpedu.utils.TimeUtil;


@Repository
public class CenterAlbumDao {

	@Autowired
	private CenterAlbumMapper centerAlbumMapper;


	public void insertCenterAlbum(CenterAlbum centerAlbum){
		centerAlbumMapper .insertCenterAlbum(centerAlbum);
	}

	public void deleteCenterAlbum(CenterAlbum centerAlbum){
		centerAlbumMapper .deleteCenterAlbum(centerAlbum);
	}

	public void updateCenterAlbumByKey(CenterAlbum centerAlbum){
		centerAlbumMapper .updateCenterAlbumByKey(centerAlbum);
	}

	public List<CenterAlbum> selectSingleCenterAlbum(CenterAlbum centerAlbum){
		return centerAlbumMapper .selectSingleCenterAlbum(centerAlbum);
	}

	public CenterAlbum selectCenterAlbum(CenterAlbum centerAlbum){
		List<CenterAlbum> centerAlbumList = centerAlbumMapper .selectSingleCenterAlbum(centerAlbum);
		if(centerAlbumList == null || centerAlbumList .size() == 0){
			return null;
		}
		return centerAlbumList .get(0);
	}

	public List<CenterAlbum> selectAllCenterAlbum(){
		return centerAlbumMapper .selectAllCenterAlbum();
	}
	/**
	 * 通过相册id获取该相册下的图片信息
	 * @param paramMap
	 * @author cxw
	 */
	public Integer queryCountalbumInfo(Map<String, Object> paramMap) {
		return centerAlbumMapper.queryCountalbumInfo(paramMap);
	}

	public List<CenterAlbum> queryByCenteralbumInfo(Map<String, Object> paramMap) {
		return centerAlbumMapper.queryByCenteralbumInfo(paramMap);
	}

	/**
	 * 获取相册
	 * @param paramMap
	 * @author cxw
	 */
	public Integer queryCountalbum(Map<String, Object> paramMap) {
		return centerAlbumMapper.queryCountalbum(paramMap);
	}

	public List<CenterAlbum> queryByCenteralbum(Map<String, Object> paramMap) {
		return centerAlbumMapper.queryByCenteralbum(paramMap);
	}

	public void updateCenterAlbumByKeyAll(CenterAlbum delcenterAlbum) {
		centerAlbumMapper.updateCenterAlbumByKeyAll(delcenterAlbum);
	}

	public void updatePhotoNum(CenterAlbum centerAlbum) {
		centerAlbumMapper.updatePhotoNum(centerAlbum);
	}

	public void updateCoverPhotoId(CenterAlbum centerAlbums) {
		centerAlbumMapper.updateCoverPhotoId(centerAlbums);

	}

	public void updateAlbumaDate(String albumId){
		try {
			CenterAlbum album2 = new CenterAlbum();
			album2.setAlbumId(Integer.valueOf(albumId));
			album2.setLastUpdateTime(TimeUtil.getCurrentTimestamp());
			centerAlbumMapper.updateCenterAlbumByKey(album2);
		} catch (Exception e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param createUserId 用户Id
	 * @param albumTitle 相册文件名
	 * @return
	 */
	public CenterAlbum  queryCenterAlbumByUserIdSome(Integer createUserId,String albumTitle ){
		return centerAlbumMapper.queryCenterAlbumByUserIdSome(createUserId, albumTitle);
	}
	/**
	 * @param createUserId 根据用户id查询用户下的所有相册
	 * @return
	 */
	public List<CenterAlbum> queryCenterAlbumByUserId(Integer createUserId){
		return centerAlbumMapper.queryCenterAlbumByUserId(createUserId);
	}
}