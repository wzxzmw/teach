package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.ListSortUtil;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachQuestionDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.entity.TeachQuestionAnswer;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
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
public class TeachQuestionService{
	
	@Autowired
	private TeachQuestionDao teachQuestionDao;
	@Autowired
	private CenterLiveService centerLiveService;
	
	@Autowired
	private CenterUserService userService;
	@Autowired
	private TeachQuestionAnswerService answerService;
	
	public TeachQuestion getTeachQuestion(TeachQuestion teachQuestion) {
		List<TeachQuestion> teachQuestionList = teachQuestionDao .selectSingleTeachQuestion(teachQuestion);
		if(teachQuestionList == null || teachQuestionList .size() <= 0){
			return null;
		}
		return teachQuestionList.get(0);
	}
	
	public void insertTeachQuestion(TeachQuestion teachQuestion){
		teachQuestionDao .insertTeachQuestion(teachQuestion);
	}

	/**
	 * 获取答疑列表信息 (分页)
	 * @param paramMap		分页参数
	 * @param start			起始数			从0开始
	 * @param limit			每页数量
	 * @param userType		用户类型
	 * @return
	 * @author 			lw
	 * @param userType 
	 * @param _type 
	 * @param U
	 * @date			2016年6月22日  下午8:11:07
	 */
	public JSONObject getQuestionsList(Map<String, Object> paramMap, Integer start, Integer limit, Integer userType, String _type) {
		if(paramMap.get("chapterId") != null  && !((Integer)paramMap.get("chapterId")).equals("")){
//			if(paramMap.get("isEssence")!= null && ((int)paramMap.get("isEssence"))==1){
//				1. 获取教学问题分页
				if((int)paramMap.get("isEssence") != 1){
					paramMap.remove("isEssence");
				}
				QueryPage<TeachQuestion> page = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachQuestion.class,"querynoEssenceByCount","querynoEssenceByPage");
				if(page.getState()){
//					page.getList().addAll(noEssencePage.getList());
					//根据时间进行倒序排序
//					ListSortUtil<TeachQuestion> sortList = new ListSortUtil<TeachQuestion>();  
//			        sortList.sort(page.getList(), "createTime", "desc"); 
					String redisTmp = null;
					CenterUser user = null;
					PublicPicture pic = null;
					TeachCourseChapter chapter = null;
					JSONObject rich = null;
					List<TeachQuestionAnswer> answersMenber = null;
					List<TeachQuestionAnswer> answers = null;
					
					//获取全部的问题回答
					List<Integer> questionIds = new ArrayList<Integer>();
					if(GameConstants.TYPE_PC.equals(_type)){
						for(TeachQuestion question : page.getList()){
							questionIds.add(question.getQuestionId());
						}
						if(questionIds.size() > 0){
							TeachQuestionAnswer answer = new TeachQuestionAnswer();
							answer.setQuestionIds(StringUtil.ListToString(questionIds, ","));
							answer.setOrderBy("create_time");
							answer.setIsDelete(GameConstants.NO_DEL);
							answers = answerService.selectSingleTeachQuestionAnswer(answer);
						}
					}
					//	2. 进行分页数据关联
					for(TeachQuestion question : page.getList()){
						//	3. 教学模块返回参数
						question.setQuestionModule(GameConstants.MANNER_TEACHING);
						
						//	4. 获取用户 名称 、昵称
						redisTmp = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
						if(!StringUtil.isEmpty(redisTmp)){
							user = JSONObject.parseObject(redisTmp, CenterUser.class);
							question.setqCreaterName(user.getRealName());
							question.setqCreaterNickname(user.getNickName());
							
							//	5. 获取问答创建人图片信息
							redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
							if(!StringUtil.isEmpty(redisTmp)){
								pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
								question.setqCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
							}
						}
						
						//	6. 如果有章节信息，获取章节名称
						if(question.getChapterId() != null){
							redisTmp = RedisComponent.findRedisObject(question.getChapterId(), TeachCourseChapter.class);
							if(!StringUtil.isEmpty(redisTmp)){
								chapter = JSONObject.parseObject(redisTmp, TeachCourseChapter.class);
								question.setChapterName(chapter.getChapterName());
							}
						}
						
						//	7.1 pc 返回参数
						if(GameConstants.TYPE_PC.equals(_type)){
							
							//空校验
							answersMenber = new ArrayList<TeachQuestionAnswer>();
							if(CollectionUtils.isEmpty(answers)){
								question.setAnswers(answersMenber);
							}else{
//								7.1.2 获取问题回复信息
								for(int i = 0 ; i < answers.size() ; i ++){
									if(question.getQuestionId().intValue() == answers.get(i).getQuestionId().intValue()){
										
										//	7.1.3 获取回复用户真实姓名
										redisTmp = RedisComponent.findRedisObject(answers.get(i).getCreateUserId(), CenterUser.class);
										if(!StringUtil.isEmpty(redisTmp)){
											user = JSONObject.parseObject(redisTmp, CenterUser.class);
											answers.get(i).setaCreaterName(user.getRealName());
											
											//	7.1.4 获取回复用户头像链接
											redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
											if(!StringUtil.isEmpty(redisTmp)){
												pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
												answers.get(i).setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
											}
										}
										answersMenber.add(answers.get(i));
										answers.remove(i);
										i--;
									}
								}
								question.setAnswers(answersMenber);
							}
							
						//	7.2 app 返回参数 ：整理回复内容返回符合app端接口参数
						}else if(GameConstants.TYPE_APP.equals(_type) && question.getContent() !=null){
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
		}else {
			if((int)paramMap.get("isEssence") != 1){
				paramMap.remove("isEssence");
			}
//			1. 获取教学问题分页
			QueryPage<TeachQuestion> page = QueryPageComponent.queryPageByRedisSimple(limit, start, paramMap, TeachQuestion.class);
			if(page.getState()){
				String redisTmp = null;
				CenterUser user = null;
				PublicPicture pic = null;
				TeachCourseChapter chapter = null;
				JSONObject rich = null;
				List<TeachQuestionAnswer> answersMenber = null;
				List<TeachQuestionAnswer> answers = null;
				
				//获取全部的问题回答
				List<Integer> questionIds = new ArrayList<Integer>();
				if(GameConstants.TYPE_PC.equals(_type)){
					for(TeachQuestion question : page.getList()){
						questionIds.add(question.getQuestionId());
					}
					if(questionIds.size() > 0){
						TeachQuestionAnswer answer = new TeachQuestionAnswer();
						answer.setQuestionIds(StringUtil.ListToString(questionIds, ","));
						answer.setOrderBy("create_time");
						answer.setIsDelete(GameConstants.NO_DEL);
						answers = answerService.selectSingleTeachQuestionAnswer(answer);
					}
				}
				//	2. 进行分页数据关联
				for(TeachQuestion question : page.getList()){
					//	3. 教学模块返回参数
					question.setQuestionModule(GameConstants.MANNER_TEACHING);
					
					//	4. 获取用户 名称 、昵称
					redisTmp = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
					if(!StringUtil.isEmpty(redisTmp)){
						user = JSONObject.parseObject(redisTmp, CenterUser.class);
						question.setqCreaterName(user.getRealName());
						question.setqCreaterNickname(user.getNickName());
						
						//	5. 获取问答创建人图片信息
						redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
						if(!StringUtil.isEmpty(redisTmp)){
							pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
							/*
							 * 压缩图片
							 */
							question.setqCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
						}
					}
					
					//	6. 如果有章节信息，获取章节名称
					if(question.getChapterId() != null){
						redisTmp = RedisComponent.findRedisObject(question.getChapterId(), TeachCourseChapter.class);
						if(!StringUtil.isEmpty(redisTmp)){
							chapter = JSONObject.parseObject(redisTmp, TeachCourseChapter.class);
							question.setChapterName(chapter.getChapterName());
						}
					}
					
					//	7.1 pc 返回参数
					if(GameConstants.TYPE_PC.equals(_type)){
						
						//空校验
						answersMenber = new ArrayList<TeachQuestionAnswer>();
						if(CollectionUtils.isEmpty(answers)){
							question.setAnswers(answersMenber);
						}else{
//							7.1.2 获取问题回复信息
							for(int i = 0 ; i < answers.size() ; i ++){
								if(question.getQuestionId().intValue() == answers.get(i).getQuestionId().intValue()){
									
									//	7.1.3 获取回复用户真实姓名
									redisTmp = RedisComponent.findRedisObject(answers.get(i).getCreateUserId(), CenterUser.class);
									if(!StringUtil.isEmpty(redisTmp)){
										user = JSONObject.parseObject(redisTmp, CenterUser.class);
										answers.get(i).setaCreaterName(user.getRealName());
										
										//	7.1.4 获取回复用户头像链接
										redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
										if(!StringUtil.isEmpty(redisTmp)){
											pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
											answers.get(i).setaCreaterHeadLink(Common.checkPic(pic.getFilePath())== true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
										}
									}
									answersMenber.add(answers.get(i));
									answers.remove(i);
									i--;
								}
							}
							question.setAnswers(answersMenber);
						}
						
					//	7.2 app 返回参数 ：整理回复内容返回符合app端接口参数
					}else if(GameConstants.TYPE_APP.equals(_type) && question.getContent() !=null){
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
	}

	
	


	
	/**
	 * 判断当前用户是否是该班级，是否是该班级的学生和教师
	 * 
	 * @param classId	班级id
	 * @param userId	用户id
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  上午10:52:32
	 */
	public CenterUser checkClassAndUser(Integer classId, Integer userId) {
		//判断班级是否存在
		String redisObject = RedisComponent.findRedisObject(classId, TeachClass.class);
		if(StringUtil.isEmpty(redisObject)){
			return null;
		}
		
		//根据用户id和班级id查询用户对象 判断是否加入班级
		CenterUser user = new CenterUser();
		user.setUserId(userId);
		user.setClassId(classId);
		user = userService.selectCenterUser(user);
		return user;
	}

	/**
	 * updateTeachQuestionByKey(修改问题表为点赞)   
	 * @author ligs
	 * @date 2016年6月27日 上午11:29:37
	 * @return
	 */
	public void updateTeachQuestionByKey(TeachQuestion teachQuestion){
		teachQuestionDao.updateTeachQuestionByKey(teachQuestion);
	}

	/**
	 * 添加教学班问题
	 * @param userId			用户id
	 * @param chapterId			章节id
	 * @param classId			课程id
	 * @param questionTitle		问题标题
	 * @param questionBody		问题内容
	 * @return
	 * @author 			lw
	 * @date			2016年6月29日  上午10:33:24
	 */
	public JSONObject addTeachQuestion(Integer userId, Integer chapterId, Integer classId, String questionTitle,String questionBody) {
		
		try {
			//	1. 添加问题
			TeachQuestion question = new TeachQuestion();
			question.setCreateUserId(userId);
			if(chapterId != null && chapterId > 0){
				question.setChapterId(chapterId);
			}
			question.setClassId(classId);
			question.setTitle(questionTitle);
			question.setContent(questionBody);
			question.setIsEssence(GameConstants.QUESTION_AND_ANSWER_NO_ESSENCE);
			question.setCreateTime(TimeUtil.getCurrentTimestamp());
			question.setAnswerNum(0);
			question.setIsDelete(GameConstants.NO_DEL);
			question.setPraiseNum(0);
			this.insertTeachQuestion(question);
			
			//	2. 用户问题数量加1 ， 并删除缓存
			userService.addQuestionNum(userId);
			
			//	3. 产生动态
			centerLiveService.addClubCenterLiveTeachQuestion(question.getQuestionId(), classId, userId);
			LogUtil.info(this.getClass(), "submitQuestion", GameConstants.SUCCESS_INSERT);
			return Common.getReturn(AppErrorCode.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "submitQuestion", String.valueOf(AppErrorCode.ERROR_SUBMITQUESTION_INSERT),e);
			return Common.getReturn(AppErrorCode.ERROR_INSERT, null);
		}
	}

	/**
	 * 问题回复加1	并删除缓存
	 * @param questionId	参数对象
	 * @author 			lw
	 * @date			2016年6月29日  上午11:16:23
	 */
	public void addAnswerNum(Integer questionId) {
		teachQuestionDao.addAnswerNum(questionId);
		//删除缓存
		JedisCache.delRedisVal(TeachQuestion.class.getSimpleName(), String.valueOf(questionId));
		
	}
	
	
	

	/**
	 * 查询单个问题详细信息
	 * @param questionId	查询参数
	 * @param start
	 * @param limit
	 * @param userType	用户类型
	 * @return
	 * @author 			lw
	 * @param _type 
	 * @date			2016年6月29日  下午8:51:22
	 */
	public JSONObject getQuestionsOne(Integer questionId, Integer start, Integer limit, Integer userType, String _type) {
		
		// 1. 参数预定义 (教学问题列表)
		List<TeachQuestion> list = new ArrayList<TeachQuestion>();
		CenterUser user = null;
		PublicPicture pic = null;
		JSONObject rich = null;
		TeachCourseChapter chapter = null; 
		String redisTmp = null;
		
		//	2. 获取问题
		TeachQuestion question = new TeachQuestion();
		question.setQuestionId(questionId);
		question = teachQuestionDao.selectTeachQuestion(question);
		if(question != null){
			
			if(question.getIsDelete() == GameConstants.NO_DEL){
				
				//	3. 教学模块
				question.setQuestionModule(GameConstants.MANNER_TEACHING);
				
				//	4. 获取用户 名称 、昵称
				redisTmp = RedisComponent.findRedisObject(question.getCreateUserId(), CenterUser.class);
				if(!StringUtil.isEmpty(redisTmp)){
					user = JSONObject.parseObject(redisTmp, CenterUser.class);
					question.setqCreaterName(user.getRealName());
					question.setqCreaterNickname(user.getNickName());
					
					//	4.1 获取问答创建人图片信息
					redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
					if(!StringUtil.isEmpty(redisTmp)){
						pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
						question.setqCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
					}
				}
				
				//	5. 如果有章节信息，获取章节名称
				if(question.getChapterId() != null){
					redisTmp = RedisComponent.findRedisObject(question.getChapterId(), TeachCourseChapter.class);
					if(!StringUtil.isEmpty(redisTmp)){
						chapter = JSONObject.parseObject(redisTmp, TeachCourseChapter.class);
						question.setChapterName(chapter.getChapterName());
					}
				}
				
				//	6.1  获取回复消息	 pc 端参数组装
				if(GameConstants.TYPE_PC.equals(_type)){
					
					//	6.1.1 获取回复信息
					TeachQuestionAnswer answer = new TeachQuestionAnswer();
					answer.setQuestionId(question.getQuestionId());
					answer.setIsDelete(GameConstants.NO_DEL);
					question.setAnswers(answerService.selectSingleTeachQuestionAnswer(answer));
					
					//	6.1.2 空校验
					if(CollectionUtils.isEmpty(question.getAnswers())){
						question.setAnswers(new ArrayList<TeachQuestionAnswer>());
					}else{
						// 6.1.2  参数组装
						for(TeachQuestionAnswer en : question.getAnswers()){
							//	6.1.2.1 获取用户真实姓名
							redisTmp = RedisComponent.findRedisObject(en.getCreateUserId(), CenterUser.class);
							if(!StringUtil.isEmpty(redisTmp)){
								user = JSONObject.parseObject(redisTmp, CenterUser.class);
								en.setaCreaterName(user.getRealName());
								
								//	6.1.2.2 获取回复人员头像链接
								redisTmp = RedisComponent.findRedisObjectPublicPicture(user.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
								if(!StringUtil.isEmpty(redisTmp)){
									pic = JSONObject.parseObject(redisTmp, PublicPicture.class);
									en.setaCreaterHeadLink(Common.checkPic(pic.getFilePath()) == true ? pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
								}
							}
						}
					}
					//	6.2  获取回复消息	 app 端参数组装
				}else if(GameConstants.TYPE_APP.equals(_type) && question.getContent()!=null){
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
		JSONObject josn = new JSONObject();
		josn.put("questions", list);
		josn.put("returnCount", 0);
		josn.put("allPage", 0);
		josn.put("currentPage", 0);
		josn.put("teachingRole", userType == null ? "" : userType);
		return Common.getReturn(AppErrorCode.SUCCESS, null, josn);
	}

	public void delTeachQuestionAll(List<TeachQuestion> delQuestionList) {
		teachQuestionDao.delTeachQuestionAll(delQuestionList);
	}

	/** 
	* @Title: getNewTeachQuestion 
	* @Description: 根据班级ID获得最后的问题
	* @return  参数说明 
	* @return TeachQuestion
	* @author liulin
	* @date 2016年7月19日 下午5:38:10
	*/
	public TeachQuestion getNewTeachQuestion(TeachQuestion teachQuestion) {
		return teachQuestionDao.getNewTeachQuestion(teachQuestion);
	}
	/*
	 * 获取根据班级ID获得最后的问题
	 */
	
	public TeachQuestion getNewTeachQuestionMax(Integer classId){
		return teachQuestionDao.getNewTeachQuestionMax(classId);
	}
}