package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.sqlmap.ArenaGuessTopicMapper;


@Repository
public class ArenaGuessTopicDao {

	@Autowired
	private ArenaGuessTopicMapper arenaGuessTopicMapper;
	
	
	public void insertArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		arenaGuessTopicMapper .insertArenaGuessTopic(arenaGuessTopic);
	}
	
	public void deleteArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		arenaGuessTopicMapper .deleteArenaGuessTopic(arenaGuessTopic);
	}
	
	public void updateArenaGuessTopicByKey(ArenaGuessTopic arenaGuessTopic){
		arenaGuessTopicMapper .updateArenaGuessTopicByKey(arenaGuessTopic);
	}
	
	public List<ArenaGuessTopic> selectSingleArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		return arenaGuessTopicMapper .selectSingleArenaGuessTopic(arenaGuessTopic);
	}
	
	public ArenaGuessTopic selectArenaGuessTopic(ArenaGuessTopic arenaGuessTopic){
		List<ArenaGuessTopic> arenaGuessTopicList = arenaGuessTopicMapper .selectSingleArenaGuessTopic(arenaGuessTopic);
		if(arenaGuessTopicList == null || arenaGuessTopicList .size() == 0){
			return null;
		}
		return arenaGuessTopicList .get(0);
	}
	
	public List<ArenaGuessTopic> selectAllArenaGuessTopic(){
		return arenaGuessTopicMapper .selectAllArenaGuessTopic();
	}
	
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public Object getEntityForDB(Map<String, Object> paramMap){
		ArenaGuessTopic tmp = new ArenaGuessTopic();
		tmp.setTopicId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectArenaGuessTopic(tmp);
	}
	
	
	
	/**
	 * 分页 count
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:10
	 */
	public Integer queryCount(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryCount(paramMap);
	}
	public List<Integer> queryByPageids(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryByPageids(paramMap);
	}

	/**
	 * 分页内容
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:39
	 */
	public  List<ArenaGuessTopic> queryByPage(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryByPage(paramMap);
	}
	
	
	
	/**
	 * 我的竞猜分页 count
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:10
	 */
	public Integer queryCountUser(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryCountUser(paramMap);
	}
	public List<Integer> queryByPageidsUser(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryByPageidsUser(paramMap);
	}
	
	/**
	 * 我的竞猜分页内容
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:39
	 */
	public  List<ArenaGuessTopic> queryByPageUser(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryByPageUser(paramMap);
	}
	
	
	/**
	 * 我的竞猜分页 count   根据下注用户id 查询 我参数的竞猜话题
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:10
	 */
/*	public Integer queryCountMy(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryCountMy(paramMap);
	}*/
	
	/**
	 * 分页内容	根据下注用户id 查询 我参数的竞猜话题
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月27日  下午4:47:39
	 */
/*	public  List<ArenaGuessTopic> queryByPageByMy(Map<String, Object> paramMap) {
		return arenaGuessTopicMapper.queryByPageByMy(paramMap);
	}*/
	
	
	

	/**
	 * 竞猜话题人数加一
	 * @param quizTopicId	竞猜话题id
	 * @author 				lw
	 * @date				2016年6月25日  上午11:55:23
	 */
	public void addOneGuessNum(Integer quizTopicId) {
		arenaGuessTopicMapper.addOneGuessNum(quizTopicId);
	}

	/**
	 * 关闭竞猜话题
	 * @param topic
	 * @author 			lw
	 * @date			2016年6月28日  上午11:21:47
	 */
	public void closeArenaGuessTopic(ArenaGuessTopic topic) {
		arenaGuessTopicMapper.closeArenaGuessTopic(topic);
		
	}

	/**
	 * 竞猜话题人数加1，竞猜数量+1
	 * @param quizTopicId
	 * @author 			lw
	 * @date			2016年7月1日  下午2:19:28
	 */
	public void addOneGuessNumAndPsersion(Integer quizTopicId) {
		arenaGuessTopicMapper.addOneGuessNumAndPsersion(quizTopicId);
	}

	/**
	 * 竞猜话题人员加1
	 * @param quizTopicId
	 * @author 			lw
	 * @date			2016年7月6日  下午2:44:35
	 */
	public void addPersonNum(Integer quizTopicId) {
		arenaGuessTopicMapper.addPersonNum(quizTopicId);
	}
}