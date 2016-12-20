package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubJoinTrainingDao;
import com.seentao.stpedu.common.entity.ClubJoinTraining;

@Service
public class ClubJoinTrainingService{
	
	@Autowired
	private ClubJoinTrainingDao clubJoinTrainingDao;
	
	public String getClubJoinTraining(ClubJoinTraining clubJoinTraining) {
		List<ClubJoinTraining> clubJoinTrainingList = clubJoinTrainingDao .selectSingleClubJoinTraining(clubJoinTraining);
		if(clubJoinTrainingList == null || clubJoinTrainingList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(clubJoinTrainingList);
	}
	public ClubJoinTraining getClubJoinTrainingAll(ClubJoinTraining clubJoinTraining) {
		List<ClubJoinTraining> clubJoinTrainingList = clubJoinTrainingDao .selectSingleClubJoinTraining(clubJoinTraining);
		if(clubJoinTrainingList == null || clubJoinTrainingList .size() <= 0){
			return null;
		}
		
		return clubJoinTrainingList.get(0);
	}
	
	/**
	 * 查询培训班人下的人 支持根据人员姓名搜索
	 * @param id
	 * @return
	 */
	public List<ClubJoinTraining> getClubJoinTrainingByClassId(ClubJoinTraining clubJoinTraining) {
		List<ClubJoinTraining> clubJoinTrainingList = clubJoinTrainingDao .selectClubJoinTrainingByClasdId(clubJoinTraining);
		if(clubJoinTrainingList == null || clubJoinTrainingList .size() <= 0){
			return null;
		}
		return clubJoinTrainingList;
	}
	
	/**
	 * 查询培训班人下的人 支持根据人员姓名搜索  数量
	 * @param id
	 * @return
	 */
	public Integer getClubJoinTrainingCount(ClubJoinTraining clubJoinTraining) {
		return  clubJoinTrainingDao .selectClubJoinTrainingCount(clubJoinTraining);
	}
	/**
	 * insertClubJoinTraining(加入培训班)   
	 * @author ligs
	 * @date 2016年6月26日 下午7:55:36
	 * @return
	 */
	public void insertClubJoinTraining(ClubJoinTraining clubJoinTraining){
		clubJoinTrainingDao.insertClubJoinTraining(clubJoinTraining);
	}
	/**
	 * updateClubJoinTrainingByKey(踢出培训班)
	 * @author ligs
	 * @date 2016年6月27日 上午10:35:25
	 * @return
	 */
	public void updateClubJoinTrainingByKey(ClubJoinTraining clubJoinTraining){
		clubJoinTrainingDao.updateClubJoinTrainingByKey(clubJoinTraining);
	}
	/**
	 * updateClubJoinTrainingByKeyAll(批量删除)   
	 * @author ligs
	 * @date 2016年7月6日 上午11:01:36
	 * @return
	 */
	public void updateClubJoinTrainingByKeyAll(List<ClubJoinTraining> upClubJoinTraining) {
		clubJoinTrainingDao.updateClubJoinTrainingByKeyAll(upClubJoinTraining);
	}
	public Integer getHasClubJoinTrainingCount(Integer userId) {
		return clubJoinTrainingDao.getHasClubJoinTrainingCount(userId);
	}
	
	/**班级id
	 * @param classId
	 * @param userId 用户Id
	 * @return
	 */
	public ClubJoinTraining queryClubJoinTrainingByClassId(Integer classId,Integer userId){
		return clubJoinTrainingDao.queryClubJoinTrainingByClassId(classId, userId);
	}
	public void updateClubJoinTrainingUdToCost(ClubJoinTraining addClubJoinTraining) {
		clubJoinTrainingDao.updateClubJoinTrainingUdToCost(addClubJoinTraining);
	}
	
}