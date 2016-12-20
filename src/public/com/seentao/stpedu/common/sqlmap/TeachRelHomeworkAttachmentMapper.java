package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.TeachRelHomeworkAttachment;
import java.util.List;

public interface TeachRelHomeworkAttachmentMapper {

	public abstract void insertTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment);
	
	public abstract void deleteTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment);
	
	public abstract void updateTeachRelHomeworkAttachmentByKey(TeachRelHomeworkAttachment teachRelHomeworkAttachment);
	
	public abstract List<TeachRelHomeworkAttachment> selectSingleTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment);
	
	public abstract List<TeachRelHomeworkAttachment> selectAllTeachRelHomeworkAttachment();

	public abstract List<Integer> findAllRelAttachment(Integer homeworkId);
	
	public abstract void insertTeachRelHomeworkAttachments(List<TeachRelHomeworkAttachment> teachRelHomeworkAttachmentList);
	
}