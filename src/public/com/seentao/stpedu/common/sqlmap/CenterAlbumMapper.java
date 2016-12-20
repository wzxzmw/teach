package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterAlbum;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CenterAlbumMapper {

	public abstract void insertCenterAlbum(CenterAlbum centerAlbum);
	
	public abstract void deleteCenterAlbum(CenterAlbum centerAlbum);
	
	public abstract void updateCenterAlbumByKey(CenterAlbum centerAlbum);
	
	public abstract List<CenterAlbum> selectSingleCenterAlbum(CenterAlbum centerAlbum);
	
	public abstract List<CenterAlbum> selectAllCenterAlbum();

	public abstract Integer queryCountalbum(Map<String, Object> paramMap);

	public abstract List<CenterAlbum> queryByCenteralbum(Map<String, Object> paramMap);

	public abstract Integer queryCountalNewbum(Map<String, Object> paramMap);

	public abstract List<CenterAlbum> queryByNewCenteralbum(Map<String, Object> paramMap);

	public abstract Integer queryCountalbumInfo(Map<String, Object> paramMap);

	public abstract List<CenterAlbum> queryByCenteralbumInfo(Map<String, Object> paramMap);

	public abstract void updateCenterAlbumByKeyAll(CenterAlbum delcenterAlbum);

	public abstract void updatePhotoNum(CenterAlbum centerAlbum);

	public abstract void updateCoverPhotoId(CenterAlbum centerAlbums);
	
	public abstract CenterAlbum  queryCenterAlbumByUserIdSome(@Param("createUserId")Integer createUserId,@Param("albumTitle")String albumTitle);
	
	public abstract List<CenterAlbum> queryCenterAlbumByUserId(@Param("createUserId")Integer createUserId);
}