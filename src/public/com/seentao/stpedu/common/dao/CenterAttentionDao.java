package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.sqlmap.CenterAttentionMapper;


@Repository
public class CenterAttentionDao {

	@Autowired
	private CenterAttentionMapper centerAttentionMapper;
	
	
	public Integer insertCenterAttention(CenterAttention centerAttention){
		return centerAttentionMapper .insertCenterAttention(centerAttention);
	}
	
	public void deleteCenterAttention(CenterAttention centerAttention){
		centerAttentionMapper .deleteCenterAttention(centerAttention);
	}
	
	public void updateCenterAttentionByKey(CenterAttention centerAttention){
		centerAttentionMapper .updateCenterAttentionByKey(centerAttention);
	}
	
	public CenterAttention selectSingleCenterAttention(CenterAttention centerAttention){
		List<CenterAttention> centerAttentionList = centerAttentionMapper .selectSingleCenterAttention(centerAttention);
		if(centerAttentionList == null || centerAttentionList .size() == 0){
			return null;
		}
		return centerAttentionList.get(0);
	}
	
	public List<CenterAttention> selectCenterAttention(CenterAttention centerAttention){
		return centerAttentionMapper .selectSingleCenterAttention(centerAttention);
	}
	
	public List<CenterAttention> selectAllCenterAttention(){
		return centerAttentionMapper .selectAllCenterAttention();
	}

	public Integer getConcernCount(CenterAttention centerattention) {
		return centerAttentionMapper.getConcernCount( centerattention);
	}

	public Integer getFansCount(CenterAttention centerattention) {
		return centerAttentionMapper.getFansCount( centerattention);
	}

	public Integer getFansCountInfo(CenterAttention attention) {

		return centerAttentionMapper.getFansCountInfo(attention);
	}

	public List<CenterAttention> selectCenterAfollow(CenterAttention attention) {

		return centerAttentionMapper.selectCenterAfollow(attention);
	}
	public Integer queryCount(Map<String, Object> paramMap){
		return centerAttentionMapper.queryCount(paramMap);
	}
	public List<CenterAttention> selectCenterAttentions(Map<String, Object> initParam) {
		return centerAttentionMapper.selectCenterAttentions(initParam);
	}

	public List<CenterAttention> getCenterAttention(CenterAttention centerAttention) {
		return centerAttentionMapper.getCenterAttention(centerAttention) ;
	}

	public Integer getCenterAttentionCount(CenterAttention centerAttention) {
		return centerAttentionMapper.getCenterAttentionCount( centerAttention);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterAttention getEntityForDB(Map<String, Object> paramMap){
		CenterAttention tmp = new CenterAttention();
		tmp.setAtteId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectSingleCenterAttention(tmp);
	}

	public Integer attentionCount(Integer valueOf) {
		return centerAttentionMapper.attentionCount(valueOf);
	}
	
	public Integer queryCountCenterAttention(Map<String, Object> paramMap) {
		return centerAttentionMapper.getCenterAttentionCount(paramMap);
	}

	public  List<CenterAttention> queryByPage(Map<String, Object> paramMap) {
		return centerAttentionMapper.getCenterAttention(paramMap);
	}

	public List<CenterAttention> getCenterAttentions(CenterAttention centerAttention) {
		return centerAttentionMapper.getCenterAttentions(centerAttention) ;
	}
	/**
	 * 根据俱乐部clubId和用户userId,关注类型为2俱乐部查询是否关注
	 */
	public CenterAttention queryCenterAttentionByUserIdAndClubId(Integer createUserId,Integer relObjetType,String relObjetId){
		return centerAttentionMapper.queryCenterAttentionByUserIdAndClubId(createUserId, relObjetType, relObjetId);
	}
	

}