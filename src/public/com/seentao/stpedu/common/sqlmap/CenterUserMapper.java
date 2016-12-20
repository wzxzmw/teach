package com.seentao.stpedu.common.sqlmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.TeachStudentHomework;


public interface CenterUserMapper {

	public abstract void insertCenterUser(CenterUser centerUser)throws InsertObjectException;
	
	public abstract void deleteCenterUser(CenterUser centerUser);
	
	public abstract void updateCenterUserByKey(CenterUser centerUser)throws UpdateObjectException;
	
	public abstract List<CenterUser> selectSingleCenterUser(CenterUser centerUser);
	
	/**根据userId查询
	 * @param userId
	 * @return
	 */
	public abstract CenterUser queryCenterUserByUserId(@Param("userId")Integer userId);
	
	public abstract List<CenterUser> selectCenterByUserAnyThing(CenterUser centeruser);
	
	public abstract List<CenterUser> selectAllCenterUser();

	public abstract Integer getSchoolCount(CenterUser centerUser);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<CenterUser> queryByPage(Map<String, Object> paramMap);

	public abstract HashMap<String, Object> selectUserClasses(CenterUser centerUser);

	public abstract Integer queryCountAll(Map<String, Object> paramMap);

	public abstract List<TeachStudentHomework> queryByPageAll(Map<String, Object> paramMap);

	public abstract void updateCenterUserAll(List<CenterUser> stdentIdAll);

	public abstract void updateCenterUserTeach(CenterUser updateUser);

	public abstract Integer queryCountCondition(CenterUser centerUser);

	public abstract List<CenterUser> getUserList(CenterUser user);

	public abstract Integer getUserListCount(CenterUser user);

	public abstract List<CenterUser> getActiveAndRecommendUser(CenterUser centeruser);
	/**查询班级里最活跃的10人
	 * 
	 * @param centeruser
	 * @return
	 */
	public abstract List<CenterUser> queryActiveAndRecommendUser(CenterUser centeruser);

	public abstract List<CenterUser> getClubActiveUser(CenterUser centeruser);

	public abstract Integer queryClubActiveUserCount(CenterUser centeruser);

	public abstract List<CenterUser> selectCenterUserByClubId(CenterUser user);
	
	public abstract void addAnswerNum(Integer userId);

	public abstract void addQuestionNum(Integer userId);

	public abstract CenterUser selectCenterUserTodayNum(CenterUser centerUser);

	public abstract List<CenterUser> selectCenterUserSource(CenterUser centeruser);

	public abstract Integer queryUserSourceCount(CenterUser centeruser);

	public abstract CenterUser selectAllCenterUsers(CenterUser centerUser);

	public abstract void updateUserLoginsByUserId(CenterUser c) throws UpdateObjectException;

	/*public abstract void updateCenterUserTodayClubNum(CenterUser centerUser);*/
	
	public abstract Integer validateNickName(CenterUser centerUser);

	public abstract void updateUser(CenterUser centerUser);

	public abstract List<CenterUser> selectCenterUserInfo(CenterUser centerUser);
	
	public abstract List<CenterUser> selectCenterUserByPhone(@Param("phone")String phone);

	public abstract List<CenterUser> selectCenterByClubIdList(CenterUser centeruser);

	public abstract Integer queryUserByClubIdCount(CenterUser centeruser);
	
	public abstract List<Map<String,Object>> queryCenterUserMapByQuizId(CenterUser centerUser);
	
	public abstract Integer queryCenterUserMapByQuizIdCount(CenterUser centerUser);
	
	public abstract List<CenterUser> selectCenterUserByClassId(Integer classId);

	public abstract CenterUser selectCenterUserByAll(@Param("userName")String userName);

	
}