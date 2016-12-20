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
import com.seentao.stpedu.common.dao.TeachQuestionAnswerDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.entity.TeachQuestionAnswer;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class TeachQuestionAnswerService{
	
	@Autowired
	private TeachQuestionAnswerDao teachQuestionAnswerDao;
	@Autowired
	private TeachQuestionService questionService;
	@Autowired
	private CenterUserService userService; 
	
	
	public List<TeachQuestionAnswer> selectSingleTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		return teachQuestionAnswerDao .selectSingleTeachQuestionAnswer(teachQuestionAnswer);
	}
	
	public String getTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer) {
		
		List<TeachQuestionAnswer> teachQuestionAnswerList = teachQuestionAnswerDao .selectSingleTeachQuestionAnswer(teachQuestionAnswer);
		if(teachQuestionAnswerList == null || teachQuestionAnswerList .size() <= 0){
			return null;
		}
		return JSONArray.toJSONString(teachQuestionAnswerList);
	}
	
	public TeachQuestionAnswer getTeachQuestionAll(TeachQuestionAnswer teachQuestionAnswer){
		List<TeachQuestionAnswer> teachQuestionAnswerList = teachQuestionAnswerDao .selectSingleTeachQuestionAnswer(teachQuestionAnswer);
		if(teachQuestionAnswerList == null || teachQuestionAnswerList .size() <= 0){
			return null;
		}
		return teachQuestionAnswerList.get(0);
	}
	
	public void insertTeachQuestionAnswer(TeachQuestionAnswer teachQuestionAnswer){
		teachQuestionAnswerDao .insertTeachQuestionAnswer(teachQuestionAnswer);
	}
	
	public void updateTeachQuestionAnswerByKey(TeachQuestionAnswer teachQuestionAnswer){
		teachQuestionAnswerDao.updateTeachQuestionAnswerByKey(teachQuestionAnswer);
	}
	
	/**
	 * 问题回复信息校验 问题和班级的存在
	 * @param questionId	问题id
	 * @return
	 * @author 				lw
	 * @param userId 
	 * @date				2016年6月23日  下午2:01:22
	 */
	public TeachQuestion checkQuestionAndClass(Integer questionId) {
		
		String questionRedis = RedisComponent.findRedisObject(questionId, TeachQuestion.class);
		if(!StringUtil.isEmpty(questionRedis)){
			TeachQuestion question = JSONObject.parseObject(questionRedis, TeachQuestion.class);
			return question;
		}
		return null;
	}

	

	/**
	 * 添加教学问题回复信息
	 * @param questionId	回复id
	 * @param userId		用户id
	 * @param answerBody	回复内容
	 * @return
	 * @author 			lw
	 * @date			2016年6月29日  上午11:23:15
	 */
	public JSONObject addQuestionAnswer(Integer questionId, Integer userId, String answerBody) {
		
		try {
			//	1. 添加班级问题
			TeachQuestionAnswer  answer = new TeachQuestionAnswer();
			answer.setQuestionId(questionId);
			answer.setCreateUserId(userId);
			answer.setCreateTime(TimeUtil.getCurrentTimestamp());
			answer.setContent(answerBody);
			answer.setPraiseNum(0);
			answer.setIsDelete(GameConstants.NO_DEL);
			this.insertTeachQuestionAnswer(answer);
			
			//	2. 问题回复加1 并删除缓存
			questionService.addAnswerNum(questionId);
			
			//	3. 用户 回复信息加一,并删除缓存
			userService.addAnswerNum(userId);
			LogUtil.info(this.getClass(), "addQuestionAnswer", GameConstants.SUCCESS_INSERT);
			return Common.getReturn(AppErrorCode.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "addQuestionAnswer", String.valueOf(AppErrorCode.ERROR_SUBMITANSWER_QUESTION_INSERT),e);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_SUBMITANSWER_QUESTION_INSERT);
		}
	}

	public void delTeachQuestionAnswerAll(List<TeachQuestionAnswer> delTeachQuestionAnswer) {
		teachQuestionAnswerDao.delTeachQuestionAnswerAll(delTeachQuestionAnswer);
	}

	
	/**
	 * 移动端：获取提问的回复信息
	 * @param userId		用户id
	 * @param start
	 * @param limit
	 * @param classType		班级类型
	 * @param questionId	问题id
	 * @param inquireType	请求类型
	 * @return
	 * @author 				lw
	 * @param _type 
	 * @date				2016年7月18日  下午10:47:00
	 */
	public String findQuestionAnswer(Integer start, Integer limit, Integer questionId, Integer inquireType, String _type,String answerId) {
		
		//	1. 问题的回复列表信息
		if(GameConstants.QUESTION_ANSWER_TYPE == inquireType){
			
			//	1.1 分页获取回复消息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("questionId", questionId);
			param.put("orderBy", "create_time");
			param.put("isDelete", GameConstants.NO_DEL);
			QueryPage<TeachQuestionAnswer> page = QueryPageComponent.queryPageByRedisSimple(limit, start, param, TeachQuestionAnswer.class);
			if(page.getState()){
				String redisTmp = null;
				CenterUser user = null;
				PublicPicture pic = null;
				
				//	1.2 参数组装
				for(TeachQuestionAnswer ans : page.getList()){
					
					//	1.2.1 获取用户真实姓名
					redisTmp = RedisComponent.findRedisObject(ans.getCreateUserId(), CenterUser.class);
					if(!StringUtil.isEmpty(redisTmp)){
						user = JSONObject.parseObject(redisTmp, CenterUser.class);
						ans.setaCreaterName(user.getRealName());
						ans.setaCreaterNickname(user.getNickName());
						//	1.2.1.1 获取回复人员头像链接
						redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(redisTmp)){
							pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
							ans.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
					}
					//app端把富文本进行过滤
					if(GameConstants.TYPE_APP.equals(_type) && ans.getContent() != null){
						ans.setAnswerBodyFilterHtml(RichTextUtil.delHTMLTag(ans.getContent()));
						ans.setContent("");
						JSONArray nullArray = new JSONArray();
						ans.setImgs(nullArray);
						ans.setLinks(nullArray);
					}
				}
			}
			return page.getMessageJSONObject("answers").toJSONString();
		}else if(2 == inquireType){//根据问题回复id获取唯一一条数据
			//1.1 分页获取回复消息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("questionId", questionId);
			param.put("orderBy", "create_time");
			param.put("isDelete", GameConstants.NO_DEL);
			param.put("answerId", Integer.valueOf(answerId));
			QueryPage<TeachQuestionAnswer> page = QueryPageComponent.queryPageByRedisSimple(1, 0, param, TeachQuestionAnswer.class);
			if(page.getState()){
				String redisTmp = null;
				CenterUser user = null;
				PublicPicture pic = null;
				JSONObject rich = null;
				
				//	1.2 参数组装
				for(TeachQuestionAnswer ans : page.getList()){
					
					//	1.2.1 获取用户真实姓名
					redisTmp = RedisComponent.findRedisObject(ans.getCreateUserId(), CenterUser.class);
					if(!StringUtil.isEmpty(redisTmp)){
						user = JSONObject.parseObject(redisTmp, CenterUser.class);
						ans.setaCreaterName(user.getRealName());
						ans.setaCreaterNickname(user.getNickName());
						//	1.2.1.1 获取回复人员头像链接
						redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(redisTmp)){
							pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
							ans.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
					}
					
					//	1.2.2 移动端参数组装
					if(GameConstants.TYPE_APP.equals(_type) && ans.getContent() != null){
						rich = RichTextUtil.parseRichText(ans.getContent());
						if(rich != null){
							ans.setContent(rich.getString("content"));
							ans.setLinks(rich.getJSONArray("links"));
							ans.setImgs(rich.getJSONArray("imgs"));
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