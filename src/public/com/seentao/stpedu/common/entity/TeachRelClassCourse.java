package com.seentao.stpedu.common.entity;


public class TeachRelClassCourse {

	private Integer relId;
	
	private Integer classId;
	
	private Integer courseId;
	
	private Integer showType;
	
	private Integer createTime;
	
	private Integer totalStudyNum;
	

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
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getTotalStudyNum() {
		return totalStudyNum;
	}

	public void setTotalStudyNum(Integer totalStudyNum) {
		this.totalStudyNum = totalStudyNum;
	}

}
