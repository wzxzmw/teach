package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubTopicComment;
import com.seentao.stpedu.common.sqlmap.ClubTopicCommentMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class ClubTopicCommentDao {

	@Autowired
	private ClubTopicCommentMapper clubTopicCommentMapper;
	
	
	public void insertClubTopicComment(ClubTopicComment clubTopicComment){
		clubTopicCommentMapper .insertClubTopicComment(clubTopicComment);
	}
	
	public void deleteClubTopicComment(ClubTopicComment clubTopicComment){
		clubTopicCommentMapper .deleteClubTopicComment(clubTopicComment);
	}
	
	public void updateClubTopicCommentByKey(ClubTopicComment clubTopicComment){
		clubTopicCommentMapper .updateClubTopicCommentByKey(clubTopicComment);
	}
	
	public List<ClubTopicComment> selectSingleClubTopicComment(ClubTopicComment clubTopicComment){
		return clubTopicCommentMapper .selectSingleClubTopicComment(clubTopicComment);
	}
	
	public ClubTopicComment selectClubTopicComment(ClubTopicComment clubTopicComment){
		List<ClubTopicComment> clubTopicCommentList = clubTopicCommentMapper .selectSingleClubTopicComment(clubTopicComment);
		if(clubTopicCommentList == null || clubTopicCommentList .size() == 0){
			return null;
		}
		return clubTopicCommentList .get(0);
	}
	
	public List<ClubTopicComment> selectAllClubTopicComment(){
		return clubTopicCommentMapper .selectAllClubTopicComment();
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return clubTopicCommentMapper.queryCount(paramMap);
	}
	public List<ClubTopicComment> queryByPage(Map<String, Object> paramMap) {
		return clubTopicCommentMapper.queryByPage(paramMap);
	}
	/**
	 * 批量更新
	 */
	public void batchUpadteClubTopicComment(/*List<ClubTopicComment> ls,*/Integer createUserId)throws RuntimeException{
		try {
			clubTopicCommentMapper.batchUpdateClubTopicComment(createUserId);
		} catch (RuntimeException e) {
			LogUtil.error(this.getClass(), "-----batchUpadteClubTopicComment-----", "批量更新失败",e);
			throw new RuntimeException(e);
		}
	}
}