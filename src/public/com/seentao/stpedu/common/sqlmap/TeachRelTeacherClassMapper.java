package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import java.util.List;
import java.util.Map;

public interface TeachRelTeacherClassMapper {

	public abstract void insertTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass);
	
	public abstract void deleteTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass);
	
	public abstract void updateTeachRelTeacherClassByKey(TeachRelTeacherClass teachRelTeacherClass);
	
	public abstract List<TeachRelTeacherClass> selectSingleTeachRelTeacherClass(TeachRelTeacherClass teachRelTeacherClass);
	
	public abstract List<TeachRelTeacherClass> selectAllTeachRelTeacherClass();

	public abstract Integer queryCountAll(Map<String, Object> paramMap);

	public abstract List<TeachRelTeacherClass> queryByPageAll(Map<String, Object> paramMap);

	public abstract Integer queryteachRelClassByCount(Map<String, Object> paramMap);

	public abstract List<TeachRelTeacherClass> queryteachRelClassByPage(Map<String, Object> paramMap);
	
}