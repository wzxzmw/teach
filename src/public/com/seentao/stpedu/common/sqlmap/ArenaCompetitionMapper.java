package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.ArenaCompetition;

public interface ArenaCompetitionMapper {

	public abstract void insertArenaCompetition(ArenaCompetition arenaCompetition);
	
	public abstract void deleteArenaCompetition(ArenaCompetition arenaCompetition);
	
	public abstract void updateArenaCompetitionByKey(ArenaCompetition arenaCompetition);
	
	public abstract List<ArenaCompetition> selectSingleArenaCompetition(ArenaCompetition arenaCompetition);
	public abstract List<ArenaCompetition> selectAllArenaCompetition();

	public abstract Integer queryCountRecommend(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPagReidsRecommend(Map<String, Object> paramMap);

	public abstract List<ArenaCompetition> queryByPage(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPagReidsHot(Map<String, Object> paramMap);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract Integer queryCountMyFollow(Map<String, Object> paramMap);

	public abstract List<ArenaCompetition> queryByPagReidsMyFollow(Map<String, Object> paramMap);

	public abstract List<ArenaCompetition> queryByPagReidsSearch(Map<String, Object> paramMap);

	public abstract Integer queryCountSearch(Map<String, Object> paramMap);

	public abstract Integer queryCountByCenterAttention(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPagReidsCenterAttention(Map<String, Object> paramMap);

	public abstract void updateAddNumber(ArenaCompetition comp);
	
	public abstract ArenaCompetition queryArenaCompetitionByClubId(@Param("clubId")Integer clubId);
}