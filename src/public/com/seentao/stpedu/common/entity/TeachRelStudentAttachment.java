package com.seentao.stpedu.common.entity;


public class TeachRelStudentAttachment {

	private Integer relId;
	
	private Integer studentId;
	
	private Integer attaId;
	
	private Integer courseHour;
	
	private Integer isStudy;
	

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getAttaId() {
		return attaId;
	}

	public void setAttaId(Integer attaId) {
		this.attaId = attaId;
	}
	public Integer getCourseHour() {
		return courseHour;
	}

	public void setCourseHour(Integer courseHour) {
		this.courseHour = courseHour;
	}
	public Integer getIsStudy() {
		return isStudy;
	}

	public void setIsStudy(Integer isStudy) {
		this.isStudy = isStudy;
	}

}
