package com.seentao.stpedu.error;


/**
 * 游戏错误码-全局管理减少类的创建
 * @author chun
 *
 * Time：2016年4月27日 上午9:53:28 
 * E-mail: 279444454@qq.com
 */
public class AppErrorCode {
	//-----------------------------通用-------------------------------------------
	public static final int DEFAULT = 99;
	
	/** 成功 */
	public static final int SUCCESS = 0;
	/** 1 操作（包括API请求、注销、校验、发送验证码等）失败 */
	public static final int ERROR_TYPE_ONE = 1;
	/** 2 参数错误（包括不合法，无效，缺少参数，格式不正确，不能为空等） */
	public static final int ERROR_TYPE_TWO = 2;
	/** 3 没有相关权限（包括创建班级等）*/
	public static final int ERROR_TYPE_THREE = 3;
	/** 4 该手机号码已经注册或用户昵称已经被占用 */
	public static final int ERROR_TYPE_FOUR = 4;
	/** 5 搜索数据为空信息 */
	public static final int ERROR_TYPE_FIVE = 5;
	/** 10 您的账号已在其它设备登录（userToken无效），请注销并重新登录 */
	public static final int ERROR_TYPE_TEN = 10;
	/** 6  插入失败  */
	public static final int ERROR_INSERT = 6;
	/** 7  更新失败  */
	public static final int ERROR_UPDATE = 7;
	/** 8  删除失败  */
	public static final int ERROR_DELETE = 8;
	/** 9  数量不足  */
	public static final int ERROR_COUNT_NOT = 9;
	/** RedisDB 数据错误 **/
	public static final int ERROR_REDIS_DB_WRONG = 10;
	/** RedisComponent 组件运行错误 **/
	public static final int ERROR_REDISCOMPONENT_RUNTIMEEXCEPTION = 11;
	/** 查询类型不正确  **/
	public static final int ERROR_TYPE_CODE = 12;
	/** 反射方法失败 */
	public static final int ERROR_REFLEX = 20;
	
	/** 调用远程游戏接口失败、或者返回需求参数为空或者为获取！ */
	public static final int ERROR_VISIT_GAME_PARAM = 31;
	/** 请求参数错误 */
	public static final String REQUEST_PARAM_ERROR = "555555";
	/** api解析 错误码获取错误消息失败 */
	public static final int ERRORMESSAGE_ERROR = -1;
	
	/** 文本输入内容或者样式使用过多 */
	public static final String CONTENT_LENGTH_TO_LONG = "500001";
	//-------------------------------redis查询id错误----------------------------------------------------------------
	
	/** 通过homeworkid 获取 redis 中  学生文本作业  对象失败   		(文本作业单表)*/
	public static final String REDIS_ID_HOMEWORK 		= "701001";
	/** 通过courseid 获取 redis 中  课程  对象失败 				(课程单表)*/
	public static final String REDIS_ID_COURSE 			= "701002";
	/** 通过cardid 获取 redis 中  课程卡  对象失败				(课程卡单表)*/
	public static final String REDIS_ID_CARD 			= "702003";
	/** 通过chapterid 获取 redis 中  章节  对象失败 			(章节单表) */
	public static final String REDIS_ID_CHAPTER 		= "702004";
	/** 通过classid 获取 redis 中  课程  对象失败				(课程单表) */
	public static final String REDIS_ID_CLASS 			= "702005";
	/** 通过schoolid 获取 redis 中  院校  对象失败				(院校单表) */
	public static final String REDIS_ID_SCHOOL 			= "702006";
	/** 通过userid 获取 redis 中  院校  对象失败				(院校单表) */
	public static final String REDIS_ID_USER 			= "702007";
	/** 通过questionId 获取 redis 中  问题  对象失败			(问题单表) */
	public static final String REDIS_ID_TEACH_QUESTION 	= "702008";
	/** 通过questionId 获取 redis 中  培训班答疑  对象失败			(问题单表) */
	public static final String REDIS_ID_CLUB_QUESTION 	= "702009";
	/** 通过compId 获取 redis 中  赛事  对象失败				(问题单表) */
	public static final String REDIS_ID_MATCH 			= "702010";
	/** 通过clubId 获取 redis 中  俱乐部  对象失败				(问题单表) */
	public static final String REDIS_ID_CLUB 			= "702011";
	/** 通过relId 获取 redis 中 课程卡课程关系  对象失败			(课程卡课程关系表) */
	public static final String REDIS_ID_RELCARDCOURSE 	= "702012";
	/** 通过topicId 获取 redis 中 竞猜话题  对象失败			(竞猜话题表) */
	public static final String REDIS_ID_GUESSTOPIC 	= "702013";
	/** 通过topicId 获取 redis 中 竞猜  对象失败			(竞猜话题表) */
	public static final String REDIS_ID_GUESS 	= "702014";
	/** 通过answerId 获取 redis 中 教学问题  对象失败			(教学问题题表) */
	public static final String REDIS_ID_QUESTIONANSWER 	= "702015";
	/** 通过answerId 获取 redis 中 培训班问题  对象失败			(培训班问题题表) */
	public static final String REDIS_ID_TRAININGQUESTIONANSWER 	= "702016";
	/** 通过adId 获取 redis 中 培训班问题  对象失败			(培训班问题题表) */
	public static final String REDIS_ID_ARENAAD 	= "702016";
	
	
	
	/** 未从数据库中查询到对象，保存redis失败  */
	public static final String ERROR_SAVE_FORM_SELECTR_OBJECT = "702020";
	//---------------------------------分页-------------------------------------------
	
	/** 未设置IQueryPage接口实现！ */
	public static final String QUERY_PAGE_DAO_IMPLEMENTS = "701001";
	
	/** 类获取错误：1.请检查相关类的Dao方法、2.请检查MyBatis的Mapper接口!、3.请检查从容器获取的方法 */
	public static final String QUERY_PAGE_XML_MAPPER = "601002";
	
	
	
