package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachCourseDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubRelClassCourse;
import com.seentao.stpedu.common.entity.ClubRelClassCourseMember;
import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentCourse;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;

@Service
public class BaseTeachCourseService{
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	@Autowired
	private TeachCourseDao teachCourseDao;
	@Autowired
	private PublicAttachmentService publicAttachmentService;
	@Autowired
	private TeachRelStudentCourseService teachRelStudentCourseService;
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	@Autowired
	private TeachRelClassCourseService teachRelClassCourseService;
	@Autowired
	private ClubTrainingCourseService clubTrainingCourseService;
	@Autowired
	private ClubRelClassCourseService clubRelClassCourseService;
	@Autowired
	private ClubRelClassCourseMemberService clubRelClassCourseMemberService;
	@Autowired
	private TeachRelCourseAttachmentService teachRelCourseAttachmentService;
	@Autowired
	private ClubRelCourseAttachmentService clubRelCourseAttachmentService;
	/**
	 * 课程卡课程关系表 根据课程卡id查询课程数量
	 * @param paramMap
	 * @return
	 */
	public Integer getCourseByCourseCardIdCount(Map<String, Object> paramMap) {
		return teachRelCardCourseService.queryCount(paramMap);
	}
	
	public TeachCourse getTeachCourse(TeachCourse teachCourse) {
		List<TeachCourse> teachCourseList = teachCourseDao .selectSingleTeachCourse(teachCourse);
		if(teachCourseList == null || teachCourseList .size() <= 0){
			return null;
		}
		
		return teachCourseList.get(0);
	}
	
	/**
	 * 根据对象参数查询对象集合
	 * @param teachCourse	对象参数
	 * @return				查询对象集合
	 * @author  lw
	 * @date    2016年6月18日 下午10:59:42
	 */
	public List<TeachCourse> selectSingleTeachCourse(TeachCourse teachCourse){
		return teachCourseDao.selectSingleTeachCourse(teachCourse);
	}

	/**
	 * 根据对象参数查询对象
	 * @param teachCourse
	 * @return
	 * @author  lw
	 * @date    2016年6月18日 下午11:00:18
	 */
	public TeachCourse selectTeachCourse(TeachCourse teachCourse){
		return teachCourseDao.selectTeachCourse(teachCourse);
	}
	
	/**
	 * 保存课程
	 * @param 	teachCourse
	 * @author  lw
	 * @date    2016年6月18日 下午10:54:37
	 */
	public void insertTeachCourse(TeachCourse teachCourse){
		teachCourseDao .insertTeachCourse(teachCourse);
	}
	
	
	public void addCourse(TeachCourse teachCourse){
		teachCourseDao.insertTeachCourse(teachCourse);
	}
	
