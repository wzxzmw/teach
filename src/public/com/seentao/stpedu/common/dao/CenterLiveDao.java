package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.sqlmap.CenterLiveMapper;


@Repository
public class CenterLiveDao {

	@Autowired
	private CenterLiveMapper centerLiveMapper;
	
	
	public void insertCenterLive(CenterLive centerLive){
		centerLiveMapper .insertCenterLive(centerLive);
	}
	
	public void deleteCenterLive(CenterLive centerLive){
		centerLiveMapper .deleteCenterLive(centerLive);
	}
	
	public void updateCenterLiveByKey(CenterLive centerLive){
		centerLiveMapper .updateCenterLiveByKey(centerLive);
	}
	
	public List<CenterLive> selectCenterLive(CenterLive centerLive){
		return centerLiveMapper .selectSingleCenterLive(centerLive);
	}
	
	public CenterLive selectSingleCenterLive(CenterLive centerLive){
		List<CenterLive> centerLiveList = centerLiveMapper .selectSingleCenterLive(centerLive);
		if(centerLiveList == null || centerLiveList .size() == 0){
			return null;
		}
		return centerLiveList .get(0);
	}
	
	public List<CenterLive> selectAllCenterLive(){
		return centerLiveMapper .selectAllCenterLive();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterLive getEntityForDB(Map<String, Object> paramMap){
		CenterLive tmp = new CenterLive();
		tmp.setLiveId(Integer.valueOf(paramMap.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectSingleCenterLive(tmp);
	}
	
	public Integer queryClubCenterLiveCount(Map<String, Object> paramMap){
		return centerLiveMapper.queryClubCenterLiveCount(paramMap);
	}
	public List<CenterLive> selectClubCenterLive(Map<String, Object> initParam) {
		return centerLiveMapper.selectClubCenterLive(initParam);
	}
	
	
	public Integer queryMemberCenterLiveCount(Map<String, Object> paramMap){
		return centerLiveMapper.queryMemberCenterLiveCount(paramMap);
	}
	public List<CenterLive> selectMemberCenterLive(Map<String, Object> initParam) {
		return centerLiveMapper.selectMemberCenterLive(initParam);
	}
	
	
	public Integer queryClassCenterLiveCount(Map<String, Object> paramMap){
		return centerLiveMapper.queryClassCenterLiveCount(paramMap);
	}
	public List<CenterLive> selectClassCenterLive(Map<String, Object> initParam) {
		return centerLiveMapper.selectClassCenterLive(initParam);
	}
	
	
	public Integer queryLiveClubCenterLiveCount(Map<String, Object> paramMap){
		return centerLiveMapper.queryLiveClubCenterLiveCount(paramMap);
	}
	public List<CenterLive> selectLiveClubCenterLive(Map<String, Object> initParam) {
		return centerLiveMapper.selectLiveClubCenterLive(initParam);
	}
	
	public Integer queryAttentionUserCenterLiveCount(Map<String, Object> paramMap){
		return centerLiveMapper.queryAttentionUserCenterLiveCount(paramMap);
	}
	public List<CenterLive> selectAttentionUserCenterLive(Map<String, Object> initParam) {
		return centerLiveMapper.selectAttentionUserCenterLive(initParam);
	}

	public CenterLive getLastCenterLiveByClubId(CenterLive centerLive) {
		return centerLiveMapper.getLastCenterLiveByClubId(centerLive);
	}

	public List<CenterLive> getLastCenterLiveByIds(Map<String, Object> paramMap) {
		return centerLiveMapper.getLastCenterLiveByIds(paramMap);
	}

	public void updateCenterLiveByKeyAll(CenterLive centerLive) {
		centerLiveMapper.updateCenterLiveByKeyAll(centerLive);
	}

	public void updateCenterLiveByKeyIsTop(CenterLive centerLive) {
		centerLiveMapper.updateCenterLiveByKeyIsTop(centerLive);
	}
	
}