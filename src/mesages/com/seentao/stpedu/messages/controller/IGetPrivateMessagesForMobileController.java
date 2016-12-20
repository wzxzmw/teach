package com.seentao.stpedu.messages.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetPrivateMessagesForMobileController {

	/**
	 * 获取私信信息移动端
	 * @param userId
	 * @param memberId
	 * @param limit
	 * @param sortType  获取信息顺序 1:最新的；2:历史信息；
	 * @param newPrivateMessageId 最新的私信id
	 * @param oldPrivateMessageId 最早的私信id
	 * @param inquireType
	 * @author cxw
	 * @return
	 */
	String getPrivateMessagesForMobile(String userId, String memberId, int limit, int sortType,
			String newPrivateMessageId, String oldPrivateMessageId, int inquireType);

}