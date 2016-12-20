package com.seentao.stpedu.persionalcenter.model;

public class Messages {
	
	private long messageId;
	private String messageType;
	private String mmCreaterId;
	private String mmCreaterName;
	private String mmCreaterNickname;
	private String mmCreaterHeadLink;
	private String messageMainTitle;
	private int mmCreateDate;
	private int mmTalkCount;
	private String messageMainId;
	private Integer enrollType;
	private String mmClubId;
	private String mmClubName;
	private String mmSchoolId;
	private String mmSchoolName;
	private String inviteDesc;
	private Integer classType;//修改类型(yy修改)

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	public Messages() {
		super();
	}

	public Messages(long messageId, String messageType, String mmCreaterId, String mmCreaterName,
			String mmCreaterNickname, String mmCreaterHeadLink, String messageMainTitle, int mmCreateDate,
			int mmTalkCount, String messageMainId, int enrollType, String mmClubId, String mmClubName,
			String mmSchoolId, String mmSchoolName, String inviteDesc) {
		this.messageId = messageId;
		this.messageType = messageType;
		this.mmCreaterId = mmCreaterId;
		this.mmCreaterName = mmCreaterName;
		this.mmCreaterNickname = mmCreaterNickname;
		this.mmCreaterHeadLink = mmCreaterHeadLink;
		this.messageMainTitle = messageMainTitle;
		this.mmCreateDate = mmCreateDate;
		this.mmTalkCount = mmTalkCount;
		this.enrollType = enrollType;
		this.mmClubId = mmClubId;
		this.mmClubName = mmClubName;
		this.mmSchoolId = mmSchoolId;
		this.mmSchoolName = mmSchoolName;
		this.inviteDesc = inviteDesc;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMmCreaterId() {
		return mmCreaterId;
	}

	public void setMmCreaterId(String mmCreaterId) {
		this.mmCreaterId = mmCreaterId;
	}

	public String getMmCreaterName() {
		return mmCreaterName;
	}

	public void setMmCreaterName(String mmCreaterName) {
		this.mmCreaterName = mmCreaterName;
	}

	public String getMmCreaterNickname() {
		return mmCreaterNickname;
	}

	public void setMmCreaterNickname(String mmCreaterNickname) {
		this.mmCreaterNickname = mmCreaterNickname;
	}

	public String getMmCreaterHeadLink() {
		return mmCreaterHeadLink;
	}

	public void setMmCreaterHeadLink(String mmCreaterHeadLink) {
		this.mmCreaterHeadLink = mmCreaterHeadLink;
	}

	public String getMessageMainTitle() {
		return messageMainTitle;
	}

	public void setMessageMainTitle(String messageMainTitle) {
		this.messageMainTitle = messageMainTitle;
	}

	public int getMmCreateDate() {
		return mmCreateDate;
	}

	public void setMmCreateDate(int mmCreateDate) {
		this.mmCreateDate = mmCreateDate;
	}

	public int getMmTalkCount() {
		return mmTalkCount;
	}

	public void setMmTalkCount(int mmTalkCount) {
		this.mmTalkCount = mmTalkCount;
	}

	public String getMessageMainId() {
		return messageMainId;
	}

	public void setMessageMainId(String messageMainId) {
		this.messageMainId = messageMainId;
	}

	public Integer getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(Integer enrollType) {
		this.enrollType = enrollType;
	}

	public String getMmClubId() {
		return mmClubId;
	}

	public void setMmClubId(String mmClubId) {
		this.mmClubId = mmClubId;
	}

	public String getMmClubName() {
		return mmClubName;
	}

	public void setMmClubName(String mmClubName) {
		this.mmClubName = mmClubName;
	}

	public String getMmSchoolId() {
		return mmSchoolId;
	}

	public void setMmSchoolId(String mmSchoolId) {
		this.mmSchoolId = mmSchoolId;
	}

	public String getMmSchoolName() {
		return mmSchoolName;
	}

	public void setMmSchoolName(String mmSchoolName) {
		this.mmSchoolName = mmSchoolName;
	}

	public String getInviteDesc() {
		return inviteDesc;
	}

	public void setInviteDesc(String inviteDesc) {
		this.inviteDesc = inviteDesc;
	}

}
