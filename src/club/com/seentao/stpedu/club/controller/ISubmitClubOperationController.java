package com.seentao.stpedu.club.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitClubOperationController {

	/***
	 * 加入或退出俱乐部
	 * @param userId
	 * @param clubId 俱乐部id
	 * @param actionType 操作 1:加入俱乐部；2:退出俱乐部；
	 * @param applicationContent 加入申请
	 * @param clubMemberId 会员id
	 * @return
	 * @throws ParseException 
	 */
	String submitClubOperation(String userId, String clubId, int actionType, String applicationContent,
			String clubMemberId) throws ParseException;

}