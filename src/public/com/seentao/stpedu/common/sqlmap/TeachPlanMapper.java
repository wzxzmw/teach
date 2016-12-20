package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachPlan;
import java.util.List;
import java.util.Map;

public interface TeachPlanMapper {

	public abstract void insertTeachPlan(TeachPlan teachPlan);
	
	public abstract void deleteTeachPlan(TeachPlan teachPlan);
	
	public abstract void updateTeachPlanByKey(TeachPlan teachPlan);
	
	public abstract List<TeachPlan> selectSingleTeachPlan(TeachPlan teachPlan);
	
	public abstract List<TeachPlan> selectAllTeachPlan();

	public abstract Integer queryCount(TeachPlan t1);

	public abstract Integer queryHaveCount(TeachPlan t1);

	public abstract Integer insertTeachPlanReturn(TeachPlan addTeachPlan);

	public abstract List<TeachPlan> mapUpdateTeachPlan(Map<String, Object> tasks);

	public abstract void delTeachPlanAll(List<TeachPlan> delTeachPlan);
	
}