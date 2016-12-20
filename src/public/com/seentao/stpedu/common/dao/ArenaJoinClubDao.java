package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ArenaJoinClub;
import com.seentao.stpedu.common.entity.ClubBackgroundColor;
import com.seentao.stpedu.common.sqlmap.ArenaJoinClubMapper;


@Repository
public class ArenaJoinClubDao {

	@Autowired
	private ArenaJoinClubMapper arenaJoinClubMapper;
	
	
	public void insertArenaJoinClub(ArenaJoinClub arenaJoinClub){
		arenaJoinClubMapper .insertArenaJoinClub(arenaJoinClub);
	}
	
	public void deleteArenaJoinClub(ArenaJoinClub arenaJoinClub){
		arenaJoinClubMapper .deleteArenaJoinClub(arenaJoinClub);
	}
	
	public void updateArenaJoinClubByKey(ArenaJoinClub arenaJoinClub){
		arenaJoinClubMapper .updateArenaJoinClubByKey(arenaJoinClub);
	}
	
	public List<ArenaJoinClub> selectSingleArenaJoinClub(ArenaJoinClub arenaJoinClub){
		return arenaJoinClubMapper .selectSingleArenaJoinClub(arenaJoinClub);
	}
	
	public ArenaJoinClub selectArenaJoinClub(ArenaJoinClub arenaJoinClub){
		List<ArenaJoinClub> arenaJoinClubList = arenaJoinClubMapper .selectSingleArenaJoinClub(arenaJoinClub);
		if(arenaJoinClubList == null || arenaJoinClubList .size() == 0){
			return null;
		}
		return arenaJoinClubList .get(0);
	}
	
	public List<ArenaJoinClub> selectAllArenaJoinClub(){
		return arenaJoinClubMapper .selectAllArenaJoinClub();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ArenaJoinClub getEntityForDB(Map<String, Object> paramMap){
		ArenaJoinClub tmp = new ArenaJoinClub();
		tmp.setClubId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectArenaJoinClub(tmp);
	}
	/**根据赛事id查询俱乐部
	 * @param gameEventId
	 * @return
	 */
	public List<Integer> queryArenaJoinClubByCompId(Integer gameEventId){
		return arenaJoinClubMapper.queryArenaJoinClubByCompId(gameEventId);
	}
}