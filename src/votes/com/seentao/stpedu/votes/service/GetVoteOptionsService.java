
package com.seentao.stpedu.votes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.BsVoteDao;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.common.entity.BsVoteItem;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午8:48:01
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
public class GetVoteOptionsService implements IGetVoteOptionService {
	@Autowired
	private BsVoteDao bsVoteDao;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.votes.service.IGetVoteOptionService#getVoteOptions(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String getVoteOptions(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType){
		
		return this.getPageByVoteOptions(userId, limit, start, inquireType, voteId);
	}
	
	/**
	 * @param userId
	 * @param limit
	 * @param start
	 * @param inquireType
	 * @param voteId
	 * @return
	 */
	protected String getPageByVoteOptions(Integer userId,Integer limit,Integer start,Integer inquireType,Integer voteId){
		Map<String, Object> map = new HashMap<>();
		map.put("limit", limit);
		map.put("start", start);
		QueryPage<BsVoteItem> appQueryPage = this.getVoteOptionsFromInquireType(userId, voteId, start, limit, inquireType, map);
		if(CollectionUtils.isEmpty(appQueryPage.getList())){
			JSONObject json = new JSONObject();
			json.put("voteOptions", new JSONArray());
			json.put("returnCount", 0);
			json.put("allPage", 0);
			json.put("currentPage", 0);
			json.put("isShowingVoteOptionCount", 0);
			return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
		}
		JSONArray jsonArray = new JSONArray();
		/*
		 * 根据投票查询投票选项
		 */
		BsVote bsVote = bsVoteDao.queryBsVoteByVoteId(voteId);
		/*
		 * 根据投票voteId查询投票选项是否被投票
		 */
		Set<String> sets = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
		List<BsRelVoteItemUser> listBsVotes = JSONArray.parseArray(sets.toString(), BsRelVoteItemUser.class);
		//当天的起始时间
		long toDayStartTime = TimeUtil.getToDayStartTime();
		//当天的结束时间
		long toDayEndTime = TimeUtil.getToDayEndTime();
		/*
		 * 当前的系统时间
		 */
		long nowTime = TimeUtil.getCurrentTimes();
		for(BsVoteItem bsVoteItem : appQueryPage.getList()){
			JSONObject obj = this.getJsonString(inquireType, bsVoteItem,userId,bsVote,listBsVotes,toDayStartTime,toDayEndTime,nowTime);
			jsonArray.add(obj);
		}
		JSONObject obj = new JSONObject();
		obj.put("voteOptions", jsonArray);
		obj.put("returnCount", appQueryPage.getCount());
		obj.put("allPage", appQueryPage.getAllPage());
		obj.put("currentPage", appQueryPage.getCurrentPage());
		JSONObject result = new JSONObject();
		result.put("result", obj);
		result.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		return result.toJSONString();
	}
	
	/**
	 * @param userId
	 * @param voteId
	 * @param start
	 * @param limit
	 * @param inquireType
	 * @param map
	 * @return
	 */
	protected QueryPage<BsVoteItem> getVoteOptionsFromInquireType(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType,Map<String,Object> map){
		
		QueryPage<BsVoteItem> appQueryPage = new QueryPage<BsVoteItem>();
		switch (inquireType) {
		/*
		 * 根据投票id获取投票选项信息列表
		 */
		case 1:
			map.put("voteId", voteId);
			return QueryPageComponent.queryPageExecute(limit, start, map, BsVoteItem.class,"queryBsVoteItemsByCount","queryBsVoteItemsByItemIds");
		default:
			return appQueryPage;
		}
	}
	
	/**参数组装
	 * @param inquireType
	 * @param bsVoteItem
	 * @return
	 */
	protected JSONObject getJsonString(Integer inquireType,BsVoteItem bsVoteItem,Integer userId,BsVote bsVote,List<BsRelVoteItemUser> ls,long toDayStartTime,long toDayEndTime,long nowTime){
		JSONObject obj = new JSONObject();
		/*
		 * 投票选项id
		 */
		obj.put("voteOptionId", String.valueOf(bsVoteItem.getItemId()));
		/*
		 * 投票选项标题
		 */
		obj.put("voteOptionTitle", bsVoteItem.getItemName());
		/*
		 * 投票选项宣传图片链接地址
		 */
		String _pic = RedisComponent.findRedisObjectPublicPicture(bsVoteItem.getImgId(), BusinessConstant.DEFAULT_IMG_CLUB);
		PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
		/*
		 * 压缩图片
		 */
		obj.put("voteOptionLogo", Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
		/*
		 * 投票选项说明
		 */
		obj.put("voteOptionDesc", bsVoteItem.getItemDesc());
		/*
		 *  每个投票选项的已投票总数
		 */
		if(ls.size()==0){
			obj.put("voteOptionTotalCount",0);
		}
		long total = 0;
		for(BsRelVoteItemUser bsRelVoteItemUser : ls){
			if(bsRelVoteItemUser.getItemId().intValue()== bsVoteItem.getItemId().intValue()){
				total+=bsRelVoteItemUser.getVoteTimes();
			}
		}
		obj.put("voteOptionTotalCount", total);
		/**
		 *  当前登录者是否能对当前投票选项进行投票（对当前选项的投票机会还没有用完）
		 *  当前用户:1、如果选择模式为单选，并且活动周期以次/天 ,当天只能投选一项，第二天还能继续投票,
		 *         2、如果选择模式为单选。并且以活动周期结束为准，整个周期只能选择一次，
		 *         3、如果选择模式为多选，并且活动周期以次/天为标准，当天能投多个选项，每个选项只能投一次一天，第二天仍然能投
		 *         4、如果选择模式为多选，并且活动周期以结束为标准，整个周期内，只能投多选项数的项。每个选项只能投一次
		 */
		/*
		 * 当投票选项没有投票时,则表示都可以投票,无论是单选还是多选都能投票
		 */

		//2、如果当前时间大于开始时间，并且小于结束时间，进行中
		if(nowTime>bsVote.getBeginTime() && nowTime <bsVote.getEndTime()){
			if(CollectionUtils.isEmpty(ls)){
				obj.put("canVote", 1);
			}
		}else if(nowTime<bsVote.getBeginTime()){
			obj.put("canVote", 0);
		}else if(nowTime > bsVote.getEndTime()){
			obj.put("canVote", 0);
		}
		/*
		 * 满足后台运营管理 没有UsrId
		 */
		if(userId != null){
			for(BsRelVoteItemUser bsRelVoteItemUser:ls){
				/*
				 * 投票选项itemId和投票选项和投票关联的itemId相等，并且投票voteId相等，并且当前userId和投票选项和投票关联的userId也相等，表示已经投票
				 */
					 /* 当前用户:1、如果选择模式为单选，并且活动周期以次/天 ,表示每天只能投一次,当天只能投选一项，第二天还能继续投票
					  * 
					  */
					if(bsVote.getChoosePattern() == 0 && bsVote.getVoteLimitType() == 2 ){
							if(bsVoteItem.getItemId().equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
								if(toDayStartTime>bsRelVoteItemUser.getVoteTime()){
									obj.put("canVote", 1);
								}
								if(toDayStartTime <bsRelVoteItemUser.getVoteTime() && bsRelVoteItemUser.getVoteTime() < toDayEndTime){
									obj.put("canVote", 0);
								}
							}
							
						}
					
					/*
					 * 2、如果选择模式为单选。并且以活动周期结束为,表示整个周期只能投一次,整个周期只能选择一次，
					 *  
					 */
					
					if(bsVote.getChoosePattern() == 0 && bsVote.getVoteLimitType() == 1){
						
						if(bsVoteItem.getItemId().equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							obj.put("canVote", 0);
						}else{
							obj.put("canVote", 1);
						}
					}
						
					 /*
					  *  3、如果选择模式为多选，并且活动周期以次/天为标准，当天能投多个选项，每个选项只能投一次一天，第二天仍然能投
					  */
					 
					if(bsVote.getChoosePattern()>=1 && bsVote.getVoteLimitType() ==2 ){
						if(bsVoteItem.getItemId().equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							//如果当前起始时间比投票创建时间大，而且结束时间比投票创建时间小 表示以前投票，
							if(toDayStartTime>bsRelVoteItemUser.getVoteTime() && bsVote.getEndTime()>bsRelVoteItemUser.getVoteTime()){
								obj.put("canVote", 1);
							}
							// 如果投票创建时间比当天的起始时间大，但是比当天的结束时间小，而且投票时间小于结束时间
							else if(toDayStartTime <bsRelVoteItemUser.getVoteTime() && bsRelVoteItemUser.getVoteTime() < toDayEndTime && bsVote.getEndTime()>bsRelVoteItemUser.getVoteTime() ){
								obj.put("canVote", 0);
							}
						}
					}
					  
					 /* 
					  * 4、如果选择模式为多选，并且活动周期以结束为标准，整个周期内，只能投多选项数的项。每个选项只能投一次
					  */
					 
					if(bsVote.getChoosePattern()>=1 && bsVote.getVoteLimitType() ==1){
						if(bsVoteItem.getItemId().equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							obj.put("canVote", 0);
						}else{
							obj.put("canVote", 1);
						}
					}
			}	
		}


		return obj;
	}
}
