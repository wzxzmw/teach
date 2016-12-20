package com.seentao.stpedu.user.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.attention.service.CenterAttentionService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.PageObject;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.ArenaGuessBet;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterAttention;
import com.seentao.stpedu.common.entity.CenterCompany;
import com.seentao.stpedu.common.entity.CenterInvitationCode;
import com.seentao.stpedu.common.entity.CenterSession;
import com.seentao.stpedu.common.entity.CenterSmsVerification;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.PublicRegion;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourseCardCount;
import com.seentao.stpedu.common.entity.TeachInstitute;
import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;
import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.entity.User;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.ArenaCompetitionService;
import com.seentao.stpedu.common.service.ArenaGuessBetService;
import com.seentao.stpedu.common.service.BaseCenterAttentionService;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterCompanyService;
import com.seentao.stpedu.common.service.CenterInvitationCodeService;
import com.seentao.stpedu.common.service.CenterSessionService;
import com.seentao.stpedu.common.service.CenterSmsVerificationService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubJoinTrainingService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.PublicRegionService;
import com.seentao.stpedu.common.service.TeachCourseCardCountService;
import com.seentao.stpedu.common.service.TeachInstituteService;
import com.seentao.stpedu.common.service.TeachPlanService;
import com.seentao.stpedu.common.service.TeachPlanSignService;
import com.seentao.stpedu.common.service.TeachRelStudentClassService;
import com.seentao.stpedu.common.service.TeachRelStudentCourseService;
//git@123.103.9.86:23585/stpedu.git
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.HttpRequest;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.MD5Utils;
import com.seentao.stpedu.utils.RandomCode;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

import sun.misc.BASE64Encoder;

/** 
* @author yy
* @date 2016年6月22日 下午9:11:18 
*/
@Service
public class BusinessUserService {
	@Autowired
	private TeachRelStudentClassService teachRelStudentClassService;
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private TeachCourseCardCountService teachCourseCardCountService;
	@Autowired
	private TeachPlanService teachPlanService;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private BaseCenterAttentionService baseCenterAttentionService;
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;
	@Autowired
	private TeachPlanSignService teachPlanSignService;
	@Autowired
	private ArenaGuessBetService arenaGuessBetService;
	@Autowired
	private CenterAccountService centerAccountService;
	@Autowired
	private CenterSessionService centerSessionService;
	@Autowired
	private CenterInvitationCodeService centerInvitationCodeService;
	@Autowired
	private CenterSmsVerificationService centerSmsVerificationService;
	@Autowired
	private ArenaCompetitionService arenaCompetitionService;
	@Autowired
	private PublicRegionService publicRegionService;
	@Autowired
	private ClubMainService clubMainService;
	@Autowired
	private CenterAttentionService centerAttentionService;
	@Autowired
	private CenterCompanyService centerCompanyService;
	@Autowired
	private TeachInstituteService teachInstituteService;
	@Autowired
	private TeachRelStudentCourseService teachRelStudentCourseService;
	
	/**
	 * 获取人员信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param memberId	人员id
	 * @param shcoolId	学校id
	 * @param classId 班级id
	 * @param clubId	俱乐部id
	 * @param gameId 比赛id
	 * @param gameFieldId	场地id
	 * @param platformModule 平台模块
	 * @param searchWord 搜索词
	 * @param gameEventId 赛事id
	 * @param clubApplyStatus 该人员申请当前俱乐部的状态
	 * @param taskId 计划任务id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param hasQualification 查询类型
	 * @param inquireType 是否有认证资格
	 * @param classType 班级类型
	 */
	public String getUsers(Integer userType, String userName, String userId, String userToken, Integer platformModule,
			Integer start, Integer limit, Integer inquireType, Integer hasQualification, Integer clubApplyStatus,
			String memberId, String schoolId, String classId, String clubId, String gameId, String gameFieldId,
			String searchWord, String gameEventId, String taskId,Integer classType,String quizId) {
		JSONObject result = new JSONObject();
		JSONObject jo = null;
		//String trimSearchWord = searchWord.trim();
		//String selectName = searchWord.equals("")?null:trimSearchWord.trim();
		String selectName = searchWord;
		if(inquireType == BusinessConstant.INQUIRETYPE_1){//根据班级id获取成绩列表
			jo = this.inquireTypeToOne(classId,start,limit);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_2){//进入比赛详情页队长选人时调用
			String resUserIds = null;
			String userIds = null;
			try {
				IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				if(gameId!= null){
					resUserIds = game.getUsersByMatchId(Integer.valueOf(gameId));
					if(!StringUtil.isEmpty(resUserIds)){
						JSONObject json = JSONObject.parseObject(resUserIds);
						json = json.getJSONObject("result");
						userIds = json.getString("userIds");
					}	
				}
				
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "getUsers", "inquireType="+inquireType+"远程调用比赛接口失败", e);
				result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				result.put(GameConstants.MSG, BusinessConstant.ERROR_HPROSE);
				return result.toJSONString();
			}
			CenterUser user = new CenterUser();
			if(platformModule == BusinessConstant.PLATFORM_MODULE_1){//教学模块 班级id
				user.setClassId(Integer.valueOf(classId));
				user.setRealName(selectName);//真实姓名
				user.setUserType(GameConstants.USER_TYPE_STUDENT);//类型：学生2
			}else if(platformModule==BusinessConstant.PLATFORM_MODULE_2){//竞技场模块
				user.setNickName(selectName);
			}else if(platformModule==BusinessConstant.PLATFORM_MODULE_3){//俱乐部模块 俱乐部id
				user.setProfession(1);//已加入俱乐部的人
				user.setClubId(Integer.valueOf(clubId));
				user.setNickName(selectName);
			}else{
				result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				result.put(GameConstants.MSG, BusinessConstant.ERROR_TYPE_CODE);//类型传入错误
				return result.toJSONString();
			}
			LogUtil.info(this.getClass(), "getUsers", "调用比赛接口platformModule="+platformModule+",userIds="+userIds);
			user.setUserIds(userIds);
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwo(user,null,platformModule);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_3){//推荐的人是查询全站
			//回答数提问数量的和 最大的为推荐的人
			CenterUser user = new CenterUser();
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToThree(user,null,inquireType);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_4){//该班级下活跃的人
			//人员信息  头像信息
			//班级下回答数提问数量的和 最大的为推荐的人
			CenterUser user = new CenterUser();
			user.setStart(start);
			user.setLimit(limit);
			user.setClassId(Integer.valueOf(classId));
			jo = this.inquireTypeToThree(user,null,inquireType);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_5){//邀请的时候展示该班级推荐的人  支持搜索
			//根据搜索词决定返回数据
			if(StringUtil.isEmpty(searchWord)){
				CenterUser user = new CenterUser();
				user.setStart(start);
				user.setLimit(limit);
				user.setIsSchlooNull("true");//推荐的人取固定10条数据
//				user.setStart(start);
//				user.setLimit(limit);
//				if(!"0".equals(classId)){
//					user.setClassId(Integer.valueOf(classId));
//				}
				jo = this.inquireTypeToThree(user,userId,inquireType);
			}else{
				//全站搜索人
				CenterUser user = new CenterUser();
				user.setRealName(selectName);//昵称
				user.setStart(start);
				user.setLimit(limit);
				jo = this.inquireTypeToTwo(user,userId,null);
			}
		}else if(inquireType==BusinessConstant.INQUIRETYPE_6){//管理后台 查询学生列表
			CenterUser user = new CenterUser();
			user.setClassId(Integer.valueOf(classId));
			user.setRealName(selectName);//真实姓名
			user.setIsIdentify(hasQualification==-1?null:hasQualification);//默认是0
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToSix(user,start,limit);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_7){//当点击“邀请加入”按钮时调用，获取该学校下未加入任何班级的人员；
			CenterUser user = new CenterUser();
			user.setIsClassNull("1");//数据库判断使用
			user.setRealName(selectName);//真实姓名
			user.setSchoolId(Integer.valueOf(schoolId));
			user.setUserType(GameConstants.USER_TYPE_STUDENT);//查询学生
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwo(user,null,null);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_8){//点击学校的班级进入该班级下的学生排名列表页时调用
			CenterUser user = new CenterUser();
			user.setStart(start);
			user.setLimit(limit);
			user.setClassId(Integer.valueOf(classId));
			user.setUserType(GameConstants.USER_TYPE_STUDENT);
			jo = inquireTypeToEight(user);//userid排序
		}else if(inquireType==BusinessConstant.INQUIRETYPE_9){//赛事下的竞猜列表右侧的奖金排名
			//猜赢次数的奖金最多的人
			ArenaGuessBet arenaguessbet = new ArenaGuessBet();
			arenaguessbet.setStart(start);
			arenaguessbet.setLimit(limit);
			jo = inquireTypeToNine(arenaguessbet);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_10){//赛事下的竞猜列表右侧的猜赢排名
			//猜赢次数最多的人
			ArenaGuessBet arenaguessbet = new ArenaGuessBet();
			arenaguessbet.setStart(start);
			arenaguessbet.setLimit(limit);
			jo = inquireTypeToTen(arenaguessbet);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_11){//赛事下的竞猜列表右侧的土豪排名
			//二级货币最多的人
			//人员信息 图片信息
			CenterAccount account = new CenterAccount();
			account.setAccountType(GameConstants.STAIR_TWO);//二级货币
			account.setUserType(BusinessConstant.ACCOUNT_USER_TYPE_1);//个人用户
			account.setStart(start);
			account.setLimit(limit);
			jo = inquireTypeToEleven(account);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_12){//教学模块，进入课程主页，根据班级id和登录用户信息获取学习报告
			jo = new JSONObject();
			List<User> list = new ArrayList<User>();
			User user = new User();
			TeachCourseCardCount t = new TeachCourseCardCount();
			t.setClassId(Integer.valueOf(classId));
			TeachCourseCardCount res = teachCourseCardCountService.getTeachCourseCardCountOne(t);
			if(res!=null){
				Integer completeNum = res.getCompleteNum()==null?0:res.getCompleteNum();//完成数量
				Integer notEndNum = res.getNotEndNum()==null?0:res.getNotEndNum();//未结束
				Integer notSetNum = res.getNotSetNum()==null?0:res.getNotSetNum();//未设置
				Integer notStartNum = res.getNotStartNum()==null?0:res.getNotStartNum();//未开始
				Integer completeChapter = res.getTotalCompleteChapter()==null?0:res.getTotalCompleteChapter();//完成章节数量
//				Integer completeHours = res.getTotalCompleteHours()==null?0:res.getTotalCompleteHours();//完成学时
				user.setCompletedCourseCards(String.valueOf(completeNum));
				user.setAllCourseCards(completeNum+notEndNum+notSetNum+notStartNum);
//				user.setLearningSeconds(completeHours);
				user.setCompletedChapters(completeChapter);
				user.setNotStartCourseCards(notStartNum);
				user.setNotCompletedCourseCards(notEndNum);
				user.setNotSetCourseCards(notSetNum);
				setCourseCount(user, Integer.valueOf(classId), Integer.valueOf(userId));
			}
			user.setClassId(classId);
			list.add(user);
			jo.put("users", list);
			jo.put("returnCount", list.size());
		}else if(inquireType==BusinessConstant.INQUIRETYPE_13){//教学模块，进入详细的成绩界面，根据班级id和个人信息获取学习报告(非登录id)
			jo = new JSONObject();
			List<User> list = new ArrayList<User>();
			//人员信息
			String cacheUser = null;
			if(!StringUtil.isEmpty(memberId)){
				cacheUser = RedisComponent.findRedisObject(Integer.valueOf(memberId), CenterUser.class);
			}
			CenterUser centeruser = null;
			TeachCourseCardCount res = null;
			if(!StringUtil.isEmpty(cacheUser)){
				User user = new User();
				centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				if(centeruser!=null){
					String linkImg = "";
					if(centeruser.getHeadImgId()==null){
						linkImg = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_USER);
						user.setHeadLinkId("");
						user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
						//
					}else{
						linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
						user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
					}
					//证书信息
					TeachRelStudentClass teachrelstudentclass = new TeachRelStudentClass();
					teachrelstudentclass.setClassId(centeruser.getClassId());
					teachrelstudentclass.setStudentId(centeruser.getUserId());
					teachrelstudentclass.setIsDelete(0);
					TeachRelStudentClass resTeachRelStudentClass = teachRelStudentClassService.selectTeachRelStudentClass(teachrelstudentclass);
					if(null!=resTeachRelStudentClass){
						//1:未申请；2:申请中；3:申请成功；4:申请失败；证书状态
						//证书状态。0:未申请，1:申请中，2:已发证。
						Integer certificate = resTeachRelStudentClass.getCertificateStatus();
						if(certificate!=null){
							if(certificate>=0 && certificate<=BusinessConstant.CERTIFICATE_STATUS.length){
								user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[certificate]);
							}else{
								user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
							}
						}else{
							user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
						}
						user.setScore((float)resTeachRelStudentClass.getAssessScore());//评分
						user.setHasQualification(resTeachRelStudentClass.getIsIdentify());//是否有认证资格
					}else{
						user.setScore(0f);//评分
						user.setHasQualification(0);//是否有认证资格
						user.setCertRequestStatus(1);
					}
					user.setUserId(memberId==null?userId:memberId);
					user.setRealName(centeruser==null?"":centeruser.getRealName());
					user.setNickname(centeruser==null?"":centeruser.getNickName());
					//学校信息
					TeachSchool teachschool = null;
					String cacheTeachSchool = RedisComponent.findRedisObject(centeruser.getSchoolId(), TeachSchool.class);
					if(!StringUtil.isEmpty(cacheTeachSchool)){
						teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
					}
					user.setSchoolId(teachschool==null?"":String.valueOf(teachschool.getSchoolId()));
					user.setSchoolName(teachschool==null?"":teachschool.getSchoolName());
				}
				//签到次数
				TeachPlan t1 = new TeachPlan();
				t1.setClassId(centeruser.getClassId());
				t1.setStudentId(Integer.valueOf(memberId));//非登录用户
				Integer totalPlan = teachPlanService.queryCount(t1);
				Integer haveTotal = teachPlanService.queryHaveCount(t1);
				user.setHasSigninTasks(haveTotal==null?0:haveTotal);//已签到数量
				user.setAllTasks(totalPlan==null?0:totalPlan);//计划总量
				
