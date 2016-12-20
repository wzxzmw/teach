package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachRelStudentCourse;

public interface TeachRelStudentCourseMapper {

	public abstract void insertTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse);
	
	public abstract void deleteTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse);
	
	public abstract void updateTeachRelStudentCourseByKey(TeachRelStudentCourse teachRelStudentCourse);
	
	public abstract List<TeachRelStudentCourse> selectSingleTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse);
	
	public abstract List<TeachRelStudentCourse> selectAllTeachRelStudentCourse();
	
	public abstract List<TeachRelStudentCourse> selectStudiedCourse(Map<String, Object> map);
	
	public abstract void updateTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse);
	
}