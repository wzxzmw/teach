package com.seentao.stpedu.phonecode.service;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.PublicSmsSendCountDao;
import com.seentao.stpedu.common.entity.PublicSmsSendCount;
import com.seentao.stpedu.common.interfaces.IAlipayServiceInterface;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.LogUtil;


/** 
* @ClassName: PhoneCodeService 
* 短信验证码发送Service
* @author W.jx
* @date 2016年6月30日 下午3:32:48 
*  
*/
@Service
public class PhoneCodeService {
	
	/** 
	* 发送短信
	* @author W.jx
	* @date 2016年7月28日 下午7:09:25 
	* @param phone 手机号
	* @param note 短息内容
	* @return
	*/
	@Transactional
	public String getPhoneCode(String phone, String note) {
		IPhoneMessagesService service = HproseRPC.getRomoteClass(ActiveUrl.SENDMSG_SERVER_INTERFACE, IPhoneMessagesService.class);
		return service.sendPhoneNote(phone, note);
	}
	
}
