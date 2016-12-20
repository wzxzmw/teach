package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.ClubMemberMoodDao;
import com.seentao.stpedu.common.entity.ClubMemberMood;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class ClubMemberMoodService{
	
	@Autowired
	private ClubMemberMoodDao clubMemberMoodDao;
	@Autowired
	private ClubMemberService clubMemberService;
	
	public ClubMemberMood getClubMemberMood(ClubMemberMood clubMemberMood) {
		List<ClubMemberMood> clubMemberMoodList = clubMemberMoodDao .selectClubMemberMood(clubMemberMood);
		if(clubMemberMoodList == null || clubMemberMoodList .size() <= 0){
			return null;
		}
		return clubMemberMoodList.get(0);
	}
	
	public void updateClubMemberMoodByKey(ClubMemberMood clubMemberMood){
		clubMemberMoodDao.updateClubMemberMoodByKey(clubMemberMood);
	}
	
	/**
	 * 传入 用户id，俱乐部id，心情内容
	 * 	新增俱乐部会员心情表
	 * @param userId
	 * @param clubId
	 * @param moodContent
	 * @return
	 * @author 			lijin
	 * @date			2016年6月25日  上午10:14:32
	 */
	
	public String addClubMemberMood(String userId,String clubId,String moodContent){
		if(clubMemberService.validateUserAndClubAndLevel(Integer.parseInt(userId), GameConstants.CLUB_PRESIDENT_COACH_MEMBER)){
			LogUtil.info(this.getClass(), "addClubMemberMood", "新增俱乐部会员心情表参数：【userId："+userId+"】，【clubId："+clubId+"】，【moodContent："+moodContent+"】");
			ClubMemberMood clubMemberMood = new ClubMemberMood(Integer.parseInt(clubId),moodContent, Common.getCurrentCreateTime(), Integer.parseInt(userId),0,0,0);
			clubMemberMoodDao.insertClubMemberMood(clubMemberMood);
			LogUtil.info(this.getClass(), "addClubMemberMood", "新增俱乐部会员心情表成功!");
			return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.SUBMIT_MOOD_TYPE_SUCCESS).toJSONString();
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_MOOD_TYPE_NOT_MEMBER).toJSONString();
		}
	}

	/** 
	* @Title: getEveryOneClubMemberMood 
	* @Description: 获取大家的心情
	* @param clubId	俱乐部ID
	* @param userId	用户ID
	* @param startTime	起始时间
	* @param endTime	结束时间
	* @param start	起始位
	* @param limit	每页显示数
	* @return QueryPage<ClubMemberMood>
	* @author liulin
	* @date 2016年6月30日 上午10:41:47
	*/
	public QueryPage<ClubMemberMood> getEveryOneClubMemberMood(Integer clubId, Integer userId, Integer startTime, Integer endTime, Integer start,
			Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", userId);
		paramMap.put("clubId", clubId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubMemberMood> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubMemberMood.class, "queryEveryOneMoodCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubMemberMoodDao.selectEveryOneClubMemberMood(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getMyClubMemberMood 
	* @Description: 获取登录者的心情ID列表
	* @param clubId	俱乐部ID
	* @param userId	用户ID
	* @param startTime	起始时间
	* @param endTime	结束时间
	* @param start	起始位
	* @param limit	每页显示数
	* @return QueryPage<ClubMemberMood>
	* @author liulin
	* @date 2016年6月30日 下午2:15:02
	*/
	public QueryPage<ClubMemberMood> getMyClubMemberMood(Integer clubId, Integer userId, int startTime, int endTime,
			Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", userId);
		paramMap.put("clubId", clubId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubMemberMood> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubMemberMood.class, "queryMyMoodCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubMemberMoodDao.selectMyClubMemberMood(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getMeClubMemberMood 
	* @Description: 个人中心，通过人员id获取某个人员的心情
	* @param clubId	俱乐部ID
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param start	起始数
	* @param limit	每页显示数
	* @return QueryPage<ClubMemberMood>
	* @author liulin
	* @date 2016年6月30日 下午2:35:59
	*/
	public QueryPage<ClubMemberMood> getMeClubMemberMood(Integer userId, Integer memberId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", memberId);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubMemberMood> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubMemberMood.class, "queryMeMoodCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubMemberMoodDao.selectMeClubMemberMood(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}
	
}