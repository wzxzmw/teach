package com.seentao.stpedu.course.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.common.service.BaseTeachStudentHomeworkService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class TeacherHomeWorksAppService {

	
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	@Autowired
	private BaseTeachStudentHomeworkService homeworkService;
	
	/**
	 * 获取教师所发文本作业信息
	 * 	教师发布的作业文本其实就是课程
	 * @param userId			必须传字段		用户id
	 * @param userType			必须传字段		用户类型
	 * @param start							分页的开始页	从0开始
	 * @param limit							每页数量
	 * @param courseCardId					课程卡id
	 * @param homeworkId					文本作业id
	 * @return
	 * @author  lw
	 * @param requestSide 
	 * @date    2016年6月18日 下午9:13:57
	 */
	public String getHomeworks(int userId, Integer start, Integer limit, Integer courseCardId, Integer homeworkId, int inquireType, Integer requestSide) {
		
		/* 查询类型：作业列表 */
		if(GameConstants.TEACHER_HOMEWORK_QUERY_PAGE == inquireType){
			return this.findAllTeachCourseByCard(courseCardId, userId, limit, start, requestSide).toJSONString();
			
		/* 查询类型：文本作业详情页 */
		}else if(GameConstants.TEACHER_HOMEWORK_QUERY_ENTITY == inquireType){
			return this.findTeachCourseEntity(homeworkId, userId).toJSONString();
			
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.TEACHER_HOME_REQUEST_PARAM).toJSONString();
	}

	
	
	
	/**
	 * 根据课程id 获取某个课程详情
	 * @param courseId	课程id
	 * @param userId	用户id
	 * @author 			lw
	 * @date			2016年6月30日  下午2:22:04
	 */
	private JSONObject findTeachCourseEntity(Integer courseId, int userId) {
		
		/*
		 * 校验课程id 存在性
		 */
		if(courseId < 1){
			LogUtil.error(this.getClass(), "findTeachCourseEntity", AppErrorCode.ERROR_COURSE_PPARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_PPARAM);
		}
		
		/*
		 * 组装页面返回消息格式
		 */
		List<TeachRelCardCourse> paramList = new ArrayList<TeachRelCardCourse>();
		JSONObject json = new JSONObject();
		json.put("returnCount", 0);
		json.put("allPage", 	0);
		json.put("currentPage", 0);
		
		/*
		 * 1.通过课程id，获取课程卡课程关系对象
		 */
		TeachRelCardCourse relCardCourse = new TeachRelCardCourse();
		relCardCourse.setCourseId(courseId);
		relCardCourse = teachRelCardCourseService.selectTeachRelCardCourse(relCardCourse);
		
		/*
		 * 获取课程信息
		 */
		if(relCardCourse != null){
			
			TeachCourseCard card = null;
			CenterUser user = null;
			TeachSchool school = null;
			PublicPicture pic = null;
			TeachStudentHomework homeWork = null;
			TeachCourse teachCourse = null;
			
			/*
			 * 查询课程卡 获取：作业开始时间的时间戳、作业结束时间的时间戳
			 */
			String jsonRedis = RedisComponent.findRedisObject(relCardCourse.getCardId(), TeachCourseCard.class);
			if(!StringUtil.isEmpty(jsonRedis)){
				card = JSONObject.parseObject(jsonRedis, TeachCourseCard.class);
			}

			/*
			 * 组装返回对象参数
			 */
			homeWork = new TeachStudentHomework();
			homeWork.setStudentId(userId);
			homeWork.setCourseId(relCardCourse.getCourseId());
			homeWork.setIsDelete(GameConstants.NO_DEL);
			homeWork = homeworkService.selectTeachStudentHomework(homeWork);
			relCardCourse.setHasHandin(homeWork == null ? 0 : 1);
			
			List<TeachRelCardCourse> list = new ArrayList<TeachRelCardCourse>();
			list.add(relCardCourse);
			this.assembleResultParam(list, null, jsonRedis, card, user, school, pic, homeWork, teachCourse);
			
			/*
			 * 保存学习人次
			 * 	通常课程卡课程对象在创建课程的时候会插入数据，这里不做空校验
			 */
			try {
				TeachRelCardCourse reCardCourse = new TeachRelCardCourse();
				reCardCourse.setCourseId(courseId);
				reCardCourse = teachRelCardCourseService.selectTeachRelCardCourse(reCardCourse);
				reCardCourse.setTotalStudyNum(reCardCourse.getTotalStudyNum()+1);
				teachRelCardCourseService.updateTeachRelCardCourse(reCardCourse);
				LogUtil.info(this.getClass(),  "assembleCourse", "课程卡课程学习人次保存/修改成功!");
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "assembleCourse", AppErrorCode.ERROR_TEACH_REL_CARD_COURSE, e);
			}
			
			paramList.add(relCardCourse);
			json.put("homeworks",paramList);
			return Common.getReturn(AppErrorCode.SUCCESS, null, json);
		}
		
		LogUtil.error(this.getClass(), "assembleCourse", AppErrorCode.ERROR_COURSE_PPARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_PPARAM);
		
	}





	/**
	 * 查询作业列表：
	 * 	教师获取某课程卡下所有的课程
	 * 
	 * @param courseCardId	课程卡id
	 * @param userId		用户id
	 * @param limit			
	 * @param start
	 * @author 				lw
	 * @param requestSide 
	 * @date				2016年6月30日  下午2:19:45
	 */
	private JSONObject findAllTeachCourseByCard(Integer courseCardId, int userId, Integer limit, int start, Integer requestSide) {
		
		/* 
		 * 1.通过课程卡id， 获取课程卡
		 * 如果课程卡未查到，抛出异常:获取教师课程卡失败 
		 */
		String redisTmp = RedisComponent.findRedisObject(courseCardId, TeachCourseCard.class);
		if(StringUtil.isEmpty(redisTmp)){
			LogUtil.error(this.getClass(), "findAllTeachCourseByCard",AppErrorCode.GET_CARD_ERROR);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CARD_ERROR);
		}
		TeachCourseCard card = JSONObject.parseObject(redisTmp, TeachCourseCard.class);

		/*
		 * 2.通过分页组件获取分页：QueryPageComponent.queryPage()
		 * 
		 *  2.1查询条件参数：
		 *  	cardId：			课程卡id
		 *  	courseType：		课程类型
		 *  	isDelete：		未删除课程
		 *  	showTypeApp：	前端/后端请求： 
		 *  						前端：显示选修和必修
		 *  						后端：显示全部
		 */
		Map<String ,Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("cardId", courseCardId);
		paramMap.put("courseType", GameConstants.TEACHER_HOMEWORK_TYPE_TEXT);
		paramMap.put("isDelete", GameConstants.NO_DEL);
		if(requestSide == GameConstants.TYPE_FRONT){
			paramMap.put("showTypeApp", GameConstants.TEACHER_HOMEWORK_HIDE);
			
		}
		QueryPage<TeachRelCardCourse> coursePage = QueryPageComponent.queryPage(limit, start, paramMap, TeachRelCardCourse.class);
		
		/*
		 *	3.分页参数组装：
		 *		3.1 查询成功：对分页对象进行参数组装 
		 *		3.2查询失败：返回失败结果
		 */
		if(coursePage.getState()){
			CenterUser user = null;
			TeachSchool school = null;
			PublicPicture pic = null;
			TeachStudentHomework homeWork = null;
			TeachCourse teachCourse = null;
			
			/* 
			 * 分页对象进行参数组装
			 */
			List<Integer> param = new ArrayList<Integer>();
			for(TeachRelCardCourse course : coursePage.getList()){
				param.add(course.getCourseId());
			}
			List<TeachStudentHomework> works = null;
			if(param.size() > 0){
				homeWork = new TeachStudentHomework();
				homeWork.setStudentId(userId);
				homeWork.setIsDelete(GameConstants.NO_DEL);
				homeWork.setCourseIds(StringUtil.ListToString(param, ","));
				works = homeworkService.selectSingleTeachStudentHomework(homeWork);
			}
			
			//组装参数
			this.assembleResultParam(coursePage.getList(), works, redisTmp,card, user, school, pic, homeWork, teachCourse);
		}
		
		/*
		 * 返回分页对象逻辑
		 */
		return coursePage.getMessageJSONObject("homeworks");
		
	}




	/**
	 * 返回参数组装
	 * @param course		课程卡课程关系对象
	 * @param userId		用户id
	 * @param redisTmp		临时对象
	 * @param user			临时对象
	 * @param school		临时对象
	 * @param pic			临时对象
	 * @param homeWork		临时对象
	 * @param teachCourse	临时对象
	 * @author 				lw
	 * @date				2016年7月5日  下午3:06:33
	 */
	private void assembleResultParam(List<TeachRelCardCourse> courses, List<TeachStudentHomework> works, String redisTmp,TeachCourseCard card
			, CenterUser user, TeachSchool school, PublicPicture pic, TeachStudentHomework homeWork, TeachCourse teachCourse) {
		
		for(TeachRelCardCourse course : courses){
			
			/*
			 * 课程时间：
			 * 从课程卡中获取当前课程时间
			 */
			if(card == null){
				course.setHomeworkStartDate(0);
				course.setHomeworkEndDate(0);
			}else{
				course.setHomeworkStartDate(card.getStartTime());
				course.setHomeworkEndDate(card.getEndTime());
			}
			
			/*
			 * 课程：
			 * 从redis中获取课程
			 */
			redisTmp = RedisComponent.findRedisObject(course.getCourseId(), TeachCourse.class);
			if(!StringUtil.isEmpty(redisTmp)){
				teachCourse = JSONObject.parseObject(redisTmp,TeachCourse.class);
				course.setHomeworkBody(teachCourse.getCourseContent());
				course.setHomeworkTitle(teachCourse.getCourseTitle());
				course.setTeacherId(teachCourse.getTeacherId());
				
				/*
				 * 是否已交作业
				 * 临时查询学生文本作业判断提交
				 */
				course.setHasHandin(0);
				if(!CollectionUtils.isEmpty(works)){
					for(int i = 0 ; i < works.size() ; i++){
						if(works.get(i).getCourseId().intValue() == course.getCourseId().intValue()){
							course.setHasHandin(1);
							works.remove(i);
							i--;
							break;
						}
					}
				}
				
				
				/*
				 * 获取教师
				 * 从redis中获取教师课程信息
				 */
				redisTmp = RedisComponent.findRedisObject(course.getTeacherId(), CenterUser.class);
				if(!StringUtil.isEmpty(redisTmp)){
					user = JSONObject.parseObject(redisTmp, CenterUser.class);
					course.setTeacherName(user.getRealName());
					
					/*
					 * 教师所在学校
					 * 从redis中获取
					 */
					redisTmp = RedisComponent.findRedisObject(user.getSchoolId(), TeachSchool.class);
					if(!StringUtil.isEmpty(redisTmp)){
						school = JSONObject.parseObject(redisTmp, TeachSchool.class);
						course.setSchoolName(school.getSchoolName());
					}
					
					/*
					 * 教师头像地址
					 * 从redis中获取
					 */
					redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						course.setTeacherHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
				}
			}
			
		}
			
		
		
		
	}

	
	
}