	//-----------------------------关注-------------------------------------------
	/** 关注的俱乐部不存在 */
	public static final String ERROR_CLUBMAIN = "505001";
	/** 关注的人不存在 */
	public static final String ERROR_USER = "505002";
	/** 已经关注过该俱乐部 */
	public static final String ATTENTCLUBMAIN = "505003";
	/** 已经关注过该对象 */
	public static final String ATTENTUSER = "505004";
	/** 未关注过该对象 */
	public static final String ERRORENTUSER = "505005";
	/** 关注的赛事不存在 */
	public static final String ERROR_COMPETITION = "505006";//-------------------
	/** 已经关注过该赛事 */
	public static final String ATTENTUSER_COMPETITION = "505007";//----------------
	//-----------------------------------教师文本作业-----------------------------------------------
	/** 教师未创建课程 */
	public static final String TEACHER_HOME_COURSE = "519001";
	/** 教师未创建作业 */
	public static final String TEACHER_HOME_WORK = "519002";
	/** 教师文本作业评分分数错误  */
	public static final String TEACHER_HOME_SCORE_WRONG = "519003";
	/** 教师文本作业 请求参数错误  */
	public static final String TEACHER_HOME_REQUEST_PARAM = "519004";
	/** 教师文本作业 新建错误  */
	public static final String TEACHER_HOME_INSERT_WRONG = "519005";

	
	//--------------------------------------发布文本作业----------------------------------------------------
	/** 教师文本作业发布 	 请求参数错误  */
	public static final String ERROR_SUBMITHOMEWORK_REQUEST_PARAM = "520001";
	/** 教师文本作业 新建作业角色错误 */
	public static final String TEACHER_HOME_ROLE = "520002";
	/** 教师文本作业 标题不能未空，不能全是空格 */
	public static final String TEACHER_HOME_TITLE = "520003";
	/** 教师文本作业 文本内容不能未空，且字数不能多于160个 */
	public static final String TEACHER_HOME_BODY = "520004";
	/** 请先设置作业卡片的时间 */
	public static final String ISCARDTIME = "520005";
	/** 文本作业标题不能包含特殊字符 */
	public static final String HOME_TITLE = "520006";
	/** 文本作业内容不能包含特殊字符 */
	public static final String HOME_CONTEXT = "520007";
	//-----------------------------------学生文本作业---------------------------------------------
	/** 学生上传文本信息 参数传递错误 */
	public static final String ERROR_REQUEST_PARAM = "522001";
	/** 学生并未加入班级 */
	public static final String ERROR_ID_RELSTUDENTCLASS = "522002";
	/** 未在课程卡规定时间之内，无法提交 */
	public static final String ERROR_COURSECARD_OUT_TIME = "522003";
	/** 提交保存或者更新失败 */
	public static final String ERROR_HOMEWORK_SAVE_OR_UPDATE = "522004";
	/** 学生回答的文本作业信息 课程标识错误 */
	public static final String ERROR_HOMEWORK_ID = "522005";
	/** 学生回答的文本作业信息 角色错误 */
	public static final String ERROR_HOMEWORK_ROLE = "522006";
	/** 学生已经提交作业，请删除作业之后再次提交 */
	public static final String ERROR_COURSECARD_SUBMITTED = "522007";
	/** 请填写文本作业内容  */
	public static final String ERROR_HOMEWORK_BODY = "519008";
	/** 请在课程卡时间范围内提交作业  */
	public static final String ERROR_HOMEWORK_SUBMIT_BODY = "519009";
	
	//---------------------------------------------获取学生回答的文本作业信息---------------------------------------------
	/** 获取学生回答的文本作业信息	请求参数错误 */
	public static final String ERROR_GETHOMEWORKSOLUTION_PARAM = "521001";
	
	//-----------------------------------章节信息-----------------------------------------------
	
	/** 班级不存在 */
	public static final String ERROR_NOTFOUND_CLASS = "514004";
	/** 课程信息不存在 */
	public static final String ERROR_NOTFOUND_COURSE = "519001";
	/** 获取教师发布的课程   请求参数错误 */
	public static final String ERROR_COURSE_PPARAM = "519002";
	//-----------------------------------获取教师信息-----------------------------------------------	
	/** 教师不存在 */
	public static final String ERROR_GET_TEACHER = "508006";
	/** 教师下没有班级 */
	public static final String ERROR_GET_CLASS = "508001";
	/** 不是教师 */
	public static final String ERROR_NOT_TEACHER = "508005";
	/** 学校下没有班级 */
	public static final String ERROR_SCHOOL_CLASS = "508002";
	/** 没有对应学校 */
	public static final String ERROR_SCHOOL = "508004";
	/** 获取教师列表失败 */
	public static final String ERROR_TEACH_GET = "508003";
	/** 同一学校下班级名称不可重复 */
	public static final String CLASSNAME_REPETITION = "508007";
	/** 获取班级列表失败 */
	public static final String GET_CLASS_ERROR = "519010";
	/** 获取教师课程卡失败 */
	public static final String GET_CARD_ERROR = "519012";
	//-----------------------------------获取班级信息---------------------------------------------
	/** 未加入该俱乐部 */
	public static final String NOT_ADD_CLUB = "505010";
	/** 俱乐部下没有班级 */
	public static final String NOT_CLASS = "505011";
	/** 班级不存在 */
	public static final String CLASS_NOT = "505012";
	/** 未创建班级或不是教师 */
	public static final String NOT_CRE_CLASS = "505013";
	/** 未搜索到班级 */
	public static final String NOT_SEARCH_CLASS = "505014";
	/** 没有参与培训班 */
	public static final String NOT_TRAIN_CLASS = "505015";
	//-----------------------------------提交班级信息----------------------------------------------
	/** 提交班级信息失败 */
	public static final String ERROR_SAVE_CLASS = "509001";
	/** 已加入过培训班 */
	public static final String ERROR_IS_CLASS = "509002";
	/** 您不是该培训班的创建人不能修改信息 */
	public static final String ERROR_NOT_CRENT = "509003";
	/** 只有会长或教练可以创建培训班 */
	public static final String NO_JURISDICTION = "509004";
	/** 已创建过培训班 */
	public static final String IS_FOUND = "509005";
	/** 加入培训班所需费用余额不足  */
	public static final String LACK_OF_BALANCE = "509006";
	/** 培训班不存在 */
	public static final String IS_CLASSES_JOIN = "509007";
	/** 所在俱乐部没有购买运营增值权不能创建收费培训班 */
	public static final String IS_JURISDICTION_JOIN = "509008";
	/** 班级介绍为2-70个汉字 */
	public static final String IS_DESC_JOIN = "509009";
	/** 班级的创建者不存在 */
	public static final String IS_CRETE_JOIN = "509010";
	/** 请输入班级名称 */
	public static final String IS_IMPORT_NAME = "509011";
	/** 请输入班级介绍 */
	public static final String IS_IMPORT_DESC = "509012";
	/** 请上传班级封面图片 */
	public static final String IS_IMPORT_PICTURE = "509013";
	/** 请设置费用 */
	public static final String IS_IMPORT_COST = "509014";
	/** 名称为2-20个中英文字符 */
	public static final String IS_TWO_TWENTY = "509015";
	//-----------------------------------设置默认班级---------------------------------------------
	/** 不是该班级的教师 */
	public static final String ERROR_TEACHERISCLASS = "510001";
	/** 请输入2-150字 */
	public static final String IS_INPUT_ONE_HUNDRED_FIFTY = "535002";
	//--------------------------------------手机端 ： 获取课程信息-----------------------------------------------------
	/** 获取课程信息 	 */
	public static final String ERROR_COURSE_REQUEST_PARAM = "515001";
	/** 学生课程班级表保存失败 	 */
	public static final String ERROR_TEACH_REL_STUDENT_COURSE = "515002";
	/** 课程卡课程学习人次保存/修改失败 	 */
	public static final String ERROR_TEACH_REL_CARD_COURSE = "515003";
	/** 班级获取，班级信息和课程信息不匹配 	 */
	public static final String ERROR_TEACH_AND_COURSE = "515004";
	/** 俱乐部培训班学习人次修改失败 	 */
	public static final String ERROR_CLUB_STUDY = "515005";
	
