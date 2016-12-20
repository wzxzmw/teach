package com.seentao.stpedu.attention.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.attention.service.InformationService;

/**
 * 
 * <pre>项目名称：stpedu    
 * 类名称：InformationController    
 */
@Controller
public class InformationController implements IInformationController {
	
	@Autowired
	private InformationService informationService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.IInformationController#submitInvitation(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	@Override
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
	@ResponseBody
	@RequestMapping("/submitInvitation")
	public String submitInvitation(Integer userId,Integer invitationType,Integer invitationObjectId,Integer questionId,Integer classId,Integer classType){
		return informationService.submitInvitation( userId, invitationType, invitationObjectId, questionId ,classId ,classType).toJSONString();
	}
		
}
