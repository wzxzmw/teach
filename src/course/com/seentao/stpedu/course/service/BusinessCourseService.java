package com.seentao.stpedu.course.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.PageObject;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachCourseDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubRelClassCourse;
import com.seentao.stpedu.common.entity.ClubRelCourseAttachment;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachRelClassCourse;
import com.seentao.stpedu.common.entity.TeachRelCourseAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentAttachment;
import com.seentao.stpedu.common.entity.TeachRelStudentCourse;
import com.seentao.stpedu.common.service.BaseTeachCourseService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubRelClassCourseService;
import com.seentao.stpedu.common.service.ClubRelCourseAttachmentService;
import com.seentao.stpedu.common.service.ClubTrainingClassService;
import com.seentao.stpedu.common.service.ClubTrainingCourseService;
import com.seentao.stpedu.common.service.PublicAttachmentService;
import com.seentao.stpedu.common.service.TeachClassService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseService;
import com.seentao.stpedu.common.service.TeachRelCardCourseService;
import com.seentao.stpedu.common.service.TeachRelClassCourseService;
import com.seentao.stpedu.common.service.TeachRelCourseAttachmentService;
import com.seentao.stpedu.common.service.TeachRelStudentAttachmentService;
import com.seentao.stpedu.common.service.TeachRelStudentCourseService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author yy
* @date 2016年6月17日 下午8:09:44 
*/
@Service
public class BusinessCourseService {
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	@Autowired
	private BaseTeachCourseService baseTeachCourseService;
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private TeachRelTeacherClassService teachRelTeacherClassService;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	@Autowired
	private TeachRelClassCourseService teachRelClassCourseService;
	@Autowired
	private ClubTrainingCourseService clubTrainingCourseService;
	@Autowired
	private ClubRelClassCourseService clubRelClassCourseService;
	@Autowired
	private ClubTrainingClassService clubTrainingClassService;
	@Autowired
	private TeachRelCourseAttachmentService teachRelCourseAttachmentService;
	@Autowired
	private ClubRelCourseAttachmentService clubRelCourseAttachmentService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private TeachCourseService teachCourseService;
	@Autowired
	private PublicAttachmentService publicAttachmentService;
	@Autowired
	private TeachRelStudentCourseService teachRelStudentCourseService;
	@Autowired
	private TeachRelStudentAttachmentService teachRelStudentAttachmentService;
	@Autowired
	private TeachCourseDao teachCourseDao;
	
	
	/**
	 * 预制课程
	 * @param playTime 附件表播放时长
	 * @param ossVideoId 上传OSS后返回的id 
	 * @param fileName 附件表文件名
	 * @param chapterId 第几章
	 * @param courseCardId 第几个课程卡
	 * @param courseTitle 课程标题
	 * @return
	 */
	public String createCourse(Integer playTime,String ossVideoId,String fileName, Integer chapterId, Integer courseCardId, String courseTitle) {
		//获取文件后缀
		String suffix = fileName.substring(fileName.indexOf(".")+1, fileName.length());
		//插入附件表  返回附件主键
		PublicAttachment publicAttachment = new PublicAttachment();
		publicAttachment.setAttaType(1);//附件类型 1:视频，2:音频，3:其他。
		publicAttachment.setFileName(fileName);//文件名
		publicAttachment.setFilePath(ossVideoId);//文件路径OSS上传后返回的id
		publicAttachment.setDownloadUrl(null);//下载地址
		publicAttachment.setSuffixName(suffix);//后缀名
		publicAttachment.setSize(null);//文件大小
		publicAttachment.setTimeLength(playTime);//播放时长
		publicAttachment.setCreateTime(0);
		publicAttachment.setCreateUserId(0);
		publicAttachmentService.insertPublicAttachment(publicAttachment);
		//确定课程卡id
		Integer cardid = getCourseCardId(0, chapterId, courseCardId);
		if(null==cardid){
			LogUtil.info(this.getClass(), "createCourse", "课程信息不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NOTFOUND_COURSE).toJSONString();//没有班级存在
		}
		//插入课程表
		TeachCourse teachcourse = new TeachCourse();
		teachcourse.setVideoAttaId(publicAttachment.getAttaId());
		teachcourse.setCourseTitle(courseTitle);
		teachcourse.setClassType(1);
		teachcourse.setTeacherId(0);
		teachcourse.setCreateTime(0);
		teachcourse.setIsDelete(0);
		teachcourse.setImageId(-1);
		teachCourseService.insertTeachCourse(teachcourse);
		//添加课程附件关系表
		TeachRelCourseAttachment teachRelCourseAttachment = new TeachRelCourseAttachment();
		teachRelCourseAttachment.setCourseId(teachcourse.getCourseId());
		teachRelCourseAttachment.setAttaId(publicAttachment.getAttaId());
		teachRelCourseAttachmentService.insertTeachRelCourseAttachment(teachRelCourseAttachment);
		//插入关系表
		TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
		teachRelCardCourse.setCardId(cardid);
		teachRelCardCourse.setCourseId(teachcourse.getCourseId());
		teachRelCardCourse.setShowType(1);
		teachRelCardCourse.setCreateTime(0);
		teachRelCardCourse.setPlanNum(0);
		teachRelCardCourse.setActualNum(0);
		teachRelCardCourse.setTotalStudyNum(0);
		teachRelCardCourseService.insertTeachRelCardCourse(teachRelCardCourse);
		//对于已创建的班级进行预制数据的增加
		//查询未删除的班级进行遍历
		List<Integer> listClass = teachClassService.getAllClassId();
		if(null!=listClass && listClass.size()>0){
			for (int i = 0; i < listClass.size(); i++) {
				Integer cardId = getCourseCardId(listClass.get(i), chapterId, courseCardId);
				if(cardId != null ){
					//根据课程id和课程卡id查询关系表判断是否已有数据
					TeachRelCardCourse teachrelcardcourses = new TeachRelCardCourse();
					teachrelcardcourses.setCardId(cardId);
					teachrelcardcourses.setCourseId(teachcourse.getCourseId());
					TeachRelCardCourse isteachrelcardcourse =teachRelCardCourseService.getSingleTeachRelCardCourse(teachrelcardcourses);
					if(isteachrelcardcourse == null ){
						//插入关系表
						TeachRelCardCourse teachrelcardcourse = new TeachRelCardCourse();
						teachrelcardcourse.setCardId(cardId);
						teachrelcardcourse.setCourseId(teachcourse.getCourseId());
						teachrelcardcourse.setShowType(1);
						teachrelcardcourse.setCreateTime(0);
						teachrelcardcourse.setPlanNum(0);
						teachrelcardcourse.setActualNum(0);
						teachrelcardcourse.setTotalStudyNum(0);
						teachRelCardCourseService.insertTeachRelCardCourse(teachrelcardcourse);
					}
				}
			}
		}else{
			LogUtil.info(this.getClass(), "createCourse", "导入预制数据时班级不存在");
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_NOTFOUND_CLASS).toJSONString();//没有班级存在
		}
		LogUtil.info(this.getClass(), "createCourse", "成功");
		return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
	}

