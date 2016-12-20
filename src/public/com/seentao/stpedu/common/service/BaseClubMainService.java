package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.dao.ClubMainDao;
import com.seentao.stpedu.common.entity.ClubMain;

/**
 * 查询俱乐部表
 * @author ligs
 */
@Service
public class BaseClubMainService{
	
	@Autowired
	private ClubMainDao clubMainDao;
	
	
	/**
	 * 单个对象查询
	 * @param clubMain	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  下午2:02:05
	 */
	public ClubMain selectClubMainEntity(ClubMain clubMain){
		return clubMainDao.selectClubMainEntity(clubMain);
	}
	
	
	/**
	 * 
	 * getClubMain(查询俱乐部表)   
	 * @author ligs
	 * @date 2016年6月18日 下午2:05:39
	 * @return
	 */
	public List<ClubMain> getClubMain(ClubMain clubMain) {
		List<ClubMain> clubMainList = clubMainDao .selectSingleClubMain( clubMain);
		if(clubMainList == null || clubMainList .size() <= 0){
			return null;
		}
		return clubMainList;
		
	}
	
	/**
	 * 
	 * getClubMain(查询俱乐部表)   
	 * @author ligs
	 * @param clubMain 
	 * @date 2016年6月18日 下午2:05:39
	 * @return
	 */
	public List<ClubMain> selectClubMain(ClubMain clubMain) {
		List<ClubMain> clubMainList = clubMainDao .selectClubMain(clubMain);
		if(clubMainList == null || clubMainList .size() <= 0){
			return null;
		}
		return clubMainList;
		
	}
	
	/**
	 * 根据俱乐部ID获取俱乐部
	 * @param clubId 俱乐部ID
	 * @return
	 * @author chengshx
	 */
	public ClubMain getClubMainById(Integer clubId){
		ClubMain clubMain = new ClubMain();
		clubMain.setClubId(clubId);
		return clubMainDao.selectClubMainEntity(clubMain);
	}
}