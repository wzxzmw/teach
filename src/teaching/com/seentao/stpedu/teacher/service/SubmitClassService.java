package com.seentao.stpedu.teacher.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachCourseDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterSerialMaxService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubJoinTrainingService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseChapterService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.common.service.TeachRelClassCourseService;
import com.seentao.stpedu.common.service.TeachRelStudentClassService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

/**
 * 提交班级信息
 * <pre>项目名称：stpedu    
 * 类名称：SubmitClassService    
 */
@Service
public class SubmitClassService {
	
	@Autowired
	private TeachClassService teachClassService;
	
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;//教师班级关系表
	
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private TeachRelStudentClassService teachRelStudentClassService;
	
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;
	
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;//俱乐部培训班
	
	@Autowired
	private ClubMemberService clubMemberService;//俱乐部会员表
	
	@Autowired
	private CenterAccountService centerAccountService;//用户账户表
	
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	
	@Autowired
	private CenterSerialMaxService centerSerialMaxService;
	
	@Autowired
	private CenterLiveService centerLiveService;
	
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	
	@Autowired
	private TeachCourseDao teachCourseDao;
	
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	
	@Autowired
	private TeachRelClassCourseService teachRelClassCourseService;
	
	@Autowired
	private ClubMainService clubMainService;
	
	
	/**
	 * 
	 * submitClass(PC端:提交班级信息)   
	 * @param classId 班级id
	 * @param className 班级名称
	 * @param classLogoId 班级logo的图片id
	 * @param classType 班级类型  1:教学班；2:俱乐部培训班；
	 * @param classDesc 班级介绍 
	 * @param needToPay 加入时是否需要收费   1:是；0:否；默认是0；
	 * @param fLevelAccount 收取的一级货币
	 * @author ligs
	 * @param userId 用户id
	 * @date 2016年6月22日 上午10:24:21
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public JSONObject submitClass(Integer classId, String className, Integer classLogoId, Integer classType, Integer userId, String classDesc, Integer needToPay, Double fLevelAccount) {
		if(classType == 1){//1:教学班；
			//判断班级名称是否在2-20个字
			boolean isTwo = Common.isValid(className , 2 , 20);
			if(isTwo == false){
				LogUtil.error(this.getClass(), "submitClass", "请输入2-20个中英文字符");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_TWO_TWENTY);
			}
			return this.submitClassCreation(classId, userId, className, classLogoId);
		}else if(classType == 2){//2:俱乐部培训班；
			//判断培训班名称是否在2-20个字
			boolean isTwo = Common.isValid(className , 2 , 20);
			if(isTwo == false){
				LogUtil.error(this.getClass(), "submitClass", "请输入2-20个中英文字符");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_TWO_TWENTY);
			}
			//判断培训班介绍是否在2-70个字
			boolean isdesc = Common.isValid(classDesc, 2 , 70);
			if(isdesc == false){
				LogUtil.error(this.getClass(), "submitClass", "请输入2-70个中英文字符");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.IS_DESC_JOIN);
			}
			return this.submitClassClub(userId, classId, className, classDesc, classLogoId, needToPay, fLevelAccount);
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.REQUEST_PARAM_ERROR);
	}
	
	/**
	 * submitDefaultClass(设置默认班级)   
	 * @param classId 班级ID
	 * @param userId 用户ID
	 * @author ligs
	 * @date 2016年6月22日 上午11:22:23
	 * @return
	 */
	@Transactional
	public JSONObject submitDefaultClass(Integer classId, Integer userId) {
		//判断设置的用户是否为该班级的教师
		TeachRelTeacherClass teachClass = new TeachRelTeacherClass();
		teachClass.setClassId(classId);
		teachClass.setTeacherId(userId);
		TeachRelTeacherClass isteachClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachClass);
		if(isteachClass == null){
			LogUtil.error(this.getClass(), "submitDefaultClass","不是该班级的教师");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_TEACHERISCLASS);
		}else {
			//有默认班级清空默认班级    添加默认班级
			TeachRelTeacherClass teacherClassRel = new TeachRelTeacherClass();
			teacherClassRel.setTeacherId(userId);
			teacherClassRel.setIsDefault(GameConstants.DENY);
			teachRelTeacherClassService.updateTeachRelTeacherClassByKey(teacherClassRel);
			
			//添加默认班级
			TeachRelTeacherClass teachTeacherClass = new TeachRelTeacherClass();
			teachTeacherClass.setClassId(classId);
			teachTeacherClass.setTeacherId(userId);
			teachTeacherClass.setIsDefault(GameConstants.ARE);
			teachRelTeacherClassService.updateTeachRelTeacherClassByKey(teachTeacherClass);
			//修改用户表classId
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			centerUser.setClassId(classId);
			try {
				centerUserService.updateCenterUserByKey(centerUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId.toString());
			LogUtil.info(this.getClass(), "submitDefaultClass", "设置默认班级成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}
	}
	/**
	 * joinClass(报名加入班级)   
	 * @param classId 班级ID
	 * @param userId 用户ID
	 * @param classType 班级类型 1：教学班  2：俱乐部培训班
	 * @author ligs
	 * @date 2016年6月22日 下午2:49:52
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public JSONObject joinClass(Integer classId, Integer userId, Integer classType)  {
		if(classType == GameConstants.TEACHING_CLASS){//教学班
			//判断要加入的用户是否为未加入班级的学生用户
			TeachRelStudentClass teachRelStudentClassadd = new TeachRelStudentClass();
			teachRelStudentClassadd.setStudentId(userId);
			teachRelStudentClassadd.setIsDelete(GameConstants.NO_DEL);
			TeachRelStudentClass isteachRelStudentClassadd = teachRelStudentClassService.getTeachRelStudentClass(teachRelStudentClassadd);
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			CenterUser isUserClass = centerUserService.selectCenterUser(centerUser);
			if(null != isUserClass.getClassId() ||  null != isteachRelStudentClassadd ){
				LogUtil.error(this.getClass(), "joinClass", "已加入班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_ALREADY_CLASS);
			}else if(isUserClass.getUserType() != GameConstants.USER_TYPE_STUDENT){
				LogUtil.error(this.getClass(), "joinClass", "不是学生");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO_USER);
			}
			//判断要加入的学生与班级是否为同一个学校  班级是否存在
			TeachClass teachClass = teachClassService.getTeachClassById(classId);
			if(teachClass == null ){
				LogUtil.error(this.getClass(), "joinClass", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASS_NOT);
			}else if(!teachClass.getSchoolId().equals(isUserClass.getSchoolId())){
				LogUtil.error(this.getClass(), "joinClass", "不是本校学生");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO_SCHOOL);
			} 
			//学生加入班级
			try {
				TeachRelStudentClass teachRelStudentClass = new TeachRelStudentClass();
				teachRelStudentClass.setClassId(classId);
				teachRelStudentClass.setStudentId(userId);
				teachRelStudentClass.setAssessScore(0);
				teachRelStudentClass.setIsIdentify(0);
				teachRelStudentClass.setIsDelete(0);
				teachRelStudentClass.setCertificateStatus(0);
				teachRelStudentClassService.insertTeachRelStudentClass(teachRelStudentClass);
				//更新用户的当前班级
				CenterUser centerUsers = new CenterUser();
				centerUsers.setClassId(classId);
				centerUsers.setUserId(userId);
				centerUserService.updateCenterUserByKey(centerUsers);
				//删除redis
				JedisCache.delRedisVal(CenterUser.class.getSimpleName() ,userId.toString());
				
				TeachClass teachClas = teachClassService.getTeachClassById(classId); 
				//更新班级成员数量
				TeachClass teachClassNum = new TeachClass();
				teachClassNum.setClassId(classId);
				teachClassNum.setStudentsNum(teachClas.getStudentsNum()+1);
				teachClassService.updateTeachClassByKey(teachClassNum);
				//删除redis
				JedisCache.delRedisVal(TeachClass.class.getSimpleName(),classId.toString());
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "joinClass", "失败");
			}
			//增加该班级下所有课程的应交作业人数
			//根据班级ID获得该班级下的章节ID
			TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
			teachCourseChapter.setClassId(classId);
			List<TeachCourseChapter> teachCourseChapterLists = teachCourseChapterService.getChapter(teachCourseChapter);
			if(teachCourseChapterLists != null && teachCourseChapterLists.size() > 0){
				for (TeachCourseChapter teachCourseChapters : teachCourseChapterLists) {
					//通过章节ID和章节类型为4的作业卡片 查询所有的课程卡
					TeachCourseCard teachCourseCard = new TeachCourseCard();
					teachCourseCard.setChapterId(teachCourseChapters.getChapterId());
					teachCourseCard.setCardType(4);
					List<TeachCourseCard> teachCourseCardList =teachCourseCardService.getTeachCourseCard(teachCourseCard);
					if(teachCourseCardList != null || teachCourseCardList.size() > 0){
						//增加课程卡课程关系表应交作业人数
						StringBuffer sb = new StringBuffer();
						for (TeachCourseCard teachCourseCards : teachCourseCardList) {
							TeachRelCardCourse teachRelCardCourses = new TeachRelCardCourse();
							teachRelCardCourses.setCardId(teachCourseCards.getCardId());
							TeachRelCardCourse isteachRelCardCourses = teachRelCardCourseService.selectTeachRelCardCourse(teachRelCardCourses);
							if(isteachRelCardCourses != null && isteachRelCardCourses.getPlanNum() != null){
								sb.append(teachCourseCards.getCardId().toString()).append(",");
							}
						}
						if(sb.length() > 1){
							String ids = sb.toString();
							ids = ids.substring(0,ids.length()-1);
							teachRelCardCourseService.updateTeachRelCardCoursePrivateAlls(ids);
						}
					}
				}
			}
			LogUtil.info(this.getClass(), "joinClass", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}else if(classType == GameConstants.CLUB_TRAIN_CLASS){//培训班
			// 根据用户userId,查询是否加入班级,加入则不能在加入
			if(clubJoinTrainingService.queryClubJoinTrainingByClassId(classId, userId)!=null)
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_IS_CLASS);
			//通过班级ID查询培训班表获得加入需要的费用以及是否免费加入 若果加入班级免费直接加入班级
			ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
			clubTrainingClass.setClassId(classId);
			ClubTrainingClass clubTrainingClassAll = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
			if(clubTrainingClassAll == null){
				LogUtil.info(this.getClass(), "joinClass", "培训班不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_CLASSES_JOIN);
			}else {
				//判断当前用户是否为该俱乐部的会长  如果为会长就免费加入
				ClubMember clubMember =  new ClubMember();
				clubMember.setUserId(userId);
				clubMember.setClubId(clubTrainingClassAll.getClubId());
				clubMember = clubMemberService.getClubMemberOne(clubMember);
				if(clubMember != null){
					if(clubMember.getLevel() == 1){//如果为会长
						return this.joinClassCharge(classId, userId);//加入自己俱乐部下教练创建的培训班免费
					}
				}
				if(clubTrainingClassAll.getCostType() == 1){//加入培训班收费
					//通过用户ID和账户类型查询用户账户表判断该用户的账户余额是否满足加入培训班
					CenterAccount centerAccount = new CenterAccount();
					centerAccount.setUserId(userId);
					centerAccount.setUserType(1);
					centerAccount.setAccountType(GameConstants.STAIR_ONE);//账户类型
					CenterAccount centerAccountAll =centerAccountService.getCenterAccount(centerAccount);
					if(centerAccountAll == null){
						LogUtil.error(this.getClass(),"joinClass", "该用户没有账户");
					}
					if(centerAccountAll.getBalance() < clubTrainingClassAll.getCostAmount()){//如果账户余额小于加入班级所需费用
						LogUtil.error(this.getClass(), "joinClass", "加入培训班所需费余额不足");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.LACK_OF_BALANCE);
					}else{
						try {
							//流水单号
							String max = centerSerialMaxService.getCenterSerialMaxByNowDate(1);
							//时间戳精确到秒
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dates = sdf.format(new Date());
						    Date parses = sdf.parse(dates);
						    int dateTime = (int)(parses.getTime()/1000);
							//用户账户余额如果>=加入培训班的费用，可以加入培训班参加培训人员表·增加一条数据
							ClubJoinTraining addClubJoinTraining = new ClubJoinTraining();
							addClubJoinTraining.setClassId(classId);//培训班ID
							addClubJoinTraining.setUserId(userId);//用户ID
							addClubJoinTraining.setJoinTime(dateTime);//加入时间
							addClubJoinTraining.setIsDelete(GameConstants.DENY);//是否删除·
							addClubJoinTraining.setTotalCost(0.0);
							clubJoinTrainingService.insertClubJoinTraining(addClubJoinTraining);
							//用户账户表扣除对应金额
							CenterAccount updateCenterAccount = new CenterAccount();
							updateCenterAccount.setAccountId(centerAccountAll.getAccountId());
							updateCenterAccount.setBalance(centerAccountAll.getBalance() - clubTrainingClassAll.getCostAmount());
							centerAccountService.updateCenterAccountByKey(updateCenterAccount);
							//修改参加培训人员表累计消费金额
							addClubJoinTraining.setTotalCost(clubTrainingClassAll.getCostAmount());
							clubJoinTrainingService.updateClubJoinTrainingUdToCost(addClubJoinTraining);
							//货币变动表添加数据
							CenterMoneyChange centerMoneyChange = new CenterMoneyChange();
							centerMoneyChange.setAccountId(centerAccountAll.getAccountId());//账户ID
							centerMoneyChange.setSerialNumber(max);//交易流水号
							centerMoneyChange.setChangeTime(dateTime);//变动时间
							centerMoneyChange.setAmount(clubTrainingClassAll.getCostAmount());//变动金额
							centerMoneyChange.setChangeType(GameConstants.SPENDING);//变动类型 2：支出
							centerMoneyChange.setStatus(GameConstants.SUCCESSFUL);//状态
							centerMoneyChange.setRelObjetType(8);
							centerMoneyChange.setRelObjetId(userId);
							centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChange);
						//俱乐部账户操作
							//获取俱乐部账户数据
							CenterAccount centerAccounts = new CenterAccount();
							centerAccounts.setUserId(clubTrainingClassAll.getClubId());
							centerAccounts.setUserType(2);
							centerAccounts.setAccountType(GameConstants.STAIR_ONE);//账户类型 一级货币
							CenterAccount centerAccountAlls =centerAccountService.getCenterAccount(centerAccounts);
							//流水单号
							String clubmax = centerSerialMaxService.getCenterSerialMaxByNowDate(1);
							//俱乐部账户增加收益
							CenterAccount updateCenterAccountadd = new CenterAccount();
							updateCenterAccountadd.setAccountId(centerAccountAlls.getAccountId());
							updateCenterAccountadd.setBalance(centerAccountAlls.getBalance() + clubTrainingClassAll.getCostAmount());
							centerAccountService.updateCenterAccountByKey(updateCenterAccountadd);
							//货币变动表添加数据
							CenterMoneyChange centerMoneyChanges = new CenterMoneyChange();
							centerMoneyChanges.setAccountId(centerAccountAlls.getAccountId());//账户ID
							centerMoneyChanges.setSerialNumber(clubmax);//交易流水号
							centerMoneyChanges.setChangeTime(dateTime);//变动时间
							centerMoneyChanges.setAmount(clubTrainingClassAll.getCostAmount());//变动金额
							centerMoneyChanges.setChangeType(GameConstants.INCOME);//变动类型 2：支出
							centerMoneyChanges.setStatus(GameConstants.SUCCESSFUL);//状态
							centerMoneyChanges.setRelObjetType(8);
							centerMoneyChanges.setRelObjetId(clubTrainingClassAll.getClubId());
							centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChanges);
							//更新俱乐部培训班学员数量 删除redis
							ClubTrainingClass clubTrainingClassNum = new ClubTrainingClass();
							clubTrainingClassNum.setClassId(classId);
							ClubTrainingClass clubClassNum =clubTrainingClassService.getClubTrainingClass(clubTrainingClassNum);
							ClubTrainingClass updateClubClassNum = new ClubTrainingClass();
							updateClubClassNum.setClassId(clubClassNum.getClassId());
							updateClubClassNum.setStudentNum(clubClassNum.getStudentNum()+1);
							clubTrainingClassService.updateClubTrainingClassByKey(updateClubClassNum);
							//删除俱乐部培训班redis
							JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
							LogUtil.info(this.getClass(),"joinClass", "成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "");
						} catch (Exception e) {
							LogUtil.error(this.getClass(), "joinClass", "加入培训班失败");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ADD_CLASS_ERROR);
						}
					}
				}else if(clubTrainingClassAll.getCostType() == 0){//加入培训班免费
					return this.joinClassCharge(classId, userId);
				}
			}
		}
		return null;
	}
	
	/**
	 * 加入培训班免费
	 * @param classId
	 * @param userId
	 * @return
	 */
	public JSONObject joinClassCharge(Integer classId,Integer userId){
		//时间戳精确到秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = sdf.format(new Date());
	    Date parses = null;
		try {
			parses = sdf.parse(dates);
		} catch (ParseException e) {
			LogUtil.error(this.getClass(), "joinClass","日期转换异常");
		}
	    int dateTime = (int)(parses.getTime()/1000);
		//参加培训人员表增加一条数据
		ClubJoinTraining addClubJoinTraining = new ClubJoinTraining();
		addClubJoinTraining.setClassId(classId);//培训班ID
		addClubJoinTraining.setUserId(userId);//用户ID
		addClubJoinTraining.setJoinTime(dateTime);//加入时间
		addClubJoinTraining.setIsDelete(GameConstants.DENY);//是否删除
		addClubJoinTraining.setTotalCost(0.0);
		clubJoinTrainingService.insertClubJoinTraining(addClubJoinTraining);
		//更新俱乐部培训班学员数量 删除redis
		ClubTrainingClass clubTrainingClassNum = new ClubTrainingClass();
		clubTrainingClassNum.setClassId(classId);
		ClubTrainingClass clubClassNum =clubTrainingClassService.getClubTrainingClass(clubTrainingClassNum);
		ClubTrainingClass updateClubClassNum = new ClubTrainingClass();
		updateClubClassNum.setClassId(clubClassNum.getClassId());
		updateClubClassNum.setStudentNum(clubClassNum.getStudentNum()+1);
		clubTrainingClassService.updateClubTrainingClassByKey(updateClubClassNum);
		//删除俱乐部培训班redis
		JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
		LogUtil.info(this.getClass(),"joinClass", "成功");
		return Common.getReturn(AppErrorCode.SUCCESS, "");
	}
	/**
	 * submitTeacherOperation(教师邀请或踢出学生操作)   
	 * @param userId 用户ID
	 * @param classId 班级ID
	 * @param classType 班级类型  1：教学班   2：俱乐部培训班
	 * @param studentId 邀请或踢出的学生id 多个id用逗号分隔
	 * @param actionType 1:邀请加入；2:踢出班级；
	 * @author ligs
	 * @date 2016年6月22日 下午4:43:23
	 * @return
	 */
	@Transactional
	public JSONObject submitTeacherOperation(Integer userId, Integer classId, String studentId, Integer actionType, Integer classType) {
		if(classType == GameConstants.TEACHING_CLASS){//教学班
			if(actionType == 1){//1：邀请加入
				try {
					TeachRelStudentClass TeachStudentClass =new TeachRelStudentClass();
					TeachStudentClass.setStudentId(Integer.valueOf(studentId));//学生ID
					TeachStudentClass.setClassId(classId);
					TeachStudentClass.setAssessScore(0);
					TeachStudentClass.setIsIdentify(0);
					TeachStudentClass.setIsDelete(GameConstants.NO_DEL);
					TeachStudentClass.setCertificateStatus(0);
					teachRelStudentClassService.insertTeachRelStudentClass(TeachStudentClass);
					JedisCache.delRedisVal(TeachRelStudentClass.class.getSimpleName(),classId.toString());
					//添加用户表学生对应班级ID
					//获取加入班级人员
					CenterUser centerUse = new CenterUser();
					centerUse.setUserId(Integer.valueOf(studentId));
					centerUse.setClassId(classId);
					centerUserService.updateCenterUserByKey(centerUse);
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(),studentId);
					//更新班级表学院数量
					TeachClass teachClass = new TeachClass();
					teachClass.setClassId(classId);
					//获取到学生要加入的班级信息
					TeachClass teachClassNum = teachClassService.selectSingleTeachClass(teachClass);
					//更新班级学员数量
					TeachClass teachClassCount  = new TeachClass();
					teachClassCount.setClassId(teachClassNum.getClassId());
					teachClassCount.setStudentsNum(teachClassNum.getStudentsNum()+1);
					teachClassService.updateTeachClassByKey(teachClassCount);
					JedisCache.delRedisVal(TeachClass.class.getSimpleName(),classId.toString());
					//增加该班级下所有课程的应交作业人数
					//根据班级ID获得改班级下的章节ID
					TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
					teachCourseChapter.setClassId(classId);
					List<TeachCourseChapter> teachCourseChapterLists = teachCourseChapterService.getChapter(teachCourseChapter);
					if(teachCourseChapterLists != null && teachCourseChapterLists.size() > 0){
					for (TeachCourseChapter teachCourseChapters : teachCourseChapterLists) {
						//通过章节ID和章节类型为4的作业卡片 查询所有的课程卡
						TeachCourseCard teachCourseCard = new TeachCourseCard();
						teachCourseCard.setChapterId(teachCourseChapters.getChapterId());
						teachCourseCard.setCardType(4);
						List<TeachCourseCard> teachCourseCardList =teachCourseCardService.getTeachCourseCard(teachCourseCard);
						StringBuffer sb = new StringBuffer();
						if(teachCourseCardList != null || teachCourseCardList.size() > 0){
							for (TeachCourseCard teachCourseCards : teachCourseCardList) {
								TeachRelCardCourse teachRelCardCourses = new TeachRelCardCourse();
								teachRelCardCourses.setCardId(teachCourseCards.getCardId());
								TeachRelCardCourse isteachRelCardCourses = teachRelCardCourseService.selectTeachRelCardCourse(teachRelCardCourses);
								if(isteachRelCardCourses != null && isteachRelCardCourses.getPlanNum() != null){
									sb.append(teachCourseCards.getCardId().toString()).append(",");
								}
							}
							if(sb.length() > 1){
								String ids = sb.toString();
								ids = ids.substring(0,ids.length()-1);
								teachRelCardCourseService.updateTeachRelCardCoursePrivateAlls(ids);
							}
						}
					}
				}
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "submitTeacherOperation", "邀请加入班级失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO);
				}
				LogUtil.info(this.getClass(), "submitTeacherOperation", "邀请加入教学班成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}else if(actionType == 2){//2：踢出班级
				try {
				//判断操作用户是否为该学生所属班级的教师
				CenterUser centerUser = centerUserService.getCenterUserById(userId);
				if(!centerUser.getClassId().equals(classId)){
					LogUtil.error(this.getClass(), "submitTeacherOperation", "不是该学生所属班级的教师");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO_TEACHER);
				}
				//调用春磊接口判断是否在参加比赛
				IGameInterfaceService iGameInterfaceService= HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				// 判断操作学生是否参加比赛
				String isGame = iGameInterfaceService.getIsPlayMatchByuserId(Integer.valueOf(studentId));
				//获取返回结果
				JSONObject jsonObjClub= JSONObject.parseObject(JSONObject.parseObject(isGame).get("result").toString());
				int isAuth = jsonObjClub.getIntValue("isAuth");
				if(isAuth == 0){// 0：用户正在比赛不能删除
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.STUDENT_IS_GAME);
				}
				//删除学生班级关系表
					TeachRelStudentClass TeachStudentClass =new TeachRelStudentClass();
					TeachStudentClass.setStudentId(Integer.valueOf(studentId));
					TeachStudentClass.setClassId(classId);
					TeachStudentClass.setIsDelete(GameConstants.YES_DEL);
					teachRelStudentClassService.updateTeachRelStudentClassByKeyNoRel(TeachStudentClass);
					//删除Redis
					JedisCache.delRedisVal(TeachRelStudentClass.class.getSimpleName(),classId.toString());
				
					//清空用户表学生对应班级ID
					CenterUser updateUser = new CenterUser();
					//获取踢出班级人员id
					updateUser.setUserId(Integer.valueOf(studentId));
					updateUser.setClassId(null);
					centerUserService.updateCenterUserTeach(updateUser);//清空用户表学生对应班级ID
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(),studentId);
					//获取踢出学生的班级信息
					TeachClass teachClass = new TeachClass();
					teachClass.setClassId(classId);
					TeachClass teachClassNum = teachClassService.selectSingleTeachClass(teachClass);
					//减去班级学院数量
					TeachClass teachClassCount  = new TeachClass();
					teachClassCount.setClassId(teachClassNum.getClassId());
					teachClassCount.setStudentsNum(teachClassNum.getStudentsNum()-1);
					teachClassService.updateTeachClassByKey(teachClassCount);
					JedisCache.delRedisVal(TeachClass.class.getSimpleName(),classId.toString());
					//减去该班级下所有课程的应交作业人数
					//根据班级ID获得改班级下的章节ID
					TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
					teachCourseChapter.setClassId(classId);
					List<TeachCourseChapter> teachCourseChapterLists = teachCourseChapterService.getChapter(teachCourseChapter);
					if(teachCourseChapterLists != null && teachCourseChapterLists.size() > 0){
					for (TeachCourseChapter teachCourseChapters : teachCourseChapterLists) {
						//通过章节ID和章节类型为4的作业卡片 查询所有的课程卡
						TeachCourseCard teachCourseCard = new TeachCourseCard();
						teachCourseCard.setChapterId(teachCourseChapters.getChapterId());
						teachCourseCard.setCardType(4);
						List<TeachCourseCard> teachCourseCardList =teachCourseCardService.getTeachCourseCard(teachCourseCard);
						if(teachCourseCardList != null || teachCourseCardList.size() > 0){
							StringBuffer sb = new StringBuffer();
							for (TeachCourseCard teachCourseCards : teachCourseCardList) {
								TeachRelCardCourse teachRelCardCourses = new TeachRelCardCourse();
								teachRelCardCourses.setCardId(teachCourseCards.getCardId());
								TeachRelCardCourse isteachRelCardCourses = teachRelCardCourseService.selectTeachRelCardCourse(teachRelCardCourses);
								if(isteachRelCardCourses != null && isteachRelCardCourses.getPlanNum() != null){
									if(isteachRelCardCourses != null && isteachRelCardCourses.getPlanNum() != null){
										sb.append(teachCourseCards.getCardId().toString()).append(",");
									}
								}
								if(sb.length() > 1){
									String ids = sb.toString();
									ids = ids.substring(0,ids.length()-1);
									teachRelCardCourseService.updateTeachRelCardCoursePrivateMinus(ids);
								}
							}
					    }
					}
				}
				} catch (Exception e) {
					e.printStackTrace();
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO);
				}
				LogUtil.info(this.getClass(),"submitTeacherOperation", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}else if(classType ==GameConstants.CLUB_TRAIN_CLASS){//俱乐部培训班
			/*if(actionType == 1){//邀请加入
				//通过班级ID查询培训班表获得加入需要的费用以及是否免费加入 若果加入班级免费直接加入班级
				ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
				clubTrainingClass.setClassId(classId);
				ClubTrainingClass clubTrainingClassAll = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
				//通过用户ID和账户类型查询用户账户表判断该用户的账户余额是否满足加入培训班
				String[] userIds = studentId.split(",");
				CenterAccount centerAccountAll =null;
				for (int i = 0;i< userIds.length ;i++) {
				CenterAccount centerAccount = new CenterAccount();
				centerAccount.setUserId(Integer.valueOf(userIds[i]));
				centerAccount.setAccountType(GameConstants.STAIR_ONE);//账户类型
				centerAccountAll =centerAccountService.getCenterAccount(centerAccount);
				}
				if(clubTrainingClassAll.getCostType() == 2){//加入培训班收费
					if(centerAccountAll.getBalance() < clubTrainingClassAll.getCostAmount()){//如果账户余额小于加入班级所需费用
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.LACK_OF_BALANCE);
					}else{
						try {
							//流水单号
							String max = centerSerialMaxService.getCenterSerialMaxByNowDate(1);
							//时间戳精确到秒
						 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dates = sdf.format(new Date());
					    	Date parses = sdf.parse(dates);
					    	int dateTime = (int)(parses.getTime()/1000);
							//邀请学生加入
							String[] auserIds = studentId.split(",");
							for (int i = 0;i< auserIds.length ;i++) {
								ClubJoinTraining clubJoinTraining =new ClubJoinTraining();
								clubJoinTraining.setUserId(Integer.valueOf(auserIds[i]));//学生ID
								clubJoinTraining.setClassId(classId);
								clubJoinTrainingService.insertClubJoinTraining(clubJoinTraining);
								JedisCache.delRedisVal(ClubJoinTraining.class.getSimpleName(),classId.toString());
							}
							//用户账户表扣除对应金额
							String[] userIdArray = studentId.split(",");
							for (int i = 0;i< userIdArray.length ;i++) {
							CenterAccount updateCenterAccount = new CenterAccount();
							updateCenterAccount.setAccountId(centerAccountAll.getAccountId());
							updateCenterAccount.setUserId(Integer.valueOf(userIdArray[i]));
							updateCenterAccount.setBalance(centerAccountAll.getBalance() - clubTrainingClassAll.getCostAmount());
							centerAccountService.updateCenterAccountByKey(updateCenterAccount);
							}
							//货币变动表添加数据
							String[] userIdArr= studentId.split(",");
							for (int i = 0;i< userIdArr.length ;i++) {
							CenterMoneyChange centerMoneyChange = new CenterMoneyChange();
							centerMoneyChange.setAccountId(centerAccountAll.getAccountId());//账户ID
							centerMoneyChange.setSerialNumber(max);//交易流水号
							centerMoneyChange.setChangeTime(dateTime);//变动时间
							centerMoneyChange.setAmount(clubTrainingClassAll.getCostAmount());//变动金额
							centerMoneyChange.setChangeType(GameConstants.SPENDING);//变动类型 2：支出
							centerMoneyChange.setRelObjetId(Integer.valueOf(userIdArr[i]));//关联对象ID
							centerMoneyChange.setStatus(GameConstants.SUCCESSFUL);//状态
							centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChange);
							}
							//获得学院数量
							Integer countNum = 0;
							String[] stuids = studentId.split(",");
							for (int i = 0; i < stuids.length; i++) {
								countNum += 1;
							}
							//更新俱乐部培训班学员数量 删除redis
							ClubTrainingClass clubTrainingClassNum = new ClubTrainingClass();
							clubTrainingClassNum.setClassId(classId);
							ClubTrainingClass clubClassNum =clubTrainingClassService.getClubTrainingClass(clubTrainingClassNum);
							ClubTrainingClass updateClubClassNum = new ClubTrainingClass();
							updateClubClassNum.setClassId(clubClassNum.getClassId());
							updateClubClassNum.setStudentNum(clubClassNum.getStudentNum()+countNum);
							clubTrainingClassService.updateClubTrainingClassByKey(clubTrainingClassNum);
							//删除俱乐部培训班redis
							JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
						} catch (Exception e) {
							
						}
					}
				}else if(clubTrainingClassAll.getCostType() == 1){//加入培训班免费
					//时间戳精确到秒
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dates = sdf.format(new Date());
					Date parses = sdf.parse(dates);
					int dateTime = (int)(parses.getTime()/1000);
					//用户账户余额如果>=加入培训班的费用，参加培训人员表增加一条数据
					//邀请学生加入
					String[] auserIds = studentId.split(",");
					for (int i = 0;i< auserIds.length ;i++) {
						ClubJoinTraining clubJoinTraining =new ClubJoinTraining();
						clubJoinTraining.setUserId(Integer.valueOf(auserIds[i]));//学生ID
						clubJoinTraining.setClassId(classId);
						clubJoinTrainingService.insertClubJoinTraining(clubJoinTraining);
						JedisCache.delRedisVal(ClubJoinTraining.class.getSimpleName(),classId.toString());
					}
					//获得学院数量
					Integer countNum = 0;
					String[] stuids = studentId.split(",");
					for (int i = 0; i < stuids.length; i++) {
						countNum += 1;
					}
					//更新俱乐部培训班学员数量 删除redis
					ClubTrainingClass clubTrainingClassNum = new ClubTrainingClass();
					clubTrainingClassNum.setClassId(classId);
					ClubTrainingClass clubClassNum =clubTrainingClassService.getClubTrainingClass(clubTrainingClassNum);
					ClubTrainingClass updateClubClassNum = new ClubTrainingClass();
					updateClubClassNum.setClassId(clubClassNum.getClassId());
					updateClubClassNum.setStudentNum(clubClassNum.getStudentNum()+countNum);
					clubTrainingClassService.updateClubTrainingClassByKey(clubTrainingClassNum);
					//删除俱乐部培训班redis
					JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
				}
			}else if(actionType == 2){//踢出班级 本期不做
					//判断操作用户是否为该学生所属培训班的教练
					ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
					clubTrainingClass.setCreateUserId(userId);
					ClubTrainingClass clubTrainingClassAll = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
					if(clubTrainingClassAll != null){
						String[] userIds = studentId.split(",");
						for (int i = 0;i< userIds.length ;i++) {
							//判断操作用户是教练还是会长
							ClubMember clubMember = new ClubMember();
							clubMember.setUserId(userId);
							ClubMember isClubMember =clubMemberService.selectClubMember(clubMember);
							if(isClubMember.getLevel() == 1){//会长
								ClubMember clubMembers = new ClubMember();
								clubMembers.setUserId(userId);
								clubMembers.setClubId(clubTrainingClassAll.getClubId());
								ClubMember isClubMembersO =clubMemberService.selectClubMember(clubMembers);
								if(isClubMembersO == null){
									return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLUB_LEVE);
								}
							}else if(isClubMember.getLevel() == 2){//教练
								ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
								clubJoinTraining.setUserId(Integer.valueOf(userIds[i]));
								clubJoinTraining.setIsDelete(GameConstants.NO_DEL);
								ClubJoinTraining clubJoinTrainingAll = clubJoinTrainingService.getClubJoinTrainingAll(clubJoinTraining);
								if(!clubTrainingClassAll.getClassId().equals(clubJoinTrainingAll.getClassId())){
									LogUtil.error(this.getClass(), "submitTeacherOperation", "不是该学生所属班级的教练");
									 return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO_COACH);
								}
							}else if(isClubMember.getLevel() == 3){
								 return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NO_COACH);
							}
						}
						//修改参加培训人员表数据isdelete为1
						int stuNum = 0;
						List<ClubJoinTraining> upClubJoinTraining = new ArrayList<>();
						for (int a = 0;a< userIds.length ;a++) {
							stuNum += 1;
							ClubJoinTraining delClubJoinTraining = new ClubJoinTraining();
							delClubJoinTraining.setUserId(Integer.valueOf(userIds[a]));
							delClubJoinTraining.setIsDelete(GameConstants.YES_DEL);
							upClubJoinTraining.add(delClubJoinTraining);
							//删除redis
							JedisCache.delRedisVal(ClubJoinTraining.class.getSimpleName(),userIds[a]);
						}
						clubJoinTrainingService.updateClubJoinTrainingByKeyAll(upClubJoinTraining);
						//减去培训班学员数量
						ClubTrainingClass updateClubTrainingClass = new ClubTrainingClass();
						updateClubTrainingClass.setClassId(classId);
						updateClubTrainingClass.setStudentNum(stuNum);
						clubTrainingClassService.updateClubTrainingClassNum(updateClubTrainingClass);
						//删除培训班redis
						JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
					LogUtil.info(this.getClass(), "submitTeacherOperation", "踢出培训班成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				}else {
					LogUtil.error(this.getClass(), "submitTeacherOperation","未创建班级");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_NOT_CLASSES);
				}
			}*/
		}
		return null;
	}
	
	
	/**
	 * 提交班级信息：教学班 |修改教学班信息
	 * @return
	 */
	public JSONObject submitClassCreation(Integer classId,Integer userId,String className,Integer classLogoId){
		LogUtil.info(this.getClass(),"submitClassCreation","classId="+classId+"_userId="+userId+"-className="+className+"-classLogoId="+classLogoId);
		if(classId == null || classId == 0){//班级ID为空时添加班级信息 
			//查询用户是否为教师
			CenterUser centerUser = centerUserService.getCenterUserById(userId);
			
			if(centerUser == null || centerUser.getUserType() != 1){//不为教师不能创建班级
				LogUtil.error(this.getClass(),"submitClass", "不是教师");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NOT_TEACHER);
			}else {
				//判断同一学校下班级名称是否有相同的
				TeachClass isShooleteachClass = new TeachClass();
				isShooleteachClass.setSchoolId(centerUser.getSchoolId());//学校ID
				List<TeachClass> shooleteachClass =	teachClassService.getTeachClassList(isShooleteachClass);
				if(!CollectionUtils.isEmpty(shooleteachClass)){
					for (TeachClass teachClass : shooleteachClass) {
						if(teachClass.getClassName().equals(className.trim())){
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASSNAME_REPETITION);
						}
					}
				}
				//时间戳精确到秒
			    int dateTime =Common.timePoke();
			    
				TeachClass teachClass = new TeachClass();
				teachClass.setClassName(className.trim());
				teachClass.setClassLogoId(classLogoId);
				//获得用户所在学校 以及学院
				if(centerUser != null && centerUser.getSchoolId() != null){
					teachClass.setSchoolId(centerUser.getSchoolId());
				}
				if(centerUser != null && centerUser.getInstId() != null){
					teachClass.setInstituteId(centerUser.getInstId());
				}
				teachClass.setStudentsNum(0);
				teachClass.setTotalTrainNum(0);
				teachClass.setOriginalCourseNum(0);
				teachClass.setCreateTime(dateTime);
				teachClass.setIsDelete(GameConstants.NO_DEL);
				try {
					@SuppressWarnings("unused")
					Integer returnId = teachClassService.insertTeachClass(teachClass);
					//添加数据到教师班级关系表   查询教师班级关系表是否已经创建班级
					TeachRelTeacherClass teachRelTeacherClass =new  TeachRelTeacherClass();
					teachRelTeacherClass.setTeacherId(userId);
					TeachRelTeacherClass isteachRelTeacherClass = teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
					if(isteachRelTeacherClass == null){
						//未创建过班级   添加教师班级关系表  教师第一次创建班级默认为默认班级
						TeachRelTeacherClass insertteachRelTeacherClass =new  TeachRelTeacherClass();
						insertteachRelTeacherClass.setTeacherId(userId);
						insertteachRelTeacherClass.setClassId(teachClass.getClassId());
						insertteachRelTeacherClass.setIsDefault(1);
						teachRelTeacherClassService.insertTeachRelTeacherClass(insertteachRelTeacherClass);
						//修改教师用户表信息
						CenterUser centerUserss = new CenterUser();
						centerUserss.setClassId(teachClass.getClassId());
						centerUserss.setUserId(userId);
						try {
							centerUserService.updateCenterUserByKey(centerUserss);
							//删除redis
							JedisCache.delRedisVal(CenterUser.class.getSimpleName() ,userId.toString());
						} catch (Exception e) {
							LogUtil.error(this.getClass(), "submitClass", "用户信息修改失败");
						}
					}else {
						//教师如果已经创建过班级    创建的班级不为默认班级
						TeachRelTeacherClass insertteachRelTeacherClass =new  TeachRelTeacherClass();
						insertteachRelTeacherClass.setTeacherId(userId);
						insertteachRelTeacherClass.setClassId(teachClass.getClassId());
						insertteachRelTeacherClass.setIsDefault(0);
						teachRelTeacherClassService.insertTeachRelTeacherClass(insertteachRelTeacherClass);
					}
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "submitClass", "提交班级信息失败", e);
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_SAVE_CLASS);
					}
					
					//根据班级ID和课程ID添加数据到班级课程关系表  获得案例课程
					//获取班级课程关系表预制数据
					List<TeachRelClassCourse> TeachRelClassList = new ArrayList<TeachRelClassCourse>();	
				    TeachRelClassCourse teachRelClassCourses = new TeachRelClassCourse();
					teachRelClassCourses.setClassId(0);
					List<TeachRelClassCourse> teachCourseLists =  teachRelClassCourseService.getTeachRelClassCourse(teachRelClassCourses);
					if(teachCourseLists.size() > 0 && teachCourseLists != null){
						for (TeachRelClassCourse teachRelClassCours : teachCourseLists) {
							//通过班级ID和课程ID添加数据到班级课程关系表
							TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
							teachRelClassCourse.setClassId(teachClass.getClassId());
							teachRelClassCourse.setCourseId(teachRelClassCours.getCourseId());//课程ID
							teachRelClassCourse.setShowType(1);
							teachRelClassCourse.setCreateTime(dateTime);
							teachRelClassCourse.setTotalStudyNum(0);
							TeachRelClassList.add(teachRelClassCourse);
						}
						teachRelClassCourseService.insertTeachRelClassCourseAll(TeachRelClassList);
					}
					
					//创建班级的同时创建章节以及章节下的课程卡
					/**
					 * 1、根据班级ID添加数据到课程章节表数据
					 * 2、根据章节ID添加数据到课程卡表 
					 * 4、根据课程卡ID和课程ID添加数据到课程卡课程关系表
					 * 5、根据班级ID和课程ID添加数据到班级课程关系表
					 */
					//根据班级ID添加数据到课程章节表数据
					//查询课程章节表班级ID为0的获得预制章节信息
					List<TeachCourseChapter> teachCouChapterList = new ArrayList<TeachCourseChapter>();	
					TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
					teachCourseChapter.setClassId(0);
					List<TeachCourseChapter> teachCourseChapterList = teachCourseChapterService.getChapter(teachCourseChapter);
					if(teachCourseChapterList != null && teachCourseChapterList.size() > 0){
						for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
							TeachCourseChapter teachCourseChapteradd = new TeachCourseChapter();
							teachCourseChapteradd.setChapterName(teachCourseChapters.getChapterName());
							teachCourseChapteradd.setChapterNo(teachCourseChapters.getChapterNo());
							teachCourseChapteradd.setClassId(teachClass.getClassId());
							teachCouChapterList.add(teachCourseChapteradd);
						}
					}
					//批量添加
					teachCourseChapterService.insertTeachCourseChapterIdAll(teachCouChapterList);
					//根据班级ID获得对应章节
					TeachCourseChapter teachCourseChapterClass = new TeachCourseChapter();
					teachCourseChapterClass.setClassId(teachClass.getClassId());
					List<TeachCourseChapter> teachCourseChapterListClass = teachCourseChapterService.getChapter(teachCourseChapterClass);
					for (TeachCourseChapter teachCourseChapterNo : teachCourseChapterListClass) {
						//根据章节id添加数据到课程卡表 
						if(teachCourseChapterNo.getChapterNo() == 1){//章节一
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 1 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 2){//章节二
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 2 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 3){//章节三
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 3 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 4){//章节四
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 4 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 5){//章节五
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 5 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 6){//章节六
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 6 , dateTime);
							}
						}else if(teachCourseChapterNo.getChapterNo() == 7){//章节七
							//根据预制的章节ID查询课程卡表获得章节下的预制课程卡信息
							for (TeachCourseChapter teachCourseChapters : teachCourseChapterList) {
								this.addChapter(teachCourseChapters, userId, teachCourseChapterNo, 7 , dateTime);
							}
						}
					}
					LogUtil.info(this.getClass(),"submitClass", "创建班级成功,预制章节信息课程卡");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}else {//班级ID不为空时修改班级信息
			//判断是否为该班级的教师
			TeachRelTeacherClass teachRelTeacherClass =new  TeachRelTeacherClass();
			teachRelTeacherClass.setClassId(classId);
			teachRelTeacherClass.setTeacherId(userId);
			TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
			if(isteachRelTeacherClass == null ){
				LogUtil.error(this.getClass(), "submitClass", "不是该班级的教师");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_TEACHERISCLASS );
			}else{
				//判断班级是否存在
				TeachClass isClass = teachClassService.getTeachClassById(classId);
				if(isClass == null && "".equals(isClass)){
					LogUtil.error(this.getClass(), "submitClass", "班级不存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.CLASS_NOT);
				}
				//判断同一学校下班级名称是否有相同的
				TeachClass isShooleteachClass = new TeachClass();
				isShooleteachClass.setSchoolId(isClass.getSchoolId());//学校ID
				List<TeachClass> shooleteachClass =	teachClassService.getTeachClassList(isShooleteachClass);
				if(shooleteachClass != null){
					for (TeachClass teachClass : shooleteachClass) {
						if(!teachClass.getClassId().equals(classId)){
							if(teachClass.getClassName().trim().equals(className.trim())){
								LogUtil.error(this.getClass(), "submitClass", "班级名称重复");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASSNAME_REPETITION);
							}
						}
					}
				}
				//修改班级信息
				TeachClass teachClass = new TeachClass();
				teachClass.setClassId(classId);
				teachClass.setClassName(className.trim());
				teachClass.setClassLogoId(classLogoId);
				try {
					teachClassService.updateTeachClassByKey(teachClass);
					//修改班级信息后删除redis缓存
					JedisCache.delRedisVal(TeachClass.class.getSimpleName(),classId.toString());
					LogUtil.info(this.getClass(),"submitClass", "成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "");
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "submitClass", "提交班级信息失败", e);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.ERROR_SAVE_CLASS);
				}
			}
		}
	}
	
	/**
	 * 俱乐部培训班：提交班级信息 | 修改班级信息
	 * @return
	 */
	public JSONObject submitClassClub(Integer userId,Integer classId,String className,String classDesc,Integer classLogoId,Integer needToPay,Double fLevelAccount){
		//判断班级ID是否为空  为空时创建培训班 不为空时修改培训班
		if(classId == null || classId == 0){//班级ID为空时创建培训班
			if(className == null || className.equals(0) || className.equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_IMPORT_NAME);
			}else if(classDesc == null || classDesc.equals(0) || classDesc.equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,  AppErrorCode.IS_IMPORT_DESC);
			}else if(classLogoId == null ||classLogoId.equals(0) || classLogoId.equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_IMPORT_PICTURE);
			}else if(needToPay == 1){
				if(fLevelAccount == 0 | fLevelAccount == null | fLevelAccount.equals("")){
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,  AppErrorCode.IS_IMPORT_COST);
				}
			}
			//通过用户ID查询俱乐部会员表判断是否为俱乐部会长或教练
			ClubMember clubMember = new ClubMember();
			clubMember.setUserId(userId);
			clubMember.setMemberStatus(GameConstants.IS_ADD_CLASS);
			ClubMember isClubMember = clubMemberService.getClubMemberOne(clubMember);
			if(isClubMember.getLevel() == GameConstants.MEMBER){
				LogUtil.error(this.getClass(), "submitClass", "只有会长或教练可以创建培训班");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.NO_JURISDICTION );
			}
			if(needToPay == 1){//收费
				//判断字数在2-70字
				boolean isNum = Common.isValid(classDesc,2, 70);
				if(isNum == false){
					LogUtil.error(this.getClass(), "submitClass", "班级介绍为2-70个汉字");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_DESC_JOIN);
				}
				//通过俱乐部ID查询俱乐部表判断是否已购买增值运营权
				ClubMain clubMain = new ClubMain();
				clubMain.setClubId(isClubMember.getClubId());
				ClubMain isClubMain = clubMainService.selectClubMainEntity(clubMain);
				if(isClubMain != null){
					if(isClubMain.getIsBuyClubVip() != 1){
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_JURISDICTION_JOIN);
					}
				}
			}
			//时间戳精确到秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dates = sdf.format(new Date());
		    Date parses = null;
			try {
				parses = sdf.parse(dates);
			} catch (ParseException e) {
				LogUtil.error(this.getClass(), "submitClass","日期转换异常");
			}
		    int dateTime = (int)(parses.getTime()/1000);
			
			ClubTrainingClass createClass = new ClubTrainingClass();
			createClass.setCreateUserId(userId);
			createClass.setClubId(isClubMember.getClubId());
			createClass.setTitle(className.trim());//班级名称
			createClass.setImageId(classLogoId);//班级logoID
			createClass.setCostType(needToPay);//是否收费
			createClass.setCostAmount(fLevelAccount);//收取的一级货币
			createClass.setIntroduce(classDesc);//班级介绍
			
			createClass.setCourseNum(0);
			createClass.setStudentNum(0);
			createClass.setCreateTime(dateTime);
			createClass.setIsBuyOfficial(0);
			createClass.setIsDelete(0);
			@SuppressWarnings("unused")
			Integer clubclassId = clubTrainingClassService.insertClubTrainingClass(createClass);
			//创建人添加到培训班
			ClubJoinTraining clubJoinTraining = new ClubJoinTraining();
			clubJoinTraining.setClassId(createClass.getClassId());
			clubJoinTraining.setUserId(userId);
			clubJoinTraining.setJoinTime(dateTime);
			clubJoinTraining.setTotalCost(0.0);
			clubJoinTraining.setIsDelete(GameConstants.NO_DEL);
			clubJoinTrainingService.insertClubJoinTraining(clubJoinTraining);
			//动态表添加一条动态数据
				CenterLive centerLive = new CenterLive();
				centerLive.setLiveType(3);//俱乐部培训班
				centerLive.setLiveMainType(3);//俱乐部培训班
				centerLive.setLiveMainId(createClass.getClassId());//动态主体类型
				centerLive.setIsTop(0);//是否置顶
				centerLive.setCreateTime(dateTime);
				centerLive.setIsDelete(GameConstants.NO_DEL);//是否删除
				centerLive.setMainObjetType(2);//俱乐部动态
				centerLive.setMainObjetId(isClubMember.getClubId());
				centerLiveService.insertCenterLive(centerLive);
				LogUtil.error(this.getClass(), "submitClass", "成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
		}else{//不为空时修改培训班
			//判断是否设置班级名称
			if(className == null || className.equals(0) || className.equals("")){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_IMPORT_NAME);
			}else if(classDesc == null || classDesc.equals(0) || classDesc.equals("")){
			//判断班级介绍是否为空
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,  AppErrorCode.IS_IMPORT_DESC);
			}else if(classLogoId == null ||classLogoId.equals(0) || classLogoId.equals("")){
			//判断班级封面图片是否为空
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_IMPORT_PICTURE);
			}else if(needToPay == 1){
			//如果创建的班级为收费 判断费用不能为0
				if(fLevelAccount == 0 | fLevelAccount == null | fLevelAccount.equals("")){
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,  AppErrorCode.IS_IMPORT_COST);
				}
			}
			//根据班级ID判断对应班级是否存在 与用户ID判断是否为班级创建人
			ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
			clubTrainingClass.setClassId(classId);
			ClubTrainingClass isClubTrainingClass = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
			if(isClubTrainingClass == null){
				LogUtil.error(this.getClass(), "submitClass", "班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASS_NOT);
			}
			//判断是否为该俱乐部的会长
			ClubMember clubMember = new ClubMember();
			clubMember.setUserId(userId);
			clubMember.setClubId(isClubTrainingClass.getClubId());
			clubMember = clubMemberService.getClubMemberOne(clubMember);
			if(clubMember != null){
				if(clubMember.getLevel() != 1){
					if(!isClubTrainingClass.getCreateUserId().equals(userId)){
						LogUtil.error(this.getClass(), "submitClass", "不是该培训班的创建人不能修改信息");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NOT_CRENT);
					}
				}
			}
			//判断字数在2-70字
			boolean isNum = Common.isValid(classDesc,2, 70);
			if(isNum == false){
				LogUtil.error(this.getClass(), "submitClass", "班级介绍为2-70个汉字");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_DESC_JOIN);
			}
			if(needToPay == 1){//收费时判断是否购买运营权
				//通过俱乐部ID查询俱乐部表判断是否已购买增值运营权
				ClubMain clubMain = new ClubMain();
				clubMain.setClubId(isClubTrainingClass.getClubId());
				ClubMain isClubMain = clubMainService.selectClubMainEntity(clubMain);
				if(isClubMain != null){
					if(isClubMain.getIsBuyClubVip() != 1){
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_JURISDICTION_JOIN);
					}
				}
			}
			//修改培训班数据
			ClubTrainingClass updateClubClass = new ClubTrainingClass();
			updateClubClass.setClassId(classId);//班级ID
			updateClubClass.setTitle(className.trim());//班级名称
			updateClubClass.setIntroduce(classDesc);//介绍
			updateClubClass.setImageId(classLogoId);//班级logoID
			updateClubClass.setCostType(needToPay);//是否收费
			updateClubClass.setCostAmount(fLevelAccount);//收取的一级货币
			clubTrainingClassService.updateClubTrainingClassByKey(updateClubClass);
			JedisCache.delRedisVal(ClubTrainingClass.class.getSimpleName(),classId.toString());
			LogUtil.info(this.getClass(), "submitClass", "成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "");
		}
	}
	
	
	/**
	 * 预制7个章节、28个课程卡、以及课程卡下的官方课程
	 */
	public void addChapter(TeachCourseChapter teachCourseChapters,Integer userId,TeachCourseChapter teachCourseChapterNo,int number ,int dateTime){
		if(teachCourseChapters.getChapterNo() == number){
			TeachCourseCard teachCourseCard = new TeachCourseCard();
			teachCourseCard.setChapterId(teachCourseChapters.getChapterId());
			List<TeachCourseCard> teachCourseCardList = teachCourseCardService.getTeachCourseCard(teachCourseCard);
			//获得预制课程卡信息
			for (TeachCourseCard teachCourseCards : teachCourseCardList) {
			//添加课程卡数据到课程卡表	
			TeachCourseCard teachCourseCardadd = new TeachCourseCard();
			teachCourseCardadd.setCardTitle(teachCourseCards.getCardTitle());//课程卡标题
			teachCourseCardadd.setCardExplain(teachCourseCards.getCardExplain());//课程卡说明
			teachCourseCardadd.setImageId(teachCourseCards.getImageId());//图片ID
			teachCourseCardadd.setTeacherId(userId);//教师ID
			teachCourseCardadd.setStatusId(0);//状态ID
			teachCourseCardadd.setChapterId(teachCourseChapterNo.getChapterId());//章节ID
			teachCourseCardadd.setCardType(teachCourseCards.getCardType());//课程卡类型
			
			Integer cardId = teachCourseCardService.insertTeachCourseCard(teachCourseCardadd);
			
		  //获取添加的课程卡ID
			if(teachCourseCardadd.getCardType() == 1){//实训卡片
				continue;
			}else if(teachCourseCardadd.getCardType() == 2){//知识点卡片
					//查询课程卡课程关系表获得预制数据
					TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
					teachRelCardCourse.setCardId(teachCourseCards.getCardId());
					List<TeachRelCardCourse> teachRelCardCourseList = teachRelCardCourseService.getTeachRelCardCourse(teachRelCardCourse);
					//通过课程卡ID和课程ID添加到课程卡课程关系表
					if(teachRelCardCourseList.size() > 0 && teachRelCardCourseList != null){
						List<TeachRelCardCourse> teachRelList = new ArrayList<TeachRelCardCourse>();
						for (TeachRelCardCourse teachRelCardCourses : teachRelCardCourseList) {
							//通过课程ID查询课程表获取课程类型
							TeachCourse teachCourses = new TeachCourse();
							teachCourses.setCourseId(teachRelCardCourses.getCourseId());
							TeachCourse isteachCourses =teachCourseDao.selectTeachCourse(teachCourses);
							if(isteachCourses != null){
								if(isteachCourses.getCourseType() == 1){
									TeachRelCardCourse teachRelCardCourseadd = new TeachRelCardCourse();
									teachRelCardCourseadd.setCardId(teachCourseCardadd.getCardId());//课程卡ID
									teachRelCardCourseadd.setCourseId(teachRelCardCourses.getCourseId());//课程ID
									teachRelCardCourseadd.setShowType(teachRelCardCourses.getShowType());//显示类型
									teachRelCardCourseadd.setCreateTime(dateTime);//创建时间
									teachRelCardCourseadd.setPlanNum(teachRelCardCourses.getPlanNum());//应交作业人数
									teachRelCardCourseadd.setActualNum(teachRelCardCourses.getActualNum());//已交作业人数
									teachRelCardCourseadd.setTotalStudyNum(teachRelCardCourses.getTotalStudyNum());//累计学习人次
									teachRelList.add(teachRelCardCourseadd);
								}
							}
						}
						teachRelCardCourseService.insertTeachRelCardCourseAlls(teachRelList);
					}
				}else if(teachCourseCardadd.getCardType() == 3){//案例卡片
					//查询课程卡课程关系表获得预制数据
					TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
					teachRelCardCourse.setCardId(teachCourseCards.getCardId());//预制数据
					List<TeachRelCardCourse> teachRelCardCourseList = teachRelCardCourseService.getTeachRelCardCourse(teachRelCardCourse);
					//通过课程卡ID和课程ID添加到课程卡课程关系表
					if(teachRelCardCourseList.size() > 0 && null != teachRelCardCourseList){
						List<TeachRelCardCourse> teachRelList = new ArrayList<TeachRelCardCourse>();
						for (TeachRelCardCourse teachRelCardCourses : teachRelCardCourseList) {
							//通过课程ID查询课程表获取课程类型
							TeachCourse teachCourses = new TeachCourse();
							teachCourses.setCourseId(teachRelCardCourses.getCourseId());
							TeachCourse isteachCourses =teachCourseDao.selectTeachCourse(teachCourses);
							if(isteachCourses != null){
								if(isteachCourses.getCourseType() == 1){
									TeachRelCardCourse teachRelCardCourseadd = new TeachRelCardCourse();
									teachRelCardCourseadd.setCardId(teachCourseCardadd.getCardId());//课程卡ID
									teachRelCardCourseadd.setCourseId(teachRelCardCourses.getCourseId());//课程ID
									teachRelCardCourseadd.setShowType(teachRelCardCourses.getShowType());//显示类型
									teachRelCardCourseadd.setCreateTime(dateTime);//创建时间
									teachRelCardCourseadd.setPlanNum(teachRelCardCourses.getPlanNum());//应交作业人数
									teachRelCardCourseadd.setActualNum(teachRelCardCourses.getActualNum());//已交作业人数
									teachRelCardCourseadd.setTotalStudyNum(teachRelCardCourses.getTotalStudyNum());//累计学习人次
									teachRelList.add(teachRelCardCourseadd);
								}
							}
						}
						teachRelCardCourseService.insertTeachRelCardCourseAlls(teachRelList);
					}
				}else if(teachCourseCardadd.getCardType() == 4){//作业卡片
					continue;
				}
			}
		}
	}
}
