package com.seentao.stpedu.common.entity;


public class TeachRelStudentClass {
	
	private Integer start;
	
	private Integer limit;
	
	private Integer relId;
	
	private Integer classId;
	
	private Integer studentId;
	
	private Integer assessScore;
	
	private Integer isIdentify;
	
	private Integer certificateStatus;
	
	private Integer isDelete;
	
	

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getAssessScore() {
		return assessScore;
	}

	public void setAssessScore(Integer assessScore) {
		this.assessScore = assessScore;
	}
	public Integer getIsIdentify() {
		return isIdentify;
	}

	public void setIsIdentify(Integer isIdentify) {
		this.isIdentify = isIdentify;
	}
	
	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}

	public Integer getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(Integer certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

}
