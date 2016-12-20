package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ArenaAdDao;
import com.seentao.stpedu.common.entity.ArenaAd;

@Service
public class ArenaAdService{
	
	@Autowired
	private ArenaAdDao arenaAdDao;
	
	public String getArenaAd(Integer id) {
		ArenaAd arenaAd = new ArenaAd();
		List<ArenaAd> arenaAdList = arenaAdDao .selectSingleArenaAd(arenaAd);
		if(arenaAdList == null || arenaAdList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(arenaAdList);
	}
}