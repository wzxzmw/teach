package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachCourseCardCountDao;
import com.seentao.stpedu.common.entity.TeachCourseCardCount;

@Service
public class TeachCourseCardCountService{
	
	@Autowired
	private TeachCourseCardCountDao teachCourseCardCountDao;
	
	public List<TeachCourseCardCount> getTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount) {
		List<TeachCourseCardCount> teachCourseCardCountList = teachCourseCardCountDao .selectSingleTeachCourseCardCount(teachCourseCardCount);
		if(teachCourseCardCountList == null || teachCourseCardCountList .size() <= 0){
			return null;
		}
		return teachCourseCardCountList;
	}
	
	public TeachCourseCardCount getTeachCourseCardCountOne(TeachCourseCardCount teachCourseCardCount) {
		List<TeachCourseCardCount> teachCourseCardCountList = teachCourseCardCountDao .selectSingleTeachCourseCardCount(teachCourseCardCount);
		if(teachCourseCardCountList == null || teachCourseCardCountList .size() <= 0){
			return null;
		}
		return teachCourseCardCountList.get(0);
	}
	
	/**
	 * 新增课程统计卡
	 * @param teachCourseCardCount
	 * @author chengshx
	 */
	public void addTeachCourseCardCount(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountDao.insertTeachCourseCardCount(teachCourseCardCount);
	}
	
	/**
	 * 根据班级ID修改课程卡统计表
	 * @param teachCourseCardCount
	 * @author chengshx
	 */
	public void updateTeachCourseCountByClassId(TeachCourseCardCount teachCourseCardCount){
		teachCourseCardCountDao.updateTeachCourseCountByClassId(teachCourseCardCount);
	}
	
	/**
	 * 根据班级ID查询课程卡统计信息
	 * @param classId 班级ID
	 * @return
	 * @author chengshx
	 */
	public TeachCourseCardCount getTeachCourseCardCountByClassId(Integer classId){
		TeachCourseCardCount teachCourseCardCount = new TeachCourseCardCount();
		teachCourseCardCount.setClassId(classId);
		return teachCourseCardCountDao.selectTeachCourseCardCount(teachCourseCardCount);
	}
}