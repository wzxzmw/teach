package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import java.util.List;
import java.util.Map;

public interface ClubTrainingQuestionAnswerMapper {

	public abstract void insertClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer);
	
	public abstract void deleteClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer);
	
	public abstract void updateClubTrainingQuestionAnswerByKey(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer);
	
	public abstract List<ClubTrainingQuestionAnswer> selectSingleClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer);
	
	public abstract List<ClubTrainingQuestionAnswer> selectAllClubTrainingQuestionAnswer();

	public abstract void delClubTrainingQuestionAnswerAll(List<ClubTrainingQuestionAnswer> delClubTrainingQuestionAnswer);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ClubTrainingQuestionAnswer> queryByPage(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageids(Map<String, Object> paramMap);
	
}