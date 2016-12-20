
package com.seentao.stpedu.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RandomCode;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月31日 上午9:13:33
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
@Transactional
public class RestPasswordUtilsService {

	@Autowired
	private CenterUserService centerUserService;
	public String resetPassword(String userId, Integer userType, String userName, String userToken, String oldPassword,
			String newPassword){
		JSONObject obj = new JSONObject();
		CenterUser centerUser = centerUserService.queryCenterUserByUserId(Integer.valueOf(userId));
		/*
		 * 附加码
		 */
		String salt = centerUser.getSalt();

		try {
			/*
			 * 加密
			 */
			String twoEncryption = encryptToMD5(oldPassword, salt);
			if(!twoEncryption.equals(centerUser.getPassword())){
				/*
				 * 旧密码不对
				 */
				obj.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
				obj.put(GameConstants.MSG, BusinessConstant.ERROR_PASSWORD);
				return obj.toJSONString();
			}
			/*
			 * 加密码
			 */
			String radomInt = RandomCode.game(4);
			/*
			 * 生成的加密码
			 */
			String twoEncryptionNew = encryptToMD5(newPassword, radomInt);
			CenterUser c = new CenterUser();
			c.setSalt(String.valueOf(radomInt));
			c.setPassword(twoEncryptionNew);
			c.setUserId(centerUser.getUserId()); 
			centerUserService.updateCenterUserByKey(c);
			/*
			 * 删除用户缓存
			 */
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(centerUser.getUserId()));
			CenterUser us = null;
			if(centerUser.getPhone() != null){
				JedisUserCacheUtils.hdelRegisterUserHashByPhone(centerUser.getPhone());
				String  msg = JedisUserCacheUtils.getRegisterUserHash(CenterUser.class.getSimpleName(),centerUser.getPhone());
				us = JSONObject.parseObject(msg, CenterUser.class);
				if(us != null){
					us.setPassword(twoEncryptionNew);
					us.setSalt(radomInt);
					JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), us.getPhone(), JSONObject.toJSONString(us));
				}
				
			}
			obj.put(GameConstants.CODE,AppErrorCode.SUCCESS);
			return obj.toJSONString();

		} catch (UpdateObjectException e) {
			LogUtil.error(this.getClass(), "updatePassword is error", "更新密码失败");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}catch (Exception e) {
			LogUtil.error(this.getClass(), "resetPassword", "更新密码失败");
		}
		return null;
	}
	
	/*
	 * 密码的加密
	 */
	public static String encryptToMD5(String oldPassword,String salt){
		
		String oneEncryption = null;
		String twoEncryption = null; 
		try {
			/*
			 * 第一次加密
			 */
			oneEncryption = MD5Utils.encryptToMD5(oldPassword);
			/*
			 * 二次加密
			 */
			 twoEncryption = MD5Utils.encryptToMD5(oneEncryption+salt);
		} catch (Exception e) {
			LogUtil.error(RestPasswordUtilsService.class, "encryptToMD5", "加密失败");
		}
		return twoEncryption;
	}
}
