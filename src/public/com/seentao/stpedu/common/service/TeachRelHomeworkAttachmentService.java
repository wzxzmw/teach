package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.TeachRelHomeworkAttachmentDao;
import com.seentao.stpedu.common.entity.TeachRelHomeworkAttachment;

@Service
public class TeachRelHomeworkAttachmentService{
	
	@Autowired
	private TeachRelHomeworkAttachmentDao teachRelHomeworkAttachmentDao;
	
	public String getTeachRelHomeworkAttachment(Integer id) {
		TeachRelHomeworkAttachment teachRelHomeworkAttachment = new TeachRelHomeworkAttachment();
		List<TeachRelHomeworkAttachment> teachRelHomeworkAttachmentList = teachRelHomeworkAttachmentDao .selectSingleTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
		if(teachRelHomeworkAttachmentList == null || teachRelHomeworkAttachmentList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(teachRelHomeworkAttachmentList);
	}
	
	
	public void insertTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentDao .insertTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public void deleteTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentDao .deleteTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public void updateTeachRelHomeworkAttachmentByKey(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		teachRelHomeworkAttachmentDao .updateTeachRelHomeworkAttachmentByKey(teachRelHomeworkAttachment);
	}
	
	public List<TeachRelHomeworkAttachment> selectSingleTeachRelHomeworkAttachment(TeachRelHomeworkAttachment teachRelHomeworkAttachment){
		return teachRelHomeworkAttachmentDao .selectSingleTeachRelHomeworkAttachment(teachRelHomeworkAttachment);
	}
	
	public void insertTeachRelHomeworkAttachments(List<TeachRelHomeworkAttachment> list) {
		teachRelHomeworkAttachmentDao.insertTeachRelHomeworkAttachments(list);
	}

	/**
	 * 根据课程文本作业id		获取所有附件id
	 * @param homeworkId
	 * @return
	 * @author 			lw
	 * @date			2016年6月30日  上午11:49:56
	 */
	public List<Integer> findAllRelAttachment(Integer homeworkId) {
		return teachRelHomeworkAttachmentDao.findAllRelAttachment(homeworkId);
	}
	
}