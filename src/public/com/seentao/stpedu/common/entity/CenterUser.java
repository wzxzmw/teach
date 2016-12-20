package com.seentao.stpedu.common.entity;

/**
 * 用户表
 */
/**
 * @author wangzx
 *
 * 2016年10月13日
 */
public class CenterUser {
	
	private String address;//现住址
	
	private Integer score;//分数
	
	private String userIds;
	
	private String isClassNull;//数据库判断使用 班级id是空的
	
	private String isSchlooNull;//数据库判断使用 学校id不是空的
	
	private Integer isIdentify;//是否认证
	
	private Integer isApplyCertificate;//证书状态 
	
	private Integer start;
	
	private Integer limit;
	
	private Integer teacherId;//教师ID
	
	private String teacherName;//教师名称
	
	private String teacherSchool;//教师所在学校
	
	private String teacherDesc;//教师介绍
	
	private String teacherHeadLink;//教师头像链接地址
	
	private Integer userId;
	
	private String userName;
	
	private String password;
	
	private String salt;
	
	private String nickName;
	
	private String realName;
	
	private Integer headImgId;
	
	private String phone;
	
	private Integer sex;
	
	private Integer schoolId;
	
	private Integer instId;
	
	private String qq;
	
	private String email;
	
	private String weixin;
	
	private String studentId;
	
	private String idCard;
	
	private String description;
	
	private Integer profession;
	
	private Integer birthday;
	
	private String studentCardNo;
	
	private String teacherCardNo;
	
	private String positionalTitle;
	
	private String speciality;
	
	private String grade;
	
	private Integer educationLevel;
	
	private Integer userType;
	
	private Integer userSourceType;
	
	private String sourceId;
	
	private Integer classId;
	
	private Integer clubId;
	
	private Integer questionNum;
	
	private Integer answerNum;
	
	private Integer clubActiveNum;
	
	private Integer praiseNum;
	
	private Integer regTime;
	
	private Integer lastLoginTime;
	
	private Integer logins;
	
	private String replacePhone;
	
	private Integer trainNum;
	
	private Integer atteClubNum;
	
	private Integer atteCompanyNum;
	
	private Integer todayClubActiveNum;
	
	private Integer guessId;
	
	private Integer userNameUpdateTimes;//用户名修改次数
	
	
	public Integer getUserNameUpdateTimes() {
		return userNameUpdateTimes;
	}

	public void setUserNameUpdateTimes(Integer userNameUpdateTimes) {
		this.userNameUpdateTimes = userNameUpdateTimes;
	}
	public CenterUser(){
		
	}
	
	public CenterUser(Integer userId){
		if(userId != null){
			this.userId=userId;
		}
	}
	public CenterUser(Integer logins,Integer lastLoginTime,Integer userId){
		this.logins = logins;
		this.lastLoginTime = lastLoginTime;
		this.userId = userId;
	}
	public CenterUser( String userName,Integer userId,Integer headImgId,String nickName,Integer sex,String phone,String realName,Integer schoolId,Integer instId,String idCard,String description,Integer birthday, Integer profession,String studentId, String studentCardNo,String teacherCardNo,String positionalTitle,String speciality,String grade,Integer educationLevel,String address,Integer userNameUpdateTimes){
		this.userName=userName;
		this.userId=userId;
		this.headImgId=headImgId;
		this.nickName=nickName;
		this.sex=sex;
		this.phone=phone;
		this.realName=realName;
		this.schoolId=schoolId;
		this.instId=instId;
		this.idCard=idCard;
		this.description=description;
		this.birthday=birthday;
		this.profession=profession;
		this.studentId=studentId;
		this.studentCardNo=studentCardNo;
		this.teacherCardNo=teacherCardNo;
		this.positionalTitle=positionalTitle;
		this.speciality=speciality;
		this.grade=grade;
		this.educationLevel=educationLevel;
		this.address=address;
		if(userNameUpdateTimes != null){
			this.userNameUpdateTimes=userNameUpdateTimes;
		}
	}
	/**
	 * @param salt 附加码
	 * @param password 密码
	 * @param phone 电话
	 * @param nickName 昵称
	 * @param userSourceType 用户来源类型。1:注册用户，2:微信用户，3:QQ用户，4:微博用户，5:导入用户
	 * @param logins 登录次数
	 * @param sex  、性别
	 * @param questionNum 提问数量
	 * @param answerNum 回答数量
	 * @param clubActiveNum 俱乐部活跃度
	 * @param praiseNum 赞数量
	 * @param regTime 注册时间
	 * @param trainNum 实训次数
	 * @param atteClubNum  关注俱乐部的数量
	 * @param atteCompanyNum 关注企业的数量
	 * @param todayClubActiveNum 当天俱乐部的活跃度
	 * @param birthday 生日 默认选择19970101
	 * @param profession 职业
	 * @param educationLevel  教育程度。1:高中及以下，2:中职技校，3:大专，4:本科，5:硕士研究生，6:博士及以上，7:其它。
	 * @param userType 用户类型。1:教师，2:学生，3:普通用户。
	 */
	public CenterUser(String salt,String password,String phone,String nickName,
					Integer userSourceType,Integer logins,Integer sex,Integer questionNum,
					Integer answerNum,Integer clubActiveNum,Integer praiseNum,Integer regTime,
					Integer trainNum,Integer atteClubNum,Integer atteCompanyNum,Integer todayClubActiveNum,
					Integer birthday,Integer profession,Integer educationLevel,Integer userType,String userName){
		this.userType = userType;
		this.todayClubActiveNum = todayClubActiveNum;
		this.birthday = birthday;
		this.profession = profession;
		this.educationLevel = educationLevel;
		this.atteCompanyNum = atteCompanyNum;
		this.atteClubNum = atteClubNum;
		this.trainNum = trainNum;
		this.regTime = regTime;
		this.praiseNum = praiseNum;
		this.clubActiveNum = clubActiveNum;
		this.answerNum = answerNum;
		this.salt=salt;
		this.password=password;
		this.phone=phone;
		this.nickName = nickName;
		this.userSourceType =userSourceType;
		this.logins=logins;
		this.sex = sex;
		this.questionNum = questionNum;
		this.userName = userName;
	}
	public CenterUser(String salt,String password,Integer userId){
		this.salt = salt;
		this.password = password;
		this.userId = userId;
	}
	/*public CenterUser(String phone,String salt,String password){
		this.phone = phone;
		this.salt= salt;
		this.password = password;
	}*/
	public Integer getGuessId() {
		return guessId;
	}

