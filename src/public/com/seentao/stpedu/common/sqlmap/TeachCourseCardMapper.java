package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.TeachCourseCard;

public interface TeachCourseCardMapper {

	public abstract Integer insertTeachCourseCard(TeachCourseCard teachCourseCard);
	
	public abstract void deleteTeachCourseCard(TeachCourseCard teachCourseCard);
	
	public abstract void updateTeachCourseCardByKey(TeachCourseCard teachCourseCard);
	
	public abstract List<TeachCourseCard> selectSingleTeachCourseCard(TeachCourseCard teachCourseCard);
	
	public abstract List<TeachCourseCard> selectAllTeachCourseCard();

	public abstract List<TeachCourseCard> getLastTeachCourseCardByChapterId(TeachCourseCard teachCourseCard);

	public abstract List<TeachCourseCard> selectByChapterIds(String cIds);

	public abstract void updateCourseCardByCardId(@Param(value="cardId")Integer cardId);

	public abstract List<TeachCourseCard> selectByChapterId(TeachCourseCard parm);
	
}