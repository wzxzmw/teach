package com.seentao.stpedu.constants;

import com.seentao.stpedu.common.entity.ArenaAd;
import com.seentao.stpedu.common.entity.ArenaCompetition;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.common.entity.ArenaGuessTopic;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMain;
import com.seentao.stpedu.common.entity.ClubTrainingQuestion;
import com.seentao.stpedu.common.entity.ClubTrainingQuestionAnswer;
import com.seentao.stpedu.common.entity.TeachClass;
import com.seentao.stpedu.common.entity.TeachCourse;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.entity.TeachCourseChapter;
import com.seentao.stpedu.common.entity.TeachQuestion;
import com.seentao.stpedu.common.entity.TeachQuestionAnswer;
import com.seentao.stpedu.common.entity.TeachRelCardCourse;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.error.AppErrorCode;

/**
 * 平台全局常量类
 */
public class GameConstants {

	// --------------------------------------通用------------------------------------------------
	/** 没有前端要的参数返回-1 */
	public static final int NOT_PARAMETER = -1;
	/** 错误返回 */
	public static final String CODE = "code";
	/** 错误信息code */
	public static final String MSG = "msg";
	/** 结果返回 */
	public static final String RESULT = "result";
	/**结果返回成功*/
	public static final int RESULT_SUCCESS = 0;
	/** 通用的判断 1代表是，0代表否 */
	public static final int YES = 1;
	public static final int NO = 0;
	/**购买官方课程包所需要的一级货币 */
	public static final int OFFICIAL_COURSE_COST = 20;
	/** 会长 */
	public static final int PRESIDENT = 1;
	/** 教练 */
	public static final int COACH = 2;
	/** 会员 */
	public static final int MEMBER = 3;
	/** 账户类型  1:一级货币*/
	public static final int STAIR_ONE = 1;
	/** 账户类型  2:二级货币*/
	public static final int STAIR_TWO = 2;
	/** 货币变动表类型 1:收入 */
	public static final int INCOME = 1;
	/** 货币变动表类型2:支出 */
	public static final int SPENDING = 2;
	/** 货币变动表类型 3:提现 */
	public static final int WITHDRAWAL = 3;
	/** 货币变动表状态 1:成功 */
	public static final int SUCCESSFUL = 1;
	/** 货币变动表状态2:失败 */
	public static final int FAILURE = 2;
	/** 货币变动表状态3:过程中 */
	public static final int PROCESS = 3;
	/** 是否删除 1：是 */
	public static final int YES_DEL = 1;
	/** 是否删除 0：否 */
	public static final int NO_DEL = 0;
	/** 多线程方法   执行失败 */
	public static final String ERROR_THREAD_RUN = "ERROR_THREAD_RUN";
	/** 多线程方法   执行成功 */
	public static final String SUCCESS_THREAD_RUN = "SUCCESS_THREAD_RUN";
	
	/** 逻辑业务标识  移动端标识 */
	public static final String TYPE_APP = "APP";
	/** 逻辑业务标识  pc端标识 */
	public static final String TYPE_PC = "PC";
	/** api文件保存路径 */
	public static final String JSONAPI_PATH = "api/";
	/** api规范起点key */
	public static final String JSONAPI_KEY = "result";
	/** 管理后端 */
	public static final int TYPE_MANAGER = 1;
	/** 显示前端 */
	public static final int TYPE_FRONT = 2;
	
	
	/** 分页逻辑中查询关键字 ids*/
	public static final String QUERYPAGE_IDS_KEY = "ids_key";
	
	//--------------------------------------------日志记录类型------------------------------------------------------
	/** 插入成功 */
	public static final String SUCCESS_INSERT = " 插入成功 ";
	/** 修改成功 */
	public static final String SUCCESS_UPDATE = " 修改成功 ";
	/** 删除成功 */
	public static final String SUCCESS_DELETE = " 删除成功 ";


	//---------------------------------功能及设计对象类型--------------------------------------------
	/** 作业文本类型  */
	public static final int TEACHER_HOMEWORK_TYPE_TEXT  = 3;
	/** 教师作业文本10分制  */
	public static final int TEACHER_HOMEWORK_SCORE  = 10;


	//----------------------------------------附件--------------------
	/** 视频	附件一级分类  */
	public static final int ATTACHMENT_VIDEO  = 1;
	/** 视频	音频  */
	public static final int ATTACHMENT_AUDIO  = 2;
	/** 视频	其他  */
	public static final int ATTACHMENT_OTHER  = 3;
	

	//--------------------------------------特殊符号--------------------------------------
	public static final String UNDERLINE="_";

	public static final String COMMA=",";

	//--------------------------------------关注类型--------------------------------------
	//-----数据库对应--------
	/** 俱乐部2 */
	public static final int CLUBMAIN = 2;
	/** 人1 */
	public static final int USER_OBJ = 1;
	/** 企业3 */
	public static final int ATTENTION_ENTERPRISE = 3;
	/** 赛事4 */
	public static final int ATTENTION_COMPETITION = 4;
	//-------------------
	/** 操作类型 ：1加关注 */
	public static final int WITH_FOCUS_ON = 1;
	/** 操作类型 ：2取消关注 */
	public static final int CANCEL_THE_ATTENTION = 2;
	
	/**1:关注的俱乐部；*/
	public static final int ATTENTION_CLUB = 1;
	/**2:关注的人*/
	public static final int ATTENTION_USER = 2;
	/**3:关注的企业*/
	public static final int IS_ATTENTION_ENTERPRISE = 3;
	/**4:关注的赛事 */
	public static final int ARRENTION_COMPETITION = 4;
	//------------消息表消息类型-1:关注消息，2:邀请回答问题，3:邀请比赛，4：系统消息。-----------------------------------
	/** 1:关注消息 */
	public static final int ATTENTION_MESSAGE = 1;
	/**2:邀请回答问题 */
	public static final int INVITATION_PROBLEM = 2;
	/**3:邀请比赛*/
	public static final int INVITATION_GAME = 3;
	/** 4：系统消息*/
	public static final int SYSTEM_MESSAGE = 4;
	//-------------------消息表关联对象类型---1:关注，2:教学问题，3:培训班问题，4:比赛。---------------------------
	/** 1:关注 */
	public static final int FOCUS_ON = 1;
	/**2:教学问题 */
	public static final int TEACHING_PROBLEMS = 2;
	/**3:培训班问题*/
	public static final int  TRAINING_THE_PROBLEM = 3;
	/** 4：俱乐部比赛*/
	public static final int  CLUB_GAME = 4;
	/** 5：学校比赛*/
	public static final int  SCHOOL_GAME = 5;
	//--------------------------------------查询类型--------------------------------------
	/** 1 班机id*/
	public static final int QUERY_TYPE_ONE = 1;
	/** 2  章节id */
	public static final int QUERY_TYPE_TWO = 2;
	/** 3 课程卡id  */
	public static final int QUERY_TYPE_THREE = 3;
	/** 4 表示俱乐部请求  */
	public static final int QUERY_TYPE_FOUR = 4;

