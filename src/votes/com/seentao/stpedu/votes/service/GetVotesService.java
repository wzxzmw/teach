
package com.seentao.stpedu.votes.service;

import java.util.HashMap;
import java.util.LinkedList;
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
* @date 创建时间：2016年10月31日 下午4:36:49
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
public class GetVotesService{
	/**评选表*/
	@Autowired
	private BsVoteDao bsVoteDao;
	
	/**获取投票列表信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return 
	 */
	public String getVotesSomes(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType){
		
		return this.getPageByBsVote(limit, start, inquireType,voteId);
	}
	
	/**  
	 * @param limit 每页数量
	 * @param start 起始页数
	 * @return 返回评选表分页数据
	 */
	protected String getPageByBsVote(Integer limit,Integer start,Integer inquireType,Integer voteId){
		Map<String, Object> map = new HashMap<>();
		map.put("limit", limit);
		map.put("start", start);
		
		QueryPage<BsVote> appQueryPage = this.getPageByBsVoteFromInquireType(inquireType, limit, start, map,voteId);
		if(CollectionUtils.isEmpty(appQueryPage.getList())){
			JSONObject json = new JSONObject();
			json.put("votes", new JSONArray());
			json.put("returnCount", 0);
			json.put("allPage", 0);
			json.put("currentPage", 0);
			return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
		}
			JSONArray jsonArray = new JSONArray();
		int nowTime = TimeUtil.getCurrentTimestamp();
		for(BsVote bsVote : appQueryPage.getList()){
			JSONObject obj = this.getJsonString(inquireType, bsVote,nowTime);
			jsonArray.add(obj);
		}
		JSONObject obj = new JSONObject();
		obj.put("votes", jsonArray);
		obj.put("returnCount", appQueryPage.getCount());
		obj.put("allPage", appQueryPage.getAllPage());
		obj.put("currentPage", appQueryPage.getCurrentPage());
		JSONObject result = new JSONObject();
		result.put("result", obj);
		result.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		return result.toJSONString();
	}
	
	/**
	 * @param inquireType
	 * @param limit
	 * @param start
	 * @param map
	 * @return
	 */
	protected QueryPage<BsVote> getPageByBsVoteFromInquireType(Integer inquireType,Integer limit,Integer start,Map<String,Object> map,Integer voteId){
		QueryPage<BsVote> appQueryPage = new QueryPage<BsVote>();
		switch (inquireType) {
			/*
			 * 评选－》首页，推荐的评选;
			 */
		case 1:
			map.put("inquireType", inquireType);
			return QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
			   
			  /*
			 * 评选－》首页，最热的评选 排序动态展示（web前3个/移动端第1个）/最新评选排序优先级：1>2>3
				1、评选开始时间倒序
				2、评选结束时间倒序
				3、评选发起时间倒序
			 */
		case 2:
			map.put("inquireType", inquireType);
			return  QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
		case 3:
			/*
			 * 评选－》首页，最新的评选；动态模块，最新的评选；
			 */
			map.put("inquireType", inquireType);
			return  QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
		case 4:
			/*
			 * 根据投票id获取唯一一条投票信息
			 */
			List<BsVote> bsVotes = new LinkedList<>();
			bsVotes.add(bsVoteDao.queryBsVoteByVoteId(voteId));
			 appQueryPage.setList(bsVotes);
			 return appQueryPage;
			 default :
				 return appQueryPage;
		}
		
	}
	