	public void updateCourse(TeachCourse teachCourse){
		teachCourseDao.updateTeachCourseByKey(teachCourse);
	}

	
	/**
	 * @param flag 1为移动端调用
	 * 获取课程信息 根据课程id
	 */
	public TeachCourse getCourseByCourseId(Integer flag, Integer classId,Integer courseCardId,Integer courseId,CenterUser user,Integer classType) {
		//修改该课程学习状态
		TeachRelStudentCourse teachrelstudentcourse = new TeachRelStudentCourse();
		teachrelstudentcourse.setStudentId(user.getUserId());
		teachrelstudentcourse.setCourseId(courseId);
		TeachRelStudentCourse trc = teachRelStudentCourseService.getTeachRelStudentCourse(teachrelstudentcourse);
		if(user.getUserType()==2){//没有学习该课程(对象是学生)
			// 判断是否是自定义课程
			TeachCourse course = new TeachCourse();
			course.setCourseId(courseId);
			course = teachCourseDao.selectTeachCourse(course);
			TeachRelStudentCourse insertTeachRelStudentCourse = new TeachRelStudentCourse();
			insertTeachRelStudentCourse.setCourseId(courseId);
			insertTeachRelStudentCourse.setStudentId(user.getUserId());
			if(trc == null){
				insertTeachRelStudentCourse.setIsStudy(0);//1 是，0否
				if(course.getCourseType() == 2 && courseCardId != 0){ //判断是自定义课程
					TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
					teachRelCardCourse.setCardId(courseCardId);
					teachRelCardCourse.setCourseId(courseId);
					teachRelCardCourse.setTotalStudyNum(0);//该字段次数会自动+1
					teachRelCardCourseService.updateTeachRelCardCourse(teachRelCardCourse);
					insertTeachRelStudentCourse.setIsStudy(1);//1 是，0否
				} 
				teachRelStudentCourseService.insertTeachRelStudentCourse(insertTeachRelStudentCourse);
			} else {
				// 判断学生是否学习该课程
				if(trc.getIsStudy() != 1){
					if(course.getCourseType() == 2 && courseCardId != 0){ //判断是自定义课程
						insertTeachRelStudentCourse.setIsStudy(1);//1 是，0否
						teachRelStudentCourseService.updateTeachRelStudentCourse(insertTeachRelStudentCourse);
					}
				}
			}
		}
		if(flag == 1){
			return null;
		}
		
		TeachCourse	teachcourse = objectConverts(courseId,user);
		teachcourse.setClassType(classType);
		if(teachcourse.getCourseType()==BusinessConstant.COURSE_TYPE_1 || teachcourse.getCourseType()==BusinessConstant.COURSE_TYPE_5){
			teachcourse.setCourseSource(1);//官方课程
		}else{
			teachcourse.setCourseSource(2);//教师课程
		}
		//课程显示类型
		if(teachcourse.getCourseType()==BusinessConstant.COURSE_TYPE_5){
			TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
			teachRelClassCourse.setClassId(classId);
			teachRelClassCourse.setCourseId(courseId);
			TeachRelClassCourse res = teachRelClassCourseService.getTeachRelClassCourseOne(teachRelClassCourse);
			teachcourse.setCourseShowType(res==null?0:res.getShowType());
		}else{
			TeachRelCardCourse selectShowType = new TeachRelCardCourse();
			selectShowType.setCardId(courseCardId);
			selectShowType.setCourseId(courseId);
			TeachRelCardCourse t = teachRelCardCourseService.selectTeachRelCardCourse(selectShowType);
			teachcourse.setCourseShowType(t==null?0:t.getShowType());
		}
		//基础课程
		if(null!=courseCardId && courseCardId!=0){
			//章节信息 课程卡信息
			Map<String,Object> map = teachCourseCardService.getCardAndChapterByCardId(courseCardId);
			teachcourse.setResChapterId((Integer)map.get("chapterId"));//章节id
			teachcourse.setResChapterTitle((String)map.get("chapterName"));//章节名称
			teachcourse.setResCourseCardId((Integer)map.get("cardId"));//课程卡id
			teachcourse.setResCcTitle((String)map.get("cardTitle"));//课程卡名称
			teachcourse.setCourseCardType((Integer)map.get("courseCardType"));//课程卡类型
		}
		return teachcourse;
	}
	
