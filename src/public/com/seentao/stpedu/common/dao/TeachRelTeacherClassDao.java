package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.sqlmap.TeachRelTeacherClassMapper;


@Repository
public class TeachRelTeacherClassDao {

	@Autowired
	private TeachRelTeacherClassMapper teachRelTeacherClassMapper;
	
	
	public void insertTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassMapper .insertTeachRelTeacherClass(teachRelTeacherClass);
	}
	
	public void deleteTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassMapper .deleteTeachRelTeacherClass(teachRelTeacherClass);
	}
	
	public void updateTeachRelTeacherClassByKey(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassMapper .updateTeachRelTeacherClassByKey(teachRelTeacherClass);
	}
	
	public List<TeachRelTeacherClass> selectSingleTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		return teachRelTeacherClassMapper .selectSingleTeachRelTeacherClass(teachRelTeacherClass);
	}
	
	public TeachRelTeacherClass selectTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		List<TeachRelTeacherClass> teachRelTeacherClassList = teachRelTeacherClassMapper .selectSingleTeachRelTeacherClass(teachRelTeacherClass);
		if(teachRelTeacherClassList == null || teachRelTeacherClassList .size() == 0){
			return null;
		}
		return teachRelTeacherClassList .get(0);
	}
	
	public List<TeachRelTeacherClass> selectAllTeachRelTeacherClass(){
		return teachRelTeacherClassMapper .selectAllTeachRelTeacherClass();
	}
	
	public Integer queryCountAll(Map<String, Object> paramMap) {
		return teachRelTeacherClassMapper.queryCountAll(paramMap);
	}

	public List<TeachRelTeacherClass> queryByPageAll(Map<String, Object> paramMap) {
		return teachRelTeacherClassMapper.queryByPageAll(paramMap);
	}
	
	public Integer queryteachRelClassByCount(Map<String, Object> paramMap) {
		return teachRelTeacherClassMapper.queryteachRelClassByCount(paramMap);
	}

	public List<TeachRelTeacherClass> queryteachRelClassByPage(Map<String, Object> paramMap) {
		return teachRelTeacherClassMapper.queryteachRelClassByPage(paramMap);
	}
}