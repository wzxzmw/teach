package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ArenaAd;
import com.seentao.stpedu.common.sqlmap.ArenaAdMapper;


@Repository
public class ArenaAdDao {

	@Autowired
	private ArenaAdMapper arenaAdMapper;
	
	
	public void insertArenaAd(ArenaAd arenaAd){
		arenaAdMapper .insertArenaAd(arenaAd);
	}
	
	public void deleteArenaAd(ArenaAd arenaAd){
		arenaAdMapper .deleteArenaAd(arenaAd);
	}
	
	public void updateArenaAdByKey(ArenaAd arenaAd){
		arenaAdMapper .updateArenaAdByKey(arenaAd);
	}
	
	public List<ArenaAd> selectSingleArenaAd(ArenaAd arenaAd){
		return arenaAdMapper .selectSingleArenaAd(arenaAd);
	}
	
	public ArenaAd selectArenaAd(ArenaAd arenaAd){
		List<ArenaAd> arenaAdList = arenaAdMapper .selectSingleArenaAd(arenaAd);
		if(arenaAdList == null || arenaAdList .size() == 0){
			return null;
		}
		return arenaAdList .get(0);
	}
	
	public List<ArenaAd> selectAllArenaAd(){
		return arenaAdMapper .selectAllArenaAd();
	}
	
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return arenaAdMapper.queryCount(paramMap);
	}

	public List<ArenaAd> queryByPage(Map<String, Object> paramMap) {
		return arenaAdMapper.queryByPage(paramMap);
	}
	
	public List<Integer> queryByPageids(Map<String, Object> paramMap) {
		return arenaAdMapper.queryByPageids(paramMap);
	}
}