package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ArenaCompetitionDiscussDao;
import com.seentao.stpedu.common.entity.ArenaCompetitionDiscuss;

@Service
public class ArenaCompetitionDiscussService{
	
	@Autowired
	private ArenaCompetitionDiscussDao arenaCompetitionDiscussDao;
	
	public ArenaCompetitionDiscuss getArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss) {
		List<ArenaCompetitionDiscuss> arenaCompetitionDiscussList = arenaCompetitionDiscussDao .selectSingleArenaCompetitionDiscuss(arenaCompetitionDiscuss);
		if(arenaCompetitionDiscussList == null || arenaCompetitionDiscussList .size() <= 0){
			return null;
		}
		
		return arenaCompetitionDiscussList.get(0);
	}
	
	public void updateArenaCompetitionDiscussByKey(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		arenaCompetitionDiscussDao.updateArenaCompetitionDiscussByKey(arenaCompetitionDiscuss);
	}
	
	public void insertArenaCompetitionDiscuss(ArenaCompetitionDiscuss arenaCompetitionDiscuss){
		arenaCompetitionDiscussDao.insertArenaCompetitionDiscuss(arenaCompetitionDiscuss);
	}

	public void delArenaCompetitionDiscussAll(List<ArenaCompetitionDiscuss> delArenaCompetitionDiscuss) {
		arenaCompetitionDiscussDao.delArenaCompetitionDiscussAll(delArenaCompetitionDiscuss);
	}
}