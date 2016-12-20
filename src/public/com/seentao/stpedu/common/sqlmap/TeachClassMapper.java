package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachClass;

public interface TeachClassMapper {

	public abstract Integer insertTeachClass(TeachClass teachClass)throws Exception;
	
	public abstract void deleteTeachClass(TeachClass teachClass);
	
	public abstract void updateTeachClassByKey(TeachClass teachClass)throws Exception;
	
	public abstract List<TeachClass> selectSingleTeachClass(TeachClass teachClass);
	
	public abstract List<TeachClass> selectAllTeachClass();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachClass> queryByPage(Map<String, Object> paramMap);

	public abstract Integer queryCountSchool(Map<String, Object> paramMap);

	public abstract List<TeachClass> queryByPageSchool(Map<String, Object> paramMap);

	public abstract List<TeachClass> selectSingleTeachClassGroup(TeachClass teachClass);
	
	public abstract List<Integer> selectAllClassId(Integer isDelete);

	public abstract void updateOriginalCourseNum(TeachClass teachClass);
	
}