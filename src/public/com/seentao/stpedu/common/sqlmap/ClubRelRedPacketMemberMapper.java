package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import java.util.List;

public interface ClubRelRedPacketMemberMapper {

	public abstract void insertClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember);
	
	public abstract void deleteClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember);
	
	public abstract void updateClubRelRedPacketMemberByKey(ClubRelRedPacketMember clubRelRedPacketMember);
	
	public abstract List<ClubRelRedPacketMember> selectSingleClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember);
	
	public abstract List<ClubRelRedPacketMember> selectAllClubRelRedPacketMember();

	public abstract void batchInsertClubRelRedPacketMember(List<ClubRelRedPacketMember> clubRelRedPacketMemberList);
	
}