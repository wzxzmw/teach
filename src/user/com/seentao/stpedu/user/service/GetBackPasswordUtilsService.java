
package com.seentao.stpedu.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.CenterSession;
import com.seentao.stpedu.common.entity.CenterSmsVerification;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.CenterSmsVerificationService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RandomCode;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月28日 上午12:45:30
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
@Transactional
public class GetBackPasswordUtilsService {
	@Autowired
	private CenterSmsVerificationService centerSmsVerificationService;
	@Autowired
	private CenterUserService centerUserService;
	/**
	 * 密码找回
	 * @return
	 */
	public String getBackPassword(String phoneNumber,String iCode,String password){
		JSONObject obj = new JSONObject();
		/*
		 * 校验手机号与验证码
		 */
		CenterSmsVerification centerSms = centerSmsVerificationService.getCenterSmsVerificationOne(new CenterSmsVerification(phoneNumber, iCode, BusinessConstant.VERIFICATION_TYPE_3) );
		if(centerSms == null){
			LogUtil.error(this.getClass(), "regist", "找回密码验证码不存在");
			obj.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			obj.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATION_CODE_NO_EXIST);//验证码不存在
			return obj.toJSONString();
		}
		/*
		 * 验证码时间
		 */
		Integer startTime = centerSms.getSendTime();
		/*
		 * 生成时间戳
		 */
		Map<String,Long> map = TimeUtil.getTimeDifference(startTime, TimeUtil.getCurrentTimestamp());
		/*
		 * 校验验证码是否过期
		 */
		if(map.get("sec") > BusinessConstant.VERIFICATION_TIME){
			LogUtil.error(this.getClass(), "regist", "找回密码验证码过期");
			obj.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			obj.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATIONCODE_INVALID);//验证码过期
			return obj.toJSONString();
		}
		/*
		 * 附加码  随机6位数字
		 */
		String radomInt = RandomCode.game(4);
		//一次加密
		String oneEncryption = null;
		/*
		 * 二次加密
		 */
		String twoEncryption = null;
		try {
			oneEncryption = MD5Utils.encryptToMD5(password);
			twoEncryption = MD5Utils.encryptToMD5(oneEncryption+radomInt);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "getBackPassword", "加密 is error"+e);
		}
		String msg = JedisUserCacheUtils.getRegisterUserHash(CenterUser.class.getSimpleName(), phoneNumber);
		CenterUser cen = JSONObject.parseObject(msg, CenterUser.class);
		if(cen == null){
			cen = centerUserService.selectCenterUserByPhone(phoneNumber);
			if(cen == null){
				obj.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				obj.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
				return obj.toJSONString();
			}
		}
		cen.setSalt(String.valueOf(radomInt));
		cen.setPassword(twoEncryption);
		try {
			centerUserService.updateCenterUserByKey(cen);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "getBackPassword", "update object is error");
		}
		/**
		 * 删除redis中phone的电话号码，并存入新密码
		 */
	//	JedisUserCacheUtils.hdelUserPasswordHashByPhone(phoneNumber);
		//JedisUserCacheUtils.setUserPasswordHashByPhone(phoneNumber, JSON.toJSONString(new RedisLogin(phoneNumber,twoEncryption)));
		/**
		 * 删除centerUser缓存
		 */
		JedisUserCacheUtils.hdelCenterUserByPhoneHash(phoneNumber);
		JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), phoneNumber, JSONObject.toJSONString(cen));
		
		JedisCache.delRedisVal(CenterSession.class.getSimpleName(),String.valueOf(cen.getUserId()));
		LogUtil.info(this.getClass(), "regist", "找回密码成功");
		obj.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		return obj.toJSONString();
	}
}
