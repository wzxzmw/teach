package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.sqlmap.PublicPictureMapper;


@Repository
public class PublicPictureDao {

	@Autowired
	private PublicPictureMapper publicPictureMapper;
	
	
	public int insertPublicPicture(PublicPicture publicPicture){
		publicPictureMapper .insertPublicPicture(publicPicture);
		return publicPicture.getPicId();
	}
	
	public void deletePublicPicture(PublicPicture publicPicture){
		publicPictureMapper .deletePublicPicture(publicPicture);
	}
	
	public void updatePublicPictureByKey(PublicPicture publicPicture){
		publicPictureMapper .updatePublicPictureByKey(publicPicture);
	}
	
	public List<PublicPicture> selectSinglePublicPicture(PublicPicture publicPicture){
		return publicPictureMapper .selectSinglePublicPicture(publicPicture);
	}
	
	public PublicPicture selectPublicPicture(PublicPicture publicPicture){
		List<PublicPicture> publicPictureList = publicPictureMapper .selectSinglePublicPicture(publicPicture);
		if(publicPictureList == null || publicPictureList .size() == 0){
			return null;
		}
		return publicPictureList .get(0);
	}
	
	public List<PublicPicture> selectAllPublicPicture(){
		return publicPictureMapper .selectAllPublicPicture();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public PublicPicture getEntityForDB(Map<String, Object> paramMap){
		PublicPicture tmp = new PublicPicture();
		tmp.setPicId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectPublicPicture(tmp);
	}
	
}