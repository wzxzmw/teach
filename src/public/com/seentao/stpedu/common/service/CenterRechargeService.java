package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.CenterRechargeDao;
import com.seentao.stpedu.common.entity.CenterRecharge;

@Service
public class CenterRechargeService{
	
	@Autowired
	private CenterRechargeDao centerRechargeDao;
	
	public CenterRecharge getCenterRecharge(CenterRecharge centerRecharge) {
		List<CenterRecharge> centerRechargeList = centerRechargeDao .selectSingleCenterRecharge(centerRecharge);
		if(centerRechargeList == null || centerRechargeList .size() <= 0){
			return null;
		}
		return centerRechargeList.get(0);
	}

	public void insertCenterRecharge(CenterRecharge centerRecharge) {
		centerRechargeDao.insertCenterRecharge(centerRecharge);
	}

	/** 
	* 根据充值表的流水号，获取该充值信息 
	* @author W.jx
	* @date 2016年6月29日 上午11:25:14 
	* @param serialNumber 流水号
	* @return
	*/
	public CenterRecharge getCenterRechargeBySerialNumber(String serialNumber) {
		CenterRecharge centerRecharge = new CenterRecharge();
		centerRecharge.setSerialNumber(serialNumber);
		centerRecharge = centerRechargeDao.selectCenterRecharge(centerRecharge );
		return centerRecharge;
	}

	/** 
	* 修改充值记录
	* @author W.jx
	* @date 2016年6月29日 下午2:21:31 
	* @param centerRecharge
	*/
	public void updateCenterRecharge(CenterRecharge centerRecharge) {
		 centerRechargeDao.updateCenterRechargeByKey(centerRecharge);
	}
}