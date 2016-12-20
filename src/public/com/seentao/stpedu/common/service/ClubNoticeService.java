package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.dao.ClubNoticeDao;
import com.seentao.stpedu.common.entity.ClubNotice;

@Service
public class ClubNoticeService{
	
	@Autowired
	private ClubNoticeDao clubNoticeDao;
	
	public void updateClubNoticeByKey(ClubNotice clubNotice){
		clubNoticeDao.updateClubNoticeByKey(clubNotice);
	}

	/** 
	* @Title: getClubNoticesInfo 
	* @Description: 根据clubId获得通知
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param start	起始数
	* @param limit	每页数量
	* @return QueryPage<ClubNotice>
	* @author liulin
	* @date 2016年6月29日 下午9:06:31
	*/
	public QueryPage<ClubNotice> getClubNoticesInfo(Integer userId, Integer clubId, Integer start, Integer limit) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clubId", clubId);
		paramMap.put("isDelete", 0);
		//缺省分页：通过反射mybatis不能级联查询
		QueryPage<ClubNotice> queryPage = QueryPageComponent.queryPageSimpleDefault(limit, start, paramMap, ClubNotice.class);
		if(queryPage.getCount() > 0){
			queryPage.setList(clubNoticeDao.selectClubNoticesInfo(queryPage.getInitParam()));
			queryPage.success();
		}
		return queryPage;
	}

	public ClubNotice getClubNotice(ClubNotice clubNotice) {
		return clubNoticeDao.selectSingleClubNotice(clubNotice);
	}

	/** 
	* @Title: addNotice 
	* @Description: 添加俱乐部通知
	* @param userId	用户ID
	* @param clubId	俱乐部ID
	* @param noticeTitle	通知标题
	* @param noticeContent	通知内容
	* @param isTop	是否置顶
	* @return Integer
	* @author liulin
	* @date 2016年6月29日 下午9:06:17
	*/
	public Integer addNotice(Integer userId, Integer clubId, String noticeTitle, String noticeContent, Integer isTop) {
		ClubNotice notice = new ClubNotice();
		notice.setTitle(noticeTitle);
		notice.setContent(noticeContent);
		notice.setCreateTime(Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000)));
		notice.setCreateUserId(userId);
		notice.setIsTop(isTop);
		notice.setClubId(clubId);
		notice.setIsDelete(0);
		clubNoticeDao.insertClubNotice(notice);
		return notice.getNoticeId();
	}

	public void delClubNoticeAll(List<ClubNotice> delClubNotice) {
		clubNoticeDao.delClubNoticeAll(delClubNotice);
	}

	public void updateClubNoticeByKeyAll(ClubNotice clubNotices) {
		clubNoticeDao.updateClubNoticeByKeyAll(clubNotices);
	}
}