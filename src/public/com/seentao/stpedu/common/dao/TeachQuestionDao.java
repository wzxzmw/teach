package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachCourseDiscuss;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.sqlmap.TeachQuestionMapper;


@Repository
public class TeachQuestionDao {

	@Autowired
	private TeachQuestionMapper teachQuestionMapper;
	
	
	public void insertTeachQuestion(TeachQuestion teachQuestion){
		teachQuestionMapper .insertTeachQuestion(teachQuestion);
	}
	
	public void deleteTeachQuestion(TeachQuestion teachQuestion){
		teachQuestionMapper .deleteTeachQuestion(teachQuestion);
	}
	
	public void updateTeachQuestionByKey(TeachQuestion teachQuestion){
		teachQuestionMapper .updateTeachQuestionByKey(teachQuestion);
	}
	
	public List<TeachQuestion> selectSingleTeachQuestion(TeachQuestion teachQuestion){
		return teachQuestionMapper .selectSingleTeachQuestion(teachQuestion);
	}
	
	public TeachQuestion selectTeachQuestion(TeachQuestion teachQuestion){
		List<TeachQuestion> teachQuestionList = teachQuestionMapper .selectSingleTeachQuestion(teachQuestion);
		if(teachQuestionList == null || teachQuestionList .size() == 0){
			return null;
		}
		return teachQuestionList .get(0);
	}
	
	public List<TeachQuestion> selectAllTeachQuestion(){
		return teachQuestionMapper .selectAllTeachQuestion();
	}
	
	public Integer queryCount(Map<String, Object> map){
		return teachQuestionMapper.queryCount(map);
	}
	
	public List<TeachQuestion> queryByPage(Map<String, Object> map){
		return teachQuestionMapper.queryByPage(map);
	}
	
	public Integer queryteachQuestionByCount(Map<String, Object> paramMap) {
		return teachQuestionMapper.queryteachQuestionByCount(paramMap);
	}
	
	public List<TeachQuestion> queryteachQuestionByPage(Map<String, String> paramMap) {
		return teachQuestionMapper.queryteachQuestionByPage(paramMap);
	}
	public Integer querynoEssenceByCount(Map<String, Object> paramMap) {
		return teachQuestionMapper.querynoEssenceByCount(paramMap);
	}
	
	public List<TeachQuestion> querynoEssenceByPage(Map<String, String> paramMap) {
		return teachQuestionMapper.querynoEssenceByPage(paramMap);
	}
	public List<Integer> queryByPageids(Map<String , Object> map){
		return teachQuestionMapper.queryByPageids(map);
	}
	
	
	public TeachQuestion getEntityForDB(Map<String , Object> map){
		TeachQuestion tmp = new TeachQuestion();
		tmp.setQuestionId(Integer.valueOf(map.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectTeachQuestion(tmp);
	}

	/**
	 * 回复数量加一
	 * @param questionId
	 * @author 			lw
	 * @date			2016年6月29日  上午11:16:57
	 */
	public void addAnswerNum(Integer questionId) {
		teachQuestionMapper.addAnswerNum(questionId);
	}

	public void delTeachQuestionAll(List<TeachQuestion> delQuestionList) {
		teachQuestionMapper.delTeachQuestionAll(delQuestionList);
	}

	public TeachQuestion getNewTeachQuestion(TeachQuestion teachQuestion) {
		List<TeachQuestion> teachQuestionList = teachQuestionMapper.getNewTeachQuestion(teachQuestion);
		if(teachQuestionList == null || teachQuestionList .size() == 0){
			return null;
		}
		return teachQuestionList .get(0);
	}
	/*
	 * 获取根据班级ID获得最后的问题
	 */
	
	public TeachQuestion getNewTeachQuestionMax(Integer classId){
		return teachQuestionMapper.getNewTeachQuestionMax(classId);
	}
}