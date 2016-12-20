package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.sqlmap.TeachCourseCardMapper;


@Repository
public class TeachCourseCardDao {

	@Autowired
	private TeachCourseCardMapper teachCourseCardMapper;
	
	
	public Integer insertTeachCourseCard(TeachCourseCard teachCourseCard){
		return teachCourseCardMapper.insertTeachCourseCard(teachCourseCard);
	}
	
	public void deleteTeachCourseCard(TeachCourseCard teachCourseCard){
		teachCourseCardMapper .deleteTeachCourseCard(teachCourseCard);
	}
	
	public void updateTeachCourseCardByKey(TeachCourseCard teachCourseCard){
		teachCourseCardMapper .updateTeachCourseCardByKey(teachCourseCard);
	}
	
	public List<TeachCourseCard> selectSingleTeachCourseCard(TeachCourseCard teachCourseCard){
		return teachCourseCardMapper .selectSingleTeachCourseCard(teachCourseCard);
	}
	
	public TeachCourseCard selectTeachCourseCard(TeachCourseCard teachCourseCard){
		List<TeachCourseCard> teachCourseCardList = teachCourseCardMapper .selectSingleTeachCourseCard(teachCourseCard);
		if(teachCourseCardList == null || teachCourseCardList .size() == 0){
			return null;
		}
		return teachCourseCardList .get(0);
	}
	
	public List<TeachCourseCard> selectAllTeachCourseCard(){
		return teachCourseCardMapper .selectAllTeachCourseCard();
	}
	
	
	public TeachCourseCard getEntityForDB(Map<String , Object> paramMap){
		TeachCourseCard teachCourseCard = new TeachCourseCard();
		teachCourseCard.setCardId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectTeachCourseCard(teachCourseCard);
	}

	public TeachCourseCard getLastTeachCourseCardByChapterId(TeachCourseCard teachCourseCard) {
		List<TeachCourseCard> teachCourseCardList = teachCourseCardMapper .getLastTeachCourseCardByChapterId(teachCourseCard);
		if(teachCourseCardList == null || teachCourseCardList .size() == 0){
			return null;
		}
		return teachCourseCardList .get(0);
	}

	public List<TeachCourseCard> selectByChapterIds(String cIds) {
		return teachCourseCardMapper.selectByChapterIds(cIds);
	}

	public void updateCourseCardByCardId(Integer cardId) {
		teachCourseCardMapper.updateCourseCardByCardId(cardId);
	}

	public List<TeachCourseCard> selectByChapterId(TeachCourseCard parm) {
		return teachCourseCardMapper.selectByChapterId(parm);
	}
	
}