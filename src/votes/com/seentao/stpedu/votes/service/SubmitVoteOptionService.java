
package com.seentao.stpedu.votes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.BsVoteDao;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.common.entity.BsVoteItem;
import com.seentao.stpedu.common.entity.VoteNumber;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月2日 上午12:35:06
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Service
public class SubmitVoteOptionService {
	/*评选*/
	@Autowired
	private BsVoteDao bsVoteDao;
     
	public static List<String> common = new ArrayList<>();
	/**投票相关信息直接存放redis中
	 * @param userId 用户userId
	 * @param voteId 投票voteId
	 * @param voteOptionIds 投票选项itemId
	 * @return
	 */
	public String SubmitVoteRedisBySomeIds(Integer userId,String voteId,String voteOptionIds){
		JSONObject jsonObject = new JSONObject();
		/*
		 * 组装hashKey
		 */
		String[] voteItemIds = voteOptionIds.split(",");
		/*
		 * 存放hash
		 */
		Map<String,String> map = new HashMap<>();
		/*
		 * 存入zset中
		 */
		Map<String,Double> maps = new HashMap<>();
		String[] fields = new String[voteItemIds.length];
		//当天的起始时间
		long toDayStartTime = TimeUtil.getToDayStartTime();
		//当天的结束时间
		long toDayEndTime = TimeUtil.getToDayEndTime();
		for(int i=0;i<voteItemIds.length ; i++){
			/*
			 * 拼接hashKey
			 */
			fields[i] = userId+""+voteId+""+voteItemIds[i];
			/*
			 * 封装BsRelVoteItemUser对象
			 */
			BsRelVoteItemUser bsRelVoteItemUser = new BsRelVoteItemUser(fields[i],Integer.valueOf(voteItemIds[i]), userId, Integer.valueOf(voteId), new Long(TimeUtil.getCurrentTimes()).intValue(), 1);
			/*
			 * 对象转换为json字符串,并且存放到map中
			 */
			map.put(fields[i], JSON.toJSONString(bsRelVoteItemUser));
			/*
			 * 存放zset中
			 */
			maps.put(JSON.toJSONString(bsRelVoteItemUser), Double.valueOf(voteId));
		}
		
		/*
		 * 根据投票voteId查询投票主题相关信息
		 */
		BsVote bsVote = bsVoteDao.queryBsVoteByVoteId(Integer.valueOf(voteId));
		/*
		 * 校验在投票结束后，当当前用户还停留在投票界面中，判断是否还能继续投票
		 */
		long systemTime = TimeUtil.getCurrentTimes();
		if(new Long(bsVote.getEndTime())< systemTime){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_END).toJSONString();
		}
		/*
		 * 取出当前投票下所有的项
		 */
		Set<String> setVoteItem = JedisUserCacheUtils.zrangeByScoreVoteItem(BsVoteItem.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(Integer.valueOf(voteId)));
		List<BsVoteItem> listBsVoteItems = JSONArray.parseArray(setVoteItem.toString(), BsVoteItem.class);
		/*
		 * 查询当前评选下已经投票项
		 */
		Set<String> sets = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
		List<BsRelVoteItemUser> setsBsRels = JSONArray.parseArray(sets.toString(), BsRelVoteItemUser.class);
		
