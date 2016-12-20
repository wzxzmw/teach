package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import java.util.List;
import java.util.Map;

public interface ClubTrainingCourseMapper {

	public abstract void insertClubTrainingCourse(ClubTrainingCourse clubTrainingCourse);
	
	public abstract void deleteClubTrainingCourse(ClubTrainingCourse clubTrainingCourse);
	
	public abstract void updateClubTrainingCourseByKey(ClubTrainingCourse clubTrainingCourse);
	
	public abstract List<ClubTrainingCourse> selectSingleClubTrainingCourse(ClubTrainingCourse clubTrainingCourse);
	
	public abstract List<ClubTrainingCourse> selectAllClubTrainingCourse();

	public abstract Integer getClubTrainingCourseCount(Map<String, Object> paramMap);

	public abstract void delClubTrainingCourseAll(List<ClubTrainingCourse> delClubTrainingCourse);
	
	public abstract List<Integer> getClassByCourseType(int classType);


}