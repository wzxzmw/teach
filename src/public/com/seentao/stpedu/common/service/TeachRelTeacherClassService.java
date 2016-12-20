package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachRelTeacherClassDao;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;

@Service
public class TeachRelTeacherClassService{
	
	@Autowired
	private TeachRelTeacherClassDao teachRelTeacherClassDao;
	
	/**
	 * 根据用户id查询单条信息
	 * @param userId
	 * @return
	 */
	public List<TeachRelTeacherClass> getTeachRelTeacherClassByUserId(Integer userId) {
		TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
		teachRelTeacherClass.setTeacherId(userId);
		List<TeachRelTeacherClass> teachRelTeacherClassList = teachRelTeacherClassDao .selectSingleTeachRelTeacherClass(teachRelTeacherClass);
		if(teachRelTeacherClassList == null || teachRelTeacherClassList .size() <= 0){
			return null;
		}
		return teachRelTeacherClassList;
	}
	/**
	 * 查询班级信息
	 * @param userId
	 * @return
	 */
	public TeachRelTeacherClass getTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass) {
		List<TeachRelTeacherClass> teachRelTeacherClassList = teachRelTeacherClassDao .selectSingleTeachRelTeacherClass(teachRelTeacherClass);
		if(teachRelTeacherClassList == null || teachRelTeacherClassList .size() <= 0){
			return null;
		}
		return teachRelTeacherClassList.get(0);
	}
	
	/**
	 * 教师所在班级与参数班级是否一致
	 * @param userId
	 * @param classId
	 * @return
	 */
	public boolean isEqualClass(Integer userId ,Integer classId){
		List<Integer> listIds = new ArrayList<Integer>();
		List<TeachRelTeacherClass> teachrelteacherclass = this.getTeachRelTeacherClassByUserId(userId);
		if(null!=teachrelteacherclass && teachrelteacherclass.size()!=0){
			for (TeachRelTeacherClass teachRelTeacherClass2 : teachrelteacherclass) {
				listIds.add(teachRelTeacherClass2.getClassId());
			}
		}else{
			return false;
		}
		if(listIds.contains(classId)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * insertTeachRelTeacherClass(添加默认班级)   
	 * @author ligs
	 * @date 2016年6月22日 下午2:11:07
	 * @return
	 */
	public void insertTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassDao.insertTeachRelTeacherClass(teachRelTeacherClass);
	}
	
	/**
	 * deleteTeachRelTeacherClass(清空默认班级)   
	 * @author ligs
	 * @date 2016年6月22日 下午2:20:25
	 * @return
	 */
	public void updateTeachRelTeacherClassByKey(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassDao.updateTeachRelTeacherClassByKey(teachRelTeacherClass);
	}
	
	
	
	/**
	 * 根据教师 和 班级id 校验是否是该班级的教师 
	 * @param teacherId
	 * @param classId
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午9:16:23
	 */
	public TeachRelTeacherClass isTeacherClass(Integer teacherId, Integer classId) {
		
		TeachRelTeacherClass select = new TeachRelTeacherClass();
		select.setClassId(classId);
		select.setTeacherId(teacherId);
		return teachRelTeacherClassDao.selectTeachRelTeacherClass(select);
	}
	/**
	 * deleteTeachRelTeacherClass(删除教师班级关系表)   
	 * @author ligs
	 * @date 2016年7月4日 下午4:23:34
	 * @return
	 */
	public void deleteTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass){
		teachRelTeacherClassDao.deleteTeachRelTeacherClass(teachRelTeacherClass);
	}
	public String getIdsByUserId(Integer userId) {
		String classIds = "";
		List<TeachRelTeacherClass> teachClasses = this.getTeachRelTeacherClassByUserId(userId);
		if(null != teachClasses){
			for(TeachRelTeacherClass teachClass : teachClasses){
				int id = teachClass.getClassId();
				classIds = classIds + id +",";
			}
			classIds = classIds.substring(0, classIds.length()-1);
		}
		return classIds;
	}
}