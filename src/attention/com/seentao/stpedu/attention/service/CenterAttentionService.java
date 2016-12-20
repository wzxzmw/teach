package com.seentao.stpedu.attention.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.service.ArenaCompetitionService;
import com.seentao.stpedu.common.service.BaseClubMainService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterNewsService;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class CenterAttentionService{
	@Autowired
	private CenterAttentionDao centerAttentionDao;
	
	//俱乐部
	@Autowired
	private BaseClubMainService baseClubMainService;
	
	//用户
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private CenterCompanyService centerCompanyService;
	
	@Autowired
	private CenterLiveService centerLiveService;
	
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	
	@Autowired
	private ArenaCompetitionService arenaCompetitionService;
	
	@Autowired
	private CenterNewsService centerNewsService;
	/**
	 * 
	 * submitAttention(加关注取消关注)   
	 * @param userId 用户id
	 * @param actionType 操作类型 1:加关注；2:取消关注；
	 * @param actionObjectType 操作对象类型 1:关注的俱乐部；2:关注的人；3:关注的企业
	 * @param actionObjectId 操作对象id
	 * @author ligs
	 * @date 2016年6月18日 上午9:52:46
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public  JSONObject submitAttention(Integer userId,Integer actionType, Integer actionObjectType, String actionObjectId){
		//判断actionType 1:加关注；2:取消关注
		if(actionType == GameConstants.WITH_FOCUS_ON){//加关注
			if(actionObjectType == GameConstants.ATTENTION_CLUB){
				//俱乐部
				return this.attentionClub(actionObjectId, userId);
			}else if(actionObjectType == GameConstants.ATTENTION_USER){
				//关注的人
				return this.attentionUser(userId, actionObjectId);
			}else if(actionObjectType == GameConstants.IS_ATTENTION_ENTERPRISE){
				//关注的企业
				return this.attentionCompany(userId, actionObjectId);
			}else if(actionObjectType == GameConstants.ARRENTION_COMPETITION){
				//关注的赛事
				return this.attentionCompetition(userId, actionObjectId);
			}
		}else if(actionType == GameConstants.CANCEL_THE_ATTENTION){//取消关注
			if(actionObjectType == GameConstants.ATTENTION_CLUB){
				//1:关注的俱乐部
				return this.attention(userId, GameConstants.CLUBMAIN, actionObjectId);
			}else if(actionObjectType ==  GameConstants.ATTENTION_USER){
				//2:关注的人
				return this.attention(userId, GameConstants.USER_OBJ, actionObjectId);
			}else if(actionObjectType ==  GameConstants.IS_ATTENTION_ENTERPRISE){
				//关注的企业
				return this.attention(userId, GameConstants.ATTENTION_ENTERPRISE, actionObjectId);
			}else if(actionObjectType ==  GameConstants.ARRENTION_COMPETITION){
				//关注的赛事
				return this.attention(userId, GameConstants.ATTENTION_COMPETITION, actionObjectId);
			}
		}
			LogUtil.info(this.getClass(), "getCenterAttention", "请求参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,AppErrorCode.REQUEST_PARAM_ERROR);
	}
	
	
	/**
	 * 
	 * attention(封装 取消关注)   
	 * @param userId 用户id
	 * @param actionObjectType 
	 * @param actionObjectId 操作对象id
	 * @author ligs
	 * @date 2016年6月19日 下午4:39:25
	 * @return
	 */
	public JSONObject attention(Integer userId,Integer actionObjectType,String actionObjectId){
		CenterAttention centerAttention = new CenterAttention();
		centerAttention.setCreateUserId(userId);
		centerAttention.setRelObjetType(actionObjectType);
		centerAttention.setRelObjetId(actionObjectId);
		//判断是否关注过该对象
		List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(centerAttention);
		if(CollectionUtils.isEmpty(centerAttentions)){
			LogUtil.error(this.getClass(), "getCenterAttention", "未关注过该对象");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERRORENTUSER);
		}
		//取消关注对象 
		centerAttentionDao.deleteCenterAttention(centerAttention);
		if(actionObjectType == 1){
			//获得关注表主键
			CenterAttention entityAttention = new CenterAttention();
			entityAttention.setCreateUserId(userId);
			entityAttention.setRelObjetType(GameConstants.USER_OBJ);//操作对象类型 1.人，2:俱乐部，3:企业。
			entityAttention.setRelObjetId(actionObjectId);
			entityAttention = centerAttentionDao.selectSingleCenterAttention(entityAttention);
			if(entityAttention != null){
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(13);
				centerLive.setLiveMainType(12);
				centerLive.setLiveMainId(entityAttention.getAtteId());
				centerLive.setIsDelete(GameConstants.YES_DEL);
				centerLive.setMainObjetType(1);
				centerLiveService.updateCenterLiveByKeyAll(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), actionObjectId);
			}
		}else if(actionObjectType == 2){
			//查询用户表减去关注俱乐部数量
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			CenterUser isCenterUser = centerUserService.selectAllCenterUsers(centerUser);
			if(isCenterUser != null){
				CenterUser centerUsers = new CenterUser();
				centerUsers.setUserId(userId);
				centerUsers.setAtteClubNum(isCenterUser.getAtteClubNum()-1);
				try {
					centerUserService.updateCenterUserByKey(centerUsers);
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "", "");
					e.printStackTrace();
				}
			}
			//删除用户表redis
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
			//获得关注表主键
			CenterAttention entityAttention = new CenterAttention();
			entityAttention.setCreateUserId(userId);
			entityAttention.setRelObjetType(GameConstants.CLUBMAIN);//操作对象类型 1.人，2:俱乐部，3:企业。
			entityAttention.setRelObjetId(actionObjectId);
			entityAttention = centerAttentionDao.selectSingleCenterAttention(entityAttention);
			if(entityAttention != null){
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(12);
				centerLive.setLiveMainType(11);
				centerLive.setLiveMainId(entityAttention.getAtteId());
				centerLive.setIsDelete(GameConstants.YES_DEL);
				centerLive.setMainObjetType(1);
				centerLiveService.updateCenterLiveByKeyAll(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), actionObjectId);
			}
		}else if(actionObjectType == 3){
			//查询用户表减去关注企业数量
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			CenterUser isCenterUser = centerUserService.selectAllCenterUsers(centerUser);
			if(isCenterUser != null){
				CenterUser centerUsers = new CenterUser();
				centerUsers.setUserId(userId);
				centerUsers.setAtteCompanyNum(isCenterUser.getAtteCompanyNum() - 1);
				//centerUserService.updateCenterUserByKey(centerUsers);
				try {
					centerUserService.updateCenterUserByKey(centerUsers);
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "", "");
					e.printStackTrace();
				}
			}
			//删除用户表redis
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
			//获得关注表主键
			CenterAttention entityAttention = new CenterAttention();
			entityAttention.setCreateUserId(userId);
			entityAttention.setRelObjetType(GameConstants.ATTENTION_ENTERPRISE);//操作对象类型 1.人，2:俱乐部，3:企业。
			entityAttention.setRelObjetId(actionObjectId);
			entityAttention = centerAttentionDao.selectSingleCenterAttention(entityAttention);
			//通过主键删除动态表
			if(entityAttention != null){
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(11);
				centerLive.setLiveMainType(10);
				centerLive.setLiveMainId(entityAttention.getAtteId());
				centerLive.setIsDelete(GameConstants.YES_DEL);
				centerLive.setMainObjetType(1);
				centerLiveService.updateCenterLiveByKeyAll(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), actionObjectId);
			}
		}else if(actionObjectType == 4){//减去关注赛事数量
			//获取关注赛事数量
			ArenaCompetition arenaCompetition = new ArenaCompetition();
			arenaCompetition.setCompId(Integer.valueOf(actionObjectId));
			ArenaCompetition isArenaCompetition = arenaCompetitionService.selectArenaCompetition(arenaCompetition);
			//减去赛事表被关注数量
			ArenaCompetition arenaCompetitions = new ArenaCompetition();
			arenaCompetitions.setCompId(Integer.valueOf(actionObjectId));
			arenaCompetitions.setAttenCompNum(isArenaCompetition.getAttenCompNum()-1);
			arenaCompetitionService.updateArenaCompetitionByKey(arenaCompetitions);
			//sesrvice有删除redsis方法
		}
		LogUtil.info(this.getClass(), "getCenterAttention", "取消关注成功");
		return Common.getReturn(AppErrorCode.SUCCESS, "");
	}


	/** 
	* @Title: getCenterAttentions 
	* @Description: 获得人员关注的企业ID列表
	* @param memberId	人员ID
	* @param start	起始数
	* @param limit	每页显示数
	* @return QueryPage<CenterAttention>
	* @author liulin
	* @date 2016年7月1日 下午12:51:44
	*/
	public QueryPage<CenterAttention> getCenterAttentions(Integer userId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", userId);
		paramMap.put("relObjetType", GameConstants.REL_OBJECT_TYPE_COMPANY);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterAttention> queryPage = QueryPageComponent.queryPageSimpleDefault(limit, start, paramMap, CenterAttention.class);
		if(queryPage.getCount() > 0){
			queryPage.setList(centerAttentionDao.selectCenterAttentions(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}


	/** 
	* @Title: getCenterAttentionByCreateUserId 
	* @Description: 根据创建者ID获取关注对象列表
	* @param attention	加条件的关注对象
	* @return List<CenterAttention>
	* @author liulin
	* @date 2016年7月4日 下午10:19:05
	*/
	public List<CenterAttention> getCenterAttentionByCreateUserId(CenterAttention attention) {
		return centerAttentionDao.selectCenterAttention(attention);
	}


	public CenterAttention getSingleCenterAttention(CenterAttention attention) {
		return centerAttentionDao.selectSingleCenterAttention(attention);
	}


	/** 
	* @Title: getCenterAttentionForMyUser 
	* @Description: 获取我关注的人的ID
	* @param centerAttention
	* @return  参数说明 
	* @return String
	* @author liulin
	* @date 2016年7月24日 下午5:33:59
	*/
	public String getCenterAttentionForMyUser(CenterAttention centerAttention) {
		String ids = "";
		List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(centerAttention);
		if(null == centerAttentions || centerAttentions.size() == 0){
			return null;
		}
		for(CenterAttention attention : centerAttentions){
			ids = ids + attention.getRelObjetId()+",";
		}
		ids = ids.substring(0, ids.length()-1);
		return ids;
	}
	
	public String getCenterAttention(CenterAttention centerAttention){
		List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(centerAttention);
		StringBuffer stringBuffer = new StringBuffer();
		if(!CollectionUtils.isEmpty(centerAttentions)){
			for(CenterAttention attention : centerAttentions){
				stringBuffer.append(attention.getAtteId()+",");
			}	
		}
		return "";
	}

	public Integer attentionCount(Integer valueOf) {
		return centerAttentionDao.attentionCount(valueOf);
	}
	/**获取企业相关信息
	 * @param centerAttention
	 * @return
	 */
	public CenterAttention queryCenterAttentionByType(CenterAttention centerAttention){
		List<CenterAttention> ls = centerAttentionDao.selectCenterAttention(centerAttention);
		if(ls.size()>0){
			return ls.get(0);
			
		}else{
			return null;
		}
	}
	
	
	/**
	 * 关注俱乐部
	 * @return
	 */
	public JSONObject attentionClub(String actionObjectId,Integer userId){
		//判断被关注的俱乐部是否存在 查询俱乐部表
		ClubMain clubMain = new ClubMain();
		clubMain.setClubId(Integer.valueOf(actionObjectId));
		List<ClubMain> isClub= baseClubMainService.selectClubMain(clubMain);
		if(CollectionUtils.isEmpty(isClub)){
			LogUtil.error(this.getClass(), "getCenterAttention", "关注的俱乐部不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_CLUBMAIN);
		}
		//判断是否已经关注过该俱乐部
		CenterAttention isAttention = new CenterAttention();
		isAttention.setCreateUserId(userId);
		isAttention.setRelObjetType(GameConstants.CLUBMAIN);
		isAttention.setRelObjetId(String.valueOf(actionObjectId));
		List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(isAttention);
			if(CollectionUtils.isEmpty(centerAttentions)){
				//获取系统时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "getCenterAttention", "日期转换错误");
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//关注表
				CenterAttention centerAttention = new CenterAttention();
				centerAttention.setCreateUserId(userId);
				centerAttention.setCreateTime(dateTime);//获取时间戳
				centerAttention.setRelObjetType(GameConstants.CLUBMAIN);//操作对象类型 1.人，2:俱乐部，3:企业。
				centerAttention.setRelObjetId(String.valueOf(actionObjectId));
				//添加一条关注数据
				Integer attentionId = centerAttentionDao.insertCenterAttention(centerAttention);
				//查询用户表增加关注俱乐部数量
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(userId);
				CenterUser isCenterUser = centerUserService.selectAllCenterUsers(centerUser);
				if(isCenterUser != null){
					if(isCenterUser.getAtteClubNum() != null){
						CenterUser centerUsers = new CenterUser();
						centerUsers.setUserId(userId);
						centerUsers.setAtteClubNum(isCenterUser.getAtteClubNum()+1);
						//centerUserService.updateCenterUserByKey(centerUsers);
						try {
							centerUserService.updateCenterUserByKey(centerUsers);
						} catch (Exception e) {
							LogUtil.error(this.getClass(), "", "");
							e.printStackTrace();
						}
					}else {
						CenterUser centerUsers = new CenterUser();
						centerUsers.setUserId(userId);
						centerUsers.setAtteClubNum(1);
						//centerUserService.updateCenterUserByKey(centerUsers);
						try {
							centerUserService.updateCenterUserByKey(centerUsers);
						} catch (Exception e) {
							LogUtil.error(this.getClass(), "", "");
							e.printStackTrace();
						}
					}
					
				}
				//删除用户表redis
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
				//动态表添加数据
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(12);
				centerLive.setLiveMainType(11);
				centerLive.setLiveMainId(centerAttention.getAtteId());
				centerLive.setIsTop(0);
				centerLive.setCreateTime(dateTime);
				centerLive.setIsDelete(GameConstants.NO_DEL);
				centerLive.setMainObjetType(1);
				centerLive.setMainObjetId(userId);
				centerLiveService.insertCenterLive(centerLive);
				LogUtil.info(this.getClass(),"getCenterAttention", "关注成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else{
				LogUtil.error(this.getClass(), "getCenterAttention", "已经关注过该俱乐部");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ATTENTCLUBMAIN);
			}
	}
	
	/**
	 * 关注人
	 * @return
	 */
	public JSONObject attentionUser(Integer userId,String actionObjectId){
		//判断被关注的人是否存在
		CenterUser centerUser = centerUserService.getCenterUserById(Integer.valueOf(actionObjectId));
		if(null == centerUser){
			LogUtil.error(this.getClass(), "getCenterAttention", "关注的人不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_USER);
		}
		//判断是否已经关注该对象
		CenterAttention centerAttention = new CenterAttention();
		centerAttention.setCreateUserId(userId);
		centerAttention.setRelObjetType(GameConstants.USER_OBJ);
		centerAttention.setRelObjetId(String.valueOf(actionObjectId));
		List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(centerAttention);
		if(centerAttentions.size() > 0 ){
			LogUtil.error(this.getClass(), "getCenterAttention", "已经关注过该对象");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ATTENTUSER);
		}else{
				try {
					//获取系统时间戳精确到秒
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dates = sdf.format(new Date());
					Date parses = null;
					try {
						parses = sdf.parse(dates);
					} catch (ParseException e) {
						LogUtil.error(this.getClass(), "getCenterAttention", "日期转换错误");
					}
					int dateTime = (int) (parses.getTime() / 1000);
					//关注表实体
					CenterAttention entityAttention = new CenterAttention();
					entityAttention.setCreateUserId(userId);
					entityAttention.setCreateTime(dateTime);//时间戳
					entityAttention.setRelObjetType(GameConstants.USER_OBJ);//操作对象类型 1.人，2:俱乐部，3:企业。
					entityAttention.setRelObjetId(String.valueOf(actionObjectId));
					//添加一条关注数据   返回关注ID
					Integer attentionId = centerAttentionDao.insertCenterAttention(entityAttention);
					//给被关注的人发送一条消息
					/*CenterNewsStatus centerNewsStatusget = new CenterNewsStatus();
					centerNewsStatusget.setUserId(Integer.valueOf(actionObjectId));
					CenterNewsStatus iscenterNewsStatusget = centerNewsStatusService.getCenterNewsStatus(centerNewsStatusget);
					//判断用户消息表是否存在数据  没有则新建消息数据
					if (iscenterNewsStatusget == null) {
						CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
						centerNewsStatus.setUserId(Integer.valueOf(actionObjectId));
						centerNewsStatus.setIsNewAttention(1);
						centerNewsStatus.setIsNewQuestionInvite(0);
						centerNewsStatus.setIsNewGameInvite(0);
						centerNewsStatus.setIsNewPrivateMessage(0);
						centerNewsStatus.setIsNewSysNews(0);
						centerNewsStatusService.insertCenterNewsStatus(centerNewsStatus);
					} else {
						CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
						centerNewsStatus.setUserId(Integer.valueOf(actionObjectId));
						centerNewsStatus.setIsNewAttention(1);
						centerNewsStatusService.updateCenterNewsStatusByKeyAttention(centerNewsStatus);
					}*/
					centerNewsStatusService.submitOrUpdateSome(Integer.valueOf(actionObjectId), 3);
					//获得关注人姓名
					CenterUser centerUsers = centerUserService.getCenterUserById(userId);
					//消息表增加数据
					CenterNews centerNews = new CenterNews();
					centerNews.setCreateTime(dateTime);
					centerNews.setUserId(Integer.valueOf(actionObjectId));
					centerNews.setNewsType(1);
					centerNews.setContent(centerUsers.getNickName() + "关注了你!");
					centerNews.setRelObjectType(1);
					centerNews.setRelObjectId(userId);
					centerNewsService.insertCenterNews(centerNews);
					//动态表添加数据
					CenterLive centerLive = new CenterLive();
					centerLive.setLiveType(13);
					centerLive.setLiveMainType(12);
					centerLive.setLiveMainId(entityAttention.getAtteId());
					centerLive.setIsTop(0);
					centerLive.setCreateTime(dateTime);
					centerLive.setIsDelete(GameConstants.NO_DEL);
					centerLive.setMainObjetType(1);
					centerLive.setMainObjetId(userId);
					centerLiveService.insertCenterLive(centerLive);
					LogUtil.info(this.getClass(), "getCenterAttention", "关注成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "getCenterAttention", "关注失败");
				}
			}
		return null;
	}
	
	/**
	 * 关注企业
	 * @return
	 */
	public JSONObject attentionCompany(Integer userId,String actionObjectId){
		//判断被关注的企业是否存在  不存在添加一条企业数据
		CenterCompany centerCompany = new CenterCompany();
		centerCompany.setCompanyId(actionObjectId);
		CenterCompany iscenterCompany =centerCompanyService.getCenterCompany(centerCompany);
		if(iscenterCompany == null ){
			CenterCompany addcenterCompany = new CenterCompany();
			addcenterCompany.setCompanyId(actionObjectId);
			addcenterCompany.setPraiseNum(0);
			centerCompanyService.insertCenterCompany(addcenterCompany);
			//获取系统时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
				LogUtil.error(this.getClass(), "getCenterAttention", "日期转换错误");
			}
		    int dateTime = (int)(parses.getTime()/1000);
			//关注表实体
			CenterAttention entityAttention = new CenterAttention();
			entityAttention.setCreateUserId(userId);
			entityAttention.setCreateTime(dateTime);//时间戳
			entityAttention.setRelObjetType(GameConstants.ATTENTION_ENTERPRISE);//操作对象类型 1.人，2:俱乐部，3:企业。
			entityAttention.setRelObjetId(actionObjectId);
			//添加一条关注数据
			Integer attentionId =centerAttentionDao.insertCenterAttention(entityAttention);
			//查询用户表增加关注企业数量
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			CenterUser isCenterUser = centerUserService.selectAllCenterUsers(centerUser);
			if(isCenterUser != null){
				if(isCenterUser.getAtteCompanyNum() != null){
					CenterUser centerUsers = new CenterUser();
					centerUsers.setUserId(userId);
					centerUsers.setAtteCompanyNum(isCenterUser.getAtteCompanyNum()+1);
					//centerUserService.updateCenterUserByKey(centerUsers);
					try {
						centerUserService.updateCenterUserByKey(centerUsers);
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "", "");
						e.printStackTrace();
					}
				}else {
					CenterUser centerUsers = new CenterUser();
					centerUsers.setUserId(userId);
					centerUsers.setAtteCompanyNum(1);
					//centerUserService.updateCenterUserByKey(centerUsers);
					try {
						centerUserService.updateCenterUserByKey(centerUsers);
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "", "");
						e.printStackTrace();
					}
				}
			}
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
			//动态表添加数据
			CenterLive centerLive = new CenterLive();
			centerLive.setLiveType(11);
			centerLive.setLiveMainType(10);
			centerLive.setLiveMainId(entityAttention.getAtteId());
			centerLive.setIsTop(0);
			centerLive.setCreateTime(dateTime);
			centerLive.setIsDelete(GameConstants.NO_DEL);
			centerLive.setMainObjetType(1);
			centerLive.setMainObjetId(userId);
			centerLiveService.insertCenterLive(centerLive);
			LogUtil.info(this.getClass(), "getCenterAttention", "关注成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}else {
			//判断是否已经关注该对象
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setCreateUserId(userId);
			centerAttention.setRelObjetType(GameConstants.ATTENTION_ENTERPRISE);
			centerAttention.setRelObjetId(actionObjectId);
			List<CenterAttention> centerAttentions = centerAttentionDao.selectCenterAttention(centerAttention);
			if(centerAttentions.size() >0 ){
				LogUtil.error(this.getClass(), "getCenterAttention", "已经关注过该企业");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ATTENTUSER);
			}else{
				//获取系统时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "getCenterAttention", "日期转换错误");
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//关注表实体
				CenterAttention entityAttention = new CenterAttention();
				entityAttention.setCreateUserId(userId);
				entityAttention.setCreateTime(dateTime);//时间戳
				entityAttention.setRelObjetType(GameConstants.ATTENTION_ENTERPRISE);//操作对象类型 1.人，2:俱乐部，3:企业。
				entityAttention.setRelObjetId(actionObjectId);
				//添加一条关注数据
				centerAttentionDao.insertCenterAttention(entityAttention);
				//查询用户表增加关注企业数量
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(userId);
				CenterUser isCenterUser = centerUserService.selectAllCenterUsers(centerUser);
				if(isCenterUser.getAtteCompanyNum() != null){
					CenterUser centerUsers = new CenterUser();
					centerUsers.setUserId(userId);
					centerUsers.setAtteCompanyNum(isCenterUser.getAtteCompanyNum()+1);
					//centerUserService.updateCenterUserByKey(centerUsers);
					try {
						centerUserService.updateCenterUserByKey(centerUsers);
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "", "");
						e.printStackTrace();
					}
				}else {
					CenterUser centerUsers = new CenterUser();
					centerUsers.setUserId(userId);
					centerUsers.setAtteCompanyNum(1);
					//centerUserService.updateCenterUserByKey(centerUsers);
					try {
						centerUserService.updateCenterUserByKey(centerUsers);
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "", "");
						e.printStackTrace();
					}
				}
				//删除用户表redis
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
				//动态表添加数据
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(11);
				centerLive.setLiveMainType(10);
				centerLive.setLiveMainId(entityAttention.getAtteId());
				centerLive.setIsTop(0);
				centerLive.setCreateTime(dateTime);
				centerLive.setIsDelete(GameConstants.NO_DEL);
				centerLive.setMainObjetType(1);
				centerLive.setMainObjetId(userId);
				centerLiveService.insertCenterLive(centerLive);
				LogUtil.info(this.getClass(), "getCenterAttention", "关注成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}
	}
	
	/**
	 * 关注赛事
	 * @return
	 */
	public JSONObject attentionCompetition(Integer userId,String actionObjectId){
		//判断关注的赛事是否存在
		ArenaCompetition arenaCompetition = new ArenaCompetition();
		arenaCompetition.setCompId(Integer.valueOf(actionObjectId));
		ArenaCompetition isArenaCompetition = arenaCompetitionService.selectArenaCompetition(arenaCompetition);
		if(isArenaCompetition == null){
			LogUtil.error(this.getClass(), "getCenterAttention", "关注的赛事不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_COMPETITION);
		}else {
			//判断是否已经关注该赛事
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setCreateUserId(userId);
			centerAttention.setRelObjetType(GameConstants.ATTENTION_COMPETITION);
			centerAttention.setRelObjetId(actionObjectId);
			CenterAttention centerAttentions = centerAttentionDao.selectSingleCenterAttention(centerAttention);
			if(centerAttentions != null){
				LogUtil.error(this.getClass(), "getCenterAttention", "已经关注过该赛事");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ATTENTUSER_COMPETITION);
			}else{
				//获取系统时间戳精确到秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdf.format(new Date());
			    Date parses = null;
				try {
					parses = sdf.parse(dates);
				} catch (ParseException e) {
					LogUtil.error(this.getClass(), "getCenterAttention", "日期转换错误");
				}
			    int dateTime = (int)(parses.getTime()/1000);
				//关注表
				CenterAttention entityAttention = new CenterAttention();
				entityAttention.setCreateUserId(userId);
				entityAttention.setCreateTime(dateTime);//时间戳
				entityAttention.setRelObjetType(GameConstants.ATTENTION_COMPETITION);//操作对象类型 1.人，2:俱乐部，3:企业。
				entityAttention.setRelObjetId(String.valueOf(actionObjectId));
				//添加一条关注数据
				centerAttentionDao.insertCenterAttention(entityAttention);
				//增加赛事表被关注数量
				ArenaCompetition arenaCompetitions = new ArenaCompetition();
				arenaCompetitions.setCompId(Integer.valueOf(actionObjectId));
				if(isArenaCompetition.getAttenCompNum() != null){
					arenaCompetitions.setAttenCompNum(isArenaCompetition.getAttenCompNum()+1);
				}else {
					arenaCompetitions.setAttenCompNum(1);
				}
				
				arenaCompetitionService.updateArenaCompetitionByKey(arenaCompetitions);
				//service有删除redis
				LogUtil.info(this.getClass(), "submitAttentionForMobile", "关注赛事成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}
	}
}