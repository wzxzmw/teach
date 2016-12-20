package com.seentao.stpedu.common.entity;


public class ClubTopicComment {

	private Integer commentId;
	
	private Integer topicId;
	
	private String content;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer praiseNum;
	
	private Integer isFrozen;
	

	private String  createrName;//评论发布者姓名
	
	private String createrNickname;//评论发布者昵称
	
	private String createrHeadLink;//评论发布者头像链接地址
	
	private Integer type;
	
	private String createrId;
	
	private String discussId;
	
	
	public String getDiscussId() {
		return discussId;
	}
	public void setDiscussId(String discussId) {
		this.discussId = discussId;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public ClubTopicComment (){
		
	}
	public ClubTopicComment(Integer createUserId){
		this.createUserId = createUserId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreaterNickname() {
		return createrNickname;
	}

	public void setCreaterNickname(String createrNickname) {
		this.createrNickname = createrNickname;
	}

	public String getCreaterHeadLink() {
		return createrHeadLink;
	}

	public void setCreaterHeadLink(String createrHeadLink) {
		this.createrHeadLink = createrHeadLink;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	public Integer getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}

}
