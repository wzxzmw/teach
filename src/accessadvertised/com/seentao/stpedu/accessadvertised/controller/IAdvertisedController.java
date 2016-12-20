package com.seentao.stpedu.accessadvertised.controller;

public interface IAdvertisedController {

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
	public String getAds(Integer userId, Integer start, Integer limit, Integer adsType);
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
	public String getAdsForMobile(Integer userId, Integer start, Integer limit, Integer adsType);
	
}
