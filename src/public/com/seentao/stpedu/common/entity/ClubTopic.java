package com.seentao.stpedu.common.entity;


public class ClubTopic {

	private Integer topicId;
	
	private String title;
	
	private String content;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer clubId;
	
	private Integer praiseNum;
	
	private Integer commentNum;
	
	private Integer isDelete;
	
	private Integer isTop;
	
	private Integer isFrozen;
	
	public ClubTopic(){
		
	}
	public ClubTopic(Integer createUserId){
		this.createUserId = createUserId ;
	}
	
	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	public Integer getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}

}
