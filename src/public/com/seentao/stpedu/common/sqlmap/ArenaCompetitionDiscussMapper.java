package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ArenaCompetitionDiscuss;
import java.util.List;
import java.util.Map;

public interface ArenaCompetitionDiscussMapper {

	public abstract void insertArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss);
	
	public abstract void deleteArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss);
	
	public abstract void updateArenaCompetitionDiscussByKey(ArenaCompetitionDiscuss arenaCompetitionDiscuss);
	
	public abstract List<ArenaCompetitionDiscuss> selectSingleArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss);
	
	public abstract List<ArenaCompetitionDiscuss> selectAllArenaCompetitionDiscuss();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ArenaCompetitionDiscuss> queryByPage(Map<String, Object> paramMap);

	public abstract void delArenaCompetitionDiscussAll(List<ArenaCompetitionDiscuss> delArenaCompetitionDiscuss);

	public abstract Integer queryCompNewByCount(Map<String, Object> paramMap);

	public abstract List<ArenaCompetitionDiscuss> queryCompNewByAge(Map<String, Object> paramMap);

	public abstract Integer queryCompoldByCount(Map<String, Object> paramMap);

	public abstract List<ArenaCompetitionDiscuss> queryCompoldByAge(Map<String, Object> paramMap);
	
}