package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.TeachRelCourseAttachmentDao;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;

@Service
public class TeachRelCourseAttachmentService{
	
	@Autowired
	private TeachRelCourseAttachmentDao teachRelCourseAttachmentDao;
	
	public List<TeachRelCourseAttachment> getTeachRelCourseAttachment(Integer id) {
		TeachRelCourseAttachment teachRelCourseAttachment = new TeachRelCourseAttachment();
		teachRelCourseAttachment.setCourseId(id);
		List<TeachRelCourseAttachment> teachRelCourseAttachmentList = teachRelCourseAttachmentDao .selectSingleTeachRelCourseAttachment(teachRelCourseAttachment);
		if(teachRelCourseAttachmentList == null || teachRelCourseAttachmentList .size() <= 0){
			return null;
		}
		return teachRelCourseAttachmentList;
	}
	
	

	public void insertTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentDao .insertTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	public void deleteTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentDao .deleteTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	public void updateTeachRelCourseAttachmentByKey(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentDao .updateTeachRelCourseAttachmentByKey(teachRelCourseAttachment);
	}
	
	public TeachRelCourseAttachment selectTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		return teachRelCourseAttachmentDao .selectTeachRelCourseAttachment(teachRelCourseAttachment);
	}

	public void batchInsertTeachRelCourseAttachment(List<TeachRelCourseAttachment> list) {
		teachRelCourseAttachmentDao.batchInsertTeachRelCourseAttachment(list);
	}
	public List<TeachRelCourseAttachment> selectSingleTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		return teachRelCourseAttachmentDao .selectSingleTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	
}