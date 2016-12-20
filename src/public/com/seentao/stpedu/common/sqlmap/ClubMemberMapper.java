package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.ClubMember;

public interface ClubMemberMapper {

	public abstract int insertClubMember(ClubMember clubMember);
	
	public abstract void deleteClubMember(ClubMember clubMember);
	
	public abstract void updateClubMemberByKey(ClubMember clubMember);
	/**
	 * 根据距主键ID修改
	 * @param 
	 * @return
	 */
	public abstract void updateClubMemberById(@Param("memberId")Integer memberId,@Param("memberStatus")Integer memberStatus)throws RuntimeException;
	
	/**
	 * @param map
	 * @return
	 */
	public abstract ClubMember queryClubMemberSome(Map<String,Object> map);
	/**
	 * 
	 * @param clubMember
	 * @return
	 */
	public abstract List<ClubMember> selectSingleClubMember(ClubMember clubMember);
	
	public abstract List<ClubMember> selectAllClubMember();

	public abstract Integer queryCount(ClubMember c);

	public abstract List<ClubMember> checkPresidentAndArenaCompetition(ClubMember member);
	
	public abstract int validatePresidentAndArenaCompetition(Map<String,Object> conditionMap);
	
	public abstract List<ClubMember> selectClubMemberByClubId(ClubMember member);

	public abstract List<ClubMember> getClubMemberList(ClubMember param);

	public abstract Integer getClubMemberCount(ClubMember param);

	public abstract void batchUpdateByUserIdAndClubId(List<ClubMember> list);

	public abstract void updateByUserIdAndClubId(ClubMember club);

	public abstract List<ClubMember> getClubMemberByStatus(ClubMember c);

	public abstract Integer queryCountByStatus(ClubMember c);

	public abstract void updateClubmemerIsremoind(ClubMember clubMember);

	public abstract List<ClubMember> selectClubMemberByUserIds(Map<String, Object> paramMap);

	public abstract void updateIsNewRemindByUserIds(Map<String, Object> paramMap);

	public abstract void updateIsNewRemindByMemberIds(Map<String, Object> paramMap);

	public abstract void updateIsNewNoticeByMemberIds(Map<String, Object> paramMap);
	
	/**
	 * 根据用户ID查询是否存在该俱乐部会员，member_status null,3,4
	 */
	public abstract ClubMember selectOnlyClubMember(@Param("userId") Integer userId);
	// 根据俱乐部clubId 和用户id ,以及1,2状态查询
		public abstract  ClubMember queryClubMemberByClubIdAndUserId(@Param("userId")Integer userId,@Param("clubId")Integer clubId);
}