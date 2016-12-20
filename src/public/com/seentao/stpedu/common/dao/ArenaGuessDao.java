package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.sqlmap.ArenaGuessMapper;


@Repository
public class ArenaGuessDao {

	@Autowired
	private ArenaGuessMapper arenaGuessMapper;
	
	
	public int insertArenaGuess(ArenaGuess arenaGuess){
		return arenaGuessMapper .insertArenaGuess(arenaGuess);
	}
	
	public void deleteArenaGuess(ArenaGuess arenaGuess){
		arenaGuessMapper .deleteArenaGuess(arenaGuess);
	}
	
	public void updateArenaGuessByKey(ArenaGuess arenaGuess){
		arenaGuessMapper .updateArenaGuessByKey(arenaGuess);
	}
	
	public List<ArenaGuess> selectSingleArenaGuess(ArenaGuess arenaGuess){
		return arenaGuessMapper .selectSingleArenaGuess(arenaGuess);
	}
	
	public ArenaGuess selectArenaGuess(ArenaGuess arenaGuess){
		List<ArenaGuess> arenaGuessList = arenaGuessMapper .selectSingleArenaGuess(arenaGuess);
		if(arenaGuessList == null || arenaGuessList .size() == 0){
			return null;
		}
		return arenaGuessList .get(0);
	}
	
	public List<ArenaGuess> selectAllArenaGuess(){
		return arenaGuessMapper .selectAllArenaGuess();
	}
	
	/**
	 * 默认分页 count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午2:39:11
	 */
	public Integer queryCount(Map<String, Object> paramMap) {
		return arenaGuessMapper.queryCount(paramMap);
	}

	/**
	 * 查询ids去redis中查询
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月30日  下午10:59:26
	 */
	public List<Integer> queryByPageids(Map<String, String> paramMap) {
		return arenaGuessMapper.queryByPageids(paramMap);
	}
	
	/**
	 * 默认分页 count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午2:39:11
	 */
	public Integer queryCountApp(Map<String, Object> paramMap) {
		return arenaGuessMapper.queryCountApp(paramMap);
	}
	
	/**
	 * 查询ids去redis中查询
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月30日  下午10:59:26
	 */
	public List<Integer> queryByPageidsApp(Map<String, String> paramMap) {
		return arenaGuessMapper.queryByPageidsApp(paramMap);
	}
	
	/**
	 * 默认分页 count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午2:39:11
	 */
	public Integer queryCountMy(Map<String, Object> paramMap) {
		return arenaGuessMapper.queryCountMy(paramMap);
	}
	
	/**
	 * 查询ids去redis中查询
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月30日  下午10:59:26
	 */
	public List<Integer> queryByPageidsMy(Map<String, String> paramMap) {
		return arenaGuessMapper.queryByPageidsMy(paramMap);
	}
	
	
	
	/**
	 * 默认分页 实体查询
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午2:39:19
	 */
	public  List<ArenaGuess> queryByPage(Map<String, Object> paramMap) {
		return  arenaGuessMapper.queryByPage(paramMap);
	}
	

	
	/**
	 * redis获取竞猜主题下的所有竞猜对象方法
	 * @param map
	 * @return
	 * @author 			lw
	 * @date			2016年6月24日  下午7:54:58
	 */
	public Object findThemeAllguess(Map<String,Object> map){
		return arenaGuessMapper.findThemeAllguess(map);
	}
	
	
	/**
	 *  redis组件 反射调用获取单表数据
	 * @param paramMap	传入参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  上午10:32:52
	 */
	public Object getEntityForDB(Map<String, Object> paramMap){
		ArenaGuess tmp = new ArenaGuess();
		tmp.setGuessId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectArenaGuess(tmp);
	}

	/**
	 * 修改竞猜状态
	 * @param guessId
	 * @param guessEnd
	 * @author 			lw
	 * @date			2016年6月28日  上午10:41:06
	 */
	public void closeArenaGuess(ArenaGuess guess) {
		
		arenaGuessMapper.closeArenaGuess(guess);
	}
	

	/**
	 * 获取最大投注者的 id 、昵称、金钱
	 * @param integer
	 * @return
	 * @author 			lw
	 * @date			2016年7月1日  下午3:23:35
	 */
	public List<ArenaGuess> findMaxAmountGuess(String string) {
		return arenaGuessMapper.findMaxAmountGuess(string);
	}

	public ArenaGuess getMostArenaGuess() {
		List<ArenaGuess> arenaGuessList = arenaGuessMapper .getMostArenaGuess();
		if(arenaGuessList == null || arenaGuessList .size() == 0){
			return null;
		}
		return arenaGuessList .get(0);
	}

	public List<ArenaGuess> findAllAcountByGuessTopicId(String topicId) {
		return arenaGuessMapper.findAllAcountByGuessTopicId(topicId);
	}

	
	
	
	
	
	
}