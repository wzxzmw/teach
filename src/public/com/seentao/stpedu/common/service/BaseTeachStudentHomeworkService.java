package com.seentao.stpedu.common.service;

import java.util.ArrayList;
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
import com.seentao.stpedu.common.dao.TeachStudentHomeworkDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachRelHomeworkAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class BaseTeachStudentHomeworkService{
	
	@Autowired
	private TeachStudentHomeworkDao teachStudentHomeworkDao;
	@Autowired
	private TeachRelHomeworkAttachmentService relHomeworkAttachmentService;
	@Autowired
	private TeachRelStudentClassService relStudentClassService;
	@Autowired
	private TeachRelTeacherClassService	teachRelTeacherClassService;
	
	public TeachStudentHomework selectTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		return teachStudentHomeworkDao .selectTeachStudentHomework(teachStudentHomework);
	}
	
	public List<TeachStudentHomework> selectSingleTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		return teachStudentHomeworkDao .selectSingleTeachStudentHomework(teachStudentHomework);
	}
	
	public TeachStudentHomework getTeachStudentHomework(TeachStudentHomework teachStudentHomework) {
		List<TeachStudentHomework> teachStudentHomeworkList = teachStudentHomeworkDao .selectSingleTeachStudentHomework(teachStudentHomework);
		if(teachStudentHomeworkList == null || teachStudentHomeworkList .size() <= 0){
			return null;
		}
		
		return teachStudentHomeworkList.get(0);
	}
	
	public TeachStudentHomework selectTeachStudentHomework(Integer id){
		TeachStudentHomework teachStudentHomework = new TeachStudentHomework();
		List<TeachStudentHomework> teachStudentHomeworkList = teachStudentHomeworkDao .selectSingleTeachStudentHomework(teachStudentHomework);
		if(CollectionUtils.isEmpty(teachStudentHomeworkList)){
			return null;
		}
		return teachStudentHomeworkList.get(0);
	}
	
	
	
	
	/**
	 * 查询所有学生文本信息，包含学生姓名，附件信息
	 * @param ids
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  上午10:25:36
	 */
	public List<TeachStudentHomework> findAllStudentsHomeWorkStudent(String ids){
		
		return teachStudentHomeworkDao.findAllStudentsHomeWorkStudent(ids);
	}
	
	/**
	 * 查询教师信息
	 * @param userId
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午6:44:14
	 */
	public TeachStudentHomework findTeacherInfo(Integer userId){
		return teachStudentHomeworkDao.findTeacherInfo(userId);
	}
	
	

	/**
	 * 分页查询学生回答的文本作业信息
	 * @param paramMap
	 * @param courseId
	 * @param start
	 * @param limit
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午4:44:29
	 */
	public String findAllStudentsHomeWorkExecute(Map<String ,Object> paramMap, Integer courseId,Integer start,Integer limit) {
		
		/*
		 * 1.查询课程信息
		 */
		String redisTmp = RedisComponent.findRedisObject(courseId, TeachCourse.class);
		if(StringUtil.isEmpty(redisTmp)){
			LogUtil.error(this.getClass(), "findAllStudentsHomeWorkExecute", AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM).toJSONString();
		}
		TeachCourse teacher = JSONObject.parseObject(redisTmp, TeachCourse.class);
		
		/*
		 * 2.查询教师用户信息
		 */
		redisTmp = RedisComponent.findRedisObject(teacher.getTeacherId(), CenterUser.class);
		if(StringUtil.isEmpty(redisTmp)){
			LogUtil.error(this.getClass(), "findAllStudentsHomeWorkExecute", AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GETHOMEWORKSOLUTION_PARAM).toJSONString();
		}
		CenterUser teacherUser = JSONObject.parseObject(redisTmp, CenterUser.class);
		
		/*
		 * 3.分页查询学生文本作业
		 */
		QueryPage<TeachStudentHomework> homeworkPage = QueryPageComponent.queryPageByRedisSimple(limit, start, paramMap, TeachStudentHomework.class);
		if(homeworkPage.getState()){
			
			//附件查询
			List<Integer> idsList = new ArrayList<Integer>();
			for(TeachStudentHomework homework : homeworkPage.getList()){
				idsList.add(homework.getHomeworkId());
			}
			List<TeachRelHomeworkAttachment> attList = null;
			if(idsList.size() > 0 ){
				TeachRelHomeworkAttachment workAtt = new TeachRelHomeworkAttachment();
				workAtt.setHomeworkIds(StringUtil.ListToString(idsList, ","));
				attList = relHomeworkAttachmentService.selectSingleTeachRelHomeworkAttachment(workAtt);
			}
			
			/*
			 * 4循环分页对象进行参数变量组装
			 */
			CenterUser user = null;
			PublicPicture pic = null;
			PublicAttachment pa = null;
			TeachSchool school = null;
			
			
			for(TeachStudentHomework homework : homeworkPage.getList()){
				
				/*
				 * 4.1获取用户名称
				 */
				redisTmp = RedisComponent.findRedisObject(homework.getStudentId(), CenterUser.class);
				if(!StringUtil.isEmpty(redisTmp)){
					user = JSONObject.parseObject(redisTmp, CenterUser.class);
					homework.setStudentName(user.getRealName());
					
					/*
					 * 4.2获取用户图片链接地址
					 */
					redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						/*
						 * 压缩图片
						 */
						homework.setStudentHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
				}
				
				/*
				 * 4.3如果已经平分，加入教师信息
				 */
				if(homework.getAssessTime() != null && homework.getAssessTime() > 0){
					homework.setHasScored(1);
					homework.setTeacherName(teacherUser.getRealName());
					homework.setTeacherId(teacherUser.getUserId());
					/*
					 * 4.31 获取教师图片
					 */
					redisTmp = RedisComponent.findRedisObjectPublicPicture(teacherUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						/*
						 * 压缩图片
						 */
						homework.setTeacherHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
					/*
					 * 4.32获取教师 所在学校
					 */
					redisTmp = RedisComponent.findRedisObject(teacherUser.getSchoolId(), TeachSchool.class);
					if(!StringUtil.isEmpty(redisTmp)){
						school = JSONObject.parseObject(redisTmp, TeachSchool.class);
						homework.setSchoolName(school.getSchoolName());
					}
				}
				
				/*
				 * 4.4查询文本作业附件
				 */
				if(!CollectionUtils.isEmpty(attList)){
					for(int i = 0 ; i < attList.size() ; i ++){
						/*
						 * 4.41循环获取附件对象
						 */
						if(homework.getHomeworkId().intValue() == attList.get(i).getHomeworkId().intValue()){
							redisTmp = RedisComponent.findRedisObject(attList.get(i).getAttaId(), PublicAttachment.class);
							if(!StringUtil.isEmpty(redisTmp)){
								pa = JSONObject.parseObject(redisTmp, PublicAttachment.class);
								homework.getHomeworkFiles().add(pa);
								attList.remove(i);
								i--;
							}
						}
					}
				}
			}
		}
		
		return  homeworkPage.getMessageJSONObject("solutions").toJSONString();
	}

	/**
	 * 作业文本对象保存
	 * @param save
	 * @author 			lw
	 * @param homeworkFileIds 
	 * @date			2016年6月21日  下午8:47:27
	 */
	public JSONObject save(TeachStudentHomework save, String homeworkFileIds) {
		
		//保存作业文本信息
		try {
			teachStudentHomeworkDao .insertTeachStudentHomework(save);
			LogUtil.info(this.getClass(), "save", GameConstants.SUCCESS_INSERT);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "save", String.valueOf(AppErrorCode.ERROR_INSERT), e);
			e.printStackTrace();
		}
		//保存附件关系表信息
		this.batchStudenthomeworkAndAttachment(homeworkFileIds, save.getHomeworkId());
		
		return Common.getReturn(AppErrorCode.SUCCESS, null);
	}

	public String update(TeachStudentHomework update, String homeworkFileIds) {
		teachStudentHomeworkDao .updateTeachStudentHomeworkByKey(update);
		JedisCache.delRedisVal(TeachStudentHomework.class.getSimpleName(), String.valueOf(update.getHomeworkId()));
		
		//删除附件表
		TeachRelHomeworkAttachment delEntity = new TeachRelHomeworkAttachment();
		delEntity.setHomeworkId(update.getHomeworkId());
		relHomeworkAttachmentService.deleteTeachRelHomeworkAttachment(delEntity);
		
		//保存附件关系表信息
		this.batchStudenthomeworkAndAttachment(homeworkFileIds, update.getHomeworkId());
		
		return Common.getReturn(AppErrorCode.SUCCESS, null).toJSONString();
	}
	
	/**
	 * 学生作业附件表批量插入
	 * @param homeworkFileIds
	 * @param homeworkId
	 * @author 			lw
	 * @date			2016年6月21日  下午9:53:48
	 */
	private void batchStudenthomeworkAndAttachment(String homeworkFileIds,Integer homeworkId){
		
		//附件关系对象组装
		if(!StringUtil.isEmpty(homeworkFileIds)){
			
			List<String> strToList = StringUtil.strToList(homeworkFileIds);
			TeachRelHomeworkAttachment relAttachment = null;
			if(!CollectionUtils.isEmpty(strToList)){
				List<TeachRelHomeworkAttachment> list = new ArrayList<TeachRelHomeworkAttachment>();
				
				//组装批量保存对象
				for(String str : strToList){
					relAttachment = new TeachRelHomeworkAttachment();
					relAttachment.setHomeworkId(homeworkId);
					relAttachment.setAttaId(Integer.valueOf(str));
					list.add(relAttachment);
				}
				
				try {
					
					//批量保存
					relHomeworkAttachmentService.insertTeachRelHomeworkAttachments(list);
					LogUtil.error(this.getClass(), "batchStudenthomeworkAndAttachment", GameConstants.SUCCESS_INSERT);
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "batchStudenthomeworkAndAttachment", String.valueOf(AppErrorCode.ERROR_INSERT),e);
					e.printStackTrace();
				}
				
			}
		}
	}


	/**
	 * 教师对学生上传作业的评分
	 * @param userId				用户id
	 * @param scoreObjectId			文本作业id
	 * @param score					分数
	 * @param textEvaluation		教师对作业的文字点评
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午10:35:15
	 */
	public String submitHomeWorkeScore(int userId, int scoreObjectId, String textEvaluation, int score) {
		
		//	1 查询文本作业
		String redis = RedisComponent.findRedisObject(scoreObjectId, TeachStudentHomework.class);
		if(redis != null){
			TeachStudentHomework homework = JSONObject.parseObject(redis, TeachStudentHomework.class);
			
			// 2 作业未删除
			if(GameConstants.NO_DEL == homework.getIsDelete()){
				
				//	3 保存作业
				try {
					homework.setAssessContent(textEvaluation);
					homework.setAssessScore(score);
					homework.setAssessTime(TimeUtil.getCurrentTimestamp());
					homework.setTeacherId(userId);
					teachStudentHomeworkDao.updateTeachStudentHomeworkByKey(homework);
					
					//	4  删除redis缓存
					JedisCache.delRedisVal(TeachStudentHomework.class.getSimpleName(), String.valueOf(scoreObjectId));
					LogUtil.info(this.getClass(), "submitHomeWorkeScore", GameConstants.SUCCESS_UPDATE);
					return Common.getReturn(AppErrorCode.SUCCESS, "评分成功!").toJSONString();
							
				} catch (Exception e) {
					
					//	保存失败
					e.printStackTrace();
					LogUtil.error(this.getClass(), "submitHomeWorkeScore", String.valueOf(AppErrorCode.ERROR_INSERT), e);
					return Common.getReturn(AppErrorCode.ERROR_UPDATE, AppErrorCode.ERROR_SUBMITSCORE_UPLOAD_UPDATE).toJSONString();
				}
			}
			
			//	作业已经删除
			LogUtil.error(this.getClass(), "submitHomeWorkeScore", String.valueOf(AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_ISDELETE));
			return Common.getReturn(AppErrorCode.ERROR_UPDATE, AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_ISDELETE).toJSONString();
		}
		
		//	没有该作业
		LogUtil.error(this.getClass(), "submitHomeWorkeScore", String.valueOf(AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_EXISTENT));
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_EXISTENT).toJSONString();
	}

	
	/**
	 * 教师对班级学生的评分
	 * @param userId				教师id
	 * @param scoreObjectId			学生id
	 * @param score					分数
	 * @param textEvaluation		教师对作业的文字点评
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午10:56:07
	 */
	public String submitStudentScore(int userId, int scoreObjectId, int score) {
		
		//	1. 获取学生信息
		String userRedis = RedisComponent.findRedisObject(scoreObjectId, CenterUser.class);
		if(!StringUtil.isEmpty(userRedis)){
			CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
			
			//2. 获取教师班级关系 
			TeachRelTeacherClass teachRelTeacherClass = new TeachRelTeacherClass();
			teachRelTeacherClass.setClassId(user.getClassId());
			teachRelTeacherClass.setTeacherId(userId);
			teachRelTeacherClass = teachRelTeacherClassService.getTeachRelTeacherClass(teachRelTeacherClass);
			if(teachRelTeacherClass != null){
				
				//3. 获取平分对象
				TeachRelStudentClass selectBean = new TeachRelStudentClass();
				selectBean.setClassId(user.getClassId());
				selectBean.setStudentId(scoreObjectId);
				selectBean.setIsDelete(GameConstants.NO_DEL);
				selectBean = relStudentClassService.selectTeachRelStudentClass(selectBean);
				
				//4. 判断平分对象是否存在
				if(selectBean == null){
					selectBean = new TeachRelStudentClass();
					selectBean.setClassId(user.getClassId());
					selectBean.setStudentId(scoreObjectId);
					selectBean.setIsDelete(GameConstants.NO_DEL);
				}
				
				//	5. 修改平分
				selectBean.setAssessScore(score);
				
				//	6. 更新并删除缓存
				try {
					return relStudentClassService.saveOrUpdate(selectBean).toJSONString();
				} catch (Exception e) {
					
					//跟新失败
					e.printStackTrace();
					LogUtil.error(this.getClass(), "submitStudentScore", AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_INSERT,e);
					return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_INSERT).toJSONString();
				}
				
			}
			
			//教师未管理该班级 
			LogUtil.error(this.getClass(), "submitStudentScore", AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_CLASS_TEACHER);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_CLASS_TEACHER).toJSONString();
		}
		
		//平分学生信息错误 
		LogUtil.error(this.getClass(), "submitStudentScore", AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_CLASS_STUDENT);
		return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_SUBMITSCORE_RELCLASS_CLASS_STUDENT).toJSONString();
		
	}
	
	/**
	 * updateTeachStudentHomeworkByKey(逻辑删除作业)   
	 * @author ligs
	 * @date 2016年7月4日 下午7:51:37
	 * @return
	 */
	public void updateTeachStudentHomeworkByKey(TeachStudentHomework teachStudentHomework){
		teachStudentHomeworkDao.updateTeachStudentHomeworkByKey(teachStudentHomework);
	}
	
	
	
	
	
	
}