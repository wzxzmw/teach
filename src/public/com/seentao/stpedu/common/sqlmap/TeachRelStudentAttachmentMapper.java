package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;

public interface TeachRelStudentAttachmentMapper {

	public abstract void insertTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment);
	
	public abstract void deleteTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment);
	
	public abstract void updateTeachRelStudentAttachmentByKey(TeachRelStudentAttachment teachRelStudentAttachment);
	
	public abstract List<TeachRelStudentAttachment> selectSingleTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment);
	
	public abstract List<TeachRelStudentAttachment> selectAllTeachRelStudentAttachment();
	
	public abstract List<TeachRelStudentAttachment> selectTeachRelStudentAttachmentByIds(Map map);
	
	public abstract void batchUpdateTeachRelStudentAttachment(List<TeachRelStudentAttachment> list);
	
	public abstract Integer selectStudentCourseHour(Integer studentId);
	
}