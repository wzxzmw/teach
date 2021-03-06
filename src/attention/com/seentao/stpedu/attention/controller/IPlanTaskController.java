package com.seentao.stpedu.attention.controller;

import java.text.ParseException;


public interface IPlanTaskController {

	/**
	 * submitTask(提交计划任务信息)   
	 * @param userId 用户ID
	 * @param taskId 计划任务id
	 * @param taskDate 计划任务发生时间的时间戳
	 * @param taskContent 任务内容
	 * @param targetContent 任务目标
	 * @param needSign 是否需要签到  1:是；0:否；默认是0
	 * @author ligs
	 * @date 2016年6月28日 下午10:21:55
	 * @return
	 * @throws ParseException 
	 */
	String submitTask(Integer userId, Integer taskId, Integer taskDate, String taskContent, String targetContent,
			Integer needSign);

	/**
	 * getTasks(获取计划任务信息)   
	 * @param 用户ID
	 * @param classId 班级ID
	 * @param startYear 开始年份
	 * @param startMonth 开始月份
	 * @param endYear 截止年
	 * @param endMonth 截止月
	 * @author ligs
	 * @date 2016年6月29日 上午10:44:44
	 * @return
	 * @throws ParseException 
	 */
	String getTasks(Integer userId, Integer classId, String startYear, String startMonth, String endYear,
			String endMonth);

}