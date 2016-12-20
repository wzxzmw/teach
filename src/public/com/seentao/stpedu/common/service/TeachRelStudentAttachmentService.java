package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.dao.TeachRelStudentAttachmentDao;
import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;

@Service
public class TeachRelStudentAttachmentService{
	
	@Autowired
	private TeachRelStudentAttachmentDao teachRelStudentAttachmentDao;
	
	/**
	 * 通过学生Id和附件Id获取学生附件关系
	 * @param studentId 学生ID
	 * @param attaId 附件ID
	 * @return
	 * @author chengshx
	 */
	public TeachRelStudentAttachment selectTeachRelStudentAttachment(Integer studentId, Integer attaId){
		String key = studentId + "_" + attaId;
		String jsonStr = JedisCache.getRedisVal(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_ATTA, key);
		if(jsonStr == null){
			TeachRelStudentAttachment teachRelStudentAttachment = new TeachRelStudentAttachment();
			teachRelStudentAttachment.setStudentId(studentId);
			teachRelStudentAttachment.setAttaId(attaId);
			teachRelStudentAttachment = teachRelStudentAttachmentDao.selectSingleTeachRelStudentAttachment(teachRelStudentAttachment);
			if(teachRelStudentAttachment == null){
				return null;
			}
			jsonStr = JSONObject.toJSONString(teachRelStudentAttachment);
			JedisCache.setRedisVal(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_ATTA, key, jsonStr);
		}
		return JSONObject.parseObject(jsonStr, TeachRelStudentAttachment.class);
	}
	
	public void insertTeachRelStudentAttachment(TeachRelStudentAttachment teachRelStudentAttachment){
		teachRelStudentAttachmentDao.insertTeachRelStudentAttachment(teachRelStudentAttachment);
	}
	
	
	/**
	 * 更新缓存
	 * @param flag 0:修改缓存，1:修改数据库
	 * @param teachRelCourseAttachment
	 * @author chengshx
	 */
	public void updateTeachRelStudentAttachment(Integer flag, TeachRelStudentAttachment teachRelStudentAttachment){
		Integer studentId = teachRelStudentAttachment.getStudentId();
		Integer attId = teachRelStudentAttachment.getAttaId();
		String key = studentId + "_" + attId;
		String jsonStr = JSONObject.toJSONString(teachRelStudentAttachment);
		JedisCache.setRedisVal(GameConstants.COURSE_PRE, GameConstants.REDIS_COURSE_ATTA, key, jsonStr);
		if(flag == 1){
			teachRelStudentAttachmentDao.updateTeachRelStudentAttachmentByKey(teachRelStudentAttachment);
		}
	}
	
	public List<TeachRelStudentAttachment> selectTeachRelStudentAttachmentList(TeachRelStudentAttachment teachRelStudentAttachment){
		return teachRelStudentAttachmentDao.selectSingleTeachRelStudentAttachments(teachRelStudentAttachment);
	}
	
	/**
	 * 获取学生附件关系
	 * @param attaIds 附件ID
	 * @return
	 * @author chengshx
	 */
	public List<TeachRelStudentAttachment> selectTeachRelStudentAttachmentByIds(Integer studentId, String attaIds){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("attaIds", attaIds);
		return teachRelStudentAttachmentDao.selectTeachRelStudentAttachmentByIds(map);
	}
	
	public void batchUpdateTeachRelStudentAttachment(List<TeachRelStudentAttachment> list){
		teachRelStudentAttachmentDao.batchUpdateTeachRelStudentAttachment(list);
	}
	
	/**
	 * 获取学生总课时
	 * @param studentId 学生ID
	 * @return
	 * @author chengshx
	 */
	public Integer selectStudentCourseHour(Integer studentId){
		return teachRelStudentAttachmentDao.selectStudentCourseHour(studentId);
	}
	
}