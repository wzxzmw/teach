package com.seentao.stpedu.guess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaGuessTopicService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class PublishQuizTopicAppService {

	
	@Autowired
	private ArenaGuessTopicService guessTopicService;
	
	/**
	 * 创建竞猜话题
	 * @param userId			用户id
	 * @param quizTopicTitle	竞猜主题
	 * @param quizTopicDesc		竞猜说明
	 * @param gameId			竞赛id
	 * @return
	 * @author 					lw
	 * @date					2016年6月24日  下午8:13:57
	 */
	public String submitQuizTopic(Integer userId, String quizTopicTitle, String quizTopicDesc, Integer gameId) {
		
		//	1. 向 比赛发送一个gameId (比赛id) ，比赛回复一个赛事id
		Integer newGameId = null;
		try {
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			String gameData = game.getCompetitionIdByMatchId(gameId);
			if(!StringUtil.isEmpty(gameData)){
				JSONObject json = JSONObject.parseObject(gameData);
				json = (JSONObject) json.get("result");
				if(json != null && json.getInteger("competitionId") != null){
					newGameId = json.getInteger("competitionId");
				}else{
					LogUtil.error(this.getClass(), "getQuizTopic",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM)).toJSONString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "getQuizTopic",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM), e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM)).toJSONString();
		}
		
		//	2. 校验用户是否是队长
		if(guessTopicService.checkClubPresident(newGameId,userId)){
			LogUtil.error(this.getClass(), "checkClubPresident", AppErrorCode.ERROR_GUESS_POWER);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GUESS_POWER).toJSONString();
		};
			
		//	3. 加入竞猜话题到数据库
		try {
			ArenaGuessTopic guess = new ArenaGuessTopic();
			guess.setMatchId(gameId);
			guess.setTitle(quizTopicTitle);
			guess.setContent(quizTopicDesc);
			guess.setCreateTime(TimeUtil.getCurrentTimestamp());
			guess.setGuessNum(0);
			guess.setStatus(GameConstants.GUESS_START_TOPIC);
			guess.setJoinNum(0);
			guess.setCompId(newGameId);
			guessTopicService.insertArenaGuessTopic(guess);
			LogUtil.info(this.getClass(), "submitQuizTopic", GameConstants.SUCCESS_INSERT);
			return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "submitQuizTopic", String.valueOf(AppErrorCode.ERROR_INSERT), e);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_ARENAGUESS_INSERT).toJSONString();
		}
		
	}
	
	
	
	
	

}
