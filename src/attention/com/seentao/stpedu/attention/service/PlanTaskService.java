package com.seentao.stpedu.attention.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachPlan;
import com.seentao.stpedu.common.entity.TeachPlanSign;
import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachPlanService;
import com.seentao.stpedu.common.service.TeachPlanSignService;
import com.seentao.stpedu.common.service.TeachRelStudentClassService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

/**
 * 计划任务信息
 * <pre>项目名称：stpedu    
 * 类名称：PlanTaskController    
 */
@Service
public class PlanTaskService {
	
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private TeachPlanService teachPlanService;
	
	@Autowired
	private TeachClassService teachClassService;
	
	@Autowired
	private TeachRelStudentClassService teachRelStudentClassService;
	
	@Autowired
	private TeachPlanSignService teachPlanSignService;
	
	@Autowired
	private TeachRelTeacherClassService  teachRelTeacherClassService;
	
	/**
	 * submitTask(提交计划任务信息)   
	 * @param userId 用户ID
	 * @param taskId 计划任务id
	 * @param taskDate 计划任务发生时间的时间戳
	 * @param taskContent 任务内容
	 * @param targetContent 任务目标
	 * @param needSign 是否需要签到  1:是；0:否；默认是0
	 * @author ligs
	 * @date 2016年6月28日 下午10:21:55
	 * @return
	 * @throws ParseException 
	 */
	public JSONObject submitTask(Integer userId, Integer taskId, Integer taskDate, String taskContent, String targetContent,Integer needSign)  {
		//判断任务目标和任务内容不能有特殊字符
		String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
		for(String s:str){
			if(targetContent.contains(s)){
				LogUtil.error(this.getClass(), "submitTask", "任务目标不能包含特殊字符");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,AppErrorCode.TASKGOALSTANDARD);
			}
			if(taskContent.contains(s)){
				LogUtil.error(this.getClass(), "submitTask", "任务内容不能包含特殊字符 ");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,AppErrorCode.TASKCONTEXTSTANDARD);
			}
		}
		//获取系统时间戳精确到秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dates = sdf.format(new Date());
	    Date parses = null;
		try {
			parses = sdf.parse(dates);
		} catch (ParseException e) {
			LogUtil.error(this.getClass(), "submitTask", "日期转换异常");
		}
	    int dateTime = (int)(parses.getTime()/1000);
	    //判断发布时间不能早于当前时间
		if(null != taskDate){
			if(dateTime > taskDate){
				LogUtil.error(this.getClass(), "submitTask", "计划任务不能早于当前时间 ");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.TASKTIME);
			}
	    }
	    //增加任务内容和任务目标字数校验
	    boolean isContext = Common.isValid(taskContent.trim() , 2 , 140);
	    if(!isContext){
			LogUtil.error(this.getClass(), "submitTask", "任务内容字数在2-140字");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.TASKCONTEXT);
		}
	    boolean isTask = Common.isValid(targetContent.trim() , 2 , 140);
	    if(!isTask){
			LogUtil.error(this.getClass(), "submitTask", "任务目标字数在2-140字");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.TASKGOAL);
		}

		if(taskId == 0 || taskId == null){//新增计划
			//通过用户ID查询【用户表】判断是否为该班级教师
			CenterUser centerUser = new CenterUser();
			centerUser.setUserId(userId);
			CenterUser iscenterUser =centerUserService.selectCenterUser(centerUser);
			if(iscenterUser.getUserType() != GameConstants.USER_TYPE_TEAHCER){
				LogUtil.error(this.getClass(), "submitTask", "您不是该班级的教师");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLASS);
			}else {
				//获得班级ID查询计划表判断该班级这一天是否已经发布任务
				TeachPlan teachPlan = new TeachPlan();
				teachPlan.setClassId(iscenterUser.getClassId());
				TeachPlan isTeachPlan =teachPlanService.selectTeachPlan(teachPlan);
				if(isTeachPlan == null ){
					//判断是否开启签到  开启签到时计划签到表添加班级成员信息
					if(needSign == GameConstants.IS_SIGN_ONE){//开启签到
						//通过班级ID查询班级表获得应签到人数
						TeachClass teachClass = teachClassService.getTeachClassById(iscenterUser.getClassId());
						TeachPlan addTeachPlan = new TeachPlan();
						addTeachPlan.setClassId(iscenterUser.getClassId());
						addTeachPlan.setPlanDate(taskDate);
						addTeachPlan.setPlanTask(taskContent);
						addTeachPlan.setPlanTarget(targetContent);
						addTeachPlan.setIsSign(GameConstants.IS_SIGN_ONE);
						addTeachPlan.setPlanSignNum(teachClass.getStudentsNum());
						addTeachPlan.setActualSignNum(0);
						addTeachPlan.setCreateTime(dateTime);
						addTeachPlan.setIsDelete(GameConstants.NO_DEL);
						//添加到计划表返回计划任务ID
						@SuppressWarnings("unused")
						Integer planIds = teachPlanService.insertTeachPlanReturn(addTeachPlan);
						//通过班级id查询学生班级关系表获得学生ID
						List<TeachPlanSign> teachStudentClassAll =new ArrayList<TeachPlanSign>();
						TeachRelStudentClass teachRelStudentClass = new TeachRelStudentClass();
						teachRelStudentClass.setClassId(iscenterUser.getClassId());
						teachRelStudentClass.setIsDelete(GameConstants.NO_DEL);
						List<TeachRelStudentClass> teachRelStudentClassAll = teachRelStudentClassService.getTeachRelStudentClassList(teachRelStudentClass);
						if(teachRelStudentClassAll != null || teachRelStudentClassAll.size() > 0){
							for (TeachRelStudentClass addteachRelStudentClass : teachRelStudentClassAll) {
								//学生信息添加到计划任务签到表
								TeachPlanSign teachPlanSign = new TeachPlanSign();
								teachPlanSign.setPlanId(addTeachPlan.getPlanId());
								teachPlanSign.setStudentId(addteachRelStudentClass.getStudentId());
								teachPlanSign.setIsDelete(GameConstants.NO_DEL);
								teachStudentClassAll.add(teachPlanSign);
							}
							teachPlanSignService.insertTeachPlanSignAlls(teachStudentClassAll);
						}
						LogUtil.info(this.getClass(), "submitTask", "成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}else if(needSign == GameConstants.IS_SIGN_NO){//不开启签到
						TeachPlan addTeachPlan = new TeachPlan();
						addTeachPlan.setClassId(iscenterUser.getClassId());
						addTeachPlan.setPlanDate(taskDate);
						addTeachPlan.setPlanTask(taskContent);
						addTeachPlan.setPlanTarget(targetContent);
						addTeachPlan.setIsSign(GameConstants.IS_SIGN_NO);
						addTeachPlan.setCreateTime(dateTime);
						addTeachPlan.setIsDelete(GameConstants.NO_DEL);
						//添加到计划表
						teachPlanService.insertTeachPlan(addTeachPlan);
						LogUtil.info(this.getClass(), "submitTask", "成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}
				}else if(isTeachPlan != null ){
					if((isTeachPlan.getCreateTime()*1000) == taskDate){
						LogUtil.error(this.getClass(), "submitTask", "该班级已经在计划日期发布过任务");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.CLASS_IS_ISSUE);
					}
					//判断是否开启签到  开启签到时计划签到表添加班级成员信息
					if(needSign == GameConstants.IS_SIGN_ONE){//开启签到
						//通过班级ID查询班级表获得应签到人数
						TeachClass teachClass = teachClassService.getTeachClassById(iscenterUser.getClassId());
						TeachPlan addTeachPlan = new TeachPlan();
						addTeachPlan.setClassId(iscenterUser.getClassId());
						addTeachPlan.setPlanDate(taskDate);
						addTeachPlan.setPlanTask(taskContent);
						addTeachPlan.setPlanTarget(targetContent);
						addTeachPlan.setIsSign(GameConstants.IS_SIGN_ONE);
						addTeachPlan.setPlanSignNum(teachClass.getStudentsNum());
						addTeachPlan.setActualSignNum(0);
						addTeachPlan.setCreateTime(dateTime);
						addTeachPlan.setIsDelete(GameConstants.NO_DEL);
						//添加到计划表返回计划任务ID
						@SuppressWarnings("unused")
						Integer planIds = teachPlanService.insertTeachPlanReturn(addTeachPlan);
						//通过班级id查询学生班级关系表获得学生ID
						List<TeachPlanSign> teachStudentClassAll =new ArrayList<TeachPlanSign>();
						TeachRelStudentClass teachRelStudentClass = new TeachRelStudentClass();
						teachRelStudentClass.setClassId(iscenterUser.getClassId());
						teachRelStudentClass.setIsDelete(GameConstants.NO_DEL);
						List<TeachRelStudentClass> teachRelStudentClassAll = teachRelStudentClassService.getTeachRelStudentClassList(teachRelStudentClass);
						if(teachRelStudentClassAll != null){
							for (TeachRelStudentClass addteachRelStudentClass : teachRelStudentClassAll) {
								//学生信息添加到计划任务签到表
								TeachPlanSign teachPlanSign = new TeachPlanSign();
								teachPlanSign.setPlanId(addTeachPlan.getPlanId());
								teachPlanSign.setStudentId(addteachRelStudentClass.getStudentId());
								teachPlanSign.setIsDelete(GameConstants.NO_DEL);
								teachStudentClassAll.add(teachPlanSign);
							}
							teachPlanSignService.insertTeachPlanSignAlls(teachStudentClassAll);
						}
						LogUtil.info(this.getClass(), "submitTask", "成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}else if(needSign == GameConstants.IS_SIGN_NO){//不开启签到
					    
						TeachPlan addTeachPlan = new TeachPlan();
						addTeachPlan.setClassId(iscenterUser.getClassId());
						addTeachPlan.setPlanDate(taskDate);
						addTeachPlan.setPlanTask(taskContent);
						addTeachPlan.setPlanTarget(targetContent);
						addTeachPlan.setIsSign(GameConstants.IS_SIGN_NO);
						addTeachPlan.setCreateTime(dateTime);
						addTeachPlan.setIsDelete(GameConstants.NO_DEL);
						//添加到计划表
						teachPlanService.insertTeachPlan(addTeachPlan);
						LogUtil.info(this.getClass(), "submitTask", "成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "");
					}
				}
				
			}
		}else {//修改计划任务信息
			//判断是否为教师
			TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
			teachRelTeacherClass.setTeacherId(userId);
			TeachRelTeacherClass isteachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
			if(isteachRelTeacherClass == null){
				LogUtil.error(this.getClass(), "submitTask", "不是教师");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_ERROR_CLASS);
			}else {
				//修改任务信息
				TeachPlan upTeachPlan = new TeachPlan();
				upTeachPlan.setPlanId(taskId);
				upTeachPlan.setPlanTask(taskContent);
				upTeachPlan.setPlanTarget(targetContent);
				//添加到计划表
				teachPlanService.updateTeachPlanByKey(upTeachPlan);
				LogUtil.info(this.getClass(), "submitTask", "修改成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "");
			}
		}
		
		return null;
	}
	
	/**
	 * getTasks(获取计划任务信息)   
	 * @param 用户ID
	 * @param classId 班级ID
	 * @param startYear 开始年份
	 * @param startMonth 开始月份
	 * @param endYear 截止年
	 * @param endMonth 截止月
	 * @author ligs
	 * @date 2016年6月29日 上午10:44:44
	 * @return
	 * @throws ParseException 
	 */
	public JSONObject getTasks(Integer userId, Integer classId, String startYear, String startMonth, String endYear,String endMonth) {
		//通过班级ID和用户ID查询用户表判断是否属于该班级
		CenterUser centerUser = new CenterUser();
		centerUser.setUserId(userId);
		CenterUser isCenterUser = centerUserService.selectCenterUser(centerUser);
		if(isCenterUser.getUserType() == 1){//如果为教师
			TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
			teachRelTeacherClass.setTeacherId(userId);
			teachRelTeacherClass.setClassId(classId);
			TeachRelTeacherClass isTeachRelTeacherClass =teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
			if(isTeachRelTeacherClass == null){
				LogUtil.error(this.getClass(), "submitComment", "教师不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}else if(isCenterUser.getUserType() == 2){//如果为学生
			if(!isCenterUser.getClassId().equals(classId)){
				LogUtil.error(this.getClass(), "submitComment", "学生不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}else if(isCenterUser.getUserType() == 3){//如果为用户
			if(!isCenterUser.getClassId().equals(classId)){
				LogUtil.error(this.getClass(), "submitComment", "用户不属于该班级");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.IS_NOT_CLASS_USER);
			}
		}
			//获取当月最后一天
			Integer maxDay = TimeUtil.getMaxDayByYearMonth(Integer.parseInt(endYear), Integer.parseInt(endMonth));
			//判断计划日期在开始年月截止年月与之内
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
			String startTime =startYear +" "+ startMonth+" 01 00:00:00";
			String endTime = endYear +" "+ endMonth +" "+ maxDay +" 23:59:59";
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = sdf.parse(startTime);
				endDate =sdf.parse(endTime);
			} catch (ParseException e) {
				LogUtil.error(this.getClass(), "getTasks" , "日期转换异常");
			}
			
			long staDate = (long)(startDate.getTime()/1000);
			
			long enddate = (long)(endDate.getTime()/1000);
			Map<String , Object> tasks = new HashMap<String , Object>();
			tasks.put("staDate", staDate);//开始年月
			tasks.put("endDate", enddate);//截止年月
			tasks.put("classId", classId);//班级ID
			tasks.put("isDelete", GameConstants.NO_DEL);//是否删除
			List<TeachPlan> returnTeachPlan = teachPlanService.mapUpdateTeachPlan(tasks);
			if(returnTeachPlan == null || returnTeachPlan.size() <= 0){
				JSONObject json = new JSONObject();
				json.put("tasks", new JSONArray());
				LogUtil.error(this.getClass(), "getTasks", "查询的时间段内没有计划任务信息");
				return Common.getReturn(AppErrorCode.SUCCESS,"", json);
			}
			JSONArray array = new JSONArray();
			for (TeachPlan teachPlan : returnTeachPlan) {
				//分割计划任务日期  得到 年 月 日
				SimpleDateFormat  sip = new SimpleDateFormat("yyyy MM dd HH mm ss");
				long plandate =(long)teachPlan.getPlanDate();
				teachPlan.setTaskDate(String.valueOf(plandate));
				//转换时间戳为日期格式
				String spDate = sip.format(plandate*1000);
				String[] splitDate = spDate.split(" ");
				for (int i = 0; i < splitDate.length; i++) {
					teachPlan.setTaskYear(Integer.valueOf(splitDate[0]));//年
					teachPlan.setTaskMonth(Integer.valueOf(splitDate[1]));//月
					teachPlan.setTaskDay(Integer.valueOf(splitDate[2]));//日
				}
				//根据用户ID和计划ID查询计划签到表判断是否已经签到
				if(teachPlan.getIsSign() == 1){//已开启签到
					//当前登录者是否已经签到
					if(isCenterUser.getUserType() == 1){
						teachPlan.setHasSigned(0);//当前登录者是否已经签到
					}else {
						TeachPlanSign teachPlanSign = new TeachPlanSign();
						teachPlanSign.setIsDelete(GameConstants.NO_DEL);
						teachPlanSign.setStudentId(userId);
						teachPlanSign.setPlanId(teachPlan.getPlanId());
						TeachPlanSign isteachPlanSign = teachPlanSignService.getTeachPlanSigns(teachPlanSign);
						if(isteachPlanSign == null){
							if(isCenterUser.getUserType() == 2 || isCenterUser.getUserType() == 3){//用户为后加入的学生时添加计划签到表
								TeachPlanSign teachPlanSignd = new TeachPlanSign();
								teachPlanSignd.setPlanId(teachPlan.getPlanId());
								teachPlanSignd.setStudentId(userId);
								teachPlanSignd.setIsDelete(GameConstants.NO_DEL);
								teachPlanSignService.insertTeachPlanSign(teachPlanSignd);
							}
							teachPlan.setHasSigned(0);//未签到
						}else {
							if(isteachPlanSign.getSignTime() == null){
								teachPlan.setHasSigned(0);//未签到
							}else{
								teachPlan.setHasSigned(1);//已签到
							}
						}
					}
				}else if(teachPlan.getIsSign() == 0){//未开启签到
					teachPlan.setHasSigned(0);//当前登录者是否已经签到
				}
				TeachClass teachClass = teachClassService.getTeachClassById(classId);
				teachPlan.setPlanSignNum(teachClass.getStudentsNum());//应签到学生数量
				
				array.add(teachPlan);
			}
			
			JSONObject json = new JSONObject();
			json.put("tasks", array);
			LogUtil.info(this.getClass(), "getTasks", "获取计划任务成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", json);
	}
}
