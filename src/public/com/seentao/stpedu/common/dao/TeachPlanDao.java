package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.sqlmap.TeachPlanMapper;


@Repository
public class TeachPlanDao {

	@Autowired
	private TeachPlanMapper teachPlanMapper;
	
	
	public void insertTeachPlan(TeachPlan teachPlan){
		teachPlanMapper .insertTeachPlan(teachPlan);
	}
	
	public void deleteTeachPlan(TeachPlan teachPlan){
		teachPlanMapper .deleteTeachPlan(teachPlan);
	}
	
	public void updateTeachPlanByKey(TeachPlan teachPlan){
		teachPlanMapper .updateTeachPlanByKey(teachPlan);
	}
	
	public List<TeachPlan> selectSingleTeachPlan(TeachPlan teachPlan){
		return teachPlanMapper .selectSingleTeachPlan(teachPlan);
	}
	
	public TeachPlan selectTeachPlan(TeachPlan teachPlan){
		List<TeachPlan> teachPlanList = teachPlanMapper .selectSingleTeachPlan(teachPlan);
		if(teachPlanList == null || teachPlanList .size() == 0){
			return null;
		}
		return teachPlanList .get(0);
	}
	
	public List<TeachPlan> selectAllTeachPlan(){
		return teachPlanMapper .selectAllTeachPlan();
	}

	public Integer queryCount(TeachPlan t1) {
		return teachPlanMapper.queryCount( t1);
	}

	public Integer queryHaveCount(TeachPlan t1) {
		return teachPlanMapper.queryHaveCount( t1);
	}

	public Integer insertTeachPlanReturn(TeachPlan addTeachPlan) {
		return teachPlanMapper.insertTeachPlanReturn(addTeachPlan);
	}

	public List<TeachPlan> mapUpdateTeachPlan(Map<String, Object> tasks) {
		return teachPlanMapper.mapUpdateTeachPlan(tasks);
	}

	public void delTeachPlanAll(List<TeachPlan> delTeachPlan) {
		teachPlanMapper.delTeachPlanAll(delTeachPlan);
	}
}