	/**
	 * 对象转换
	 * @param teachcourse
	 * @param centeruser
	 * @return
	 */
	private TeachCourse objectConverts(Integer courseId,CenterUser centeruser){
		//缓存中获取课程信息
		String cache = RedisComponent.findRedisObject(courseId, TeachCourse.class);
		TeachCourse teachcourseCache = JSONObject.parseObject(cache,TeachCourse.class);
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		List<PublicAttachment> otherList = null;
		/*//视频附件集合
		if(teachcourseCache.getVideoAttaId()!=null){
			PublicAttachment video = new PublicAttachment();
			video.setAttaId(teachcourseCache.getVideoAttaId());
			videoList = publicAttachmentService.getPublicAttachment(video);
		}
		
		//音频附件集合
		if(teachcourseCache.getAudioAttaId()!=null){
			PublicAttachment audio = new PublicAttachment();
			audio.setAttaId(teachcourseCache.getAudioAttaId());
			audiotList = publicAttachmentService.getPublicAttachment(audio);
		}
		
		//其他附件
		if(teachcourseCache.getCourseId()!=null){
			otherList = publicAttachmentService.getOtherPublicAttachment(teachcourseCache.getCourseId());
		}*/
		//根据课程id查询课程附件关系表获取附件id
		if(teachcourseCache.getCourseId() != null){
			//其它附件
			otherList =this.returnAttment(teachcourseCache.getCourseId(), 3);
			//音频附件
			audiotList =this.returnAttment(teachcourseCache.getCourseId(), 2);
			//视频附件
			videoList =	this.returnAttment(teachcourseCache.getCourseId(), 1);
		}
		teachcourseCache.setVideoList(videoList==null?new ArrayList<PublicAttachment>():videoList);//视频
		teachcourseCache.setAudiotList(audiotList==null?new ArrayList<PublicAttachment>():audiotList);//音频
		teachcourseCache.setOtherList(otherList==null?new ArrayList<PublicAttachment>():otherList);//其他附件
		
		//学习状态 
		TeachRelStudentCourse teachrelstudentcourse = new TeachRelStudentCourse();
		teachrelstudentcourse.setStudentId(centeruser.getUserId());
		teachrelstudentcourse.setCourseId(teachcourseCache.getCourseId());
		TeachRelStudentCourse trc = teachRelStudentCourseService.getTeachRelStudentCourse(teachrelstudentcourse);
		if(null!=trc){//0  否 1 是
			teachcourseCache.setResLearningStatus(trc.getIsStudy() == null ? 0 : trc.getIsStudy());
		}else{
			teachcourseCache.setResLearningStatus(0);
		}
		//课程图像
		String courseImg = "";
		if(teachcourseCache.getCourseType() == 1 || teachcourseCache.getCourseType() == 5){
			courseImg = Common.getImgUrl(teachcourseCache.getImageId(), BusinessConstant.DEFAULT_IMG_PRACTICE_COURSE);
		}else if(teachcourseCache.getCourseType() == 2){
			courseImg = Common.getImgUrl(teachcourseCache.getImageId(), BusinessConstant.DEFAULT_IMG_CUSTOM_COURSE);
		}
		teachcourseCache.setResCourseLink(Common.checkPic(courseImg) == true ? courseImg+ActiveUrl.HEAD_MAP:courseImg);//课程图片链接
		
		String teacherCache = RedisComponent.findRedisObject(teachcourseCache.getTeacherId(),CenterUser.class);
		CenterUser teacher = JSONObject.parseObject(teacherCache,CenterUser.class);
		if(teacherCache!=null && teacherCache!=""){
			//教师图片信息
			String teacherImg = Common.getImgUrl(teacher.getHeadImgId(),BusinessConstant.DEFAULT_IMG_USER);
			teachcourseCache.setResTeacherHeadLink(Common.checkPic(teacherImg) == true ?teacherImg+ActiveUrl.HEAD_MAP:teacherImg );//教师图片链接
			teachcourseCache.setResTeacherName(teacher.getRealName());//教师名称
			
			//学校信息
			String cacheTeachSchool = RedisComponent.findRedisObject(teacher.getSchoolId(), TeachSchool.class);
			TeachSchool	teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
			teachcourseCache.setResSchoolName(teachschool==null?"":teachschool.getSchoolName());//学校名称
		}
		return teachcourseCache;
	}
	
	/**
	 * 根据课程卡id查询课程集合
	 * @param userId
	 * @param start
	 * @param limit
	 * @param courseId
	 * @return
	 */
	public List<TeachCourse> getCourseByCourseCardId(CenterUser centeruser,Integer courseCardId, Integer requestSide,Integer classType) {
		TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
		teachRelCardCourse.setCardId(courseCardId);
		teachRelCardCourse.setStart(centeruser.getStart());
		teachRelCardCourse.setLimit(centeruser.getLimit());
		if(requestSide.equals(2)){
			teachRelCardCourse.setRequestSide(3);
		}
		//分页查询课程
		List<TeachRelCardCourse> courseList = teachRelCardCourseService.getTeachRelCardCourse(teachRelCardCourse);
		List<TeachCourse> resList = new ArrayList<TeachCourse>();
		if(null!=courseList && courseList.size()>0){
			for (int i = 0; i < courseList.size(); i++) {
				TeachRelCardCourse course = courseList.get(i);
				TeachCourse res = objectConverts(course.getCourseId(), centeruser);
				Integer coursetype = res.getCourseType();
				if(coursetype==BusinessConstant.COURSE_TYPE_1||coursetype==BusinessConstant.COURSE_TYPE_5){
					res.setCourseSource(BusinessConstant.COURSE_TYPE_1);//官方课程
				}else{
					res.setCourseSource(BusinessConstant.COURSE_TYPE_2);//教师课程
				}
				res.setClassType(classType);//班级类型(教学班，培训班)
				res.setResLearningNum(course.getTotalStudyNum());//每个课程学习的人数
				res.setCourseShowType(course.getShowType());//课程展示类型
				if(requestSide==BusinessConstant.REQUESTSIDE_TYPE_1){//后端管理请求  返回所有课程
					resList.add(res);
				}else{//前端请求  隐藏的课程不返回
					if(course.getShowType()!=3){
						resList.add(res);
					}
				}
			}
		}
		return resList;
	}

	
	/**
	 * 课程应交人数加一
	 * @param homeWork	查询参数对象
	 * @author 			lw
	 * @date			2016年6月24日  上午11:46:14
	 */
	public void addActualnum(Integer courseId) {
		teachRelCardCourseService.addActualnum(courseId);
	}
	
