package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.entity.ClubTopic;
import com.seentao.stpedu.common.sqlmap.ClubRemindMapper;


@Repository
public class ClubRemindDao {

	@Autowired
	private ClubRemindMapper clubRemindMapper;
	
	
	public void insertClubRemind(ClubRemind clubRemind){
		clubRemindMapper .insertClubRemind(clubRemind);
	}
	
	public void deleteClubRemind(ClubRemind clubRemind){
		clubRemindMapper .deleteClubRemind(clubRemind);
	}
	
	public void updateClubRemindByKey(ClubRemind clubRemind){
		clubRemindMapper .updateClubRemindByKey(clubRemind);
	}
	
	public List<ClubRemind> selectClubRemind(ClubRemind clubRemind){
		return clubRemindMapper .selectSingleClubRemind(clubRemind);
	}
	
	public ClubRemind selectSingleClubRemind(ClubRemind clubRemind){
		List<ClubRemind> clubRemindList = clubRemindMapper .selectSingleClubRemind(clubRemind);
		if(clubRemindList == null || clubRemindList .size() == 0){
			return null;
		}
		return clubRemindList .get(0);
	}
	
	public List<ClubRemind> selectAllClubRemind(){
		return clubRemindMapper .selectAllClubRemind();
	}

	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubRemind getEntityForDB(Map<String, Object> paramMap){
		ClubRemind tmp = new ClubRemind();
		tmp.setRemindId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectSingleClubRemind(tmp);
	}
	public Integer queryCount(Map<String, Object> paramMap){
		return clubRemindMapper.queryCount(paramMap);
	}
}