package com.seentao.stpedu.club.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.CustomizeException;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.CenterAccountDao;
import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.dao.ClubMemberDao;
import com.seentao.stpedu.common.dao.ClubMemberMoodDao;
import com.seentao.stpedu.common.dao.ClubTopicCommentDao;
import com.seentao.stpedu.common.dao.ClubTopicDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterLiveService;
import com.seentao.stpedu.common.service.CenterMoneyChangeService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMainService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Service
public class SubmitClubOperationService {

	@Autowired
	private ClubMemberDao clubMemberDao;

	@Autowired
	private ClubMainDao clubMainDao;
	/**
	 * 用户账户
	 */
	@Autowired
	private CenterAccountDao accountDao;
	/**
	 * 货币变动
	 */
	@Autowired
	private CenterMoneyChangeService centerMoneyChangeService;
	/**
	 * 俱乐部话题
	 */
	@Autowired
	private ClubTopicDao clubTopicDao;
	/**
	 * 俱乐部话题评论
	 */
	@Autowired
	private ClubTopicCommentDao clubTopicCommentDao;
	/**
	 * 俱乐部会员心情
	 */
	@Autowired
	private ClubMemberMoodDao clubMemberMoodDao;

	@Autowired
	private CenterLiveService centerLiveService;

	@Autowired
	private ClubMemberService clubMemberService;

	@Autowired
	private ClubMainService clubMainService;

	@Autowired
	private CenterUserService centerUserService;

	@Autowired
	private CenterAccountService centerAccountService;
	/***
	 * 加入或退出俱乐部
	 * @param userId当前登录用户的id 
	 * @param clubId 俱乐部id
	 * @param actionType 操作 1:加入俱乐部；2:退出俱乐部；
	 * @param applicationContent 加入申请
	 * @param clubMemberId 会员id
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public String operateclub(String userId, String clubId, int actionType, String applicationContent,
			String clubMemberId) throws ParseException {
		//判读用户是加入俱乐部还是退出俱乐部,当SUBMIT_CLUB_JOIN 为1表示加入俱乐部
		if(actionType == GameConstants.SUBMIT_CLUB_JOIN){
			//判断用户是否已经加入俱乐部
			/**
			 * 加入俱乐部条件
			 * 	1、已经加入俱乐部不能在加入
			 * 	2、自己已经创建俱乐部后已经有俱乐部，则不能在加入俱乐部
			 */
			//1.判断用户是否已经存在俱乐部，或者已经加入俱乐部,根据用户userId和俱乐部clubId	
			ClubMember _clubMember = clubMemberDao.selectOnlyClubMember(Integer.valueOf(userId));
			
			if(_clubMember !=null && _clubMember.getMemberStatus() == 1)
				return Common.getReturn(AppErrorCode.ERROR_TYPE_FOUR, AppErrorCode.JOIN_THE_CLUB_SUCCESS).toJSONString();
			//1.获取加入俱乐部的方式
			ClubMain _clubMain = new ClubMain();
			_clubMain.setClubId(Integer.valueOf(clubId));

			/**
			 * 1.如果是新用户加入俱乐部，memberStatus为null
			 * 2. 当曾经加入该俱乐部，但是已经退出，
			 */
			// 判断会员的状态为2的时候，则加入俱乐部为审核中
			if(_clubMember != null && _clubMember.getMemberStatus() == GameConstants.CLUB_MEMBER_STATE_SH){
				LogUtil.error(this.getClass(), "operateclub", "正在审核中");
				return  Common.getReturn(AppErrorCode.ERROR_TYPE_FOUR, AppErrorCode.QUIT_THE_CLUB_SUCCESSFULLY).toJSONString();
			}
				
