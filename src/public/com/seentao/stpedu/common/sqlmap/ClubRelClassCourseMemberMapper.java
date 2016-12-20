package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRelClassCourseMember;
import java.util.List;

public interface ClubRelClassCourseMemberMapper {

	public abstract void insertClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember);
	
	public abstract void deleteClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember);
	
	public abstract void updateClubRelClassCourseMemberByKey(ClubRelClassCourseMember clubRelClassCourseMember);
	
	public abstract List<ClubRelClassCourseMember> selectSingleClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember);
	
	public abstract List<ClubRelClassCourseMember> selectAllClubRelClassCourseMember();
	
}