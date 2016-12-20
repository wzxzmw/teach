package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachCourseCardDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;
/**
 * teach_course_card 课程卡表基本操作
 */
@Service
public class TeachCourseCardService{
	@Autowired
	private TeachCourseCardDao teachCourseCardDao;
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	@Autowired
	private TeachRelCardCourseService teachRelCardCourseService;
	
	public List<TeachCourseCard> getTeachCourseCard(TeachCourseCard teachCourseCard) {
		List<TeachCourseCard> teachCourseCardList = teachCourseCardDao .selectSingleTeachCourseCard(teachCourseCard);
		if(teachCourseCardList == null || teachCourseCardList .size() <= 0){
			return null;
		}
		return teachCourseCardList;
	}
	
	public TeachCourseCard getTeachCourseCardOne(Integer cardId) {
		TeachCourseCard teachCourseCard = new TeachCourseCard();
		teachCourseCard.setCardId(cardId);
		List<TeachCourseCard> teachCourseCardList = teachCourseCardDao .selectSingleTeachCourseCard(teachCourseCard);
		if(teachCourseCardList == null || teachCourseCardList .size() <= 0){
			return null;
		}else{
			return teachCourseCardList.get(0);
		}
	}
	
	public void updateCourseCard(TeachCourseCard teachCourseCard) {
		teachCourseCardDao.updateTeachCourseCardByKey(teachCourseCard);
	}
	
	public void updateCourseCardByCardId(Integer cardId) {
		teachCourseCardDao.updateCourseCardByCardId(cardId);
	}
	
	/**
	 * 根据课程卡id查询课程卡信息以及章节信息
	 * @param courseCardId
	 * @return
	 */
	@Transactional
	public TeachCourseChapter getCourseCardByCardId(Integer courseCardId) {
		TeachCourseCard teachcoursecard = null;
		TeachCourseChapter teachcoursechapter = null;
		//课程卡缓存
		String cardCache = RedisComponent.findRedisObject(courseCardId, TeachCourseCard.class);
		if(!StringUtil.isEmpty(cardCache)){
			teachcoursecard = JSONObject.parseObject(cardCache,TeachCourseCard.class);
			if(teachcoursecard!=null){
				//章节缓存
				String chapterCache = RedisComponent.findRedisObject(teachcoursecard.getChapterId(), TeachCourseChapter.class);
				teachcoursechapter = JSONObject.parseObject(chapterCache,TeachCourseChapter.class);
				if(teachcoursechapter!=null){
					List<TeachCourseCard> cardList = new ArrayList<TeachCourseCard>();
					cardList.add(teachCourseCardConverts(teachcoursecard));
					for (TeachCourseCard teachCourseCard2 : cardList) {
						if(teachCourseCard2.getStartTime() != null){
							teachCourseCard2.setStrStartTime(String.valueOf(teachCourseCard2.getStartTime()));
						}
						if(teachCourseCard2.getEndTime() != null){
							teachCourseCard2.setStrEndTime(String.valueOf(teachCourseCard2.getEndTime()));
						}
					}
					teachcoursechapter.setCourseCards(cardList);
					//更新课程卡点击次数+1
					Integer cardId = teachcoursecard.getCardId();
					if(cardId!=null){
						teachCourseCardDao.updateCourseCardByCardId(cardId);
						JedisCache.delRedisVal(TeachCourseCard.class.getSimpleName(), String.valueOf(cardId));
					}
				}
			}
		}
		return teachcoursechapter;
	}
	
	//根据章节id获取课程卡信息以及章节信息
	public List<TeachCourseChapter> getCourseCardByChapterId(Integer chapterId) {
		TeachCourseChapter t = new TeachCourseChapter();
		t.setChapterId(chapterId);
		return getCourseCard(t);
	}

	//根据班级id获取课程卡信息以及章节信息
	public List<TeachCourseChapter> getCourseCardByClassId(Integer classId) {
		TeachCourseChapter t = new TeachCourseChapter();
		t.setClassId(classId);
		return getCourseCard(t);
	}
	
	//获取课程卡以及章节信息
	private List<TeachCourseChapter> getCourseCard(TeachCourseChapter t){
		//获取章节下的所有课程卡
		List<TeachCourseChapter> list = teachCourseChapterService.getChapter(t);
		if(null!=list && list.size()!=0){
			for (int i = 0; i < list.size(); i++) {
				TeachCourseChapter tcc = list.get(i);
				TeachCourseCard tcs = new TeachCourseCard();
				tcs.setChapterId(tcc.getChapterId());
				List<TeachCourseCard> tcsList = teachCourseCardDao.selectSingleTeachCourseCard(tcs);
				if(null!=tcsList && tcsList.size()!=0){
					List<TeachCourseCard> resList = new ArrayList<TeachCourseCard>(); 
					for (int j = 0; j < tcsList.size(); j++) {
						TeachCourseCard res = teachCourseCardConverts(tcsList.get(j));
						resList.add(res);
					}
					for (TeachCourseCard teachCourseCard2 : resList) {
						if(teachCourseCard2.getStartTime() != null){
							teachCourseCard2.setStrStartTime(String.valueOf(teachCourseCard2.getStartTime()));
						}
						if(teachCourseCard2.getEndTime() != null){
							teachCourseCard2.setStrEndTime(String.valueOf(teachCourseCard2.getEndTime()));
						}
					}
					tcc.setCourseCards(resList);
				}else{
					List<TeachCourseCard> nullList = new ArrayList<TeachCourseCard>(); 
					tcc.setCourseCards(nullList);
				}
			}
			return list;
		}else{
			LogUtil.error(this.getClass(), "getCourseCard", "章节下没有课程卡");
			return null;
		}
	}
	