	//--------------------------------提交作业或人员的评分-------------------------------
	/** 学生提交作业或人员的评分 请求参数错误 */
	public static final String ERROR_SUBMITHOMEWORKSOLUTION_REQUEST_PARAM = "523001";
	/** 教师提交作业或人员的评分 请求参数错误 */
	public static final String ERROR_SUBMITSCORE_REQUEST_PARAM = "523002";
	/** 教师对班级学生的评分 请求参数错误 */
	public static final String ERROR_SUBMITSCORE_RELCLASS_REQUEST_PARAM = "523003";
	/** 教师对班级学生的评分 插入失败 **/
	public static final String ERROR_SUBMITSCORE_RELCLASS_INSERT = "523004";
	/** 教师对学生上传作业的评分 更新失败 **/
	public static final String ERROR_SUBMITSCORE_UPLOAD_UPDATE = "523005";
	/** 文本作业已经删除 **/
	public static final String ERROR_SUBMITSCORE_RELCLASS_ISDELETE = "523006";
	/** 文本作业不存在 **/
	public static final String ERROR_SUBMITSCORE_RELCLASS_EXISTENT = "523007";
	/** 教师未管理该班级 **/
	public static final String ERROR_SUBMITSCORE_RELCLASS_CLASS_TEACHER = "523008";
	/** 平分学生信息错误 **/
	public static final String ERROR_SUBMITSCORE_RELCLASS_CLASS_STUDENT = "523009";
	//--------------------------------报名加入班级-------------------------------
	/** 已经加入班级 */
	public static final String ERROR_ALREADY_CLASS = "511001";
	/** 不是学生  */
	public static final String ERROR_NO_USER = "511002";
	/** 不是本校学生 */
	public static final String ERROR_NO_SCHOOL = "511003";
	/**加入班级失败*/
	public static final String ADD_CLASS_ERROR = "511007";
	//--------------------------------邀请或踢出班级-------------------------------
	/** 用户不是该学生所属班级的教师 */
	public static final String ERROR_NO_TEACHER = "525001";
	/** 未创建班级 */
	public static final String ERROR_NOT_CLASSES = "525002";
	/** 学生正在参赛阶段请比赛结束后再踢出! */
	public static final String  STUDENT_IS_GAME ="525003";
	/** 不是该俱乐部会长 */
	public static final String  IS_NOT_CLUB_LEVE ="525004";
	/** 不是教练 */
	public static final String ERROR_NO_COACH = "525005";
	/** 失败 */
	public static final String ERROR_NO = "525006";
	//--------------------------------对信息进行点赞点踩置顶等操作-------------------------------
	/** 信息主体不存在 */
	public static final String INFORMATION_NOT = "529001";
	/** 已经对信息主体进行过加精 */
	public static final String ALREADY_DIGEST = "529002";
	/** 已经对信息主体进行过点赞 */
	public static final String ALREADY_LIKE = "529003";
	/** 未开启签到功能 */
	public static final String NOT_OPEN_SIGN = "529004";
	/** 未到计划签到日期 */
	public static final String NOT_SIGN_IN = "529005";
	/** 请操作自己俱乐部的信息 */
	public static final String ONESELF_CLUB = "529006";
	/** 态度主体类型参数错误 */
	public static final String PARAMETER_ERROR = "529007";
	/** 仅会长可以置顶 */
	public static final String PRESIDENT_STICK = "529008";
	/** 仅会长可以取消置顶 */
	public static final String PRESIDENT_CANCEL_STICK = "529009";
	/** 已经对信息主体进行过点踩 */
	public static final String ALREADY_TRAMPLE = "529010";
	/** 用户不存在 */
	public static final String USER_ERROR = "529011";
	/** 已经对人员进行过点赞 */
	public static final String IS_USER_LIKE = "529012";
	/** 照片不存在 */
	public static final String PHOTO_NOT = "529013";
	/** 已经对照片进行过点赞 */
	public static final String IS_PHOTO_LIKE = "529014";
	/** 已经签到 */
	public static final String IS_SIGN_IN = "529015";
	/** 已经对话题评论进行过点赞 */
	public static final String IS_TOPIC_LIKE = "529016";
	/** 信息主体不存在或已冻结 */
	public static final String INFORMATION_NOT_IS_FREEZE = "529017";
	/** 计划签到日期已过 */
	public static final String NOT_SIGN_FORMERLY = "529018";
	//---------------------------------学生证书------------------------------------------
	/** 学生认证参数传递错误 **/
	public static final String ERROR_ACTIONTYPE_REQUEST_PARAM  = "524001";
	/** 学生认证处理类型失败 **/
	public static final String ERROR_ACTIONTYPE_TYPE  = "524002";
	/** 学生和教师关系不匹配 **/
	public static final String ERROR_ACTIONTYPE_TEACHER_AND_USER  = "524003";
	
