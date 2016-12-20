package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubRelClassCourseMemberDao;
import com.seentao.stpedu.common.entity.ClubRelClassCourseMember;

@Service
public class ClubRelClassCourseMemberService{
	
	@Autowired
	private ClubRelClassCourseMemberDao clubRelClassCourseMemberDao;
	
	public ClubRelClassCourseMember getClubRelClassCourseMember(ClubRelClassCourseMember clubRelClassCourseMember) {
		List<ClubRelClassCourseMember> clubRelClassCourseMemberList = clubRelClassCourseMemberDao .selectSingleClubRelClassCourseMember(clubRelClassCourseMember);
		if(clubRelClassCourseMemberList == null || clubRelClassCourseMemberList .size() <= 0){
			return null;
		}
		
		return clubRelClassCourseMemberList.get(0);
	}

	public void insertClubRelClassCourseMember(ClubRelClassCourseMember insertMember) {
		clubRelClassCourseMemberDao.insertClubRelClassCourseMember(insertMember);
	}
}