package com.seentao.stpedu.common.dao;


import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.sqlmap.ClubRedPacketMapper;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;


@Repository
public class ClubRedPacketDao {

	@Autowired
	private ClubRedPacketMapper clubRedPacketMapper;
	
	
	public void insertClubRedPacket(ClubRedPacket clubRedPacket){
		clubRedPacketMapper .insertClubRedPacket(clubRedPacket);
	}
	
	public void deleteClubRedPacket(ClubRedPacket clubRedPacket){
		clubRedPacketMapper .deleteClubRedPacket(clubRedPacket);
	}
	
	public void updateClubRedPacketByKey(ClubRedPacket clubRedPacket){
		clubRedPacketMapper .updateClubRedPacketByKey(clubRedPacket);
	}
	
	public List<ClubRedPacket> selectSingleClubRedPacket(ClubRedPacket clubRedPacket){
		return clubRedPacketMapper .selectSingleClubRedPacket(clubRedPacket);
	}
	
	public ClubRedPacket selectClubRedPacket(ClubRedPacket clubRedPacket){
		List<ClubRedPacket> clubRedPacketList = clubRedPacketMapper .selectSingleClubRedPacket(clubRedPacket);
		if(clubRedPacketList == null || clubRedPacketList .size() == 0){
			return null;
		}
		return clubRedPacketList .get(0);
	}
	
	public List<ClubRedPacket> selectAllClubRedPacket(){
		return clubRedPacketMapper .selectAllClubRedPacket();
	}

	public List<ClubRedPacket> getClubRedPacketList(Map<String, Integer> param) {
		return clubRedPacketMapper.getClubRedPacketList(param);
	}

	public Integer getClubRedPacketCount(Map<String, Integer> param) {
		return clubRedPacketMapper.getClubRedPacketCount(param);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubRedPacket getEntityForDB(Map<String, Object> paramMap){
		ClubRedPacket tmp = new ClubRedPacket();
		tmp.setRedPacketId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectClubRedPacket(tmp);
	}
}