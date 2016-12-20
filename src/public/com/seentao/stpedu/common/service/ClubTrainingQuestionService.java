package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.ClubTrainingQuestionDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubJoinTraining;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.RichTextUtil;
import com.seentao.stpedu.utils.StringUtil;
import com.seentao.stpedu.utils.TimeUtil;

@Service
public class ClubTrainingQuestionService{
	
	@Autowired
	private ClubTrainingQuestionDao clubTrainingQuestionDao;
	@Autowired
	private ClubJoinTrainingService clubJoinTrainingService;
	@Autowired
	private CenterUserService userService;
	@Autowired
	private ClubTrainingQuestionAnswerService clubQuestionAnswerService;
	
	public ClubTrainingQuestion getClubTrainingQuestion(ClubTrainingQuestion clubTrainingQuestion) {
		List<ClubTrainingQuestion> clubTrainingQuestionList = clubTrainingQuestionDao .selectSingleClubTrainingQuestion(clubTrainingQuestion);
		if(clubTrainingQuestionList == null || clubTrainingQuestionList .size() <= 0){
			return null;
		}
		
		return clubTrainingQuestionList.get(0);
	}
	/**
	 * updateClubTrainingQuestionByKey(修改培训班问题表)   
	 * @author ligs
	 * @date 2016年6月27日 下午3:51:52
	 * @return
	 */
	public void updateClubTrainingQuestionByKey(ClubTrainingQuestion clubTrainingQuestion){
		clubTrainingQuestionDao.updateClubTrainingQuestionByKey(clubTrainingQuestion);
	}
	
	/**
	 * 校验当前用户是否加入培训班
	 * @param classId	培训班id
	 * @param userId	用户id
	 * @return
	 * @author 			lw
	 * @date			2016年6月29日  上午10:39:12
	 */
	public ClubJoinTraining checkClassAndUser(Integer classId, Integer userId) {
		
		ClubJoinTraining clubJon = new ClubJoinTraining();
		clubJon.setClassId(classId);
		clubJon.setUserId(userId);
		clubJon.setIsDelete(GameConstants.NO_DEL);
		clubJon = clubJoinTrainingService.getClubJoinTrainingAll(clubJon);
		return clubJon;
	}
	
	/**
	 * 培训班问题提交
	 * @param classId			培训班id
	 * @param userId			用户id
	 * @param questionTitle		培训班问题标题
	 * @param questionBody		培训班问题内容
	 * @return
	 * @author 					lw
	 * @date					2016年6月29日  上午10:53:13
	 */
	public JSONObject addClubQuestion(Integer classId, Integer userId, String questionTitle, String questionBody) {
		try {
			
			//	1. 添加问题到数据库
			ClubTrainingQuestion clubQuestion = new ClubTrainingQuestion();
			clubQuestion.setClassId(classId);
			clubQuestion.setCreateTime(TimeUtil.getCurrentTimestamp());
			clubQuestion.setCreateUserId(userId);
			clubQuestion.setPraiseNum(0);
			clubQuestion.setIsEssence(0);
			clubQuestion.setAnswerNum(0);
			clubQuestion.setTitle(questionTitle);
			clubQuestion.setContent(questionBody);
			clubQuestion.setIsDelete(GameConstants.NO_DEL);
			clubTrainingQuestionDao.insertClubTrainingQuestion(clubQuestion);
			
			//	2. 用户问题数量加1 ， 并删除缓存
			userService.addQuestionNum(userId);
			LogUtil.info(this.getClass(), "addClubQuestion", String.valueOf(GameConstants.SUCCESS_INSERT));
			return Common.getReturn(AppErrorCode.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "addClubQuestion", String.valueOf(AppErrorCode.ERROR_INSERT));
			return Common.getReturn(AppErrorCode.ERROR_INSERT, AppErrorCode.ERROR_SUBMITQUESTION_INSERT);
		}
	}
	
