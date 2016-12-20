package com.seentao.stpedu.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.common.service.BaseTeachStudentHomeworkService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class TeachStudentHomeworkAppService {

	@Autowired
	private BaseTeachStudentHomeworkService studentHomeworkService;
	@Autowired
	private TeachRelCardCourseService relCardCourseService;
	
	/**
	 * 上传文本作业
	 * @param userId			用户id
	 * @param homeworkId		课程id
	 * @param hSolutionBody		文本作业内容
	 * @param homeworkFileIds	附件ids
	 * @return
	 * @author 					lw
	 * @date					2016年6月24日  上午9:20:11
	 */
	public String submitHomeworkSolution(int userId, int homeworkId, String hSolutionBody, String homeworkFileIds) {
		
		//	1. 查询课程对象
		TeachRelCardCourse cardCourse = new TeachRelCardCourse();
		cardCourse.setCourseId(homeworkId);
		cardCourse = relCardCourseService.selectTeachRelCardCourse(cardCourse);
		if(cardCourse == null){
			LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.ERROR_COURSECARD_OUT_TIME);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSECARD_OUT_TIME).toJSONString();
		}
		
		//	2. 查询课程卡对象
		String cardRedis = RedisComponent.findRedisObject(cardCourse.getCardId(), TeachCourseCard.class);
		if(StringUtil.isEmpty(cardRedis)){
			LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.ERROR_COURSECARD_OUT_TIME);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSECARD_OUT_TIME).toJSONString();
		}
		TeachCourseCard card = JSONObject.parseObject(cardRedis, TeachCourseCard.class);
		
		//	3. 当前时间
		int time = TimeUtil.getCurrentTimestamp();
		
		//	4.	课程卡时间之内学生才能提交作业
		if(card.getStartTime() <= time && card.getEndTime() >= time ){
			
			//	4.1 如果学生已经提交过作业，需要删除之后才能再次提交
			TeachStudentHomework homeWork = new TeachStudentHomework();
			homeWork.setStudentId(userId);
			homeWork.setCourseId(homeworkId);
			homeWork.setIsDelete(GameConstants.NO_DEL);
			TeachStudentHomework entityTmp = studentHomeworkService.selectTeachStudentHomework(homeWork);
			if(entityTmp != null){
				LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.ERROR_COURSECARD_SUBMITTED);
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSECARD_SUBMITTED).toJSONString();
			}
			
			/*
			 * 	4.21  学生提交作业
			 * 	4.22 保存附件关系
			 */
			homeWork.setHomeworkExplain(hSolutionBody);
			homeWork.setSubmitTime(time);
			homeWork.setTeacherId(card.getTeacherId());
			String msg = studentHomeworkService.save(homeWork, homeworkFileIds).toJSONString();
			
			//	4.3 课程卡 课程 关系表 应交作业人数加一 并删除缓存
			relCardCourseService.addActualnum(homeworkId);
			
			return msg;
		}
		
		LogUtil.error(this.getClass(), "submitHomeworkSolution", AppErrorCode.ERROR_HOMEWORK_SUBMIT_BODY);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_HOMEWORK_SUBMIT_BODY).toJSONString();
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
