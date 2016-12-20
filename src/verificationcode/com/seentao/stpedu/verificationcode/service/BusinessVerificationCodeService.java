package com.seentao.stpedu.verificationcode.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.CenterSmsVerification;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.service.CenterSmsVerificationService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.phonecode.service.PhoneCodeService;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RandomCode;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author yy
* @date 2016年6月27日 下午5:41:14 
*/
@Service
public class BusinessVerificationCodeService {
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private CenterSmsVerificationService centerSmsVerificationService;
	@Autowired
	private PhoneCodeService phoneCodeService;
	/**
	 * 发送验证码
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param phoneNumber 手机号码
	 * @param iType 验证类型
	 * 1:注册验证码，2:手机绑定验证码，3:密码找回验证码，4:提现验证码。
	 * 1:注册；2:密码找回；3:申请提现；4:绑定手机号；
	 */
	@SuppressWarnings("all")
	public String submitIdentifyingCode(Integer userType, String userName, String userId, String phoneNumber,
			String clubId, Integer iType) {
		LogUtil.info(this.getClass(), "submitIdentifyingCode","接口开始调用,iType="+iType+",phoneNumber="+phoneNumber);
		JSONObject json = new JSONObject();
		//验证手机号是否符合规则
		boolean isMobile = isMobile(phoneNumber);
		if(isMobile){
			//校验手机是否存在
			CenterUser centeruser = new CenterUser();
			centeruser.setPhone(phoneNumber);
			CenterUser result = centerUserService.selectCenterUser(centeruser);
			if(iType==BusinessConstant.ITYPE_1){//注册
				if(null==result){
					json = this.sendIdentifyingCode(phoneNumber,1);
					LogUtil.info(this.getClass(), "submitIdentifyingCode", "(注册)发送验证码成功");
				}else{
					LogUtil.error(this.getClass(), "submitIdentifyingCode","(注册)手机已注册");
					json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
					json.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
				}
			}else if(iType==BusinessConstant.ITYPE_2){//密码找回
				if(null!=result){
					json = this.sendIdentifyingCode(phoneNumber,3);
					LogUtil.info(this.getClass(), "submitIdentifyingCode", "(找回密码)发送验证码成功");
				}else{
					LogUtil.info(this.getClass(), "submitIdentifyingCode","(找回密码)要找回的手机不存在");
					json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
					json.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);//要找回的手机不存在
				}
			}else if(iType==BusinessConstant.ITYPE_3){//申请提现
				if(null!=result){
					if(!StringUtil.isEmpty(clubId) && result.getUserId()!=null){
						ClubMember clubMember = new ClubMember();
						clubMember.setClubId(Integer.valueOf(clubId));
						clubMember.setUserId(result.getUserId());
						ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
						if(null!=resClubMember){
							//判断当前用户是不是俱乐部会长  
							if(resClubMember.getLevel()==GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT){
								json = this.sendIdentifyingCode(phoneNumber,4);
								LogUtil.info(this.getClass(), "submitIdentifyingCode", "(申请提现)发送验证码成功");
							}else{
								LogUtil.error(this.getClass(), "submitIdentifyingCode","(申请提现)用户不是会长");
								json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_THREE);
								json.put(GameConstants.MSG, BusinessConstant.ERROR_ROLE);
							}
						}else{
							LogUtil.info(this.getClass(), "submitIdentifyingCode","(申请提现)俱乐部不存在该会员");
							json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
							json.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);//俱乐部不存在该会员
						}
					}else{
						LogUtil.info(this.getClass(), "submitIdentifyingCode","(申请提现)clubId="+clubId);
						json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
						json.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
					}
				}else{
					LogUtil.info(this.getClass(), "submitIdentifyingCode","(申请提现)用户不存在");
					json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
					json.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
				}
			}else if(iType==BusinessConstant.ITYPE_4){//绑定手机
				if(null==result){
					json = this.sendIdentifyingCode(phoneNumber,2);
					LogUtil.info(this.getClass(), "submitIdentifyingCode", "(手机绑定)发送验证码成功");
				}else{
					LogUtil.error(this.getClass(), "submitIdentifyingCode","(手机绑定)手机已注册");
					json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
					json.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
				}
			}else{
				LogUtil.info(this.getClass(), "submitIdentifyingCode","查询类型传入错误");
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				json.put(GameConstants.MSG, BusinessConstant.ERROR_TYPE_CODE);
			}
		}else{
			//手机号不规则
			LogUtil.info(this.getClass(), "submitIdentifyingCode","输入的手机号码不规则");
			json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			json.put(GameConstants.MSG, BusinessConstant.PHONE_IS_NOT_RULES);
		}
		return json.toJSONString();
	}
	
	/**
	 * 发送验证码
	 * @param phoneNumber
	 * @return
	 */
	@Transactional
	private JSONObject sendIdentifyingCode(String phoneNumber, Integer iType) {
		JSONObject json = new JSONObject();
		try {
			//随机4位数字作为验证码
			String sendCode = RandomCode.game(4);
			LogUtil.info(this.getClass(), "sendIdentifyingCode","开始调用至臻发送短信验证码");
			//调用至臻发送短信验证码
			String resJsonObject = phoneCodeService.getPhoneCode(phoneNumber.trim(), sendCode);
			String returnParam = JSONObject.parseObject(resJsonObject).getString("result");
			LogUtil.info(this.getClass(), "sendIdentifyingCode","结束调用至臻发送短信验证码,调用结果为:"+returnParam);
			if(returnParam.equals("true")){
				//判断该号码是否存在验证码
				CenterSmsVerification code = new CenterSmsVerification();
				code.setPhone(phoneNumber);
				code.setType(iType);
				CenterSmsVerification resCode = centerSmsVerificationService.getCenterSmsVerificationOne(code);
				if(null==resCode){
					CenterSmsVerification centerSmsVerification = new CenterSmsVerification();
					centerSmsVerification.setPhone(phoneNumber);
					centerSmsVerification.setSendTime(TimeUtil.getCurrentTimestamp());
					centerSmsVerification.setType(iType);
					centerSmsVerification.setVerificationCode(sendCode);
					centerSmsVerificationService.insertCenterSmsVerification(centerSmsVerification);
				}else{
					CenterSmsVerification centerSmsVerification = new CenterSmsVerification();
					centerSmsVerification.setPhone(phoneNumber);
					centerSmsVerification.setSendTime(TimeUtil.getCurrentTimestamp());
					centerSmsVerification.setType(iType);
					centerSmsVerification.setVerificationCode(sendCode);
					centerSmsVerification.setVerificationId(resCode.getVerificationId());
					centerSmsVerificationService.updateCenterSmsVerification(centerSmsVerification);
				}
				LogUtil.info(this.getClass(), "sendIdentifyingCode","验证码插入数据库操作成功");
				json.put(GameConstants.CODE, AppErrorCode.SUCCESS);
			}else{
				//发送验证码失败
				LogUtil.error(this.getClass(), "sendIdentifyingCode", "调用至臻发送验证码失败");
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
				json.put(GameConstants.MSG, BusinessConstant.CALL_FAIL);
			}
		} catch (Exception e) {
			LogUtil.error(BusinessVerificationCodeService.class, "sendIdentifyingCode", "发送验证码异常", e);
			json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
			json.put(GameConstants.MSG, BusinessConstant.ERROR_INSERT);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return json;
	}
	
	/**
	 * 校验手机号
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
		boolean result = false;
		if(!StringUtil.isEmpty(str)){
			String phone = str.trim();
			//该字符串是数字并且长度为11位
	        if(phone.matches("[0-9]+") && phone.length()==11){
	        	String[] nums = BusinessConstant.PHONE_NUMBER;
	        	String tit = phone.substring(0, 3);//前三位是否在有效的号段内
	        	List<String> tempList = Arrays.asList(nums);
	        	if(tempList.contains(tit)){
	        		result = true;
	        	}else{
	        		result = false;
	        	}
	        }else{
	        	result = false;
	        }
		}
        return result;  
    }  

}


