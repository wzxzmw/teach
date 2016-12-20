package com.seentao.stpedu.redpacket.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redpacket.service.BusinessRedPacketService;
import com.seentao.stpedu.utils.LogUtil;

/** 
* @author yy
* @date 2016年6月27日 下午5:42:08 
*/
@Controller
public class RedPacketController implements IRedPacketController {
	@Autowired
	private BusinessRedPacketService businessRedPacketService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.redpacket.controller.IRedPacketController#submitRedPacket(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitRedPacket")
	public String submitRedPacket(Integer userType,String userName,String userId,String userToken,
			Integer redPacketObject,Integer redPacketType,Integer perRedPacketCount,
			String clubId,String redPacketContent,String redPacketUserIds
			){
		
		/*if(redPacketContent == null || (redPacketContent = redPacketContent.trim()).length() < 2 || redPacketContent.length() > 70){
			LogUtil.error(this.getClass(), "submitRedPacket", AppErrorCode.ERROR_ADD_CONTENT_LENGTH);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADD_CONTENT_LENGTH).toJSONString();
		}*/
		if(StringUtils.isNotEmpty(redPacketContent) && (redPacketContent = redPacketContent.trim()).length() < 2 || redPacketContent.length() > 70){
			LogUtil.error(this.getClass(), "submitRedPacket", AppErrorCode.ERROR_ADD_CONTENT_LENGTH);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADD_CONTENT_LENGTH).toJSONString();
		}
		return businessRedPacketService.submitRedPacket(userId, redPacketObject,redPacketType, perRedPacketCount,
				 clubId, redPacketContent,redPacketUserIds);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.redpacket.controller.IRedPacketController#getRedPacketType(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getRedPacketType")
	public String getRedPacketType(Integer userType,String userName,String userId,String userToken,Integer inquireType){
		return businessRedPacketService.getRedPacketType(userId,inquireType);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.redpacket.controller.IRedPacketController#getRedPackets(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getRedPackets")
	public String getRedPackets(Integer userType,String userName,String userId,String userToken,
			String clubId,Integer redPacketShowType,Integer start,Integer limit){
		return businessRedPacketService.getRedPackets(userId,clubId,redPacketShowType,start,limit);
	}
	
}


