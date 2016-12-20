package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.ArenaGuess;

public interface ArenaGuessMapper {

	public abstract int insertArenaGuess(ArenaGuess arenaGuess);
	
	public abstract void deleteArenaGuess(ArenaGuess arenaGuess);
	
	public abstract void updateArenaGuessByKey(ArenaGuess arenaGuess);
	
	public abstract List<ArenaGuess> selectSingleArenaGuess(ArenaGuess arenaGuess);
	
	public abstract List<ArenaGuess> selectAllArenaGuess();

	public abstract Object findThemeAllguess(Map<String, Object> map);

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ArenaGuess> queryByPage(Map<String, Object> paramMap);

	public abstract void closeArenaGuess(ArenaGuess guess);

	public abstract List<Integer> queryByPageids(Map<String, String> paramMap);

	public abstract List<ArenaGuess> findMaxAmountGuess(String topicIds);

	public abstract Integer queryCountApp(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageidsApp(Map<String, String> paramMap);

	public abstract Integer queryCountMy(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageidsMy(Map<String, String> paramMap);

	public abstract List<ArenaGuess> getMostArenaGuess();

	public abstract List<ArenaGuess> findAllAcountByGuessTopicId(String topicId);	
}