	//----------------------------------------分页----------------------------------
	/** 分页查询查询dao路径 */
	public static final String QUERYPAGE_PPATH_PACKAGE  = "com.seentao.stpedu.common.dao.";
	/** 分页查询 count 持久化方法 “queryCount” */
	public static final String QUERYPAGE_QUERYCOUNT  = "queryCount";
	/** 分页查询 查询持久化方法  查询整个对象“queryByPage” */
	public static final String QUERYPAGE_QUERYBYPAGE  = "queryByPage";
	/** 分页查询 查询持久化方法 只查询id “queryByPageids” */
	public static final String QUERYPAGE_QUERYBYPAGE_IDS  = "queryByPageids";

	//------------------------------------------redis封装对象查询-----------------------------------------------
	/** redis一级目录 */
	public static final String REDIS_PATH = "stpedu:";
	/** 课程统计redis前缀*/
	public static final String COURSE_PRE = "course:";
	/** 学生附件关系表 */
	public static final String REDIS_COURSE_ATTA = "TeachRelStudentAttachment";
	public static final String REDIS_COURSE_CLASS = "TeachRelClassCourse";
	public static final String REDIS_COURSE_CARD = "TeachRelCardCourse";
	public static final String REDIS_COURSE_COUNT = "TeachRelStudentCourseCount";
	/** 用户校验注册*/
	public static final String REDIS_USER="user_phone:";
	/**用户投票*/
	public static final String REDIS_HASH_VOTE="user_hash_vote:";
	/**用户投票zset*/
	public static final String REDIS_ZSET_VOTE="user_zset_vote:";
	/**投票备份redis*/
	public static final String REDIS_HASH_BACKUP_VOTE="user_back_vote:";
	/** redis组件dao路径 */
	public static final String REDIS_PPATH_PACKAGE  = "com.seentao.stpedu.common.dao.";
	/** redis组件 对象查询 方法 “getEntityForDB” */
	public static final String REDIS_QUERY_ENTITY  = "getEntityForDB";

	//--------------------------------------------教师作业--------------------------------------------------------------
	/** 当前教师作业文本列表查询 */
	public static final int TEACHER_HOMEWORK_QUERY_PAGE  = 1;
	/** 教师作业文本查询 */
	public static final int TEACHER_HOMEWORK_QUERY_ENTITY  = 2;
	/** 教师作业文本 显示类型		必修 */
	public static final int TEACHER_HOMEWORK_REQUIRED  = 1;
	/** 教师作业文本 显示类型		选修 */
	public static final int TEACHER_HOMEWORK_ELECTIVE  = 2;
	/** 教师作业文本 显示类型		隐藏 */
	public static final int TEACHER_HOMEWORK_HIDE  = 3;

	//-----------------------------邀请其他人或推送俱乐部回答问题操作--------------------------------------------------
	
	/** 1:邀请人 */
	public static final int INVITATION_PEOPLE = 1;
	/** 2:推送俱乐部 */
	public static final int PUSH_CLUB = 2;
	//---------------------------------------------用户类型--------------------------------------------------------------------
	/** 用户类型：教师:1 */
	public static final int USER_TYPE_TEAHCER = 1;
	/** 用户类型：学生:2 */
	public static final int USER_TYPE_STUDENT = 2;
	/** 用户类型：普通用户:3*/
	public static final int USER_TYPE_USER = 3;
	//---------------------------------------------是否为默认班级------------------------------------------------------------
	/** 是 1*/
	public static final int ARE = 1;
	/** 否 0*/
	public static final int DENY = 0;
	//-------------------------------------教师------------------------------------------------
	/** 教师对学生上传作业的评分 */
	public static final int TEACHER_SUBMITSCORE_HOMEWORK = 1;
	/** 教师对班级学生的评分 */
	public static final int TEACHER_SUBMITSCORE_CLASS = 2;

	//---------------------------------------学生认证操作---------------------------------------------------
	/** 添加认证资格	*/
	public static final int STUDENT_ACTIONTYPE_ADD = 1;
	/** 取消认证资格	*/
	public static final int STUDENT_ACTIONTYPE_DEL = 2;
	/** 申请证书		*/
	public static final int STUDENT_ACTIONTYPE_VIEW = 3;

	//-----------------------------------------------获取答疑信息-------------------------------------------------------
	/** 获取疑问列表		*/
	public static final int STUDENT_QUESTION_PAGE = 1;
	/** 获取疑问详情		*/
	public static final int STUDENT_QUESTION_ENTITY = 2;

	/** 移动端: 问题的回复列表信息 */
	public static final int QUESTION_ANSWER_TYPE = 1;

	//-------------------------------------问题精华-------------------
	/**  提交问题信息	默认无精华 */
	public static final int QUESTION_AND_ANSWER_ESSENCE = 1;
	/**  提交问题信息	默认无精华 */
	public static final int QUESTION_AND_ANSWER_NO_ESSENCE = 0;



