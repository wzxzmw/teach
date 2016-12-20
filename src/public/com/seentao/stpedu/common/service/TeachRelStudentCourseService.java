package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.dao.TeachCourseDao;
import com.seentao.stpedu.common.dao.TeachRelCourseAttachmentDao;
import com.seentao.stpedu.common.dao.TeachRelStudentAttachmentDao;
import com.seentao.stpedu.common.dao.TeachRelStudentCourseDao;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachRelStudentCourse;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUtil;

import redis.clients.jedis.Jedis;

@Service
public class TeachRelStudentCourseService{
	
	@Autowired
	private TeachRelStudentCourseDao teachRelStudentCourseDao;
	@Autowired
	private TeachRelCourseAttachmentDao teachRelCourseAttachmentDao;
	@Autowired
	private TeachCourseDao teachCourseDao;
	@Autowired
	private TeachRelStudentAttachmentDao teachRelStudentAttachmentDao;
	
	public TeachRelStudentCourse getTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse) {
		List<TeachRelStudentCourse> teachRelStudentCourseList = teachRelStudentCourseDao .selectSingleTeachRelStudentCourse(teachRelStudentCourse);
		if(teachRelStudentCourseList == null || teachRelStudentCourseList .size() <= 0){
			return null;
		}
		return teachRelStudentCourseList.get(0);
	}

	public void insertTeachRelStudentCourse(TeachRelStudentCourse insertTeachRelStudentCourse) {
		teachRelStudentCourseDao.insertTeachRelStudentCourse(insertTeachRelStudentCourse);
	}
	
	/**
	 * 获取学生课程统计信息
	 * @param userId 用户ID
	 * @param classId 班级ID
	 * @return {"completedCourses":0, "allCourses":0}
	 * @author chengshx
	 */
	public JSONObject selectTeachRelStudentCourseCount(Integer classId, Integer userId){
		String key = classId + "_" + userId;
		String jsonStr = JedisCache.getRedisVal(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_COUNT, key);
		if(jsonStr == null){
			TeachRelStudentCourse teachRelStudentCourse = new TeachRelStudentCourse();
			teachRelStudentCourse.setCourseId(userId);
			List<TeachCourse> listAll = teachCourseDao.selectAllCourse(classId);
			teachRelStudentCourse.setIsStudy(1);
			List<TeachRelStudentCourse> listEnd = teachRelStudentCourseDao.selectStudiedCourse(classId, userId);
			Integer learningSeconds = teachRelStudentAttachmentDao.selectStudentCourseHour(userId);
			JSONObject json = new JSONObject();
			json.put("completedCourses", listEnd == null ? 0 : listEnd.size());
			json.put("allCourses", listAll == null ? 0 : listAll.size());
			json.put("learningSeconds", learningSeconds == null ? 0 : learningSeconds);
			JedisCache.setRedisVal(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_COUNT, key, json.toJSONString());
			jsonStr = json.toJSONString();
		}
		return JSONObject.parseObject(jsonStr);
	}
	
	/**
	 * 删除学生课程统计信息记录
	 * @param classId 学生所在班级ID
	 * @author chengshx
	 */
	public void updateTeachRelStudentCourseCount(Integer classId){
		String key = String.valueOf(classId) + "_";
		Jedis jedis = JedisUtil.getJedis();
		Set<String> keys = jedis.hkeys(GameConstants.COURSE_PRE + GameConstants.REDIS_COURSE_COUNT);
		JedisUtil.release(jedis);
		List<String> list = new ArrayList<String>();
		for(String s : keys){
			if(s.startsWith(key)){
				list.add(s);
			}
		}
		if(list.size()>0){
			JedisCache.delRedisValWithPrefix(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_COUNT, list.toArray(new String[0]));
		}
	}
	
	
	public void updateTeachRelStudentCourse(TeachRelStudentCourse teachRelStudentCourse){
		teachRelStudentCourseDao.updateTeachRelStudentCourse(teachRelStudentCourse);
	}
	
}