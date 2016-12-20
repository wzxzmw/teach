package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.TeachCourseDao;
import com.seentao.stpedu.common.entity.TeachCourse;

@Service
public class TeachCourseService{
	
	@Autowired
	private TeachCourseDao teachCourseDao;
	
	public String getTeachCourse(Integer id) {
		TeachCourse teachCourse = new TeachCourse();
		List<TeachCourse> teachCourseList = teachCourseDao .selectSingleTeachCourse(teachCourse);
		if(teachCourseList == null || teachCourseList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(teachCourseList);
	}
	
	public void insertTeachCourse(TeachCourse teachcourse) {
		teachCourseDao.insertTeachCourse(teachcourse);
	}
}