package com.seentao.stpedu.photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.photo.service.SubmitCenterAlbumService;
import com.seentao.stpedu.photo.service.SubmitCenterAlbumsService;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Controller
public class SubmitAlbumController implements ISubmitCenterAlbumController {

	@Autowired
	private SubmitCenterAlbumService submitCenterAlbumService;
	@Autowired
	private SubmitCenterAlbumsService suCenterAlbumsService;
	/* (non-Javadoc)提交相册信息 
	 * @see com.seentao.stpedu.photo.controller.ISubmitCenterAlbumController#submitCenterAlbum(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="submitAlbum")
	public String submitAlbum(String userId,String albumTitle,String albumDesc,String albumId){
	
		//校验相册描述长度
		if(!Common.isValidLength(albumDesc,30)){
			LogUtil.error(this.getClass(), "submitAlbum", "请输入0~30个字");
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.CHARACTER_TOO_LONG).toJSONString();
		}
		if(albumTitle.length()>10 || albumTitle.length()<2){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_TWO_TEN).toJSONString();
		}
		//if(!Common.isValidName(albumTitle)){
			//校验相册名称
		//}
		/**
		 * 1、根据相册id查询相册是否存在
		 * 2、根据
		 */
		//判断用户是否创建相册
		/*if(submitCenterAlbumService.userIsExist(albumTitle,userId)){
			//相册名称占用
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ALBUM_NAME_OCCUPANCY).toJSONString();
		}*/
		return suCenterAlbumsService.submitAlbum(userId,albumTitle,albumDesc,albumId);
	}
}
