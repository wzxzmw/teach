package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachCourseChapterDao;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.utils.LogUtil;
/**
 *teach_course_chapter 课程章节表 
 */
@Service
public class TeachCourseChapterService{
	
	@Autowired
	private TeachCourseChapterDao teachCourseChapterDao;
	
	/**
	 * 根据班级id获取章节信息
	 * @param calssId 课程id
	 * @param type 类型：1、PC端  2、手机端
	 */
	public List<TeachCourseChapter> getChapter(TeachCourseChapter teachCourseChapter) {
		List<TeachCourseChapter> teachCourseChapterList = teachCourseChapterDao .selectSingleTeachCourseChapter(teachCourseChapter);
		if(teachCourseChapterList == null || teachCourseChapterList .size() <= 0){
			return null;
		}	
		return teachCourseChapterList;
	}
	
	/**
	 * 根据班级id获取章节信息
	 * @param calssId 课程id
	 * @param type 类型：1、PC端  2、手机端
	 */
	public List<TeachCourseChapter> getChapterForMobile(TeachCourseChapter teachCourseChapter) {
		List<TeachCourseChapter> teachCourseChapterList = teachCourseChapterDao .selectTeachCourseChapterAndYearAndMatchId(teachCourseChapter);
		if(teachCourseChapterList == null || teachCourseChapterList .size() <= 0){
			return null;
		}	
		return teachCourseChapterList;
	}
	
	/**
	 * 根据班级id获取章节信息
	 * @param calssId
	 */
	public TeachCourseChapter getChapterByChapterId(TeachCourseChapter t) {
		TeachCourseChapter teachCourseChapter = teachCourseChapterDao.selectTeachCourseChapter(t);
		return teachCourseChapter;
	}

	/**
	 * 创建班级时添加章节信息
	 */
	public void insertTeachCourseChapterId(TeachCourseChapter teachCourseChapteradd) {
		teachCourseChapterDao.insertTeachCourseChapterId(teachCourseChapteradd);
	}

	/** 
	* @Title: getLastTeachCourseChapterByClassId 
	* @Description: 获取班级最后发布的课程章节
	* @param classId	班级ID
	* @return TeachCourseChapter
	* @author liulin
	* @date 2016年7月21日 上午10:38:41
	*/
	public TeachCourseChapter getLastTeachCourseChapterByClassId(Integer classId) {
		TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
		teachCourseChapter.setClassId(classId);
		return teachCourseChapterDao.getLastTeachCourseChapterByClassId(teachCourseChapter);
	}
	
	public List<Integer> getAllChapetIdByClass(Integer classId){
		return teachCourseChapterDao.selectAllChapetIdByClass(classId);
	}

	public void insertTeachCourseChapterIdAll(List<TeachCourseChapter> teachCouChapterList) {
		if(teachCouChapterList.size()>0 && teachCouChapterList!=null){
			teachCourseChapterDao.insertTeachCourseChapterIdAll(teachCouChapterList);
		}else{
			LogUtil.info(this.getClass(), "insertTeachCourseChapterIdAll", "批量插入失败");
		}
	}
}