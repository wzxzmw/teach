package com.seentao.stpedu.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.PublicAttachmentDao;
import com.seentao.stpedu.common.entity.PublicAttachment;

@Service
public class PublicAttachmentService{
	
	@Autowired
	private PublicAttachmentDao publicAttachmentDao;
	
	public List<PublicAttachment> getPublicAttachment(PublicAttachment publicAttachment) {
		List<PublicAttachment> publicAttachmentList = publicAttachmentDao .selectSinglePublicAttachment(publicAttachment);
		if(publicAttachmentList == null || publicAttachmentList .size() <= 0){
			return null;
		}
		return publicAttachmentList;
	}
	
	/**
	 * 根据课程id查询该id下其他附件信息
	 * @param courseId
	 * @return
	 */
	public List<PublicAttachment> getOtherPublicAttachment(Integer courseId) {
		return publicAttachmentDao.getOtherPublicAttachment(courseId);
	}

	public List<PublicAttachment> getClubOtherPublicAttachment(Integer courseId) {
		return publicAttachmentDao.getClubOtherPublicAttachment(courseId);
	}
	
	/**
	 * 根据课程id查询该id下其他附件信息
	 * @param attachmentids
	 * @return
	 */
	public List<PublicAttachment> getOtherPublicAttachment(Map<String, Object> attachmentids) {
		return publicAttachmentDao.getOtherPublicAttachment(attachmentids);
	}

	public List<PublicAttachment> getClubOtherPublicAttachment(Map<String, Object> attachmentids) {
		return publicAttachmentDao.getClubOtherPublicAttachment(attachmentids);
	}
	
	public Integer insertPublicAttachment(PublicAttachment publicAttachment){
		return publicAttachmentDao.insertPublicAttachment(publicAttachment);
	}

	
}