package com.seentao.stpedu.common.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.CenterLiveDao;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class CenterLiveService{

	@Autowired
	private CenterLiveDao centerLiveDao;
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;

	public CenterLive getCenterLive(CenterLive centerLive) {
		List<CenterLive> centerLiveList = centerLiveDao .selectCenterLive(centerLive);
		if(centerLiveList == null || centerLiveList .size() <= 0){
			return null;
		}

		return centerLiveList.get(0);
	}

	/** 
	 * @Title: addClubCenterLive 
	 * @Description: 添加俱乐部话题动态 
	 * @param topicid	话题ID
	 * @param clubId  俱乐部ID
	 * @return void
	 * @author liulin
	 * @date 2016年6月28日 下午8:24:29
	 */
	public void addTopicClubCenterLive(Integer topicid, Integer clubId, Integer isTop) {
		//如果本次话题为置顶    取消俱乐部动态表其他话题的置顶
		if(isTop == 1){
			//取消动态表话题的置顶
			CenterLive centerLive =new CenterLive();
			centerLive.setLiveType(GameConstants.LIVE_TYPE_TOPIC);//俱乐部话题
			centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_TOPIC);//俱乐部话题
			centerLive.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLUB);//俱乐部动态
			centerLive.setMainObjetId(clubId);//俱乐部id
			centerLive.setIsDelete(GameConstants.NO_DEL);
			centerLive.setIsTop(1);
			centerLive = this.getCenterLive(centerLive);
			if(null != centerLive){
				centerLive.setIsTop(0);
				this.updateCenterLiveByKeyIsTop(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), centerLive.getLiveId().toString());
			}
		}
		CenterLive live = new CenterLive();
		live.setLiveType(GameConstants.LIVE_TYPE_TOPIC);
		live.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_TOPIC);
		live.setLiveMainId(topicid);
		live.setIsTop(isTop);
		Integer createTime = Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000));
		live.setCreateTime(createTime);
		live.setIsDelete(0);
		live.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLUB);
		live.setMainObjetId(clubId);
		centerLiveDao.insertCenterLive(live);
	}

	/** 
	 * @Title: addClubCenterLive 
	 * @Description: 添加俱乐部提醒动态 
	 * @param topicid	话题ID
	 * @param clubId  俱乐部ID
	 * @return void
	 * @author liulin
	 * @date 2016年6月28日 下午8:24:29
	 */
	public void addRemindClubCenterLive(Integer remindId, Integer clubId) {
		CenterLive live = new CenterLive();
		live.setLiveType(GameConstants.LIVE_TYPE_REMIND);
		live.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_REMIND);
		live.setLiveMainId(remindId);
		live.setIsTop(0);
		Integer createTime = Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000));
		live.setCreateTime(createTime);
		live.setIsDelete(0);
		live.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLUB);
		live.setMainObjetId(clubId);
		centerLiveDao.insertCenterLive(live);
	}

	/** 
	 * @Title: addNoticeClubCenterLive 
	 * @Description: 添加俱乐部通知动态
	 * @param noticeId	通知ID
	 * @param clubId  俱乐部ID
	 * @return void
	 * @author liulin
	 * @date 2016年6月29日 下午9:20:50
	 */
	public void addNoticeClubCenterLive(Integer noticeId, Integer clubId, Integer isTop){
		if(isTop == 1){
			//取消动态表通知的置顶
			CenterLive centerLive =new CenterLive();
			centerLive.setLiveType(GameConstants.LIVE_TYPE_NOTICE);//俱乐部通知
			centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_NOTICE);//俱乐部通知
			centerLive.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLUB);//俱乐部动态
			centerLive.setMainObjetId(clubId);//俱乐部id
			centerLive.setIsDelete(GameConstants.NO_DEL);
			centerLive.setIsTop(1);
			centerLive = this.getCenterLive(centerLive);
			if(null != centerLive){
				centerLive.setIsTop(0);
				this.updateCenterLiveByKeyIsTop(centerLive);
				JedisCache.delRedisVal(CenterLive.class.getSimpleName(), centerLive.getLiveId().toString());
			}
		}
		CenterLive live = new CenterLive();
		live.setLiveType(GameConstants.LIVE_TYPE_NOTICE);
		live.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_NOTICE);
		live.setLiveMainId(noticeId);
		live.setIsTop(isTop);
		Integer createTime = Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000));
		live.setCreateTime(createTime);
		live.setIsDelete(0);
		live.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLUB);
		live.setMainObjetId(clubId);
		centerLiveDao.insertCenterLive(live);
	}

	public void insertCenterLive(CenterLive addcenterLive) {
		centerLiveDao.insertCenterLive(addcenterLive);
	}
	/**
	 * 进入俱乐部 产生动态数据
	 * @param clubId
	 * @param userId
	 * @author cxw
	 * @throws ParseException 
	 */
	public void addCenterLive(String clubId,String userId) throws ParseException{
		//加入俱乐部产生动态表记录
		try {
			CenterLive centerLive = new CenterLive();
			centerLive.setLiveType(GameConstants.LIVE_TYPE_JOIN);
			centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_CLUB);
			centerLive.setLiveMainId(Integer.valueOf(clubId));
			centerLive.setIsTop(GameConstants.NO);
			centerLive.setCreateTime(TimeUtil.getCurrentTimestamp());
			centerLive.setIsDelete(GameConstants.NO);
			centerLive.setMainObjetType(GameConstants.LIVE_MAIN_TYPE_MAN);
			centerLive.setMainObjetId(Integer.valueOf(userId));
			centerLiveDao.insertCenterLive(centerLive);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 教学提交问题产生动态
	 * @param questionId	问题id
	 * @param userId		用户id
	 * @author 				lw
	 * @param userId 
	 * @date				2016年6月29日  下午5:04:30
	 */
	public void addClubCenterLiveTeachQuestion(Integer questionId, Integer classId, Integer userId) {
		
		//	1. 关注消息发送
		//centerNewsStatusService.submitCenterNewStatusQuestion(userId);
		centerNewsStatusService.submitOrUpdateSome(userId, 2);
		//	2. 如果关注的人用户消息状态表添加一条记录   给被关注的人发送一条消息
		CenterLive live = new CenterLive();
		live.setCreateTime(TimeUtil.getCurrentTimestamp());
		live.setLiveType(GameConstants.LIVE_TYPE_QUESTION);
		live.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_QUESTION);
		live.setLiveMainId(questionId);
		live.setIsTop(0);
		live.setIsDelete(GameConstants.NO_DEL);
		live.setMainObjetType(GameConstants.MAIN_OBJECT_TYPE_CLASS);
		live.setMainObjetId(classId);
		centerLiveDao.insertCenterLive(live);
		
	}

	/**
	 * 新增动态记录
	 * @param centerLive
	 * @author chengshx
	 */
	public void addCenterLive(CenterLive centerLive){
		centerLiveDao.insertCenterLive(centerLive);
	}

	/** 
	 * @Title: getClubCenterLive 
	 * @Description: 获取俱乐部的动态
	 * @param clubId	俱乐部ID
	 * @param start	起始数
	 * @param limit	每页显示数
	 * @return QueryPage<CenterLive>
	 * @author liulin
	 * @date 2016年7月4日 下午3:37:00
	 */
	public QueryPage<CenterLive> getClubCenterLive(Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mainObjetId", clubId);
		paramMap.put("isDelete", 0);
		paramMap.put("mainObjetType", GameConstants.MAIN_OBJECT_TYPE_CLUB);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterLive> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, CenterLive.class, "queryClubCenterLiveCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(centerLiveDao.selectClubCenterLive(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	 * @Title: getMemberCenterLive 
	 * @Description: 获取人员对应的动态
	 * @param memberId	人员ID
	 * @param start	起始数
	 * @param limit	每页显示数
	 * @return QueryPage<CenterLive>
	 * @author liulin
	 * @date 2016年7月4日 下午8:05:15
	 */
	public QueryPage<CenterLive> getMemberCenterLive(Map<String, Object> paramMap, Integer start, Integer limit) {
		paramMap.put("isDelete", 0);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterLive> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, CenterLive.class, "queryMemberCenterLiveCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(centerLiveDao.selectMemberCenterLive(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	 * @Title: getClassCenterLive 
	 * @Description: 获取动态模块的班级动态
	 * @param classId	班级ID
	 * @param start	起始数
	 * @param limit	每页显示数
	 * @return QueryPage<CenterLive>
	 * @author liulin
	 * @date 2016年7月4日 下午9:23:18
	 */
	public QueryPage<CenterLive> getClassCenterLive(Integer classId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mainObjetId", classId);
		paramMap.put("isDelete", 0);
		paramMap.put("mainObjetType", GameConstants.MAIN_OBJECT_TYPE_CLASS);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterLive> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, CenterLive.class, "queryClassCenterLiveCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(centerLiveDao.selectClassCenterLive(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getLiveClubCenterLive 
	* @Description: 获取动态模块的俱乐部动态
	* @param start	起始数
	* @param limit	每页显示数
	* @return QueryPage<CenterLive>
	* @author liulin
	* @date 2016年7月5日 上午9:03:51
	*/
	public QueryPage<CenterLive> getLiveClubCenterLive(Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDelete", 0);
		paramMap.put("mainObjetType", GameConstants.MAIN_OBJECT_TYPE_CLUB);
		paramMap.put("mainObjetId", clubId);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterLive> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, CenterLive.class, "queryLiveClubCenterLiveCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(centerLiveDao.selectLiveClubCenterLive(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getAttentionUserCenterLive 
	* @Description: 获取指定人的动态
	* @param ids
	* @param start
	* @param limit
	* @return  参数说明 
	* @return QueryPage<CenterLive>
	* @author liulin
	* @date 2016年7月5日 上午9:13:33
	*/
	public QueryPage<CenterLive> getAttentionUserCenterLive(String ids, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDelete", 0);
		paramMap.put("mainObjetIds", ids);
		paramMap.put("mainObjetType", GameConstants.MAIN_OBJECT_TYPE_USER);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<CenterLive> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, CenterLive.class, "queryAttentionUserCenterLiveCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(centerLiveDao.selectAttentionUserCenterLive(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}
	/**
	 * 创建擂台比赛
	 * @author cxw
	 */
	public void CreateGame(){
		CenterLive centerLive = new CenterLive();
		centerLive.setLiveType(GameConstants.LIVE_TYPE_ARENA);
		centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_GAME);
		centerLive.setLiveMainId(2);
		centerLive.setIsTop(GameConstants.NO);
		centerLive.setCreateTime(TimeUtil.getCurrentTimestamp());
		centerLive.setIsDelete(GameConstants.NO);
		centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);
		centerLive.setMainObjetId(2);
		centerLiveDao.insertCenterLive(centerLive);
	}

	/**
	 * 创建实训比赛
	 * @author cxw
	 */
	public void CreateTraining(){
		CenterLive centerLive = new CenterLive();
		centerLive.setLiveType(GameConstants.LIVE_TYPE_PRATICE);
		centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_GAME);
		centerLive.setLiveMainId(2);
		centerLive.setIsTop(GameConstants.NO);
		centerLive.setCreateTime(TimeUtil.getCurrentTimestamp());
		centerLive.setIsDelete(GameConstants.NO);
		centerLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_CLASS);
		centerLive.setMainObjetId(3);
		centerLiveDao.insertCenterLive(centerLive);
	}

	/**
	 * 用户参加比赛
	 * @author cxw
	 */
	public void UserParticipate(){
		CenterLive centerLive = new CenterLive();
		centerLive.setLiveType(GameConstants.LIVE_TYPE_GAME);
		centerLive.setLiveMainType(GameConstants.LIVE_MAIN_TYPE_GAME);
		centerLive.setLiveMainId(2);
		centerLive.setIsTop(GameConstants.NO);
		centerLive.setCreateTime(TimeUtil.getCurrentTimestamp());
		centerLive.setIsDelete(GameConstants.NO);
		centerLive.setMainObjetType(GameConstants.LIVE_MAIN_TYPE_MAN);
		centerLive.setMainObjetId(1);
		centerLiveDao.insertCenterLive(centerLive);
	}

	/** 
	* @Title: getLastCenterLiveByClubId 
	* @Description: 获取俱乐部下的最后动态
	* @param clubId
	* @return CenterLive
	* @author liulin
	* @date 2016年7月24日 上午10:33:08
	*/
	public CenterLive getLastCenterLiveByClubId(Integer clubId) {
		CenterLive centerLive = new CenterLive();
		centerLive.setMainObjetId(clubId);
		centerLive.setIsDelete(0);
		return centerLiveDao.getLastCenterLiveByClubId(centerLive);
	}

	/** 
	* @Title: getLastCenterLiveByIds 
	* @Description: 根据主体用户ID获得最新动态
	* @param ids
	* @return  参数说明 
	* @return CenterLive
	* @author liulin
	* @date 2016年7月24日 下午5:56:28
	*/
	public CenterLive getLastCenterLiveByIds(String ids) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		paramMap.put("isDelete", 0);
		List<CenterLive> centerLives = centerLiveDao.getLastCenterLiveByIds(paramMap);
		if(null == centerLives || centerLives.size() == 0){
			return null;
		}
		return centerLives.get(0);
	}
	
	public void updateCenterLiveByKey(CenterLive centerLive){
		centerLiveDao .updateCenterLiveByKey(centerLive);
	}

	public void updateCenterLiveByKeyAll(CenterLive centerLive) {
		centerLiveDao.updateCenterLiveByKeyAll(centerLive);
	}

	public void updateCenterLiveByKeyIsTop(CenterLive centerLive) {
		centerLiveDao.updateCenterLiveByKeyIsTop(centerLive);
	}
	
	public void deleteCenterLive(CenterLive centerLive){
		centerLiveDao.deleteCenterLive(centerLive);
	}
}