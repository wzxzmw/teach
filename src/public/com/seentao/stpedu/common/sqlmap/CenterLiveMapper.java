package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterLive;
import java.util.List;
import java.util.Map;

public interface CenterLiveMapper {

	public abstract void insertCenterLive(CenterLive centerLive);
	
	public abstract void deleteCenterLive(CenterLive centerLive);
	
	public abstract void updateCenterLiveByKey(CenterLive centerLive);
	
	public abstract List<CenterLive> selectSingleCenterLive(CenterLive centerLive);
	
	public abstract List<CenterLive> selectAllCenterLive();

	public abstract Integer queryClubCenterLiveCount(Map<String, Object> paramMap);

	public abstract List<CenterLive> selectClubCenterLive(Map<String, Object> initParam);

	public abstract Integer queryMemberCenterLiveCount(Map<String, Object> paramMap);

	public abstract List<CenterLive> selectMemberCenterLive(Map<String, Object> initParam);

	public abstract List<CenterLive> selectClassCenterLive(Map<String, Object> initParam);

	public abstract Integer queryClassCenterLiveCount(Map<String, Object> paramMap);

	public abstract Integer queryLiveClubCenterLiveCount(Map<String, Object> paramMap);

	public abstract List<CenterLive> selectLiveClubCenterLive(Map<String, Object> initParam);

	public abstract Integer queryAttentionUserCenterLiveCount(Map<String, Object> paramMap);

	public abstract List<CenterLive> selectAttentionUserCenterLive(Map<String, Object> initParam);

	public abstract CenterLive getLastCenterLiveByClubId(CenterLive centerLive);

	public abstract List<CenterLive> getLastCenterLiveByIds(Map<String, Object> paramMap);

	public abstract void updateCenterLiveByKeyAll(CenterLive centerLive);

	public abstract void updateCenterLiveByKeyIsTop(CenterLive centerLive);
	
}