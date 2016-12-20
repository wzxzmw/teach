package com.seentao.stpedu.club.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.SubmitClubOperationService;
/**
 * @author cxw
 */
@Controller
public class SubmitClubOperationController implements ISubmitClubOperationController {
	
	@Autowired
	private SubmitClubOperationService submitClubOperationService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.club.controller.ISubmitClubOperationController#submitClubOperation(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitClubOperation")
	public String submitClubOperation(String userId,String clubId,int actionType,String applicationContent,String clubMemberId) throws ParseException{
		
		return submitClubOperationService.operateclub(userId,clubId,actionType,applicationContent,clubMemberId);
	}
}
