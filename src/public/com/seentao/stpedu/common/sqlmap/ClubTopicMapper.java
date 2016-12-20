package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.CustomizeException;
import com.seentao.stpedu.common.entity.ClubTopic;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ClubTopicMapper {

	public abstract void insertClubTopic(ClubTopic clubTopic);
	
	public abstract void deleteClubTopic(ClubTopic clubTopic);
	
	public abstract void updateClubTopicByKey(ClubTopic clubTopic);
	/**
	 * 批量更新话题
	 * @param clubTopics 
	 * @param createUserId
	 */
	public abstract void batchUpdateClubTopic(@Param("clubId")Integer clubId,@Param("createUserId")Integer createUserId)throws CustomizeException;
	
	public abstract List<ClubTopic> selectSingleClubTopic(ClubTopic clubTopic);
	
	public abstract List<ClubTopic> selectAllClubTopic();

	public abstract List<ClubTopic> selectHotClubTopicByClubId(Map<String, Object> paramMap);
	
	public abstract List<ClubTopic> selectNewClubTopicByClubId(Map<String, Object> paramMap);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract Integer countBackClubTopic(Map<String, Object> paramMap);

	public abstract List<ClubTopic> selectBackClubTopicByClubId(Map<String, Object> initParam);

	public abstract List<ClubTopic> selectCenterClubTopicByClubId(Map<String, Object> initParam);

	public abstract void delClubTopicAll(List<ClubTopic> delClubTopic);

	public abstract void updateClubTopicByKeyAll(ClubTopic clubTopics);

	
}