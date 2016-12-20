package com.seentao.stpedu.club.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitClubController {

	/***
	 * 提交俱乐部
	 * @param userId
	 * @param clubId  俱乐部id
	 * @param clubType 俱乐部类型1:官方；2:非官方；
	 * @param clubName  俱乐部名称
	 * @param clubDesc  俱乐部介绍
	 * @param clubLogoId  俱乐部logo图片Id
	 * @param provinceId  俱乐部所属省id
	 * @param cityId  俱乐部所属城市id
	 * @param schoolId  俱乐部所属学校id
	 * @param addMemberType  会员加入方式
	 * @param fLevelAccount  收取的一级货币
	 * @param gameBannerId   擂台海报id
	 * @param teachingBannerId 培训海报id
	 * @param styleBannerId    风格海报id
	 * @param backgroundColorId 背景色id
	 * @return actionType  提交操作
	 * @author cxw
	 * @throws ParseException 
	 */
	String submitClub(String userId, String clubId, int clubType, String clubName, String clubDesc, String clubLogoId,
			String provinceId, String cityId, String schoolId, int addMemberType, int fLevelAccount,
			String gameBannerId, String teachingBannerId, String styleBannerId, String backgroundColorId,
			int actionType);

}