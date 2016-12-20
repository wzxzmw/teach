package com.seentao.stpedu.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelStudentCourse;
import com.seentao.stpedu.common.sqlmap.TeachRelStudentCourseMapper;


@Repository
public class TeachRelStudentCourseDao {

	@Autowired
	private TeachRelStudentCourseMapper teachRelStudentCourseMapper;
	
	
	public void insertTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		teachRelStudentCourseMapper .insertTeachRelStudentCourse(teachRelStudentCourse);
	}
	
	public void deleteTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		teachRelStudentCourseMapper .deleteTeachRelStudentCourse(teachRelStudentCourse);
	}
	
	public void updateTeachRelStudentCourseByKey(TeachRelStudentCourse teachRelStudentCourse){
		teachRelStudentCourseMapper .updateTeachRelStudentCourseByKey(teachRelStudentCourse);
	}
	
	public List<TeachRelStudentCourse> selectSingleTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		return teachRelStudentCourseMapper .selectSingleTeachRelStudentCourse(teachRelStudentCourse);
	}
	
	public TeachRelStudentCourse selectTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		List<TeachRelStudentCourse> teachRelStudentCourseList = teachRelStudentCourseMapper .selectSingleTeachRelStudentCourse(teachRelStudentCourse);
		if(teachRelStudentCourseList == null || teachRelStudentCourseList .size() == 0){
			return null;
		}
		return teachRelStudentCourseList .get(0);
	}
	
	public List<TeachRelStudentCourse> selectAllTeachRelStudentCourse(){
		return teachRelStudentCourseMapper .selectAllTeachRelStudentCourse();
	}
	
	public List<TeachRelStudentCourse> selectStudiedCourse(Integer classId, Integer userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		map.put("userId", userId);
		return teachRelStudentCourseMapper.selectStudiedCourse(map);
	}
	
	public void updateTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		teachRelStudentCourseMapper.updateTeachRelStudentCourse(teachRelStudentCourse);
	}
}