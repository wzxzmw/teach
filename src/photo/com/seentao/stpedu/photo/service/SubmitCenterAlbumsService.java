
package com.seentao.stpedu.photo.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class SubmitCenterAlbumsService {
	@Autowired
	private CenterAlbumDao albumDao;
	/**
	 * @param userId 用户userID
	 * @param albumTitle 相册主题
	 * @param albumDesc 相册描述
	 * @param albumId 相册id
	 * @return
	 */
	public String submitAlbum(String userId, String albumTitle, String albumDesc, String albumId){
		try {
			CenterAlbum album = new CenterAlbum();
			album.setCreateUserId(Integer.valueOf(userId));
			album.setCreateTime(TimeUtil.getCurrentTimestamp());
			album.setAlbumName(albumTitle.trim());
			album.setAlbumExplain(albumDesc);
			album.setPhotoNum(0);
			album.setLastUpdateTime(TimeUtil.getCurrentTimestamp());
			album.setIsDelete(0);
			album.setCoverPhotoId(0);
			if("0".equals(albumId) || StringUtils.isEmpty(albumId)){
				//实现添加操作，校验相册名称 1、userId，2、albumTitle
				CenterAlbum al = albumDao.queryCenterAlbumByUserIdSome(Integer.valueOf(userId),albumTitle);
				if(al !=null){
					
					//相册名称占用
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ALBUM_NAME_OCCUPANCY).toJSONString();
				}
				else{
					albumDao.insertCenterAlbum(album);
					LogUtil.info(this.getClass(), "submitcenterAlbum", "创建相册成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}
			}else{
				List<CenterAlbum> ls =albumDao.queryCenterAlbumByUserId(Integer.valueOf(userId));
				for(CenterAlbum centerAlbum :ls){
					if(Integer.valueOf(albumId).intValue() != centerAlbum.getAlbumId().intValue()){
						if(albumTitle.trim().equals(centerAlbum.getAlbumName())){
							return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ALBUM_NAME_OCCUPANCY).toJSONString();
						}
					}
				}
				CenterAlbum albums = new CenterAlbum();
				albums.setCreateUserId(Integer.valueOf(userId));
				albums.setCreateTime(TimeUtil.getCurrentTimestamp());
				albums.setAlbumName(albumTitle.trim());
				albums.setAlbumExplain(albumDesc);
				albums.setLastUpdateTime(TimeUtil.getCurrentTimestamp());
				albums.setAlbumId(Integer.valueOf(albumId));
				albums.setCreateUserId(Integer.valueOf(userId));
				albums.setCoverPhotoId(0);
				albumDao.updateCenterAlbumByKey(albums);
				LogUtil.info(this.getClass(), "submitcenterAlbum", "修改相册成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "submitcenterAlbum", "操作相册失败");
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.FAILED_TO_OPERATE_ALBUM).toJSONString();
		}
	}
}
