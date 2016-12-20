package com.seentao.stpedu.photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.photo.service.GetAlbumPhotoesService;
/**
 * @author cxw
 */
@Controller
public class GetAlbumPhotoesController implements IGetAlbumPhotoesController {

	@Autowired
	private GetAlbumPhotoesService getAlbumPhotoesService;
	/* (non-Javadoc)获取相册下的图片信息
	 * @see com.seentao.stpedu.photo.controller.IGetAlbumPhotoesController#getAlbumPhotoes(java.lang.String, java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="getAlbumPhotoes")
	public String getAlbumPhotoes(String userId,String memberId,String albumId,int start,int limit,int inquireType){
		
		return	getAlbumPhotoesService.getAlbumPhotoesInfo(userId,memberId,albumId,start,limit,inquireType);
		
		
	}
}
