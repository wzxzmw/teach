package com.seentao.stpedu.doubt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.ClubTrainingQuestionService;
import com.seentao.stpedu.common.service.TeachQuestionService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;


@Service
public class TellMeQuestionsAppService {


	@Autowired
	private TeachQuestionService questionsService;
	@Autowired
	private ClubTrainingQuestionService clubQuestionService;
	
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
	 * @param classType 
	 * @param _type 
	 * @date					2016年6月22日  下午7:44:54
	 */
	public String findQuestions(Integer userId, Integer classId, Integer isExcellent, Integer inquireType,
			Integer chapterId, Integer questionId, Integer start, Integer limit, Integer classType, String _type) {
		
		// 1. 获取用户信息
		String redisData = RedisComponent.findRedisObject(userId, CenterUser.class);
		if(StringUtil.isEmpty(redisData)){
			LogUtil.error(this.getClass(), "findQuestions", AppErrorCode.ERROR_QUESTIONS_USER_OUT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUESTIONS_USER_OUT).toJSONString();
		}
		CenterUser user = JSONObject.parseObject(redisData, CenterUser.class);
		
		//	2.1 问题列表
		if(GameConstants.STUDENT_QUESTION_PAGE == inquireType){
			
			//	2.1.1  加载分页参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("isDelete", GameConstants.NO_DEL);
			paramMap.put("classId", classId);
			
			//	2.1.2  加入精华查询参数
			if(isExcellent !=null){
				paramMap.put("isEssence", isExcellent);
			}
			
			//	2.1.3 教学逻辑列表
			if(GameConstants.TEACHING_CLASS == classType){
				
				//	2.1.4.2 加入章节参数
				if(chapterId != null && chapterId > 0 ){
					paramMap.put("chapterId", chapterId);
				}
				
				//	2.1.4.3 分页查询
				return  questionsService.getQuestionsList(paramMap, start, limit, user.getUserType(), _type).toJSONString();
			
			//	2.1.4  培训班逻辑列表
			}else if (GameConstants.CLUB_TRAIN_CLASS == classType){
				
				return clubQuestionService.getQuestionsList(paramMap, start, limit, user.getUserType(), _type).toJSONString();
			}
			
		//	2.2 问题详细信息
		}else if(GameConstants.STUDENT_QUESTION_ENTITY == inquireType && questionId!= null && questionId > 0){
			
			//	2.2 教学详情逻辑
			if(GameConstants.TEACHING_CLASS == classType){
				return  questionsService.getQuestionsOne(questionId, start, limit, user.getUserType(), _type).toJSONString();
			
			//	2.3 培训班详情逻辑
			}else if (GameConstants.CLUB_TRAIN_CLASS == classType){
				
				return clubQuestionService.getQuestionsOne(questionId, start, limit, user.getUserType(), _type).toJSONString();
			}
		}
		
		LogUtil.error(this.getClass(), "findQuestions", AppErrorCode.ERROR_QUESTIONS_TYPE);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUESTIONS_TYPE).toJSONString();
	}
	
	
	


}
