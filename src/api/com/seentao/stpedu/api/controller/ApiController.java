package com.seentao.stpedu.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.ApiConfig;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.hprose.HproseRPC;

/**
 * @ClassName: ApiController 
 * @Description: API总接口
 * @author zhengchunlei
 * @date 2016年5月30日 下午7:18:15
 *
 */
@Controller
public class ApiController{


	@ResponseBody
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String actionIndex(HttpServletRequest request)
	{

		String  result = "";
		String action ="";
		String params ="";
		//获取前台传参 TODO：目前通过get方式获取，需要改成POST方式
		String requestParam = "";
		//获取前台传参 目前通过get方式获取，需要改成POST方式
		// 需要POST接受的时候打开，并修改上边的RequestMethod.POST
		InputStream is = null; 
		try { 
			is = request.getInputStream(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			//读取HTTP请求内容 
			String buffer = null; 
			StringBuffer sb = new StringBuffer(); 
			while ((buffer = br.readLine()) != null) { 
				sb.append(buffer); 
			} 
			requestParam = sb.toString();
		}catch(IOException e) { 
			e.printStackTrace();
		}
		if(null != requestParam && !"".equals(requestParam)){
			try{
				JSONObject requestObj = JSONObject.parseObject(requestParam);
				String header = requestObj.getString("header");
				//校验请求头信息
				if(null != header && !"".equals(header)){
					//校验版本信息 TODO
					String clientType = (JSONObject.parseObject(header)).getString("clientType");
					String clientVersion = (JSONObject.parseObject(header)).getString("clientVersion");
				}else{
					return Common.getReturn(1, "请求JSON信息缺少请求头信息。").toJSONString();
				}
				//校验请求体信息
				String body = requestObj.getString("body");
				if(null != body && !"".equals(body)){
					//校验请求动作
					action = (JSONObject.parseObject(body)).getString("action");
					if(null != action && !"".equals(action)){

					}else{
						return Common.getReturn(1, "缺少请求动作信息").toJSONString();
					}

					//校验请求参数信息
					params = (JSONObject.parseObject(body)).getString("requestParam");
					if(null != params && !"".equals(params)){
						// 登录和验证码不校验Token
//						if(!action.equals("login") && !action.equals("getVerifyPicture") && !action.equals("loginForMobile")){
//							int userId = (JSONObject.parseObject(params)).getInteger("userId");
//	        				String userToken = (JSONObject.parseObject(params)).getString("userToken");
//	        		    	
//	        				Object[] cParams = new Object[]{userId, userToken};
//							String isUserTokenValid = (String)HproseRPC.invoke(ActiveUrl.BASE_IP_URL, "isUserTokenValid", cParams);;
//							if(null != isUserTokenValid && !"".equals(isUserTokenValid)){
//								JSONObject isUserTokenValidObj = JSONObject.parseObject(isUserTokenValid);
//								if("0".equals(isUserTokenValidObj.get("code").toString())){
//									JSONObject resultObj = isUserTokenValidObj.getJSONObject("result");
//									if(0 == resultObj.getInteger("isValid")){
//										return Common.getReturn(10, "您的帐号已在别处登录！如非本人操作，请及时修改密码。").toJSONString();
//									}
//								}else{
//									return isUserTokenValid;
//								}
//							}
//						}
					}else{
						return Common.getReturn(1, "缺少请求参数信息").toJSONString();
					}
				}else{
					return Common.getReturn(1, "请求JSON信息缺少请求体信息。").toJSONString();
				}
			}catch(Exception e){
				e.printStackTrace();
				return Common.getReturn(1, "请求JSON错误，无法解析。").toJSONString();
			}

		}else{
			return Common.getReturn(1, "请求参数不能为空").toJSONString();
		}

		Method[] methods;
		try {
			methods = this.getClass().newInstance().getClass().getMethods();
			//全部方法
			for (int  i = 0;  i< methods.length; i++){
				if(action.equals(methods[i].getName())){//和传入方法名匹配 
					Object obj = this.getClass().newInstance();
					//调用方法  
					try {
						result = (String) methods[i].invoke(obj, params);
						break;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}     
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	} 

	/**
	 * 通过json key值 获取json文件里的result键值结果（公用）
	 * @param jsonKey json对象的key值
	 * @return
	 */
	private JSONObject _getApiConfig(String jsonKey){
		JSONObject apiConfigStart = ApiConfig.getObject(jsonKey);
		JSONObject apiConfig = new JSONObject();
		//判断获取对象是否成功
		if(Integer.parseInt(apiConfigStart.get("code").toString())==0){
			apiConfig =JSONObject.parseObject(Common.null2Str(apiConfigStart.get("result")));
		}else{
			return apiConfigStart;
		}
		return apiConfig;
	}






	//拼装service传参
	private JSONArray _getRequestByConfig(JSONObject paramObj, JSONObject apiConfig){
		if(null != apiConfig){
			JSONObject requestObjd = JSONObject.parseObject(apiConfig.get("request").toString());
			JSONArray arrRequestEnd  = new JSONArray();
			if(null != requestObjd){
				for(Entry<String,Object> entryResult : requestObjd.entrySet()){
					for(Entry<String,Object> entryResultParam : paramObj.entrySet()){
						//判断配置文件里的request的value值是否等于前端传过来的参数的key值
						if(entryResultParam.getKey().equals(entryResult.getValue().toString())){
							//顺序根据配置文件来
							arrRequestEnd.set(Integer.parseInt(entryResult.getKey()),entryResultParam.getValue());
						}
					}
				}
			}
			return arrRequestEnd;
		}else{
			return null;
		}
	}



	/**
	 * 获取最后接口返回值
	 * @param action 要访问的接口名称
	 * @param requestParam 传参
	 * @param url 接口URL
	 * @return
	 */
	private String _getReturnEnd(String jsonName, String action, String requestParam, String url){
		JSONObject paramObj = new JSONObject();
		if(null != requestParam && !"".equals(requestParam)){
			paramObj = JSONObject.parseObject(requestParam);
		}else{
			return Common.getReturn(1, "参数不能为空").toJSONString();
		}
		JSONObject joResultEnd = new JSONObject();
		//从api.json配置文件里获取对应接口的json对象
		ApiConfig.getJsonFile("api/"+jsonName+".json");
		JSONObject apiConfig = this._getApiConfig(action);

		//获取配置文件里request参数
		JSONArray arrRequestEnd  = this._getRequestByConfig(paramObj, apiConfig);
		//通过hprose rpc通信模块来获取其他action里的方法 注：需要将controller抽象出对应接口，然后在applicationContext-mvc.xml里配置，详细配置见配置文件
		String returnResult = "";
		if(null != arrRequestEnd && arrRequestEnd.size()>0){
			//传参设置
			Object[] cParams = new Object[arrRequestEnd.size()];
			for(int i = 0; i < arrRequestEnd.size(); i ++){
				cParams[i] = arrRequestEnd.get(i);
			}
			//访问方法
			returnResult = (String)HproseRPC.invoke(url, action, cParams);
		}else{
			return Common.getReturn(1, "JSON文件获取请求配置失败").toJSONString();
		}
		return new JsonComponent(returnResult, action, jsonName).init().execute().getReulst().toJSONString();
	}


	/** 文本作业操作*/

	/**
	 * 获取教师所发文本作业信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getHomeworks(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		Integer requestSide = requestObj.getInteger("requestSide");
		if(requestSide == null){
			requestObj.put("requestSide", 0);
		}
		switch (inquireType) {
		case GameConstants.TEACHER_HOMEWORK_QUERY_PAGE:
			requestObj.put("homeworkId", 0);
			break;
		case GameConstants.TEACHER_HOMEWORK_QUERY_ENTITY:
			requestObj.put("courseCardId", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_COURSE_PPARAM).toJSONString();
		}
		return this._getReturnEnd("teacherhomeworks","getHomeworks", requestObj.toJSONString(), ActiveUrl.TEACHER_HOME_WORKS);
	}

	/**
	 * 获取学生回答的文本作业信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getHomeworkSolution(String requestParam){
		return this._getReturnEnd("studenthomeworksolution","getHomeworkSolution", requestParam, ActiveUrl.TEACHER_HOME_WORKS);
	}

	/**
	 * 教师发布文本作业
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitHomework(String requestParam){
		return this._getReturnEnd("teachersubmithomework","submitHomework", requestParam, ActiveUrl.TEACHER_HOME_WORKS);
	}

	/**
	 * 上传文本作业
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitHomeworkSolution(String requestParam){
		JSONObject parse = JSONObject.parseObject(requestParam);
		if(parse != null && parse.getString("homeworkFileIds") == null){
			parse.put("homeworkFileIds", "null");
		}
		return this._getReturnEnd("studentsubmithomeworks","submitHomeworkSolution", parse.toJSONString(), ActiveUrl.TEACHER_HOME_WORKS);
	}

	/**
	 * 提交作业或人员的评分
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitScore(String requestParam){
		return this._getReturnEnd("teacherhomeworksscore","submitScore", requestParam, ActiveUrl.TEACHER_HOME_WORKS);
	}


	/** 教师认证 */

	/**
	 * 提交作业或人员的评分
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitCertRequest(String requestParam){
		return this._getReturnEnd("teachercert","submitCertRequest", requestParam, ActiveUrl.TEACHER_CERT);
	}

	/** 答疑信息 */

	/**
	 * 获取答疑信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getQuestions(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case GameConstants.STUDENT_QUESTION_PAGE:
			requestObj.put("questionId", 0);
			break;
		case GameConstants.STUDENT_QUESTION_ENTITY:
			requestObj.put("classId", 0);
			requestObj.put("chapterId", 0);
			requestObj.put("isExcellent", 0);
			requestObj.put("limit", 0);
			requestObj.put("start", 0);
			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUESTIONS_PARAM).toJSONString();
		}
		requestObj.put("_type", GameConstants.TYPE_PC);
		return this._getReturnEnd("getquestions","getQuestions", requestObj.toJSONString(), ActiveUrl.QUESTIONS);
	}

	/**
	 * 提交问题的回复信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitAnswer(String requestParam){
		return this._getReturnEnd("submitanswer","submitAnswer", requestParam, ActiveUrl.QUESTIONS);
	}

	/**
	 * 提交问题信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitQuestion(String requestParam){
		return this._getReturnEnd("submitquestion","submitQuestion", requestParam, ActiveUrl.QUESTIONS);
	}
	

	/**
	 * 获取提问的回复信息
	 * @param requestParam
	 * @return
	 * @author 			lw
	 * @date			2016年8月17日  下午8:43:57
	 */
	public String getAnswers(String requestParam){
		JSONObject parseObject = JSONObject.parseObject(requestParam);
		parseObject.put("_type", "PC");
		parseObject.put("answerId", "");
		return this._getReturnEnd("getanswers","getAnswersForMobile", parseObject.toJSONString(), ActiveUrl.QUESTIONS);
	}

	/** 竞猜 */

	/**
	 * 获取竞猜信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getQuizs(String requestParam){
		JSONObject json = JSONObject.parseObject(requestParam);
		Object object = json.get("inquireType");
		if(object == null){
			json.put("memberId", 0);
			json.put("inquireType", 1);
		}
		json.put("_type", GameConstants.TYPE_PC);
		return this._getReturnEnd("getquizs","getQuizs", json.toJSONString(), ActiveUrl.GUESS);
	}

	/**
	 * 结束竞猜话题或竞猜
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String finishQuiz(String requestParam){
		return this._getReturnEnd("finishquiz","finishQuiz", requestParam, ActiveUrl.GUESS);
	}

	/**
	 * 获取竞猜话题信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getQuizTopic(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case GameConstants.QUIZ_TOPIC_TYPE_HOT:
			requestObj.put("quizTopicId", 0);
			break;
		case GameConstants.QUIZ_TOPIC_TYPE_NEW:
		case GameConstants.QUIZ_TOPIC_TYPE_MY:
			requestObj.put("quizTopicId", 0);
			requestObj.put("gameEventId", 0);
			break;
		case GameConstants.QUIZ_TOPIC_TYPE_INFO:
			requestObj.put("gameEventId", 0);
			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_QUIZ_TOPIC_REQUEST_PARAM).toJSONString();
		}
		return this._getReturnEnd("getquiztopic","getQuizTopic", requestObj.toJSONString(), ActiveUrl.GUESS);
	}

	/**
	 * 创建竞猜
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitQuiz(String requestParam){
		return this._getReturnEnd("createquiz","submitQuiz", requestParam, ActiveUrl.GUESS);
	}

	/**
	 * 获竞猜公布结果
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitQuizResult(String requestParam){
		return this._getReturnEnd("submitquizresult","submitQuizResult", requestParam, ActiveUrl.GUESS);
	}

	/**
	 * 创建竞猜话题
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitQuizTopic(String requestParam){
		return this._getReturnEnd("submitquiztopic","submitQuizTopic", requestParam, ActiveUrl.GUESS);
	}

	/** 赛事 */
	/**
	 * 获取赛事信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getGameEvents(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case GameConstants.MATCH_GAMEEVENTS_RECOMMEND:
		case GameConstants.MATCH_GAMEEVENTS_HOTTEST:
		case GameConstants.MATCH_GAMEEVENTS_NEWEST:
		case GameConstants.MATCH_GAMEEVENTS_CLUB_FOLLOW:
			requestObj.put("gameEventId", 0);
			requestObj.put("searchWord", "");
			if(requestObj.getInteger("memberId") == null){
				requestObj.put("memberId", 0);
			}
			break;
		case GameConstants.MATCH_GAMEEVENTS_SEARCH:
			requestObj.put("gameEventId", 0);
			requestObj.put("memberId", 0);
			break;
		case GameConstants.MATCH_GAMEEVENTS_ID:
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("searchWord", 0);
			requestObj.put("memberId", 0);
			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM).toJSONString();
		}
		requestObj.put("_type", GameConstants.TYPE_PC);
		return this._getReturnEnd("getgameevents","getGameEvents", requestObj.toJSONString(), ActiveUrl.MATCH);
	}

	/**
	 * 提交赛事信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitGameEvent(String requestParam){
		return this._getReturnEnd("submitgameevent","submitGameEvent", requestParam, ActiveUrl.MATCH);
	}


	/** 广告 */
	/**
	 * 获取广告信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getAds(String requestParam){
		return this._getReturnEnd("getads","getAds", requestParam, ActiveUrl.ADVERTISED);
	}







	/**
	 * submitAttention(加关注取消关注)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitAttention(String requestParam){
		return this._getReturnEnd("centerattention","submitAttention", requestParam, ActiveUrl.ATTENTION_CENTERATTENTION_IP_URL);
	}

	/**
	 * submitAttention(移动端：加关注取消关注)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitAttentionForMobile(String requestParam){
		return this._getReturnEnd("centerattention","submitAttention", requestParam, ActiveUrl.ATTENTION_CENTERATTENTION_IP_URL);
	}

	/**
	 * getTeachers(获取教师信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getTeachers(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("classId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("classType", 0);
			return this._getReturnEnd("getTeacherMessage","getTeachers", requestObj.toString(), ActiveUrl.TEACHER_GETTEACHERMESSAGE_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("classType", 0);
			return this._getReturnEnd("getTeacherMessage","getTeachers", requestObj.toString(), ActiveUrl.TEACHER_GETTEACHERMESSAGE_IP_URL);
		case 3:
			requestObj.put("teacherId", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			return this._getReturnEnd("getTeacherMessage","getTeachers", requestObj.toString(), ActiveUrl.TEACHER_GETTEACHERMESSAGE_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * getClasses(获取班级信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getClasses(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("classId", 0);
			requestObj.put("clubId", 0);  
			requestObj.put("sortType", 0);
			requestObj.put("classType", 0);
			requestObj.put("memberId", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 2:
			requestObj.put("schoolId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("classId", 0);
			requestObj.put("clubId", 0);  
			requestObj.put("sortType", 0);
			requestObj.put("classType", 0);
			requestObj.put("memberId", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 3:
			requestObj.put("schoolId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0);  
			requestObj.put("sortType", 0);
			requestObj.put("start", 0);  
			requestObj.put("limit", 0);
			requestObj.put("memberId", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 4:
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0);  
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("memberId", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 5:
			requestObj.put("schoolId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("sortType", 0);
			requestObj.put("memberId", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 6:
			requestObj.put("schoolId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);  
			requestObj.put("sortType", 0);
			requestObj.put("memberId", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 7:
			requestObj.put("schoolId", 0);
			requestObj.put("teacherId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);  
			requestObj.put("sortType", 0);
			requestObj.put("requestSide", 0);
			return this._getReturnEnd("getClassesMessage","getClasses", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);

		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * getClassesForMobile(移动端:获取班级信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getClassesForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("schoolId", 0);  
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("sortType", 0);
			requestObj.put("memberId", 0);
			return this._getReturnEnd("getClassesMessageTypeOne","getClassesForMobile", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 2:
			requestObj.put("schoolId", 0);  
			requestObj.put("clubId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("teacherId", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("sortType", 0);
			requestObj.put("memberId", 0);
			return this._getReturnEnd("getClassesMessageTwo","getClassesForMobile", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 3:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("memberId", 0);
			return this._getReturnEnd("getClassesMessageThree","getClassesForMobile", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 4:
			requestObj.put("schoolId", 0);  
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("sortType", 0);
			requestObj.put("memberId", 0);
			return this._getReturnEnd("getClassesMessageFour","getClassesForMobile", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		case 5:
			requestObj.put("schoolId", 0);  
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("sortType", 0);
			return this._getReturnEnd("getClassesMessageFive","getClassesForMobile", requestObj.toString(), ActiveUrl.TEACHER_GETCLASSES_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}
	/**
	 * submitClass(提交班级信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitClass(String requestParam){
		return this._getReturnEnd("classoperation","submitClass", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}

	/**
	 * submitDefaultClass(设置默认班级)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitDefaultClass(String requestParam){
		return this._getReturnEnd("classoperation","submitDefaultClass", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}

	/**
	 * submitDefaultClassForMobile(移动端：设置默认班级)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitDefaultClassForMobile(String requestParam){
		return this._getReturnEnd("classoperation","submitDefaultClass", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}

	/**
	 * joinClass(报名加入班级)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String joinClass(String requestParam){
		return this._getReturnEnd("classoperation","joinClass", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}

	/**
	 * joinClassForMobile(移动端：报名加入班级)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String joinClassForMobile(String requestParam){
		return this._getReturnEnd("classoperation","joinClass", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}


	/**
	 * submitTeacherOperation(教师邀请或踢出学生)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitTeacherOperation(String requestParam){
		return this._getReturnEnd("classoperation","submitTeacherOperation", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}

	/**
	 * submitTeacherOperationForMobile(移动端：教师邀请或踢出学生)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitTeacherOperationForMobile(String requestParam){
		return this._getReturnEnd("classoperation","submitTeacherOperation", requestParam, ActiveUrl.CLASS_OPERATION_IP_URL);
	}










	/**
	 * submitQuizBetting(竞猜下注)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitQuizBetting(String requestParam){
		return this._getReturnEnd("submitQuiz","submitQuizBetting", requestParam, ActiveUrl.BET_QUIZ_IP_URL);
	}

	/**
	 * getVirtualGoods(获取登录用户的虚拟物品)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getVirtualGoods(String requestParam){
		return this._getReturnEnd("virtualgood","getVirtualGoods", requestParam, ActiveUrl.GET_USER_VIRTUAL_URL);
	}

	/**
	 * getFLevelAccount(获取一级货币)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getFLevelAccount(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("clubId", 0);
			return this._getReturnEnd("levelaccount","getFLevelAccount", requestObj.toString(), ActiveUrl.GET_USER_LEVEL_URL);
		case 2:
			return this._getReturnEnd("levelaccount","getFLevelAccount", requestObj.toString(), ActiveUrl.GET_USER_LEVEL_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}

	}

	/**
	 * getPrivateMessages(获取私信)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getPrivateMessages(String requestParam){
		return this._getReturnEnd("privatemessage","getPrivateMessages", requestParam, ActiveUrl.GET_PRIVATE_LETTER_URL);
	}

	/**
	 * submitPrivateMessage(发私信)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitPrivateMessage(String requestParam){
		return this._getReturnEnd("submitpmssage","submitPrivateMessage", requestParam, ActiveUrl.SEND_PRIVATE_MESSAGES_URL);
	}

	/**
	 * submitAlbumCover(设置相册封面)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitAlbumCover(String requestParam){
		return this._getReturnEnd("submitAlbumc","submitAlbumCover", requestParam, ActiveUrl.SET_ALBUM_COVER_URL);
	}

	/**
	 * submitAlbumCover(提交相册图片信息)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitAlbumPhoto(String requestParam){
		return this._getReturnEnd("submitAlbumP","submitAlbumPhoto", requestParam, ActiveUrl.SUBMIT_PHOTO_ALBUM_IMAGE_INFO_URL);
	}

	/**
	 * submitAlbumCover(提交相册信息)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitAlbum(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer albumId = requestObj.getInteger("albumId");
		if(albumId == null){
			requestObj.put("albumId", 0);
			return this._getReturnEnd("submitCenterAlbum","submitalbum", requestObj.toString(), ActiveUrl.SUBMIT_ALBUM_INFORMATION_URL);
		}else{
			return this._getReturnEnd("submitCenterAlbum","submitalbum", requestObj.toString(), ActiveUrl.SUBMIT_ALBUM_INFORMATION_URL);
		}
	}

	/**
	 * getAlbums(获取相册信息)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getAlbums(String requestParam){
		return this._getReturnEnd("getAlbum","getAlbums", requestParam, ActiveUrl.GET_ALBUM_INFORMATION_URL);
	}

	/**
	 * getAlbumPhotoes(获取相册下的图片信息)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getAlbumPhotoes(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("getAlbumPhoto","getAlbumPhotoes", requestObj.toString(), ActiveUrl.GET_PHOTO_ALBUM_URL);
		case 2:
			return this._getReturnEnd("getAlbumPhoto","getAlbumPhotoes", requestObj.toString(), ActiveUrl.GET_PHOTO_ALBUM_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}

	}

	/**
	 * getBackgroundColor(获取自定义背景色)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getBackgroundColor(String requestParam){
		return this._getReturnEnd("getBgroudColor","getBackgroundColor", requestParam, ActiveUrl.GET_BACKGROUND_COLOR_URL);
	}

	/**
	 * submitClub( 提交俱乐部)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitClub(String requestParam){
		return this._getReturnEnd("submitClub","submitClub", requestParam, ActiveUrl.SUBMIT_CLUB_URL);
	}

	/**
	 * SubmitClubMember(俱乐部会员操作)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitClubMember(String requestParam){
		return this._getReturnEnd("submitClubMem","submitClubMember", requestParam, ActiveUrl.SUBMIT_CLUBMEMBER_URL);
	}

	/**
	 * submitClubOperation(加入或退出俱乐部)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitClubOperation(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionType = requestObj.getInteger("actionType");
		String applicationContent = requestObj.getString("applicationContent");
		switch (actionType) {
		case 1:
			if("".equals(applicationContent)){
				requestObj.put("applicationContent", "");
			}
			return this._getReturnEnd("submitClubOpera","submitClubOperation", requestObj.toString(), ActiveUrl.JOIN_OR_EXITCLUB_URL);
		case 2:
			requestObj.put("applicationContent", "0");
			return this._getReturnEnd("submitClubOpera","submitClubOperation", requestObj.toString(), ActiveUrl.JOIN_OR_EXITCLUB_URL);
		default:
			return Common.getReturn(1, "actionType参数错误").toJSONString();
		}
	}

	/**
	 * getClubs(获取俱乐部信息)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getClubs(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			requestObj.put("clubId", 0); 
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 2:
			requestObj.put("memberId", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 3:
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0); 
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 4:
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0); 
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 5:
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0); 
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 6:
			requestObj.put("searchWord", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		case 7:
			requestObj.put("searchWord", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0); 
			requestObj.put("gameEventId", 0);
			return this._getReturnEnd("getClubs","getClubs", requestObj.toString(), ActiveUrl.GET_CLUBS_INFO_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}

	}





	/**
	 * submitAttitude(对内容主体进行点赞加精等态度操作)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitAttitude(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer attiMainType = requestObj.getInteger("attiMainType");
		switch (attiMainType) {
		case 1:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 2:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 4:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		default:
			requestObj.put("platformModule",0);
			return this._getReturnEnd("messageHandel","submitAttitude", requestObj.toString(), ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		}
	}
	/**
	 * submitAttitudeForMobile(移动端：对内容主体进行点赞加精等态度操作)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitAttitudeForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer attiMainType = requestObj.getInteger("attiMainType");
		switch (attiMainType) {
		case 1:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 2:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 4:
			return this._getReturnEnd("messageHandel","submitAttitude", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		default:
			requestObj.put("platformModule",0);
			return this._getReturnEnd("messageHandel","submitAttitude", requestObj.toString(), ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		}
	}

	/**
	 * submitDelete(对信息主体的删除操作)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitDelete(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionObjectType = requestObj.getInteger("actionObjectType");
		switch (actionObjectType) {
		case 1:
			return this._getReturnEnd("messageHandel","submitDelete", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 2:
			return this._getReturnEnd("messageHandel","submitDelete", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 4:
			return this._getReturnEnd("messageHandel","submitDelete", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 5:
			return this._getReturnEnd("messageHandel","submitDelete", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		case 8:
			return this._getReturnEnd("messageHandel","submitDelete", requestParam, ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		default:
			requestObj.put("platformModule",0);
			return this._getReturnEnd("messageHandel","submitDelete", requestObj.toString(), ActiveUrl.ATTENTION_MESSAGE_IP_URL);
		}
	}

	/**
	 * submitInvitation(邀请其他人或推送俱乐部回答问题操作)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitInvitation(String requestParam){
		return this._getReturnEnd("informAtion","submitInvitation", requestParam, ActiveUrl.ATTENTION_INFORMATION_IP_URL);
	}

	/**
	 * submitInvitationForMobile(移动端：邀请其他人或推送俱乐部回答问题操作)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitInvitationForMobile(String requestParam){
		return this._getReturnEnd("informAtion","submitInvitation", requestParam, ActiveUrl.ATTENTION_INFORMATION_IP_URL);
	}

	/**
	 * getTasks(获取计划任务信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getTasks(String requestParam){
		return this._getReturnEnd("planTask","getTasks", requestParam, ActiveUrl.ATTENTION_PLANTASK_IP_URL);
	}

	/**
	 * getTasksForMobile(移动端：获取计划任务信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getTasksForMobile(String requestParam){
		return this._getReturnEnd("planTask","getTasks", requestParam, ActiveUrl.ATTENTION_PLANTASK_IP_URL);
	}

	/**
	 * submitTask(提交计划任务信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitTask(String requestParam){
		return this._getReturnEnd("planTask","submitTask", requestParam, ActiveUrl.ATTENTION_PLANTASK_IP_URL);
	}
	/**
	 * getComments(获取群组评论信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getComments(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","getComments", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("classType",0);
			requestObj.put("topicId", 0);
			requestObj.put("commentType", 0);
			return this._getReturnEnd("groupComment","getComments", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 3:
			requestObj.put("classId", 0);
			requestObj.put("classType",0);
			requestObj.put("gameEventId", 0);
			requestObj.put("commentType", 0);
			return this._getReturnEnd("groupComment","getComments", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * getCommentsForMobile(移动端：获取群组评论信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getCommentsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId",0);
			requestObj.put("start", 0);
			return this._getReturnEnd("getGroupCommentMobile","getCommentsForMobile", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("topicId",0);
			requestObj.put("commentType", 0);
			requestObj.put("start", 0);
			return this._getReturnEnd("getGroupCommentMobile","getCommentsForMobile", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 3:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("gameEventId", 0);
			requestObj.put("commentType", 0);
			requestObj.put("sortType", 0);
			if(requestObj.getInteger("newCommentId") == null){
				requestObj.put("newCommentId", 0);
			}
			if(requestObj.getInteger("oldCommentId") == null){
				requestObj.put("oldCommentId", 0);
			}
			if(requestObj.getInteger("start") == null){
				requestObj.put("start", 0);
			}
			return this._getReturnEnd("getGroupCommentMobile","getCommentsForMobile", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 4:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId",0);
			requestObj.put("sortType", 0);
			requestObj.put("newCommentId", 0);
			requestObj.put("oldCommentId",0);
			requestObj.put("start", 0);
			return this._getReturnEnd("getGroupCommentMobile","getCommentsForMobile", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}
	/**
	 * submitComment(提交群组评论信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitComment(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer commentModule = requestObj.getInteger("commentModule");
		switch (commentModule) {
		case 1:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 3:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 4:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("gameEventId", 0);
			requestObj.put("commentType", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		default:
			return Common.getReturn(1, "commentModule参数错误").toJSONString();
		}
	}
	/**
	 * submitCommentForMobile(移动端：提交群组评论信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String submitCommentForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer commentModule = requestObj.getInteger("commentModule");
		switch (commentModule) {
		case 1:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 3:
			requestObj.put("gameEventId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		case 4:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("gameEventId", 0);
			requestObj.put("commentType", 0);
			return this._getReturnEnd("groupComment","submitComment", requestObj.toString(), ActiveUrl.ATTENTION_GROUPCOMMENTK_IP_URL);
		default:
			return Common.getReturn(1, "commentModule参数错误").toJSONString();
		}
	}

	/**
	 * getSchools(获取学校信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getSchools(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("schoolsMessage","getSchools", requestParam, ActiveUrl.ATTENTION_SCHOOLSMESSAGE_IP_URL);
		case 2:
			requestObj.put("sortType", 0);
			return this._getReturnEnd("schoolsMessageITypeTwo","getSchools", requestObj.toString(), ActiveUrl.ATTENTION_SCHOOLSMESSAGE_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * getSchoolsForMobile(移动端：获取学校信息)
	 * @param requestParam 请求参数
	 * @author ligs
	 * @return
	 */
	public String getSchoolsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("schoolsMessage","getSchools", requestParam, ActiveUrl.ATTENTION_SCHOOLSMESSAGE_IP_URL);
		case 2:
			requestObj.put("sortType", 0);
			return this._getReturnEnd("schoolsMessageITypeTwo","getSchools", requestObj.toString(), ActiveUrl.ATTENTION_SCHOOLSMESSAGE_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * submitMood(发心情)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String submitMood(String requestParam){
		return this._getReturnEnd("persionalCenter","submitMood", requestParam, ActiveUrl.CLUB_PERSIONAL_IP_URL);
	}


	/**
	 * getRemindRedDot(提示红点的操作)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getRemindRedDot(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionObjectType = requestObj.getInteger("actionType");
		switch (actionObjectType) {
		case 1:
			return this._getReturnEnd("persionalCenter","getRemindRedDot", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		case 2:
			requestObj.put("clubId",0);
			return this._getReturnEnd("persionalCenter","getRemindRedDot", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		case 3:
			requestObj.put("clubId",0);
			return this._getReturnEnd("persionalCenter","getRemindRedDot", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		case 4:
			return this._getReturnEnd("persionalCenter","getRemindRedDot", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		default:
			requestObj.put("clubId",0);
			return this._getReturnEnd("persionalCenter","getRemindRedDot", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		}

	}
	
	/**
	 * getRemindRedDot(提示红点的操作【手机】)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getRemindRedDotForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionObjectType = requestObj.getInteger("actionType");
		switch (actionObjectType) {
		case 1:
			return this._getReturnEnd("persionalCenter","getRemindRedDotForMobile", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		case 2:
			requestObj.put("clubId",0);
			return this._getReturnEnd("persionalCenter","getRemindRedDotForMobile", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		default:
			requestObj.put("clubId",0);
			return this._getReturnEnd("persionalCenter","getRemindRedDotForMobile", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
		}

	}

	/**
	 * submitAttachmentOSS(提交附件的OSS信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String submitAttachmentOSS(String requestParam){

		return this._getReturnEnd("persionalCenter","submitAttachmentOSS", requestParam, ActiveUrl.CLUB_PERSIONAL_IP_URL);
	}

	/**
	 * submitLoginUser(提交登录用户的信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String submitLoginUser(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);



		if(!requestObj.containsKey("collegeId")){
			requestObj.put("collegeId", "");
		}

		if(!requestObj.containsKey("realName")){
			requestObj.put("realName", "");
		}
		if(!requestObj.containsKey("phoneNumber")){
			requestObj.put("phoneNumber", "");
		}

		if(!requestObj.containsKey("provinceId")){
			requestObj.put("provinceId", "");
		}
		if(!requestObj.containsKey("cityId")){
			requestObj.put("cityId", "");
		}
		if(!requestObj.containsKey("birthday")){
			requestObj.put("birthday", "");
		}
		if(!requestObj.containsKey("profession")){
			requestObj.put("profession", "");
		}
		if(!requestObj.containsKey("studentID")){
			requestObj.put("studentID", "");
		}
		if(!requestObj.containsKey("studentCard")){
			requestObj.put("studentCard", "");
		}
		if(!requestObj.containsKey("teacherCard")){
			requestObj.put("teacherCard", "");
		}
		if(!requestObj.containsKey("positionalTitle")){
			requestObj.put("positionalTitle", "");
		}
		if(!requestObj.containsKey("speciality")){
			requestObj.put("speciality", "");
		}
		if(!requestObj.containsKey("grade")){
			requestObj.put("grade", "");
		}
		if(!requestObj.containsKey("educationLevel")){
			requestObj.put("educationLevel", "");
		}
		//String nickname,Integer sex,Integer schoolId,String idcard,String desc,String address
		if(!requestObj.containsKey("nickname")){
			requestObj.put("nickname", "");
		}
		if(!requestObj.containsKey("sex")){
			requestObj.put("sex", -1);
		}
		if(!requestObj.containsKey("schoolId")){
			requestObj.put("schoolId", -1);
		}
		if(!requestObj.containsKey("idcard")){
			requestObj.put("idcard", "");
		}
		if(!requestObj.containsKey("desc")){
			requestObj.put("desc", "");
		}
		if(!requestObj.containsKey("address")){
			requestObj.put("address", "");
		}
		
		

		return this._getReturnEnd("persionalCenter","submitLoginUser", requestObj.toString(), ActiveUrl.CLUB_PERSIONAL_IP_URL);
	}


	/**
	 * getMessages(获取消息信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getMessages(String requestParam){
		return this._getReturnEnd("clubCenter","getMessages", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}
	
	/**
	 * getMessages(获取消息信息【手机】)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getMessagesForMobile(String requestParam){
		return this._getReturnEnd("clubCenter","getMessagesForMobile", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}
	
	

	/**
	 * getClubRights(获取俱乐部权益信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getClubRights(String requestParam){
		return this._getReturnEnd("clubCenter","getClubRights", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}

	/**
	 * buyClubRights(购买俱乐部权益)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String buyClubRights(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionObjectType = requestObj.getInteger("actionType");
		switch (actionObjectType) {
		case 3:
			return this._getReturnEnd("clubCenter","buyClubRights", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
		default:
			requestObj.put("classType",0);
			requestObj.put("classId",0);
			return this._getReturnEnd("clubCenter","buyClubRights", requestObj.toJSONString(), ActiveUrl.CLUB_CENTER_IP_URL);
		}
	}

	/**
	 * getIncomeAndExpenses(获取俱乐部或个人的账务收支信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getIncomeAndExpenses(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType.intValue()) {
		case 3:
			requestObj.put("incomeAndExpenses","0");
			requestObj.put("accountType","0");
			return this._getReturnEnd("clubCenter","getIncomeAndExpenses", requestObj.toString(), ActiveUrl.CLUB_CENTER_IP_URL);
		case 4:	
			requestObj.put("incomeAndExpenses","0");
			requestObj.put("accountType","0");
			return this._getReturnEnd("clubCenter","getIncomeAndExpenses", requestObj.toString(), ActiveUrl.CLUB_CENTER_IP_URL);
		default:
			return this._getReturnEnd("clubCenter","getIncomeAndExpenses", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
		}


	}

	/**
	 * getCashing(获取提现信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getCashing(String requestParam){

		return this._getReturnEnd("clubCenter","getCashing", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}

	/**
	 * applyCashing(提交提现申请)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String applyCashing(String requestParam){
		return this._getReturnEnd("clubCenter","applyCashing", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}

	/**
	 * getAccountTypes(获取账户类型标识信息)
	 * @param requestParam 请求参数
	 * @author lijin
	 * @return
	 */
	public String getAccountTypes(String requestParam){
		return this._getReturnEnd("clubCenter","getAccountTypes", requestParam, ActiveUrl.CLUB_CENTER_IP_URL);
	}

	/**
	 * login(登录)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String login(String requestParam){
		return this._getReturnEnd("user","login", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * logout(登出)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String logout(String requestParam){
		return this._getReturnEnd("user","logout", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * regist(提交注册或密码找回信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String regist(String requestParam){
		return this._getReturnEnd("user","regist", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * resetPassword(密码重置)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String resetPassword(String requestParam){
		return this._getReturnEnd("user","resetPassword", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * getLoginUser(获取登录用户的信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getLoginUser(String requestParam){
		return this._getReturnEnd("user","getLoginUser", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * getUsers(获取人员信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getUsers(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		if(inquireType==1){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==2){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameFieldId", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==3){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==4){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==5){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==6){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("classType", "0");
			requestObj.put("quizId", 0);
		}else if(inquireType==7){
			requestObj.put("memberId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==8){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==9){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==10){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==11){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==12){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==13){
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==14){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==15){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==16){
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}else if(inquireType==17){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==18){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==19){
			requestObj.put("memberId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==20){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==21){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==22){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==23){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==24){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==25){
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==26){
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==27){
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==28){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==29){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
		}else if(inquireType==30){
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
		}
		else{
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
		return this._getReturnEnd("user","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * exchangeAccount(一级货币兑换虚拟物品)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String exchangeAccount(String requestParam){
		return this._getReturnEnd("recharge","exchangeAccount", requestParam, ActiveUrl.RECHARGE_CONTROLLER_IP_URL);
	}

	/**
	 * submitDeposit(充值)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitDeposit(String requestParam){
		return this._getReturnEnd("recharge","submitDeposit", requestParam, ActiveUrl.RECHARGE_CONTROLLER_IP_URL);
	}

	/**
	 * checkPayment(校验支付是否成功)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String checkPayment(String requestParam){
		return this._getReturnEnd("recharge","checkPayment", requestParam, ActiveUrl.RECHARGE_CONTROLLER_IP_URL);
	}

	/**
	 * submitIdentifyingCode(发送验证码)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitIdentifyingCode(String requestParam){
		return this._getReturnEnd("verificationCode","submitIdentifyingCode", requestParam, ActiveUrl.VERIFICATION_CODE_CONTROLLER_IP_URL);
	}

	/**
	 * submitRedPacket(发红包)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitRedPacket(String requestParam){
		return this._getReturnEnd("redpacket","submitRedPacket", requestParam, ActiveUrl.RED_PACKET_CONTROLLER_IP_URL);
	}

	/**
	 * getRedPacketType(获取红包类型信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getRedPacketType(String requestParam){
		return this._getReturnEnd("redpacket","getRedPacketType", requestParam, ActiveUrl.RED_PACKET_CONTROLLER_IP_URL);
	}

	/**
	 * getRedPackets(获取红包信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getRedPackets(String requestParam){
		return this._getReturnEnd("redpacket","getRedPackets", requestParam, ActiveUrl.RED_PACKET_CONTROLLER_IP_URL);
	}

	/**
	 * getChapters(获取课程章节信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getChapters(String requestParam){
		return this._getReturnEnd("course","getChapters", requestParam, ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * getCourseCards(获取课程卡信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getCourseCards(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		if(inquireType==1){
			requestObj.put("chapterId", "");
			requestObj.put("courseCardId","");
		}else if(inquireType==2){
			requestObj.put("classId", "");
			requestObj.put("courseCardId","");
		}else if(inquireType==3){
			requestObj.put("classId", "");
			requestObj.put("chapterId", "");
		}else{
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
		return this._getReturnEnd("course","getCourseCards", requestObj.toString(), ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * submitCourseCard(提交自定义课程卡)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitCourseCard(String requestParam){
		return this._getReturnEnd("course","submitCourseCard", requestParam, ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * getCourse(获取课程信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getCourse(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		if(inquireType==1){
			requestObj.put("classId", "");
			requestObj.put("classType",0);
			requestObj.put("courseId","");
			requestObj.put("platformModule",0);
		}else if(inquireType==2){
			requestObj.put("courseCardId", "");
			requestObj.put("courseId","");
			requestObj.put("platformModule",0);
		}else if(inquireType==3){
			requestObj.put("start", 0);
			requestObj.put("limit",0);
			requestObj.put("requestSide", 0);
			requestObj.put("platformModule",0);
		}else if(inquireType==4){
			requestObj.put("classId", "");
			requestObj.put("classType",0);
			requestObj.put("courseId","");
			requestObj.put("platformModule",0);
			requestObj.put("courseCardId", "");
			requestObj.put("requestSide",0);
		}else{
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
		return this._getReturnEnd("course","getCourse", requestObj.toString(), ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * submitCourse(提交自定义课程信息)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitCourse(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer classType = requestObj.getInteger("classType");
		if(classType==1){
			requestObj.put("classId", "");//教学班不需要班级id
		}
		return this._getReturnEnd("course","submitCourse", requestObj.toString(), ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * submitCourseShowType(设置课程显示类型)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitCourseShowType(String requestParam){
		return this._getReturnEnd("course","submitCourseShowType", requestParam, ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getTopics 
	 * @Description: 获取话题
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 上午11:17:14
	 */
	public String getTopics(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("club","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 2:
			requestObj.put("memberId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("club","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 3:
			requestObj.put("memberId", 0);
			requestObj.put("clubId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			return this._getReturnEnd("club","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 4:
			requestObj.put("memberId", 0);
			requestObj.put("topicId", 0);
			return this._getReturnEnd("club","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 5:
			requestObj.put("clubId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("club","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/** 
	 * @Title: submitTopic 
	 * @Description: 提交话题
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午3:50:41
	 */
	public String submitTopic(String requestParam){
		return this._getReturnEnd("club","submitTopic", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}


	/** 
	 * @Title: getReminds 
	 * @Description: 获取提醒
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午3:50:41
	 */
	public String getReminds(String requestParam){
		return this._getReturnEnd("club","getReminds", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: submitRemind 
	 * @Description: 添加提醒
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午3:56:51
	 */
	public String submitRemind(String requestParam){
		return this._getReturnEnd("club","submitRemind", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getWebNotices
	 * @Description: 获取通知
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午3:58:43
	 */
	public String getWebNotices(String requestParam){
		return this._getReturnEnd("club","getWebNotices", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: submitNotice 
	 * @Description: 添加通知
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午4:00:20
	 */
	public String submitNotice(String requestParam){
		return this._getReturnEnd("club","submitNotice", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getMoods 
	 * @Description: 获取心情
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午4:01:30
	 */
	public String getMoods(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			return this._getReturnEnd("club","getMoods", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 2:
			requestObj.put("memberId", 0);
			return this._getReturnEnd("club","getMoods", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 3:
			requestObj.put("clubId", 0);
			requestObj.put("timeRange", 0);
			return this._getReturnEnd("club","getMoods", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/** 
	 * @Title: getDynamics 
	 * @Description: 获取动态
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午4:02:45
	 */
	public String getDynamics(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			return this._getReturnEnd("club","getDynamics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 2:
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);
			return this._getReturnEnd("club","getDynamics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 3:
			requestObj.put("memberId", 0);
			requestObj.put("clubId", 0);
			return this._getReturnEnd("club","getDynamics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 4:
			requestObj.put("memberId", 0);
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			return this._getReturnEnd("club","getDynamics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 5:
			requestObj.put("memberId", 0);
			requestObj.put("classId", 0);
			requestObj.put("classType", 0);
			requestObj.put("clubId", 0);
			return this._getReturnEnd("club","getDynamics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/** 
	 * @Title: submitUserDesc 
	 * @Description: 提交个人签名
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:10:59
	 */
	public String submitUserDesc(String requestParam){
		return this._getReturnEnd("user","submitUserDesc", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: submitBindingNumber 
	 * @Description: 绑定手机号
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:13:37
	 */
	public String submitBindingNumber(String requestParam){
		return this._getReturnEnd("user","submitBindingNumber", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: submitFeedback 
	 * @Description: 提交意见反馈
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:14:11
	 */
	public String submitFeedback(String requestParam){
		return this._getReturnEnd("user","submitFeedback", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getProtocol 
	 * @Description: 获取协议内容
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:14:43
	 */
	public String getProtocol(String requestParam){
		return this._getReturnEnd("user","getProtocol", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getCompanies 
	 * @Description: 获取企业信息
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:17:16
	 */
	public String getCompanies(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			return this._getReturnEnd("user","getCompanies", requestObj.toJSONString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		case 2:
			return this._getReturnEnd("user","getCompanies", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/** 
	 * @Title: getVerifyPicture 
	 * @Description: 申请获取图片验证码
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月6日 下午5:19:48
	 */
	public String getVerifyPicture(String requestParam){
		return this._getReturnEnd("user","getVerifyPicture", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}



	//-----------------------------------------------------移动端----------------------------------------------------------------------

	/**
	 * 移动端：获取广告信息
	 * @param requestParam
	 * @return
	 * @author 				lw
	 * @date				2016年7月18日  上午10:38:23
	 */
	public String getAdsForMobile(String requestParam){
		return this._getReturnEnd("getads","getAdsForMobile", requestParam, ActiveUrl.ADVERTISED);
	}



	/**
	 * 移动端：提交作业或人员的评分
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitScoreForMobile(String requestParam){
		return this._getReturnEnd("teacherhomeworksscore","submitScore", requestParam, ActiveUrl.TEACHER_HOME_WORKS);
	}



	/**
	 * 移动端：提交作业或人员的评分
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitCertRequestForMobile(String requestParam){
		return this._getReturnEnd("teachercert","submitCertRequest", requestParam, ActiveUrl.TEACHER_CERT);
	}


	/**
	 * 移动端：提交问题的回复信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitAnswerForMobile(String requestParam){
		//参数转换
		JSONObject json = JSONObject.parseObject(requestParam);
		if(json != null && json.getString("answerBodyText") != null){
			json.put("answerBody", json.getString("answerBodyText"));
		}
		return this._getReturnEnd("submitanswer","submitAnswer", json.toJSONString(), ActiveUrl.QUESTIONS);
	}

	/**
	 * 移动端：获取竞猜信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getQuizsForMobile(String requestParam){
		JSONObject json = JSONObject.parseObject(requestParam);
		Integer inquireType = json.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			json.put("quizTopicId", json.getInteger("gameId"));
			json.put("memberId", 0);
			json.put("_type", GameConstants.TYPE_APP);
			break;
		case 2:
			json.put("quizTopicId", 0);
			json.put("_type", GameConstants.TYPE_APP);
			break;
		}
		return this._getReturnEnd("getquizsApp","getQuizs", json.toJSONString(), ActiveUrl.GUESS);
	}
	/**
	 * 移动端：获取竞猜信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getGameEventsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case GameConstants.MATCH_GAMEEVENTS_RECOMMEND:
		case GameConstants.MATCH_GAMEEVENTS_HOTTEST:
		case GameConstants.MATCH_GAMEEVENTS_NEWEST:
		case GameConstants.MATCH_GAMEEVENTS_CLUB_FOLLOW:
			requestObj.put("gameEventId", 0);
			requestObj.put("searchWord", "");
			break;
		case GameConstants.MATCH_GAMEEVENTS_SEARCH:
			requestObj.put("gameEventId", 0);
			break;
		case GameConstants.MATCH_GAMEEVENTS_ID:
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("searchWord", 0);

			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_GET_GAMEEVENTS_PARAM).toJSONString();
		}
		requestObj.put("_type", GameConstants.TYPE_APP);
		return this._getReturnEnd("getgameeventsApp","getGameEvents", requestObj.toJSONString(), ActiveUrl.MATCH);
	}


	/**
	 * 移动端：提交问题信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String submitQuestionForMobile(String requestParam){
		JSONObject parse = JSONObject.parseObject(requestParam);
		String str = null;
		if(parse!=null && (str = parse.getString("questionBodyText")) !=null ){
			parse.put("questionBody", str);
		}
		return this._getReturnEnd("submitquestion","submitQuestion", parse.toJSONString(), ActiveUrl.QUESTIONS);
	}


	/**
	 * 移动端：获取答疑信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getQuestionsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case GameConstants.STUDENT_QUESTION_PAGE:
			requestObj.put("questionId", 0);
			break;
		case GameConstants.STUDENT_QUESTION_ENTITY:
			requestObj.put("classId", 0);
			requestObj.put("chapterId", 0);
			requestObj.put("isExcellent", 0);
			requestObj.put("limit", 0);
			requestObj.put("start", 0);
			break;
		default:
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_QUESTIONS_PARAM).toJSONString();
		}
		requestObj.put("_type", GameConstants.TYPE_APP);
		return this._getReturnEnd("getquestionsApp","getQuestions", requestObj.toJSONString(), ActiveUrl.QUESTIONS);
	}

	/**
	 * 移动端：获取提问的回复信息
	 * @param requestParam 请求参数
	 * @author lw
	 * @return
	 */
	public String getAnswersForMobile(String requestParam){
		JSONObject parseObject = JSONObject.parseObject(requestParam);
		Integer inquireType = parseObject.getInteger("inquireType");
		parseObject.put("_type", "APP");
		if(inquireType==1){
			parseObject.put("answerId", "");
		}
		return this._getReturnEnd("getanswersformobile","getAnswersForMobile", parseObject.toJSONString(), ActiveUrl.QUESTIONS);
	}




	/**
	 * loginForMobile(移动端登录)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String loginForMobile(String requestParam){
		return this._getReturnEnd("user","loginForMobile", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * getLoginUserForMobile(获取登录用户的信息移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getLoginUserForMobile(String requestParam){
		return this._getReturnEnd("user","getLoginUserForMobile", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * getUsersForMobile(获取人员信息移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return TODO
	 */
	public String getUsersForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		if(inquireType==1){//队长选人
			requestObj.put("inquireType", 2);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameFieldId", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_1","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==2){
			requestObj.put("inquireType", 5);
			requestObj.put("gameFieldId", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_2","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==3){
			requestObj.put("inquireType", 6);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("classType", "0");
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_3","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==4){
			requestObj.put("inquireType", 7);
			requestObj.put("memberId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_4","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==5){
			requestObj.put("inquireType", 8);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_5","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==6){
			requestObj.put("inquireType", 13);
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_6","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==7){
			requestObj.put("inquireType", 18);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_7","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==8){
			requestObj.put("inquireType", 19);
			requestObj.put("memberId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_8","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==9){
			requestObj.put("inquireType", 21);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_9","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==10){
			requestObj.put("inquireType", 23);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_10","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==11){
			requestObj.put("inquireType", 25);
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_11","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==12){
			requestObj.put("inquireType", 26);
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_12","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==13){
			requestObj.put("inquireType", 27);
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_12","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==14){
			requestObj.put("inquireType", 28);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("searchWord", "0");
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_14","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else if(inquireType==15){
			requestObj.put("inquireType", 29);
			requestObj.put("memberId", "0");
			requestObj.put("shcoolId", "0");
			requestObj.put("classId", "0");
			requestObj.put("classType", 0);
			requestObj.put("clubId", "0");
			requestObj.put("gameId", "0");
			requestObj.put("gameFieldId", "0");
			requestObj.put("platformModule", 0);
			requestObj.put("gameEventId", "0");
			requestObj.put("clubApplyStatus", 0);
			requestObj.put("taskId", "0");
			requestObj.put("hasQualification", 0);
			requestObj.put("quizId", 0);
			return this._getReturnEnd("user_inquire_15","getUsers", requestObj.toString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		}else{
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * getChaptersForMobile(获取课程章节信息移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String getChaptersForMobile(String requestParam){
		return this._getReturnEnd("course","getChaptersForMobile", requestParam, ActiveUrl.COURSE_CONTROLLER_IP_URL);
	}

	/**
	 * getCourseForMobile(获取课程卡信息移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return TODO
	 */
	public String getCourseForMobile(String requestParam){
		JSONObject jsonData = JSONObject.parseObject(requestParam);
		Integer inquireType = 0;
		if(jsonData == null || (inquireType = jsonData.getInteger("inquireType")) == null ){
			return Common.getReturn(1, "inquireType参数错误").toJSONString(); 

		}

		if(jsonData.getInteger("courseId") == null){
			jsonData.put("courseId", 0);
		}
		
		if(inquireType == GameConstants.COURSE_LIST || inquireType == GameConstants.COURSE_CLASS_LIST){
			return this._getReturnEnd("courseApp","getCourseForMobile", jsonData.toJSONString(), ActiveUrl.COURSE_CONTROLLER_IP_URL);

		}else if(inquireType == GameConstants.COURSE_CLASS_INFO){
			jsonData.put("start", 0);
			jsonData.put("limit", 3);
			jsonData.put("chapterId", 0);
			jsonData.put("ccType", 0);
			jsonData.put("requestSide", 0);
			jsonData.put("requestSide", 0);
			return this._getReturnEnd("courseApp3","getCourseForMobile", jsonData.toJSONString(), ActiveUrl.COURSE_CONTROLLER_IP_URL);

		}

		return Common.getReturn(1, "inquireType参数错误").toJSONString(); 

	}

	/**
	 * submitIdentifyingCodeForMobile(发送验证码移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String submitIdentifyingCodeForMobile(String requestParam){
		return this._getReturnEnd("verificationCode","submitIdentifyingCode", requestParam, ActiveUrl.VERIFICATION_CODE_CONTROLLER_IP_URL);
	}

	/**
	 * resetPasswordForMobile(密码重置移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String resetPasswordForMobile(String requestParam){
		return this._getReturnEnd("user","resetPassword", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * registForMobile(提交注册或密码找回信息移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String registForMobile(String requestParam){
		return this._getReturnEnd("user","regist", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/**
	 * logoutForMobile(登出移动端)
	 * @param requestParam 请求参数
	 * @author yy
	 * @return
	 */
	public String logoutForMobile(String requestParam){
		return this._getReturnEnd("user","logoutForMobile", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getTopicsForMobile 
	 * @Description: 移动端：获取话题
	 * @param requestParam	请求参数
	 * @return  参数说明 
	 * @return String
	 * @author liulin
	 * @date 2016年7月18日 下午7:34:03
	 */
	public String getTopicsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("clubForMobile","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 2:
			requestObj.put("memberId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("clubForMobile","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 3:
			requestObj.put("memberId", 0);
			requestObj.put("clubId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			requestObj.put("start", 0);
			requestObj.put("limit", 0);
			return this._getReturnEnd("clubForMobile","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 4:
			requestObj.put("inquireType", 5);
			requestObj.put("clubId", 0);
			requestObj.put("topicId", 0);
			requestObj.put("startDate", 0);
			requestObj.put("endDate", 0);
			requestObj.put("searchWord", 0);
			return this._getReturnEnd("clubForMobile","getTopics", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/** 
	 * @Title: submitTopicForMobile 
	 * @Description: 移动端：发话题
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月18日 下午7:38:17
	 */
	public String submitTopicForMobile(String requestParam){
		return this._getReturnEnd("club","submitTopic", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}
	/** 
	 * @Title: getDynamicsForMobile 
	 * @Description: 移动端：获取动态
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月18日 下午8:04:16
	 */
	public String getDynamicsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("club","getDynamicsForMobile", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		case 2:
			requestObj.put("clubId", 0);
			return this._getReturnEnd("club","getDynamicsForMobile", requestObj.toJSONString(), ActiveUrl.CLUB_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * submitQuizBetting(竞猜下注 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitQuizBettingForMobile(String requestParam){
		return this._getReturnEnd("submitQuiz","submitQuizBetting", requestParam, ActiveUrl.BET_QUIZ_IP_URL);
	}

	/**
	 * getVirtualGoods(获取登录用户的虚拟物品 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getVirtualGoodsForMobile(String requestParam){
		return this._getReturnEnd("virtualgood","getVirtualGoods", requestParam, ActiveUrl.GET_USER_VIRTUAL_URL);
	}

	/**
	 * getVirtualGoods(获取一级货币 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getFLevelAccountForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("clubId", 0);
			return this._getReturnEnd("levelaccountForMobile","getFLevelAccount", requestObj.toString(), ActiveUrl.GET_USER_LEVEL_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}

	/**
	 * submitClubOperation(加入或退出俱乐部 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitClubOperationForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer actionType = requestObj.getInteger("actionType");
		switch (actionType) {
		case 1:
			return this._getReturnEnd("submitClubOpera","submitClubOperation", requestObj.toString(), ActiveUrl.JOIN_OR_EXITCLUB_URL);
		case 2:
			requestObj.put("applicationContent", "0");
			return this._getReturnEnd("submitClubOpera","submitClubOperation", requestObj.toString(), ActiveUrl.JOIN_OR_EXITCLUB_URL);
		default:
			return Common.getReturn(1, "actionType参数错误").toJSONString();
		}
	}

	/**
	 * SubmitClubMember(俱乐部会员操作 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitClubMemberActionForMobile(String requestParam){
		return this._getReturnEnd("submitClubMem","submitClubMember", requestParam, ActiveUrl.SUBMIT_CLUBMEMBER_URL);
	}

	/**
	 * submitPrivateMessage(发私信 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String submitPrivateMessageForMobile(String requestParam){
		return this._getReturnEnd("submitpmssage","submitPrivateMessage", requestParam, ActiveUrl.SEND_PRIVATE_MESSAGES_URL);
	}

	/**
	 * getClubs(获取俱乐部信息 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getClubsForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("recordingActivity", 0);
			return this._getReturnEnd("getClubsForMobile","getClubsForMobile", requestObj.toString(), ActiveUrl.GET_CLUBSFORMOBILE_INFO_URL);
		case 2:
			requestObj.put("searchWord", "0");
			return this._getReturnEnd("getClubsForMobile","getClubsForMobile", requestObj.toString(), ActiveUrl.GET_CLUBSFORMOBILE_INFO_URL);
		case 3:
			requestObj.put("searchWord", "0");
			requestObj.put("clubId", 0); 
			return this._getReturnEnd("getClubsForMobile","getClubsForMobile", requestObj.toString(), ActiveUrl.GET_CLUBSFORMOBILE_INFO_URL);
		case 4:
			requestObj.put("searchWord", "0");
			return this._getReturnEnd("getClubsForMobile","getClubsForMobile", requestObj.toString(), ActiveUrl.GET_CLUBSFORMOBILE_INFO_URL);
		case 5:
			requestObj.put("searchWord", "0");
			requestObj.put("start", 0);
			requestObj.put("limit", 0); 
			return this._getReturnEnd("getClubsForMobiles","getClubsForMobile", requestObj.toString(), ActiveUrl.GET_CLUBSFORMOBILE_INFO_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}

	}


	/**
	 * getPrivateMessages(获取私信 移动端)
	 * @param requestParam 请求参数
	 * @author chaixw
	 * @return
	 */
	public String getPrivateMessagesForMobile(String requestParam){
		return this._getReturnEnd("privatemessageForMoblie","getPrivateMessagesForMobile", requestParam, ActiveUrl.GET_PRIVATE_LETTER_FOR_MOBILR_URL);
	}

	public String getDynamicEntryForMobile(String requestParam){
		return this._getReturnEnd("club","getDynamicEntryForMobile", requestParam, ActiveUrl.CLUB_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: submitBindingNumberForMobile 
	 * @Description: 移动端：绑定手机号
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月19日 上午9:22:50
	 */
	public String submitBindingNumberForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		requestObj.put("verifyPictureNumber", 0);
		return this._getReturnEnd("user","submitBindingNumber", requestObj.toJSONString(), ActiveUrl.USER_CONTROLLER_IP_URL);
	}

	/** 
	 * @Title: getCompaniesForMobile 
	 * @Description: 移动端：获取企业信息
	 * @param requestParam	请求参数
	 * @return String
	 * @author liulin
	 * @date 2016年7月19日 上午9:58:13
	 */
	public String getCompaniesForMobile(String requestParam){
		JSONObject requestObj = JSONObject.parseObject(requestParam);
		Integer inquireType = requestObj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			requestObj.put("memberId", 0);
			return this._getReturnEnd("user","getCompaniesForMobile", requestObj.toJSONString(), ActiveUrl.USER_CONTROLLER_IP_URL);
		case 2:
			return this._getReturnEnd("user","getCompaniesForMobile", requestParam, ActiveUrl.USER_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}
	/**PC getVotes 获取投票列表信息
	 * @param requestParam
	 * @return
	 */
	public String getVotes(String requestParam){
		JSONObject obj = JSONObject.parseObject(requestParam);
		Integer inquireType = obj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			obj.put("voteId", 0);
			return this._getReturnEnd("getVotes","getVotes", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 2:
			obj.put("voteId", 0);
			return this._getReturnEnd("getVotes","getVotes", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 3:
			obj.put("voteId", 0);
			
			return this._getReturnEnd("getVotes","getVotes", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 4:
			return this._getReturnEnd("getVotes","getVotes", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
			
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
		
	}
	
	/**移动端 getVotes 获取投票列表信息
	 * @param requestParam
	 * @return
	 */
	public String getVotesForMobile(String requestParam){
		JSONObject obj = JSONObject.parseObject(requestParam);
		Integer inquireType = obj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("getVotesForMobile","getVotesForMobile", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 2:
			return this._getReturnEnd("getVotesForMobile","getVotesForMobile", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 3:
			return this._getReturnEnd("getVotesForMobile","getVotesForMobile", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		case 4:
			return this._getReturnEnd("getVotesForMobile","getVotesForMobile", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
			
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
		
	}
	
	/**获取投票选项信息
	 * @param requestParam
	 * @return
	 */
	public String getVoteOptions(String requestParam){
		JSONObject obj = JSONObject.parseObject(requestParam);
		Integer inquireType = obj.getInteger("inquireType");
		switch (inquireType) {
		case 1:
			return this._getReturnEnd("getVoteOptions","getVoteOptions", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
		default:
			return Common.getReturn(1, "inquireType参数错误").toJSONString();
		}
	}
	//
	/**提交投票选项信息
	 * @param requestParam
	 * @return
	 */
	public String submitVoteOption(String requestParam){
		JSONObject obj = JSONObject.parseObject(requestParam);
		return this._getReturnEnd("submitVoteOption","submitVoteOption", obj.toJSONString(), ActiveUrl.VOTE_CONTROLLER_IP_URL);
	}
}
