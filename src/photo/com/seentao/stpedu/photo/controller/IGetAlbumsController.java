package com.seentao.stpedu.photo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetAlbumsController {

	/***
	 * 获取相册信息
	 * @param userId
	 * @param memberId 人员id
	 * @param start
	 * @param limit
	 * @param inquireType 查询类型
	 * @return
	 */
	String getAlbums(String userId, String memberId, int start, int limit, int inquireType);

}