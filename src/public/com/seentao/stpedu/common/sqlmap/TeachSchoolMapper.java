package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachSchool;
import java.util.List;
import java.util.Map;

public interface TeachSchoolMapper {

	public abstract void insertTeachSchool(TeachSchool teachSchool);
	
	public abstract void deleteTeachSchool(TeachSchool teachSchool);
	
	public abstract void updateTeachSchoolByKey(TeachSchool teachSchool);
	
	public abstract List<TeachSchool> selectSingleTeachSchool(TeachSchool teachSchool);
	
	public abstract List<TeachSchool> selectAllTeachSchool();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachSchool> queryByPage(Map<String, Object> paramMap);

	public abstract Integer querySchoolCount(Map<String, Object> paramMap);

	public abstract List<TeachSchool> querySchoolByPage(Map<String, Object> paramMap);

	
}