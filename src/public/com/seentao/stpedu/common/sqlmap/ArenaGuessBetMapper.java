package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.ArenaGuessBet;

public interface ArenaGuessBetMapper {

	public abstract void insertArenaGuessBet(ArenaGuessBet arenaGuessBet);
	
	public abstract void deleteArenaGuessBet(ArenaGuessBet arenaGuessBet);
	
	public abstract void updateArenaGuessBetByKey(ArenaGuessBet arenaGuessBet);
	
	public abstract List<ArenaGuessBet> selectSingleArenaGuessBet(ArenaGuessBet arenaGuessBet);
	
	public abstract List<ArenaGuessBet> selectAllArenaGuessBet();

	public abstract List<ArenaGuessBet> getMaxBetList(ArenaGuessBet arenaguessbet);

	public abstract Integer getMaxBetCount();

	public abstract List<ArenaGuessBet> getWinNumberList(ArenaGuessBet arenaguessbet);

	public abstract Integer getWinNumberCount();

	public abstract void executeGuessWinResult(List<ArenaGuessBet> winBet);

	public abstract void executeGuessFailResult(List<ArenaGuessBet> failBet);

	public abstract Integer getArenaGuessBetCount(@Param(value="userId")Integer userId);

	public abstract List<ArenaGuessBet> sumGuessBetByGuessId(ArenaGuessBet tmpParam);

	
}