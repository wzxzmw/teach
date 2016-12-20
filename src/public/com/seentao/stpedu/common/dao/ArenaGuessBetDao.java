package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ArenaGuessBet;
import com.seentao.stpedu.common.sqlmap.ArenaGuessBetMapper;


@Repository
public class ArenaGuessBetDao {

	@Autowired
	private ArenaGuessBetMapper arenaGuessBetMapper;
	
	
	public void insertArenaGuessBet(ArenaGuessBet arenaGuessBet){
		arenaGuessBetMapper .insertArenaGuessBet(arenaGuessBet);
	}
	
	public void deleteArenaGuessBet(ArenaGuessBet arenaGuessBet){
		arenaGuessBetMapper .deleteArenaGuessBet(arenaGuessBet);
	}
	
	public void updateArenaGuessBetByKey(ArenaGuessBet arenaGuessBet){
		arenaGuessBetMapper .updateArenaGuessBetByKey(arenaGuessBet);
	}
	
	public List<ArenaGuessBet> selectSingleArenaGuessBet(ArenaGuessBet arenaGuessBet){
		return arenaGuessBetMapper .selectSingleArenaGuessBet(arenaGuessBet);
	}
	
	public ArenaGuessBet selectArenaGuessBet(ArenaGuessBet arenaGuessBet){
		List<ArenaGuessBet> arenaGuessBetList = arenaGuessBetMapper .selectSingleArenaGuessBet(arenaGuessBet);
		if(arenaGuessBetList == null || arenaGuessBetList .size() == 0){
			return null;
		}
		return arenaGuessBetList .get(0);
	}
	
	public List<ArenaGuessBet> selectAllArenaGuessBet(){
		return arenaGuessBetMapper .selectAllArenaGuessBet();
	}

	public List<ArenaGuessBet> getMaxBetList(ArenaGuessBet arenaguessbet) {
		return arenaGuessBetMapper.getMaxBetList(arenaguessbet);
	}

	public Integer getMaxBetCount() {
		return arenaGuessBetMapper.getMaxBetCount();
	}

	public List<ArenaGuessBet> getWinNumberList(ArenaGuessBet arenaguessbet) {
		return arenaGuessBetMapper.getWinNumberList(arenaguessbet);
	}

	public Integer getWinNumberCount() {
		return arenaGuessBetMapper.getWinNumberCount();
	}

	public void executeGuessWinResult(List<ArenaGuessBet> winBet) {
		arenaGuessBetMapper.executeGuessWinResult(winBet);
	}

	public void executeGuessFailResult(List<ArenaGuessBet> failBet) {
		arenaGuessBetMapper.executeGuessFailResult(failBet);
	}

	public Integer getArenaGuessBetCount(Integer valueOf) {
		return arenaGuessBetMapper.getArenaGuessBetCount(valueOf);
	}

	public List<ArenaGuessBet> sumGuessBetByGuessId(ArenaGuessBet tmpParam) {
		return arenaGuessBetMapper.sumGuessBetByGuessId(tmpParam);
	}

}