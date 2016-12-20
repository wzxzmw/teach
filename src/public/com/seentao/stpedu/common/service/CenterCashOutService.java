package com.seentao.stpedu.common.service;


import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.CenterCashOutDao;
import com.seentao.stpedu.common.entity.CenterCashOut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CenterCashOutService{
	
	@Autowired
	private CenterCashOutDao centerCashOutDao;
	
	public String getCenterCashOut(Integer id) {
		CenterCashOut centerCashOut = new CenterCashOut();
		List<CenterCashOut> centerCashOutList = centerCashOutDao .selectSingleCenterCashOut(centerCashOut);
		if(centerCashOutList == null || centerCashOutList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(centerCashOutList);
	}
}