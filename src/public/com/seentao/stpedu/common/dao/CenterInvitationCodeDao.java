package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterInvitationCode;
import com.seentao.stpedu.common.sqlmap.CenterInvitationCodeMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class CenterInvitationCodeDao {

	@Autowired
	private CenterInvitationCodeMapper centerInvitationCodeMapper;
	
	
	public void insertCenterInvitationCode(CenterInvitationCode centerInvitationCode){
		centerInvitationCodeMapper .insertCenterInvitationCode(centerInvitationCode);
	}
	
	public void deleteCenterInvitationCode(CenterInvitationCode centerInvitationCode){
		centerInvitationCodeMapper .deleteCenterInvitationCode(centerInvitationCode);
	}
	
	public void updateCenterInvitationCodeByKey(CenterInvitationCode centerInvitationCode)throws UpdateObjectException{
		try {
			centerInvitationCodeMapper .updateCenterInvitationCodeByKey(centerInvitationCode);
			
		} catch (UpdateObjectException e) {
			LogUtil.error(this.getClass(), "updateCenterInvitationCodeByKey", "updateCenterInvitationCodeByKey is error"+e);
			throw new UpdateObjectException(e);
		}
	}
	
	public List<CenterInvitationCode> selectSingleCenterInvitationCode(CenterInvitationCode centerInvitationCode){
		return centerInvitationCodeMapper .selectSingleCenterInvitationCode(centerInvitationCode);
	}
	
	public CenterInvitationCode selectCenterInvitationCode(CenterInvitationCode centerInvitationCode){
		List<CenterInvitationCode> centerInvitationCodeList = centerInvitationCodeMapper .selectSingleCenterInvitationCode(centerInvitationCode);
		if(centerInvitationCodeList == null || centerInvitationCodeList .size() == 0){
			return null;
		}
		return centerInvitationCodeList .get(0);
	}
	
	public List<CenterInvitationCode> selectAllCenterInvitationCode(){
		return centerInvitationCodeMapper .selectAllCenterInvitationCode();
	}
	
	public void batchInsertCenterInvitationCode(List<CenterInvitationCode> list){
		centerInvitationCodeMapper.batchInsertCenterInvitationCode(list);
	}
}