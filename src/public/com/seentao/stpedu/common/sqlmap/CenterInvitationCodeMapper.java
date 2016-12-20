package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterInvitationCode;
import java.util.List;

public interface CenterInvitationCodeMapper {

	public abstract void insertCenterInvitationCode(CenterInvitationCode centerInvitationCode);
	
	public abstract void deleteCenterInvitationCode(CenterInvitationCode centerInvitationCode);
	
	public abstract void updateCenterInvitationCodeByKey(CenterInvitationCode centerInvitationCode)throws UpdateObjectException;
	
	public abstract List<CenterInvitationCode> selectSingleCenterInvitationCode(CenterInvitationCode centerInvitationCode);
	
	public abstract List<CenterInvitationCode> selectAllCenterInvitationCode();
	
	public abstract void batchInsertCenterInvitationCode(List<CenterInvitationCode> list);
	
}