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
import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachInstitute;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubJoinTrainingService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.PublicPictureService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachInstituteService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.common.service.TeachSchoolService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

/**
 * 获取班级信息
 * <pre>项目名称：stpedu    
 * GetClassesController    
 */
@Service
public class GetClassesService {
	
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private TeachClassService teachClassService;//班级表
	
	@Autowired
	private PublicPictureService publicPictureService;//图片表
	
	@Autowired
	private TeachSchoolService teachSchoolService;//学校表
	
	@Autowired
	private TeachInstituteService teachInstituteService;//学院表
	
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;
	
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;//俱乐部培训班表
	
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;//参加培训人员表
	
	
	@Autowired
	private ClubMainDao clubMainDao;
	
	@Autowired
	private ClubMemberService clubMemberService;
	/**
	 * getClasses(获取班级信息) 
	 * @param userId 用户Id  
	 * @param schoolId 学校id
	 * @param teacherId 教师id
	 * @param classId 班级id
	 * @param classType 班级类型 1：教学班 2：俱乐部培训班
	 * @param clubId 俱乐部id
	 * @param searchWord 搜素词
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param start 分页的开始页
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @author ligs
	 * @param memberId 人员ID
	 * @date 2016年6月20日 下午9:30:42
	 * @return
	 */
	public JSONObject getClasses(Integer userId,Integer schoolId, Integer teacherId, Integer classId, String searchWord, Integer start,Integer limit, Integer inquireType,Integer clubId,Integer sortType,Integer classType, Integer memberId,Integer requestSide) {
		if(inquireType == GameConstants.YES_ADD_CLASS){
			//1：学生端，显示可加入的班级列表
			return this.classesOne( userId, schoolId,  teacherId,  searchWord,  start, limit);
		}else if(inquireType == GameConstants.ONESELF_CLASS){
			//2：教师端，显示自己创建的班级列表
			return this.getClassTwo( userId,  start, limit);
		}else if(inquireType == GameConstants.ACQUISITION_ONE_CLASS){
			//3.通过班级id获取唯一一条班级信息
			return this.getClassThree(classType, classId, userId);
		}else if(inquireType == GameConstants.RANKING_CLASS){
			//4；教学模块下的学校菜单，点击学校进入该学校下的班级排名列表页时调用(获取教学班)
			return this.getClassFour(schoolId, sortType, limit, start);
		}else if(inquireType == GameConstants.CLUB_CLASS){
			//5:获取俱乐部下的培训班列表
			return this.getClassFive(clubId, userId, limit, start, requestSide);
		}else if(inquireType == GameConstants.COURSE_TRAINING_CLASS){
			//6:教学模块下，自由用户点击“课程”菜单会显示全站的培训班信息
			return this.getClassSix(userId, limit, start);
		}else if(inquireType == GameConstants.TRAIN_CLASS){
			//7:获取当前登录者参加的培训班列表，使用场景有：1.个人中心－》我参加的培训班
			return this.getClassSeven(memberId, userId, limit, start);
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * getClassesForMobile(移动端：获取班级信息)  
	 * @param userId 用户id 
	 * @param schoolId 学校id
	 * @param teacherId 教师id
	 * @param classId 班级id
	 * @param classType 班级类型 1：教学班 2：俱乐部培训班
	 * @param clubId 俱乐部id
	 * @param searchWord 搜素词
	 * @param start 分页的开始页
	 * @param limit 每页数量
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param inquireType 查询类型
	 * @author ligs
	 * @date 2016年7月19日 上午9:51:42
	 * @return
	 */
	public JSONObject getClassesForMobile(Integer userId, Integer schoolId, Integer teacherId, Integer classId,String searchWord, Integer start, Integer limit, Integer inquireType, Integer clubId, Integer sortType,Integer classType,Integer memberId) {
		if(inquireType == 1){
			//教师端，显示自己创建的班级列表（获取教学班)
			return this.getClassForMobileOne(userId, limit, start);
		}else if(inquireType == 2){
			//通过班级id获取唯一一条班级信息
			return this.getClassForMobileTwo(classType, classId, userId);
		}else if(inquireType == 3){
			//教学模块下的学校菜单，点击学校进入该学校下的班级排名列表页时调用(获取教学班)
			return this.getClassForMobileThree(schoolId, sortType, limit, start);
		}else if(inquireType == 4){
			//1.俱乐部模块下，我的俱乐部主页->培训，获取该俱乐部下的培训班列表(clubId不能为空)；
			//2.教学模块下，自由用户点击“学习”菜单会显示全站的培训班信息(clubId为空)；
			return this.getClassForMobileFour(clubId, userId, limit, start);
		}if(inquireType == 5){
			return this.getClassSeven(memberId, userId, limit, start);
		}
		LogUtil.error(this.getClass(), "getClasses", "请求参数错误");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,"请求参数错	误");
	}
	
	
	
	
	/**
	 * pc端1：学生端，显示可加入的班级列表
	 * @return
	 */
	public  JSONObject classesOne(Integer userId,Integer schoolId, Integer teacherId, String searchWord, Integer start,Integer limit){
		if( null == teacherId || teacherId == 0){
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("schoolId", schoolId);
			paramMap.put("searchWord", searchWord);
			paramMap.put("isDelete",GameConstants.NO_DEL );
			//调用分页组件
			QueryPage<TeachClass> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachClass.class);
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getClasses", "获取班级列表失败");
				appQueryPage.error(AppErrorCode.GET_CLASS_ERROR);
			}else if(appQueryPage.getList().size() <=0){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.NOT_SEARCH_CLASS);
			}else {
				for(TeachClass teachclass :appQueryPage.getList()){
					//通过班级id获得对应班级教师
					TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
					teachRelTeacherClass.setClassId(teachclass.getClassId());
					TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					if(isteachRelTeacherClass != null){
						CenterUser centerUser = new CenterUser();
						centerUser.setUserId(isteachRelTeacherClass.getTeacherId());
						CenterUser centerUsers = centerUserService.selectCenterUser(centerUser);
						teachclass.setTeacherIds(String.valueOf(centerUsers.getUserId()));//教师id
						teachclass.setTeacherName(centerUsers.getRealName());//教师名称
						//根据教师头像id获得教师头像地址
						String headImgId = RedisComponent.findRedisObjectPublicPicture(centerUsers.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(headImgId)){
							PublicPicture pic = JSONObject.parseObject(headImgId, PublicPicture.class);
							/*
							 * compress picture
							 */
							teachclass.setTeacherHeadLink(Common.checkPic(pic.getFilePath())== true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
						teachclass.setClassIdStage(String.valueOf(teachclass.getClassId()));
					}
				}
				LogUtil.info(this.getClass(), "getClasses", "获取班级信息成功");
				return appQueryPage.getMessageJSONObject("classes");
			}
		}else {
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			paramMap.put("teacherId", teacherId);
			if(searchWord.equals("0")){
				paramMap.put("searchWord", "");	
			}else {
				paramMap.put("searchWord", searchWord);	
			}
			//调用分页组件
			QueryPage<TeachRelTeacherClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachRelTeacherClass.class,"queryteachRelClassByCount","queryteachRelClassByPage");
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getClasses", "获取班级列表失败");
				appQueryPage.error(AppErrorCode.GET_CLASS_ERROR);
			}
			for (TeachRelTeacherClass teachRelTeacherClass : appQueryPage.getList()) {
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(teachRelTeacherClass.getTeacherId());
				CenterUser centerUsers = centerUserService.selectCenterUser(centerUser);
				
				teachRelTeacherClass.setTeacherId(centerUsers.getUserId());//教师id
				teachRelTeacherClass.setTeacherName(centerUsers.getRealName());//教师名称
				//根据教师头像id获得教师头像地址
				String imgUrl = Common.getImgUrl(centerUsers.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					teachRelTeacherClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				TeachClass teachClass = new TeachClass();
				teachClass.setClassId(teachRelTeacherClass.getClassId());
				TeachClass isteachClass = teachClassService.getTeachClass(teachClass);
				//通过班级图片ID获得班级图片
				String imgUrlClass = Common.getImgUrl(isteachClass.getClassLogoId(), BusinessConstant.DEFAULT_IMG_CLASS);
				if(!StringUtil.isEmpty(imgUrlClass)){
					teachRelTeacherClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
				}
				if(isteachClass.getClassName() != null){
					teachRelTeacherClass.setClassName(isteachClass.getClassName());
				}
				if(isteachClass.getStudentsNum() != null){
					teachRelTeacherClass.setStudentsNum(isteachClass.getStudentsNum());
				}else {
					teachRelTeacherClass.setStudentsNum(0);
				}
				teachRelTeacherClass.setClassIdStage(String.valueOf(teachRelTeacherClass.getClassId()));
			}
			LogUtil.info(this.getClass(), "getClasses", "获取班级信息成功");
			return appQueryPage.getMessageJSONObject("classes");
			
		}
		return null;
	}
	
	/**
	 * pc端 2：教师端，显示自己创建的班级列表
	 * @return
	 */
	public JSONObject getClassTwo(Integer userId,Integer start,Integer limit){
		//根据教师ID获得对应的班级id
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		centerUser.setUserType(GameConstants.USER_TYPE_TEAHCER);
		CenterUser userClass = centerUserService.selectAllCenterUsers(centerUser);
		if(userClass == null || userClass.getClassId() == null){
			LogUtil.error(this.getClass(), "getClasses", "未创建班级或不是教师");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.NOT_CRE_CLASS);
		}
		//根据用户表的班级ID查询对应的班级
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("teacherId", userClass.getUserId());
		//调用分页组件
		QueryPage<TeachRelTeacherClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachRelTeacherClass.class,"queryCountAll","queryByPageAll");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getClasses", "教师下没有班级");
			appQueryPage.error(AppErrorCode.ERROR_GET_CLASS);
		}
		for (TeachRelTeacherClass teachRolClass : appQueryPage.getList()) {
			//通过教师班级关系表的班级ID查询班级表
			TeachClass teachClasses = new TeachClass();
			teachClasses.setClassId(teachRolClass.getClassId());
			TeachClass isteachClasses =teachClassService.getTeachClass(teachClasses);
			//通过教师班级关系表的教师ID查询用户表
			CenterUser centerUsers = new CenterUser();
			centerUsers.setUserId(teachRolClass.getTeacherId());
			CenterUser isuserClass = centerUserService.selectAllCenterUsers(centerUsers);
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(isuserClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				/*
				 * 压缩图片
				 */
				teachRolClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			//通过班级图片ID获得班级图片
			String imgUrlClass = Common.getImgUrl(isteachClasses.getClassLogoId(), BusinessConstant.DEFAULT_IMG_CLASS);
			if(!StringUtil.isEmpty(imgUrlClass)){
				teachRolClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
			}
			if(isteachClasses.getSchoolId() != null){
				//根据学校ID获得学校名称
				TeachSchool teachSchool = teachSchoolService.getTeachSchool(isteachClasses.getSchoolId());
				if(teachSchool != null && teachSchool.getSchoolName() != null){
					teachRolClass.setSchoolName(teachSchool.getSchoolName());//学校名称
				}
			}
			if(isteachClasses.getInstituteId() != null){
				//根据学院id获得学院名称
				TeachInstitute teachInstitute = teachInstituteService.getTeachInstitute(isteachClasses.getInstituteId());
				if(teachInstitute != null && teachInstitute.getInstName() != null){
					teachRolClass.setCollegeName(teachInstitute.getInstName());//学院名称
				}
			}
			//根据班级id和教师ID判断班级是否为默认的
			TeachRelTeacherClass iteachRelTeacherClass = new TeachRelTeacherClass();
			iteachRelTeacherClass.setClassId(isteachClasses.getClassId());
			iteachRelTeacherClass.setTeacherId(isuserClass.getUserId());
			TeachRelTeacherClass isteachRelClass = teachRelTeacherClassService.getTeachRelTeacherClass(iteachRelTeacherClass);
			
			//添加班级表信息
			teachRolClass.setClassIdStage(String.valueOf(teachRolClass.getClassId()));
			teachRolClass.setClassName(isteachClasses.getClassName());//班级名称
			teachRolClass.setTeacherIds(String.valueOf(isuserClass.getUserId()));//教师ID
			teachRolClass.setTeacherName(isuserClass.getRealName());//教师名称
			teachRolClass.setIsDefaultClass(isteachRelClass.getIsDefault());//是否为默认 1:是   0:否   默认为0
			if(isteachClasses.getStudentsNum() != null){
				teachRolClass.setStudentsNum(isteachClasses.getStudentsNum());
			}else {
				teachRolClass.setStudentsNum(0);
			}
		}
		LogUtil.info(this.getClass(),"getClasses" , "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	
	/**
	 * pc端3.通过班级id获取唯一一条班级信息
	 * @return
	 */
	public JSONObject getClassThree(Integer classType,Integer classId,Integer userId){
		if(classType == GameConstants.TEACHING_CLASS){//教学班
			//根据班级ID判断对应班级是否存在
			TeachClass isteachClass =  new TeachClass();
			isteachClass.setClassId(classId);
			isteachClass.setIsDelete(GameConstants.NO_DEL);
			TeachClass teachClass =  teachClassService.selectSingleTeachClass(isteachClass);
			if(teachClass == null && "".equals(teachClass)){
				LogUtil.error(this.getClass(), "getClasses", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT);
			}
			//查询到的数据放到实体里面
			TeachClass teachClassAll = new TeachClass();
			//根据班级ID查询教师班级关系表获得教师ID
			TeachRelTeacherClass iteachRelTeacherClass = new TeachRelTeacherClass();
			iteachRelTeacherClass.setClassId(classId);
			TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(iteachRelTeacherClass);
			//根据教师Id获得对应的教师信息
			if(isteachRelTeacherClass != null){
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(isteachRelTeacherClass.getTeacherId());
				CenterUser userClass = centerUserService.selectCenterUser(centerUser);
				if(userClass == null || "".equals(userClass)){
					LogUtil.error(this.getClass(), "getClasses", "教师不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_GET_TEACHER);
				}
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(userClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					/*
					 * compress picture
					 */
					teachClassAll.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				//通过班级图片ID获得班级图片
				String imgUrlClass = Common.getImgUrl(teachClass.getClassLogoId(), BusinessConstant.DEFAULT_IMG_CLASS);
				if(!StringUtil.isEmpty(imgUrlClass)){
					
					teachClassAll.setClassLogoLink(Common.checkPic(imgUrlClass) == true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
				}
				//根据学校ID获得学校名称
				//从缓存获取
				if(teachClass.getSchoolId() != null){
					String school = JedisCache.getRedisVal(null, TeachSchool.class.getSimpleName(), teachClass.getSchoolId().toString());
					if(school != null && "".equals(school)){
						TeachSchool teachSchools = JSONObject.parseObject(school, TeachSchool.class);
						teachClassAll.setSchoolName(teachSchools.getSchoolName());//学校名称
					}else {
						TeachSchool teachSchool = teachSchoolService.getTeachSchool(teachClass.getSchoolId());
						if(teachSchool != null ){
							teachClassAll.setSchoolName(teachSchool.getSchoolName());//学校名称
							//加入缓存
							JedisCache.setRedisVal(null, TeachSchool.class.getSimpleName(),teachClass.getSchoolId().toString(),JSONObject.toJSONString(teachSchool));
						}
					}
				}
				//根据学院id获得学院名称
				//从缓存获取
				if(teachClass.getInstituteId() != null){
					String teachIntstitute = JedisCache.getRedisVal(null, TeachInstitute.class.getSimpleName(), teachClass.getInstituteId().toString());
					if(teachIntstitute != null && "".equals(teachIntstitute)){
						TeachInstitute Intstitute = JSONObject.parseObject(teachIntstitute, TeachInstitute.class);
						teachClassAll.setCollegeName(Intstitute.getInstName());//学院名称
					}else {
						TeachInstitute teachInstitute = teachInstituteService.getTeachInstitute(teachClass.getInstituteId());
						if(teachInstitute != null && teachInstitute.getInstName() != null){
							teachClassAll.setCollegeName(teachInstitute.getInstName());//学院名称
							//加入缓存
							JedisCache.setRedisVal(null, TeachInstitute.class.getSimpleName(),teachClass.getInstituteId().toString(),JSONObject.toJSONString(teachInstitute));
						}
					}
				}
				
				teachClassAll.setClassIdStage(String.valueOf(teachClass.getClassId()));//班级ID
				teachClassAll.setClassName(teachClass.getClassName());//班级名称
				teachClassAll.setTeacherIds(String.valueOf(userClass.getUserId()));//教师ID//
				teachClassAll.setTeacherName(userClass.getRealName());//教师名称
				if(teachClass.getStudentsNum() != null){
					teachClassAll.setStudentsNum(teachClass.getStudentsNum());//班级成员数量
				}else {
					teachClassAll.setStudentsNum(0);
				}
				teachClassAll.setCreateTime(teachClass.getCreateTime());//创建班级的时间戳
				teachClassAll.setClassLogoId(teachClass.getClassLogoId()==null?BusinessConstant.DEFAULT_IMG_CLASS:teachClass.getClassLogoId());//班级LoGo图片id
				teachClassAll.setSchoolId(teachClass.getSchoolId());//学校ID
				teachClassAll.setInstituteId(teachClass.getInstituteId());//学院ID
				teachClassAll.setIsDefaultClass(isteachRelTeacherClass.getIsDefault());//是否为默认 1:是   0:否   默认为0
				LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
				JSONArray array = new JSONArray();
				array.add(teachClassAll);
				JSONObject json = new JSONObject();
				json.put("classes", array);
				return Common.getReturn(AppErrorCode.SUCCESS,"",json);
			}else {
				LogUtil.error(this.getClass(), "getClasses", "未创建班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.NOT_CRE_CLASS);
			}
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){//俱乐部培训班
				//根据班级ID判断对应培训班是否存在
				ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
				clubTrainingClass.setClassId(classId);
				ClubTrainingClass isclubTrainingClass = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
				if(isclubTrainingClass == null){
					LogUtil.error(this.getClass(), "getClasses", "班级不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT);
				}else {
					//通过培训班创建者ID查询用户表
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(isclubTrainingClass.getCreateUserId());
					CenterUser centerUserAll = centerUserService.selectCenterUser(centerUser);
					if(centerUserAll == null){
						LogUtil.error(this.getClass(), "getClasses", "创建者不存在");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_CRETE_JOIN);
					}
					ClubTrainingClass returnClubClass = new ClubTrainingClass();
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						/*
						 * compress picture
						 */
						returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					//通过班级图片ID获得班级图片
					String imgUrlClass = Common.getImgUrl(isclubTrainingClass.getImageId(), BusinessConstant.DEFAULT_IMG_CLASS);
					if(!StringUtil.isEmpty(imgUrlClass)){
						returnClubClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
					}
					//通过俱乐部ID查询俱乐部表获得俱乐部名称
					ClubMain clubMain = new ClubMain();
					clubMain.setClubId(isclubTrainingClass.getClubId());
					ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
					if(clubMainName != null && clubMainName.getClubName() != null){
						returnClubClass.setClubName(clubMainName.getClubName());//教练所属俱乐部名称
					}
					//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
					ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
					clubJoinTraining.setClassId(classId);
					clubJoinTraining.setUserId(userId);
					ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
					if(isclubJoinTraining == null){
						returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
					}else{
						returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
					}
					//购买官方课程包所需要的一级货币
					double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
					if(isclubTrainingClass.getClassId() != null){
						returnClubClass.setClassIdStage(String.valueOf(isclubTrainingClass.getClassId()));//班级ID
					}
					if(isclubTrainingClass.getTitle() != null){
						returnClubClass.setClassName(isclubTrainingClass.getTitle());//班级名称
					}
					if(centerUserAll.getUserId() != null){
						returnClubClass.setTeacherIds(String.valueOf(centerUserAll.getUserId()));//教练ID
					}
					if(centerUserAll.getNickName() != null){
						returnClubClass.setTeacherName(centerUserAll.getNickName());//教练名称
					}
					if(isclubTrainingClass.getStudentNum() != null){
						returnClubClass.setStudentsNum(isclubTrainingClass.getStudentNum());//学员数量
					}else {
						returnClubClass.setStudentsNum(0);
					}
					if(isclubTrainingClass.getCreateTime() != null){
						returnClubClass.setCreateTime(isclubTrainingClass.getCreateTime());//创建班级时间
					}
					if(isclubTrainingClass.getImageId() != null){
						returnClubClass.setClassLogoId(isclubTrainingClass.getImageId());//班级LogoId
					}
					if(isclubTrainingClass.getIntroduce() != null){
						returnClubClass.setIntroduce(isclubTrainingClass.getIntroduce());//班级介绍
					}
					if(isclubTrainingClass.getCostType() != null){
						returnClubClass.setCostType(isclubTrainingClass.getCostType());//加入时是否需要收费
					}
					if(isclubTrainingClass.getCostAmount() != null){
						returnClubClass.setCostAmount(isclubTrainingClass.getCostAmount());//加入时需要的一级货币
					}
					if(isclubTrainingClass.getCourseNum() != null){
						returnClubClass.setClubCourseCount(isclubTrainingClass.getCourseNum());//培训班课程数
					}
					if(isclubTrainingClass.getIsBuyOfficial() != null){
						returnClubClass.setHasBuyOfficialCourse(isclubTrainingClass.getIsBuyOfficial());//该培训班是否已购买课程包 1:已购买
					}
					returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
					JSONArray array = new JSONArray();
					array.add(returnClubClass);
					JSONObject json = new JSONObject();
					json.put("classes", array);
					LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "",json);
				}
		}
		return null;
	}
	
	/**
	 * PC端 4；教学模块下的学校菜单，点击学校进入该学校下的班级排名列表页时调用(获取教学班)
	 * @return
	 */
	public JSONObject getClassFour(Integer schoolId,Integer sortType,Integer limit,Integer start){
		//判断学校下有没有班级
		TeachClass teachClass = new TeachClass();
		teachClass.setSchoolId(schoolId);
		TeachClass classAll =  teachClassService.selectSingleTeachClass(teachClass);
		if(classAll == null && "".equals(classAll)){
			LogUtil.error(this.getClass(),"getClasses", "学校下没有班级");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_SCHOOL_CLASS);
		}
		TeachClass classes = new TeachClass();
		classes.setSchoolId(schoolId);
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("schoolId", schoolId);
		paramMap.put("sortType",sortType );//排序类型 1:人数；2:学时；3:实训；4:原创
		//调用分页组件
		QueryPage<TeachClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachClass.class,"queryCountSchool","queryByPageSchool");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(),"getClasses", "学校下没有班级");
			appQueryPage.error(AppErrorCode.ERROR_SCHOOL_CLASS);
		}
		for (TeachClass teachClassAll : appQueryPage.getList()) {
			//根据班级id查询用户表获得教师信息
			CenterUser centerUser = new CenterUser();
			centerUser.setClassId(teachClass.getClassId());
			centerUser.setUserType(GameConstants.USER_TYPE_TEAHCER);
			CenterUser userClass = centerUserService.selectCenterUser(centerUser);
			if(userClass == null && "".equals(userClass)){
				LogUtil.error(this.getClass(),"getClasses", "教师不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_GET_TEACHER);
			}
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(userClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				/*
				 * 压缩图片
				 */
				teachClassAll.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			teachClassAll.setClassIdStage(String.valueOf(teachClassAll.getClassId()));
			teachClassAll.setTeacherIds(String.valueOf(userClass.getUserId()));//教师ID
			teachClassAll.setTeacherName(userClass.getRealName());//教师名称
			teachClassAll.setLearningSeconds(teachClassAll.getTotalCompleteHours());//学时总秒数
			teachClassAll.setPracticeCount(teachClassAll.getTotalTrainNum());//实训次数
			teachClassAll.setCourseCounts(teachClassAll.getOriginalCourseNum());//原创课时数
		}
		LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	/**
	 * PC端 5:获取俱乐部下的培训班列表
	 * @return
	 */
	public JSONObject getClassFive(Integer clubId,Integer userId,Integer limit,Integer start,Integer requestSide){
		//根据用户ID查询用户表获得俱乐部ID判读用户所在俱乐部和需要获得班级信息的俱乐部是否一致
		CenterUser centerUserClub = centerUserService.getCenterUserById(userId);
		
		ClubMember clubMember = new ClubMember();
		clubMember.setClubId(clubId);
		clubMember.setUserId(userId);
		ClubMember isClubMember = clubMemberService.getClubMemberOne(clubMember);
		
		//根据俱乐部ID查询俱乐部培训班表获得班级信息
		Map<String,Object> clubClass = new HashMap<String,Object>();
		if( requestSide == 1){//后端
			if(isClubMember.getLevel() == 1){//会长
				clubClass.put("clubId", clubId);
				clubClass.put("isDelete", GameConstants.NO_DEL);
			}else if(isClubMember.getLevel() == 2){//教练
				clubClass.put("createUserId", userId);
				clubClass.put("clubId", clubId);
				clubClass.put("isDelete", GameConstants.NO_DEL);
			}
		}else if( requestSide == 2){//前端
				clubClass.put("clubId", clubId);
				clubClass.put("isDelete", GameConstants.NO_DEL);
		}
		//调用分页组件
		QueryPage<ClubTrainingClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, clubClass, ClubTrainingClass.class,"queryClubClassCount","queryClubClassByPage");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(),"getClasses", "俱乐部下没有培训班");
			appQueryPage.error(AppErrorCode.NOT_CLASS);
		}
		for (ClubTrainingClass returnClubClass : appQueryPage.getList()) {
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(centerUserClub.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				/*
				 * 压缩图片
				 */
				returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			//通过班级图片ID获得班级图片
			String imgUrlClass = Common.getImgUrl(returnClubClass.getImageId(), BusinessConstant.DEFAULT_IMG_TRAINING);
			if(!StringUtil.isEmpty(imgUrlClass)){
				returnClubClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
			}
			//通过俱乐部ID查询俱乐部表获得俱乐部名称
			ClubMain clubMain = new ClubMain();
			clubMain.setClubId(clubId);
			ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
			//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
			ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
			clubJoinTraining.setClassId(returnClubClass.getClassId());
			clubJoinTraining.setUserId(userId);
			ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
			if(isclubJoinTraining == null){
				returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
			}else {
				returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
			}
			//购买官方课程包所需要的一级货币
			double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
			returnClubClass.setClassIdStage(String.valueOf(returnClubClass.getClassId()));//班级ID
			returnClubClass.setClassName(returnClubClass.getTitle());//班级名称
			returnClubClass.setTeacherIds(String.valueOf(returnClubClass.getCreateUserId()));//教练ID
			returnClubClass.setTeacherName(centerUserClub.getNickName());//教练名称
			returnClubClass.setClubId(centerUserClub.getClubId());//教练所属俱乐部ID
			returnClubClass.setClubName(clubMainName.getClubName());//教练所属俱乐部名称
			returnClubClass.setStudentsNum(returnClubClass.getStudentNum());//班级成员数量
			returnClubClass.setClubCourseCount(returnClubClass.getCourseNum());//培训班课程数
			returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
		}
		LogUtil.info(this.getClass(), "getClasses", "成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	/**
	 * PC端 6:教学模块下，自由用户点击“课程”菜单会显示全站的培训班信息
	 * @return
	 */
	public JSONObject getClassSix(Integer userId,Integer limit,Integer start){
		//根据俱乐部ID查询俱乐部培训班表获得班级信息
		Map<String,Object> clubClass = new HashMap<String,Object>();
		clubClass.put("isDelete", GameConstants.NO_DEL);
		//调用分页组件
		QueryPage<ClubTrainingClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, clubClass, ClubTrainingClass.class,"queryClubClassCountAll","queryClubClassByPageAll");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getClasses", "获取培训班失败");
			appQueryPage.error(AppErrorCode.NOT_CLASS);
		}
		for (ClubTrainingClass returnClubClass : appQueryPage.getList()) {
			//通过创建者ID查询用户表获得教师信息、
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(returnClubClass.getCreateUserId());
			CenterUser centerUserClub =centerUserService.selectCenterUser(centerUser);
			//从缓存取数据
			if(centerUserClub != null){
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(centerUserClub.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl)== true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				//通过班级图片ID获得班级图片
				String imgUrlClass = Common.getImgUrl(returnClubClass.getImageId(), BusinessConstant.DEFAULT_IMG_TRAINING);
				if(!StringUtil.isEmpty(imgUrlClass)){
					returnClubClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
				}
				//通过俱乐部ID查询俱乐部表获得俱乐部名称
				ClubMain clubMain = new ClubMain();
				clubMain.setClubId(returnClubClass.getClubId());
				ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
				if(clubMainName != null && clubMainName.getClubName() != null){
					returnClubClass.setClubName(clubMainName.getClubName());//教练所属俱乐部名称
				}
				returnClubClass.setTeacherName(centerUserClub.getNickName());//教练名称
				returnClubClass.setClubId(centerUserClub.getClubId());//教练所属俱乐部ID
			}
			//购买官方课程包所需要的一级货币
			double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
			
			//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
			ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
			clubJoinTraining.setClassId(returnClubClass.getClassId());
			clubJoinTraining.setUserId(userId);
			ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);

			if(isclubJoinTraining == null){
				returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
			}else {
				returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
			}
			returnClubClass.setClassName(returnClubClass.getTitle());//班级名称
			if(returnClubClass.getStudentNum() != null){
				returnClubClass.setStudentsNum(returnClubClass.getStudentNum());
			}else {
				returnClubClass.setStudentsNum(0);
			}
			returnClubClass.setClassLogoId(returnClubClass.getImageId());//俱乐部培训班图片id
			returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
			returnClubClass.setClassIdStage(String.valueOf(returnClubClass.getClassId()));
			returnClubClass.setTeacherIds(String.valueOf(returnClubClass.getCreateUserId()));
		} 
		LogUtil.info(this.getClass(), "getClasses", "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	/**
	 * PC端   7:获取当前登录者参加的培训班列表，使用场景有：1.个人中心－》我参加的培训班
	 * @return
	 */
	public JSONObject getClassSeven(Integer memberId,Integer userId,Integer limit,Integer start){
		Map<String,Object> clubMap = new HashMap<String,Object>();
		if(memberId != null && memberId != 0){
			clubMap.put("userId", memberId);
		}else {
			clubMap.put("userId", userId);
		}
		clubMap.put("isDelete", GameConstants.NO_DEL);
		//调用分页组件
		QueryPage<ClubJoinTraining> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, clubMap, ClubJoinTraining.class,"queryClubJoinCountAll","queryClubJoinByPageAll");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getClasses", "获取培训班失败");
			appQueryPage.error(AppErrorCode.NOT_CRE_CLASS);
		}
		for(ClubJoinTraining clubJoinTraining:appQueryPage.getList()){
			//根据班级ID判断对应培训班是否存在
			ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
			clubTrainingClass.setClassId(clubJoinTraining.getClassId());
			ClubTrainingClass isclubTrainingClass = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
			if(isclubTrainingClass != null){
				//通过培训班创建者ID查询用户表
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(isclubTrainingClass.getCreateUserId());
				CenterUser centerUserAll = centerUserService.selectCenterUser(centerUser);
				
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					clubJoinTraining.setTeacherHeadLink(Common.checkPic(imgUrl) == true ?imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				// 该用户的班级类型为2
				clubJoinTraining.setClassType(2);
				if(clubJoinTraining.getClassId() == null){
					clubJoinTraining.setIsjoin(0);
				}else{
					clubJoinTraining.setIsjoin(1);
				}
				//通过班级图片ID获得班级图片
				String imgUrlClass = Common.getImgUrl(isclubTrainingClass.getImageId(), BusinessConstant.DEFAULT_IMG_TRAINING);
				if(!StringUtil.isEmpty(imgUrlClass)){
					clubJoinTraining.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
				}
				//通过俱乐部ID查询俱乐部表获得俱乐部名称
				ClubMain clubMain = new ClubMain();
				clubMain.setClubId(centerUserAll.getClubId());
				ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
				
				//购买官方课程包所需要的一级货币
				double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
				if(isclubTrainingClass.getClassId() != null){
					clubJoinTraining.setClassIdStage(String.valueOf(isclubTrainingClass.getClassId()));//班级ID
				}
				if(isclubTrainingClass.getTitle() !=null){
					clubJoinTraining.setClassName(isclubTrainingClass.getTitle());//班级名称
				}
				if(centerUserAll.getUserId() !=null){
					clubJoinTraining.setTeacherIds(String.valueOf(centerUserAll.getUserId()));//教练ID
				}
				if(centerUserAll.getNickName() !=null){
					clubJoinTraining.setTeacherName(centerUserAll.getNickName());//教练名称
				}
				if(clubMainName.getClubId() !=null){
					clubJoinTraining.setClubId(clubMainName.getClubId());//俱乐部ID
				}
				if(clubMainName.getClubName() !=null){
					clubJoinTraining.setClubName(clubMainName.getClubName());//俱乐部名称
				}
				if(isclubTrainingClass.getStudentNum() !=null){
					clubJoinTraining.setStudentsNum(isclubTrainingClass.getStudentNum());//学员数量
				}else {
					clubJoinTraining.setStudentsNum(0);
				}
				if(isclubTrainingClass.getCreateTime() !=null){
					clubJoinTraining.setCreateTime(isclubTrainingClass.getCreateTime());//创建班级时间
				}
				if(isclubTrainingClass.getImageId() !=null){
					clubJoinTraining.setClassLogoId(isclubTrainingClass.getImageId());//班级LogoId
				}
				if(isclubTrainingClass.getIntroduce() !=null){
					clubJoinTraining.setIntroduce(isclubTrainingClass.getIntroduce());//班级介绍
				}
				if(isclubTrainingClass.getCostType() !=null){
					clubJoinTraining.setCostType(isclubTrainingClass.getCostType());//加入时是否需要收费
				}
				if(isclubTrainingClass.getCostAmount() !=null){
					clubJoinTraining.setCostAmount(isclubTrainingClass.getCostAmount());//加入时需要的一级货币
				}
				if(isclubTrainingClass.getCourseNum() !=null){
					clubJoinTraining.setClubCourseCount(isclubTrainingClass.getCourseNum());//培训班课程数
				}
				if(isclubTrainingClass.getIsBuyOfficial() !=null){
					clubJoinTraining.setHasBuyOfficialCourse(isclubTrainingClass.getIsBuyOfficial());//该培训班是否已购买课程包 1:已购买
				}
				clubJoinTraining.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
			}
	  }
		LogUtil.info(this.getClass(), "getClasses", "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	
	/**
	 * 移动端：  1：教师端，显示自己创建的班级列表（获取教学班)
	 * @return
	 */
	public JSONObject getClassForMobileOne(Integer userId,Integer limit,Integer start){
		//根据教师ID获得对应的班级id
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		centerUser.setUserType(GameConstants.USER_TYPE_TEAHCER);//用户类型：教师
		CenterUser userClass = centerUserService.selectAllCenterUsers(centerUser);
		if(userClass == null || userClass.getClassId() == null){
			LogUtil.error(this.getClass(), "getClasses", "未创建班级或不是教师");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.NOT_CRE_CLASS);
		}
		//根据用户表的班级ID查询对应的班级
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("teacherId", userClass.getUserId());
		//调用分页组件
		QueryPage<TeachRelTeacherClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachRelTeacherClass.class,"queryCountAll","queryByPageAll");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(), "getClasses", "教师下没有班级");
			appQueryPage.error(AppErrorCode.ERROR_GET_CLASS);
		}
		for (TeachRelTeacherClass teachRolClass : appQueryPage.getList()) {
			//通过教师班级关系表的班级ID查询班级表
			TeachClass teachClasses = new TeachClass();
			teachClasses.setClassId(teachRolClass.getClassId());
			TeachClass isteachClasses =teachClassService.getTeachClass(teachClasses);
			//通过教师班级关系表的教师ID查询用户表
			CenterUser centerUsers = new CenterUser();
			centerUsers.setUserId(teachRolClass.getTeacherId());
			CenterUser isuserClass = centerUserService.selectAllCenterUsers(centerUsers);
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(isuserClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				/*
				 * 压缩图片
				 */
				teachRolClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			//通过班级图片ID获得班级图片
			String imgUrlClass = Common.getImgUrl(isteachClasses.getClassLogoId(), BusinessConstant.DEFAULT_IMG_CLASS);
			if(!StringUtil.isEmpty(imgUrlClass)){
				teachRolClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
			}
			
			if(isteachClasses.getSchoolId() != null){
				//根据学校ID获得学校名称
				TeachSchool teachSchool = teachSchoolService.getTeachSchool(isteachClasses.getSchoolId());
				if(teachSchool != null && teachSchool.getSchoolName() != null){
					teachRolClass.setSchoolName(teachSchool.getSchoolName());//学校名称
				}
			}
			if(isteachClasses.getInstituteId() != null){
				//根据学院id获得学院名称
				TeachInstitute teachInstitute = teachInstituteService.getTeachInstitute(isteachClasses.getInstituteId());
				if(teachInstitute != null && teachInstitute.getInstName() != null){
					teachRolClass.setCollegeName(teachInstitute.getInstName());//学院名称
				}
			}
			//根据班级id和教师ID判断班级是否为默认的
			TeachRelTeacherClass iteachRelTeacherClass = new TeachRelTeacherClass();
			iteachRelTeacherClass.setClassId(isteachClasses.getClassId());
			iteachRelTeacherClass.setTeacherId(isuserClass.getUserId());
			TeachRelTeacherClass isteachRelClass = teachRelTeacherClassService.getTeachRelTeacherClass(iteachRelTeacherClass);
			
			//班级表信息
			if(isteachClasses.getStudentsNum() != null && !isteachClasses.getStudentsNum().equals("")){
				teachRolClass.setStudentsNum(isteachClasses.getStudentsNum());//学员数量
			}else {
				teachRolClass.setStudentsNum(0);//学员数量
			}
			teachRolClass.setClassIdStage(String.valueOf(teachRolClass.getClassId()));//班级id
			teachRolClass.setClassName(isteachClasses.getClassName());//班级名称
			teachRolClass.setTeacherIds(String.valueOf(isuserClass.getUserId()));//教师ID
			teachRolClass.setTeacherName(isuserClass.getRealName());//教师名称
			teachRolClass.setIsDefaultClass(isteachRelClass.getIsDefault());//是否为默认 1:是   0:否   默认为0
		}
		LogUtil.info(this.getClass(),"getClasses" , "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	
	/**
	 * 移动端：2:通过班级id获取唯一一条班级信息
	 * @return
	 */
	public JSONObject getClassForMobileTwo(Integer classType,Integer classId,Integer userId){
		if(classType == GameConstants.TEACHING_CLASS){//教学班
			//根据班级ID判断对应班级是否存在
			TeachClass isteachClass =  new TeachClass();
			isteachClass.setClassId(classId);
			isteachClass.setIsDelete(GameConstants.NO_DEL);
			TeachClass teachClass =  teachClassService.selectSingleTeachClass(isteachClass);
			if(teachClass == null && "".equals(teachClass)){
				LogUtil.error(this.getClass(), "getClasses", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT);
			}
			//查询到的数据放到实体里面
			TeachClass teachClassAll = new TeachClass();
			//根据班级ID查询教师班级关系表获得教师ID
			TeachRelTeacherClass iteachRelTeacherClass = new TeachRelTeacherClass();
			iteachRelTeacherClass.setClassId(classId);
			TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(iteachRelTeacherClass);
			//根据教师Id获得对应的教师信息
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(isteachRelTeacherClass.getTeacherId());
			CenterUser userClass = centerUserService.selectCenterUser(centerUser);
			if(userClass == null && "".equals(userClass)){
				LogUtil.error(this.getClass(), "getClasses", "教师不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_GET_TEACHER);
			}
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(userClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				teachClassAll.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			//通过班级图片ID获得班级图片
			String imgUrlClass = Common.getImgUrl(teachClass.getClassLogoId(), BusinessConstant.DEFAULT_IMG_CLASS);
			if(!StringUtil.isEmpty(imgUrlClass)){
				teachClassAll.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
			}
			//根据学校ID获得学校名称
			//从缓存获取
			if(teachClass.getSchoolId() != null){
				String school = JedisCache.getRedisVal(null, TeachSchool.class.getSimpleName(), teachClass.getSchoolId().toString());
				if(school != null && "".equals(school)){
					TeachSchool teachSchools = JSONObject.parseObject(school, TeachSchool.class);
					teachClassAll.setSchoolName(teachSchools.getSchoolName());//学校名称
				}else {
					TeachSchool teachSchool = teachSchoolService.getTeachSchool(teachClass.getSchoolId());
					if(teachSchool != null ){
						teachClassAll.setSchoolName(teachSchool.getSchoolName());//学校名称
						//加入缓存
						JedisCache.setRedisVal(null, TeachSchool.class.getSimpleName(),teachClass.getSchoolId().toString(),JSONObject.toJSONString(teachSchool));
					}
				}
			}
			//根据学院id获得学院名称
			//从缓存获取
			if(teachClass.getInstituteId() != null){
				String teachIntstitute = JedisCache.getRedisVal(null, TeachInstitute.class.getSimpleName(), teachClass.getInstituteId().toString());
				if(teachIntstitute != null && "".equals(teachIntstitute)){
					TeachInstitute Intstitute = JSONObject.parseObject(teachIntstitute, TeachInstitute.class);
					teachClassAll.setCollegeName(Intstitute.getInstName());//学院名称
				}else {
					TeachInstitute teachInstitute = teachInstituteService.getTeachInstitute(teachClass.getInstituteId());
					if(teachInstitute != null && teachInstitute.getInstName() != null){
						teachClassAll.setCollegeName(teachInstitute.getInstName());//学院名称
						//加入缓存
						JedisCache.setRedisVal(null, TeachInstitute.class.getSimpleName(),teachClass.getInstituteId().toString(),JSONObject.toJSONString(teachInstitute));
					}
				}
			}
			
			teachClassAll.setClassIdStage(String.valueOf(teachClass.getClassId()));//班级ID
			teachClassAll.setClassName(teachClass.getClassName());//班级名称
			teachClassAll.setTeacherIds(String.valueOf(userClass.getUserId()));//教师ID//
			teachClassAll.setTeacherName(userClass.getRealName());//教师名称
			if(teachClass.getStudentsNum() != null){
				teachClassAll.setStudentsNum(teachClass.getStudentsNum());//班级成员数量
			}else {
				teachClassAll.setStudentsNum(0);//班级成员数量
			}
			teachClassAll.setCreateTime(teachClass.getCreateTime());//创建班级的时间戳
			teachClassAll.setClassLogoId(teachClass.getClassLogoId());//班级LoGo图片id
			teachClassAll.setSchoolId(teachClass.getSchoolId());//学校ID
			teachClassAll.setInstituteId(teachClass.getInstituteId());//学院ID
			if(isteachRelTeacherClass.getIsDefault() != null){
				teachClassAll.setIsDefaultClass(isteachRelTeacherClass.getIsDefault());//是否为默认 1:是   0:否   默认为0
			}else {
				teachClassAll.setIsDefaultClass(0);//是否为默认 1:是   0:否   默认为0   为空时设为否
			}
			LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
			JSONArray array = new JSONArray();
			array.add(teachClassAll);
			JSONObject json = new JSONObject();
			json.put("classes", array);
			return Common.getReturn(AppErrorCode.SUCCESS,"",json);
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){//俱乐部培训班
				//根据班级ID判断对应培训班是否存在
				ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
				clubTrainingClass.setClassId(classId);
				ClubTrainingClass isclubTrainingClass = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
				if(isclubTrainingClass == null){
					LogUtil.error(this.getClass(), "getClasses", "班级不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT);
				}else {
					//通过培训班创建者ID查询用户表
					CenterUser centerUser = new CenterUser();
					centerUser.setUserId(isclubTrainingClass.getCreateUserId());
					CenterUser centerUserAll = centerUserService.selectCenterUser(centerUser);
					
					ClubTrainingClass returnClubClass = new ClubTrainingClass();
					
					//通过教师头像id获得头像图片地址
					String imgUrl = Common.getImgUrl(centerUserAll.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					//通过班级图片ID获得班级图片
					String imgUrlClass = Common.getImgUrl(isclubTrainingClass.getImageId(), BusinessConstant.DEFAULT_IMG_TRAINING);
					if(!StringUtil.isEmpty(imgUrlClass)){
						returnClubClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
					}
					//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
					ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
					clubJoinTraining.setClassId(classId);
					clubJoinTraining.setUserId(userId);
					ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
					if(isclubJoinTraining == null){
						returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
					}else{
						returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
					}
					//购买官方课程包所需要的一级货币
					double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());

					returnClubClass.setClassIdStage(String.valueOf(isclubTrainingClass.getClassId()));//班级ID
					returnClubClass.setClassName(isclubTrainingClass.getTitle());//班级名称
					returnClubClass.setTeacherIds(String.valueOf(centerUserAll.getUserId()));//教练ID
					returnClubClass.setTeacherName(centerUserAll.getNickName());//教练名称
					if(isclubTrainingClass.getStudentNum() != null){
						returnClubClass.setStudentsNum(isclubTrainingClass.getStudentNum());//学员数量
					}else {
						returnClubClass.setStudentsNum(0);//学员数量
					}
					returnClubClass.setCreateTime(isclubTrainingClass.getCreateTime());//创建班级时间
					returnClubClass.setClassLogoId(isclubTrainingClass.getImageId());//班级LogoId
					returnClubClass.setIntroduce(isclubTrainingClass.getIntroduce());//班级介绍
					returnClubClass.setCostType(isclubTrainingClass.getCostType());//加入时是否需要收费
					returnClubClass.setCostAmount(isclubTrainingClass.getCostAmount());//加入时需要的一级货币
					returnClubClass.setClubCourseCount(isclubTrainingClass.getCourseNum());//培训班课程数
					if(isclubTrainingClass.getIsBuyOfficial() != null ){
						returnClubClass.setHasBuyOfficialCourse(isclubTrainingClass.getIsBuyOfficial());//该培训班是否已购买课程包 1:已购买
					}else {
						returnClubClass.setHasBuyOfficialCourse(0);//该培训班是否已购买课程包 1:已购买
					}
					returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
					returnClubClass.setIsDefaultClass(0);//是否为默认 1:是   0:否   默认为0  培训班没有此参数  固定返回0
					JSONArray array = new JSONArray();
					array.add(returnClubClass);
					JSONObject json = new JSONObject();
					json.put("classes", array);
					LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "",json);
				}
			}
		return null;
	}
	
	
	/**
	 * 移动端：3.教学模块下的学校菜单，点击学校进入该学校下的班级排名列表页时调用(获取教学班)
	 * @return
	 */
	public JSONObject getClassForMobileThree(Integer schoolId,Integer sortType,Integer limit,Integer start){
		//判断学校下有没有班级
		TeachClass teachClass = new TeachClass();
		teachClass.setSchoolId(schoolId);
		TeachClass classAll =  teachClassService.selectSingleTeachClass(teachClass);
		if(classAll == null && "".equals(classAll)){
			LogUtil.error(this.getClass(),"getClasses", "学校下没有班级");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_SCHOOL_CLASS);
		}
		TeachClass classes = new TeachClass();
		classes.setSchoolId(schoolId);
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("schoolId", schoolId);
		paramMap.put("sortType",sortType );//排序类型 1:人数；2:学时；3:实训；4:原创
		//调用分页组件
		QueryPage<TeachClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachClass.class,"queryCountSchool","queryByPageSchool");
		//msg错误码
		if(!appQueryPage.getState()){
			LogUtil.error(this.getClass(),"getClasses", "学校下没有班级");
			appQueryPage.error(AppErrorCode.ERROR_SCHOOL_CLASS);
		}
		for (TeachClass teachClassAll : appQueryPage.getList()) {
			//根据班级id查询用户表获得教师信息
			CenterUser centerUser = new CenterUser();
			centerUser.setClassId(teachClass.getClassId());
			centerUser.setUserType(GameConstants.USER_TYPE_TEAHCER);
			CenterUser userClass = centerUserService.selectCenterUser(centerUser);
			if(userClass == null && "".equals(userClass)){
				LogUtil.error(this.getClass(),"getClasses", "教师不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_GET_TEACHER);
			}
			
			//通过教师头像id获得头像图片地址
			String imgUrl = Common.getImgUrl(userClass.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(!StringUtil.isEmpty(imgUrl)){
				teachClassAll.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
			}
			if(teachClassAll.getStudentsNum() != null){
				teachClassAll.setStudentsNum(teachClassAll.getStudentsNum());
			}else {
				teachClassAll.setStudentsNum(0);
			}
			if(teachClassAll.getTotalCompleteHours() != null){
				teachClassAll.setLearningSeconds(teachClassAll.getTotalCompleteHours());//学时总秒数
			}else {
				teachClassAll.setLearningSeconds(0);//学时总秒数
			}
			teachClassAll.setClassIdStage(String.valueOf(teachClassAll.getClassId()));
			teachClassAll.setTeacherIds(String.valueOf(userClass.getUserId()));//教师ID
			teachClassAll.setTeacherName(userClass.getRealName());//教师名称
			
			teachClassAll.setPracticeCount(teachClassAll.getTotalTrainNum());//实训次数
			teachClassAll.setCourseCounts(teachClassAll.getOriginalCourseNum());//原创课时数
		}
		LogUtil.info(this.getClass(), "getClasses" , "获取班级信息成功");
		return appQueryPage.getMessageJSONObject("classes");
	}
	
	
	/**
	 * 移动端   4：
	 * 1.俱乐部模块下，我的俱乐部主页->培训，获取该俱乐部下的培训班列表(clubId不能为空)；
	 * 2.教学模块下，自由用户点击“学习”菜单会显示全站的培训班信息(clubId为空)；
	 * @return
	 */
	public JSONObject getClassForMobileFour(Integer clubId,Integer userId,Integer limit,Integer start){
		if(clubId != null && clubId != 0){//1.俱乐部模块下，我的俱乐部主页->培训，获取该俱乐部下的培训班列表(clubId不能为空)；
			//根据用户ID查询用户表获得俱乐部ID判读用户所在俱乐部和需要获得班级信息的俱乐部是否一致
			CenterUser centerUserClub = centerUserService.getCenterUserById(userId);
			if(centerUserClub == null ){
				LogUtil.error(this.getClass(),"getClasses", "用户不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.USER_ERROR);
			}
			//根据俱乐部ID查询俱乐部培训班表获得班级信息
			Map<String,Object> clubClass = new HashMap<String,Object>();
			clubClass.put("clubId", clubId);
			clubClass.put("isDelete", GameConstants.NO_DEL);
			//调用分页组件
			QueryPage<ClubTrainingClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, clubClass, ClubTrainingClass.class,"queryClubClassCount","queryClubClassByPage");
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(),"getClasses", "俱乐部下没有培训班");
				appQueryPage.error(AppErrorCode.NOT_CLASS);
			}
			for (ClubTrainingClass returnClubClass : appQueryPage.getList()) {
				//通过教师头像id获得头像图片地址
				String imgUrl = Common.getImgUrl(centerUserClub.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(!StringUtil.isEmpty(imgUrl)){
					returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				//通过班级图片ID获得班级图片
				String imgUrlClass = Common.getImgUrl(returnClubClass.getImageId(), BusinessConstant.DEFAULT_IMG_CLASS);
				if(!StringUtil.isEmpty(imgUrlClass)){
					returnClubClass.setClassLogoLink(Common.checkPic(imgUrlClass)== true ? imgUrlClass+ActiveUrl.HEAD_MAP:imgUrlClass);
				}
				//通过俱乐部ID查询俱乐部表获得俱乐部名称
				ClubMain clubMain = new ClubMain();
				clubMain.setClubId(clubId);
				ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
				//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
				ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
				clubJoinTraining.setClassId(returnClubClass.getClassId());
				clubJoinTraining.setUserId(userId);
				ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
				if(isclubJoinTraining == null){
					returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
				}else {
					returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
				}
				//购买官方课程包所需要的一级货币
				double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
				returnClubClass.setClassIdStage(String.valueOf(returnClubClass.getClassId()));//班级ID
				returnClubClass.setClassName(returnClubClass.getTitle());//班级名称
				returnClubClass.setTeacherIds(String.valueOf(returnClubClass.getCreateUserId()));//教练ID
				if(centerUserClub.getNickName() != null ){
					returnClubClass.setTeacherName(centerUserClub.getNickName());//教练名称
				}
				if(centerUserClub.getClubId() != null){
					returnClubClass.setClubId(centerUserClub.getClubId());//教练所属俱乐部ID
				}
				returnClubClass.setClubName(clubMainName.getClubName());//教练所属俱乐部名称
				if(returnClubClass.getStudentNum() != null){
					returnClubClass.setStudentsNum(returnClubClass.getStudentNum());//班级成员数量	
				}else {
					returnClubClass.setStudentsNum(0);//班级成员数量	
				}
				returnClubClass.setClubCourseCount(returnClubClass.getCourseNum());//培训班课程数
				returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
				if(returnClubClass.getIsBuyOfficial() != null ){
					returnClubClass.setHasBuyOfficialCourse(returnClubClass.getIsBuyOfficial());//该培训班是否已购买课程包 1:已购买
				}else {
					returnClubClass.setHasBuyOfficialCourse(0);//该培训班是否已购买课程包 1:已购买
				}
			}
			LogUtil.info(this.getClass(), "getClasses", "成功");
			return appQueryPage.getMessageJSONObject("classes");
		}else {//2.教学模块下，自由用户点击“学习”菜单会显示全站的培训班信息(clubId为空)
			//根据俱乐部ID查询俱乐部培训班表获得班级信息
			Map<String,Object> clubClass = new HashMap<String,Object>();
			clubClass.put("isDelete", GameConstants.NO_DEL);
			//调用分页组件
			QueryPage<ClubTrainingClass> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, clubClass, ClubTrainingClass.class,"queryClubClassCountAll","queryClubClassByPageAll");
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getClasses", "获取培训班失败");
				appQueryPage.error(AppErrorCode.NOT_CLASS);
			}
			for (ClubTrainingClass returnClubClass : appQueryPage.getList()) {
				//通过创建者ID查询用户表获得教师信息、
				CenterUser centerUser = new CenterUser();
				centerUser.setUserId(returnClubClass.getCreateUserId());
				CenterUser centerUserClub =centerUserService.selectCenterUser(centerUser);
				//通过教师头像id获得头像图片地址
				if(centerUserClub!=null){
					String imgUrl = Common.getImgUrl(centerUserClub.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(imgUrl)){
						returnClubClass.setTeacherHeadLink(Common.checkPic(imgUrl) == true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
					}
					//通过俱乐部ID查询俱乐部表获得俱乐部名称
					ClubMain clubMain = new ClubMain();
					clubMain.setClubId(centerUserClub.getClubId());
					ClubMain clubMainName =clubMainDao.selectClubMainEntity(clubMain);
					if(clubMainName != null && clubMainName.getClubName() != null){
						returnClubClass.setClubName(clubMainName.getClubName());//教练所属俱乐部名称
					}
					returnClubClass.setTeacherName(centerUserClub.getNickName());//教练名称
					  returnClubClass.setClubId(centerUserClub.getClubId());//教练所属俱乐部ID
				}
				//获取班级图片
				String imgUrl = Common.getImgUrl(returnClubClass.getImageId(), BusinessConstant.DEFAULT_IMG_TRAINING);
				if(!StringUtil.isEmpty(imgUrl)){
					returnClubClass.setClassLogoLink(Common.checkPic(imgUrl)== true ? imgUrl+ActiveUrl.HEAD_MAP:imgUrl);
				}
				//购买官方课程包所需要的一级货币  
				double gameRightFee =Double.parseDouble(SystemConfig.getString("CLUB_RIGHT_GET_OFFICE_FLEVEL_ACCOUNT").toString());
				
				//根据班级ID和用户ID查询参加培训人员表判断该登录用户是否已经加入班训班
				ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
				clubJoinTraining.setClassId(returnClubClass.getClassId());
				clubJoinTraining.setUserId(userId);
				ClubJoinTraining isclubJoinTraining =clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
				if(isclubJoinTraining == null){
					returnClubClass.setIsjoin(GameConstants.NOT_ADD_CLASS);//是否已加入该培训班
				}else {
					returnClubClass.setIsjoin(GameConstants.IS_ADD_CLASS);//是否已加入该培训班
				}
				returnClubClass.setClassName(returnClubClass.getTitle());//班级名称
				
				if(returnClubClass.getStudentNum() != null){
					returnClubClass.setStudentsNum(returnClubClass.getStudentNum());
				}else {
					returnClubClass.setStudentsNum(0);
				}
				returnClubClass.setClassLogoId(returnClubClass.getImageId());//培训班图片id
				returnClubClass.setClubCourseCount(returnClubClass.getCourseNum());//培训班课程数
				returnClubClass.setClassIdStage(String.valueOf(returnClubClass.getClassId()));//班级id
				returnClubClass.setBocNeedFLevelAccount(gameRightFee);//购买官方课程包所需要的一级货币
				returnClubClass.setTeacherIds(String.valueOf(returnClubClass.getCreateUserId()));
				if(returnClubClass.getIsBuyOfficial() != null ){
					returnClubClass.setHasBuyOfficialCourse(returnClubClass.getIsBuyOfficial());//该培训班是否已购买课程包 1:已购买
				}else {
					returnClubClass.setHasBuyOfficialCourse(0);//该培训班是否已购买课程包 1:已购买
				}
			} 
			LogUtil.info(this.getClass(), "getClasses", "获取班级信息成功");
			return appQueryPage.getMessageJSONObject("classes");
		}
		
	}
	/**
	 * @param memberId 传入访问用户id
	 * @param userId 当前登录用户的id
	 * @param limit 
	 * @param start
	 * @return
	 *//*
	public JSONObject getClassForMobileFIVE(Integer memberId, Integer userId, Integer limit,Integer start){
		
		return null;
	}*/
}