	//----------------------------------答疑----------------------------------------------
	/** 答疑请求处理类型错误 **/
	public static final String ERROR_QUESTIONS_TYPE  = "526001";
	/** 答疑请求用户不匹配 **/
	public static final String ERROR_QUESTIONS_USER_OUT  = "526002";
	/** 答疑请求参数错误 **/
	public static final String ERROR_QUESTIONS_PARAM  = "526003";
	/** 答疑请求 	数据库插入失败 **/
	public static final String ERROR_QUESTIONS_INSERT  = "526004";
	
	/** 移动端 ： 获取问题回复信息 请求参数错误 */
	public static final String ERROR_ANSWERS_APP_PARAM  = "526005";
	/** 答疑请求 	班级不存在 **/
	public static final String ERROR_QUESTIONS_CLASS  = "526006";
	
	
	/**投票提示 不能重复投票*/
	public static final String ERROR_VOTE_MESSAGE="620001";
	//---------------------------------提交问题信息----------------------------------------------------
	/**投票已经结束,不能在继续投票*/
	public static final String ERROR_VOTE_END="620002";
	/** 提交问题 请求参数错误 */
	public static final String ERROR_SUBMITQUESTION_REQUEST_PARAM  = "527001";
	/** 提交问题信息求用户不匹配 */
	public static final String ERROR_SUBMITQUESTION_USER_OUT  = "527002";
	/** 提交问题信息 保存错误 */
	public static final String ERROR_SUBMITQUESTION_INSERT  = "527003";
	/** 请填写问题标题和问题内容 */
	public static final String ERROR_SUBMITQUESTION_CONTENT  = "527004";
	/** 问题标题多于30个文字 */
	public static final String ERROR_SUBMITQUESTION_TITLE_LENGTH  = "527005";
	/** 问题 该页面内容已经被删除 */
	public static final String ERROR_SUBMITQUESTION_DEL_QUESTION  = "527006";
	
	//---------------------------------------提交问题的回复信息-----------------------------------------------
	/** 提交问题的回复信息 请求参数错误 */
	public static final String ERROR_SUBMITANSWER_REQUEST_PARAM  = "528001";
	
	/** 提交问题的回复信息 问题不存在或者您未加入班级 */
	public static final String ERROR_SUBMITANSWER_QUESTION_AND_CLASS  = "528002";
	/** 提交问题的回复信息 	保存失败*/
	public static final String ERROR_SUBMITANSWER_QUESTION_INSERT  = "528003";
	
	
	
	//----------------------------------------竞猜主题--------------------------------------------
	/** 竞猜话题创建参数错误 */
	public static final String ERROR_CREATE_GUESS_REQUEST_PARAM = "557001";
	/** 竞猜话题 创建失败 */
	public static final String ERROR_ARENAGUESS_INSERT = "557002";
	/** 竞猜话题 已经关闭 */
	public static final String ERROR_GUESS_END_TOPIC = "557003";
	/** 竞猜话题 创建竞猜话题没有权利 */
	public static final String ERROR_GUESS_POWER = "557004";
	/** 竞猜话题 请填写竞猜话题标题(不能是空格) */
	public static final String ERROR_GUESS_TITLE = "557005";
	/** 竞猜话题 说明不能大于160个字 */
	public static final String ERROR_GUESS_BODY = "557006";
	
	
	//=======================================竞猜=========================================
	/** 创建竞猜请求参数错误 */
	public static final String ERROR_QUIZ_REQUEST_PARAM = "561001";
	/** 创建竞猜保存错误 */
	public static final String ERROR_QUIZ_INSERT = "561002";
	/** 创建竞猜 类型错误 */
	public static final String ERROR_QUIZ_TYPE = "561003";
	/** 创建竞猜 押注方向错误 */
	public static final String ERROR_QUIZ_DIRECTION = "561004";
	/** 创建竞猜  庄家赔率、押金参数错误 或者押金方向错误 */
	public static final String ERROR_QUIZ_MONEY_ODDS = "561005";
	/** 创建竞猜  请检查创建竞猜的话题和结束时间 */
	public static final String ERROR_QUIZ_SUBMIT = "561006";
	
	
	//-------------------------------------赛事--------------------------------------------
	/** 创建赛事 请求参数错误 */
	public static final String ERROR_GAME_EVENT_REQUEST_PARAM = "555001";
	/** 创建赛事	未加入俱乐部	*/
	public static final String ERROR_GAME_EVENT_OUT_CLUB = "555002";
	/** 创建赛事	不是俱乐部会长 或者    已经创建比赛	*/
	public static final String ERROR_GAME_EVENT_OUT_PRESIDENT_OR_CREATECOMP = "555003";
	/** 提交赛事	没有购买赛事运营权利	*/
	public static final String ERROR_GAME_EVENT_NOT_BUY = "555004";
	/** 创建赛事	俱乐部对象获取失败	*/
	public static final String ERROR_GAME_EVENT_CLUB_BEAN = "555005";
	/** 创建赛事	用户不存在	*/
	public static final String ERROR_GAME_EVENT_USER = "555006";
	/** 赛事创建	插入数据库失败 */
	public static final String ERROR_GAME_CREATE_DB = "555007";
	/** 赛事创建 	修改数据库失败 */
	public static final String ERROR_GAME_UPDATE_DB = "555008";
	/** 赛事创建 	填写赛事标题和赛事说明不能是空 */
	public static final String ERROR_GAME_INSERT_NULL = "555009";
	/** 赛事创建 	请检查赛事开始和结束时间 */
	public static final String ERROR_GAME_TIME = "555010";
	/** 赛事创建 	赛事标题输入过长 */
	public static final String ERROR_GAME_TITLE = "555012";
	
	
	//----------------------------------广告---------------------------------------
	/** 广告	获取广告信息 失败 */
	public static final String ERROR_ADVERTISED_REQUEST_PARAM = "553001";
	
	
	
	
	
	
	/** 态度主体类型参数错误 */
	public static final String ERROR_GUESS_ID_WRONG = "558001";
	
	
	//-----------------------------------------货币锁定常量-----------------------------------------------------
	/**锁定中*/
	public static final String CENTER_MONEY_LOCK_ONE = "1";
	/**已解锁*/
	public static final String CENTER_MONEY_LOCK_TWO = "2";
	/**已扣除 */
	public static final String CENTER_MONEY_LOCK_Three = "3";
	/**竞猜*/
	public static final String CENTER_MONEY_LOCK_T = "1";
	/**提现*/
	public static final String CENTER_MONEY_LOCK_TT = "2";
	
