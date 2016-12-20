package com.seentao.stpedu.teacher.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.PublicPictureService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.common.service.TeachSchoolService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;


/**
 * @author ligs
 */
@Service
public class GetMessageService {
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private TeachSchoolService teachSchoolService;
	
	@Autowired
	private PublicPictureService publicPictureService;
	
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;//培训班表
	
	@Autowired
	private TeachClassService teachClassService;
	
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;
	/**
	 * getTeachers(获取教师信息)   
	 * @param classId 班级id  
	 * @param teacherId 教师id
	 * @param start 分页的开始页(从0开始)
	 * @param limit 每页数量
	 * @param inquireType 查询类型 1：学生端未加入班级时获取教师列表，作为筛选班级的查询条件   2：通过教师id获取唯一一条教师信息   3：通过班级id获取唯一一条教师信息
	 * @author ligs
	 * @param classType 班级类型 1：教学班  2：俱乐部培训班
	 * @date 2016年6月17日 下午3:16:34
	 * @return 
	 */
	public JSONObject getTeachers(Integer classId, Integer teacherId, Integer start, Integer limit, Integer inquireType,Integer userId, Integer classType) {
		if(inquireType == 1){
			//1：学生端未加入班级时获取教师列表
			return this.teachersOne(userId, limit, start);
		}else if(inquireType == 2){
			//2：通过教师id获取唯一一条教师信息
			return this.teachersTwo(teacherId);
		}else if(inquireType == 3){ 
			//3：通过班级id获取唯一一条教师信息
			return this.teachersTherr(classType, classId);
		}
		return null;
	}
	
	
	/**
	 * inquireType 1：学生端未加入班级时获取教师列表
	 * @return
	 */
	public JSONObject teachersOne(Integer userId,Integer limit,Integer start){
		//根据学生id获得所在学校
		CenterUser centerUsers = centerUserService.getCenterUserById(userId);
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("userType", GameConstants.USER_TYPE_TEAHCER);
		paramMap.put("schoolId", centerUsers.getSchoolId());
		//调用分页组件
		QueryPage<CenterUser> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, CenterUser.class);
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getTeachers", "获取教师列表失败");
			appQueryPage.error(AppErrorCode.ERROR_TEACH_GET);
		 }
		for (CenterUser centerUser : appQueryPage.getList()) {
			centerUser.setTeacherId(centerUser.getUserId());
			//通过教师头像id获得头像图片地址
			String headImgId = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(headImgId)){
				/*
				 * compress picture
				 */
				centerUser.setTeacherHeadLink(Common.checkPic(headImgId) == true ? headImgId+ActiveUrl.HEAD_MAP:headImgId );
			}
			centerUser.setTeacherId(centerUser.getUserId());//教师id
			centerUser.setTeacherName(centerUser.getRealName());//教师名称
		}
		LogUtil.info(this.getClass(),"getTeachers", "成功");
		return appQueryPage.getMessageJSONObject("teachers");
	}
	
	
	
	/**
	 * 2：通过教师id获取唯一一条教师信息
	 * @return
	 */
	public JSONObject teachersTwo(Integer teacherId){
		CenterUser returnCenterUser = new CenterUser();
		//查询用户表
		CenterUser centerUser = new CenterUser();
		centerUser.setUserType(GameConstants.USER_TYPE_TEAHCER);
		centerUser.setUserId(teacherId);
		CenterUser centerUserAll = centerUserService.selectCenterUser(centerUser);
		if(centerUserAll == null){
			LogUtil.error(this.getClass(),"getTeachers", "教师不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_TEACHER);
		}
		//通过教师头像id获得头像图片地址
		String headImgId = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
		if(!StringUtil.isEmpty(headImgId)){
			
			returnCenterUser.setTeacherHeadLink(Common.checkPic(headImgId) == true ? headImgId+ActiveUrl.HEAD_MAP:headImgId );
		}
		//根据用户表的学校Id查询学校表获得学校名称
		TeachSchool teachSchool = new TeachSchool();
		teachSchool.setSchoolId(centerUserAll.getSchoolId());
		TeachSchool teachSchoolAll = teachSchoolService.selectSingleTeachSchool(teachSchool);
		if(teachSchoolAll == null && "".equals(teachSchoolAll)){
			LogUtil.error(this.getClass(),"getTeachers", "没有对应学校");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_SCHOOL);
		}
		//组装前台JSON数据
		returnCenterUser.setTeacherSchool(teachSchoolAll.getSchoolName());//教师所在学校
		returnCenterUser.setTeacherDesc(centerUserAll.getDescription());//教师介绍
		returnCenterUser.setTeacherId(centerUserAll.getUserId());//教师id
		returnCenterUser.setTeacherName(centerUserAll.getRealName());//教师名称
		JSONArray json = new JSONArray();
		json.add(returnCenterUser);
		JSONObject returns = new JSONObject();
		returns.put("teachers", json);
		LogUtil.info(this.getClass(), "getTeachers", "成功");
		return Common.getReturn(AppErrorCode.SUCCESS, "",returns);
	}
	
	
	/**
	 * 3：通过班级id获取唯一一条教师信息
	 * @return
	 */
	public JSONObject teachersTherr(Integer classType,Integer classId){
		if(classType == GameConstants.TEACHING_CLASS){//教学班
			//通过班级ID查询班级是否存在
			TeachClass seteachClass =new TeachClass();
			seteachClass.setClassId(classId);
			seteachClass.setIsDelete(GameConstants.NO_DEL);
			TeachClass teachClass = teachClassService.selectSingleTeachClass(seteachClass);
			if(teachClass == null){
				LogUtil.error(this.getClass(), "getTeachers", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASS_NOT);
			}else {
					JSONObject teachers = new JSONObject();
					//通过班级ID查询教师班级关系表获得教师ID
					TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
					teachRelTeacherClass.setClassId(classId);
					TeachRelTeacherClass isteachRelTeacherClass = teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					CenterUser centerUserAll = null;
					//通过教师ID查询用户表获得教师信息
					if(isteachRelTeacherClass != null && isteachRelTeacherClass.getTeacherId() != null){
						String centerUserMessage = JedisCache.getRedisVal(null, CenterUser.class.getSimpleName(), isteachRelTeacherClass.getTeacherId().toString());
						if(centerUserMessage !=null && !"".equals(centerUserMessage)){
							centerUserAll = JSONObject.parseObject(centerUserMessage , CenterUser.class);
						}else {
							CenterUser centerUser = new CenterUser();
							centerUser.setUserId(isteachRelTeacherClass.getTeacherId());
							centerUserAll = centerUserService.selectCenterUser(centerUser);
						}
					}
					//通过教师头像id获得头像图片地址
					String headImgId = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(headImgId)){
						teachers.put("teacherHeadLink", Common.checkPic(headImgId) == true ? headImgId+ActiveUrl.HEAD_MAP:headImgId);//教师头像链接地址
					}
					//根据用户表的学校Id查询学校表获取学校名称
					if(centerUserAll.getSchoolId() != null && centerUserAll.getSchoolId() != 0){
						TeachSchool teachSchool = new TeachSchool();
						teachSchool.setSchoolId(centerUserAll.getSchoolId());
						TeachSchool teachSchoolAll = teachSchoolService.selectSingleTeachSchool(teachSchool);
						if(teachSchoolAll != null && teachSchoolAll.getSchoolName() != null){
							teachers.put("teacherSchool", teachSchoolAll.getSchoolName());//教师所在学校
						}
					}
					if(isteachRelTeacherClass.getTeacherId() != null){
						teachers.put("teacherId", isteachRelTeacherClass.getTeacherId());//教师id
					}
					if(centerUserAll.getRealName() != null){
						teachers.put("teacherName",centerUserAll.getRealName());//教师名称
					}
					if(centerUserAll.getDescription() != null){
						teachers.put("teacherDesc",centerUserAll.getDescription());//教师介绍
					}
					JSONArray json = new JSONArray();
					json.add(teachers);
					JSONObject result = new JSONObject();
					result.put("teachers", json);
					LogUtil.info(this.getClass(), "getTeachers", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "",result);
			}
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){//俱乐部培训班
			//通过培训班ID查询培训班表判断培训班是否存在并获得创建者ID
			ClubTrainingClass  clubTrainingClass = new ClubTrainingClass();
			clubTrainingClass.setClassId(classId);
			clubTrainingClass.setIsDelete(GameConstants.NO_DEL);
			ClubTrainingClass  clubTrainingClassAll =clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
			if(clubTrainingClassAll == null){
				LogUtil.error(this.getClass(), "getTeachers", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT );
			}
			//通过创建者ID查询用户表获得  教师ID 昵称  所在学校 介绍  教师头像链接地址
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(clubTrainingClassAll.getCreateUserId());
			CenterUser centerUserAll =  centerUserService.selectCenterUser(centerUser);
			if(centerUserAll == null){
				LogUtil.error(this.getClass(), "getTeachers", "教师不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_GET_TEACHER);
			}
			//根据用户的学校ID查询学校表获得学校名称
			TeachSchool teachSchool = new TeachSchool();
			teachSchool.setSchoolId(centerUserAll.getSchoolId());
			TeachSchool teachSchoolAll = teachSchoolService.selectSingleTeachSchool(teachSchool);
			CenterUser returnCenterUser = new CenterUser();
			//通过教师头像id获得头像图片地址
			String headImgId = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(headImgId)){
				returnCenterUser.setTeacherHeadLink(Common.checkPic(headImgId) == true ? headImgId+ActiveUrl.HEAD_MAP:headImgId );//头像链接地址
			}
			returnCenterUser.setTeacherId(centerUserAll.getUserId());//教练ID
			returnCenterUser.setTeacherName(centerUserAll.getNickName());//教练名称
			returnCenterUser.setTeacherSchool(teachSchoolAll.getSchoolName());//教师所在学校
			returnCenterUser.setTeacherDesc(centerUserAll.getDescription());//教师介绍
			LogUtil.info(this.getClass(), "getTeachers", "成功");
			JSONArray json = new JSONArray();
			json.add(returnCenterUser);
			JSONObject returns = new JSONObject();
			returns.put("teachers", json);
			return Common.getReturn(AppErrorCode.SUCCESS, "" ,returns);
		}
		return null;
	}
}
