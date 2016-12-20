package com.seentao.stpedu.messages.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.CenterPrivateMessageDao;
import com.seentao.stpedu.common.dao.CenterUserDao;
import com.seentao.stpedu.common.entity.CenterPrivateMessage;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Service
public class GetPrivateMessagesService {


	@Autowired
	private CenterUserDao centerUserDao;

	@Autowired
	private CenterPrivateMessageDao centerPrivateMessageDao;
	/**
	 * 获取私信信息
	 * @param userId
	 * @param privateMessageId 私信id
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @return
	 */
	public String getprivatemessages(String userId, int start, int limit,String privateMessageId, int inquireType) {

		try { 

			CenterUser centerUser = null;
			CenterPrivateMessage centerPrivateMessage = new CenterPrivateMessage();
			centerPrivateMessage.setPrivateMessageId(Integer.valueOf(privateMessageId));
			CenterPrivateMessage message2 = centerPrivateMessageDao.selectCenterPrivateMessage(centerPrivateMessage);
			if(message2 == null){
				JSONObject json = new JSONObject();
				json.put("getprivatemessages", new JSONArray());
				return Common.getReturn(AppErrorCode.SUCCESS, "", json).toJSONString();
			}
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("talkId", message2.getTalkId());
			//调用分页组件
			//根据userid查询私信表 获取 接受者id = userid 的所有私信
			QueryPage<CenterPrivateMessage> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterPrivateMessage.class, "queryCountPrivateMessage", "queryByAgeprivateMessage");
			//msg错误码
			if(appQueryPage.getState()){

				for (CenterPrivateMessage privateMessage : appQueryPage.getList()) {

					//根据获取到的 私信发布者id 获取基本信息'昵称';'真实姓名';'头像ID';
					centerUser = new CenterUser();
					centerUser.setUserId(privateMessage.getSendUserId());
					CenterUser userOne = centerUserDao.selectCenterUserOne(centerUser);
					privateMessage.setNickName(userOne.getNickName());
					privateMessage.setRealName(userOne.getRealName());

					//根据头像ID查询getHeadImgId链接地址
					String msg = Common.getImgUrl(userOne.getHeadImgId(),BusinessConstant.DEFAULT_IMG_USER);
					privateMessage.setFilePath(Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
					//判断私信发布者是否是登陆者
					if(privateMessage.getSendUserId()==Integer.valueOf(userId)){
						privateMessage.setPmIsLoginUser(1);
					}else{
						privateMessage.setPmIsLoginUser(0);
					}
//					//向用户消息状态表改是否有新私信
//					CenterNewsStatus centerNewsStatus = null;
//					centerNewsStatus = new CenterNewsStatus();
//					centerNewsStatus.setUserId(privateMessage.getReceiveUserId());
//					CenterNewsStatus status = centerNewsStatusDao.selectCenterNewsStatus(centerNewsStatus);
//					if(status==null){
//						centerNewsStatus.setUserId(privateMessage.getReceiveUserId());
//						centerNewsStatus.setIsNewPrivateMessage(1);
//						centerNewsStatus.setIsNewAttention(0);
//						centerNewsStatus.setIsNewQuestionInvite(0);
//						centerNewsStatus.setIsNewGameInvite(0);
//						centerNewsStatus.setIsNewSysNews(0);
//						centerNewsStatusDao.insertCenterNewsStatus(centerNewsStatus);
//					}else{
//						centerNewsStatus.setStatusId(status.getStatusId());
//						centerNewsStatus.setIsNewPrivateMessage(1);
//						centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
//					}
				}
				LogUtil.info(this.getClass(), "getprivatemessages", "获取私信成功");
				//获取私信对方userid
				CenterPrivateMessage centerP = new CenterPrivateMessage();
				centerP.setTalkId(message2.getTalkId());
				CenterPrivateMessage message = centerPrivateMessageDao.selectCenterPriMessage(centerP);

				//判断是否是登陆者
				if(message.getReceiveUserId() == Integer.parseInt(userId)){
					//获取私信对方nickname
					centerUser = new CenterUser();
					centerUser.setUserId(message.getSendUserId());
					CenterUser user = centerUserDao.selectCenterUserOne(centerUser);
					paramMap.clear();
					paramMap.put("pmReceiverId", message.getSendUserId().toString());
					paramMap.put("pmReceiverNickname", user.getNickName());
				}else{
					//获取私信对方nickname
					centerUser = new CenterUser();
					centerUser.setUserId(message.getReceiveUserId());
					CenterUser user = centerUserDao.selectCenterUserOne(centerUser);
					paramMap.clear();
					paramMap.put("pmReceiverId", message.getReceiveUserId().toString());
					paramMap.put("pmReceiverNickname", user.getNickName());
				}
			}
			return appQueryPage.getMessageJSONObject("privateMessages", paramMap).toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_PRIVATE_INFORMATION_FAILED).toJSONString();
		}

	}

}
