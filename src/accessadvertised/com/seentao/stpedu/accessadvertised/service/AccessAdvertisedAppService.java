package com.seentao.stpedu.accessadvertised.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.entity.ArenaAd;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class AccessAdvertisedAppService {

	
	
	
	/**
	 * 获取广告信息
	 * @param userId	用户id
	 * @param start
	 * @param limit
	 * @param adsType	广告类型
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  下午2:26:36
	 */
	public String getAdvertisements(Integer start, Integer limit, Integer adsType) {
		
		//TODO 默认返回 广告跳转类型 ：1:赛事详情页； 还没设计完
		if(GameConstants.AD_MATCH_TYPE == adsType){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("adType", GameConstants.AD_MATCH_TYPE);
			param.put("isDelete", GameConstants.NO_DEL);
			QueryPage<ArenaAd> queryPage = QueryPageComponent.queryPageByRedisSimple(limit, start, param, ArenaAd.class);
			for(ArenaAd arenaAd : queryPage.getList()){
				/*
				 * 压缩图片
				 */
				arenaAd.setAdsImgLink(Common.checkPic(arenaAd.getAdsImgLink())==true ? arenaAd.getAdsImgLink()+ActiveUrl.ROTATION_MAP:arenaAd.getAdsImgLink());
			}
			String msg = queryPage.getMessageJSONObject("ads").toJSONString();
			return msg;
		}
		
		LogUtil.error(this.getClass(), "getAds", AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM).toJSONString();
	}
	
	/**
	 * 获取广告信息
	 * @param userId	用户id
	 * @param start
	 * @param limit
	 * @param adsType	广告类型
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  下午2:26:36
	 */
	public String getAdsForMobile(Integer start, Integer limit, Integer adsType) {
		
		//TODO 默认返回 广告跳转类型 ：1:赛事详情页； 还没设计完
		if(GameConstants.AD_MATCH_TYPE == adsType){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("adType", GameConstants.AD_MATCH_TYPE);
			param.put("isDelete", GameConstants.NO_DEL);
			QueryPage<ArenaAd> queryPage = QueryPageComponent.queryPageByRedisSimple(limit, start, param, ArenaAd.class);
			for(ArenaAd arenaAd : queryPage.getList()){
				/*
				 * 压缩图片
				 */
				arenaAd.setAdsImgLink(Common.checkPic(arenaAd.getAdsImgLink())==true ? arenaAd.getAdsImgLink()+ActiveUrl.HEAD_MAP:arenaAd.getAdsImgLink());
			}
			String msg = queryPage.getMessageJSONObject("ads").toJSONString();
			return msg;
		}
		
		LogUtil.error(this.getClass(), "getAds", AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ADVERTISED_REQUEST_PARAM).toJSONString();
	}

}
