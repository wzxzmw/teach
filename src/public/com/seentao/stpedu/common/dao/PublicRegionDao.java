package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.PublicRegion;
import com.seentao.stpedu.common.sqlmap.PublicRegionMapper;


@Repository
public class PublicRegionDao {

	@Autowired
	private PublicRegionMapper publicRegionMapper;
	
	
	public void insertPublicRegion(PublicRegion publicRegion){
		publicRegionMapper .insertPublicRegion(publicRegion);
	}
	
	public void deletePublicRegion(PublicRegion publicRegion){
		publicRegionMapper .deletePublicRegion(publicRegion);
	}
	
	public void updatePublicRegionByKey(PublicRegion publicRegion){
		publicRegionMapper .updatePublicRegionByKey(publicRegion);
	}
	
	public List<PublicRegion> selectSinglePublicRegion(PublicRegion publicRegion){
		return publicRegionMapper .selectSinglePublicRegion(publicRegion);
	}
	
	public PublicRegion selectPublicRegion(PublicRegion publicRegion){
		List<PublicRegion> publicRegionList = publicRegionMapper .selectSinglePublicRegion(publicRegion);
		if(publicRegionList == null || publicRegionList .size() == 0){
			return null;
		}
		return publicRegionList .get(0);
	}
	
	public List<PublicRegion> selectAllPublicRegion(){
		return publicRegionMapper .selectAllPublicRegion();
	}

	public List<PublicRegion> selectPublicRegionByCityId(PublicRegion publicRegion) {
		return publicRegionMapper.selectPublicRegionByCityId(publicRegion);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public PublicRegion getEntityForDB(Map<String, Object> paramMap){
		PublicRegion tmp = new PublicRegion();
		tmp.setRegionId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectPublicRegion(tmp);
	}
}