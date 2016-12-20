package com.seentao.stpedu.guess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.guess.service.AccessGetQuizsAppService;
import com.seentao.stpedu.guess.service.FinishQuizAppService;
import com.seentao.stpedu.guess.service.PublishGetQuizTopicAppService;
import com.seentao.stpedu.guess.service.PublishQuizAppService;
import com.seentao.stpedu.guess.service.PublishQuizResultAppService;
import com.seentao.stpedu.guess.service.PublishQuizTopicAppService;
import com.seentao.stpedu.utils.LogUtil;

@Controller
public class GuessController implements IGuessController {
	
	
	
	@Autowired
	private AccessGetQuizsAppService appService;
	@Autowired
	private FinishQuizAppService finishQuizAppService;
	@Autowired
	private PublishGetQuizTopicAppService publishGetQuizTopicAppService;
	@Autowired
	private PublishQuizAppService publishQuizAppService;
	@Autowired
	private PublishQuizResultAppService publishQuizResultAppService;
	@Autowired
	private PublishQuizTopicAppService publishQuizTopicAppService;
	
	/**
	 * 获取竞猜信息
	 * @param userId			用户id
	 * @param quizTopicId		竞猜话题id
	 * @param start				
	 * @param limit
	 * @param _type				pc/app端类型
	 * @return
	 * @author 					lw
	 * @date					2016年6月27日  下午2:29:49
	 */
	@Override
	@ResponseBody
	public String getQuizs(Integer userId, Integer quizTopicId, Integer start, Integer limit, String _type, Integer memberId, Integer inquireType){
		
		if(userId == null || quizTopicId == null || _type == null){
			LogUtil.error(this.getClass(), "getQuizs", AppErrorCode.ERROR_GETQUIZS_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GETQUIZS_REQUEST_PARAM).toJSONString();
		}
		
		return appService.getQuizs(userId, quizTopicId, start, limit, _type, memberId, inquireType);
	}
	

	
	/**
	 * 结束竞猜话题或竞猜
	 * @param userId			用户id
	 * @param actionType		操作类型
	 * @param actionObjectId	操作对象id
	 * @return	
	 * @author 					lw
	 * @date					2016年6月27日  上午11:29:34
	 */
	@Override
	@ResponseBody
	public String finishQuiz(Integer userId, Integer actionType, Integer actionObjectId){
		
		if(userId == null || actionType == null || actionObjectId == null){
			LogUtil.error(this.getClass(), "finishQuizController", AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM).toJSONString();
		}
		
		return finishQuizAppService.finishQuiz(userId, actionObjectId, actionType);
	}
	

	
	
	/**
	 * 获取竞猜话题信息
	 * @param userId		用户id
	 * @param gameEventId	赛事id
	 * @param quizTopicId	竞猜话题id
	 * @param start			
	 * @param limit
	 * @param inquireType	查询类型
	 * @return
	 * @author 				lw
	 * @date				2016年6月27日  下午4:05:19
	 */
	@Override
	@ResponseBody
	public String getQuizTopic(Integer userId, Integer gameEventId, Integer quizTopicId, Integer start, Integer limit, Integer inquireType){
		
		if(userId == null || inquireType == null){
			LogUtil.error(this.getClass(), "getQuizTopic", AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM).toJSONString();
		}
		
		return publishGetQuizTopicAppService.getQuizTopic(userId, gameEventId, quizTopicId, start, limit, inquireType);
	}
	
	
	
	
	
	/**
	 * 创建竞猜
	 * @param userId					用户id
	 * @param quizTopicId				竞猜话题id
	 * @param quizType					竞猜类型					1:普通竞猜；2:坐庄竞猜；
	 * @param quizTitle					竞猜主题
	 * @param quizEndDate				竞猜结束时间的时间戳
	 * @param bankerBettingObject		庄家押哪方					1:能；2:不能； 		只有竞猜类型为坐庄竞猜时传该字段
	 * @param odds						赔率
	 * @param bankerBettingCount		庄家投注数量(虚拟物品的数量)		底金
	 * @return
	 * @author 							lw
	 * @date							2016年6月24日  下午9:48:21
	 */
	@Override
	@ResponseBody
	public String submitQuiz(Integer userId, Integer quizTopicId, Integer quizType, String quizTitle, 
			Integer quizEndDate, Integer bankerBettingObject, Float odds, Double bankerBettingCount){
		
		if( quizTopicId == null || quizType == null ){
			LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZ_REQUEST_PARAM).toJSONString();
					
		}
		
		//校验提交文本内容
		if( quizTitle == null || (quizTitle = quizTitle.trim()).length() <= 0 || quizEndDate == null){
			LogUtil.error(this.getClass(), "submitQuiz", AppErrorCode.ERROR_QUIZ_SUBMIT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZ_SUBMIT).toJSONString();
		}
		
		return publishQuizAppService.submitQuiz(userId, quizTopicId, quizType, quizTitle, quizEndDate, bankerBettingObject, odds, bankerBettingCount);
	}
	

	
	
	/**
	 * 竞猜公布结果
	 * @param userId			用户id
	 * @param quizId			竞猜id
	 * @param quizWinner		竞猜获胜方
	 * @return
	 * @author 					lw
	 * @date					2016年6月26日  下午2:14:21
	 */
	@Override
	@RequestMapping("/submitQuizResult")
	@ResponseBody
	public String submitQuizResult(Integer userId, Integer quizId, Integer quizWinner){
		
		if(userId == null || quizId == null || quizWinner == null){
			LogUtil.error(this.getClass(), "submitQuizResult", AppErrorCode.ERROR_QUIZRESULT_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUIZRESULT_REQUEST_PARAM).toJSONString();
		}
		
		return publishQuizResultAppService.submitQuizResult(userId, quizId, quizWinner);
	}
	
	
	
	
	

	
	
	
	/**
	 * 创建竞猜话题
	 * @param userId			用户id
	 * @param quizTopicTitle	竞猜主题
	 * @param quizTopicDesc		竞猜说明
	 * @param gameId			比赛id
	 * @return
	 * @author 			lw
	 * @date			2016年6月24日  下午8:09:51
	 */
	@Override
	@ResponseBody
	public String submitQuizTopic(Integer userId, String quizTopicTitle, String quizTopicDesc, Integer gameId){
		
		if(StringUtils.isEmpty(gameId)){
			LogUtil.error(this.getClass(), "submitQuizTopic", AppErrorCode.ERROR_CREATE_GUESS_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_CREATE_GUESS_REQUEST_PARAM).toJSONString();
		}
		
		//	1. 校验竞猜话题标题
		if(quizTopicTitle == null || (quizTopicTitle = quizTopicTitle.trim()).length() <= 0){
			LogUtil.error(this.getClass(), "submitQuizTopic", AppErrorCode.ERROR_GUESS_TITLE);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GUESS_TITLE).toJSONString();
		}
		
		//	2. 校验竞猜话题说明
		if(quizTopicDesc != null && quizTopicDesc.length() > 160 ){
			LogUtil.error(this.getClass(), "submitQuizTopic", AppErrorCode.ERROR_GUESS_TITLE);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GUESS_TITLE).toJSONString();
		}
		
		
		return publishQuizTopicAppService.submitQuizTopic(userId, quizTopicTitle, quizTopicDesc, gameId);
	}
	
	

}
