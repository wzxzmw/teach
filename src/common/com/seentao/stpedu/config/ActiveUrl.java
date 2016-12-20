package com.seentao.stpedu.config;

/**
 * 
 * @ClassName: ActiveUrl 
 * @Description: 动作URL配置
 * @author zhengchunlei
 * @date 2016年5月30日 下午6:56:23
 *
 */

public class ActiveUrl {

	/**平台地址**/
	public static String BASE_IP = null;
	/**比赛地址**/
	public static String GAME_IP = null;
	/**人才地址**/
	public static String RENCAI_IP = null;
	/**比赛地址**/
	public static String BASE_SERVICE_IP = null;
	/**客户端地址*/
	public static String BROWER_CLIENT_ID = null;
	/**轮播图压缩大小*/
	public static String ROTATION_MAP = null;
	/**头像大小*/
	public static String HEAD_MAP= null;
	/**卡片大小*/
	public static String CARD_MAP = null;
	static{
		BASE_IP = SystemConfig.getString("plat.url");
		GAME_IP = SystemConfig.getString("game.url");
		RENCAI_IP = SystemConfig.getString("rencai.url");
		BASE_SERVICE_IP = SystemConfig.getString("baseservice.url");
		BROWER_CLIENT_ID = SystemConfig.getString("client.url");
		/*******图片压缩大小******/
		ROTATION_MAP = SystemConfig.getString("rotation.map");
		HEAD_MAP = SystemConfig.getString("head.map");
		CARD_MAP = SystemConfig.getString("card.map");
	}
	/*客户端地址*/
	public static final String CLIENT_IP_URL = BROWER_CLIENT_ID + "/";
	
	public static final String GAME_INTERFACE_URL = "http://" + GAME_IP + "/game/interface";
	/** 获取教师所发文本作业信息 */
	public static final String TEACHER_HOME_WORKS = "http://"+BASE_IP+"/texthomeworks";
	/** 教师认证 */
	public static final String TEACHER_CERT = "http://"+BASE_IP+"/cert";
	/**	答疑信息 */
	public static final String QUESTIONS = "http://"+BASE_IP+"/questions";
	/**	竞猜*/
	public static final String GUESS = "http://"+BASE_IP+"/guess";
	/**	赛事*/
	public static final String MATCH = "http://"+BASE_IP+"/match";
	/** 广告 */
	public static final String ADVERTISED = "http://"+BASE_IP+"/advertised";
	/** 错误编码转换成错误消息 */
	public static final String ERROR_MESSAGE = "http://"+BASE_IP+"/errorMessage";
	/** 错误编码转换成错误消息   方法名*/
	public static final String ERROR_MESSAGE_FUNCTION_NAME = "tellMeMessage";
	
	
	public static final String ATTENTION_CENTERATTENTION_IP_URL = "http://"+BASE_IP+"/attention/centerAttention";

	public static final String TEACHER_GETTEACHERMESSAGE_IP_URL = "http://"+BASE_IP+"/teacher/getMessage";

	public static final String TEACHER_GETCLASSES_IP_URL = "http://"+BASE_IP+"/teacher/getClasses";

	public static final String CLASS_OPERATION_IP_URL = "http://"+BASE_IP+"/teacher/sbmitClass";

	public static final String BET_QUIZ_IP_URL = "http://"+BASE_IP+"/account/submitquizbetting";

	public static final String GET_USER_LEVEL_URL = "http://"+BASE_IP+"/account/virtualgoods";

	public static final String GET_USER_VIRTUAL_URL = "http://"+BASE_IP+"/account/virtualgoods";

	public static final String GET_PRIVATE_LETTER_URL = "http://"+BASE_IP+"/messages/getprivatemessages";

	public static final String SEND_PRIVATE_MESSAGES_URL = "http://"+BASE_IP+"/messages/submitprivatemessages";

	public static final String SET_ALBUM_COVER_URL = "http://"+BASE_IP+"/photo/submitalbumcover";

	public static final String SUBMIT_PHOTO_ALBUM_IMAGE_INFO_URL = "http://"+BASE_IP+"/photo/submitalbumphoto";

