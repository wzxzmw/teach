package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;
import com.seentao.stpedu.common.sqlmap.TeachPlanSignMapper;


@Repository
public class TeachPlanSignDao {

	@Autowired
	private TeachPlanSignMapper teachPlanSignMapper;
	
	
	public void insertTeachPlanSign(TeachPlanSign teachPlanSign){
		teachPlanSignMapper .insertTeachPlanSign(teachPlanSign);
	}
	
	public void deleteTeachPlanSign(TeachPlanSign teachPlanSign){
		teachPlanSignMapper .deleteTeachPlanSign(teachPlanSign);
	}
	
	public void updateTeachPlanSignByKey(TeachPlanSign teachPlanSign){
		teachPlanSignMapper .updateTeachPlanSignByKey(teachPlanSign);
	}
	
	public List<TeachPlanSign> selectSingleTeachPlanSign(TeachPlanSign teachPlanSign){
		return teachPlanSignMapper .selectSingleTeachPlanSign(teachPlanSign);
	}
	
	public TeachPlanSign selectTeachPlanSign(TeachPlanSign teachPlanSign){
		List<TeachPlanSign> teachPlanSignList = teachPlanSignMapper .selectSingleTeachPlanSign(teachPlanSign);
		if(teachPlanSignList == null || teachPlanSignList .size() == 0){
			return null;
		}
		return teachPlanSignList .get(0);
	}
	
	public List<TeachPlanSign> selectAllTeachPlanSign(){
		return teachPlanSignMapper .selectAllTeachPlanSign();
	}

	public List<TeachPlanSign> getTeachPlanSignList(TeachPlanSign teachPlanSign) {
		return teachPlanSignMapper .getTeachPlanSignList(teachPlanSign);
	}

	public Integer getTeachPlanSignCount(TeachPlanSign teachPlanSign) {
		return teachPlanSignMapper .getTeachPlanSignCount(teachPlanSign);
	}

	public void insertTeachPlanSignAlls(List<TeachPlanSign> teachStudentClassAll) {
		teachPlanSignMapper.insertTeachPlanSignAlls(teachStudentClassAll);
	}

}