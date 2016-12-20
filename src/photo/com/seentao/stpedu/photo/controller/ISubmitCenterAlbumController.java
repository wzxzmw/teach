package com.seentao.stpedu.photo.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitCenterAlbumController {

	/***
	 * 提交相册信息
	 * @param userId   
	 * @param albumTitle   相册名称
	 * @param albumDesc 相册描述
	 * @return
	 * @throws ParseException 
	 */
	String submitAlbum(String userId, String albumTitle, String albumDesc,String albumId) throws ParseException;

}