package com.seentao.stpedu.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.dao.CenterPhotoDao;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.common.entity.CenterPhoto;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Service
public class SubmitAlbumCoverService {

	@Autowired
	private CenterAlbumDao albumDao;
	/**
	 * 照片表
	 */
	@Autowired
	private CenterPhotoDao centerPhotoDao;
	/***
	 * 设置相册图片的封面
	 * @param userId
	 * @param albumId 相册id
	 * @param albumPhotoId 	相册图片的id
	 * @return
	 * @author cxw
	 * @return 
	 */
	public String submitPhotoAlbumCover(String userId, String albumId, String albumPhotoId) {

		try {
			//判断图片是否是本相册：根据 相册图片的id 去照片表查询相册id是否是本相册
			CenterPhoto centerPhoto = new CenterPhoto();
			centerPhoto.setPhotoId(Integer.valueOf(albumPhotoId));
			centerPhoto.setCreateUserId(Integer.valueOf(userId));
			CenterPhoto photo = centerPhotoDao.selectCenterPhotoInfo(centerPhoto);
				if(photo.getAlbumId()==Integer.valueOf(albumId)){
					//以 userId 相册id 取修改相册封面
					CenterAlbum album = new CenterAlbum();
					album.setCreateUserId(Integer.valueOf(userId));
					album.setCoverPhotoId(photo.getImageId());
					album.setAlbumId(Integer.valueOf(albumId));
					albumDao.updateCenterAlbumByKey(album);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}
			LogUtil.error(this.getClass(), "submitPhotoAlbumCover", "图片不属于本相册");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.PICTURES_DONOT_BELONG_TO_THIS_ALBUM).toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PICTURES_DONOT_BELONG_TO_THIS_ALBUM).toJSONString();
		}

	}
}
