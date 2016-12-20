package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ArenaJoinClubDao;
import com.seentao.stpedu.common.entity.ArenaJoinClub;

@Service
public class ArenaJoinClubService{
	
	@Autowired
	private ArenaJoinClubDao arenaJoinClubDao;
	
	public String getArenaJoinClub(Integer id) {
		ArenaJoinClub arenaJoinClub = new ArenaJoinClub();
		List<ArenaJoinClub> arenaJoinClubList = arenaJoinClubDao .selectSingleArenaJoinClub(arenaJoinClub);
		if(arenaJoinClubList == null || arenaJoinClubList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(arenaJoinClubList);
	}
	
	/**
	 * 添加赛事俱乐部记录
	 * @param compId 比赛ID
	 * @param clubId 俱乐部ID
	 * @author chengshx
	 */
	public void addArenaJoinClub(Integer compId, Integer clubId){
		ArenaJoinClub arenaJoinClub = new ArenaJoinClub();
		arenaJoinClub.setCompId(compId);
		arenaJoinClub.setClubId(clubId);
		arenaJoinClubDao.insertArenaJoinClub(arenaJoinClub);
	}
	
	/**
	 * 根据比赛ID和俱乐部ID查询
	 * @param compId 比赛ID
	 * @param clubId 俱乐部ID
	 * @return
	 * @author chengshx
	 */
	public ArenaJoinClub getArenaJoinClub(Integer compId, Integer clubId){
		ArenaJoinClub arenaJoinClub = new ArenaJoinClub();
		arenaJoinClub.setCompId(compId);
		arenaJoinClub.setClubId(clubId);
		return arenaJoinClubDao.selectArenaJoinClub(arenaJoinClub);
	}
}