package com.seentao.stpedu.common.entity;


public class TeachSchool {

	private Integer schoolId;
	
	private String schoolName;
	
	private Integer regionId;
	
	private String schoolType;
	
	private String schoolProperties; 
	
	private Integer studentNum;//学生人数
	
	private Integer learningSeconds;//学时总秒数
	
	private Integer practiceCount;//实训次数
	
	private Integer courseCount;//原创课时数
	
	private Integer provinceId;//学校所在省ID
	
	private String provinceName;//学校所在省名称
	
	private String strSchoolId;//String学校id
	
	
	public String getStrSchoolId() {
		return strSchoolId;
	}

	public void setStrSchoolId(String strSchoolId) {
		this.strSchoolId = strSchoolId;
	}

	public String getSchoolProperties() {
		return schoolProperties;
	}

	public void setSchoolProperties(String schoolProperties) {
		this.schoolProperties = schoolProperties;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
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

	public Integer getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(Integer courseCount) {
		this.courseCount = courseCount;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	

}
