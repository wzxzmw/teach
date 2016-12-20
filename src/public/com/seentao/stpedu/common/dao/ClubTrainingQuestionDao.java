package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubRedPacket;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.sqlmap.ClubTrainingQuestionMapper;


@Repository
public class ClubTrainingQuestionDao {

	@Autowired
	private ClubTrainingQuestionMapper clubTrainingQuestionMapper;
	
	
	public void insertClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion){
		clubTrainingQuestionMapper .insertClubTrainingQuestion(clubTrainingQuestion);
	}
	
	public void deleteClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion){
		clubTrainingQuestionMapper .deleteClubTrainingQuestion(clubTrainingQuestion);
	}
	
	public void updateClubTrainingQuestionByKey(ClubTrainingQuestion clubTrainingQuestion){
		clubTrainingQuestionMapper .updateClubTrainingQuestionByKey(clubTrainingQuestion);
	}
	
	public List<ClubTrainingQuestion> selectSingleClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion){
		return clubTrainingQuestionMapper .selectSingleClubTrainingQuestion(clubTrainingQuestion);
	}
	
	public ClubTrainingQuestion selectClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion){
		List<ClubTrainingQuestion> clubTrainingQuestionList = clubTrainingQuestionMapper .selectSingleClubTrainingQuestion(clubTrainingQuestion);
		if(clubTrainingQuestionList == null || clubTrainingQuestionList .size() == 0){
			return null;
		}
		return clubTrainingQuestionList .get(0);
	}
	
	public List<ClubTrainingQuestion> selectAllClubTrainingQuestion(){
		return clubTrainingQuestionMapper .selectAllClubTrainingQuestion();
	}
	
	public ClubTrainingQuestion getEntityForDB(Map<String, Object> paramMap){
		ClubTrainingQuestion tmp = new ClubTrainingQuestion();
		tmp.setQuestionId(Integer.valueOf(paramMap.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectClubTrainingQuestion(tmp);
		
	}

	public void addAnswerNum(Integer questionId) {
		clubTrainingQuestionMapper.addAnswerNum(questionId);
	}
	
	public Integer queryCount(Map<String, Object> map){
		return clubTrainingQuestionMapper.queryCount(map);
	}
	
	public List<TeachQuestion> queryByPage(Map<String, Object> map){
		return clubTrainingQuestionMapper.queryByPage(map);
	}
	
	public List<Integer> queryByPageids(Map<String , Object> map){
		return clubTrainingQuestionMapper.queryByPageids(map);
	}

	public void delClubTrainingQuestionAll(List<ClubTrainingQuestion> delClubTrainingQuestion) {
		clubTrainingQuestionMapper.delClubTrainingQuestionAll(delClubTrainingQuestion);
	}
	
	
	
	
	
	
	
	
}