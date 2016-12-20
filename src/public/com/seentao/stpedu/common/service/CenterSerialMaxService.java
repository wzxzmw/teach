package com.seentao.stpedu.common.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.CenterSerialMaxDao;
import com.seentao.stpedu.common.entity.CenterSerialMax;

@Service
public class CenterSerialMaxService{
	
	@Autowired
	private CenterSerialMaxDao centerSerialMaxDao;
	
	public String getCenterSerialMax(Integer id) {
		CenterSerialMax centerSerialMax = new CenterSerialMax();
		List<CenterSerialMax> centerSerialMaxList = centerSerialMaxDao .selectSingleCenterSerialMax(centerSerialMax);
		if(centerSerialMaxList == null || centerSerialMaxList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(centerSerialMaxList);
	}
	
	/**
	 * 获取当前日期的最大流水号
	 * @param type 交易类型
	 * @return str
	 * 交易流水生成规则 
	 * 类型1位 1：一级虚拟货币，2:二级虚拟货币
	 * 日期8位 4位年份 2位月份 2位日
	 * 时间6位 2位小时 2位分钟 2位秒
	 * 顺序号6位 每天从000001 开始
	 */
	public synchronized String getCenterSerialMaxByNowDate(Integer type) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat nowDate = new SimpleDateFormat("yyyyMMdd");
		String nowDateStr = nowDate.format(new Date());
		SimpleDateFormat nowDates = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowDateStrs = nowDates.format(new Date());
		//查询最大的num
		CenterSerialMax centerSerialMax = new CenterSerialMax();
		centerSerialMax.setSerialTime(Integer.parseInt(nowDateStr));
		CenterSerialMax resCenterSerialMax = centerSerialMaxDao.selectCenterSerialMax(centerSerialMax);
		if(resCenterSerialMax == null){//数据为空
			CenterSerialMax insertCenterSerialMax = new CenterSerialMax();
			insertCenterSerialMax.setSerialTime(Integer.parseInt(nowDateStr));
			insertCenterSerialMax.setSerialMax(1);
			centerSerialMaxDao.insertCenterSerialMax(insertCenterSerialMax);
			sb.append(type);
			sb.append(nowDateStrs);
			sb.append("000001");
		}else{
			String[] str = new String[]{"","00000","0000","000","00","0",""};
			int maxNum = resCenterSerialMax.getSerialMax()+1;
			CenterSerialMax updateCenterSerialMax = new CenterSerialMax();
			updateCenterSerialMax.setSerialMaxId(resCenterSerialMax.getSerialMaxId());
			updateCenterSerialMax.setSerialTime(Integer.parseInt(nowDateStr));
			updateCenterSerialMax.setSerialMax(resCenterSerialMax.getSerialMax());
			centerSerialMaxDao.updateCenterSerialMaxByKey(updateCenterSerialMax);
			String maxNumStr = String.valueOf(maxNum);
			String zero = str[maxNumStr.length()];
			sb.append(type);
			sb.append(nowDateStrs);
			sb.append(zero);
			sb.append(maxNumStr);
		}
		return sb.toString();
	}
	
}