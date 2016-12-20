package com.seentao.stpedu.constants;
/** 
* @author yy
* @date 2016年6月29日 上午10:00:05 
*/
public class BusinessConstant {
	//--------OSS--------
	public static final String OSS_RATE = "@90q";
	//-------通用---------
	/** 查询类型不正确  **/
	public static final String ERROR_TYPE_CODE = "501001";
	/** 查询失败  **/
	public static final String ERROR_SELECT = "501002";
	/** 修改失败  **/
	public static final String ERROR_UPDATE = "501003";
	/** 删除失败  **/
	public static final String ERROR_DELETE = "501004";
	/** 插入失败  **/
	public static final String ERROR_INSERT = "501005";
	//查询类型
	public static final int INQUIRETYPE_1 = 1;
	public static final int INQUIRETYPE_2 = 2;
	public static final int INQUIRETYPE_3 = 3;
	public static final int INQUIRETYPE_4 = 4;
	public static final int INQUIRETYPE_5 = 5;
	public static final int INQUIRETYPE_6 = 6;
	public static final int INQUIRETYPE_7 = 7;
	public static final int INQUIRETYPE_8 = 8;
	public static final int INQUIRETYPE_9 = 9;
	public static final int INQUIRETYPE_10 = 10;
	public static final int INQUIRETYPE_11 = 11;
	public static final int INQUIRETYPE_12 = 12;
	public static final int INQUIRETYPE_13 = 13;
	public static final int INQUIRETYPE_14 = 14;
	public static final int INQUIRETYPE_15 = 15;
	public static final int INQUIRETYPE_16 = 16;
	public static final int INQUIRETYPE_17 = 17;
	public static final int INQUIRETYPE_18 = 18;
	public static final int INQUIRETYPE_19 = 19;
	public static final int INQUIRETYPE_20 = 20;
	public static final int INQUIRETYPE_21 = 21;
	public static final int INQUIRETYPE_22 = 22;
	public static final int INQUIRETYPE_23 = 23;
	public static final int INQUIRETYPE_24 = 24;
	public static final int INQUIRETYPE_25 = 25;
	public static final int INQUIRETYPE_26 = 26;
	public static final int INQUIRETYPE_27 = 27;
	public static final int INQUIRETYPE_28 = 28;
	public static final int INQUIRETYPE_29 = 29;
	public static final int INQUIRETYPE_30 =30;
	//请求类型
	public static final int PLATFORM_MODULE_1 = 1;
	public static final int PLATFORM_MODULE_2 = 2;
	public static final int PLATFORM_MODULE_3 = 3;
	//红包查询类型
	public static final int REDPACKETSHOWTYPE_1 = 1;//收到的
	public static final int REDPACKETSHOWTYPE_2 = 2;//我发的
	public static final int REDPACKETSHOWTYPE_3 = 3;//红包排名
	//红包类型
	public static final int CLUB_RED_PACKET_TYPE_1 = 1;//新道B红包
	public static final int CLUB_RED_PACKET_TYPE_2 = 2;//虚拟币红包(鲜花)
	//---------业务类型----
	public static final int USER_SOURCE_TYPE_1 = 1;//注册用户
	public static final int USER_SOURCE_TYPE_2 = 2;//微信用户
	public static final int USER_SOURCE_TYPE_3 = 3;//QQ用户
	public static final int USER_SOURCE_TYPE_4 = 4;//微博用户
	public static final int USER_SOURCE_TYPE_5 = 5;//导入用户
	public static final int REGISTTYPE_1 = 1;//注册
	public static final int REGISTTYPE_2 = 2;//找回密码
	public static final int VERIFICATION_TYPE_1 = 1;//注册验证码
	public static final int VERIFICATION_TYPE_2 = 2;//手机绑定
	public static final int VERIFICATION_TYPE_3 = 3;//密码找回
	public static final int VERIFICATION_TYPE_4 = 4;//提现
	//验证码超时时间
	public static final int VERIFICATION_TIME = 600;//单位是秒
	public static final boolean IS_MAST_INVITATION_CODE = false;//注册是否需要邀请码
	public static final int INVITATION_CODE_YES = 1;//邀请码未被使用
	public static final int INVITATION_CODE_NO = 2;//邀请码已被使用
	//账户用户类型
	public static final int ACCOUNT_USER_TYPE_1 = 1;//个人用户
	public static final int ACCOUNT_USER_TYPE_2 = 2;//俱乐部用户
	//请求方式
	public static final int REQUESTSIDE_TYPE_1 = 1;//前端请求
	public static final int REQUESTSIDE_TYPE_2 = 2;//后台请求
	//课程类型。1:官方课程，2:自定义课程，3:文本作业，4:实训作业。5:案例课程。
	public static final int COURSE_TYPE_1 = 1;
	public static final int COURSE_TYPE_2 = 2;
	public static final int COURSE_TYPE_3 = 3;
	public static final int COURSE_TYPE_4 = 4;
	public static final int COURSE_TYPE_5 = 5;
	//用户账户类型
	public static final int USER_ACCOUNT_TYPE_1 = 1;//个人用户
	public static final int USER_ACCOUNT_TYPE_2 = 2;//俱乐部账户
	//发送包对象
	public static final int RED_PACKET_OBJECT_1 = 1;//全部人
	public static final int RED_PACKET_OBJECT_2 = 2;//指定人
	public static final int RED_PACKET_OBJECT_3 = 3;//俱乐部
	//获取验证码类型
	public static final int ITYPE_1 = 1;//注册
	public static final int ITYPE_2 = 2;//密码找回
	public static final int ITYPE_3 = 3;//申请提现
	public static final int ITYPE_4 = 4;//绑定手机号
	/**充值与一级货币比例  */
	public static final double RECHARGE_RATIO_ONE_LEVEL = 10;
	/**一级货币与虚拟货币比例  */
	public static final double RECHARGE_RATIO_VIRTUAL  = 100;
	//充值状态
	public static final int RECHARGE_TYPE_1 = 1;//成功
	public static final int RECHARGE_TYPE_2 = 2;//支付中
	//俱乐部查询状态
	public static final int CLUB_APPLY_STATUS_1 = 1;//审核中的会员
	public static final int CLUB_APPLY_STATUS_2 = 2;//已加入的会员
	//默认图片id
	public static final int DEFAULT_IMG_USER = -7;//默认头像
	public static final int DEFAULT_IMG_CLUB = -8;//默认俱乐部logo
	public static final int DEFAULT_IMG_TRAINING = -11;//默认培训班图像
	public static final int DEFAULT_IMG_CLASS = -9;//默认班级logo
	