	private Integer getCourseCardId(Integer calssId, Integer chapterNo, Integer courseCardId) {
		Integer cardId = null;
		TeachCourseCard parm = new TeachCourseCard();
		parm.setStartTime(chapterNo);
		parm.setEndTime(calssId);
		List<TeachCourseCard> list = teachCourseCardService.selectByChapterId(parm);
		if(null!=list && list.size()>0){
			TeachCourseCard t = list.get(courseCardId-1);
			cardId = t.getCardId();
		}
		return cardId;
	}

	/**
	 * 获取课程卡信息
	 * @param classId
	 * @param chapterId
	 * @param courseCardId
	 * @param inquireType
	 * @return
	 */
	public String getCourseCards(Integer classId, Integer chapterId, Integer courseCardId, Integer inquireType) {
		LogUtil.info(this.getClass(), "getCourseCards", "classId="+classId+",chapterId="+chapterId+",courseCardId="+courseCardId+
				",inquireType="+inquireType);
		JSONObject jo = new JSONObject();
		try {
			if(inquireType==GameConstants.QUERY_TYPE_THREE){//课程卡id
				List<TeachCourseChapter> list = new ArrayList<TeachCourseChapter>();
				TeachCourseChapter t = teachCourseCardService.getCourseCardByCardId(courseCardId);
				if(t!=null){
					list.add(t);
				}
				jo.put("chapters",list);
				LogUtil.info(this.getClass(), "getCourseCards", "获取课程卡信息成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "",jo).toJSONString();
			}else if(inquireType==GameConstants.QUERY_TYPE_TWO){//章节id 
				List<TeachCourseChapter> list = teachCourseCardService.getCourseCardByChapterId(chapterId);
				jo.put("chapters", list==null?new ArrayList<TeachCourseChapter>():list);
				LogUtil.info(this.getClass(), "getCourseCards", "获取课程卡信息成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "",jo).toJSONString();
			}else if(inquireType==GameConstants.QUERY_TYPE_ONE){//班级id
				List<TeachCourseChapter> list = teachCourseCardService.getCourseCardByClassId(classId);
				jo.put("chapters",list==null?new ArrayList<TeachCourseChapter>():list);
				LogUtil.info(this.getClass(), "getCourseCards", "获取课程卡信息成功");
				return Common.getReturn(AppErrorCode.SUCCESS, "",jo).toJSONString();
			}else{
				LogUtil.error(this.getClass(), "getCourseCards", "查询类型不正确");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(BusinessCourseService.class, "getCourseCards", "获取课程卡信息异常",e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, BusinessConstant.ERROR_TEACHER_COURSE_CARD).toJSONString();
		}
	}
	
	/**
	 * 提交自定义课程卡(只是修改)
	 * @param userId
	 * @param courseCardId
	 * @param ccTitle
	 * @param ccDesc
	 * @param ccLinkId
	 * @param ccStartDate
	 * @param ccEndDate
	 * @return
	 */
	@Transactional
	public String updateCourseCard(Integer userId, Integer courseCardId, String ccTitle, String ccDesc, Integer ccLinkId,
			Integer ccStartDate, Integer ccEndDate) {
		LogUtil.info(this.getClass(), "updateCourseCard", "提交自定义课程卡开始调用,courseCardId="+courseCardId+",userId="+userId);
		String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
		for(String s:str){
			if(ccTitle.contains(s)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TITLE_NOT_REGULAR).toJSONString();
			}
			if(ccDesc.contains(s)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TITLE_NOT_CONTEXT).toJSONString();
			}
		}
		try {
			//查询该课程卡id是否存在
			TeachCourseChapter teachcoursechapter = teachCourseCardService.getCourseCardByCardId(courseCardId);
			TeachCourseCard t = null;
			if(teachcoursechapter != null && teachcoursechapter.getCourseCards() != null 
					&&  (t = teachcoursechapter.getCourseCards().get(0)) != null){
				//用户是否是教师类型 
				boolean isTeacher = centerUserService.isTeacher(userId);
				//课程卡班级和老师在的班级是否一致
				boolean isEquClass = teachRelTeacherClassService.isEqualClass(userId, teachcoursechapter.getClassId());
				if(isTeacher&&isEquClass){
					//校验标题与说明
					int title = StringUtil.StringVerification(ccTitle, BusinessConstant.TILE_LENGTH, true);
					if(title!=3){
						return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.SUBMIT_COURSE_ERROR_TITLE[title]).toJSONString();
					}
					int desc = StringUtil.StringVerification(ccDesc, BusinessConstant.DESC_LENGTH, false);
					if(desc!=3){
						return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.SUBMIT_COURSE_ERROR_DESC[desc]).toJSONString();
					}
					t.setCardId(courseCardId);
					t.setCardTitle(ccTitle);
					t.setTeacherId(userId);
					t.setCardExplain(ccDesc);
					t.setImageId(ccLinkId);
					if(ccEndDate!=0&&ccStartDate!=0){
						t.setEndTime(ccEndDate);
						t.setStartTime(ccStartDate);
						t.setStatusId(GameConstants.CARD_STATUS_1);//未开始
					}
					teachCourseCardService.updateCourseCard(t);
					JedisCache.delRedisVal(TeachCourseCard.class.getSimpleName(), String.valueOf(courseCardId));
					LogUtil.info(this.getClass(), "updateCourseCard", "提交自定义课程卡成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}else{
					if(!isTeacher){
						LogUtil.error(this.getClass(), "updateCourseCard", "用户不是老师");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_IS_TEACHER).toJSONString();
					}else{
						LogUtil.error(this.getClass(), "updateCourseCard", "用户班级与课程卡班级不一致");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_ISQEU_CLASS).toJSONString();
					}
				}
			}else{
				LogUtil.error(this.getClass(), "updateCourseCard", "要修改的课程卡不存在");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TEACHCOURSECARD).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(BusinessCourseService.class, "updateCourseCard", "更新自定义课程卡信息异常!",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Common.getReturn(AppErrorCode.ERROR_UPDATE, BusinessConstant.ERROR_UPDATE).toJSONString();
		}
	}
	
	/**
	 * 提交自定义课程信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param courseId 课程id
	 * @param courseTitle	课程标题
	 * @param courseDesc	课程说明
	 * @param courseShowType	课程显示类型
	 * @param courseBody 课程内容
	 * @param courseCardId	所属课程卡id
	 * @param audioFileIds 音频附件id集合
	 * @param otherFileIds	其它附件id集合
	 * @param classType	班级类型
	 */
	@Transactional
	public String addOrUpdateCourse(Integer userId, String courseId, String courseTitle, String courseDesc,
			Integer courseShowType, String courseBody, Integer courseCardId, String audioFileIds,
			String otherFileIds,Integer classType,String classId) {
		LogUtil.info(this.getClass(), "addOrUpdateCourse", "接口开始调用,userId="+userId+",courseId="+courseId);
		try {
			String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
			for(String s:str){
				if(courseTitle.contains(s))
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TITLE_NOT_REGULAR).toJSONString();
			}
			//判断课程内容在5000字以内
			boolean isCourseBody = Common.isValid(courseBody.trim(), 0, 5000);
			if(!isCourseBody){
				LogUtil.info(this.getClass(), "addOrUpdateCourse","提交自定义课程内容不能超过5000字 ");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, BusinessConstant.IS_CONTEXT).toJSONString();
			}
			//校验标题与说明
			int title = StringUtil.StringVerification(courseTitle, BusinessConstant.TILE_LENGTH, true);
			if(title!=3){//未成功
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.SUBMIT_COURSE_ERROR_TITLE[title]).toJSONString();
			}
			if(classType==GameConstants.TEACHING_CLASS){//教学班1
				//判断用户角色 用户是否是教师
				String uesrCache = RedisComponent.findRedisObject(userId,CenterUser.class);
				CenterUser centeruser = JSONObject.parseObject(uesrCache,CenterUser.class);
				if(centeruser.getUserType()==GameConstants.USER_TYPE_TEAHCER){
					TeachCourse t = new TeachCourse();
					t.setCourseTitle(courseTitle);
					t.setCourseExplain(courseDesc);
					t.setTeacherId(userId);
					t.setCourseContent(courseBody);
					//根据id判断是新增还是修改
					if(courseId.equals("")){//add
						t.setCreateTime(TimeUtil.getCurrentTimestamp());
						t.setIsDelete(GameConstants.NO_DEL);
						t.setCourseType(BusinessConstant.COURSE_TYPE_2);//课程类型。1:官方课程，2:自定义课程，3:文本作业，4:实训作业。5:案例课程。
						baseTeachCourseService.addCourse(t);
						//关系表新增数据
						TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
						teachRelCardCourse.setCardId(courseCardId);
						teachRelCardCourse.setCourseId(t.getCourseId());
						teachRelCardCourse.setShowType(courseShowType);
						teachRelCardCourse.setCreateTime(TimeUtil.getCurrentTimestamp());
						teachRelCardCourse.setPlanNum(0);
						teachRelCardCourse.setActualNum(0);
						teachRelCardCourse.setTotalStudyNum(0);
						teachRelCardCourseService.insertTeachRelCardCourse(teachRelCardCourse);
						// add by chengshx 2016-10-27
						teachRelStudentCourseService.updateTeachRelStudentCourseCount(centeruser.getClassId());
						LogUtil.info(this.getClass(), "addOrUpdateCourse", "(教学班)插入teach_course,teach_rel_card_course成功");
						//批量插入其它附件
						this.submitAccessory(otherFileIds, t.getCourseId());
						//批量插入音频附件
						this.submitAccessory(audioFileIds, t.getCourseId());
						//更新原创数量
						Integer cla = centeruser.getClassId();
						if(cla!=null){
							TeachClass teachClass = new TeachClass();
							teachClass.setClassId(cla);
							teachClassService.updateOriginalCourseNum(teachClass);
						}
						return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
					}else{//update
						//是否存在课程
						TeachCourse teachCourse = new TeachCourse();
						teachCourse.setCourseId(Integer.valueOf(courseId));
						TeachCourse resCourse = baseTeachCourseService.selectTeachCourse(teachCourse);
						if(null!=resCourse){
							t.setCourseId(Integer.valueOf(courseId));
							baseTeachCourseService.updateCourse(t);
							//关系表更新数据
							TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
							teachRelCardCourse.setCardId(courseCardId);
							teachRelCardCourse.setCourseId(Integer.valueOf(courseId));
							teachRelCardCourse.setShowType(courseShowType);
							teachRelCardCourseService.updateTeachRelCardCourse(teachRelCardCourse);
							//删除缓存
							JedisCache.delRedisVal(TeachCourse.class.getSimpleName(), String.valueOf(courseId));
							//删除 
							if(courseId!=null && courseId!=""){
								TeachRelCourseAttachment delete = new TeachRelCourseAttachment();
								delete.setCourseId(Integer.valueOf(courseId));
								teachRelCourseAttachmentService.deleteTeachRelCourseAttachment(delete);
								//批量插入附件
								this.submitAccessory(otherFileIds, Integer.valueOf(courseId));
								//批量插入音频附件
								this.submitAccessory(audioFileIds, Integer.valueOf(courseId));
							}
							LogUtil.info(this.getClass(), "addOrUpdateCourse", "(教学班)修改teach_course,teach_rel_card_course成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
						}else{
							LogUtil.error(this.getClass(), "addOrUpdateCourse", "要修改的课程不存在");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.COURSE_NOT_EXIST).toJSONString();
						}
					}
				}else{
					//用户不是教师
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_IS_TEACHER).toJSONString();
				}
			}else if(classType==GameConstants.CLUB_TRAIN_CLASS){//培训班2
				int desc = StringUtil.StringVerification(courseDesc, BusinessConstant.DESC_LENGTH, false);
				if(desc!=3){//未成功
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.SUBMIT_COURSE_ERROR_DESC[desc]).toJSONString();
				}
				ClubMember clubMember = new ClubMember();
				clubMember.setUserId(userId);
				clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
				ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
				if(resClubMember!=null){
					//用户角色是教练或者会长
					if(resClubMember.getLevel()==GameConstants.CLUB_MEMBER_LEVEL_COACH||resClubMember.getLevel()==GameConstants.CLUB_MEMBER_LEVEL_PRESIDENT){
						//培训班课程
						ClubTrainingCourse clubCourse = new ClubTrainingCourse();
						clubCourse.setCreateTime(TimeUtil.getCurrentTimestamp());//创建时间
						clubCourse.setCourseTitle(courseTitle);//标题
						clubCourse.setCourseExplain(courseDesc);//说明
						if(!StringUtil.isEmpty(audioFileIds)){
							clubCourse.setAudioAttaId(Integer.valueOf(audioFileIds));//音频
						}
						/*if(!audioFileIds.equals("")){
						}*/
						clubCourse.setCreateUserId(userId);//创建人
						clubCourse.setCourseContent(courseBody);//内容
						if(courseId.equals("")){//add
							//校验班级id
							if(!StringUtil.isEmpty(classId)){
								//培训课程表 新增数据
								clubCourse.setTotalStudyNum(0);
								clubCourse.setIsDelete(GameConstants.NO_DEL);//未删除0
								clubCourse.setCourseType(BusinessConstant.COURSE_TYPE_2);//自定义课程
								clubTrainingCourseService.insertClubTrainingCourse(clubCourse);
								//培训班课程关系表 新增数据
								ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
								clubRelClassCourse.setClassId(Integer.valueOf(classId));//培训班id
								clubRelClassCourse.setCourseId(clubCourse.getCourseId());
								clubRelClassCourse.setCreateTime(TimeUtil.getCurrentTimestamp());
								clubRelClassCourse.setIsShow(courseShowType);
								clubRelClassCourse.setTotalStudyNum(0);
								clubRelClassCourseService.insertClubRelClassCourse(clubRelClassCourse);
								//批量插入附件
								if(!StringUtil.isEmpty(otherFileIds)){
									String[] others = otherFileIds.split(",");
									List<ClubRelCourseAttachment> list = new ArrayList<ClubRelCourseAttachment>();
									for (int i = 0; i < others.length; i++) {
										ClubRelCourseAttachment clubRelCourseAttachment = new ClubRelCourseAttachment();
										clubRelCourseAttachment.setCourseId(clubCourse.getCourseId());
										clubRelCourseAttachment.setAttaId(Integer.valueOf(others[i]));
										list.add(clubRelCourseAttachment);
									}
									clubRelCourseAttachmentService.batchInsertClubRelCourseAttachment(list);
								}
								//更新课程数量
								ClubTrainingClass updateClubTrainingClass = new ClubTrainingClass();
								updateClubTrainingClass.setClassId(Integer.valueOf(classId));
								updateClubTrainingClass.setCourseNum(1);
								clubTrainingClassService.updateClubTrainingClassNumByKey(updateClubTrainingClass);
								LogUtil.info(this.getClass(), "addOrUpdateCourse", "(培训班)插入club_training_course,club_rel_class_course成功");
								return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
							}else{
								LogUtil.error(this.getClass(), "addOrUpdateCourse", "俱乐部下没有班级");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.CLUB_NOT_EXIST_CLASS).toJSONString();
							}
						}else{//update
							//判断要修改的课程是否存在  
							ClubTrainingCourse clubTrainingCourse = new ClubTrainingCourse();
							clubTrainingCourse.setCourseId(Integer.valueOf(courseId));
							ClubTrainingCourse resClubTrainingCourse = clubTrainingCourseService.getClubTrainingCourse(clubTrainingCourse);
							if(null!=resClubTrainingCourse){//修改课程信息
								clubCourse.setCourseId(Integer.valueOf(courseId));
								clubTrainingCourseService.updateClubTrainingCourse(clubCourse);
								
								ClubTrainingClass clubTrainingClass = new ClubTrainingClass();
								clubTrainingClass.setClubId(resClubMember.getClubId());
								clubTrainingClass.setCreateUserId(userId);
								ClubTrainingClass resClubTrainingClass = clubTrainingClassService.getClubTrainingClass(clubTrainingClass);
								
								ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
								clubRelClassCourse.setCourseId(Integer.valueOf(courseId));
								clubRelClassCourse.setIsShow(courseShowType);
								clubRelClassCourse.setClassId(resClubTrainingClass.getClassId());
								clubRelClassCourseService.updateClubRelClassCourse(clubRelClassCourse);
								//删除缓存
								JedisCache.delRedisVal(ClubTrainingCourse.class.getSimpleName(), String.valueOf(courseId));
								//删除附件
								if(courseId!=null && courseId!=""){
									ClubRelCourseAttachment delete = new ClubRelCourseAttachment();
									delete.setCourseId(Integer.valueOf(courseId));
									clubRelCourseAttachmentService.deleteClubRelCourseAttachment(delete);
									//批量插入附件
									if(!StringUtil.isEmpty(otherFileIds)){
										String[] others = otherFileIds.split(",");
										List<ClubRelCourseAttachment> list = new ArrayList<ClubRelCourseAttachment>();
										for (int i = 0; i < others.length; i++) {
											ClubRelCourseAttachment clubRelCourseAttachment = new ClubRelCourseAttachment();
											clubRelCourseAttachment.setCourseId(clubCourse.getCourseId());
											clubRelCourseAttachment.setAttaId(Integer.valueOf(others[i]));
											list.add(clubRelCourseAttachment);
										}
										clubRelCourseAttachmentService.batchInsertClubRelCourseAttachment(list);
									}
								}
								LogUtil.info(this.getClass(), "addOrUpdateCourse", "(培训班)修改club_training_course,club_rel_class_course成功");
								return Common.getReturn(AppErrorCode.SUCCESS,"").toJSONString();
							}else{
								LogUtil.error(this.getClass(), "addOrUpdateCourse", "培训班课程不存在");
								return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE,BusinessConstant.COURSE_NOT_EXIST).toJSONString();
							}
						}
					}else{
						LogUtil.error(this.getClass(), "addOrUpdateCourse", "用户不是教练");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.USER_NOT_COACH).toJSONString();
					}
				}else{
					LogUtil.error(this.getClass(), "addOrUpdateCourse", "该用户都不是俱乐部成员");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.CLUB_USER_NOT_EXIST).toJSONString();
				}
			}else{
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(BusinessCourseService.class, "addOrUpdateCourse", "更新或添加自定义课程信息异常!",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Common.getReturn(AppErrorCode.ERROR_UPDATE, BusinessConstant.ERROR_INSERT).toJSONString();
		}
	}
	
	/**
	 * 设置课程显示类型
	 * @param courseId
	 * @param courseShowType
	 * @return
	 */
	public String updateCourse(Integer userId,Integer courseId, Integer courseShowType,Integer classType,String classId, String courseCardId) {
		LogUtil.info(this.getClass(), "updateCourse", "接口开始调用userId="+userId+",courseId="+courseId+
				",courseShowType="+courseShowType+",classType="+classType);
		try {
			if(classType==GameConstants.TEACHING_CLASS){
			 String uesrCache = RedisComponent.findRedisObject(userId,CenterUser.class);
			 CenterUser centeruser = JSONObject.parseObject(uesrCache,CenterUser.class);
			 	//是否为教师 1
				if(centeruser.getUserType()==GameConstants.USER_TYPE_TEAHCER){
					//判断课程类型是案例课程还是基础课程
					if(!courseCardId.equals("")){
						TeachRelCardCourse teachRelCardCourse = new TeachRelCardCourse();
						teachRelCardCourse.setCardId(Integer.valueOf(courseCardId));
						teachRelCardCourse.setCourseId(courseId);
						teachRelCardCourse.setShowType(courseShowType);
						teachRelCardCourseService.updateTeachRelCardCourse(teachRelCardCourse);
						// add by chengshx 2016-11-04
						if(courseShowType == 1){
							// 未学习修改学生附件关系为未学习
							TeachCourse course = new TeachCourse();
							course.setCourseId(courseId);
							course = teachCourseDao.selectTeachCourse(course);
							if(course.getCourseType() != 2){ //判断是自定义课程
								// 获取课程下的所有附件
								TeachRelCourseAttachment teachRelCourseAttachment = new TeachRelCourseAttachment();
								teachRelCourseAttachment.setCourseId(courseId);
								List<TeachRelCourseAttachment> list = teachRelCourseAttachmentService.selectSingleTeachRelCourseAttachment(teachRelCourseAttachment);
								// 获取班级下的所有学生
								List<CenterUser> userList = centerUserService.selectCenterUserByClassId(Integer.valueOf(classId));
								for(TeachRelCourseAttachment t : list){
									// 获取视频的总时长
									Integer attachmentId = t.getAttaId();
									String attachmentStr = RedisComponent.findRedisObject(Integer.valueOf(attachmentId), PublicAttachment.class);
									PublicAttachment publicAttachment = JSONObject.toJavaObject(JSONObject.parseObject(attachmentStr), PublicAttachment.class);
									Integer timeLength = publicAttachment.getTimeLength();
									TeachRelStudentAttachment stuAtta = new TeachRelStudentAttachment();
									for(CenterUser u : userList){
										stuAtta = teachRelStudentAttachmentService.selectTeachRelStudentAttachment(u.getUserId(), attachmentId);
										if(stuAtta != null && stuAtta.getCourseHour() < timeLength * 0.8){
											stuAtta.setIsStudy(0);
											teachRelStudentAttachmentService.updateTeachRelStudentAttachment(1, stuAtta);
										}
									}
								}
							}
						}
					}else{//案例课程
						TeachRelClassCourse teachRelClassCourse = new TeachRelClassCourse();
						teachRelClassCourse.setClassId(Integer.valueOf(classId));
						teachRelClassCourse.setCourseId(courseId);
						teachRelClassCourse.setShowType(courseShowType);
						teachRelClassCourseService.updateTeachRelClassCourse(teachRelClassCourse);
					}
					// update by chengshx 2016-11-03
//					String key = null;
//					if(courseCardId.equals("")){
//						key = classId + "_" + courseId;
//						JedisCache.delRedisVal(GameConstants.REDIS_COURSE_CLASS, key);
//					} else {
//						key = courseCardId + "_" + courseId;
//						JedisCache.delRedisVal(GameConstants.REDIS_COURSE_CARD, key);
//					}
					LogUtil.info(this.getClass(), "updateCourse", "(教学班)设置课程显示类型成功");
					return Common.getReturn(AppErrorCode.SUCCESS,"").toJSONString();
				}else{
					LogUtil.error(this.getClass(), "updateCourse", "(教学班)用户不是教师");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_IS_TEACHER).toJSONString();
				}
			}else if(classType==GameConstants.CLUB_TRAIN_CLASS){//俱乐部班2
				ClubMember clubMember = new ClubMember();
				clubMember.setUserId(userId);
				ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
				if(resClubMember!=null){
					//用户角色是教练或者会长
					if(resClubMember.getLevel()==GameConstants.COACH || resClubMember.getLevel()==GameConstants.PRESIDENT){
						ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
						clubRelClassCourse.setCourseId(courseId);
						clubRelClassCourse.setIsShow(courseShowType);
						clubRelClassCourse.setClassId(Integer.valueOf(classId));
						clubRelClassCourseService.updateClubRelClassCourse(clubRelClassCourse);
						LogUtil.error(this.getClass(), "updateCourse", "(培训班)课程显示类型修改成功");
						return Common.getReturn(AppErrorCode.SUCCESS,"").toJSONString();
					}else{
						LogUtil.error(this.getClass(), "updateCourse", "(培训班)用户不是教练");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.USER_NOT_COACH).toJSONString();
					}
				}else{
					LogUtil.error(this.getClass(), "updateCourse", "(培训班)该用户都不是俱乐部成员");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.CLUB_USER_NOT_EXIST).toJSONString();
				}
			}else{
				LogUtil.error(this.getClass(), "updateCourse", "班级类型传入不正确");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(BusinessCourseService.class, "updateCourse", "设置课程显示类型异常!",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_SET_COURSE_TYPE).toJSONString();
		}
	}
	
	/**
	 * 获取课程信息
	 * @param userId
	 * @param start
	 * @param limit
	 * @param classId
	 * @param courseCardId
	 * @param courseId
	 * @param requestSide
	 * @param platformModule
	 * @param inquireType
	 * @return
	 */
	@Transactional
	public String getCourse(String userIdStr, Integer start, Integer limit, String classIdStr, String courseCardIdStr,
			String courseIdStr, Integer requestSide, Integer platformModule, Integer inquireType,Integer classType) {
		LogUtil.info(this.getClass(), "getCourse", "获取课程信息开始调用,inquireType="+inquireType+
				",platformModule="+platformModule+",classType="+classType+",classId="+classIdStr
				+",courseId="+courseIdStr+",courseCardId="+courseCardIdStr+",requestSide="+requestSide
				+",userId="+userIdStr);
		JSONObject jo = new JSONObject();
		Integer userId = userIdStr.equals("")?0:Integer.valueOf(userIdStr);
		Integer classId = classIdStr.equals("")?0:Integer.valueOf(classIdStr);
		Integer courseId = courseIdStr.equals("")?0:Integer.valueOf(courseIdStr);
		Integer courseCardId = courseCardIdStr.equals("")?0:Integer.valueOf(courseCardIdStr);
		try {
			if(inquireType==GameConstants.QUERY_TYPE_ONE){//课程卡id
				String cacheUser = RedisComponent.findRedisObject(userId,CenterUser.class);
				CenterUser centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
				centeruser.setStart(start);
				centeruser.setLimit(limit);
				//当前登录者在本班级的角色
				Integer role = centeruser.getUserType();
				//查询该课程卡下的课程teach_rel_card_course 课程卡课程关系表
				List<TeachCourse> counrseList = baseTeachCourseService.getCourseByCourseCardId(centeruser,courseCardId,requestSide,classType);
				Integer count = 0;
				if(counrseList.size()!=0){
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("cardId", courseCardId);
					if(requestSide.equals(2))
						paramMap.put("showTypeApp", 3);
					//查询该课程卡下课程数量
					count = baseTeachCourseService.getCourseByCourseCardIdCount(paramMap);
				}
				jo = PageObject.getPageObject(count, centeruser.getStart(), centeruser.getLimit(), counrseList, "courses");
				jo.put("teachingRole",role);
				LogUtil.info(this.getClass(), "getCourseCards", "根据课程卡id获取信息成功");
			}else if(inquireType==GameConstants.QUERY_TYPE_TWO){//班级id
				//当前用户在本班级角色
				Integer role = 0;
				Integer count = 0;
				List<TeachCourse> counrseList = null;//教学班
				List<ClubTrainingCourse> clubTrainingCourseList = null;//培训班
				if(classType==GameConstants.TEACHING_CLASS){//教学班1
					//教学模块查看案例课程列表使用
					String cacheUser = RedisComponent.findRedisObject(userId,CenterUser.class);
					CenterUser centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					centeruser.setStart(start);
					centeruser.setLimit(limit);
					//当前登录者在本班级的角色
					role = centeruser.getUserType();
					//班级课程关系表 --查询集合
					counrseList = baseTeachCourseService.getCourseByClassId(centeruser,requestSide,classId,classType);
					if(counrseList.size()!=0){
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("classId", classId);
						//班级课程关系表 --查询数量
						count = baseTeachCourseService.getCourseByClassIdCount(paramMap);
					}
					jo = PageObject.getPageObject(count, start,limit,counrseList, "courses");
					LogUtil.info(this.getClass(), "getCourseCards", "根据教学班班级id获取信息成功");
				}else if(classType==GameConstants.CLUB_TRAIN_CLASS){//俱乐部培训班2
					//用户在俱乐部角色
					ClubMember clubmember = new ClubMember();
					clubmember.setUserId(userId);
					ClubMember resClubmember = clubMemberService.getClubMemberOne(clubmember);
					role = resClubmember==null?0:resClubmember.getLevel();
					
					//培训班课程关系表 --查询集合
					clubTrainingCourseList = baseTeachCourseService.getClubCourseByClassId(courseCardId,requestSide,classId,classType,start,limit,userId);
					if(clubTrainingCourseList.size()!=0){
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("classId",classId);
						//培训班课程关系表--课程数量
						count = baseTeachCourseService.getClubCourseByClassIdCount(paramMap);
					}
					jo = PageObject.getPageObject(count, start,limit,clubTrainingCourseList, "courses");
					LogUtil.info(this.getClass(), "getCourseCards", "根据培训班班级id获取信息成功");
				}
				jo.put("teachingRole",role);
			}else if(inquireType==GameConstants.QUERY_TYPE_THREE){//课程id
				Integer role = null;
				if(classType==GameConstants.TEACHING_CLASS){//教学班
					//课程卡id 班级id
					String cacheUser = RedisComponent.findRedisObject(userId,CenterUser.class);
					CenterUser centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					role = centeruser.getUserType();
					TeachCourse  course = baseTeachCourseService.getCourseByCourseId(0, classId,courseCardId,courseId,centeruser,classType);
					List<TeachCourse> list = new ArrayList<TeachCourse>();
					list.add(course);
					jo = PageObject.getPageObject(list.size(), 0,0,list,"courses");
					LogUtil.info(this.getClass(), "getCourseCards", "根据课程id(教学班)获取信息成功");
				}else if(classType==GameConstants.CLUB_TRAIN_CLASS){//培训班2
					//该用户在俱乐部角色
					ClubMember clubmember = new ClubMember();
					clubmember.setUserId(userId);
					ClubMember resClubmember = clubMemberService.getClubMemberOne(clubmember);
					role = resClubmember.getLevel();
					
					//修改学习次数
					ClubRelClassCourse clubRelClassCourse = new ClubRelClassCourse();
					clubRelClassCourse.setCourseId(courseId);
					clubRelClassCourse.setClassId(classId);
					clubRelClassCourseService.updateClubRelClassCourse(clubRelClassCourse);
					
					ClubTrainingCourse clubCourse = baseTeachCourseService.getClubCourseByCourseId(userId, courseId,classId,classType);
					List<ClubTrainingCourse> list = new ArrayList<ClubTrainingCourse>();
					list.add(clubCourse);
					jo = PageObject.getPageObject(list.size(), 0,0,list,"courses");
					LogUtil.info(this.getClass(), "getCourseCards", "根据课程id(培训班)获取信息成功");
				}
				jo.put("teachingRole",role);
			}else if(inquireType==GameConstants.QUERY_TYPE_FOUR){//获取官方课程
				//根据用户id去俱乐部会员表查询会员在俱乐部的角色
				ClubMember clubmember = new ClubMember();
				clubmember.setUserId(userId);
				ClubMember resClubmember = clubMemberService.getClubMemberOne(clubmember);
				jo.put("teachingRole",resClubmember==null?0:resClubmember.getLevel());//当前登录者在俱乐部的角色
				//查询官方课程
				ClubTrainingCourse clubtrainingcourse = new ClubTrainingCourse();
				clubtrainingcourse.setCourseType(BusinessConstant.COURSE_TYPE_1);
				List<ClubTrainingCourse> counrseList = baseTeachCourseService.getClubTrainingCourse(clubtrainingcourse,userId,classType);
				jo = PageObject.getPageObject(counrseList.size(), 0,0,counrseList,"courses");
				LogUtil.info(this.getClass(), "getCourseCards", "俱乐部获取官方课程信息成功");
			}else{//查询类型报错
				LogUtil.error(this.getClass(), "getCourseCards", "查询类型不正确");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(BusinessCourseService.class, "getCourse", "获取课程信息异常!",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Common.getReturn(AppErrorCode.SUCCESS, BusinessConstant.ERROR_COURSE,jo).toJSONString();
		}
		return Common.getReturn(AppErrorCode.SUCCESS, "",jo).toJSONString();
	}

	
	/**
	 * 手机端：获取课程信息
	 * @param userId		用户id
	 * @param start			分页位便宜
	 * @param limit			分页查询数目
	 * @param classId		班级id
	 * @param classType		班级类型	1:教学班；2:俱乐部培训班；
	 * @param chapterId		章节id
	 * @param ccType		课程卡类型	2:知识点卡片；3:案例视频卡片；
	 * @param courseId		课程id
	 * @param requestSide	请求类型 	1:管理后端；2:前端；
	 * @param inquireType	功能类型
	 * @return
	 * @author 			lw
	 * @date			2016年7月26日  下午6:23:59
	 */
	public String getCourseForMobile(Integer userId, Integer start, Integer limit, Integer classId, Integer classType,
			Integer chapterId, Integer ccType, Integer courseId, Integer requestSide, Integer inquireType, Integer courseCardId) {
		
		//查询用户身份
		String redisData = RedisComponent.findRedisObject(userId, CenterUser.class);
		if(redisData == null ){
			LogUtil.error(this.getClass(), "getCourseForMobile", String.valueOf(AppErrorCode.ERROR_COURSE_REQUEST_PARAM));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_REQUEST_PARAM).toJSONString();
		}
		CenterUser user = JSONObject.parseObject(redisData, CenterUser.class);
		
		
		switch (inquireType) {
		
		//教学班信息列表获取
		case GameConstants.COURSE_LIST:
			if(chapterId != null && ccType != null && requestSide != null && classType != null ){
				//获取章节
			/*	redisData = RedisComponent.findRedisObject(chapterId, TeachCourseChapter.class);
				if(redisData == null){
					LogUtil.error(this.getClass(), "findTeachCourseByChapterAndCcType", String.valueOf(AppErrorCode.ERROR_COURSE_REQUEST_PARAM));
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_REQUEST_PARAM).toJSONString();
				}*/
				//获取章节下面的课程卡
				TeachCourseCard card = new TeachCourseCard();
				card.setEndTime(classId);
				card.setCardType(ccType);
				card.setImageId(chapterId);
				List<TeachCourseCard> listCard = teachCourseCardService.selectByChapterId(card);
				String cardIdsStr = null;
				if(listCard == null || listCard.size() == 0){
					JSONObject json = new JSONObject();
					json.put("returnCount", 0);
					json.put("allPage", 0);
					json.put("currentPage", 0);
					json.put("teachingRole", user.getUserType());
					json.put("courses", new JSONArray());
					return Common.getReturn(AppErrorCode.SUCCESS, null, json).toJSONString();
				}else{
					List<Integer> cardIds = new ArrayList<Integer>();
					for (int i = 0; i < listCard.size(); i++) {
						cardIds.add(listCard.get(i).getCardId());
					}
					cardIdsStr = StringUtil.ListToString(cardIds, ",");
				}
				
				//分页查询
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("cardId", cardIdsStr);
				if(requestSide == GameConstants.TYPE_FRONT){
					paramMap.put("showTypeApp", GameConstants.TEACHER_HOMEWORK_HIDE);
				}
				return baseTeachCourseService.findTeachCourseByChapterAndCcType(paramMap, start, limit, user.getUserType(), classType, userId, ccType).toJSONString();
			}
			break;
			
		//教学班信息列表获取
		case GameConstants.COURSE_CLASS_LIST:
			if(classId != null && requestSide!= null && classType != null){
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				
				//教学班级案例课程:所有班级案例课程是一样,根据课程id分组
				if(classType == GameConstants.TEACHING_CLASS){
					//前端操作
					if(requestSide == GameConstants.TYPE_FRONT){
						map.put("showTypeApp", GameConstants.TEACHER_HOMEWORK_HIDE);
					}
					map.put("courseType", GameConstants.CASE_COURSE);
					map.put("classId", classId);
					ccType = GameConstants.CASE_COURSE;
					return teachRelClassCourseService.findTeachCourseByChapterAndCcType(map, start, limit, user.getUserType(), classType, userId, ccType).toJSONString();
					
				//培训班
				}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
					//前端操作
					if(requestSide == GameConstants.TYPE_FRONT){
						map.put("isShowApp", GameConstants.TEACHER_HOMEWORK_HIDE);
					}
					map.put("classId", classId);
					return clubRelClassCourseService.findTeachCourseByClassId(map, start, limit, user.getUserType(), classType, userId).toJSONString();
				}
				
			}
			break;
		
		//根据课程id获取课程详情 
		case GameConstants.COURSE_CLASS_INFO:
			if(courseId != null && classId != null && classType!=null){
				//教学班
				if(classType == GameConstants.TEACHING_CLASS){
					// add by chengshx 2016-11-03
					String centerUserStr = RedisComponent.findRedisObject(userId, CenterUser.class);
					JSONObject userObj = JSONObject.parseObject(centerUserStr);
					CenterUser centerUser = JSONObject.toJavaObject(userObj, CenterUser.class);
					baseTeachCourseService.getCourseByCourseId(1, classId,courseCardId,courseId,centerUser,classType);
					return baseTeachCourseService.findOneTeachCourse(courseId, userId , classId, user.getUserType(), GameConstants.TEACHING_CLASS).toJSONString();
					
				//培训班
				}else if(classType == GameConstants.CLUB_TRAIN_CLASS){
					return clubRelClassCourseService.findOneTeachCourse(courseId, userId, classId, user.getUserType(), GameConstants.CLUB_TRAIN_CLASS).toJSONString();
				}
			}
			break;
		}
		
		LogUtil.error(this.getClass(), "getCourseForMobile", String.valueOf(AppErrorCode.ERROR_COURSE_REQUEST_PARAM));
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_REQUEST_PARAM).toJSONString();
	}
	
	/**
	 * 提交音频附件id  教学班
	 * @param accessory 附件id
	 * @param courseIds 课程id
	 */
	public void submitAccessory(String accessory,Integer courseIds){
		//批量插入音频附件
		if(!StringUtil.isEmpty(accessory)){
			String[] attachmentIds = accessory.split(",");
			List<TeachRelCourseAttachment> list = new ArrayList<TeachRelCourseAttachment>();
			for (int i = 0; i < attachmentIds.length; i++) {
				TeachRelCourseAttachment teachrelcourseattachment = new TeachRelCourseAttachment();
				teachrelcourseattachment.setCourseId(courseIds);
				teachrelcourseattachment.setAttaId(Integer.valueOf(attachmentIds[i]));
				list.add(teachrelcourseattachment);
			}
			teachRelCourseAttachmentService.batchInsertTeachRelCourseAttachment(list);
		}
	}
	
	/**
	 * 提交音频附件id  培训班
	 * @param accessory 附件id
	 * @param courseIds 课程id
	 *//*
	public void submitClubAccessory(String accessory,Integer courseIds){
		//批量插入音频附件
		if(!StringUtil.isEmpty(accessory)){
			String[] attachmentIds = accessory.split(",");
			List<ClubRelCourseAttachment> list = new ArrayList<ClubRelCourseAttachment>();
			for (int i = 0; i < attachmentIds.length; i++) {
				ClubRelCourseAttachment clubRelCourseAttachment = new ClubRelCourseAttachment();
				clubRelCourseAttachment.setCourseId(courseIds);
				clubRelCourseAttachment.setAttaId(Integer.valueOf(attachmentIds[i]));
				list.add(clubRelCourseAttachment);
			}
			clubRelCourseAttachmentService.batchInsertClubRelCourseAttachment(list);
		}
	}*/
}


