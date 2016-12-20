package com.seentao.stpedu.api.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.config.ApiConfig;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

import hprose.client.HproseClient;
import hprose.client.HproseHttpClient;

/**
 * JOSON转换组件
 * @author 	lw
 * @date	2016年7月7日  下午2:50:14
 *
 */
public class JsonComponent {

	//json文件名
	private String apiJsonFileName;
	
	//执行接口函数名
	private String funactionName;
	
	//接口函数返回json结果
	private String funactionResultJson;
	
	//执行结果
	private boolean isSuccess = false;
	
	//----------------临时对象数据---------------------
	
	//接口函数返回JSONObject数据;
	private JSONObject funcResultJson;
	
	//页面返回JSONObject
	private JSONObject webResult = new JSONObject();
	
	//接口函数级别API
	private JSONObject API_FUNC;
	//接口函数返回起点级别API
	private JSONObject API_START;
	
	
	/*
	 * 解析对象：
	 * 1.解析OBJECT对象
	 * 2.解析Array对象
	 */
	private ParseJSONObject pJsonObj = new ParseJSONObject();
	private ParseJSONArray 	pJsonArr = new ParseJSONArray();
	
	/**
	 * 初始化
	 * new 对象是防并发
	 * @param funactionResultJson	接口函数返回
	 * @param funactionName			接口函数方法名
	 * @param apiJsonFileName		接口函数规范.json文件名
	 * @param serviceApp 
	 */
	public JsonComponent(String funactionResultJson, String funactionName, String apiJsonFileName){
		this.funactionResultJson = funactionResultJson;
		this.funactionName = funactionName;
		this.apiJsonFileName = apiJsonFileName;
	}
	
	
	
	/**
	 * 初始化
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:09:06
	 */
	public JsonComponent init(){
		
		//	1. 获取接口函数返回JSONObject数据;
		JSONObject json = JSONObject.parseObject(this.funactionResultJson);
		
		//	2. 函数必须要有返回标记
		if(json == null || json.getInteger(GameConstants.CODE) == null){
			return error(1, funactionName + "：接口没有返回结果!");
		}
		
		//	3. 初始化 接口函数返回JSONObject数据;
		this.funcResultJson = json; 
		
		//	4. 校验是是否是错误消息：如果是错误消息返回
		if(this.isResolve(this.funcResultJson)){
			return this;
		}
		
		
		/* 下面是正确消息返回  */
		
		
		//	5. 接口规范json文件获取
		json = getFileJSONObject(GameConstants.JSONAPI_PATH + apiJsonFileName +".json");
		if(CollectionUtils.isEmpty(json)){
			return error(1,"请设置 "+funactionName+" 的 apiJson规范[xx.json]文件.");
		}
		
		//	6. 获取接口规范函数级别API
		API_FUNC = (JSONObject) json.get(funactionName);
		if(CollectionUtils.isEmpty(API_FUNC)){
			return error(1,"请设置apiJson规范中["+funactionName+"函数返回api规范].");
		}
		
		/*
		 * 7. 获取接口规范起点级别API:判断是否使用api
		 */
		API_START = (JSONObject) API_FUNC.get(GameConstants.JSONAPI_KEY);
		if(CollectionUtils.isEmpty(API_START)){
			
			/*	7.1 不使用api直接返回字符串
			 * 	1.api和 返回参数都没有 起点，
			 * 	2.接口返回code = 0 直接返回成功
			 */
			if(this.isUseApi()){
				return this;
				
			//	7.2使用api但是没有配置
			}else{
				return error(1,"apiJson 规范中没有起点!");
			}
		}
		
		return success(null);
	}
	
	
	