	/**
	 * 根据班级id查询课程
	 * @param centeruser
	 * @param courseCardId
	 * @param requestSide
	 * @return
	 */
	public List<TeachCourse> getCourseByClassId(CenterUser centeruser,Integer requestSide,Integer classId,Integer classType) {
		TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
		teachRelClassCourse.setClassId(classId);
		List<TeachRelClassCourse> courseList = teachRelClassCourseService.getTeachRelClassCourse(teachRelClassCourse);
		List<TeachCourse> resList = new ArrayList<TeachCourse>();
		if(null!=courseList && courseList.size()>0){
			for (int i = 0; i < courseList.size(); i++) {
				TeachRelClassCourse course = courseList.get(i);
				TeachCourse res = objectConverts(course.getCourseId(), centeruser);
				//课程类型
				Integer coursetype = res.getCourseType();
				if(coursetype==BusinessConstant.COURSE_TYPE_1||coursetype==BusinessConstant.COURSE_TYPE_5){
					res.setCourseSource(BusinessConstant.COURSE_TYPE_1);//官方课程
				}else{
					res.setCourseSource(BusinessConstant.COURSE_TYPE_2);//教师课程
				}
				res.setClassType(classType);
				res.setCourseShowType(course.getShowType());//课程展示类型
				res.setResLearningNum(course.getTotalStudyNum());//每个课程学习的人数
				if(requestSide==BusinessConstant.REQUESTSIDE_TYPE_1){//后端管理请求  返回所有课程
					resList.add(res);
				}else{//前端请求  隐藏的课程不返回
					if(course.getShowType()!=3){
						resList.add(res);
					}
				}
			}
		}
		return resList;
	}
	
	/**
	 * 根据班级id查询课程数量
	 * @param paramMap
	 * @return
	 */
	public Integer getCourseByClassIdCount(Map<String, Object> paramMap) {
		return teachRelClassCourseService.getCourseByClassIdCount(paramMap);
	}
	
	/**
	 * 根据培训班id查询记录
	 * @param paramMap
	 * @return
	 */
	public List<ClubTrainingCourse> getClubCourseByClassId(Integer courseCardId, Integer requestSide,
			Integer classId,Integer classType,Integer start,Integer limit,Integer userId) {
		ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
		clubRelClassCourse.setClassId(classId);
		clubRelClassCourse.setStart(start);
		clubRelClassCourse.setLimit(limit);
		List<ClubRelClassCourse> courseList = clubRelClassCourseService.getClubRelClassCourse(clubRelClassCourse);
		List<ClubTrainingCourse> resList = new ArrayList<ClubTrainingCourse>();
		if(null!=courseList && courseList.size()>0){
			for (int i = 0; i < courseList.size(); i++) {
				ClubRelClassCourse course = courseList.get(i);
				ClubTrainingCourse res = clubObjectConverts(course.getCourseId(),userId);
				//课程类型
				Integer coursetype = res.getCourseType();
				if(coursetype==BusinessConstant.COURSE_TYPE_1){
					res.setCourseSource(BusinessConstant.COURSE_TYPE_1);//官方课程
				}else{
					res.setCourseSource(BusinessConstant.COURSE_TYPE_2);//培训班教练(自定义课程)
				}
				res.setCourseShowType(course.getIsShow());
				res.setClassType(classType);
				res.setResLearningNum(course.getTotalStudyNum());//每个课程学习的人数
				if(requestSide==BusinessConstant.REQUESTSIDE_TYPE_1){//后端管理请求  返回所有课程
					resList.add(res);
				}else{//前端请求  隐藏的课程不返回
					if(course.getIsShow()!=3){
						resList.add(res);
					}
				}
			}
		}
		return resList;
	}
	
	/**
	 * 根据培训班id查询数量
	 * @param paramMap
	 * @return
	 */
	public Integer getClubCourseByClassIdCount(Map<String, Object> paramMap) {
		return clubRelClassCourseService.getClubCourseByClassIdCount(paramMap);
	}
	
