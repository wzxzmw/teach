package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.ClubRelClassCourseMember;
import com.seentao.stpedu.common.sqlmap.ClubRelClassCourseMemberMapper;


@Repository
public class ClubRelClassCourseMemberDao {

	@Autowired
	private ClubRelClassCourseMemberMapper clubRelClassCourseMemberMapper;
	
	
	public void insertClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember){
		clubRelClassCourseMemberMapper .insertClubRelClassCourseMember(clubRelClassCourseMember);
	}
	
	public void deleteClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember){
		clubRelClassCourseMemberMapper .deleteClubRelClassCourseMember(clubRelClassCourseMember);
	}
	
	public void updateClubRelClassCourseMemberByKey(ClubRelClassCourseMember clubRelClassCourseMember){
		clubRelClassCourseMemberMapper .updateClubRelClassCourseMemberByKey(clubRelClassCourseMember);
	}
	
	public List<ClubRelClassCourseMember> selectSingleClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember){
		return clubRelClassCourseMemberMapper .selectSingleClubRelClassCourseMember(clubRelClassCourseMember);
	}
	
	public ClubRelClassCourseMember selectClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember){
		List<ClubRelClassCourseMember> clubRelClassCourseMemberList = clubRelClassCourseMemberMapper .selectSingleClubRelClassCourseMember(clubRelClassCourseMember);
		if(clubRelClassCourseMemberList == null || clubRelClassCourseMemberList .size() == 0){
			return null;
		}
		return clubRelClassCourseMemberList .get(0);
	}
	
	public List<ClubRelClassCourseMember> selectAllClubRelClassCourseMember(){
		return clubRelClassCourseMemberMapper .selectAllClubRelClassCourseMember();
	}
}