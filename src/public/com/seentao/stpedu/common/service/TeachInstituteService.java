package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.TeachInstituteDao;
import com.seentao.stpedu.common.entity.TeachInstitute;

/**
 * 学院表基本操作
 * <pre>项目名称：stpedu    
 * 类名称：TeachInstituteService    
 */
@Service
public class TeachInstituteService{
	
	@Autowired
	private TeachInstituteDao teachInstituteDao;
	
	public TeachInstitute getTeachInstitute(Integer id) {
		TeachInstitute teachInstitute = new TeachInstitute();
		teachInstitute.setInstId(id);
		List<TeachInstitute> teachInstituteList = teachInstituteDao .selectSingleTeachInstitute(teachInstitute);
		if(teachInstituteList == null || teachInstituteList .size() <= 0){
			return null;
		}
		
		return teachInstituteList.get(0);
	}
	
	public TeachInstitute getTeachInstituteBySchoolId(Integer id) {
		TeachInstitute teachInstitute = new TeachInstitute();
		teachInstitute.setSchoolId(id);
		List<TeachInstitute> teachInstituteList = teachInstituteDao .selectSingleTeachInstitute(teachInstitute);
		if(teachInstituteList == null || teachInstituteList .size() <= 0){
			return null;
		}
		return teachInstituteList.get(0);
	}
}