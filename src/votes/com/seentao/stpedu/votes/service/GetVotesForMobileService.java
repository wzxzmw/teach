
package com.seentao.stpedu.votes.service; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月2日 上午12:10:03
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class GetVotesForMobileService {
	/**
	 * 获取投票选项
	 */
	public String getVotesSomes(Integer userId,Integer start, Integer limit, Integer inquireType,String userName,Integer userType,String userToken){
		return this.getPageByBsVote(userId,limit, start, inquireType,userName,userType,userToken);
	}
	/**  
	 * @param limit 每页数量
	 * @param start 起始页数
	 * @return 返回评选表分页数据
	 */
	protected String getPageByBsVote(Integer userId,Integer limit,Integer start,Integer inquireType,String userName,Integer userType,String userToken){
		Map<String, Object> map = new HashMap<>();
		map.put("limit", limit);
		map.put("start", start);
		
		QueryPage<BsVote> appQueryPage = this.getPageByBsVoteFromInquireType(inquireType, limit, start, map);
		if(CollectionUtils.isEmpty(appQueryPage.getList())){
			JSONObject json = new JSONObject();
			json.put("votes", new JSONArray());
			json.put("returnCount", 0);
			json.put("allPage", 0);
			json.put("currentPage", 0);
			return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
		}
			JSONArray jsonArray = new JSONArray();
			/*
			 *判断投票状态 1、未开始，2、进行中 3、已结束
			 */
			int nowTime = TimeUtil.getCurrentTimestamp();
		for(BsVote bsVote : appQueryPage.getList()){
			JSONObject obj = this.getJsonString(userId,inquireType, bsVote,userName,userType,userToken,nowTime);
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
	protected QueryPage<BsVote> getPageByBsVoteFromInquireType(Integer inquireType,Integer limit,Integer start,Map<String,Object> map){
		QueryPage<BsVote> appQueryPage = new QueryPage<BsVote>();
		switch (inquireType) {
			/*
			 * 评选－》首页，推荐的评选;
			 */
		case 1:
			map.put("inquireType", inquireType);
			return QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
			   
			  /*
			 * 评选－》首页，最热的评选
			 */
		case 2:
			/*
			 * 排序后的评选
			 */
			map.put("inquireType", inquireType);
			return  QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
		case 3:
			/*
			 * 评选－》首页，最新的评选；动态模块，最新的评选；
			 */
			map.put("inquireType", inquireType);
			return  QueryPageComponent.queryPageExecute(limit, start, map, BsVote.class,"queryCountByBsVote","queryByPageBsVote");
			 default :
				 return appQueryPage;
		}
		
	}
	/**参数组装
	 * @param inquireType 查询类型
	 * @param bsVote 评选对象
	 * @return JSONObject对象
	 */
	protected JSONObject  getJsonString(Integer userId,Integer inquireType,BsVote bsVote,String userName,Integer userType,String userToken,int nowTime){
		JSONObject obj = new JSONObject();
		/*
		 * 投票id
		 */
		obj.put("voteId", String.valueOf(bsVote.getVoteId()));
		/*
		 *投票主题 
		 */
		obj.put("voteTitle", bsVote.getVoteTitle());
		
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
		obj.put("voteLogo", pic==null ?"":pic.getFilePath()+ActiveUrl.HEAD_MAP);
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
		/*
		 * 投票选项wap页的链接地址 
		 */
		obj.put("voteOptionUrl", ActiveUrl.CLIENT_IP_URL+"voteDetail?"+"voteId="+bsVote.getVoteId());
		return obj;
	}

}
