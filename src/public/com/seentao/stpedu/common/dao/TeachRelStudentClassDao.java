package com.seentao.stpedu.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.sqlmap.TeachRelStudentClassMapper;


@Repository
public class TeachRelStudentClassDao {

	@Autowired
	private TeachRelStudentClassMapper teachRelStudentClassMapper;
	
	
	public void insertTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassMapper .insertTeachRelStudentClass(teachRelStudentClass);
	}
	
	public void deleteTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassMapper .deleteTeachRelStudentClass(teachRelStudentClass);
	}
	
	public void updateTeachRelStudentClassByKey(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassMapper .updateTeachRelStudentClassByKey(teachRelStudentClass);
	}
	
	public List<TeachRelStudentClass> selectSingleTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		return teachRelStudentClassMapper .selectSingleTeachRelStudentClass(teachRelStudentClass);
	}
	
	public TeachRelStudentClass selectTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		List<TeachRelStudentClass> teachRelStudentClassList = teachRelStudentClassMapper .selectSingleTeachRelStudentClass(teachRelStudentClass);
		if(teachRelStudentClassList == null || teachRelStudentClassList .size() == 0){
			return null;
		}
		return teachRelStudentClassList .get(0);
	}
	
	public List<TeachRelStudentClass> selectAllTeachRelStudentClass(){
		return teachRelStudentClassMapper .selectAllTeachRelStudentClass();
	}

	public void saveStudentAll(List<TeachRelStudentClass> classes) {
		teachRelStudentClassMapper.saveStudentAll(classes);
	}

	public void deleteTeachStudentAll(List<TeachRelStudentClass> teachStudentAll) {
		teachRelStudentClassMapper.deleteTeachStudentAll(teachStudentAll);
	}

	public List<TeachRelStudentClass> getGradesByClassId(String classId, Integer start, Integer limit) {
		TeachRelStudentClass t = new TeachRelStudentClass();
		t.setClassId(Integer.valueOf(classId));
		t.setIsDelete(0);
		t.setStart(start);
		t.setLimit(limit);
		return teachRelStudentClassMapper.getGradesByClassId(t);
	}

	public Integer queryCountByClassId(String classId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("classId", Integer.valueOf(classId));
		return teachRelStudentClassMapper.queryCountByClassId(map);
	}

	public void updateTeachRelStudentClassByKeyNoRel(TeachRelStudentClass teachStudentClass) {
		teachRelStudentClassMapper.updateTeachRelStudentClassByKeyNoRel(teachStudentClass);
	}
}