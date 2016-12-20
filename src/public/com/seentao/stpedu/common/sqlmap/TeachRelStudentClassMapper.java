package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachRelStudentClass;

public interface TeachRelStudentClassMapper {

	public abstract void insertTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass);
	
	public abstract void deleteTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass);
	
	public abstract void updateTeachRelStudentClassByKey(TeachRelStudentClass teachRelStudentClass);
	
	public abstract List<TeachRelStudentClass> selectSingleTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass);
	
	public abstract List<TeachRelStudentClass> selectAllTeachRelStudentClass();

	public abstract void saveStudentAll(List<TeachRelStudentClass> classes);

	public abstract void deleteTeachStudentAll(List<TeachRelStudentClass> teachStudentAll);
	
	public abstract List<TeachRelStudentClass> getGradesByClassId(TeachRelStudentClass t);

	public abstract Integer queryCountByClassId(Map<String, Object> map);

	public abstract void updateTeachRelStudentClassByKeyNoRel(TeachRelStudentClass teachStudentClass);
	
}