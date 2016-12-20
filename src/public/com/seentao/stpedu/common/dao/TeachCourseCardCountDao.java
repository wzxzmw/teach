package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.TeachCourseCardCount;
import com.seentao.stpedu.common.sqlmap.TeachCourseCardCountMapper;


@Repository
public class TeachCourseCardCountDao {

	@Autowired
	private TeachCourseCardCountMapper teachCourseCardCountMapper;
	
	
	public void insertTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountMapper .insertTeachCourseCardCount(teachCourseCardCount);
	}
	
	public void deleteTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountMapper .deleteTeachCourseCardCount(teachCourseCardCount);
	}
	
	public void updateTeachCourseCardCountByKey(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountMapper .updateTeachCourseCardCountByKey(teachCourseCardCount);
	}
	
	public List<TeachCourseCardCount> selectSingleTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount){
		return teachCourseCardCountMapper .selectSingleTeachCourseCardCount(teachCourseCardCount);
	}
	
	public TeachCourseCardCount selectTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount){
		List<TeachCourseCardCount> teachCourseCardCountList = teachCourseCardCountMapper .selectSingleTeachCourseCardCount(teachCourseCardCount);
		if(teachCourseCardCountList == null || teachCourseCardCountList .size() == 0){
			return null;
		}
		return teachCourseCardCountList .get(0);
	}
	
	public List<TeachCourseCardCount> selectAllTeachCourseCardCount(){
		return teachCourseCardCountMapper .selectAllTeachCourseCardCount();
	}
	
	public void updateTeachCourseCountByClassId(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountMapper.updateTeachCourseCountByClassId(teachCourseCardCount);
	}
}