package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterFeedbackDao;
import com.seentao.stpedu.common.entity.CenterFeedback;
import com.seentao.stpedu.error.AppErrorCode;

@Service
public class CenterFeedbackService{
	
	@Autowired
	private CenterFeedbackDao centerFeedbackDao;
	
	

	/** 
	* @Title: addCenterFeedback 
	* @Description: 提交意见反馈
	* @param userId	用户ID
	* @param content	意见内容
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午8:04:28
	*/
	public String addCenterFeedback(Integer userId, String content) {
		CenterFeedback centerFeedback = new CenterFeedback();
		centerFeedback.setCreateTime(Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000)));
		centerFeedback.setCreateUserId(userId);
		centerFeedback.setContent(content);
		centerFeedbackDao.insertCenterFeedback(centerFeedback);
		return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
	}
}