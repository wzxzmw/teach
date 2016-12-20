package com.seentao.stpedu.photo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/***
 * @author cxw
 */
@Service
public class GetAlbumPhotoesService {

	/**
	 * 图片表
	 */
	@Autowired
	private PublicPictureDao publicPictureDao;

	@Autowired
	private CenterAlbumDao albumDao;

	@Autowired
	private CenterPhotoDao centerPhotoDao;
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
	public String getAlbumPhotoesInfo(String userId, String memberId, String albumId, int start, int limit,
			int inquireType) {
		CenterPhoto centerPhoto = null;
		centerPhoto = new CenterPhoto();
		if(StringUtils.isNoneEmpty(memberId)){
			centerPhoto.setCreateUserId(Integer.valueOf(memberId));
		}else{
			centerPhoto.setCreateUserId(Integer.valueOf(userId));
		}
		CenterPhoto photo2 = centerPhotoDao.selectCenterPDesc(centerPhoto);
		if(inquireType == 1){
			if(null == photo2){
				JSONObject json = new JSONObject();
				json.put("albumPhotoes", new JSONArray());
				return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
			}
			//获取最新上传的相册图片信息 
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			if(StringUtil.isEmpty(memberId)){
				paramMap.put("createUserId", Integer.valueOf(userId));
			}else{
				paramMap.put("createUserId", Integer.valueOf(memberId));
			}
			paramMap.put("albumId", photo2.getAlbumId());
			//调用分页组件
			QueryPage<CenterPhoto> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterPhoto.class,"queryCountalNewbum","queryByNewCenteralbum");
			//msg错误码
			if(!appQueryPage.getState()){
				appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
			}
			for (CenterPhoto centerP : appQueryPage.getList()) {
				//根据图片的id查询【图片表】获取路径，创建时间
				PublicPicture picture = new PublicPicture();
				picture.setPicId(centerP.getImageId());
				List<PublicPicture> publicPicture = publicPictureDao.selectSinglePublicPicture(picture);
				for (PublicPicture picture2 : publicPicture) {
					//					centerP.setFilePath(picture2.getFilePath());
					centerP.setFilePath(Common.getImgUrl(centerP.getImageId(),null));
					centerP.setCreatePhotoTime(picture2.getCreateTime());
				}
				CenterAlbum album = new CenterAlbum();
				album.setAlbumId(centerP.getAlbumId());
				CenterAlbum album2 = albumDao.selectCenterAlbum(album);
				centerP.setAlbumExplain(album2.getAlbumExplain());
				CenterAlbum centerAlbum = new CenterAlbum();
				centerAlbum.setAlbumId(centerP.getAlbumId());
				List<CenterAlbum> list = albumDao.selectSingleCenterAlbum(centerAlbum);
				if(list.size()>=0){
					boolean a = false;
					for (CenterAlbum CenterAlbum : list) {
						centerP.setAlbumName(CenterAlbum.getAlbumName());
						centerP.setLastUpdateTime(CenterAlbum.getLastUpdateTime());
						centerP.setPhotoNum(CenterAlbum.getPhotoNum());
						if(Integer.valueOf(CenterAlbum.getCoverPhotoId())==centerP.getImageId()){
							a= true;
							break;
						}
					}
					if(a){
						centerP.setCoverPhotoId(GameConstants.YES);
					}else{
						centerP.setCoverPhotoId(GameConstants.NO);
					}
				}else{
					centerP.setCoverPhotoId(GameConstants.NO);
				}
			}
			return appQueryPage.getMessageJSONObject("albumPhotoes").toJSONString();
		}else if(inquireType == 2){

			//通过相册id获取该相册下的图片信息
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("albumId", Integer.valueOf(albumId));
			//调用分页组件
			QueryPage<CenterPhoto> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterPhoto.class,"queryCountalbumInfo","queryByCenteralbumInfo");
			if(appQueryPage.getList().size()<=0){
				CenterAlbum album = new CenterAlbum();
				album.setAlbumId(Integer.valueOf(albumId));
				CenterAlbum album2 = albumDao.selectCenterAlbum(album);
				JSONObject json = new JSONObject();
				JSONObject jsono = new JSONObject();
				JSONArray array = new JSONArray();
				jsono.put("createTime", album2.getCreateTime());
				jsono.put("lastUpdateTime", album2.getLastUpdateTime());
				jsono.put("albumName", album2.getAlbumName());
				jsono.put("albumExplain", album2.getAlbumExplain());
				jsono.put("praiseNum",album2.getPhotoNum());
				array.add(jsono);
				json.put("albumPhotoes", array);
				json.put("returnCount", 0);
				return Common.getReturn(AppErrorCode.SUCCESS, "", json).toJSONString();
			}
			//msg错误码
			if(!appQueryPage.getState()){
				appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
			}
			for (CenterPhoto centerP : appQueryPage.getList()) {
				//根据图片的id查询【图片表】获取路径，创建时间
				PublicPicture picture = new PublicPicture();
				picture.setPicId(centerP.getImageId());
				List<PublicPicture> publicPicture = publicPictureDao.selectSinglePublicPicture(picture);
				for (PublicPicture picture2 : publicPicture) {
					//						centerP.setFilePath(picture2.getFilePath());
					centerP.setFilePath(Common.getImgUrl(centerP.getImageId(),null));
					centerP.setCreatePhotoTime(picture2.getCreateTime());
				}
				CenterAlbum album = new CenterAlbum();
				album.setAlbumId(Integer.valueOf(albumId));
				CenterAlbum album2 = albumDao.selectCenterAlbum(album);
				centerP.setAlbumExplain(album2.getAlbumExplain());
				CenterAlbum centerAlbum = new CenterAlbum();
				centerAlbum.setAlbumId(Integer.valueOf(albumId));
				List<CenterAlbum> list = albumDao.selectSingleCenterAlbum(centerAlbum);
				if(list.size()>=0){
					boolean a = false;
					for (CenterAlbum CenterAlbum : list) {
						centerP.setAlbumName(CenterAlbum.getAlbumName());
						centerP.setLastUpdateTime(CenterAlbum.getLastUpdateTime());
						centerP.setPhotoNum(CenterAlbum.getPhotoNum());
						if(Integer.valueOf(CenterAlbum.getCoverPhotoId())==centerP.getPhotoId()){
							a= true;
							break;
						}
					}
					if(a){
						centerP.setCoverPhotoId(GameConstants.YES);
					}else{
						centerP.setCoverPhotoId(GameConstants.NO);
					}

				}else{
					centerP.setCoverPhotoId(GameConstants.NO);
				}

			}

			return appQueryPage.getMessageJSONObject("albumPhotoes").toJSONString();

		}
		LogUtil.error(this.getClass(), "getAlbumPhotoesInfo", "查询条件有误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
	}
}

