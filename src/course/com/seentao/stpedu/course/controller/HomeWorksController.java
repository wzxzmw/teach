package com.seentao.stpedu.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.course.service.PublishhomeworkAppService;
import com.seentao.stpedu.course.service.PublishsubmitScoreAppService;
import com.seentao.stpedu.course.service.StudentHomeWorksAppService;
import com.seentao.stpedu.course.service.TeachStudentHomeworkAppService;
import com.seentao.stpedu.course.service.TeacherHomeWorksAppService;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Controller
public class HomeWorksController implements IHomeWorksController {

	
	@Autowired
	private TeacherHomeWorksAppService appService;
	@Autowired
	private StudentHomeWorksAppService studentHomeWorksService;
	@Autowired
	private PublishhomeworkAppService  publishhomeworkAppService;
	@Autowired
	private TeachStudentHomeworkAppService teachStudentHomeworkAppService;
	@Autowired
	public PublishsubmitScoreAppService publishsubmitScoreAppService;
	
	/**
	 * 获取教师所发文本作业信息
	 * 	教师发布的作业文本其实就是文本课程
	 * 	
	 * @param userName			必须传字段		用户名
	 * @param userId			必须传字段		用户id(学生)
	 * @param userType			必须传字段		用户类型
	 * @param userToken			必须传字段		用户唯一标识
	 * @param start							分页的开始页	从0开始
	 * @param limit							每页数量
	 * @param courseCardId					课程卡id
	 * @param homeworkId					文本作业id
	 * @param inquireType		必须传字段		查询类型
	 * @return
	 * @author  lw			
	 * @date    2016年6月18日 下午8:28:35
	 */
	@Override
	@ResponseBody
	public String getHomeworks(Integer userId, Integer start, Integer limit, Integer courseCardId, Integer homeworkId , Integer inquireType, Integer requestSide){
		
		/* 传递参数校验:用户id 和 查询类型  */
		if(userId == null || inquireType == null){
			LogUtil.error(this.getClass(), "getHomeworks", AppErrorCode.ERROR_COURSE_PPARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_PPARAM).toJSONString();
		}
		
		return appService.getHomeworks(userId,start,limit,courseCardId,homeworkId,inquireType, requestSide);
	}
	
	
	
	/**
	 * 获取学生回答的文本作业信息
	 * @param userId		用户id
	 * @param homeworkId	作业文本id
	 * @param start			分页查询页
	 * @param limit			分页显示数量
	 * @return
	 * @author 				lw-21
	 * @date				2016年6月20日  上午11:42:57
	 */
	@Override
	@ResponseBody
	public String getHomeworkSolution(Integer userId, Integer homeworkId, Integer start, Integer limit){
		if(userId == null || homeworkId == null){
			LogUtil.error(this.getClass(), "getHomeworkSolution", AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM).toJSONString();
		}
		return studentHomeWorksService.getHomeworkSolution(userId,homeworkId,start,limit);
	}
	
	
	

	/**
	 * 教师发布文本作业
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param homeworkId			文本作业id
	 * @param homeworkTitle			作业标题
	 * @param homeworkBody			作业内容
	 * @param courseCardId			所属课程卡id
	 * @param homeworkShowType		作业显示类型
	 * @return
	 * @author  lw
	 * @date    2016年6月18日 下午9:59:25
	 */
	@Override
	@ResponseBody
	public String submitHomework(Integer userId,  Integer homeworkId, String homeworkTitle, String homeworkBody, Integer courseCardId,Integer homeworkShowType){
		
		//	1. 基本参数校验
		if( userId == null || homeworkShowType==null){
			LogUtil.error(this.getClass(), "submitHomework", AppErrorCode.ERROR_SUBMITHOMEWORK_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITHOMEWORK_REQUEST_PARAM).toJSONString();
		}
		
		// 2. 文本作业标题校验
		if(homeworkTitle == null || (homeworkTitle = homeworkTitle.trim()).length() <= 0){
			LogUtil.error(this.getClass(), "submitHomework", AppErrorCode.TEACHER_HOME_TITLE);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_TITLE).toJSONString();
		}
		String strBody = "";
		if(!homeworkBody.equals("")||homeworkBody!=null){
			strBody=homeworkBody.replaceAll("<[.[^<]]*>","");
		}
		// 3. 文本作业内容校验
		if(strBody.equals("") || strBody == null || strBody.length() <= 0 || strBody.length() > 160){
			LogUtil.error(this.getClass(), "submitHomework", AppErrorCode.TEACHER_HOME_BODY);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_BODY).toJSONString();
		}
		// 4. 文本作业标题做特殊字符校验
		String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
		for(String s:str){
			if(homeworkTitle.contains(s)){
				LogUtil.error(this.getClass(), "submitTask", "文本作业标题不能包含特殊字符");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,AppErrorCode.HOME_TITLE).toJSONString();
			}
		}
		return publishhomeworkAppService.submitTeachCourse(userId,homeworkId,homeworkTitle,homeworkBody,courseCardId,homeworkShowType);
	} 
	
	/**
	 * 上传文本作业
	 * @param userId			学生id
	 * @param homeworkId		文本作业id
	 * @param hSolutionBody		文本作业内容
	 * @param homeworkFileIds	附件ids		多个附件id之间用逗号分隔
	 * @return
	 * @author 					lw
	 * @date					2016年6月24日  上午9:16:41
	 */
	@Override
	@ResponseBody
	public String submitHomeworkSolution(Integer userId, Integer homeworkId, String hSolutionBody, String homeworkFileIds){
		
		//	1.1 基本参数校验
		if(userId == null ||  homeworkId == null ){
			LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.TEACHER_HOME_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_REQUEST_PARAM).toJSONString();
		}
		
		//	1.2	文本长度校验
		if( hSolutionBody != null && hSolutionBody.length() > 0 ){
			return teachStudentHomeworkAppService.submitHomeworkSolution(userId, homeworkId, hSolutionBody, homeworkFileIds);
		}
		
		//	1.2 错误消息返回
		LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.ERROR_HOMEWORK_BODY);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_HOMEWORK_BODY).toJSONString();
	}
	
	
	
	
	
	
	
	/**
	 * 提交作业或人员的评分	
	 * @param userId				用户id
	 * @param scoreType				提交评分类型
	 * @param scoreObjectId			评分对象id
	 * @param score					分数
	 * @param textEvaluation		教师对作业的文字点评
	 * @return
	 * @author 						lw
	 * @date						2016年6月21日  下午10:10:08
	 */
	@Override
	@ResponseBody
	public String submitScore(Integer userId, Integer scoreType, Integer scoreObjectId, Integer score, String textEvaluation){
		if(userId == null || scoreType == null || scoreObjectId == null || score == null){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITHOMEWORKSOLUTION_REQUEST_PARAM).toJSONString();
		}
		return publishsubmitScoreAppService.submitScore(userId, scoreType, scoreObjectId, score, textEvaluation);
	}



}
