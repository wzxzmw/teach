package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.ClubTrainingClass;
import com.seentao.stpedu.common.entity.ClubTrainingCourse;
import com.seentao.stpedu.common.sqlmap.ClubTrainingClassMapper;


@Repository
public class ClubTrainingClassDao {

	@Autowired
	private ClubTrainingClassMapper clubTrainingClassMapper;
	
	
	public void insertClubTrainingClass(ClubTrainingClass clubTrainingClass){
		clubTrainingClassMapper .insertClubTrainingClass(clubTrainingClass);
	}
	
	public void deleteClubTrainingClass(ClubTrainingClass clubTrainingClass){
		clubTrainingClassMapper .deleteClubTrainingClass(clubTrainingClass);
	}
	
	public void updateClubTrainingClassByKey(ClubTrainingClass clubTrainingClass){
		clubTrainingClassMapper .updateClubTrainingClassByKey(clubTrainingClass);
	}
	
	public List<ClubTrainingClass> selectSingleClubTrainingClass(ClubTrainingClass clubTrainingClass){
		return clubTrainingClassMapper .selectSingleClubTrainingClass(clubTrainingClass);
	}
	
	public ClubTrainingClass selectClubTrainingClass(ClubTrainingClass clubTrainingClass){
		List<ClubTrainingClass> clubTrainingClassList = clubTrainingClassMapper .selectSingleClubTrainingClass(clubTrainingClass);
		if(clubTrainingClassList == null || clubTrainingClassList .size() == 0){
			return null;
		}
		return clubTrainingClassList .get(0);
	}
	
	public List<ClubTrainingClass> selectAllClubTrainingClass(){
		return clubTrainingClassMapper .selectAllClubTrainingClass();
	}
	public Integer queryClubClassCount(Map<String, Object> paramMap) {
		return clubTrainingClassMapper.queryClubClassCount(paramMap);
	}

	public List<ClubTrainingClass> queryClubClassByPage(Map<String, Object> paramMap) {
		return clubTrainingClassMapper.queryClubClassByPage(paramMap);
	}
	public Integer queryClubClassCountAll(Map<String, Object> paramMap) {
		return clubTrainingClassMapper.queryClubClassCountAll(paramMap);
	}

	public List<ClubTrainingClass> queryClubClassByPageAll(Map<String, Object> paramMap) {
		return clubTrainingClassMapper.queryClubClassByPageAll(paramMap);
	}
	
	public List<Integer> getClassByCourseType(int classType){
		return clubTrainingClassMapper.getClassByCourseType(classType);
	}

	public Integer returninsertClubTrainingClass(ClubTrainingClass clubTrainingClass) {
		return clubTrainingClassMapper.returninsertClubTrainingClass(clubTrainingClass);
	}
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubTrainingClass getEntityForDB(Map<String, Object> paramMap){
		ClubTrainingClass tmp = new ClubTrainingClass();
		tmp.setClassId(Integer.valueOf(paramMap.get("id_key").toString()));
		tmp.setIsDelete(0);
		return this.selectClubTrainingClass(tmp);
	}

	public void updateClubTrainingClassNum(ClubTrainingClass updateClubTrainingClass) {
		clubTrainingClassMapper.updateClubTrainingClassNum(updateClubTrainingClass);
	}

	public void updateClubTrainingClassNumByKey(ClubTrainingClass updateClubTrainingClass) {
		clubTrainingClassMapper.updateClubTrainingClassNumByKey(updateClubTrainingClass);
	}
}