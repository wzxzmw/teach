package com.seentao.stpedu.common.entity;

/**
 * 获取教师信息
 * <pre>项目名称：stpedu    
 * 类名称：TeacherMessage    
 */
public class TeacherMessage {
	
	private Integer teacherId;//教师id
	
	private String teacherName;//教师名称
	
	private String teacherSchool;//教师所在学校
	
	private String teacherDesc;//教师介绍
	
	private String teacherHeadLink;//教师头像链接地址

	

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherSchool() {
		return teacherSchool;
	}

	public void setTeacherSchool(String teacherSchool) {
		this.teacherSchool = teacherSchool;
	}

	public String getTeacherDesc() {
		return teacherDesc;
	}

	public void setTeacherDesc(String teacherDesc) {
		this.teacherDesc = teacherDesc;
	}

	public String getTeacherHeadLink() {
		return teacherHeadLink;
	}

	public void setTeacherHeadLink(String teacherHeadLink) {
		this.teacherHeadLink = teacherHeadLink;
	}
	
}
