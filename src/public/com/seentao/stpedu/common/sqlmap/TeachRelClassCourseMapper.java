package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachRelClassCourse;

public interface TeachRelClassCourseMapper {

	public abstract void insertTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse);
	
	public abstract void deleteTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse);
	
	public abstract void updateTeachRelClassCourseByKey(TeachRelClassCourse teachRelClassCourse);
	
	public abstract List<TeachRelClassCourse> selectSingleTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse);
	
	public abstract List<TeachRelClassCourse> selectAllTeachRelClassCourse();

	public abstract Integer getCourseByClassIdCount(Map<String, Object> paramMap);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachRelClassCourse> queryByPage(Map<String, Object> paramMap);

	public abstract void insertTeachRelClassCourseAll(List<TeachRelClassCourse> list);
	
}