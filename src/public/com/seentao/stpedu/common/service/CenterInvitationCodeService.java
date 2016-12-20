package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.dao.CenterInvitationCodeDao;
import com.seentao.stpedu.common.entity.CenterInvitationCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class CenterInvitationCodeService{
	
	@Autowired
	private CenterInvitationCodeDao centerInvitationCodeDao;
	
	public void addCenterInvirationCode(CenterInvitationCode centerInvitationCode){
		centerInvitationCodeDao.insertCenterInvitationCode(centerInvitationCode);
	}
	
	public String getCenterInvitationCode(CenterInvitationCode centerInvitationCode) {
		List<CenterInvitationCode> centerInvitationCodeList = centerInvitationCodeDao .selectSingleCenterInvitationCode(centerInvitationCode);
		if(centerInvitationCodeList == null || centerInvitationCodeList .size() <= 0){
			return null;
		}
		return JSONArray.toJSONString(centerInvitationCodeList);
	}
	
	public CenterInvitationCode getCenterInvitationCodeOne(CenterInvitationCode centerInvitationCode) {
		List<CenterInvitationCode> centerInvitationCodeList = centerInvitationCodeDao .selectSingleCenterInvitationCode(centerInvitationCode);
		if(centerInvitationCodeList == null || centerInvitationCodeList .size() <= 0){
			return null;
		}
		return centerInvitationCodeList.get(0);
	}
	
	public void updateCenterInvitationCode(CenterInvitationCode centerInvitationCode) throws UpdateObjectException{
		try {
			centerInvitationCodeDao.updateCenterInvitationCodeByKey(centerInvitationCode);
		} catch (UpdateObjectException e) {
			LogUtil.error(this.getClass(), "updateCenterInvitationCodeByKey", "update is error", e);
			throw new UpdateObjectException(e);
		}
	}
	
	public void batchInsertCenterInvitationCode(List<CenterInvitationCode> list){
		centerInvitationCodeDao.batchInsertCenterInvitationCode(list);
	}
}