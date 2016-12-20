package com.seentao.stpedu.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisAndDBBean;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.service.BaseTeachCourseService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.common.service.TeachRelStudentClassService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class PublishhomeworkAppService {

	@Autowired
	private BaseTeachCourseService baseTeachCourseService;
	
	@Autowired
	private TeachRelStudentClassService relStudentClassService;
	@Autowired
	private TeachRelTeacherClassService relTeacherClassService;
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	/**
	 * 教师发布文本作业
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param homeworkId			文本作业id
	 * @param homeworkTitle			作业标题
	 * @param homeworkBody			作业内容
	 * @param courseCardId			所属课程卡id
	 * @param homeworkShowType		作业显示类型
	 * @return
	 * @author  lw
	 * @date    2016年6月18日 下午10:10:26
	 */
	public String submitTeachCourse(Integer userId,  Integer homeworkId, String homeworkTitle
			, String homeworkBody, Integer courseCardId,Integer homeworkShowType) {

		//	1.初始化作业类型
		homeworkShowType = homeworkShowType == null ? 0 : homeworkShowType;
		
		//根据课程卡id查询课程卡表判断课程卡是否设置时间
		if(courseCardId != null){
			TeachCourseCard teachCourseCard = new TeachCourseCard();
			teachCourseCard.setCardId(courseCardId);
			TeachCourseCard teachCourseCardTime =teachCourseCardService.getSingleTeachCourseCard(teachCourseCard);
			if(teachCourseCardTime != null ){
				if(teachCourseCardTime.getStartTime() == null || teachCourseCardTime.getEndTime() == null){
					LogUtil.error(this.getClass(), "submitTeachCourse", AppErrorCode.ISCARDTIME);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ISCARDTIME).toJSONString();
				}
			} 
		}
		//	2.校验作业类型
		switch (homeworkShowType) {
		case GameConstants.TEACHER_HOMEWORK_REQUIRED:
		case GameConstants.TEACHER_HOMEWORK_ELECTIVE:
		case GameConstants.TEACHER_HOMEWORK_HIDE:
			
			//	3.1  新建课程
			if(homeworkId == null || homeworkId <= 0 ){
				
				//	3.11 通过科课程卡id查询查询章节对象 
				RedisAndDBBean<TeachCourseChapter> chapterRedis = RedisComponent.recursionRedisObjectExecute(courseCardId, TeachCourseChapter.class, GameConstants.REDIS_AGREEMENT.CARD,GameConstants.REDIS_AGREEMENT.CHAPTER);
				if(chapterRedis.isSuccess()){
				
				//	3.12 校验 用户是否是 该班级教师
				TeachRelTeacherClass teacherClass = relTeacherClassService.isTeacherClass(userId,chapterRedis.getResultBean().getClassId());
				if(teacherClass == null){
					LogUtil.error(this.getClass(), "submitTeachCourse", AppErrorCode.TEACHER_HOME_ROLE);
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_ROLE).toJSONString();
				}
				
				//	3.13 获取班级人数 （对应返回字段：应交作业）
				Integer countStudent = relStudentClassService.queryCountByClassId(String.valueOf(chapterRedis.getResultBean().getClassId()));
				TeachCourse param = new TeachCourse(); 
				param.setCourseTitle(homeworkTitle);
				param.setCourseContent(homeworkBody);
				param.setTeacherId(userId);
				param.setCourseType(GameConstants.TEACHER_HOMEWORK_TYPE_TEXT);
				param.setCreateTime(TimeUtil.getCurrentTimestamp());
				param.setIsDelete(GameConstants.NO_DEL);
				try {
					
					// 3.14 保存文本作业
					baseTeachCourseService.insertTeachCourse(param);
					
					//	3.15 添加关系 到 课程卡课程关系表
					TeachRelCardCourse cardCourse = new TeachRelCardCourse();
					cardCourse.setPlanNum(countStudent);
					cardCourse.setCardId(courseCardId);
					cardCourse.setCourseId(param.getCourseId());
					cardCourse.setShowType(homeworkShowType);
					cardCourse.setCreateTime(TimeUtil.getCurrentTimestamp());
					cardCourse.setActualNum(0);
					cardCourse.setTotalStudyNum(0);
					teachRelCardCourseService.insertTeachRelCardCourse(cardCourse);
					LogUtil.info(this.getClass(), "submitTeachCourse", GameConstants.SUCCESS_INSERT);
					return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(this.getClass(), "submitTeachCourse", String.valueOf(AppErrorCode.ERROR_INSERT));
					return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.TEACHER_HOME_INSERT_WRONG).toJSONString();
				}
			}
		}else{
			//	3.2	课程更新
			TeachCourse param = new TeachCourse();
			param.setCourseId(homeworkId);
			param.setTeacherId(userId);
			param.setCourseType(GameConstants.TEACHER_HOMEWORK_TYPE_TEXT);			
			param.setIsDelete(GameConstants.NO_DEL);						
			param = baseTeachCourseService.selectTeachCourse(param);
			
			//	3.21 教师未创建课程校验
			if(param == null){
				LogUtil.error(this.getClass(), "submitTeachCourse", AppErrorCode.ERROR_SUBMITHOMEWORK_REQUEST_PARAM);
				return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.TEACHER_HOME_COURSE).toJSONString(); 
			}
			
			//	3.22 修改课程信息
			try {
				param.setCourseTitle(homeworkTitle);
				param.setCourseContent(homeworkBody);
				baseTeachCourseService.updateCourse(param);
				LogUtil.info(this.getClass(), "submitTeachCourse", GameConstants.SUCCESS_UPDATE);
				
				//	3.23 删除redis缓存
				JedisCache.delRedisVal(TeachCourse.class.getSimpleName(), String.valueOf(homeworkId));
				return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "submitTeachCourse", String.valueOf(AppErrorCode.ERROR_UPDATE));
				return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.TEACHER_HOME_COURSE).toJSONString();
			}
		}

		default:
			LogUtil.error(this.getClass(), "submitTeachCourse", AppErrorCode.ERROR_SUBMITHOMEWORK_REQUEST_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.TEACHER_HOME_INSERT_WRONG).toJSONString();
		}
		
	}
	
	

}
