package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubMemberMood;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.sqlmap.ClubMemberMoodMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class ClubMemberMoodDao {

	@Autowired
	private ClubMemberMoodMapper clubMemberMoodMapper;
	
	
	public void insertClubMemberMood(ClubMemberMood clubMemberMood){
		clubMemberMoodMapper .insertClubMemberMood(clubMemberMood);
	}
	
	public void deleteClubMemberMood(ClubMemberMood clubMemberMood){
		clubMemberMoodMapper .deleteClubMemberMood(clubMemberMood);
	}
	/**
	 * 发布的俱乐部心情 冻结
	 * @param memberMoods
	 * @param createUserId
	 */
	public void batchUpdateClubMemberMood(Integer clubId,Integer createUserId)throws Exception{
		try {
			clubMemberMoodMapper.batchUpdateClubMemberMood(clubId, createUserId);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "batchUpdateClubMemberMood", "发布的俱乐部心情 冻结", e);
			throw new Exception(e);
		}
	}
	public void updateClubMemberMoodByKey(ClubMemberMood clubMemberMood){
		clubMemberMoodMapper .updateClubMemberMoodByKey(clubMemberMood);
	}
	
	public List<ClubMemberMood> selectClubMemberMood(ClubMemberMood clubMemberMood){
		return clubMemberMoodMapper .selectSingleClubMemberMood(clubMemberMood);
	}
	
	public ClubMemberMood selectSingleClubMemberMood(ClubMemberMood clubMemberMood){
		List<ClubMemberMood> clubMemberMoodList = clubMemberMoodMapper .selectSingleClubMemberMood(clubMemberMood);
		if(clubMemberMoodList == null || clubMemberMoodList .size() == 0){
			return null;
		}
		return clubMemberMoodList .get(0);
	}
	
	public List<ClubMemberMood> selectAllClubMemberMood(){
		return clubMemberMoodMapper .selectAllClubMemberMood();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubMemberMood getEntityForDB(Map<String, Object> paramMap){
		ClubMemberMood tmp = new ClubMemberMood();
		tmp.setMoodId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectSingleClubMemberMood(tmp);
	}
	public Integer queryCount(Map<String, Object> paramMap){
		return clubMemberMoodMapper.queryCount(paramMap);
	}
	
	public Integer queryEveryOneMoodCount(Map<String, Object> paramMap){
		return clubMemberMoodMapper.queryEveryOneMoodCount(paramMap);
	}
	public List<ClubMemberMood> selectEveryOneClubMemberMood(Map<String, Object> initParam) {
		return clubMemberMoodMapper.selectEveryOneClubMemberMood(initParam);
	}
	
	public Integer queryMyMoodCount(Map<String, Object> paramMap){
		return clubMemberMoodMapper.queryMyMoodCount(paramMap);
	}
	public List<ClubMemberMood> selectMyClubMemberMood(Map<String, Object> initParam) {
		return clubMemberMoodMapper.selectMyClubMemberMood(initParam);
	}
	
	public Integer queryMeMoodCount(Map<String, Object> paramMap){
		return clubMemberMoodMapper.queryMeMoodCount(paramMap);
	}
	public List<ClubMemberMood> selectMeClubMemberMood(Map<String, Object> initParam) {
		return clubMemberMoodMapper.selectMeClubMemberMood(initParam);
	}
}