	//--------------------------------登录信息---------------------------------------------------
	/**不支持该种方式登录*/
	public static final String ERROR_LOGIN = "501002";
	
	
	
	//-----------------------------------------------竞猜公布结果----------------------------------------------------
	/** 竞猜公布结果	请求参数错误*/
	public static final String ERROR_QUIZRESULT_REQUEST_PARAM = "560001";
	/** 竞猜下注表		未查询到数据*/
	public static final String ERROR_QUIZRESULT_BET = "560002";
	/** 竞猜下注		请正确提交下注金额 */
	public static final String ERROR_QUIZRESULT_BET_MONEY = "560016";
	/** 竞猜公布结果	奖金金额错误 */
	public static final String ERROR_QUIZRESULT_MONEY = "560003";
	/** 竞猜公布结果	下注表人数错误 */
	public static final String ERROR_QUIZRESULT_BET_PERSON_NUM = "560004";
	/** 竞猜公布结果	货币锁定表人数错误 */
	public static final String ERROR_QUIZRESULT_LOCK_PERSON_NUM = "560005";
	/** 竞猜公布结果	存储数据库失败 */
	public static final String ERROR_QUIZRESULT_TO_DB = "560006";
	/** 竞猜公布结果	竞猜还未结束 或者已经公布结果 */
	public static final String ERROR_QUIZRESULT_GUESS_NOT_END = "560007";
	/** 竞猜公布结果	多线程（一个小时之后执行） 竞猜结果失败 */
	public static final String ERROR_QUIZRESULT_GUESS_AFTER_EXE = "560008";
	/** 竞猜公布结果	多线程（一个小时之后执行） 没有竞猜结果公布对象 */
	public static final String ERROR_QUIZRESULT_GUESS_AFTER_NOT_OBJ = "560009";
	/** 竞猜公布结果	竞猜结果已经分配 */
	public static final String ERROR_QUIZRESULT_GUESS_FINISH = "560010";
	/** 竞猜公布结果	正反方下注表都没有数据 */
	public static final String ERROR_QUIZRESULT_GUESS_BET_FINISH = "560011";
	/** 竞猜公布结果	下注双方没有账户*/
	public static final String ERROR_QUIZRESULT_ACCOUNT = "560012";
	/** 竞猜公布结果	保存结果：没有账户表变动 */
	public static final String ERROR_QUIZRESULT_RESULT_ACCOUNT = "560013";
	/** 竞猜公布结果	保存结果：没有资金变动变动*/
	public static final String ERROR_QUIZRESULT_RESULT_MONGEY_CHANGE = "560014";
	/** 竞猜公布结果	保存结果：现金锁定表变动*/
	public static final String ERROR_QUIZRESULT_RESULT_LOCK = "560015";

	
	//-------------------------------------俱乐部话题--------------------------------------------
		/** 获取俱乐部话题失败 */
		public static final String ERROR_GET_TOPICS = "572001";
	
		/** 发布话题失败 */
		public static final String ERROR_ADD_TOPIC = "573001";
		/** 您没有发布话题的权限 */
		public static final String ERROR_ADD_TOPIC_PERN = "573002";
		
		/** 获取提醒失败 */
		public static final String ERROR_GET_REMIND = "574001";
		
		/** 发布提醒失败 */
		public static final String ERROR_ADD_REMIND = "575001";
		
		/** 您没有发布提醒的权限*/
		public static final String ERROR_ADD_REMIND_PERN = "575002";
		
		/** 提醒内容须在2到70个字之间*/
		public static final String ERROR_STR = "575004";
		
		/** 您没有发布提醒 请输入2 - 70个字，不包括收尾空格*/
		public static final String ERROR_ADD_CONTENT_LENGTH = "575003";
		
		/** 获取通知失败 */
		public static final String ERROR_GET_NOTICE = "576001";
		
		/** 发布提醒失败 */
		public static final String ERROR_ADD_NOTICE = "577001";
		
		/** 您没有发布通知的权限 */
		public static final String ERROR_ADD_NOTICE_PERN = "577002";
		
		/** 内容为2-2000个字  */
		public static final String _CONT_ERROR = "577003";
		/** 获得动态失败 */
		public static final String ERROR_GET_ATTITUDE = "578001";
		
		/** 获取心情失败 */
		public static final String ERROR_GET_MOOD = "579001";
		
		/** 提交签名失败*/
		public static final String ERROR_SUBMIT_USER_DESC = "595001";
		
		/** 验证码错误*/
		public static final String ERROR_USER_VERIFY_NUMBER = "510401";
		
		/** 绑定手机号失败*/
		public static final String ERROR_ADD_USER_PHONE = "510402";
		
		/** 原手机号码不正确*/
		public static final String ERROR_FAIL_USER_PHONE = "510403";
		
		/** 申请验证码失败 */
		public static final String ERROR_GET_VERIFY_PICTURE = "510301";
		/** 获取企业失败 */
		public static final String ERROR_GET_COMPANIES = "510501";
	
	//----------------------------------------------结束竞猜话题或竞猜------------------------------------
	/** 结束竞猜话题或竞猜	请求参数失败 */
	public static final String ERROR_FINISHQUIZ_REQUEST_PARAM = "559001";
	/** 结束竞猜话题或竞猜	不是俱乐部会长 */
	public static final String ERROR_FINISHQUIZ_PRESIDENT = "559002";
	/** 结束竞猜话题或竞猜	持久化失败 */
	public static final String ERROR_FINISHQUIZ_TO_DB = "559003";
	/** 结束竞猜话题或竞猜	不是竞猜创建人 没有权利 */
	public static final String ERROR_FINISHQUIZ_GUESS_POWER = "559004";
	/** 结束竞猜话题或竞猜	竞猜话题获取失败*/
	public static final String ERROR_FINISHQUIZ_GUESSTOPIC = "559005";
	
	
	//--------------------------------------------获取竞猜信息-----------------------------------------
	/** 获取竞猜信息	请求参数错误 */
	public static final String ERROR_GETQUIZS_REQUEST_PARAM = "558011";
	
