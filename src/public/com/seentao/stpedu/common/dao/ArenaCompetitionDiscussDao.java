package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ArenaCompetitionDiscuss;
import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;
import com.seentao.stpedu.common.sqlmap.ArenaCompetitionDiscussMapper;


@Repository
public class ArenaCompetitionDiscussDao {

	@Autowired
	private ArenaCompetitionDiscussMapper arenaCompetitionDiscussMapper;
	
	
	public void insertArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		arenaCompetitionDiscussMapper .insertArenaCompetitionDiscuss(arenaCompetitionDiscuss);
	}
	
	public void deleteArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		arenaCompetitionDiscussMapper .deleteArenaCompetitionDiscuss(arenaCompetitionDiscuss);
	}
	
	public void updateArenaCompetitionDiscussByKey(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		arenaCompetitionDiscussMapper .updateArenaCompetitionDiscussByKey(arenaCompetitionDiscuss);
	}
	
	public List<ArenaCompetitionDiscuss> selectSingleArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		return arenaCompetitionDiscussMapper .selectSingleArenaCompetitionDiscuss(arenaCompetitionDiscuss);
	}
	
	public ArenaCompetitionDiscuss selectArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		List<ArenaCompetitionDiscuss> arenaCompetitionDiscussList = arenaCompetitionDiscussMapper .selectSingleArenaCompetitionDiscuss(arenaCompetitionDiscuss);
		if(arenaCompetitionDiscussList == null || arenaCompetitionDiscussList .size() == 0){
			return null;
		}
		return arenaCompetitionDiscussList .get(0);
	}
	
	public List<ArenaCompetitionDiscuss> selectAllArenaCompetitionDiscuss(){
		return arenaCompetitionDiscussMapper .selectAllArenaCompetitionDiscuss();
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryCount(paramMap);
	}
	public List<ArenaCompetitionDiscuss> queryByPage(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryByPage(paramMap);
	}
	
	public Integer queryCompNewByCount(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryCompNewByCount(paramMap);
	}
	public List<ArenaCompetitionDiscuss> queryCompNewByAge(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryCompNewByAge(paramMap);
	}
	
	public Integer queryCompoldByCount(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryCompoldByCount(paramMap);
	}
	public List<ArenaCompetitionDiscuss> queryCompoldByAge(Map<String, Object> paramMap) {
		return arenaCompetitionDiscussMapper.queryCompoldByAge(paramMap);
	}

	public void delArenaCompetitionDiscussAll(List<ArenaCompetitionDiscuss> delArenaCompetitionDiscuss) {
		arenaCompetitionDiscussMapper.delArenaCompetitionDiscussAll(delArenaCompetitionDiscuss);
		
	}
}