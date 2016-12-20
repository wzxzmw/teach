package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.ClubRelRemindMemberDao;
import com.seentao.stpedu.common.dao.ClubRemindDao;
import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;

@Service
public class ClubRemindService{
	
	@Autowired
	private ClubRemindDao clubRemindDao;
	@Autowired
	private ClubRelRemindMemberDao clubRelRemindMemberDao;
	
	public String getClubRemind(Integer id) {
		ClubRemind clubRemind = new ClubRemind();
		List<ClubRemind> clubRemindList = clubRemindDao .selectClubRemind(clubRemind);
		if(clubRemindList == null || clubRemindList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(clubRemindList);
	}

	/** 
	* @Title: getEveryOneClubReminds 
	* @Description: 获取大家的提醒
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页数量
	* @return QueryPage<ClubRelRemindMember>
	* @author liulin
	* @date 2016年6月28日 下午9:34:18
	*/
	public QueryPage<ClubRelRemindMember> getEveryOneClubReminds(Integer userId, Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", userId);
		paramMap.put("clubId", clubId);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubRelRemindMember> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubRelRemindMember.class, "queryRemindMemberCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubRelRemindMemberDao.selectEveryOneClubRemind(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getMeClubReminds 
	* @Description: 我的提醒
	* @param userId
	* @param clubId
	* @param start
	* @param limit
	* @return QueryPage<ClubRelRemindMember>
	* @author liulin
	* @date 2016年6月29日 上午10:53:32
	*/
	public QueryPage<ClubRelRemindMember> getMeClubReminds(Integer userId, Integer clubId, Integer start,
			Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("createUserId", userId);
		paramMap.put("clubId", clubId);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubRelRemindMember> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubRelRemindMember.class, "queryMeRemindMemberCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubRelRemindMemberDao.selectMeClubRemind(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getMyClubReminds 
	* @Description: 提醒我的
	* @param userId
	* @param clubId
	* @param start
	* @param limit
	* @return QueryPage<ClubRelRemindMember>
	* @author liulin
	* @date 2016年6月29日 上午11:21:17
	*/
	public QueryPage<ClubRelRemindMember> getMyClubReminds(Integer userId, Integer clubId, Integer start,Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("clubId", clubId);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubRelRemindMember> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubRelRemindMember.class, "queryMyRemindMemberCount");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubRelRemindMemberDao.selectMyClubRemind(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	public int addRemind(Integer userId, Integer clubId, String remindContent) {
		ClubRemind remind = new ClubRemind();
		remind.setClubId(clubId);
		remind.setContent(remindContent);
		remind.setCreateTime(Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000)));
		remind.setCreateUserId(userId);
		clubRemindDao.insertClubRemind(remind);
		return remind.getRemindId();
	}
}