		/*
		 * 根据投票hashkey查询当前选项是否被投票
		 */
		List<String> stringBsRels = JedisUserCacheUtils.hmgetVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), fields);
		common.add(null);
		stringBsRels.removeAll(common);
		List<BsRelVoteItemUser> isBsRelVoteItemUser = JSONArray.parseArray(stringBsRels.toString(), BsRelVoteItemUser.class);
		/**公共list*/
		List<BsRelVoteItemUser> lstBsRelVoteUser = null;
		/*
		 *判断当前评选项下是否已经有投票，当没有任何投票，则实现添加，无论单选还是多选 
		 */
		if(setsBsRels.size()==0 ){
			
			this.addVoteNumAndItem(map, bsVote, maps);
			/*
			 * 添加次數
			 */
			this.addRedisVoteNumber(bsVote);
//			JedisUserCacheUtils.hsetVoteNumber(VoteNumber.class.getSimpleName(), String.valueOf(bsVote.getVoteId()), JSON.toJSONString(new VoteNumber(bsVote.getVoteId(), 1)));
			/*
			 * 查询投票后是否当前用户还能继续投票
			 */
			Set<String> sts = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
			lstBsRelVoteUser = JSONArray.parseArray(sts.toString(), BsRelVoteItemUser.class);
			/**校验是否能投票*/
			this.canVote(jsonObject, bsVote, lstBsRelVoteUser,voteItemIds,userId,toDayStartTime,toDayEndTime);
			return Common.getReturn(AppErrorCode.SUCCESS,"",jsonObject).toJSONString();
		}
		/*当前登录者是否能对本次提交的这些投票选项继续进行投票（对提交的这些选项的投票机会还没有用完）
		 * 如果为单选模式，
		 */
		if(bsVote.getChoosePattern() == 0){
			/*
			 * 针对当前如果每天只能投一次
			 */
			for(String str :voteItemIds){
				/*所有的评选项*/
				for(BsVoteItem bsVoteItem:listBsVoteItems){
					/*如果当前评选项属于该属于该评选*/
					if(bsVoteItem.getItemId().equals(Integer.valueOf(str)) && Integer.valueOf(voteId).equals(bsVoteItem.getVoteId())){
						/*setsBsRels表示已经投票*/
						for(BsRelVoteItemUser bsRelVoteItemUser :setsBsRels){
							if(Integer.valueOf(voteId).equals(bsVoteItem.getVoteId())&& userId.equals(bsRelVoteItemUser.getUserId())){
								if(bsVote.getVoteLimitType() ==2){
									if(toDayStartTime < bsRelVoteItemUser.getVoteTime() && bsRelVoteItemUser.getVoteTime() < toDayEndTime){
										return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
									}
								}
								if(bsVote.getVoteLimitType() == 1){
									if(Integer.valueOf(bsVote.getBeginTime())<Integer.valueOf(bsRelVoteItemUser.getVoteTime())&& Integer.valueOf(bsRelVoteItemUser.getVoteTime())<Integer.valueOf(bsVote.getEndTime())){
										return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
									}
								}
							}
						}
					}
					
				}
			}
			/*
			 * isBsRelVoteItemUser 根据投票hashkey查询当前选项是否被投票
			 */
		
			JSONObject obj = this.canVote(jsonObject, bsVote, isBsRelVoteItemUser, voteItemIds, userId, toDayStartTime, toDayEndTime);
			/*
			 * 表示当前投票选项没有投票过，并且也属于当前voteId
			 */
			String  canVote = obj.get("canVoteForVoteOptions").toString();
			if(canVote.contains("0")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
			}
					
			if(isBsRelVoteItemUser.size()==0){
				
				this.addVoteNumAndItem(map, bsVote, maps);
			}else{
				/*
				 * 实现批量添加
				 */
				this.addRedisVote(isBsRelVoteItemUser,stringBsRels);
			}
			/*
			 * 添加投票次数
			 */
			this.addRedisVoteNumber(bsVote);
			/*
			 * 查询投票后是否当前用户还能继续投票
			 */
			Set<String> setsBs = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
			lstBsRelVoteUser = JSONArray.parseArray(setsBs.toString(), BsRelVoteItemUser.class);
			this.canVote(jsonObject, bsVote, lstBsRelVoteUser,voteItemIds,userId,toDayStartTime,toDayEndTime);
			return Common.getReturn(AppErrorCode.SUCCESS,"",jsonObject).toJSONString();
		}
		/*查询当前人在多选中已经投多少次 判断当前天的已投时间
		 * 查询出已经投票的投票对象
		 */
		int total =0;
		for(BsRelVoteItemUser bsRelVoteItemUser :setsBsRels){
			/**判断投票时间是否大于当天的起始时间。并且小于当天的结束时间 多选并且是 */
			if(bsVote.getChoosePattern()>=1 && bsVote.getVoteLimitType() ==2 ){
				if(userId.equals(bsRelVoteItemUser.getUserId()) && toDayStartTime<bsRelVoteItemUser.getVoteTime().intValue() && bsRelVoteItemUser.getVoteTime().intValue()<toDayEndTime ){
					total++;
				}
				
			}
			/**针对多选的活动结束*/
			if(bsVote.getChoosePattern()>=1 && bsVote.getVoteLimitType() ==1){
				/**判断投票时间是否大于当天的起始时间。并且小于当天的结束时间*/
				if(userId.equals(bsRelVoteItemUser.getUserId()) && bsVote.getBeginTime()<bsRelVoteItemUser.getVoteTime().intValue() && bsRelVoteItemUser.getVoteTime().intValue()<bsVote.getEndTime() ){
					total++;
				}

			}
		}
		/**如果当前总选项大于限制选项。则投票提示 不能重复投票*/
		if(total>=bsVote.getChoosePattern()){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
		}
		/**针对多选的活动结束*/