	/**
	 * 对象转换
	 * @param teachcourse
	 * @param centeruser
	 * @return
	 */
	private ClubTrainingCourse clubObjectConverts(Integer courseId,Integer userId){
		//缓存中获取课程信息
		String cache = RedisComponent.findRedisObject(courseId, ClubTrainingCourse.class);
		ClubTrainingCourse clubcourseCache = JSONObject.parseObject(cache,ClubTrainingCourse.class);
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		List<PublicAttachment> otherList = null;
		
		//视频附件集合
		if(clubcourseCache.getVideoAttaId()!=null){
			PublicAttachment video = new PublicAttachment();
			video.setAttaId(clubcourseCache.getVideoAttaId());
			videoList = publicAttachmentService.getPublicAttachment(video);
		}
		
		//音频附件集合
		if(clubcourseCache.getAudioAttaId()!=null){
			PublicAttachment audio = new PublicAttachment();
			audio.setAttaId(clubcourseCache.getAudioAttaId());
			audiotList = publicAttachmentService.getPublicAttachment(audio);
		}
		
		//其他附件
		if(clubcourseCache.getCourseId()!=null){
			otherList = publicAttachmentService.getClubOtherPublicAttachment(clubcourseCache.getCourseId());
		}
		
		clubcourseCache.setVideoList(videoList==null?new ArrayList<PublicAttachment>():videoList);//视频
		clubcourseCache.setAudiotList(audiotList==null?new ArrayList<PublicAttachment>():audiotList);//音频
		clubcourseCache.setOtherList(otherList==null?new ArrayList<PublicAttachment>():otherList);//其他附件
		
		//学习状态 
		ClubRelClassCourseMember clubRelClassCourseMember = new ClubRelClassCourseMember();
		clubRelClassCourseMember.setCourseId(courseId);
		clubRelClassCourseMember.setStudentId(Integer.valueOf(userId));
		ClubRelClassCourseMember res = clubRelClassCourseMemberService.getClubRelClassCourseMember(clubRelClassCourseMember);
		if(null!=res){
			clubcourseCache.setResLearningStatus(res.getIsStudy() == null ? 0 : res.getIsStudy());
		}else{
			clubcourseCache.setResLearningStatus(0);
		}
		
		//课程图像
		String courseImg = Common.getImgUrl(clubcourseCache.getImageId(), BusinessConstant.DEFAULT_IMG_PRACTICE_COURSE);
		clubcourseCache.setResCourseLink(Common.checkPic(courseImg) == true ? courseImg+ActiveUrl.HEAD_MAP:courseImg);//课程图片链接
		
		String cacheUser = RedisComponent.findRedisObject(clubcourseCache.getCreateUserId(),CenterUser.class);
		CenterUser centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
		if(centeruser!=null){
			//教练图片信息
			String teacherImg = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			clubcourseCache.setResTeacherHeadLink(Common.checkPic(teacherImg) == true ? teacherImg+ActiveUrl.HEAD_MAP:teacherImg);//教练图片链接
			
			clubcourseCache.setResTeacherName(centeruser.getRealName());//教练名称
			
			//学校信息
			String cacheTeachSchool = RedisComponent.findRedisObject(centeruser.getSchoolId(), TeachSchool.class);
			TeachSchool	teachschool = JSONObject.parseObject(cacheTeachSchool,TeachSchool.class);
			clubcourseCache.setResSchoolName(teachschool==null?"":teachschool.getSchoolName());//学校名称
		}
		return clubcourseCache;
	}
	
	/**
	 * 俱乐部获取官方课程数量
	 * @param paramMap
	 * @return
	 */
	public Integer getClubTrainingCourseCount(Map<String, Object> paramMap) {
		return clubTrainingCourseService.getClubTrainingCourseCount(paramMap);
	}

	/**
	 * 俱乐部获取官方课程
	 * @param clubtrainingcourse
	 * @param centeruser
	 * @return
	 */
	public List<ClubTrainingCourse> getClubTrainingCourse(ClubTrainingCourse clubtrainingcourse,Integer userId,Integer classType) {
		List<ClubTrainingCourse> clubtrainingcourseList = clubTrainingCourseService.getClubTrainingCourseList(clubtrainingcourse);
		List<ClubTrainingCourse> resList = new ArrayList<ClubTrainingCourse>();
		if(null!=clubtrainingcourseList && clubtrainingcourseList.size()>0){
			for (int i = 0; i < clubtrainingcourseList.size(); i++) {
				ClubTrainingCourse course = clubtrainingcourseList.get(i);
				ClubTrainingCourse res = clubObjectConverts(course.getCourseId(),userId);
				res.setClassType(classType);//班级类型
				resList.add(res);
			}
		}
		return resList;
	}
	
