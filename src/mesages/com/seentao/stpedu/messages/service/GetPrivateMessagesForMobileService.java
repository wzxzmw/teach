package com.seentao.stpedu.messages.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.seentao.stpedu.utils.StringUtil;

@Service
public class GetPrivateMessagesForMobileService {

	@Autowired
	private CenterUserDao centerUserDao;

	@Autowired
	private CenterPrivateMessageDao centerPrivateMessageDao;
	/**
	 * 获取私信信息 移动端
	 * @param userId
	 * @param memberId
	 * @param limit
	 * @param sortType  获取信息顺序 1:最新的；2:历史信息；
	 * @param newPrivateMessageId 最新的私信id
	 * @param oldPrivateMessageId 最早的私信id
	 * @param inquireType
	 * @author cxw
	 * @return 
	 * @return
	 */
	public String getPrivateMessageForMobile(String userId, String memberId, int limit, int sortType,
			String newPrivateMessageId, String oldPrivateMessageId, int inquireType) {

		//判断发送者 与 接受者是否存在私信
		CenterPrivateMessage centerPMTalk = null;
		centerPMTalk = new CenterPrivateMessage();
		centerPMTalk.setSendUserId(Integer.valueOf(userId));
		centerPMTalk.setReceiveUserId(Integer.valueOf(memberId));
		CenterPrivateMessage list = centerPrivateMessageDao.selectCenterPrivateMessageTalk(centerPMTalk);
		if(list!=null){
			//获取私信信息
			Integer talkId = list.getTalkId();
			return this.getprivagte(userId, memberId, limit, sortType, newPrivateMessageId, oldPrivateMessageId, inquireType, talkId);
		}else{
			//判断发送者 与 接受者是否存在私信
			centerPMTalk = new CenterPrivateMessage();
			centerPMTalk.setSendUserId(Integer.valueOf(memberId));
			centerPMTalk.setReceiveUserId(Integer.valueOf(userId));
			CenterPrivateMessage list2 = centerPrivateMessageDao.selectCenterPrivateMessageTalk(centerPMTalk);
			if(list2==null){
				//不存在返回
				LogUtil.error(this.getClass(), "getprivatemessages", "没有对话");
				JSONObject json = new JSONObject();
				json.put("newPrivateMessageId", newPrivateMessageId);
				json.put("privateMessages", new JSONArray());
				return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
			}else{
				//获取私信信息
				Integer talkId = list2.getTalkId();
				return this.getprivagte(userId, memberId, limit, sortType, newPrivateMessageId, oldPrivateMessageId, inquireType,talkId);
			}
		}

	}

