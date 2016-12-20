
package com.seentao.stpedu.user.service;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.CenterSession;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.RedisUserPhone;
import com.seentao.stpedu.common.service.CenterSessionService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月28日 上午11:09:31
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
public class LoginCenterUserService {
	
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private CenterSessionService centerSessionService;
	
	/**
	 * @param userName 用户名
	 * @param password  密码
	 * @param pushToken 推送唯一标示
	 * @param sourceID 微信用户，QQ用户，Sina微博传openid，当用户类型为2,3,4时该字段必填
	 * @param clientType
	 * @return
	 */
	public String loginCenterUser(String userName, String password, String pushToken, String sourceID,Integer clientType){
		
		JSONObject obj = new JSONObject();
		/*
		 * 校验用户名是否是手机还是自由用户
		 */
		/*Pattern pattern = Pattern.compile("[0-9]*");
		boolean matches = pattern.matcher(userName).matches();
		CenterUser centerUser = new CenterUser();
		//RedisLogin redisLogin =null;
		if(matches){
			LogUtil.info(this.getClass(), "login", "使用手机号方式登录");
			centerUser.setUserName(userName);
			String msg = JedisUserCacheUtils.getRegisterUserHash(CenterUser.class.getSimpleName(),userName);
			
			if(StringUtils.isEmpty(msg)){
				centerUser = centerUserService.selectCenterUserByPhone(userName);
				JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), userName, JSONObject.toJSONString(centerUser));
			}else {
				centerUser = JSONObject.parseObject(msg, CenterUser.class);
			}
			//String redis = JedisUserCacheUtils.getCheckLoginHash(userName);
			//RedisUserPhone redisUserPhone = JSONObject.parseObject(redis, RedisUserPhone.class);
			//redisUserPhone.setUserToken(pushToken);
			
		}else{
			centerUser.setUserName(userName);
			centerUser = centerUserService.selectCenterUser(centerUser);
		}*/
		//新加代码
		CenterUser centerUser = new CenterUser();
		centerUser.setUserName(userName);
		String msg = JedisUserCacheUtils.getRegisterUserHash(CenterUser.class.getSimpleName(),userName);
		if(StringUtils.isEmpty(msg)){
			centerUser = centerUserService.selectCenterUserByAll(userName);
			if(centerUser != null){
				JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), centerUser.getPhone(), JSONObject.toJSONString(centerUser));
			}
		}else {
			centerUser = JSONObject.parseObject(msg, CenterUser.class);
		}
		if(centerUser == null){
			LogUtil.error(this.getClass(), "login", "用户不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_USER_DOES_NOT_EXIST).toJSONString();
		}
		/*
		 * 验证码
		 */
		String rodmSalt = centerUser.getSalt();
		/*
		 * 用户id
		 */
		Integer userId = centerUser.getUserId();
		/*
		 * 密码
		 */
		String dbPassword = centerUser.getPassword();
		//登录令牌=userid+时间戳  md5
		String token = "";
		//加密后的密码
		String encryption = "";
		try {
			token =  MD5Utils.encryptToMD5(userId+String.valueOf(TimeUtil.getCurrentTimestamp()));
			String oneEncryption = MD5Utils.encryptToMD5(password);
			encryption = MD5Utils.encryptToMD5(oneEncryption+rodmSalt);
			LogUtil.info(this.getClass(), "login", "登录前密码"+password+"DB密码"+dbPassword);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "MD5Utils", "MD5Utils 加密解密失败"+e);
		}
		/*
		 * 校验密码是否一致
		 */
		if(!encryption.equals(dbPassword)){
			//密码错误
			LogUtil.error(this.getClass(), "login", "密码错误");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_PASSWORD).toJSONString();
		}
		CenterSession selectSession = new CenterSession();
		selectSession.setUserId(centerUser.getUserId());
		selectSession.setClientType(clientType);
		CenterSession resSession = centerSessionService.getCenterSessionOne(selectSession);
		String key = String.valueOf(centerUser.getUserId())+"&"+String.valueOf(clientType);
		if(resSession == null ){
			try {
				CenterSession session = new CenterSession(TimeUtil.getCurrentTimestamp(), centerUser.getUserId(), centerUser.getUserSourceType(), token, clientType);
				centerSessionService.insertCenterSession(session);
				//TODO 当前登录存放在redis中,而且is_login为1，存放pushToken
				JedisUserCacheUtils.setCheckLoginHash(centerUser.getPhone(), JSON.toJSONString(new RedisUserPhone(centerUser.getPhone(), token, 1, 1)));
				//登录信息放入缓存key--userId+clientType
				JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(session));
				//JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(centerSession));
				//JedisCache.setRedisVal(CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(session));
			} catch (InsertObjectException e) {
				LogUtil.error(this.getClass(), "InsertObjectException obj is error", "InsertObjectException obj is error "+e);
			}catch(Exception e){
				LogUtil.error(this.getClass(), "insert obj is error", "insert obj is error "+e);
			}
		}else{
			resSession.setLoginTime(TimeUtil.getCurrentTimestamp());
			resSession.setUserToken(token);
			centerSessionService.updateCenterSession(resSession);
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(),key);
			JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(resSession));
			//JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(centerSession));
			//JedisCache.setRedisVal(CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(resSession));
		}
		/*
		 *登录次数+1 修改最后登录时间 
		 */
		try {
			centerUserService.updateUserLoginsByUserId(new CenterUser(0, TimeUtil.getCurrentTimestamp(), centerUser.getUserId()));
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "UpdateObjectException", "UpdateObjectException is error"+e);
		}
		obj.put("userId", centerUser.getUserId());
		obj.put("userName",centerUser.getUserName());
		obj.put("userType", centerUser.getUserType());
		obj.put("nickName", centerUser.getNickName());
		obj.put("userToken", token);
		String linkImg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
		obj.put("imgLink",linkImg);
		//删除用户缓存
		JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(centerUser.getUserId()));
		LogUtil.info(this.getClass(), "login", "登录成功");
		return Common.getReturn(AppErrorCode.SUCCESS, "", obj).toJSONString();
	}
}
