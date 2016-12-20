package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachQuestionAnswer;

public interface TeachQuestionAnswerMapper {

	public abstract void insertTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer);
	
	public abstract void deleteTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer);
	
	public abstract void updateTeachQuestionAnswerByKey(TeachQuestionAnswer teachQuestionAnswer);
	
	public abstract List<TeachQuestionAnswer> selectSingleTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer);
	
	public abstract List<TeachQuestionAnswer> selectAllTeachQuestionAnswer();

	public abstract void delTeachQuestionAnswerAll(List<TeachQuestionAnswer> delTeachQuestionAnswer);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<TeachQuestionAnswer> queryByPage(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageids(Map<String, String> paramMap);
	
}