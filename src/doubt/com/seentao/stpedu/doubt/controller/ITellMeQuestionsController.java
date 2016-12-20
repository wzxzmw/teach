package com.seentao.stpedu.doubt.controller;

public interface ITellMeQuestionsController {

	
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
	public String getQuestions(Integer userId, Integer classId, Integer isExcellent, Integer inquireType,
			 Integer chapterId, Integer questionId, Integer start, Integer limit, Integer classType, String _type);
	
	
	
	
	/**
	 * 提交问题的回复信息
	 * @param userId		用户id
	 * @param questionId	问题id
	 * @param answerBody	问题回复内容
	 * @return
	 * @author 				lw
	 * @date				2016年6月23日  下午1:52:48
	 */
	public String submitAnswer(Integer userId, Integer questionId, String answerBody, Integer classType);
	
	
	
	/**
	 * 提交问题信息
	 * @param userId			用户id
	 * @param questionTitle		问题标题
	 * @param questionBody		问题内容		富文本
	 * @param classId			问题所属班级id
	 * @param chapterId			问题所属章节id
	 * @param _type				请求端类型		pc/app
	 * @return
	 * @author 					lw
	 * @date					2016年6月23日  上午10:46:01
	 */
	public String submitQuestion(Integer userId, String questionTitle, String questionBody, Integer classId, String chapterId, Integer classType);
	
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
	public String getAnswersForMobile(Integer start, Integer limit, Integer classType, Integer questionId, Integer inquireType, String _type,String answerId);
		
}
