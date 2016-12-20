package com.seentao.stpedu.common.entity;
/** 
* @author yy
* @date 2016年6月22日 下午9:21:21 
*/
public class User {
	/**
	 * 参加的比赛数量(是俱乐部内部的比赛和外部比赛的数量和)
	 */
	private Integer joinGameCount;
	/**
	 * 加入的培训班数量
	 */
	private Integer joinTrainingClassCount;
	/**
	 * 参加的竞猜数量
	 */
	private Integer joinQuizCount;
	/**
	 * 关注的赛事数量
	 */
	private Integer attentionGameEvenCount;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 个人签名
	 */
	private String desc;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 学生证号
	 */
	private String studentCard;
	/**
	 * 头像id
	 */
	private String headLinkId;
	/**
	 * 头像链接
	 */
	private String headLink;
	/**
	 * 所在学校id
	 */
	private String schoolId;
	/**
	 * 所在学校名称
	 */
	private String schoolName;
	/**
	 * 班级id
	 */
	private String classId;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 是否有认证资格
	 */
	private Integer hasQualification;
	/**
	 * 申请证书状态
	 */
	private Integer certRequestStatus;
	/**
	 * 学院id
	 */
	private String collegeId;
	/**
	 * 学院名称
	 */
	private String collegeName;
	/**
	 * 个人评分
	 */
	private Float score;
	/**
	 * 教学成绩id
	 */
	private String teachingAchievementId;
	/**
	 * 已完成的卡片数量
	 */
	private String completedCourseCards;
	/**
	 * 所有卡片数量
	 */
	private Integer allCourseCards;
	/**
	 * 已学习卡片的时间总和(总秒数)
	 */
	private Integer learningSeconds;
	/**
	 * 已学习的章节数量
	 */
	private Integer completedChapters;
	/**
	 * 未开始的卡片数量
	 */
	private Integer notStartCourseCards;
	/**
	 * 未完成的卡片数量
	 */
	private Integer notCompletedCourseCards;
	/**
	 * 未设置的卡片数量
	 */
	private Integer notSetCourseCards;
	/**
	 * 全部的计划任务数量
	 */
	private Integer allTasks;
	/**
	 * 已签到的计划任务数量
	 */
	private Integer hasSigninTasks;
	/**
	 *查询时传递的memberId是否已关注此人
	 */
	private Integer hasBeenAttention;
	/**
	 * 是否相互关注
	 */
	private Integer attentionEachOther;
	/**
	 * 所在俱乐部id
	 */
	private String clubId;
	/**
	 *所在俱乐部名称
	 */
	private String clubName;
	/**
	 * 俱乐部logo链接地址
	 */
	private String clubLogoLink;
	/**
	 * 俱乐部模块角色
	 */
	private Integer clubRole;
	/**
	 * 该人员申请当前俱乐部的状态
	 */
	private Integer clubApplyStatus;
	/**
	 * 加入时填写的申请内容
	 */
	private String applicationContent;
	/**
	 *加入培训班时间的时间戳
	 */
	private String joinClassDate;
	/**
	 * 加入培训班时消费的一级货币
	 */
	private Integer fLevelAccountForJoinClass;
	/**
	 * 获取的人员是否为登录者
	 */
	private Integer isLoginUser;
	/**
	 * 点赞数量
	 */
	private Integer supportCount;
	/**
	 * 关注的数量
	 */
	private Integer attentionNum;
	/**
	 * 粉丝的数量
	 */
	private Integer fansNum;
	/**
	 * 参加实训次数
	 */
	private Integer practiceCount;
	/**
	 * 是否已加入俱乐部
	 */
	private Integer hasJoinClub;
	/**
	 * 俱乐部人数
	 */
	private Integer clubMember;
	/**
	 * 关注的俱乐部数量
	 */
	private Integer attentionClubCount;
	/**
	 * 关注的企业数量
	 */
	private Integer attentionCompanyCount;
	/**
	 * 教学模块角色
	 */
	private Integer teachingRole;
	/**
	 * 签到时间的时间戳
	 */
	private String signinDate;
	/**
	 * 加入俱乐部状态(新增属性)
	 */
	private Integer clubStatus;
	
