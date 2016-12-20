package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.CenterFeedback;
import java.util.List;

public interface CenterFeedbackMapper {

	public abstract void insertCenterFeedback(CenterFeedback centerFeedback);
	
	public abstract void deleteCenterFeedback(CenterFeedback centerFeedback);
	
	public abstract void updateCenterFeedbackByKey(CenterFeedback centerFeedback);
	
	public abstract List<CenterFeedback> selectSingleCenterFeedback(CenterFeedback centerFeedback);
	
	public abstract List<CenterFeedback> selectAllCenterFeedback();
	
}