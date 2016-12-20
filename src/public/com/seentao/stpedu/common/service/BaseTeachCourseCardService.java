package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.TeachCourseCardDao;
import com.seentao.stpedu.common.entity.TeachCourseCard;

@Service
public class BaseTeachCourseCardService{
	
	@Autowired
	private TeachCourseCardDao teachCourseCardDao;
	
	public String getTeachCourseCard(Integer id) {
		TeachCourseCard teachCourseCard = new TeachCourseCard();
		List<TeachCourseCard> teachCourseCardList = teachCourseCardDao .selectSingleTeachCourseCard(teachCourseCard);
		if(teachCourseCardList == null || teachCourseCardList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(teachCourseCardList);
	}
}