	//---------------------------------注册信息---------------------
	
	
	
	
	
	//-------------------------------------获取竞猜话题信息---------------------------------------
	/** 获取竞猜话题信息	请求参数错误 */
	public static final String ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM = "556001";
	/** 获取竞猜话题信息	请求比赛消息失败 */
	public static final String ERROR_GET_MATCH = "556002";
	//-------------------------------------发送验证码----------------
	/**用户角色不对*/
	public static final String ERROR_ROLE = "592001";
	//--------------------------------账户信息----------------
	
	/**该账户不存在*/
	public static final String ERROR_NO_ACCOUNT = "597002";
	//--------------------------------账户信息----------------
	
	
	
	//------------------------------------------竞猜下注-----------------------------------------------------
	/**竞猜下注	参数错误*/
	public static final String ERROR_DO_GUESS_REQUEST_PARAM = "562001";
	/**竞猜下注	投递方向错误*/
	public static final String ERROR_DO_GUESS_DIRECTION = "562002";	
	/**虚拟币不足*/
	public static final String DEFICIENCY_OF_VIRTUAL_CURRENCY = "562003";
	/**竞猜下注	不能跟随庄家投递  */
	public static final String ERROR_DO_GUESS_GUEST_DIRECTION = "562004";	
	/**竞猜下注	投递金额大于目前可投金额 */
	public static final String ERROR_DO_GUESS_OUT_MAX_ACCOUNT = "562005";	
	/**竞猜下注	当前竞猜庄家不能下注 */
	public static final String ERROR_DO_GUESS_LANDLORD = "562006";	
	/**竞猜下注	超过竞猜结束时间不能下注 */
	public static final String ERROR_DO_GUESS_TIME_END = "562007";	
	
	//-------------------------------------------获取赛事信息--------------------------------------------------------
	/**	获取赛事信息	请求参数错误 */
	public static final String ERROR_GET_GAMEEVENTS_PARAM = "554001";	
	/** 获取赛事信息	并无请求的赛事id*/
	public static final String ERROR_GET_GAMEEVENTS_NO_MATCH = "554002";	
	
	//------------------------------------用户虚拟物品-一级货币-------------------------------------
	
	/**获取虚拟金额成功**/
	public static final String GET_THE_USER_VIRTUAL_GOODS = "563001";	
	/**获取虚拟货币失败**/
	public static final String FAILED_TO_OBTAIN_VIRTUAL_CURRENCY = "563002";
	/** 钱包余额不足 **/
	public static final String SHORTAGE_FIRST_CLASS_CURRENCY = "563003";
	
	//------------------------------------获取自定义背景色-------------------------------------
	
	/**获取自定义背景色失败**/
	public static final String GETS_CUSTOM_BACKGROUND_COLOR_FAILED = "567001";
	/**	**/
	public static final String GETS_CUSTOM_BACKGROUND_COLOR_SUCCESS = "567002";
	
	//------------------------------------俱乐部-------------------------------------
	
	/**俱乐部信息为空**/
	public static final String GET_ALL_CLUBS_INFO_FAILED = "564001";
	/**参赛俱乐部信息为空**/
	public static final String CLUB_INFORMATION_IS_EMPTY = "564002";
	/**获取俱乐部信息成功**/
	public static final String GET_CLUB_INFORMATION_SUCCESS = "564003";
	/**获取俱乐部信息失败**/
	public static final String GET_CLUB_INFORMATION_FAILED = "564004";
	/**会员不是该俱乐部**/
	public static final String MEMBER_IS_NOT_THE_CLUB = "569001";
	/**俱乐部会员操作成功**/
	public static final String CLUB_MEMBER_OPERATION_SUCCESS = "569002";
	/**俱乐部会员操作失败**/
	public static final String CLUB_MEMBER_OPERATION_FAILED = "569003";
	
	public static final String CLUB_MEMBER_CANCEL_APPLA = "569111";
	/** 或 已加入俱乐部**/
	public static final String JOIN_THE_CLUB_SUCCESS = "568001";
	/**加入俱乐部失败**/
	public static final String JOIN_THE_CLUB_FAILED = "568002";
	/**操作数据库更新异常啦*/
	public static final String DATABASE_UPDATE_EXCEPTION = "569004";
	/**正在审核中**/
	public static final String QUIT_THE_CLUB_SUCCESSFULLY = "568003";
	/**加入俱乐部申请成功，请耐心等待审核*/
	public static final String QUIT_THE_CLUB_SUCCESSFULLY_MESSAGE ="加入俱乐部申请成功，请耐心等待审核";
	/**比赛未结束退出俱乐部失败**/
	public static final String THE_GAME_HAS_NOT_ENDED = "568004";
	/**你么有权限T人**/
	public static final String YOU_DONOT_HAVE_ACCESS_TO_T_PEOPLE = "568005";
	/**会长不能退出**/
	public static final String THE_PRESIDENT_CANT_QUIT = "568006";
	/**教练不能退出*/
	public static final String THE_COACH_CANT_QUIT = "568009";
	/**加入俱乐部成功*/
	public static final String JOIN_CLUB_SUCCESS="5681000";
	/**家兔俱乐部成功*/
	public static final String JOIN_CLUB_SUCCESS_MESSAGE="加入俱乐部成功";
	/**加入俱乐部申请成功，请耐心等待审核*/
	//public static final String JOIN_CLUB_SUCCESS_WAIT="5681100";
	/**有正在参加的擂台赛不能退出俱乐部**/
	public static final String CANNOT_LEAVE_THE_CLUB = "568007";
	/**用户不属于俱乐部 */
	public static final String _USER_IS_NOT = "568008";
	/**你已创建俱乐部**/
	public static final String YOU_HAVE_CREATED_THE_CLUB = "566001";
	/**俱乐部信息异常操作失败**/
	public static final String CREATE_CLUB_SUCCESS =  "566002";
	/**修改风格id成功**/
	public static final String MODIFY_STYLE_BACKGROUND_SUCCESS =  "566003";
	/**修改背景色id成功**/
	public static final String MODIFY_THE_BACKGCOLOR_SUCCESS =  "566004";
	/**修改基本信息成功**/
	public static final String MODIFY_BASIC_INFORMATION_SUCCESSFULLY =  "566005";
	/**修改擂台海报id成功**/
	public static final String MODIFY_ARENA_POSTER_BACKGROUNDSUCCESS =  "566006";
	/**修改海报id成功**/
	public static final String SUCCESSFUL_CHANGE_OF_THE_BACKGROUND =  "566007";
	/**修改俱乐部失败**/
	public static final String FAILED_TO_MODIFY_THE_CLUB = "566008";
	/**请输入2~30个字的介绍**/
	public static final String PLEASE_ENTER_TWO_THREE_WORD = "566009";
	/**校验创业宝--请正确输入创业宝金额*/
	public static final String VERIFY_MONEY="561100";
	/**请选择地区或学校**/
	public static final String PLEASE_SELECT_AREA_OR_SCHOOL = "566010";
	/**请选择会员加入方式**/
	public static final String PLEASE_SELECT_MEMBER_JOIN_CLUB = "566011";
	//-----------------------------私信----------------------------------------
	
