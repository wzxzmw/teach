package com.seentao.stpedu.doubt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.service.ClubTrainingQuestionAnswerService;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.TeachQuestionAnswerService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class PublishAnswerAppService {

	
	@Autowired
	private TeachQuestionAnswerService questionAnswerService;
	@Autowired
	private ClubTrainingQuestionService clubQuestionService;
	@Autowired
	private ClubTrainingQuestionAnswerService clubAnswerService;
	
	/**
	 * 提交问题的回复信息
	 * @param userId		用户id
	 * @param questionId	问题id
	 * @param answerBody	问题回复内容
	 * @param classType 	班级类型
	 * @param chapterId 	章节id
	 * @param classId 		班级id
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  下午1:56:13
	 */
	public String doSubmitAnswer(Integer userId, Integer questionId, String answerBody, Integer classType) {
		
		
		//	1. 教学班逻辑
		if(classType == GameConstants.TEACHING_CLASS){
			
			//	1.1 问题和班级的校验
			TeachQuestion  question = questionAnswerService.checkQuestionAndClass(questionId);
			if(question == null ){
				LogUtil.error(this.getClass(), "doSubmitAnswer", AppErrorCode.ERROR_SUBMITANSWER_QUESTION_AND_CLASS);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITANSWER_QUESTION_AND_CLASS).toJSONString();
			}
			
			//	1.2 添加 回复信息 并把回复问题数量+1 并删除缓存，用户 回复信息加一,并删除缓存
			return questionAnswerService.addQuestionAnswer(questionId, userId, answerBody).toJSONString();
		
		//	2. 俱乐部班级逻辑
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
			
			//	2.1 所属俱乐部问题的校验
			ClubTrainingQuestion question = clubQuestionService.checkClassAndClass(questionId);
			if(question == null){
				LogUtil.error(this.getClass(), "doSubmitAnswer", AppErrorCode.ERROR_SUBMITANSWER_QUESTION_AND_CLASS);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITANSWER_QUESTION_AND_CLASS).toJSONString();
			}
			
			//	2.2 添加 回复信息 并把回复问题数量+1 并删除缓存，用户 回复信息加一,并删除缓存
			return clubAnswerService.addClubAnswer(questionId, userId, answerBody).toJSONString();
		}
		
		LogUtil.error(this.getClass(), "doSubmitAnswer", AppErrorCode.ERROR_SUBMITANSWER_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITANSWER_REQUEST_PARAM).toJSONString();
	}

	
	
}