	/**参数组装
	 * @param inquireType 查询类型
	 * @param bsVote 评选对象
	 * @return JSONObject对象
	 */
	protected JSONObject  getJsonString(Integer inquireType,BsVote bsVote,int nowTime){
		JSONObject obj = new JSONObject();
		/*
		 * 投票id
		 */
		obj.put("voteId", String.valueOf(bsVote.getVoteId()));
		/*
		 *投票主题 
		 */
		obj.put("voteTitle", bsVote.getVoteTitle());
		/*
		 *判断投票状态 1、未开始，2、进行中 3、已结束
		 */
		// 1、如果当前时间小于开始时间 未开始
		if(nowTime<bsVote.getBeginTime()){
			obj.put("voteStatus", 1);
		}
		//2、如果当前时间大于开始时间，并且小于结束时间，进行中
		if(nowTime>bsVote.getBeginTime() && nowTime <bsVote.getEndTime()){
			obj.put("voteStatus", 2);
		}
		// 3、当前时间如果大于结束时间 已结束
		if(nowTime > bsVote.getEndTime()){
			obj.put("voteStatus", 3);
		}
		/*
		 * 图片路径
		 */
		String _pic = RedisComponent.findRedisObjectPublicPicture(bsVote.getImgId(), BusinessConstant.DEFAULT_IMG_CLUB);
		PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
		/*
		 * 投票宣传图片链接地址
		 */
		if(inquireType == 1){
			obj.put("voteLogo", Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.ROTATION_MAP:pic.getFilePath());
		}else{
			obj.put("voteLogo", Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
		}
		/*
		 * 投票开始时间的时间戳
		 */
		obj.put("voteStartDate", String.valueOf(bsVote.getBeginTime()));
		/*
		 * 投票结束时间的时间戳
		 */
		obj.put("voteEndDate", String.valueOf(bsVote.getEndTime()));
		/*
		 * 投票说明
		 */
		obj.put("voteDesc", bsVote.getVoteDesc());
		if(inquireType == 4){
			/*
			 * 投票限制类型1:次；2:次/天；默认为1	
			 */
			obj.put("voteLimitType", bsVote.getVoteLimitType());
			/*
			 * 投票限制数量 默认为1，代表无限制；
			 */
			obj.put("voteLimitCount", bsVote.getVoteLimitNum()==null ?1:bsVote.getVoteLimitNum());
			/*
			 * 投票选择模式 1:单选；2:多选；默认为1；
			 */
			obj.put("voteSelectType", bsVote.getChoosePattern() ==null || bsVote.getChoosePattern()==0?1:bsVote.getChoosePattern()>=1 ? 2:bsVote.getChoosePattern());
			
			/*
			 * 每人最多可投几个选项 当投票选择模式为多选时，需要设置该参数；默认为1；
			 */
			obj.put("perMostVoteChance", bsVote.getChoosePattern() ==null || bsVote.getChoosePattern()==0?1:bsVote.getChoosePattern());
			/*
			 * 已投票总数 已投票总数
			 */
			Set<String> setBsVotes = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(bsVote.getVoteId())), String.valueOf(bsVote.getVoteId()));
			List<BsRelVoteItemUser> listBsRelVotes = JSONArray.parseArray(setBsVotes.toString(), BsRelVoteItemUser.class);
			/*
			 * 查询多少个选项
			 */
			Set<String> voteItem = JedisUserCacheUtils.zrangeByScoreVoteItem(BsVoteItem.class.getSimpleName(),String.valueOf(Integer.valueOf(bsVote.getVoteId())), String.valueOf(bsVote.getVoteId()));
			List<BsVoteItem> bsVoteItems = JSONArray.parseArray(voteItem.toString(),BsVoteItem.class);
			int voteTotalCount =0;
			for(BsVoteItem bsVoteItem:bsVoteItems){
				for(BsRelVoteItemUser bsRelVoteItemUser:listBsRelVotes){
					if(bsRelVoteItemUser.getVoteId().intValue() == bsVoteItem.getVoteId().intValue()&& bsRelVoteItemUser.getItemId().intValue()==bsVoteItem.getItemId().intValue()){
						voteTotalCount+=bsRelVoteItemUser.getVoteTimes();
					}
				}
			}
			//String result = JedisUserCacheUtils.hgetVoteNumber(VoteNumber.class.getSimpleName(),String.valueOf(bsVote.getVoteId()));
			//VoteNumber voteNumber = JSONObject.parseObject(result, VoteNumber.class);
			//obj.put("voteTotalCount", voteNumber == null ? 0 :voteNumber.getCount());
			obj.put("voteTotalCount", voteTotalCount);
			setBsVotes = null;
			listBsRelVotes = null;
			voteItem = null;
			/*
			 * 是否显示已投票总数 1:是；0:否；默认为1；
			 */
			obj.put("isShowingVoteTotalCount", 1);
		}
		return obj;
	}

	public void batchUpdateVoteCount(List<BsVote> list) {
		bsVoteDao.batchUpdateVoteCount(list);
	}
}
