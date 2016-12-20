package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import java.util.List;

public interface TeachRelCourseAttachmentMapper {

	public abstract void insertTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment);
	
	public abstract void deleteTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment);
	
	public abstract void updateTeachRelCourseAttachmentByKey(TeachRelCourseAttachment teachRelCourseAttachment);
	
	public abstract void batchUpdateTeachRelCourseAttachment(List<TeachRelCourseAttachment> list);
	
	public abstract List<TeachRelCourseAttachment> selectSingleTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment);
	
	public abstract List<TeachRelCourseAttachment> selectAllTeachRelCourseAttachment();

	public abstract void batchInsertTeachRelCourseAttachment(List<TeachRelCourseAttachment> list);
	
	public abstract Integer selectStudentCourseHour(Integer studentId);
	
}