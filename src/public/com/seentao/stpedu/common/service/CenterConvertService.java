package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.CenterConvertDao;
import com.seentao.stpedu.common.entity.CenterConvert;

@Service
public class CenterConvertService{
	
	@Autowired
	private CenterConvertDao centerConvertDao;
	
	public String getCenterConvert(Integer id) {
		CenterConvert centerConvert = new CenterConvert();
		List<CenterConvert> centerConvertList = centerConvertDao .selectSingleCenterConvert(centerConvert);
		if(centerConvertList == null || centerConvertList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(centerConvertList);
	}

	public void insertCenterConvert(CenterConvert centerConvert) {
		centerConvertDao.insertCenterConvert(centerConvert);
	}
}