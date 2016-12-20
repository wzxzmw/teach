package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import com.seentao.stpedu.common.sqlmap.ClubTrainingQuestionAnswerMapper;


@Repository
public class ClubTrainingQuestionAnswerDao {

	@Autowired
	private ClubTrainingQuestionAnswerMapper clubTrainingQuestionAnswerMapper;
	
	
	public void insertClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		clubTrainingQuestionAnswerMapper .insertClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
	}
	
	public void deleteClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		clubTrainingQuestionAnswerMapper .deleteClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
	}
	
	public void updateClubTrainingQuestionAnswerByKey(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		clubTrainingQuestionAnswerMapper .updateClubTrainingQuestionAnswerByKey(clubTrainingQuestionAnswer);
	}
	
	public List<ClubTrainingQuestionAnswer> selectSingleClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		return clubTrainingQuestionAnswerMapper .selectSingleClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
	}
	
	public ClubTrainingQuestionAnswer selectClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		List<ClubTrainingQuestionAnswer> clubTrainingQuestionAnswerList = clubTrainingQuestionAnswerMapper .selectSingleClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
		if(clubTrainingQuestionAnswerList == null || clubTrainingQuestionAnswerList .size() == 0){
			return null;
		}
		return clubTrainingQuestionAnswerList .get(0);
	}
	
	public List<ClubTrainingQuestionAnswer> selectAllClubTrainingQuestionAnswer(){
		return clubTrainingQuestionAnswerMapper .selectAllClubTrainingQuestionAnswer();
	}

	public void delClubTrainingQuestionAnswerAll(List<ClubTrainingQuestionAnswer> delClubTrainingQuestionAnswer) {
		clubTrainingQuestionAnswerMapper.delClubTrainingQuestionAnswerAll(delClubTrainingQuestionAnswer);
		
	}
	
	public Integer queryCount(Map<String, Object> paramMap) {
		return clubTrainingQuestionAnswerMapper.queryCount(paramMap);
	}
	public List<Integer> queryByPageids(Map<String, Object> paramMap) {
		return clubTrainingQuestionAnswerMapper.queryByPageids(paramMap);
	}

	public  List<ClubTrainingQuestionAnswer> queryByPage(Map<String, Object> paramMap) {
		return clubTrainingQuestionAnswerMapper.queryByPage(paramMap);
	}
	
	
}