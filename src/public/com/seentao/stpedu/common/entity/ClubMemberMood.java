package com.seentao.stpedu.common.entity;


public class ClubMemberMood {

	private Integer moodId;
	
	private Integer clubId;
	
	private String content;
	
	private Integer createTime;
	
	private Integer createUserId;
	
	private Integer praiseNum;
	
	private Integer againstNum;
	
	private Integer isFrozen;

	public ClubMemberMood(){
		super();
	}
	public ClubMemberMood(Integer createUserId){
		this.createUserId =  createUserId;
	}
	public ClubMemberMood(Integer clubId,String content, Integer createTime, Integer createUserId,Integer praiseNum,Integer againstNum,Integer isFrozen){
		this.clubId=clubId;
		this.content=content;
		this.createTime=createTime;
		this.createUserId=createUserId;
		this.praiseNum=praiseNum;
		this.againstNum=againstNum;
		this.isFrozen=isFrozen;
		
	}
	
	public Integer getMoodId() {
		return moodId;
	}

	public void setMoodId(Integer moodId) {
		this.moodId = moodId;
	}
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
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
	public Integer getAgainstNum() {
		return againstNum;
	}

	public void setAgainstNum(Integer againstNum) {
		this.againstNum = againstNum;
	}
	public Integer getIsFrozen() {
		return isFrozen;
	}
	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}

}