	//----------------------对信息主体进行点赞加精等态度操作----------------------------------
	//---------------attiMainType------------------		
	/**1:答疑的问题  */
	public static final int MANNER_ANSWER_ISSUE = 1;
	/**2:答疑的回答  */
	public static final int MANNER_QUESTIONS_ANSWER = 2;
	/**3:计划任务  */
	public static final int MANNER_SCHEDULED_TASK = 3;
	/**4:讨论(群组)*/
	public static final int MANNER_DISCUSSION_GROUP = 4;
	/**5:通知*/
	public static final int MANNER_INFORM = 5;
	/**6:话题*/
	public static final int MANNER_GAMBIT = 6;
	/**7:心情*/
	public static final int MANNER_MOOD = 7;
	/**8:企业*/
	public static final int MANNER_ENTERPRISE = 8;
	/**9:人员*/
	public static final int MANNER_USER = 9;
	/**10:相册图片*/
	public static final int MANNER_PHOTO_ALBUM = 10;
	/**11:话题的评论*/
	public static final int MANNER_TOPIC = 11;
	//---------------actionType 1:加精；2:点赞；3:签到；4:置顶；5:取消置顶；6:点踩-----------------------
	/**1:加精*/
	public static final int MANNER_ADD_CREAM = 1;
	/**2:点赞*/
	public static final int MANNER_THUMB_UP = 2;
	/**3:签到*/
	public static final int MANNER_SIGN_IN = 3;
	/**4:置顶*/
	public static final int MANNER_STICK = 4;
	/**5:取消置顶*/
	public static final int MANNER_CANCEL_STICK = 5;
	/**6:点踩*/
	public static final int MANNER_DOT_TRAMPLE = 6;
	//-----------------------------平台模块----------------------------------------------
	/** 教学 1*/
	public static final int MANNER_TEACHING = 1;
	/** 竞技场 2*/
	public static final int MANNER_ARENA = 2;
	/** 俱乐部 3*/
	public static final int MANNER_CLUB = 3;
	/** 是否已经加精 1：是 */
	public static final int IS_CREAM = 1;
	/** 是否已经加精 0：否 */
	public static final int NO_CREAM = 0;
	//-----------------------------态度表关联对象类型--------------------------------------
	/** 1.课程问题，*/
	public static final int COURSE_ISSUE = 1;
	/** 2:课程回答， */
	public static final int COURSE_ANSWER = 2;
	/** 3:课程讨论， */
	public static final int COURSE_DISCUSSION = 3;
	/** 4:赛事讨论， */
	public static final int COMPETITION_DISCUSSION = 4;
	/** 5:培训班讨论， */
	public static final int TRAIN_DISCUSSION = 5;
	/** 6:培训班问题， */
	public static final int TRAIN_ISSUE = 6;
	/** 7:培训班回答， */
	public static final int TRAIN_ANSWER = 7;
	/** 8:个人心情， */
	public static final int PERSONAL_MOOD = 8;
	/** 9:相册照片*/
	public static final int PHOTO_ALBUM = 9;
	/** 10:俱乐部话题， */
	public static final int CLUB_TOPIC = 10;
	/** 11:企业*/
	public static final int ENTERPRISE = 11;
	/** 12:人员 */
	public static final int PERSONNEL = 12;
	/** 13:话题评论内容 */
	public static final int TOPICCONTENT = 13;
	//----------态度类型-----------------
	/** 1:赞 */
	public static final int PRAISE = 1;
	/** 2：踩 */
	public static final int TRAMPLE = 2;
	//---------------------------------竞猜话题--------------------------------------------
	/** 竞猜主题状态  开始 */
	public static final int GUESS_START_TOPIC = 1;
	/** 竞猜主题状态  结束 */
	public static final int GUESS_END_TOPIC = 2;

	/** 竞猜状态  开始中 */
	public static final int GUESS_START = 1;
	/** 竞猜状态  结束 */
	public static final int GUESS_END = 2;
	/** 竞猜状态  分配完成 */
	public static final int GUESS_FINISH = 3;
	/** 竞猜 手续费 百分比 */
	public static final double GUESS_CUT = 0.05;
	
	
	
	//-----------------------------------------俱乐部--------------------------------------------------------
	/** 赛事运营权限	没有购买 */
	public static final int CLUB_NOT_COMPETITION = 0;
	/** 赛事运营权限	有购买 */
	public static final int CLUB_BUY_COMPETITION = 1;
	/*
	 * 俱乐部状态
	 */
	/**审核中**/
	public static final int CLUB_STATUS_ONE = 1;
	/**审核通过**/
	public static final int CLUB_STATUS_TWO = 2;
	/**审核未通过**/
	public static final int CLUB_STATUS_THREE = 3;
	
	//-------------------------------------获取课程信息--------------------------------------------
	/** 教学班信息列表获取   (根据章节id和所属课程卡类型获取课程列表)*/
	public static final int COURSE_LIST = 1;
	/** 教学班信息列表获取   (根据章节id和所属课程卡类型获取课程列表)*/
	public static final int COURSE_CLASS_LIST = 2;
	/** 根据课程id获取课程详情 */
	public static final int COURSE_CLASS_INFO = 3;
	
	//----------------------------------获取班级信息--------------------------------------------------
	
	/** 1：学生端，显示可加入的班级列表 */
	public static final int YES_ADD_CLASS = 1;
	/** 2：教师端，显示自己创建的班级列表 */
	public static final int ONESELF_CLASS = 2;
	/** 3：通过班级id获取唯一一条班级信息 */
	public static final int ACQUISITION_ONE_CLASS = 3;
	/** 4：教学模块下的学校菜单，点击学校进入该学校下的班级排名列表页时调用(获取教学班) */
	public static final int RANKING_CLASS = 4;
	/** 5：获取俱乐部下的培训班列表 */
	public static final int CLUB_CLASS = 5;
	/** 6:教学模块下，自由用户点击“课程”菜单会显示全站的培训班信息 */ 
	public static final int COURSE_TRAINING_CLASS = 6;
	/** TRAIN_CLASS 7:获取当前登录者参加的培训班列表 */ 
	public static final int TRAIN_CLASS = 7;
	/** 班级类型 1：教学班 */
	public static final int TEACHING_CLASS = 1;
	/** 班级类型 2：俱乐部培训班 */
	public static final int CLUB_TRAIN_CLASS = 2;
	
	/** 竞猜结果  公布时间 一个小时 */
	public static final int GUESS_RESULT_TIME = 1000*60*60;//1000*60*60;
	/** 竞猜结果  未公布 */
	public static final int GUESS_RESULT_NOT = 0;
	/** 竞猜结果  正方胜利 */
	public static final int GUESS_RESULT_SQUARE = 1;
	/** 竞猜结果  反方胜利 */
	public static final int GUESS_RESULT_CON_SIDE = 2;
	/** 竞猜结果  无效结果 */
	public static final int GUESS_RESULT_INVALID = 3;
	/** 竞猜类型  普通 */
	public static final int GUESS_ORDINARY = 1;
	/** 竞猜类型  坐庄 */
	public static final int GUESS_LANDLORD = 2;
	/** 竞猜压注 方向   能 */
	public static final int GUESS_CAN = 1;
	/** 竞猜压注 方向   不能 */
	public static final int GUESS_CANNOT = 2;
	/** 竞猜结果 方向   无效 */
	public static final int GUESS_INVALID = 3;
	
