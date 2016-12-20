package com.seentao.stpedu.attention.controller;

import java.text.ParseException;


public interface IInformationController {

	/**
	 * submitInvitation(邀请其他人或推送俱乐部回答问题操作)   
	 * @param userId 用户ID
	 * @param invitationType  邀请对象类型    1:邀请人；2:推送俱乐部；
	 * @param invitationObjectId 邀请对象id 人员id或俱乐部id
	 * @param questionId 问题id
	 * @param classId 问题所属班级ID
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @author ligs
	 * @date 2016年6月28日 上午11:46:16
	 * @return
	 * @throws ParseException 
	 */
	String submitInvitation(Integer userId, Integer invitationType, Integer invitationObjectId, Integer questionId,
			Integer classId, Integer classType);

}