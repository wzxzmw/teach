package com.seentao.stpedu.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachPlanDao;
import com.seentao.stpedu.common.entity.TeachPlan;

@Service
public class TeachPlanService{
	
	@Autowired
	private TeachPlanDao teachPlanDao;
	
	public List<TeachPlan> getTeachPlan(TeachPlan teachPlan) {
		List<TeachPlan> teachPlanList = teachPlanDao .selectSingleTeachPlan(teachPlan);
		if(teachPlanList == null || teachPlanList .size() <= 0){
			return null;
		}
		
		return teachPlanList;
	}
	
	/**
	 * 查询班级下的开启签到计划的总数
	 * @param t1
	 * @return
	 */
	public Integer queryCount(TeachPlan t1) {
		return teachPlanDao.queryCount(t1);
	}
	
	/**
	 * 查询该用户在本班级签到的次数
	 * @param t1
	 * @return
	 */
	public Integer queryHaveCount(TeachPlan t1) {
		return teachPlanDao.queryHaveCount( t1);
	}
	/**
	 * selectTeachPlan(查询计划表)   
	 * @author ligs
	 * @date 2016年6月27日 下午9:21:29
	 * @return
	 */
	public TeachPlan selectTeachPlan(TeachPlan teachPlan){
	TeachPlan teachPlans = teachPlanDao.selectTeachPlan(teachPlan);
	return 	teachPlans;
	}
	
	/**
	 * 修改计划表
	 */
	public void updateTeachPlanByKey(TeachPlan teachPlan){
		teachPlanDao.updateTeachPlanByKey(teachPlan);
	}
	
	/**
	 * insertTeachPlanReturn(添加返回ID)   
	 * @author ligs
	 * @date 2016年6月29日 上午10:16:19
	 * @return
	 */
	public Integer insertTeachPlanReturn(TeachPlan addTeachPlan) {
		return teachPlanDao.insertTeachPlanReturn(addTeachPlan);
	}
	
	public void insertTeachPlan(TeachPlan teachPlan){
		teachPlanDao.insertTeachPlan(teachPlan);
	}

	public List<TeachPlan> mapUpdateTeachPlan(Map<String, Object> tasks) {
		return teachPlanDao.mapUpdateTeachPlan(tasks);
	}

	public void delTeachPlanAll(List<TeachPlan> delTeachPlan) {
		teachPlanDao.delTeachPlanAll(delTeachPlan);
	}
	
}