package com.seentao.stpedu.common.entity;

public class ClubJoinTraining {
	private Integer start;

	private Integer limit;

	private Integer clubId;// 俱乐部id

	private String nickName;// 用户真实姓名

	private Integer joinId;

	private Integer classId;

	private Integer userId;

	private Integer joinTime;

	private Double totalCost;

	private Integer isDelete;
	private Integer isjoin;
	private Integer classType;


	public ClubJoinTraining(){
		
	}
	public ClubJoinTraining(Integer isDelete,Integer userId,Integer classId){
		this.isDelete = isDelete;
		this.userId= userId;
		this.classId = classId;
	}
	public Integer getIsjoin() {
		return isjoin;
	}

	public void setIsjoin(Integer isjoin) {
		this.isjoin = isjoin;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	private String teacherHeadLink;
	private String classLogoLink;
	private String className;
	private String teacherIds;
	private String teacherName;
	private Integer studentsNum;
	private Integer createTime;
	private Integer classLogoId;
	private String introduce;
	private Integer costType;
	private Double costAmount;
	private Integer clubCourseCount;
	private Integer hasBuyOfficialCourse;
	private Double bocNeedFLevelAccount;
	private String clubName;
	private String classIdStage;

	public String getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
	}

	public String getClassIdStage() {
		return classIdStage;
	}

	public void setClassIdStage(String classIdStage) {
		this.classIdStage = classIdStage;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getTeacherHeadLink() {
		return teacherHeadLink;
	}

	public void setTeacherHeadLink(String teacherHeadLink) {
		this.teacherHeadLink = teacherHeadLink;
	}

	public String getClassLogoLink() {
		return classLogoLink;
	}

	public void setClassLogoLink(String classLogoLink) {
		this.classLogoLink = classLogoLink;
	}

	public Integer getStudentsNum() {
		return studentsNum;
	}

	public void setStudentsNum(Integer studentsNum) {
		this.studentsNum = studentsNum;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getClassLogoId() {
		return classLogoId;
	}

	public void setClassLogoId(Integer classLogoId) {
		this.classLogoId = classLogoId;
	}

	public Integer getCostType() {
		return costType;
	}

	public void setCostType(Integer costType) {
		this.costType = costType;
	}

	public Double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(Double costAmount) {
		this.costAmount = costAmount;
	}

	public Integer getClubCourseCount() {
		return clubCourseCount;
	}

	public void setClubCourseCount(Integer clubCourseCount) {
		this.clubCourseCount = clubCourseCount;
	}

	public Integer getHasBuyOfficialCourse() {
		return hasBuyOfficialCourse;
	}

	public void setHasBuyOfficialCourse(Integer hasBuyOfficialCourse) {
		this.hasBuyOfficialCourse = hasBuyOfficialCourse;
	}

	public Double getBocNeedFLevelAccount() {
		return bocNeedFLevelAccount;
	}

	public void setBocNeedFLevelAccount(Double bocNeedFLevelAccount) {
		this.bocNeedFLevelAccount = bocNeedFLevelAccount;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}

	public Integer getJoinId() {
		return joinId;
	}

	public void setJoinId(Integer joinId) {
		this.joinId = joinId;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Integer joinTime) {
		this.joinTime = joinTime;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
