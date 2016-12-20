package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubRelClassCourse;
import com.seentao.stpedu.common.sqlmap.ClubRelClassCourseMapper;


@Repository
public class ClubRelClassCourseDao {

	@Autowired
	private ClubRelClassCourseMapper clubRelClassCourseMapper;
	
	
	public void insertClubRelClassCourse(ClubRelClassCourse clubRelClassCourse){
		clubRelClassCourseMapper .insertClubRelClassCourse(clubRelClassCourse);
	}
	
	public void deleteClubRelClassCourse(ClubRelClassCourse clubRelClassCourse){
		clubRelClassCourseMapper .deleteClubRelClassCourse(clubRelClassCourse);
	}
	
	public void updateClubRelClassCourseByKey(ClubRelClassCourse clubRelClassCourse){
		clubRelClassCourseMapper .updateClubRelClassCourseByKey(clubRelClassCourse);
	}
	
	public List<ClubRelClassCourse> selectSingleClubRelClassCourse(ClubRelClassCourse clubRelClassCourse){
		return clubRelClassCourseMapper .selectSingleClubRelClassCourse(clubRelClassCourse);
	}
	
	public ClubRelClassCourse selectClubRelClassCourse(ClubRelClassCourse clubRelClassCourse){
		List<ClubRelClassCourse> clubRelClassCourseList = clubRelClassCourseMapper .selectSingleClubRelClassCourse(clubRelClassCourse);
		if(clubRelClassCourseList == null || clubRelClassCourseList .size() == 0){
			return null;
		}
		return clubRelClassCourseList .get(0);
	}
	
	public List<ClubRelClassCourse> selectAllClubRelClassCourse(){
		return clubRelClassCourseMapper .selectAllClubRelClassCourse();
	}
	
	public void insertClubRelClassCourses(List<ClubRelClassCourse> ClubRelClassCourses){
		clubRelClassCourseMapper.insertClubRelClassCourses(ClubRelClassCourses);
	}

	public Integer getClubCourseByClassIdCount(Map<String, Object> paramMap) {
		return clubRelClassCourseMapper.getClubCourseByClassIdCount(paramMap);
	}
	
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return clubRelClassCourseMapper.queryCount(paramMap);
	}

	public List<ClubRelClassCourse> queryByPage(Map<String, Object> paramMap) {
		return clubRelClassCourseMapper.queryByPage(paramMap);
	}
	
	
	
}