	/**
	 * 已上课程数
	 */
	private Integer completedCourses;
	
	/**
	 * 所有课程数
	 */
	private Integer allCourses;
	
	public Integer getClubStatus() {
		return clubStatus;
	}
	public void setClubStatus(Integer clubStatus) {
		this.clubStatus = clubStatus;
	}
	public Integer getJoinGameCount() {
		return joinGameCount;
	}
	public void setJoinGameCount(Integer joinGameCount) {
		this.joinGameCount = joinGameCount;
	}
	public Integer getJoinTrainingClassCount() {
		return joinTrainingClassCount;
	}
	public void setJoinTrainingClassCount(Integer joinTrainingClassCount) {
		this.joinTrainingClassCount = joinTrainingClassCount;
	}
	public Integer getJoinQuizCount() {
		return joinQuizCount;
	}
	public void setJoinQuizCount(Integer joinQuizCount) {
		this.joinQuizCount = joinQuizCount;
	}
	public Integer getAttentionGameEvenCount() {
		return attentionGameEvenCount;
	}
	public void setAttentionGameEvenCount(Integer attentionGameEvenCount) {
		this.attentionGameEvenCount = attentionGameEvenCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getStudentCard() {
		return studentCard;
	}
	public void setStudentCard(String studentCard) {
		this.studentCard = studentCard;
	}
	public String getHeadLinkId() {
		return headLinkId;
	}
	public void setHeadLinkId(String headLinkId) {
		this.headLinkId = headLinkId;
	}
	public String getHeadLink() {
		return headLink;
	}
	public void setHeadLink(String headLink) {
		this.headLink = headLink;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getHasQualification() {
		return hasQualification;
	}
	public void setHasQualification(Integer hasQualification) {
		this.hasQualification = hasQualification;
	}
	public Integer getCertRequestStatus() {
		return certRequestStatus;
	}
	public void setCertRequestStatus(Integer certRequestStatus) {
		this.certRequestStatus = certRequestStatus;
	}
	public String getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public String getTeachingAchievementId() {
		return teachingAchievementId;
	}
	public void setTeachingAchievementId(String teachingAchievementId) {
		this.teachingAchievementId = teachingAchievementId;
	}
	public String getCompletedCourseCards() {
		return completedCourseCards;
	}
	public void setCompletedCourseCards(String completedCourseCards) {
		this.completedCourseCards = completedCourseCards;
	}
	public Integer getAllCourseCards() {
		return allCourseCards;
	}
	public void setAllCourseCards(Integer allCourseCards) {
		this.allCourseCards = allCourseCards;
	}
	public Integer getLearningSeconds() {
		return learningSeconds;
	}
	public void setLearningSeconds(Integer learningSeconds) {
		this.learningSeconds = learningSeconds;
	}
	public Integer getCompletedChapters() {
		return completedChapters;
	}
	public void setCompletedChapters(Integer completedChapters) {
		this.completedChapters = completedChapters;
	}
	public Integer getNotStartCourseCards() {
		return notStartCourseCards;
	}
	public void setNotStartCourseCards(Integer notStartCourseCards) {
		this.notStartCourseCards = notStartCourseCards;
	}
	public Integer getNotCompletedCourseCards() {
		return notCompletedCourseCards;
	}
	public void setNotCompletedCourseCards(Integer notCompletedCourseCards) {
		this.notCompletedCourseCards = notCompletedCourseCards;
	}
	public Integer getNotSetCourseCards() {
		return notSetCourseCards;
	}
	public void setNotSetCourseCards(Integer notSetCourseCards) {
		this.notSetCourseCards = notSetCourseCards;
	}
	public Integer getAllTasks() {
		return allTasks;
	}
	public void setAllTasks(Integer allTasks) {
		this.allTasks = allTasks;
	}
	public Integer getHasSigninTasks() {
		return hasSigninTasks;
	}
	public void setHasSigninTasks(Integer hasSigninTasks) {
		this.hasSigninTasks = hasSigninTasks;
	}
	public Integer getHasBeenAttention() {
		return hasBeenAttention;
	}
	public void setHasBeenAttention(Integer hasBeenAttention) {
		this.hasBeenAttention = hasBeenAttention;
	}
	public Integer getAttentionEachOther() {
		return attentionEachOther;
	}
	public void setAttentionEachOther(Integer attentionEachOther) {
		this.attentionEachOther = attentionEachOther;
	}
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getClubLogoLink() {
		return clubLogoLink;
	}
	public void setClubLogoLink(String clubLogoLink) {
		this.clubLogoLink = clubLogoLink;
	}
	public Integer getClubRole() {
		return clubRole;
	}
	public void setClubRole(Integer clubRole) {
		this.clubRole = clubRole;
	}
	public Integer getClubApplyStatus() {
		return clubApplyStatus;
	}
	public void setClubApplyStatus(Integer clubApplyStatus) {
		this.clubApplyStatus = clubApplyStatus;
	}
	public String getApplicationContent() {
		return applicationContent;
	}
	public void setApplicationContent(String applicationContent) {
		this.applicationContent = applicationContent;
	}
	public String getJoinClassDate() {
		return joinClassDate;
	}
	public void setJoinClassDate(String joinClassDate) {
		this.joinClassDate = joinClassDate;
	}
	public Integer getfLevelAccountForJoinClass() {
		return fLevelAccountForJoinClass;
	}
	public void setfLevelAccountForJoinClass(Integer fLevelAccountForJoinClass) {
		this.fLevelAccountForJoinClass = fLevelAccountForJoinClass;
	}
	public Integer getIsLoginUser() {
		return isLoginUser;
	}
	public void setIsLoginUser(Integer isLoginUser) {
		this.isLoginUser = isLoginUser;
	}
	public Integer getSupportCount() {
		return supportCount;
	}
	public void setSupportCount(Integer supportCount) {
		this.supportCount = supportCount;
	}
	public Integer getAttentionNum() {
		return attentionNum;
	}
	public void setAttentionNum(Integer attentionNum) {
		this.attentionNum = attentionNum;
	}
	public Integer getFansNum() {
		return fansNum;
	}
	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}
	public Integer getPracticeCount() {
		return practiceCount;
	}
	public void setPracticeCount(Integer practiceCount) {
		this.practiceCount = practiceCount;
	}
	public Integer getHasJoinClub() {
		return hasJoinClub;
	}
	public void setHasJoinClub(Integer hasJoinClub) {
		this.hasJoinClub = hasJoinClub;
	}
	public Integer getClubMember() {
		return clubMember;
	}
	public void setClubMember(Integer clubMember) {
		this.clubMember = clubMember;
	}
	public Integer getAttentionClubCount() {
		return attentionClubCount;
	}
	public void setAttentionClubCount(Integer attentionClubCount) {
		this.attentionClubCount = attentionClubCount;
	}
	public Integer getAttentionCompanyCount() {
		return attentionCompanyCount;
	}
	public void setAttentionCompanyCount(Integer attentionCompanyCount) {
		this.attentionCompanyCount = attentionCompanyCount;
	}
	public Integer getTeachingRole() {
		return teachingRole;
	}
	public void setTeachingRole(Integer teachingRole) {
		this.teachingRole = teachingRole;
	}
	public String getSigninDate() {
		return signinDate;
	}
	public void setSigninDate(String signinDate) {
		this.signinDate = signinDate;
	}
	public Integer getCompletedCourses() {
		return completedCourses;
	}
	public void setCompletedCourses(Integer completedCourses) {
		this.completedCourses = completedCourses;
	}
	public Integer getAllCourses() {
		return allCourses;
	}
	public void setAllCourses(Integer allCourses) {
		this.allCourses = allCourses;
	}
	
}


