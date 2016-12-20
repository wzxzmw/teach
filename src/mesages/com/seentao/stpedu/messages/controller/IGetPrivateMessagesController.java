package com.seentao.stpedu.messages.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetPrivateMessagesController {

	/**
	 * 获取私信信息
	 * @param userId
	 * @param privateMessageId 私信id
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @return
	 */
	String getPrivateMessages(String userId, int start, int limit,String privateMessageId, int inquireType);

}