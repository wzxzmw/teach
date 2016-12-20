package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;
import com.seentao.stpedu.common.sqlmap.ClubRelCourseAttachmentMapper;


@Repository
public class ClubRelCourseAttachmentDao {

	@Autowired
	private ClubRelCourseAttachmentMapper clubRelCourseAttachmentMapper;
	
	
	public void insertClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment){
		clubRelCourseAttachmentMapper .insertClubRelCourseAttachment(clubRelCourseAttachment);
	}
	
	public void deleteClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment){
		clubRelCourseAttachmentMapper .deleteClubRelCourseAttachment(clubRelCourseAttachment);
	}
	
	public void updateClubRelCourseAttachmentByKey(ClubRelCourseAttachment clubRelCourseAttachment){
		clubRelCourseAttachmentMapper .updateClubRelCourseAttachmentByKey(clubRelCourseAttachment);
	}
	
	public List<ClubRelCourseAttachment> selectSingleClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment){
		return clubRelCourseAttachmentMapper .selectSingleClubRelCourseAttachment(clubRelCourseAttachment);
	}
	
	public ClubRelCourseAttachment selectClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment){
		List<ClubRelCourseAttachment> clubRelCourseAttachmentList = clubRelCourseAttachmentMapper .selectSingleClubRelCourseAttachment(clubRelCourseAttachment);
		if(clubRelCourseAttachmentList == null || clubRelCourseAttachmentList .size() == 0){
			return null;
		}
		return clubRelCourseAttachmentList .get(0);
	}
	
	public List<ClubRelCourseAttachment> selectAllClubRelCourseAttachment(){
		return clubRelCourseAttachmentMapper .selectAllClubRelCourseAttachment();
	}

	public void batchInsertClubRelCourseAttachment(List<ClubRelCourseAttachment> list) {
		clubRelCourseAttachmentMapper. batchInsertClubRelCourseAttachment(list);
	}
}