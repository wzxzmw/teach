package com.seentao.stpedu.redpacket.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.PageObject;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubRedPacketType;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.ResRedPacket;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterSerialMaxService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.common.service.ClubRedPacketService;
import com.seentao.stpedu.common.service.ClubRedPacketTypeService;
import com.seentao.stpedu.common.service.ClubRelRedPacketMemberService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

/** 
* @author yy
* @date 2016年6月27日 下午5:42:36 
*/
@Service
public class BusinessRedPacketService {
	@Autowired
	private CenterAccountService centerAccountService;
	@Autowired
	private ClubRedPacketTypeService clubRedPacketTypeService;
	@Autowired
	private ClubRedPacketService clubRedPacketService;
	@Autowired
	private ClubMemberService clubMemberService;
	@Autowired
	private ClubRelRedPacketMemberService clubRelRedPacketMemberService;
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	@Autowired
	private CenterSerialMaxService centerSerialMaxService;
	@Autowired
	private CenterLiveService centerLiveService;
	/**
	 * 发红包
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param redPacketContent 红包内容
	 * @param redPacketObject 赠送对象(1:全部会员；2:指定的人；3:俱乐部；)
	 * @param redPacketUserIds 赠送人员id 多个id以逗号分隔
	 * @param redPacketType 红包类型 1:一级货币；2:鲜花；
	 * @param perRedPacketCount 每人赠送的数量
	 */
	@Transactional
	public String submitRedPacket(String userId, Integer redPacketObject, Integer redPacketType,
			Integer perRedPacketCount, String clubId, String redPacketContent, String redPacketUserIds) {
		LogUtil.info(this.getClass(), "submitRedPacket", "userId="+userId+",redPacketObject="+redPacketObject+
				",redPacketType="+redPacketType+",perRedPacketCount="+perRedPacketCount+",redPacketUserIds="+redPacketUserIds);
		JSONObject jo = null;
		if(redPacketObject==BusinessConstant.RED_PACKET_OBJECT_1){//全部会员
			//根据用户id查询俱乐部下所有通过审核的会员
			ClubMember clubMember = new ClubMember();
			clubMember.setClubId(Integer.valueOf(clubId));
			clubMember.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);//会员状态。1:已加入，2:审核中，3:审核失败，4:已退出。
			List<ClubMember>  list = clubMemberService.getClubMember(clubMember);
			if(list!=null && list.size()>0){
				List<Integer> sendUserIdList = new ArrayList<Integer>();
				for (int i = 0; i < list.size(); i++) {
					Integer id = list.get(i).getUserId();
					//不给自己发红包
					if(!String.valueOf(id).equals(userId)){
						sendUserIdList.add(id);
					}
				}
				jo = this.submitRedPacketBranch(userId,redPacketType,perRedPacketCount,redPacketContent,sendUserIdList,redPacketObject,clubId);
				LogUtil.info(this.getClass(), "submitRedPacket", "给全部会员发送红包成功");
			}else{
				LogUtil.error(this.getClass(), "submitRedPacket", "俱乐部下会员为空");
				jo = new JSONObject();
				jo.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_FIVE);
				jo.put(GameConstants.MSG, BusinessConstant.ERROR_USER_DOES_NOT_EXIST);
				return jo.toJSONString();
			}
		}else if(redPacketObject==BusinessConstant.RED_PACKET_OBJECT_2){//指定的人
			List<Integer> sendUserIdList = new ArrayList<Integer>();
			String[] arrayIds = redPacketUserIds.split(",");
			for (int i = 0; i < arrayIds.length; i++) {
				sendUserIdList.add(Integer.valueOf(arrayIds[i]));
			}
			jo = this.submitRedPacketBranch(userId,redPacketType,perRedPacketCount,redPacketContent,sendUserIdList,redPacketObject,clubId);
			LogUtil.info(this.getClass(), "submitRedPacket", "给部分会员发送红包成功");
		}else if(redPacketObject==BusinessConstant.RED_PACKET_OBJECT_3){//俱乐部
			Integer redPacketId = this.adequacyOfBalance(1,perRedPacketCount,redPacketType,userId, clubId, redPacketContent, redPacketObject);
			//红包主键id
			if(redPacketId!=null){
				jo = this.submitRedPacketClub(userId,redPacketType,perRedPacketCount,redPacketContent,redPacketObject,clubId,redPacketId);
				LogUtil.info(this.getClass(), "submitRedPacket", "给俱乐部发送红包成功");
			}else{
				//余额不足
				jo = new JSONObject();
				jo.put(GameConstants.CODE, AppErrorCode.ERROR_COUNT_NOT);
				jo.put(GameConstants.MSG, BusinessConstant.USER_ACCOUNT_LACK);
			}
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
		}
		return jo.toJSONString();
	}

	//发送俱乐部红包
	private JSONObject submitRedPacketClub(String userId, Integer redPacketType, Integer perRedPacketCount,
			String redPacketContent, Integer redPacketObject,String clubId, Integer redPacketId) {
		JSONObject json = new JSONObject();
		//俱乐部账户增加金额
		CenterAccount centerAccount = new CenterAccount();
		centerAccount.setUserType(2);//俱乐部账户
		centerAccount.setUserId(Integer.valueOf(clubId));
		CenterAccount acount = centerAccountService.getCenterAccount(centerAccount);
		if(acount==null){
			LogUtil.info(BusinessRedPacketService.class, "submitRedPacketClub", "该俱乐部"+clubId+"没有俱乐部账户!");
		}
		CenterAccount updateAccount = new CenterAccount();
		updateAccount.setAccountId(acount.getAccountId());
		updateAccount.setBalance(acount.getBalance()+perRedPacketCount);
		centerAccountService.updateCenterAccountByKey(updateAccount);
		//资金流动表增加记录
		//流水号(3)
		String max = centerSerialMaxService.getCenterSerialMaxByNowDate(redPacketType);
		CenterMoneyChange oneLeveChange = new CenterMoneyChange();
		oneLeveChange.setAccountId(acount.getAccountId());//账户id
		oneLeveChange.setSerialNumber(max);//流水号
		oneLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
		oneLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
		oneLeveChange.setAmount((double)perRedPacketCount);//变动金额
		oneLeveChange.setChangeType(1);//变动类型(1收入，2支出,3提现)
		oneLeveChange.setRelObjetType(2);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
		oneLeveChange.setRelObjetId(redPacketId);//关联id(红包id)
		oneLeveChange.setStatus(1);//状态(状态。1:成功，2:失败，3:过程中。)
		centerMoneyChangeService.insertCenterMoneyChange(oneLeveChange);
		//更新该会员发送红包数量
		//更新该会员发送红包数量
		ClubMember clubmemberOne = new ClubMember();
		clubmemberOne.setUserId(Integer.valueOf(userId));
		clubmemberOne.setClubId(Integer.valueOf(clubId));
		//<if test="rpSendNum != null"> rp_send_num = rp_send_num+1, </if>
		clubmemberOne.setRpSendNum(0);//该参数不为空即可
		clubMemberService.updateByUserIdAndClubId(clubmemberOne);
		
		//插入关系表
		ClubRelRedPacketMember clubRelRedPacketMember = new ClubRelRedPacketMember();
		//根据俱乐部id获取会长id
		//人员信息
		String clubmainCache = RedisComponent.findRedisObject(Integer.valueOf(clubId), ClubMain.class);
		ClubMain clubmain = null;
		if(!StringUtil.isEmpty(clubmainCache)){
			clubmain = JSONObject.parseObject(clubmainCache,ClubMain.class);
		}
		clubRelRedPacketMember.setRpId(redPacketId);//红包id
		clubRelRedPacketMember.setUserId(clubmain==null?0:clubmain.getCreateUserId());//接收该红包的俱乐部会长id
		clubRelRedPacketMember.setUserType(2);//俱乐部
		clubRelRedPacketMemberService.insertClubRelRedPacketMember(clubRelRedPacketMember);
		//增加动态
		CenterLive addcenterLive = new CenterLive();
		addcenterLive.setCreateTime(TimeUtil.getCurrentTimestamp());
		addcenterLive.setIsDelete(GameConstants.NO_DEL);
		//动态类型。1:俱乐部话题，2:俱乐部擂台，3:俱乐部培训，4:俱乐部提醒，5:俱乐部通知，6:俱乐部红包，7:俱乐部邀请答疑，8:教学通知，9:教学答疑，10:教学实训，11:用户关注职位动态，12:用户关注俱乐部动态，13:用户关注人动态，14:用户加入俱乐部动态，15:用户参加比赛动态。
		addcenterLive.setLiveType(6);
		//动态主体类型。1:俱乐部话题，2:比赛，3:俱乐部培训班，4:俱乐部提醒，5:俱乐部通知，6:俱乐部红包，7:俱乐部邀请答疑，8:教学通知，9:教学答疑，10:企业，11:俱乐部，12:用户。
		addcenterLive.setLiveMainType(6);
		addcenterLive.setLiveMainId(redPacketId);//动态主题id  红包id
		addcenterLive.setIsTop(0);//是否置顶
		addcenterLive.setMainObjetId(Integer.valueOf(clubId));//主体用户ID
		addcenterLive.setMainObjetType(GameConstants.LIVE_MAIN_CLUB_TYPE);//动态用户类型。1:用户动态，2:俱乐部动态，3:班级动态。
		centerLiveService.insertCenterLive(addcenterLive);
		json.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		return json;
	}

	private JSONObject submitRedPacketBranch(String userId, Integer redPacketType, Integer perRedPacketCount,
			String redPacketContent, List<Integer> redPacketUserIds,Integer redPacketObject,String clubId) {
		JSONObject json = new JSONObject();
		//判断发红包者余额，如果余额充足，则减少账户金额，并增加资金变动记录 并返回红包id
		Integer redPacketId = this.adequacyOfBalance(redPacketUserIds.size(),perRedPacketCount,redPacketType,userId, clubId, redPacketContent, redPacketObject);
		//红包主键id
		if(redPacketId!=null){
			this.updateAccount(redPacketUserIds,perRedPacketCount,redPacketType,userId,redPacketContent,redPacketObject,redPacketId,clubId);
			json.put(GameConstants.CODE, AppErrorCode.SUCCESS);
		}else{
			//余额不足
			json.put(GameConstants.CODE, AppErrorCode.ERROR_COUNT_NOT);
			json.put(GameConstants.MSG, BusinessConstant.USER_ACCOUNT_LACK);
		}
		return json;
	}

	private boolean updateAccount(List<Integer> sendUserIds, Integer perRedPacketCount,
			Integer redPacketType,String userId,String redPacketContent,Integer redPacketObject,Integer redPacketId,String clubId) {
		boolean flag = true;
		try {
			Integer intUserId = Integer.valueOf(userId);
			//人员信息
			String cacheUser = RedisComponent.findRedisObject(intUserId, CenterUser.class);
			CenterUser centeruser = null;
			if(!StringUtil.isEmpty(cacheUser)){
				centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
			}
			
			//数据库记录单条操作
			//更新该会员发送红包数量
			ClubMember clubmemberOne = new ClubMember();
			clubmemberOne.setUserId(intUserId);
			clubmemberOne.setClubId(centeruser==null?null:centeruser.getClubId());
			//<if test="rpSendNum != null"> rp_send_num = rp_send_num+1, </if>
			clubmemberOne.setRpSendNum(0);//该参数不为空即可
			clubMemberService.updateByUserIdAndClubId(clubmemberOne);
			
			//动态表加动态
			CenterLive addcenterLive = new CenterLive();
			addcenterLive.setCreateTime(TimeUtil.getCurrentTimestamp());
			addcenterLive.setIsDelete(GameConstants.NO_DEL);
			//动态类型。1:俱乐部话题，2:俱乐部擂台，3:俱乐部培训，4:俱乐部提醒，5:俱乐部通知，6:俱乐部红包，7:俱乐部邀请答疑，8:教学通知，9:教学答疑，10:教学实训，11:用户关注职位动态，12:用户关注俱乐部动态，13:用户关注人动态，14:用户加入俱乐部动态，15:用户参加比赛动态。
			addcenterLive.setLiveType(6);
			//动态主体类型。1:俱乐部话题，2:比赛，3:俱乐部培训班，4:俱乐部提醒，5:俱乐部通知，6:俱乐部红包，7:俱乐部邀请答疑，8:教学通知，9:教学答疑，10:企业，11:俱乐部，12:用户。
			addcenterLive.setLiveMainType(6);
			addcenterLive.setLiveMainId(redPacketId);//动态主题id 红包id
			addcenterLive.setIsTop(0);//是否置顶
			addcenterLive.setMainObjetId(Integer.valueOf(clubId));//主体用户ID
			addcenterLive.setMainObjetType(2);//动态用户类型。1:用户动态，2:俱乐部动态，3:班级动态。
			centerLiveService.insertCenterLive(addcenterLive);
			
			//(1批量)
			List<CenterAccount> batchAccountList = new ArrayList<CenterAccount>();
			//(2批量)
			List<ClubMember> batchClubMemberList = new ArrayList<ClubMember>();
			//(4批量)
			List<ClubRelRedPacketMember> batchRelRedPacketList = new ArrayList<ClubRelRedPacketMember>();
			for (int i = 0; i < sendUserIds.size(); i++) {
				Integer updateId = sendUserIds.get(i);
				
				//修改用户账户金额(1批量)
				CenterAccount centeraccount = new CenterAccount();
				centeraccount.setUserId(updateId);//用户id
				centeraccount.setAccountType(redPacketType);//用户账户类型
				centeraccount.setBalance((double)perRedPacketCount);//增加金额
				batchAccountList.add(centeraccount);
				
				//修改俱乐部会员表(2批量)
				ClubMember clubmember = new ClubMember();
				clubmember.setUserId(updateId);//用户id
				if(redPacketType==GameConstants.STAIR_ONE){//新道B红包
					clubmember.setTotalReceiveRp1(perRedPacketCount);//每人一级货币增加数量
				}else{
					clubmember.setTotalReceiveRp2(perRedPacketCount);//每人二级货币增加数量
				}
				//clubmember.setClubId(0);//俱乐部id
				clubmember.setIsNewRedPacket(1);//是否有红包存在
				batchClubMemberList.add(clubmember);
				
				//插入俱乐部红包会员关系表(4批量)
				ClubRelRedPacketMember clubRelRedPacketMember = new ClubRelRedPacketMember();
				clubRelRedPacketMember.setRpId(redPacketId);//红包id
				clubRelRedPacketMember.setUserId(updateId);//发送的人id
				clubRelRedPacketMember.setUserType(1);//会员
				batchRelRedPacketList.add(clubRelRedPacketMember);
				
			}
			//(1批量)
			centerAccountService.batchUpdateByUserIdAndAccountType(batchAccountList);
			//(2批量)
			clubMemberService.batchUpdateByUserIdAndClubId(batchClubMemberList);
			//(4批量)
			clubRelRedPacketMemberService.batchInsertClubRelRedPacketMember(batchRelRedPacketList);
			
			//资金变动记录增加(3批量)
			List<CenterAccount> accountList = centerAccountService.getCenterAccountList(sendUserIds, redPacketType);
			//流水号(3)
			String max = centerSerialMaxService.getCenterSerialMaxByNowDate(redPacketType);
			List<CenterMoneyChange> centerMoneyChangeList = new ArrayList<CenterMoneyChange>();
			for (int i = 0; i < accountList.size(); i++) {
				CenterAccount account = accountList.get(i);
				CenterMoneyChange oneLeveChange = new CenterMoneyChange();
				oneLeveChange.setAccountId(account.getAccountId());//账户id
				oneLeveChange.setSerialNumber(max);//流水号
				oneLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
				oneLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
				oneLeveChange.setAmount((double)perRedPacketCount);//变动金额
				oneLeveChange.setChangeType(GameConstants.INCOME);//变动类型(1收入，2支出,3提现)
				oneLeveChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_RED);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
				oneLeveChange.setRelObjetId(redPacketId);//关联id(红包id)
				oneLeveChange.setStatus(GameConstants.SUCCESSFUL);//状态(状态。1:成功，2:失败，3:过程中。)
				centerMoneyChangeList.add(oneLeveChange);
			}
			//(3批量)
			centerMoneyChangeService.insertCenterMoneyChange(centerMoneyChangeList);
		} catch (Exception e){
			flag = false;
			LogUtil.error(BusinessRedPacketService.class, "updateAccount", "扣除用户消费金额异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return flag;
	}

	//判断余额,如果余额足够则更新消费金额
	private Integer adequacyOfBalance(Integer redPacketUserIds, Integer perRedPacketCount, Integer redPacketType,
			String userId,String clubId,String redPacketContent,Integer redPacketObject) {
		Integer sign = null;
		Double oneLevel = 0.0;//一级账户余额
		Double twoLevel = 0.0;//二级账户余额
		Integer oneLevelAccountId = null;//一级账户id
		Integer twoLevelAccountId = null;//二级账户id
		CenterAccount centerAccount = new CenterAccount();
		centerAccount.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//个人账户
		centerAccount.setUserId(Integer.valueOf(userId));
		List<CenterAccount> acountList = centerAccountService.getCenterAccountList(centerAccount);
		if(!CollectionUtils.isEmpty(acountList) && acountList.size()==2){
			for (int i = 0; i < acountList.size(); i++) {
				CenterAccount account = acountList.get(i);
				if(account.getAccountType()==GameConstants.STAIR_ONE){//一级账户
					oneLevel = account.getBalance();
					oneLevelAccountId = account.getAccountId();
				}else if(account.getAccountType()==GameConstants.STAIR_TWO){//二级账户
					Double twoLevelOld = account.getBalance();//鲜花余额
					//真正的余额=余额-锁定金额
					twoLevel = twoLevelOld-account.getLockAmount();
					twoLevelAccountId = account.getAccountId();
				}
			}
			Double amount = Double.valueOf(String.valueOf(redPacketUserIds*perRedPacketCount));
			if(redPacketType==GameConstants.STAIR_ONE){//一级货币
				if(amount <= oneLevel){
					//扣除一级账户金额
					CenterAccount updateAccount = new CenterAccount();
					updateAccount.setAccountId(oneLevelAccountId);
					updateAccount.setBalance(oneLevel-amount);
					centerAccountService.updateCenterAccountByKey(updateAccount);
					//插入俱乐部红包表
					ClubRedPacket clubRedPacket = new ClubRedPacket();
					clubRedPacket.setClubId(Integer.valueOf(clubId));//俱乐部id
					clubRedPacket.setCreateTime(TimeUtil.getCurrentTimestamp());//创建时间
					clubRedPacket.setCreateUserId(Integer.valueOf(userId));//创建人
					clubRedPacket.setEverybodyNum(perRedPacketCount);//每人数量
					clubRedPacket.setMessage(redPacketContent);//红包内容
					clubRedPacket.setType(redPacketType);//红包类型
					clubRedPacket.setUserType(redPacketObject);//用户类型。1:部分会员，2:全部会员，3:俱乐部。
					clubRedPacketService.insertClubRedPacket(clubRedPacket);
					sign = clubRedPacket.getRedPacketId();
					//插入金额变动金额
					//流水号
					String max = centerSerialMaxService.getCenterSerialMaxByNowDate(redPacketType);
					//变动表一级货币增加记录
					CenterMoneyChange oneLeveChange = new CenterMoneyChange();
					oneLeveChange.setAccountId(oneLevelAccountId);//账户id
					oneLeveChange.setSerialNumber(max);//流水号
					oneLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
					oneLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
					oneLeveChange.setAmount(amount);//变动金额
					oneLeveChange.setChangeType(GameConstants.SPENDING);//变动类型(1收入，2支出,3提现)
					oneLeveChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_RED);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
					oneLeveChange.setRelObjetId(sign);//关联id
					oneLeveChange.setStatus(GameConstants.SUCCESSFUL);//状态(状态。1:成功，2:失败，3:过程中。)
					centerMoneyChangeService.insertCenterMoneyChange(oneLeveChange);
				}else{
					sign = null;
				}
			}else if(redPacketType==GameConstants.STAIR_TWO){//二级货币
				if(amount <= twoLevel){
					//扣除二级账户余额
					CenterAccount updateAccount = new CenterAccount();
					updateAccount.setAccountId(twoLevelAccountId);
					updateAccount.setBalance(twoLevel-amount);
					centerAccountService.updateCenterAccountByKey(updateAccount);
					//插入金额变动
					//插入俱乐部红包表
					ClubRedPacket clubRedPacket = new ClubRedPacket();
					clubRedPacket.setClubId(Integer.valueOf(clubId));//俱乐部id
					clubRedPacket.setCreateTime(TimeUtil.getCurrentTimestamp());//创建时间
					clubRedPacket.setCreateUserId(Integer.valueOf(userId));//创建人
					clubRedPacket.setEverybodyNum(perRedPacketCount);//每人数量
					clubRedPacket.setMessage(redPacketContent);//红包内容
					clubRedPacket.setType(redPacketType);//红包类型
					clubRedPacket.setUserType(redPacketObject);//用户类型。1:部分会员，2:全部会员，3:俱乐部。
					clubRedPacketService.insertClubRedPacket(clubRedPacket);
					sign = clubRedPacket.getRedPacketId();
					//流水号
					String max = centerSerialMaxService.getCenterSerialMaxByNowDate(redPacketType);
					//变动表二级货币增加记录
					CenterMoneyChange oneLeveChange = new CenterMoneyChange();
					oneLeveChange.setAccountId(twoLevelAccountId);//账户id
					oneLeveChange.setSerialNumber(max);//流水号
					oneLeveChange.setChangeTime(TimeUtil.getCurrentTimestamp());//变动时间
					oneLeveChange.setLockId(null);//锁定id(充值，提现，竞猜)
					oneLeveChange.setAmount(amount);//变动金额
					oneLeveChange.setChangeType(GameConstants.SPENDING);//变动类型(1收入，2支出,3提现)
					oneLeveChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_RED);//关联对象类型(关联对象类型。1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买。)
					oneLeveChange.setRelObjetId(sign);//关联id
					oneLeveChange.setStatus(GameConstants.SUCCESSFUL);//状态(状态。1:成功，2:失败，3:过程中。)
					centerMoneyChangeService.insertCenterMoneyChange(oneLeveChange);
				}else{
					sign = null;
				}
			}else{
				sign = null;
			}
		}else{
			sign = null;
		}
		return sign;
	}

	/**
	 * 获取红包类型信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param inquireType 查询类型
	 */
	public String getRedPacketType(String userId, Integer inquireType) {
		LogUtil.info(this.getClass(), "getRedPacketType", "开始调用,userId="+userId+",inquireType"+inquireType);
		JSONObject json = new JSONObject();
		if(inquireType==BusinessConstant.INQUIRETYPE_1){
			//查询红包类型
			List<ClubRedPacketType> list = clubRedPacketTypeService.getClubRedPacketType(null);
			if(!CollectionUtils.isEmpty(list)){
				//查询用户下边的两个账户
				CenterAccount centerAccount = new CenterAccount();
				centerAccount.setUserId(Integer.valueOf(userId));
				centerAccount.setUserType(BusinessConstant.USER_ACCOUNT_TYPE_1);//用户类型。1:个人用户，2:俱乐部用户。
				List<CenterAccount> countList = centerAccountService.getCenterAccountList(centerAccount);
				Integer oneLeve = 0;//一级账户余额
				Integer twoLeve = 0;//二级账户余额
				if(!CollectionUtils.isEmpty(countList)){
					for (int i = 0; i < countList.size(); i++) {
						CenterAccount account = countList.get(i);
						if(account.getAccountType()==GameConstants.STAIR_ONE){//一级账户
							oneLeve = (new Double(account.getBalance())).intValue(); 
						}else if(account.getAccountType()==GameConstants.STAIR_TWO){//二级账户
							twoLeve = (new Double(account.getBalance())).intValue(); 
						}
					}
					for (int i = 0; i < list.size(); i++) {
						ClubRedPacketType clubredpackettype = list.get(i);
						if(clubredpackettype.getTypeId()==BusinessConstant.CLUB_RED_PACKET_TYPE_1){//RMB红包
							clubredpackettype.setRedPacketCount(oneLeve);
						}else if(clubredpackettype.getTypeId()==BusinessConstant.CLUB_RED_PACKET_TYPE_2){//虚拟币红包
							clubredpackettype.setRedPacketCount(twoLeve);
						}
					}
					json.put("redPacketTypes",list);
					LogUtil.info(this.getClass(), "getRedPacketType", "获取红包类型信息成功");
					return Common.getReturn(AppErrorCode.SUCCESS, "",json).toJSONString();
				}else{//用户没有账户
					return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.USER_NOT_ACCOUNT).toJSONString();
				}
			}else{//没有配置字典表
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.REDPACKET_NOT_REDEPLOY).toJSONString();
			}
		}else{//传入参数错误
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, BusinessConstant.ERROR_TYPE_CODE).toJSONString();
		}
	}
	
	/**
	 * 获取红包信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param redPacketShowType 红包显示分类(1：收到的红包；2:我发的红包；3:大家收到的红包排名；)
	 * @param start 开始
	 * @param limit 结束
	 */
	@Transactional
	public String getRedPackets(String userId, String clubId, Integer redPacketShowType, Integer start, Integer limit) {
		LogUtil.info(this.getClass(), "getRedPackets", "开始调用,userId="+userId+",redPacketShowType"+redPacketShowType);
		if(redPacketShowType==BusinessConstant.REDPACKETSHOWTYPE_1){//收到的红包1
			List<ResRedPacket> resList = new ArrayList<ResRedPacket>();
			Map<String,Integer> param = new HashMap<String,Integer>();
			param.put("user_id", Integer.valueOf(userId));
			param.put("start", start);
			param.put("limit", limit);
			Integer count = clubRedPacketService.getClubRedPacketCount(param);
			List<ClubRedPacket> redPacketlist = clubRedPacketService.getClubRedPacketList(param);
			if(redPacketlist!=null && redPacketlist.size()!=0){
				//修改红包红点
				ClubMember update = new ClubMember();
				update.setIsNewRedPacket(0);
				update.setClubId(Integer.valueOf(clubId));
				update.setUserId(Integer.valueOf(userId));
				clubMemberService.updateByUserIdAndClubId(update);
				for (int i = 0; i < redPacketlist.size(); i++) {
					ClubRedPacket resClubRedPacket = redPacketlist.get(i);
					ResRedPacket res = new ResRedPacket();
					//人员信息
					String cacheUser = RedisComponent.findRedisObject(resClubRedPacket.getCreateUserId(), CenterUser.class);
					CenterUser centeruser = null;
					if(!StringUtil.isEmpty(cacheUser)){
						centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					}
					res.setRedPacketId(String.valueOf(resClubRedPacket.getRedPacketId()));//红包id
					res.setRpCreaterId(String.valueOf(resClubRedPacket.getCreateUserId()));//红包创建人
					res.setRpCreaterName(centeruser.getRealName());//发送人真实姓名
					res.setRpCreaterNickname(centeruser.getNickName());//发送人昵称
					//图片信息
					String cachePublicPicture = RedisComponent.findRedisObject(centeruser.getHeadImgId(), PublicPicture.class);
					if(!StringUtil.isEmpty(cachePublicPicture)){//个人头像信息
						PublicPicture publicpicture = JSONObject.parseObject(cachePublicPicture,PublicPicture.class);
						res.setRpCreaterHeadLink(Common.checkPic(publicpicture.getFilePath()) == true ? publicpicture.getFilePath()+ActiveUrl.HEAD_MAP:publicpicture.getFilePath());//发送人头像
					}
					ClubMember clubMember = new ClubMember();
					clubMember.setClubId(centeruser.getClubId());
					clubMember.setUserId(centeruser.getUserId());
					ClubMember resClubMember = clubMemberService.queryClubMemberByClubIdAndUserId(centeruser.getUserId(),centeruser.getClubId());
					res.setRpClubRole(resClubMember.getLevel());//发红的人在俱乐部的角色
					res.setRedPacketContent(resClubRedPacket.getMessage());//红包内容
					res.setRpCreateDate(String.valueOf(resClubRedPacket.getCreateTime()));//红包创建时间
					res.setRedPacketType(resClubRedPacket.getType());//红包类型
					res.setRedPacketCount(resClubRedPacket.getEverybodyNum());//每人数量
					resList.add(res);
				}
			}
			JSONObject jo = PageObject.getPageObject(count, start, limit, resList, "redPackets");
			LogUtil.info(this.getClass(), "getRedPackets", "获取收到的红包信息成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", jo).toJSONString();
		}else if(redPacketShowType==BusinessConstant.REDPACKETSHOWTYPE_2){//我发的红包
			List<ResRedPacket> resList = new ArrayList<ResRedPacket>();
			Map<String,Integer> param = new HashMap<String,Integer>();
			param.put("create_user_id", Integer.valueOf(userId));
			param.put("start", start);
			param.put("limit", limit);
			Integer count = clubRedPacketService.getClubRedPacketCount(param);
			List<ClubRedPacket> redPacketlist = clubRedPacketService.getClubRedPacketList(param);
			if(redPacketlist!=null){
				for (int i = 0; i < redPacketlist.size(); i++) {
					ClubRedPacket resClubRedPacket = redPacketlist.get(i);
					List<ClubRelRedPacketMember> userIds = resClubRedPacket.getReceiveUserList();
					for (int j = 0; j < userIds.size(); j++) {
						ClubRelRedPacketMember  clubrelredpacketmember = userIds.get(j);
						ResRedPacket res = new ResRedPacket();
						//人员信息
						String cacheUser = RedisComponent.findRedisObject(clubrelredpacketmember.getUserId(), CenterUser.class);
						CenterUser centeruser = null;
						if(!StringUtil.isEmpty(cacheUser)){
							centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
						}
						res.setRpReceiverId(String.valueOf(clubrelredpacketmember.getUserId()));//红包接收用户id
						res.setRpReceiverName(centeruser.getRealName());//红包接收人姓名
						res.setRpReceiverNickname(centeruser.getNickName());//红包接收人昵称
						//图片信息
						String cachePublicPicture = RedisComponent.findRedisObject(centeruser.getHeadImgId(), PublicPicture.class);
						if(!StringUtil.isEmpty(cachePublicPicture)){//个人头像信息
							PublicPicture publicpicture = JSONObject.parseObject(cachePublicPicture,PublicPicture.class);
							res.setRpReceiverHeadLink(Common.checkPic(publicpicture.getFilePath()) == true ? publicpicture.getFilePath()+ActiveUrl.HEAD_MAP:publicpicture.getFilePath());//接收人头像连接
						}
						ClubMember clubMember = new ClubMember();
						clubMember.setClubId(centeruser.getClubId());
						clubMember.setUserId(centeruser.getUserId());
						ClubMember resClubMember = clubMemberService.getClubMemberOne(clubMember);
						res.setRpReceiverClubRole(resClubMember==null?0:resClubMember.getLevel());//接收者在俱乐部角色
						res.setRedPacketType(resClubRedPacket.getType());//红包类型
						res.setRedPacketCount(resClubRedPacket.getEverybodyNum());//红包数量
						res.setRedPacketContent(resClubRedPacket.getMessage());//红包内容
						res.setRpCreateDate(String.valueOf(resClubRedPacket.getCreateTime()));//红包日期
						resList.add(res);
					}
				}
			}
			JSONObject jo = PageObject.getPageObject(count, start, limit, resList, "redPackets");
			LogUtil.info(this.getClass(), "getRedPackets", "获取我发的红包信息成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", jo).toJSONString();
		}else if(redPacketShowType==BusinessConstant.REDPACKETSHOWTYPE_3){//大家收到的红包排名
			ClubMember param = new ClubMember();
			param.setClubId(Integer.valueOf(clubId));
			param.setMemberStatus(1);
			param.setStart(start);
			param.setLimit(limit);
			//根据俱乐部id查询俱乐部下二级货币累计数量最多的人(二级货币数量倒序)
			List<ClubMember> list = clubMemberService.getClubMemberList(param);
			Integer count = clubMemberService.getClubMemberCount(param);
			List<ResRedPacket> resList = new ArrayList<ResRedPacket>();
			if(list!=null){
				for (ClubMember clubMember : list) {
					Integer clubMemberUserId = clubMember.getUserId();
					ResRedPacket res = new ResRedPacket();
					//人员信息
					String cacheUser = RedisComponent.findRedisObject(clubMemberUserId, CenterUser.class);
					CenterUser centeruser = null;
					if(!StringUtil.isEmpty(cacheUser)){
						centeruser = JSONObject.parseObject(cacheUser,CenterUser.class);
					}
					res.setRpReceiverName(centeruser.getRealName());//真实姓名
					res.setRpReceiverNickname(centeruser.getNickName());//昵称
					res.setRpReceiverId(String.valueOf(centeruser.getUserId()));//红包的接收者id
					//图片信息
					String img = Common.getImgUrl(centeruser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					res.setRpReceiverHeadLink(Common.checkPic(img) == true ? img+ActiveUrl.HEAD_MAP:img);//发送人头像
					res.setRpReceiverClubRole(clubMember.getLevel());//人在俱乐部的角色
					res.setRedPacketType(2);//红包类型
					res.setRedPacketCount(clubMember.getTotalReceiveRp2());//累计接收数量
					resList.add(res);
				}
			}
			JSONObject jo = PageObject.getPageObject(count, start, limit, resList, "redPackets");
			LogUtil.info(this.getClass(), "getRedPackets", "获取红包信息排名成功");
			return Common.getReturn(AppErrorCode.SUCCESS, "", jo).toJSONString();
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TYPE_CODE).toJSONString();
		}
	}
	
}


