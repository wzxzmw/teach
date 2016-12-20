package com.seentao.stpedu.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.ClubTrainingCourseDao;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;

@Service
public class ClubTrainingCourseService{
	
	@Autowired
	private ClubTrainingCourseDao clubTrainingCourseDao;
	
	public ClubTrainingCourse getClubTrainingCourse(ClubTrainingCourse clubTrainingCourse) {
		List<ClubTrainingCourse> clubTrainingCourseList = clubTrainingCourseDao .selectSingleClubTrainingCourse(clubTrainingCourse);
		if(clubTrainingCourseList == null || clubTrainingCourseList .size() <= 0){
			return null;
		}
		
		return clubTrainingCourseList.get(0);
	}
	
	public List<ClubTrainingCourse> getClubTrainingCourseList(ClubTrainingCourse clubTrainingCourse) {
		List<ClubTrainingCourse> clubTrainingCourseList = clubTrainingCourseDao .selectSingleClubTrainingCourse(clubTrainingCourse);
		if(clubTrainingCourseList == null || clubTrainingCourseList .size() <= 0){
			return null;
		}
		
		return clubTrainingCourseList;
	}


	public void insertClubTrainingCourse(ClubTrainingCourse clubCourse) {
		clubTrainingCourseDao.insertClubTrainingCourse(clubCourse);
	}

	public void updateClubTrainingCourse(ClubTrainingCourse clubCourse) {
		clubTrainingCourseDao.updateClubTrainingCourseByKey(clubCourse);
	}

	public Integer getClubTrainingCourseCount(Map<String, Object> paramMap) {
		return clubTrainingCourseDao. getClubTrainingCourseCount( paramMap);
	}

	public void delClubTrainingCourseAll(List<ClubTrainingCourse> delClubTrainingCourse) {
		clubTrainingCourseDao.delClubTrainingCourseAll(delClubTrainingCourse);
	}
	
	/**
	 * 根据课程类型获取官方课程类型
	 * @return
	 * TODO:redis
	 */
	public List<Integer> getClassByCourseType(int classType){
		return clubTrainingCourseDao.getClassByCourseType(classType);
	}
}