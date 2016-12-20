package com.seentao.stpedu.persionalcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMemberMoodService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.match.controller.GameEventsController;
import com.seentao.stpedu.persionalcenter.service.PersionalCenterService;

@Controller
public class PersionalCenterController implements IPersionalCenterController {

	@Autowired
	private ClubMemberMoodService clubMemberMoodService;
	@Autowired
	private PersionalCenterService persionalCenterService;
	@Autowired
	private CenterUserService centerUserService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IPersionalCenterController#submitMood(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitMood")

	public String submitMood(String userName,String userId,String userType,String userToken,String clubId,String moodContent){
		if( null== userId  || null==clubId|| !StringUtils.hasText(moodContent) ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_MOOD_TYPE_DATA_ERROE).toJSONString();
		}
		//校验内容长度
		
		if(moodContent == null || (moodContent = moodContent.trim()).length() < 2 ||  moodContent.length() > 70){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_MOOD_TYPE_TEXT_LEN).toJSONString();
		}
		return clubMemberMoodService.addClubMemberMood(userId, clubId, moodContent);
	} 
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IPersionalCenterController#getRemindRedDot(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getRemindRedDot")
	public String getRemindRedDot(String userName,String userId,String userType,String userToken,String actionType,String clubId){
		if( null== userId  || null==clubId|| null==actionType ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_RED_DATA_ERROE).toJSONString();
		}
		if(userId.equals("") || clubId.equals("")){
			userId = "0";
			clubId = "0";
		}
		return persionalCenterService.getRemindRedDot(Integer.parseInt(userId), Integer.parseInt(clubId),Integer.parseInt(actionType));
	} 
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IPersionalCenterController#getRemindRedDot(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getRemindRedDotForMobile")
	public String getRemindRedDotForMobile(String userName,String userId,String userType,String userToken,String actionType,String clubId){
		if( null== userId  || null==clubId|| null==actionType ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_RED_DATA_ERROE).toJSONString();
		}
		if(userId.equals("") || clubId.equals("")){
			userId = "0";
			clubId = "0";
		}
		return persionalCenterService.getRemindRedDot(Integer.parseInt(userId), Integer.parseInt(clubId),Integer.parseInt(actionType));
	}
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IPersionalCenterController#submitAttachmentOSS(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitAttachmentOSS")
	public String submitAttachmentOSS(String userName,String userId,String userType,String userToken,String attachmentName,String attachmentLink,String attachmentSeconds,String attachmentMajorType,String attachmentType){
		if( null== userId  || null==userType|| null==attachmentName||null==attachmentLink||null==attachmentSeconds||null==attachmentMajorType||null==attachmentType ){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_OSS_DATA_ERROE).toJSONString();
		}
		return persionalCenterService.submitAttachmentOSS(Integer.parseInt(userId), userType, attachmentName, attachmentLink.split("@")[0], Integer.parseInt(attachmentSeconds), Integer.parseInt(attachmentMajorType), attachmentType);
	} 
	
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.persionalcenter.controller.IPersionalCenterController#submitLoginUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitLoginUser")
	public String submitLoginUser(String userName,String userId,String userType,String userToken,String actionType,String headLinkId,String nickname,Integer sex,String realName,String phoneNumber,Integer schoolId,Integer collegeId,String provinceId,String cityId,String idcard,String desc,Integer birthday,Integer profession,String studentID,String studentCard,String teacherCard,String positionalTitle,String speciality,String grade,Integer educationLevel,String address,String userNameForEdit){
		
		if("".equals(collegeId)){
			collegeId=null;
		}
		if(nickname == null){
			nickname="";
		}
		if("".equals(realName)){
			realName=null;
		}
		if(-1==sex){
			sex=null;
		}
		if(-1==schoolId){
			schoolId=null;
		}
		if("".equals(idcard)){
			idcard=null;
		}
		if(desc == null){
			desc="";
		}
		if(address == null){
			address="";
		}
		if("".equals(phoneNumber)){
			phoneNumber=null;
		}
		if("".equals(provinceId)){
			provinceId=null;
		}
		if("".equals(cityId)){
			cityId=null;
		}
		if(birthday == null){
			birthday=Integer.valueOf("");
		}
		if("".equals(profession)){
			profession=null;
		}
		if(studentID == null){
			studentID="";
		}
		if("".equals(studentCard.trim())){
			studentCard="";
		}
		if("".equals(teacherCard)){
			teacherCard=null;
		}
		if(positionalTitle == null){
			positionalTitle="";
		}
		if(speciality == null){
			speciality="";
		}
		if(grade == null){
			grade="";
		}
		if("".equals(educationLevel)){
			educationLevel=null;
		}
		Integer headLinkIdPast=null;
		if("".equals(headLinkId)){
			headLinkId="0";
		}
		
		if( null== userId){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_DATA_ERROE).toJSONString();
		}
		if(null!=headLinkId){
			headLinkIdPast=Integer.parseInt(headLinkId);
		}
		if(actionType.equals("2")){
			return persionalCenterService.submitLoginUser(null,Integer.parseInt(userId),headLinkIdPast,null,null,null,null,null,null,null,null,null, null,null, null,null,null,null,null,null,null,null,actionType);
		}else{
			//数据校验
			//昵称校验
			//
			//修改的用户名校验
			if(userNameForEdit == null || userNameForEdit.trim().equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.SUBMIT_USER_USERNAME_REPLACE).toJSONString();
			}
			//nickname="学生102";
			if(!Common.isValidName(nickname)){
				//昵称格式不对
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_CHAR_LEN).toJSONString();
			}
			//用户名校验
			if(!Common.isValidNameNull(realName)){
				//用户名格式不对
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_USERNAME_REPLACE).toJSONString();
			}
			//数据格式长度校验
			if(!Common.isValidLength(desc, 70)&&!Common.isValidLength(studentID, 70)&&!Common.isValidLength(studentCard, 70)&&!Common.isValidLength(teacherCard, 70)&&!Common.isValidLength(positionalTitle, 70)&&!Common.isValidLength(speciality, 70)&&!Common.isValidLength(grade, 70)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_CHAR_LEN_UNIT).toJSONString();
			}
			//昵称判断重复nickname
			CenterUser tempCenterUser=new CenterUser();
			tempCenterUser.setUserId(Integer.parseInt(userId));
			//昵称可以重复 已找项目经理确认
			/*tempCenterUser.setNickName(nickname);
			if(!centerUserService.validateNickName(tempCenterUser)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_NICKNAME_REPLACE).toJSONString();
			}*/
			//判断用户名是否重复user_name
			tempCenterUser.setNickName(null);
			tempCenterUser.setUserName(userNameForEdit);
			if(!centerUserService.validateNickName(tempCenterUser)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_USERNAME_).toJSONString();
			}
			//判断用户名是否和别人的手机号重复
			tempCenterUser.setNickName(null);
			tempCenterUser.setUserName(null);
			tempCenterUser.setPhone(userNameForEdit);
			if(!centerUserService.validateNickName(tempCenterUser)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_USERNAME_).toJSONString();
			}
			//判断用户名长度
			boolean userlength = Common.isValid(userNameForEdit.trim(), 0, 100);
			if(!userlength){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.SUBMIT_USER_LENGTH_).toJSONString();
			}
			return persionalCenterService.submitLoginUser(userName,Integer.parseInt(userId),headLinkIdPast,nickname,sex,phoneNumber,realName,schoolId,collegeId,idcard,desc,birthday, profession,studentID, studentCard,teacherCard,positionalTitle,speciality,grade,educationLevel,address,userNameForEdit.trim(),actionType);
		}
	} 
}