	/**
	 * 校验 该问题是否是培训班
	 * @param questionId
	 * @param classId
	 * @return
	 * @author 			lw
	 * @date			2016年6月29日  上午11:46:35
	 */
	public ClubTrainingQuestion checkClassAndClass(Integer questionId) {
		String questionRedis = RedisComponent.findRedisObject(questionId, ClubTrainingQuestion.class);
		if(!StringUtil.isEmpty(questionRedis)){
			ClubTrainingQuestion question = JSONObject.parseObject(questionRedis, ClubTrainingQuestion.class);
			return question;
		}
		
		return null;
	}
	
	
	
	
	/**
	 * 俱乐部 问题 回复数量加1
	 * @param questionId	问题id
	 * @author 				lw
	 * @date				2016年6月29日  下午1:59:44
	 */
	public void addAnswerNum(Integer questionId) {
		
		//回复数量加1
		clubTrainingQuestionDao.addAnswerNum(questionId);
		//删除缓存
		JedisCache.delRedisVal(ClubTrainingQuestion.class.getSimpleName(), String.valueOf(questionId));
	}
	
	
	/**
	 * 获取答疑列表信息 (分页)
	 * @param paramMap		分页参数
	 * @param start			起始数			从0开始
	 * @param limit			每页数量
	 * @param userType		用户类型
	 * @return
	 * @author 			lw
	 * @param _type 
	 * @date			2016年6月29日  下午8:04:19
	 */
	public JSONObject getQuestionsList(Map<String, Object> paramMap, Integer start, Integer limit, Integer userType, String _type) {
		
		//	1. 获取教学问题分页对象
		QueryPage<ClubTrainingQuestion> page = QueryPageComponent.queryPageByRedisSimple(limit, start, paramMap, ClubTrainingQuestion.class);
		if(page.getState()){
			String redisTmp = null;
			CenterUser user = null;
			PublicPicture pic = null;
			JSONObject rich = null;
			List<ClubTrainingQuestionAnswer> memberAs = null;
			List<ClubTrainingQuestionAnswer> answers = null;
			
			if(GameConstants.TYPE_PC.equals(_type)){
				// 6.1.1 查询回复信息
				List<Integer> questionids = new ArrayList<Integer>();
				for(ClubTrainingQuestion question : page.getList()){
					questionids.add(question.getQuestionId());
				}
				if(questionids.size() > 0){
					ClubTrainingQuestionAnswer answer = new ClubTrainingQuestionAnswer();
					answer.setQuestionIds(StringUtil.ListToString(questionids, ","));
					answer.setIsDelete(GameConstants.NO_DEL);
					answers = clubQuestionAnswerService.selectSingleClubTrainingQuestionAnswer(answer);
				}
			}
			
			//	2. 进行分页数据关联
			for(ClubTrainingQuestion question : page.getList()){
				//	3. 初始化章节
				question.setChapterName("");
				question.setChapterId(0);
				
				//	4. 培训班模块
				question.setQuestionModule(GameConstants.MANNER_CLUB);
				
				//	5. 获取用户 名称 、昵称
				redisTmp = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
				if(!StringUtil.isEmpty(redisTmp)){
					user = JSONObject.parseObject(redisTmp, CenterUser.class);
					question.setqCreaterName(user.getRealName());
					question.setqCreaterNickname(user.getNickName());
					
					//	5.1. 获取问答创建人图片信息
					redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						/*
						 * 压缩图片
						 */
						question.setqCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
				}
				
				//	6.1  获取回复消息	 pc 端参数组装
				if(GameConstants.TYPE_PC.equals(_type)){
					
					memberAs = new ArrayList<ClubTrainingQuestionAnswer>();
					
					//	6.1.2 回复信息空校验
					if(CollectionUtils.isEmpty(answers)){
						question.setAnswers(memberAs);
						
					}else{
						
//						6.1.3 回复信息参数组装
						for(int i = 0 ; i < answers.size() ; i ++){
							if(answers.get(i).getQuestionId().intValue() == question.getQuestionId().intValue()){
								//	6.1.3.1 获取用户真实姓名
								redisTmp = RedisComponent.findRedisObject(answers.get(i).getCreateUserId(), CenterUser.class);
								if(!StringUtil.isEmpty(redisTmp)){
									user = JSONObject.parseObject(redisTmp, CenterUser.class);
									answers.get(i).setaCreaterName(user.getRealName());
									
									//	6.1.3.2 获取回复人员头像链接
									redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
									if(!StringUtil.isEmpty(redisTmp)){
										pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
										/*
										 * 压缩图片
										 */
										answers.get(i).setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
									}
								}
								memberAs.add(answers.get(i));
								answers.remove(i);
								i--;
							}
						}
						question.setAnswers(memberAs);
						
					}
					
				//	6.2   获取回复消息 app 端参数组装
				}else if(GameConstants.TYPE_APP.equals(_type) && question.getContent() != null){
					rich = RichTextUtil.parseRichText(question.getContent());
					if(rich != null){
						question.setContent(rich.getString("content"));
						question.setLinks(rich.getJSONArray("links"));
						question.setImgs(rich.getJSONArray("imgs"));
					}
				}
				
			}
		}
		paramMap.clear();
		paramMap.put("teachingRole", userType == null ? "" : userType);
		return page.getMessageJSONObject("questions", paramMap);
		
	}
	
	
	/**
	 * 查询单个问题详细信息
	 * @param questionId	问题id
	 * @param start
	 * @param limit
	 * @param userType		用户类型
	 * @return
	 * @author 			lw
	 * @param _type 
	 * @date			2016年6月29日  下午9:02:45
	 */
	public JSON getQuestionsOne(Integer questionId, Integer start, Integer limit, Integer userType, String _type) {
		
		// 1. 参数预定义 (教学问题列表)
		List<ClubTrainingQuestion> list = new ArrayList<ClubTrainingQuestion>();
		CenterUser user = null;
		PublicPicture pic = null;
		JSONObject rich = null;
		String redisTmp = null;
		
		//	2. 获取问题
		ClubTrainingQuestion question = new ClubTrainingQuestion();
		question.setQuestionId(questionId);
		question = clubTrainingQuestionDao.selectClubTrainingQuestion(question);
		
		if(question != null){
			
			if( question.getIsDelete() == GameConstants.NO_DEL){
				
				//	3. 培训班模块
				question.setQuestionModule(GameConstants.MANNER_CLUB);
				
				//	4. 获取用户 名称 、昵称
				redisTmp = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
				if(!StringUtil.isEmpty(redisTmp)){
					user = JSONObject.parseObject(redisTmp, CenterUser.class);
					question.setqCreaterName(user.getRealName());
					question.setqCreaterNickname(user.getNickName());
					
					//	4.2 获取问答创建人图片信息
					redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						/*
						 * 压缩图片
						 */
						question.setqCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
				}
				
				//	5.1  获取回复消息	 pc 端参数组装
				if(GameConstants.TYPE_PC.equals(_type)){
					
					//	5.1.1 获取问题回复信息
					ClubTrainingQuestionAnswer answer = new ClubTrainingQuestionAnswer();
					answer.setQuestionId(question.getQuestionId());
					answer.setIsDelete(GameConstants.NO_DEL);
					question.setAnswers(clubQuestionAnswerService.selectSingleClubTrainingQuestionAnswer(answer));
					
					//	5.1.2 空校验
					if(CollectionUtils.isEmpty(question.getAnswers())){
						question.setAnswers(new ArrayList<ClubTrainingQuestionAnswer>());
					}else{
						
						//	5.1.3 问题参数组装
						for(ClubTrainingQuestionAnswer en : question.getAnswers()){
							
							//	5.1.3.1 获取用户真实姓名
							redisTmp = RedisComponent.findRedisObject(en.getCreateUserId(), CenterUser.class);
							if(!StringUtil.isEmpty(redisTmp)){
								user = JSONObject.parseObject(redisTmp, CenterUser.class);
								en.setaCreaterName(user.getRealName());
								
								//	5.1.3.2 获取回复人员头像链接
								redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
								if(!StringUtil.isEmpty(redisTmp)){
									pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
									/*
									 * 压缩图片
									 */
									en.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
								}
							}
						}
					}
					//	5.1  获取回复消息	 app 端参数组装
				}else if(GameConstants.TYPE_APP.equals(_type) && question.getContent() != null){
					rich = RichTextUtil.parseRichText(question.getContent());
					if(rich != null){
						question.setContent(rich.getString("content"));
						question.setLinks(rich.getJSONArray("links"));
						question.setImgs(rich.getJSONArray("imgs"));
					}
				}
				
				list.add(question);
			}else if(question.getIsDelete() == GameConstants.YES_DEL){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.ERROR_SUBMITQUESTION_DEL_QUESTION); 
			}
		}
		JSONObject msg = new JSONObject();
		msg.put("questions", list);
		msg.put("returnCount", 0);
		msg.put("allPage", 0);
		msg.put("currentPage", 0);
		msg.put("teachingRole", userType == null ? "" : userType);
		return Common.getReturn(AppErrorCode.SUCCESS, "", msg);
	}
	/**
	 * delClubTrainingQuestionAll(批量删除)   
	 * @author ligs
	 * @date 2016年7月4日 上午11:57:43
	 * @return
	 */
	public void delClubTrainingQuestionAll(List<ClubTrainingQuestion> delClubTrainingQuestion) {
		clubTrainingQuestionDao.delClubTrainingQuestionAll(delClubTrainingQuestion);
	}
	
	
	
	
}