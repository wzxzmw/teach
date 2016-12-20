package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ArenaJoinClub;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArenaJoinClubMapper {

	public abstract void insertArenaJoinClub(ArenaJoinClub arenaJoinClub);
	
	public abstract void deleteArenaJoinClub(ArenaJoinClub arenaJoinClub);
	
	public abstract void updateArenaJoinClubByKey(ArenaJoinClub arenaJoinClub);
	
	public abstract List<ArenaJoinClub> selectSingleArenaJoinClub(ArenaJoinClub arenaJoinClub);
	
	public abstract List<ArenaJoinClub> selectAllArenaJoinClub();
	/**根据赛事id查询俱乐部
	 * @param gameEventId
	 * @return
	 */
	public abstract List<Integer> queryArenaJoinClubByCompId(@Param("gameEventId")Integer gameEventId);
	
}