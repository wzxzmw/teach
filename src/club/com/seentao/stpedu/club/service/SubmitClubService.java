package com.seentao.stpedu.club.service;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;

@Service
public class SubmitClubService {
	@Autowired
	private ClubMainDao clubMainDao;

	@Autowired
	private ClubMemberDao clubMemberDao;

	@Autowired
	private ClubMainService clubMainService;


	/***
	 * 提交俱乐部 
	 * @param userId
	 * @param clubId  俱乐部id
	 * @param clubType 俱乐部类型1:官方；2:非官方；
	 * @param clubName  俱乐部名称
	 * @param clubDesc  俱乐部介绍
	 * @param clubLogoId  俱乐部logo图片Id
	 * @param provinceId  俱乐部所属省id
	 * @param cityId  俱乐部所属城市id
	 * @param schoolId  俱乐部所属学校id
	 * @param addMemberType  会员加入方式1:申请；2:公开；3:付费；
	 * @param fLevelAccount  收取的一级货币
	 * @param gameBannerId   擂台海报id
	 * @param teachingBannerId 培训海报id
	 * @param styleBannerId    风格海报id
	 * @param backgroundColorId 背景色id
	 * @return actionType  提交操作
	 * @author cxw
	 * @throws ParseException 
	 */
	@Transactional
	public String submitClubInfo(String userId,String clubId, int clubType, String clubName, String clubDesc, String clubLogoId,
			String provinceId, String cityId, String schoolId, int addMemberType, int fLevelAccount,
			String gameBannerId, String teachingBannerId, String styleBannerId, String backgroundColorId,
			int actionType){

		try {
			if(clubId == "" || clubId == userId){
				//判断用户是否已创建俱乐部
				ClubMain clubMain = new ClubMain();
				clubMain.setCreateUserId(Integer.valueOf(userId));
				ClubMain main = clubMainDao.selectClubMainEntity(clubMain);
				if(null != main){
					LogUtil.error(this.getClass(), "submitClubInfo", "你已创建俱乐部");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FOUR, AppErrorCode.YOU_HAVE_CREATED_THE_CLUB).toJSONString();
				}

				//判断用户是否加入俱乐部
				ClubMember member = clubMemberDao.selectOnlyClubMember(Integer.valueOf(userId));
				if(member!=null&&member.getMemberStatus()==1){
					LogUtil.error(this.getClass(), "submitClubInfo", "你已加入俱乐部");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FOUR, AppErrorCode.JOIN_THE_CLUB_SUCCESS).toJSONString();
				}
				clubMainService.addClub(userId, clubName, clubLogoId, clubDesc, addMemberType, fLevelAccount, clubType, schoolId,cityId);
				JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId);
				return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();

			}

			//判断clubid是不是本用户创建
			ClubMain clubMain = null;
			clubMain = new ClubMain();
			clubMain.setClubId(Integer.valueOf(clubId));
			clubMain.setCreateUserId(Integer.valueOf(userId));
			ClubMain main = clubMainDao.selectClubMainEntity(clubMain);
			if(null == main ){
				LogUtil.error(this.getClass(), "submitClubInfo", "俱乐部信息为空");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.GET_ALL_CLUBS_INFO_FAILED).toJSONString();
			}
			if(main.getCreateUserId() == Integer.parseInt(userId)&&main.getClubId()==Integer.parseInt(clubId)){

				if(actionType==GameConstants.SUBMIT_OPERATION_ONE){
					//actionType==1 处理风格海报id 
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					clubMain.setStyleBannerId(Integer.valueOf(styleBannerId));
					clubMain.setBgColorId(backgroundColorId =="" ? 0 :Integer.valueOf(backgroundColorId));
					clubMainDao.updateClubMainByKey(clubMain);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}else if(actionType == GameConstants.SUBMIT_OPERATION_TWO){
					//actionType==2处理背景色id
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					if("".equals(backgroundColorId)){
						clubMain.setBgColorId(null);
					}else{
						clubMain.setBgColorId(Integer.valueOf(backgroundColorId));
					}
					clubMainDao.updateClubMainByKey(clubMain);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}else if(actionType == GameConstants.SUBMIT_OPERATION_THREE){
					//actionType==3处理其它基本信息的参数项
					//判断俱乐部会员大于等于200或者俱乐部是否购买运营增值权限
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					ClubMain mainEntity = clubMainDao.selectClubMainEntity(clubMain);
					if(addMemberType == GameConstants.CLUB_JOIN_THREE){
						if(mainEntity.getMemberNum()>=200||mainEntity.getIsBuyClubVip() == 1){
							clubMain = new ClubMain();
							clubMain.setClubId(Integer.valueOf(clubId));
							clubMain.setClubName(clubName);
							if(StringUtils.isEmpty(clubLogoId)){
								clubMain.setLogoId(null);
							}else{
								clubMain.setLogoId(Integer.valueOf(clubLogoId));
							}
							clubMain.setClubExplain(clubDesc);
							clubMain.setAddMemberType(addMemberType);
							clubMain.setAddAmount(Double.valueOf(fLevelAccount));
							clubMain.setClubType(clubType);
							//
							if(StringUtils.isNotEmpty(schoolId)){
								clubMain.setSchoolId(Integer.valueOf(schoolId));
							}
							clubMain.setRegionId(Integer.valueOf(cityId));
							clubMainDao.updateClubMainByKey(clubMain);
							JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
							return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
						}
						LogUtil.error(this.getClass(), "submitClubInfo", "会员人数不足或未购买权益");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_THREE, "").toJSONString();
					}
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					clubMain.setClubName(clubName);
					if(StringUtils.isEmpty(clubLogoId)){
						clubMain.setLogoId(0);
					}else{
						clubMain.setLogoId(Integer.valueOf(clubLogoId));
					}
					clubMain.setClubExplain(clubDesc);
					clubMain.setAddMemberType(addMemberType);
					clubMain.setAddAmount(Double.valueOf(fLevelAccount));
					clubMain.setClubType(clubType);
					if(StringUtils.isNotEmpty(schoolId)){
						clubMain.setSchoolId(Integer.valueOf(schoolId));
					}
					clubMain.setRegionId(Integer.valueOf(cityId));
					clubMainDao.updateClubMainByKey(clubMain);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();

				}else if(actionType == GameConstants.SUBMIT_OPERATION_FOUR){
					//actionType==4处理擂台海报id
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					clubMain.setGameBannerId(Integer.valueOf(gameBannerId));
					clubMainDao.updateClubMainByKey(clubMain);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}else if(actionType == GameConstants.SUBMIT_OPERATION_FIVE){
					//actionType == 5 处理培训海报id
					clubMain = new ClubMain();
					clubMain.setClubId(Integer.valueOf(clubId));
					clubMain.setTeachBannerId(Integer.valueOf(teachingBannerId));
					clubMainDao.updateClubMainByKey(clubMain);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				}
				return Common.getReturn(AppErrorCode.SUCCESS, String.valueOf(AppErrorCode.SUCCESS)).toJSONString();
			}
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "submitClubInfo", "俱乐部信息有误", e);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CREATE_CLUB_SUCCESS).toJSONString();
		}
		LogUtil.error(this.getClass(), "submitClubInfo", "俱乐部不是该用户创建");
		return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.FAILED_TO_MODIFY_THE_CLUB).toJSONString();
	}


	public boolean userIsExist(String clubName) {
		LogUtil.info(this.getClass(), "userIsExist", "昵称占用判断【nickName："+clubName+"】");
		ClubMain clubMain=new  ClubMain(null, clubName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null, null, null, null, null);
		List<ClubMain> list= clubMainDao.selectSingleClubMain(clubMain);
		if(list.size()>0){

			return true;
		}else{
			return false;
		}
	}

}
