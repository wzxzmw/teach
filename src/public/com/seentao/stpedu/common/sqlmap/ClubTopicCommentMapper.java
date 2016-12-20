package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.ClubTopicComment;

public interface ClubTopicCommentMapper {

	public abstract void insertClubTopicComment(ClubTopicComment clubTopicComment);
	
	public abstract void deleteClubTopicComment(ClubTopicComment clubTopicComment);
	
	public abstract void updateClubTopicCommentByKey(ClubTopicComment clubTopicComment);
	
	public abstract List<ClubTopicComment> selectSingleClubTopicComment(ClubTopicComment clubTopicComment);
	
	public abstract List<ClubTopicComment> selectAllClubTopicComment();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ClubTopicComment> queryByPage(Map<String, Object> paramMap);
	/**
	 * 批量更新话题评论
	 * @param createUserId
	 * @throws RuntimeException
	 */
	public abstract void batchUpdateClubTopicComment(@Param("createUserId") Integer createUserId)throws RuntimeException;
}