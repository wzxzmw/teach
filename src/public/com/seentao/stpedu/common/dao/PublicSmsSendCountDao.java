package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.PublicSmsSendCount;
import com.seentao.stpedu.common.sqlmap.PublicSmsSendCountMapper;


@Repository
public class PublicSmsSendCountDao {

	@Autowired
	private PublicSmsSendCountMapper publicSmsSendCountMapper;
	
	
	public void insertPublicSmsSendCount(PublicSmsSendCount publicSmsSendCount){
		publicSmsSendCountMapper .insertPublicSmsSendCount(publicSmsSendCount);
	}
	
	public void deletePublicSmsSendCount(PublicSmsSendCount publicSmsSendCount){
		publicSmsSendCountMapper .deletePublicSmsSendCount(publicSmsSendCount);
	}
	
	public void updatePublicSmsSendCountByKey(PublicSmsSendCount publicSmsSendCount){
		publicSmsSendCountMapper .updatePublicSmsSendCountByKey(publicSmsSendCount);
	}
	
	public List<PublicSmsSendCount> selectSinglePublicSmsSendCount(PublicSmsSendCount publicSmsSendCount){
		return publicSmsSendCountMapper .selectSinglePublicSmsSendCount(publicSmsSendCount);
	}
	
	public PublicSmsSendCount selectPublicSmsSendCount(PublicSmsSendCount publicSmsSendCount){
		List<PublicSmsSendCount> publicSmsSendCountList = publicSmsSendCountMapper .selectSinglePublicSmsSendCount(publicSmsSendCount);
		if(publicSmsSendCountList == null || publicSmsSendCountList .size() == 0){
			return null;
		}
		return publicSmsSendCountList .get(0);
	}
	
	public List<PublicSmsSendCount> selectAllPublicSmsSendCount(){
		return publicSmsSendCountMapper .selectAllPublicSmsSendCount();
	}
	
	
	public PublicSmsSendCount selectPublicSmsSendCountByPhone(PublicSmsSendCount publicSmsSendCount){
		List<PublicSmsSendCount> publicSmsSendCountList = publicSmsSendCountMapper .selectPublicSmsSendCountByPhone(publicSmsSendCount);
		if(publicSmsSendCountList == null || publicSmsSendCountList .size() == 0){
			return null;
		}
		return publicSmsSendCountList .get(0);
	}
	
	public void updatePublicSmsSendCountByPhone(PublicSmsSendCount publicSmsSendCount){
		publicSmsSendCountMapper .updatePublicSmsSendCountByPhone(publicSmsSendCount);
	}
}