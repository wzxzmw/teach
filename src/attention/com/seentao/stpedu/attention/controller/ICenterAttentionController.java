package com.seentao.stpedu.attention.controller;

import java.text.ParseException;


public interface ICenterAttentionController {

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
	String submitAttention(Integer userId, Integer actionType, Integer actionObjectType, String actionObjectId);

}