package com.seentao.stpedu.common.sqlmap;


import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubRedPacket;

public interface ClubRedPacketMapper {

	public abstract void insertClubRedPacket(ClubRedPacket clubRedPacket);
	
	public abstract void deleteClubRedPacket(ClubRedPacket clubRedPacket);
	
	public abstract void updateClubRedPacketByKey(ClubRedPacket clubRedPacket);
	
	public abstract List<ClubRedPacket> selectSingleClubRedPacket(ClubRedPacket clubRedPacket);
	
	public abstract List<ClubRedPacket> selectAllClubRedPacket();

	public abstract List<ClubRedPacket> getClubRedPacketList(Map<String, Integer> param);

	public abstract Integer getClubRedPacketCount(Map<String, Integer> param);
	
}