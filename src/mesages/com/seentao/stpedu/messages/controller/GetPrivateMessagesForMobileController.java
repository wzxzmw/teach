package com.seentao.stpedu.messages.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.messages.service.GetPrivateMessagesForMobileService;

@Controller
public class GetPrivateMessagesForMobileController implements IGetPrivateMessagesForMobileController {

	@Autowired
	private GetPrivateMessagesForMobileService getPrivateMessagesForMobileService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.messages.controller.IGetPrivateMessagesForMobileController#getPrivateMessagesForMobile(java.lang.String, java.lang.String, int, int, java.lang.String, java.lang.String, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping("getPrivateMessagesForMobile")
	public String getPrivateMessagesForMobile(String userId,String memberId,int limit,int sortType,String newPrivateMessageId,String oldPrivateMessageId,int inquireType){

		return getPrivateMessagesForMobileService.getPrivateMessageForMobile(userId,memberId,limit,sortType,newPrivateMessageId,oldPrivateMessageId,inquireType);

	}
}
