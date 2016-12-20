package com.seentao.stpedu.account.service;


import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.CenterAccountDao;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
@Service
public class VirtualGoodsService {

	@Autowired
	private CenterAccountDao centerAccountDao;

	@Autowired
	private ClubMemberDao clubMemberDao;
	/**
	 * 获取登录用户的虚拟物品
	 * @param userName
	 * @param userType
	 * @return
	 * @author chaixw
	 */
	public JSONObject getVirtualGoodsCount(String userId) {

		try {
			JSONObject josn = new JSONObject();
			//获取用户账户虚拟金额
			CenterAccount centerAccount = new CenterAccount();
			centerAccount.setUserId(Integer.valueOf(userId));
			centerAccount.setUserType(GameConstants.INDIVIDUAL_USER);
			centerAccount.setAccountType(GameConstants.STAIR_TWO);
			CenterAccount account = centerAccountDao.selectCenterAccountType(centerAccount);
			if(account == null){
				LogUtil.error(this.getClass(), "getFLevelAccount", "没有虚拟货币");
				JSONObject json = new JSONObject();
				json.put("virtualGoodsCount", 0);
				return Common.getReturn(AppErrorCode.SUCCESS, "",json);
			}
			//返回可用虚拟金额
			if(account.getLockAmount()==null){
				josn.put("virtualGoodsCount", account.getBalance().intValue());
				return Common.getReturn(AppErrorCode.SUCCESS, "", josn);
			}else{
				int money = account.getBalance().intValue()-account.getLockAmount().intValue();//(account.getBalance() - account.getLockAmount());
				if(money<=0){
					josn.put("virtualGoodsCount", 0);
				}else{
					josn.put("virtualGoodsCount", money);
				}
				return Common.getReturn(AppErrorCode.SUCCESS, "", josn);
				
			}
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "getVirtualGoodsCount", "获取虚拟货币失败");
			e.printStackTrace();
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.FAILED_TO_OBTAIN_VIRTUAL_CURRENCY);
		}
		
	}
	/***
	 *  获取一级货币
	 * @param userId 用户id
	 * @param clubId  俱乐 部id
	 * @param inquireType 查询类型 1、个人一级货币 2、俱乐部一级货币
	 * @return
	 */
	public String getFLevelAccount(String userId, String clubId, int inquireType) {

		if(inquireType == GameConstants.GET_USER_FIR_CURRENCY){
			//根据用户Id查询一级货币
			try {
				JSONObject JSON = new JSONObject();
				CenterAccount centerAccount = new CenterAccount();
				centerAccount.setUserId(Integer.valueOf(userId));
				centerAccount.setAccountType(GameConstants.STAIR_ONE);
				centerAccount.setUserType(GameConstants.INDIVIDUAL_USER);
				CenterAccount account = centerAccountDao.selectCenterAccountType(centerAccount);
				if(account == null){
					LogUtil.error(this.getClass(), "getFLevelAccount", "没有一级货币");
					JSONObject json = new JSONObject();
					json.put("canChangeCash", 0);
					json.put("fLevelAccount", 0);
					return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
				}
				//计算可兑换现金
				if(account.getLockAmount()==null){
					double canChangeCash = account.getBalance()/BusinessConstant.RECHARGE_RATIO_ONE_LEVEL;
					
					DecimalFormat    df   = new DecimalFormat("######0.00"); 
					String format = df.format(canChangeCash);
					//yy修改 1:1提现
					JSON.put("fLevelAccount", account.getBalance());
					JSON.put("canChangeCash", account.getBalance());
					return Common.getReturn(AppErrorCode.SUCCESS, "获取一级货币成功", JSON).toJSONString();
				}else{
					double money = (account.getBalance() - account.getLockAmount());
					double canChangeCash = money/BusinessConstant.RECHARGE_RATIO_ONE_LEVEL;
					
					DecimalFormat    df   = new DecimalFormat("######0.00"); 
					String format = df.format(canChangeCash);
					
					JSON.put("fLevelAccount", money);
					JSON.put("canChangeCash", format);
					return Common.getReturn(AppErrorCode.SUCCESS, "获取一级货币成功", JSON).toJSONString();
				}
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "getFLevelAccount", "获取一级货币失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "获取一级货币失败").toJSONString();
			}
		}
		if(inquireType == GameConstants.GET_CLUB_FIR_CURRENCY){
			try {
				//校验当前useid的权限
				JSONObject JSON = new JSONObject();
				ClubMember clubMember = new ClubMember();
				clubMember.setUserId(Integer.valueOf(userId));
				clubMember.setClubId(Integer.valueOf(clubId));
				List<ClubMember> main = clubMemberDao.selectSingleClubMember(clubMember);
				if(null == main || main.size()<=0){
					LogUtil.error(this.getClass(), "getFLevelAccount", "获取俱乐部一级货币查询信息有误");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, "获取俱乐部一级货币查询信息有误").toJSONString();
				}
				for (ClubMember member : main) {
					if(member.getMemberStatus() != GameConstants.CLUB_MEMBER_STATE_JOIN && member.getLevel() != GameConstants.PRESIDENT && member.getLevel() != GameConstants.COACH){
						LogUtil.error(this.getClass(), "getFLevelAccount", "你没有权限");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_THREE, "没有权限").toJSONString();
					}
					//根据用户Id查询一级货币
					CenterAccount centerAccount = new CenterAccount();
					centerAccount.setUserId(Integer.valueOf(clubId));
					centerAccount.setAccountType(GameConstants.STAIR_ONE);
					centerAccount.setUserType(GameConstants.CLUB_USER);
					CenterAccount account = centerAccountDao.selectCenterAccountType(centerAccount);
					if(account == null || account.getBalance() == null){
						LogUtil.error(this.getClass(), "getFLevelAccount", "俱乐部没有一级货币");
						JSONObject json = new JSONObject();
						json.put("canChangeCash", 0);
						json.put("fLevelAccount", 0);
						return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
					}
					//计算可兑换现金
					if(account.getLockAmount() == null){
						double canChangeCash = account.getBalance()/BusinessConstant.RECHARGE_RATIO_ONE_LEVEL;
						
						DecimalFormat    df   = new DecimalFormat("######0.00"); 
						String format = df.format(canChangeCash);
						
						JSON.put("fLevelAccount", account.getBalance());
						JSON.put("canChangeCash", account.getBalance());
						return Common.getReturn(AppErrorCode.SUCCESS, "获取一级货币成功", JSON).toJSONString();
					}else{
						double money = (account.getBalance() - account.getLockAmount());
						double canChangeCash = money/BusinessConstant.RECHARGE_RATIO_ONE_LEVEL;

						DecimalFormat    df   = new DecimalFormat("######0.00"); 
						String format = df.format(canChangeCash);
						
						JSON.put("fLevelAccount", money);
						JSON.put("canChangeCash", money);
						return Common.getReturn(AppErrorCode.SUCCESS, "获取一级货币成功", JSON).toJSONString();
					}
				}

			} catch (Exception e) {
				LogUtil.error(this.getClass(), "getFLevelAccount", "获取一级货币失败");
				e.printStackTrace();
				return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, "获取一级货币失败").toJSONString();
			}
		}
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
	}
}