/*			if(bsVote.getChoosePattern()>=1 && bsVote.getVoteLimitType() ==1 ){
				int count = 0;
				for(BsRelVoteItemUser bsRelVoteItemUser :setsBsRels){
					*//**判断投票时间是否大于当天的起始时间。并且小于当天的结束时间*//*
				if(userId.equals(bsRelVoteItemUser.getUserId()) && bsVote.getBeginTime()<bsRelVoteItemUser.getVoteTime().intValue() && bsRelVoteItemUser.getVoteTime().intValue()<bsVote.getEndTime() ){
					count++;
				}

			}
			if(count>=bsVote.getChoosePattern()){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
			}
		}*/
		//TODO
		/*如果当前传入选项没有被投票，则可以继续投票*/
		if(stringBsRels.size() == 0){
			
			this.addVoteNumAndItem(map, bsVote, maps);
			/*
			 * 更新次数
			 */
			this.addRedisVoteNumber(bsVote);
			Set<String> setsBs = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
			lstBsRelVoteUser = JSONArray.parseArray(setsBs.toString(), BsRelVoteItemUser.class);
			this.canVote(jsonObject, bsVote, lstBsRelVoteUser,voteItemIds,userId,toDayStartTime,toDayEndTime);
			return Common.getReturn(AppErrorCode.SUCCESS,"",jsonObject).toJSONString();
		}
		/*setsBsRels 当前评选下所有的投票，比较当前传入的选项是否已经被投票,*/
		JSONObject obj = this.canVote(jsonObject, bsVote, setsBsRels, voteItemIds, userId, toDayStartTime, toDayEndTime);
		String  canVote = obj.get("canVoteForVoteOptions").toString();
		if("0".equals(canVote)){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_VOTE_MESSAGE).toJSONString();
		}	
		
		this.addRedisVote(isBsRelVoteItemUser, stringBsRels);
		/*更新评选下的投票次数*/
		this.addRedisVoteNumber(bsVote);
		
		Set<String> setsBs = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(voteId)), String.valueOf(voteId));
		lstBsRelVoteUser = JSONArray.parseArray(setsBs.toString(), BsRelVoteItemUser.class);
		this.canVote(jsonObject, bsVote, lstBsRelVoteUser,voteItemIds,userId,toDayStartTime,toDayEndTime);
		return Common.getReturn(AppErrorCode.SUCCESS,"",jsonObject).toJSONString();
	}

	/* 更新评选下的投票次数 */
	protected void addRedisVoteNumber(BsVote bsVote) {
		String vo = JedisUserCacheUtils.hgetVoteNumber(VoteNumber.class.getSimpleName(), String.valueOf(bsVote.getVoteId()));
		VoteNumber voteNumber = JSONObject.parseObject(vo, VoteNumber.class);
		if (voteNumber == null) {
			VoteNumber number = new VoteNumber(bsVote.getVoteId(), 1);
			JedisUserCacheUtils.hsetVoteNumber(VoteNumber.class.getSimpleName(), String.valueOf(bsVote.getVoteId()), JSON.toJSONString(number));
			number = null;
		} else {
			voteNumber.setCount(voteNumber.getCount() + 1);
			JedisUserCacheUtils.hsetVoteNumber(VoteNumber.class.getSimpleName(), String.valueOf(bsVote.getVoteId()), JSON.toJSONString(voteNumber));
		}
	}
		/*
		 * 添加到redis中
		 */
	protected void addRedisVote(List<BsRelVoteItemUser> isBsRelVoteItemUser, List<String> stringBsRels) {
		/*
		 * 当不是第一次投票的时候表示更新次数以及投票时间 存放在redis中的hash
		 */
		this.hmsetVoteRelVoteItemUser(isBsRelVoteItemUser);
		/*
		 * 当不是第一次投票的时候表示更新次数以及投票时间 存放在redis中zset中
		 */
		this.zremVoteRelVoteItemUser(stringBsRels);
		/*
		 * 继续存储在zset中
		 */
		this.zaddVoteRelVoteItemUser(isBsRelVoteItemUser);
	}
	/*
	 * 校验当前登录则是否能继续投票的机会
	 */
	protected JSONObject canVote(JSONObject obj,BsVote bsVote,List<BsRelVoteItemUser> listBsRelVoteItems,String[] voteItemIds,Integer userId,long toDayStartTime,long toDayEndTime){
		/*
		 * 拼接当前投票则投票是否还有机会投票
		 */
		StringBuffer canVote = new StringBuffer();
		/**传入批量的选项voteItemIds*/
		for(int i=0;i<voteItemIds.length;i++){
			if(CollectionUtils.isEmpty(listBsRelVoteItems)){
				canVote.append(1);
			}
			/*listBsRelVoteItems 表示当前评选下所有的投票*/
			for(BsRelVoteItemUser bsRelVoteItemUser:listBsRelVoteItems){
				/*
				 * 如果为单选模式
				 */
				if(bsVote.getChoosePattern() == 0){
					/*
					 * 当前用户:1、如果选择模式为单选，并且活动周期以次/天 ,表示每天只能投一次,当天只能投选一项，第二天还能继续投票
					 */
					if(bsVote.getVoteLimitType() == 2){
						if(Integer.valueOf(voteItemIds[i]).equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) 
								&& userId.equals(bsRelVoteItemUser.getUserId())){
							
							if(bsRelVoteItemUser.getVoteTime() < toDayStartTime){
								canVote.append(1);
							}
							if(toDayStartTime < bsRelVoteItemUser.getVoteTime() && bsRelVoteItemUser.getVoteTime() < toDayEndTime){
								canVote.append(0);
							}
						}
						
					}
					/*
					 * 2、如果选择模式为单选。并且以活动周期结束为,表示整个周期只能投一次,整个周期只能选择一次，
					 */
					if(bsVote.getVoteLimitType() == 1){
						if(Integer.valueOf(voteItemIds[i]).equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							canVote.append(0);
						}/*else{
							canVote.append(1);
						}*/
					}
					
				}
				/*
				 * 多选模式
				 */
				if(bsVote.getChoosePattern()>=1){
					 /*
					  *  3、如果选择模式为多选，并且活动周期以次/天为标准，当天能投多个选项，每个选项只能投一次一天，第二天仍然能投
					  */
					if(bsVote.getVoteLimitType() ==2){
						if(Integer.valueOf(voteItemIds[i]).equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							//如果当前起始时间比投票创建时间大，而且结束时间比投票创建时间小 表示以前投票，
							if(toDayStartTime>bsRelVoteItemUser.getVoteTime() && bsVote.getEndTime()>bsRelVoteItemUser.getVoteTime()){
								canVote.append(1);
							}
							// 如果投票创建时间比当天的起始时间大，但是比当天的结束时间小，而且投票时间小于结束时间
							else if(toDayStartTime <bsRelVoteItemUser.getVoteTime() && bsRelVoteItemUser.getVoteTime() < toDayEndTime && bsVote.getEndTime()>bsRelVoteItemUser.getVoteTime() ){
								canVote.append(0);
							}
						}
					}
					/* 
					 * 4、如果选择模式为多选，并且活动周期以结束为标准，整个周期内，只能投多选项数的项。每个选项只能投一次
					 */
					
					if(bsVote.getVoteLimitType() == 1){
						if(Integer.valueOf(voteItemIds[i]).equals(bsRelVoteItemUser.getItemId())  && bsRelVoteItemUser.getVoteId().equals(bsVote.getVoteId()) && userId.equals(bsRelVoteItemUser.getUserId())){
							canVote.append(0);
						}/*else{
							canVote.append(1);
						}*/
					}
				}
			}
			
			/*
			 * 当第一次投票后判定其投票选项还能否继续投票
			 */
			obj.put("canVoteForVoteOptions", canVote);
		}
		return obj;
	}
	
		/*
		 * 投票的添加和更新操作
		 */
	protected void addVoteNumAndItem(Map<String,String> map ,BsVote bsVote,Map<String,Double> maps){
		/*
		 * 批量插入投票和投票选项关联
		 */
		JedisUserCacheUtils.hmsetVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), map);
		/*
		 * 存入zset中
		 */
		JedisUserCacheUtils.zaddVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), maps);
		
	}
	/**批量更新redis中时间以及次数
	 * @param ls
	 */
	protected void hmsetVoteRelVoteItemUser(List<BsRelVoteItemUser> isBsRelVoteItemUser){
		Map<String,String> maps = new HashMap<>();
		for(BsRelVoteItemUser bsRelVoteItemUser:isBsRelVoteItemUser){
			bsRelVoteItemUser.setVoteTime(new Long(TimeUtil.getCurrentTimes()).intValue());
			bsRelVoteItemUser.setVoteTimes(bsRelVoteItemUser.getVoteTimes()+1);
			maps.put(bsRelVoteItemUser.getHashKey(), JSON.toJSONString(bsRelVoteItemUser));
		}
		if(!maps.isEmpty())
		JedisUserCacheUtils.hmsetVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), maps);
	}

	
	/**删除zset
	 * @param ls
	 */
	protected void zremVoteRelVoteItemUser(List<String> stringBsRels){
		String[] str = (String[])stringBsRels.toArray(new String[stringBsRels.size()]);
		JedisUserCacheUtils.zremVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), str);
	}
	
	/**添加数据到redis中
	 * @param ls
	 */
	protected void zaddVoteRelVoteItemUser(List<BsRelVoteItemUser> isBsRelVoteItemUser){
		Map<String,Double> map = new HashMap<>();
		for(BsRelVoteItemUser bsRelVoteItemUser:isBsRelVoteItemUser){
			bsRelVoteItemUser.setVoteTime(new Long(TimeUtil.getCurrentTimes()).intValue());
			bsRelVoteItemUser.setVoteTimes(bsRelVoteItemUser.getVoteTimes());
			map.put(JSON.toJSONString(bsRelVoteItemUser),Double.valueOf(bsRelVoteItemUser.getVoteId()));
		}
		JedisUserCacheUtils.zaddVoteRelVoteItemUser(BsRelVoteItemUser.class.getSimpleName(), map);
	}
	
}
