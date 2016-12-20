package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.sqlmap.TeachRelCardCourseMapper;


@Repository
public class TeachRelCardCourseDao {

	@Autowired
	private TeachRelCardCourseMapper teachRelCardCourseMapper;
	
	
	public void insertTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse){
		teachRelCardCourseMapper .insertTeachRelCardCourse(teachRelCardCourse);
	}
	
	public void deleteTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse){
		teachRelCardCourseMapper .deleteTeachRelCardCourse(teachRelCardCourse);
	}
	
	public void updateTeachRelCardCourseByKey(TeachRelCardCourse teachRelCardCourse){
		teachRelCardCourseMapper .updateTeachRelCardCourseByKey(teachRelCardCourse);
	}
	
	public List<TeachRelCardCourse> selectSingleTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse){
		return teachRelCardCourseMapper .selectSingleTeachRelCardCourse(teachRelCardCourse);
	}
	
	public TeachRelCardCourse selectTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse){
		List<TeachRelCardCourse> teachRelCardCourseList = teachRelCardCourseMapper .selectSingleTeachRelCardCourse(teachRelCardCourse);
		if(teachRelCardCourseList == null || teachRelCardCourseList .size() == 0){
			return null;
		}
		return teachRelCardCourseList .get(0);
	}
	
	public List<TeachRelCardCourse> selectAllTeachRelCardCourse(){
		return teachRelCardCourseMapper .selectAllTeachRelCardCourse();
	}

	public Integer queryCount(Map<String, Object> paramMap) {
		return teachRelCardCourseMapper.queryCount(paramMap);
	}

	public  List<TeachRelCardCourse> queryByPage(Map<String, Object> paramMap) {
		return teachRelCardCourseMapper.queryByPage(paramMap);
	}
	
	public List<Integer> queryByPageids(Map<String, String> paramMap) {
		return teachRelCardCourseMapper.queryByPageids(paramMap);
	}
	
	
	
	
	
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public TeachRelCardCourse getEntityForDB(Map<String, Object> paramMap){
		TeachRelCardCourse tmp = new TeachRelCardCourse();
		tmp.setRelId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectTeachRelCardCourse(tmp);
	}

	/**
	 * 课程应交数量加一
	 * @param courseId
	 * @author 			lw
	 * @date			2016年6月30日  上午10:02:08
	 */
	public void addActualnum(int courseId) {
		teachRelCardCourseMapper.addActualnum(courseId);
	}

	public TeachRelCardCourse getLastTeachRelCardCourseByCardId(TeachRelCardCourse teachRelCardCourse) {
		List<TeachRelCardCourse> teachRelCardCourseList = teachRelCardCourseMapper .getLastTeachRelCardCourseByCardId(teachRelCardCourse);
		if(teachRelCardCourseList == null || teachRelCardCourseList .size() == 0){
			return null;
		}
		return teachRelCardCourseList .get(0);
	}

	public Integer getStudyNumBycourseCardId(Integer cardId) {
		return teachRelCardCourseMapper.getStudyNumBycourseCardId(cardId);
	}

	public void updateTeachRelCardCoursePrivate(TeachRelCardCourse teachRelCardCourse) {
		teachRelCardCourseMapper.updateTeachRelCardCoursePrivate(teachRelCardCourse);
	}

	public void insertTeachRelCardCourseAlls(List<TeachRelCardCourse> list) {
		teachRelCardCourseMapper.insertTeachRelCardCourseAlls(list);
	}

	public void updateTeachRelCardCoursePrivateAlls(String ids) {
		teachRelCardCourseMapper.updateTeachRelCardCoursePrivateAlls(ids);
	}

	public void updateTeachRelCardCoursePrivateMinus(String ids) {
		teachRelCardCourseMapper.updateTeachRelCardCoursePrivateMinus(ids);
	}

	public TeachRelCardCourse getSingleTeachRelCardCourse(TeachRelCardCourse teachrelcardcourses) {
		return teachRelCardCourseMapper.getSingleTeachRelCardCourse(teachrelcardcourses);
	}
	
}