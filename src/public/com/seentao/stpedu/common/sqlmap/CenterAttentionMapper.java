package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.CenterAttention;


public interface CenterAttentionMapper {

	public abstract Integer insertCenterAttention(CenterAttention centerAttention);
	
	public abstract void deleteCenterAttention(CenterAttention centerAttention);
	
	public abstract void updateCenterAttentionByKey(CenterAttention centerAttention);
	
	public abstract List<CenterAttention> selectSingleCenterAttention(CenterAttention centerAttention);
	
	public abstract List<CenterAttention> selectAllCenterAttention();

	public abstract Integer getConcernCount(CenterAttention centerattention);

	public abstract Integer getFansCount(CenterAttention centerattention);

	public abstract Integer getFansCountInfo(CenterAttention attention);

	public abstract List<CenterAttention> selectCenterAfollow(CenterAttention attention);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<CenterAttention> selectCenterAttentions(Map<String, Object> initParam);

	public abstract List<CenterAttention> getCenterAttention(CenterAttention centerAttention);

	public abstract Integer getCenterAttentionCount(CenterAttention centerAttention);

	public abstract Integer attentionCount(@Param(value="createUserId")Integer createUserId);

	public abstract Integer getCenterAttentionCount(Map<String, Object> paramMap);

	public abstract List<CenterAttention> getCenterAttention(Map<String, Object> paramMap);

	public abstract List<CenterAttention> getCenterAttentions(CenterAttention centerAttention);
	
	public abstract CenterAttention queryCenterAttentionByUserIdAndClubId(@Param(value="createUserId")Integer createUserId,@Param(value="relObjetType")Integer relObjetType,@Param("relObjetId")String relObjetId);

	
}