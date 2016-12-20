package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubBackgroundColorDao;
import com.seentao.stpedu.common.entity.ClubBackgroundColor;

@Service
public class ClubBackgroundColorService{
	
	@Autowired
	private ClubBackgroundColorDao clubBackgroundColorDao;
	
	public String getClubBackgroundColor(Integer id) {
		ClubBackgroundColor clubBackgroundColor = new ClubBackgroundColor();
		List<ClubBackgroundColor> clubBackgroundColorList = clubBackgroundColorDao .selectSingleClubBackgroundColor(clubBackgroundColor);
		if(clubBackgroundColorList == null || clubBackgroundColorList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(clubBackgroundColorList);
	}
}