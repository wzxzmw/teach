package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;
import java.util.List;

public interface ClubRelCourseAttachmentMapper {

	public abstract void insertClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment);
	
	public abstract void deleteClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment);
	
	public abstract void updateClubRelCourseAttachmentByKey(ClubRelCourseAttachment clubRelCourseAttachment);
	
	public abstract List<ClubRelCourseAttachment> selectSingleClubRelCourseAttachment(ClubRelCourseAttachment clubRelCourseAttachment);
	
	public abstract List<ClubRelCourseAttachment> selectAllClubRelCourseAttachment();

	public abstract void batchInsertClubRelCourseAttachment(List<ClubRelCourseAttachment> list);
	
}