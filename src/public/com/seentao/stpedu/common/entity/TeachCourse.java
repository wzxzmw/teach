package com.seentao.stpedu.common.entity;

import java.util.List;

public class TeachCourse {
	private Integer courseCardType;//课程卡类型
	
	private Integer courseSource;//课程来源
	
	private Integer cardId;//课程卡Id
	
	private Integer courseShowType;//课程展示类型
	
	private Integer classType;//班级类型
	
	private Integer start;//截取开始
	
	private Integer limit;//截取数量
	
	private List<PublicAttachment> videoList;//视频附件集合
	
	private List<PublicAttachment> audiotList;//音频附件集合
	
	private List<PublicAttachment> otherList;//其他附件集合
	
	private String resCourseLink;//课程图片链接
	
	private String resTeacherHeadLink;//教师图片链接
	
	private String resTeacherName;//教师名称
	
	private String resSchoolName;//学校名称
	
	private Integer resLearningNum;//在线人数
	
	private Integer resChapterId;//章节di
	
	private String resChapterTitle;//章节标题
	
	private Integer resCourseCardId;//课程卡id
	
	private String resCcTitle;//课程卡标题
	
	private Integer resLearningStatus;//学习状态
	
	private Integer handinHomeworkNum;//已交作业人数
	
	private Integer hasHandin;//是否已交作业 	当前登录者是否已经交了该作业.1:已交；0:未交；
	
	private Integer homeworkStartDate;//该时间取作业所对应的课程卡的时间
	
	private Integer homeworkEndDate;//作业结束时间的时间戳
	
	private Integer studentId;//学生参数id
	
	private Integer courseId;
	
	private String courseTitle;
	
	private String courseExplain;
	
	private String courseContent;
	
	private Integer teacherId;
	
	private Integer videoAttaId;
	
	private Integer audioAttaId;
	
	private Integer matchId;
	
	private Integer courseType;
	
	private Integer createTime;
	
	private Integer isDelete;
	
	private Integer imageId;
	
	private Integer classId;
	
	
	

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Integer getCourseCardType() {
		return courseCardType;
	}

	public void setCourseCardType(Integer courseCardType) {
		this.courseCardType = courseCardType;
	}

	public Integer getCourseSource() {
		return courseSource;
	}

	public void setCourseSource(Integer courseSource) {
		this.courseSource = courseSource;
	}

	public Integer getCourseShowType() {
		return courseShowType;
	}

	public void setCourseShowType(Integer courseShowType) {
		this.courseShowType = courseShowType;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	public String getResTeacherHeadLink() {
		return resTeacherHeadLink;
	}

	public void setResTeacherHeadLink(String resTeacherHeadLink) {
		this.resTeacherHeadLink = resTeacherHeadLink;
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

	public Integer getResChapterId() {
		return resChapterId;
	}

	public void setResChapterId(Integer resChapterId) {
		this.resChapterId = resChapterId;
	}

	public String getResChapterTitle() {
		return resChapterTitle;
	}

	public void setResChapterTitle(String resChapterTitle) {
		this.resChapterTitle = resChapterTitle;
	}

	public Integer getResCourseCardId() {
		return resCourseCardId;
	}

	public void setResCourseCardId(Integer resCourseCardId) {
		this.resCourseCardId = resCourseCardId;
	}

	public String getResCcTitle() {
		return resCcTitle;
	}

	public void setResCcTitle(String resCcTitle) {
		this.resCcTitle = resCcTitle;
	}

	public Integer getResLearningStatus() {
		return resLearningStatus;
	}

	public void setResLearningStatus(Integer resLearningStatus) {
		this.resLearningStatus = resLearningStatus;
	}

	public Integer getHandinHomeworkNum() {
		return handinHomeworkNum;
	}

	public void setHandinHomeworkNum(Integer handinHomeworkNum) {
		this.handinHomeworkNum = handinHomeworkNum;
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

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
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

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
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

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	
}
