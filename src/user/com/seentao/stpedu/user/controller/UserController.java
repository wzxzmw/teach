package com.seentao.stpedu.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.service.CenterFeedbackService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.user.service.BusinessUserService;
import com.seentao.stpedu.user.service.LoginCenterUserService;
import com.seentao.stpedu.user.service.RegisterUtilsService;
import com.seentao.stpedu.user.service.RestPasswordUtilsService;

/** 
* @author yy
* @date 2016年6月22日 下午9:10:46 
*/
@Controller
public class UserController implements IUserController {
	
	@Autowired
	private BusinessUserService businessUserService;
	@Autowired
	private RegisterUtilsService registerUtilsService;
	@Autowired
	private LoginCenterUserService loginCenterUserService;
	@Autowired
	private RestPasswordUtilsService restPasswordUtilsService;
	
	/** 
	* @Fields centerUserService : 用户服务
	*/ 
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private CenterFeedbackService centerFeedbackService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getUsers(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getUsers")
	public String getUsers(Integer userType,String userName,String userId,String userToken,
			Integer platformModule,Integer start,Integer limit,
			Integer inquireType,Integer hasQualification,Integer clubApplyStatus,
			String memberId,String schoolId,String classId,
			String clubId,String gameId,String gameFieldId,
			String searchWord,String gameEventId,String taskId,Integer classType,String quizId
			){
		return businessUserService.getUsers(userType,userName,userId,userToken,
				platformModule,start, limit,inquireType,hasQualification, clubApplyStatus,
				memberId,schoolId,classId,clubId,gameId, gameFieldId,
				searchWord,gameEventId,taskId,classType,quizId);
		
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#login(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/login")
	public String login(String userName,String password,String pushToken,String sourceID){
		//1---pc端登录 TODO
		//return businessUserService.login(userName,password,pushToken,sourceID,1).toJSONString();
		return loginCenterUserService.loginCenterUser(userName, password, pushToken, sourceID, 1);
	}
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#login(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/loginForMobile")
	public String loginForMobile(String userName,String password,String pushToken,String sourceID){
		//2---移动端登录 TODO
		//return businessUserService.login(userName,password,pushToken,sourceID,2).toJSONString();
		return loginCenterUserService.loginCenterUser(userName, password, pushToken, sourceID, 2);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#logout(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/logout")
	public String logout(String userId,Integer userType,String userName,String userToken){
		return businessUserService.logout(userId,userType,userName,userToken,1);
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/logoutForMobile")
	public String logoutForMobile(String userId,Integer userType,String userName,String userToken){
		return businessUserService.logout(userId,userType,userName,userToken,2);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#regist(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/regist")
	public String regist(String phoneNumber,String iCode,String password,String invitationCode,Integer registType,String nickName){
		//return businessUserService.regist(phoneNumber,iCode,password,invitationCode,registType,nickName);
		return registerUtilsService.registUser(phoneNumber, iCode, password, invitationCode, registType, nickName);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#resetPassword(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/resetPassword")
	public String resetPassword(String userId,Integer userType,String userName,
			String userToken,String oldPassword,String newPassword){
		/*//return businessUserService.resetPassword(userId,userType,userName,
				 userToken,oldPassword,newPassword);*/
		return restPasswordUtilsService.resetPassword(userId, userType, userName, userToken, oldPassword, newPassword);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getLoginUser(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getLoginUser")
	public String getLoginUser(String userId,Integer userType,String userName,String userToken,
			Integer inquireType){
		return businessUserService.getLoginUser(userId,inquireType,userToken);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#submitUserDesc(java.lang.Integer, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitUserDesc")
	public String submitUserDesc(Integer userId, String desc){
		return centerUserService.updateUserDesc(userId, desc);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#submitBindingNumber(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitBindingNumber")
	public String submitBindingNumber(Integer userId, String oldBindingNumber, String newBindingNumber, String verifyPictureNumber, String verifyNumber){
		return centerUserService.addBindingNumber(userId, oldBindingNumber, newBindingNumber, verifyPictureNumber, verifyNumber);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#submitFeedback(java.lang.Integer, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitFeedback")
	public String submitFeedback(Integer userId, String content){
		return centerFeedbackService.addCenterFeedback(userId, content);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getProtocol(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getProtocol")
	public String getProtocol(Integer userId, Integer protocolType){
		return centerUserService.getProtocol(userId, protocolType);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getCompanies(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getCompanies")
	public String getCompanies(Integer userId, Integer memberId, Integer start, Integer limit, Integer inquireType){
		return businessUserService.getCompanies(userId, memberId, start, limit, inquireType,1);
	}
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getVerifyPicture(java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getVerifyPicture")
	public String getVerifyPicture(Integer userId){
		return businessUserService.getVerifyPicture(userId);
	}
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.user.controller.IUserController#getLoginUser(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getLoginUserForMobile")
	public String getLoginUserForMobile(String userId,Integer userType,String userName,String userToken,
			Integer inquireType){
		return businessUserService.getLoginUser(userId,inquireType,userToken);
	}

	@Override
	public String getCompaniesForMobile(Integer userId, Integer memberId, Integer start, Integer limit,
			Integer inquireType) {
		return businessUserService.getCompanies(userId, memberId, start, limit, inquireType,2);
	}
	
	/**
	 *  验证手机号码是否存在
	 * @param userId 用户id 预留
	 * @param phoneNumber 验证的手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyPhoneNumberExist")
	public String verifyPhoneNumberExist(String userId,String phoneNumber){
		if(phoneNumber == null || phoneNumber.equals("")){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IMPUT_PHONE).toJSONString();
		}
		return businessUserService.verifyPhoneNumberExist(userId,phoneNumber);
	}
}


