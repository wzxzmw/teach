package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.sqlmap.TeachClassMapper;


@Repository
public class TeachClassDao {

	@Autowired
	private TeachClassMapper teachClassMapper;
	
	
	public Integer insertTeachClass(TeachClass teachClass)throws Exception{
		return 	teachClassMapper .insertTeachClass(teachClass);
	}
	
	public void deleteTeachClass(TeachClass teachClass){
		teachClassMapper .deleteTeachClass(teachClass);
	}
	
	public void updateTeachClassByKey(TeachClass teachClass)throws Exception{
		teachClassMapper .updateTeachClassByKey(teachClass);
	}
	
	public List<TeachClass> selectSingleTeachClass(TeachClass teachClass){
		return teachClassMapper .selectSingleTeachClass(teachClass);
	}
	
	public TeachClass selectTeachClass(TeachClass teachClass){
		List<TeachClass> teachClassList = teachClassMapper .selectSingleTeachClass(teachClass);
		if(teachClassList == null || teachClassList .size() == 0){
			return null;
		}
		return teachClassList .get(0);
	}
	
	public List<TeachClass> selectAllTeachClass(){
		return teachClassMapper .selectAllTeachClass();
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return teachClassMapper.queryCount(paramMap);
	}

	public List<TeachClass> queryByPage(Map<String, Object> paramMap) {
		return teachClassMapper.queryByPage(paramMap);
	}
	
	public Integer queryCountSchool(Map<String, Object> paramMap) {
		return teachClassMapper.queryCountSchool(paramMap);
	}

	public List<TeachClass> queryByPageSchool(Map<String, Object> paramMap) {
		return teachClassMapper.queryByPageSchool(paramMap);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public TeachClass getEntityForDB(Map<String, Object> paramMap){
		TeachClass tmp = new TeachClass();
		tmp.setClassId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectTeachClass(tmp);
	}

	public List<TeachClass> selectSingleTeachClassGroup(TeachClass teachClass) {
		return teachClassMapper.selectSingleTeachClassGroup(teachClass);
	}
	
	public List<Integer> selectAllClassId(Integer isDelete){
		return teachClassMapper.selectAllClassId(isDelete);
	}

	public void updateOriginalCourseNum(TeachClass teachClass) {
		teachClassMapper.updateOriginalCourseNum(teachClass);
	}
}