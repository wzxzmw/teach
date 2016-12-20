package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import java.util.List;
import java.util.Map;

public interface CenterPrivateMessageMapper {

	public abstract void insertCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage);
	
	public abstract void deleteCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage);
	
	public abstract void updateCenterPrivateMessageByKey(CenterPrivateMessage centerPrivateMessage);
	
	public abstract List<CenterPrivateMessage> selectSingleCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage);
	
	public abstract List<CenterPrivateMessage> selectAllCenterPrivateMessage();

	public abstract Integer queryCountPrivateMessage(Map<String, Object> paramMap);

	public abstract List<CenterPrivateMessage> queryByAgeprivateMessage(Map<String, Object> paramMap);

	public abstract CenterPrivateMessage selectCenterPrivateMessageLast(CenterPrivateMessage selcenterPrivateMessage);

	public abstract Integer queryCountPMageMobile(Map<String, Object> paramMap);

	public abstract List<CenterPrivateMessage> queryByAgepMageMobile(Map<String, Object> paramMap);

	public abstract List<CenterPrivateMessage> queryByOldMageMobile(Map<String, Object> paramMap);

	public abstract List<CenterPrivateMessage> selectCenterPriMessage(CenterPrivateMessage centerPrivateMessage);

	public abstract List<CenterPrivateMessage> getLastCenterPrivateMessageByUserId(
			CenterPrivateMessage centerPrivateMessage);

	public abstract List<CenterPrivateMessage> selectCenterPrivateMessageTalk(
			CenterPrivateMessage centerPrivateMessage);
	
}