package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ClubMain;

public interface ClubMainMapper {

	public abstract int insertClubMain(ClubMain clubMain);
	
	public abstract void deleteClubMain(ClubMain clubMain);
	
	public abstract void updateClubMainByKey(ClubMain clubMain);
	
	public abstract List<ClubMain> selectSingleClubMain(ClubMain clubMain);
	
	public abstract List<ClubMain> selectAllClubMain();

	public abstract List<ClubMain> selectAllClubMainCount(Map<String, Object> paramMap);

	public abstract List<ClubMain> selectClubMainInfo(ClubMain clubMain);

	public abstract Integer queryCountclubmain(Map<String, Object> paramMap);

	public abstract List<ClubMain> queryByAgeclubmain(Map<String, Object> paramMap);

	public abstract Integer queryClubCount(Map<String, Object> paramMap);

	public abstract List<ClubMain> queryClubCountInfo(Map<String, Object> paramMap);

	public abstract Integer queryClubNewCount(Map<String, Object> paramMap);

	public abstract List<ClubMain> queryClubCountNewInfo(Map<String, Object> paramMap);

	public abstract Integer queryCountclubmatch(Map<String, Object> paramMap);

	public abstract List<ClubMain> queryByclubmainmatch(Map<String, Object> paramMap);

	public abstract Integer queryCountclubfollow(Map<String, Object> paramMap);

	public abstract List<ClubMain> queryByclubmainfollow(Map<String, Object> paramMap);

	public abstract List<ClubMain> selectClubInfo(ClubMain clubMain);
	
	
}