	/**获取私信信息失败**/
	public static final String GET_PRIVATE_INFORMATION_FAILED = "570001";
	/**发私信成功**/
	public static final String SUBMIT_INFORMATION_SUCCESS = "570002";
	/**发私信失败**/
	public static final String SUBMIT_INFORMATION_FAILED = "570003";
	/**请输出少于140个字大于2个字**/
	public static final String SUBMIT_NUM_FAILED = "570004";
	/**不能自己发给自己**/
	public static final String CAN_NOT_SEND_THEIR_OWN = "570005";
	/**私信内容不能为空**/
	public static final String CAN_NOT_SEND_NULL = "570006";
	
	//-----------------------------相册----------------------------------------
	
	/**获取人员相册信息失败**/
	public static final String FAILED_TO_GET_THE_STAFF_HOTO_ALBUM = "597001";
	
	/**设置封面成功**/
	public static final String SET_COVER_SUCCESS = "597002";
	/**图片不属于本相册**/
	public static final String PICTURES_DONOT_BELONG_TO_THIS_ALBUM = "597003";
	/**提交相册图片成功**/
	public static final String SUBMIT_PHOTO_ALBUM_SUCCESS = "597004";
	/**提交相册图片失败**/
	public static final String SUBMIT_PHOTO_ALBUM_FAILED = "597005";
	/**创建相册成功**/
	public static final String CREATE_A_PHOTO_ALBUM = "597006";
	/**修改相册成功**/
	public static final String ALBUM_SUCCESS = "597007";
	/**操作相册失败**/
	public static final String FAILED_TO_OPERATE_ALBUM = "597008";
	
	/**相册用户信息不匹配**/
	public static final String Album_user_information_donot_match = "597009";
	
	/**请输入0~30个字**/
	public static final String CHARACTER_TOO_LONG = "511009";
	
	/**请输入2-10字*/
	public static final String SUBMIT_USER_TWO_TEN="511005";
	
	/**相册名称占用*/
	public static final String ALBUM_NAME_OCCUPANCY="511004";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//------------------------邀请其他人或推送俱乐部回答问题操作----------------------------------------
	/** 已经邀请过该用户回答问题 */
	public static final String IS_ANSWER_QUESTIONS = "530001";
	/** 该问题已经推送到俱乐部 */
	public static final String IS_ERROR_CLUB = "530002";
	/** 推送成功 */
	public static final String IS_PUSH_SUCCESS = "530003";
	/** 邀请成功 */
	public static final String IS_INVITE_SUCCESS = "530004";
	/** 邀请失败 */
	public static final String IS_INVITE_ERROR = "530005";
	/** 问题id未获取到 */
	public static final String IS_PARAMETER_ERROR = "530006";
	//-------------------------获取评论信息----------------------------------------------
	/** 不属于该班级 */
	public static final String IS_NOT_CLASS_USER = "534001";
	/** 获取评论列表失败 */
	public static final String GET_COMMENT_ERROR = "534002";
	/** 获取通知列表失败 */
	public static final String GET_INFORM_ERROR = "534003";
	/** 获取赛事交流列表失败 */
	public static final String COMPETITION_DISCUSSION = "534004";
	/** 获取话题交流列表失败 */
	public static final String TOPIC_DISCUSSION = "534005";
	//-------------------------提交评论信息-------------------------------------------
	/** 您不属于该培训班 */
	public static final String IS_NOT_CLASS = "535001";
	//---------------------------提交计划任务信息--------------------------------------------------
	/** 您不是该班级的教师 */
	public static final String IS_ERROR_CLASS = "533001";
	/** 该班级已经在计划日期发布过任务 */
	public static final String CLASS_IS_ISSUE = "533002";
	/** 计划任务不能早于当前时间 */
	public static final String TASKTIME = "533003";
	/** 任务内容字数在2-140字 */
	public static final String TASKCONTEXT = "533004";
	/** 任务目标字数在2-140字 */
	public static final String TASKGOAL = "533005";
	/** 任务内容不能包含特殊字符 */
	public static final String TASKCONTEXTSTANDARD = "533006";
	/** 任务目标不能包含特殊字符 */
	public static final String TASKGOALSTANDARD = "533007";
	//------------------------------获取计划任务信息------------------------------------------------
	/** 您不是该班级的学生或者教师 */
	public static final String NOT_ERROR_USER_TEACH = "532001";
	/** 查询的时间段内没有计划任务信息 */
	public static final String SELECT_DATE_NOT_MESSAGE = "532002";
	//----------------------------获取学校信息------------------------------------------
	/** 获取学校列表失败 */
	public static final String GET_CLASS_LIST_ERROR = "536001";
	//-----------------------------对信息主体的删除操作---------------------------------------------------------
	/** 要删除的信息主体不存在 */
	public static final String DEL_MESSAGE_NOT = "531001";
	/** 您不是该信息主体的创建人 */
	public static final String NOT_MESSAGE_CREATE = "531002";
	/** 班级不存在 */
	public static final String ERROR_CLASS_NOT = "531003";
	/** 班级中存在学员不可删除 */
	public static final String ERROR_CLASS_USER = "531004";
	/** 您不是该班级的教师 */
	public static final String USER_NOT_CLASS_TEACH = "531005";
	/** 没有删除权限 */
	public static final String NOT_DEL_JURISDICTION = "531006";
	/** 官方课程不能删除 */
	public static final String NOT_OFFICIAL_ERROR = "531007";
	/** 课程已结束不能删除作业 */
	public static final String COURSE_ERROR_TIME = "531008";
	/** 相册里面有照片不能删除 */
	public static final String PHOTO_NOT_DEL = "531009";
	/** 请求参数错误 */
	public static final String REQUEST_PARAMETER_ERROR = "531010";
	/** 您不是该信息主体的创建人或不是教师 */
	public static final String NOT_MESSAGE_CREATE_TEACHER = "531011";
	/** 已经有人签到,不能删除计划任务! */
	public static final String IS_NOT_MESSAGE_SIGN_IN = "531012";
	/** 只有培训班创建人可以删除! */
	public static final String IS_CLUBCLASS_SIGN_IN = "531013";
	
	
	
