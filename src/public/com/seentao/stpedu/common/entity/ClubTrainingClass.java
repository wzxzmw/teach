package com.seentao.stpedu.common.entity;


public class ClubTrainingClass {

	private Integer classId;
	
	private Integer clubId;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private String title;
	
	private String introduce;
	
	private Integer imageId;
	
	private Integer costType;
	
	private Double costAmount;
	
	private Integer studentNum;
	
	private Integer isDelete;
	
	private Integer courseNum;
	
	private Integer isBuyOfficial;//是否已购买官方课程
	
	private  String teacherName;//教练名称
	
	private String teacherIds;
	
	private  String teacherHeadLink;//教练头像
	
	private  Integer classType = 2;//班级类型 2：俱乐部培训班
	
	private Integer classLogoId;//班级logoid
	
	private String classLogoLink;//班级logo地址
	
	private Integer isjoin;//是否已加入该培训班
	
	private Integer clubCourseCount;//培训班课程数
	
	private Integer hasBuyOfficialCourse;//是否已购买课程包
	
	private double bocNeedFLevelAccount;//购买官方课程所需要的货币
	
	private String clubName;//俱乐部名称
	
	private String className;//班级名称
	
	private String classIdStage;//前台classId
	
	private Integer isDefaultClass;//是否默认
	
	
	private Integer studentsNum;
	
	
		
	
	
	
	
	public Integer getIsDefaultClass() {
		return isDefaultClass;
	}

	public void setIsDefaultClass(Integer isDefaultClass) {
		this.isDefaultClass = isDefaultClass;
	}

	public String getClassIdStage() {
		return classIdStage;
	}

	public void setClassIdStage(String classIdStage) {
		this.classIdStage = classIdStage;
	}

	public Integer getClassLogoId() {
		return classLogoId;
	}

	public void setClassLogoId(Integer classLogoId) {
		this.classLogoId = classLogoId;
	}

	public Integer getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(Integer courseNum) {
		this.courseNum = courseNum;
	}

	

	public String getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
	}

	public Integer getStudentsNum() {
		return studentsNum;
	}

	public void setStudentsNum(Integer studentsNum) {
		this.studentsNum = studentsNum;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsBuyOfficial() {
		return isBuyOfficial;
	}

	public void setIsBuyOfficial(Integer isBuyOfficial) {
		this.isBuyOfficial = isBuyOfficial;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}


	public double getBocNeedFLevelAccount() {
		return bocNeedFLevelAccount;
	}

	public void setBocNeedFLevelAccount(double bocNeedFLevelAccount) {
		this.bocNeedFLevelAccount = bocNeedFLevelAccount;
	}

	public Integer getHasBuyOfficialCourse() {
		return hasBuyOfficialCourse;
	}

	public void setHasBuyOfficialCourse(Integer hasBuyOfficialCourse) {
		this.hasBuyOfficialCourse = hasBuyOfficialCourse;
	}

	
	

	public Integer getClubCourseCount() {
		return clubCourseCount;
	}

	public void setClubCourseCount(Integer clubCourseCount) {
		this.clubCourseCount = clubCourseCount;
	}

	public Integer getIsjoin() {
		return isjoin;
	}

	public void setIsjoin(Integer isjoin) {
		this.isjoin = isjoin;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherHeadLink() {
		return teacherHeadLink;
	}

	public void setTeacherHeadLink(String teacherHeadLink) {
		this.teacherHeadLink = teacherHeadLink;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	public String getClassLogoLink() {
		return classLogoLink;
	}

	public void setClassLogoLink(String classLogoLink) {
		this.classLogoLink = classLogoLink;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
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
	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

}
