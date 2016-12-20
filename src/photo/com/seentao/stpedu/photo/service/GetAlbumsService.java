package com.seentao.stpedu.photo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.CenterAlbumDao;
import com.seentao.stpedu.common.dao.CenterPhotoDao;
import com.seentao.stpedu.common.dao.PublicPictureDao;
import com.seentao.stpedu.common.entity.CenterAlbum;
import com.seentao.stpedu.common.entity.CenterPhoto;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/**
 * @author cxw
 */
@Service
public class GetAlbumsService {

	@Autowired
	private CenterAlbumDao CenterAlbumDao;

	@Autowired
	private CenterPhotoDao centerPhotoDao;
	/**
	 * 获取相册信息
	 * @param userId
	 * @param memberId
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @return
	 */
	@Transactional
	public String getAlbumsInfo(String userId, String memberId, int start, int limit, int inquireType) {
		
		//判断用户是否创建相册
		if(memberId == null){
			CenterAlbum album1 = new CenterAlbum();
			album1.setCreateUserId(Integer.valueOf(userId));
			List<CenterAlbum> list = CenterAlbumDao.selectSingleCenterAlbum(album1);
			if(null == list || list.size()<=0){
				JSONObject json = new JSONObject();
				json.put("albums", new JSONArray());
				return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
			}
			try {
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("createUserId", Integer.valueOf(userId));
				//调用分页组件
				QueryPage<CenterAlbum> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterAlbum.class,"queryCountalbum","queryByCenteralbum");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
				}
				for (CenterAlbum centerAlbum : appQueryPage.getList()) {
					if(centerAlbum.getCoverPhotoId()==null || centerAlbum.getCoverPhotoId() == 0){
						CenterPhoto centerPhoto = new CenterPhoto();
						centerPhoto.setAlbumId(centerAlbum.getAlbumId());
						CenterPhoto CenterPhotoId = centerPhotoDao.selectCenterPhotoId(centerPhoto);
						if(null == CenterPhotoId  || CenterPhotoId.getImageId() == null){
							centerAlbum.setFilePath("");
						}else{
//							PublicPicture picture = new PublicPicture();
//							picture.setPicId(CenterPhotoId.getImageId());
//							PublicPicture publicPicture = publicPictureDao.selectPublicPicture(picture);
//							centerAlbum.setFilePath(publicPicture.getFilePath());
							centerAlbum.setFilePath(Common.checkPic(Common.getImgUrl(CenterPhotoId.getImageId(),null)) == true ? Common.getImgUrl(CenterPhotoId.getImageId(),null)+ActiveUrl.HEAD_MAP:Common.getImgUrl(CenterPhotoId.getImageId(),null));
						}
					}else{
						//根据'封面照片ID获取链接地址
//						PublicPicture picture = new PublicPicture();
//						picture.setPicId(centerAlbum.getCoverPhotoId());
//						PublicPicture publicPicture = publicPictureDao.selectPublicPicture(picture);
//						centerAlbum.setFilePath(publicPicture.getFilePath());
						String msg =Common.getImgUrl(centerAlbum.getCoverPhotoId(),null);
						centerAlbum.setFilePath(Common.checkPic(msg) == true ? msg+ActiveUrl.HEAD_MAP:msg);
					}
				}

				return appQueryPage.getMessageJSONObject("albums").toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "getAlbumsInfo", "获取人员相册信息失败");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.FAILED_TO_GET_THE_STAFF_HOTO_ALBUM).toJSONString();
			}
		}else{
			CenterAlbum album1 = new CenterAlbum();
			if(!StringUtil.isEmpty(memberId)){
				album1.setCreateUserId(Integer.valueOf(memberId));
			}else{
				album1.setCreateUserId(Integer.valueOf(userId));
			}
			List<CenterAlbum> list = CenterAlbumDao.selectSingleCenterAlbum(album1);
			if(null == list || list.size()<=0){
				JSONObject json = new JSONObject();
				json.put("albums", new JSONArray());
				return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
			}
			try {
				Map<String ,Object> paramMap = new HashMap<String ,Object>();
				paramMap.put("createUserId", Integer.valueOf(userId));
				//调用分页组件
				QueryPage<CenterAlbum> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterAlbum.class,"queryCountalbum","queryByCenteralbum");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
				}
				for (CenterAlbum centerAlbum : appQueryPage.getList()) {
					if(centerAlbum.getCoverPhotoId()==null || centerAlbum.getCoverPhotoId() == 0){
						CenterPhoto centerPhoto = new CenterPhoto();
						centerPhoto.setAlbumId(centerAlbum.getAlbumId());
						CenterPhoto CenterPhotoId = centerPhotoDao.selectCenterPhotoId(centerPhoto);
						if(null == CenterPhotoId  || CenterPhotoId.getImageId() == null){
							centerAlbum.setFilePath("");
						}else{
//							PublicPicture picture = new PublicPicture();
//							picture.setPicId(CenterPhotoId.getImageId());
//							PublicPicture publicPicture = publicPictureDao.selectPublicPicture(picture);
//							centerAlbum.setFilePath(publicPicture.getFilePath());
							String msg = Common.getImgUrl(CenterPhotoId.getImageId(),null);
							centerAlbum.setFilePath(Common.checkPic(msg) == true ? msg +ActiveUrl.HEAD_MAP:msg);
						}
					}else{
						//根据'封面照片ID获取链接地址
//						PublicPicture picture = new PublicPicture();
//						picture.setPicId(centerAlbum.getCoverPhotoId());
//						PublicPicture publicPicture = publicPictureDao.selectPublicPicture(picture);
//						centerAlbum.setFilePath(publicPicture.getFilePath());
						String msg = Common.getImgUrl(centerAlbum.getCoverPhotoId(),null);
						centerAlbum.setFilePath(Common.checkPic(msg) == true ? msg +ActiveUrl.HEAD_MAP:msg);
					}
				}

				return appQueryPage.getMessageJSONObject("albums").toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "getAlbumsInfo", "获取人员相册信息失败");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.FAILED_TO_GET_THE_STAFF_HOTO_ALBUM).toJSONString();
			}
		}
	}
	
	public String getAlbumsSomeThings(String userId, String memberId, int start, int limit, int inquireType){
		CenterAlbum album1 = new CenterAlbum();
		if(userId.equals(memberId)){
			album1.setCreateUserId(Integer.valueOf(userId));
		}else{
			album1.setCreateUserId(Integer.valueOf(memberId));
		}
		List<CenterAlbum> list = CenterAlbumDao.selectSingleCenterAlbum(album1);
		if(null == list || list.size()<=0){
			JSONObject json = new JSONObject();
			json.put("albums", new JSONArray());
			return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
		}
		try {
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			if(userId.equals(memberId)){
				paramMap.put("createUserId", Integer.valueOf(userId));
			}else{
				paramMap.put("createUserId", Integer.valueOf(memberId));
			}
			//调用分页组件
			QueryPage<CenterAlbum> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterAlbum.class,"queryCountalbum","queryByCenteralbum");
			//msg错误码
			if(!appQueryPage.getState()){
				appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
			}
			for (CenterAlbum centerAlbum : appQueryPage.getList()) {
				if(centerAlbum.getCoverPhotoId()==null || centerAlbum.getCoverPhotoId() == 0){
					CenterPhoto centerPhoto = new CenterPhoto();
					centerPhoto.setAlbumId(centerAlbum.getAlbumId());
					CenterPhoto CenterPhotoId = centerPhotoDao.selectCenterPhotoId(centerPhoto);
					if(null == CenterPhotoId  || CenterPhotoId.getImageId() == null){
						centerAlbum.setFilePath("");
					}else{
						String msg = Common.getImgUrl(CenterPhotoId.getImageId(),null); 
						centerAlbum.setFilePath(Common.checkPic(msg) == true ? msg +ActiveUrl.HEAD_MAP:msg);
					}
				}else{
					//根据'封面照片ID获取链接地址
					String ms = Common.getImgUrl(centerAlbum.getCoverPhotoId(),null);
					centerAlbum.setFilePath(Common.checkPic(ms) == true ? ms +ActiveUrl.HEAD_MAP:ms);
				}
			}

			return appQueryPage.getMessageJSONObject("albums").toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "getAlbumsInfo", "获取人员相册信息失败");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.FAILED_TO_GET_THE_STAFF_HOTO_ALBUM).toJSONString();
		}
	
	}

}
