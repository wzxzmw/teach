package com.seentao.stpedu.photo.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;
/**
 * @author cxw
 */
@Service
public class SubmitCenterAlbumService {

	@Autowired
	private CenterAlbumDao albumDao;
	/***
	 * 提交相册信息
	 * @param userId   
	 * @param albumTitle   相册名称
	 * @param albumDesc 相册描述
	 * @param albumId 
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public String submitAlbum(String userId, String albumTitle, String albumDesc, String albumId){

		try {
			if("0".equals(albumId)){

				CenterAlbum album = new CenterAlbum();
				album.setCreateUserId(Integer.valueOf(userId));
				album.setCreateTime(TimeUtil.getCurrentTimestamp());
				album.setAlbumName(albumTitle);
				album.setAlbumExplain(albumDesc);
				album.setPhotoNum(0);
				album.setLastUpdateTime(TimeUtil.getCurrentTimestamp());
				album.setIsDelete(0);
				album.setCoverPhotoId(0);
				albumDao.insertCenterAlbum(album);
				LogUtil.info(this.getClass(), "submitcenterAlbum", "创建相册成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}else{
				CenterAlbum album = null;
				album = new CenterAlbum();
				album.setAlbumId(Integer.valueOf(albumId));
				album.setCreateUserId(Integer.valueOf(userId));
				List<CenterAlbum> list = albumDao.selectSingleCenterAlbum(album);
				if(list == null || list.size()<0){
					LogUtil.error(this.getClass(), "submitAlbum", "没有修改的相册信息");
					return Common.getReturn(AppErrorCode.SUCCESS, "" ).toJSONString();
				}

				album = new CenterAlbum();
				album.setAlbumId(Integer.valueOf(albumId));
				album.setCreateUserId(Integer.valueOf(userId));
				album.setAlbumName(albumTitle);
				album.setAlbumExplain(albumDesc);
				album.setPhotoNum(0);
				album.setLastUpdateTime(TimeUtil.getCurrentTimestamp());
				album.setIsDelete(0);
				album.setCoverPhotoId(0);
				albumDao.updateCenterAlbumByKey(album);
				LogUtil.info(this.getClass(), "submitcenterAlbum", "修改相册成功");
				
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "submitcenterAlbum", "操作相册失败");
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.FAILED_TO_OPERATE_ALBUM).toJSONString();
		}
	}
	public boolean userIsExist(String albumTitle, String userId) {
		LogUtil.info(this.getClass(), "userIsExist", "昵称占用判断【nickName："+albumTitle+"】");
		CenterAlbum album = new CenterAlbum(null, Integer.parseInt(userId), null, albumTitle, null, null, null, null, null, null, null, null, null);
		List<CenterAlbum> list = albumDao.selectSingleCenterAlbum(album);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