	/** 竞猜 当前登陆者是 庄家 */
	public static final int GUESS_IS_BANKER = 1;
	/** 竞猜 当前登陆者不是 庄家 */
	public static final int GUESS_NOT_BANKER = 0;
	/** 竞猜 当前登陆 已经投注 */
	public static final int GUESS_IS_BET = 1;
	/** 竞猜 当前登陆 没有投注 */
	public static final int GUESS_NOT_BET = 1;
	/** 竞猜类型	竞猜话题信息 */
	public static final int GUESS_TOPIC = 1;
	/** 竞猜类型	我参加的竞猜 */
	public static final int GUESS_MY = 2;
	/** app 竞猜类型	获取竞猜赛事信息 */
	public static final int GUESS_TOPIC_APP = 1;
	/** app 竞猜类型	我参加的竞猜 */
	public static final int GUESS_MY_APP = 2;
	
	

	/** 是否已加入该培训班 1：已加入 */
	public static final int IS_ADD_CLASS = 1;
	/** 是否已加入该培训班 0：未加入 */
	public static final int NOT_ADD_CLASS = 0;
	/** 官方课程 1 */
	public static final int OFFICIAL_COURSE = 1;
	/** 案例课程 5 */
	public static final int CASE_COURSE = 5;
	/** 竞猜坐庄 最小赔率设置 */
	public static final double GUESS_ODDS_MIN = 0.1d;
	/** 竞猜坐庄 最大赔率设置 */
	public static final double GUESS_ODDS_MAX = 9.9d;

	/** 竞猜坐庄 最低金钱投入 */
	public static final double GUESS_MONEY_MIN = 1000d;



	//------------------------------------赛事-----------------------------------------------------

	/** 赛事 提交类型   提交赛事图片 */
	public static final int GAMEEVENT_TYPE_IMG = 1;
	/** 赛事 提交类型   提交赛事基本信息 */
	public static final int GAMEEVENT_TYPE_BASE = 2;
	


	//------------------------------------俱乐部会员----------------------------------------------------
	/** 俱乐部会员表	会员级别	会长	 */
	public static final int CLUB_MEMBER_LEVEL_PRESIDENT = 1;
	/** 俱乐部会员表	会员级别	教练	 */
	public static final int CLUB_MEMBER_LEVEL_COACH = 2;
	/** 俱乐部会员表	会员级别	会员	 */
	public static final int CLUB_MEMBER_LEVEL_MEMBER = 3;
	/** 俱乐部会员表	会员状态	加入 */
	public static final int CLUB_MEMBER_STATE_JOIN = 1;
	/** 俱乐部会员表	会员状态	已退出 */
	public static final int CLUB_MEMBER_STATE_OUT = 4;
	/** 俱乐部会员表	 会员状态    审核中*/
	public static final int CLUB_MEMBER_STATE_SH = 2;	
	/** 俱乐部会员表	 会员状态    审核失败*/
	public static final int CLUB_MEMBER_STATE_FIAL = 3;
	/** 俱乐部会员表	 会员状态  未加入*/
	public static final int CLUB_MEMBER_STATE_NOJOIN = 4;


	//-----------------------------------------货币锁定常量-----------------------------------------------------
	/**锁定中*/
	public static final String CENTER_MONEY_LOCK_ONE = "1";
	/**已解锁*/
	public static final String CENTER_MONEY_LOCK_TWO = "2";
	/**已扣除 */
	public static final String CENTER_MONEY_LOCK_THREE = "3";
	/**竞猜*/
	public static final String CENTER_MONEY_LOCK_T = "1";
	/**提现*/
	public static final String CENTER_MONEY_LOCK_TT = "2";


	//----------------------------------------------竞猜结果公布------------------------------------------------
	/** 竞猜结果公布	赢 */
	public static final int GUESS_RESULT_WIN = 1;
	/** 竞猜结果公布	输 */
	public static final int GUESS_RESULT_FAIL = 2;
	
	//------------------------------------------------货币变动表---------------------------------------------------------

	/** 变动类型。1:收入，2:支出，3:提现。*/
	public static final int MONEY_CHANGE_INCOME = 1;
	public static final int MONEY_CHANGE_EXPENDITURE = 2;
	public static final int MONEY_CHANGE_WITHDRAWALS = 3;
	/** 关联对象类型。1.竞猜，2:红包，3：兑换，4：提现，5：充值，6：购买，7：加入俱乐部，8：加入培训班。*/
	public static final int MONEY_CHANGE_LINK_TYPE_GUESS = 1;
	public static final String MONEY_CHANGE_LINK_TYPE_GUESS_CN = "竞猜";
	
	public static final int MONEY_CHANGE_LINK_TYPE_RED = 2;
	public static final String MONEY_CHANGE_LINK_TYPE_RED_CN = "红包";
	
	public static final int MONEY_CHANGE_LINK_TYPE_EXC = 3;
	public static final String MONEY_CHANGE_LINK_TYPE_EXC_CN = "兑换";
	
	public static final int MONEY_CHANGE_LINK_TYPE_CASH = 4;
	public static final String MONEY_CHANGE_LINK_TYPE_CASH_CN = "提现";
	
	public static final int MONEY_CHANGE_LINK_TYPE_RECH = 5;
	public static final String MONEY_CHANGE_LINK_TYPE_RECH_CN = "充值";
	
	public static final int MONEY_CHANGE_LINK_TYPE_BUY = 6;
	public static final String MONEY_CHANGE_LINK_TYPE_BUY_CN = "购买";
	
	public static final int MONEY_CHANGE_LINK_TYPE_CLUB = 7;
	public static final String MONEY_CHANGE_LINK_TYPE_CLUB_CN = "加入俱乐部";
	
	public static final int MONEY_CHANGE_LINK_TYPE_TRAINING = 8;
	public static final String MONEY_CHANGE_LINK_TYPE_TRAINING_CN = "加入培训班";
	
	/**支付方式 */
	public static final String BUY_TYPE_ALIBA="支付宝";
	public static final String BUY_TYPE_TENCENT="微信";
	/** 状态。1:成功，2:失败，3:过程中。 */
	public static final int MONEY_CHANGE_STATE_SUCCESS = 1;
	public static final int MONEY_CHANGE_STATE_FAIL = 2;
	public static final int MONEY_CHANGE_STATE_PROCESS = 3;
	


	//-------------------------------------------------结束竞猜话题或竞猜-----------------------------------------------------------------
	/** 结束竞猜话题或竞猜	1:结束竞猜话题； */
	public static final int FINISHQUIZ_GUESS_TOPIC = 1;
	/** 结束竞猜话题或竞猜	 2:结束竞猜； */
	public static final int FINISHQUIZ_GUESS = 2;

