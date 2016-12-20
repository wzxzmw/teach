package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;
import com.seentao.stpedu.common.sqlmap.TeachRelStudentAttachmentMapper;


@Repository
public class TeachRelStudentAttachmentDao {

	@Autowired
	private TeachRelStudentAttachmentMapper teachRelStudentAttachmentMapper;
	
	
	public void insertTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment){
		teachRelStudentAttachmentMapper .insertTeachRelStudentAttachment(teachRelStudentAttachment);
	}
	
	public void deleteTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment){
		teachRelStudentAttachmentMapper .deleteTeachRelStudentAttachment(teachRelStudentAttachment);
	}
	
	public void updateTeachRelStudentAttachmentByKey(TeachRelStudentAttachment teachRelStudentAttachment){
		teachRelStudentAttachmentMapper .updateTeachRelStudentAttachmentByKey(teachRelStudentAttachment);
	}
	
	public List<TeachRelStudentAttachment> selectSingleTeachRelStudentAttachments(TeachRelStudentAttachment teachRelStudentAttachment){
		return teachRelStudentAttachmentMapper .selectSingleTeachRelStudentAttachment(teachRelStudentAttachment);
	}
	
	public TeachRelStudentAttachment selectSingleTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment){
		List<TeachRelStudentAttachment> teachRelStudentAttachmentList = teachRelStudentAttachmentMapper .selectSingleTeachRelStudentAttachment(teachRelStudentAttachment);
		if(teachRelStudentAttachmentList == null || teachRelStudentAttachmentList .size() == 0){
			return null;
		}
		return teachRelStudentAttachmentList .get(0);
	}
	
	public List<TeachRelStudentAttachment> selectAllTeachRelStudentAttachment(){
		return teachRelStudentAttachmentMapper .selectAllTeachRelStudentAttachment();
	}
	
	public List<TeachRelStudentAttachment> selectTeachRelStudentAttachmentByIds(Map<String, Object> map){
		return teachRelStudentAttachmentMapper .selectTeachRelStudentAttachmentByIds(map);
	}
	
	public void batchUpdateTeachRelStudentAttachment(List<TeachRelStudentAttachment> list){
		teachRelStudentAttachmentMapper .batchUpdateTeachRelStudentAttachment(list);
	}
	
	public Integer selectStudentCourseHour(Integer studentId){
		Integer hours = teachRelStudentAttachmentMapper.selectStudentCourseHour(studentId);
		return hours == null ? 0 : hours;
	}
}