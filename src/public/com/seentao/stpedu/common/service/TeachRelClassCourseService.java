package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachRelClassCourseDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;

@Service
public class TeachRelClassCourseService{
	
	@Autowired
	private TeachRelClassCourseDao teachRelClassCourseDao;
	@Autowired
	private BaseTeachCourseService baseTeachCourseService;
	
	public List<TeachRelClassCourse> getTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse) {
		List<TeachRelClassCourse> teachRelClassCourseList = teachRelClassCourseDao .selectSingleTeachRelClassCourse(teachRelClassCourse);
		
		return teachRelClassCourseList;
	}
	
	public TeachRelClassCourse getTeachRelClassCourseOne(TeachRelClassCourse teachRelClassCourse) {
		List<TeachRelClassCourse> teachRelClassCourseList = teachRelClassCourseDao .selectSingleTeachRelClassCourse(teachRelClassCourse);
		if(teachRelClassCourseList == null || teachRelClassCourseList .size() <= 0){
			return null;
		}
		return teachRelClassCourseList.get(0);
	}

	public void updateTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse) {
		teachRelClassCourseDao.updateTeachRelClassCourseByKey(teachRelClassCourse);
	}

	public Integer getCourseByClassIdCount(Map<String, Object> paramMap) {
		return teachRelClassCourseDao.getCourseByClassIdCount(paramMap);
	}
	
	public void insertTeachRelClassCourse(TeachRelClassCourse teachRelClassCourse){
		teachRelClassCourseDao.insertTeachRelClassCourse(teachRelClassCourse);
	}

	public JSON findTeachCourseByChapterAndCcType(Map<String, Object> map, Integer start, Integer limit, 
			Integer teachingRole, Integer classType, Integer userId, Integer ccType) {
		TeachRelCardCourse reCourse = null;
		String redisData = null;
		QueryPage<TeachRelClassCourse> queryPage = QueryPageComponent.queryPage(limit, start, map, TeachRelClassCourse.class);
		List<TeachRelCardCourse> list = new ArrayList<TeachRelCardCourse>();
		
		if(queryPage.getState()){
			CenterUser teacher = null;
			PublicPicture teacherPic = null;
			TeachSchool school = null;
			if( queryPage.getList().size() > 0 ){
				
				//返回对象参数瓶装
				for(TeachRelClassCourse en : queryPage.getList()){
					reCourse = new TeachRelCardCourse();
					reCourse.setCourseId(en.getCourseId());
					reCourse.setTotalStudyNum(en.getTotalStudyNum());
					reCourse.setShowType(en.getShowType());
					list.add(reCourse);
				}
				
				//获取课程
				redisData = RedisComponent.findRedisObject(queryPage.getList().get(0).getCourseId(), TeachCourse.class);
				TeachCourse course = null;
				if(redisData != null){
					course = JSONObject.parseObject(redisData, TeachCourse.class);
					
					//获取教师
					redisData = RedisComponent.findRedisObject(course.getTeacherId() , CenterUser.class);
					if(redisData != null ){
						teacher = JSONObject.parseObject(redisData, CenterUser.class);
						
						//教师图片地址
						redisData = RedisComponent.findRedisObject(teacher.getUserId() , PublicPicture.class);
						if(redisData != null){
							teacherPic = JSONObject.parseObject(redisData, PublicPicture.class);
						}
						
						//教师学校
						redisData = RedisComponent.findRedisObject(teacher.getSchoolId() , TeachSchool.class);
						if(redisData != null){
							school = JSONObject.parseObject(redisData, TeachSchool.class);
						}
					}
				}
				
			}
			
			baseTeachCourseService.assembleCourse(list, classType, userId, teacher, teacherPic, school, ccType);
		}
		map.clear();
		map.put("teachingRole", teachingRole);
		JSONObject json = queryPage.getMessageJSONObject("courses", map);
		JSONObject result = json.getJSONObject(GameConstants.JSONAPI_KEY);
		result.remove("courses");
		result.put("courses", list);
		return json;
	}

	public void insertTeachRelClassCourseAll(List<TeachRelClassCourse> teachRelClassList) {
		if(teachRelClassList != null && teachRelClassList.size() > 0){
			teachRelClassCourseDao.insertTeachRelClassCourseAll(teachRelClassList);
		}
	}
	
	/**
	 * 根据课程ID查询
	 * @param courseId 课程ID
	 * @return
	 * @author chengshx
	 */
	public JSONObject selectTeachRelClassCourse(Integer classId, Integer courseId){
		String key = classId + "_" + courseId;
		String jsonStr = JedisCache.getRedisVal(null, GameConstants.REDIS_COURSE_CLASS, key);
		if(jsonStr == null){
			TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
			teachRelClassCourse.setClassId(classId);
			teachRelClassCourse.setCourseId(courseId);
			teachRelClassCourse = teachRelClassCourseDao.selectTeachRelClassCourse(teachRelClassCourse);
			jsonStr = JSONObject.toJSONString(teachRelClassCourse);
			JedisCache.setRedisVal(null, GameConstants.REDIS_COURSE_CLASS, key, jsonStr);
		}
		return JSONObject.parseObject(jsonStr);
	}
}