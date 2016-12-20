package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachCourseCardCount;
import java.util.List;

public interface TeachCourseCardCountMapper {

	public abstract void insertTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount);
	
	public abstract void deleteTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount);
	
	public abstract void updateTeachCourseCardCountByKey(TeachCourseCardCount teachCourseCardCount);
	
	public abstract void updateTeachCourseCountByClassId(TeachCourseCardCount teachCourseCardCount);
	
	public abstract List<TeachCourseCardCount> selectSingleTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount);
	
	public abstract List<TeachCourseCardCount> selectAllTeachCourseCardCount();
	
}