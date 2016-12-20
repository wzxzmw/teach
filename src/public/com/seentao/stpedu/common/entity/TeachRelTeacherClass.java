package com.seentao.stpedu.common.entity;


public class TeachRelTeacherClass {

	private Integer relId;
	
	private Integer classId;
	
	private Integer teacherId;
	
	private Integer isDefault;
	
	
	private String className;
	
	private  String teacherName;//教师名称
	
	private  String teacherHeadLink;//教师头像
	
	private  Integer classType = 1;//班级类型 1：教学班
	
	private String schoolName;//学校名称
	
	private String collegeName;//学院名称
	
	private Integer isDefaultClass;//是否默认
	
	private String classLogoLink;//班级logo地址
	
	private Integer learningSeconds;//学时总秒数
	
	private Integer practiceCount;//实训次数
	
	private Integer courseCounts;//原创课时数

	private Integer studentsNum;
	
	private String teacherIds;
	
	private String classIdStage;
	
	
	

	public String getClassIdStage() {
		return classIdStage;
	}

	public void setClassIdStage(String classIdStage) {
		this.classIdStage = classIdStage;
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public Integer getIsDefaultClass() {
		return isDefaultClass;
	}

	public void setIsDefaultClass(Integer isDefaultClass) {
		this.isDefaultClass = isDefaultClass;
	}

	public String getClassLogoLink() {
		return classLogoLink;
	}

	public void setClassLogoLink(String classLogoLink) {
		this.classLogoLink = classLogoLink;
	}

	public Integer getLearningSeconds() {
		return learningSeconds;
	}

	public void setLearningSeconds(Integer learningSeconds) {
		this.learningSeconds = learningSeconds;
	}

	public Integer getPracticeCount() {
		return practiceCount;
	}

	public void setPracticeCount(Integer practiceCount) {
		this.practiceCount = practiceCount;
	}

	public Integer getCourseCounts() {
		return courseCounts;
	}

	public void setCourseCounts(Integer courseCounts) {
		this.courseCounts = courseCounts;
	}

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
