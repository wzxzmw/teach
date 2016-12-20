package com.seentao.stpedu.photo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.photo.service.SubmitAlbumPhotoService;
import com.seentao.stpedu.photo.service.SubmitBean;
/**
 * @author cxw
 */
@Controller
public class SubmitAlbumPhotoController implements ISubmitAlbumPhotoController {

	@Autowired
	private SubmitAlbumPhotoService submitAlbumPhotoService;

	/* (non-Javadoc)提交相册的图片信息
	 * @see com.seentao.stpedu.photo.controller.ISubmitAlbumPhotoController#submitAlbumPhoto(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="submitAlbumPhoto")
	public String submitAlbumPhoto(String userId,String albumId,List<SubmitBean> albumPhotoes){
		
		//return submitAlbumPhotoService.submitAlbumPhotoInfo(userId,albumId,albumPhotoes);
		return submitAlbumPhotoService.submitAlbumPhotoInfo(userId, albumId, albumPhotoes);
	}
}
