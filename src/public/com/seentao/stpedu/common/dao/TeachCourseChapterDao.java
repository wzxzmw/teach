package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.sqlmap.TeachCourseChapterMapper;


@Repository
public class TeachCourseChapterDao {

	@Autowired
	private TeachCourseChapterMapper teachCourseChapterMapper;
	
	
	public void insertTeachCourseChapter(TeachCourseChapter teachCourseChapter){
		teachCourseChapterMapper .insertTeachCourseChapter(teachCourseChapter);
	}
	
	public void deleteTeachCourseChapter(TeachCourseChapter teachCourseChapter){
		teachCourseChapterMapper .deleteTeachCourseChapter(teachCourseChapter);
	}
	
	public void updateTeachCourseChapterByKey(TeachCourseChapter teachCourseChapter){
		teachCourseChapterMapper .updateTeachCourseChapterByKey(teachCourseChapter);
	}
	
	public List<TeachCourseChapter> selectSingleTeachCourseChapter(TeachCourseChapter teachCourseChapter){
		return teachCourseChapterMapper .selectSingleTeachCourseChapter(teachCourseChapter);
	}
	
	public TeachCourseChapter selectTeachCourseChapter(TeachCourseChapter teachCourseChapter){
		List<TeachCourseChapter> teachCourseChapterList = teachCourseChapterMapper .selectSingleTeachCourseChapter(teachCourseChapter);
		if(teachCourseChapterList == null || teachCourseChapterList .size() == 0){
			return null;
		}
		return teachCourseChapterList .get(0);
	}
	
	public List<TeachCourseChapter> selectAllTeachCourseChapter(){
		return teachCourseChapterMapper .selectAllTeachCourseChapter();
	}

	
	/**
	 * redis组件 反射调用获取单表数据
	 * @param queryParam	查询参数
	 * @return
	 * @author 				lw
	 * @date				2016年6月21日  下午7:24:07
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		TeachCourseChapter tmp = new TeachCourseChapter();
		tmp.setChapterId(Integer.valueOf(queryParam.get("id_key").toString()));
		return this.selectTeachCourseChapter(tmp);
	}

	public void insertTeachCourseChapterId(TeachCourseChapter teachCourseChapteradd) {
		teachCourseChapterMapper.insertTeachCourseChapterId(teachCourseChapteradd);
	}

	public TeachCourseChapter getLastTeachCourseChapterByClassId(TeachCourseChapter teachCourseChapter) {
		List<TeachCourseChapter> teachCourseChapterList = teachCourseChapterMapper .getLastTeachCourseChapterByClassId(teachCourseChapter);
		if(teachCourseChapterList == null || teachCourseChapterList .size() == 0){
			return null;
		}
		return teachCourseChapterList .get(0);
	}
	
	public List<Integer> selectAllChapetIdByClass(Integer classId){
		return teachCourseChapterMapper.selectAllChapetIdByClass(classId);
	}
	
	public List<TeachCourseChapter> selectTeachCourseChapterAndYearAndMatchId(TeachCourseChapter teachCourseChapter){
		return teachCourseChapterMapper .selectTeachCourseChapterAndYearAndMatchId(teachCourseChapter);
	}

	public void insertTeachCourseChapterIdAll(List<TeachCourseChapter> teachCouChapterList) {
		teachCourseChapterMapper.insertTeachCourseChapterIdAll(teachCouChapterList);
	}
}