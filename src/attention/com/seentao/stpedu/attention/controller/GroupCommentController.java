package com.seentao.stpedu.attention.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.attention.service.GroupCommentService;

/**
 * 群组评论信息
 * <pre>项目名称：stpedu    
 * 类名称：GroupComment    
 */
@Controller
public class GroupCommentController implements IGroupCommentController {
	
	@Autowired
	private GroupCommentService groupCommentService;
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.IGroupCommentController#getComments(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	/**
	 * getComments(获取群组评论信息)
	 * @param userId 用户ID
	 * @param classId 班级id
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id
	 * @param topicId 话题id
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @param start 起始页 从0开始
	 * @param limit 每页数量 
	 * @param inquireType
	 * @author ligs
	 * @date 2016年6月28日 下午3:43:04
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getComments")
	public String getComments(Integer classId,Integer classType,Integer gameEventId,Integer topicId,Integer commentType,Integer start,Integer limit, Integer inquireType,Integer userId){
		return groupCommentService.getComments( classId, classType, gameEventId, topicId, commentType, start, limit,  inquireType, userId).toJSONString();
	}
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.IGroupCommentController#submitComment(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer)    
	 */
	@Override
	/**
	 * submitComment(提交群组评论信息)   
	 * @param userId 用户ID
	 * @param commentModule 群组评论功能所在模块 1:教学的群组模块；2:竞技场的赛事交流模块；3:俱乐部的培训班中的群组模块；4:俱乐部的话题模块
	 * @param classId 班级id 当 群组评论功能所在模块 为1和3时，会传递该值
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id 当 群组评论功能所在模块 为2时，会传递该值
	 * @param topicId 话题id 当 群组评论功能所在模块 为4时，会传递该值
	 * @param commentBody 群组评论内容
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @author ligs
	 * @date 2016年6月28日 下午8:32:50
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/submitComment")
	public String submitComment(Integer userId,Integer commentModule,Integer classId,Integer classType,Integer gameEventId,Integer topicId,String commentBody,Integer commentType){
		return groupCommentService.submitComment( userId, commentModule, classId, classType, gameEventId, topicId, commentBody, commentType).toJSONString();
	}
	
	
	
	
	
	/**
	 * getCommentsForMobile(移动端：获取群组评论信息)
	 * @param userId 用户ID
	 * @param classId 班级id
	 * @param classType 班级类型 1:教学班；2:俱乐部培训班；
	 * @param gameEventId 赛事id
	 * @param topicId 话题id
	 * @param commentType 群组评论类型  1:讨论；2:通知；
	 * @param start 起始页 从0开始
	 * @param limit 每页数量 
	 * @param sortType 获取信息顺序   1:最新的；2:历史信息；
	 * @param newCommentId 最新的群组评论信息id
	 * @param oldCommentId 最早的群组评论信息id
	 * @param inquireType
	 * @author ligs
	 * @date 2016年7月19日 下午20:36:04
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getCommentsForMobile")
	public String getCommentsForMobile(Integer userId,Integer classId,Integer classType,Integer gameEventId,Integer topicId,Integer commentType,Integer start,Integer limit,Integer sortType,Integer newCommentId,Integer oldCommentId,Integer inquireType){
		return groupCommentService.getCommentsForMobile( userId, classId, classType, gameEventId, topicId, commentType, start, limit, sortType, newCommentId, oldCommentId, inquireType).toJSONString();
	}
}
