package com.seentao.stpedu.common.entity;



public class CenterNews {

	private Integer newsId;
	
	private Integer createTime;
	
	private Integer userId;
	
	private Integer newsType;
	
	private String content;
	
	private Integer relObjectType;
	
	private Integer relObjectId;
	
	private Integer createUserId;
	

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getNewsType() {
		return newsType;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getRelObjectType() {
		return relObjectType;
	}

	public void setRelObjectType(Integer relObjectType) {
		this.relObjectType = relObjectType;
	}
	public Integer getRelObjectId() {
		return relObjectId;
	}

	public void setRelObjectId(Integer relObjectId) {
		this.relObjectId = relObjectId;
	}

}
