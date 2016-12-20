package com.seentao.stpedu.common.dao;


import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.common.sqlmap.CenterPrivateMessageTalkMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;


@Repository
public class CenterPrivateMessageTalkDao {

	@Autowired
	private CenterPrivateMessageTalkMapper centerPrivateMessageTalkMapper;
	
	
	public Integer insertCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk){
		centerPrivateMessageTalkMapper .insertCenterPrivateMessageTalk(centerPrivateMessageTalk);
		return centerPrivateMessageTalk.getTalkId();
	}
	
	public void deleteCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk){
		centerPrivateMessageTalkMapper .deleteCenterPrivateMessageTalk(centerPrivateMessageTalk);
	}
	
	public void updateCenterPrivateMessageTalkByKey(CenterPrivateMessageTalk centerPrivateMessageTalk){
		centerPrivateMessageTalkMapper .updateCenterPrivateMessageTalkByKey(centerPrivateMessageTalk);
	}
	
	public List<CenterPrivateMessageTalk> selectSingleCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk){
		return centerPrivateMessageTalkMapper .selectSingleCenterPrivateMessageTalk(centerPrivateMessageTalk);
	}
	
	public CenterPrivateMessageTalk selectCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk){
		List<CenterPrivateMessageTalk> centerPrivateMessageTalkList = centerPrivateMessageTalkMapper .selectSingleCenterPrivateMessageTalk(centerPrivateMessageTalk);
		if(centerPrivateMessageTalkList == null || centerPrivateMessageTalkList .size() == 0){
			return null;
		}
		return centerPrivateMessageTalkList .get(0);
	}
	
	public List<CenterPrivateMessageTalk> selectAllCenterPrivateMessageTalk(){
		return centerPrivateMessageTalkMapper .selectAllCenterPrivateMessageTalk();
	}
	
	
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerPrivateMessageTalkMapper.countCenterPrivateMessageTalkByCondition(paramMap);
	}
	
	public List<CenterPrivateMessageTalk> queryByPage(Map<String, Object> paramMap) {
		List<CenterPrivateMessageTalk> centerPrivateMessageTalkList = centerPrivateMessageTalkMapper .selectCenterPrivateMessageTalkByCondition(paramMap);
		if(centerPrivateMessageTalkList == null || centerPrivateMessageTalkList .size() == 0){
			return null;
		}
		return centerPrivateMessageTalkList;
	}

	/*
	 * 查询当前用户是发送则或者接受则的最新的一条消息
	 */
	
	public CenterPrivateMessageTalk queryCenterPrivateMessageTalkNew(Integer userId){
		return centerPrivateMessageTalkMapper.queryCenterPrivateMessageTalkNew(userId);
	}
	
	
	public CenterPrivateMessageTalk selectCenterPrivateMessageTalkExistence(CenterPrivateMessageTalk centerPM) {
		return centerPrivateMessageTalkMapper.selectCenterPrivateMessageTalkExistence(centerPM);
	}

}