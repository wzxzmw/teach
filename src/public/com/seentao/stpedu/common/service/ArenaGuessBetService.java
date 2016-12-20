package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.dao.ArenaGuessBetDao;
import com.seentao.stpedu.common.entity.ArenaGuessBet;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ArenaGuessBetService{

	@Autowired
	private ArenaGuessBetDao arenaGuessBetDao;

	public String getArenaGuessBet(Integer id) {
		ArenaGuessBet arenaGuessBet = new ArenaGuessBet();
		List<ArenaGuessBet> arenaGuessBetList = arenaGuessBetDao .selectSingleArenaGuessBet(arenaGuessBet);
		if(arenaGuessBetList == null || arenaGuessBetList .size() <= 0){
			return null;
		}

		return JSONArray.toJSONString(arenaGuessBetList);
	}

	/**
	 * @param userId
	 * @param quizId
	 * @param bettingObject
	 * @param bettingCount
	 * @param lockId
	 */
	public void submitQuizBett(Integer userId,Integer quizId, int bettingObject, Double bettingCount, int lockId) {
		try {   
			//判断是用户否存在下注     
			ArenaGuessBet arenaGuessBet = null;
			arenaGuessBet = new ArenaGuessBet();
			arenaGuessBet.setAmount(bettingCount);
			arenaGuessBet.setUserId(userId);
			arenaGuessBet.setGuessId(quizId);
			arenaGuessBet.setPosition(bettingObject);
			arenaGuessBet.setBetTime(TimeUtil.getCurrentTimestamp());
			arenaGuessBet.setLockId(lockId);
			arenaGuessBet.setBetResult(0);
			arenaGuessBet.setBonus(0.00);
			arenaGuessBetDao.insertArenaGuessBet(arenaGuessBet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ArenaGuessBet> getMaxBetList(ArenaGuessBet arenaguessbet) {
		return arenaGuessBetDao.getMaxBetList(arenaguessbet);
	}

	public Integer getMaxBetCount() {
		return arenaGuessBetDao.getMaxBetCount();
	}

	public List<ArenaGuessBet> getWinNumberList(ArenaGuessBet arenaguessbet) {
		return arenaGuessBetDao.getWinNumberList(arenaguessbet);
	}

	public Integer getWinNumberCount() {
		return arenaGuessBetDao.getWinNumberCount();
	}


	public void insertArenaGuessBet(ArenaGuessBet arenaGuessBet){
		arenaGuessBetDao .insertArenaGuessBet(arenaGuessBet);
	}

	/**
	 * 获取关联竞猜的所有下注对象
	 * @param guessId
	 * @return
	 * @author 			lw
	 * @date			2016年6月26日  下午7:48:37
	 */
	public List<ArenaGuessBet> findAllGuessBet(Integer guessId) {
		ArenaGuessBet tmpParam = new ArenaGuessBet();
		tmpParam.setGuessId(guessId);
		return arenaGuessBetDao.selectSingleArenaGuessBet(tmpParam);
	}

	/**
	 * 根据竞猜 人员id，竞猜id 获取竞猜对象
	 * @param guessId	竞猜id
	 * @param userId	竞猜人员id
	 * @return
	 * @author 			lw
	 * @date			2016年7月1日  上午9:39:28
	 */
	public List<ArenaGuessBet> sumGuessBetByGuessId(String guessIds, Integer userId) {
		ArenaGuessBet tmpParam = new ArenaGuessBet();
		tmpParam.setGuessIds(guessIds);
		tmpParam.setUserId(userId);
		return arenaGuessBetDao.sumGuessBetByGuessId(tmpParam);
	}


	/**
	 * 批量修改竞猜下注表成功和失败
	 * @param winBet	成功
	 * @param failBet	失败
	 * @author 			lw
	 * @date			2016年7月3日  下午3:31:56
	 */
	public void executeGuessResult(List<ArenaGuessBet> winBet, List<ArenaGuessBet> failBet) {
		if(!CollectionUtils.isEmpty(winBet)){
			arenaGuessBetDao.executeGuessWinResult(winBet);
		}
		if(!CollectionUtils.isEmpty(failBet)){
			arenaGuessBetDao.executeGuessFailResult(failBet);
		}
	}

	public Integer getArenaGuessBetCount(Integer valueOf) {
		return arenaGuessBetDao.getArenaGuessBetCount(valueOf);
	}

}