	public void setGuessId(Integer guessId) {
		this.guessId = guessId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getIsClassNull() {
		return isClassNull;
	}

	public void setIsClassNull(String isClassNull) {
		this.isClassNull = isClassNull;
	}

	public String getIsSchlooNull() {
		return isSchlooNull;
	}

	public void setIsSchlooNull(String isSchlooNull) {
		this.isSchlooNull = isSchlooNull;
	}

	public Integer getIsIdentify() {
		return isIdentify;
	}

	public void setIsIdentify(Integer isIdentify) {
		this.isIdentify = isIdentify;
	}

	public Integer getIsApplyCertificate() {
		return isApplyCertificate;
	}

	public void setIsApplyCertificate(Integer isApplyCertificate) {
		this.isApplyCertificate = isApplyCertificate;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getHeadImgId() {
		return headImgId;
	}

	public void setHeadImgId(Integer headImgId) {
		this.headImgId = headImgId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProfession() {
		return profession;
	}

	public void setProfession(Integer profession) {
		this.profession = profession;
	}

	public Integer getBirthday() {
		return birthday;
	}

	public void setBirthday(Integer birthday) {
		this.birthday = birthday;
	}

	public String getStudentCardNo() {
		return studentCardNo;
	}

	public void setStudentCardNo(String studentCardNo) {
		this.studentCardNo = studentCardNo;
	}

	public String getTeacherCardNo() {
		return teacherCardNo;
	}

	public void setTeacherCardNo(String teacherCardNo) {
		this.teacherCardNo = teacherCardNo;
	}

	public String getPositionalTitle() {
		return positionalTitle;
	}

	public void setPositionalTitle(String positionalTitle) {
		this.positionalTitle = positionalTitle;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(Integer educationLevel) {
		this.educationLevel = educationLevel;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserSourceType() {
		return userSourceType;
	}

	public void setUserSourceType(Integer userSourceType) {
		this.userSourceType = userSourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}

	public Integer getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public Integer getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(Integer answerNum) {
		this.answerNum = answerNum;
	}

	public Integer getClubActiveNum() {
		return clubActiveNum;
	}

	public void setClubActiveNum(Integer clubActiveNum) {
		this.clubActiveNum = clubActiveNum;
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	public Integer getRegTime() {
		return regTime;
	}

	public void setRegTime(Integer regTime) {
		this.regTime = regTime;
	}

	public Integer getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Integer lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLogins() {
		return logins;
	}

	public void setLogins(Integer logins) {
		this.logins = logins;
	}

	public String getReplacePhone() {
		return replacePhone;
	}

	public void setReplacePhone(String replacePhone) {
		this.replacePhone = replacePhone;
	}

	public Integer getTrainNum() {
		return trainNum;
	}

	public void setTrainNum(Integer trainNum) {
		this.trainNum = trainNum;
	}

	public Integer getAtteClubNum() {
		return atteClubNum;
	}

	public void setAtteClubNum(Integer atteClubNum) {
		this.atteClubNum = atteClubNum;
	}

	public Integer getAtteCompanyNum() {
		return atteCompanyNum;
	}

	public void setAtteCompanyNum(Integer atteCompanyNum) {
		this.atteCompanyNum = atteCompanyNum;
	}

	public Integer getTodayClubActiveNum() {
		return todayClubActiveNum;
	}

	public void setTodayClubActiveNum(Integer todayClubActiveNum) {
		this.todayClubActiveNum = todayClubActiveNum;
	}
	

}