	//---------------------------------------------------获取竞猜话题信息--------------------------------------------------
	/** 获取竞猜话题信息	最热竞猜话题	类型  */
	public static final int QUIZ_TOPIC_TYPE_HOT = 1;
	/** 获取竞猜话题信息 	最新竞猜话题	类型  */
	public static final int QUIZ_TOPIC_TYPE_NEW = 2;
	/** 获取竞猜话题信息	我参与的竞猜话题	类型  */
	public static final int QUIZ_TOPIC_TYPE_MY = 3;
	/** 获取竞猜话题信息	 竞猜话题详情	类型  */
	public static final int QUIZ_TOPIC_TYPE_INFO = 4;
	
	//---------------------------------------------------获取俱乐部类型--------------------------------------------------
		/** 我的俱乐部主页，获取最热的话题	类型  */
		public static final int CLUB_TOPIC_TYPE_HOT = 1;
		/** 我的俱乐部主页，获取最新的话题	类型  */
		public static final int CLUB_TOPIC_TYPE_NEW = 2;
		/** 通过话题id获取唯一一条话题信息	类型  */
		public static final int CLUB_TOPIC_TYPE_MY = 3;
		/** 俱乐部后台管理中的话题管理列表页调用	类型  */
		public static final int CLUB_TOPIC_TYPE_INFO = 4;
		/** 个人中心，通过人员id获取某个人员的话题	类型  */
		public static final int CENTER_TOPIC_TYPE_INFO = 5;
		
//---------------------------------------------------提醒的显示分类--------------------------------------------------
		/** 大家的提醒 */
		public static final int CLUB_REMIND_TYPE_EVERYONE = 1;
		/** 提醒我的	类型  */
		public static final int CLUB_REMIND_TYPE_MY = 2;
		/** 我的提醒	类型  */
		public static final int CLUB_REMIND_TYPE_ME = 3;
//---------------------------------------------------心情的显示分类--------------------------------------------------
		/** 我的俱乐部主页，获取大家的心情 */
		public static final int CLUB_MOOD_TYPE_EVERYONE = 1;
		/** 我的俱乐部主页，获取当前登录者的心情  */
		public static final int CLUB_MOOD_TYPE_MY = 2;
		/** 个人中心，通过人员id获取某个人员的心情 */
		public static final int CLUB_MOOD_TYPE_ME = 3;
//---------------------------------------------------通知的显示分类--------------------------------------------------
		/**我的俱乐部主页，获取通知信息列表 */
		public static final int CLUB_NOTICE_TYPE_INFO = 1;
		
//---------------------------------------------------提醒对象类型--------------------------------------------------
		/** 全部会员 */
		public static final int REMIND_OBJECT_ALL = 1;
		/** 指定的人  */
		public static final int REMIND_OBJECT_OTHER = 2;
			
		
//---------------------------------------------------关注关联对象类型--------------------------------------------------
		/** 人 */
		public static final int REL_OBJECT_TYPE_USER = 1;
		/** 俱乐部  */
		public static final int REL_OBJECT_TYPE_CLUB = 2;
		/** 企业  */
		public static final int REL_OBJECT_TYPE_COMPANY = 3;
		
		/**已关注*/
		public static final int REL_OBJECT_ALREADY_CONCERNED  = 1;
		/**未关注*/
		public static final int REL_OBJECT_NOT_CONCERNED  = 0;
		/**全部*/
		public static final int REL_OBJECT_CLUB_ALL  = -1;
		

//---------------------------------------------------查询企业类型--------------------------------------------------
		/** 职位模块的调用  */
		public static final int GET_COMPANY_ONE = 1;
		/** 个人中心->我关注的企业调用*/
		public static final int GET_COMPANY_TWO = 2;
//---------------------------------------------------查询动态类型--------------------------------------------------
		/** 我的俱乐部主页，获取俱乐部的动态  */
		public static final int GET_ATTITUDE_ONE = 1;
		/** 个人中心的动态，显示人员id对应的动态*/
		public static final int GET_ATTITUDE_TWO = 2;
		/** 动态模块的班级动态*/
		public static final int GET_ATTITUDE_THREE = 3;
		/**动态模块的俱乐部动态*/
		public static final int GET_ATTITUDE_FOUR = 4;
		/** 动态模块的个人动态*/
		public static final int GET_ATTITUDE_FIVE = 5;
		/** 移动端：我的俱乐部主页，获取俱乐部的动态  */
		public static final int GET_ATTITUDE_SIX = 6;
		/** 移动端：动态模块的个人动态*/
		public static final int GET_ATTITUDE_SEVEN = 7;
//---------------------------------------------------动态类型--------------------------------------------------
		/** 俱乐部话题	类型  */
		public static final int LIVE_TYPE_TOPIC = 1;
		/** 俱乐部擂台	类型  */
		public static final int LIVE_TYPE_ARENA = 2;
		/** 俱乐部提醒	类型  */
		public static final int LIVE_TYPE_REMIND = 4;
		/** 俱乐部通知	类型  */
		public static final int LIVE_TYPE_NOTICE = 5;
		/** 教学答疑	类型  */
		public static final int LIVE_TYPE_QUESTION = 9;
		/** 教学实训	类型  */
		public static final int LIVE_TYPE_PRATICE = 10;
		/**加入俱乐部**/
		public static final int LIVE_TYPE_JOIN = 14;
		/**用户参加比赛**/
		public static final int LIVE_TYPE_GAME = 15;
		
		
//---------------------------------------------------动态主体类型--------------------------------------------------
		/** 俱乐部话题	类型  */
		public static final int LIVE_MAIN_TYPE_TOPIC = 1;
		/** 俱乐部比赛	类型  */
		public static final int LIVE_MAIN_TYPE_GAME = 2;
		/** 俱乐部话题	类型  */
		public static final int LIVE_MAIN_TYPE_REMIND = 4;
		/** 俱乐部通知	类型  */
		public static final int LIVE_MAIN_TYPE_NOTICE = 5;
		/** 教学答疑	类型  */
		public static final int LIVE_MAIN_TYPE_QUESTION = 9;
		/** 企业	类型  */
		public static final int LIVE_MAIN_TYPE_ENTERPRISE = 10;
		/**俱乐部 */
		public static final int LIVE_MAIN_TYPE_CLUB = 11;
		
//-----------------------------------------------------动态用户类型---------------------------------------------
		/**用户动态*/
		public static final int LIVE_MAIN_TYPE_MAN = 1;
		/**俱乐部动态*/
		public static final int LIVE_MAIN_CLUB_TYPE = 2;
		/**班级动态*/
		public static final int LIVE_MAIN_CLUB_CLASS = 3;

//---------------------------------------------------主体用户类型--------------------------------------------------
		/** 用户动态  类型  */
		public static final int MAIN_OBJECT_TYPE_USER = 1;
		/** 俱乐部动态动态  类型  */
		public static final int MAIN_OBJECT_TYPE_CLUB = 2;
		/** 班级动态  类型  */
		public static final int MAIN_OBJECT_TYPE_CLASS = 3;
	//---------------------------------------------------获取竞猜话题信息--------------------------------------------------
	
	

