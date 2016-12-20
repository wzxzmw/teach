package com.seentao.stpedu.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.dao.TeachRelCardCourseDao;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class TeachRelCardCourseService{
	
	@Autowired
	private TeachRelCardCourseDao teachRelCardCourseDao;
	
	public List<TeachRelCardCourse> getTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse) {
		List<TeachRelCardCourse> teachRelCardCourseList = teachRelCardCourseDao .selectSingleTeachRelCardCourse(teachRelCardCourse);
		/*if(teachRelCardCourseList == null || teachRelCardCourseList .size() <= 0){
			return null;
		}*/
		
		return teachRelCardCourseList;
	}
	
	public TeachRelCardCourse selectTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse){
		return teachRelCardCourseDao.selectTeachRelCardCourse(teachRelCardCourse);
	}

	public void updateTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse) {
		teachRelCardCourseDao.updateTeachRelCardCourseByKey(teachRelCardCourse);
	}

	public void insertTeachRelCardCourse(TeachRelCardCourse teachRelCardCourse) {
		teachRelCardCourseDao.insertTeachRelCardCourse(teachRelCardCourse);
	}

	public Integer queryCount(Map<String, Object> paramMap) {
		return teachRelCardCourseDao.queryCount(paramMap) ;
	}

	/**
	 * 课程人数加一
	 * @param courseId
	 * @author 				lw
	 * @date				2016年6月30日  上午10:01:32
	 */
	public void addActualnum(int courseId) {
		//课程卡课程关系表 人数加一
		teachRelCardCourseDao.addActualnum(courseId);
		//获取缓存对象id 并删除缓存
		TeachRelCardCourse cardCourse = new TeachRelCardCourse();
		cardCourse.setCourseId(courseId);
		cardCourse = this.selectTeachRelCardCourse(cardCourse);
		if(cardCourse != null){
			JedisCache.delRedisVal(TeachRelCardCourse.class.getSimpleName(), String.valueOf(cardCourse.getRelId()));
		}
	}

	public TeachRelCardCourse getLastTeachRelCardCourseByCardId(Integer cardId) {
		TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
		teachRelCardCourse.setCardId(cardId);
		return teachRelCardCourseDao.getLastTeachRelCardCourseByCardId(teachRelCardCourse);
	}

	public Integer getStudyNumBycourseCardId(Integer cardId) {
		return teachRelCardCourseDao.getStudyNumBycourseCardId(cardId);
	}

	public void updateTeachRelCardCoursePrivate(TeachRelCardCourse teachRelCardCourse) {
		teachRelCardCourseDao.updateTeachRelCardCoursePrivate(teachRelCardCourse);
	}

	public void insertTeachRelCardCourseAlls(List<TeachRelCardCourse> list) {
		if(list != null && list.size() > 0){
			teachRelCardCourseDao.insertTeachRelCardCourseAlls(list);
		}else{
			LogUtil.error(this.getClass(), "insertTeachRelCardCourseAlls","不能传null");
		}
	}

	public void updateTeachRelCardCoursePrivateAlls(String ids) {
		teachRelCardCourseDao.updateTeachRelCardCoursePrivateAlls(ids);
	}

	public void updateTeachRelCardCoursePrivateMinus(String ids) {
		teachRelCardCourseDao.updateTeachRelCardCoursePrivateMinus(ids);
	}

	public TeachRelCardCourse getSingleTeachRelCardCourse(TeachRelCardCourse teachrelcardcourses) {
		return teachRelCardCourseDao.getSingleTeachRelCardCourse(teachrelcardcourses);
	}
	
	/**
	 * 根据课程ID查询
	 * @param courseId 课程ID
	 * @return
	 * @author chengshx
	 */
	public JSONObject selectTeachRelCardCourse(Integer courseCardId, Integer courseId){
//		String key = courseCardId + "_" + courseId;
//		String jsonStr = JedisCache.getRedisVal(null, GameConstants.REDIS_COURSE_CARD, key);
//		if(jsonStr == null){
			TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
			teachRelCardCourse.setCardId(courseCardId);
			teachRelCardCourse.setCourseId(courseId);
			teachRelCardCourse = teachRelCardCourseDao.selectTeachRelCardCourse(teachRelCardCourse);
//			jsonStr = JSONObject.toJSONString(teachRelCardCourse);
//			JedisCache.setRedisVal(null, GameConstants.REDIS_COURSE_CARD, key, jsonStr);
//		}
//		return JSONObject.parseObject(jsonStr);
			return (JSONObject) JSONObject.toJSON(teachRelCardCourse);
	}

}