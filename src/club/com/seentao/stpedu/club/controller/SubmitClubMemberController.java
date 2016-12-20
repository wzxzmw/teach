package com.seentao.stpedu.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.SubmitClubMemberService;
/**
 * @author cxw
 */
@Controller
public class SubmitClubMemberController implements ISubmitClubMemberController {

	@Autowired
	private SubmitClubMemberService clubMemberService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.club.controller.ISubmitClubMemberController#SubmitClubMember(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="/submitClubMember")
	public String submitClubMember(String userId,String clubId,int actionType,String clubMemberId){

		return clubMemberService.submitClubMember(userId,clubId,actionType,clubMemberId);

	}
}
