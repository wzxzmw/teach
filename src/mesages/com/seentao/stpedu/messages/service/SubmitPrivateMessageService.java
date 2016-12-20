package com.seentao.stpedu.messages.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterPrivateMessageTalkDao;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterPrivateMessageTalk;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterPrivateMessageTalkService;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;
/**
 * @author cxw
 */
@Service
public class SubmitPrivateMessageService {

	@Autowired
	private CenterPrivateMessageTalkDao centerPrivateMessageTalkDao;

	@Autowired
	private CenterPrivateMessageTalkService centerPrivateMessageTalkService;

	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	
	/**
	 * 发私信
	 * @param userId  用户id
	 * @param pmObjectId  私信对象id
	 * @param pmContent   私信内容
	 * 1,验证两者之间是否在私信对话表存在，如果存在则插入私信表
	 * 2,如果不存在关系,则向私信对话表插入一条数据,然后插入私信表
	 * 3,更新私信状态
	 * 4,更新对话表最新一条私信id和创建时间,私信数量(在查询消息-私信列表的时候用)
	 * A-B B-A
	 * yy修改
	 */
	@Transactional
	public String submitPrivateMess(String userId, String pmObjectId, String pmContent) {
		try {
			//校验内容长度
			if(pmContent.equals("") || pmContent.length()>140){
				LogUtil.error(this.getClass(), "submitPrivateMess", "请输出少于140个字大于2个字");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_NUM_FAILED).toJSONString();
			}
			//TODO 此处可做缓存userId+pmObjectId或者pmObjectId+userId做key
			//查询发送者和接受者是否存在关系
			Integer taskId = null;//私信对话表主键id
			Integer count = null;//私信数量
			CenterPrivateMessageTalk centerPM = new CenterPrivateMessageTalk();
			centerPM.setSendUserId(Integer.valueOf(userId));
			centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
			CenterPrivateMessageTalk message = centerPrivateMessageTalkDao.selectCenterPrivateMessageTalkExistence(centerPM);
			if(null==message){//插入私信对话表,记录两者之间的关系
				centerPM.setSendUserId(Integer.valueOf(userId));
				centerPM.setReceiveUserId(Integer.valueOf(pmObjectId));
				centerPM.setCreateTime(TimeUtil.getCurrentTimestamp());
				centerPM.setPrivateMessageNum(0);
				centerPM.setLastPrivateMessageId(0);
				taskId= centerPrivateMessageTalkDao.insertCenterPrivateMessageTalk(centerPM);
				count = 0;
			}else{
				taskId = message.getTalkId();
				count = message.getPrivateMessageNum();
			}
			//插入私信表
			CenterPrivateMessage res = centerPrivateMessageTalkService.addPrivateMessage(userId, pmObjectId, pmContent,taskId);
			//TODO 对象空指针校验 手动抛出异常回滚
			//修改消息表,用户是否存在新私信消息
			//centerNewsStatusService.submitCenterNewStatus(pmObjectId);
			centerNewsStatusService.submitOrUpdateSome(Integer.valueOf(pmObjectId), 1);
			//更新(最新一条私信id,创建时间,私信数量)字段
			CenterPrivateMessageTalk centerPrivateMessageTalk = new CenterPrivateMessageTalk();
			centerPrivateMessageTalk.setTalkId(taskId);
			centerPrivateMessageTalk.setPrivateMessageNum(count+1);
			centerPrivateMessageTalk.setCreateTime(res.getCreateTime());
			centerPrivateMessageTalk.setLastPrivateMessageId(res.getPrivateMessageId());
			centerPrivateMessageTalkService.updateCenterPrivateMessageTalkByKey(centerPrivateMessageTalk);
			return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_INFORMATION_FAILED).toJSONString();
		}
	}
	
}
