package com.seentao.stpedu.doubt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class PublishQuestionAppService {

	
	@Autowired
	private TeachQuestionService questionsService;
	@Autowired
	private ClubTrainingQuestionService clubQuestionsService;
	/**
	 * 提交问题信息
	 * @param userId			用户id
	 * @param questionTitle		问题标题
	 * @param questionBody		问题内容		富文本
	 * @param classId			问题所属班级id
	 * @param chapterId			问题所属章节id
	 * @return
	 * @author 					lw
	 * @param classType 
	 * @date					2016年6月23日  上午10:49:55
	 */
	public String submitQuestion(Integer userId, String questionTitle, String questionBody, Integer classId, String chapterId, Integer classType) {
		
		//	1. 教学班逻辑
		if(classType == GameConstants.TEACHING_CLASS){
			
			//	1.1 发布人和班级校验
			CenterUser user = questionsService.checkClassAndUser(classId, userId);
			if(user == null){
				LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT).toJSONString();
			}
			
			//	1.2 章节 和 班级 校验
			Integer cid = 0;
			if(!StringUtil.isEmpty(chapterId) && (cid = Integer.valueOf(chapterId)) > 0){
				
				String chapterRedis = RedisComponent.findRedisObject(cid, TeachCourseChapter.class);
				if(StringUtil.isEmpty(chapterRedis)){
					LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT).toJSONString();
				}
				TeachCourseChapter chapter = JSONObject.parseObject(chapterRedis, TeachCourseChapter.class);
				
				if(!classId.equals(chapter.getClassId())){
					LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT).toJSONString();
				}
			}
			
			//	1.3 保存教学问题
			return questionsService.addTeachQuestion(userId, cid, classId, questionTitle, questionBody).toJSONString();
			
		
		//	2. 俱乐部班逻辑
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
			
			//	2.1 用户和班级校验
			ClubJoinTraining clubJon = clubQuestionsService.checkClassAndUser(classId, userId);
			if(clubJon == null){
				LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_USER_OUT).toJSONString();
			}
			
			//	2.2 保存培训班问题
			return clubQuestionsService.addClubQuestion(classId, userId, questionTitle, questionBody).toJSONString();
			
		}
		
		LogUtil.error(this.getClass(), "submitQuestion", AppErrorCode.ERROR_SUBMITQUESTION_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITQUESTION_REQUEST_PARAM).toJSONString();
		
		
	}

	
	
	
	
}
