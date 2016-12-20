package com.seentao.stpedu.invitation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.entity.CenterInvitationCode;
import com.seentao.stpedu.common.service.CenterInvitationCodeService;
import com.seentao.stpedu.utils.LogUtil;

@Controller
public class InvitationController {
	
	@Autowired
	private CenterInvitationCodeService centerInvitationCodeService;

	/**
	 * 生成邀请码
	 * @param num 邀请码个数
	 * @return
	 * @author chengshx
	 */
	@RequestMapping("/genInvitationCode")
	@ResponseBody
	public String genInvitationCode(Integer num){
		if(num == null || num < 0){
			return "请输入生成邀请码的个数！";
		}
		List<CenterInvitationCode> list = new ArrayList<CenterInvitationCode>();
		for(int i = 0; i < num; i++){
			StringBuilder sb = new StringBuilder();
			String code = UUID.randomUUID().toString();
			sb.append(code.substring(0, 8));
			sb.append(code.substring(code.length() - 2, code.length()));
			CenterInvitationCode centerInvitationCode = new CenterInvitationCode();
			centerInvitationCode.setInvitationCode(sb.toString().toUpperCase());
			centerInvitationCode.setStatus(1);
			list.add(centerInvitationCode);
		}
		// 批量插入邀请码
		centerInvitationCodeService.batchInsertCenterInvitationCode(list);
		LogUtil.info(this.getClass(), "genInvatationCode", "生成【" + num + "】个邀请码成功！");
		return "生成【" + num + "】个邀请码成功！";
	}
}
