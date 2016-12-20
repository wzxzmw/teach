package com.seentao.stpedu.common.entity;


public class ArenaCompetitionDiscuss {

	private Integer discussId;
	
	private Integer compId;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer praiseNum;
	
	private String content;
	
	private Integer isDelete;
	

	private String  createrName;//评论发布者姓名
	
	private String createrNickname;//评论发布者昵称
	
	private String createrHeadLink;//评论发布者头像链接地址
	
	private String commentId;//群组评论信息id
	
	private String createrId;
	
	private Integer type;
	
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
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

	public Integer getDiscussId() {
		return discussId;
	}

	public void setDiscussId(Integer discussId) {
		this.discussId = discussId;
	}
	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
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
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
