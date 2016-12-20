package com.seentao.stpedu.common.sqlmap;


import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterNews;

public interface CenterNewsMapper {

	public abstract void insertCenterNews(CenterNews centerNews);
	
	public abstract void deleteCenterNews(CenterNews centerNews);
	
	public abstract void updateCenterNewsByKey(CenterNews centerNews);
	
	public abstract List<CenterNews> selectSingleCenterNews(CenterNews centerNews);
	
	public abstract List<CenterNews> selectAllCenterNews();
	
	public abstract  Integer countCenterNewsByCondition(Map<String, Object> paramMap);
	
	public abstract List<CenterNews>  selectCenterNewsByCondition(Map<String, Object> paramMap);

	public abstract List<CenterNews> selectCenterNewsByUserId(CenterNews centerNews);

}