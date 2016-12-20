package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.entity.ClubJoinTraining;

public interface ClubJoinTrainingMapper {

	public abstract void insertClubJoinTraining(ClubJoinTraining clubJoinTraining);
	
	public abstract void deleteClubJoinTraining(ClubJoinTraining clubJoinTraining);
	
	public abstract void updateClubJoinTrainingByKey(ClubJoinTraining clubJoinTraining);
	
	public abstract List<ClubJoinTraining> selectSingleClubJoinTraining(ClubJoinTraining clubJoinTraining);
	
	public abstract List<ClubJoinTraining> selectAllClubJoinTraining();

	public abstract List<ClubJoinTraining> selectClubJoinTrainingByClasdId(ClubJoinTraining clubJoinTraining);

	public abstract Integer selectClubJoinTrainingCount(ClubJoinTraining clubJoinTraining);

	public abstract void updateClubJoinTrainingByKeyAll(List<ClubJoinTraining> upClubJoinTraining);

	public abstract Integer getHasClubJoinTrainingCount(@Param(value="userId")Integer userId);

	public abstract Integer queryClubJoinCountAll(Map<String, Object> paramMap);

	public abstract List<ClubJoinTraining> queryClubJoinByPageAll(Map<String, Object> paramMap);
	/**根据班级id和用户id查询是否存在班级
	 * @param classId
	 * @param userId
	 * @return
	 */
	public abstract ClubJoinTraining queryClubJoinTrainingByClassId(@Param("classId")Integer classId,@Param("userId")Integer userId);

	public abstract void updateClubJoinTrainingUdToCost(ClubJoinTraining addClubJoinTraining);
	
}