package com.seentao.stpedu.messages.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.messages.service.GetPrivateMessagesService;
/**
 * @author cxw
 */
@Controller
public class GetPrivateMessagesController implements IGetPrivateMessagesController {

	@Autowired
	private GetPrivateMessagesService getPrivateMessagesService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.messages.controller.IGetPrivateMessagesController#getPrivateMessages(java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="getPrivateMessages")
	public String getPrivateMessages(String userId,int start,int limit,String privateMessageId, int inquireType){
		
		return getPrivateMessagesService.getprivatemessages(userId,start,limit,privateMessageId,inquireType);
	}
}
