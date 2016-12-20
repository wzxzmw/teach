package com.seentao.stpedu.attention.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.attention.service.MessageHandleService;

/**
 * 对信息主体操作
 * <pre>项目名称：stpedu    
 * 类名称：MessageHandleController    
 */
@Controller
public class MessageHandleController implements IMessageHandleController {

	@Autowired
	private MessageHandleService messageHandleService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.IMessageHandleController#submitAttitude(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	@Override
	/**
	 * submitAttitude(对内容主体进行点赞、加精等态度操作) 
	 * @param userId 用户ID
	 * @param attiMainType 态度主体类型 1:答疑的问题；2:答疑的回答；3:计划任务；4:讨论(群组)；5:通知；6:话题；7:心情；8:企业；9:人员；10:相册图片；
	 * @param attiMainId 态度主体id
	 * @param platformModule 平台模块 1:教学；2:竞技场；3:俱乐部；4:个人中心；当态度主体类型为1:答疑的问题；2:答疑的回答；4:讨论(群组)的时候传递该参数
	 * @param actionType 操作类型  1:加精；2:点赞；3:签到；4:置顶；5:取消置顶；6:点踩；
	 * @author ligs
	 * @date 2016年6月26日 下午10:23:35
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/submitAttitude")
	public String submitAttitude(Integer userId,Integer attiMainType,String attiMainId,Integer platformModule,Integer actionType){
		return messageHandleService.submitAttitude( userId, attiMainType, attiMainId, platformModule, actionType).toJSONString();
	}
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.IMessageHandleController#submitDelete(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)    
	 */
	@Override
	/**
	 * submitDelete(对信息主体的删除操作)
	 * @param userId 用户ID
	 * @param actionObjectType 操作对象类型 1:答疑的问题；2:答疑的回答；3:计划任务；4:讨论(群组)；5:班级；6:通知(俱乐部的)；7:话题(俱乐部的)；8:课程；9:学生提交的作业；10:相册；11:相册图片；12:私信   
	 * @param platformModule 平台模块 1:教学；2:竞技场；3:俱乐部；4:个人中心；当操作对象类型为1:答疑的问题；2:答疑的回答；4:讨论(群组)；8:课程的时候传递该参数
	 * @param actionObjectId 操作对象id 多个id以逗号分隔 
	 * @author ligs 
	 * @date 2016年7月4日 上午9:22:16
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/submitDelete")
	public String submitDelete(Integer userId,Integer actionObjectType,Integer platformModule,String actionObjectId){
		return messageHandleService.submitDelete( userId, actionObjectType, platformModule, actionObjectId).toJSONString();
	}
	
}
