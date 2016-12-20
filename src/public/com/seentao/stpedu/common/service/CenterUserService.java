package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterUserDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.RedisUserPhone;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/**
 *center_user 用户表基本操作
 */
@Service
public class CenterUserService{
	public static final String PROTOCOL_LINK = "http://note.youdao.com/yws/public/redirect/share?id=0d2e0882082d4d8de80f162295b0913b&type=false";
	
	@Autowired
	private CenterUserDao centerUserDao;
	
	@Autowired
	private CenterSmsVerificationService centerSmsVerificationService;
	/**
	 * 条件查询用户信息
	 * @param id
	 */
	public List<CenterUser> selectCenterUserList(CenterUser centeruser){
		List<CenterUser> centerUserList = centerUserDao.selectSingleCenterUser(centeruser);
		if(centerUserList == null || centerUserList .size() <= 0){
			return null;
		}else{
			return centerUserList;
		}
	}
	
	public List<CenterUser> selectCenterByUserAnyThing(CenterUser centeruser){
		return centerUserDao.selectCenterByUserAnyThing(centeruser);
	}
	/**
	 * 根据用户id查询出用户信息
	 * @param id
	 */
	public CenterUser getCenterUserById(Integer userId) {
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		CenterUser centerUsers = centerUserDao.selectAllCenterUsers(centerUser);
		return centerUsers;
	}
	
	/**
	 * 查询全部用户信息
	 * @param id
	 */
	public List<CenterUser> selectAllCenterUser(){
		List<CenterUser> centerUserList = centerUserDao.selectAllCenterUser();
		if(centerUserList == null || centerUserList .size() <= 0){
			return null;
		}else{
			return centerUserList;
		}
	}
	
	/**
	 * 判断用户是否是教师
	 * @param userId
	 * @return
	 */
	public boolean isTeacher(Integer userId){
		String userCache = RedisComponent.findRedisObject(userId, CenterUser.class);
		if(StringUtil.isEmpty(userCache)){
			return false;
		}else{
			CenterUser centeruser = JSONObject.parseObject(userCache, CenterUser.class);
			if(centeruser.getUserType()==1){
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * 根据用户id和用户类型查询用户信息
	 * @author  ligs
	 */
	public CenterUser selectCenterUser(CenterUser centerUser){
		return centerUserDao.selectCenterUserOne(centerUser);
	}
	
	/**根据userId查询
	 * @param userId
	 * @return
	 */
	public CenterUser queryCenterUserByUserId(Integer userId){
		return centerUserDao.queryCenterUserByUserId(userId);
	}
	
	public CenterUser selectCenterUserInfo(CenterUser centerUser){
		return centerUserDao.selectCenterUserInfo(centerUser);
	}
	/**
	 * 根据手机号码校验是否已经存在手机号
	 * @param phone
	 * @return
	 */
	public CenterUser selectCenterUserByPhone(String phone){
		return centerUserDao.selectCenterUserByPhone(phone);
	}
	/**
	 * updateCenterUserByKey(学生加入班级)   
	 * @author ligs
	 * @date 2016年6月22日 下午3:29:39
	 * @return
	 */
	public void updateCenterUserByKey(CenterUser centerUser)throws UpdateObjectException{
	try {
		centerUserDao.updateCenterUserByKey(centerUser);
	} catch (UpdateObjectException e) {
		throw new UpdateObjectException("update object is error",e);
	}
		
	}
	/**
	 * updateCenterUserAll(学生批量加入班级)   
	 * @author ligs
	 * @date 2016年6月22日 下午3:29:39
	 * @return
	 */
	public void updateCenterUserAll(List<CenterUser> stdentIdAll){
		centerUserDao.updateCenterUserAll(stdentIdAll);
	}
	/**
	 * teachUpdateCenterUser(把学生踢出班级)   
	 * @author ligs
	 * @date 2016年6月23日 下午10:09:25
	 * @return
	 */
	public void updateCenterUserTeach(CenterUser updateUser) {
		centerUserDao.updateCenterUserTeach(updateUser);
	}
	

	public Integer queryUserCount(CenterUser centerUser) {
		return centerUserDao.queryUserCount(centerUser);
	}

	public List<CenterUser> getUserList(CenterUser user) {
		return centerUserDao.getUserList(user);
	}

	public Integer getUserListCount(CenterUser user) {
		return centerUserDao.getUserListCount(user);
	}
	
	/**
	 * 查询班级里活跃的人 或者推荐的人
	 * @param centeruser
	 * @return
	 */
	public List<CenterUser> getActiveAndRecommendUser(CenterUser centeruser) {
		return centerUserDao.getActiveAndRecommendUser( centeruser);
	}

	/**查询班级里最活跃的10人
	 * 
	 * @param centeruser
	 * @return
	 */
	public List<CenterUser> queryActiveAndRecommendUser(CenterUser centeruser){
		return centerUserDao.queryActiveAndRecommendUser( centeruser);
	}
	public List<CenterUser> getClubActiveUser(CenterUser centeruser) {
		return centerUserDao.getClubActiveUser( centeruser);
	}

	public Integer queryClubActiveUserCount(CenterUser centeruser) {
		return centerUserDao.queryClubActiveUserCount( centeruser);
	}

	public void insertCenterUser(CenterUser c) throws InsertObjectException{
		try {
			centerUserDao.insertCenterUser(c);
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "insertCenterUser", "insertCenterUser user error"+e);
			throw new InsertObjectException("insertCenterUser is error",e);
			// TODO: handle exception
		}
	}
	/**
	 * 根据竞猜quizId查询竞猜详情
	 * @param centerUser
	 * @return
	 */
	public List<Map<String,Object>> queryCenterUserMapByQuizId(CenterUser centerUser){
		return centerUserDao.queryCenterUserMapByQuizId(centerUser);
	}
	
	/**
	 * 查询竞猜下注总条数
	 */
	public Integer queryCenterUserMapByQuizIdCount(CenterUser centerUser){
		return centerUserDao.queryCenterUserMapByQuizIdCount(centerUser);
	}

	/** 
	* @Title: selectCenterUserByClubId 
	* @Description: 根据俱乐部ID获得用户ID列表
	* @param user
	* @return List<CenterUser>
	* @author liulin
	* @date 2016年6月29日 下午2:48:09
	*/
	public List<CenterUser> selectCenterUserByClubId(CenterUser user) {
		return centerUserDao.selectCenterUserByClubId(user);
	}
	/**
	 * 培训班/教师：用户回复信息加一 并删除用户缓存
	 * @param userId	用户id
	 * @author 			lw
	 * @date			2016年6月29日  下午2:12:12
	 */
	public void addAnswerNum(Integer userId) {
		
		//用户回复信息加一
		centerUserDao.addAnswerNum(userId);
		//删除用户缓存
		JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(userId));
		
	}

	/**
	 * 用户提问数量加一，并删除用户缓存
	 * @param userId
	 * @author 			lw
	 * @date			2016年6月29日  下午2:28:36
	 */
	public void addQuestionNum(Integer userId) {
		//用户回复数量加一
		centerUserDao.addQuestionNum(userId);
		//删除用户缓存
		JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(userId));
	}

