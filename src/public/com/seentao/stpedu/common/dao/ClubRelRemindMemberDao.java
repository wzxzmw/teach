package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.sqlmap.ClubRelRemindMemberMapper;


@Repository
public class ClubRelRemindMemberDao {

	@Autowired
	private ClubRelRemindMemberMapper clubRelRemindMemberMapper;
	
	
	public void insertClubRelRemindMember(ClubRelRemindMember clubRelRemindMember){
		clubRelRemindMemberMapper .insertClubRelRemindMember(clubRelRemindMember);
	}
	
	public void deleteClubRelRemindMember(ClubRelRemindMember clubRelRemindMember){
		clubRelRemindMemberMapper .deleteClubRelRemindMember(clubRelRemindMember);
	}
	
	public void updateClubRelRemindMemberByKey(ClubRelRemindMember clubRelRemindMember){
		clubRelRemindMemberMapper .updateClubRelRemindMemberByKey(clubRelRemindMember);
	}
	
	public List<ClubRelRemindMember> selectClubRelRemindMember(ClubRelRemindMember clubRelRemindMember){
		return clubRelRemindMemberMapper .selectSingleClubRelRemindMember(clubRelRemindMember);
	}
	
	public ClubRelRemindMember selectSingleClubRelRemindMember(ClubRelRemindMember clubRelRemindMember){
		List<ClubRelRemindMember> clubRelRemindMemberList = clubRelRemindMemberMapper .selectSingleClubRelRemindMember(clubRelRemindMember);
		if(clubRelRemindMemberList == null || clubRelRemindMemberList .size() == 0){
			return null;
		}
		return clubRelRemindMemberList .get(0);
	}
	
	public List<ClubRelRemindMember> selectAllClubRelRemindMember(){
		return clubRelRemindMemberMapper .selectAllClubRelRemindMember();
	}

	public void insertBatchClubRelRemindMember(List<ClubRelRemindMember> members) {
		clubRelRemindMemberMapper.insertBatchClubRelRemindMember(members);
	}
	
	public Integer queryRemindMemberCount(Map<String, Object> paramMap){
		return clubRelRemindMemberMapper.queryRemindMemberCount(paramMap);
	}
	public List<ClubRelRemindMember> selectEveryOneClubRemind(Map<String, Object> initParam) {
		return clubRelRemindMemberMapper.selectEveryOneClubRemind(initParam);
	}
	public Integer queryMeRemindMemberCount(Map<String, Object> paramMap){
		return clubRelRemindMemberMapper.queryMeRemindMemberCount(paramMap);
	}
	public List<ClubRelRemindMember> selectMeClubRemind(Map<String, Object> initParam) {
		return clubRelRemindMemberMapper.selectMeClubRemind(initParam);
	}
	
	public Integer queryMyRemindMemberCount(Map<String, Object> paramMap){
		return clubRelRemindMemberMapper.queryMyRemindMemberCount(paramMap);
	}
	public List<ClubRelRemindMember> selectMyClubRemind(Map<String, Object> initParam) {
		return clubRelRemindMemberMapper.selectMyClubRemind(initParam);
	}
}