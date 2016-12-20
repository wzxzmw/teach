package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachSchoolDao;
import com.seentao.stpedu.common.entity.TeachSchool;
/**
 * teach_school 学校表基本操作
 */
@Service
public class TeachSchoolService{
	
	@Autowired
	private TeachSchoolDao teachSchoolDao;
	
	public TeachSchool getTeachSchool(Integer id) {
		TeachSchool teachSchool = new TeachSchool();
		teachSchool.setSchoolId(id);
		List<TeachSchool> teachSchoolList = teachSchoolDao .selectSingleTeachSchool(teachSchool);
		if(teachSchoolList == null || teachSchoolList .size() <= 0){
			return null;
		}else{
			return teachSchoolList.get(0);
		}
	}
	
	/**
	 * 
	 * selectSingleTeachSchool(查询教师所在学校)   
	 * @author ligs
	 * @date 2016年6月20日 上午10:39:56
	 * @return
	 */
	public TeachSchool selectSingleTeachSchool(TeachSchool teachSchool){
		List<TeachSchool> teachSchoolList = teachSchoolDao .selectSingleTeachSchool(teachSchool);
		if(teachSchoolList == null || teachSchoolList .size() == 0){
			return null;
		}
		return teachSchoolList .get(0);
	}
	
	/**
	 * 
	 * selectSingleTeachSchool(查询教师所在学校)   
	 * @author ligs
	 * @date 2016年6月20日 上午10:39:56
	 * @return
	 */
	public List<TeachSchool> selectSingleTeachSchoolAll(TeachSchool teachSchool){
		List<TeachSchool> teachSchoolList = teachSchoolDao .selectSingleTeachSchool(teachSchool);
		if(teachSchoolList == null || teachSchoolList .size() == 0){
			return null;
		}
		return teachSchoolList;
	}
}