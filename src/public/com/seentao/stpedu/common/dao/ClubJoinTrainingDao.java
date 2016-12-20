package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.sqlmap.ClubJoinTrainingMapper;


@Repository
public class ClubJoinTrainingDao {

	@Autowired
	private ClubJoinTrainingMapper clubJoinTrainingMapper;
	
	
	public void insertClubJoinTraining(ClubJoinTraining clubJoinTraining){
		clubJoinTrainingMapper .insertClubJoinTraining(clubJoinTraining);
	}
	
	public void deleteClubJoinTraining(ClubJoinTraining clubJoinTraining){
		clubJoinTrainingMapper .deleteClubJoinTraining(clubJoinTraining);
	}
	
	public void updateClubJoinTrainingByKey(ClubJoinTraining clubJoinTraining){
		clubJoinTrainingMapper .updateClubJoinTrainingByKey(clubJoinTraining);
	}
	
	public List<ClubJoinTraining> selectSingleClubJoinTraining(ClubJoinTraining clubJoinTraining){
		return clubJoinTrainingMapper .selectSingleClubJoinTraining(clubJoinTraining);
	}
	
	public ClubJoinTraining selectClubJoinTraining(ClubJoinTraining clubJoinTraining){
		List<ClubJoinTraining> clubJoinTrainingList = clubJoinTrainingMapper .selectSingleClubJoinTraining(clubJoinTraining);
		if(clubJoinTrainingList == null || clubJoinTrainingList .size() == 0){
			return null;
		}
		return clubJoinTrainingList .get(0);
	}
	
	public List<ClubJoinTraining> selectAllClubJoinTraining(){
		return clubJoinTrainingMapper .selectAllClubJoinTraining();
	}

	public List<ClubJoinTraining> selectClubJoinTrainingByClasdId(ClubJoinTraining clubJoinTraining) {
		return clubJoinTrainingMapper.selectClubJoinTrainingByClasdId(clubJoinTraining);
	}

	public Integer selectClubJoinTrainingCount(ClubJoinTraining clubJoinTraining) {
		return clubJoinTrainingMapper.selectClubJoinTrainingCount(clubJoinTraining);
	}

	public void updateClubJoinTrainingByKeyAll(List<ClubJoinTraining> upClubJoinTraining) {
		clubJoinTrainingMapper.updateClubJoinTrainingByKeyAll(upClubJoinTraining);
	}

	public Integer getHasClubJoinTrainingCount(Integer userId) {
		return clubJoinTrainingMapper.getHasClubJoinTrainingCount(userId);
	}
	
	public Integer queryClubJoinCountAll(Map<String, Object> paramMap) {
		return clubJoinTrainingMapper.queryClubJoinCountAll(paramMap);
	}

	public List<ClubJoinTraining> queryClubJoinByPageAll(Map<String, Object> paramMap) {
		return clubJoinTrainingMapper.queryClubJoinByPageAll(paramMap);
	}
	/**
	 * @param classId
	 * @param userId
	 * @return 根据用户id和班级id查询是否加入班级
	 */
	public ClubJoinTraining queryClubJoinTrainingByClassId(Integer classId,Integer userId){
		return clubJoinTrainingMapper.queryClubJoinTrainingByClassId(classId, userId);
	}

	public void updateClubJoinTrainingUdToCost(ClubJoinTraining addClubJoinTraining) {
		clubJoinTrainingMapper.updateClubJoinTrainingUdToCost(addClubJoinTraining);
	}
}