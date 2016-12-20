package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.BsVote;
import com.seentao.stpedu.common.sqlmap.BsVoteMapper;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class BsVoteDao {

	@Autowired
	private BsVoteMapper bsVoteMapper;
	/*
	 * 添加评选项对象
	 */
	public  void insertBsVote(BsVote bsVote)throws InsertObjectException{
		try {
			bsVoteMapper.insertBsVote(bsVote);	
		} catch (InsertObjectException e) {
			LogUtil.error(BsVoteDao.class, "insertBsVote", "insert bsVote is error");
			throw new InsertObjectException("insert bsVote is error",e);
		}
	}
	/*
	 * 修改评选数量
	 */
	public void updateBsVoteByVoteIdVoteNum(BsVote bsVote)throws UpdateObjectException{
		try {
			int count = bsVoteMapper.updateBsVoteByVoteIdVoteNum(bsVote.getVoteId(),bsVote.getVoteNum()+1);
			if(count >=1){
				bsVote.setVoteNum(bsVote.getVoteNum()+1);
				JedisUserCacheUtils.hsetBsVoteByVoteId(BsVote.class.getSimpleName(), String.valueOf(bsVote.getVoteId()), JSON.toJSONString(bsVote));
			}
		} catch (UpdateObjectException e) {
			LogUtil.error(this.getClass(), "updateBsVoteByVoteIdVoteNum", "update voteNum is error ");
			throw new UpdateObjectException("update voteNum is error ",e);
		}
	}
	/*
	 * 分页查询推荐评选
	 */
	public List<BsVote> queryByPageBsVote(Map<String,Object> map){
		return  bsVoteMapper.queryByPageBsVote(map);
	}
	/*
	 * 查询推荐评选总数
	 */
	public Integer queryCountByBsVote(Map<String,Object> map){
		return bsVoteMapper.queryCountByBsVote(map);
	}
	/*
	 * 根据投票id获取唯一一条投票信息
	 */
	public BsVote queryBsVoteByVoteId(Integer voteId){
		String msg = JedisCache.getRedisVal(null,BsVote.class.getSimpleName(), String.valueOf(voteId));
		BsVote bsVote = JSONObject.parseObject(msg, BsVote.class);
		if(bsVote == null)
			bsVote = bsVoteMapper.queryBsVoteByVoteId(voteId);
		return bsVote;
	}
	public void updateVoteCount(Integer voteId, Integer voteCount) throws UpdateObjectException {
		bsVoteMapper.updateBsVoteByVoteIdVoteNum(voteId,voteCount);
	}
	public void batchUpdateVoteCount(List<BsVote> list) {
		bsVoteMapper.batchUpdateVoteCount(list);
	}
}