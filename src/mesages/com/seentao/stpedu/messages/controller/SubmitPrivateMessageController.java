package com.seentao.stpedu.messages.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.messages.service.SubmitPrivateMessageService;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/**
 * @author cxw
 */
@Controller
public class SubmitPrivateMessageController implements ISubmitPrivateMessageController {

	@Autowired
	private SubmitPrivateMessageService submitPrivateMessageService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.messages.controller.ISubmitPrivateMessageController#submitPrivateMessage(java.lang.String, java.lang.String, java.lang.String)
	 */
	/**
	 * userId 登录用户的UserId
	 * pmObjectId 私信对象的ID
	 * pmContent 私信内容
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="submitPrivateMessage")
	public String submitPrivateMessage(String userId,String pmObjectId,String pmContent){

		if(userId.equals(pmObjectId)){
			LogUtil.error(this.getClass(), "submitPrivateMessage", "不能自己发给自己");
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.CAN_NOT_SEND_THEIR_OWN).toJSONString();
		}
		if(StringUtil.isEmpty(pmContent.trim())){
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.CAN_NOT_SEND_NULL).toJSONString();
		}
		return submitPrivateMessageService.submitPrivateMess(userId,pmObjectId,pmContent);


	}
}