	/**
	 * 根据课程id在培训班课程表查询数据
	 * @param centeruser
	 * @param courseId
	 * @param classId
	 * @return
	 */
	public ClubTrainingCourse getClubCourseByCourseId(Integer userId, Integer courseId, Integer classId, Integer classType) {
		//学习状态 
		ClubRelClassCourseMember clubRelClassCourseMember = new ClubRelClassCourseMember();
		clubRelClassCourseMember.setCourseId(courseId);
		clubRelClassCourseMember.setStudentId(userId);
		ClubRelClassCourseMember resMember = clubRelClassCourseMemberService.getClubRelClassCourseMember(clubRelClassCourseMember);
		if(resMember==null){
			ClubRelClassCourseMember insertMember = new ClubRelClassCourseMember();
			insertMember.setCourseId(courseId);
			insertMember.setStudentId(userId);
			insertMember.setIsStudy(0);
			clubRelClassCourseMemberService.insertClubRelClassCourseMember(insertMember);
		}
		
		ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
		clubRelClassCourse.setClassId(classId);
		clubRelClassCourse.setCourseId(courseId);
		ClubRelClassCourse clubrelclasscourse = clubRelClassCourseService.getClubRelClassCourseOne(clubRelClassCourse);
		ClubTrainingCourse res = clubObjectConverts(courseId,userId);
		if(clubrelclasscourse!=null){
			res.setResLearningNum(clubrelclasscourse.getTotalStudyNum()==null?0:clubrelclasscourse.getTotalStudyNum());
			res.setCourseShowType(clubrelclasscourse.getIsShow()==null?0:clubrelclasscourse.getIsShow());//课程显示类型
		}
		res.setClassType(classType);
		if(res.getCourseType()==BusinessConstant.COURSE_TYPE_1){
			res.setCourseSource(BusinessConstant.COURSE_TYPE_1);//来自官方
		}else{
			res.setCourseSource(BusinessConstant.COURSE_TYPE_2);//来自教练
		}
		return res;
	}

	public void delTeachCourseAll(List<TeachCourse> delTeachCourse) {
		teachCourseDao.delTeachCourseAll(delTeachCourse);
	}

