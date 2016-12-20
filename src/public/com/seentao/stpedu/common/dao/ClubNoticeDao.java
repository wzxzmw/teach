package com.seentao.stpedu.common.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubNotice;
import com.seentao.stpedu.common.entity.ClubRemind;
import com.seentao.stpedu.common.sqlmap.ClubNoticeMapper;


@Repository
public class ClubNoticeDao {

	@Autowired
	private ClubNoticeMapper clubNoticeMapper;
	
	
	public void insertClubNotice(ClubNotice clubNotice){
		clubNoticeMapper .insertClubNotice(clubNotice);
	}
	
	public void deleteClubNotice(ClubNotice clubNotice){
		clubNoticeMapper .deleteClubNotice(clubNotice);
	}
	
	public void updateClubNoticeByKey(ClubNotice clubNotice){
		clubNoticeMapper .updateClubNoticeByKey(clubNotice);
	}
	
	public List<ClubNotice> selectClubNotice(ClubNotice clubNotice){
		return clubNoticeMapper .selectSingleClubNotice(clubNotice);
	}
	
	public ClubNotice selectSingleClubNotice(ClubNotice clubNotice){
		List<ClubNotice> clubNoticeList = clubNoticeMapper .selectSingleClubNotice(clubNotice);
		if(clubNoticeList == null || clubNoticeList .size() == 0){
			return null;
		}
		return clubNoticeList .get(0);
	}
	
	public List<ClubNotice> selectAllClubNotice(){
		return clubNoticeMapper .selectAllClubNotice();
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubNotice getEntityForDB(Map<String, Object> paramMap){
		ClubNotice tmp = new ClubNotice();
		tmp.setNoticeId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectSingleClubNotice(tmp);
	}
	
	public Integer queryCount(Map<String, Object> paramMap){
		return clubNoticeMapper.queryCount(paramMap);
	}

	public List<ClubNotice> selectClubNoticesInfo(Map<String, Object> initParam) {
		return clubNoticeMapper.selectClubNoticesInfo(initParam);
	}

	public void delClubNoticeAll(List<ClubNotice> delClubNotice) {
		clubNoticeMapper.delClubNoticeAll(delClubNotice);
	}

	public void updateClubNoticeByKeyAll(ClubNotice clubNotices) {
		clubNoticeMapper.updateClubNoticeByKeyAll(clubNotices);
	}
}