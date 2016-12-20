package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.sqlmap.CenterPrivateMessageMapper;

@Repository
public class CenterPrivateMessageDao {

	@Autowired
	private CenterPrivateMessageMapper centerPrivateMessageMapper;
	
	
	public void insertCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage){
		centerPrivateMessageMapper .insertCenterPrivateMessage(centerPrivateMessage);
	}
	
	public void deleteCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage){
		centerPrivateMessageMapper .deleteCenterPrivateMessage(centerPrivateMessage);
	}
	
	public void updateCenterPrivateMessageByKey(CenterPrivateMessage centerPrivateMessage){
		centerPrivateMessageMapper .updateCenterPrivateMessageByKey(centerPrivateMessage);
	}
	
	public List<CenterPrivateMessage> selectSingleCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage){
		return centerPrivateMessageMapper .selectSingleCenterPrivateMessage(centerPrivateMessage);
	}
	
	public CenterPrivateMessage selectCenterPrivateMessage(CenterPrivateMessage centerPrivateMessage){
		List<CenterPrivateMessage> centerPrivateMessageList = centerPrivateMessageMapper .selectSingleCenterPrivateMessage(centerPrivateMessage);
		if(centerPrivateMessageList == null || centerPrivateMessageList .size() == 0){
			return null;
		}
		return centerPrivateMessageList .get(0);
	}
	
	public List<CenterPrivateMessage> selectAllCenterPrivateMessage(){
		return centerPrivateMessageMapper .selectAllCenterPrivateMessage();
	}
	
	/**
	 *  获取私信信息
	 * @param paramMap
	 * @return
	 */
	public Integer queryCountPrivateMessage(Map<String, Object> paramMap) {
		return centerPrivateMessageMapper.queryCountPrivateMessage(paramMap);
	}

	public List<CenterPrivateMessage> queryByAgeprivateMessage(Map<String, Object> paramMap) {
		return centerPrivateMessageMapper.queryByAgeprivateMessage(paramMap);
	}

	public CenterPrivateMessage selectCenterPrivateMessageLast(CenterPrivateMessage selcenterPrivateMessage) {
		 return centerPrivateMessageMapper.selectCenterPrivateMessageLast(selcenterPrivateMessage);
	}

	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterPrivateMessage getEntityForDB(Map<String, Object> paramMap){
		CenterPrivateMessage tmp = new CenterPrivateMessage();
		tmp.setPrivateMessageId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectCenterPrivateMessage(tmp);
	}
	
	/**
	 *  获取私信信息 移动端
	 * @param paramMap
	 * @return
	 */
	public Integer queryCountPMageMobile(Map<String, Object> paramMap) {
		return centerPrivateMessageMapper.queryCountPMageMobile(paramMap);
	}

	public List<CenterPrivateMessage> queryByAgepMageMobile(Map<String, Object> paramMap) {
		return centerPrivateMessageMapper.queryByAgepMageMobile(paramMap);
	}
	//历史消息
	public List<CenterPrivateMessage> queryByOldMageMobile(Map<String, Object> paramMap) {
		return centerPrivateMessageMapper.queryByOldMageMobile(paramMap);
	}

	public CenterPrivateMessage selectCenterPriMessage(CenterPrivateMessage centerPrivateMessage){
		List<CenterPrivateMessage> centerPrivateMessageList = centerPrivateMessageMapper .selectCenterPriMessage(centerPrivateMessage);
		if(centerPrivateMessageList == null || centerPrivateMessageList .size() == 0){
			return null;
		}
		return centerPrivateMessageList .get(0);
	}

	public CenterPrivateMessage getLastCenterPrivateMessageByUserId(CenterPrivateMessage centerPrivateMessage) {
		List<CenterPrivateMessage> centerPrivateMessageList = centerPrivateMessageMapper .getLastCenterPrivateMessageByUserId(centerPrivateMessage);
		if(centerPrivateMessageList == null || centerPrivateMessageList .size() == 0){
			return null;
		}
		return centerPrivateMessageList .get(0);
	}
	
	public CenterPrivateMessage selectCenterPrivateMessageTalk(CenterPrivateMessage centerPrivateMessage){
		List<CenterPrivateMessage> centerPrivateMessageList = centerPrivateMessageMapper .selectCenterPrivateMessageTalk(centerPrivateMessage);
		if(centerPrivateMessageList == null || centerPrivateMessageList .size() == 0){
			return null;
		}
		return centerPrivateMessageList .get(0);
	}

}