	public static final int DEFAULT_IMG_PRACTICE_COURSE = -4;//默认官方课程图像
	public static final int DEFAULT_IMG_CUSTOM_COURSE = -2;//默认自定义课程图片
	
	public static final int DEFAULT_IMG_PRACTICAL = -1;//默认实训卡图片
	public static final int DEFAULT_IMG_KNOWLEDGE_COURSE = -5;//默认知识点卡图片
	public static final int DEFAULT_IMG_CASE_COURSE = -3;//默认案例卡图片
	public static final int DEFAULT_IMG_TASK = -6;//默认作业卡图片
	
	public static final int DEFAULT_IMG_MATCH = -12;//默认赛事图片
	public static final int DEFAULT_IMG_ENTERPRISE = -10;//默认企业图标
	
	public static final int DEFAULT_IMG_BGCOLOR = 5;//默认背景颜色id
	//课程卡默认图片数组
	public static final int[] DEFAULT_IMG_COURSE_CARD = new int[]{0,DEFAULT_IMG_PRACTICAL
			,DEFAULT_IMG_KNOWLEDGE_COURSE,DEFAULT_IMG_CASE_COURSE,DEFAULT_IMG_TASK};
	//课程默认图片数组
	public static final int[] DEFAULT_IMG_COURSE = new int[]{0,DEFAULT_IMG_PRACTICE_COURSE
			,DEFAULT_IMG_CUSTOM_COURSE};
	//申请证书状态
	public static final int[] CERTIFICATE_STATUS = new int[]{1,2,3,4};
	public static final int TILE_LENGTH = 40;//课程卡标题长度
	public static final int DESC_LENGTH = 280;//课程卡说明长度
	//标题错误代码
	public static final String[] SUBMIT_COURSE_ERROR_TITLE = new String[]{
			BusinessConstant.ERROR_TITLE_IS_NULL,BusinessConstant.ERROR_TITLE_IS_NOT_IRREGULAR,BusinessConstant.ERROR_TITLE_LENGTH	
	};
	//说明错误代码
	public static final String[] SUBMIT_COURSE_ERROR_DESC = new String[]{
			BusinessConstant.ERROR_DESC_IS_NULL,BusinessConstant.ERROR_DESC_IS_NOT_IRREGULAR,BusinessConstant.ERROR_DESC_LENGTH
	};
	//俱乐部不用购买俱乐部权益需要达到的人数数量
	public static final int PERSON_NUM = 200;
	//手机号码段
	public static final String[] PHONE_NUMBER = new String[]{
			"130","131","132","133","134","135","136","137","138","139",
			"141","143","145","147","149",
			"150","151","152","153","155","156","157","158","159",
			"170","171","172","173","175","176","177","178",
			"180","181","182","183","184","185","186","187","188","189"
	};
	//------错误代码----------------------
	/**用户不存在*/
	public static final String ERROR_USER_DOES_NOT_EXIST = "501006";
	/**密码不正确*/
	public static final String ERROR_PASSWORD = "501007";
	/** 班级下没有章节信息 */
	public static final String ERROR_TEACHER_COURSE_CHAPTER = "512001";
	/** 班级不存在 */
	public static final String ERROR_TEACHER_CLASS = "512002";
	/** 或许课程卡信息异常 */
	public static final String ERROR_TEACHER_COURSE_CARD = "513001";
	/** 课程卡不存在 */
	public static final String ERROR_TEACHCOURSECARD = "514001";
	/** 用户不是教师 */
	public static final String ERROR_IS_TEACHER = "514002";
	/** 教师所在班级与课程卡班级不一致 */
	public static final String ERROR_ISQEU_CLASS = "514003";
	/** 获取课程信息异常*/
	public static final String ERROR_COURSE = "515001";
	/**课程不存在*/
	public static final String COURSE_NOT_EXIST = "516001";
	/**用户不是俱乐部成员*/
	public static final String CLUB_USER_NOT_EXIST = "516002";
	/**用户不是教练*/
	public static final String USER_NOT_COACH = "516003";
	/**俱乐部下没有创建班级*/
	public static final String CLUB_NOT_EXIST_CLASS = "516004";
	/** 课程内容不能超过5000字 */
	public static final String IS_CONTEXT = "516005";
	/**设置课程显示类型异常*/
	public static final String ERROR_SET_COURSE_TYPE = "517001";
	/**账户余额不足*/
	public static final String USER_ACCOUNT_LACK   = "582001";
	/**该用户没有账户存在*/
	public static final String USER_NOT_ACCOUNT = "583001";
	/**没有配置红包类型字典表*/
	public static final String REDPACKET_NOT_REDEPLOY  = "583002";
	/**调用至臻发送验证码失败*/
	public static final String CALL_FAIL  = "583003";
	/**用户不是会长*/
	public static final String ERROR_ROLE = "583004";
	/**不是有效的手机号*/
	public static final String PHONE_IS_NOT_RULES  = "583005";
	/**充值发生异常(个人,俱乐部)*/
	public static final String ERROR_RECHARGE_CODE = "593001";
	/**俱乐部id不一致*/
	public static final String ERROR_CLUB_DIFFERING  = "593002";
	/**虚拟币兑换异常*/
	public static final String EXCHANGE_EXCEPTION   = "594001";
	/**邀请码不存在 */
	public static final String ERROR_PLEASE_CODE_NO_EXIST = "608001";
	/** 邀请码已被注册 */
	public static final String ERROR_PLEASE_CODE_INVALID = "608002";
	/**验证码过期*/
	public static final String ERROR_INVITATIONCODE_INVALID = "608003";
	/**验证码不存在*/
	public static final String ERROR_INVITATION_CODE_NO_EXIST = "608004";
	/**注册或找回密码异常*/
	public static final String REGIST_EXCEPTION  = "608005";
	/** 手机已经注册*/
	public static final String ERROR_HAVE_REGIST = "608006";
	/**登录发生异常*/
	public static final String ERROR_LOGIN = "608007";
	/**没有购买官方课程*/
	public static final String ERROR_NO_PURCHASE = "608008";
	/**远程调用接口异常*/
	public static final String ERROR_HPROSE = "608009";
	/**标题不能为空*/
	public static final String ERROR_TITLE_IS_NULL = "514009";
	/**说明不能为空*/
	public static final String ERROR_DESC_IS_NULL = "514004";
	/**说明只能包含英文,数字,中文(暂时不做校验)*/
	public static final String ERROR_DESC_IS_NOT_IRREGULAR = "514005";
	/**标题只能包含英文,数字,中文*/
	public static final String ERROR_TITLE_IS_NOT_IRREGULAR = "514006";
	/**标题长度不能超过20*/
	public static final String ERROR_TITLE_LENGTH = "514007";
	/**说明长度不能超过140*/
	public static final String ERROR_DESC_LENGTH = "514008";
	/**标题有不合格字符*/
	public static final String ERROR_TITLE_NOT_REGULAR="514010";
	/**内容有不合格字符*/
	public static final String ERROR_TITLE_NOT_CONTEXT="514011";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**根据充值id未获取到信息*/
	public static final String NOT_RECHARGE_BY_ID  = "583005";
	
	
	
}