	//获取教师名称 学习课程的人数 图片链接 所在学校
	public TeachCourseCard teachCourseCardConverts(TeachCourseCard teachCourseCard){
		CenterUser centerUser = null;
		TeachSchool teachSchool = null;
		//课程图片资源
		String courseImg = Common.getImgUrl(teachCourseCard.getImageId(), BusinessConstant.DEFAULT_IMG_COURSE_CARD[teachCourseCard.getCardType()]);
		//用户信息
		String centerUserCache = RedisComponent.findRedisObject(teachCourseCard.getTeacherId(), CenterUser.class);
		if(!StringUtil.isEmpty(centerUserCache)){
			centerUser = JSONObject.parseObject(centerUserCache,CenterUser.class);
		}
		if(centerUser!=null){
			String teacherImg = Common.getImgUrl(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
			//学校信息
			String schoolCache = RedisComponent.findRedisObject(centerUser.getSchoolId(), TeachSchool.class);
			if(!StringUtil.isEmpty(schoolCache)){
				teachSchool = JSONObject.parseObject(schoolCache,TeachSchool.class);
			}
			teachCourseCard.setTeacherHeadLink(Common.checkPic(teacherImg) == true ? teacherImg+ActiveUrl.HEAD_MAP:teacherImg);
		}
		/*
		 * 压缩图片
		 */
		teachCourseCard.setCcLink(Common.checkPic(courseImg)== true ? courseImg+ActiveUrl.HEAD_MAP:courseImg);
		teachCourseCard.setSchoolName(teachSchool==null?"":teachSchool.getSchoolName());
		//课程卡点击次数
		teachCourseCard.setLearningNum(teachCourseCard.getClickCount()==null?0:teachCourseCard.getClickCount());
		teachCourseCard.setTeacherName(centerUser==null?"":centerUser.getRealName());
		//比赛运行年是否为初始年
		if(null == teachCourseCard.getMatchId() || null == teachCourseCard.getMatchYear()){
			teachCourseCard.setIsTheFirstYear(0);
		}else{
			if(teachCourseCard.getMatchYear() == 1){
				teachCourseCard.setIsTheFirstYear(1);
			}else{
				teachCourseCard.setIsTheFirstYear(0);
			}
		}
		return teachCourseCard;
	}
	
	/**
	 * 查询章节信息以及课程卡信息
	 * @param cardId
	 * @return
	 */
	public Map<String, Object> getCardAndChapterByCardId(Integer cardId) {
		//课程卡信息
		String cardCache = RedisComponent.findRedisObject(cardId, TeachCourseCard.class);
		TeachCourseCard teachcoursecard = JSONObject.parseObject(cardCache,TeachCourseCard.class);
		TeachCourseChapter teachcoursechapter = null;
		if(null!=teachcoursecard){
			//章节信息
			String chapterCache = RedisComponent.findRedisObject(teachcoursecard.getChapterId(), TeachCourseChapter.class);
			teachcoursechapter = JSONObject.parseObject(chapterCache,TeachCourseChapter.class);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("chapterId", teachcoursechapter==null?null:teachcoursechapter.getChapterId());
		map.put("chapterName", teachcoursechapter==null?"":teachcoursechapter.getChapterName());
		map.put("cardId", teachcoursecard==null?null:teachcoursecard.getCardId());
		map.put("cardTitle", teachcoursecard==null?"":teachcoursecard.getCardTitle());
		map.put("courseCardType", teachcoursecard==null?"":teachcoursecard.getCardType());
		return map;
	}
	/**
	 * @return 
	 */
	public Integer insertTeachCourseCard(TeachCourseCard teachCourseCard){
		return teachCourseCardDao.insertTeachCourseCard(teachCourseCard);
	}
	
	public TeachCourseCard getSingleTeachCourseCard(TeachCourseCard teachCourseCard){
		return teachCourseCardDao.selectTeachCourseCard(teachCourseCard);
	}

	/** 
	* @Title: getLastTeachCourseCardByChapterId 
	* @Description: 获取这一章节最后的课程卡
	* @param chapterId
	* @return TeachCourseCard
	* @author liulin
	* @date 2016年7月21日 上午11:46:33
	*/
	public TeachCourseCard getLastTeachCourseCardByChapterId(Integer chapterId) {
		TeachCourseCard teachCourseCard = new TeachCourseCard();
		teachCourseCard.setChapterId(chapterId);
		teachCourseCard.setCardType(4);
		return teachCourseCardDao.getLastTeachCourseCardByChapterId(teachCourseCard);
	}

	/**
	 * 根据章节id 查询课程卡对象
	 * 
	 * @param cIds		章节ids
	 * @return
	 * @author 			lw
	 * @date			2016年7月28日  下午6:16:12
	 */
	public List<TeachCourseCard> selectByChapterIds(String cIds) {
		return teachCourseCardDao.selectByChapterIds(cIds);
	}

	public List<TeachCourseCard> selectByChapterId(TeachCourseCard parm) {
		return teachCourseCardDao.selectByChapterId(parm);
	}
	
	
}