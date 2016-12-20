package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.sqlmap.TeachSchoolMapper;


@Repository
public class TeachSchoolDao {

	@Autowired
	private TeachSchoolMapper teachSchoolMapper;
	
	
	public void insertTeachSchool(TeachSchool teachSchool){
		teachSchoolMapper .insertTeachSchool(teachSchool);
	}
	
	public void deleteTeachSchool(TeachSchool teachSchool){
		teachSchoolMapper .deleteTeachSchool(teachSchool);
	}
	
	public void updateTeachSchoolByKey(TeachSchool teachSchool){
		teachSchoolMapper .updateTeachSchoolByKey(teachSchool);
	}
	
	public List<TeachSchool> selectSingleTeachSchool(TeachSchool teachSchool){
		return teachSchoolMapper .selectSingleTeachSchool(teachSchool);
	}
	
	public TeachSchool selectTeachSchool(TeachSchool teachSchool){
		List<TeachSchool> teachSchoolList = teachSchoolMapper .selectSingleTeachSchool(teachSchool);
		if(teachSchoolList == null || teachSchoolList .size() == 0){
			return null;
		}
		return teachSchoolList .get(0);
	}
	
	public List<TeachSchool> selectAllTeachSchool(){
		return teachSchoolMapper .selectAllTeachSchool();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public TeachSchool getEntityForDB(Map<String, Object> paramMap){
		TeachSchool tmp = new TeachSchool();
		tmp.setSchoolId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectTeachSchool(tmp);
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return teachSchoolMapper.queryCount(paramMap);
	}
	public List<TeachSchool> queryByPage(Map<String, Object> paramMap) {
		return teachSchoolMapper.queryByPage(paramMap);
	}
	public Integer querySchoolCount(Map<String, Object> paramMap) {
		return teachSchoolMapper.querySchoolCount(paramMap);
	}
	public List<TeachSchool> querySchoolByPage(Map<String, Object> paramMap) {
		return teachSchoolMapper.querySchoolByPage(paramMap);
	}
}