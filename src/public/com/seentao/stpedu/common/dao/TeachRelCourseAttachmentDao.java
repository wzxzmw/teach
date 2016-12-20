package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.sqlmap.TeachRelCourseAttachmentMapper;


@Repository
public class TeachRelCourseAttachmentDao {

	@Autowired
	private TeachRelCourseAttachmentMapper teachRelCourseAttachmentMapper;
	
	
	public void insertTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentMapper .insertTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	public void deleteTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentMapper .deleteTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	public void updateTeachRelCourseAttachmentByKey(TeachRelCourseAttachment teachRelCourseAttachment){
		teachRelCourseAttachmentMapper .updateTeachRelCourseAttachmentByKey(teachRelCourseAttachment);
	}
	
	public void batchUpdateTeachRelCourseAttachment(List<TeachRelCourseAttachment> list){
		teachRelCourseAttachmentMapper .batchUpdateTeachRelCourseAttachment(list);
	}
	
	public List<TeachRelCourseAttachment> selectSingleTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		return teachRelCourseAttachmentMapper .selectSingleTeachRelCourseAttachment(teachRelCourseAttachment);
	}
	
	public TeachRelCourseAttachment selectTeachRelCourseAttachment(TeachRelCourseAttachment teachRelCourseAttachment){
		List<TeachRelCourseAttachment> teachRelCourseAttachmentList = teachRelCourseAttachmentMapper .selectSingleTeachRelCourseAttachment(teachRelCourseAttachment);
		if(teachRelCourseAttachmentList == null || teachRelCourseAttachmentList .size() == 0){
			return null;
		}
		return teachRelCourseAttachmentList .get(0);
	}
	
	public List<TeachRelCourseAttachment> selectAllTeachRelCourseAttachment(){
		return teachRelCourseAttachmentMapper .selectAllTeachRelCourseAttachment();
	}

	public void batchInsertTeachRelCourseAttachment(List<TeachRelCourseAttachment> list) {
		teachRelCourseAttachmentMapper.batchInsertTeachRelCourseAttachment(list);
	}
	
	public Integer selectStudentCourseHour(Integer studentId){
		Integer hours = teachRelCourseAttachmentMapper.selectStudentCourseHour(studentId);
		return hours == null ? 0 : hours;
	}
}