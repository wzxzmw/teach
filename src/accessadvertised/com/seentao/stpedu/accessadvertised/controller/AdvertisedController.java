package com.seentao.stpedu.accessadvertised.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.accessadvertised.service.AccessAdvertisedAppService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;




@Controller
public class AdvertisedController implements IAdvertisedController {

	
	@Autowired
	private AccessAdvertisedAppService appService;
	
	
	
	/**
	 * 获取广告信息
	 * @param userId		用户id
	 * @param start		
	 * @param limit
	 * @param adsType		广告类型
	 * @return
	 * @author 				lw
	 * @date				2016年6月28日  下午2:26:41
	 */
	@ResponseBody
	public String getAds(Integer userId, Integer start, Integer limit, Integer adsType){
		
		if(userId == null || adsType == null){
			LogUtil.error(this.getClass(), "getAds", AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM).toJSONString();
		}
		
		return appService.getAdvertisements(start, limit, adsType);
	}
	
	/**
	 * 获取广告信息
	 * @param userId		用户id
	 * @param start		
	 * @param limit
	 * @param adsType		广告类型
	 * @return
	 * @author 				lw
	 * @date				2016年6月28日  下午2:26:41
	 */
	@ResponseBody
	public String getAdsForMobile(Integer userId, Integer start, Integer limit, Integer adsType){
		
		if(userId == null || adsType == null){
			LogUtil.error(this.getClass(), "getAds", AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM).toJSONString();
		}
		
		return appService.getAdsForMobile(start, limit, adsType);
	}
}
