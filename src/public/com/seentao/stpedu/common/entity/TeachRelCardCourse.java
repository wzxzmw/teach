package com.seentao.stpedu.common.entity;

import com.alibaba.fastjson.JSONArray;

public class TeachRelCardCourse {
	private Integer start;
	
	private Integer limit;
	
	private Integer relId;
	
	private Integer cardId;
	
	private Integer courseId;
	
	private Integer showType;
	
	private Integer createTime;
	
	private Integer planNum;
	
	private Integer actualNum;
	
	private Integer totalStudyNum;
	
	//作业标题
	private String homeworkTitle;
	
	//作业内容
	private String homeworkBody;
	
	//教师id
	private Integer	teacherId;
	
	//教师名称
	private String teacherName;
	
	//教师所在学校
	private String schoolName;
	
	//是否已交作业
	private Integer hasHandin;
	
	//班级类型
	private Integer classType;
	
	//课程说明
	private String courseDesc;
	
	//课程图片
	private String courseLink;
	
	//课程来源
	private Integer courseSource;
	
	//视频附件集合
	private JSONArray videoFiles;
	
	//音频附件
	private JSONArray audioFiles;
	
	//其他附件
	private JSONArray otherFiles;
	
	//章节
	private Integer chapterId;
	
	//所属章节标题
	private String chapterTitle;
	
	//学习状态
	private Integer learningStatus;
	
	//课程卡标题
	private String ccTitle;
	
	//课程卡类型
	private Integer ccType;
	
	//内容需替换的超链接信息集合
	private JSONArray links;
	
	//内容需替换的图片信息集合
	private JSONArray imgs;
	
	private String courseCardId;
	
	private Integer requestSide;//前后端标识
	
	
	
	public Integer getRequestSide() {
		return requestSide;
	}

	public void setRequestSide(Integer requestSide) {
		this.requestSide = requestSide;
	}

	//作业开始时间的时间戳
	private Integer homeworkStartDate;
	//作业结束时间的时间戳
	private Integer homeworkEndDate;
	
	//教师头像链接地址
	private String teacherHeadLink;
	
	

	

	public String getCourseCardId() {
		return courseCardId;
	}

	public void setCourseCardId(String courseCardId) {
		this.courseCardId = courseCardId;
	}

	public JSONArray getLinks() {
		return links;
	}

	public void setLinks(JSONArray links) {
		this.links = links;
	}

	public JSONArray getImgs() {
		return imgs;
	}

	public void setImgs(JSONArray imgs) {
		this.imgs = imgs;
	}

	public String getCcTitle() {
		return ccTitle;
	}

	public void setCcTitle(String ccTitle) {
		this.ccTitle = ccTitle;
	}

	public Integer getCcType() {
		return ccType;
	}

	public void setCcType(Integer ccType) {
		this.ccType = ccType;
	}

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public String getChapterTitle() {
		return chapterTitle;
	}

	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}

	public Integer getLearningStatus() {
		return learningStatus;
	}

	public void setLearningStatus(Integer learningStatus) {
		this.learningStatus = learningStatus;
	}

	public JSONArray getVideoFiles() {
		return videoFiles;
	}

	public void setVideoFiles(JSONArray videoFiles) {
		this.videoFiles = videoFiles;
	}

	public JSONArray getAudioFiles() {
		return audioFiles;
	}

	public void setAudioFiles(JSONArray audioFiles) {
		this.audioFiles = audioFiles;
	}

	public JSONArray getOtherFiles() {
		return otherFiles;
	}

	public void setOtherFiles(JSONArray otherFiles) {
		this.otherFiles = otherFiles;
	}

	public Integer getCourseSource() {
		return courseSource;
	}

	public void setCourseSource(Integer courseSource) {
		this.courseSource = courseSource;
	}

	public String getCourseLink() {
		return courseLink;
	}

	public void setCourseLink(String courseLink) {
		this.courseLink = courseLink;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
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

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
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
	public Integer getPlanNum() {
		return planNum;
	}

	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}
	public Integer getActualNum() {
		return actualNum;
	}

	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}
	public Integer getTotalStudyNum() {
		return totalStudyNum;
	}

	public void setTotalStudyNum(Integer totalStudyNum) {
		this.totalStudyNum = totalStudyNum;
	}

	public String getHomeworkTitle() {
		return homeworkTitle;
	}

	public void setHomeworkTitle(String homeworkTitle) {
		this.homeworkTitle = homeworkTitle;
	}

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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getHasHandin() {
		return hasHandin;
	}

	public void setHasHandin(Integer hasHandin) {
		this.hasHandin = hasHandin;
	}

	public Integer getHomeworkStartDate() {
		return homeworkStartDate;
	}

	public void setHomeworkStartDate(Integer homeworkStartDate) {
		this.homeworkStartDate = homeworkStartDate;
	}

	public Integer getHomeworkEndDate() {
		return homeworkEndDate;
	}

	public void setHomeworkEndDate(Integer homeworkEndDate) {
		this.homeworkEndDate = homeworkEndDate;
	}

	public String getHomeworkBody() {
		return homeworkBody;
	}

	public void setHomeworkBody(String homeworkBody) {
		this.homeworkBody = homeworkBody;
	}

	public String getTeacherHeadLink() {
		return teacherHeadLink;
	}

	public void setTeacherHeadLink(String teacherHeadLink) {
		this.teacherHeadLink = teacherHeadLink;
	}

}
