package com.seentao.stpedu.common.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubTrainingClassDao;
import com.seentao.stpedu.common.entity.ClubTrainingClass;

@Service
public class ClubTrainingClassService{
	
	@Autowired
	private ClubTrainingClassDao clubTrainingClassDao;
	
	public ClubTrainingClass getClubTrainingClass(ClubTrainingClass clubTrainingClass) {
		List<ClubTrainingClass> clubTrainingClassList = clubTrainingClassDao .selectSingleClubTrainingClass(clubTrainingClass);
		if(clubTrainingClassList == null || clubTrainingClassList .size() <= 0){
			return null;
		}
		
		return clubTrainingClassList.get(0);
	}
	/**
	 * updateClubTrainingClassByKey(修改培训班表)   
	 * @author ligs
	 * @date 2016年6月26日 下午5:02:40
	 * @return
	 */
	public void updateClubTrainingClassByKey(ClubTrainingClass clubTrainingClass){
		clubTrainingClassDao.updateClubTrainingClassByKey(clubTrainingClass);
	}
	/**
	 * insertClubTrainingClass(创建培训班)   
	 * @param userName 用户名
	 * @param userId 用户id
	 * @param userType 用户类型
	 * @param userToken 用户唯一标识
	 * @author ligs
	 * @date 2016年6月26日 下午5:28:00
	 * @return
	 */
	public Integer insertClubTrainingClass(ClubTrainingClass clubTrainingClass){
		return clubTrainingClassDao.returninsertClubTrainingClass(clubTrainingClass);
	}
	
	/**
	 * 根据课程类型获取官方课程类型
	 * @return
	 * TODO:redis
	 */
	public List<Integer> getClassByCourseType(int classType){
		return clubTrainingClassDao.getClassByCourseType(classType);
	}
	public void updateClubTrainingClassNum(ClubTrainingClass updateClubTrainingClass) {
		clubTrainingClassDao.updateClubTrainingClassNum(updateClubTrainingClass);
	}
	/**
	 * 修改课程数量
	 * @param i
	 * @param classId
	 */
	public void updateClubTrainingClassNumByKey(ClubTrainingClass updateClubTrainingClass) {
		clubTrainingClassDao.updateClubTrainingClassNumByKey(updateClubTrainingClass);
	}
	
	
}