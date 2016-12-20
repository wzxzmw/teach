package com.seentao.stpedu.guess.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaGuessTopicService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class PublishGetQuizTopicAppService {

	
	
	@Autowired
	private ArenaGuessTopicService topicService;
	
	/**
	 * 获取竞猜话题信息
	 * @param userId		用户id
	 * @param gameEventId	赛事id
	 * @param quizTopicId	竞猜话题id
	 * @param start			
	 * @param limit
	 * @param inquireType	查询类型
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:09:48
	 */
	public String getQuizTopic(Integer userId, Integer gameEventId, Integer quizTopicId, 
			Integer start, Integer limit, Integer inquireType) {
		
		//	1. 初始化分页参数容器
		Map<String, Object> param = new HashMap<String, Object>();
		
		switch (inquireType) {
		
		/*
		 * 	2.1 最热竞猜话题
		 */
		case GameConstants.QUIZ_TOPIC_TYPE_HOT:
			
			//	2.1.1 请求游戏端当前赛事下所有的比赛id以及比赛名称
			Map<Integer,String> idsMap = new HashMap<Integer,String>();
			if(gameEventId!= null && gameEventId > 0 ){
				
				//	2.1.2 根据游戏id获取所有的比赛id和比赛名称
				try {
					IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
					String redisDate = game.getMatchIdsByCompetitionId(gameEventId);
					if(!StringUtil.isEmpty(redisDate)){
						JSONObject json = JSONObject.parseObject(redisDate);
						JSONArray aArr =JSONArray.parseArray(json.get("result").toString());
						for(Object en : aArr){
							json = (JSONObject) en;
							idsMap.put(Integer.valueOf(json.get("gameId").toString()), json.get("gameName").toString());
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					LogUtil.error(this.getClass(), "getQuizTopic",String.valueOf(AppErrorCode.ERROR_GET_MATCH));
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, String.valueOf(AppErrorCode.ERROR_GET_MATCH)).toJSONString();
				}
				
				//	2.1.3 校验赛事id 和赛事标题
				if(CollectionUtils.isEmpty(idsMap)){
					LogUtil.error(this.getClass(), "getQuizTopic", AppErrorCode.ERROR_GET_MATCH);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_MATCH).toJSONString();
				}
				
				//	2.1.4 组装成查询比赛ids
				param.put("matchIds", StringUtil.ListToString(idsMap.keySet(), ","));
				
			}
			
			param.put("orderBy", "join_user_num DESC,create_time");
			
			//	2.1.5 分页查询
			return topicService.queryPage(idsMap, limit, start, param).toJSONString();
			
		/*
		 * 	2.2 最新竞猜话题
		 */
		case GameConstants.QUIZ_TOPIC_TYPE_NEW:
			param.put("orderBy", "create_time");
			return topicService.queryPage(null, limit, start, param).toJSONString();
			
		/*
		 * 	2.3 我的竞猜话题
		 */
		case GameConstants.QUIZ_TOPIC_TYPE_MY:
			param.put("userId", userId);
			return topicService.findUserByPage(limit, start, param).toJSONString();
			
		/*
		 * 	2.4 竞猜话题id查询对象
		 */
		case GameConstants.QUIZ_TOPIC_TYPE_INFO:
			return topicService.findGuessTopicEntity(quizTopicId).toJSONString();

		default:
			LogUtil.error(this.getClass(), "getQuizTopic", AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM).toJSONString();
		}
		
		
		
	}

	
}
