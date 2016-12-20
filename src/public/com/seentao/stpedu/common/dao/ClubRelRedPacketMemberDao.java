package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.sqlmap.ClubRelRedPacketMemberMapper;


@Repository
public class ClubRelRedPacketMemberDao {

	@Autowired
	private ClubRelRedPacketMemberMapper clubRelRedPacketMemberMapper;
	
	
	public void insertClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember){
		clubRelRedPacketMemberMapper .insertClubRelRedPacketMember(clubRelRedPacketMember);
	}
	
	public void deleteClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember){
		clubRelRedPacketMemberMapper .deleteClubRelRedPacketMember(clubRelRedPacketMember);
	}
	
	public void updateClubRelRedPacketMemberByKey(ClubRelRedPacketMember clubRelRedPacketMember){
		clubRelRedPacketMemberMapper .updateClubRelRedPacketMemberByKey(clubRelRedPacketMember);
	}
	
	public List<ClubRelRedPacketMember> selectSingleClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember){
		return clubRelRedPacketMemberMapper .selectSingleClubRelRedPacketMember(clubRelRedPacketMember);
	}
	
	public ClubRelRedPacketMember selectClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember){
		List<ClubRelRedPacketMember> clubRelRedPacketMemberList = clubRelRedPacketMemberMapper .selectSingleClubRelRedPacketMember(clubRelRedPacketMember);
		if(clubRelRedPacketMemberList == null || clubRelRedPacketMemberList .size() == 0){
			return null;
		}
		return clubRelRedPacketMemberList .get(0);
	}
	
	public List<ClubRelRedPacketMember> selectAllClubRelRedPacketMember(){
		return clubRelRedPacketMemberMapper .selectAllClubRelRedPacketMember();
	}

	public void batchInsertClubRelRedPacketMember(List<ClubRelRedPacketMember> clubRelRedPacketMemberList) {
		clubRelRedPacketMemberMapper.batchInsertClubRelRedPacketMember(clubRelRedPacketMemberList);
	}
}