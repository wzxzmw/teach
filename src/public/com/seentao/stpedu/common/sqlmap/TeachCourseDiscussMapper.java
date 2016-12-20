package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TeachCourseDiscussMapper {

	public abstract void insertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss)throws Exception;
	
	public abstract void deleteTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss);
	
	public abstract void updateTeachCourseDiscussByKey(TeachCourseDiscuss teachCourseDiscuss);
	
	public abstract List<TeachCourseDiscuss> selectSingleTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss);
	
	public abstract List<TeachCourseDiscuss> selectAllTeachCourseDiscuss();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachCourseDiscuss> queryByPage(Map<String, Object> paramMap);

	public abstract Integer returninsertTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss);

	public abstract void delTeachCourseDiscussAll(List<TeachCourseDiscuss> delTeachCourseDiscuss);

	public abstract List<TeachCourseDiscuss> getNewTeachCourseDiscuss(TeachCourseDiscuss teachCourseDiscuss);

	public abstract Integer queryNewestByCount(Map<String, Object> paramMap);

	public abstract List<TeachCourseDiscuss> queryNewestByPage(Map<String, Object> paramMap);

	public abstract Integer queryOldByCount(Map<String, Object> paramMap);

	public abstract List<TeachCourseDiscuss> queryOldByPage(Map<String, Object> paramMap);
	/*
	 *最后一条的课程通知ID
	 */
	public abstract Integer getNewTeachCourseDiscussMax(@Param("classId")Integer classId);
	
}