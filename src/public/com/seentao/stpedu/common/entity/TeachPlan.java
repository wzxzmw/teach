package com.seentao.stpedu.common.entity;


public class TeachPlan {
	
	private Integer studentId;//学生id
	
	private Integer planId;
	
	private Integer classId;
	
	private Integer planDate;
	
	private String planTask;
	
	private String planTarget;
	
	private Integer isSign;
	
	private Integer planSignNum;
	
	private Integer actualSignNum;
	
	private Integer createTime;

	private Integer isDelete;
	
	private String taskDate;//计划发生日期的时间戳
	
	private Integer taskYear;//计划任务发生时间的年
	
	private Integer taskMonth;//计划任务发生时间的月
	
	private Integer taskDay;//计划任务发生时间的日
	
	private Integer hasSigned;//当前登录者是否已经签到
	
	
	
	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public Integer getTaskYear() {
		return taskYear;
	}

	public void setTaskYear(Integer taskYear) {
		this.taskYear = taskYear;
	}

	public Integer getTaskMonth() {
		return taskMonth;
	}

	public void setTaskMonth(Integer taskMonth) {
		this.taskMonth = taskMonth;
	}

	public Integer getTaskDay() {
		return taskDay;
	}

	public void setTaskDay(Integer taskDay) {
		this.taskDay = taskDay;
	}

	public Integer getHasSigned() {
		return hasSigned;
	}

	public void setHasSigned(Integer hasSigned) {
		this.hasSigned = hasSigned;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Integer planDate) {
		this.planDate = planDate;
	}
	public String getPlanTask() {
		return planTask;
	}

	public void setPlanTask(String planTask) {
		this.planTask = planTask;
	}
	public String getPlanTarget() {
		return planTarget;
	}

	public void setPlanTarget(String planTarget) {
		this.planTarget = planTarget;
	}
	public Integer getIsSign() {
		return isSign;
	}

	public void setIsSign(Integer isSign) {
		this.isSign = isSign;
	}
	public Integer getPlanSignNum() {
		return planSignNum;
	}

	public void setPlanSignNum(Integer planSignNum) {
		this.planSignNum = planSignNum;
	}
	public Integer getActualSignNum() {
		return actualSignNum;
	}

	public void setActualSignNum(Integer actualSignNum) {
		this.actualSignNum = actualSignNum;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

}
