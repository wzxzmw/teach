package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRedPacketType;
import java.util.List;

public interface ClubRedPacketTypeMapper {

	public abstract void insertClubRedPacketType(ClubRedPacketType clubRedPacketType);
	
	public abstract void deleteClubRedPacketType(ClubRedPacketType clubRedPacketType);
	
	public abstract void updateClubRedPacketTypeByKey(ClubRedPacketType clubRedPacketType);
	
	public abstract List<ClubRedPacketType> selectSingleClubRedPacketType(ClubRedPacketType clubRedPacketType);
	
	public abstract List<ClubRedPacketType> selectAllClubRedPacketType();
	
}