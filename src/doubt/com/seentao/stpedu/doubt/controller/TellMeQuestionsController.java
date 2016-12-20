package com.seentao.stpedu.doubt.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.service.ClubTrainingQuestionAnswerService;
import com.seentao.stpedu.common.service.TeachQuestionAnswerService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.doubt.service.PublishAnswerAppService;
import com.seentao.stpedu.doubt.service.PublishQuestionAppService;
import com.seentao.stpedu.doubt.service.TellMeQuestionsAppService;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Controller
public class TellMeQuestionsController implements ITellMeQuestionsController{

	@Autowired
	private TellMeQuestionsAppService appService;
	@Autowired
	private PublishAnswerAppService publishAnswerAppService;
	@Autowired
	private PublishQuestionAppService publishQuestionAppService;
	@Autowired
	private TeachQuestionAnswerService answerService;
	@Autowired
	private ClubTrainingQuestionAnswerService clubQuestionAnswerService;
	
	
	
	
	
	
	
	/**
	 * 获取答疑信息
	 * @param userId			用户id
	 * @param start				起始数			从0开始
	 * @param limit				每页数量
	 * @param classId			班级id
	 * @param chapterId			章节id
	 * @param questionId		问题id
	 * @param isExcellent		是否获取精华问题		1:是；0：否(获取全部)；默认为0
	 * @param inquireType		查询类型
	 * @return
	 * @author 					lw
	 * @date					2016年6月22日  下午5:48:40
	 */
	@Override
	@ResponseBody
	public String getQuestions(Integer userId, Integer classId, Integer isExcellent, Integer inquireType,
			 Integer chapterId, Integer questionId, Integer start, Integer limit, Integer classType, String _type){
		
		if(userId == null ||  classId == null || isExcellent == null || inquireType == null || classType == null || _type == null){
			LogUtil.error(this.getClass(), "getQuestions", AppErrorCode.ERROR_QUESTIONS_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUESTIONS_PARAM).toJSONString();
		}
		
		return appService.findQuestions(userId, classId, isExcellent, inquireType, chapterId, questionId, start, limit, classType, _type);
	}
	
	

	
	/**
	 * 提交问题的回复信息
	 * @param userId		用户id
	 * @param questionId	问题id
	 * @param answerBody	问题回复内容
	 * @param classType		班级类型	：培训班/教学班
	 * @return
	 * @author 				lw
	 * @date				2016年6月23日  下午1:52:48
	 */
	@SuppressWarnings("unused")
	@Override
	@ResponseBody
	public String submitAnswer(Integer userId, Integer questionId, String answerBody, Integer classType){
		
		int answerBodyLength = answerBody.trim().length();
		if(userId == null || questionId == null || StringUtils.isEmpty(answerBody)|| answerBodyLength <= 0 || classType == null){
			LogUtil.error(this.getClass(), "submitAnswer", AppErrorCode.ERROR_SUBMITANSWER_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITANSWER_REQUEST_PARAM).toJSONString();
		}
		
		//校验内容长度
		if(answerBodyLength > 5000){
			LogUtil.error(this.getClass(), "submitAnswer", AppErrorCode.CONTENT_LENGTH_TO_LONG);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CONTENT_LENGTH_TO_LONG).toJSONString();
		}
		return publishAnswerAppService.doSubmitAnswer(userId, questionId, answerBody, classType);
	}
	
	

	
	
	
	/**
	 * 提交问题信息
	 * @param userId			用户id
	 * @param questionTitle		问题标题
	 * @param questionBody		问题内容		富文本
	 * @param classId			问题所属班级id
	 * @param chapterId			问题所属章节id
	 * @return
	 * @author 					lw
	 * @date					2016年6月23日  上午10:46:01
	 */
	@Override
	@ResponseBody
	public String submitQuestion(Integer userId, String questionTitle, String questionBody, Integer classId, String chapterId, Integer classType){
		
		//	1. 基本参数校验
		if(userId == null || classId == null || classType == null){
			LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_REQUEST_PARAM).toJSONString();
		}
		
		//	2. 页面参数提交校验
		if(questionTitle == null || (questionTitle = questionTitle.trim()).length() <= 0 ||  questionBody == null || questionBody.length() <= 0){
			LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_CONTENT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_CONTENT).toJSONString();
		}
		
		// 3. 页面标题校验
		if(questionTitle.length() > 30){
			LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_TITLE_LENGTH);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_TITLE_LENGTH).toJSONString();
		}
		
		//	4. 页面内容校验
		if(questionBody.length() > 5000){
			LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.CONTENT_LENGTH_TO_LONG);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CONTENT_LENGTH_TO_LONG).toJSONString();
		}
		// 5.校验特殊字符
		String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
		for(String s:str){
			if(questionTitle.contains(s)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TITLE_NOT_REGULAR).toJSONString();
			}
		}
		
		return publishQuestionAppService.submitQuestion(userId, questionTitle, questionBody, classId, chapterId, classType);
	}




	/**
	 * 移动端：获取提问的回复信息
	 * @param userId		用户id
	 * @param start
	 * @param limit
	 * @param classType		班级类型
	 * @param questionId	问题id
	 * @param inquireType	请求类型
	 * @return
	 * @author 				lw
	 * @date				2016年7月18日  下午10:47:00
	 */
	@Override
	public String getAnswersForMobile(Integer start, Integer limit, Integer classType, Integer questionId, Integer inquireType, String _type,String answerId) {
		if("".equals(answerId)){
			answerId = null;
		}
		//	1. 基本参数校验
		if(classType == null || inquireType == null){
			LogUtil.error(this.getClass(), "getAnswersForMobile" , AppErrorCode.ERROR_ANSWERS_APP_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ANSWERS_APP_PARAM.toString()).toJSONString();
		}
		
		//	2.1 教学逻辑
		if(classType == GameConstants.TEACHING_CLASS){
			return answerService.findQuestionAnswer(start, limit, questionId, inquireType, _type,answerId);
			
		//	2.2 教学班逻辑
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
			return clubQuestionAnswerService.findQuestionAnswer(start, limit, questionId, inquireType, _type,answerId);
		}
		
		LogUtil.error(this.getClass(), "getAnswersForMobile" , AppErrorCode.ERROR_ANSWERS_APP_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ANSWERS_APP_PARAM.toString()).toJSONString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