	/**
	 * 根据章节id和所属课程卡类型获取课程列表
	 * @param chapterId	章节id
	 * @param ccType	课程卡类型
	 * @return
	 * @author 			lw
	 * @param limit 
	 * @param start 
	 * @param classType 
	 * @param userId 
	 * @param integer 
	 * @param requestSide 请求端
	 * @param integer 
	 * @date			2016年7月26日  下午7:49:43
	 */
	public JSONObject findTeachCourseByChapterAndCcType(Map<String, Object> paramMap, Integer start, Integer limit, Integer teachingRole, Integer classType, Integer userId, Integer ccType) {
		
		String redisData = null;
		QueryPage<TeachRelCardCourse> queryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachRelCardCourse.class);
		if(queryPage.getState()){
			CenterUser teacher = null;
			PublicPicture teacherPic = null;
			TeachSchool school = null;
			if( queryPage.getList().size() > 0 ){
				
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
			this.assembleCourse(queryPage.getList(), classType, userId, teacher, teacherPic, school, ccType);
		}
		paramMap.clear();
		paramMap.put("teachingRole", teachingRole);
		
		return queryPage.getMessageJSONObject("courses", paramMap);
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
	 * @param ccType 
	 * @date			2016年7月26日  下午8:09:00
	 */
	public void assembleCourse(List<TeachRelCardCourse> list, Integer classType, Integer userId, CenterUser teacher, PublicPicture teacherPic, TeachSchool school, Integer ccType) {
		
		String redisData = null;
		PublicPicture pic = null;
		TeachCourse course = null;
		PublicAttachment att = null;
		List<PublicAttachment> otherFiles = null;
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		TeachRelStudentCourse relStudentCourse = null;
		JSONArray arrJson = null;
		for(TeachRelCardCourse en : list){
			
			//班级类型
			en.setClassType(classType);
			//课程卡类型
			en.setCcType(ccType);
			
			//查询课程
			redisData = RedisComponent.findRedisObject(en.getCourseId(), TeachCourse.class);
			if(redisData != null){
				course = JSONObject.parseObject(redisData, TeachCourse.class);
				
				en.setCourseId(course.getCourseId());
				en.setHomeworkTitle(course.getCourseTitle());
				en.setCourseDesc(course.getCourseExplain());
				en.setCourseCardId(String.valueOf(en.getCardId()));
				//课程来源
				if(course.getCourseType() == BusinessConstant.COURSE_TYPE_1 || course.getCourseType() == BusinessConstant.COURSE_TYPE_5){
					//官方
					en.setCourseSource(1);
				}else{
					//教师(或培训班教练)；
					en.setCourseSource(2);
				}
				
				//获取课程图片链接地址
				redisData = RedisComponent.findRedisObject(course.getImageId(), PublicPicture.class);
				if(redisData != null){
					pic = JSONObject.parseObject(redisData, PublicPicture.class);
					en.setCourseLink(Common.checkPic(pic.getFilePath())==true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
				}
				String courseImg = "";
				if(course.getCourseType() == 1 || course.getCourseType() == 5){
					courseImg = Common.getImgUrl(course.getImageId(), BusinessConstant.DEFAULT_IMG_PRACTICE_COURSE);
				}else if(course.getCourseType() == 2){
					courseImg = Common.getImgUrl(course.getImageId(), BusinessConstant.DEFAULT_IMG_CUSTOM_COURSE);
				}
				en.setCourseLink(courseImg);//课程图片链接

				/*
				//获取视频附件信息
				redisData = RedisComponent.findRedisObject(course.getVideoAttaId() , PublicAttachment.class);
				if(redisData != null){
					att = JSONObject.parseObject(redisData, PublicAttachment.class);
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
					arrJson = new JSONArray();
					arrJson.add(att);
					en.setAudioFiles(arrJson);
				}else{
					en.setAudioFiles(new JSONArray());
				}*/
				
				
				//根据课程id查询课程附件关系表获取附件id
				if(course.getCourseId() != null){
					//其它附件
					otherFiles =this.returnAttment(course.getCourseId(), 3);
					//音频附件
					audiotList =this.returnAttment(course.getCourseId(), 2);
					//视频附件
					videoList =	this.returnAttment(course.getCourseId(), 1);
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
						en.setTeacherName(teacher.getRealName());
						
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
				relStudentCourse = new TeachRelStudentCourse();
				relStudentCourse.setCourseId(course.getCourseId());
				relStudentCourse.setStudentId(userId);
				relStudentCourse = teachRelStudentCourseService.getTeachRelStudentCourse(relStudentCourse);
				en.setLearningStatus(relStudentCourse == null ? 0 : 1);
				
			}
		}
	}

	
	/**
	 * 手机端：查询班级详情
	 * @param courseId
	 * @return
	 * @author 			lw
	 * @param classId 
	 * @param userId 
	 * @param teachingRole 
	 * @param classType 
	 * @date			2016年7月27日  上午12:59:33
	 */
	public JSONObject findOneTeachCourse(Integer courseId, Integer userId, Integer classId, Integer teachingRole, int classType) {
		TeachRelCardCourse en = new TeachRelCardCourse();
		TeachCourse course = new TeachCourse();
		course.setCourseId(courseId);
		course = teachCourseDao.selectTeachCourse(course);
		
		PublicAttachment att = null;
		JSONObject rich = null;
		JSONArray arrJson = null;
		List<PublicAttachment> otherFiles = null;
		List<PublicAttachment> videoList = null;
		List<PublicAttachment> audiotList = null;
		if(course != null){
			en.setCourseId(course.getCourseId());
			en.setHomeworkTitle(course.getCourseTitle());
			en.setCourseDesc(course.getCourseExplain());
			en.setShowType(course.getCourseType());
			
			//课程来源
			if(course.getCourseType() == BusinessConstant.COURSE_TYPE_1 || course.getCourseType() == BusinessConstant.COURSE_TYPE_5){
				//官方
				en.setCourseSource(1);
			}else{
				//教师(或培训班教练)；
				en.setCourseSource(2);
			}
			//班级类型
			en.setClassType(classType);
			
			//获取课程图片链接地址
			String redisData = RedisComponent.findRedisObject(course.getImageId(), PublicPicture.class);
			if(redisData != null){
				PublicPicture pic = JSONObject.parseObject(redisData, PublicPicture.class);
				en.setCourseLink(Common.checkPic(pic.getFilePath())==true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
			}
			/*
			//获取视频附件信息
			redisData = RedisComponent.findRedisObject(course.getVideoAttaId() , PublicAttachment.class);
			if(redisData != null){
				att = JSONObject.parseObject(redisData, PublicAttachment.class);
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
				arrJson = new JSONArray();
				arrJson.add(att);
				en.setAudioFiles(arrJson);
			}else{
				en.setAudioFiles(new JSONArray());
			}
			
			//获取其他附件
			List<PublicAttachment> otherFiles = publicAttachmentService.getOtherPublicAttachment(course.getCourseId());
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
				//根据课程id查询课程附件关系表获取附件id
				if(course.getCourseId() != null){
					//其它附件
					otherFiles =this.returnAttment(course.getCourseId(), 3);
					//音频附件
					audiotList =this.returnAttment(course.getCourseId(), 2);
					//视频附件
					videoList =	this.returnAttment(course.getCourseId(), 1);
				}
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
				redisData = RedisComponent.findRedisObject(course.getTeacherId(), CenterUser.class);
				if(redisData != null ){
					CenterUser teacher = JSONObject.parseObject(redisData, CenterUser.class);
					en.setTeacherName(teacher.getRealName());
					
					redisData = RedisComponent.findRedisObject(course.getTeacherId(), PublicPicture.class);
					//教师图片地址
					if(redisData != null){
						PublicPicture teacherPic = JSONObject.parseObject(redisData, PublicPicture.class);
						en.setTeacherHeadLink(Common.checkPic(teacherPic.getFilePath()) == true ?teacherPic.getFilePath()+ActiveUrl.HEAD_MAP:teacherPic.getFilePath());
					}
					
					redisData = RedisComponent.findRedisObject(teacher.getSchoolId(), TeachSchool.class);
					//学校名称
					if(redisData != null){
						TeachSchool school = JSONObject.parseObject(redisData, TeachSchool.class);
						en.setSchoolName(school.getSchoolName());
					}
				}
			}
			
			//课程卡
			TeachRelCardCourse reCardCourse = new TeachRelCardCourse();
			reCardCourse.setCourseId(course.getCourseId());
			reCardCourse = teachRelCardCourseService.selectTeachRelCardCourse(reCardCourse);
			if(reCardCourse != null){
				
				redisData = RedisComponent.findRedisObject(reCardCourse.getCardId(), TeachCourseCard.class);
				if(redisData != null){
					TeachCourseCard card = JSONObject.parseObject(redisData, TeachCourseCard.class);
					en.setCardId(card.getCardId());
					en.setCcTitle(card.getCardTitle());
					en.setCcType(card.getCardType());
					
					//章节信息chapter
					redisData = RedisComponent.findRedisObject(card.getChapterId(), TeachCourseChapter.class);
					if(redisData != null){
						TeachCourseChapter chapter = JSONObject.parseObject(redisData, TeachCourseChapter.class);
						en.setChapterId(chapter.getChapterId());
						en.setChapterTitle(chapter.getChapterName());
					}
					
					//保存学习人次
					try {
						reCardCourse.setTotalStudyNum(reCardCourse.getTotalStudyNum()+1);
						teachRelCardCourseService.updateTeachRelCardCourse(reCardCourse);
						LogUtil.info(this.getClass(),  "assembleCourse", "课程卡课程学习人次保存/修改成功!");
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.error(this.getClass(), "assembleCourse", AppErrorCode.ERROR_TEACH_REL_CARD_COURSE, e);
					}
				}
			}
			
			//学习状态
			TeachRelStudentCourse relStudentCourse = new TeachRelStudentCourse();
			relStudentCourse.setCourseId(course.getCourseId());
			relStudentCourse.setStudentId(userId);
			relStudentCourse = teachRelStudentCourseService.getTeachRelStudentCourse(relStudentCourse);
			if(relStudentCourse == null){
				en.setLearningStatus(0);
				relStudentCourse = new TeachRelStudentCourse();
				relStudentCourse.setCourseId(course.getCourseId());
				relStudentCourse.setStudentId(userId);
				relStudentCourse.setIsStudy(0);
				try {
					//保存学生班级表
					teachRelStudentCourseService.insertTeachRelStudentCourse(relStudentCourse);
					LogUtil.info(this.getClass(),  "assembleCourse", "学生班级表保存成功!");
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(this.getClass(), "assembleCourse", AppErrorCode.ERROR_TEACH_REL_STUDENT_COURSE, e);
				}
			}else{
				en.setLearningStatus(1);
			}
			
			
			
			//app内容封装
			rich = RichTextUtil.parseRichText(course.getCourseContent());
			if(rich != null){
				en.setHomeworkBody(rich.getString("content"));
				en.setLinks(rich.getJSONArray("links"));
				en.setImgs(rich.getJSONArray("imgs"));
			}
			
		}else{
			en.setLinks(new JSONArray());
			en.setImgs(new JSONArray());
		}
		List<TeachRelCardCourse> list = new ArrayList<TeachRelCardCourse>();
		list.add(en);
		JSONObject json = new JSONObject();
		json.put("courses", JSONArray.toJSON(list));
		json.put("teachingRole", teachingRole);
		return Common.getReturn(AppErrorCode.SUCCESS, null, json);
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