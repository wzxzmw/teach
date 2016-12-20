package com.seentao.stpedu.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachPlanSignDao;
import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;

@Service
public class TeachPlanSignService{
	
	@Autowired
	private TeachPlanSignDao teachPlanSignDao;
	
	public List<TeachPlanSign> getTeachPlanSign(TeachPlanSign teachPlanSign) {
		List<TeachPlanSign> teachPlanSignList = teachPlanSignDao .selectSingleTeachPlanSign(teachPlanSign);
		if(teachPlanSignList == null || teachPlanSignList .size() <= 0){
			return null;
		}
		return teachPlanSignList;
	}
	
	public TeachPlanSign getTeachPlanSigns(TeachPlanSign teachPlanSign) {
		List<TeachPlanSign> teachPlanSignList = teachPlanSignDao .selectSingleTeachPlanSign(teachPlanSign);
		if(teachPlanSignList == null || teachPlanSignList .size() <= 0){
			return null;
		}
		return teachPlanSignList.get(0);
	}
	/**
	 * updateTeachPlanSignByKey(修改)   
	 * @author ligs
	 * @date 2016年6月27日 下午9:03:15
	 * @return
	 */
	public void updateTeachPlanSignByKey(TeachPlanSign teachPlanSign){
		teachPlanSignDao.updateTeachPlanSignByKey(teachPlanSign);
	}
	/**
	 * insertTeachPlanSign(添加)   
	 * @author ligs
	 * @date 2016年6月29日 上午10:12:46
	 * @return
	 */
	public void insertTeachPlanSign(TeachPlanSign teachPlanSign){
		teachPlanSignDao.insertTeachPlanSign(teachPlanSign);
	}

	public List<TeachPlanSign> getTeachPlanSignList(TeachPlanSign teachPlanSign) {
		return teachPlanSignDao.getTeachPlanSignList( teachPlanSign);
	}

	public Integer getTeachPlanSignCount(TeachPlanSign teachPlanSign) {
		return teachPlanSignDao.getTeachPlanSignCount( teachPlanSign);
	}

	public void insertTeachPlanSignAlls(List<TeachPlanSign> teachStudentClassAll) {
		teachPlanSignDao.insertTeachPlanSignAlls(teachStudentClassAll);
	}
}