package com.seentao.stpedu.match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.match.service.AccessGameEventsAppService;
import com.seentao.stpedu.match.service.PublishGameEventAppService;
import com.seentao.stpedu.utils.LogUtil;

@Controller
public class GameEventsController implements IGameEventsController{

	
	@Autowired
	private AccessGameEventsAppService appService;
	@Autowired
	private PublishGameEventAppService publishGameEventAppService;
	
	/**
	 * 获取赛事信息
	 * @param userId			用户id
	 * @param gameEventId		赛事id
	 * @param searchWord		搜索词
	 * @param _type				pc/app端
	 * @param start				
	 * @param limit
	 * @param inquireType		查询类型：	1.推荐赛事 2.最热赛事 3.最新赛事 4.我关注俱乐部发布赛事 5.根据赛事id获取唯一赛事信息 6.顶部菜单搜索赛事
	 * @return
	 * @author 					lw
	 * @date					2016年7月4日  下午3:36:14
	 */
	@ResponseBody
	public String getGameEvents(Integer userId, Integer gameEventId, String searchWord, Integer start, Integer limit, Integer inquireType, Integer memberId,String _type){
		
		/*if(memberId == null || inquireType == null){
			LogUtil.error(this.getClass(), "getGameEvents", AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM).toJSONString();
		}*/
		
		return appService.getGameEvents(userId, gameEventId, searchWord, start, limit,inquireType, memberId, _type);
	}
	
	
	
	
	
	
	/**
	 * 提交赛事信息
	 * @param userId					用户id
	 * @param gameEventId				赛事id
	 * @param gameEventTitle			赛事标标题
	 * @param gameEventLinkId			赛事宣传图片id
	 * @param gameEventStartDate		赛事开始时间
	 * @param gameEventEndDate			赛事结束时间
	 * @param gameEventDesc				赛事介绍
	 * @param actionType				操作提交类型
	 * @return
	 * @author 							lw
	 * @date							2016年6月25日  上午9:24:26
	 */
	@ResponseBody
	public String submitGameEvent(Integer userId, Integer gameEventId, String gameEventTitle, Integer gameEventLinkId, 
			Integer gameEventStartDate, Integer gameEventEndDate, String gameEventDesc, Integer actionType){
		if(userId == null ){
			LogUtil.error(this.getClass(), "submitGameEvent", AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM).toJSONString();
		}
		return publishGameEventAppService.submitGameEvent(userId, gameEventId, gameEventTitle, gameEventLinkId, gameEventStartDate, gameEventEndDate, gameEventDesc, actionType);
	}
	
	
}
