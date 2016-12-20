package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.ClubRedPacketType;
import com.seentao.stpedu.common.sqlmap.ClubRedPacketTypeMapper;


@Repository
public class ClubRedPacketTypeDao {

	@Autowired
	private ClubRedPacketTypeMapper clubRedPacketTypeMapper;
	
	
	public void insertClubRedPacketType(ClubRedPacketType clubRedPacketType){
		clubRedPacketTypeMapper .insertClubRedPacketType(clubRedPacketType);
	}
	
	public void deleteClubRedPacketType(ClubRedPacketType clubRedPacketType){
		clubRedPacketTypeMapper .deleteClubRedPacketType(clubRedPacketType);
	}
	
	public void updateClubRedPacketTypeByKey(ClubRedPacketType clubRedPacketType){
		clubRedPacketTypeMapper .updateClubRedPacketTypeByKey(clubRedPacketType);
	}
	
	public List<ClubRedPacketType> selectSingleClubRedPacketType(ClubRedPacketType clubRedPacketType){
		return clubRedPacketTypeMapper .selectSingleClubRedPacketType(clubRedPacketType);
	}
	
	public ClubRedPacketType selectClubRedPacketType(ClubRedPacketType clubRedPacketType){
		List<ClubRedPacketType> clubRedPacketTypeList = clubRedPacketTypeMapper .selectSingleClubRedPacketType(clubRedPacketType);
		if(clubRedPacketTypeList == null || clubRedPacketTypeList .size() == 0){
			return null;
		}
		return clubRedPacketTypeList .get(0);
	}
	
	public List<ClubRedPacketType> selectAllClubRedPacketType(){
		return clubRedPacketTypeMapper .selectAllClubRedPacketType();
	}
}