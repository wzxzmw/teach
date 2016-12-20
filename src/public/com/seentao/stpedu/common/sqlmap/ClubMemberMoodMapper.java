package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubMemberMood;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ClubMemberMoodMapper {

	public abstract void insertClubMemberMood(ClubMemberMood clubMemberMood);
	
	public abstract void deleteClubMemberMood(ClubMemberMood clubMemberMood);
	
	/**
	 * 发布的俱乐部心情 冻结
	 * @param _clubmemberMoods
	 * @param createUserId
	 */
	public abstract void batchUpdateClubMemberMood(@Param("clubId") Integer clubId ,@Param("createUserId") Integer createUserId);
	
	public abstract void updateClubMemberMoodByKey(ClubMemberMood clubMemberMood);
	
	public abstract List<ClubMemberMood> selectSingleClubMemberMood(ClubMemberMood clubMemberMood);
	
	public abstract List<ClubMemberMood> selectAllClubMemberMood();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract Integer queryEveryOneMoodCount(Map<String, Object> paramMap);

	public abstract List<ClubMemberMood> selectEveryOneClubMemberMood(Map<String, Object> initParam);

	public abstract Integer queryMyMoodCount(Map<String, Object> paramMap);

	public abstract List<ClubMemberMood> selectMyClubMemberMood(Map<String, Object> initParam);

	public abstract Integer queryMeMoodCount(Map<String, Object> paramMap);

	public abstract List<ClubMemberMood> selectMeClubMemberMood(Map<String, Object> initParam);
	
}