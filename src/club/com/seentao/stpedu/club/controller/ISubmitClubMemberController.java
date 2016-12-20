package com.seentao.stpedu.club.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ISubmitClubMemberController {

	/**
	 * 俱乐部会员操作
	 * @param userId  用户id
	 * @param clubId  俱乐部id
	 * @param actionType 操作 1:审核通过；2:审核拒绝加入；3:设为教练；4:取消教练资格；
	 * @param clubMemberId 会员id
	 * @author cxw
	 * @return
	 */
	String submitClubMember(String userId, String clubId, int actionType, String clubMemberId);

}