package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import java.util.List;
import java.util.Map;

public interface ClubRelRemindMemberMapper {

	public abstract void insertClubRelRemindMember(ClubRelRemindMember clubRelRemindMember);
	
	public abstract void deleteClubRelRemindMember(ClubRelRemindMember clubRelRemindMember);
	
	public abstract void updateClubRelRemindMemberByKey(ClubRelRemindMember clubRelRemindMember);
	
	public abstract List<ClubRelRemindMember> selectSingleClubRelRemindMember(ClubRelRemindMember clubRelRemindMember);
	
	public abstract List<ClubRelRemindMember> selectAllClubRelRemindMember();

	public abstract void insertBatchClubRelRemindMember(List<ClubRelRemindMember> members);
	
	public abstract List<ClubRelRemindMember> selectEveryOneClubRemind(Map<String, Object> initParam);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract Integer queryRemindMemberCount(Map<String, Object> paramMap);

	public abstract Integer queryMeRemindMemberCount(Map<String, Object> paramMap);

	public abstract List<ClubRelRemindMember> selectMeClubRemind(Map<String, Object> initParam);

	public abstract Integer queryMyRemindMemberCount(Map<String, Object> paramMap);

	public abstract List<ClubRelRemindMember> selectMyClubRemind(Map<String, Object> initParam);
	
}