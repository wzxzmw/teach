package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubRedPacketTypeDao;
import com.seentao.stpedu.common.entity.ClubRedPacketType;

@Service
public class ClubRedPacketTypeService{
	
	@Autowired
	private ClubRedPacketTypeDao clubRedPacketTypeDao;
	
	public List<ClubRedPacketType> getClubRedPacketType(ClubRedPacketType clubRedPacketType) {
		List<ClubRedPacketType> clubRedPacketTypeList = clubRedPacketTypeDao .selectSingleClubRedPacketType(clubRedPacketType);
		if(clubRedPacketTypeList == null || clubRedPacketTypeList .size() <= 0){
			return null;
		}
		
		return clubRedPacketTypeList;
	}
}