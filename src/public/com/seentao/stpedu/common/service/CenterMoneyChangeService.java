package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.CenterMoneyChangeDao;
import com.seentao.stpedu.common.entity.CenterMoneyChange;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class CenterMoneyChangeService{
	
	@Autowired
	private CenterMoneyChangeDao centerMoneyChangeDao;
	
	@Autowired
	private CenterSerialMaxService centerSerialMaxService;
	public CenterMoneyChange getCenterMoneyChange(CenterMoneyChange centerMoneyChange) {
		List<CenterMoneyChange> centerMoneyChangeList = centerMoneyChangeDao .selectSingleCenterMoneyChange(centerMoneyChange);
		if(centerMoneyChangeList == null || centerMoneyChangeList .size() <= 0){
			return null;
		}
		
		return centerMoneyChangeList.get(0);
	}
	

	
	
	
	public void insertCenterMoneyChange(CenterMoneyChange centerMoneyChange){
		centerMoneyChangeDao.insertCenterMoneyChange(centerMoneyChange);
	}
	
	public void insertCenterMoneyChange(List<CenterMoneyChange> centerMoneyChange){
		centerMoneyChangeDao.insertCenterMoneyChange(centerMoneyChange);
	}
	
	/***
	 * 加入付费俱乐部时增加货币变动表记录
	 * @param accountId
	 * @param addAmount
	 * @param clubId
	 * @author cxw
	 */
	public void addCenterMoneyChange(Integer accountId,Double addAmount,String clubId,Integer relObjectType,int type){
		CenterMoneyChange centerMoneyChange = new CenterMoneyChange();
		centerMoneyChange.setAccountId(accountId);
		//调用yy的方法生成流水号
		String maxByNowDate = centerSerialMaxService.getCenterSerialMaxByNowDate(1);
		centerMoneyChange.setSerialNumber(maxByNowDate);
		centerMoneyChange.setChangeTime(TimeUtil.getCurrentTimestamp());
		centerMoneyChange.setAmount(addAmount);
		//
		centerMoneyChange.setChangeType(type);
		//centerMoneyChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_CLUB);
		centerMoneyChange.setRelObjetType(relObjectType);
		centerMoneyChange.setRelObjetId(Integer.valueOf(clubId));
		centerMoneyChange.setStatus(GameConstants.MONEY_CHANGE_STATE_SUCCESS);
		//增加货币变动表记录
		centerMoneyChangeDao.insertCenterMoneyChange(centerMoneyChange);
	}
	
	/***
	 * 加入付费俱乐部时用户货币变动表记录
	 * @param accountId
	 * @param addAmount
	 * @param clubId
	 * @author cxw
	 */
	public void addCenterMoneyOne(Integer accountId,Double addAmount,String clubMemberId,Integer relObjectType){
		CenterMoneyChange centerMoneyChange = new CenterMoneyChange();
		centerMoneyChange.setAccountId(accountId);
		//调用yy的方法生成流水号
		String maxByNowDate = centerSerialMaxService.getCenterSerialMaxByNowDate(1);
		centerMoneyChange.setSerialNumber(maxByNowDate);
		centerMoneyChange.setChangeTime(TimeUtil.getCurrentTimestamp());
		centerMoneyChange.setAmount(addAmount);
		centerMoneyChange.setChangeType(GameConstants.SPENDING);
		//centerMoneyChange.setRelObjetType(GameConstants.MONEY_CHANGE_LINK_TYPE_CLUB);
		centerMoneyChange.setRelObjetType(relObjectType);
		centerMoneyChange.setRelObjetId(Integer.valueOf(clubMemberId));
		centerMoneyChange.setStatus(GameConstants.MONEY_CHANGE_STATE_SUCCESS);
		//增加货币变动表记录
		centerMoneyChangeDao.insertCenterMoneyChange(centerMoneyChange);
	}
	
	
}