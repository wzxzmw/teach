package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.CustomizeException;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.sqlmap.ClubTopicMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class ClubTopicDao {

	@Autowired
	private ClubTopicMapper clubTopicMapper;
	
	
	public void insertClubTopic(ClubTopic clubTopic){
		clubTopicMapper .insertClubTopic(clubTopic);
	}
	
	public void deleteClubTopic(ClubTopic clubTopic){
		clubTopicMapper .deleteClubTopic(clubTopic);
	}
	
	public void updateClubTopicByKey(ClubTopic clubTopic){
		clubTopicMapper .updateClubTopicByKey(clubTopic);
	}
	/**
	 * 批量更新话题
	 * @param clubTopics
	 */
	public void batchUpdateClubTopic(Integer clubId,Integer createUserId)throws CustomizeException{
		try {
			clubTopicMapper.batchUpdateClubTopic(clubId, createUserId);
		} catch (CustomizeException e) {
			LogUtil.error(this.getClass(), "----batchUpdateClubTopic-------", "批量更新话题失败", e);
			throw new CustomizeException("批量更新话题失败",e);
		}
	}
	public List<ClubTopic> selectClubTopic(ClubTopic clubTopic){
		return clubTopicMapper .selectSingleClubTopic(clubTopic);
	}
	
	public ClubTopic selectSingleClubTopic(ClubTopic clubTopic){
		List<ClubTopic> clubTopicList = clubTopicMapper .selectSingleClubTopic(clubTopic);
		if(clubTopicList == null || clubTopicList .size() == 0){
			return null;
		}
		return clubTopicList .get(0);
	}
	
	public List<ClubTopic> selectAllClubTopic(){
		return clubTopicMapper .selectAllClubTopic();
	}

	public List<ClubTopic> selectHotClubTopicByClubId(Map<String, Object> initParam) {
		return clubTopicMapper.selectHotClubTopicByClubId(initParam);
	}
	
	public List<ClubTopic> selectNewClubTopicByClubId(Map<String, Object> initParam) {
		return clubTopicMapper.selectNewClubTopicByClubId(initParam);
	}
	
	public Integer queryCount(Map<String, Object> paramMap){
		return clubTopicMapper.queryCount(paramMap);
	}
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubTopic getEntityForDB(Map<String, Object> paramMap){
		ClubTopic tmp = new ClubTopic();
		tmp.setTopicId(Integer.valueOf(paramMap.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectSingleClubTopic(tmp);
	}

	public Integer countBackClubTopic(Map<String, Object> paramMap){
		return clubTopicMapper.countBackClubTopic(paramMap);
	}
	public List<ClubTopic> selectBackClubTopicByClubId(Map<String, Object> initParam) {
		return clubTopicMapper.selectBackClubTopicByClubId(initParam);
	}

	public List<ClubTopic> selectCenterClubTopicByClubId(Map<String, Object> initParam) {
		return clubTopicMapper.selectCenterClubTopicByClubId(initParam);
	}

	public void delClubTopicAll(List<ClubTopic> delClubTopic) {
		clubTopicMapper.delClubTopicAll(delClubTopic);
	}

	public void updateClubTopicByKeyAll(ClubTopic clubTopics) {
		clubTopicMapper.updateClubTopicByKeyAll(clubTopics);
	}
}