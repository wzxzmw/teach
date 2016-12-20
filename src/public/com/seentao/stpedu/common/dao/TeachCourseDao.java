package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.sqlmap.TeachCourseMapper;


@Repository
public class TeachCourseDao{

	@Autowired
	private TeachCourseMapper teachCourseMapper;
	
	
	public void insertTeachCourse(TeachCourse teachCourse){
		teachCourseMapper .insertTeachCourse(teachCourse);
	}
	
	public void deleteTeachCourse(TeachCourse teachCourse){
		teachCourseMapper .deleteTeachCourse(teachCourse);
	}
	
	public void updateTeachCourseByKey(TeachCourse teachCourse){
		teachCourseMapper .updateTeachCourseByKey(teachCourse);
	}
	
	public List<TeachCourse> selectSingleTeachCourse(TeachCourse teachCourse){
		return teachCourseMapper .selectSingleTeachCourse(teachCourse);
	}
	
	public TeachCourse selectTeachCourse(TeachCourse teachCourse){
		List<TeachCourse> teachCourseList = teachCourseMapper .selectSingleTeachCourse(teachCourse);
		if(teachCourseList == null || teachCourseList .size() == 0){
			return null;
		}
		return teachCourseList .get(0);
	}
	
	public List<TeachCourse> selectAllTeachCourse(){
		return teachCourseMapper .selectAllTeachCourse();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return teachCourseMapper.queryCount(paramMap);
	}

	public  List<TeachCourse> queryByPage(Map<String, Object> paramMap) {
		return (List<TeachCourse>) teachCourseMapper.queryByPage(paramMap);
	}
	
	
	/**
	 * redis组件 反射调用获取单表数据
	 * @param queryParam	查询参数
	 * @return
	 * @author 				lw
	 * @date				2016年6月21日  下午7:24:07
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		TeachCourse tmp = new TeachCourse();
		tmp.setCourseId(Integer.valueOf(queryParam.get("id_key").toString()));
		if(queryParam.get("studentId") !=null ){
			tmp.setStudentId(Integer.valueOf(queryParam.get("studentId").toString()));
		}
		return selectTeachCourse(tmp);
	}

	public void delTeachCourseAll(List<TeachCourse> delTeachCourse) {
		teachCourseMapper.delTeachCourseAll(delTeachCourse);
	}

	
	public List<TeachCourse> selectAllCourse(Integer classId){
		return teachCourseMapper.selectAllCourse(classId);
	}
	

}