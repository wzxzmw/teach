package com.seentao.stpedu.common.entity;

import java.util.List;

public class ClubTrainingCourse {
	private Integer courseShowType;//课程显示类型
	
	private Integer courseSource;//课程来源
	
	private Integer classType;//班级类型
	
	private Integer start;
	
	private Integer limit;
	
	private List<PublicAttachment> videoList;//视频附件集合
	
	private List<PublicAttachment> audiotList;//音频附件集合
	
	private List<PublicAttachment> otherList;//其他附件集合
	
	private String resCourseLink;//课程图片链接
	
	private String resTeacherHeadLink;//教练图片链接
	
	private String resTeacherName;//教练名称
	
	private String resSchoolName;//学校名称
	
	private Integer resLearningNum;//在线人数
	
	private Integer resLearningStatus;//学习状态
	
	private Integer courseId;
	
	private Integer courseType;
	
	private String courseTitle;
	
	private String courseExplain;
	
	private String courseContent;
	
	private Integer imageId;
	
	private Integer videoAttaId;
	
	private Integer audioAttaId;
	
	private Integer createTime;
	
	private Integer createUserId;
	
	private Integer isDelete;
	
	private Integer totalStudyNum;

	public Integer getCourseShowType() {
		return courseShowType;
	}

	public void setCourseShowType(Integer courseShowType) {
		this.courseShowType = courseShowType;
	}

	public Integer getCourseSource() {
		return courseSource;
	}

	public void setCourseSource(Integer courseSource) {
		this.courseSource = courseSource;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
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

	public List<PublicAttachment> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<PublicAttachment> videoList) {
		this.videoList = videoList;
	}

	public List<PublicAttachment> getAudiotList() {
		return audiotList;
	}

	public void setAudiotList(List<PublicAttachment> audiotList) {
		this.audiotList = audiotList;
	}

	public List<PublicAttachment> getOtherList() {
		return otherList;
	}

	public void setOtherList(List<PublicAttachment> otherList) {
		this.otherList = otherList;
	}

	public String getResCourseLink() {
		return resCourseLink;
	}

	public void setResCourseLink(String resCourseLink) {
		this.resCourseLink = resCourseLink;
	}

	public String getResTeacherHeadLink() {
		return resTeacherHeadLink;
	}

	public void setResTeacherHeadLink(String resTeacherHeadLink) {
		this.resTeacherHeadLink = resTeacherHeadLink;
	}

	public String getResTeacherName() {
		return resTeacherName;
	}

	public void setResTeacherName(String resTeacherName) {
		this.resTeacherName = resTeacherName;
	}

	public String getResSchoolName() {
		return resSchoolName;
	}

	public void setResSchoolName(String resSchoolName) {
		this.resSchoolName = resSchoolName;
	}

	public Integer getResLearningNum() {
		return resLearningNum;
	}

	public void setResLearningNum(Integer resLearningNum) {
		this.resLearningNum = resLearningNum;
	}

	public Integer getResLearningStatus() {
		return resLearningStatus;
	}

	public void setResLearningStatus(Integer resLearningStatus) {
		this.resLearningStatus = resLearningStatus;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseExplain() {
		return courseExplain;
	}

	public void setCourseExplain(String courseExplain) {
		this.courseExplain = courseExplain;
	}

	public String getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Integer getVideoAttaId() {
		return videoAttaId;
	}

	public void setVideoAttaId(Integer videoAttaId) {
		this.videoAttaId = videoAttaId;
	}

	public Integer getAudioAttaId() {
		return audioAttaId;
	}

	public void setAudioAttaId(Integer audioAttaId) {
		this.audioAttaId = audioAttaId;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getTotalStudyNum() {
		return totalStudyNum;
	}

	public void setTotalStudyNum(Integer totalStudyNum) {
		this.totalStudyNum = totalStudyNum;
	}	
}
