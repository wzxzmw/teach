package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachClassDao;
import com.seentao.stpedu.common.entity.TeachClass;
/**
 *teach_class 班级表基本操作
 */
@Service
public class TeachClassService{
	
	@Autowired
	private TeachClassDao teachClassDao;
	
	
	public TeachClass getTeachClass(TeachClass teachClass) {
		List<TeachClass> teachClassList = teachClassDao .selectSingleTeachClass(teachClass);
		if(teachClassList == null || teachClassList .size() <= 0){
			return null;
		}else{
			return teachClassList.get(0);
		}
	}
	
	public List<TeachClass> getTeachClassList(TeachClass teachClass) {
		List<TeachClass> teachClassList = teachClassDao .selectSingleTeachClassGroup(teachClass);
		if(teachClassList == null || teachClassList .size() <= 0){
			return null;
		}else{
			return teachClassList;
		}
	}
	
	public TeachClass getTeachClassById(Integer id) {
		TeachClass teachClass = new TeachClass();
		teachClass.setClassId(id);
		List<TeachClass> teachClassList = teachClassDao .selectSingleTeachClass(teachClass);
		if(teachClassList == null || teachClassList .size() <= 0){
			return null;
		}else{
			return teachClassList.get(0);
		}
	}
	
	/**
	 * selectSingleTeachClass(查询班级表)   
	 * @author ligs
	 * @date 2016年6月21日 上午11:16:14
	 * @return
	 */
	public TeachClass selectSingleTeachClass(TeachClass teachClass){
		List<TeachClass> teachClassList= teachClassDao .selectSingleTeachClass(teachClass);
		if(teachClassList == null || teachClassList .size() == 0){
			return null;
		}
		return teachClassList .get(0);
	}
	/**
	 * insertTeachClass(提交班级信息)   
	 * @author ligs
	 * @date 2016年6月22日 上午10:57:14
	 * @return
	 */
	public Integer insertTeachClass(TeachClass teachClass)throws Exception{
		return teachClassDao.insertTeachClass(teachClass);
	}
	/**
	 * updateTeachClassByKey(修改班级信息)   
	 * @author ligs
	 * @date 2016年6月22日 上午10:58:39
	 * @return
	 */
	public void updateTeachClassByKey(TeachClass teachClass)throws Exception{
		teachClassDao.updateTeachClassByKey(teachClass);
	}
	
	public List<Integer> getAllClassId(){
		return teachClassDao.selectAllClassId(0);
	}

	public void updateOriginalCourseNum(TeachClass teachClass) {
		teachClassDao.updateOriginalCourseNum(teachClass);
	}
}