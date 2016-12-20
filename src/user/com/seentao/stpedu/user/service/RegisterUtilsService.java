
package com.seentao.stpedu.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月27日 下午2:52:41
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
@Transactional
public class RegisterUtilsService {
	
	@Autowired
	private NewRegistUserUtilsService newRegistUserUtilsService;
	@Autowired
	private GetBackPasswordUtilsService getBackPasswordUtilsService;
	/**用户注册
	 * @param phoneNumber 电话号码
	 * @param iCode 验证码
	 * @param password 密码
	 * @param invitationCode 邀请码 当注册类型为1:新注册 时该字段是必填项
	 * @param registType 注册类型 1:新注册 2:密码找回
	 * @param nickName 昵称 	当注册类型为1:新注册 时该字段是必填项
	 * @return
	 */
	public String registUser(String phoneNumber, String iCode, String password, String invitationCode, Integer registType,String nickName){
		switch (registType) {
		case 1:
			return newRegistUserUtilsService.newRegistUser(phoneNumber, iCode, password, nickName, invitationCode);
		case 2:
			return getBackPasswordUtilsService.getBackPassword(phoneNumber, iCode, password);
		default:
			return "";
		}
	}
	
}
