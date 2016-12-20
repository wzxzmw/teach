package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;
import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.sqlmap.ClubTrainingDiscussMapper;


@Repository
public class ClubTrainingDiscussDao {

	@Autowired
	private ClubTrainingDiscussMapper clubTrainingDiscussMapper;
	
	
	public void insertClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss){
		clubTrainingDiscussMapper .insertClubTrainingDiscuss(clubTrainingDiscuss);
	}
	
	public void deleteClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss){
		clubTrainingDiscussMapper .deleteClubTrainingDiscuss(clubTrainingDiscuss);
	}
	
	public void updateClubTrainingDiscussByKey(ClubTrainingDiscuss clubTrainingDiscuss){
		clubTrainingDiscussMapper .updateClubTrainingDiscussByKey(clubTrainingDiscuss);
	}
	
	public List<ClubTrainingDiscuss> selectSingleClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss){
		return clubTrainingDiscussMapper .selectSingleClubTrainingDiscuss(clubTrainingDiscuss);
	}
	
	public ClubTrainingDiscuss selectClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss){
		List<ClubTrainingDiscuss> clubTrainingDiscussList = clubTrainingDiscussMapper .selectSingleClubTrainingDiscuss(clubTrainingDiscuss);
		if(clubTrainingDiscussList == null || clubTrainingDiscussList .size() == 0){
			return null;
		}
		return clubTrainingDiscussList .get(0);
	}
	
	public List<ClubTrainingDiscuss> selectAllClubTrainingDiscuss(){
		return clubTrainingDiscussMapper .selectAllClubTrainingDiscuss();
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryCount(paramMap);
	}
	public List<ClubTrainingDiscuss> queryByPage(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryByPage(paramMap);
	}
	public Integer queryCountOne(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryCountOne(paramMap);
	}
	public List<ClubTrainingDiscuss> queryPageOne(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryPageOne(paramMap);
	}
	
	public Integer queryByCountnew(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryByCountnew(paramMap);
	}
	public List<ClubTrainingDiscuss> queryByPagenew(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryByPagenew(paramMap);
	}
	
	public Integer queryByCountOld(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryByCountOld(paramMap);
	}
	public List<ClubTrainingDiscuss> queryByPageOld(Map<String, Object> paramMap) {
		return clubTrainingDiscussMapper.queryByPageOld(paramMap);
	}

	public void delClubTrainingDiscussAll(List<ClubTrainingDiscuss> delClubTrainingDiscuss) {
		clubTrainingDiscussMapper.delClubTrainingDiscussAll(delClubTrainingDiscuss);
	}
}