	/** 
	* @Title: updateUserDesc 
	* @Description: 提交个人签名
	* @param userId
	* @param desc
	* @return  参数说明 
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午3:26:39
	*/
	public String updateUserDesc(Integer userId, String desc) {
		try{
			String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
			CenterUser oldUser = JSONObject.parseObject(userRedis, CenterUser.class);
			if(oldUser != null){
				CenterUser newUser = new CenterUser();
				newUser.setUserId(oldUser.getUserId());
				newUser.setDescription(desc);
				centerUserDao.updateCenterUserByKey(newUser);
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(newUser.getUserId()));
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "updateUserDesc", "提交个人签名失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_SUBMIT_USER_DESC).toJSONString();
		}
		return null;
	}

	/** 
	* @Title: addBindingNumber 
	* @Description: 绑定手机号码
	* @param userId	用户ID
	* @param oldBindingNumber	旧手机号码
	* @param newBindingNumber	新手机号码
	* @param verifyPictureNumber	图片验证码
	* @param verifyNumber	手机验证码
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午4:02:33
	*/
	public String addBindingNumber(Integer userId, String oldBindingNumber, String newBindingNumber,
			String verifyPictureNumber, String verifyNumber) {
		try{
			//比较验证码
			boolean flag = centerSmsVerificationService.compareNumber(newBindingNumber, verifyNumber);
			if(flag){
				//获取用户信息
				String userRedis = RedisComponent.findRedisObject(userId, CenterUser.class);
				CenterUser oldUser = JSONObject.parseObject(userRedis, CenterUser.class);
				if(oldUser != null && oldUser.getPhone().equals(oldBindingNumber)){
					CenterUser newUser = new CenterUser();
					newUser.setUserId(userId);
					newUser.setPhone(newBindingNumber);
					//newUser.setReplacePhone(newBindingNumber);
					centerUserDao.updateCenterUserByKey(newUser);
					/*
					 * 根据手机绑定变换
					 */
					JedisUserCacheUtils.setCheckLoginHash(oldUser.getPhone().trim(), JSON.toJSONString(new RedisUserPhone(oldUser.getPhone().trim(), "", 0, 2)));
					JedisUserCacheUtils.setCheckLoginHash(newBindingNumber.trim(), JSON.toJSONString(new RedisUserPhone(newBindingNumber.trim(), "", 0, 1)));
					/*
					 * 
					 */
					CenterUser us =JSONObject.parseObject(JedisUserCacheUtils.getRegisterUserHash(CenterUser.class.getSimpleName(), oldBindingNumber), CenterUser.class);
					if(us != null){
						us.setPhone(newBindingNumber);
						JedisUserCacheUtils.setRegisterUserHash(CenterUser.class.getSimpleName(), newBindingNumber , JSON.toJSONString(us));
					}
					JedisUserCacheUtils.hdelRegisterUserHashByPhone(oldBindingNumber);
					JedisUserCacheUtils.hdelCenterUserByPhoneHash(oldBindingNumber);
					//TODO 
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(newUser.getUserId()));
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}else{
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_FAIL_USER_PHONE).toJSONString();
				}
				
			}else{
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_USER_VERIFY_NUMBER).toJSONString();
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "addBindingNumber", "绑定手机号失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ADD_USER_PHONE).toJSONString();
		}
	}

	/** 
	* @Title: getProtocol 
	* @Description: 获取协议内容
	* @param userId	用户ID
	* @param protocolType	协议类型
	* @return String
	* @author liulin
	* @date 2016年6月30日 下午8:22:14
	*/
	public String getProtocol(Integer userId, Integer protocolType) {
		//TODO 等刘红卫提供
		JSONObject response = new JSONObject();
		String protocolContent = "";
		String protocolLink = "";
		if(protocolType==1){
			//protocolContent = GameConstants.PROTOCOL_CONTENT_ONE;
			protocolLink = PROTOCOL_LINK;
		}else if(protocolType==2){
			//protocolContent = GameConstants.PROTOCOL_CONTENT_TWO;
			protocolLink = PROTOCOL_LINK;
		}else if(protocolType==3){
			//protocolContent = GameConstants.PROTOCOL_CONTENT_THREE;
			protocolLink = PROTOCOL_LINK;
		}else if(protocolType==4){
			//protocolContent = GameConstants.PROTOCOL_CONTENT_FOUR;
			protocolLink = PROTOCOL_LINK;
		}else if(protocolType==5){
			//protocolContent = GameConstants.PROTOCOL_CONTENT_FIVE;
			protocolLink = PROTOCOL_LINK;
		}
		Map<String, Object> backParam = new HashMap<String, Object>();
		backParam.put("protocolContent", protocolContent);
		backParam.put("protocolLink", protocolLink);
		return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
	}

	public List<CenterUser> selectCenterUserSource(CenterUser centeruser) {
		return centerUserDao.selectCenterUserSource(centeruser);
	}

	public Integer queryUserSourceCount(CenterUser centeruser) {
		return centerUserDao.queryUserSourceCount(centeruser);
	}

	/**
	 * 用户加入俱乐部更新用户俱乐部id
	 * @param userId
	 * @param clubId
	 * @author cxw
	 */
    public void updateCenterUser(String userId,String clubId){
    	try {
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(Integer.valueOf(userId));
			centerUser.setClubId(Integer.valueOf(clubId));
			centerUserDao.updateCenterUserByKey(centerUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
	 * 用户退出俱乐部更新用户俱乐部id
	 * @param userId
	 * @param clubId
	 * @author cxw
	 */
    public void quitCenterUser(Integer userId){
    	try {
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			centerUser.setClubId(null);
			centerUserDao.updateCenterUser(centerUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public CenterUser selectAllCenterUsers(CenterUser centerUser) {
		return centerUserDao.selectAllCenterUsers(centerUser);
	}

	public void updateUserLoginsByUserId(CenterUser c) throws UpdateObjectException  {
		try {
			centerUserDao.updateUserLoginsByUserId(c);
		} catch (UpdateObjectException e) {
			throw new UpdateObjectException("service UpdateObjectException is error",e);
		}
	}
    /**
	 * 根据用户id判断昵称重复
	 * @param userId
	 * @param clubId
	 * @author lijin
	 */
	public boolean validateNickName(CenterUser centerUser){
		
		return centerUserDao.validateNickName(centerUser);
	}

	public List<CenterUser> selectCenterByClubIdList(CenterUser centeruser) {
		return centerUserDao.selectCenterByClubIdList(centeruser);
	}

	public Integer queryUserByClubIdCount(CenterUser centeruser) {
		return centerUserDao.queryUserByClubIdCount(centeruser);
	}
	
	public List<CenterUser> selectCenterUserByClassId(Integer classId){
		return centerUserDao.selectCenterUserByClassId(classId);
	}

	public CenterUser selectCenterUserByAll(String userName) {
		return centerUserDao.selectCenterUserByAll(userName);
	}
	
}