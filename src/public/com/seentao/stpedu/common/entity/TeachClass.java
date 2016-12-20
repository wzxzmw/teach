package com.seentao.stpedu.common.entity;


public class TeachClass {

	private Integer classId;
	
	private String className;
	
	private Integer classLogoId;
	
	private Integer schoolId;
	
	private Integer instituteId;
	
	private Integer studentsNum;
	
	
	private Integer totalTrainNum;
	
	private Integer originalCourseNum;
	
	private Integer createTime;
	
	private Integer isDelete;
	
	private String teacherIds;//教师ID
	
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
	
	private Integer totalCompleteHours;//学时总秒数
	
	private String classIdStage;//String类型classId
	
	
	
	
	
	
	public String getClassIdStage() {
		return classIdStage;
	}

	public void setClassIdStage(String classIdStage) {
		this.classIdStage = classIdStage;
	}

	public Integer getTotalCompleteHours() {
		return totalCompleteHours;
	}

	public void setTotalCompleteHours(Integer totalCompleteHours) {
		this.totalCompleteHours = totalCompleteHours;
	}

	

	public String getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getClassLogoId() {
		return classLogoId;
	}

	public void setClassLogoId(Integer classLogoId) {
		this.classLogoId = classLogoId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Integer instituteId) {
		this.instituteId = instituteId;
	}
	public Integer getStudentsNum() {
		return studentsNum;
	}

	public void setStudentsNum(Integer studentsNum) {
		this.studentsNum = studentsNum;
	}
	
	public Integer getTotalTrainNum() {
		return totalTrainNum;
	}

	public void setTotalTrainNum(Integer totalTrainNum) {
		this.totalTrainNum = totalTrainNum;
	}
	public Integer getOriginalCourseNum() {
		return originalCourseNum;
	}

	public void setOriginalCourseNum(Integer originalCourseNum) {
		this.originalCourseNum = originalCourseNum;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

}
