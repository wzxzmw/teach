
package com.seentao.stpedu.votes.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.dao.BsRelVoteItemUserDao;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月6日 上午7:02:19
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Component
public class AnnotationQuartzService {
	/*提交投票选项信息*/
	@Autowired
	private BsRelVoteItemUserDao bsRelVoteItemUserDao;
	
	private String VOTE_COUNT = "BsVoteCount";
	
	public static List<String> common = new LinkedList<>();
	
	/**
	 * 定时启动。每天 00:00 执行一次
	 * s M h d m w y
	 */
//	@Scheduled(fixedRate = 1000*60*1)
//	@Scheduled(cron="0 0 0 * * *")
	public void batchVoteStatusIsEnd(){
		Random rand = new Random();
		Integer time = rand.nextInt(100) + 20;
		try {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e1) {
				LogUtil.error("", e1);
			}
			String isWorking = JedisCache.getRedis(VOTE_COUNT);
			if(isWorking != null && Integer.valueOf(isWorking).intValue() == 1){
				LogUtil.info(this.getClass(), "batchVoteStatusIsEnd", "已在执行统计投票信息");
				return;
			}
			JedisCache.setRedis(VOTE_COUNT, "1");
			LogUtil.info(this.getClass(), "autoTask", "统计投票信息开始执行");
			/*
			 * 取出备份数据
			 */
			Map<String, String> map = JedisUserCacheUtils.hgetBackAllBsVote(BsVote.class.getSimpleName());
			/*
			 * 当前时间
			 */
			long systemTime = TimeUtil.getCurrentTimes();
			
			List<String> ls = new LinkedList<>();
			for(Map.Entry<String, String> me : map.entrySet()){
				String val = me.getValue();
					BsVote bsVote = JSONObject.parseObject(val, BsVote.class);
					if(bsVote.getEndTime() < systemTime){
						ls.add(String.valueOf(bsVote.getVoteId()));
				}
				val = null;
				bsVote = null;
			}
			
			common.add(null);
			ls.removeAll(common);
			
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			for(String strings :ls){
				Set<String> sets = JedisUserCacheUtils.zrangebyscore(BsRelVoteItemUser.class.getSimpleName(), String.valueOf(Integer.valueOf(strings)), String.valueOf(Integer.valueOf(strings)));
				for(String str :sets){
					stringBuffer.append(""+str+",");
				}
				sets = null;
			}
			List<BsRelVoteItemUser> lsr = null;
			if(stringBuffer.toString().length() !=1){
				lsr = JSONArray.parseArray(stringBuffer.substring(0, stringBuffer.length()-1)+"]", BsRelVoteItemUser.class);
				
			}
			String[] members= (String[])ls.toArray(new String[ls.size()]);
			try {
				if(!CollectionUtils.isEmpty(lsr)){
					bsRelVoteItemUserDao.batchBsRelVoteItemUser(lsr);
					/*
					 * 删除备份表已经结束的投票
					 */
					JedisUserCacheUtils.hdelBsVoteByVotes(BsVote.class.getSimpleName(), members);
				}
				LogUtil.info(this.getClass(), "batchVoteStatusIsEnd", "统计投票信息执行完成");
			} catch(InsertObjectException e){
				LogUtil.error(AnnotationQuartzService.class, "batchBsRelVoteItemUser", "batch BsRelVoteItemUser is error ");
				e.printStackTrace();
			}
		} finally {
			JedisCache.setRedis(VOTE_COUNT, "0");
		}
	}

}
