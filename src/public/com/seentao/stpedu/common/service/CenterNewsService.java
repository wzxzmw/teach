package com.seentao.stpedu.common.service;


import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.CenterNewsDao;
import com.seentao.stpedu.common.entity.CenterNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CenterNewsService{
	
	@Autowired
	private CenterNewsDao centerNewsDao;
	
	public CenterNews getCenterNews(CenterNews centerNews) {
		List<CenterNews> centerNewsList = centerNewsDao .selectSingleCenterNews(centerNews);
		if(centerNewsList == null || centerNewsList .size() <= 0){
			return null;
		}
		
		return centerNewsList.get(0);
	}
	
	public void deleteCenterNews(CenterNews centerNews){
		centerNewsDao.deleteCenterNews(centerNews);
	}
	
	public void insertCenterNews(CenterNews centerNews){
		centerNewsDao.insertCenterNews(centerNews);
	}

	/** 
	* @Title: getCenterNews 
	* @Description: 获取用户最后一条消息
	* @param userId
	* @return CenterNews
	* @author liulin
	* @date 2016年7月19日 上午11:11:35
	*/
	public CenterNews getCenterNews(Integer userId) {
		CenterNews centerNews = new CenterNews();
		centerNews.setUserId(userId);
		List<CenterNews> centerNewsList = centerNewsDao.selectCenterNewsByUserId(centerNews);
		if(centerNewsList == null || centerNewsList .size() <= 0){
			return null;
		}
		return centerNewsList.get(0);
	}
}