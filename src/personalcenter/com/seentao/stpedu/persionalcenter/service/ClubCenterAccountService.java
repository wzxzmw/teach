package com.seentao.stpedu.persionalcenter.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.CenterCashOutDao;
import com.seentao.stpedu.common.dao.CenterSmsVerificationDao;
import com.seentao.stpedu.common.entity.CenterAccount;
import com.seentao.stpedu.common.entity.CenterCashOut;
import com.seentao.stpedu.common.entity.CenterConvert;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.common.entity.CenterRecharge;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.service.CenterAccountService;
import com.seentao.stpedu.common.service.CenterBankService;
import com.seentao.stpedu.common.service.CenterSerialMaxService;
import com.seentao.stpedu.common.service.CenterUserService;
import com.seentao.stpedu.common.service.ClubMemberService;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.persionalcenter.model.Cashing;
import com.seentao.stpedu.persionalcenter.model.IncomeAndExpenses;
import com.seentao.stpedu.utils.LogUtil;


@Service
public class ClubCenterAccountService {
	
	@Autowired
	private CenterBankService centerBankService;
	@Autowired
	private CenterAccountService centerAccountService;
	@Autowired
	private CenterSmsVerificationDao centerSmsVerificationDao;
	@Autowired
	private CenterUserService centerUserService;
	@Autowired
	private CenterCashOutDao centerCashOutDao;
	@Autowired
	private CenterSerialMaxService centerSerialMaxService;
	@Autowired
	private ClubMemberService clubMemberService;
	
