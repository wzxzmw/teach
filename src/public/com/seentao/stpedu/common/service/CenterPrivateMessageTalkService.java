package com.seentao.stpedu.common.service;


import java.util.List;
import com.seentao.stpedu.common.dao.CenterPrivateMessageDao;
import com.seentao.stpedu.common.dao.CenterPrivateMessageTalkDao;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CenterPrivateMessageTalkService{

	@Autowired
	private CenterPrivateMessageDao  centerPrivateMessageDao;
	@Autowired
	private CenterPrivateMessageTalkDao centerPrivateMessageTalkDao;

	public CenterPrivateMessageTalk getCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk) {
		List<CenterPrivateMessageTalk> centerPrivateMessageTalkList = centerPrivateMessageTalkDao .selectSingleCenterPrivateMessageTalk(centerPrivateMessageTalk);
		if(centerPrivateMessageTalkList == null || centerPrivateMessageTalkList .size() <= 0){
			return null;
		}

		return centerPrivateMessageTalkList.get(0);
	}
	/**
	 * deleteCenterPrivateMessageTalk(删除私信)   
	 * @author ligs
	 * @date 2016年7月4日 下午8:41:37
	 * @return
	 */
	public void deleteCenterPrivateMessageTalk(CenterPrivateMessageTalk centerPrivateMessageTalk){
		centerPrivateMessageTalkDao.deleteCenterPrivateMessageTalk(centerPrivateMessageTalk);
	}
	/**
	 * updateCenterPrivateMessageTalkByKey(修改私信)   
	 * @author ligs
	 * @date 2016年7月4日 下午9:04:27
	 * @return
	 */
	public void updateCenterPrivateMessageTalkByKey(CenterPrivateMessageTalk centerPrivateMessageTalk){
		centerPrivateMessageTalkDao.updateCenterPrivateMessageTalkByKey(centerPrivateMessageTalk);
	}
	/**
	 * 添加私信表信息
	 * @param userId
	 * @param pmObjectId
	 * @param pmContent
	 * @author cxw
	 */
	public CenterPrivateMessage addPrivateMessage(String userId,String pmObjectId,String pmContent,Integer taskId){
		CenterPrivateMessage centerPrivateM = new CenterPrivateMessage();
		try {
			centerPrivateM.setTalkId(taskId);
			centerPrivateM.setSendUserId(Integer.valueOf(userId));
			centerPrivateM.setReceiveUserId(Integer.valueOf(pmObjectId));
			centerPrivateM.setCreateTime(TimeUtil.getCurrentTimestamp());
			centerPrivateM.setContent(pmContent);
			centerPrivateMessageDao.insertCenterPrivateMessage(centerPrivateM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return centerPrivateM;
	}

	public void addPrivateMessageI(String userId,String pmObjectId,String pmContent){
		LogUtil.info(this.getClass(), "addPrivateMessageI", " sendUserId：【userId："+userId+"】，receiveId【pmObjectId："+pmObjectId+"】，【pmContent："+pmContent+"】");
		try {
			CenterPrivateMessageTalk centerPM = null;
			centerPM = new CenterPrivateMessageTalk();
			centerPM.setSendUserId(Integer.valueOf(pmObjectId));
			centerPM.setReceiveUserId(Integer.valueOf(userId));
			CenterPrivateMessageTalk talk = centerPrivateMessageTalkDao.selectCenterPrivateMessageTalk(centerPM);
			if(talk == null){   
				centerPM = new CenterPrivateMessageTalk();
				centerPM.setSendUserId(Integer.valueOf(userId));
				centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPM.setCreateTime(TimeUtil.getCurrentTimestamp());
				centerPM.setPrivateMessageNum(0);
				centerPM.setLastPrivateMessageId(0);
				Integer messageTalk = centerPrivateMessageTalkDao.insertCenterPrivateMessageTalk(centerPM);
				//添加私信表         
				CenterPrivateMessage centerPrivateM = new CenterPrivateMessage();
				centerPrivateM.setTalkId(messageTalk);
				centerPrivateM.setSendUserId(Integer.valueOf(userId));
				centerPrivateM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPrivateM.setCreateTime(TimeUtil.getCurrentTimestamp());
				centerPrivateM.setContent(pmContent);
				centerPrivateMessageDao.insertCenterPrivateMessage(centerPrivateM);
			}else{
				//添加到私信对话表
				//		    centerPM = new CenterPrivateMessageTalk();
				//			centerPM.setSendUserId(Integer.valueOf(userId));
				//			centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
				//			CenterPrivateMessageTalk talkId = centerPrivateMessageTalkDao.selectCenterPrivateMessageTalk(centerPM);
				//添加私信表
				CenterPrivateMessage centerPrivateM = new CenterPrivateMessage();
				centerPrivateM.setTalkId(talk.getTalkId());
				centerPrivateM.setSendUserId(Integer.valueOf(userId));
				centerPrivateM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPrivateM.setCreateTime(TimeUtil.getCurrentTimestamp());
				centerPrivateM.setContent(pmContent);
				centerPrivateMessageDao.insertCenterPrivateMessage(centerPrivateM);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 修改私信表字段
	 * @param userId
	 * @param pmObjectId
	 * @author cxw
	 */
	public void updatePrivateMessage(String userId,String pmObjectId){

		try {
			CenterPrivateMessage centerPrivateMessage =  null;
			centerPrivateMessage = new CenterPrivateMessage();
			centerPrivateMessage.setSendUserId(Integer.valueOf(userId));
			centerPrivateMessage.setReceiveUserId(Integer.valueOf(pmObjectId));
			CenterPrivateMessage message2 = centerPrivateMessageDao.selectCenterPrivateMessage(centerPrivateMessage);
			
			centerPrivateMessage = new CenterPrivateMessage();
			centerPrivateMessage.setTalkId(message2.getTalkId());
			CenterPrivateMessage message3 = centerPrivateMessageDao.selectCenterPriMessage(centerPrivateMessage);
			
			CenterPrivateMessageTalk centerPM = null;
			centerPM = new CenterPrivateMessageTalk();
			centerPM.setSendUserId(Integer.valueOf(pmObjectId));
			centerPM.setReceiveUserId(Integer.valueOf(userId));
			CenterPrivateMessageTalk talk = centerPrivateMessageTalkDao.selectCenterPrivateMessageTalk(centerPM);
			if(talk==null){
				CenterPrivateMessageTalk centerPM1 = null;
				centerPM1 = new CenterPrivateMessageTalk();
				centerPM1.setSendUserId(Integer.valueOf(userId));
				centerPM1.setReceiveUserId(Integer.valueOf(pmObjectId));
				CenterPrivateMessageTalk message = centerPrivateMessageTalkDao.selectCenterPrivateMessageTalk(centerPM1);
				
				//修改私信对话表字段
				centerPM = new CenterPrivateMessageTalk();
				Integer privateMessageNum = message.getPrivateMessageNum();
				centerPM.setTalkId(message.getTalkId());
				centerPM.setSendUserId(Integer.valueOf(userId));
				centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPM.setPrivateMessageNum(privateMessageNum+1);
				centerPM.setLastPrivateMessageId(message3.getPrivateMessageId());
				centerPrivateMessageTalkDao.updateCenterPrivateMessageTalkByKey(centerPM);
			}else{
				//修改私信对话表字段
				centerPM = new CenterPrivateMessageTalk();
				Integer privateMessageNum = talk.getPrivateMessageNum();
				centerPM.setTalkId(talk.getTalkId());
				centerPM.setSendUserId(Integer.valueOf(userId));
				centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPM.setPrivateMessageNum(privateMessageNum+1);
				centerPM.setLastPrivateMessageId(message3.getPrivateMessageId());
				centerPrivateMessageTalkDao.updateCenterPrivateMessageTalkByKey(centerPM);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}