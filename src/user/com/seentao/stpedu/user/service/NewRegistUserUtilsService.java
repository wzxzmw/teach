
package com.seentao.stpedu.user.service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterInvitationCode;
import com.seentao.stpedu.common.entity.CenterSmsVerification;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.RedisUserPhone;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterInvitationCodeService;
import com.seentao.stpedu.common.service.CenterSmsVerificationService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RandomCode;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月28日 上午12:39:43
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
@Transactional
public class NewRegistUserUtilsService {
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private CenterSmsVerificationService centerSmsVerificationService;
	@Autowired
	private CenterInvitationCodeService centerInvitationCodeService;
	@Autowired
	private CenterAccountService centerAccountService;
	/*
	 * 注册新用户
	 */
	public String newRegistUser(String phoneNumber,String iCode ,String password,String nickName,String invitationCode){
		JSONObject jsonObject = new JSONObject();
		/*
		 * 校验新用户是否已经存在
		 */
		String redisUse = JedisUserCacheUtils.getCheckLoginHash(phoneNumber.trim());
		if(StringUtils.isNotEmpty(redisUse)){
			RedisUserPhone obj = JSONObject.parseObject(redisUse, RedisUserPhone.class);
			/**
			 * 当缓存中已经存在表示已经被占用
			 */
			if(obj.getIs_check() == 1){
				/*
				 * 返回参数提示信息
				 */
				jsonObject= this.backMessages(jsonObject);
				return jsonObject.toJSONString();
			}
		}else{
			/**
			 * 当缓存不存在，则存存放注册信息到redis中,
			 */
			CenterUser resUser = centerUserService.selectCenterUserByPhone(phoneNumber);
			if(resUser !=null){
				/*
				 *根据电话号码删除注册信息 
				 */
				JedisUserCacheUtils.hdelRegisterUserHashByPhone(phoneNumber.trim());
				/*
				 * 当数据库中查询到该用户.则存入redis中，并且显示已经注册 1、表示已经注册，0、表示没有登录
				 */
				JedisUserCacheUtils.setCheckLoginHash(phoneNumber.trim(), 
						JSON.toJSONString(new RedisUserPhone(phoneNumber, 0, 1)));
				/*
				 * 当缓存中已经存在表示已经被占用
				 */
				LogUtil.error(this.getClass(), "newRegistUser","(注册)手机已注册");
				jsonObject=this.backMessages(jsonObject);
				return jsonObject.toJSONString();
			}
		}
		/*
		 * 当redis和数据库中都不存在,则redis中存入phone，没有登录而且没有注册
		 */
		JedisUserCacheUtils.setCheckLoginHash(phoneNumber, 
				JSON.toJSONString(new RedisUserPhone(phoneNumber.trim(), 0, 0)));
		/*phoneNumber 电话号码
		 * iCode 表示 验证码
		 * 验证码类型。1:注册验证码，
	 	 *          2:手机绑定验证码，
	     * 			3:密码找回验证码，
	     * 			4:提现验证码。
	     * 
		 */
		CenterSmsVerification ceterSms =centerSmsVerificationService.getCenterSmsVerificationOne(
				new CenterSmsVerification(phoneNumber,iCode,1));
		/*
		 * 校验注册码是否存在
		 */
		if(ceterSms ==null){
			LogUtil.error(this.getClass(), "--newRegistUser--", "注册验证码不存在");
			jsonObject.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FIVE);
			jsonObject.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATION_CODE_NO_EXIST);
			return jsonObject.toJSONString();
		}
		
		/**
		 * 校验昵称
		 */
		if(!Common.isValidName(nickName)){
			//昵称格式不对
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_NICKNAME).toJSONString();
		}
		/*
		 * 短信发送时间
		 */
		Integer startTime = ceterSms.getSendTime();
		/*
		 *计算时间差 
		 */
		Map<String,Long> map = TimeUtil.getTimeDifference(startTime, TimeUtil.getCurrentTimestamp());
		LogUtil.error(this.getClass(), "newRegistUser", "校验找回密码验证码成功");
		/*
		 *校验验证码是否过期 
		 */
		if(map.get("sec") > BusinessConstant.VERIFICATION_TIME){
			LogUtil.error(this.getClass(), "regist", "找回密码验证码过期");
			jsonObject.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			jsonObject.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATIONCODE_INVALID);//验证码过期
			return jsonObject.toJSONString();
		}
		/*
		 * 是否使用邀请码注册
		 */
		CenterInvitationCode centerCode = null;
		if(BusinessConstant.IS_MAST_INVITATION_CODE){
			centerCode = centerInvitationCodeService.getCenterInvitationCodeOne(new CenterInvitationCode(invitationCode));
			if(centerCode == null){
				LogUtil.error(this.getClass(), "regist", "注册邀请码已被使用");
				jsonObject.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				jsonObject.put(GameConstants.MSG, BusinessConstant.ERROR_PLEASE_CODE_INVALID);
				return jsonObject.toJSONString();
			}
		}
		//附加码  随机6位数字
		String radomInt = RandomCode.game(4);
		//一次加密
		String oneEncryption =null;
		//二次加密
		String twoEncryption=null;
		try {
			oneEncryption  = MD5Utils.encryptToMD5(password);
			twoEncryption = MD5Utils.encryptToMD5(oneEncryption+radomInt);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.error(this.getClass(), "newRegistUser ", e+"");
		}catch (Exception e) {
			LogUtil.error(this.getClass(), "newRegistUser ", e+"");
		}
		Integer regTime = TimeUtil.getCurrentTimestamp();
		int birthday = (int)TimeUtil.getSpecifiedTimeStamp("19970101");//默认值
		CenterUser centerUser = new CenterUser(String.valueOf(radomInt), twoEncryption, phoneNumber, nickName, 1, 0, -1, 0, 0, 0, 0, regTime, 0, 0, 0, 0, birthday, 1, 4, 3,phoneNumber); 
		try {
			centerUserService.insertCenterUser(centerUser);
			String objs = JSONObject.toJSONString(centerUser);
			/*
			 * 保存密码和用户名到redis中
			 */
		//	JedisUserCacheUtils.setUserPasswordHashByPhone(phoneNumber, JSONObject.toJSONString(new RedisLogin(phoneNumber, twoEncryption)));
			/*
			 * 保存用户注册信息到redis中
			 */
			JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), phoneNumber.trim(), objs);
			/*
			 *保存用户信息，并校验是否已经注册 
			 */
			JedisUserCacheUtils.setCheckLoginHash(phoneNumber, JSON.toJSONString(new RedisUserPhone(phoneNumber, "", 0, 1)));
			if(BusinessConstant.IS_MAST_INVITATION_CODE){
				/**
				 * 更新验证码
				 */
				centerCode.setStatus(BusinessConstant.INVITATION_CODE_NO);
				centerCode.setRegUserId(centerUser.getUserId());
				centerCode.setUseTime(TimeUtil.getCurrentTimestamp());
				centerInvitationCodeService.updateCenterInvitationCode(centerCode);
			}
			/*
			 * 账户类型(1:一级货币 为创业宝)
			 */
			centerAccountService.insertCenterAccount(new CenterAccount(GameConstants.STAIR_ONE, centerUser.getUserId(), BusinessConstant.ACCOUNT_USER_TYPE_1, TimeUtil.getCurrentTimestamp(), 0d, 0d));
			/*
			 * 账户类型(2:二级货币。为鲜花)
			 */
			centerAccountService.insertCenterAccount(new CenterAccount(GameConstants.STAIR_TWO,centerUser.getUserId(),BusinessConstant.ACCOUNT_USER_TYPE_1,TimeUtil.getCurrentTimestamp(),0d,0d));
			jsonObject.put(GameConstants.CODE, AppErrorCode.SUCCESS);
			return jsonObject.toJSONString();
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "newRegist", e+"插入用户失败");
			//e.printStackTrace();
		}catch(UpdateObjectException e){
			LogUtil.error(this.getClass(), "newRegist", e+"更新验证码失败");
			//e.printStackTrace();
		}catch (Exception e) {
			JedisUserCacheUtils.hdelRegisterUserHashByPhone(phoneNumber);
			LogUtil.error(this.getClass(), "newRegist", e+"操作失败");
			//e.printStackTrace();
		}
		
		return null;
	}
	/**返回提示信息
	 * @param obj
	 * @return
	 */
	public JSONObject backMessages(JSONObject obj){
		obj.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
		obj.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
		return obj;
	}
}
