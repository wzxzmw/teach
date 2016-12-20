package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachQuestion;

import java.util.List;
import java.util.Map;

public interface ClubTrainingQuestionMapper {

	public abstract void insertClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion);
	
	public abstract void deleteClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion);
	
	public abstract void updateClubTrainingQuestionByKey(ClubTrainingQuestion clubTrainingQuestion);
	
	public abstract List<ClubTrainingQuestion> selectSingleClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion);
	
	public abstract List<ClubTrainingQuestion> selectAllClubTrainingQuestion();

	public abstract void addAnswerNum(Integer questionId);

	public abstract Integer queryCount(Map<String, Object> map);

	public abstract List<TeachQuestion> queryByPage(Map<String, Object> map);

	public abstract List<Integer> queryByPageids(Map<String, Object> map);

	public abstract void delClubTrainingQuestionAll(List<ClubTrainingQuestion> delClubTrainingQuestion);
	
}