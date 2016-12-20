package com.seentao.stpedu.attention.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.attention.service.CenterAttentionService;

/**
 * 
 *@author ligs
 */
@Controller
public class CenterAttentionController implements ICenterAttentionController{
	
	@Autowired
	private CenterAttentionService centerAttentionService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.ICenterAttentionController#getCenterAttention(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	@Override
	/**
	 * 
	 * submitAttention(加关注取消关注)   
	 * @param userId 用户id
	 * @param actionType 操作类型
	 * @param actionObjectType 操作对象类型
	 * @param actionObjectId 操作对象id
	 * @author ligs
	 * @date 2016年6月18日 上午9:52:46
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/submitAttention")
	public String submitAttention(Integer userId,Integer actionType,Integer actionObjectType,String actionObjectId){
		return centerAttentionService .submitAttention(userId,actionType,actionObjectType,actionObjectId).toJSONString();
	}
	
}