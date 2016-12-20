package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.TeachRelCardCourse;

public interface TeachRelCardCourseMapper {

	public abstract void insertTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse);
	
	public abstract void deleteTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse);
	
	public abstract void updateTeachRelCardCourseByKey(TeachRelCardCourse teachRelCardCourse);
	
	public abstract List<TeachRelCardCourse> selectSingleTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse);
	
	public abstract List<TeachRelCardCourse> selectAllTeachRelCardCourse();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract void addActualnum(int courseId);

	public abstract List<String> findAllTeachCourseByCardId(Integer courseCardId);

	public abstract List<TeachRelCardCourse> queryByPage(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageids(Map<String, String> paramMap);

	public abstract List<TeachRelCardCourse> getLastTeachRelCardCourseByCardId(TeachRelCardCourse teachRelCardCourse);

	public abstract Integer getStudyNumBycourseCardId(@Param(value="cardId")Integer cardId);

	public abstract void updateTeachRelCardCoursePrivate(TeachRelCardCourse teachRelCardCourse);

	public abstract void insertTeachRelCardCourseAlls(List<TeachRelCardCourse> list);

	public abstract void updateTeachRelCardCoursePrivateAlls(String ids);

	public abstract void updateTeachRelCardCoursePrivateMinus(String ids);

	public abstract TeachRelCardCourse getSingleTeachRelCardCourse(TeachRelCardCourse teachrelcardcourses);
	
}