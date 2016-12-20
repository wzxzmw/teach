package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.sqlmap.ArenaCompetitionMapper;


@Repository
public class ArenaCompetitionDao {

	@Autowired
	private ArenaCompetitionMapper arenaCompetitionMapper;
	
	
	public void insertArenaCompetition(ArenaCompetition arenaCompetition){
		arenaCompetitionMapper .insertArenaCompetition(arenaCompetition);
	}
	
	public void deleteArenaCompetition(ArenaCompetition arenaCompetition){
		arenaCompetitionMapper .deleteArenaCompetition(arenaCompetition);
	}
	
	public void updateArenaCompetitionByKey(ArenaCompetition arenaCompetition){
		arenaCompetitionMapper .updateArenaCompetitionByKey(arenaCompetition);
	}
	
	public List<ArenaCompetition> selectSingleArenaCompetition(ArenaCompetition arenaCompetition){
		return arenaCompetitionMapper .selectSingleArenaCompetition(arenaCompetition);
	}
	
	public ArenaCompetition selectArenaCompetition(ArenaCompetition arenaCompetition){
		List<ArenaCompetition> arenaCompetitionList = arenaCompetitionMapper .selectSingleArenaCompetition(arenaCompetition);
		if(arenaCompetitionList == null || arenaCompetitionList .size() == 0){
			return null;
		}
		return arenaCompetitionList .get(0);
	}
	
	public List<ArenaCompetition> selectAllArenaCompetition(){
		return arenaCompetitionMapper .selectAllArenaCompetition();
	}
	

	/**
	 * 推荐 分页查询count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public Integer queryCountRecommend(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryCountRecommend(paramMap);
	}
	/**
	 * 推荐 分页查询 redis获取id
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<Integer> queryByPagReidsRecommend(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPagReidsRecommend(paramMap);
	}
	
	/**
	 * 分页查询 赛事对象
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<ArenaCompetition> queryByPage(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPage(paramMap);
	}
	/**
	 * 分页查询count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public Integer queryCount(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryCount(paramMap);
	}
	
	/**
	 * 我关注的俱乐部赛事：分页查询 赛事对象
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<ArenaCompetition> queryByPagReidsMyFollow(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPagReidsMyFollow(paramMap);
	}
	/**
	 * 我关注的俱乐部赛事：分页查询count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public Integer queryCountMyFollow(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryCountMyFollow(paramMap);
	}
	
	/**
	 * 我关注的俱乐部赛事：分页查询 赛事对象
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<ArenaCompetition> queryByPagReidsSearch(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPagReidsSearch(paramMap);
	}
	/**
	 * 我关注的俱乐部赛事：分页查询count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public Integer queryCountSearch(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryCountSearch(paramMap);
	}
	
	
	
	/**
	 * 最热 分页查询 redis获取id
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<Integer> queryByPagReidsHot(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPagReidsHot(paramMap);
	}
	
	/**
	 * 我关注的俱乐部赛事：分页查询count
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public Integer queryCountByCenterAttention(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryCountByCenterAttention(paramMap);
	}
	
	
	
	/**
	 * 最热 分页查询 redis获取id
	 * @param paramMap
	 * @return
	 * @author 			lw
	 * @date			2016年7月4日  下午4:20:47
	 */
	public List<Integer> queryByPagReidsCenterAttention(Map<String, Object> paramMap){
		return arenaCompetitionMapper .queryByPagReidsCenterAttention(paramMap);
	}
	
	
	
	
	/**
	 * redis组件 反射调用获取单表数据
	 * @param queryParam	查询参数
	 * @return
	 * @author 				lw
	 * @date				2016年6月21日  下午7:24:07
	 */
	public Object getEntityForDB(Map<String,Object> queryParam) {
		ArenaCompetition tmp = new ArenaCompetition();
		tmp.setCompId(Integer.valueOf(queryParam.get("id_key").toString()));
		return this.selectArenaCompetition(tmp);
	}

	public void updateAddNumber(ArenaCompetition comp) {
		this.arenaCompetitionMapper.updateAddNumber(comp);
	}
	
	/**
	 * 根据club_id 查询是否有赛事
	 */
	public ArenaCompetition queryArenaCompetitionByClubId(Integer clubId){
		return arenaCompetitionMapper.queryArenaCompetitionByClubId(clubId);
	}
}