	/**
	 * 获取俱乐部或个人的账务收支信息
	 * @return
	 * @author  lijin
	 * @throws ParseException 
	 * @date    2016年6月29日 上午9:54:25
	 */
	public String getIncomeAndExpenses(int userId,int userType ,int incomeAndExpenses,int accountType,String startDate,String endDate,int start,int limit,int inquireType,String clubId) throws ParseException{
		                                               
		
		LogUtil.info(this.getClass(), "getIncomeAndExpenses", " 获取俱乐部或个人的账务收支信息：【userId："+userId+"】，【userType："+userType+"】，【inquireType："+inquireType+"】，【start："+start+"】，【incomeAndExpenses："+incomeAndExpenses+"】，【startDate："+startDate+"】，【endDate："+endDate+"】，【clubId："+clubId+"】");
		JSONObject jsonObject=new JSONObject();
		if(GameConstants.INCOME_EXPENSE_ONE==inquireType){
			if(clubMemberService.validateUserAndClubAndLevel(userId,GameConstants.CLUB_PRESIDENT_COACH)){
			 //俱乐部管理模块，获取俱乐部的收支情况
				jsonObject=getInquireTypeOne(jsonObject,userId,userType,accountType,startDate,endDate,incomeAndExpenses,start,limit,clubId);
				LogUtil.info(this.getClass(), "getIncomeAndExpenses", " 获取俱乐部或个人的账务收支信息成功");
				//(yy修改校验用户是否存在账户)
				if(jsonObject!=null){
					return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_CLUB_ACCOUNT_RIGHTS_SUCCESS, jsonObject).toJSONString();
				}else{
					JSONObject json = new JSONObject();
					json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
					json.put(GameConstants.MSG, BusinessConstant.USER_NOT_ACCOUNT);
					return json.toJSONString();
				}
			}else{
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CLUB_ACCOUNT_NOT_MEMBER).toJSONString();
			}
		}else{
			switch(inquireType){
		        //获取个人的一级货币或者虚拟物品的收支情况，个人中心的账务管理模块调用
				case GameConstants.INCOME_EXPENSE_TWO:
					jsonObject=getInquireTypeTwo(jsonObject,userId,userType,accountType,startDate,endDate,incomeAndExpenses,start,limit,clubId);
		        break;
		        //获取个人的一级货币的充值记录，个人中心的账务管理模块调用
				case GameConstants.INCOME_EXPENSE_THREE:
					jsonObject=getInquireTypeThree(jsonObject,userId,userType,startDate,endDate,start,limit);
		        break;
		        //获取个人的虚拟物品的兑换记录，个人中心的账务管理模块调用
				case GameConstants.INCOME_EXPENSE_FOUR:
					jsonObject=getInquireTypeFour(jsonObject,userId,userType,startDate,endDate,accountType,start,limit);
		        break;
			}
			LogUtil.info(this.getClass(), "getIncomeAndExpenses", " 获取俱乐部或个人的账务收支信息成功");
			//(yy修改校验用户是否存在账户)
			if(jsonObject!=null){
				return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_CLUB_ACCOUNT_RIGHTS_SUCCESS, jsonObject).toJSONString();
			}else{
				JSONObject json = new JSONObject();
				json.put(GameConstants.CODE, AppErrorCode.ERROR_TYPE_TWO);
				json.put(GameConstants.MSG, BusinessConstant.USER_NOT_ACCOUNT);
				return json.toJSONString();
			}
		}
	}
	
	/**
	 *俱乐部管理模块，获取俱乐部的收支情况
	 * @return
	 * @author  lijin
	 * @throws ParseException 
	 * @date    2016年6月29日 上午9:54:25
	 */
	public JSONObject getInquireTypeOne(JSONObject jsonObject,int userId,int userType,int accountType,String startDate,String endDate,int incomeAndExpenses,int start,int limit,String clubId) throws ParseException{
		//根据账户类型，用户id，用户类型，获取账户id
		CenterAccount centerAccount=new CenterAccount();
		centerAccount.setAccountType(accountType);
		
		centerAccount.setUserId(Integer.parseInt(clubId));
		centerAccount.setUserType(2);
		
		centerAccount= centerAccountService.getCenterAccount(centerAccount);
		//用户不存在账户
		if(centerAccount==null){
			return null;
		}
		//根据账户表id，收支标识，查询货币变动表，判断创建时间在查询起始，结束时间的记录
		//分页查询
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("accountId", centerAccount.getAccountId());
		conditionMap.put("changeType", incomeAndExpenses);
		conditionMap.put("startTime", startDate);
		conditionMap.put("endTime", endDate);
		//调用分页组件
		QueryPage<CenterMoneyChange> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterMoneyChange.class);
		//返回数据对象封装
		List<CenterMoneyChange> centerMoneyChangeList= appQueryPage.getList();
		List<IncomeAndExpenses> retList=new ArrayList<IncomeAndExpenses>();
		//总交易值
		double allIaeFLevelAccount=0;
		for(CenterMoneyChange centerMoneyChange:centerMoneyChangeList){
			IncomeAndExpenses incomeAndExpensesModel=new IncomeAndExpenses();
			//交易时间的时间戳
			incomeAndExpensesModel.setIaeCreateDate(String.valueOf(centerMoneyChange.getChangeTime()));
			//收支对象类型:1:个人2:俱乐部
			incomeAndExpensesModel.setIaeObjectType(centerAccount.getUserType());
			//交易状态标识
			incomeAndExpensesModel.setIaeStatus(centerMoneyChange.getStatus());
			//交易类型标识
			incomeAndExpensesModel.setIaeType(centerMoneyChange.getRelObjetType());
			//交易类型名称
			incomeAndExpensesModel.setIaeName(getChangeTypeCN(centerMoneyChange.getRelObjetType()));
			//交易值(一级货币或虚拟物品的数量)
			incomeAndExpensesModel.setIaeValue(centerMoneyChange.getAmount().intValue());
			//收支id
			incomeAndExpensesModel.setIncomeAndExpensesId(centerMoneyChange.getChangeId());
			//交易流水编号
			incomeAndExpensesModel.setIncomeAndExpensesNo(centerMoneyChange.getSerialNumber());
			retList.add(incomeAndExpensesModel);
			allIaeFLevelAccount=getAllIaeFLevelAccount(centerMoneyChange.getChangeType(),allIaeFLevelAccount,centerMoneyChange.getAmount());
		}
		
		jsonObject.put("incomeAndExpenses", retList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		jsonObject.put("allIaeFLevelAccount", Math.floor(allIaeFLevelAccount));
		return jsonObject;
	}
	
	/**
	 *俱乐部管理模块，获取俱乐部的收支情况
	 * @return
	 * @author  lijin
	 * @throws ParseException 
	 * @date    2016年6月29日 上午9:54:25
	 */
	public JSONObject getInquireTypeTwo(JSONObject jsonObject,int userId,int userType,int accountType,String startDate,String endDate,int incomeAndExpenses,int start,int limit,String clubId) throws ParseException{
		                                                            
		
		//根据账户类型，用户id，用户类型，获取账户id
		CenterAccount centerAccount=new CenterAccount();
		centerAccount.setAccountType(accountType);
		centerAccount.setUserId(userId);
		centerAccount.setUserType(1);
	
		centerAccount= centerAccountService.getCenterAccount(centerAccount);
		//用户不存在账户
		if(centerAccount==null){
			return null;
		}
		//根据账户表id，收支标识，查询货币变动表，判断创建时间在查询起始，结束时间的记录
		//分页查询
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("accountId", centerAccount.getAccountId());
		conditionMap.put("changeType", incomeAndExpenses);
		conditionMap.put("startTime", startDate);
		conditionMap.put("endTime", endDate);
		//调用分页组件
		QueryPage<CenterMoneyChange> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterMoneyChange.class);
		//返回数据对象封装
		List<CenterMoneyChange> centerMoneyChangeList= appQueryPage.getList();
		List<IncomeAndExpenses> retList=new ArrayList<IncomeAndExpenses>();
		//总交易值
		double allIaeFLevelAccount=0;
		for(CenterMoneyChange centerMoneyChange:centerMoneyChangeList){
			IncomeAndExpenses incomeAndExpensesModel=new IncomeAndExpenses();
			//交易时间的时间戳
			incomeAndExpensesModel.setIaeCreateDate(String.valueOf(centerMoneyChange.getChangeTime()));
			//收支对象类型:1:个人2:俱乐部
			incomeAndExpensesModel.setIaeObjectType(centerAccount.getUserType());
			//交易状态标识
			incomeAndExpensesModel.setIaeStatus(centerMoneyChange.getStatus());
			//交易类型标识
			incomeAndExpensesModel.setIaeType(centerMoneyChange.getRelObjetType());
			//交易类型名称
			incomeAndExpensesModel.setIaeName(getChangeTypeCN(centerMoneyChange.getRelObjetType()));
			//交易值(一级货币或虚拟物品的数量)
			incomeAndExpensesModel.setIaeValue(centerMoneyChange.getAmount().intValue());
			//收支id
			incomeAndExpensesModel.setIncomeAndExpensesId(centerMoneyChange.getChangeId());
			//交易流水编号
			incomeAndExpensesModel.setIncomeAndExpensesNo(centerMoneyChange.getSerialNumber());
			retList.add(incomeAndExpensesModel);
			allIaeFLevelAccount=getAllIaeFLevelAccount(centerMoneyChange.getChangeType(),allIaeFLevelAccount,centerMoneyChange.getAmount());
		}
		
		jsonObject.put("incomeAndExpenses", retList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		jsonObject.put("allIaeFLevelAccount", allIaeFLevelAccount);
		return jsonObject;
	}
	
	
	public String getChangeTypeCN(int changeType){
		//1.竞猜，2:红包，3:兑换，4:提现，5:充值，6:购买，7:加入俱乐部，8:加入培训班。
		String ret="";
		switch(changeType){
			case 1:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_GUESS_CN;
				break;
			case 2:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_RED_CN;
				break;
			case 3:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_EXC_CN;
				break;
			case 4:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_CASH_CN;
				break;
			case 5:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_RECH_CN;
				break;
			case 6:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_BUY_CN;
				break;
			case 7:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_CLUB_CN;
				break;
			case 8:
				ret=GameConstants.MONEY_CHANGE_LINK_TYPE_TRAINING_CN;
				break;
		}
		return ret;
	}
	
	public String getRechargeTypeCN(int changeType){
		if(1==changeType){
			return GameConstants.BUY_TYPE_ALIBA;
		}else{
			return GameConstants.BUY_TYPE_TENCENT;
		}
	}
	
	public Double getAllIaeFLevelAccount(int changeType,double allIaeFLevelAccount,double iaeValue){
		if(1==changeType){
			return allIaeFLevelAccount+iaeValue;
		}else{
			return iaeValue-allIaeFLevelAccount;
		}
	}
	

	/**
	 *获取个人的一级货币的充值记录，个人中心的账务管理模块调用
	 * @return
	 * @author  lijin
	 * @throws ParseException 
	 * @date    2016年6月29日 上午9:54:25
	 */
	public JSONObject getInquireTypeThree(JSONObject jsonObject,int userId,int userType,String startDate,String endDate,int start,int limit) throws ParseException{
		
		//获取充值账户
		CenterAccount centerAccount=new CenterAccount();
		centerAccount.setAccountType(1);
		centerAccount.setUserId(userId);
		//个人账户
		centerAccount.setUserType(1);
		centerAccount=centerAccountService.getCenterAccount(centerAccount);
		//用户不存在账户
		if(centerAccount==null){
			return null;
		}
		//根据用户id，查询充值表，判断创建时间在查询起始，结束时间的记录。
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("accountId", centerAccount.getAccountId());
		conditionMap.put("startTime", startDate);
		conditionMap.put("endTime", endDate);
		
		
		
		//调用分页组件
		QueryPage<CenterRecharge> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterRecharge.class);
		List<CenterRecharge> centerRechargeList= appQueryPage.getList();
		List<IncomeAndExpenses> retList=new ArrayList<IncomeAndExpenses>();
		//总交易值
		double allIaeFLevelAccount=0;
		for(CenterRecharge centerRecharge:centerRechargeList){
			IncomeAndExpenses incomeAndExpensesModel=new IncomeAndExpenses();
			//交易时间的时间戳
			incomeAndExpensesModel.setIaeCreateDate(String.valueOf(centerRecharge.getCreateTime()));
			//收支对象类型:1:个人2:俱乐部
			incomeAndExpensesModel.setIaeObjectType(userType);
			//交易状态标识
			incomeAndExpensesModel.setIaeStatus(centerRecharge.getStatus());
			//交易类型标识
			incomeAndExpensesModel.setIaeType(centerRecharge.getRechargeType());
			//交易类型名称
			incomeAndExpensesModel.setIaeName(centerRecharge.getRemarks());
			//交易值(一级货币或虚拟物品的数量)
			incomeAndExpensesModel.setIaeValue(centerRecharge.getAmount().intValue());
			//收支id
			incomeAndExpensesModel.setIncomeAndExpensesId(centerRecharge.getRechargeId());
			//交易流水编号
			incomeAndExpensesModel.setIncomeAndExpensesNo(centerRecharge.getSerialNumber());
			retList.add(incomeAndExpensesModel);
			allIaeFLevelAccount+=centerRecharge.getAmount();
		}
		
		jsonObject.put("incomeAndExpenses", retList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		jsonObject.put("allIaeFLevelAccount", allIaeFLevelAccount);
		
		
		return jsonObject;
	}
	
	/**
	 *获取个人的虚拟物品的兑换记录，个人中心的账务管理模块调用
	 * @return
	 * @author  lijin
	 * @date    2016年6月29日 上午9:54:25
	 */
	public JSONObject getInquireTypeFour(JSONObject jsonObject,int userId,int userType,String startDate,String endDate,int accountType,int start,int limit){
		//根据用户id，查询兑换表，判断创建时间在查询起始，结束时间的记录。
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("createUserId", userId);
		conditionMap.put("startTime", startDate);
		conditionMap.put("endTime", endDate);
		
		//调用分页组件
		QueryPage<CenterConvert> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterConvert.class);
		List<CenterConvert> centerConvertList= appQueryPage.getList();
		List<IncomeAndExpenses> retList=new ArrayList<IncomeAndExpenses>();
		//总交易值
		double allIaeFLevelAccount=0;
		for(CenterConvert centerConvert:centerConvertList){
			IncomeAndExpenses incomeAndExpensesModel=new IncomeAndExpenses();
			//交易时间的时间戳
			incomeAndExpensesModel.setIaeCreateDate(String.valueOf(centerConvert.getCreateTime()));
			//收支对象类型:1:个人2:俱乐部
			incomeAndExpensesModel.setIaeObjectType(userType);
			//交易状态标识
			incomeAndExpensesModel.setIaeStatus(1);
			//交易类型标识
			incomeAndExpensesModel.setIaeType(2);
			//交易类型名称
			incomeAndExpensesModel.setIaeName(GameConstants.ACCOUNT_TYPE_TWO);
			//交易值(一级货币或虚拟物品的数量)
			incomeAndExpensesModel.setIaeValue(centerConvert.getVirtualAmount().intValue());
			//收支id
			incomeAndExpensesModel.setIncomeAndExpensesId(Common.null2Int(centerConvert.getConvertId()));
			//交易流水编号
			incomeAndExpensesModel.setIncomeAndExpensesNo(Common.null2Str(centerConvert.getSerialNumber()));
			retList.add(incomeAndExpensesModel);
			allIaeFLevelAccount+=centerConvert.getVirtualAmount();
		}
				
		jsonObject.put("incomeAndExpenses", retList);
		jsonObject.put("returnCount", appQueryPage.getCount());
		jsonObject.put("allPage", appQueryPage.getAllPage());
		jsonObject.put("currentPage", appQueryPage.getCurrentPage());
		jsonObject.put("allIaeFLevelAccount", allIaeFLevelAccount);
		
		return jsonObject;
	}
	
	/**
	 *获取提现信息
	 * @return
	 * @author  lijin
	 * @date    2016年6月29日 上午9:54:25
	 */
	public String getCashing(int userId,String userType ,String clubId,String startDate,String endDate,int limit,int start){
		if(clubMemberService.validateUserAndClubAndLevel(userId, GameConstants.CLUB_PRESIDENT_COACH)){
			LogUtil.info(this.getClass(), "getCashing", "获取提现信息参数：【userId："+userId+"】，【userType："+userType+"】，【startDate："+startDate+"】，【endDate："+endDate+"】，【start："+start+"】");
			//根据 用户名id和查询时间，查询提现表，获取总数量，总页数，当前页
			JSONObject jsonObject=new JSONObject();
			
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			conditionMap.put("createUserId", userId);
			conditionMap.put("startTime", startDate);
			conditionMap.put("endTime",endDate);
			//调用分页组件
			QueryPage<CenterCashOut> appQueryPage = QueryPageComponent.queryPage(limit, start, conditionMap, CenterCashOut.class);
			List<CenterCashOut> centerCashOutList= appQueryPage.getList();
			List<Cashing> retList=new ArrayList<Cashing>();
			//数据封装
			for(CenterCashOut centerCashOut:centerCashOutList){
				Cashing cashing=new Cashing();
				cashing.setCashingFLevelAccount(Common.null2Double(centerCashOut.getAmount()));
				cashing.setCashingId(centerCashOut.getCashOutId());
				cashing.setCashingStatus(centerCashOut.getStatus());
				cashing.setcCreateDate(String.valueOf(centerCashOut.getCreateTime()));
				retList.add(cashing);
			}
			jsonObject.put("returnCount", appQueryPage.getCount());
			jsonObject.put("allPage", appQueryPage.getAllPage());
			jsonObject.put("currentPage", appQueryPage.getCurrentPage());
			jsonObject.put("cashing", retList);
			LogUtil.info(this.getClass(), "getCashing", "获取提现信息参数");
			return  Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_CASH_RIGHTS_SUCCESS, jsonObject).toJSONString();
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_CASH_NOT_MEMBER).toJSONString();
		}
	} 
	
	/**
	 * 提交提现申请
	 * @author  lijin
	 * @date    2016年6月29日 下午17:54:25
	 */
	public String applyCashing(int userId,int userType ,double cash,int accountType,String accountNum,String applyContent,String iCode){
		LogUtil.info(this.getClass(), "applyCashing", "提交提现申请信息参数：【userId："+userId+"】，【userType："+userType+"】，【accountType："+accountType+"】，【accountNum："+accountNum+"】，【applyContent："+applyContent+"】，【iCode："+iCode+"】");
		if(clubMemberService.validateUserAndClubAndLevel(userId, GameConstants.CLUB_PRESIDENT_COACH)){
		    //根据用户id，获取电话号码
			CenterUser centerUser= centerUserService.getCenterUserById(userId);
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			//获取时间在当前时间和10分钟后的结束时间
			Calendar cal = Calendar.getInstance(); 
			conditionMap.put("endTime", (int)(cal.getTime().getTime()/1000));
			cal.add(Calendar.MINUTE, -10);
			conditionMap.put("startTime", (int)(cal.getTime().getTime()/1000));
			conditionMap.put("type", 4);
			conditionMap.put("iCode", iCode);
			conditionMap.put("phone", centerUser.getPhone());
			//校验【根据 手机号，验证码类型，验证码查询短信验证码表，并且验证码有效期为10分钟】
			boolean checkFlag= centerSmsVerificationDao.selectCenterSmsVerificationByConditon(conditionMap);
			if(checkFlag){
				//满足校验条件：新增提现表一条记录：提现状态设置为1，其余插入记录中
				CenterCashOut centerCashOut=new CenterCashOut();
				centerCashOut.setAccount(accountNum);
				centerCashOut.setCashOutType(accountType);
				centerCashOut.setAmount(cash);
				centerCashOut.setCreateTime((int)(new Date().getTime()/1000));
				centerCashOut.setCreateUserId(userId);
				centerCashOut.setReason(applyContent);
				centerCashOut.setSerialNumber(centerSerialMaxService.getCenterSerialMaxByNowDate(accountType));
				centerCashOut.setStatus(1);
				centerCashOutDao.insertCenterCashOut(centerCashOut);
				LogUtil.info(this.getClass(), "applyCashing", "提交提现申请成功");
				return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.APPLY_CASH_RIGHTS_SUCCESS).toJSONString();
			}else{
				LogUtil.info(this.getClass(), "applyCashing", "验证码失效");
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.APPLY_CASH_VALIDATE_ERROR).toJSONString();
			}
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.APPLY_CASH_NOT_MEMBER).toJSONString();
		}
	} 
	
	/**
	 * 获取账户类型标识信息
	 * @author  lijin
	 * @date    2016年6月28日 下午11:54:25
	 */
	public String getAccountTypes(int userId){
		LogUtil.info(this.getClass(), "getAccountTypes", "获取账户类型标识信息参数【userId："+userId+"】");
		if(clubMemberService.validateUserAndClubAndLevel(userId, GameConstants.CLUB_PRESIDENT)){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("accountTypes", centerBankService.getAllAccountTypes());
			LogUtil.info(this.getClass(), "getAccountTypes", "获取账户类型标识信息");
			return Common.getReturn(AppErrorCode.SUCCESS, AppErrorCode.GET_ACCOUNT_TYPE_SUCCESS, jsonObject).toJSONString();
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.GET_ACCOUNT_TYPE_NOT_MEMBER).toJSONString();
		}
	} 

}
