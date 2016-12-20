package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachInstitute;
import com.seentao.stpedu.common.sqlmap.TeachInstituteMapper;


@Repository
public class TeachInstituteDao {

	@Autowired
	private TeachInstituteMapper teachInstituteMapper;
	
	
	public void insertTeachInstitute(TeachInstitute teachInstitute){
		teachInstituteMapper .insertTeachInstitute(teachInstitute);
	}
	
	public void deleteTeachInstitute(TeachInstitute teachInstitute){
		teachInstituteMapper .deleteTeachInstitute(teachInstitute);
	}
	
	public void updateTeachInstituteByKey(TeachInstitute teachInstitute){
		teachInstituteMapper .updateTeachInstituteByKey(teachInstitute);
	}
	
	public List<TeachInstitute> selectSingleTeachInstitute(TeachInstitute teachInstitute){
		return teachInstituteMapper .selectSingleTeachInstitute(teachInstitute);
	}
	
	public TeachInstitute selectTeachInstitute(TeachInstitute teachInstitute){
		List<TeachInstitute> teachInstituteList = teachInstituteMapper .selectSingleTeachInstitute(teachInstitute);
		if(teachInstituteList == null || teachInstituteList .size() == 0){
			return null;
		}
		return teachInstituteList .get(0);
	}
	
	public List<TeachInstitute> selectAllTeachInstitute(){
		return teachInstituteMapper .selectAllTeachInstitute();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		TeachInstitute teachInstitute = new TeachInstitute();
		teachInstitute.setInstId(Integer.valueOf(queryParam.get("id_key").toString()));
		return selectTeachInstitute(teachInstitute);
	}
	
}