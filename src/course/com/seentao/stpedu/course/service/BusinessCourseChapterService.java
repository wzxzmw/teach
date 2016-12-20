package com.seentao.stpedu.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachCourseChapterService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

/** 
* @author yy
* @date 2016年6月18日 上午11:51:27 
*/
@Service
public class BusinessCourseChapterService {
	
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	@Autowired
	private TeachClassService teachClassService;
	
	/**
	 * 接口id--12
	 * 根据班级id获取章节信息
	 * @param calssId
	 * @param type 类型：1、PC端  2、手机端
	 */
	public String getChapterByClassId(Integer calssId, int type) {
		LogUtil.info(this.getClass(), "getChapterByClassId","接口开始调用calssId="+calssId);
		JSONObject jo = new JSONObject();
		try {
			//班级是否存在
			TeachClass teachClass = teachClassService.getTeachClassById(calssId);
			if(null!=teachClass){
				//String redisString = JedisCache.getRedis(TeachCourseChapter.class.getSimpleName()+"classId"+calssId);
				TeachCourseChapter teachCourseChapter = new TeachCourseChapter();
				teachCourseChapter.setClassId(calssId);
				List<TeachCourseChapter> teachCourseChapterList = null;
				if(type==1){
					teachCourseChapterList = teachCourseChapterService.getChapter(teachCourseChapter);
				}else{
					teachCourseChapterList = teachCourseChapterService.getChapterForMobile(teachCourseChapter);
					//比赛经营年是否为初始年
					if(teachCourseChapterList!=null && teachCourseChapterList.size()>0){
						for (TeachCourseChapter teachCourseChapter2 : teachCourseChapterList) {
							if(null == teachCourseChapter2.getMatchId() || null == teachCourseChapter2.getMatchYear()){
								teachCourseChapter2.setIsTheFirstYear(0);
							}else{
								if(teachCourseChapter2.getMatchYear() == 1){
									teachCourseChapter2.setIsTheFirstYear(1);
								}else{
									teachCourseChapter2.setIsTheFirstYear(0);
								}
							}
						}
					}
				}
				//班级下是否存在章节信息
				if(null!=teachCourseChapterList && teachCourseChapterList.size()!=0){
					//JedisCache.setRedis(TeachCourseChapter.class.getSimpleName()+"classId"+calssId, JSONObject.toJSONString(teachCourseChapterList));
					jo.put("chapters", teachCourseChapterList);
					if(type==2){
						//比赛id
						Integer matchId = teachCourseChapterList.get(1).getMatchId();
						jo.put("matchId", matchId);
						JSONObject josn = new JSONObject();
						josn.put("result",jo);
						josn.put("code",AppErrorCode.SUCCESS);
						return josn.toJSONString();
					}else{
						LogUtil.info(this.getClass(), "getChapterByClassId","获取章节信息成功");
						return Common.getReturn(AppErrorCode.SUCCESS, "", jo).toJSONString();
					}
				}else{
					LogUtil.error(this.getClass(), "getChapterByClassId","该班级下没有章节信息存在");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TEACHER_COURSE_CHAPTER).toJSONString();
				}
			}else{
				LogUtil.error(this.getClass(), "getChapterByClassId","班级不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TEACHER_CLASS).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "getChapterByClassId","根据班级查询章节信息异常",e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TEACHER_CLASS).toJSONString();
		}
	}
	
}


