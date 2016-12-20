package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubTrainingClass;
import java.util.List;
import java.util.Map;

public interface ClubTrainingClassMapper {

	public abstract void insertClubTrainingClass(ClubTrainingClass clubTrainingClass);
	
	public abstract void deleteClubTrainingClass(ClubTrainingClass clubTrainingClass);
	
	public abstract void updateClubTrainingClassByKey(ClubTrainingClass clubTrainingClass);
	
	public abstract List<ClubTrainingClass> selectSingleClubTrainingClass(ClubTrainingClass clubTrainingClass);
	
	public abstract List<ClubTrainingClass> selectAllClubTrainingClass();

	public abstract Integer queryClubClassCount(Map<String, Object> paramMap);

	public abstract List<ClubTrainingClass> queryClubClassByPage(Map<String, Object> paramMap);
	
	public abstract List<Integer> getClassByCourseType(int classType);

	public abstract Integer returninsertClubTrainingClass(ClubTrainingClass clubTrainingClass);

	public abstract Integer queryClubClassCountAll(Map<String, Object> paramMap);

	public abstract List<ClubTrainingClass> queryClubClassByPageAll(Map<String, Object> paramMap);

	public abstract void updateClubTrainingClassNum(ClubTrainingClass updateClubTrainingClass);

	public abstract void updateClubTrainingClassNumByKey(ClubTrainingClass updateClubTrainingClass);
		
	public abstract Integer queryMyTrainClassCount(Map<String, Object> paramMap);
	
	public abstract List<ClubTrainingClass> queryMyTrainClassByPage(Map<String, Object> paramMap);
	
}