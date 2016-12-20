package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.seentao.stpedu.common.dao.CenterAttentionDao;
import com.seentao.stpedu.common.entity.CenterAttention;

/** 
* @author yy
* @date 2016年6月24日 下午5:22:35 
*/
@Service
public class BaseCenterAttentionService {
	@Autowired
	private CenterAttentionDao centerAttentionDao;
	
	public List<CenterAttention> getCenterAttentionList(CenterAttention centerAttention) {
		List<CenterAttention> list = centerAttentionDao.selectCenterAttention(centerAttention);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list;
	}
	
	
	/**
	 * 根据创建者id查询创建关注的数量以及被关注的数量
	 * @param centerattention
	 * @return
	 */
	public Map<String, Integer> getConcernCount(Integer userId) {
		CenterAttention concernParam = new CenterAttention();
		concernParam.setCreateUserId(userId);
		concernParam.setRelObjetType(1);
		Integer concern = centerAttentionDao.getConcernCount(concernParam);//我关注的数量
		CenterAttention concernParam1 = new CenterAttention();
		concernParam1.setRelObjetType(1);
		concernParam1.setRelObjetId(String.valueOf(userId));
		Integer fans = centerAttentionDao.getFansCount(concernParam1);//我的粉丝数量
		Map<String,Integer> res = new HashMap<String,Integer>();
		res.put("concern", concern);
		res.put("fans", fans);
		return res;
	}
	
	/**
	 * 根据创建者id查询我关注的企业数量以及俱乐部数量
	 * @param centerattention
	 * @return
	 */
	public Map<String, Integer> getBusinessAndCulbCount(Integer userId) {
		CenterAttention c = new CenterAttention();
		c.setCreateUserId(userId);
		c.setRelObjetType(3);
		Integer bussinessCount = centerAttentionDao.getConcernCount(c);//我关注的企业数量
		CenterAttention c1 = new CenterAttention();
		c1.setCreateUserId(userId);
		c1.setRelObjetType(2);
		Integer clubCount = centerAttentionDao.getConcernCount(c1);//我关注的俱乐部数量
		Map<String,Integer> res = new HashMap<String,Integer>();
		res.put("bussinessCount", bussinessCount);
		res.put("clubCount", clubCount);
		return res;
	}
	
	/**
	 * 判断两个id之间是否互相关注 以及我是否关注他
	 * @param centerattention
	 * @return
	 */
	public Map<String, Integer> getIsConcernCount(Integer userId,Integer otherId) {
		Map<String,Integer> res = new HashMap<String,Integer>();
		//我是否关注他
		CenterAttention c = new CenterAttention();
		c.setRelObjetType(1);//个人
		c.setCreateUserId(userId);
		c.setRelObjetId(String.valueOf(otherId));
		List<CenterAttention> list = centerAttentionDao.selectCenterAttention(c);
		//他是否关注我
		CenterAttention centerattention = new CenterAttention();
		centerattention.setRelObjetType(1);//个人
		centerattention.setCreateUserId(otherId);
		centerattention.setRelObjetId(String.valueOf(userId));
		List<CenterAttention> listCenterattention = centerAttentionDao.selectCenterAttention(centerattention);
		if(!CollectionUtils.isEmpty(list)){//我是否关注他
			res.put("isConcern", 1);//我关注了他
		}else{
			res.put("isConcern", 0);//我没有关注他
		}
		//是否互相关注
		if(!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(listCenterattention)){
			res.put("concern", 1);//互相关注
		}else{
			res.put("concern", 0);//没有互相关注
		}
		return res;
	}
	
	/**
	 * 判断用户是否关注某一对象
	 * @param createUserId 创建者ID
	 * @param relObjetId 关联对象ID
	 * @return
	 * @author chengshx
	 */
	public CenterAttention isAttention(Integer createUserId, String relObjetId){
		CenterAttention centerAttention = new CenterAttention();
		centerAttention.setCreateUserId(createUserId);
		centerAttention.setRelObjetId(relObjetId);
		List<CenterAttention> list = centerAttentionDao.selectCenterAttention(centerAttention);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	public List<CenterAttention> getCenterAttention(CenterAttention centerAttention) {
		return centerAttentionDao.getCenterAttention(centerAttention) ;
	}


	public Integer getCenterAttentionCount(CenterAttention centerAttention) {
		return centerAttentionDao.getCenterAttentionCount( centerAttention);
	}


	public List<CenterAttention> getCenterAttentions(CenterAttention centerAttention) {
		return centerAttentionDao.getCenterAttentions(centerAttention);
	}
	
}


