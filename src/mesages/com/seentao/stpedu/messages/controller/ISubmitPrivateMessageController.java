package com.seentao.stpedu.messages.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitPrivateMessageController {

	/**
	 * 发私信
	 * @param userId  用户id
	 * @param pmObjectId  私信对象id
	 * @param pmContent   私信内容
	 * @return
	 * @author cxw
	 */
	String submitPrivateMessage(String userId, String pmObjectId, String pmContent);

}