package com.seentao.stpedu.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseCardCount;
import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachCourseCardCountService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseChapterService;
import com.seentao.stpedu.common.service.TeachRelStudentAttachmentService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

@Component
public class TimerTaskService{
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	@Autowired
	private TeachCourseCardCountService teachCourseCardCountService;
	@Autowired
	private TeachRelStudentAttachmentService teachRelStudentAttachmentService;
	@Autowired
	private CenterUserService centerUserService;
	
	private String COUNT_KEY = "TeachCourseCardCount";

	/**
	 * 定时启动。每天 00:00 执行一次
	 * s M h d m w y
	 */
//	@Scheduled(cron = "0 0 0 * * *")
	public void autoTask(){
		Random rand = new Random();
		Integer time = rand.nextInt(100) + 20;
		try {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e1) {
				LogUtil.error("", e1);
			}
			String isWorking = JedisCache.getRedis(COUNT_KEY);
			if(isWorking != null && Integer.valueOf(isWorking).intValue() == 1){
				LogUtil.info(this.getClass(), "autoTask", "已在执行统计课程卡信息");
				return;
			}
			JedisCache.setRedis(COUNT_KEY, "1");
			LogUtil.info(this.getClass(), "autoTask", "统计课程卡信息开始执行");
			// 将视频时长写入到数据库
			List<TeachRelStudentAttachment> list = new ArrayList<TeachRelStudentAttachment>();
			Map<String, String> map = JedisCache.getRedisMap(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_ATTA);
			for(Map.Entry<String, String> me : map.entrySet()){
				String val = me.getValue();
				TeachRelStudentAttachment t = JSONObject.toJavaObject(JSONObject.parseObject(val), TeachRelStudentAttachment.class);
				list.add(t);
			}
			if(list.size() > 0){
				teachRelStudentAttachmentService.batchUpdateTeachRelStudentAttachment(list);
			}
			// 删除个人学时统计缓存
			JedisCache.delRedisMap(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_COUNT);
			// 【班级表】获取所有未删除班级ID
			List<Integer> classIds = teachClassService.getAllClassId();
			// 【课程章节表】获取所有班级的章节ID
			for(Integer classId : classIds){
				Integer notSetNum = 0; // 未设置数量
				Integer notStartNum = 0; // 未开始数量
				Integer notEndNum = 0; // 进行中数量
				Integer completeNum = 0; // 已结束数量
				Integer totalCompleteChapter = 0; // 总完成章节
				Integer totalCompleteHours = 0; // 总完成课时
				// 统计班级总学时
				// 获取班级下的所有学生
				List<CenterUser> userList = centerUserService.selectCenterUserByClassId(classId);
				for(CenterUser u : userList){
					Integer userId = u.getUserId();
					// 获取学生的所有课时
					Integer hours = teachRelStudentAttachmentService.selectStudentCourseHour(userId);
					totalCompleteHours += hours;
				}
				List<Integer> chapterIds = teachCourseChapterService.getAllChapetIdByClass(classId);
				if(chapterIds == null) chapterIds = new ArrayList<Integer>();
				for(Integer chapterId : chapterIds){
					// 章节是否完成标识
					Boolean isComplete = true;
					// 【课程卡表】获取章节的课程卡
					TeachCourseCard teachCourseCard = new TeachCourseCard();
					teachCourseCard.setChapterId(chapterId);
					List<TeachCourseCard> cards = teachCourseCardService.getTeachCourseCard(teachCourseCard);
					if(cards == null) cards = new ArrayList<TeachCourseCard>();
					Integer currentTime = (int) (System.currentTimeMillis() / 1000);
					// 根据课程卡开始时间和结束时间更新课程卡状态
					for(TeachCourseCard card : cards){
						Integer startTime = card.getStartTime(); // 开始时间
						Integer endTime = card.getEndTime(); // 结束时间
						int status = card.getStatusId().intValue();
						if(status == GameConstants.CARD_STATUS_0){// 未设置数量
							notSetNum++;
						} else if (status == GameConstants.CARD_STATUS_1){// 未开始
							// 判断是否开始或是否结束
							if(currentTime >= startTime && currentTime < endTime){ // 进行中
								card.setStatusId(GameConstants.CARD_STATUS_2);
								notEndNum++;
							} else if (currentTime >= endTime){ // 已结束
								card.setStatusId(GameConstants.CARD_STATUS_3);
								completeNum++;
							} else {// 未开始
								notStartNum++;
							}
						} else if (status == GameConstants.CARD_STATUS_2){// 进行中
							// 判断是否结束
							if(currentTime >= endTime){ // 已结束
								card.setStatusId(GameConstants.CARD_STATUS_3);
								completeNum++;
							} else {
								notEndNum++;
							}
						} else if (status == GameConstants.CARD_STATUS_3){// 已结束
							completeNum++;
						}
						if(card.getStatusId().intValue() != GameConstants.CARD_STATUS_3){
							isComplete = false;
						} 
						/**else {
							Integer hours = endTime - startTime;
							totalCompleteHours += hours;
						}*/
						teachCourseCardService.updateCourseCard(card);
					}
					if(isComplete) totalCompleteChapter++; // 章节完成
				}
				// 计算班级课程卡统计信息
				// 【课程卡统计表】统计信息不存在新增统计信息
				TeachCourseCardCount teachCourseCardCount = teachCourseCardCountService.getTeachCourseCardCountByClassId(classId);
				Boolean isSave = false;
				if(teachCourseCardCount == null){
					isSave = true;
					teachCourseCardCount = new TeachCourseCardCount();
				}
				// 【课程卡统计表】统计信息存在修改统计信息
				teachCourseCardCount.setClassId(classId);
				teachCourseCardCount.setNotSetNum(notSetNum);
				teachCourseCardCount.setNotStartNum(notStartNum);
				teachCourseCardCount.setNotEndNum(notEndNum);
				teachCourseCardCount.setCompleteNum(completeNum);
				teachCourseCardCount.setTotalCompleteChapter(totalCompleteChapter);
				teachCourseCardCount.setTotalCompleteHours(totalCompleteHours);
				try {
					if(isSave){ // 新增
						teachCourseCardCountService.addTeachCourseCardCount(teachCourseCardCount);
					} else { // 更新
						teachCourseCardCountService.updateTeachCourseCountByClassId(teachCourseCardCount);
					}
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "autoTask", "修改课程卡统计信息出错", e);
				}
			}
			LogUtil.info(this.getClass(), "autoTask", "统计课程卡信息执行完成");
		} finally {
			JedisCache.setRedis(COUNT_KEY, "0");
		}
	}
}
