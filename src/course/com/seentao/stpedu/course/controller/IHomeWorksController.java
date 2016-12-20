package com.seentao.stpedu.course.controller;

public interface IHomeWorksController {

	
	/**
	 * 获取教师所发文本作业信息
	 * 	教师发布的作业文本其实就是课程
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
	public String getHomeworks(Integer userId, Integer start, Integer limit, Integer courseCardId, Integer homeworkId , Integer inquireType, Integer requestSide);
	
	
	
	
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
	public String getHomeworkSolution(Integer userId, Integer homeworkId, Integer start, Integer limit);
	
	
	
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
	public String submitHomework(Integer userId,  Integer homeworkId, String homeworkTitle, String homeworkBody, Integer courseCardId,Integer homeworkShowType);
	
	
	

	
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
	public String submitHomeworkSolution(Integer userId, Integer homeworkId, String hSolutionBody, String homeworkFileIds);
	
	
	
	
	
	
	
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
	public String submitScore(Integer userId, Integer scoreType, Integer scoreObjectId, Integer score, String textEvaluation);
	
	
}
