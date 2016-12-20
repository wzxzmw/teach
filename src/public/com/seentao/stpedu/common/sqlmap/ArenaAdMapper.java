package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ArenaAd;

public interface ArenaAdMapper {

	public abstract void insertArenaAd(ArenaAd arenaAd);
	
	public abstract void deleteArenaAd(ArenaAd arenaAd);
	
	public abstract void updateArenaAdByKey(ArenaAd arenaAd);
	
	public abstract List<ArenaAd> selectSingleArenaAd(ArenaAd arenaAd);
	
	public abstract List<ArenaAd> selectAllArenaAd();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ArenaAd> queryByPage(Map<String, Object> paramMap);
	
	public abstract List<Integer> queryByPageids(Map<String, Object> paramMap);
	
}