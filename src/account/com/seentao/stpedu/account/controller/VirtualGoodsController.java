package com.seentao.stpedu.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.account.service.VirtualGoodsService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.StringUtil;

/**
 * 
 * @author cxw
 *
 */
@Controller
public class VirtualGoodsController implements IVirtualGoodsController {

	@Autowired
	private VirtualGoodsService virtualGoodsService;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.account.controller.IVirtualGoodsController#getVirtualGoods(java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "getVirtualGoods")
	public String getVirtualGoods(String userId){
		if("".equals(userId)){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
		}
		return virtualGoodsService.getVirtualGoodsCount(userId).toJSONString();
		
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.account.controller.IVirtualGoodsController#getFLevelAccount(java.lang.String, java.lang.String, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "getFLevelAccount")
	public String getFLevelAccount(String userId,String clubId,int inquireType){
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(clubId)){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
		}
	  return virtualGoodsService.getFLevelAccount(userId,clubId,inquireType);
	}
}
