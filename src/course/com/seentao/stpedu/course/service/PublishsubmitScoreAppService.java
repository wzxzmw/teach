package com.seentao.stpedu.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.service.BaseTeachStudentHomeworkService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class PublishsubmitScoreAppService {

	
	@Autowired
	private BaseTeachStudentHomeworkService teachStudentHomeworkService;
	
	/**
	 * 提交作业或人员的评分	
	 * @param userId				用户id
	 * @param scoreType				提交评分类型
	 * @param scoreObjectId			评分对象id
	 * @param score					分数
	 * @param textEvaluation		教师对作业的文字点评
	 * @return
	 * @author 						lw
	 * @date						2016年6月21日  下午10:07:55
	 */
	public String submitScore(int userId, int scoreType, int scoreObjectId, int score, String textEvaluation) {
		
		//	1. 校验教师评价分数
		if(0 >= score &&  score> GameConstants.TEACHER_HOMEWORK_SCORE){
			LogUtil.error(this.getClass(), "submitScore", AppErrorCode.TEACHER_HOME_SCORE_WRONG);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_SCORE_WRONG).toJSONString();
		}
		
		//	2.1 教师对学生上传作业的评分
		if(scoreType == GameConstants.TEACHER_SUBMITSCORE_HOMEWORK){
			
			return teachStudentHomeworkService.submitHomeWorkeScore(userId,scoreObjectId,textEvaluation,score);
			
		//	2.2 教师对班级学生的评分
		}else if(scoreType == GameConstants.TEACHER_SUBMITSCORE_CLASS){
			
			return teachStudentHomeworkService.submitStudentScore(userId,scoreObjectId,score);
		}
		
		LogUtil.error(this.getClass(), "submitScore", AppErrorCode.ERROR_SUBMITHOMEWORKSOLUTION_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITHOMEWORKSOLUTION_REQUEST_PARAM).toJSONString();
	}
	
	
	
	

}
