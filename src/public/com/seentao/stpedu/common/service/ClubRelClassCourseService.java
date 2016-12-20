package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ClubRelClassCourseDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubRelClassCourse;
import com.seentao.stpedu.common.entity.ClubRelClassCourseMember;
import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ClubRelClassCourseService{
	
	@Autowired
	private ClubRelClassCourseDao clubRelClassCourseDao;
	@Autowired
	private ClubRelClassCourseMemberService clubRelClassCourseMemberService;
	@Autowired
	private ClubTrainingCourseService clubTrainingCourseService;
	@Autowired
	private PublicAttachmentService publicAttachmentService;
	@Autowired
	private ClubRelCourseAttachmentService clubRelCourseAttachmentService;
	@Autowired
	private TeachRelCourseAttachmentService teachRelCourseAttachmentService;
	public List<ClubRelClassCourse> getClubRelClassCourse(ClubRelClassCourse clubRelClassCourse) {
		List<ClubRelClassCourse> clubRelClassCourseList = clubRelClassCourseDao .selectSingleClubRelClassCourse(clubRelClassCourse);
		if(clubRelClassCourseList == null || clubRelClassCourseList .size() <= 0){
			return null;
		}
		
		return clubRelClassCourseList;
	}
	
	public ClubRelClassCourse getClubRelClassCourseOne(ClubRelClassCourse clubRelClassCourse) {
		List<ClubRelClassCourse> clubRelClassCourseList = clubRelClassCourseDao .selectSingleClubRelClassCourse(clubRelClassCourse);
		if(clubRelClassCourseList == null || clubRelClassCourseList .size() <= 0){
			return null;
		}
		
		return clubRelClassCourseList.get(0);
	}

	public void updateClubRelClassCourse(ClubRelClassCourse clubRelClassCourse) {
		clubRelClassCourseDao.updateClubRelClassCourseByKey(clubRelClassCourse);
	}

	public void insertClubRelClassCourse(ClubRelClassCourse clubRelClassCourse) {
		clubRelClassCourseDao.insertClubRelClassCourse(clubRelClassCourse);
	}

	public Integer getClubCourseByClassIdCount(Map<String, Object> paramMap) {
		return clubRelClassCourseDao.getClubCourseByClassIdCount(paramMap);
	}

	/**
	 * 手机端:获取培训班列表
	 * @param map
	 * @param start
	 * @param limit
	 * @param userType
	 * @param classType
	 * @param userId
	 * @return
	 * @author 			lw
	 * @date			2016年7月27日  上午1:30:25
	 */
	public JSON findTeachCourseByClassId(Map<String, Object> map, Integer start, Integer limit, Integer teachingRole, Integer classType, Integer userId) {
		
		//返回培训班列表实体
		TeachRelCardCourse relCourse = null;
		List<TeachRelCardCourse> relCourseList = new ArrayList<TeachRelCardCourse>();
		
		String redisData = null;
		ClubTrainingCourse clubCourse = null;
		
		//分页查询培训班
		QueryPage<ClubRelClassCourse> queryPage = QueryPageComponent.queryPage(limit, start, map, ClubRelClassCourse.class);
		if(queryPage.getState()){
			
			//适应性返回参数修改：把	ClubRelClassCourse 转换成	TeachRelCardCourse
			for(ClubRelClassCourse en : queryPage.getList()){
				
				redisData = RedisComponent.findRedisObject(en.getCourseId(), ClubTrainingCourse.class);
				if(redisData != null){
					clubCourse = JSONObject.parseObject(redisData, ClubTrainingCourse.class);
					relCourse = new TeachRelCardCourse();
					relCourse.setCourseId(clubCourse.getCourseId());
					relCourse.setTotalStudyNum(clubCourse.getTotalStudyNum());
					relCourse.setShowType(en.getIsShow());
					relCourseList.add(relCourse);
				}
				
			}
			
			//获取班级教师信息
			CenterUser teacher = null;
			PublicPicture teacherPic = null;
			TeachSchool school = null;
			if(relCourseList.size() > 0 ){
				//获取课程
				redisData = RedisComponent.findRedisObject(relCourseList.get(0).getCourseId(), ClubTrainingCourse.class);
				ClubTrainingCourse course = null;
				if(redisData != null){
					course = JSONObject.parseObject(redisData, ClubTrainingCourse.class);
					
					//获取教师
					redisData = RedisComponent.findRedisObject(course.getCreateUserId() , CenterUser.class);
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
				this.assembleTeachCourse(relCourseList, classType, userId, teacher, teacherPic, school);
			}
		}
		map.clear();
		map.put("teachingRole", teachingRole);
		JSONObject msg = queryPage.getMessageJSONObject("courses", map);
		JSONObject result = ((JSONObject)msg.get(GameConstants.JSONAPI_KEY));
		result.remove("courses");
		result.put("courses", JSONArray.toJSON(relCourseList));
		
		return msg;
	}
	
	
	
	

	/**
	 * 手机端数据组装
	 * @param list
	 * @author 			lw
	 * @param classType 
	 * @param userId 
	 * @param user 
	 * @param teacherPic 
	 * @param chapter 
	 * @param card 
	 * @param school 
	 * @date			2016年7月26日  下午8:09:00
	 */
	private void assembleTeachCourse(List<TeachRelCardCourse> list, Integer classType, Integer userId, CenterUser teacher, PublicPicture teacherPic, TeachSchool school) {
		
		String redisData = null;
		PublicPicture pic = null;
		ClubTrainingCourse course = null;
		PublicAttachment att = null;
		List<PublicAttachment> otherFiles = null;
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		ClubRelClassCourseMember courseMember = null;
		JSONArray arrJson = null;
		for(TeachRelCardCourse en : list){
			
			//班级类型
			en.setClassType(classType);
			
			//查询课程
			redisData = RedisComponent.findRedisObject(en.getCourseId(), ClubTrainingCourse.class);
			if(redisData != null){
				course = JSONObject.parseObject(redisData, ClubTrainingCourse.class);
				
				en.setCourseId(course.getCourseId());
				en.setHomeworkTitle(course.getCourseTitle());
				en.setCourseDesc(course.getCourseExplain());
				
				//课程来源:1:官方；2:教师(或培训班教练)；
				en.setCourseSource(course.getCourseType());
				
				//2:俱乐部培训班，课程显示类型是3:隐藏；4:显示；
				en.setShowType(en.getShowType() == 1 ? 4 : 3);
				
				//获取课程图片链接地址
				redisData = RedisComponent.findRedisObject(course.getImageId(), PublicPicture.class);
				if(redisData != null){
					pic = JSONObject.parseObject(redisData, PublicPicture.class);
					en.setCourseLink(Common.checkPic(pic.getFilePath()) ==true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
				}
				
				//获取视频附件信息
				redisData = RedisComponent.findRedisObject(course.getVideoAttaId() , PublicAttachment.class);
				if(redisData != null){
					att = JSONObject.parseObject(redisData, PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_VIDEO);
					arrJson = new JSONArray();
					arrJson.add(att);
					en.setVideoFiles(arrJson);
				}else{
					en.setVideoFiles(new JSONArray());
				}
				
				//获取音频附件信息
				redisData = RedisComponent.findRedisObject(course.getAudioAttaId() , PublicAttachment.class);
				if(redisData != null){
					att = JSONObject.parseObject(redisData, PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_AUDIO);
					arrJson = new JSONArray();
					arrJson.add(att);
					en.setAudioFiles(arrJson);
				}else{
					en.setAudioFiles(new JSONArray());
				}
				
				//获取其他附件
				otherFiles = publicAttachmentService.getClubOtherPublicAttachment(course.getCourseId());
				if(!CollectionUtils.isEmpty(otherFiles)){
					JSONArray json = (JSONArray)JSONArray.toJSON(otherFiles);
					for(int i = 0 ; i < json.size() ; i++){
						att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
						att.setAttachmentMajorType(GameConstants.ATTACHMENT_OTHER);
					}
					en.setOtherFiles(json);
				}else{
					en.setOtherFiles(new JSONArray());
				}
				//根据课程id查询课程附件关系表获取附件id
				if(course.getCourseId() != null){
					//其它附件
					otherFiles =this.returnClubAttment(course.getCourseId(), 3);
					//音频附件
					audiotList =this.returnClubAttment(course.getCourseId(), 2);
					//视频附件
					// update by chengshx 2016-11-04
					PublicAttachment video = new PublicAttachment();
					video.setAttaId(course.getVideoAttaId());
					videoList = publicAttachmentService.getPublicAttachment(video);
//					videoList =	this.returnClubAttment(course.getCourseId(), 1);
				}
				//获取视频附件信息
				if(!CollectionUtils.isEmpty(videoList)){
					JSONArray json = (JSONArray)JSONArray.toJSON(videoList);
					for(int i = 0 ; i < json.size() ; i++){
						att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
						att.setAttachmentMajorType(GameConstants.ATTACHMENT_VIDEO);
					}
					en.setVideoFiles(json);
				}else{
					en.setVideoFiles(new JSONArray());
				}
				//获取音频附件信息
				if(!CollectionUtils.isEmpty(audiotList)){
					JSONArray json = (JSONArray)JSONArray.toJSON(audiotList);
					for(int i = 0 ; i < json.size() ; i++){
						att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
						att.setAttachmentMajorType(GameConstants.ATTACHMENT_AUDIO);
					}
					en.setAudioFiles(json);
				}else{
					en.setAudioFiles(new JSONArray());
				}
				//获取其他附件
				if(!CollectionUtils.isEmpty(otherFiles)){
					JSONArray json = (JSONArray)JSONArray.toJSON(otherFiles);
					for(int i = 0 ; i < json.size() ; i++){
						att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
						att.setAttachmentMajorType(GameConstants.ATTACHMENT_OTHER);
					}
					en.setOtherFiles(json);
				}else{
					en.setOtherFiles(new JSONArray());
				}
				
				//获取教师信息
				if(en.getCourseSource() != GameConstants.OFFICIAL_COURSE){
					if(teacher != null ){
						en.setTeacherName(teacher.getNickName());
						
						//教师图片地址
						if(teacherPic != null){
							en.setTeacherHeadLink(Common.checkPic(teacherPic.getFilePath()) == true ? teacherPic.getFilePath()+ActiveUrl.HEAD_MAP:teacherPic.getFilePath());
						}
						
						//学校名称
						if(school != null){
							en.setSchoolName(school.getSchoolName());
						}
					}
				}
				
				
				//学习状态
				courseMember = new ClubRelClassCourseMember();
				courseMember.setCourseId(course.getCourseId());
				courseMember.setStudentId(userId);
				courseMember = clubRelClassCourseMemberService.getClubRelClassCourseMember(courseMember);
				en.setLearningStatus(courseMember == null ? 0 : 1);
				
			}
		}
	}

	/**
	 * 校验班级 和 课程关系
	 * @param classId
	 * @param courseId
	 * @return
	 * @author 			lw
	 * @date			2016年7月27日  下午6:52:02
	 */
	private JSON checkClubAndCourse(int classId, int courseId){
		ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
		clubRelClassCourse.setClassId(classId);
		clubRelClassCourse.setCourseId(courseId);
		clubRelClassCourse = clubRelClassCourseDao.selectClubRelClassCourse(clubRelClassCourse);
		if(clubRelClassCourse == null){
			LogUtil.error(this.getClass(), "checkClubAndCourse", String.valueOf(AppErrorCode.ERROR_TEACH_AND_COURSE));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_TEACH_AND_COURSE);
		}
		return null;
	}
	
	/**
	 * 手机端：培训班相信查看
	 * @param courseId	课程id
	 * @param userId	用户id
	 * @param classId	班级id
	 * @param userType	用户类型
	 * @return
	 * @author 			lw
	 * @param classType 
	 * @date			2016年7月27日  上午10:58:12
	 */
	public JSON findOneTeachCourse(int courseId, Integer userId, Integer classId, Integer teachingRole, int classType) {
		//校验班级 和 课程
		JSON msg = this.checkClubAndCourse(classId, courseId);
		if(msg != null){
			return msg;
		}
		
		JSONArray arrJson = null;
		String redisData = null;
		PublicAttachment att = null;
		PublicPicture pic =null;
		List<PublicAttachment> otherFiles = null;
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		ClubTrainingCourse course = new ClubTrainingCourse();
		course.setCourseId(courseId);
		course = clubTrainingCourseService.getClubTrainingCourse(course);
		TeachRelCardCourse en = new TeachRelCardCourse();
		if(course != null){
			en.setCourseId(course.getCourseId());
			en.setTotalStudyNum(course.getTotalStudyNum());

			en.setCourseId(course.getCourseId());
			en.setHomeworkTitle(course.getCourseTitle());
			en.setCourseDesc(course.getCourseExplain());
			en.setShowType(course.getCourseType());
			
			//课程类型
			en.setClassType(classType);
			
			//课程来源:1:官方；2:教师(或培训班教练)；
			en.setCourseSource(course.getCourseType());
			
			//获取课程图片链接地址
			redisData = RedisComponent.findRedisObject(course.getImageId(), PublicPicture.class);
			if(redisData != null){
				pic = JSONObject.parseObject(redisData, PublicPicture.class);
				en.setCourseLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			}
			
			/*//获取视频附件信息
			redisData = RedisComponent.findRedisObject(course.getVideoAttaId() , PublicAttachment.class);
			if(redisData != null){
				att = JSONObject.parseObject(redisData, PublicAttachment.class);
				att.setAttachmentMajorType(GameConstants.ATTACHMENT_VIDEO);
				arrJson = new JSONArray();
				arrJson.add(att);
				en.setVideoFiles(arrJson);
			}else{
				en.setVideoFiles(new JSONArray());
			}
			
			//获取音频附件信息
			redisData = RedisComponent.findRedisObject(course.getAudioAttaId() , PublicAttachment.class);
			if(redisData != null){
				att = JSONObject.parseObject(redisData, PublicAttachment.class);
				att.setAttachmentMajorType(GameConstants.ATTACHMENT_AUDIO);
				arrJson = new JSONArray();
				arrJson.add(att);
				en.setAudioFiles(arrJson);
			}else{
				en.setAudioFiles(new JSONArray());
			}
			
			//获取其他附件
			List<PublicAttachment> otherFiles = publicAttachmentService.getClubOtherPublicAttachment(course.getCourseId());
			if(!CollectionUtils.isEmpty(otherFiles)){
				JSONArray json = (JSONArray)JSONArray.toJSON(otherFiles);
				for(int i = 0 ; i < json.size() ; i++){
					att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_OTHER);
				}
				en.setOtherFiles(json);
			}else{
				en.setOtherFiles(new JSONArray());
			}*/
			
			//根据课程id查询课程附件关系表获取附件id
			if(course.getCourseId() != null){
				//其它附件
				otherFiles =this.returnClubAttment(course.getCourseId(), 3);
				//音频附件
				audiotList =this.returnClubAttment(course.getCourseId(), 2);
				//视频附件
				videoList =	this.returnClubAttment(course.getCourseId(), 1);
			}
			//获取视频附件信息
			if(!CollectionUtils.isEmpty(videoList)){
				JSONArray json = (JSONArray)JSONArray.toJSON(videoList);
				for(int i = 0 ; i < json.size() ; i++){
					att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_VIDEO);
				}
				en.setVideoFiles(json);
			}else{
				en.setVideoFiles(new JSONArray());
			}
			//获取音频附件信息
			if(!CollectionUtils.isEmpty(audiotList)){
				JSONArray json = (JSONArray)JSONArray.toJSON(audiotList);
				for(int i = 0 ; i < json.size() ; i++){
					att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_AUDIO);
				}
				en.setAudioFiles(json);
			}else{
				en.setAudioFiles(new JSONArray());
			}
			//获取其他附件
			if(!CollectionUtils.isEmpty(otherFiles)){
				JSONArray json = (JSONArray)JSONArray.toJSON(otherFiles);
				for(int i = 0 ; i < json.size() ; i++){
					att =  JSONObject.parseObject(json.get(i).toString(), PublicAttachment.class);
					att.setAttachmentMajorType(GameConstants.ATTACHMENT_OTHER);
				}
				en.setOtherFiles(json);
			}else{
				en.setOtherFiles(new JSONArray());
			}
			//查询班级教师关系表
			if(en.getCourseSource() != GameConstants.OFFICIAL_COURSE){
				
					
				//获取教师信息
				redisData = RedisComponent.findRedisObject(course.getCreateUserId(), CenterUser.class);
				if(redisData != null){
					CenterUser teacher = JSONObject.parseObject(redisData, CenterUser.class);
					en.setTeacherName(teacher.getNickName());
					
					//教师图片地址
					redisData = RedisComponent.findRedisObject(teacher.getHeadImgId(), PublicPicture.class);
					if(redisData != null ){
						pic = JSONObject.parseObject(redisData, PublicPicture.class);
						en.setTeacherHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
					
					//学校名称
					redisData = RedisComponent.findRedisObject(teacher.getSchoolId(), TeachSchool.class);
					if(redisData != null){
						TeachSchool school = JSONObject.parseObject(redisData, TeachSchool.class);
						en.setSchoolName(school.getSchoolName());
					}
				}
			
			}
			
			
			
			//学习状态
			ClubRelClassCourseMember courseMember = new ClubRelClassCourseMember();
			courseMember.setCourseId(course.getCourseId());
			courseMember.setStudentId(userId);
			courseMember = clubRelClassCourseMemberService.getClubRelClassCourseMember(courseMember);
			en.setLearningStatus(courseMember == null ? 0 : 1);
			
			//app内容封装
			JSONObject rich = RichTextUtil.parseRichText(course.getCourseContent());
			if(rich != null){
				en.setHomeworkBody(rich.getString("content"));
				en.setLinks(rich.getJSONArray("links"));
				en.setImgs(rich.getJSONArray("imgs"));
			}
			
			//学习人次
			try {
				ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
				clubRelClassCourse.setCourseId(course.getCourseId());
				clubRelClassCourse.setClassId(classId);
				clubRelClassCourse = this.getClubRelClassCourseOne(clubRelClassCourse);
				if(clubRelClassCourse == null){
					clubRelClassCourse = new ClubRelClassCourse();
					clubRelClassCourse.setCourseId(course.getCourseId());
					clubRelClassCourse.setClassId(classId);
					clubRelClassCourse.setCreateTime(TimeUtil.getCurrentTimestamp());
					clubRelClassCourse.setIsShow(1);
					clubRelClassCourse.setTotalStudyNum(0);
					this.insertClubRelClassCourse(clubRelClassCourse);
					LogUtil.info(this.getClass(), "findOneTeachCourse", "俱乐部学习人次保存成功");
				}else{
					clubRelClassCourse.setTotalStudyNum(clubRelClassCourse.getTotalStudyNum()+1);
					this.updateClubRelClassCourse(clubRelClassCourse);
					LogUtil.info(this.getClass(), "findOneTeachCourse", "俱乐部学习人次修改成功");
				}
				
				//2:俱乐部培训班，课程显示类型是3:隐藏；4:显示；
				en.setShowType(clubRelClassCourse.getIsShow() == 1 ? 4 : 3);
				
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(this.getClass(), "findOneTeachCourse", String.valueOf(AppErrorCode.ERROR_CLUB_STUDY), e);
			}
		}
		
		List<TeachRelCardCourse> list = new ArrayList<TeachRelCardCourse>();
		list.add(en);
		JSONObject json = new JSONObject();
		json.put("courses", JSONArray.toJSON(list));
		json.put("teachingRole", teachingRole);
		return Common.getReturn(AppErrorCode.SUCCESS, null, json);
	}
	
	/**
	 * 批量插入关系表
	 * @param courseId	课程id
	 * @param userId	用户id
	 * @param classId	班级id
	 * @param userType	用户类型
	 * @return
	 * @author 			lijin
	 * @param classType 
	 * @date			2016年7月29日  上午10:58:12
	 */
	
	public void insertBatchClubRelClass(List<ClubRelClassCourse> clubRelClassCourseList){
		
		
		clubRelClassCourseDao.insertClubRelClassCourses(clubRelClassCourseList);
		
	}
	
	
	
	
	/**
	 * 获取附件集合
	 * @return
	 */
	public List<PublicAttachment> returnAttment(Integer courseId,Integer type){
		List<PublicAttachment> list = new ArrayList<>();
		//根据课程id查询课程附件关系表获取附件id
		if(courseId != null){
			TeachRelCourseAttachment teachrelcourseattachment = new TeachRelCourseAttachment();
			teachrelcourseattachment.setCourseId(courseId);
			List<TeachRelCourseAttachment> selectSingleTeachRelCourseAttachment = teachRelCourseAttachmentService.selectSingleTeachRelCourseAttachment(teachrelcourseattachment);
			if(selectSingleTeachRelCourseAttachment != null && selectSingleTeachRelCourseAttachment.size() > 0){
				for (TeachRelCourseAttachment teachRelCourseAttachment2 : selectSingleTeachRelCourseAttachment) {
					Map<String,Object> attachmentids = new HashMap<String,Object>();
					attachmentids.put("courseid", courseId);//课程卡id
					attachmentids.put("attaid", teachRelCourseAttachment2.getAttaId());//附件id
					attachmentids.put("type", type);//附件类型
					//附件集合
					List<PublicAttachment> lists = publicAttachmentService.getOtherPublicAttachment(attachmentids);
					if(lists != null && lists.size() > 0){
						list.addAll(lists);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 培训班获取附件集合
	 * @return
	 */
	public List<PublicAttachment> returnClubAttment(Integer courseId,Integer type){
		List<PublicAttachment> list = new ArrayList<>();
		//根据课程id查询课程附件关系表获取附件id
		if(courseId != null){
			ClubRelCourseAttachment clubRelCourseAttachment = new ClubRelCourseAttachment();
			clubRelCourseAttachment.setCourseId(courseId);
			List<ClubRelCourseAttachment> selectSingleClubRelCourseAttachment = clubRelCourseAttachmentService.selectSingleClubRelCourseAttachment(clubRelCourseAttachment);
			if(selectSingleClubRelCourseAttachment != null && selectSingleClubRelCourseAttachment.size() > 0){
				for (ClubRelCourseAttachment clubRelCourseAttachment2 : selectSingleClubRelCourseAttachment) {
					Map<String,Object> attachmentids = new HashMap<String,Object>();
					attachmentids.put("courseid", courseId);//课程卡id
					attachmentids.put("attaid", clubRelCourseAttachment2.getAttaId());//附件id
					attachmentids.put("type", type);//附件类型
					//附件集合
					List<PublicAttachment> lists = publicAttachmentService.getClubOtherPublicAttachment(attachmentids);
					if(lists != null && lists.size() > 0){
						list.addAll(lists);
					}
				}
			}
		}
		return list;
	}
}