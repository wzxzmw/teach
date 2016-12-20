package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubBackgroundColor;
import com.seentao.stpedu.common.sqlmap.ClubBackgroundColorMapper;


@Repository
public class ClubBackgroundColorDao {

	@Autowired
	private ClubBackgroundColorMapper clubBackgroundColorMapper;
	
	
	public void insertClubBackgroundColor(ClubBackgroundColor clubBackgroundColor){
		clubBackgroundColorMapper .insertClubBackgroundColor(clubBackgroundColor);
	}
	
	public void deleteClubBackgroundColor(ClubBackgroundColor clubBackgroundColor){
		clubBackgroundColorMapper .deleteClubBackgroundColor(clubBackgroundColor);
	}
	
	public void updateClubBackgroundColorByKey(ClubBackgroundColor clubBackgroundColor){
		clubBackgroundColorMapper .updateClubBackgroundColorByKey(clubBackgroundColor);
	}
	
	public List<ClubBackgroundColor> selectSingleClubBackgroundColor(ClubBackgroundColor clubBackgroundColor){
		return clubBackgroundColorMapper .selectSingleClubBackgroundColor(clubBackgroundColor);
	}
	
	public ClubBackgroundColor selectClubBackgroundColor(ClubBackgroundColor clubBackgroundColor){
		List<ClubBackgroundColor> clubBackgroundColorList = clubBackgroundColorMapper .selectSingleClubBackgroundColor(clubBackgroundColor);
		if(clubBackgroundColorList == null || clubBackgroundColorList .size() == 0){
			return null;
		}
		return clubBackgroundColorList .get(0);
	}
	
	public List<ClubBackgroundColor> selectAllClubBackgroundColor(){
		return clubBackgroundColorMapper .selectAllClubBackgroundColor();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubBackgroundColor getEntityForDB(Map<String, Object> paramMap){
		ClubBackgroundColor tmp = new ClubBackgroundColor();
		tmp.setBgColorId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectClubBackgroundColor(tmp);
	}
}