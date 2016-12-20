package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.sqlmap.ClubMainMapper;


@Repository
public class ClubMainDao {

	@Autowired
	private ClubMainMapper clubMainMapper;


	public int insertClubMain(ClubMain clubMain){
		clubMainMapper .insertClubMain(clubMain);
		return clubMain.getClubId();
	}

	public void deleteClubMain(ClubMain clubMain){
		clubMainMapper .deleteClubMain(clubMain);
	}

	public void updateClubMainByKey(ClubMain clubMain){
		clubMainMapper .updateClubMainByKey(clubMain);
	}

	public List<ClubMain> selectSingleClubMain(ClubMain clubMain){
		return clubMainMapper .selectSingleClubMain(clubMain);
	}

	public List<ClubMain> selectClubMain(ClubMain clubMain){
		List<ClubMain> clubMainList = clubMainMapper .selectSingleClubMain(clubMain);
		if(clubMainList == null || clubMainList .size() == 0){
			return null;
		}
		return clubMainList;
	}

	public ClubMain selectClubMainEntity(ClubMain clubMain){
		List<ClubMain> clubMainList = clubMainMapper .selectSingleClubMain(clubMain);
		if(clubMainList == null || clubMainList .size() == 0){
			return null;
		}
		return clubMainList.get(0);
	}

	public List<ClubMain> selectAllClubMain(){
		return clubMainMapper .selectAllClubMain();
	}



	/**
	 * redis组件 反射调用获取单表数据
	 * @param queryParam	查询参数
	 * @return
	 * @author 				lw
	 * @date				2016年6月21日  下午7:24:07
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		ClubMain tmp = new ClubMain();
		tmp.setClubId(Integer.valueOf(queryParam.get("id_key").toString()));
		return this.selectClubMainEntity(tmp);
	}
	
	//public ClubMain query
	
	public List<ClubMain> selectAllClubMainCount(Map<String, Object> paramMap) {

		return clubMainMapper.selectAllClubMainCount(paramMap);
	}

	public List<ClubMain> selectClubMainInfo(ClubMain clubMain) {
		
		List<ClubMain> list = clubMainMapper.selectClubMainInfo(clubMain);

		return list;
	}
	
	public Integer queryCountclubmain(Map<String, Object> paramMap) {
		return clubMainMapper.queryCountclubmain(paramMap);
	}

	public List<ClubMain> queryByAgeclubmain(Map<String, Object> paramMap) {
		return clubMainMapper.queryByAgeclubmain(paramMap);
	}
	/**
	 * 查询最热俱乐部信息
	 * @param paramMap
	 * @return
	 */
	public Integer queryClubCount(Map<String, Object> paramMap) {
		return clubMainMapper.queryClubCount(paramMap);
	}

	public List<ClubMain> queryClubCountInfo(Map<String, Object> paramMap) {
		return clubMainMapper.queryClubCountInfo(paramMap);
	}
	/**
	 * 查询最新俱乐部
	 * @param paramMap
	 * @return
	 */
	public Integer queryClubNewCount(Map<String, Object> paramMap) {
		return clubMainMapper.queryClubNewCount(paramMap);
	}

	public List<ClubMain> queryClubCountNewInfo(Map<String, Object> paramMap) {
		return clubMainMapper.queryClubCountNewInfo(paramMap);
	}
	
	/**
	 * 获取赛事俱乐部信息
	 * @param paramMap
	 * @return
	 */
	public Integer queryCountclubmatch(Map<String, Object> paramMap) {
		return clubMainMapper.queryCountclubmatch(paramMap);
	}

	public List<ClubMain> queryByclubmainmatch(Map<String, Object> paramMap) {
		return clubMainMapper.queryByclubmainmatch(paramMap);
	}
	
	/**
	 * 获取关注俱乐部信息
	 * @param paramMap
	 * @return
	 */
	public Integer queryCountclubfollow(Map<String, Object> paramMap) {
		return clubMainMapper.queryCountclubfollow(paramMap);
	}

	public List<ClubMain> queryByclubmainfollow(Map<String, Object> paramMap) {
		return clubMainMapper.queryByclubmainfollow(paramMap);
	}

	public List<ClubMain> selectClubInfo(ClubMain clubMain){
		return clubMainMapper .selectClubInfo(clubMain);
	}
	

}