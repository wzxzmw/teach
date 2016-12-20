package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.seentao.stpedu.common.dao.CenterAccountDao;
import com.seentao.stpedu.common.dao.CenterUserDao;
import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ClubMainService{

	@Autowired
	private ClubMainDao clubMainDao;

	@Autowired
	private CenterAccountDao accountDao;

	@Autowired
	private CenterUserDao centerUserDao;

	@Autowired
	private ClubMemberDao clubMemberDao;

	public void updateClubMain(ClubMain clubmain) {
		clubMainDao.updateClubMainByKey(clubmain);
	}

	
	public List<ClubMain> selectSingleClubMain(ClubMain clubMain){
		return clubMainDao .selectSingleClubMain(clubMain);
	}
	
	/**
	 * 根据id获取clubMain对象
	 * @param clubId
	 * @author lijin
	 */

	public ClubMain getClubMainByClubId(String clubId){
		Map<String,Object> queryParam=new HashMap<>();
		queryParam.put("id_key", clubId);

		return (ClubMain)clubMainDao.getEntityForDB(queryParam);
	}

	/**
	 * 添加俱乐部
	 * @param userId
	 * @param clubName
	 * @param clubLogoId
	 * @param clubDesc
	 * @param addMemberType
	 * @param fLevelAccount
	 * @param clubType
	 * @param schoolId
	 * @author cxw
	 */
	@Transactional
	public void addClub(String userId,String clubName,String clubLogoId,String clubDesc,Integer addMemberType,
			Integer fLevelAccount,Integer clubType,String schoolId,String cityId){
		try {
			ClubMain clubMain = new ClubMain();
			clubMain.setCreateUserId(Integer.valueOf(userId));
			clubMain.setClubName(clubName);
			if("".equals(clubLogoId)){
				clubMain.setLogoId(null);
			}else{
				clubMain.setLogoId(Integer.valueOf(clubLogoId));
			}
			clubMain.setClubExplain(clubDesc);
			clubMain.setMemberNum(1);
			if(clubType==1){
				clubMain.setStatus(GameConstants.CLUB_STATUS_ONE);
			}else if(clubType == 2){
				clubMain.setStatus(GameConstants.CLUB_STATUS_TWO);
			}
			clubMain.setCreateTime(TimeUtil.getCurrentTimestamp());
			clubMain.setGameBannerId(null);
			clubMain.setTeachBannerId(null);
			clubMain.setStyleBannerId(null);
			clubMain.setBgColorId(null);
			clubMain.setAddMemberType(addMemberType);
			clubMain.setAddAmount(Double.valueOf(fLevelAccount));
			clubMain.setIsNotAudited(0);
			clubMain.setIsBuyClubVip(0);
			clubMain.setIsBuyCompetition(0);
			clubMain.setClubType(clubType);
			if(StringUtils.isNotEmpty(schoolId)){
				clubMain.setSchoolId(Integer.valueOf(schoolId));
			}
				clubMain.setRegionId(Integer.valueOf(cityId));
			int club = clubMainDao.insertClubMain(clubMain);

			//用户账户表添加俱乐部账户信息
			CenterAccount account = new CenterAccount();
			account.setUserId(club);//
			account.setUserType(GameConstants.CLUB_USER);
			account.setCreateTime(TimeUtil.getCurrentTimestamp());
			account.setAccountType(GameConstants.STAIR_ONE);
			account.setBalance(0.00);
			account.setLockAmount(0.00);
			accountDao.insertCenterAccount(account);

			//更新用户的俱乐部id
			CenterUser centerUser = null;
			centerUser = new CenterUser();
			centerUser.setUserId(Integer.valueOf(userId));
			centerUser.setClubId(club);	
			centerUserDao.updateCenterUserByKey(centerUser);
			JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId);
			//用户创建俱乐部后加入会员表
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(club);
			clubMember.setUserId(Integer.valueOf(userId));
			clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
			clubMember.setLevel(GameConstants.PRESIDENT);
			clubMember.setRpSendNum(0);
			clubMember.setRpReceiveNum(0);
			clubMember.setTotalReceiveRp1(0);
			clubMember.setTotalReceiveRp2(0);
			clubMember.setApplyExplain(null);
			clubMember.setIsNewRemind(0);
			clubMember.setIsNewNotice(0);
			clubMember.setIsNewRedPacket(0);
			clubMemberDao.insertClubMember(clubMember);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
	}
	/**
	 * 加入俱乐部审核通知修改状态
	 * @param clubId
	 * @author cxw
	 */
	public void updateClubStatus(String clubId){
		try {
			ClubMain clubMain2 = new ClubMain();
			clubMain2.setIsNotAudited(Integer.valueOf(GameConstants.YES));
			clubMain2.setClubId(Integer.valueOf(clubId));
			clubMainDao.updateClubMainByKey(clubMain2);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "-----updateClubStatus-----", "加入俱乐部审核通知修改状态");
		}
	}
	/**
	 * 加入俱乐部人员数量+1
	 * @param clubId
	 * @author cxw
	 */
	public void addClubMemberNum(String clubId){
		try {
			ClubMain clubMain = new ClubMain();
			clubMain.setClubId(Integer.valueOf(clubId));
			ClubMain mainEntity = clubMainDao.selectClubMainEntity(clubMain);
			Integer memberNum = mainEntity.getMemberNum();
			clubMain.setMemberNum(memberNum+1);
			clubMain.setClubId(Integer.valueOf(clubId));
			clubMainDao.updateClubMainByKey(clubMain);
		} catch (Exception e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 退出俱乐部时人员-1
	 * @param clubId
	 * @author cxw
	 */
	public void decrCurrencyClubMemberNum(String clubId){
		try {
			ClubMain clubMain = new ClubMain();
			clubMain.setClubId(Integer.valueOf(clubId));
			ClubMain mainEntity = clubMainDao.selectClubMainEntity(clubMain);
			Integer memberNum = mainEntity.getMemberNum();
			clubMain.setMemberNum(memberNum-1);
			clubMain.setClubId(Integer.valueOf(clubId));
			clubMainDao.updateClubMainByKey(clubMain);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "addClubMemberNum", "俱乐部人数加失败");
			e.printStackTrace();
		}
	}

	public  ClubMain selectClubMainEntity(ClubMain clubMain){
		return clubMainDao.selectClubMainEntity(clubMain);
	}

	/**
	 * 加入俱乐部成功时审核通知修改状态
	 * @param clubId
	 * @author cxw
	 */
	public void updateClubStatusSuccess(String clubId){
		try {
			ClubMain clubMain2 = new ClubMain();
			clubMain2.setIsNotAudited(GameConstants.NO);
			clubMain2.setClubId(Integer.valueOf(clubId));
			clubMainDao.updateClubMainByKey(clubMain2);
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "addClubMemberNum", "俱乐部人数加失败");
			e.printStackTrace();
		}
	}

	/**
	 * 加入俱乐部失败时审核通知修改状态
	 * @param clubId
	 * @author cxw
	 */
	public void updateClubStatusFail(String clubId){
		try {
			ClubMain clubMain2 = new ClubMain();
			clubMain2.setIsNotAudited(GameConstants.NO);
			clubMain2.setClubId(Integer.valueOf(clubId));
			clubMainDao.updateClubMainByKey(clubMain2);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}


}