	//------------------获取群组评论信息------------------------------------------------
	/** 1:讨论  */
	public static final int MESSAGE_DISCUSSION = 1;
	/** 2:通知 */
	public static final int MESSAGE_INFORM = 2;

	//---------------------------------------------------提示红点操作--------------------------------------------------
	/** 判断俱乐部下是否有待审核的会员  */
	public static final int REMIND_RED_DOT_TYPE_ONE=1;
	/** 判断顶部菜单的消息  */
	public static final int REMIND_RED_DOT_TYPE_TWO=2;
	/** 消息中心的各项菜单是否有未读消息  */
	public static final int REMIND_RED_DOT_TYPE_THREE=3;
	/** 我的俱乐部主页左侧菜单是否有未读消息  */
	public static final int REMIND_RED_DOT_TYPE_FOUR=4;

	/** 默认值0  */
	public static final int REMIND_RED_DOT_DEFAULT_ZERO=0;
	/** 默认值1  */
	public static final int REMIND_RED_DOT_DEFAULT_ONE=1;
	//---------------------------------------------------获取消息信息--------------------------------------------------
	/** 私信  */
	public static final int GET_MESSAGE_ONE=1;
	/** 关注  */
	public static final int GET_MESSAGE_TWO=2;
	/** 邀请  */
	public static final int GET_MESSAGE_THREE=3;
	/** 比赛  */
	public static final int GET_MESSAGE_FOUR=4;
	/** 系统  */
	public static final int GET_MESSAGE_FIVE=5;
	
	
	//---------------------------------------------------获取俱乐部权益信息--------------------------------------------------
	/** 1:是；0:否 */
	public static final int  CLUB_RIGHT_ONE=1;
	
	
	public static final int  CLUB_RIGHT_ZERO=0;
	
	/**会员人数为200人*/
	public static final int CLUB_NUM = 200;
	
	/** 默认参数为0*/
	public static final  int DEFAULT_NUM = 0;
	//TODO:一级货币==常量 ：问产品经理 double类型
	//TODO:
	
	
	
	
	

	//---------------------------------------------------购买俱乐部权益--------------------------------------------------
	/** 购买俱乐部运营权  */
	public static final int BUY_CLUB_RIGHT_ONE=1;
	/** 购买赛事运营权  */
	public static final int BUY_CLUB_RIGHT_TWO=2;
	/** 购买官方课程包  */
	public static final int BUY_CLUB_RIGHT_THREE=3;

	//---------------------------------------------------获取俱乐部账务收取信息--------------------------------------------------
	/** 俱乐部管理模块，获取俱乐部的收支情况  */
	public static final int INCOME_EXPENSE_ONE=1;
	/** 获取个人的一级货币或者虚拟物品的收支情况，个人中心的账务管理模块调用  */
	public static final int INCOME_EXPENSE_TWO=2;
	/** 获取个人的一级货币的充值记录，个人中心的账务管理模块调用  */
	public static final int INCOME_EXPENSE_THREE=3;
	/** 获取个人的虚拟物品的兑换记录，个人中心的账务管理模块调用  */
	public static final int INCOME_EXPENSE_FOUR=4;

	//---------------------------------------------------俱乐部加入方式--------------------------------------------------
	/**申请*/
	public static final int CLUB_JOIN_ONE = 1;
	/**公开*/
	public static final int CLUB_JOIN_TWO = 2;
	/**付费*/
	public static final int CLUB_JOIN_THREE = 3;
	
	//-------------------------------------------获取赛事信息--------------------------------------------------------
	/**	获取赛事信息	1.推荐赛事*/
	public static final int MATCH_GAMEEVENTS_RECOMMEND = 1;
	/**	获取赛事信息	 2.最热赛事 */
	public static final int MATCH_GAMEEVENTS_HOTTEST = 2;
	/**	获取赛事信息	 3.最新赛事  */
	public static final int MATCH_GAMEEVENTS_NEWEST = 3;
	/**	获取赛事信息	 4.我关注俱乐部发布赛事   */
	public static final int MATCH_GAMEEVENTS_CLUB_FOLLOW = 4;
	/**	获取赛事信息	 5.根据赛事id获取唯一赛事信息   */
	public static final int MATCH_GAMEEVENTS_ID = 5;
	/**	获取赛事信息	 6.顶部菜单搜索赛事    */
	public static final int MATCH_GAMEEVENTS_SEARCH = 6;
	
	//-------------------------------------------------广告类型----------------------------------------------------------------
	/** 赛事广告 */
	public static final int AD_MATCH_TYPE  = 1;
	
	//--------------------------------------------------广告主题类型----------------------------------------------------------------------
	/** 赛事广告 */
	public static final int AD_MATCH_SUBJECT_TYPE  = 1;
	
	//---------------------------------------------------会员变量--------------------------------------------------
	/**会长，教练，会员*/
	public static final String CLUB_PRESIDENT_COACH_MEMBER = "1,2,3";
	/**会长，教练*/
	public static final String CLUB_PRESIDENT_COACH="1,2";
	/**会长*/
	public static final String CLUB_PRESIDENT="1";
	
	//---------------------------------------------------用户账户-------------------------------
	/**1.个人用户**/
	public static final int INDIVIDUAL_USER = 1;
	/**2.俱乐部用户**/
	public static final int CLUB_USER = 2;
	 
	
	//--------------------------------------------获取俱乐部信息------------------------------
	/**	获取俱乐部信息 1.获取全部俱乐部*/
	public static final int GET_CLUB_INFORMATION = 1;
	
	/**	获取俱乐部信息 2.获取赛事俱乐部*/
	public static final int GET_THE_CLUB_INFORMATION = 2;
	
