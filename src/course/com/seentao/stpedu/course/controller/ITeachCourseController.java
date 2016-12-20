package com.seentao.stpedu.course.controller;

/** 
* @author yy
* @date 2016年7月5日 下午9:52:08 
*/
public interface ITeachCourseController {

	/**
	 * 接口id--12
	 * 获取课程章节信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param classId	班级id
	 */
	String getChapters(Integer userType, String userName, String userId, String userToken, Integer classId);
	
	/**
	 * 获取课程卡信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param classId	班级id
	 * @param chapterId	章节id
	 * @param courseCardId 课程卡id
	 * @param inquireType 查询类型
	 */
	String getCourseCards(Integer userType, String userName, String userId, String userToken, Integer classId,
			Integer chapterId, Integer courseCardId, Integer inquireType);

	/**
	 * 提交自定义课程卡
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param courseCardId	课程卡id
	 * @param ccTitle	课程卡标题
	 * @param ccDesc 课程卡说明
	 * @param ccLinkId	课程卡图片id
	 * @param ccStartDate 课程卡开始时间的时间戳
	 * @param ccEndDate	课程卡结束时间的时间戳
	 */
	String submitCourseCard(Integer userType, String userName, Integer userId, String userToken, Integer courseCardId,
			String ccTitle, String ccDesc, Integer ccLinkId, Integer ccStartDate, Integer ccEndDate);

	/**
	 * 获取课程信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param classId	班级id
	 * @param courseCardId	课程卡id
	 * @param courseId 课程id
	 * @param start	分页的开始页
	 * @param limit 每页数量
	 * @param requestSide	请求端
	 * @param platformModule 平台模块(预留字段)
	 * @param inquireType	查询类型
	 * @param classType	班级类型
	 */
	String getCourse(Integer userType, String userName, String userId, String userToken, String classId,
			String courseCardId, String courseId, Integer start, Integer limit, Integer requestSide,
			Integer platformModule, Integer inquireType, Integer classType);

	/**
	 * 提交自定义课程信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param courseId 课程id
	 * @param courseTitle	课程标题
	 * @param courseDesc	课程说明
	 * @param courseShowType	课程显示类型
	 * @param courseBody 课程内容
	 * @param courseCardId	所属课程卡id
	 * @param audioFileIds 音频附件id集合
	 * @param otherFileIds	其它附件id集合
	 * @param classType	班级类型
	 */
	String submitCourse(Integer userType, String userName, Integer userId, String userToken, String courseId,
			String courseTitle, String courseDesc, Integer courseShowType, String courseBody, Integer courseCardId,
			String audioFileIds, String otherFileIds, Integer classType,String classId);

	/**
	 * 设置课程显示类型
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param courseId 课程id
	 * @param courseShowType	课程显示类型(1:必修；2:选修；3:隐藏)
	 * @param classType 班级类型
	 */
	String submitCourseShowType(Integer userType, String userName, Integer userId, String userToken, Integer courseId,
			Integer courseShowType, Integer classType,String classId, String courseCardId);

	/**
	 * 记录课程学习操作(比如视频点击播放时调用该接口，则记入学生学习该课时的时长)
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param courseId 课程id
	 * @param attachmentId	附件id
	 */
	String submitCourseLearning(Integer userType, String userName, String userId, String userToken, String courseId,
			String attachmentId);

	/**
	 * 接口id--12
	 * 获取课程章节信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param classId	班级id
	 */
	String getChaptersForMobile(Integer userType,String userName,String userId,String userToken,Integer classId);
	
	
	/**
	 * 手机端：获取课程信息
	 * @param userId		用户id
	 * @param start			分页位便宜
	 * @param limit			分页查询数目
	 * @param classId		班级id
	 * @param classType		班级类型	1:教学班；2:俱乐部培训班；
	 * @param chapterId		章节id
	 * @param ccType		课程卡类型	2:知识点卡片；3:案例视频卡片；
	 * @param courseId		课程id
	 * @param requestSide	请求类型 	1:管理后端；2:前端；
	 * @param inquireType	功能类型
	 * @return
	 * @author 			lw
	 * @date			2016年7月26日  下午6:23:59
	 */
	public String getCourseForMobile(Integer userId, Integer start, Integer limit, Integer classId, Integer classType, Integer chapterId, Integer ccType, Integer courseId, Integer requestSide, Integer inquireType);
	
	
	public String getCourseForMobile3(Integer userId, Integer start, Integer limit, Integer classId, Integer classType, Integer chapterId, Integer ccType, Integer courseId, Integer requestSide, Integer inquireType, Integer courseCardId);
	/**
	 * 提交视频播放时间
	 * @param userName 用户名
	 * @param userId 用户id
	 * @param userType 用户类型
	 * @param userToken 用户唯一标识
	 * @param currentTimes 播放时间(单位：秒)
	 * @param courseId 课程id
	 * @param courseCardId 所属课程卡id
	 * @param classType 课程所属班级的类型
	 * @param attachmentId 附件id
	 * @return
	 * @author chengshx
	 */
	public String submitVideoViewTime(String userName, String userId, String userType, String userToken, String currentTimes, String courseId, String courseCardId, Integer classType, String attachmentId);
	
	/**
	 * 移动端：提交视频播放时间
	 * @param userName 用户名
	 * @param userId 用户id
	 * @param userType 用户类型
	 * @param userToken 用户唯一标识
	 * @param currentTimes 播放时间(单位：秒)
	 * @param courseId 课程id
	 * @param courseCardId 所属课程卡id
	 * @param classType 课程所属班级的类型
	 * @param attachmentId 附件id
	 * @return
	 * @author chengshx
	 */
	public String submitVideoViewTimeForMobile(String userName, String userId, String userType, String userToken, String currentTimes, String courseId, String courseCardId, Integer classType, String attachmentId);
}


