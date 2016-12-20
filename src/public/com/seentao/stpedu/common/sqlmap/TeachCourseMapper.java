package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachCourse;

public interface TeachCourseMapper {

	public abstract void insertTeachCourse(TeachCourse teachCourse);
	
	public abstract void deleteTeachCourse(TeachCourse teachCourse);
	
	public abstract void updateTeachCourseByKey(TeachCourse teachCourse);
	
	public abstract List<TeachCourse> selectSingleTeachCourse(TeachCourse teachCourse);
	
	public abstract List<TeachCourse> selectAllTeachCourse();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachCourse> queryByPage(Map<String, Object> paramMap);

	public abstract void addActualnum(TeachCourse param);

	public abstract List<Integer> queryByPageids(Map<String, String> paramMap);

	public abstract void delTeachCourseAll(List<TeachCourse> delTeachCourse);
	
	public abstract List<TeachCourse> selectAllCourse(Integer classId);
	
}