	/**
	 * 程序主执行方法
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:32:13
	 */
	public JsonComponent execute(){
		
		//	1. 初始化结果判断
		if(!this.isSuccess){
			return this;
		}
		
		/*
		 * 2. 函数返回json 和 api规范对比
		 * 	1.获取起点元素.
		 * 	2.判断起点元素 中 元素类型进行对比.
		 * 	_key 	: 函数返回结果起点
		 * 	api_Key	： api起点
		 */
		JSONObject INPUT_KEY = (JSONObject) funcResultJson.get(GameConstants.JSONAPI_KEY);
		if(CollectionUtils.isEmpty(INPUT_KEY)){
			this.success(null);
			return this;
		}
		
		//	3. 获取函数返回起点的所有key
		Set<String> inputSet = INPUT_KEY.keySet();
		
		/*
		 * 	4.根据api 起点设置进行解析 
		 * 		对比函数返回数据
		 */
		String funcKey = null;
		for(Entry<String, Object> apiEn : API_START.entrySet()){
			funcKey = apiEn.getValue().toString();
			
			//	4.1 拥有返回结果
			if(inputSet.contains(funcKey)){
				webResult.put(apiEn.getKey(), this.jsonFactory(funcKey, INPUT_KEY.get(funcKey)));
			
			/*
			 * 	4.2 没有返回结果，给出默认值：ParseJSON.DEFAULT_VAL
			 * 	TODO : 	目前有缺陷：如果接口返回list或者obj对象没有指定默认的JSONArray和JSONObject 统一处理成ParseJSON.DEFAULT_VAL
			 * 			处理：修改apiJson规范文件，把映射对象key给定对象或者数组后缀，然后在下面判断key的后缀，给定JSONArray或者JSONObject
			 */
			}else{
				webResult.put(apiEn.getKey(), ParseJSON.DEFAULT_VAL);
			}
		}
		
		return this.success(null);
	}

	


	/**
	 * 工厂选择解析方式
	 * @param funcKey		调用函数名
	 * @author 				lw
	 * @param inputData 	输入数据
	 * @date				2016年7月7日  下午4:02:34
	 */
	private Object jsonFactory(String funcKey, Object inputData) {
		
		//	1. json对象解析
		if(inputData instanceof JSONObject){
			pJsonObj.clean();
			return pJsonObj.init(API_FUNC, funcKey, inputData).execute().getResult();
		
		//	2. json数组解析
		}else if(inputData instanceof JSONArray){
			pJsonArr.clean();
			return pJsonArr.init(API_FUNC, funcKey, inputData).execute().getResult();
		
		//	3. 基本类型解析
		}else{
			return inputData;
		}
		
	}



	/**
	 * 判断是否是错误消息
	 * @param funcResultJson2
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:35:37
	 */
	private boolean isResolve(JSONObject funcResultJson) {
		
		//	1. 获取返回状态
		Integer code = funcResultJson.getInteger(GameConstants.CODE);
		
		//	2. 获取返回消息码
		String msg = funcResultJson.getString(GameConstants.MSG);
		
		/*
		 * 	3.执行逻辑判断 
		 * 		a.错误状态拦截：执行错误逻辑
		 * 		b.正常状态继续解析
		 */
		if(code != 0){
			
			//	3.1 执行错误逻辑
			this.error(1, msg);
			return true;
		}
		
		return false;
	}



	/**
	 * 执行失败
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:24:16
	 */
	private JsonComponent error(Integer code, String msg){
		
		//	1. 返回结果加入错误状态
		webResult.put(GameConstants.CODE, code);
		
		//	2. 错误消息服务查询
		HproseClient client = new HproseHttpClient();
		client.useService(ActiveUrl.ERROR_MESSAGE);
		try {
			String returnResult = (String)client.invoke(ActiveUrl.ERROR_MESSAGE_FUNCTION_NAME, new Object[]{msg});
			if(returnResult != null){
				
				//	3.1 返回结果加入错误消息
				webResult.put(GameConstants.MSG,returnResult);
			}
		} catch (IOException e) {
			
			//	3.2 返回结果加入错误消息
			webResult.put(GameConstants.MSG,msg);
			e.printStackTrace();
			LogUtil.error(this.getClass(), "error", String.valueOf(AppErrorCode.ERRORMESSAGE_ERROR), e);
		}
		
		this.isSuccess = false;
		
		return this;
	}
	
	

