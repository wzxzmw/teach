package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.CenterPrivateMessageDao;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;

@Service
public class CenterPrivateMessageService{
	
	@Autowired
	private CenterPrivateMessageDao centerPrivateMessageDao;
	
	public CenterPrivateMessage getCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage) {
		List<CenterPrivateMessage> centerPrivateMessageList = centerPrivateMessageDao .selectSingleCenterPrivateMessage(centerPrivateMessage);
		if(centerPrivateMessageList == null || centerPrivateMessageList .size() <= 0){
			return null;
		}
		
		return centerPrivateMessageList.get(0);
	}
	
	public void deleteCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage){
		centerPrivateMessageDao.deleteCenterPrivateMessage(centerPrivateMessage);
	}


	public CenterPrivateMessage selectCenterPrivateMessageLast(CenterPrivateMessage selcenterPrivateMessage) {
		return centerPrivateMessageDao.selectCenterPrivateMessageLast(selcenterPrivateMessage);
	}

	/** 
	* @Title: getLastCenterPrivateMessageByUserId 
	* @Description: 根据userID获取用户最后一条私信
	* @param userId
	* @return CenterPrivateMessage
	* @author liulin
	* @date 2016年8月4日 下午3:48:01
	*/
	public CenterPrivateMessage getLastCenterPrivateMessageByUserId(Integer userId) {
		CenterPrivateMessage centerPrivateMessage = new CenterPrivateMessage();
		centerPrivateMessage.setReceiveUserId(userId);
		return centerPrivateMessageDao.getLastCenterPrivateMessageByUserId(centerPrivateMessage);
	}
}