package com.seentao.stpedu.persionalcenter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.persionalcenter.service.ClubCenterAccountService;
import com.seentao.stpedu.persionalcenter.service.ClubCenterService;

@Controller
public class ClubCenterController implements IClubCenterController {
	
	@Autowired
	private ClubCenterService clubCenterService;
	@Autowired
	private ClubCenterAccountService clubCenterAccountService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getMessages(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getMessages")

	public String getMessages(String userName,String userId,String userType,String userToken,String messageType,String start,String limit){
		if( null== userId  ||null==messageType){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_MESSAGE_DATA_ERROR).toJSONString();
		}
		return clubCenterService.getMessages(Integer.parseInt(userId), Integer.parseInt(messageType),Integer.parseInt(limit),Integer.parseInt(start));
	} 
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getMessages(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getMessagesForMobile")
	public String getMessagesForMobile(String userName,String userId,String userType,String userToken,String messageType,String start,String limit){
		if( null== userId  ||null==messageType){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_MESSAGE_DATA_ERROR).toJSONString();
		}
		return clubCenterService.getMessagesMobile(Integer.parseInt(userId), Integer.parseInt(messageType),Integer.parseInt(limit),Integer.parseInt(start));
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getClubRights(java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getClubRights")	
	public String getClubRights(String userName,String userId,String userType,String userToken,String clubId){
		if( null==clubId){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CLUB_RIGHTS_DATA_ERROE).toJSONString();
		}
		return clubCenterService.getClubRights(Integer.parseInt(clubId),Integer.parseInt(userId));
	} 
	
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#buyClubRights(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/buyClubRights")
	public String buyClubRights(String userName,String userId,String userType,String userToken,String actionType,String clubId,String classId,String classType){
		if( null==actionType){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, "请求参数传递错误！").toJSONString();
		}
		return clubCenterService.buyClubRights(clubId,Integer.parseInt(userId), Integer.parseInt(actionType),classId, classType);
	} 
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getIncomeAndExpenses(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getIncomeAndExpenses")
	public String getIncomeAndExpenses(String userName,String userId,String userType,String userToken,String clubId ,String incomeAndExpenses,String accountType,String startDate,String endDate,String start,String limit,String inquireType){
		/*if( null== userId  ||  null==userType || null==incomeAndExpenses || null==accountType ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CLUB_ACCOUNT_RIGHTS_DATA_ERROE).toJSONString();
		}*/
		if(inquireType.equals("3")){
			incomeAndExpenses= "0";
			accountType = "0";
		}
		if(startDate.equals("")){
			startDate=null;
		}
		if(endDate.equals("")){
			endDate=null;
		}		
		try {
			return clubCenterAccountService.getIncomeAndExpenses(Integer.parseInt(userId),Integer.parseInt(userType) , Integer.parseInt(incomeAndExpenses),Integer.parseInt(accountType),startDate,endDate,Integer.parseInt(start), Integer.parseInt(limit),Integer.parseInt(inquireType),clubId);
		} catch (Exception e) {
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CLUB_ACCOUNT_RIGHTS_DATA_ERROE).toJSONString();
		} 
	} 
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getCashing(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	@Override
	@ResponseBody
	@RequestMapping("/getCashing")
	public String getCashing(String userName,String userId,String userType ,String userToken,String clubId,String startDate,String endDate,String start,String limit){
		
		if( null== userId  || null==clubId || null==userType || null==startDate || null==endDate ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CASH_RIGHTS_DATA_ERROE).toJSONString();
		}
		if(startDate.equals("")){
			startDate=null;
		}
		if(endDate.equals("")){
			endDate=null;
		}
		return clubCenterAccountService.getCashing(Integer.parseInt(userId), userType, clubId, startDate,endDate,Integer.parseInt(limit),Integer.parseInt(start));
	} 
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#applyCashing(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/applyCashing")
	public String applyCashing(String userName,String userId,String userType,String userToken ,String clubId,String cash,String accountType,String accountNum,String applyContent,String iCode){
		
		if( null== userId  || null==userType || null==cash || null==accountType||null==accountNum||null==applyContent||null==iCode ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.APPLY_CASH_RIGHTS_DATA_ERROE).toJSONString();
		}
		return clubCenterAccountService.applyCashing(Integer.parseInt(userId), Integer.parseInt(userType),Double.parseDouble(cash),Integer.parseInt(accountType), accountNum, applyContent, iCode);
	} 
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IClubCenterController#getAccountTypes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getAccountTypes")
	public String getAccountTypes(String userName ,String userId,String userType,String userToken){
		
		if( null== userId || null==userType ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_ACCOUNT_TYPE_DATA_ERROE).toJSONString();
		}		
		return clubCenterAccountService.getAccountTypes(Integer.parseInt(userId));
	} 
	
}
