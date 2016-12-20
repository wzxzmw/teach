package com.seentao.stpedu.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.GetClubsForMobileService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/**
 * @author cxw
 */
@Controller
public class GetClubsForMobileController implements IGetClubsForMobileController {

	@Autowired
	private GetClubsForMobileService getClubsForMobileService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.club.controller.IGetClubsForMobileController#getClubsForMobile(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "getClubsForMobile")
	public String getClubsForMobile(String userId,Integer userType,String memberId,int hasConcernTheClub,
	String searchWord,String clubId,int start,int limit,int recordingActivity,int inquireType){
  
		if(inquireType==GameConstants.GET_CLUB_INFORMATION_FIVE&&StringUtil.isEmpty(clubId)){
			LogUtil.error(this.getClass(),"getClubsForMobile", "请求参数有误");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
		}
		return getClubsForMobileService.getClubsForMobileInfo(userId,userType,memberId,hasConcernTheClub,searchWord,clubId,start,limit,recordingActivity,inquireType);
	}
}
