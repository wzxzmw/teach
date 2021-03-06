package com.seentao.stpedu.common.entity;


public class ClubTrainingDiscuss {

	private Integer discussId;
	
	private Integer classId;
	
	private String content;
	
	private Integer createTime;
	
	private Integer createUserId;
	
	private Integer praiseNum;
	
	private Integer isDelete;
	
	private String  createrName;//评论发布者姓名
	
	private String createrNickname;//评论发布者昵称
	
	private String createrHeadLink;//评论发布者头像链接地址
	
	private String createrId;//评论发布者id
	
	private Integer type;//群主评论类型
	
	private String commentId;//群组评论信息id
	
	
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

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
	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
