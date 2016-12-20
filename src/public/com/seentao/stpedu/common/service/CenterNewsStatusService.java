package com.seentao.stpedu.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seentao.stpedu.common.dao.CenterNewsStatusDao;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.utils.LogUtil;

@Service
@Transactional
public class CenterNewsStatusService{
	
	@Autowired
	private CenterNewsStatusDao centerNewsStatusDao;
	/**
	 * 添加消息状态
	 * @param centerNewsStatus
	 * @throws RuntimeException
	 */
	public void insertCenterNewsStatus(CenterNewsStatus centerNewsStatus) throws RuntimeException{
		try {
			centerNewsStatusDao.insertCenterNewsStatus(centerNewsStatus);
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据用户id查询
	 * @return
	 */
	public CenterNewsStatus queryCenterNewsStatusByUserId(Integer userId){
		
		return centerNewsStatusDao.queryCenterNewsStatusByUserId(userId);
	}
	
	public void updateCenterNewsStatusByKey(CenterNewsStatus centerNewsStatus)throws RuntimeException{
		try {
			centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改用户消息状态
	 * @param pmObjectId
	 * @return
	 * @author cxw
	 */
	/*public void submitCenterNewStatus(String pmObjectId) throws RuntimeException{
		//向用户消息状态表改是否有新私信
		try {
			CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
			CenterNewsStatus status = centerNewsStatusDao.queryCenterNewsStatusByUserId(Integer.valueOf(pmObjectId));
			if(status==null){
				centerNewsStatus.setUserId(Integer.valueOf(pmObjectId));
				centerNewsStatus.setIsNewPrivateMessage(1);
				centerNewsStatus.setIsNewAttention(0);
				centerNewsStatus.setIsNewQuestionInvite(0);
				centerNewsStatus.setIsNewGameInvite(0);
				centerNewsStatus.setIsNewSysNews(0);
				try {
					centerNewsStatusDao.insertCenterNewsStatus(centerNewsStatus);
				} catch (Exception e) {
					throw  new RuntimeException(e);
				}
			}else{
				centerNewsStatus.setStatusId(status.getStatusId());
				centerNewsStatus.setIsNewPrivateMessage(1);
				centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/

	/*public void updateCenterNewsStatusByKeyAll(CenterNewsStatus centerStatus) {
		centerNewsStatusDao.updateCenterNewsStatusByKeyAll(centerStatus);
	}*/


	/**
	 * 修改问题消息状态表
	 * @param userId
	 * @author 			lw
	 * @date			2016年7月19日  下午9:49:51
	 */
/*	public void submitCenterNewStatusQuestion(Integer userId) {
		//向用户消息状态表改是否有新私信
		try {
			CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
			CenterNewsStatus status = centerNewsStatusDao.queryCenterNewsStatusByUserId(userId);
			if(status==null){
				centerNewsStatus.setUserId(userId);
				centerNewsStatus.setIsNewPrivateMessage(0);
				centerNewsStatus.setIsNewAttention(0);
				centerNewsStatus.setIsNewQuestionInvite(1);
				centerNewsStatus.setIsNewGameInvite(0);
				centerNewsStatus.setIsNewSysNews(0);
				centerNewsStatusDao.insertCenterNewsStatus(centerNewsStatus);
			}else{
				centerNewsStatus.setStatusId(status.getStatusId());
				centerNewsStatus.setIsNewQuestionInvite(1);
				centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	/**
	 * 修改私信状态表
	 * @param userId
	 * @author 			cxw
	 * @date			2016年7月19日  下午9:49:51
	 */
/*	public void submitPrivateMessage(Integer receiveUserId)throws RuntimeException{
		//向用户消息状态表改是否有新私信
		CenterNewsStatus centerNewsStatus = new CenterNewsStatus();
		CenterNewsStatus status = centerNewsStatusDao.queryCenterNewsStatusByUserId(receiveUserId);
		if(status==null){
			centerNewsStatus.setUserId(receiveUserId);
			centerNewsStatus.setIsNewPrivateMessage(1);
			centerNewsStatus.setIsNewAttention(0);
			centerNewsStatus.setIsNewQuestionInvite(0);
			centerNewsStatus.setIsNewGameInvite(0);
			centerNewsStatus.setIsNewSysNews(0);
			try {
				centerNewsStatusDao.insertCenterNewsStatus(centerNewsStatus);
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		}else{
			centerNewsStatus.setStatusId(status.getStatusId());
			centerNewsStatus.setIsNewPrivateMessage(1);
			try {
				centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}*/

	/**
	 * @param userId
	 * @param actionType 1、表示私信 2、新问题,3、表示新关注,4、新比赛邀请 5、新的系统消息
	 */
	public void submitOrUpdateSome(Integer userId,Integer actionType)throws RuntimeException{
		
		CenterNewsStatus centerNewsStatus = centerNewsStatusDao.queryCenterNewsStatusByUserId(userId);
		if(centerNewsStatus == null){
			CenterNewsStatus status = new CenterNewsStatus();
			status.setUserId(userId);
			this.commonStatusToInsert(status, actionType);
			try {
				
				centerNewsStatusDao.insertCenterNewsStatus(status);
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "submitOrUpdateSome", "insert some centerNewsStatus is error", e);
				throw new RuntimeException(e);
			}
		}else{
			this.commonStatusToUpdate(centerNewsStatus, actionType);
			try {
				centerNewsStatusDao.updateCenterNewsStatusByKey(centerNewsStatus);
			} catch (Exception e) {
				LogUtil.error(this.getClass(), "submitOrUpdateSome", "update some centerNewsStatus is error", e);
				throw new RuntimeException(e);
			}
		}
		
	}
	
	public void commonStatusToInsert(CenterNewsStatus centerNewsStatus,Integer actionType){
		if(actionType == 1){
			centerNewsStatus.setIsNewPrivateMessage(1);
		}else{
			centerNewsStatus.setIsNewPrivateMessage(0);
		}if(actionType == 2){
			centerNewsStatus.setIsNewQuestionInvite(1);
		}else{
			centerNewsStatus.setIsNewQuestionInvite(0);
		}if(actionType == 3){
			centerNewsStatus.setIsNewAttention(1);
		}else{
			centerNewsStatus.setIsNewAttention(0);
		}if(actionType ==4){
			centerNewsStatus.setIsNewGameInvite(1);
		}else{
			centerNewsStatus.setIsNewGameInvite(0);
		}if(actionType == 5){
			centerNewsStatus.setIsNewSysNews(1);
		}else{
			centerNewsStatus.setIsNewSysNews(0);
		}
	}
	public void commonStatusToUpdate(CenterNewsStatus centerNewsStatus,Integer actionType){
		if(actionType == 1){
			centerNewsStatus.setIsNewPrivateMessage(1);
		}if(actionType == 2){
			centerNewsStatus.setIsNewQuestionInvite(1);
		}if(actionType == 3){
			centerNewsStatus.setIsNewAttention(1);
		}if(actionType ==4){
			centerNewsStatus.setIsNewGameInvite(1);
		}if(actionType == 5){
			centerNewsStatus.setIsNewSysNews(1);
		}	
	}
	/** 
	* @Title: getCenterNewsStatusByUserId 
	* @Description: 根据用户ID获得消息状态
	* @param userId
	* @return CenterNewsStatus
	* @author liulin
	* @date 2016年8月4日 下午3:25:06
	*/
	public CenterNewsStatus getCenterNewsStatusByUserId(Integer userId) {
		return centerNewsStatusDao.queryCenterNewsStatusByUserId(userId);
	}
}