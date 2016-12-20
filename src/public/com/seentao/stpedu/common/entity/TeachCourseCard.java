package com.seentao.stpedu.common.entity;


public class TeachCourseCard {
	
	private String teacherHeadLink;//老师图片链接
	
	private Integer learningNum;//人数
	
	private String teacherName;//名称
	
	private String schoolName;//学校
	
	private String ccLink;//图片链接
	
	private Integer cardId;
	
	private String cardTitle;
	
	private String cardExplain;
	
	private Integer imageId;
	
	private Integer startTime;
	
	private Integer endTime;
	
	private Integer teacherId;
	
	private Integer statusId;
	
	private Integer chapterId;
	
	private Integer cardType;
	
	private Integer matchId;
	
	private Integer matchYear;
	
	private Integer isTheFirstYear;//比赛运行年是否为初始年
	
	private Integer clickCount;//新增字段(课程卡点击次数)
	
	private String strStartTime;//String类型开始时间
	
	private String strEndTime;//String类型结束时间
	
	

	public String getStrStartTime() {
		return strStartTime;
	}

	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	public String getStrEndTime() {
		return strEndTime;
	}

	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public Integer getIsTheFirstYear() {
		return isTheFirstYear;
	}

	public void setIsTheFirstYear(Integer isTheFirstYear) {
		this.isTheFirstYear = isTheFirstYear;
	}

	public String getTeacherHeadLink() {
		return teacherHeadLink;
	}

	public void setTeacherHeadLink(String teacherHeadLink) {
		this.teacherHeadLink = teacherHeadLink;
	}

	public Integer getLearningNum() {
		return learningNum;
	}

	public void setLearningNum(Integer learningNum) {
		this.learningNum = learningNum;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCcLink() {
		return ccLink;
	}

	public void setCcLink(String ccLink) {
		this.ccLink = ccLink;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCardTitle() {
		return cardTitle;
	}

	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}
	public String getCardExplain() {
		return cardExplain;
	}

	public void setCardExplain(String cardExplain) {
		this.cardExplain = cardExplain;
	}
	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}
	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	public Integer getMatchYear() {
		return matchYear;
	}

	public void setMatchYear(Integer matchYear) {
		this.matchYear = matchYear;
	}

}
