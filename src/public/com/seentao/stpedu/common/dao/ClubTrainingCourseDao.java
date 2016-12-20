package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.sqlmap.ClubTrainingCourseMapper;


@Repository
public class ClubTrainingCourseDao {

	@Autowired
	private ClubTrainingCourseMapper clubTrainingCourseMapper;
	
	
	public void insertClubTrainingCourse(ClubTrainingCourse clubTrainingCourse){
		clubTrainingCourseMapper .insertClubTrainingCourse(clubTrainingCourse);
	}
	
	public void deleteClubTrainingCourse(ClubTrainingCourse clubTrainingCourse){
		clubTrainingCourseMapper .deleteClubTrainingCourse(clubTrainingCourse);
	}
	
	public void updateClubTrainingCourseByKey(ClubTrainingCourse clubTrainingCourse){
		clubTrainingCourseMapper .updateClubTrainingCourseByKey(clubTrainingCourse);
	}
	
	public List<ClubTrainingCourse> selectSingleClubTrainingCourse(ClubTrainingCourse clubTrainingCourse){
		return clubTrainingCourseMapper .selectSingleClubTrainingCourse(clubTrainingCourse);
	}
	
	public ClubTrainingCourse selectClubTrainingCourse(ClubTrainingCourse clubTrainingCourse){
		List<ClubTrainingCourse> clubTrainingCourseList = clubTrainingCourseMapper .selectSingleClubTrainingCourse(clubTrainingCourse);
		if(clubTrainingCourseList == null || clubTrainingCourseList .size() == 0){
			return null;
		}
		return clubTrainingCourseList .get(0);
	}
	
	public List<ClubTrainingCourse> selectAllClubTrainingCourse(){
		return clubTrainingCourseMapper .selectAllClubTrainingCourse();
	}

	public Integer getClubTrainingCourseCount(Map<String, Object> paramMap) {
		return clubTrainingCourseMapper.getClubTrainingCourseCount(paramMap) ;
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		ClubTrainingCourse clubTrainingCourse = new ClubTrainingCourse();
		clubTrainingCourse.setCourseId(Integer.valueOf(queryParam.get("id_key").toString()));
		return selectClubTrainingCourse(clubTrainingCourse);
	}

	public void delClubTrainingCourseAll(List<ClubTrainingCourse> delClubTrainingCourse) {
		clubTrainingCourseMapper.delClubTrainingCourseAll(delClubTrainingCourse);
	}
	
	public List<Integer> getClassByCourseType(int classType){
		return clubTrainingCourseMapper.getClassByCourseType(classType);
	}

	
	
}