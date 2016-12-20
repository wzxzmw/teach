package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.sqlmap.TeachRelClassCourseMapper;


@Repository
public class TeachRelClassCourseDao {

	@Autowired
	private TeachRelClassCourseMapper teachRelClassCourseMapper;
	
	
	public void insertTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse){
		teachRelClassCourseMapper .insertTeachRelClassCourse(teachRelClassCourse);
	}
	
	public void deleteTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse){
		teachRelClassCourseMapper .deleteTeachRelClassCourse(teachRelClassCourse);
	}
	
	public void updateTeachRelClassCourseByKey(TeachRelClassCourse teachRelClassCourse){
		teachRelClassCourseMapper .updateTeachRelClassCourseByKey(teachRelClassCourse);
	}
	
	public List<TeachRelClassCourse> selectSingleTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse){
		return teachRelClassCourseMapper .selectSingleTeachRelClassCourse(teachRelClassCourse);
	}
	
	public TeachRelClassCourse selectTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse){
		List<TeachRelClassCourse> teachRelClassCourseList = teachRelClassCourseMapper .selectSingleTeachRelClassCourse(teachRelClassCourse);
		if(teachRelClassCourseList == null || teachRelClassCourseList .size() == 0){
			return null;
		}
		return teachRelClassCourseList .get(0);
	}
	
	public List<TeachRelClassCourse> selectAllTeachRelClassCourse(){
		return teachRelClassCourseMapper .selectAllTeachRelClassCourse();
	}

	public Integer getCourseByClassIdCount(Map<String, Object> paramMap) {
		return teachRelClassCourseMapper.getCourseByClassIdCount(paramMap) ;
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return teachRelClassCourseMapper.queryCount(paramMap);
	}

	public  List<TeachRelClassCourse> queryByPage(Map<String, Object> paramMap) {
		return teachRelClassCourseMapper.queryByPage(paramMap);
	}

	public void insertTeachRelClassCourseAll(List<TeachRelClassCourse> teachRelClassList) {
		teachRelClassCourseMapper.insertTeachRelClassCourseAll(teachRelClassList);
	}
	
	
	
}