	/**	获取俱乐部信息 3.获取关注俱乐部*/
	public static final int GET_ATTENTION_CLUB_INFORMATION = 3;
	
	/**	获取俱乐部信息 4.获取最热俱乐部*/
	public static final int GET_NEW_CLUB_INFORMATION = 4;
	
	/**	获取俱乐部信息 5.获取最新俱乐部*/
	public static final int GET_HOT_CLUB_INFORMATION = 5;
	
	/**	获取俱乐部信息 6.获取我的俱乐部*/
	public static final int GET_MY_CLUB_INFORMATION = 6;
	
	/**	获取俱乐部信息 7.获取我的俱乐部*/
	public static final int GET_MY_CLUB_INFORMATION_ACTIVE_DEGREE = 7;
	
	/** 获取俱乐部信息 2.获取关注俱乐部 移动端*/
	public static final int GET_CLUB_INFORMATION_FORMOBILE = 2;
	
	/**	获取俱乐部信息 3.获取最热俱乐部 移动端*/
	public static final int GET_HOT_CLUB_INFORMATION_FORMOBILE = 3;
	
	/**	获取俱乐部信息 4.获取最新俱乐部  移动端*/
	public static final int GET_NEW_CLUB_INFORMATION_FORMOBILE = 4;
	
	/**	通过俱乐部列表进入俱乐部首页 5  移动端*/
	public static final int GET_CLUB_INFORMATION_FIVE = 5;
	
	
	
	
	
	
	
	
	
	
	//--------------------------------------------俱乐部会员表操作------------------------------
	/**	俱乐部会员表操作 1:审核通过*/
	public static final int SUBMIT_CLUB_ONE = 1;
	/**	俱乐部会员表操作 2:审核拒绝加入*/
	public static final int SUBMIT_CLUB_TWO = 2;
	/**	俱乐部会员表操作 3:设为教练*/
	public static final int SUBMIT_CLUB_THREE = 3;
	/**	俱乐部会员表操作 4:取消教练资格*/
	public static final int SUBMIT_CLUB_FOUR = 4;
	
	//--------------------------------------------加入或退出俱乐部------------------------------
	/**	加入或退出俱乐部 1:加入俱乐部*/
	public static final int SUBMIT_CLUB_JOIN = 1;
	/**	加入或退出俱乐部 2:退出俱乐部*/
	public static final int SUBMIT_CLUB_SIGN_OUT = 2;
	
	//--------------------------------------------提交俱乐部----------------------------
	/**	提交操作 1:处理风格海报id和背景色id*/
	public static final int SUBMIT_OPERATION_ONE = 1;
	/**	提交操作 2:处理背景色id*/
	public static final int SUBMIT_OPERATION_TWO = 2;
	/**	提交操作 3:处理其它基本信息的参数项*/
	public static final int SUBMIT_OPERATION_THREE = 3;
	/**	提交操作 4:处理擂台海报id*/
	public static final int SUBMIT_OPERATION_FOUR = 4;
	/**	提交操作 5:处理培训海报id*/
	public static final int SUBMIT_OPERATION_FIVE = 5;
	
	//-------------------------------------获取一级货币------------------------------------
	
	/**	获取一级货币 1:个人中心，账务信息页使用；*/
	public static final int GET_USER_FIR_CURRENCY = 1;
	/**	获取一级货币 2:获取俱乐部的一级货币；*/
	public static final int GET_CLUB_FIR_CURRENCY = 2;
	
	
	//-------------------------------------类型------------------------------------
	
	public static final String ACCOUNT_TYPE_TWO="鲜花";
	
	public static final String CLUB_TRAIN_IS_BUY="已经购买过官方课程包";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * redis 单表  查询约定
	 * 
	 * 	因为所有的 数据库bean 没有指定超类实体 ， 没有实现超类统一 id。
	 * 	所以在程序中实现某些组件的时候无法进行对象关联。
	 *  于是在这里定义部分使用到的 数据库bean 实体唯一标识。
	 *  
	 * @author 	lw
	 * @date	2016年6月22日  上午9:20:27
	 *
	 */
	public enum REDIS_AGREEMENT{

		/** 文本作业	对象*/
		HOMEWORK("homeworkId",AppErrorCode.REDIS_ID_HOMEWORK,TeachStudentHomework.class)
		/** 课程		对象*/
		,COURSE("courseId",AppErrorCode.REDIS_ID_COURSE,TeachCourse.class)
		/** 课程卡 课程	对象*/
		,RELCARDCOURSE("relId",AppErrorCode.REDIS_ID_RELCARDCOURSE,TeachRelCardCourse.class)
		/** 课程卡	对象*/
		,CARD("cardId",AppErrorCode.REDIS_ID_CARD,TeachCourseCard.class)
		/** 章节		对象*/
		,CHAPTER("chapterId",AppErrorCode.REDIS_ID_CHAPTER,TeachCourseChapter.class)
		/** 班级		对象*/
		,CLASSES("classId",AppErrorCode.REDIS_ID_CLASS,TeachClass.class)
		/** 学校		对象*/
		,SCHOOL("schoolId",AppErrorCode.REDIS_ID_SCHOOL,TeachSchool.class)

		/** 用户		对象*/
		,USER("userId",AppErrorCode.REDIS_ID_USER,CenterUser.class)

		/** 问题		教学*/
		,QUESTION("questionId",AppErrorCode.REDIS_ID_TEACH_QUESTION,TeachQuestion.class)
		/** 问题		培训班*/
		,CLUBQUESTION("questionId",AppErrorCode.REDIS_ID_CLUB_QUESTION,ClubTrainingQuestion.class) 
		/** 赛事		对象*/
		,MATCH("compId",AppErrorCode.REDIS_ID_MATCH,ArenaCompetition.class)
		/** 俱乐部	对象*/
		,CLUB("clubId",AppErrorCode.REDIS_ID_CLUB,ClubMain.class)
		/** 竞猜话题	对象*/
		,GUESSTOPIC("topicId",AppErrorCode.REDIS_ID_GUESSTOPIC,ArenaGuessTopic.class)
		/** 竞猜	对象*/
		,GUESS("guessId",AppErrorCode.REDIS_ID_GUESS,ArenaGuess.class)
		/** 教学问题 	对象*/
		,TEACHANSWER("answerId",AppErrorCode.REDIS_ID_QUESTIONANSWER,TeachQuestionAnswer.class)
		/** 俱乐部问题	对象*/
		,CLUBANSWER("answerId",AppErrorCode.REDIS_ID_TRAININGQUESTIONANSWER,ClubTrainingQuestionAnswer.class)
		/** 广告	对象*/
		,ARENAAD("adId",AppErrorCode.REDIS_ID_ARENAAD,ArenaAd.class)







