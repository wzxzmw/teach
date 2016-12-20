package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachCourseChapter;
import java.util.List;

public interface TeachCourseChapterMapper {

	public abstract void insertTeachCourseChapter(TeachCourseChapter teachCourseChapter);
	
	public abstract void deleteTeachCourseChapter(TeachCourseChapter teachCourseChapter);
	
	public abstract void updateTeachCourseChapterByKey(TeachCourseChapter teachCourseChapter);
	
	public abstract List<TeachCourseChapter> selectSingleTeachCourseChapter(TeachCourseChapter teachCourseChapter);
	
	public abstract List<TeachCourseChapter> selectAllTeachCourseChapter();

	public abstract void insertTeachCourseChapterId(TeachCourseChapter teachCourseChapteradd);

	public abstract List<TeachCourseChapter> getLastTeachCourseChapterByClassId(TeachCourseChapter teachCourseChapter);
	
	public abstract List<Integer> selectAllChapetIdByClass(Integer classId);

	/** 
	* 获取章节信息，和matchId和matchYear
	* @author W.jx
	* @date 2016年7月26日 下午2:38:28 
	* @param teachCourseChapter
	* @return
	*/
	public abstract List<TeachCourseChapter> selectTeachCourseChapterAndYearAndMatchId(TeachCourseChapter teachCourseChapter);
	
	public abstract void insertTeachCourseChapterIdAll(List<TeachCourseChapter> teachCouChapterList);
}