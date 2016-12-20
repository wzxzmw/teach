package com.seentao.stpedu.common.entity;


public class TeachPlanSign {
	private Integer start;
	
	private Integer limit;
	
	private Integer classId;//班级id
	
	private Integer signId;
	
	private Integer planId;
	
	private Integer studentId;
	
	private Integer signTime;
	
	private Integer isDelete;
	
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getSignId() {
		return signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}
	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getSignTime() {
		return signTime;
	}

	public void setSignTime(Integer signTime) {
		this.signTime = signTime;
	}

}
