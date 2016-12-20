package com.seentao.stpedu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.course.service.BusinessCourseChapterService;
import com.seentao.stpedu.course.service.BusinessCourseMediaService;
import com.seentao.stpedu.course.service.BusinessCourseService;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

/** 
* @author yy
* @date 2016年6月17日 下午4:35:39 
*/
@Controller
public class TeachCourseController implements ITeachCourseController {
	
	@Autowired
	private BusinessCourseChapterService businessCourseChapterService;
	
	@Autowired
	private BusinessCourseService businessCourseService;
	
	@Autowired
	private BusinessCourseMediaService BusinessCourseMediaService;
	
	/**
	 * 预制课程
	 * @param playTime 附件表播放时长
	 * @param ossVideoId 上传OSS后返回的id 
	 * @param fileName 附件表文件名
	 * @param chapterId 第几章
	 * @param courseCardId 第几个课程卡
	 * @param courseTitle 课程标题
	 * @return
	 */
	@RequestMapping("/createCourse")
	@ResponseBody
	public String createCourse(Integer playTime,String ossVideoId,String fileName, Integer chapterId, Integer courseCardId, String courseTitle){
		//TODO 校验参数
		return businessCourseService.createCourse( playTime, ossVideoId, fileName,  chapterId,  courseCardId,  courseTitle);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#getChapters(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@RequestMapping("/getChapters")
	@ResponseBody
	public String getChapters(Integer userType,String userName,String userId,String userToken,Integer classId){
		return businessCourseChapterService.getChapterByClassId(classId, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#getCourseCards(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getCourseCards")
	public String getCourseCards(Integer userType,String userName,String userId,String userToken,Integer classId
			,Integer chapterId,Integer courseCardId,Integer inquireType){
		return businessCourseService.getCourseCards(classId,chapterId,courseCardId,inquireType);
	}
	
	
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
	@Override
	@ResponseBody
	@RequestMapping(value = "/getCourseForMobile")
	public String getCourseForMobile(Integer userId, Integer start, Integer limit, Integer classId, Integer classType, Integer chapterId, Integer ccType, Integer courseId, Integer requestSide, Integer inquireType){
		if(classType == null || inquireType == null){
			LogUtil.error(this.getClass(), "getCourseForMobile" , String.valueOf(AppErrorCode.ERROR_COURSE_REQUEST_PARAM));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_REQUEST_PARAM).toJSONString();
			
		}
		return businessCourseService.getCourseForMobile(userId, start, limit, classId, classType, chapterId, ccType, courseId, requestSide, inquireType, null);
	}
	
	public String getCourseForMobile3(Integer userId, Integer start, Integer limit, Integer classId, Integer classType, Integer chapterId, Integer ccType, Integer courseId, Integer requestSide, Integer inquireType, Integer courseCardId){
		if(classType == null || inquireType == null || inquireType != 3){
			LogUtil.error(this.getClass(), "getCourseForMobile" , String.valueOf(AppErrorCode.ERROR_COURSE_REQUEST_PARAM));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_REQUEST_PARAM).toJSONString();
			
		}
		return businessCourseService.getCourseForMobile(userId, start, limit, classId, classType, chapterId, ccType, courseId, requestSide, inquireType, courseCardId);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#submitCourseCard(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitCourseCard")
	public String submitCourseCard(Integer userType,String userName,Integer userId,String userToken,
			Integer courseCardId,String ccTitle,String ccDesc,
			Integer ccLinkId,Integer ccStartDate,Integer ccEndDate
			){
		return businessCourseService.updateCourseCard(userId,courseCardId,ccTitle,ccDesc,ccLinkId,ccStartDate,ccEndDate);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#getCourse(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/getCourse")
	public String getCourse(Integer userType,String userName,String userId,String userToken,
			String classId,String courseCardId,String courseId,
			Integer start,Integer limit,Integer requestSide,Integer platformModule,Integer inquireType,Integer classType
			){
		return businessCourseService.getCourse(userId,start,limit,classId,courseCardId,courseId,requestSide,platformModule,inquireType,classType);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#submitCourse(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitCourse")
	public String submitCourse(Integer userType,String userName,Integer userId,String userToken,
			String courseId,String courseTitle,String courseDesc,
			Integer courseShowType,String courseBody,Integer courseCardId,String audioFileIds,String otherFileIds
			,Integer classType,String classId){
		return businessCourseService.addOrUpdateCourse(userId,courseId,courseTitle,courseDesc,courseShowType,
				courseBody,courseCardId,audioFileIds,otherFileIds,classType,classId
				);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#submitCourseShowType(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitCourseShowType")
	public String submitCourseShowType(Integer userType,String userName,Integer userId,String userToken,
			Integer courseId,Integer courseShowType,Integer classType,String classId, String courseCardId){
		return businessCourseService.updateCourse(userId,courseId,courseShowType,classType,classId,courseCardId);
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#submitCourseLearning(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitCourseLearning")
	public String submitCourseLearning(Integer userType,String userName,String userId,String userToken,
			String courseId,String attachmentId){
		//TODO
		return "";
	}
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.course.controller.ITeachCourseController#getChapters(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@RequestMapping("/getChaptersForMobile")
	@ResponseBody
	public String getChaptersForMobile(Integer userType,String userName,String userId,String userToken,Integer classId){
		return businessCourseChapterService.getChapterByClassId(classId, 2);
	}
	
	@Override
	public String submitVideoViewTime(String userName, String userId, String userType, String userToken, String currentTimes, String courseId, String courseCardId, Integer classType, String attachmentId){
		return BusinessCourseMediaService.submitVideoViewTime(userId, currentTimes, courseId, courseCardId, classType, attachmentId).toJSONString();
	}
	
	@Override
	public String submitVideoViewTimeForMobile(String userName, String userId, String userType, String userToken, String currentTimes, String courseId, String courseCardId, Integer classType, String attachmentId){
		return BusinessCourseMediaService.submitVideoViewTime(userId, currentTimes, courseId, courseCardId, classType, attachmentId).toJSONString();
	}
	
}


