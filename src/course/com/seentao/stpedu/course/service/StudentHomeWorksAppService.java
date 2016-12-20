package com.seentao.stpedu.course.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.service.BaseTeachStudentHomeworkService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class StudentHomeWorksAppService {

	@Autowired
	private BaseTeachStudentHomeworkService teachStudentHomeworkService;
	@Autowired
	private TeachRelCardCourseService relCardCourseService;
	
	
	
	/**
	 * 获取学生回答的文本作业信息
	 * @param userId/teacherId		用户id
	 * @param courseId				作业文本id
	 * @param start					分页查询页
	 * @param limit					分页显示数量
	 * @return
	 * @author 						lw
	 * @date						2016年6月20日  上午11:43:45
	 */
	public String getHomeworkSolution(int userId, int courseId, Integer start, Integer limit) {
		
		/*
		 * 1.校验课程存在性
		 */
		String courseRedis = RedisComponent.findRedisObject(courseId, TeachCourse.class);
		if(StringUtil.isEmpty(courseRedis)){
			LogUtil.error(this.getClass(), "getHomeworkSolution", AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_HOMEWORK_ROLE).toJSONString();
		}
		
		/*
		 * 2.获取用户信息
		 */
		String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
		if(StringUtil.isEmpty(userRedis)){
			LogUtil.error(this.getClass(), "getHomeworkSolution", AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_HOMEWORK_ROLE).toJSONString();
			
		}
		CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
		
	
		/* 3.1判断当前用户类型：学生 */
		if(GameConstants.USER_TYPE_STUDENT == user.getUserType()){
			
			return this.findAllStudentCourseHomeWork(courseId,userId,start ,limit);
		
		/* 3.2判断当前用户类型：教师 */
		}else if(GameConstants.USER_TYPE_TEAHCER == user.getUserType()){
			
			return this.findAllTeacherCourseAllStudentsHomeWork(courseId, start ,limit);
		}
		
		LogUtil.error(this.getClass(), "getHomeworkSolution", AppErrorCode.ERROR_HOMEWORK_ROLE);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_HOMEWORK_ROLE).toJSONString();
		
		
	}
	
	
	/**
	 * 获取学生的所有
	 * @param courseId
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午2:09:00
	 */
	private String findAllStudentCourseHomeWork(int courseId, int userId, Integer start, Integer limit) {
		
		/*
		 * 查询课程卡：
		 * 	1.通过课程卡课程关系表查询课程id
		 * 	2.获取课程卡
		 */
		//课程卡课程关系
		TeachRelCardCourse relCardCourse = new TeachRelCardCourse();
		relCardCourse.setCourseId(courseId);
		relCardCourse = relCardCourseService.selectTeachRelCardCourse(relCardCourse);
		if(relCardCourse == null){
			LogUtil.error(this.getClass(), "findAllStudentCourseHomeWork", AppErrorCode.ERROR_NOTFOUND_CLASS);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_NOTFOUND_CLASS).toJSONString();
		}
		//课程卡
		String cardRdis = RedisComponent.findRedisObject(relCardCourse.getCardId(), TeachCourseCard.class);
		if(StringUtil.isEmpty(cardRdis)){
			LogUtil.error(this.getClass(), "findAllStudentCourseHomeWork", AppErrorCode.ERROR_NOTFOUND_CLASS);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_NOTFOUND_CLASS).toJSONString();
		}
		TeachCourseCard card = JSONObject.parseObject(cardRdis, TeachCourseCard.class);
		
		/*
		 * 分页组件查询文本作业
		 * 	1.查询类型：
		 * 		1.1不再时间范围内返回全部学生提交的作业
		 * 		1.2若在时间范围内返回自己提交的作业
		 * 	2.查询参数
		 * 		1.未被删除
		 * 		2.课程id
		 */
		Map<String ,Object> selectParam = new HashMap<String, Object>();
		selectParam.put("isDelete", GameConstants.NO_DEL);
		selectParam.put("courseId", courseId);
		//当前时间
		int timeInt = TimeUtil.getCurrentTimestamp();
		
		if(card.getEndTime() < timeInt ||  timeInt < card.getStartTime()){
			
			/*
			 * 不再时间范围内返回全部学生提交的作业
			 * 通过课程id查询
			 */
			selectParam.put("courseId", courseId);
			return teachStudentHomeworkService.findAllStudentsHomeWorkExecute(selectParam,courseId,start,limit);
			
		}else{
			
			/*
			 * 若在时间范围内返回自己提交的作业
			 * 通过用户id查询
			 */
			selectParam.put("studentId", userId);
			return teachStudentHomeworkService.findAllStudentsHomeWorkExecute(selectParam,courseId,start,limit);
		}
			
	}



	/**
	 * 教师获取全部学生提交的作业文本
	 * @param homeworkId		查询作业文本id
	 * @param userId				用户id
	 * @param start					查询页
	 * @return
	 * @author 						lw
	 * @param param 
	 * @date						2016年6月20日  下午9:21:10
	 */
	public String findAllTeacherCourseAllStudentsHomeWork(int courseId, Integer start,Integer limit) {
			Map<String ,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("courseId", courseId);
			paramMap.put("isDelete", GameConstants.NO_DEL);
			//分页查询学生作业文本
			return teachStudentHomeworkService.findAllStudentsHomeWorkExecute(paramMap,courseId,start,limit);
	}


	

}
