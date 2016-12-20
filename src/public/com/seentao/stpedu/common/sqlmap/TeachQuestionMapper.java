package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.TeachQuestion;

public interface TeachQuestionMapper {

	public abstract void insertTeachQuestion(TeachQuestion teachQuestion);
	
	public abstract void deleteTeachQuestion(TeachQuestion teachQuestion);
	
	public abstract void updateTeachQuestionByKey(TeachQuestion teachQuestion);
	
	public abstract List<TeachQuestion> selectSingleTeachQuestion(TeachQuestion teachQuestion);
	
	public abstract List<TeachQuestion> selectAllTeachQuestion();
	
	public abstract Integer queryCount(Map<String, Object> map);
	
	public abstract List<TeachQuestion> queryByPage(Map<String, Object> map);
	
	public abstract void addAnswerNum(Integer questionId);

	public abstract List<Integer> queryByPageids(Map<String, Object> map);

	public abstract void delTeachQuestionAll(List<TeachQuestion> delQuestionList);

	public abstract List<TeachQuestion> getNewTeachQuestion(TeachQuestion teachQuestion);

	public abstract Integer queryteachQuestionByCount(Map<String, Object> paramMap);

	public abstract List<TeachQuestion> queryteachQuestionByPage(Map<String, String> paramMap);

	public abstract Integer querynoEssenceByCount(Map<String, Object> paramMap);

	public abstract List<TeachQuestion> querynoEssenceByPage(Map<String, String> paramMap);
	/*
	 * 获取根据班级ID获得最后的问题
	 */
	
	public abstract TeachQuestion getNewTeachQuestionMax(@Param("classId")Integer classId);
}