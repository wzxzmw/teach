package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubTrainingDiscussDao;
import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;

@Service
public class ClubTrainingDiscussService{
	
	@Autowired
	private ClubTrainingDiscussDao clubTrainingDiscussDao;
	
	public ClubTrainingDiscuss getClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss) {
		List<ClubTrainingDiscuss> clubTrainingDiscussList = clubTrainingDiscussDao .selectSingleClubTrainingDiscuss(clubTrainingDiscuss);
		if(clubTrainingDiscussList == null || clubTrainingDiscussList .size() <= 0){
			return null;
		}
		
		return clubTrainingDiscussList.get(0);
	}
	
	public void updateClubTrainingDiscussByKey(ClubTrainingDiscuss clubTrainingDiscuss){
		clubTrainingDiscussDao.updateClubTrainingDiscussByKey(clubTrainingDiscuss);
	}
	
	public void insertClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss){
		clubTrainingDiscussDao.insertClubTrainingDiscuss(clubTrainingDiscuss);
	}

	public void delClubTrainingDiscussAll(List<ClubTrainingDiscuss> delClubTrainingDiscuss) {
		clubTrainingDiscussDao.delClubTrainingDiscussAll(delClubTrainingDiscuss);
		
	}
}