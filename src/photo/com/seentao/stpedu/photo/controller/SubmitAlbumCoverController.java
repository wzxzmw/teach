package com.seentao.stpedu.photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.photo.service.SubmitAlbumCoverService;
/**
 * @author cxw
 */
@Controller
public class SubmitAlbumCoverController implements ISubmitAlbumCoverController {

	@Autowired
	private SubmitAlbumCoverService submitQuizBettingController;
	/* (non-Javadoc)设置相册图片的封面
	 * @see com.seentao.stpedu.photo.controller.ISubmitAlbumCoverController#submitAlbumCover(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="submitAlbumCover")
	public String submitAlbumCover(String userId,String albumId,String albumPhotoId){
		
		return submitQuizBettingController.submitPhotoAlbumCover(userId,albumId,albumPhotoId);
	}
}
