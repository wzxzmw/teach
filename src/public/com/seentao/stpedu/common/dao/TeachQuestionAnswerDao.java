package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachQuestionAnswer;
import com.seentao.stpedu.common.sqlmap.TeachQuestionAnswerMapper;


@Repository
public class TeachQuestionAnswerDao {

	@Autowired
	private TeachQuestionAnswerMapper teachQuestionAnswerMapper;
	
	
	public void insertTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		teachQuestionAnswerMapper .insertTeachQuestionAnswer(teachQuestionAnswer);
	}
	
	public void deleteTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		teachQuestionAnswerMapper .deleteTeachQuestionAnswer(teachQuestionAnswer);
	}
	
	public void updateTeachQuestionAnswerByKey(TeachQuestionAnswer teachQuestionAnswer){
		teachQuestionAnswerMapper .updateTeachQuestionAnswerByKey(teachQuestionAnswer);
	}
	
	public List<TeachQuestionAnswer> selectSingleTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		return teachQuestionAnswerMapper .selectSingleTeachQuestionAnswer(teachQuestionAnswer);
	}
	
	public TeachQuestionAnswer selectTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		List<TeachQuestionAnswer> teachQuestionAnswerList = teachQuestionAnswerMapper .selectSingleTeachQuestionAnswer(teachQuestionAnswer);
		if(teachQuestionAnswerList == null || teachQuestionAnswerList .size() == 0){
			return null;
		}
		return teachQuestionAnswerList .get(0);
	}
	
	public List<TeachQuestionAnswer> selectAllTeachQuestionAnswer(){
		return teachQuestionAnswerMapper .selectAllTeachQuestionAnswer();
	}

	public void delTeachQuestionAnswerAll(List<TeachQuestionAnswer> delTeachQuestionAnswer) {
		teachQuestionAnswerMapper.delTeachQuestionAnswerAll(delTeachQuestionAnswer);
	}

	public Integer queryCount(Map<String, Object> paramMap) {
		return teachQuestionAnswerMapper.queryCount(paramMap);
	}
	
	public List<Integer> queryByPageids(Map<String, String> paramMap) {
		return teachQuestionAnswerMapper.queryByPageids(paramMap);
	}
	public  List<TeachQuestionAnswer> queryByPage(Map<String, Object> paramMap) {
		return teachQuestionAnswerMapper.queryByPage(paramMap);
	}
}