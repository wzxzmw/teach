package com.seentao.stpedu.common.dao;


import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.sqlmap.CenterNewsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;


@Repository
public class CenterNewsDao {

	@Autowired
	private CenterNewsMapper centerNewsMapper;
	
	
	public void insertCenterNews(CenterNews centerNews){
		centerNewsMapper .insertCenterNews(centerNews);
	}
	
	public void deleteCenterNews(CenterNews centerNews){
		centerNewsMapper .deleteCenterNews(centerNews);
	}
	
	public void updateCenterNewsByKey(CenterNews centerNews){
		centerNewsMapper .updateCenterNewsByKey(centerNews);
	}
	
	public List<CenterNews> selectSingleCenterNews(CenterNews centerNews){
		return centerNewsMapper .selectSingleCenterNews(centerNews);
	}
	
	public CenterNews selectCenterNews(CenterNews centerNews){
		List<CenterNews> centerNewsList = centerNewsMapper .selectSingleCenterNews(centerNews);
		if(centerNewsList == null || centerNewsList .size() == 0){
			return null;
		}
		return centerNewsList .get(0);
	}
	
	public List<CenterNews> selectAllCenterNews(){
		return centerNewsMapper .selectAllCenterNews();
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerNewsMapper.countCenterNewsByCondition(paramMap);
	}
	
	public List<CenterNews> queryByPage(Map<String, Object> paramMap) {
		List<CenterNews> centerNewsList = centerNewsMapper .selectCenterNewsByCondition(paramMap);
		if(centerNewsList == null || centerNewsList .size() == 0){
			return null;
		}
		return centerNewsList;
	}

	public List<CenterNews> selectCenterNewsByUserId(CenterNews centerNews) {
		return centerNewsMapper .selectCenterNewsByUserId(centerNews);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterNews getEntityForDB(Map<String, Object> paramMap){
		CenterNews tmp = new CenterNews();
		tmp.setNewsId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectCenterNews(tmp);
	}
}