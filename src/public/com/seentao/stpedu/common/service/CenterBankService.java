package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.CenterBankDao;
import com.seentao.stpedu.common.entity.CenterBank;
import com.seentao.stpedu.persionalcenter.model.AccountTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CenterBankService{
	
	@Autowired
	private CenterBankDao centerBankDao;
	
	public String getCenterBank(Integer id) {
		CenterBank centerBank = new CenterBank();
		List<CenterBank> centerBankList = centerBankDao .selectSingleCenterBank(centerBank);
		if(centerBankList == null || centerBankList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(centerBankList);
	}
	
	
	
	public List<AccountTypes> getAllAccountTypes(){
		List<AccountTypes> centerBankList=centerBankDao.selectAllAccountTypes();
		if(centerBankList == null || centerBankList .size() <= 0){
			return new ArrayList<AccountTypes>();
		}
		return centerBankList;
		
	}
	
	
}