	/**
	 * 执行成功
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:24:16
	 */
	private JsonComponent success(String msg){
		webResult.put(GameConstants.CODE, 0);
		if(msg != null){
			webResult.put(GameConstants.MSG, msg);
		}
		this.isSuccess = true;
		return this;
	}
	
	/**
	 * 如果返回结果不使用api ： 
	 * 	1.api和 返回参数都没有 起点，
	 * 	2.接口返回code = 0 直接返回成功
	 * @return
	 * @author 			lw
	 * @date			2016年7月9日  下午4:05:50
	 */
	private boolean isUseApi(){
		
		//	1. 接口返回结果没有数据，不使用api
		if(this.funcResultJson.getInteger(GameConstants.CODE) == 0  && 
				(this.funcResultJson.getJSONObject(GameConstants.JSONAPI_KEY) == null || "".equals(this.funcResultJson.getJSONObject(GameConstants.JSONAPI_KEY)))){
			this.success(funcResultJson.getString(GameConstants.MSG));
			this.isSuccess = false;
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 获取文件的全部
	 * @param jsonFilePath
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午3:05:33
	 */
	public static JSONObject getFileJSONObject(String jsonFilePath){
		JSONObject jsonReturn = new JSONObject();
		InputStream in = ApiConfig.class.getClassLoader().getResourceAsStream(jsonFilePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = in.read()) != -1) {
				baos.write(i);
			}
			String apijson = baos.toString();
			jsonReturn = JSONObject.parseObject(apijson);
			jsonReturn.put(GameConstants.CODE, 0);
			return jsonReturn;
		} catch (IOException e) {
			jsonReturn.put(GameConstants.CODE, 1);
			jsonReturn.put("msg", "从文件里获取数据失败");
			return null;
		}
		
	}
	
	public JSON getReulst(){
		return webResult;
	}
	
	public static void main(String[] args) {
	/*	String str = "{'result':{'returnCount':0,'teachingRole':1,'questions':[{'answerNum':4,'answers':[{'aCreaterHeadLink':'www.filepath','aCreaterName':'爱捣蛋','answerId':4,'content':'我是俱乐123213','createTime':1467189922,'createUserId':1,'isDelete':0,'praiseNum':0,'questionId':3},{'aCreaterHeadLink':'www.filepath','aCreaterName':'爱捣蛋','answerId':5,'content':'我是俱乐123213哈哈','createTime':1467189937,'createUserId':1,'isDelete':0,'praiseNum':0,'questionId':3},{'aCreaterHeadLink':'www.filepath','aCreaterName':'爱捣蛋','answerId':6,'content':'我是俱乐123213哈哈12323','createTime':1467190008,'createUserId':1,'isDelete':0,'praiseNum':0,'questionId':3},{'aCreaterHeadLink':'www.filepath','aCreaterName':'爱捣蛋','answerId':7,'content':'我是俱乐123213哈哈12323','createTime':1467190028,'createUserId':1,'isDelete':0,'praiseNum':0,'questionId':3}],'classId':1,'content':'问题内容zz俱乐部','createTime':1467189890,'createUserId':1,'isDelete':0,'isEssence':0,'praiseNum':0,'questionId':3,'questionModule':3,'title':'问题zz俱乐部'}],'currentPage':0,'allPage':0},'code':0,'msg':''}";
		long start = System.currentTimeMillis();
		JSON reulst = new JsonComponent(str, "getQuestions", "getquestions").init().execute().getReulst();
		long end = System.currentTimeMillis();
		System.out.println("reulst: " + reulst.toJSONString() +" ---------time:"+(end - start));*/
		
	}
	
	
}
