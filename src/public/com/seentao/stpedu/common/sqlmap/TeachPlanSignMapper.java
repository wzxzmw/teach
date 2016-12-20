package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;
import java.util.List;
import java.util.Map;

public interface TeachPlanSignMapper {

	public abstract void insertTeachPlanSign(TeachPlanSign teachPlanSign);
	
	public abstract void deleteTeachPlanSign(TeachPlanSign teachPlanSign);
	
	public abstract void updateTeachPlanSignByKey(TeachPlanSign teachPlanSign);
	
	public abstract List<TeachPlanSign> selectSingleTeachPlanSign(TeachPlanSign teachPlanSign);
	
	public abstract List<TeachPlanSign> selectAllTeachPlanSign();

	public abstract List<TeachPlanSign> getTeachPlanSignList(TeachPlanSign teachPlanSign);

	public abstract Integer getTeachPlanSignCount(TeachPlanSign teachPlanSign);

	public abstract void insertTeachPlanSignAlls(List<TeachPlanSign> teachStudentClassAll);

	
}