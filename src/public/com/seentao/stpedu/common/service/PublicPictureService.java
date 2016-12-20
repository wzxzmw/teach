package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.PublicPictureDao;
import com.seentao.stpedu.common.entity.PublicPicture;
/**
 *public_picture 图片表基本操作
 */
@Service
public class PublicPictureService{
	
	@Autowired
	private PublicPictureDao publicPictureDao;
	
	public List<PublicPicture> getPublicPicture(PublicPicture publicPicture) {
		List<PublicPicture> publicPictureList = publicPictureDao .selectSinglePublicPicture(publicPicture);
		if(publicPictureList == null || publicPictureList .size() <= 0){
			return null;
		}
		return publicPictureList;
	}
	
	public PublicPicture getPublicPictureOne(PublicPicture publicPicture) {
		List<PublicPicture> publicPictureList = publicPictureDao .selectSinglePublicPicture(publicPicture);
		if(publicPictureList == null || publicPictureList .size() <= 0){
			return null;
		}else{
			return publicPictureList.get(0);
		}
	}
	public PublicPicture getPublicPicture(Integer  id) {
		PublicPicture publicPicture = new PublicPicture();
		publicPicture.setPicId(id);
		List<PublicPicture> publicPictureList = publicPictureDao .selectSinglePublicPicture(publicPicture);
		if(publicPictureList == null || publicPictureList .size() <= 0){
			return null;
		}else{
			return publicPictureList.get(0);
		}
	}
	
	//新增图片表
	public int insertPublicPicture(PublicPicture publicPicture){
		return publicPictureDao.insertPublicPicture(publicPicture);
	}
	
	
}