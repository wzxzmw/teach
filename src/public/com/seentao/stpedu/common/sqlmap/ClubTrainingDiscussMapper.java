package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubTrainingDiscuss;
import java.util.List;
import java.util.Map;

public interface ClubTrainingDiscussMapper {

	public abstract void insertClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss);
	
	public abstract void deleteClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss);
	
	public abstract void updateClubTrainingDiscussByKey(ClubTrainingDiscuss clubTrainingDiscuss);
	
	public abstract List<ClubTrainingDiscuss> selectSingleClubTrainingDiscuss(ClubTrainingDiscuss clubTrainingDiscuss);
	
	public abstract List<ClubTrainingDiscuss> selectAllClubTrainingDiscuss();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ClubTrainingDiscuss> queryByPage(Map<String, Object> paramMap);

	public abstract void delClubTrainingDiscussAll(List<ClubTrainingDiscuss> delClubTrainingDiscuss);

	public abstract Integer queryByCountnew(Map<String, Object> paramMap);

	public abstract List<ClubTrainingDiscuss> queryByPagenew(Map<String, Object> paramMap);

	public abstract Integer queryByCountOld(Map<String, Object> paramMap);

	public abstract List<ClubTrainingDiscuss> queryByPageOld(Map<String, Object> paramMap);

	public abstract Integer queryCountOne(Map<String, Object> paramMap);

	public abstract List<ClubTrainingDiscuss> queryPageOne(Map<String, Object> paramMap);
	
}