package com.seentao.stpedu.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IVirtualGoodsController {

	/***
	 * 获取登录用户的虚拟物品
	 * @param userName
	 * @param userId
	 * @param userType     用户类型
	 * @param userToken    用户唯一标识
	 * @param virtualType  虚拟物品类型
	 * @param inquireType  查询类型
	 * @author cxw
	 * @return
	 */
	String getVirtualGoods(String userId);

	/***
	 * 获取一级货币
	 * @param userId   用户id
	 * @param clubId   俱乐 部id
	 * @param inquireType 查询类型
	 * @author cxw
	 * @return
	 */
	String getFLevelAccount(String userId, String clubId, int inquireType);

}