	public static final String SUBMIT_ALBUM_INFORMATION_URL = "http://"+BASE_IP+"/photo/submitalbum";

	public static final String GET_ALBUM_INFORMATION_URL = "http://"+BASE_IP+"/photo/getalbums";

	public static final String ATTENTION_MESSAGE_IP_URL = "http://"+BASE_IP+"/attention/messageHandle";

	public static final String ATTENTION_INFORMATION_IP_URL = "http://"+BASE_IP+"/attention/infromaction";

	public static final String ATTENTION_PLANTASK_IP_URL = "http://"+BASE_IP+"/attention/planTask";

	public static final String ATTENTION_GROUPCOMMENTK_IP_URL = "http://"+BASE_IP+"/attention/groupComment";

	public static final String ATTENTION_SCHOOLSMESSAGE_IP_URL = "http://"+BASE_IP+"/attention/schoolsMessage";

	public static final String CLUB_CENTER_IP_URL="http://"+BASE_IP+"/persionalcenter/clubCenter";

	public static final String CLUB_PERSIONAL_IP_URL="http://"+BASE_IP+"/persionalcenter/persionalCenter";

	public static final String GET_PHOTO_ALBUM_URL="http://"+BASE_IP+"/photo/getalbumphotos";

	public static final String USER_CONTROLLER_IP_URL="http://"+BASE_IP+"/user";
	
	public static final String VOTE_CONTROLLER_IP_URL = "http://"+BASE_IP+"/vote";

	public static final String RECHARGE_CONTROLLER_IP_URL="http://"+BASE_IP+"/recharge";

	public static final String VERIFICATION_CODE_CONTROLLER_IP_URL="http://"+BASE_IP+"/verificationcode";

	public static final String RED_PACKET_CONTROLLER_IP_URL="http://"+BASE_IP+"/redpacket";

	public static final String COURSE_CONTROLLER_IP_URL="http://"+BASE_IP+"/course";

	public static final String CLUB_CONTROLLER_IP_URL="http://"+BASE_IP+"/club";
	public static final String GET_BACKGROUND_COLOR_URL="http://"+BASE_IP+"/club/getbackgroudcolor";
	
	public static final String SUBMIT_CLUB_URL="http://"+BASE_IP+"/club/submitclub";
	
	public static final String SUBMIT_CLUBMEMBER_URL="http://"+BASE_IP+"/club/submitclubmember";
	
	public static final String JOIN_OR_EXITCLUB_URL="http://"+BASE_IP+"/club/submitClubOperation";
	
	public static final String GET_CLUBS_INFO_URL="http://"+BASE_IP+"/club/getClubs";
	
	public static final String GET_CLUBSFORMOBILE_INFO_URL="http://"+BASE_IP+"/club/getClubsForMobile";
	
	public static final String GET_PRIVATE_LETTER_FOR_MOBILR_URL = "http://"+BASE_IP+"/messages/getPrivateMessagesForMobile";
	
	public static final String REQUESTMATCHINFO_URL = "http://"+ GAME_IP +"/game/interface";
	
	public static final String REQUESTOTHERCOMPANY_URL = "http://" + RENCAI_IP + "/findEntForStpeduByIds";
	public static final String REQUESTALLCOMPANY_URL = "http://" + RENCAI_IP + "/findAllEntForStpedu";
	/** 
	* @Fields ALIPAY_SERVER_INTERFACE : 支付宝，获取参数和签名 
	*/ 
	public static final String ALIPAY_SERVER_INTERFACE = "http://" + BASE_SERVICE_IP + "/stpbaseservice/alipayInterface";
	/** 
	* @Fields SENDMSG_SERVER_INTERFACE : 发送短信
	*/ 
	public static final String SENDMSG_SERVER_INTERFACE = "http://" + BASE_SERVICE_IP + "/stpbaseservice/sendMsg";
	
	/** 平台给比赛提供的接口 **/
	public static final String BASE_IP_URL = "http://" + BASE_IP + "/game";
	
}