				TeachCourseCardCount t = new TeachCourseCardCount();
				t.setClassId(centeruser.getClassId());
				res = teachCourseCardCountService.getTeachCourseCardCountOne(t);
				if(res!=null){
					Integer completeNum = res.getCompleteNum()==null?0:res.getCompleteNum();//完成数量
					Integer notEndNum = res.getNotEndNum()==null?0:res.getNotEndNum();//未结束
					Integer notSetNum = res.getNotSetNum()==null?0:res.getNotSetNum();//未设置
					Integer notStartNum = res.getNotStartNum()==null?0:res.getNotStartNum();//未开始
					Integer completeChapter = res.getTotalCompleteChapter()==null?0:res.getTotalCompleteChapter();//完成章节数量
//					Integer completeHours = res.getTotalCompleteHours()==null?0:res.getTotalCompleteHours();//完成学时
					user.setCompletedCourseCards(String.valueOf(completeNum));
					user.setAllCourseCards(completeNum+notEndNum+notSetNum+notStartNum);
//					user.setLearningSeconds(completeHours);
					user.setCompletedChapters(completeChapter);
					user.setNotStartCourseCards(notStartNum);
					user.setNotCompletedCourseCards(notEndNum);
					user.setNotSetCourseCards(notSetNum);
					setCourseCount(user, centeruser.getClassId(), Integer.valueOf(memberId));
				}else{
					user.setCompletedCourseCards("0");
					user.setAllCourseCards(0);
					user.setLearningSeconds(0);
					user.setCompletedChapters(0);
					user.setNotStartCourseCards(0);
					user.setNotCompletedCourseCards(0);
					user.setNotSetCourseCards(0);
				}
				list.add(user);
			}
			jo.put("users", list);
			jo.put("returnCount", list.size());
		}else if(inquireType==BusinessConstant.INQUIRETYPE_14){//教学模块，进入详细的成绩界面，左侧分页显示该班级下的人员；
			CenterUser user = new CenterUser();
			user.setClassId(Integer.valueOf(classId));
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToSix(user,start,limit);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_15){//比赛编辑页，报名分组模式为队长指派时使用，获取该班级下不是当前比赛中的参赛者的人，或者该俱乐部下不是当前比赛中的参赛者的人
			if(classType==GameConstants.TEACHING_CLASS){//教学班
				//TODO 调用春磊接口返回该比赛下的所有人  传入比赛id 返回list<userId>
				String resUserIds = null;
				String userIds = null;
				try {
					IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
					resUserIds = game.getUsersByMatchId(Integer.valueOf(gameId));
					if(!StringUtil.isEmpty(resUserIds)){
						JSONObject json = JSONObject.parseObject(resUserIds);
						json = json.getJSONObject("result");
						userIds = json.getString("userIds");
					}
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "getUsers", "inquireType="+inquireType+"远程调用比赛接口失败", e);
					result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
					result.put(GameConstants.MSG, BusinessConstant.ERROR_HPROSE);
					return result.toJSONString();
				}
				CenterUser user = new CenterUser();
				if(platformModule==BusinessConstant.PLATFORM_MODULE_1){//教学模块 班级id
					user.setClassId(Integer.valueOf(classId));//
					//user.setUserType(GameConstants.USER_TYPE_STUDENT);//类型：学生
				}else if(platformModule==BusinessConstant.PLATFORM_MODULE_2){//竞技场模块
					//全站人员
				}else if(platformModule==BusinessConstant.PLATFORM_MODULE_3){//俱乐部模块 俱乐部id
					user.setProfession(1);//已加入俱乐部的人
					user.setClubId(Integer.valueOf(clubId));
					if(userIds.equals("")){
						userIds = userId;
					}else{
						userIds = userIds +","+userId;
					}
				}
				user.setUserIds(userIds.equals("")?null:userIds);//不在这个userId中的数据
				user.setStart(start);
				user.setLimit(limit);
				jo = this.inquireTypeToTwo(user,null,platformModule);
			}else{//俱乐部培训班
				
			}
		}else if(inquireType==BusinessConstant.INQUIRETYPE_17){//竞技场模块，创建邀请赛时，筛选学校负责人时调用；
			CenterUser user = new CenterUser();
			user.setIsSchlooNull("1");//数据库判断使用   查询学校id不为空的数据
			user.setNickName(selectName);
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToSeventeen(user);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_18){//通过俱乐部id获取该俱乐部下可以参加比赛的人
			//TODO 调用春磊接口  比赛id 返回正在参加比赛的用户list<Integer>
			String resUserIds = null;
			String userIds = null;
			try {
				IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				resUserIds = game.getUsersByMatchId(Integer.valueOf(gameId));
				if(!StringUtil.isEmpty(resUserIds)){
					JSONObject json = JSONObject.parseObject(resUserIds);
					json = json.getJSONObject("result");
					userIds = json.getString("userIds");
				}
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "getUsers", "inquireType="+inquireType+"远程调用比赛接口失败", e);
				result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				result.put(GameConstants.MSG, BusinessConstant.ERROR_HPROSE);
				return result.toJSONString();
			}
			CenterUser user = new CenterUser();
			user.setProfession(1);//已加入俱乐部的人
			user.setClubId(Integer.valueOf(clubId));
			user.setNickName(selectName);
			user.setUserIds(userIds.equals("")?null:userIds);
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwo(user,null,3);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_19){//通过学校id获取该学校下可以参加比赛的人员
			// TODO 调用春磊接口  比赛id 返回正在参加比赛的用户list<Integer>
			String resUserIds = null;
			String userIds = null;
			try {
				IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
				resUserIds = game.getUsersByMatchId(Integer.valueOf(gameId));
				if(!StringUtil.isEmpty(resUserIds)){
					JSONObject json = JSONObject.parseObject(resUserIds);
					json = json.getJSONObject("result");
					userIds = json.getString("userIds");
				}
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "getUsers", "inquireType="+inquireType+"远程调用比赛接口失败", e);
				result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				result.put(GameConstants.MSG, BusinessConstant.ERROR_HPROSE);
				return result.toJSONString();
			}
			CenterUser user = new CenterUser();
			user.setSchoolId(Integer.valueOf(schoolId));
			user.setNickName(selectName);
			user.setUserIds(userIds.equals("")?null:userIds);
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwo(user,null,null);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_20){//俱乐部首页右侧栏目，获取最活跃的人
			//人的信息 图像信息  俱乐部信息  是否互相关注
			CenterUser user = new CenterUser();
			user.setUserId(Integer.valueOf(userId));
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwenty(user);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_21){//获取当前俱乐部的人员(不查询出自己)
			ClubMember c = new ClubMember();
			c.setClubId(Integer.valueOf(clubId));
			c.setSearchWord(selectName);
			c.setStart(start);
			c.setLimit(limit);
			c.setMemberStatus(1);
			c.setUserId(Integer.valueOf(userId));
			jo = this.inquireTypeToTwentyOne(c,userId);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_22){//使用场景：我的俱乐部主页，获取精英教练
			ClubMember c = new ClubMember();
			c.setLevel(GameConstants.COACH);//查询类型为教练
			c.setSearchWord(selectName);
			c.setClubId(Integer.valueOf(clubId));
			c.setStart(start);
			c.setLimit(limit);
			c.setMemberStatus(1);
			jo = this.inquireTypeToTwentyOne(c,null);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_23){//1.我的俱乐部会员列表页2.俱乐部后台的会员管理列表;
			ClubMember c = new ClubMember();
			c.setClubId(Integer.valueOf(clubId));
			c.setSearchWord(selectName);
			if(clubApplyStatus==BusinessConstant.CLUB_APPLY_STATUS_1){//待审核
				c.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_SH);//审核中
			}else if(clubApplyStatus==BusinessConstant.CLUB_APPLY_STATUS_2){//已加入
				c.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
			}
			c.setStart(start);
			c.setLimit(limit);
			jo = this.inquireTypeToTwentyThree(c,userId);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_24){//进入具体的某个培训班，人员管理列表页调用
			ClubJoinTraining c = new ClubJoinTraining();
			c.setClassId(Integer.valueOf(classId));
			c.setNickName(selectName);
			c.setStart(start);
			c.setLimit(limit);
			jo = this.inquireTypeToTwentyFour(c);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_25){//个人中心模块，获取个人信息(包括教学、俱乐部相关的数据)
			jo = this.inquireTypeToTwentyFive(memberId,userId,userToken);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_26){//获取个人关注人员列表
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setStart(start);
			centerAttention.setLimit(limit);
			centerAttention.setRelObjetType(GameConstants.USER_OBJ);//类型是人1
			centerAttention.setCreateUserId(Integer.valueOf(memberId));
			jo = this.inquireTypeToTwentySix(centerAttention,inquireType,userId);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_27){//获取个人粉丝列表
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setStart(start);
			centerAttention.setLimit(limit);
			centerAttention.setRelObjetType(GameConstants.USER_OBJ);//类型是人
			centerAttention.setRelObjetId(memberId);
			jo = this.inquireTypeToTwentySix(centerAttention,inquireType,userId);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_28){//教学模块，获取任务下的签到人员信息
			jo = this.inquireTypeToTwentyEight(taskId,classId,start,limit);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_29){//全站搜索人员
			CenterUser user = new CenterUser();
			user.setNickName(selectName);
			user.setStart(start);
			user.setLimit(limit);
			jo = this.inquireTypeToTwentyNine(user,userId);
		}else if(inquireType==BusinessConstant.INQUIRETYPE_30){
			CenterUser user = new CenterUser();
			user.setLimit(limit);
			user.setStart(start);
			if(!StringUtil.isEmpty(memberId)){
				user.setUserId(Integer.valueOf(userId));
			}
			user.setGuessId(Integer.valueOf(quizId));
			jo = this.inquireTypeToThrity(user);
		}else{
			//类型查询错误
			result.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			result.put(GameConstants.MSG, AppErrorCode.ERROR_TYPE_CODE);
			return result.toJSONString();
		}
		result.put("result", jo);
		result.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		return result.toJSONString();
	}
	public JSONObject inquireTypeToThrity(CenterUser centerUser){
		List<Map<String,Object>> maps = centerUserService.queryCenterUserMapByQuizId(centerUser);
		for (Map<String,Object> map : maps) {
			//拆分
			Integer userId =(int)map.get("user_id");
			map.put("userId", userId);
			String nickName = (String)map.get("nick_name");
			map.put("nickname", nickName);
			Integer linkId= new Integer(map.get("head_img_id")==null?0:(int)map.get("head_img_id"));
			String pic_ = RedisComponent.findRedisObjectPublicPicture(linkId, BusinessConstant.DEFAULT_IMG_CLUB);
			PublicPicture pic = JSONObject.parseObject(pic_, PublicPicture.class);
			/*
			 * 压缩图片
			 */
			map.put("headLink", Common.checkPic(pic.getFilePath()) == true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			long bettingRank =(long)map.get("rownumber") ;
			map.put("bettingRank", bettingRank);
			Integer lastBettingDate = (Integer)map.get("bet_time");
			map.put("lastBettingDate", lastBettingDate);
			BigDecimal amount = (BigDecimal)map.get("amount");
			
			Integer betPosition = (int) map.get("bet_position");
			if(betPosition.intValue()==1){
				map.put("bettingCanCount", String.valueOf(amount.intValue())+"花");
				map.put("bettingCannotCount", 0+"花");
			}else{
				map.put("bettingCannotCount", String.valueOf(amount.intValue())+"花");
				map.put("bettingCanCount", 0+"花");
			}
			//BigDecimal bettingCanCount = (BigDecimal)(map.get("sure_amount")==null?0:map.get("sure_amount"));
			//BigDecimal bettingCannotCount = (BigDecimal)(map.get("negative_amount")==null?0:map.get("negative_amount"));
			//map.put("bettingCannotCount", bettingCannotCount.intValue()+"花");
			BigDecimal bettingGain = (BigDecimal)(map.get("gain")==null?0:map.get("gain"));
			map.put("bettingGain", bettingGain.intValue()+"花");
		}
		Integer count = centerUserService.queryCenterUserMapByQuizIdCount(centerUser);
		JSONObject obj =PageObject.getPageObject(count, centerUser.getStart(), centerUser.getLimit(), maps, "users");
		return obj;
	}
	
	private JSONObject inquireTypeToEleven(CenterAccount account) {
		List<User> userList = new ArrayList<User>();
		List<CenterAccount> accountList = centerAccountService.getMaxBalanceList(account);
		Integer count = centerAccountService.getMaxBalanceCount(account);
		if(!CollectionUtils.isEmpty(accountList)){
			for (int i = 0; i < accountList.size(); i++) {
				CenterAccount centeraccount = accountList.get(i);
				User user = new User();
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(centeraccount.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				}
				//图片信息
				String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(centeruser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
					/*
					 * 压缩图片
					 */
					user.setHeadLink(Common.checkPic(linkImg)== true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				user.setUserId(String.valueOf(centeruser.getUserId()));//用户信息
				user.setRealName(centeruser.getRealName());
				user.setNickname(centeruser.getNickName());
				user.setDesc(centeruser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, account.getStart(), account.getLimit(), userList, "users");
	}

	private JSONObject inquireTypeToTen(ArenaGuessBet arenaguessbet) {
		List<User> userList = new ArrayList<User>();
		List<ArenaGuessBet> betList = arenaGuessBetService.getWinNumberList(arenaguessbet);
		Integer count = arenaGuessBetService.getWinNumberCount();
		if(!CollectionUtils.isEmpty(betList)){
			for (int i = 0; i < betList.size(); i++) {
				ArenaGuessBet bet = betList.get(i);
				User user = new User();
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(bet.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				}
				//图片信息
				String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(centeruser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
					/*
					 * 压缩图片
					 */
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				user.setUserId(String.valueOf(centeruser.getUserId()));//用户信息
				user.setRealName(centeruser.getRealName());
				user.setNickname(centeruser.getNickName());
				user.setDesc(centeruser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, arenaguessbet.getStart(), arenaguessbet.getLimit(), userList, "users");
	}

	private JSONObject inquireTypeToNine(ArenaGuessBet arenaguessbet) {
		List<User> userList = new ArrayList<User>();
		List<ArenaGuessBet> betList = arenaGuessBetService.getMaxBetList(arenaguessbet);
		Integer count = arenaGuessBetService.getMaxBetCount();
		if(!CollectionUtils.isEmpty(betList)){
			for (int i = 0; i < betList.size(); i++) {
				ArenaGuessBet bet = betList.get(i);
				User user = new User();
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(bet.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				}
				//图片信息
				String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(centeruser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
					//user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
					/*
					 * 压缩图片
					 */
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				user.setUserId(String.valueOf(centeruser.getStudentId()));//用户信息
				user.setRealName(centeruser.getRealName());
				user.setNickname(centeruser.getNickName());
				user.setDesc(centeruser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, arenaguessbet.getStart(), arenaguessbet.getLimit(), userList, "users");
	}

	//inquireType==20 查询俱乐部活跃的人
	private JSONObject inquireTypeToTwenty(CenterUser centeruser) {
		List<User> userList = new ArrayList<User>();
		List<CenterUser> listUser = centerUserService.getClubActiveUser(centeruser);
		Integer count = centerUserService.queryClubActiveUserCount(centeruser);
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				//图片信息
				String linkImg = Common.getImgUrl(resUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(resUser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(resUser.getHeadImgId()));
					/*
					 * 压缩图片
					 */
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
					//user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
				}
				//俱乐部信息
				String  cachenClubmain = RedisComponent.findRedisObject(Integer.valueOf(resUser.getClubId()), ClubMain.class);
				if(!StringUtil.isEmpty(cachenClubmain)){
					ClubMain clubmain = JSONObject.parseObject(cachenClubmain,ClubMain.class);
					//俱乐部id
					user.setClubId(clubmain==null?"":String.valueOf(clubmain.getClubId()));
					//俱乐部名称
					user.setClubName(clubmain==null?"":clubmain.getClubName());
					//俱乐部logo地址
					Integer imgId = clubmain.getLogoId()==null?BusinessConstant.DEFAULT_IMG_CLUB:clubmain.getLogoId();
					String cacheClubLogoLink = RedisComponent.findRedisObject(imgId, PublicPicture.class);
					if(!StringUtil.isEmpty(cacheClubLogoLink)){
						PublicPicture publicpicture = JSONObject.parseObject(cacheClubLogoLink,PublicPicture.class);
						/*
						 * 压缩图片
						 */
						user.setClubLogoLink(Common.checkPic(publicpicture.getFilePath()) ?publicpicture.getFilePath()+ActiveUrl.HEAD_MAP:publicpicture.getFilePath());
					}
				}
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(resUser.getClubId());
				clubMember.setUserId(resUser.getUserId());
				ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
				user.setClubRole(resClubMember==null?0:resClubMember.getLevel());
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setNickname(resUser.getNickName());
				Map<String, Integer> map = baseCenterAttentionService.getIsConcernCount(centeruser.getUserId(),resUser.getUserId());
				user.setHasBeenAttention(map.get("isConcern"));// 我是否关注他
				user.setAttentionEachOther(map.get("concern"));//是否互相关注
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	//inquireType==3 全站搜索
	private JSONObject inquireTypeToThree(CenterUser centeruser,String userId,int inquireType) {
		List<User> userList = new ArrayList<User>();
		List<CenterUser> listUser = null;
		Integer count = 0;
		if(inquireType ==5){
			count=10;
			//默认显示10条记录
			listUser =centerUserService.queryActiveAndRecommendUser(centeruser);
		}else{
			listUser = centerUserService.getActiveAndRecommendUser(centeruser);
			if(centeruser.getIsSchlooNull()!=null && centeruser.getIsSchlooNull().equals("true")){
				count = listUser.size();
			}else{
				count = centerUserService.queryUserCount(centeruser);
			}
		}
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				if(userId!=null){
					Map<String,Integer> map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId), Integer.valueOf(resUser.getUserId()));
					user.setHasBeenAttention(map.get("isConcern"));//是否关注他
				}
				//图片信息
				String linkImg = Common.getImgUrl(resUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(resUser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(resUser.getHeadImgId()));
					/*
					 * 压缩图片
					 */
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setNickname(resUser.getNickName());
				user.setDesc(resUser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	//inquireType==29 全站搜索
	private JSONObject inquireTypeToTwentyNine(CenterUser centeruser,String userId) {
		List<User> userList = new ArrayList<User>();
		//查询用户
		List<CenterUser> listUser = centerUserService.selectCenterUserList(centeruser);
		//查询数量
		Integer count = centerUserService.queryUserCount(centeruser);
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				//图片信息
				String linkImg = Common.getImgUrl(resUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(resUser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(resUser.getHeadImgId()));
					/*
					 * compress picture
					 */
					
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				Map<String, Integer> map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId),resUser.getUserId());
				user.setHasBeenAttention(map.get("isConcern"));// 我是否关注他
				user.setAttentionEachOther(map.get("concern"));//是否互相关注
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setNickname(resUser.getNickName());
				user.setDesc(resUser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	//inquireType==28 教学模块，获取任务下的签到人员信息
	private JSONObject inquireTypeToTwentyEight(String taskId,String classId,Integer start,Integer limit) {
		List<User> userList = new ArrayList<User>();
		TeachPlanSign teachPlanSign = new TeachPlanSign();
		teachPlanSign.setPlanId(Integer.valueOf(taskId));
		teachPlanSign.setStart(start);
		teachPlanSign.setLimit(limit);
		teachPlanSign.setClassId(Integer.valueOf(classId));
		List<TeachPlanSign>  list = teachPlanSignService.getTeachPlanSignList(teachPlanSign);
		Integer count = teachPlanSignService.getTeachPlanSignCount(teachPlanSign);
		if(!CollectionUtils.isEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				User user = new User();
				TeachPlanSign t = list.get(i);
				Integer userId = t.getStudentId();
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(userId, CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					//证书信息
					TeachRelStudentClass t1 = new TeachRelStudentClass();
					t1.setStudentId(centeruser.getUserId());
					t1.setIsDelete(0);
					TeachRelStudentClass resTeachRelStudentClass = teachRelStudentClassService.selectTeachRelStudentClass(t1);
					if(null!=resTeachRelStudentClass){
						//1:未申请；2:申请中；3:申请成功；4:申请失败；证书状态
						//证书状态。0:未申请，1:申请中，2:已发证。(数据库状态)
						Integer certificate = resTeachRelStudentClass.getCertificateStatus();
						if(certificate!=null){
							if(certificate>=0 && certificate<=BusinessConstant.CERTIFICATE_STATUS.length){
								user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[certificate]);
							}else{
								user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
							}
						}else{
							user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
						}
						if(resTeachRelStudentClass.getAssessScore()==null){
							user.setScore(0f);
						}else{
							user.setScore((float)resTeachRelStudentClass.getAssessScore());
						}
					}
				}
				//图片信息
				String cachePublicPicture = RedisComponent.findRedisObject(centeruser.getHeadImgId(), PublicPicture.class);
				if(!StringUtil.isEmpty(cachePublicPicture)){//个人头像信息
					PublicPicture publicpicture = JSONObject.parseObject(cachePublicPicture,PublicPicture.class);
					user.setHeadLinkId(String.valueOf(publicpicture.getPicId()));
					/*
					 * compress picture
					 */
					
					user.setHeadLink(Common.checkPic(publicpicture.getFilePath()) == true ? publicpicture.getFilePath()+ActiveUrl.HEAD_MAP:publicpicture.getFilePath());
				}
				user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));
				user.setRealName(centeruser==null?"":centeruser.getRealName());
				user.setNickname(centeruser==null?"":centeruser.getNickName());
				user.setSigninDate(String.valueOf(t.getSignTime()));//签到时间
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, start, limit, userList, "users");
	}

	//inquireType==26 个人中心模块，获取个人关注人员列表  个人粉丝列表
	private JSONObject inquireTypeToTwentySix(CenterAttention centerAttention,Integer inquireType,String userId) {
		List<User> userList = new ArrayList<User>();
		List<CenterAttention> list = baseCenterAttentionService.getCenterAttentions(centerAttention);
		Integer count = baseCenterAttentionService.getCenterAttentionCount(centerAttention);
		if(!CollectionUtils.isEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				User user = new User();
				CenterAttention c = list.get(i);
				Map<String, Integer> map = null;
				String cacheUser = null;
				if(inquireType==26){//我是否关注他(我关注的人)
					map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId),Integer.valueOf(c.getRelObjetId()));
					//人员信息
					cacheUser = RedisComponent.findRedisObject(Integer.valueOf(c.getRelObjetId()), CenterUser.class);
				}else if(inquireType==27){//我和他是否互相关注(我的粉丝)
					map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId),c.getCreateUserId());
					//人员信息
					cacheUser = RedisComponent.findRedisObject(c.getCreateUserId(), CenterUser.class);
				}
				user.setHasBeenAttention(map.get("isConcern"));// 我是否关注他
				user.setAttentionEachOther(map.get("concern"));//是否互相关注
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					//图片信息
					String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(centeruser.getHeadImgId()!=null){
						user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
						/*
						 * compress picture
						 */
						
						user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
						//user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
					}
					user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));
					user.setRealName(centeruser==null?"":centeruser.getRealName());
					user.setNickname(centeruser==null?"":centeruser.getNickName());
					userList.add(user);
				}
			}
		}
		return PageObject.getPageObject(count, centerAttention.getStart(), centerAttention.getLimit(), userList, "users");
	}

	//inquireType==25 个人中心模块，获取个人信息(包括教学、俱乐部相关的数据)
	private JSONObject inquireTypeToTwentyFive(String memberId,String userId,String userToken) {
		JSONObject jo = new JSONObject();
		List<User> userList = new ArrayList<User>();
		User user = new User();
		CenterUser centeruser = null;
		String cacheUser = null;
		//如果memberId为空 则查询的是自己的
		if(memberId == null){
			memberId = userId;
		}
		Map<String, Integer> map2 = baseCenterAttentionService.getConcernCount(Integer.valueOf(memberId));
		//粉丝数量
		user.setFansNum(map2.get("fans"));
		//关注数量
		user.setAttentionNum(map2.get("concern"));
		if(memberId.equals(userId)){//同一个人
			//关注信息
			user.setHasBeenAttention(0);
			user.setAttentionEachOther(0);
			//用户信息
			cacheUser = RedisComponent.findRedisObject(Integer.valueOf(userId), CenterUser.class);
		}else{//不为空则是查询他人的
			//用户信息
			Map<String,Integer> map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId), Integer.valueOf(memberId));
			user.setHasBeenAttention(map.get("isConcern"));//是否关注他
			user.setAttentionEachOther(map.get("concern"));//是否互相关注
			cacheUser = RedisComponent.findRedisObject(Integer.valueOf(memberId), CenterUser.class);
		}
		if(!StringUtil.isEmpty(cacheUser)){
			centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
			user.setSex(centeruser.getSex() ==null ?-1: centeruser.getSex()==2?1:centeruser.getSex() == 1?0:-1);			user.setClassId(centeruser.getClassId()==null?"":String.valueOf(centeruser.getClassId()));
			//关注企业数量
			user.setAttentionCompanyCount(centeruser.getAtteCompanyNum()==null?0:centeruser.getAtteCompanyNum());
			//关注俱乐部数量
			user.setAttentionClubCount(centeruser.getAtteClubNum()==null?0:centeruser.getAtteClubNum());
			//点赞数量
			user.setSupportCount(centeruser.getPraiseNum()==null?0:centeruser.getPraiseNum());
			//是否已加入俱乐部
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(centeruser.getClubId());
			clubMember.setUserId(centeruser.getUserId());
			clubMember.setMemberStatus(1);//已加入
			ClubMember resclubMember = clubMemberService.selectClubMember(clubMember);
			//是否已加入俱乐部
			if(resclubMember!=null){
				user.setHasJoinClub(1);
			}else{
				user.setHasJoinClub(0);
			}
			//俱乐部状态	
			user.setClubStatus(0);
			if(centeruser.getClubId()!=null){
				String  cachenClubmain = RedisComponent.findRedisObject(centeruser.getClubId(), ClubMain.class);
				if(!StringUtil.isEmpty(cachenClubmain)){
					ClubMain clubmain = JSONObject.parseObject(cachenClubmain,ClubMain.class);
					//俱乐部id
					user.setClubId(clubmain==null?"":String.valueOf(clubmain.getClubId()));
					//俱乐部名称
					user.setClubName(clubmain==null?"":clubmain.getClubName());
					//俱乐部logo地址
					String clublogoImg = Common.getImgUrl(clubmain.getLogoId(), BusinessConstant.DEFAULT_IMG_CLUB);
					/*
					 * compress picture
					 */
					user.setClubLogoLink(Common.checkPic(clublogoImg) == true ? clublogoImg+ActiveUrl.HEAD_MAP:clublogoImg);
					//俱乐部人数
					user.setClubMember(clubmain.getMemberNum()==null?0:clubmain.getMemberNum());
					//俱乐部状态	
					user.setClubStatus(clubmain==null?0:clubmain.getStatus());
				}
			}
			user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));
			user.setRealName(centeruser==null?"":centeruser.getRealName());
			user.setNickname(centeruser==null?"":centeruser.getNickName());
			user.setDesc(centeruser==null?"":centeruser.getDescription());
			//头像信息
			String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			if(centeruser.getHeadImgId()!=null){
				user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
				/*
				 * compress picture
				 */
				
				user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
//				user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
			}
			//证书信息
			TeachRelStudentClass t = new TeachRelStudentClass();
			t.setStudentId(centeruser.getUserId());
			t.setIsDelete(0);
			TeachRelStudentClass resTeachRelStudentClass = teachRelStudentClassService.selectTeachRelStudentClass(t);
			if(null!=resTeachRelStudentClass){
				//1:未申请；2:申请中；3:申请成功；4:申请失败；证书状态
				//证书状态。0:未申请，1:申请中，2:已发证。
				Integer certificate = resTeachRelStudentClass.getCertificateStatus();
				if(certificate!=null){
					if(certificate>=0 && certificate<=BusinessConstant.CERTIFICATE_STATUS.length){
						user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[certificate]);
					}else{
						user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
					}
				}else{
					user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
				}
				if(resTeachRelStudentClass.getAssessScore()==null){
					user.setScore(0f);
				}else{
					user.setScore((float)resTeachRelStudentClass.getAssessScore());
				}
				user.setTeachingAchievementId("");//该参数暂时不用
			}
			//课程卡信息
			if(centeruser.getClassId()!=null){
				TeachCourseCardCount teachcoursecardcount = new TeachCourseCardCount();
				teachcoursecardcount.setClassId(centeruser.getClassId());
				TeachCourseCardCount resTeachCourseCardCount = teachCourseCardCountService.getTeachCourseCardCountOne(teachcoursecardcount);
				if(resTeachCourseCardCount!=null){
					Integer completeNum = resTeachCourseCardCount.getCompleteNum()==null?0:resTeachCourseCardCount.getCompleteNum();
					user.setCompletedCourseCards(String.valueOf(completeNum));
					Integer NotEndNum = resTeachCourseCardCount.getNotEndNum()==null?0:resTeachCourseCardCount.getNotEndNum();
					Integer NotSetNum = resTeachCourseCardCount.getNotSetNum()==null?0:resTeachCourseCardCount.getNotSetNum();
					Integer NotStatrNum = resTeachCourseCardCount.getNotStartNum()==null?0:resTeachCourseCardCount.getNotStartNum();
					int total = completeNum+NotEndNum+NotSetNum+NotStatrNum;
					user.setAllCourseCards(total);
//					user.setLearningSeconds(resTeachCourseCardCount.getTotalCompleteHours()==null?0:resTeachCourseCardCount.getTotalCompleteHours());
					user.setCompletedChapters(resTeachCourseCardCount.getTotalCompleteChapter()==null?0:resTeachCourseCardCount.getTotalCompleteChapter());
					setCourseCount(user, centeruser.getClassId(), Integer.valueOf(memberId));
				}
			}
			
			//是否是登录
			if(memberId.equals(userId)){
				user.setIsLoginUser(1);//是本人登录
			}else{
				user.setIsLoginUser(0);//不是登陆者
			}
			//查询人员类型
			user.setTeachingRole(centeruser.getUserType());
		}
		String gameResult = null;
		String practiceResult = null;
		//俱乐部比赛+竞技场比赛
		String joinGameCount = null;
		//实训作业比赛
		String practiceCount = null;
		try {
			IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
			//对比赛状态没有限制
			gameResult = game.getWebGameCountByUserId(Integer.valueOf(memberId),"3,4",null);
			//查询比赛状态为结束的
			practiceResult = game.getWebGameCountByUserId(Integer.valueOf(memberId),"2","5");
			if(!StringUtil.isEmpty(gameResult)){
				JSONObject json = JSONObject.parseObject(gameResult);
				json = json.getJSONObject("result");
				joinGameCount = json.getString("baseMatchInfoCount");
			}
			if(!StringUtil.isEmpty(practiceResult)){
				JSONObject json = JSONObject.parseObject(practiceResult);
				json = json.getJSONObject("result");
				practiceCount = json.getString("baseMatchInfoCount");
			}
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "getUsers", "inquireType="+25+"远程调用比赛接口失败", e);
			/*jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
			jo.put(GameConstants.MSG, BusinessConstant.ERROR_HPROSE);
			return jo;*/
		}
		
		//参加实训次数
		user.setPracticeCount(practiceCount==null?0:Integer.valueOf(practiceCount));
		//参加的比赛数量(是俱乐部内部的比赛和外部比赛的数量和) 调用接口
		user.setJoinGameCount(joinGameCount==null?0:Integer.valueOf(joinGameCount));
		
		//加入的培训班数量
		Integer clubClassCount = clubJoinTrainingService.getHasClubJoinTrainingCount(Integer.valueOf(memberId));
		user.setJoinTrainingClassCount(clubClassCount==null?0:clubClassCount);
		
		//参加的竞猜数量
		Integer arenaGuessBetCount = arenaGuessBetService.getArenaGuessBetCount(Integer.valueOf(memberId));
		user.setJoinQuizCount(arenaGuessBetCount==null?0:arenaGuessBetCount);
		
		//关注的赛事数量
		Integer attentionCount = centerAttentionService.attentionCount(Integer.valueOf(memberId));
		user.setAttentionGameEvenCount(attentionCount==null?0:attentionCount);
		userList.add(user);
		jo.put("users", userList);
		jo.put("returnCount", userList.size());
		return jo;
	}

	//inquireType==24 进入具体的某个培训班，人员管理列表页调用
	private JSONObject inquireTypeToTwentyFour(ClubJoinTraining c) {
		List<User> userList = new ArrayList<User>();
		List<ClubJoinTraining> list = clubJoinTrainingService.getClubJoinTrainingByClassId(c);
		Integer count = clubJoinTrainingService.getClubJoinTrainingCount(c);
		if(!CollectionUtils.isEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				User user = new User();
				ClubJoinTraining clubjointraining = list.get(i);
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(clubjointraining.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				TeachSchool teachschool = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					//学校信息
					String cacheTeachSchool = RedisComponent.findRedisObject(centeruser.getSchoolId(), TeachSchool.class);
					if(!StringUtil.isEmpty(cacheTeachSchool)){
						teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
					}
				}
				//俱乐部信息
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(centeruser==null?0:centeruser.getClubId());
				ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
				
				user.setSchoolId(teachschool==null?"":String.valueOf(teachschool.getSchoolId()));
				user.setSchoolName(teachschool==null?"":teachschool.getSchoolName());
				user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));//用户信息
				user.setRealName(centeruser==null?"":centeruser.getRealName());
				user.setNickname(centeruser==null?"":centeruser.getNickName());
				user.setClubRole(resClubMember==null?0:resClubMember.getLevel());//人在俱乐部的类型
				user.setJoinClassDate(String.valueOf(clubjointraining.getJoinTime()));//加入时间
				user.setfLevelAccountForJoinClass(new Double(clubjointraining.getTotalCost()).intValue());//消费总额
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, c.getStart(), c.getLimit(), userList, "users");
	}

	private JSONObject inquireTypeToTwentyThree(ClubMember c,String userId) {
		List<User> userList = new ArrayList<User>();
		List<ClubMember> res = clubMemberService.getClubMemberByStatus(c);
		Integer count = clubMemberService.queryCountByStatus(c);
		//当待审核数据为空时修改红点消失
		if(c.getMemberStatus() == GameConstants.CLUB_MEMBER_STATE_SH){
			if(CollectionUtils.isEmpty(res)){
				ClubMain clubmain = new ClubMain();
				clubmain.setClubId(c.getClubId());
				clubmain.setIsNotAudited(0);//当点击之后红点就消失
				clubMainService.updateClubMain(clubmain);
				JedisCache.delRedisVal(ClubMain.class.getSimpleName(),String.valueOf(c.getClubId()));
			}
		}
		if(!CollectionUtils.isEmpty(res)){
			for (int i = 0; i < res.size(); i++) {
				User user = new User();
				ClubMember resClubMember = res.get(i);
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(resClubMember.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				}
				//图像信息
				String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(centeruser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
//					user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
					/*
					 * compress picture
					 */
					
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				//学校信息
				TeachSchool teachschool = null;
				String cacheTeachSchool = RedisComponent.findRedisObject(centeruser.getSchoolId(), TeachSchool.class);
				if(!StringUtil.isEmpty(cacheTeachSchool)){
					teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
				}
				user.setSchoolId(teachschool==null?"":String.valueOf(teachschool.getSchoolId()));
				user.setSchoolName(teachschool==null?"":teachschool.getSchoolName());
				user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));//用户信息
				user.setRealName(centeruser==null?"":centeruser.getRealName());
				user.setNickname(centeruser==null?"":centeruser.getNickName());
				user.setSex(centeruser.getSex() ==null ?-1: centeruser.getSex()==2?1:centeruser.getSex() == 1?0:-1);
				user.setDesc(centeruser==null?"":centeruser.getDescription());
				//俱乐部信息
				user.setClubApplyStatus(resClubMember==null?0:resClubMember.getMemberStatus());
				user.setApplicationContent(resClubMember==null?"":resClubMember.getApplyExplain());
				//关注数量
				Map<String,Integer> map = baseCenterAttentionService.getConcernCount(centeruser.getUserId());
				user.setAttentionNum(map==null?0:map.get("concern"));//关注数
				user.setFansNum(map==null?0:map.get("fans"));//粉丝数
				Map<String, Integer> maps = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId),resClubMember.getUserId());
				user.setHasBeenAttention(maps.get("isConcern"));// 我是否关注他
				//俱乐部信息
				ClubMember clubMember = new ClubMember();
				clubMember.setClubId(centeruser.getClubId());
				clubMember.setUserId(centeruser.getUserId());
				ClubMember role = clubMemberService.queryClubMemberByClubIdAndUserId(centeruser.getUserId(),centeruser.getClubId());
				user.setClubRole(role==null?0:role.getLevel());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, c.getStart(), c.getLimit(), userList, "users");
	}

	//inquireType==21 俱乐部人员信息
	private JSONObject inquireTypeToTwentyOne(ClubMember c,String userId) {
		List<User> userList = new ArrayList<User>();
		List<ClubMember> res = clubMemberService.getClubMemberByStatus(c);
		Integer count = clubMemberService.queryCountByStatus(c);
		if(!CollectionUtils.isEmpty(res)){
			for (int i = 0; i < res.size(); i++) {
				User user = new User();
				ClubMember resClubMember = res.get(i);
				//俱乐部信息
				ClubMain clubmain = null;
				String cacheClubMain = RedisComponent.findRedisObject(resClubMember.getClubId(), ClubMember.class);
				if(!StringUtil.isEmpty(cacheClubMain)){
					clubmain = JSONObject.parseObject(cacheClubMain,ClubMain.class);
				}
				//人员信息
				String cacheUser = RedisComponent.findRedisObject(resClubMember.getUserId(), CenterUser.class);
				CenterUser centeruser = null;
				if(!StringUtil.isEmpty(cacheUser)){
					centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(centeruser.getHeadImgId()!=null){
						user.setHeadLinkId(String.valueOf(centeruser.getHeadImgId()));
//						user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
						/*
						 * compress picture
						 */
						
						user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
					}	
				}
				if(clubmain!=null && clubmain.getStatus()!=null){
					user.setClubStatus(clubmain==null?0:clubmain.getStatus());
				}else{
					user.setClubStatus(-1);
				}
				user.setUserId(centeruser==null?"":String.valueOf(centeruser.getUserId()));//用户信息
				user.setRealName(centeruser==null?"":centeruser.getRealName());
				user.setNickname(centeruser==null?"":centeruser.getNickName());
				user.setClubApplyStatus(resClubMember==null?0:resClubMember.getMemberStatus());
				user.setClubId(clubmain==null?"":String.valueOf(clubmain.getClubId()));
				user.setClubName(clubmain==null?"":clubmain.getClubName());
				user.setClubRole(resClubMember.getLevel());
				if(null!=userId){
					Map<String, Integer> map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId),resClubMember.getUserId());
					user.setHasBeenAttention(map.get("isConcern"));// 我是否关注他
					user.setAttentionEachOther(map.get("concern"));//是否互相关注
				}
				userList.add(user);
				
			}
		}
		return PageObject.getPageObject(count, c.getStart(), c.getLimit(), userList, "users");
	}

	//inquireType==17 竞技场模块，创建邀请赛时，筛选学校负责人时调用；
	private JSONObject inquireTypeToSeventeen(CenterUser centeruser) {
		List<User> userList = new ArrayList<User>();
		//查询用户
		List<CenterUser> listUser = centerUserService.selectCenterUserList(centeruser);
		//查询数量
		Integer count = centerUserService.queryUserCount(centeruser);
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				//学校信息
				String cacheTeachSchool = RedisComponent.findRedisObject(resUser.getSchoolId(), TeachSchool.class);
				if(!StringUtil.isEmpty(cacheTeachSchool)){
					TeachSchool teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
					user.setSchoolId(String.valueOf(teachschool.getSchoolId()));
					user.setSchoolName(teachschool.getSchoolName());
				}
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setNickname(resUser.getNickName());
				user.setHasBeenAttention(0);//查询时传递的memberId是否已关注此人，如果没有传memberId则使用userId (字段暂时没用到)
				user.setAttentionEachOther(0);//是否相互关注  (字段暂时没用到)
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	//inquireType==8 点击学校的班级进入该班级下的学生排名列表页时调用
	private JSONObject inquireTypeToEight(CenterUser centeruser) {
		List<User> userList = new ArrayList<User>();
		List<CenterUser> listUser = centerUserService.selectCenterUserSource(centeruser);
		Integer count = centerUserService.queryUserSourceCount(centeruser);
		//查询用户
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				//1:未申请；2:申请中；3:申请成功；4:申请失败；证书状态
				//证书状态。0:未申请，1:申请中，2:已发证。
				Integer certificate = resUser.getIsApplyCertificate();
				if(certificate!=null){
					if(certificate>=0 && certificate<=BusinessConstant.CERTIFICATE_STATUS.length){
						user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[certificate]);
					}else{
						user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
					}
				}else{
					user.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
				}
				//班级信息
				String cacheTeachClass = RedisComponent.findRedisObject(resUser.getClassId(), TeachClass.class);
				if(!StringUtil.isEmpty(cacheTeachClass)){//班级信息
					TeachClass teachclass = JSONObject.parseObject(cacheTeachClass,TeachClass.class);
					user.setClassId(String.valueOf(teachclass.getClassId()));
					user.setClassName(teachclass.getClassName());
				}
				//学校信息
				String cacheTeachSchool = RedisComponent.findRedisObject(resUser.getSchoolId(), TeachSchool.class);
				if(!StringUtil.isEmpty(cacheTeachSchool)){
					TeachSchool teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
					user.setSchoolId(String.valueOf(teachschool.getSchoolId()));
					user.setSchoolName(teachschool.getSchoolName());
				}
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setScore((float)resUser.getScore());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	//inquireType==6 教师管理后台的人员管理列表页调用
	private JSONObject inquireTypeToSix(CenterUser user, Integer start, Integer limit) {
		List<User> userList = new ArrayList<User>();
		Integer count = centerUserService.getUserListCount(user);
		List<CenterUser> listUser = centerUserService.getUserList(user);
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User users = new User();
				CenterUser resUser = listUser.get(i);
				//图片信息
				String linkImg = Common.getImgUrl(resUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(resUser.getHeadImgId()!=null){
					users.setHeadLinkId(String.valueOf(resUser.getHeadImgId()));
//					users.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
					/*
					 * compress picture
					 */
					
					users.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}
				//班级信息
				String cacheTeachClass = RedisComponent.findRedisObject(resUser.getClassId(), TeachClass.class);
				if(!StringUtil.isEmpty(cacheTeachClass)){//班级信息
					TeachClass teachclass = JSONObject.parseObject(cacheTeachClass,TeachClass.class);
					users.setClassId(String.valueOf(teachclass.getClassId()));
					users.setClassName(teachclass.getClassName());
				}
				//学校信息
				String cacheTeachSchool = RedisComponent.findRedisObject(resUser.getSchoolId(), TeachSchool.class);
				if(!StringUtil.isEmpty(cacheTeachSchool)){//个人头像信息
					TeachSchool teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
					users.setSchoolId(String.valueOf(teachschool.getSchoolId()));
					users.setSchoolName(teachschool.getSchoolName());
				}
				
				users.setScore(resUser.getScore()==null?0:Float.valueOf(resUser.getScore()));
				users.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				users.setRealName(resUser.getRealName());
				users.setNickname(resUser.getNickName());
				users.setDesc(resUser.getDescription());
				users.setHasQualification(resUser.getIsIdentify());//是否认证
				//1:未申请；2:申请中；3:申请成功；4:申请失败；证书状态
				//证书状态。0:未申请，1:申请中，2:已发证。
				Integer certificate = resUser.getIsApplyCertificate();
				if(certificate!=null){
					if(certificate>=0 && certificate<=BusinessConstant.CERTIFICATE_STATUS.length){
						users.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[certificate]);
					}else{
						users.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
					}
				}else{
					users.setCertRequestStatus(BusinessConstant.CERTIFICATE_STATUS[0]);
				}
				userList.add(users);
			}
		}
		return PageObject.getPageObject(count, start,limit, userList, "users");
	}


	//inquireType==1 根据班级id获取班级人员成绩排名;
	private JSONObject inquireTypeToOne(String classId,Integer start,Integer limit){
		Integer count = teachRelStudentClassService.queryCountByClassId(classId);
		List<User> userList = teachRelStudentClassService.getGradesByClassId(classId,start,limit);
		return PageObject.getPageObject(count, start, limit, userList, "users");
	}
	
	//inquireType==2 根据班级id获取班级人员成绩排名;
	private JSONObject inquireTypeToTwo(CenterUser centeruser,String userId,Integer platformModule){
		List<User> userList = new ArrayList<User>();
		List<CenterUser> listUser = null;
		Integer count = 0;
		if(null!=platformModule && platformModule==BusinessConstant.PLATFORM_MODULE_3){
			//查询用户
			listUser = centerUserService.selectCenterByClubIdList(centeruser);
			//查询数量
			count = centerUserService.queryUserByClubIdCount(centeruser);
		}else{
			//查询用户
			listUser = centerUserService.selectCenterByUserAnyThing(centeruser);
			//查询数量
			count = centerUserService.queryUserCount(centeruser);
		}
		if(!CollectionUtils.isEmpty(listUser)){
			for (int i = 0; i < listUser.size(); i++) {
				User user = new User();
				CenterUser resUser = listUser.get(i);
				if(userId!=null){
					Map<String,Integer> map = baseCenterAttentionService.getIsConcernCount(Integer.valueOf(userId), Integer.valueOf(resUser.getUserId()));
					user.setHasBeenAttention(map.get("isConcern"));//是否关注他
				}
				String linkImg = Common.getImgUrl(resUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
				if(resUser.getHeadImgId()!=null){
					user.setHeadLinkId(String.valueOf(resUser.getHeadImgId()));
//					user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
					/*
					 * compress picture
					 */
					
					user.setHeadLink(Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
				}else{
					user.setHeadLinkId("");
					user.setHeadLink(linkImg+ActiveUrl.HEAD_MAP);
				}
				if(resUser.getSchoolId()!=null){
					user.setSchoolId(String.valueOf(resUser.getSchoolId()));
				}else{
					user.setSchoolId("");
				}
				user.setUserId(String.valueOf(resUser.getUserId()));//用户信息
				user.setRealName(resUser.getRealName());
				user.setNickname(resUser.getNickName());
				user.setDesc(resUser.getDescription());
				userList.add(user);
			}
		}
		return PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), userList, "users");
	}

	/**
	 * 登录接口
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param sourceID 来源id
	 * @param pushToken	推送唯一标示
	 * @param password 密码
	 */
	@Transactional
	public JSONObject login(String userName, String password, String pushToken, String sourceID,Integer clientType) {
		LogUtil.info(this.getClass(), "login", "登录接口开始调用，登录用户为>"+userName+",clientType="+clientType);
		JSONObject jo = new JSONObject();
		Pattern pattern = Pattern.compile("[0-9]*");
		boolean b = pattern.matcher(userName).matches();
		//根据用户名查询用户
		CenterUser centeruser = new CenterUser();
		if(b){
			centeruser.setPhone(userName);
			LogUtil.info(this.getClass(), "login", "使用手机号方式登录");
		}else{
			centeruser.setUserName(userName);
			LogUtil.info(this.getClass(), "login", "使用导入用户账户方式登录");
		}
		CenterUser result = centerUserService.selectCenterUser(centeruser);
		if(null==result){
			LogUtil.error(this.getClass(), "login", "用户不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
		}else{
			String rodmSalt = result.getSalt();
			Integer userId = result.getUserId();
			String dbPassword = result.getPassword();
			//登录令牌=userid+时间戳  md5
			String token = "";
			//加密后的密码
			String encryption = "";
			try {
				token =  MD5Utils.encryptToMD5(userId+String.valueOf(TimeUtil.getCurrentTimestamp()));
				String oneEncryption = MD5Utils.encryptToMD5(password);
				encryption = MD5Utils.encryptToMD5(oneEncryption+rodmSalt);
				LogUtil.info(this.getClass(), "login", "登录前密码"+password+"DB密码"+dbPassword);
				//校验密码是否一致
				if(encryption.equals(dbPassword)){
					CenterSession selectSession = new CenterSession();
					selectSession.setUserId(result.getUserId());
					selectSession.setClientType(clientType);
					CenterSession resSession = centerSessionService.getCenterSessionOne(selectSession);
					String key = String.valueOf(result.getUserId())+"&"+String.valueOf(clientType);
					if(resSession==null){
						CenterSession centerSession = new CenterSession();
						centerSession.setLoginTime(TimeUtil.getCurrentTimestamp());
						centerSession.setUserId(result.getUserId());
						centerSession.setUserSource(result.getUserSourceType());//用户来源类型
						centerSession.setUserToken(token);
						centerSession.setClientType(clientType);
						centerSessionService.insertCenterSession(centerSession);
						//TODO 当前登录存放在redis中,而且is_login为1，存放pushToken
						//JedisUserCacheUtils.setCheckLoginHash(result.getPhone(), JSON.toJSONString(new RedisUserPhone(result.getPhone(), pushToken, 1, 1)));
						//登录信息放入缓存key--userId+clientType
						JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(centerSession));
					}else{
						resSession.setLoginTime(TimeUtil.getCurrentTimestamp());
						resSession.setUserToken(token);
						centerSessionService.updateCenterSession(resSession);
						JedisCache.delRedisVal(CenterUser.class.getSimpleName(),key);
						JedisCache.setRedisVal(null, CenterSession.class.getSimpleName(),key,JSONObject.toJSONString(resSession));
					}
					
					//登录次数+1 修改最后登录时间
					CenterUser c = new CenterUser();
					c.setLogins(0);//改字段不为空即可
					c.setLastLoginTime(TimeUtil.getCurrentTimestamp());
					c.setUserId(result.getUserId());
					centerUserService.updateUserLoginsByUserId(c);
					
					jo.put("userId", result.getUserId());
					jo.put("userName",result.getUserName());
					jo.put("userType", result.getUserType());
					jo.put("nickName", result.getNickName());
					jo.put("userToken", token);
					String linkImg = Common.getImgUrl(result.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					jo.put("imgLink",linkImg);
					//删除用户缓存
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(result.getUserId()));
					LogUtil.info(this.getClass(), "login", "登录成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "", jo);
				}else{
					//密码错误
					LogUtil.error(this.getClass(), "login", "密码错误");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_PASSWORD);
				}
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "login","密码加密异常",e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, BusinessConstant.ERROR_LOGIN);
			}
		}
	}

	/**
	 * 注销登录接口
	 * @param userId
	 * @param userType
	 * @param userName
	 * @param userToken
	 * @return
	 */
	public String logout(String userId, Integer userType, String userName, String userToken,Integer clientType) {
		try {
			centerSessionService.deleteCenterSessionByUserId(Integer.valueOf(userId),clientType);
			String key = userId+"&"+String.valueOf(clientType);
			//删除登录信息缓存
			JedisCache.delRedisVal(CenterSession.class.getSimpleName(),key);
			return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
		} catch (Exception e) {
			LogUtil.error(BusinessUserService.class, "logout","删除centerSession异常",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Common.getReturn(AppErrorCode.ERROR_DELETE, BusinessConstant.ERROR_DELETE).toJSONString();
		}
	}

	
	/**
	 * 提交注册或密码找回信息
	 * @param phoneNumber	手机号
	 * @param iCode	验证码
	 * @param password	密码
	 * @param invitationCode 邀请码
	 * @param registType 注册类型 1新注册 2密码找回
	 */
	@Transactional
	public String regist(String phoneNumber, String iCode, String password, String invitationCode, Integer registType,String nickName) {
		LogUtil.info(this.getClass(), "regist", "注册接口开始调用>phoneNumber="
					+phoneNumber+",iCode="+iCode+",registType="+registType+
					",password="+password+",invitationCode="+invitationCode);
		JSONObject jo = new JSONObject();
		try {
			//新注册
			if(registType==BusinessConstant.REGISTTYPE_1){
				/**
				 * 校验手机号是否已经存在
				 *//*
				String redisUse = JedisUserCacheUtils.getCheckLoginHash(phoneNumber);
				if(StringUtils.isNotEmpty(redisUse)){
					RedisUserPhone obj = JSONObject.parseObject(redisUse, RedisUserPhone.class);
					if(obj.getIs_check() == 1){
						*//**
						 * 当缓存中已经存在表示已经被占用
						 *//*
						jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
						jo.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
						return jo.toJSONString();
					}
				}else{
					*//**
					 * 当缓存不存在，则存放在redis中,
					 *//*
					CenterUser resUser = centerUserService.selectCenterUserByPhone(phoneNumber);
					if(resUser !=null){
						//当数据库中查询到该用户.则存入redis中，并且显示已经注册
						JedisUserCacheUtils.setCheckLoginHash(phoneNumber.trim(), JSON.toJSONString(new RedisUserPhone(phoneNumber, "", 0, 1)));
						
						*//**
						 * 当缓存中已经存在表示已经被占用
						 *//*
						jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
						jo.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
						return jo.toJSONString();
					}
				}
				//RedisUserPhone redisObject = new RedisUserPhone(phoneNumber, userToken, 0, 0);
				JedisUserCacheUtils.setCheckLoginHash(phoneNumber, JSON.toJSONString(new RedisUserPhone(phoneNumber, "", 0, 0)));*/
				
				//CenterUser select = new CenterUser();
				//select.setPhone(phoneNumber);
				//CenterUser resUser = centerUserService.selectCenterUserInfo(select);
				CenterUser resUser = centerUserService.selectCenterUserByPhone(phoneNumber);
				if(resUser==null){
					//校验手机号与验证码
					CenterSmsVerification centerSmsVerification = new CenterSmsVerification();
					centerSmsVerification.setPhone(phoneNumber);
					centerSmsVerification.setVerificationCode(iCode);
					centerSmsVerification.setType(BusinessConstant.VERIFICATION_TYPE_1);//(验证码类型。1:注册验证码，2:手机绑定验证码，3:密码找回验证码，4:提现验证码。)
					CenterSmsVerification rescentersmsverification = centerSmsVerificationService.getCenterSmsVerificationOne(centerSmsVerification);
					//验证码是否存在
					if(rescentersmsverification!=null){
						//校验昵称
						if(!Common.isValidName(nickName)){
							//昵称格式不对
							return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_NICKNAME).toJSONString();
						}
						Integer startTime = rescentersmsverification.getSendTime();
						Map<String,Long> map = TimeUtil.getTimeDifference(startTime, TimeUtil.getCurrentTimestamp());
						LogUtil.info(this.getClass(), "regist", "校验手验证码是否超时成功");
						//验证码目前有效期是十分钟  以秒为单位进行判断
						if(map.get("sec") <= BusinessConstant.VERIFICATION_TIME){
							//是否使用邀请码注册
							if(BusinessConstant.IS_MAST_INVITATION_CODE){
								LogUtil.info(this.getClass(), "regist", "使用邀请码注册开始调用");
								//校验邀请码
								CenterInvitationCode centerInvitationCode = new CenterInvitationCode();
								centerInvitationCode.setInvitationCode(invitationCode);
								CenterInvitationCode resCenterInvitationCode = centerInvitationCodeService.getCenterInvitationCodeOne(centerInvitationCode);
								if(resCenterInvitationCode!=null){
									//邀请码是否被使用
									if(resCenterInvitationCode.getStatus()==BusinessConstant.INVITATION_CODE_YES){
										//附加码  随机6位数字
										String radomInt = RandomCode.game(4);
										//一次加密
										String oneEncryption = MD5Utils.encryptToMD5(password);
										//二次加密
										String twoEncryption = MD5Utils.encryptToMD5(oneEncryption+radomInt);
										CenterUser c = new CenterUser();
										c.setSalt(String.valueOf(radomInt));
										c.setPassword(twoEncryption);
										c.setPhone(phoneNumber);
										c.setNickName(nickName);
										//1:注册用户，2:微信用户，3:QQ用户，4:微博用户，5:导入用户
										c.setUserSourceType(1);//用户来源类型 
										c.setUserType(3);//普通用户
										c.setLogins(0);//登录次数默认是0次
										c.setSex(-1);//性别默认是-1
										c.setQuestionNum(0);//提问数量
										c.setAnswerNum(0);//回答数量
										c.setClubActiveNum(0);//俱乐部活跃度
										c.setPraiseNum(0);//赞数量
										c.setRegTime(TimeUtil.getCurrentTimestamp());//注册时间
										c.setTrainNum(0);//实训次数
										c.setAtteClubNum(0);//关注俱乐部数量
										c.setAtteCompanyNum(0);//关注企业数量
										c.setTodayClubActiveNum(0);//当天俱乐部活跃度
										long birthday = TimeUtil.getSpecifiedTimeStamp("19970101");//默认值
										c.setBirthday((int)birthday);//生日
										c.setProfession(1);//职业
										c.setEducationLevel(4);//教育程度
										centerUserService.insertCenterUser(c);//返回用户id
										//更新邀请码表
										resCenterInvitationCode.setStatus(BusinessConstant.INVITATION_CODE_NO);//已使用
										resCenterInvitationCode.setRegUserId(c.getUserId());//用户id
										resCenterInvitationCode.setUseTime(TimeUtil.getCurrentTimestamp());
										centerInvitationCodeService.updateCenterInvitationCode(resCenterInvitationCode);
										//给该用户创建一级账户
										CenterAccount oneLevel = new CenterAccount();
										oneLevel.setAccountType(GameConstants.STAIR_ONE);//账户类型(1:一级货币)
										oneLevel.setUserId(c.getUserId());
										oneLevel.setUserType(BusinessConstant.ACCOUNT_USER_TYPE_1);
										oneLevel.setCreateTime(TimeUtil.getCurrentTimestamp());
										oneLevel.setBalance(0d);
										oneLevel.setLockAmount(0d);
										centerAccountService.insertCenterAccount(oneLevel);
										CenterAccount twoLevel = new CenterAccount();
										twoLevel.setAccountType(GameConstants.STAIR_TWO);//账户类型(2:二级货币。)
										twoLevel.setUserId(c.getUserId());
										twoLevel.setUserType(BusinessConstant.ACCOUNT_USER_TYPE_1);
										twoLevel.setCreateTime(TimeUtil.getCurrentTimestamp());
										twoLevel.setBalance(0d);
										twoLevel.setLockAmount(0d);
										centerAccountService.insertCenterAccount(twoLevel);
										jo.put(GameConstants.CODE, AppErrorCode.SUCCESS);
										LogUtil.info(this.getClass(), "regist", "使用邀请码注册成功");
									}else{
										LogUtil.error(this.getClass(), "regist", "注册邀请码已被使用");
										jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
										jo.put(GameConstants.MSG, BusinessConstant.ERROR_PLEASE_CODE_INVALID);
									}
								}else{
									LogUtil.error(this.getClass(), "regist", "注册邀请码不存在");
									jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
									jo.put(GameConstants.MSG, BusinessConstant.ERROR_PLEASE_CODE_NO_EXIST);
								}
							}else{//不需要邀请码
								LogUtil.info(this.getClass(), "regist", "无邀请码注册开始调用");
								//附加码  随机6位数字
								String radomInt = RandomCode.game(4);
								//一次加密
								String oneEncryption = MD5Utils.encryptToMD5(password);
								//二次加密
								String twoEncryption = MD5Utils.encryptToMD5(oneEncryption+radomInt);
								CenterUser c = new CenterUser();
								c.setSalt(String.valueOf(radomInt));
								c.setPassword(twoEncryption);
								c.setNickName(nickName);
								c.setPhone(phoneNumber);
								c.setUserSourceType(1);
								c.setUserType(3);//普通用户
								c.setLogins(0);//登录次数默认是0次
								c.setSex(-1);//性别默认是-1
								c.setQuestionNum(0);//提问数量
								c.setAnswerNum(0);//回答数量
								c.setClubActiveNum(0);//俱乐部活跃度
								c.setPraiseNum(0);//赞数量
								c.setRegTime(TimeUtil.getCurrentTimestamp());//注册时间
								c.setTrainNum(0);//实训次数
								c.setAtteClubNum(0);//关注俱乐部数量
								c.setAtteCompanyNum(0);//关注企业数量
								c.setTodayClubActiveNum(0);//当天俱乐部活跃度
								long birthday = TimeUtil.getSpecifiedTimeStamp("19970101");//默认值
								c.setBirthday((int)birthday);//生日
								c.setProfession(1);//职业
								c.setEducationLevel(4);//教育程度
								centerUserService.insertCenterUser(c);//返回用户id
								//JedisUserCacheUtils.setCheckLoginHash(phoneNumber, JSON.toJSONString(new RedisUserPhone(phoneNumber, "", 0, 1)));			
								//给该用户创建一级账户
								CenterAccount oneLevel = new CenterAccount();
								oneLevel.setAccountType(GameConstants.STAIR_ONE);
								oneLevel.setUserId(c.getUserId());
								oneLevel.setUserType(BusinessConstant.ACCOUNT_USER_TYPE_1);
								oneLevel.setCreateTime(TimeUtil.getCurrentTimestamp());
								oneLevel.setBalance(0d);
								oneLevel.setLockAmount(0d);
								centerAccountService.insertCenterAccount(oneLevel);
								CenterAccount twoLevel = new CenterAccount();
								twoLevel.setAccountType(GameConstants.STAIR_TWO);
								twoLevel.setUserId(c.getUserId());
								twoLevel.setUserType(BusinessConstant.ACCOUNT_USER_TYPE_1);
								twoLevel.setCreateTime(TimeUtil.getCurrentTimestamp());
								twoLevel.setBalance(0d);
								twoLevel.setLockAmount(0d);
								centerAccountService.insertCenterAccount(twoLevel);
								jo.put(GameConstants.CODE, AppErrorCode.SUCCESS);
								LogUtil.info(this.getClass(), "regist", "不使用邀请码注册成功");
							}
						}else{
							LogUtil.error(this.getClass(), "regist", "注册验证码超时");
							jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
							jo.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATIONCODE_INVALID);//验证码超时
						}
					}else{
						LogUtil.error(this.getClass(), "regist", "注册验证码不存在");
						jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FIVE);
						jo.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATION_CODE_NO_EXIST);
					}
				}else{
					LogUtil.error(this.getClass(), "regist","(注册)手机已注册");
					jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FOUR);
					jo.put(GameConstants.MSG, BusinessConstant.ERROR_HAVE_REGIST);
				}
			}else if(registType==BusinessConstant.REGISTTYPE_2){//找回密码
				LogUtil.info(this.getClass(), "regist", "找回密码开始调用");
				//校验手机号与验证码
				CenterSmsVerification centerSmsVerification = new CenterSmsVerification();
				centerSmsVerification.setPhone(phoneNumber);
				centerSmsVerification.setVerificationCode(iCode);
				centerSmsVerification.setType(BusinessConstant.VERIFICATION_TYPE_3);//密码找回验证码类型
				CenterSmsVerification resCenterSmsVerification = centerSmsVerificationService.getCenterSmsVerificationOne(centerSmsVerification);
				if(resCenterSmsVerification!=null){//校验验证码是否存在
					Integer startTime = resCenterSmsVerification.getSendTime();
					Map<String,Long> map = TimeUtil.getTimeDifference(startTime, TimeUtil.getCurrentTimestamp());
					LogUtil.error(this.getClass(), "regist", "校验找回密码验证码成功");
					if(map.get("sec") <= BusinessConstant.VERIFICATION_TIME){//校验验证码是否过期
						//附加码  随机6位数字
						String radomInt = RandomCode.game(4);
						//一次加密
						String oneEncryption = MD5Utils.encryptToMD5(password);
						//二次加密
						String twoEncryption = MD5Utils.encryptToMD5(oneEncryption+radomInt);
						CenterUser centeruser = new CenterUser();
						centeruser.setPhone(phoneNumber);
						CenterUser result = centerUserService.selectCenterUser(centeruser);
						if(result!=null){
							result.setSalt(String.valueOf(radomInt));
							result.setPassword(twoEncryption);
							centerUserService.updateCenterUserByKey(result);
							
							JedisCache.delRedisVal(CenterSession.class.getSimpleName(),String.valueOf(result.getUserId()));
							LogUtil.info(this.getClass(), "regist", "找回密码成功");
							jo.put(GameConstants.CODE, AppErrorCode.SUCCESS);
						}else{
							jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
							jo.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
						}
					}else{
						LogUtil.error(this.getClass(), "regist", "找回密码验证码过期");
						jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
						jo.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATIONCODE_INVALID);//验证码过期
					}
				}else{
					LogUtil.error(this.getClass(), "regist", "找回密码验证码不存在");
					jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
					jo.put(GameConstants.MSG, BusinessConstant.ERROR_INVITATION_CODE_NO_EXIST);//验证码不存在
				}
			}else{
				LogUtil.error(this.getClass(), "regist", "传入类型不正确");
				jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				jo.put(GameConstants.MSG, BusinessConstant.ERROR_TYPE_CODE);
			}
		} catch (Exception e) {
			LogUtil.error(BusinessUserService.class, "regist","注册或找回密码异常",e);
			jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
			jo.put(GameConstants.MSG, BusinessConstant.REGIST_EXCEPTION);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return jo.toJSONString();
	}
	
	/**
	 * 密码重置
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 */
	public String resetPassword(String userId, Integer userType, String userName, String userToken, String oldPassword,
			String newPassword) {
		JSONObject jo = new JSONObject();
		//校验旧密码
		CenterUser centeruser = new CenterUser();
		centeruser.setUserId(Integer.valueOf(userId));
		CenterUser result = centerUserService.selectCenterUser(centeruser);
		String salt = result.getSalt();//附加码
		try {
			//一次加密
			String oneEncryption = MD5Utils.encryptToMD5(oldPassword);
			//二次加密
			String twoEncryption = MD5Utils.encryptToMD5(oneEncryption+salt);
			if(twoEncryption.equals(result.getPassword())){
				String radomInt = RandomCode.game(4);
				//一次加密
				String oneEncryptionNew = MD5Utils.encryptToMD5(newPassword);
				//二次加密
				String twoEncryptionNew = MD5Utils.encryptToMD5(oneEncryptionNew+radomInt);
				CenterUser c = new CenterUser();
				c.setSalt(String.valueOf(radomInt));
				c.setPassword(twoEncryptionNew);
				c.setUserId(result.getUserId());
				centerUserService.updateCenterUserByKey(c);
				//删除用户缓存
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(), String.valueOf(result.getUserId()));
				jo.put(GameConstants.CODE,AppErrorCode.SUCCESS);
			}else{
				jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
				jo.put(GameConstants.MSG, BusinessConstant.ERROR_PASSWORD);//旧密码不对
			}
		} catch (Exception e) {
			jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_ONE);
			LogUtil.error(BusinessUserService.class, "resetPassword","密码重置异常",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return jo.toJSONString();
	}
	
	/**
	 * 获取登录用户的信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标示
	 * @param inquireType 查询类型 (废弃)
	 */
	public String getLoginUser(String userId, Integer inquireType,String userToken) {
		LogUtil.info(this.getClass(), "getLoginUser", "访问用户id为>"+userId);
		JSONObject json = new JSONObject();
		String cacheUser = RedisComponent.findRedisObject(Integer.valueOf(userId), CenterUser.class);
		CenterUser centeruser = null;
		if(!StringUtil.isEmpty(cacheUser)){
			centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
		}else{
			LogUtil.error(this.getClass(), "getLoginUser", "该用户不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_USER_DOES_NOT_EXIST).toJSONString();
		}
		if(centeruser!=null){
			String linkImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			//地址
			json.put("address", centeruser.getAddress());
			json.put("headLinkId",String.valueOf(centeruser.getHeadImgId()));
			/*
			 * 压缩图片
			 */
			json.put("headLink", Common.checkPic(linkImg) == true ? linkImg+ActiveUrl.HEAD_MAP:linkImg);
			
			json.put("userId", String.valueOf(centeruser.getUserId()));
			//如果是注册用户则账号和手机号是一致的
			if(centeruser.getUserSourceType().equals(1)){
				if(centeruser.getPhone().equals(centeruser.getUserName()) || centeruser.getUserName() == null || centeruser.getUserName().equals("")){
					json.put("userName",centeruser.getPhone());
				}else {
					json.put("userName",centeruser.getUserName());
				}
			}else{
				json.put("userName",centeruser.getUserName());
			}
			//用户来源类型
			json.put("userType",centeruser.getUserSourceType());
			json.put("userToken", userToken);
			json.put("nickname", centeruser.getNickName()==null?"":centeruser.getNickName());
			json.put("sex",centeruser.getSex() ==null ?-1: centeruser.getSex()==2?1:centeruser.getSex() == 1?0:-1);
			json.put("phoneNumber",centeruser.getPhone());
			json.put("realName",centeruser.getRealName());
			json.put("userNameUpdateTimes", centeruser.getUserNameUpdateTimes()==null?0:centeruser.getUserNameUpdateTimes());
			//省市信息
			String cacheTeachSchool = RedisComponent.findRedisObject(centeruser.getSchoolId(), TeachSchool.class);
			TeachSchool teachschool = null;
			if(!StringUtil.isEmpty(cacheTeachSchool)){
				teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
				if(teachschool!=null && teachschool.getRegionId()!=null){
					//省市信息
					List<PublicRegion> publicregionlist = publicRegionService.getPublicRegionByCityId(teachschool.getRegionId());
					if(publicregionlist != null){
						for (int i = 0; i < publicregionlist.size(); i++) {
							PublicRegion publicregion = publicregionlist.get(i);
							if(publicregion.getRegionType()==0){//省
								json.put("provinceId", String.valueOf(publicregion.getRegionId()));
							}else if(publicregion.getRegionType()==1){//市
								json.put("cityId",String.valueOf(publicregion.getRegionId()));
							}
						}
					}
					
				}
			}
			
			//学校信息
			json.put("schoolId", teachschool==null?"":String.valueOf(teachschool.getSchoolId()));
			json.put("schoolName", teachschool==null?"":teachschool.getSchoolName());
			
			//学院信息
			if(centeruser.getSchoolId()!=null){
				TeachInstitute teachinstitute = teachInstituteService.getTeachInstituteBySchoolId(centeruser.getSchoolId());
				if(null!=teachinstitute){
					json.put("collegeId",String.valueOf(teachinstitute.getInstId()));
					json.put("collegeName",teachinstitute.getInstName());
				}
			}
			
			json.put("idcard", centeruser.getIdCard());
			json.put("desc", centeruser.getDescription());
			json.put("birthday", centeruser.getBirthday()==null?"":String.valueOf(centeruser.getBirthday()));
			json.put("profession", centeruser.getProfession()==null?3:centeruser.getProfession());
			json.put("studentID", centeruser.getStudentId());
			json.put("studentCard", centeruser.getStudentCardNo());
			json.put("teacherCard", centeruser.getTeacherCardNo());
			json.put("positionalTitle", centeruser.getPositionalTitle());
			json.put("speciality", centeruser.getSpeciality());
			json.put("grade", centeruser.getGrade());
			json.put("educationLevel", centeruser.getEducationLevel()==null?7:centeruser.getEducationLevel());
			json.put("teachingRole",centeruser.getUserType());
			json.put("address", centeruser.getAddress());
			//当前用户是否加入班级
			if(centeruser.getClassId()==null){
				json.put("hasJoinClass", 0);//是否加入班级 0否
			}else{
				json.put("hasJoinClass", 1);
				json.put("classId",String.valueOf(centeruser.getClassId()));
			}
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(centeruser.getClubId());
			clubMember.setUserId(centeruser.getUserId());
			clubMember.setMemberStatus(1);//已加入
			ClubMember resclubMember = clubMemberService.selectClubMember(clubMember);
			if(resclubMember==null){
				json.put("hasJoinClub", 0);//是否加入俱乐部 0否 1 是
				json.put("clubRole", 0);
				json.put("clubId", centeruser.getClubId());
				json.put("gameEventId",String.valueOf(0));//俱乐部赛事id
			}else{
				//赛事信息
				ArenaCompetition arenacompetition = new ArenaCompetition();
				arenacompetition.setClubId(centeruser.getClubId());
				ArenaCompetition res = arenaCompetitionService.selectArenaCompetition(arenacompetition);
				json.put("clubRole", resclubMember==null?0:resclubMember.getLevel());
				json.put("hasJoinClub",1);
				json.put("clubId", String.valueOf(centeruser.getClubId()));
				json.put("gameEventId", res==null?"":String.valueOf(res.getCompId()));//俱乐部赛事id
			}
			LogUtil.info(this.getClass(), "getLoginUser","获取登录用户信息成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", json).toJSONString();
		}else{
			LogUtil.error(this.getClass(), "getLoginUser","用户不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_USER_DOES_NOT_EXIST).toJSONString();
		}
	}

	/** 
	* @Title: getCompanies 
	* @Description: 获取企业信息列表
	* @param userId	用户ID
	* @param memberId	人员ID
	* @param start	起始位
	* @param limit	每页显示数
	* @param inquireType	查询类型
	* @return String
	* yy---新增参数clientType=1 代表pc端请求clientType=2移动端请求
	* @author liulin
	* @date 2016年6月30日 下午9:01:56
	*/
	public String getCompanies(Integer userId, Integer memberId, Integer start, Integer limit, Integer inquireType, Integer clientType) {
		String dataStr = TimeUtil.getCurrentTime();
		String sign = MD5Utils.getMD5Str(dataStr);
		try{
			if(inquireType.intValue() == GameConstants.GET_COMPANY_ONE){
				//TODO 请求地址
				String param = "pageNo="+start+"&pageSize="+limit+"&sign="+sign+"&dataStr="+dataStr;
				//http请求获得企业信息
				JSONObject obj = getCompanyPost(ActiveUrl.REQUESTALLCOMPANY_URL,param);
				int count = obj.getInteger("totalRecord");
				JSONArray companyArray = obj.getJSONArray("ents");
				if(null == companyArray){
					Map<String, Object> backParam = new HashMap<String, Object>();
					backParam.put("returnCount", 0);
					backParam.put("allPage", 1);
					backParam.put("currentPage", 1);
					backParam.put("companies", JSONArray.parseArray("[]"));
					return Common.getReturn(AppErrorCode.SUCCESS, "",backParam).toJSONString();
				}
				//返回的数据集合
				JSONArray array = getAllCompanyArray(userId,companyArray,clientType);
				int allPage = getAllPage(count, limit);
				int currentPage = getCurrentPage(start, limit);
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", count);
				backParam.put("allPage", allPage);
				backParam.put("currentPage", currentPage);
				backParam.put("companies", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}else if(inquireType.intValue() == GameConstants.GET_COMPANY_TWO){
				//(yy修改)memberId为空查询的是自己的否则查询的是他人的信息
				Integer selectId = null;
				if(memberId == null || memberId == userId){
					selectId = userId;
				}else{
					selectId=memberId;
				}
				//分页查询企业
				QueryPage<CenterAttention> queryPage = centerAttentionService.getCenterAttentions(selectId, start, limit);
				if(CollectionUtils.isEmpty(queryPage.getList())){
					return queryPage.getMessageJSONObject("companies").toJSONString();
				}
				//获得关注企业列表
				List<CenterAttention> centerAttentions = queryPage.getList();
				//获取企业id字符串
				String ids = "";
				for(CenterAttention attention : centerAttentions){
					ids = ids + attention.getRelObjetId()+",";
				}
				ids = ids.substring(0, ids.length()-1);
				//TODO请求地址
				String param = "entIds="+ids+"&sign="+sign+"&dataStr="+dataStr;
				JSONObject obj = getCompanyPost(ActiveUrl.REQUESTOTHERCOMPANY_URL, param);
				JSONArray companyArray = obj.getJSONArray("ents");
				//返回的数据集合
				JSONArray array = getCompanyArray(companyArray,userId,clientType);
				//获得返回的总页数，当前页，总数量数据
				int count = queryPage.getCount();
				int allPage = getAllPage(count, limit);
				int currentPage = getCurrentPage(start, limit);
				Map<String, Object> backParam = new HashMap<String, Object>();
				backParam.put("returnCount", count);
				backParam.put("allPage", allPage);
				backParam.put("currentPage", currentPage);
				backParam.put("companies", array);
				return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
			}
		}catch(Exception e){
			LogUtil.error(this.getClass(), "getCompanies", "获取企业失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_COMPANIES).toJSONString();
		}
		return null;
	}
	
	/** 
	* @Title: getVerifyPicture 
	* @Description: 申请获取图片验证码
	* @param userId	用户ID
	* @return String
	* @author liulin
	* @date 2016年7月5日 下午4:07:26
	*/
	public String getVerifyPicture(Integer userId) {
		BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		int width = 75, height = 25;    
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
        // 获取图形上下文    
        Graphics g = image.getGraphics();    
        // 生成随机类    
        Random random = new Random();    
        // 设定背景色    
        g.setColor(getRandColor(200, 250));    
        g.fillRect(0, 0, width, height);    
        // 设定字体    
        g.setFont(new Font("Times New Roman", Font.PLAIN, 24));    
        // 画边框    
        g.setColor(getRandColor(160, 200));    
        g.drawRect(0, 0, width - 1, height - 1);    
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到    
        g.setColor(getRandColor(160, 200));    
        for (int i = 0; i < 155; i++) {    
            int x = random.nextInt(width);    
            int y = random.nextInt(height);    
            int xl = random.nextInt(12);    
            int yl = random.nextInt(12);    
            g.drawLine(x, y, x + xl, y + yl);    
        }    
        // 取随机产生的认证码(4位数字)    
        String sRand = "";    
        for (int i = 0; i < 4; i++) {    
            String rand = String.valueOf(random.nextInt(10));    
            sRand += rand;    
            // 将认证码显示到图象中    
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));    
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成    
            g.drawString(rand, 13 * i + 14, 20);    
        }    
        // 图象生效    
        g.dispose();    
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        // 输出图象到页面    
        try {
			ImageIO.write(image, "JPEG", bos);
			byte[] buf = bos.toByteArray();
			String verifyPictureLink = encoder.encodeBuffer(buf);
			Map<String, Object> backParam = new HashMap<String, Object>();
			backParam.put("verifyPictureLink", verifyPictureLink);
			backParam.put("verifyPictureNumber", sRand);
			return Common.getReturn(AppErrorCode.SUCCESS, "", backParam).toJSONString();
		} catch (IOException e) {
			LogUtil.error(this.getClass(), "getVerifyPicture", "申请验证码失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_GET_VERIFY_PICTURE).toJSONString();
		}finally{
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 给定范围获得随机颜色    
	private Color getRandColor(int fc, int bc) {
        Random random = new Random();    
        if (fc > 255)    
            fc = 255;    
        if (bc > 255)    
            bc = 255;    
        int r = fc + random.nextInt(bc - fc);    
        int g = fc + random.nextInt(bc - fc);    
        int b = fc + random.nextInt(bc - fc);    
        return new Color(r, g, b);    
	}

	/** 
	* @Title: getAllCompanyArray 
	* @Description: 组装返回集合
	* @param companyArray	企业数据
	* @return JSONArray
	* @author liulin
	* @date 2016年7月1日 下午12:31:34
	*/
	private JSONArray getAllCompanyArray(Integer userId, JSONArray companyArray,Integer clientType) {
		JSONArray array = new JSONArray();
		for(int i = 0; i<companyArray.size(); i++){
			JSONObject json = companyArray.getJSONObject(i);
			JSONObject company = new JSONObject();
			company.put("companyId", json.getString("companyId"));
			company.put("companyName", json.getString("companyName"));
			/*
			 * companyLogoLink 该链接为访问链接
			 */
			String val = json.getString("companyDetailLink");
			company.put("companyDetailLink", val);
			/*
			 *获取图片 
			 */
			String imgLink= json.getString("companyLogoLink");
			if(StringUtil.isEmpty(imgLink)){
				imgLink = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_ENTERPRISE);
			}
			company.put("companyLogoLink", Common.checkPic(imgLink) == true ? imgLink+ActiveUrl.HEAD_MAP:SystemConfig.getString("COMPANY_LOGO_LINK")+imgLink);
			
			company.put("postCount", json.getInteger("postCount"));
			company.put("postTypeCount", json.getInteger("postTypeCount"));
			CenterCompany centerCompany = new CenterCompany();
			centerCompany.setCompanyId(json.getString("companyId"));
			centerCompany = centerCompanyService.getCenterCompany(centerCompany);
			int supportCount = 0;
			if(centerCompany == null){
				supportCount = 0;
			}else{
				supportCount = centerCompany.getPraiseNum();
			}
			//获取关注信息
			CenterAttention attention = new CenterAttention();
			attention.setCreateUserId(userId);
			attention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_COMPANY);
			attention.setRelObjetId(json.getString("companyId"));
			attention = centerAttentionService.getSingleCenterAttention(attention);
			if(null != attention){
				company.put("hasBeenAttention", 1);
			}else{
				company.put("hasBeenAttention", 0);
			}
			company.put("supportCount", supportCount);
			company.put("companyDetailLink", json.getString("companyDetailLink"));
			array.add(company);
		}
		return array;
	}
	
	/** 
	* @Title: getCompanyArray 
	* @Description: 组装返回集合
	* @param companyArray	企业数据
	* @return JSONArray
	* @author liulin
	* @date 2016年7月1日 下午12:31:34
	*/
	private JSONArray getCompanyArray(JSONArray companyArray,Integer userId,Integer clientType) {
		JSONArray array = new JSONArray();
		
		for(int i = 0; i<companyArray.size(); i++){
			JSONObject json = companyArray.getJSONObject(i);
			JSONObject company = new JSONObject();
			company.put("companyId", json.getString("companyId"));
			company.put("companyName", json.getString("companyName"));
			/*
			 * 该链接为访问链接
			 */
			String imgLink = json.getString("companyDetailLink");
			company.put("companyDetailLink",imgLink);
			/*
			 * 获取图片链接
			 */
			String val = json.getString("companyLogoLink");
			if(StringUtils.isEmpty(val)){
				val = Common.getImgUrl(null, BusinessConstant.DEFAULT_IMG_ENTERPRISE);
			}

			company.put("companyLogoLink", Common.checkPic(val)== true ? val +ActiveUrl.HEAD_MAP:SystemConfig.getString("COMPANY_LOGO_LINK") +val);
			company.put("postCount", json.getInteger("postCount"));
			company.put("postTypeCount", json.getInteger("postTypeCount"));
			CenterCompany centerCompany = new CenterCompany();
			centerCompany.setCompanyId(json.getString("companyId"));
			centerCompany = centerCompanyService.getCenterCompany(centerCompany);
			int supportCount = 0;
			if(centerCompany == null){
				supportCount = 0;
			}else{
				supportCount = centerCompany.getPraiseNum();
			}
			CenterAttention centerAttention = new CenterAttention();
			centerAttention.setRelObjetId(json.getString("companyId"));
			centerAttention.setCreateUserId(userId);
			centerAttention.setRelObjetType(GameConstants.REL_OBJECT_TYPE_COMPANY);
			centerAttention = centerAttentionService.queryCenterAttentionByType(centerAttention);
			if(centerAttention == null){
				company.put("hasBeenAttention", 0);
				
			}else{
				company.put("hasBeenAttention", 1);
			}
			
			company.put("supportCount", supportCount);
			company.put("companyDetailLink", json.getString("companyDetailLink"));
			array.add(company);
		}
		return array;
	}
	
	//获得总页数
	private Integer getAllPage(Integer count, Integer limit) {
		if(count>0 && limit>0){
			int tmp = count/limit;
			return (count%limit > 0) ? ++tmp : tmp ;
		}
		return 0;
	}
	
	//获得当前页
	private Integer getCurrentPage(Integer start, Integer limit) {
		int currentPage = 1;
		if(start > 0 && limit >0){
			return (start/limit)+1;
		}
		return currentPage;
	}
	
	/**
	 * 获取企业对象
	 * @param param
	 * @return
	 * @author 			lw
	 * @date			2016年8月28日  下午8:19:51
	 */
	public static JSONObject getCompanyPost(String url ,String param){
		String companyJson = HttpRequest.sendPost(url, param);
		JSONObject obj = JSONObject.parseObject(companyJson);
		JSONArray jsonArray = obj.getJSONArray("ents");
		String datestr = TimeUtil.getFormatTime();
		JSONObject jsonObj = null;
		for(Object en : jsonArray){
			jsonObj = (JSONObject) en;
			String src = jsonObj.getString("companyDetailLink");
			String entId = jsonObj.getString("companyId");
			String companyDetailLink ="";
			try {
				companyDetailLink = src+"&datestr="+datestr+"&sign="+MD5Utils.getMD5Str("ent_id="+entId+"&datestr="+datestr);
			} catch (Exception e) {
				LogUtil.error(BusinessUserService.class, "getCompanyPost", e+"");
			}
			jsonObj.put("companyDetailLink", companyDetailLink);
			/*if(!StringUtil.isEmpty(jsonObj.getString("companyDetailLink"))){
				jsonObj.put("companyLogoLink", SystemConfig.getString("COMPANY_LOGO_LINK") + (Common.checkPic(jsonObj.getString("companyLogoLink")) == true ? jsonObj.getString("companyLogoLink")+ActiveUrl.HEAD_MAP:jsonObj.getString("companyLogoLink")));
			}*/
		}
		return obj;
	}
	
	/**
	 * 获取学生课程统计信息
	 * @param user
	 * @param classId
	 * @param userId
	 * @author chengshx
	 */
	private void setCourseCount(User user, Integer classId, Integer userId){
		// add by chengshx 2016-10-27  课程统计信息
		JSONObject countJson = teachRelStudentCourseService.selectTeachRelStudentCourseCount(classId, userId);
		Integer completedCourses = countJson.getInteger("completedCourses");
		Integer allCourses = countJson.getInteger("allCourses");
		String learningSecondsStr = countJson.getString("learningSeconds");
		Integer learningSeconds = 0;
		if(learningSecondsStr != null && !learningSecondsStr.equals("")){
			learningSeconds = Integer.valueOf(learningSecondsStr);
		}
		user.setCompletedCourses(completedCourses);
		user.setAllCourses(allCourses);
		user.setLearningSeconds(learningSeconds);
	}
	
	/**
	 *  验证手机号码是否存在
	 * @param userId 用户id 预留
	 * @param phoneNumber 验证的手机号
	 * @return
	 */
	public String verifyPhoneNumberExist(String userId, String phoneNumber) {
		JSONObject json = new JSONObject();
		/*
		 * 验证手机号是否符合规则
		 */
		boolean isMobile = isMobile(phoneNumber);
		if(isMobile){
				/*
				 * 校验手机号是否存在
				 */
				CenterUser centeruser = new CenterUser();
				centeruser.setPhone(phoneNumber);
				
				CenterUser result = centerUserService.selectCenterUser(centeruser);
				
				if(null==result){//数据库不存在该手机号
					LogUtil.info(this.getClass(), "verifyPhoneNumberExist","(验证手机号是否存在)手机号未被注册");
					
					json.put("whetherExists", 0);
					
					return Common.getReturn(GameConstants.RESULT_SUCCESS,"",json).toJSONString();
				}else{//数据库存在该手机号
						LogUtil.info(this.getClass(), "verifyPhoneNumberExist","(验证手机号是否存在)手机号已被注册");
						
						json.put("whetherExists", 1);
						
						return Common.getReturn(GameConstants.RESULT_SUCCESS,AppErrorCode.PHONE_REGISTER,json).toJSONString();
				}
		}else{
			/*
			 * 手机号不规则
			 */
			LogUtil.info(this.getClass(), "verifyPhoneNumberExist","输入的手机号码不规则");
			
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE,AppErrorCode.PHONE_NO_RULE).toJSONString();
		}
	}
	
	/**
	 * 校验手机号
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
		boolean result = false;
		if(!StringUtil.isEmpty(str)){
			String phone = str.trim();
			//该字符串是数字并且长度为11位
	        if(phone.matches("[0-9]+") && phone.length()==11){
	        	String[] nums = BusinessConstant.PHONE_NUMBER;
	        	String tit = phone.substring(0, 3);//前三位是否在有效的号段内
	        	List<String> tempList = Arrays.asList(nums);
	        	if(tempList.contains(tit)){
	        		result = true;
	        	}else{
	        		result = false;
	        	}
	        }else{
	        	result = false;
	        }
		}
        return result;  
    }  
}



 