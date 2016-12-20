package com.seentao.stpedu.common.entity;


public class ArenaAd {

	private Integer adId;
	
	private String title;
	
	private String linkUrl;
	
	private Integer imgId;
	
	private Integer adType;
	
	private Integer isDelete;
	
	private Integer adMainId;
	
	private Integer adMainType;
	
	//广告图片链接地址
	private String adsImgLink;
	//广告跳转类型 1:赛事详情页；
	private String adsGotoType = "1";
	

	public String getAdsGotoType() {
		return adsGotoType;
	}

	public void setAdsGotoType(String adsGotoType) {
		this.adsGotoType = adsGotoType;
	}

	public String getAdsImgLink() {
		return adsImgLink;
	}

	public void setAdsImgLink(String adsImgLink) {
		this.adsImgLink = adsImgLink;
	}

	public Integer getAdMainId() {
		return adMainId;
	}

	public void setAdMainId(Integer adMainId) {
		this.adMainId = adMainId;
	}

	public Integer getAdMainType() {
		return adMainType;
	}

	public void setAdMainType(Integer adMainType) {
		this.adMainType = adMainType;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getImgId() {
		return imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}


}
