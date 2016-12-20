package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.TeachCourseDiscussDao;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;

@Service
public class TeachCourseDiscussService{
	
	@Autowired
	private TeachCourseDiscussDao teachCourseDiscussDao;
	
	public TeachCourseDiscuss getTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss) {
		List<TeachCourseDiscuss> teachCourseDiscussList = teachCourseDiscussDao .selectSingleTeachCourseDiscuss(teachCourseDiscuss);
		if(teachCourseDiscussList == null || teachCourseDiscussList .size() <= 0){
			return null;
		}
		return teachCourseDiscussList.get(0);
	}
	
	public void updateTeachCourseDiscussByKey(TeachCourseDiscuss teachCourseDiscuss){
		teachCourseDiscussDao.updateTeachCourseDiscussByKey(teachCourseDiscuss);
	}
	
	public void insertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss)throws Exception{
		teachCourseDiscussDao.insertTeachCourseDiscuss(teachCourseDiscuss);
	}
	
	public Integer returninsertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss){
		return teachCourseDiscussDao.returninsertTeachCourseDiscuss(teachCourseDiscuss);
	}

	public void delTeachCourseDiscussAll(List<TeachCourseDiscuss> delTeachCourseDiscuss) {
		teachCourseDiscussDao.delTeachCourseDiscussAll(delTeachCourseDiscuss);
	}

	public TeachCourseDiscuss getNewTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss) {
		List<TeachCourseDiscuss> teachCourseDiscussList = teachCourseDiscussDao .getNewTeachCourseDiscuss(teachCourseDiscuss);
		if(teachCourseDiscussList == null || teachCourseDiscussList .size() <= 0){
			return null;
		}
		return teachCourseDiscussList.get(0);
	}
	/*
	 * 最后一条的课程通知ID
	 */
	public Integer getNewTeachCourseDiscussMax(Integer classId){
		return teachCourseDiscussDao.getNewTeachCourseDiscussMax(classId);
	}
}