	private String getprivagte(String userId, String memberId, int limit, int sortType,
			String newPrivateMessageId, String oldPrivateMessageId, int inquireType, Integer talkId){
		
		int start = 0;
		int newId = StringUtil.isEmpty(newPrivateMessageId) ? 0 : Integer.valueOf(newPrivateMessageId).intValue();
		int oldId = StringUtil.isEmpty(oldPrivateMessageId) ? 0 : Integer.valueOf(oldPrivateMessageId).intValue();
		
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("talkId", talkId);
		QueryPage<CenterPrivateMessage> appQueryPage = null;
		
		if(sortType == 1){
			
			//第一次请求 需要调历史消息
			if(newId == 0 && oldId == 0){
				
				appQueryPage = this.findOldMessage(paramMap, oldPrivateMessageId, userId, oldId, limit, start);
				
			}else{
				
				//获取最新消息
				if(StringUtils.isNotEmpty(newPrivateMessageId)){
					paramMap.put("privateMessageId", newId);	
				}else{
					paramMap.put("privateMessageId",null);
				}
				
				//调用分页组件
				//根据userid查询私信表 获取 接受者id = userid 的所有私信
				appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterPrivateMessage.class, "queryCountPMageMobile", "queryByAgepMageMobile");
				//msg错误码
				if(!appQueryPage.getState()){
					appQueryPage.error(AppErrorCode.GET_CLASS_ERROR);
				}
				
				for (CenterPrivateMessage centerPrivate : appQueryPage.getList()) {
					//根据获取到的 私信发布者id 获取基本信息'昵称';'真实姓名';'头像ID';
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(centerPrivate.getSendUserId());
					CenterUser userOne = centerUserDao.selectCenterUserInfo(centerUser);
					centerPrivate.setNickName(Common.null2Str(userOne.getNickName()));
					centerPrivate.setRealName(Common.null2Str(userOne.getRealName()));
					
					//根据头像ID查询getHeadImgId链接地址
					String msg = Common.getImgUrl(userOne.getHeadImgId(),BusinessConstant.DEFAULT_IMG_USER);
					
					centerPrivate.setFilePath(Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
					//判断私信发布者是否是登陆者
					if(centerPrivate.getSendUserId()==Integer.valueOf(userId)){
						centerPrivate.setPmIsLoginUser(1);
					}else{
						centerPrivate.setPmIsLoginUser(0);
					}
					//			centerNewsStatusService.submitPrivateMessage(centerPrivate.getReceiveUserId());
				}
			}
			
			
		}else if(sortType == 2){
			
			appQueryPage = this.findOldMessage(paramMap, oldPrivateMessageId, userId, oldId, limit, start);
		}
		
		//判断最新最后的消息id
		if(appQueryPage != null && appQueryPage.getList().size() > 0){
			paramMap.clear();
			CenterPrivateMessage oldmessage = appQueryPage.getList().get(0);//最早的
			CenterPrivateMessage newmessage = appQueryPage.getList().get(appQueryPage.getList().size()-1);//最新
			paramMap.put("newPrivateMessageId", newId < newmessage.getPrivateMessageId() ? newmessage.getPrivateMessageId() : newId);
			paramMap.put("oldPrivateMessageId", oldId <= 0 ? oldmessage.getPrivateMessageId() : oldId > oldmessage.getPrivateMessageId() ? oldmessage.getPrivateMessageId() : oldId);
		}else{
			paramMap.clear();
			paramMap.put("newPrivateMessageId", newId );
			paramMap.put("oldPrivateMessageId", oldId );
		}
		
		return appQueryPage.getMessageJSONObject("privateMessages",paramMap).toJSONString();
	}

	/**
	 * 查询历史消息
	 * @param paramMap
	 * @param oldPrivateMessageId
	 * @param appQueryPage
	 * @param userId
	 * @param oldId
	 * @param limit
	 * @param start
	 * @author 			lw
	 * @return 
	 * @date			2016年8月23日  下午2:52:44
	 */
	private QueryPage<CenterPrivateMessage> findOldMessage(Map<String, Object> paramMap, String oldPrivateMessageId, String userId,int oldId, int limit, int start) {
		//历史消息
		//获取最新消息
		if(StringUtils.isNotEmpty(oldPrivateMessageId)){
			paramMap.put("privateMessageId", oldId);
		}else{
			paramMap.put("privateMessageId",null);
		}
		
		//调用分页组件
		//根据userid查询私信表 获取 接受者id = userid 的所有私信
		QueryPage<CenterPrivateMessage> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, CenterPrivateMessage.class, "queryCountPMageMobile", "queryByOldMageMobile");
		//msg错误码
		if(!appQueryPage.getState()){
			appQueryPage.error(AppErrorCode.GET_CLASS_ERROR);
		}

			
		for (CenterPrivateMessage centerPrivate : appQueryPage.getList()) {
			//根据获取到的 私信发布者id 获取基本信息'昵称';'真实姓名';'头像ID';
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(centerPrivate.getSendUserId());
			CenterUser userOne = centerUserDao.selectCenterUserOne(centerUser);
			centerPrivate.setNickName(userOne.getNickName());
			centerPrivate.setRealName(userOne.getRealName());
			
			//根据头像ID查询getHeadImgId链接地址
			String msg = Common.getImgUrl(userOne.getHeadImgId(),BusinessConstant.DEFAULT_IMG_USER);
			centerPrivate.setFilePath(Common.checkPic(msg)==true ? msg+ActiveUrl.HEAD_MAP:msg);
			//判断私信发布者是否是登陆者
			if(centerPrivate.getSendUserId()==Integer.valueOf(userId)){
				centerPrivate.setPmIsLoginUser(1);
			}else{
				centerPrivate.setPmIsLoginUser(0);
			}
			//				centerNewsStatusService.submitPrivateMessage(centerPrivate.getReceiveUserId());
		}
		
		//分页反转
		appQueryPage.resultListReverse();
		
		return appQueryPage;
		
	}

}
