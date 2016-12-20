package com.seentao.stpedu.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.ClubService;
import com.seentao.stpedu.club.service.DynamicMobileService;
import com.seentao.stpedu.club.service.GetDynamicsForMobileService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

/** 
* @ClassName: ClubController 
* @Description: 俱乐部操作控制器
* @author liulin
* @date 2016年6月26日 下午8:42:28 
*/
@Controller
public class ClubController implements IClubController {
	
	/** 
	* @Fields clubService : 俱乐部业务类
	*/ 
	@Autowired
	private ClubService clubService;
	@Autowired
	private DynamicMobileService dynamicMobileService;
	@Autowired
	private GetDynamicsForMobileService getDynamicsForMobileService;

	/*
	* Title: getTopics
	*Description: 
	* @param userId
	* @param memberId
	* @param clubId
	* @param topicId
	* @param startDate
	* @param endDate
	* @param searchWord
	* @param start
	* @param limit
	* @param inquireType
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getTopics(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getTopics")
	@ResponseBody
	public String getTopics(Integer userId, Integer memberId, Integer clubId, Integer topicId, Integer startDate, Integer endDate, String searchWord, Integer start, Integer limit, Integer inquireType){
		return clubService.getTopics(userId, memberId, clubId, topicId, startDate, endDate, searchWord, start, limit, inquireType);
	}
	
	/*
	* Title: submitTopic
	*Description: 
	* @param userId
	* @param clubId
	* @param topicTitle
	* @param topicContent
	* @param isTop
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#submitTopic(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/submitTopic")
	@ResponseBody
	public String submitTopic(Integer userId, Integer clubId, String topicTitle, String topicContent, Integer isTop){
		if(topicTitle == null || (topicTitle = topicTitle.trim()).length() < 2 || topicTitle.length() > 20){
			LogUtil.error(this.getClass(), "submitTopic", AppErrorCode.IS_TWO_TWENTY);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.IS_TWO_TWENTY).toJSONString();
		}
		
		if(topicContent == null || (topicContent = topicContent.trim()).length() < 2 || topicContent.length() > 2000){
			LogUtil.error(this.getClass(), "submitTopic", AppErrorCode._CONT_ERROR);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode._CONT_ERROR).toJSONString();
		}
		return clubService.addTopic(userId, clubId, topicTitle, topicContent, isTop);
	}
	
	/*
	* Title: getReminds
	*Description: 
	* @param userId
	* @param clubId
	* @param remindShowType
	* @param start
	* @param limit
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getReminds(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getReminds")
	@ResponseBody
	public String getReminds(Integer userId, Integer clubId, Integer remindShowType, Integer start, Integer limit){
		return clubService.getReminds(userId, clubId, remindShowType, start, limit);
	}
	
	/*
	* Title: submitRemind
	*Description: 
	* @param userId
	* @param clubId
	* @param remindContent
	* @param remindObject
	* @param remindUserIds
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#submitRemind(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String) 
	*/
	@Override
	@RequestMapping("/submitRemind")
	@ResponseBody
	public String submitRemind(Integer userId, Integer clubId, String remindContent, Integer remindObject, String remindUserIds){
		
		if(remindContent == null || (remindContent = remindContent.trim()).length() < 2 || remindContent.length() > 70){
			LogUtil.error(this.getClass(), "submitRemind", AppErrorCode.ERROR_ADD_CONTENT_LENGTH);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADD_CONTENT_LENGTH).toJSONString();
		}
		return clubService.addRemind(userId, clubId, remindContent, remindObject, remindUserIds);
	}
	
	/*
	* Title: getWebNotices
	*Description: 
	* @param userId
	* @param clubId
	* @param start
	* @param limit
	* @param inquireType
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getWebNotices(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getWebNotices")
	@ResponseBody
	public String getWebNotices(Integer userId, Integer clubId, Integer start, Integer limit, Integer inquireType){
		return clubService.getWebNotices(userId, clubId, start, limit, inquireType);
	}
	
	/*
	* Title: submitNotice
	*Description: 
	* @param userId
	* @param clubId
	* @param noticeTitle
	* @param noticeContent
	* @param isTop
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#submitNotice(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/submitNotice")
	@ResponseBody
	public String submitNotice(Integer userId, Integer clubId, String noticeTitle, String noticeContent, Integer isTop){
		//校验标题
		boolean isYes = Common.isValid(noticeTitle.trim(), 2 , 20 );
		if(isYes == false){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_TWO_TWENTY).toJSONString();
		}
		//校验内容字数
		boolean isContext = Common.isValid(noticeContent.trim(), 2, 2000);
		if(isContext == false){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode._CONT_ERROR).toJSONString();
		}
		return clubService.addNotice(userId, clubId, noticeTitle.trim(), noticeContent.trim(), isTop);
	}
	
	/*
	* Title: getMoods
	*Description: 
	* @param userId
	* @param memberId
	* @param clubId
	* @param timeRange
	* @param start
	* @param limit
	* @param inquireType
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getMoods(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getMoods")
	@ResponseBody
	public String getMoods(Integer userId, Integer memberId, Integer clubId, Integer timeRange, Integer start, Integer limit, Integer inquireType){
		return clubService.getMoods(userId, memberId, clubId, timeRange, start, limit, inquireType);
	}
	
	/*
	* Title: getDynamics
	*Description: 
	* @param userId
	* @param memberId
	* @param classId
	* @param classType
	* @param clubId
	* @param start
	* @param limit
	* @param inquireType
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getDynamics(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getDynamics")
	@ResponseBody
	public String getDynamics(Integer userId, Integer memberId, Integer classId, Integer classType, Integer clubId, Integer start, Integer limit, Integer inquireType){
		//clubService.getDynamics(userId, memberId, classId, classType, clubId, start, limit, inquireType);
		return clubService.getDynamicsUpdate(userId, memberId, classId, classType, clubId, start, limit, inquireType);
	}
	
	/*
	* Title: getDynamicEntryForMobile
	*Description: 
	* @param userId
	* @return 
	* @see com.seentao.stpedu.club.controller.IClubController#getDynamicEntryForMobile(java.lang.Integer) 
	*/
	@Override
	@RequestMapping("/getDynamicEntryForMobile")
	@ResponseBody
	public String getDynamicEntryForMobile(Integer userId){
//		return clubService.getDynamicEntryForMobile(userId);
		return dynamicMobileService.getDynamicEntryForMobile(userId);
	}

	@Override
	public String getDynamicsForMobile(Integer userId, Integer clubId, Integer start, Integer limit,
			Integer inquireType) {
		// TODO Auto-generated method stub
		return getDynamicsForMobileService.getDynamicsSomes(userId, clubId, start, limit, inquireType);
	}
}
