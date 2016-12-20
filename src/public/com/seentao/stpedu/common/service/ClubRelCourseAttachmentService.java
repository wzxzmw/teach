package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubRelCourseAttachmentDao;
import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;

@Service
public class ClubRelCourseAttachmentService{
	
	@Autowired
	private ClubRelCourseAttachmentDao clubRelCourseAttachmentDao;
	
	public String getClubRelCourseAttachment(Integer id) {
		ClubRelCourseAttachment clubRelCourseAttachment = new ClubRelCourseAttachment();
		List<ClubRelCourseAttachment> clubRelCourseAttachmentList = clubRelCourseAttachmentDao .selectSingleClubRelCourseAttachment(clubRelCourseAttachment);
		if(clubRelCourseAttachmentList == null || clubRelCourseAttachmentList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(clubRelCourseAttachmentList);
	}

	public void deleteClubRelCourseAttachment(ClubRelCourseAttachment delete) {
		clubRelCourseAttachmentDao.deleteClubRelCourseAttachment(delete);
	}

	public void batchInsertClubRelCourseAttachment(List<ClubRelCourseAttachment> list) {
		clubRelCourseAttachmentDao.batchInsertClubRelCourseAttachment(list);
	}
	public List<ClubRelCourseAttachment> selectSingleClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment){
		return clubRelCourseAttachmentDao .selectSingleClubRelCourseAttachment(clubRelCourseAttachment);
	}
}