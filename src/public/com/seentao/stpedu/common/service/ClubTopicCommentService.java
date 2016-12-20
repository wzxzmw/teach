package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubTopicCommentDao;
import com.seentao.stpedu.common.entity.ClubTopicComment;

@Service
public class ClubTopicCommentService{
	
	@Autowired
	private ClubTopicCommentDao clubTopicCommentDao;
	
	public ClubTopicComment getClubTopicComment(ClubTopicComment clubTopicComment) {
		List<ClubTopicComment> clubTopicCommentList = clubTopicCommentDao .selectSingleClubTopicComment(clubTopicComment);
		if(clubTopicCommentList == null || clubTopicCommentList .size() <= 0){
			return null;
		}
		
		return clubTopicCommentList.get(0);
	}
	
	public void insertClubTopicComment(ClubTopicComment clubTopicComment){
		clubTopicCommentDao.insertClubTopicComment(clubTopicComment);
	}
	
	public void updateClubTopicCommentByKey(ClubTopicComment clubTopicComment){
		clubTopicCommentDao.updateClubTopicCommentByKey(clubTopicComment);
	}
}