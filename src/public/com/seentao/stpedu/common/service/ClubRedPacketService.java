package com.seentao.stpedu.common.service;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.ClubRedPacketDao;
import com.seentao.stpedu.common.entity.ClubRedPacket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubRedPacketService{
	
	@Autowired
	private ClubRedPacketDao clubRedPacketDao;
	
	public List<ClubRedPacket> getClubRedPacket(ClubRedPacket clubRedPacket) {
		List<ClubRedPacket> clubRedPacketList = clubRedPacketDao .selectSingleClubRedPacket(clubRedPacket);
		if(clubRedPacketList == null || clubRedPacketList .size() <= 0){
			return null;
		}
		return clubRedPacketList;
	}

	public List<ClubRedPacket> getClubRedPacketList(Map<String, Integer> param) {
		return clubRedPacketDao. getClubRedPacketList(param) ;
	}

	public Integer getClubRedPacketCount(Map<String, Integer> param) {
		return clubRedPacketDao.getClubRedPacketCount(param);
	}

	public void insertClubRedPacket(ClubRedPacket clubRedPacket) {
		clubRedPacketDao.insertClubRedPacket(clubRedPacket);
	}
	
}