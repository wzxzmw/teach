package com.seentao.stpedu.common.sqlmap;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;

public interface CenterPrivateMessageTalkMapper {

	public abstract void insertCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk);
	
	public abstract void deleteCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk);
	
	public abstract void updateCenterPrivateMessageTalkByKey(CenterPrivateMessageTalk centerPrivateMessageTalk);
	
	public abstract List<CenterPrivateMessageTalk> selectSingleCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk);
	
	public abstract List<CenterPrivateMessageTalk> selectAllCenterPrivateMessageTalk();
	
	public abstract Integer countCenterPrivateMessageTalkByCondition(Map<String, Object> paramMap);
	
	public abstract List<CenterPrivateMessageTalk> selectCenterPrivateMessageTalkByCondition(Map<String, Object> paramMap);

	public abstract Integer queryCountPrivateMessageTalk(Map<String, Object> paramMap);

	public abstract List<CenterPrivateMessageTalk> queryByAgeprivateMessageTalk(Map<String, Object> paramMap);

	public abstract CenterPrivateMessageTalk selectCenterPrivateMessageTalkExistence(CenterPrivateMessageTalk centerPM);
	/*
	 * 取出最新一条消息
	 */
	public CenterPrivateMessageTalk queryCenterPrivateMessageTalkNew(@Param("userId") Integer userId);

}