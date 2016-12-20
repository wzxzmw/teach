package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelHomeworkAttachment;
import com.seentao.stpedu.common.sqlmap.TeachRelHomeworkAttachmentMapper;


@Repository
public class TeachRelHomeworkAttachmentDao {

	@Autowired
	private TeachRelHomeworkAttachmentMapper teachRelHomeworkAttachmentMapper;
	
	
	public void insertTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentMapper .insertTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public void deleteTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentMapper .deleteTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public void updateTeachRelHomeworkAttachmentByKey(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentMapper .updateTeachRelHomeworkAttachmentByKey(teachRelHomeworkAttachment);
	}
	
	public List<TeachRelHomeworkAttachment> selectSingleTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		return teachRelHomeworkAttachmentMapper .selectSingleTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public TeachRelHomeworkAttachment selectTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		List<TeachRelHomeworkAttachment> teachRelHomeworkAttachmentList = teachRelHomeworkAttachmentMapper .selectSingleTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
		if(teachRelHomeworkAttachmentList == null || teachRelHomeworkAttachmentList .size() == 0){
			return null;
		}
		return teachRelHomeworkAttachmentList .get(0);
	}
	
	public List<TeachRelHomeworkAttachment> selectAllTeachRelHomeworkAttachment(){
		return teachRelHomeworkAttachmentMapper .selectAllTeachRelHomeworkAttachment();
	}

	public List<Integer> findAllRelAttachment(Integer homeworkId) {
		return teachRelHomeworkAttachmentMapper.findAllRelAttachment(homeworkId);
	}
	
	public void insertTeachRelHomeworkAttachments(List<TeachRelHomeworkAttachment> list) {
		teachRelHomeworkAttachmentMapper.insertTeachRelHomeworkAttachments(list);
	}
}