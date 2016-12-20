package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.ClubRemindDao;
import com.seentao.stpedu.common.dao.ClubTopicDao;
import com.seentao.stpedu.common.entity.ClubTopic;

/** 
* @ClassName: ClubTopicService 
* @Description: 俱乐部话题服务
* @author liulin
* @date 2016年6月26日 下午10:47:46 
*/
@Service
public class ClubTopicService{
	
	@Autowired
	private ClubTopicDao clubTopicDao;
	
	/** 
	* @Title: getClubTopic 
	* @Description: 根据俱乐部ID获得最热话题ID列表
	* @param clubId
	* @return List<ClubTopic>
	* @author liulin
	* @date 2016年6月26日 下午10:51:57
	*/
	public QueryPage<ClubTopic> getHotClubTopic(Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(clubId != null){
			paramMap.put("clubId", clubId);
		}
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubTopic> queryPage = QueryPageComponent.queryPageSimpleDefault(limit, start, paramMap, ClubTopic.class );
		if(queryPage.getCount() > 0){
			queryPage.setList(clubTopicDao.selectHotClubTopicByClubId(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}
	
	/** 
	* @Title: getNewClubTopic 
	* @Description: 根据俱乐部ID获得最新话题ID列表
	* @param clubId
	* @param start
	* @param limit
	* @return  参数说明 
	* @return QueryPage<ClubTopic>
	* @author liulin
	* @date 2016年6月28日 上午10:37:36
	*/
	public QueryPage<ClubTopic> getNewClubTopic(Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(clubId != null){
			paramMap.put("clubId", clubId);
		}
		paramMap.put("isDelete", 0);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubTopic> queryPage = QueryPageComponent.queryPageSimpleDefault(limit, start, paramMap, ClubTopic.class );
		if(queryPage.getCount() > 0){
			queryPage.setList(clubTopicDao.selectNewClubTopicByClubId(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getBackClubTopic 
	* @Description: 俱乐部后台管理中的话题管理列表页调用
	* @param paramMap
	* @param start
	* @param limit
	* @return  参数说明 
	* @return QueryPage<ClubTopic>
	* @author liulin
	* @date 2016年6月28日 下午3:31:22
	*/
	public QueryPage<ClubTopic> getBackClubTopic(Map<String, Object> paramMap, Integer start, Integer limit) {
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubTopic> queryPage = QueryPageComponent.queryPageDefaultExecute(limit, start, paramMap, ClubTopic.class, "countBackClubTopic");
		if(queryPage.getCount() > 0){
			queryPage.setList(clubTopicDao.selectBackClubTopicByClubId(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	/** 
	* @Title: getCenterClubTopic 
	* @Description: 个人中心，通过人员id(createUserId)获取某个人员的话题
	* @param memberId
	* @param start
	* @param limit
	* @return  参数说明 
	* @return QueryPage<ClubTopic>
	* @author liulin
	* @date 2016年6月28日 下午4:21:41
	*/
	public QueryPage<ClubTopic> getCenterClubTopic(Integer memberId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(memberId != null){
			paramMap.put("createUserId", memberId);
		}
		paramMap.put("isDelete", 0);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubTopic> queryPage = QueryPageComponent.queryPageSimpleDefault(limit, start, paramMap, ClubTopic.class );
		if(queryPage.getCount() > 0){
			queryPage.setList(clubTopicDao.selectCenterClubTopicByClubId(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}
	
	public ClubTopic selectSingleClubTopic(ClubTopic clubTopic){
		List<ClubTopic> clubTopicList = clubTopicDao .selectClubTopic(clubTopic);
		if(clubTopicList == null || clubTopicList .size() == 0){
			return null;
		}
		return clubTopicList .get(0);
	}
	
	public void updateClubTopicByKey(ClubTopic clubTopic){
		clubTopicDao.updateClubTopicByKey(clubTopic);
	}

	/** 
	* @Title: addTopic 
	* @Description: 添加话题 
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param topicTitle	标题
	* @param topicContent	内容
	* @param isTop	是否置顶
	* @return int
	* @author liulin
	* @date 2016年6月28日 下午8:42:29
	*/
	public int addTopic(Integer userId, Integer clubId, String topicTitle, String topicContent, Integer isTop) {
		ClubTopic topic = new ClubTopic();
		topic.setTitle(topicTitle);
		topic.setContent(topicContent);
		topic.setCreateUserId(userId);
		long currentTime = System.currentTimeMillis()/1000;
		Integer createTime = Integer.parseInt(String.valueOf(currentTime));
		topic.setCreateTime(createTime);
		topic.setClubId(clubId);
		topic.setPraiseNum(0);
		topic.setCommentNum(0);
		topic.setIsDelete(0);
		topic.setIsTop(isTop);
		topic.setIsFrozen(0);
		clubTopicDao.insertClubTopic(topic);
		return topic.getTopicId();
	}

	public void delClubTopicAll(List<ClubTopic> delClubTopic) {
		clubTopicDao.delClubTopicAll(delClubTopic);
	}

	public void updateClubTopicByKeyAll(ClubTopic clubTopics) {
		clubTopicDao.updateClubTopicByKeyAll(clubTopics);
	}

}