			//查询俱乐部为null,会员状态为3或者4的时候申请加入
			if(_clubMember == null || GameConstants.CLUB_MEMBER_STATE_FIAL== _clubMember.getMemberStatus() || GameConstants.CLUB_MEMBER_STATE_NOJOIN == _clubMember.getMemberStatus()){
				//根据俱乐部clubId查询俱乐部信息
				_clubMain = clubMainDao.selectClubMainEntity(_clubMain);
				//获取加入该俱乐部的加入方式 1.为申请加入 2、公开,3、付费
				if(_clubMain.getAddMemberType() == GameConstants.CLUB_JOIN_ONE ){
					//加入俱乐部产生动态表记录
					centerLiveService.addCenterLive(clubId, userId);
					//把该用户加入到俱乐部会员表中
					clubMemberService.addClubMember(userId, clubId, applicationContent);
					//把俱乐部表的'是否存在未审核会员'改为1
					clubMainService.updateClubStatus(clubId);
				}
				//当加入俱乐部 的加入方式为2，则为公开加入
				else if(_clubMain.getAddMemberType() == GameConstants.CLUB_JOIN_TWO){
					if(_clubMember == null){
						//当俱乐部会员表为Null,则添加一条数据
						clubMemberService.addClubMemberType(userId, clubId);
						//俱乐部表会员人数量+1
						clubMainService.addClubMemberNum(clubId);
						//加入俱乐部产生动态表记录
						centerLiveService.addCenterLive(clubId, userId);
					}else{
						clubMemberService.updateClubMemberInfo(userId, clubId, _clubMember.getMemberId());
						//俱乐部表会员人数量+1
						clubMainService.addClubMemberNum(clubId);
						//加入俱乐部产生动态表记录
						centerLiveService.addCenterLive(clubId, userId);
					}
				
				}
				//当加入俱乐部的加入方式为3,则为付费加入
				else if(_clubMain.getAddMemberType() == GameConstants.CLUB_JOIN_THREE){
					try {
						//判断用户一级货币是否满足进入俱乐部费用
						//获取进入俱乐部费用
						Double addAmount = _clubMain.getAddAmount();
						//获取用户的一级货币 
						CenterAccount account =new CenterAccount();
						account.setUserId(Integer.valueOf(userId));
						account.setUserType(GameConstants.INDIVIDUAL_USER);
						account.setAccountType(GameConstants.STAIR_ONE);
						
						account = accountDao.selectCenterOneMoney(account);
						
						//获取俱乐部账户id
						CenterAccount _account = new CenterAccount();
						_account.setUserId(Integer.valueOf(clubId));
						_account = accountDao.selectCenterAccount(_account);
						//现金额 - 锁定金额
						Double money = account.getBalance() - account.getLockAmount();
						if(money.doubleValue()>=addAmount.doubleValue()){
							//用户账户表减入会金额
							Double Money = account.getBalance()-addAmount;
							account.setBalance(Money);
							account.setUserType(GameConstants.INDIVIDUAL_USER);
							account.setAccountType(GameConstants.STAIR_ONE);
							account.setAccountId(account.getAccountId());
							//更新用户一级货币
							accountDao.updateCenterAccountByKey(account);

							//更新俱乐部账户余额
							centerAccountService.updateClubCenterAccount(addAmount, clubId);
							//增加货币变动表记录(俱乐部)
							centerMoneyChangeService.addCenterMoneyChange(_account.getAccountId(), addAmount, clubId,GameConstants.MONEY_CHANGE_LINK_TYPE_CLUB,GameConstants.INCOME);
							
							//增加货币变动表记录(个人)
							centerMoneyChangeService.addCenterMoneyOne(account.getAccountId(),addAmount,clubMemberId,GameConstants.MONEY_CHANGE_LINK_TYPE_CLUB);
							//加入俱乐部
							if(_clubMember == null){
								clubMemberService.addClubMemberType(userId, clubId);
								//俱乐部表会员人数量+1
								clubMainService.addClubMemberNum(clubId);
								//加入俱乐部产生动态表记录
								centerLiveService.addCenterLive(clubId, userId);
							}else{
								clubMemberService.updateClubMemberInfo(userId, clubId, _clubMember.getMemberId());
								//俱乐部表会员人数量+1
								clubMainService.addClubMemberNum(clubId);
								//加入俱乐部产生动态表记录
								centerLiveService.addCenterLive(clubId, userId);
							}
							//return Common.getReturn(AppErrorCode.SUCCESS, "").toJSONString();
						}else{
							LogUtil.error(this.getClass(), "operateclub","钱包余额不足");
							return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.SHORTAGE_FIRST_CLASS_CURRENCY).toString();
						}
					} catch (Exception e) {
						LogUtil.error(this.getClass(), "operateclub", "加入俱乐部失败");
						return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.JOIN_THE_CLUB_FAILED).toJSONString();
					}
				
				}
				try {
					centerUserService.updateCenterUser(userId, clubId);
					JedisCache.delRedisVal(CenterUser.class.getSimpleName(), userId);
					JedisCache.delRedisVal(ClubMain.class.getSimpleName(), clubId);
				} catch (Exception e) {
					LogUtil.error(this.getClass(), "-----updateCenterUser  updateClubmemerIsremoind------", "");
					return Common.getReturn(AppErrorCode.ERROR_COUNT_NOT, AppErrorCode.DATABASE_UPDATE_EXCEPTION, "").toString();
				}
				//是否有新的消息提醒
				if(_clubMain.getAddMemberType() == GameConstants.CLUB_JOIN_THREE || (_clubMember == null || GameConstants.CLUB_MEMBER_STATE_FIAL== _clubMember.getMemberStatus() || GameConstants.CLUB_MEMBER_STATE_NOJOIN == _clubMember.getMemberStatus())
						){
					return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.JOIN_CLUB_SUCCESS_MESSAGE).toJSONString();
				}else if(_clubMain.getAddMemberType() == GameConstants.CLUB_JOIN_ONE){
					return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.QUIT_THE_CLUB_SUCCESSFULLY_MESSAGE).toJSONString();
				}
			}
		}else{
			ClubMember	member = new ClubMember();
				//表示被会长踢出
				//member.setUserId(Integer.valueOf(clubMemberId));
				//member.setClubId(Integer.valueOf(clubId));
				//member.setMemberStatus(GameConstants.CLUB_MEMBER_STATE_JOIN);
				//member = clubMemberDao.selectClubMember(member);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("userId", Integer.valueOf(clubMemberId));
				map.put("clubId", Integer.valueOf(clubId));
				member = clubMemberDao.queryClubMemberSome(map);
				// 判断会长不能退出，俱乐部的用户ID和当前俱乐部的会员ID比较
				if(member.getLevel() == 1){
					LogUtil.error(this.getClass(), "operateclub","会长不能退出");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.THE_PRESIDENT_CANT_QUIT).toJSONString();
				}else if(member.getLevel() == 2){
					LogUtil.error(this.getClass(), "operateclub","教练不能退出");
					return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.THE_COACH_CANT_QUIT).toJSONString();
				}else{
					//调用比赛获取会员比赛状态
					try {
						IGameInterfaceService platGameService = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);
						Integer cardIdsStr = null;
						String cardsResult = platGameService.getMatchStatusByClubId(Integer.valueOf(clubId),Integer.valueOf(clubMemberId));
							JSONObject cardsObj = JSONObject.parseObject(cardsResult);
							// matchStatus -1：没有返回值；1:比赛未开始；4:比赛进行中；5:比赛结束
							if("0".equals(cardsObj.get("code").toString())){
								cardIdsStr = cardsObj.getJSONObject("result").getInteger("matchStatus");
							}else{
								return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, BusinessConstant.ERROR_HPROSE).toJSONString();
							}
						//   当返回值为5的时候，表示比赛结束可以退出，当-1的时候表示没有参加
						if(cardIdsStr == 5 || cardIdsStr == -1){
							clubMemberService.updateClubMember(member.getMemberId());
							// 修改会员状态为退出
							this.updateStatus(member);
							//退出俱乐部人员-1
							if(member.getMemberStatus() != 2){
								clubMainService.decrCurrencyClubMemberNum(clubId);
							}
							centerUserService.quitCenterUser(Integer.valueOf(clubMemberId));
							JedisCache.delRedisVal(ClubMain.class.getSimpleName(),clubId);
							JedisCache.delRedisVal(CenterUser.class.getSimpleName(), clubMemberId);
							JedisCache.delRedisVal(ClubMember.class.getSimpleName(),String.valueOf(member.getMemberId()));
							LogUtil.info(this.getClass(), "operateclub", "退出俱乐部成功");
							return Common.getReturn(AppErrorCode.SUCCESS, "退出俱乐部成功").toJSONString();
						}else if(cardIdsStr == 4 || cardIdsStr == 1){
							LogUtil.info(this.getClass(), "operateclub", "比赛未结束退出俱乐部失败");
							return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.THE_GAME_HAS_NOT_ENDED).toJSONString();
						}
					} catch (Exception e) {
						LogUtil.info(this.getClass(), "operateclub", "退出俱乐部失败");
						return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.THE_GAME_HAS_NOT_ENDED).toJSONString();
					}
				}
		}
		return null;
	}

	public void updateStatus(ClubMember clubMember){
		//发布的俱乐部话题评论，冻结发布的俱乐部话题，发布的俱乐部心情 不能回复，不能赞，不能踩。
		try {
				// 批量更新冻结评论
				clubTopicCommentDao.batchUpadteClubTopicComment(clubMember.getUserId());
			//冻结发布的俱乐部话题
			   clubTopicDao.batchUpdateClubTopic(clubMember.getClubId(), clubMember.getUserId());
			   //发布的俱乐部心情 冻结
			   clubMemberMoodDao.batchUpdateClubMemberMood(clubMember.getClubId(), clubMember.getUserId());
		} catch (RuntimeException e) {
			LogUtil.error(this.getClass(), "---updateStatus--", "冻结评论失败",e);
		}catch (CustomizeException e) {
			LogUtil.error(this.getClass(), "---updateStatus--", "批量评论话题失败",e);
		}catch (Exception e) {
			LogUtil.error(this.getClass(), "---updateStatus--", "发布的俱乐部心情 冻结",e);
		}
	}
}
