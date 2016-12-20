package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisAndDBBean;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ArenaGuessTopicDao;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class ArenaGuessTopicService{
	
	@Autowired
	private ArenaGuessTopicDao arenaGuessTopicDao;
	@Autowired
	private ClubMemberService memberService;
	@Autowired
	private ArenaGuessService guessService;
	
	public String getArenaGuessTopic(Integer id) {
		ArenaGuessTopic arenaGuessTopic = new ArenaGuessTopic();
		arenaGuessTopic.setTopicId(id);
		List<ArenaGuessTopic> arenaGuessTopicList = arenaGuessTopicDao .selectSingleArenaGuessTopic(arenaGuessTopic);
		if(arenaGuessTopicList == null || arenaGuessTopicList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(arenaGuessTopicList);
	}
	
	public ArenaGuessTopic selectArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		return arenaGuessTopicDao.selectArenaGuessTopic(arenaGuessTopic);
	}
	
	public void updateArenaGuessTopicByKey(ArenaGuessTopic arenaGuessTopic){
		arenaGuessTopicDao .updateArenaGuessTopicByKey(arenaGuessTopic);
	}
	
	public void insertArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		arenaGuessTopicDao .insertArenaGuessTopic(arenaGuessTopic);
	}
	
	public  List<ArenaGuessTopic> queryByPage(Map<String, Object> paramMap){
		return arenaGuessTopicDao.queryByPage(paramMap);
	}
	

	/**
	 * 竞猜话题 	
	 *  null	:总人数加1
	 * 	true	:竞猜数据库竞猜数量加1
	 * 	false	:竞猜数据库竞猜数量+1，总人数加1
	 * @param quizTopicId
	 * @author 			lw
	 * @param b 
	 * @date			2016年6月24日  下午10:46:14
	 */
	public void addOneGuessNum(Integer quizTopicId, Boolean b) {
		
		//	1.1 竞猜话题人员加1
		if(b == null){
			arenaGuessTopicDao.addPersonNum(quizTopicId);
		
		//	1.2 竞猜数据库竞猜数量+1，总人数加1
		}else if(b){
			arenaGuessTopicDao.addOneGuessNumAndPsersion(quizTopicId);
			
		//	1.3 竞猜数据库竞猜数量加1
		}else{
			arenaGuessTopicDao.addOneGuessNum(quizTopicId);
		}
		
		//	2. 删除缓存
		JedisCache.delRedisVal(ArenaGuessTopic.class.getSimpleName(), String.valueOf(quizTopicId));
	}

	/**
	 * 校验用户是否是该赛事的俱乐部会长
	 * @param compId	赛事id
	 * @param userId	用户id
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  上午11:57:17
	 */
	public boolean checkClubPresident(Integer newGameId, Integer userId) {
		
		//	1. 通过赛事id获取俱乐部对象
		RedisAndDBBean<ClubMain> clubMainRedis = RedisComponent.recursionRedisObjectExecute(newGameId, ClubMain.class, 
				GameConstants.REDIS_AGREEMENT.MATCH,GameConstants.REDIS_AGREEMENT.CLUB);
		if(clubMainRedis.isSuccess()){
			ClubMain club = clubMainRedis.getResultBean();
			if(club == null ){
				return true;
			}
			
			//	2. 校验是否是赛事 俱乐部 会长
			ClubMember member = new ClubMember();
			member.setClubId(club.getClubId());
			member.setUserId(userId);
			member.setLevel(GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT);
			member.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
			member = memberService.checkPresidentAndArenaCompetition(member);
			if(member == null){
				return true;
			}
			
			return false;
		}
		
		return true;
	}


	/**
	 * 关闭竞猜话题
	 * @param actionObjectId	竞猜话题id
	 * @param userId			用户id
	 * @return
	 * @author 					lw
	 * @date					2016年6月28日  上午11:15:16
	 */
	public JSONObject closeArenaGuessTopic(Integer actionObjectId, Integer userId) {
		//	1. 获取竞猜话题
		String topicRedis = RedisComponent.findRedisObject(actionObjectId, ArenaGuessTopic.class);
		if(StringUtil.isEmpty(topicRedis)){
			LogUtil.error(this.getClass(), "finishQuiz",AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_REQUEST_PARAM);
		}
		ArenaGuessTopic topic = JSONObject.parseObject(topicRedis, ArenaGuessTopic.class);
		
		//	2. 通过比赛id获取赛事id
		Integer compId = null;
		try {
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			topicRedis = game.getCompetitionIdByMatchId(topic.getMatchId());
			JSONObject parse = (JSONObject) JSONObject.parse(topicRedis);
			if(parse.getInteger(GameConstants.CODE) == 0){
				compId = ((JSONObject)parse.get("result")).getInteger("competitionId");
			}else{
				LogUtil.error(this.getClass(), "finishQuiz",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			LogUtil.error(this.getClass(), "finishQuiz",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM),e1);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
		}
		
		//	3. 校验是否是俱乐部会长
		if(this.checkClubPresident(compId,userId)){
			LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_FINISHQUIZ_PRESIDENT);
		}
		
		//	4. 结束竞猜话题
		try {
			topic.setStatus(GameConstants.GUESS_END_TOPIC);
			arenaGuessTopicDao.closeArenaGuessTopic(topic);
			
			//	5. 删除缓存
			JedisCache.delRedisVal(ArenaGuessTopic.class.getSimpleName(), String.valueOf(topic.getTopicId()));
			LogUtil.info(this.getClass(), "finishQuiz", String.valueOf(AppErrorCode.SUCCESS));
			return Common.getReturn(AppErrorCode.SUCCESS, null);
					
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "finishQuiz", AppErrorCode.ERROR_FINISHQUIZ_TO_DB);
			return Common.getReturn(AppErrorCode.ERROR_UPDATE, AppErrorCode.ERROR_FINISHQUIZ_TO_DB);
		}
	}

	/**
	 * 分页查询 
	 * @param limit
	 * @param start
	 * @param param
	 * @param class1
	 * @return
	 * @author 			lw
	 * @param idsMap 
	 * @date			2016年7月1日  下午2:39:03
	 */
	public JSONObject queryPage(Map<Integer, String> idsMap, Integer limit, Integer start, Map<String, Object> param) {
		
		//	1. 分页查询
		QueryPage<ArenaGuessTopic> queryPage = QueryPageComponent.queryPageByRedisSimple(limit, start, param, ArenaGuessTopic.class);
		if(queryPage.getState()){
			CenterUser user  = null;
			String	   redis =null;
			JSONObject json = null;
			List<ArenaGuess> maxGuess = null;
			List<ArenaGuess> countGuess = null;
			
			//	2. 初始化游戏端接口
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			
			List<Integer> paramList = new ArrayList<Integer>();
			for(ArenaGuessTopic en : queryPage.getList()){
				paramList.add(en.getTopicId());
			}
			if(paramList.size() > 0 ){
				//获取加入竞猜话题下最大投注者信息
				maxGuess = guessService.findMaxGuess(StringUtil.ListToString(paramList, ","));
				countGuess = guessService.findAllAcountByGuessTopicId(StringUtil.ListToString(paramList, ","));
			}
			//	3. 组装返回参数
			for(ArenaGuessTopic en : queryPage.getList()){
				
				//	4. 比赛名称获取
				if(CollectionUtils.isEmpty(idsMap)){
					
					//	4.1 单独比赛名称获取
					try {
						redis = game.getBaseMatchInfoByMatchId(en.getMatchId());
						if(!StringUtil.isEmpty(redis)){
							json = JSONObject.parseObject(redis);
							json = (JSONObject) json.get("result");
							if(json != null){
								en.setGameTitle(String.valueOf(json.get("matchName")));
							}
						}
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "findGuessTopicEntity",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM), e);
					}
					
				}else{
					//	4.2 加入 比赛标题
					en.setGameTitle(idsMap.get(en.getMatchId()));
				}
				
				//	5. 投入总金额
				if(countGuess != null){
					if(countGuess.size() > 0 && !countGuess.equals("")){
						for(int i = 0 ; i < countGuess.size() ; i ++){
							if(countGuess.get(i).getTopicId().intValue() == en.getTopicId()){
								en.setBettingCount(countGuess.get(i).getSureAmount());
								countGuess.remove(i);
								i--;
								break;
							}
						}
					}else {
						en.setBettingCount(0.0);
					}
				}
				
				//	6. 加入竞猜话题下最大投注者信息
				if(maxGuess != null){
					for(int i = 0 ; i < maxGuess.size() ; i ++){
						if(en.getTopicId().intValue() == maxGuess.get(i).getTopicId().intValue()){

							//	6.1 id
							en.setTopBettingUserId(maxGuess.get(i).getMaxUserId());
							
							//	6.2 投注者昵称
							redis = RedisComponent.findRedisObject(maxGuess.get(i).getMaxUserId(), CenterUser.class);
							if(!StringUtil.isEmpty(redis)){
								user = JSONObject.parseObject(redis, CenterUser.class);
								en.setTopBettingNickname(user.getNickName());
								maxGuess.remove(i);
								i--;
								break;
							}
						}
					}
				}
			}
		}
		
		return queryPage.getMessageJSONObject("quizTopics");
	}

	/**
	 * 根据竞猜话题id 获取竞猜话题
	 * @param quizTopicId	竞猜话题id
	 * @return
	 * @author 				lw
	 * @param idsMap 
	 * @date				2016年7月1日  下午4:27:14
	 */
	public JSONObject findGuessTopicEntity(Integer quizTopicId) {
		
		//	1. 获取竞猜话题
		String redis = RedisComponent.findRedisObject(quizTopicId, ArenaGuessTopic.class);
		if(StringUtil.isEmpty(redis)){
			LogUtil.error(this.getClass(), "findGuessTopicEntity", AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM);
		}
		ArenaGuessTopic topic = JSONObject.parseObject(redis, ArenaGuessTopic.class);
		
		//	2. 比赛id获取比赛标题
		try {
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			redis = game.getBaseMatchInfoByMatchId(topic.getMatchId());
			if(!StringUtil.isEmpty(redis)){
				JSONObject json = JSONObject.parseObject(redis);
				if(json.getInteger(GameConstants.CODE) == 0){
					json = (JSONObject) json.get("result");
					if(json != null){
						topic.setGameTitle(json.getString("matchName"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "findGuessTopicEntity",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
		}
		
		//	3. 加入竞猜话题下最大投注者信息
		List<ArenaGuess> guess = guessService.findMaxGuess(topic.getTopicId().toString());
		if(!CollectionUtils.isEmpty(guess)){
			//	3.1 最大投注者id
			topic.setTopBettingUserId(guess.get(0).getMaxUserId());
			
			//	3.2 最大投注者昵称
			redis = RedisComponent.findRedisObject(guess.get(0).getMaxUserId(), CenterUser.class);
			if(!StringUtil.isEmpty(redis)){
				CenterUser user = JSONObject.parseObject(redis, CenterUser.class);
				topic.setTopBettingNickname(user.getNickName());
			}
			
			//	3.3 话题投入总金额
			List<ArenaGuess> countList = guessService.findAllAcountByGuessTopicId(guess.get(0).getTopicId().toString());
			topic.setBettingCount(countList == null ? 0 : countList.get(0).getSureAmount());
		}
		
		
		
		List<ArenaGuessTopic> list = new ArrayList<ArenaGuessTopic>();
		list.add(topic);
		JSONObject json = new JSONObject();
		json.put("returnCount", 0);
		json.put("allPage", 	0);
		json.put("currentPage", 0);
		json.put("quizTopics", 	list);
		
		return Common.getReturn(AppErrorCode.SUCCESS, null, json);
	}

	
	/**
	 * 查询我的竞猜
	 * @param limit	
	 * @param start
	 * @param param			查询参数
	 * @return
	 * @author 				lw
	 * @date				2016年7月1日  下午4:49:25
	 */
	public JSON findUserByPage(Integer limit, Integer start, Map<String, Object> param) {
		
		//	1. 分页查询
		QueryPage<ArenaGuessTopic> page = QueryPageComponent.queryPageByRedisExecute(limit, start, param, ArenaGuessTopic.class, "queryCountUser", "queryByPageidsUser", "queryByPageUser");
		if(page.getState()){
			CenterUser user  = null;
			String	   redis =null;
			List<Integer> paramList = new ArrayList<Integer>();
			List<ArenaGuess> maxGuess = null;
			List<ArenaGuess> countGuess = null;
			
			for(ArenaGuessTopic en : page.getList()){
				paramList.add(en.getTopicId());
			}
			
			if(paramList.size() > 0){
				String paramStr = StringUtil.ListToString(paramList, ",");
				maxGuess = guessService.findMaxGuess(paramStr);
				countGuess = guessService.findAllAcountByGuessTopicId(paramStr);
			}
			
			
			//	2. 初始游戏端接口
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			
			//	3. 组装返回参数
			for(ArenaGuessTopic en : page.getList()){
				
				//	4. 通过 ArenaGuessTopic.matchId 比赛id获取比赛标题
				try {
					redis = game.getBaseMatchInfoByMatchId(en.getMatchId());
					if(!StringUtil.isEmpty(redis)){
						JSONObject json = JSONObject.parseObject(redis);
						json = (JSONObject) json.get("result");
						if(json.getInteger(GameConstants.CODE) == 0){
							en.setGameTitle(json.getString("matchName"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(this.getClass(), "findUserByPage",String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM));
				}
				
				//	5. 加入竞猜话题下最大投注者信息
				if(maxGuess != null){
					for(int i = 0 ; i < maxGuess.size() ; i ++){
						if(en.getTopicId().intValue() == maxGuess.get(i).getTopicId().intValue()){
							
							//	 5.1 最大投注者id
							en.setTopBettingUserId(maxGuess.get(i).getMaxUserId());
							
							//	 5.2 最大投注者昵称
							redis = RedisComponent.findRedisObject(maxGuess.get(i).getMaxUserId(), CenterUser.class);
							if(!StringUtil.isEmpty(redis)){
								user = JSONObject.parseObject(redis, CenterUser.class);
								en.setTopBettingNickname(user.getNickName());
							}
							maxGuess.remove(i);
							i--;
							break;
						}
					}
				}
				
				//	6. 话题投入总金额
				if(countGuess != null){
					for(int i = 0 ; i < countGuess.size() ; i ++){
						if(en.getTopicId().intValue() == countGuess.get(i).getTopicId().intValue()){
							en.setBettingCount(countGuess.get(i).getSureAmount());
							countGuess.remove(i);
							i--;
							break;
						}
					}
				}
			}
		}
		return page.getMessageJSONObject("quizTopics");
	}
	
	
	
	
	
	
	
	
}