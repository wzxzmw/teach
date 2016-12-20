package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.seentao.stpedu.common.entity.CenterFeedback;
import com.seentao.stpedu.common.sqlmap.CenterFeedbackMapper;


@Repository
public class CenterFeedbackDao {

	@Autowired
	private CenterFeedbackMapper centerFeedbackMapper;
	
	
	public void insertCenterFeedback(CenterFeedback centerFeedback){
		centerFeedbackMapper .insertCenterFeedback(centerFeedback);
	}
	
	public void deleteCenterFeedback(CenterFeedback centerFeedback){
		centerFeedbackMapper .deleteCenterFeedback(centerFeedback);
	}
	
	public void updateCenterFeedbackByKey(CenterFeedback centerFeedback){
		centerFeedbackMapper .updateCenterFeedbackByKey(centerFeedback);
	}
	
	public List<CenterFeedback> selectCenterFeedback(CenterFeedback centerFeedback){
		return centerFeedbackMapper .selectSingleCenterFeedback(centerFeedback);
	}
	
	public CenterFeedback selectSingleCenterFeedback(CenterFeedback centerFeedback){
		List<CenterFeedback> centerFeedbackList = centerFeedbackMapper .selectSingleCenterFeedback(centerFeedback);
		if(centerFeedbackList == null || centerFeedbackList .size() == 0){
			return null;
		}
		return centerFeedbackList .get(0);
	}
	
	public List<CenterFeedback> selectAllCenterFeedback(){
		return centerFeedbackMapper .selectAllCenterFeedback();
	}
}