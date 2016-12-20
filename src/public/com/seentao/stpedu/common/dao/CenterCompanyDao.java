package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.sqlmap.CenterCompanyMapper;


@Repository
public class CenterCompanyDao {

	@Autowired
	private CenterCompanyMapper centerCompanyMapper;
	
	
	public Integer insertCenterCompany(CenterCompany centerCompany){
		return centerCompanyMapper .insertCenterCompany(centerCompany);
	}
	
	public void deleteCenterCompany(CenterCompany centerCompany){
		centerCompanyMapper .deleteCenterCompany(centerCompany);
	}
	
	public void updateCenterCompanyByKey(CenterCompany centerCompany){
		centerCompanyMapper .updateCenterCompanyByKey(centerCompany);
	}
	
	public List<CenterCompany> selectCenterCompany(CenterCompany centerCompany){
		return centerCompanyMapper .selectSingleCenterCompany(centerCompany);
	}
	
	public CenterCompany selectSingleCenterCompany(CenterCompany centerCompany){
		List<CenterCompany> centerCompanyList = centerCompanyMapper .selectSingleCenterCompany(centerCompany);
		if(centerCompanyList == null || centerCompanyList .size() == 0){
			return null;
		}
		return centerCompanyList .get(0);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterCompany getEntityForDB(Map<String, Object> paramMap){
		CenterCompany tmp = new CenterCompany();
		tmp.setDataId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectSingleCenterCompany(tmp);
	}
	
	public List<CenterCompany> selectAllCenterCompany(){
		return centerCompanyMapper .selectAllCenterCompany();
	}
}