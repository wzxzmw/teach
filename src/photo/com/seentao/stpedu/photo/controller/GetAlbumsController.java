package com.seentao.stpedu.photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.photo.service.GetAlbumsService;
/*
 * cxw
 */
@Controller
public class GetAlbumsController implements IGetAlbumsController {

	@Autowired
	private GetAlbumsService  getAlbumsService;
	/* (non-Javadoc)获取相册信息
	 * @see com.seentao.stpedu.photo.controller.IGetAlbumsController#getAlbums(java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="getAlbums")
	public String getAlbums(String userId,String memberId,int start,int limit,int inquireType){
		
		//return getAlbumsService.getAlbumsInfo(userId,memberId,start,limit,inquireType);
		return getAlbumsService.getAlbumsSomeThings(userId, memberId, start, limit, inquireType);
		
	}
}