		;

		/** 查询 表主键字符串名 */
		private String id_FieldName;

		/** 查询 失败返回错误编码 */
		private String select_Error;

		/** 查询对象类型 */
		private Class<?> clazz;

		private REDIS_AGREEMENT(String id_FieldName, String select_Error, Class<?> clazz){
			this.id_FieldName = id_FieldName;
			this.select_Error = select_Error;
			this.clazz = clazz;
		}

		public String getId_FieldName() {
			return id_FieldName;
		}


		public String getSelect_Error() {
			return select_Error;
		}


		public Class<?> getClazz() {
			return clazz;
		}

		/**
		 * 获取制定对象的数据库id唯一标识字段
		 * 
		 * 请 检查 并 实现单表约定！
		 * 
		 * @param clazz					传入clazz
		 * @return
		 * @author 						lw
		 * @date						2016年6月23日  下午3:43:00
		 */
		public static String getRedisObjectIdName(Class<?> clazz){

			for(REDIS_AGREEMENT val : REDIS_AGREEMENT.values()){
				if(val.getClazz() == clazz){
					return val.getId_FieldName();
				}
			}

			return null;
		}

	}



//----------------------------提交群组评论信息--------------------------------------------
 /** 1:教学的群组模块  */
 public static final int TEACHING_GROUP = 1;
 /** 2:竞技场的赛事交流模块  */
 public static final int ARENA_COMPETITION = 2;
 /** 3:俱乐部的培训班中的群组模块  */
 public static final int CLUB_CLASS_GROUP = 3;
 /** 4:俱乐部的话题模块  */
 public static final int CLUB_TOPIC_MODULE = 4;

//----------------提交计划任务信息-------------------------------------------------
/** 是否需要签到  1:是； */
 public static final int IS_SIGN_ONE =1;
/** 是否需要签到  0:否； */
 public static final int IS_SIGN_NO =0;

 //---------------------获取学校信息---------------------------------------------------
/** 获取学校信息  inquireType  1 */
public static final int GET_CLASS_MESSAGE_ONE = 1;
/** 获取学校信息  inquireType  2 */
public static final int GET_CLASS_MESSAGE_TWO = 2;

	//----------------充值表状态-------------------------------------------------
	/** 1:支付成功 */
	public static final int RECHARGE_SUCCESS = 1;
	/** 2:支付失败 */
	public static final int RECHARGE_FAIL = 2;
	/** 3:等待支付 */
	public static final int RECHARGE_WAIT  = 3;
	/** 4:取消充值 */
	public static final int RECHARGE_CANCEL = 4;
	
	
	
	
//-----------------------------对信息主体的删除操作----------------------------------------------------------------------
	/**操作对象类型 1:答疑的问题；*/
	public static final int DEL_ANSWER_QUESTIONS_ISSUE = 1;
	/**操作对象类型 2:答疑的回答；*/
	public static final int DEL_ANSWER_QUESTIONS_ANSWER = 2;
	/**操作对象类型 3:计划任务；*/
	public static final int DEL_PLAN_TASK = 3;
	/**操作对象类型 4:讨论(群组)；*/
	public static final int DEL_DISCUSSION_GROUP = 4;
	/**操作对象类型 5:班级；*/
	public static final int DEL_CLASS = 5;
	/**操作对象类型 6:通知(俱乐部的)*/
	public static final int DEL_INFORM = 6;
	/**操作对象类型 7:话题(俱乐部的)；*/
	public static final int DEL_TOPIC = 7;
	/**操作对象类型 8:课程；*/
	public static final int DEL_COURSE = 8;
	/**操作对象类型 9:学生提交的作业*/
	public static final int DEL_SUTDENT_JOB = 9;
	/**操作对象类型 10:相册*/
	public static final int DEL_PHOTO = 10;
	/**操作对象类型 11:相册图片*/
	public static final int DEL_PICTURE = 11;
	/**操作对象类型 12:私信*/
	public static final int DEL_PRIVATE_LETTER = 12;
// --------------------------------------协议内容------------------------------------------------
	/** 内容一 */
	public static final String PROTOCOL_CONTENT_ONE = "内容一";
	/** 内容二 */
	public static final String PROTOCOL_CONTENT_TWO = "内容二";
	/** 内容三*/
	public static final String PROTOCOL_CONTENT_THREE = "内容三";
	/** 内容四 */
	public static final String PROTOCOL_CONTENT_FOUR = "内容四";
	/** 内容五 */
	public static final String PROTOCOL_CONTENT_FIVE = "内容五";
// --------------------------------------班级类型------------------------------------------------
	/** 1:教学班 */
	public static final int CLASS_TYPE_TEACHING = 1;
	/** 2:俱乐部培训班； */
	public static final int CLASS_TYPE_CLUB = 2;
	
// --------------------------------------课程卡状态------------------------------------------------	
	/** 0:未设置 */
	public static final int CARD_STATUS_0 = 0;
	/** 1:未开始 */
	public static final int CARD_STATUS_1 = 1;
	/** 2:进行中 */
	public static final int CARD_STATUS_2 = 2;
	/** 3:已结束 */
	public static final int CARD_STATUS_3 = 3;
// --------------------------------------动态入口类型------------------------------------------------	
	/** 1:消息*/
	public static final String DYNAMIC_ENTRY_TYPE_ONE = "1";
	/** 2:班级群(群组讨论)*/
	public static final String DYNAMIC_ENTRY_TYPE_TWO = "2";
	/** 3:答疑*/
	public static final String DYNAMIC_ENTRY_TYPE_THREE = "3";
	/** 4:实训*/
	public static final String DYNAMIC_ENTRY_TYPE_FOUR = "4";
	/** 5:俱乐部*/
	public static final String DYNAMIC_ENTRY_TYPE_FIVE = "5";
	/** 6:推荐*/
	public static final String DYNAMIC_ENTRY_TYPE_SIX = "6";
	/** 7:竞猜*/
	public static final String DYNAMIC_ENTRY_TYPE_SEVEN = "7";
	/** 8:人脉*/
	public static final String DYNAMIC_ENTRY_TYPE_EIGHT = "8";
	/** 8:评选*/
	public static final String DYNAMIC_ENTRY_TYPE_NINE = "9";
}
