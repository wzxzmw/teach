package com.seentao.stpedu.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业表
 * @author lw
 * @date   2016年6月18日 上午9:36:34
 */
public class TeachStudentHomework {

	private Integer homeworkId;
	
	private String homeworkExplain;
	
	private Integer studentId;
	
	private Integer submitTime;
	
	private Integer courseId;
	
	private String assessContent;
	
	private Integer assessScore;
	
	private Integer assessTime;
	
	private Integer teacherId;
	
	private Integer isDelete;
	//学生名称
	private String studentName;
	//学生头像链接地址
	private String studentHeadLink;
	//附件
	private List<PublicAttachment> homeworkFiles = new ArrayList<PublicAttachment>();
	
	//教师名称
	private String teacherName;
	//教师头像链接地址
	private String teacherHeadLink;
	//教师所在学校
	private String schoolName;
	//是否平分
	private Integer hasScored = 0;
	
	
	//数据库查询参数传递
	private String courseIds;

	public String getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(String courseIds) {
		this.courseIds = courseIds;
	}

	public void setHasScored(Integer hasScored) {
		this.hasScored = hasScored;
	}

	public Integer getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Integer homeworkId) {
		this.homeworkId = homeworkId;
	}
	public String getHomeworkExplain() {
		return homeworkExplain;
	}

	public void setHomeworkExplain(String homeworkExplain) {
		this.homeworkExplain = homeworkExplain;
	}
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Integer submitTime) {
		this.submitTime = submitTime;
	}
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getAssessContent() {
		return assessContent;
	}

	public void setAssessContent(String assessContent) {
		this.assessContent = assessContent;
	}
	public Integer getAssessScore() {
		return assessScore;
	}

	public void setAssessScore(Integer assessScore) {
		this.assessScore = assessScore;
	}
	public Integer getAssessTime() {
		return assessTime;
	}

	public void setAssessTime(Integer assessTime) {
		this.assessTime = assessTime;
	}
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentHeadLink() {
		return studentHeadLink;
	}

	public void setStudentHeadLink(String studentHeadLink) {
		this.studentHeadLink = studentHeadLink;
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getHasScored() {
		return hasScored;
	}
	
	public void hasScored(){
		if(this.assessTime > 0 ){
			this.hasScored = 1;
		}
	}

	public List<PublicAttachment> getHomeworkFiles() {
		return homeworkFiles;
	}

	public void setHomeworkFiles(List<PublicAttachment> homeworkFiles) {
		this.homeworkFiles = homeworkFiles;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}



}
