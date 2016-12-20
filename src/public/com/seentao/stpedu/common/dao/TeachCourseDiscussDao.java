package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.sqlmap.TeachCourseDiscussMapper;


@Repository
public class TeachCourseDiscussDao {

	@Autowired
	private TeachCourseDiscussMapper teachCourseDiscussMapper;
	
	
	public void insertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss)throws Exception{
		teachCourseDiscussMapper .insertTeachCourseDiscuss(teachCourseDiscuss);
	}
	
	public void deleteTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss){
		teachCourseDiscussMapper .deleteTeachCourseDiscuss(teachCourseDiscuss);
	}
	
	public void updateTeachCourseDiscussByKey(TeachCourseDiscuss teachCourseDiscuss){
		teachCourseDiscussMapper .updateTeachCourseDiscussByKey(teachCourseDiscuss);
	}
	
	public List<TeachCourseDiscuss> selectSingleTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss){
		return teachCourseDiscussMapper .selectSingleTeachCourseDiscuss(teachCourseDiscuss);
	}
	
	public TeachCourseDiscuss selectTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss){
		List<TeachCourseDiscuss> teachCourseDiscussList = teachCourseDiscussMapper .selectSingleTeachCourseDiscuss(teachCourseDiscuss);
		if(teachCourseDiscussList == null || teachCourseDiscussList .size() == 0){
			return null;
		}
		return teachCourseDiscussList .get(0);
	}
	
	public List<TeachCourseDiscuss> selectAllTeachCourseDiscuss(){
		return teachCourseDiscussMapper .selectAllTeachCourseDiscuss();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryCount(paramMap);
	}
	public List<TeachCourseDiscuss> queryByPage(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryByPage(paramMap);
	}
	
	public Integer queryNewestByCount(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryNewestByCount(paramMap);
	}
	public List<TeachCourseDiscuss> queryNewestByPage(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryNewestByPage(paramMap);
	}
	
	public Integer queryOldByCount(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryOldByCount(paramMap);
	}
	public List<TeachCourseDiscuss> queryOldByPage(Map<String, Object> paramMap) {
		return teachCourseDiscussMapper.queryOldByPage(paramMap);
	}

	public Integer returninsertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss) {
		return teachCourseDiscussMapper.returninsertTeachCourseDiscuss(teachCourseDiscuss);
	}

	public void delTeachCourseDiscussAll(List<TeachCourseDiscuss> delTeachCourseDiscuss) {
		teachCourseDiscussMapper.delTeachCourseDiscussAll(delTeachCourseDiscuss);
	}
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public TeachCourseDiscuss getEntityForDB(Map<String, Object> paramMap){
		TeachCourseDiscuss tmp = new TeachCourseDiscuss();
		tmp.setDiscussId(Integer.valueOf(paramMap.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectTeachCourseDiscuss(tmp);
	}

	public List<TeachCourseDiscuss> getNewTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss) {
		return teachCourseDiscussMapper.getNewTeachCourseDiscuss(teachCourseDiscuss);
	}
	/*
	 *最后一条的课程通知ID
	 */
	public Integer getNewTeachCourseDiscussMax(Integer classId){
		return teachCourseDiscussMapper.getNewTeachCourseDiscussMax(classId); 
	}
}