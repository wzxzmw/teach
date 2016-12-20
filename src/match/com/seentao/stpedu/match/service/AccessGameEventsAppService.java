package com.seentao.stpedu.match.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.service.ArenaCompetitionService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class AccessGameEventsAppService {
	@Autowired
	private ArenaCompetitionService competitionService;
	
	/**
	 * 获取赛事信息
	 * @param userId			用户id
	 * @param gameEventId		赛事id
	 * @param searchWord		搜索词
	 * @param start				
	 * @param limit
	 * @param inquireType		查询类型：	1.推荐赛事 2.最热赛事 3.最新赛事 4.我关注俱乐部发布赛事 5.根据赛事id获取唯一赛事信息 6.顶部菜单搜索赛事
	 * @return
	 * @author 					lw
	 * @param _type 
	 * @date					2016年7月4日  下午3:35:57
	 */
	public String getGameEvents(Integer userId, Integer gameEventId, String searchWord, Integer start, Integer limit,Integer inquireType,Integer memberId, String _type) {
		
		//	1. 默认关注赛事
		Integer persionId = userId;
		
		//	2. 请求参数容器
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		
		switch (inquireType) {
		
		/*
		 * 	3.1 推荐赛事信息
		 *  	查询 参赛俱乐部表 （arena_join_club）
		 *  	查询条件： 最多俱乐部参加的赛事降序分页
		 */
		case GameConstants.MATCH_GAMEEVENTS_RECOMMEND:
			return competitionService.queryPage(inquireType,limit, start, paramMap, "queryCountRecommend", "queryByPagReidsRecommend", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
			
		/*
		 * 	3.2 最热赛事信息
		 * 		查询 赛事表：
		 * 		查询条件：最多报名队伍数量降序分页
		 */
		case GameConstants.MATCH_GAMEEVENTS_HOTTEST:
			paramMap.put("orderBy", "total_sign_num");
			return competitionService.queryPage(inquireType,limit, start, paramMap, null, "queryByPagReidsHot", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
			
		/*
		 * 	3.3 最新赛事
		 * 		查询赛事表：
		 *		查询条件：赛事创建时间降序分页
		 */
		case GameConstants.MATCH_GAMEEVENTS_NEWEST:
			paramMap.put("orderBy", "start_time");
			return competitionService.queryPage(inquireType,limit, start, paramMap, null, "queryByPagReidsHot", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
			
		//	3.4 我关注俱乐部发布的赛事
		case GameConstants.MATCH_GAMEEVENTS_CLUB_FOLLOW:
			
			// 加入关注对象
			paramMap.put("userId", memberId);
			
			/*
			 * 	3.4.1 某人关注的赛事
			 * 		查询关注表 关联 赛事表分页查询：
			 * 		查询条件：关注表 的 ：某人、赛事关注类型
			 * 		查询值：关注表： rel_objet_id
			 */
			//if(memberId != null && memberId > 0){
				paramMap.put("type", GameConstants.ATTENTION_COMPETITION);
				return competitionService.queryPage(inquireType,limit, start, paramMap, "queryCountByCenterAttention", "queryByPagReidsCenterAttention", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
		/*		
			 *  我关注的俱乐部的赛事已经被淘汰
			 * 	3.4.2 我关注的赛事
			 * 		查询关注表 关联 赛事表分页查询：
			 * 		查询条件：关注表 的 ：某人、赛事关注类型
			 * 		查询值：赛事表id(comp_id)
			 * 
			 * 	注意：和 某人关注的赛事 查询逻辑不一样!具体看.xml中sql语句
			 
			}else{
				paramMap.put("relObjetType", GameConstants.REL_OBJECT_TYPE_CLUB);
				return competitionService.queryPage(limit, start, paramMap, "queryCountMyFollow", "queryByPagReidsMyFollow", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
			}*/
			
		/*
		 * 	3.5 搜索词搜索赛事
		 * 		查询赛事表：根据赛事titile模糊查询 分页
		 */
		case GameConstants.MATCH_GAMEEVENTS_SEARCH:
			if(!StringUtil.isEmpty(searchWord)){
				paramMap.put("like", searchWord);
			}
			return competitionService.queryPage(inquireType,limit, start, paramMap, "queryCountSearch", "queryByPagReidsSearch", _type, persionId).getMessageJSONObject("gameEvents").toJSONString();
			
		//	3.6 通过赛事id获取赛事对象
		case GameConstants.MATCH_GAMEEVENTS_ID:
			// update by chengshx 2016-11-24
			List<ArenaCompetition> list = new ArrayList<ArenaCompetition>();
			JSONObject json = new JSONObject();
			json.clear();
			json.put("returnCount", 0);
			json.put("allPage", 	0);
			json.put("currentPage", 0);
			if(gameEventId != 0){
				//	3.6.1 根据赛事id获取赛事信息
				ArenaCompetition comp = competitionService.queryPageOne(gameEventId, _type, persionId);
				if(comp == null){
					LogUtil.error(this.getClass(), "getGameEvents", AppErrorCode.ERROR_GET_GAMEEVENTS_NO_MATCH);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_GAMEEVENTS_NO_MATCH).toJSONString();
				}
				/*
				 * 图片压缩
				 */
				String game = comp.getGameEventLink();
				comp.setGameEventLink(game.substring(0, game.length()-4)+ActiveUrl.ROTATION_MAP);
	//			comp.setClubLogoLink(Common.checkPic(comp.getClubLogoLink())== true ?comp.getClubLogoLink()+ActiveUrl.HEAD_MAP:comp.getClubLogoLink());
				//	3.6.2 返回结果拼装
				list.add(comp);
			} 
			json.put("gameEvents", 	list);
			return Common.getReturn(AppErrorCode.SUCCESS, null, json).toJSONString();
			
		default:
			LogUtil.error(this.getClass(), "getGameEvents", AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM).toJSONString();
		}
		
		
		
	}

}
