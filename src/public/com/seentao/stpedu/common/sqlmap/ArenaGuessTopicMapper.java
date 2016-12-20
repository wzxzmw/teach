package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ArenaGuessTopic;

public interface ArenaGuessTopicMapper {

	public abstract void insertArenaGuessTopic(ArenaGuessTopic arenaGuessTopic);
	
	public abstract void deleteArenaGuessTopic(ArenaGuessTopic arenaGuessTopic);
	
	public abstract void updateArenaGuessTopicByKey(ArenaGuessTopic arenaGuessTopic);
	
	public abstract List<ArenaGuessTopic> selectSingleArenaGuessTopic(ArenaGuessTopic arenaGuessTopic);
	
	public abstract List<ArenaGuessTopic> selectAllArenaGuessTopic();

	public abstract void addOneGuessNum(Integer quizTopicId);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ArenaGuessTopic> queryByPage(Map<String, Object> paramMap);

	public abstract Integer queryCountMy(Map<String, Object> paramMap);

	public abstract List<ArenaGuessTopic> queryByPageByMy(Map<String, Object> paramMap);

	public abstract void closeArenaGuessTopic(ArenaGuessTopic topic);

	public abstract void addOneGuessNumAndPsersion(Integer quizTopicId);

	public abstract List<Integer> queryByPageids(Map<String, Object> paramMap);

	public abstract Integer queryCountUser(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageidsUser(Map<String, Object> paramMap);

	public abstract List<ArenaGuessTopic> queryByPageUser(Map<String, Object> paramMap);

	public abstract void addPersonNum(Integer quizTopicId);
	
}