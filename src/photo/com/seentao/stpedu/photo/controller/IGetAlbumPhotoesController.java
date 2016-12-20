package com.seentao.stpedu.photo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetAlbumPhotoesController {

	/**
	 * 获取相册下的图片信息
	 * @param userId
	 * @param memberId 人员id
	 * @param albumId 相册id
	 * @param start
	 * @param limit
	 * @param inquireType 查询类型
	 * @return
	 */
	String getAlbumPhotoes(String userId, String memberId, String albumId, int start, int limit, int inquireType);

}