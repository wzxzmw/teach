package com.seentao.stpedu.persionalcenter.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterUserDao;
import com.seentao.stpedu.common.dao.PublicAttachmentDao;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.service.CenterNewsStatusService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.PublicPictureService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.redis.JedisUserCacheUtils;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class PersionalCenterService {
	
	@Autowired
	private PublicAttachmentDao publicAttachmentDao;
	@Autowired
	private CenterUserDao centerUserDao;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private CenterNewsStatusService centerNewsStatusService;
	@Autowired
	private PublicPictureService publicPictureService;
	
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private ClubMainService clubMainService;
	
	
	/**
	 * 提示红点的操作
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	public String getRemindRedDot(int userId,int clubId,int actionType){
		//if(clubMemberService.validateUserAndClubAndLevel(userId, GameConstants.CLUB_PRESIDENT_COACH_MEMBER)){
			LogUtil.info(this.getClass(), "getRemindRedDot", "提示红点的操作参数：【userId："+userId+"】，【clubId："+clubId+"】，【actionType："+actionType+"】");
			JSONObject jsonObject=new JSONObject();
			switch(actionType){
				//判断俱乐部下是否有待审核的会员
				case GameConstants.REMIND_RED_DOT_TYPE_ONE:
					jsonObject=getActionTypeOne(jsonObject,clubId,userId);
		        break;
		        //判断顶部菜单的消息
				case GameConstants.REMIND_RED_DOT_TYPE_TWO:
					jsonObject=getActionTypeTwo(jsonObject,userId,actionType);
			    break;
			    //消息中心的各项菜单是否有未读消息
				case GameConstants.REMIND_RED_DOT_TYPE_THREE:
					jsonObject=getActionTypeThree(jsonObject,userId,actionType);
			    break;
			    //我的俱乐部主页左侧菜单是否有未读消息
				case GameConstants.REMIND_RED_DOT_TYPE_FOUR:
					jsonObject=getActionTypeFour(jsonObject,userId,clubId);
			    break;
			}
			LogUtil.info(this.getClass(), "getRemindRedDot", "提示红点的操作成功！！");
			return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_RED_SUCCESS, jsonObject).toJSONString();
		//}else{
		//	return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_RED_NOT_MEMBER).toJSONString();
		//}
	}
	
	/**
	 * 提示红点的操作
	 * @author  lijin
	 * @date    2016年6月30日 下午16:59:25
	 */
	public JSONObject getActionTypeOne(JSONObject jsonObject,int clubId,int userId){
		//actionType=1：获取俱乐部会员表 会员状态为【审核中状态】数据
		//is_not_audited	
		//查询俱乐部中是否有待审会员？ 有：显示红点、没有：不显示
		String club = RedisComponent.findRedisObject(clubId, ClubMain.class);
		ClubMain clubs = JSONObject.parseObject(club, ClubMain.class);
		/*ClubMain club = new ClubMain();
		club.setClubId(clubId);
		List<ClubMain> clubList = clubMainService.selectSingleClubMain(club);*/
		
		int isShowingRedDot=0;
		if(clubs != null){
			if(null!=clubs.getIsNotAudited() && clubs.getIsNotAudited() == 1){
				isShowingRedDot = 1;
			}else{
				isShowingRedDot=0;
			}
			}
		/*if(null!=clubList && clubList.size()==1){
			ClubMain clubmain = clubList.get(0);
			if(null!=clubmain.getIsNotAudited() && clubmain.getIsNotAudited() == 1){
				isShowingRedDot = 1;
			}else{
				isShowingRedDot=0;
			}
		}*/
		return getReturnObjet(jsonObject,isShowingRedDot
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
				  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		
	}
	
	/**
	 * 判断顶部菜单的消息和判断消息中心的各项菜单是否有未读消息
	 * @author  lijin
	 * @date    2016年6月30日 下午16:59:25
	 */
	public JSONObject getActionTypeTwo(JSONObject jsonObject,int userId,int actionType){
		//根据userid判断消息状态表里面的【是否有新私信，是否有新关注，是否有新问题邀请，是否有新比赛邀请，是否有新系统消息】
		CenterNewsStatus centerNewsStatus=centerNewsStatusService.queryCenterNewsStatusByUserId(userId);
		int status=0;
		if(null!=centerNewsStatus){
			status=Common.null2Int(centerNewsStatus.getIsNewAttention())+Common.null2Int(centerNewsStatus.getIsNewGameInvite())+Common.null2Int(centerNewsStatus.getIsNewPrivateMessage())+Common.null2Int(centerNewsStatus.getIsNewQuestionInvite())+Common.null2Int(centerNewsStatus.getIsNewSysNews());
		}
		int redDotDefault=0;
	
		if(status>0){
			redDotDefault=1;
		}else{
			redDotDefault=0;
		}
		
		if(null==centerNewsStatus){
			return  getReturnObjet(jsonObject,redDotDefault
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		}else{
			return  getReturnObjet(jsonObject,redDotDefault
					 ,Common.null2Int(centerNewsStatus.getIsNewPrivateMessage())
					 ,Common.null2Int(centerNewsStatus.getIsNewAttention())
					 ,Common.null2Int(centerNewsStatus.getIsNewQuestionInvite())
					 ,Common.null2Int(centerNewsStatus.getIsNewGameInvite())
					 ,Common.null2Int(centerNewsStatus.getIsNewSysNews())
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		}
	}
	
	/**
	 * 判断顶部菜单的消息和判断消息中心的各项菜单是否有未读消息
	 * @author  lijin
	 * @date    2016年6月30日 下午16:59:25
	 */
	public JSONObject getActionTypeThree(JSONObject jsonObject,int userId,int actionType){
		//根据userid判断消息状态表里面的【是否有新私信，是否有新关注，是否有新问题邀请，是否有新比赛邀请，是否有新系统消息】
		CenterNewsStatus centerNewsStatus=centerNewsStatusService.queryCenterNewsStatusByUserId(Integer.valueOf(userId));
		int status=0;
		int redDotDefault;
		if(null!=centerNewsStatus){
			status=Common.null2Int(centerNewsStatus.getIsNewAttention())+Common.null2Int(centerNewsStatus.getIsNewGameInvite())+Common.null2Int(centerNewsStatus.getIsNewPrivateMessage())+Common.null2Int(centerNewsStatus.getIsNewQuestionInvite())+Common.null2Int(centerNewsStatus.getIsNewSysNews());
		}
	
		if(status>0){
			redDotDefault=1;
		}else{
			redDotDefault=0;
		}
		
		if(null==centerNewsStatus){
			return  getReturnObjet(jsonObject,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		}else{
			return  this.getReturnObjet(jsonObject,redDotDefault
					 ,Common.null2Int(centerNewsStatus.getIsNewPrivateMessage())
					 ,Common.null2Int(centerNewsStatus.getIsNewAttention())
					 ,Common.null2Int(centerNewsStatus.getIsNewQuestionInvite())
					 ,Common.null2Int(centerNewsStatus.getIsNewGameInvite())
					 ,Common.null2Int(centerNewsStatus.getIsNewSysNews())
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					 ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		}
	}
	
	/**
	 * 我的俱乐部主页左侧菜单是否有未读消息
	 * @author  lijin
	 * @date    2016年6月30日 下午16:59:25
	 */
	public JSONObject getActionTypeFour(JSONObject jsonObject,int userId,int clubId){
		//根据俱乐部会员表相关字段判读是否有新提醒、通知、红包。    
		ClubMember clubMember=new ClubMember();
		clubMember.setUserId(userId);
		clubMember.setClubId(clubId);
		clubMember=clubMemberService.queryClubMemberByClubIdAndUserId(userId,clubId);
		
		if(null==clubMember){
			return getReturnObjet(jsonObject,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO);
		}else{
			return getReturnObjet(jsonObject,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,GameConstants.REMIND_RED_DOT_DEFAULT_ZERO
					  ,Common.null2Int(clubMember.getIsNewRemind())
					  ,Common.null2Int(clubMember.getIsNewNotice())
					  ,Common.null2Int(clubMember.getIsNewRedPacket()));
		}
	}
	
	
	public JSONObject getReturnObjet(JSONObject jsonObject,int isShowingRedDot,int isShowingPrivateMessageRedDot,int isShowingAttantionRedDot,int isShowingInviteRedDot,int isShowingGameRedDot,int isShowingSysRedDot,int isShowingRemindRedDot,int isShowingNoticeRedDot,int isShowingRedPacketRedDot){
		jsonObject.put("isShowingRedDot", isShowingRedDot);
		jsonObject.put("isShowingPrivateMessageRedDot", isShowingPrivateMessageRedDot);
		jsonObject.put("isShowingAttantionRedDot", isShowingAttantionRedDot);
		jsonObject.put("isShowingInviteRedDot", isShowingInviteRedDot);
		jsonObject.put("isShowingGameRedDot", isShowingGameRedDot);
		jsonObject.put("isShowingSysRedDot", isShowingSysRedDot);
		jsonObject.put("isShowingRemindRedDot",isShowingRemindRedDot );
		jsonObject.put("isShowingNoticeRedDot",isShowingNoticeRedDot );
		jsonObject.put("isShowingRedPacketRedDot",isShowingRedPacketRedDot );
		return jsonObject;
	}
	
	
	/**
	 * 提交附件的OSS信息
	 * @author  lijin
	 * @date    2016年6月29日 下午20:46:25
	 */
	public String submitAttachmentOSS(int userId,String userType,String attachmentName,String attachmentLink,int attachmentSeconds,int attachmentMajorType,String attachmentType){
		LogUtil.info(this.getClass(), "submitAttachmentOSS",
				"提交附件的OSS信息参数【userId：" + userId + "】,【userType：" + userType + "】,【attachmentName：" + attachmentName
						+ "】,【attachmentLink：" + attachmentLink + "】,【attachmentSeconds：" + attachmentSeconds
						+ "】,【attachmentMajorType：" + attachmentMajorType + "】");
		JSONObject jsonObject = new JSONObject();
		// 根据提交的信息，在附件表里新增一条数据
		PublicAttachment publicAttachment = new PublicAttachment(attachmentMajorType, attachmentName, attachmentLink,
				attachmentLink.substring(attachmentLink.lastIndexOf(".")), attachmentSeconds, Common.getCurrentCreateTime(), userId);
		
		//根据attachmentMajorType=4判断为图片，图片的url保存在图片表里面
		int attachmentId =0;
		//图片保存在图片表中
		PublicPicture publicPicture=new PublicPicture();
		publicPicture.setCreateTime(Common.getCurrentCreateTime());
		publicPicture.setCreateUserId(userId);
		publicPicture.setDownloadUrl(attachmentLink);
		publicPicture.setFilePath(attachmentLink);
		publicPicture.setSize(0.0);
		publicPicture.setSuffixName(attachmentLink.substring(attachmentLink.lastIndexOf(".")));
		if(4==attachmentMajorType){
			attachmentId=publicPictureService.insertPublicPicture(publicPicture);
			//publicAttachmentDao.insertPublicAttachment(publicAttachment);
		}else{
			//publicPictureService.insertPublicPicture(publicPicture);
			attachmentId = publicAttachmentDao.insertPublicAttachment(publicAttachment);
		}
		 System.out.println("attachmentId:"+attachmentId);
		// 返回插入附件id
		jsonObject.put("attachmentId", attachmentId);
		LogUtil.info(this.getClass(), "submitAttachmentOSS", "提交附件的OSS信息");
		return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.SUBMIT_OSS_SUCCESS, jsonObject).toJSONString();
		
	}
	
	/**
	 * 提交登录用户的信息
	 * @return
	 * @author  lijin
	 * @date    2016年6月29日 下午21:59:25
	 */
	public String submitLoginUser(String userName,Integer userId,Integer headImgId,String nickName,Integer sex,String phone,String realName,Integer schoolId,Integer instId,String idCard,String description,Integer birthday, Integer profession,String studentId, String studentCardNo,String teacherCardNo,String positionalTitle,String speciality,String grade,Integer educationLevel,String address,String userNameForEdit,String actionType){
		LogUtil.info(this.getClass(), "submitLoginUser",
				"提交附件的OSS信息参数【userId：" + userId + "】,【userName：" + userName + "】,【headImgId：" + headImgId
						+ "】,【nickName：" + nickName + "】,【sex：" + sex + "】,【phone：" + phone + "】,【realName：" + realName
						+ "】,【schoolId：" + schoolId + "】,【realName：" + realName + "】,【schoolId：" + schoolId
						+ "】,【instId：" + instId + "】,【idCard：" + idCard + "】,【description：" + description
						+ "】,【birthday：" + birthday + "】,【profession：" + profession + "】,【studentId：" + studentId
						+ "】,【studentCardNo：" + studentCardNo + "】,【teacherCardNo：" + teacherCardNo
						+ "】,【positionalTitle：" + positionalTitle + "】,【speciality：" + speciality + "】,【grade：" + grade
						+ "】,【educationLevel：" + educationLevel + "】");
		
			//根据用户id，跟新用户表
			if(actionType.equals("1")){
				if(null!=sex){
					sex=(sex==1?2:1);
				}else {
					sex=-1;
				}
			}
			//判断用户名是否重复本次是否修改用户名
			Integer userNameUpdateTimes = null;
			CenterUser centerUserName = new CenterUser();
			CenterUser centerUser = null;
			if(actionType.equals("1")){
				centerUserName.setUserId(userId);
				centerUser = centerUserDao.selectCenterUserOne(centerUserName);
				if(centerUser != null){
					if(centerUser.getUserName() != null){
						/*
						 * 用户名不相同修改次数加一
						 */
						if(!centerUser.getUserName().equals(userNameForEdit)){
							if(centerUser.getUserNameUpdateTimes() == null ){
								userNameUpdateTimes = 1;
							}else {
								userNameUpdateTimes = centerUser.getUserNameUpdateTimes() + 1;
							}
						}
					}else if(!userNameForEdit.equals("") || userNameForEdit != null){
						userNameUpdateTimes = 1;
					}
				}
			}
			CenterUser centerUsera=new CenterUser(userNameForEdit, userId, headImgId, nickName,sex , phone, realName, schoolId, instId, idCard, description, birthday,  profession, studentId,  studentCardNo, teacherCardNo, positionalTitle, speciality, grade, educationLevel,address,userNameUpdateTimes);
			try {
				centerUserDao.updateCenterUserByKey(centerUsera);
				
				centerUser = centerUserDao.queryCenterUserByUserId(userId);
				if(StringUtils.isNotEmpty(centerUser.getPhone())){
					JedisUserCacheUtils.delHdelUserHash(CenterUser.class.getSimpleName(), centerUser.getPhone());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//删除用户表缓存信息
			JedisCache.removeRedisVal(null, CenterUser.class.getSimpleName(), String.valueOf(userId));
			LogUtil.info(this.getClass(), "submitLoginUser", "提交登录用户的信息");
			return  Common.getReturn(AppErrorCode.SUCCESS,"成功").toJSONString();
	} 
	
	
	
	/**
	 * 提交登录用户的信息
	 * @return
	 * @author  lijin
	 * @date    2016年6月29日 下午21:59:25
	 */
	public String submitLoginUserHeadImgId(String userName,Integer userId,Integer headImgId,String nickName,Integer sex,String phone,String realName,Integer schoolId,Integer instId,String idCard,String description,Integer birthday, Integer profession,String studentId, String studentCardNo,String teacherCardNo,String positionalTitle,String speciality,String grade,Integer educationLevel){
		LogUtil.info(this.getClass(), "submitLoginUser",
				"提交附件的OSS信息参数【userId：" + userId + "】,【userName：" + userName + "】,【headImgId：" + headImgId
						+ "】,【nickName：" + nickName + "】,【sex：" + sex + "】,【phone：" + phone + "】,【realName：" + realName
						+ "】,【schoolId：" + schoolId + "】,【realName：" + realName + "】,【schoolId：" + schoolId
						+ "】,【instId：" + instId + "】,【idCard：" + idCard + "】,【description：" + description
						+ "】,【birthday：" + birthday + "】,【profession：" + profession + "】,【studentId：" + studentId
						+ "】,【studentCardNo：" + studentCardNo + "】,【teacherCardNo：" + teacherCardNo
						+ "】,【positionalTitle：" + positionalTitle + "】,【speciality：" + speciality + "】,【grade：" + grade
						+ "】,【educationLevel：" + educationLevel + "】");
		
		//根据用户id，跟新用户表
			
			CenterUser centerUser=centerUserService.getCenterUserById(userId);
			
			centerUserDao.deleteCenterUser(centerUser);
			
			centerUser.setUserName(userName);
			centerUser.setUserId(userId);
			centerUser.setHeadImgId(headImgId);
			centerUser.setNickName(nickName);
			centerUser.setSex( sex==1?0:1);
			centerUser.setPhone(phone);
			centerUser.setRealName(realName);
			centerUser.setSchoolId(schoolId);
			centerUser.setInstId(instId);
			centerUser.setDescription(description);
			centerUser.setBirthday(birthday);
			centerUser.setProfession(profession);
			centerUser.setStudentCardNo(studentCardNo);
			centerUser.setTeacherCardNo(teacherCardNo);
			centerUser.setPositionalTitle(positionalTitle);
			centerUser.setSpeciality(speciality);
			centerUser.setGrade(grade);
			centerUser.setEducationLevel(educationLevel);
			try {
				centerUserDao.insertCenterUser(centerUser);
				
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "insertCenterUser", "insertCenterUser is error"+ e);
			}
			
			
			//删除用户表缓存信息
			JedisCache.removeRedisVal(null, CenterUser.class.getSimpleName(), String.valueOf(userId));
			LogUtil.info(this.getClass(), "submitLoginUser", "提交登录用户的信息");
			return  Common.getReturn(AppErrorCode.SUCCESS,AppErrorCode.SUBMIT_USER_SUCCESS).toJSONString();
	} 
	
	/**
	 * 昵称已被占用判断
	 * @return
	 * @author  lijin
	 * @date    2016年7月17日 下午21:59:25
	 */
	public boolean userIsExist(String nickName){
		LogUtil.info(this.getClass(), "userIsExist", "昵称占用判断【nickName："+nickName+"】");
		CenterUser centerUser=new CenterUser(null, null, null, nickName, null, null, null, null, null, null, null, null,  null, null,  null, null, null, null, null, null,null,null);
		List<CenterUser> list= centerUserDao.selectSingleCenterUser(centerUser);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
}
