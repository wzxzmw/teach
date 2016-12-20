package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachInstitute;
import java.util.List;

public interface TeachInstituteMapper {

	public abstract void insertTeachInstitute(TeachInstitute teachInstitute);
	
	public abstract void deleteTeachInstitute(TeachInstitute teachInstitute);
	
	public abstract void updateTeachInstituteByKey(TeachInstitute teachInstitute);
	
	public abstract List<TeachInstitute> selectSingleTeachInstitute(TeachInstitute teachInstitute);
	
	public abstract List<TeachInstitute> selectAllTeachInstitute();
	
}