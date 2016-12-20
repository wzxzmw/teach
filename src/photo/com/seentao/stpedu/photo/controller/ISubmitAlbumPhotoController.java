package com.seentao.stpedu.photo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.photo.service.SubmitBean;

public interface ISubmitAlbumPhotoController {

	/**
	 * 提交相册图片信息
	 * @param userId  用户id
	 * @param albumId 相册id
	 * @parama albumPhotoes 相册图片信息的集合
	 * @return
	 * @author cxw
	 */
	String submitAlbumPhoto(String userId, String albumId, List<SubmitBean> albumPhotoes);

}