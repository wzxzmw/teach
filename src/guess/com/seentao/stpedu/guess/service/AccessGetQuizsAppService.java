package com.seentao.stpedu.guess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessBet;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaGuessBetService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class AccessGetQuizsAppService {

	@Autowired
	private ArenaGuessBetService betService;
	
	/**
	 * 获取竞猜信息
	 * @param userId		用户id
	 * @param quizTopicId	竞猜话题id
	 * @param start
	 * @param limit
	 * @param _type 
	 * @param memberId    	人员id
	 * @param inquire		查询类型
	 * @return
	 * @author 				lw	
	 * @date				2016年6月27日  下午2:35:50
	 * @updateAuthor   		W.jx
	 */
	public String getQuizs(Integer userId, Integer quizTopicId, Integer start, Integer limit, String _type, Integer memberId, Integer inquireType){
		
		
		QueryPage<ArenaGuess> queryPage = null;
		
		//	1. 分页查询参数容器
		Map<String, Object> paramMap = new HashMap<String, Object>();
				
		//	2. pc端
		if(GameConstants.TYPE_PC.equals(_type) ){
			
			//	2.1 竞猜话题信息列表
			if(GameConstants.GUESS_TOPIC == inquireType){
				paramMap.put("topicId", quizTopicId);
				queryPage = QueryPageComponent.queryPageByRedisSimple(limit, start, paramMap, ArenaGuess.class);
			
			//	2.2 我参加的竞猜列表
			}else if(GameConstants.GUESS_MY == inquireType){
				paramMap.put("memberId", memberId);
				queryPage = QueryPageComponent.queryPageByRedisExecute(limit, start, paramMap, ArenaGuess.class
						, "queryCountMy", "queryByPageidsMy", null);
			}
			
		//	3. 移动端
		}else if(GameConstants.TYPE_APP.equals(_type) ){
			
			//	3.1 某个赛事下的竞猜信息
			if(GameConstants.GUESS_TOPIC_APP == inquireType){
				
				paramMap.put("matchId", quizTopicId);
				queryPage = QueryPageComponent.queryPageByRedisExecute(limit, start, paramMap, ArenaGuess.class, "queryCountApp", "queryByPageidsApp", null);
			
			//	3.2 我参加的竞猜
			}else if(GameConstants.GUESS_MY_APP == inquireType){
				paramMap.put("memberId", memberId);
				queryPage = QueryPageComponent.queryPageByRedisExecute(limit, start, paramMap, ArenaGuess.class
						, "queryCountMy", "queryByPageidsMy", null);
			}
		}
		
		//	4. 查询错误校验
		if(queryPage == null || !queryPage.getState()){
			LogUtil.error(this.getClass(), "getQuizs", AppErrorCode.ERROR_GETQUIZS_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GETQUIZS_REQUEST_PARAM).toJSONString();
		}
		
		//	5. 组装参数
		this.assembleResultParam(queryPage.getList(), userId, inquireType, _type);
		
		//	6. 数据返回
		return queryPage.getMessageJSONObject("quizs").toJSONString();
		
		
		
	}




	
	
	
	
	
	

	/**
	 * 竞猜对象返回参数组装
	 * @param list
	 * @param userId
	 * @author 			lw
	 * @param inquireType 
	 * @param _type 
	 * @date			2016年7月18日  上午11:22:03
	 */
	private void assembleResultParam(List<ArenaGuess> list, Integer userId, Integer inquireType, String _type) {
		String 				redisTmp 		= null;
		ArenaGuessTopic 	topic 			= null;
		CenterUser 			user 			= null;
		PublicPicture		pic				= null;
		List<ArenaGuessBet>	sumBetCount		= null;
		//	1. 初始化游戏端接口
		IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
		
		//	当前投注者是否已经投注 ，当前登录者投注哪方，当前登录者投注数量(虚拟物品的数量) 
		List<Integer> paramList = new ArrayList<Integer>();
		for(ArenaGuess en : list){
			paramList.add(en.getGuessId());
		}
		if(paramList.size() > 0 ){
			sumBetCount = betService.sumGuessBetByGuessId(StringUtil.ListToString(paramList, ","), userId);
		}
		
		//	2. 参数组装
		for(ArenaGuess en :list){
			if(en.getBankerAmount()==null){
				en.setBankerAmount(0.0);
			}
			if(en.getBankerPosition()==null){
				en.setBankerPosition(0);
			}
			if(en.getStatus() ==3){
				en.setStatus(2);
			}
			en.setCreaterId(String.valueOf(en.getCreateUserId()));
			en.setQuizId(String.valueOf(en.getGuessId()));
			en.setTopBettingUserId(String.valueOf(en.getMaxUserId()));
			//	2.1 初始化倍率
			if(en.getOdds() == null){
				en.setOdds(0d);
			}
			
			//	2.2 当前登录用户id
			//en.setUserId(userId);
			en.setUserIdString(String.valueOf(userId));
			
			//	2.3获取竞猜话题
			redisTmp = RedisComponent.findRedisObject(en.getTopicId(), ArenaGuessTopic.class);
			if(!StringUtil.isEmpty(redisTmp)){
				topic = JSONObject.parseObject(redisTmp, ArenaGuessTopic.class);
				en.setQuizStartDate(topic.getCreateTime());
				en.setQuizTopicId(topic.getTopicId());
				en.setQuizTopicTitle(topic.getTitle());
				
				//如果 是移动端 并且 是获取竞猜列表不 返回 赛事名称和赛事id
				if(GameConstants.TYPE_APP.equals(_type) && GameConstants.GUESS_TOPIC_APP == inquireType){
					
				}else{
					
					en.setGameId(topic.getMatchId());
					
					try {
						//	2.3.1 获取赛事名称
						String gameData = game.getBaseMatchInfoByMatchId(topic.getMatchId());
						if(!StringUtil.isEmpty(gameData)){
							JSONObject json = JSONObject.parseObject(gameData);
							json = (JSONObject) json.get("result");
							if(json != null && json.getString("matchName") != null){
								en.setGameTitle(json.getString("matchName"));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			//	2.4 获取竞猜发布者昵称
			redisTmp = RedisComponent.findRedisObject(en.getCreateUserId(), CenterUser.class);
			if(!StringUtil.isEmpty(redisTmp)){
				user = JSONObject.parseObject(redisTmp, CenterUser.class);
				en.setCreaterNickname(user.getNickName());
				
				//	2.4.2 头像链接
				redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(redisTmp)){
					pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
					en.setCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
				}
			}
			
			//	2.5 投注者最多
			redisTmp = RedisComponent.findRedisObject(en.getMaxUserId(), CenterUser.class);
			if(!StringUtil.isEmpty(redisTmp)){
				user = JSONObject.parseObject(redisTmp, CenterUser.class);
				en.setTopBettingNickname(user.getNickName());
				
			}
			
			//	2.6.1 竞猜类型 坐庄 
			if( GameConstants.GUESS_LANDLORD == en.getGuessType()){
				
				//非庄家最大可投数量
				en.setOtherCanBettingCount(Math.floor((en.getBankerAmount()*1000)/(en.getOdds()*1000)));
				en.setIsTheBanker(en.getCreateUserId().intValue() == userId ? GameConstants.GUESS_IS_BANKER : GameConstants.GUESS_NOT_BANKER);
				
			//	2.6.2 竞猜类型 普通 
			}else{
				en.setIsTheBanker(GameConstants.GUESS_NOT_BANKER);
				en.setOtherCanBettingCount(0d);
			}
			
			//	2.7 当前投注者是否已经投注 ，当前登录者投注哪方，当前登录者投注数量(虚拟物品的数量) 
			if(sumBetCount != null){
				for(int i = 0 ; i < sumBetCount.size() ; i++){
					if(sumBetCount.get(i).getGuessId().intValue() == en.getGuessId().intValue()){
						en.setHasBetting(GameConstants.GUESS_IS_BET);
						//能、不能或者双方都投递
						if(GameConstants.GUESS_LANDLORD == en.getGuessType()){
							en.setBettingObject(en.getCreateUserId().equals(userId) ? en.getBankerPosition() : en.getBankerPosition() == GameConstants.GUESS_CAN ? GameConstants.GUESS_CANNOT : GameConstants.GUESS_CAN);
						}else{
							en.setBettingObject(0);
						}
						en.setBettingCount(sumBetCount.get(i).getAmount());
						sumBetCount.remove(i);
						i--;
						break;
					}
				}
			}
			
			if(en.getHasBetting() == null){
				en.setHasBetting(GameConstants.GUESS_NOT_BANKER);
				en.setBettingObject(0);
				en.setBettingCount(0d);
			}
			
		}
		
	}


}
