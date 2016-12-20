package com.seentao.stpedu.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.common.entity.VoteNumber;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.votes.service.GetVotesService;
/**
 * 同步缓存中投票的数量到数据库中
 */
@Component
public class SynchronousData {
	@Autowired
	private GetVotesService iGetVotesSomesService;
	
//	@Scheduled(cron = "0 0/30 * * * *")
	public void voteCount(){
		//获取所有评选数据
		List<String> listCache = JedisUserCacheUtils.hgetAllVoteNumber(VoteNumber.class.getSimpleName());
		if(listCache!=null && listCache.size()>0){
			List<BsVote> list = new ArrayList<BsVote>();
			for (int i = 0; i < listCache.size(); i++) {
				// = (VoteNumber)JSONObject.toJSON(listCache.get(i));
				VoteNumber json = JSONObject.parseObject(listCache.get(i), VoteNumber.class);
				if(json!=null){
					if(json.getVoteId()!=null && json.getCount()!=null){
						BsVote bsvote = new BsVote();
						bsvote.setVoteId(json.getVoteId());
						bsvote.setVoteNum(json.getCount());
						list.add(bsvote);
					}
				}
			}
			if(list.size()>0){
				iGetVotesSomesService.batchUpdateVoteCount(list);
			}
		}
	}
	
}
