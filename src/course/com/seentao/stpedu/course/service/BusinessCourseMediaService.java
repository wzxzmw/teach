package com.seentao.stpedu.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentCourse;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.common.service.TeachRelClassCourseService;
import com.seentao.stpedu.common.service.TeachRelCourseAttachmentService;
import com.seentao.stpedu.common.service.TeachRelStudentAttachmentService;
import com.seentao.stpedu.common.service.TeachRelStudentCourseService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;

/**
 * @author chengshx
 */
@Service
public class BusinessCourseMediaService {

	@Autowired
	private TeachRelCourseAttachmentService teachRelCourseAttachmentService;
	@Autowired
	private TeachRelClassCourseService teachRelClassCourseService;
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	@Autowired
	private TeachRelStudentCourseService teachRelStudentCourseService;
	@Autowired
	private TeachRelStudentAttachmentService teachRelStudentAttachmentService;
	
	/**
	 * 提交视频播放时间
	 * @param userId 用户id
	 * @param currentTimes 播放时间(单位：秒)
	 * @param courseId 课程id
	 * @param courseCardId 所属课程卡id
	 * @param classType 1:教学班；2:俱乐部培训班；
	 * @param attachmentId 附件id
	 * @return
	 * @author chengshx
	 */
	public JSONObject submitVideoViewTime(String userId, String currentTimes, String courseId, String courseCardId, Integer classType, String attachmentId){
		// 判断学生附件关系是否存在
		TeachRelStudentAttachment teachRelStudentAttachment = teachRelStudentAttachmentService.selectTeachRelStudentAttachment(Integer.valueOf(userId), Integer.valueOf(attachmentId));
		if(teachRelStudentAttachment == null){
			// 不存在插入记录
			teachRelStudentAttachment = new TeachRelStudentAttachment();
			teachRelStudentAttachment.setStudentId(Integer.valueOf(userId));
			teachRelStudentAttachment.setAttaId(Integer.valueOf(attachmentId));
			teachRelStudentAttachment.setIsStudy(0);
			teachRelStudentAttachment.setCourseHour(0);
			teachRelStudentAttachmentService.insertTeachRelStudentAttachment(teachRelStudentAttachment);
		}
		// 判断课程是否必修
		Integer longestViewTimes = 0;
		JSONObject courseObj = null;
//		if(courseCardId == null || courseCardId.trim().equals("") || courseCardId.equals("null")){// 班级下的视频，案例课程不统计
//			courseObj = teachRelClassCourseService.selectTeachRelClassCourse(classId, Integer.valueOf(courseId));
//		} else { //课程卡下的视频
		if(courseCardId != null && !courseCardId.equals("") && !courseCardId.equals("null")){
			courseObj = teachRelCardCourseService.selectTeachRelCardCourse(Integer.valueOf(courseCardId), Integer.valueOf(courseId));
		}
//		}
		// 非必修课程不做统计
		/*if(courseObj.getInteger("showType") != 1){
			JSONObject result = new JSONObject();
			result.put("longestViewTimes", longestViewTimes);
			return Common.getReturn(0, "", result);
		}*/
		// 获取视频的总时长
		String attachmentStr = RedisComponent.findRedisObject(Integer.valueOf(attachmentId), PublicAttachment.class);
		PublicAttachment publicAttachment = JSONObject.toJavaObject(JSONObject.parseObject(attachmentStr), PublicAttachment.class);
		Integer timeLength = publicAttachment.getTimeLength();
		Integer currentTime = Integer.valueOf(currentTimes);
		// 判断是否已学习
		if(teachRelStudentAttachment.getIsStudy() == 1){ // 已学习
			longestViewTimes = timeLength;
		} else { //未学习
			// 判断时长是否大于等于80%，案例课程不统计
			if(currentTime >= timeLength * 0.8){
				// 修改视频状态为已学习
				teachRelStudentAttachment.setIsStudy(1);
				if(courseCardId != null && !courseCardId.equals("") && !courseCardId.equals("null") && courseObj.getInteger("showType") == 1){
					teachRelStudentAttachment.setCourseHour(timeLength);
				}
				teachRelStudentAttachmentService.updateTeachRelStudentAttachment(1, teachRelStudentAttachment);
				// 删除个人学时统计缓存
				JedisCache.delRedisMap(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_COUNT);
				longestViewTimes = timeLength;
				// 判断课程下的所有必修视频是否已学习
				// 获取课程下的所有视频
				TeachRelCourseAttachment courseAttachment = new TeachRelCourseAttachment();
				courseAttachment.setCourseId(Integer.valueOf(courseId));
				List<TeachRelCourseAttachment> attaList = teachRelCourseAttachmentService.selectSingleTeachRelCourseAttachment(courseAttachment);
				StringBuilder attaIds = new StringBuilder();
				
				for(int i = 0; i < attaList.size(); i++){
					TeachRelCourseAttachment t = attaList.get(i);
					if(i < (attaList.size() - 1)){
						attaIds.append(t.getAttaId() + ",");
					}else{
						attaIds.append(t.getAttaId());
					}
				}
				// 查询学生课程附件关系是否都存在
				List<TeachRelStudentAttachment> stuList = teachRelStudentAttachmentService.selectTeachRelStudentAttachmentByIds(Integer.valueOf(userId), attaIds.toString());
				// 判断课程的视频是否都已学习
				Integer isStudy = attaList.size() == stuList.size() ? 1 : 0;
				TeachRelStudentCourse stuCourse = new TeachRelStudentCourse();
				stuCourse.setStudentId(Integer.valueOf(userId));
				stuCourse.setCourseId(Integer.valueOf(courseId));
				stuCourse = teachRelStudentCourseService.getTeachRelStudentCourse(stuCourse);
				if(isStudy == 1 && stuCourse != null && stuCourse.getIsStudy() != 1){// 都已学习
					// 修改课程累计学习人次+1
					if(courseCardId == null || courseCardId.trim().equals("") || courseCardId.equals("null")){//该课程是案例课程
						// 获取用户班级ID
						String centerUserStr = RedisComponent.findRedisObject(Integer.valueOf(userId), CenterUser.class);
						JSONObject userObj = JSONObject.parseObject(centerUserStr);
						CenterUser centerUser = JSONObject.toJavaObject(userObj, CenterUser.class);
						Integer classId = centerUser.getClassId();
						TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
						teachRelClassCourse.setClassId(classId);
						teachRelClassCourse.setCourseId(Integer.valueOf(courseId));
						teachRelClassCourse.setTotalStudyNum(0);//该字段次数会自动+1
						teachRelClassCourseService.updateTeachRelClassCourse(teachRelClassCourse);
					}else{//该课程是基础课程
						TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
						teachRelCardCourse.setCardId(Integer.valueOf(courseCardId));
						teachRelCardCourse.setCourseId(Integer.valueOf(courseId));
						teachRelCardCourse.setTotalStudyNum(0);//该字段次数会自动+1
						teachRelCardCourseService.updateTeachRelCardCourse(teachRelCardCourse);
					}
					// 修改课程为已学习
					TeachRelStudentCourse teachRelStudentCourse = new TeachRelStudentCourse();
					teachRelStudentCourse.setCourseId(Integer.valueOf(courseId));
					teachRelStudentCourse.setStudentId(Integer.valueOf(userId));
					teachRelStudentCourse.setIsStudy(1);
					teachRelStudentCourseService.updateTeachRelStudentCourse(teachRelStudentCourse);
				}
			} else {
				// 当前观看时长大于历史观看时长更新记录，案例课程不统计
				if(currentTime > teachRelStudentAttachment.getCourseHour() && courseCardId != null && !courseCardId.equals("") && !courseCardId.equals("null") && courseObj.getInteger("showType") == 1){
					if(currentTime > timeLength){
						currentTime = timeLength;
					}
					teachRelStudentAttachment.setCourseHour(currentTime);
					teachRelStudentAttachmentService.updateTeachRelStudentAttachment(0, teachRelStudentAttachment);
				}
				longestViewTimes = currentTime;
			}
		}
		JSONObject result = new JSONObject();
		result.put("longestViewTimes", longestViewTimes);
		return Common.getReturn(0, "", result);
	}
}
