package com.seentao.stpedu.club.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IClubController {

	/** 
	* @Title: getTopics 
	* @Description: 获取话题信息
	* @param userId		用户ID
	* @param memberId	人员ID
	* @param clubId		俱乐部id
	* @param topicId	话题id
	* @param startDate	查询开始时间的时间戳
	* @param endDate	查询结束时间的时间戳
	* @param searchWord	搜索词
	* @param start		起始数
	* @param limit		每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月28日 上午10:04:21
	*/
	String getTopics(Integer userId, Integer memberId, Integer clubId, Integer topicId, Integer startDate,
			Integer endDate, String searchWord, Integer start, Integer limit, Integer inquireType);

	/** 
	* @Title: submitTopic 
	* @Description: 发布话题
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param topicTitle	话题标题
	* @param topicContent	话题内容
	* @param isTop 是否置顶
	* @return String
	* @author liulin
	* @date 2016年6月28日 下午5:40:58
	*/
	String submitTopic(Integer userId, Integer clubId, String topicTitle, String topicContent, Integer isTop);

	/** 
	* @Title: getReminds 
	* @Description: 获取提醒 
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param remindShowType	提醒的显示分类
	* @param start	起始数
	* @param limit	每页数量
	* @return String
	* @author liulin
	* @date 2016年6月28日 下午9:04:02
	*/
	String getReminds(Integer userId, Integer clubId, Integer remindShowType, Integer start, Integer limit);

	/** 
	* @Title: submitRemind 
	* @Description: 发布提醒
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param remindContent	提醒内容
	* @param remindObject	提醒对象
	* @param remindUserIds	提醒人员的id
	* @return String
	* @author liulin
	* @date 2016年6月29日 上午11:56:29
	*/
	String submitRemind(Integer userId, Integer clubId, String remindContent, Integer remindObject,
			String remindUserIds);

	/** 
	* @Title: getWebNotices 
	* @Description: 获取通知
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午4:53:26
	*/
	String getWebNotices(Integer userId, Integer clubId, Integer start, Integer limit, Integer inquireType);

	/** 
	* @Title: submitNotice 
	* @Description: 发送通知 
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param noticeTitle	通知标题
	* @param noticeContent	通知内容
	* @param isTop	是否置顶
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午7:20:39
	*/
	String submitNotice(Integer userId, Integer clubId, String noticeTitle, String noticeContent, Integer isTop);

	/** 
	* @Title: getMoods 
	* @Description: 获取心情
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param clubId	俱乐部ID
	* @param timeRange 查询的时间范围
	* @param start	起始数
	* @param limit	每页数量
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年6月29日 下午9:39:53
	*/
	String getMoods(Integer userId, Integer memberId, Integer clubId, Integer timeRange, Integer start, Integer limit,
			Integer inquireType);

	/** 
	* @Title: getDynamics 
	* @Description: 获取动态
	* @param userId	用户ID
	* @param memberId	会员ID
	* @param classId	班级ID
	* @param classType	班级类型
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页显示数
	* @param inquireType	查询类型
	* @return String
	* @author liulin
	* @date 2016年7月4日 上午11:44:31
	*/
	String getDynamics(Integer userId, Integer memberId, Integer classId, Integer classType, Integer clubId,
			Integer start, Integer limit, Integer inquireType);
	
	/** 
	* @Title: getDynamicEntryForMobile 
	* @Description: 移动端：获取动态入口信息
	* @param userId
	* @return String
	* @author liulin
	* @date 2016年7月19日 上午10:15:34
	*/
	String getDynamicEntryForMobile(Integer userId);
	/**移动端获取动态信息  
	 * @param userId
	 * @param clubId
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @return
	 */
	String getDynamicsForMobile(Integer userId, Integer clubId,Integer start,Integer limit,Integer inquireType);

}