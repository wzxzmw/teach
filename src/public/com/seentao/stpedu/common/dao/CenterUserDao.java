package com.seentao.stpedu.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.sqlmap.CenterUserMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class CenterUserDao {

	@Autowired
	private CenterUserMapper centerUserMapper;
	
	
	public void insertCenterUser(CenterUser centerUser)throws InsertObjectException{
		try {
			centerUserMapper .insertCenterUser(centerUser);
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "insertCenterUser", "insertCenterUser user error"+e);
			throw new InsertObjectException("insertCenterUser is error",e);
		}
	}
	
	public void deleteCenterUser(CenterUser centerUser){
		centerUserMapper .deleteCenterUser(centerUser);
	}
	
	public void updateCenterUserByKey(CenterUser centerUser)throws UpdateObjectException{
		try {
			centerUserMapper .updateCenterUserByKey(centerUser);
		} catch (UpdateObjectException e) {
			throw new UpdateObjectException("update object is error",e);
		}
		
	}
	
	public List<CenterUser> selectSingleCenterUser(CenterUser centerUser){
		return centerUserMapper.selectSingleCenterUser(centerUser);
	}
	
	public List<CenterUser> selectCenterByUserAnyThing(CenterUser centeruser){
		return centerUserMapper.selectCenterByUserAnyThing(centeruser);
	}
	public List<CenterUser> selectCenterUser(CenterUser centerUser){
		List<CenterUser> centerUserList = centerUserMapper .selectSingleCenterUser(centerUser);
		return centerUserList;
	}
	
	public CenterUser selectCenterUserOne(CenterUser centerUser){
		List<CenterUser> centerUserList = centerUserMapper .selectSingleCenterUser(centerUser);
		if(null!=centerUserList && centerUserList.size()!=0){
			return centerUserList.get(0);
		}else{
			return null;
		}
	}
	/**根据userId查询
	 * @param userId
	 * @return
	 */
	public CenterUser queryCenterUserByUserId(Integer userId){
		return centerUserMapper.queryCenterUserByUserId(userId);
	}
	
	public CenterUser selectCenterUserInfo(CenterUser centerUser){
		List<CenterUser> centerUserList = centerUserMapper .selectCenterUserInfo(centerUser);
		if(null!=centerUserList && centerUserList.size()!=0){
			return centerUserList.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 根据手机号码校验是否已经存在手机号
	 * @param phone
	 * @return
	 */
	public CenterUser selectCenterUserByPhone(String phone){
		List<CenterUser> ls = centerUserMapper.selectCenterUserByPhone(phone);
		if(CollectionUtils.isEmpty(ls)){
			return null;
		}else{
			return ls.get(0);
		}
	}
	public List<CenterUser> selectAllCenterUser(){
		return centerUserMapper .selectAllCenterUser();
	}

	public Integer getSchoolCount(CenterUser centerUser) {
		return centerUserMapper.getSchoolCount(centerUser);
	}
	public Integer queryCount(Map<String, Object> paramMap) {
		return centerUserMapper.queryCount(paramMap);
	}

	public List<CenterUser> queryByPage(Map<String, Object> paramMap) {
		return centerUserMapper.queryByPage(paramMap);
	}
	
	public HashMap<String, Object> selectUserClasses(CenterUser centerUser) {
		return centerUserMapper.selectUserClasses(centerUser);
	}

	public void updateCenterUserAll(List<CenterUser> stdentIdAll) {
		centerUserMapper.updateCenterUserAll(stdentIdAll);
	}

	public void updateCenterUserTeach(CenterUser updateUser) {
		centerUserMapper.updateCenterUserTeach(updateUser);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public CenterUser getEntityForDB(Map<String, Object> paramMap){
		CenterUser tmp = new CenterUser();
		tmp.setUserId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectCenterUserOne(tmp);
	}
	public Integer queryUserCount(CenterUser centerUser) {
		return centerUserMapper.queryCountCondition(centerUser);
	}

	public List<CenterUser> getUserList(CenterUser user) {
		return centerUserMapper.getUserList(user);
	}

	public Integer getUserListCount(CenterUser user) {
		return centerUserMapper.getUserListCount(user);
	}

	public List<CenterUser> getActiveAndRecommendUser(CenterUser centeruser) {
		return centerUserMapper.getActiveAndRecommendUser( centeruser) ;
	}
	/**查询班级里最活跃的10人
	 * 
	 * @param centeruser
	 * @return
	 */
	public List<CenterUser> queryActiveAndRecommendUser(CenterUser centeruser){
		return centerUserMapper.queryActiveAndRecommendUser( centeruser);
	}
	public List<CenterUser> getClubActiveUser(CenterUser centeruser) {
		return centerUserMapper.getClubActiveUser( centeruser) ;
	}

	public Integer queryClubActiveUserCount(CenterUser centeruser) {
		return centerUserMapper.queryClubActiveUserCount( centeruser) ;
	}

	public List<CenterUser> selectCenterUserByClubId(CenterUser user) {
		return centerUserMapper.selectCenterUserByClubId(user) ;
	}
	public void addAnswerNum(Integer userId) {
		centerUserMapper.addAnswerNum(userId);
	}

	public void addQuestionNum(Integer userId) {
		centerUserMapper.addQuestionNum(userId);
	}

	public CenterUser selectCenterUserTodayNum(CenterUser centerUser) {

		return centerUserMapper.selectCenterUserTodayNum(centerUser);
	}

	public List<CenterUser> selectCenterUserSource(CenterUser centeruser) {
		return centerUserMapper.selectCenterUserSource(centeruser);
	}

	public Integer queryUserSourceCount(CenterUser centeruser) {
		return centerUserMapper.queryUserSourceCount(centeruser);
	}

	public CenterUser selectAllCenterUsers(CenterUser centerUser) {
		return centerUserMapper.selectAllCenterUsers(centerUser);
	}

	public void updateUserLoginsByUserId(CenterUser c) throws UpdateObjectException{
		try {
			centerUserMapper.updateUserLoginsByUserId(c);
		} catch (UpdateObjectException e) {
			throw new UpdateObjectException("更新用户失败",e);
		}
	}

	/*public void updateCenterUserTodayClubNum(CenterUser centerUser) {

		centerUserMapper.updateCenterUserTodayClubNum(centerUser);
	}*/
	
	 /**
		 * 根据用户id判断昵称重复
		 * @param userId
		 * @param clubId
		 * @author lijin
		 */
		public boolean validateNickName(CenterUser centerUser){
			int num=centerUserMapper.validateNickName(centerUser);
			if(num==0){
				return true;
			}else{
				return false;
			}
		}

	public void updateCenterUser(CenterUser centerUser) {

		centerUserMapper.updateUser(centerUser);
	}

	public List<CenterUser> selectCenterByClubIdList(CenterUser centeruser) {
		return centerUserMapper.selectCenterByClubIdList(centeruser);
	}

	public Integer queryUserByClubIdCount(CenterUser centeruser) {
		return centerUserMapper.queryUserByClubIdCount(centeruser);
	}
	
	public List<Map<String,Object>> queryCenterUserMapByQuizId(CenterUser centerUser){
			return centerUserMapper.queryCenterUserMapByQuizId(centerUser);
	}
	/**
	 * 查询竞猜下注总条数
	 */
	public Integer queryCenterUserMapByQuizIdCount(CenterUser centerUser){
		return centerUserMapper.queryCenterUserMapByQuizIdCount(centerUser);
	}
	
	public List<CenterUser> selectCenterUserByClassId(Integer classId){
		return centerUserMapper.selectCenterUserByClassId(classId);
	}

	public CenterUser selectCenterUserByAll(String userName) {
		return centerUserMapper.selectCenterUserByAll(userName);
	}

}