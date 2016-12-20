package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.attention.service.CenterAttentionService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ArenaAdDao;
import com.seentao.stpedu.common.dao.ArenaCompetitionDao;
import com.seentao.stpedu.common.entity.ArenaAd;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class ArenaCompetitionService{
	
	@Autowired
	private ArenaCompetitionDao arenaCompetitionDao;
	@Autowired
	private ArenaJoinClubService joinClubService;
	@Autowired
	private CenterAttentionService centerAttentionService;
	@Autowired
	private ArenaAdDao arenaAdDao;
	
	
	public String getArenaCompetition(Integer id) {
		ArenaCompetition arenaCompetition = new ArenaCompetition();
		arenaCompetition.setCompId(id);
		List<ArenaCompetition> arenaCompetitionList = arenaCompetitionDao .selectSingleArenaCompetition(arenaCompetition);
		if(arenaCompetitionList == null || arenaCompetitionList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(arenaCompetitionList);
	}
	
	
	public void insertArenaCompetition(ArenaCompetition arenaCompetition){
		arenaCompetitionDao .insertArenaCompetition(arenaCompetition);
	}
	
	
	
	
	public ArenaCompetition selectArenaCompetition(ArenaCompetition arenaCompetition){
		return  arenaCompetitionDao .selectArenaCompetition(arenaCompetition);
	}

	
	public void updateArenaCompetitionByKey(ArenaCompetition arenaCompetition){
		arenaCompetitionDao .updateArenaCompetitionByKey(arenaCompetition);
		JedisCache.delRedisVal(ArenaCompetition.class.getSimpleName(), String.valueOf(arenaCompetition.getCompId()));
	}
	/**
	 * 
	 * 创建赛事信息
	 * @param userId					用户id
	 * @param gameEventTitle			赛事标标题
	 * @param gameEventLinkId			赛事宣传图片id
	 * @param gameEventStartDate		赛事开始时间
	 * @param gameEventEndDate			赛事结束时间
	 * @param gameEventIntroduce		赛事介绍
	 * @return
	 * @author 							lw
	 * @param club_id 
	 * @date							2016年6月25日  上午10:25:24
	 */

	public JSONObject createArenaCompetition(Integer userId, String gameEventTitle, Integer gameEventLinkId,
			Integer gameEventStartDate, Integer gameEventEndDate, String gameEventIntroduce, Integer club_id) {
		
		try {
			
			//	1. 加入赛事
			ArenaCompetition comp = new ArenaCompetition();
			comp.setTitle(gameEventTitle);
			comp.setImgId(gameEventLinkId);
			comp.setStartTime(gameEventStartDate);
			comp.setEndTime(gameEventEndDate);
			comp.setIntroduce(gameEventIntroduce);
			comp.setClubId(club_id);
			comp.setNotStartNum(0);
			comp.setInProcessNum(0);
			comp.setTotalSignNum(0);
			this.insertArenaCompetition(comp);
			
			//	2. 添加 参赛俱乐部表
			joinClubService.addArenaJoinClub(comp.getCompId(), club_id);
			LogUtil.info(this.getClass(), "createArenaCompetition", GameConstants.SUCCESS_INSERT);
			JSONObject json = Common.getReturn(AppErrorCode.SUCCESS, null);
			JSONObject obj = new JSONObject();
			obj.put("gameEventId", comp.getCompId());
			json.put("result", obj);
			return json;
					
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "createArenaCompetition", AppErrorCode.ERROR_GAME_CREATE_DB , e);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_GAME_CREATE_DB);
			
		}
		
	}


	/**
	 * 修改赛事信息 
	 * @param gameEventId				赛事id
	 * @param gameEventTitle			赛事标标题
	 * @param gameEventLinkId			赛事宣传图片id
	 * @param gameEventStartDate		赛事开始时间
	 * @param gameEventEndDate			赛事结束时间
	 * @param gameEventIntroduce		赛事介绍
	 * @param actionType				操作提交类型
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  下午1:32:20
	 */
	public String updateArenaCompetitionInfo(Integer gameEventId, Integer actionType, Integer gameEventLinkId,
			Integer gameEventEndDate, Integer gameEventStartDate, String gameEventTitle, String gameEventIntroduce) {
		
		//	1. 获取赛事信息
		ArenaCompetition comp = new ArenaCompetition();
		comp.setCompId(gameEventId);
		comp = arenaCompetitionDao.selectArenaCompetition(comp);
		if(comp == null){
			LogUtil.error(this.getClass(), "updateArenaCompetitionInfo", AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM).toJSONString();
					
		}
		
		//	2. 提交赛事图片
		if( GameConstants.GAMEEVENT_TYPE_IMG == actionType){
			
			try {
				
				comp.setImgId(gameEventLinkId);
				this.updateArenaCompetitionByKey(comp);
				
				// 滚动面板上的赛事时修改信息
				ArenaAd arenaAd = new ArenaAd();
				arenaAd.setAdMainId(gameEventId);
				arenaAd = arenaAdDao.selectArenaAd(arenaAd);
				if(arenaAd != null){
					arenaAd.setImgId(gameEventLinkId);
					arenaAdDao.updateArenaAdByKey(arenaAd);
					JedisCache.delRedisMap(null, ArenaAd.class.getSimpleName());
				}
				LogUtil.info(this.getClass(), "submitGameEvent", GameConstants.SUCCESS_INSERT);
				return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
						
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "updateArenaCompetitionInfo", AppErrorCode.ERROR_GAME_UPDATE_DB, e);
				return Common.getReturn(AppErrorCode.ERROR_UPDATE, AppErrorCode.ERROR_GAME_UPDATE_DB).toJSONString();
			}
		
		//	3. 提交赛事基本信息
		}else if( GameConstants.GAMEEVENT_TYPE_BASE == actionType ){
			
			try {
				
				//保存赛事基本信息
				comp.setEndTime(gameEventEndDate);
				comp.setStartTime(gameEventStartDate);
				comp.setTitle(gameEventTitle);
				comp.setIntroduce(gameEventIntroduce);
				this.updateArenaCompetitionByKey(comp);
				LogUtil.info(this.getClass(), "updateArenaCompetitionInfo", AppErrorCode.ERROR_GAME_UPDATE_DB);
				return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
				
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "updateArenaCompetitionInfo", AppErrorCode.ERROR_GAME_UPDATE_DB, e);
				return Common.getReturn(AppErrorCode.ERROR_UPDATE, AppErrorCode.ERROR_GAME_UPDATE_DB).toJSONString();
			}
		}
		
		LogUtil.error(this.getClass(), "updateArenaCompetitionInfo", AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GAME_EVENT_REQUEST_PARAM).toJSONString();
	}


	/**
	 * 获取赛事信息 分页数据组装：从redis中获取数据进行组装
	 * @param en
	 * @param redisTmp
	 * @param pic
	 * @param club
	 * @author 			lw
	 * @param game 
	 * @param json 
	 * @param _type 
	 * @param rich 
	 * @param memberId 
	 * @param att 
	 * @date			2016年7月4日  下午5:09:43
	 */
	public void findDataToRedisByPage(List<ArenaCompetition> compList, List<CenterAttention> attList, String redisTmp, PublicPicture pic, ClubMain club, IGameInterfaceService game, JSONObject json, String _type, JSONObject rich) {
		
		for(ArenaCompetition en : compList){
			
			/*
			 * 	1. 获取比赛信息 ：
			 * 		a.未开始 
			 * 		b.正在进行的比赛数量
			 */
			try {
				redisTmp = game.getNumMatchInfoByCompetitionId(en.getCompId());
				if(!StringUtil.isEmpty(redisTmp)){
					json = JSONObject.parseObject(redisTmp);
					json = json.getJSONObject("result");
					en.setNotStartNum(json.get("noStartMatchNum") != null ? json.getInteger("noStartMatchNum") : 0);
					en.setInProcessNum(json.get("startMatchNum") != null ? json.getInteger("startMatchNum") : 0);
				}else{
					en.setNotStartNum(0);
					en.setInProcessNum(0);
				}
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "findDataToRedisByPage", String.valueOf(AppErrorCode.ERROR_VISIT_GAME_PARAM) , e);
				en.setNotStartNum(0);
				en.setInProcessNum(0);
			}
			
			//	2. 判断是否已经关注
			en.setHasBeenAttention(0);
			if(!CollectionUtils.isEmpty(attList)){
				for(int i = 0 ; i < attList.size() ; i++){
					if(Integer.parseInt(attList.get(i).getRelObjetId()) == en.getCompId().intValue()){
						en.setHasBeenAttention(1);
						attList.remove(i);
						i--;
						break;
					}
				}
				
			}
			
			//	3. 赛事宣传图片链接地址
			redisTmp = RedisComponent.findRedisObjectPublicPicture(en.getImgId(), BusinessConstant.DEFAULT_IMG_MATCH);
			if(!StringUtil.isEmpty(redisTmp)){
				pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
				en.setGameEventLink(Common.checkPic(pic.getFilePath())==true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			}
			
			//	4. 赛事所属俱乐部logo链接地址 ，和俱乐部名称
			redisTmp = RedisComponent.findRedisObject(en.getClubId(), ClubMain.class);
			if(!StringUtil.isEmpty(redisTmp)){
				club = JSONObject.parseObject(redisTmp, ClubMain.class);
				en.setClubName(club.getClubName());
				//redisTmp = RedisComponent.findRedisObjectPublicPicture(en.getClubId(), BusinessConstant.DEFAULT_IMG_CLUB);
				redisTmp = RedisComponent.findRedisObjectPublicPicture(club.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
				if(!StringUtil.isEmpty(redisTmp)){
					pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
					en.setClubLogoLink(Common.checkPic(pic.getFilePath())==true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
				}
			}
			
			//	5. 如果是移动端 处理 赛事文本信息
			if(GameConstants.TYPE_APP.equals(_type) && en.getIntroduce()!=null){
				
				rich = RichTextUtil.parseRichText(en.getIntroduce());
				if(rich != null){
					en.setIntroduce(rich.getString("content"));
					en.setLinks(rich.getJSONArray("links"));
					en.setImgs(rich.getJSONArray("imgs"));
				}else{
					en.setLinks(new JSONArray());
					en.setImgs(new JSONArray());
					
				}
			}
		}
	}

	
	/**
	 * 赛事5种类型分页公用方法
	 * @param limit
	 * @param start
	 * @param paramMap					查询参数
	 * @param queryCountMethodName		查询count方法名
	 * @param queryByPageIdsMethodName	查询page ids 方法名
	 * @author 							lw
	 * @param _type 
	 * @param memberId 
	 * @date							2016年7月4日  下午8:24:03
	 */
	public QueryPage<ArenaCompetition> queryPage(Integer inquireType,Integer limit, Integer start, Map<String, Object> paramMap, String queryCountMethodName, String queryByPageIdsMethodName, String _type, Integer memberId) {
		
		//	1. 分页查询
		QueryPage<ArenaCompetition> page = QueryPageComponent.queryPageByRedisExecute(limit, start, paramMap, ArenaCompetition.class, queryCountMethodName, queryByPageIdsMethodName, null);
		if(page.getState()){
			String 			redisTmp = null;
			PublicPicture 	pic = null;
			ClubMain		club = null;
			JSONObject 		json = null;
			JSONObject 		rich = null;
			CenterAttention att = null;
			
			//	2. 初始化游戏端接口
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			
			//	3. 分页数据组装
			List<Integer> params = new ArrayList<Integer>();
			for(ArenaCompetition en : page.getList()){
				if(inquireType == 5){
					if(en.getGameEventLink() != null){
						en.setGameEventLink(Common.checkPic(en.getGameEventLink()) == true ?en.getGameEventLink()+ActiveUrl.ROTATION_MAP:en.getGameEventLink());	
					}
				}
				else{
					if(en.getGameEventLink() != null){
						en.setGameEventLink(Common.checkPic(en.getGameEventLink()) == true ?en.getGameEventLink()+ActiveUrl.HEAD_MAP:en.getGameEventLink());
					}
				}
				if(en.getClubLogoLink() != null){
					en.setClubLogoLink(Common.checkPic(en.getClubLogoLink()) ==true ?en.getClubLogoLink()+ActiveUrl.HEAD_MAP:en.getClubLogoLink());
				}
				params.add(en.getCompId());
			}
			
			List<CenterAttention> attList = null;
			if(params.size() > 0){
				att = new CenterAttention();
				att.setRelObjetIdIds(StringUtil.ListWithColonToString(params, ","));
				att.setCreateUserId(memberId);
				att.setRelObjetType(GameConstants.ATTENTION_COMPETITION);
				attList = centerAttentionService.getCenterAttentionByCreateUserId(att);
			}
			this.findDataToRedisByPage(page.getList(), attList, redisTmp, pic, club, game, json, _type, rich);
		}
		
		return page;
	}


	/**
	 * 赛事查询，通过赛事id查询某个赛事信息
	 * @param gameEventId
	 * @return
	 * @author 			lw
	 * @param _type 
	 * @param persionId 
	 * @date			2016年7月4日  下午8:31:19
	 */
	public ArenaCompetition queryPageOne(Integer gameEventId, String _type, Integer persionId) {
		//	1. 获取赛事对象
		String redisData = RedisComponent.findRedisObject(gameEventId, ArenaCompetition.class);
		if(StringUtil.isEmpty(redisData)){
			return null;
		}
		ArenaCompetition comp = JSONObject.parseObject(redisData, ArenaCompetition.class);
		
		String 			redisTmp = null;
		PublicPicture 	pic = null;
		ClubMain		club = null;
		JSONObject 		json = null;
		JSONObject 		rich = null;
		CenterAttention att = null;
		
		//	2. 初始化游戏端接口
		IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
		
		//	3. 组装数据
		att = new CenterAttention();
		att.setRelObjetId(comp.getCompId().toString());
		att.setCreateUserId(persionId);
		att.setRelObjetType(GameConstants.ATTENTION_COMPETITION);
		att = centerAttentionService.getSingleCenterAttention(att);
		comp.setHasBeenAttention(att == null ? 0 : 1);
		
		List<ArenaCompetition> compList = new ArrayList<ArenaCompetition>();
		compList.add(comp);
		this.findDataToRedisByPage(compList, null, redisTmp, pic, club, game, json, _type, rich);
		return comp;
	}


	/**
	 * 赛事数据修改
	 * @param comp
	 * @author 			lw
	 * @date			2016年8月24日  下午5:39:50
	 */
	public void updateAddNumber(ArenaCompetition comp) {
		this.arenaCompetitionDao.updateAddNumber(comp);
	}

	
	
	
	
	
}