package com.seentao.stpedu.guess.controller;

public interface IGuessController {

	
	/**
	 * 获取竞猜信息
	 * @param userId			用户id
	 * @param quizTopicId		竞猜话题id
	 * @param start				
	 * @param limit
	 * @param _type				pc/app端类型
	 * @param memberId			人员id
	 * @param inquireType		查询类型
	 * @return
	 * @author 					lw
	 * @date					2016年6月27日  下午2:29:49
	 */
	public String getQuizs(Integer userId, Integer quizTopicId, Integer start, Integer limit, String _type, Integer memberId, Integer inquireType);
	
	
	
	/**
	 * 	结束竞猜话题或竞猜
	 * @param userId			用户id
	 * @param actionType		操作类型
	 * @param actionObjectId	操作对象id
	 * @return	
	 * @author 					lw
	 * @date					2016年6月27日  上午11:29:34
	 */
	public String finishQuiz(Integer userId, Integer actionType, Integer actionObjectId);
	
	
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
	public String getQuizTopic(Integer userId, Integer gameEventId, Integer quizTopicId, Integer start, Integer limit, Integer inquireType);
	
	
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
	public String submitQuiz(Integer userId, Integer quizTopicId, Integer quizType, String quizTitle, 
			Integer quizEndDate, Integer bankerBettingObject, Float odds, Double bankerBettingCount);
	
	
	
	/**
	 * 竞猜公布结果
	 * @param userId			用户id
	 * @param quizId			竞猜id
	 * @param quizWinner		竞猜获胜方
	 * @return
	 * @author 					lw
	 * @date					2016年6月26日  下午2:14:21
	 */
	public String submitQuizResult(Integer userId, Integer quizId, Integer quizWinner);
	
	
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
	public String submitQuizTopic(Integer userId, String quizTopicTitle, String quizTopicDesc, Integer gameId);

	
	
}
