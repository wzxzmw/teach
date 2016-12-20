package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubRelClassCourse;

public interface ClubRelClassCourseMapper {

	public abstract void insertClubRelClassCourse(ClubRelClassCourse clubRelClassCourse);
	
	public abstract void deleteClubRelClassCourse(ClubRelClassCourse clubRelClassCourse);
	
	public abstract void updateClubRelClassCourseByKey(ClubRelClassCourse clubRelClassCourse);
	
	public abstract List<ClubRelClassCourse> selectSingleClubRelClassCourse(ClubRelClassCourse clubRelClassCourse);
	
	public abstract List<ClubRelClassCourse> selectAllClubRelClassCourse();
	
	public abstract void insertClubRelClassCourses(List<ClubRelClassCourse> ClubRelClassCourses);

	public abstract Integer getClubCourseByClassIdCount(Map<String, Object> paramMap);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ClubRelClassCourse> queryByPage(Map<String, Object> paramMap);

	

	
}