package com.seentao.stpedu.photo.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.dao.CenterPhotoDao;
import com.seentao.stpedu.common.dao.PublicPictureDao;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.common.entity.CenterPhoto;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.service.CenterAlbumService;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class SubmitAlbumPhotoService {

	@Autowired
	private CenterPhotoDao centerPhotoDao;

	@Autowired
	private CenterAlbumService centerAlbumService;

	@Autowired
	private PublicPictureDao publicdao;

	@Autowired
	private CenterAlbumDao centerAlbumDao;

	/**
	 * 提交相册图片信息
	 * @param userId  用户id
	 * @param albumId 相册id
	 * @param albumPhotoes 相册图片信息的集合
	 * @author cxw
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public String submitAlbumPhotoInfo(String userId, String albumId, List<SubmitBean> albumPhotoes) {
		try {
			CenterAlbum album = new CenterAlbum();
			album.setAlbumId(Integer.valueOf(albumId));
			album.setCreateUserId(Integer.valueOf(userId));
			List<CenterAlbum> list = centerAlbumDao.selectSingleCenterAlbum(album);
			if(null == list || list.size()<=0){
				LogUtil.error(this.getClass(), "submitAlbumPhotoInfo", "相册用户信息不匹配");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.Album_user_information_donot_match).toJSONString();
			}

			for (SubmitBean submitBean : albumPhotoes) {

				String attachmentLink = submitBean.getAttachmentLink();
				PublicPicture picture = new PublicPicture();
				picture.setFilePath(submitBean.getAttachmentLink());
				picture.setDownloadUrl(submitBean.getAttachmentLink());
				picture.setSuffixName(attachmentLink.substring(attachmentLink.lastIndexOf(".")));
				picture.setSize(Double.valueOf(submitBean.getAttachmentSeconds()));
				picture.setCreateTime(TimeUtil.getCurrentTimestamp());
				picture.setCreateUserId(Integer.valueOf(userId));
				int pictureId = publicdao.insertPublicPicture(picture);

				CenterPhoto centerPhoto = new CenterPhoto();
				centerPhoto.setAlbumId(Integer.valueOf(albumId));
				centerPhoto.setCreateUserId(Integer.valueOf(userId));
				centerPhoto.setCreateTime(TimeUtil.getCurrentTimestamp());
				centerPhoto.setImageId(pictureId);
				centerPhoto.setPraiseNum(0);
				centerPhoto.setIsDelete(0);

				centerPhotoDao.insertCenterPhoto(centerPhoto);
				//相册照片数量进行+操作
				centerAlbumService.addAlbumPhontNum(albumId);
				//更新时间
				centerAlbumDao.updateAlbumaDate(albumId);
				
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(this.getClass(), "submitAlbumPhotoInfo", "提交相册失败");
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.SUBMIT_PHOTO_ALBUM_FAILED).toJSONString();
		}
		return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
	}


}
