package com.seentao.stpedu.club.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetBackgroundColorController {

	/**
	 * 获取自定义背景色
	 * @param userId
	 * @param userType 用户类型
	 * @param userToken
	 * @return
	 */
	String getBackgroundColor(String userId);

}