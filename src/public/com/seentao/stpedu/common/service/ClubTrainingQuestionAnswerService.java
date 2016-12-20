package com.seentao.stpedu.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ClubTrainingQuestionAnswerDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ClubTrainingQuestionAnswerService{
	
	@Autowired
	private ClubTrainingQuestionAnswerDao clubTrainingQuestionAnswerDao;
	@Autowired
	private ClubTrainingQuestionService clubQuestionService;
	@Autowired
	private CenterUserService userService; 
	
	
	public List<ClubTrainingQuestionAnswer> selectSingleClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		return clubTrainingQuestionAnswerDao .selectSingleClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
	}
	public ClubTrainingQuestionAnswer getClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer) {
		List<ClubTrainingQuestionAnswer> clubTrainingQuestionAnswerList = clubTrainingQuestionAnswerDao .selectSingleClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
		if(clubTrainingQuestionAnswerList == null || clubTrainingQuestionAnswerList .size() <= 0){
			return null;
		}
		
		return clubTrainingQuestionAnswerList.get(0);
	}
	
	public void insertClubTrainingQuestionAnswer(ClubTrainingQuestionAnswer clubTrainingQuestionAnswer){
		clubTrainingQuestionAnswerDao .insertClubTrainingQuestionAnswer(clubTrainingQuestionAnswer);
	}

	public void updateClubTrainingQuestionAnswerByKey(ClubTrainingQuestionAnswer upClubTrainingQuestionAnswer) {
		clubTrainingQuestionAnswerDao.updateClubTrainingQuestionAnswerByKey(upClubTrainingQuestionAnswer);
	}

	
	/**
	 * 添加 回复信息 并把回复问题数量+1 并删除缓存
	 * @param questionId		问题id
	 * @param userId			用户id
	 * @param answerBody		回复内容
	 * @return
	 * @author 					lw
	 * @date					2016年6月29日  下午1:56:06
	 */
	public JSONObject addClubAnswer(Integer questionId, Integer userId, String answerBody) {
		
		try {
			//	1. 添加回复
			ClubTrainingQuestionAnswer club = new ClubTrainingQuestionAnswer();
			club.setQuestionId(questionId);
			club.setCreateUserId(userId);
			club.setCreateTime(TimeUtil.getCurrentTimestamp());
			club.setPraiseNum(0);
			club.setContent(answerBody);
			club.setIsDelete(GameConstants.NO_DEL);
			this.insertClubTrainingQuestionAnswer(club);
			
			//	2. 问题回复数量+1 并删除缓存
			clubQuestionService.addAnswerNum(questionId);
			
			//	3. 用户回复信息+1 ，并删除用户缓存
			userService.addAnswerNum(userId);
			LogUtil.info(this.getClass(), "addClubAnswer", String.valueOf(GameConstants.SUCCESS_INSERT));
			return Common.getReturn(AppErrorCode.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "addClubAnswer", AppErrorCode.ERROR_QUESTIONS_INSERT);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_QUESTIONS_INSERT);
		}
	}
	public void delClubTrainingQuestionAnswerAll(List<ClubTrainingQuestionAnswer> delClubTrainingQuestionAnswer) {
		clubTrainingQuestionAnswerDao.delClubTrainingQuestionAnswerAll(delClubTrainingQuestionAnswer);
	}
	
	/**
	 * 移动端：获取问题回复信息
	 * @param userId
	 * @param start
	 * @param limit
	 * @param classType
	 * @param questionId
	 * @param inquireType
	 * @return
	 * @author 			lw
	 * @param _type 
	 * @date			2016年7月18日  下午11:05:18
	 */
	public String findQuestionAnswer(Integer start, Integer limit, Integer questionId, Integer inquireType, String _type,String answerId) {
		
		//	1. 问题的回复列表信息 
		if(GameConstants.QUESTION_ANSWER_TYPE == inquireType){
			
			//	1.1 分页查询回复信息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("questionId", questionId);
			param.put("isDelete", GameConstants.NO_DEL);
			QueryPage<ClubTrainingQuestionAnswer> page = QueryPageComponent.queryPageByRedisSimple(limit, start, param, ClubTrainingQuestionAnswer.class);
			if(page.getState()){
				String redisTmp = null;
				CenterUser user = null;
				PublicPicture pic = null;
				
				//	1.2 参数组装
				for(ClubTrainingQuestionAnswer en : page.getList()){
					
					//	1.2.1 获取用户真实姓名
					redisTmp = RedisComponent.findRedisObject(en.getCreateUserId(), CenterUser.class);
					if(!StringUtil.isEmpty(redisTmp)){
						user = JSONObject.parseObject(redisTmp, CenterUser.class);
						en.setaCreaterName(user.getRealName());
						en.setaCreaterNickname(user.getNickName());
						//	1.2.1.1 获取回复人员头像链接
						redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(redisTmp)){
							pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
							/*
							 * 压缩图片
							 */
							en.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) ==true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
						//app端把富文本进行过滤
						if(GameConstants.TYPE_APP.equals(_type) && en.getContent() != null){
							en.setAnswerBodyFilterHtml(RichTextUtil.delHTMLTag(en.getContent()));
							en.setContent("");
							JSONArray nullArray = new JSONArray();
							en.setImgs(nullArray);
							en.setLinks(nullArray);
						}
					}
				}
			}
			return page.getMessageJSONObject("answers").toJSONString();
		}else if(2 == inquireType){
			//1.1 分页查询回复信息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("questionId", questionId);
			param.put("isDelete", GameConstants.NO_DEL);
			param.put("answerId", Integer.valueOf(answerId));
			QueryPage<ClubTrainingQuestionAnswer> page = QueryPageComponent.queryPageByRedisSimple(1, 0, param, ClubTrainingQuestionAnswer.class);
			if(page.getState()){
				String redisTmp = null;
				CenterUser user = null;
				PublicPicture pic = null;
				JSONObject rich = null;
				
				//	1.2 参数组装
				for(ClubTrainingQuestionAnswer en : page.getList()){
					
					//	1.2.1 获取用户真实姓名
					redisTmp = RedisComponent.findRedisObject(en.getCreateUserId(), CenterUser.class);
					if(!StringUtil.isEmpty(redisTmp)){
						user = JSONObject.parseObject(redisTmp, CenterUser.class);
						en.setaCreaterName(user.getRealName());
						en.setaCreaterNickname(user.getNickName());
						//	1.2.1.1 获取回复人员头像链接
						redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(redisTmp)){
							pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
							en.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
					}
					
					//	1.2.2 移动端参数变化
					if(GameConstants.TYPE_APP.equals(_type) && en.getContent() != null){
						rich = RichTextUtil.parseRichText(en.getContent());
						if(rich != null){
							en.setContent(rich.getString("content"));
							en.setLinks(rich.getJSONArray("links"));
							en.setImgs(rich.getJSONArray("imgs"));
						}
					}
				}
				
			}
			return page.getMessageJSONObject("answers").toJSONString();
		}
		LogUtil.error(this.getClass(), "findQuestionAnswer" , AppErrorCode.ERROR_ANSWERS_APP_PARAM);
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ANSWERS_APP_PARAM.toString()).toJSONString();
	}
	
}