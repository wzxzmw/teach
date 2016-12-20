package com.seentao.stpedu.club.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Service
public class SubmitClubMemberService {
	@Autowired
	private ClubMemberDao clubMemberDao;
	
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private ClubMainService clubMainService;
	/**
	 * 俱乐部会员操作
	 * @param userId  用户id
	 * @param clubId  俱乐部id
	 * @param actionType 操作 1:审核通过；2:审核拒绝加入；3:设为教练；4:取消教练资格；
	 * @param clubMemberId 会员id
	 * @author cxw
	 * @return
	 */
	public String submitClubMember(String userId, String clubId, int actionType, String clubMemberId) {

			//根据用户ID和会员ID，以及会员的状态查询出当前用户的信息
			ClubMember	clubMember = new ClubMember();
			clubMember.setUserId(Integer.valueOf(clubMemberId));
			clubMember.setClubId(Integer.valueOf(clubId));
			// 1、做通过操作，查询审核中的会员
			if(actionType ==1 || actionType == 2 ){
				clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_SH);
			}else if(actionType == 3 || actionType == 4){
				clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);	
			}
			clubMember = clubMemberDao.selectClubMember(clubMember);
			if(clubMember == null)
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CLUB_MEMBER_CANCEL_APPLA).toJSONString();
			switch (actionType) {
			// 表示审核通过，同意加入
			case GameConstants.SUBMIT_CLUB_ONE:
				clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
				try {
					clubMemberDao.updateClubMemberById(clubMember.getMemberId(), GameConstants.CLUB_MEMBER_STATE_JOIN);
					centerUserService.updateCenterUser(clubMemberId, clubId);
					//俱乐部表会员人数量+1
					clubMainService.addClubMemberNum(clubId);
					//修改俱乐部表是否存在未审核字段
					//clubMainService.updateClubStatusSuccess(clubId);
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(), clubMemberId);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				} catch (Exception e) {
					LogUtil.error(SubmitClubMemberService.class, "----GameConstants.SUBMIT_CLUB_ONE-----", "加入失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CLUB_MEMBER_OPERATION_FAILED).toJSONString();
				}
				// 审核拒绝加入	
			case GameConstants.SUBMIT_CLUB_TWO:
				try {
					clubMemberDao.updateClubMemberById(clubMember.getMemberId(), GameConstants.CLUB_MEMBER_STATE_FIAL);
					centerUserService.quitCenterUser(Integer.valueOf(clubMemberId));
					//修改俱乐部表是否存在未审核字段
					//clubMainService.updateClubStatusFail(clubId);
					JedisCache.delRedisVal(ClubMember.class.getSimpleName(), clubMemberId);
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(), clubMemberId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(SubmitClubMemberService.class, "----GameConstants.SUBMIT_CLUB_TWO-----", "审核拒绝加入");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CLUB_MEMBER_OPERATION_FAILED).toJSONString();
				}
				//设置为教练
			case GameConstants.SUBMIT_CLUB_THREE:
				clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
				clubMember.setLevel(GameConstants.CLUB_MEMBER_LEVEL_COACH);
				try {
					clubMemberDao.updateClubMemberByKey(clubMember);
					JedisCache.delRedisVal(ClubMember.class.getSimpleName(), clubMemberId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString(); 
				} catch (Exception e) {
					LogUtil.error(SubmitClubMemberService.class, "----GameConstants.SUBMIT_CLUB_THREE-----", "设置为教练失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CLUB_MEMBER_OPERATION_FAILED).toJSONString();
				}
				//取消教练资格
			case GameConstants.SUBMIT_CLUB_FOUR:
				try {
					clubMember.setLevel(GameConstants.CLUB_MEMBER_LEVEL_MEMBER);
					clubMemberDao.updateClubMemberByKey(clubMember);
					JedisCache.delRedisVal(ClubMember.class.getSimpleName(), clubMemberId);
					return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString(); 
				} catch (Exception e) {
					LogUtil.error(SubmitClubMemberService.class, "----GameConstants.SUBMIT_CLUB_THREE-----", "设置为教练失败");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.CLUB_MEMBER_OPERATION_FAILED).toJSONString();
				}
			default:
				
				 return Common.getReturn(AppErrorCode.DEFAULT, "").toJSONString();
			}
	}

}