	//-----------------------------获取消息信息---------------------------------------------------------
	//请求参数传递错误
	public static final String GET_MESSAGE_DATA_ERROR="585000";
	//用户非会员
	public static final String GET_MESSAGE_NOT_MEMBER="585001";
	//成功
	public static final String GET_MESSAGE_SUCCESS="585002";
	
	//----------------------------获取俱乐部权益信息---------------------------------------------------
	//请求参数传递错误
	public static final String GET_CLUB_RIGHTS_DATA_ERROE="586000";
	//成功
	public static final String GET_CLUB_RIGHTS_SUCCESS="585001";
	
	//----------------------------购买俱乐部权益信息---------------------------------------------------
	//请求参数传递错误
	public static final String BUY_CLUB_RIGHTS_DATA_ERROE="587000";
	//成功
	public static final String BUY_CLUB_RIGHTS_SUCCESS="587001";
	//已经购买过官方课程包
	public static final String BUY_CLUB_TRAN_IS_BUY="587002";
	//已经购买赛事运营权
	public static final String BUY_CLUB_MATCH_IS_BUY="587003";
	//已经购买权益
	public static final String BUY_CLUB_RIGHT_IS_BUY="587004";
	//用户非会员
	public static final String BUY_CLUB_NOT_MEMBER="587005";
	//-----------------------------获取俱乐部或个人的账务收支信息-----------------------------------------
	//请求参数传递错误
	public static final String GET_CLUB_ACCOUNT_RIGHTS_DATA_ERROE="588000";
	//成功
	public static final String GET_CLUB_ACCOUNT_RIGHTS_SUCCESS="588001";
	//用户非会员
	public static final String GET_CLUB_ACCOUNT_NOT_MEMBER="588002";
	
	//-----------------------------获取提现信息------------------------------------------------------------
	//请求参数传递错误
	public static final String GET_CASH_RIGHTS_DATA_ERROE="589000";
	//成功
	public static final String GET_CASH_RIGHTS_SUCCESS="589001";
	//用户非会员
	public static final String GET_CASH_NOT_MEMBER="588902";
		
	//-----------------------------提交提现申请-----------------------------------------------------------
	//请求参数传递错误
	public static final String APPLY_CASH_RIGHTS_DATA_ERROE="590000";
	//成功
	public static final String APPLY_CASH_RIGHTS_SUCCESS="590001";
	//用户非会员
	public static final String APPLY_CASH_NOT_MEMBER="590002";	
	//验证码失效
	public static final String APPLY_CASH_VALIDATE_ERROR="590003";	
	
	//----------------------------获取账户类型标识信息---------------------------------------------------
	//请求参数传递错误
	public static final String GET_ACCOUNT_TYPE_DATA_ERROE="591000";
	//成功
	public static final String GET_ACCOUNT_TYPE_SUCCESS="591001";
	//用户非会员
	public static final String GET_ACCOUNT_TYPE_NOT_MEMBER="591002";
	
	//----------------------------发心情-----------------------------------------------------------------
	//请求参数传递错误
	public static final String SUBMIT_MOOD_TYPE_DATA_ERROE="580000";
	//成功
	public static final String SUBMIT_MOOD_TYPE_SUCCESS="580001";
	//用户非会员
	public static final String SUBMIT_MOOD_TYPE_NOT_MEMBER="580002";
	//请输入2-70个字
	public static final String SUBMIT_MOOD_TYPE_TEXT_LEN="580003";
	
  //------------------------------各种提示红点判断是否显示---------------------------------------------
	//请求参数传递错误
	public static final String GET_RED_DATA_ERROE="584000";
	//成功
	public static final String GET_RED_SUCCESS="584001";
	//用户非会员
	public static final String GET_RED_NOT_MEMBER="584002";
   //-----------------------------提交附件的OSS信息-----------------------------------------------------
	//请求参数传递错误
	public static final String SUBMIT_OSS_DATA_ERROE="599000";
	//成功
	public static final String SUBMIT_OSS_SUCCESS="599001";
		
	//----------------------------提交登录用户的信息-----------------------------------------------------
	/**请求参数传递错误*/
	public static final String SUBMIT_USER_DATA_ERROE="510100";
	/**成功*/
	public static final String SUBMIT_USER_SUCCESS="510101";	
	/**请输入2-10字*/
	public static final String SUBMIT_USER_CHAR_LEN="510102";
	/**昵称已被占用，请重新输入*/
	public static final String SUBMIT_USER_NICKNAME_REPLACE="510103";
	/**用户名格式不正确*/
	public static final String SUBMIT_USER_USERNAME_REPLACE="510104";
	/**请输入70字以内*/
	public static final String SUBMIT_USER_CHAR_LEN_UNIT="510105";
	/**昵称格式不正确*/
	public static final String SUBMIT_USER_NICKNAME="510106";
	/**该用户名已存在*/
	public static final String SUBMIT_USER_USERNAME_="510107";
	/** 用户名过长 */
	public static final String SUBMIT_USER_LENGTH_="510108";
	//----------------------------------验证手机号是否重复--------------------------------------------------
	/** 输入的手机号码不规则 */
	public static final String PHONE_NO_RULE = "617001";
	/** 请填写手机号 */
	public static final String IMPUT_PHONE = "617002";
	/** 手机号已被注册 */
	public static final String PHONE_REGISTER = "617003";
	/** 不能与原手机号重复 */
	public static final String PHONE_REPETITION = "617004";
	
	
}