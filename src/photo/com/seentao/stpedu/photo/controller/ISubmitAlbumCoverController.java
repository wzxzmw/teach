package com.seentao.stpedu.photo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitAlbumCoverController {

	/***
	 * 设置相册图片的封面
	 * @param userId
	 * @param albumId 相册id
	 * @param albumPhotoId 	相册图片的id
	 * @return
	 */
	String submitAlbumCover(String userId, String albumId, String albumPhotoId);

}