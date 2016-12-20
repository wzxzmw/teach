package com.seentao.stpedu.common.entity;


public class ClubRelClassCourseMember {

	private Integer relId;
	
	private Integer courseId;
	
	private Integer studentId;
	
	private Integer isStudy;
	

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getIsStudy() {
		return isStudy;
	}

	public void setIsStudy(Integer isStudy) {
		this.isStudy = isStudy;
	}

}
