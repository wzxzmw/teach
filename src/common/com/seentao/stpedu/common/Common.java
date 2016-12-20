package com.seentao.stpedu.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.utils.IdcardValidator;
import com.seentao.stpedu.utils.StringUtil;

public class Common {
	public static String DEFAULT_URL = "image.staoedu.com";
	/*
	 * 校验本地压缩 wangzx
	 */
	public static boolean checkPic(final String url){
		if(url.contains(DEFAULT_URL))
			return true;
		return false;
	}
	
	//获取json串并转化成json对象
	public static JSONObject parseJson(String jsonStr){
		JSONObject json = JSONObject.parseObject(jsonStr);
		JSONObject jsonBody=(JSONObject) json.get("body");
		JSONObject jsonRequestParam=(JSONObject) jsonBody.get("requestParam");
		return jsonRequestParam;
	}

	//根据虚拟日期转化成X年X月X日
	public static String parseDate(int dateInt){
		String returnResult="";
		HashMap<String,Integer> mapDate = parseDateMap(dateInt);
		int year = mapDate.get("year");
		int month= mapDate.get("month");
		int day = mapDate.get("day");

		if(year>0){
			returnResult += "第"+year+"年";
		}else{
			returnResult += "初始年";
		}

		returnResult += month+"月";
		returnResult += day+"日";

		return returnResult;
	}

	//根据虚拟日期转化成年月日map
	public static HashMap<String,Integer> parseDateMap(int dateInt){

		int year=dateInt/360;
		Double month=Math.ceil(Double.parseDouble(Float.valueOf(dateInt%360f/30f).toString()));
		int day=dateInt%360%30;

		if(year>0 && month>0){
			year = year;
		}else if(year>0 && month==0){
			year = year-1;
		}else{
			year = 0;
		}

		HashMap<String,Integer> mapDate = new HashMap<String,Integer>();
		mapDate.put("year", year);
		mapDate.put("month", (month>0)? month.intValue():12);
		mapDate.put("day", (day==0)? 30:day);
		return mapDate;
	}


	//通过年和阶段转化时间
	public static String toDate(int year,int stage){
		String returnResult="";
		if(year>1){
			returnResult+="第"+(year-1)+"年";
		}else{
			returnResult+="初始年";
		}
		if(stage==1){
			returnResult+="年初";
		}else if(stage==2){
			returnResult+="年末";
		}
		return returnResult;
	}

	

	/**
	 * 方法返回结果
	 * @param code
	 * @param msg
	 * @param result
	 * @return
	 */
	public static JSONObject getReturn(int code, String msg, Object result){
		return getReturn(code, msg, null, result);
	}

	public static JSONObject getReturn(int code,String msg){
		return getReturn(code, msg, null, null);
	}


	//	public static JSONObject getReturn(int code, String msg, Map<String,Object> tempMap){
	//		JSONObject objResult = new JSONObject();
	//		objResult.put("code", code);
	//		objResult.put("msg", msg);
	//		@SuppressWarnings("rawtypes")
	//		Iterator iter = tempMap.keySet().iterator();
	//		while (iter.hasNext()) {
	//			Object key = iter.next();
	//			objResult.put(key.toString(), tempMap.get(key));
	//			
	//		}
	//		return objResult;
	//	}


	public static JSONObject getReturn(int code,String msg,Map<String,Object> tempMap,Object result){
		
		if(msg == null){
			msg = "";
		}
		
		JSONObject objResult = new JSONObject();
		objResult.put(GameConstants.CODE, code);
		objResult.put(GameConstants.MSG, msg);
		objResult.put(GameConstants.JSONAPI_KEY, result);
		
		if(CollectionUtils.isEmpty(tempMap)){
			return objResult;
		}
		
		for(Entry<String, Object> en : tempMap.entrySet()){
			objResult.put(en.getKey(), en.getValue());
		}
		
		return objResult;
	}
	


	/**
	 * 方法返回结果
	 * @param objArray
	 * @param sb_output
	 * @return StringBuffer
	 * 格式类型：产品标识_产品名称_可产数量_产品单价_交货期，产品标识_产品名称_可产数量_产品单价_交货期
	 */
	public static StringBuffer getFormatStr(StringBuffer sb_output,Object [] objArray){

		if("".equals(sb_output.toString())){
			for(Object tempObj:objArray){
				sb_output.append(tempObj);
			}
			return sb_output;
		}else{
			sb_output.append(GameConstants.COMMA);
			for(Object tempObj:objArray){
				sb_output.append(tempObj);
			}
			return sb_output;
		}
	}



	/** 
	 * 对象属性转换为字段  例如：userName to user_name 
	 * @param property 字段名 
	 * @return 
	 */  
	public static String propertyToField(String property) {  
		if (null == property) {  
			return "";  
		}  
		char[] chars = property.toCharArray();  
		StringBuffer sb = new StringBuffer();  
		for (char c : chars) {  
			if (CharUtils.isAsciiAlphaUpper(c)) {  
				sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));  
			} else {  
				sb.append(c);  
			}  
		}  
		return sb.toString();  
	}  

	/** 
	 * 字段转换成对象属性 例如：user_name to userName 
	 * @param field 
	 * @return 
	 */  
	public static String fieldToProperty(String field) {  
		if (null == field) {  
			return "";  
		}  
		char[] chars = field.toCharArray();  
		StringBuffer sb = new StringBuffer();  
		for (int i = 0; i < chars.length; i++) {  
			char c = chars[i];  
			if (c == '_') {  
				int j = i + 1;  
				if (j < chars.length) {  
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));  
					i++;  
				}  
			} else {  
				sb.append(c);  
			}  
		}  
		return sb.toString();  
	}  

	//根据年,月获取当前年月初和月末的天数
	public static HashMap<String,Integer> getParseDateStarEnd(int year,int month){

		HashMap<String,Integer> mapDate = new HashMap<String,Integer>();

		mapDate.put("monthStar", year*360+month*30);
		mapDate.put("monthEnd", year*360+(month+1)*30);
		return mapDate;
	}


	/**
	 * 获取接口返回值里对应字段（从配置文件里获取）
	 * @param apiConfig json配置文件里对应的接口json
	 * @param param 对应后边的参数 如 "gameStage": "game_stage" 里应是"game_stage"
	 * @param result json文件里的key值
	 * @return 返回前边参数 如 "gameStage": "game_stage" 里应是"gameStage"
	 * @author zhengchunlei
	 */
	public static String getReturnParam(JSONObject apiConfig, String param){
		String returnParam = "";
		try{
			//获取配置文件里的返回结果参数
			JSONObject resultObject = JSONObject.parseObject(apiConfig.get("result").toString());  
			if(null != param && !"".equals(param)){
				if(resultObject.size()>0){
					//如果接口文档返回值有多个，则返回多个参数 或者  返回明细，两个方法通用
					for(Entry<String,Object> entryResult : resultObject.entrySet()){
						String strval = entryResult.getKey().toString();
						//获取配置文件里的返回结果参数
						Object object = apiConfig.get(strval);
						if(null != object){
							JSONObject result1Object = JSONObject.parseObject(apiConfig.get(strval).toString());
							if(null != result1Object && result1Object.size()>0){
								for(Entry<String,Object> entryResult1 : result1Object.entrySet()){
									//获取配置文件里的返回结果参数
									if(param.equals(entryResult1.getValue().toString())){
										returnParam = entryResult1.getKey();
										break;
									}
								}
							}
						}else{
							//获取配置文件里的返回结果参数
							if(param.equals(entryResult.getValue().toString())){
								returnParam = entryResult.getKey();
								break;
							}	 
						}


					}
				}
			}else{
				if(resultObject.size()>0){
					//如果接口文档返回值有多个，则返回多个参数 或者  返回明细，两个方法通用
					for(Entry<String,Object> entryResult : resultObject.entrySet()){
						String strval = entryResult.getKey().toString();
						//获取配置文件里的返回结果参数
						Object object = apiConfig.get(strval);
						if(null != object){
							returnParam += strval+",";
						}
					}
					returnParam=returnParam.substring(0,returnParam.length()-1);
				}

			}
		}catch(Exception e){
			return returnParam;
		}

		return returnParam;
	}


	/**  
	 * 根据开始时间和结束时间，计算两个时间段的年份 
	 * @param firstDate 开始时间
	 * @param endDate 结束时间
	 * @return   相差的年份
	 * @author W.jx
	 * @date 2016年6月3日 上午10:16:33 
	 */
	public static Integer getYearBettweenFirstAndEnd(Integer firstDate, Integer endDate){
		Integer year = 0;
		if(endDate >= firstDate){
			year = (endDate - firstDate)/360;
		}
		return year;
	}

	/*** 
	 *  
	 * 获取JSON类型 
	 * 判断规则 
	 * 判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本        
	 * @param str 
	 * @return 
	 * @author zhengchunlei
	 */ 
	public static String getJSONType(String str){ 

		if(StringUtil.isEmpty(str)){
			return "";
		}
		final char[] strChar = str.substring(0, 1).toCharArray(); 
		final char firstChar = strChar[0]; 

		if(firstChar == '{'){ 
			return "object"; 
		}else if(firstChar == '['){ 
			return "array"; 
		}else{ 
			return ""; 
		} 
	}

	/*** 
	 *  null转换成空串    
	 * @param str 
	 * @return 
	 * @author lijin
	 */ 
	public static String null2Str(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}


	/*** 
	 *  null转换成0
	 * @param str
	 * @return 
	 * @author lijin
	 */ 
	public static int null2Int(Object obj){
		return (obj == null) ? 0 : (int)obj;
	}

	/*** 
	 *  null转换成0
	 * @param str
	 * @return 
	 * @author lijin
	 */ 
	public static double null2Double(Object obj){
		return (obj == null) ? 0 : (double)obj;
	}

	/*** 
	 *  获取当前时间
	 * @return 
	 * @author lijin
	 */ 
	public static int getCurrentCreateTime(){
		return (int)(System.currentTimeMillis()/1000);
	}


	/**
	 * 整型数字转汉字数字
	 * @param num 整型数字
	 * @return
	 */
	public static String intToHanNum(int num){
		String hanNum = "";
		switch(num){
		case 1:
			hanNum = "一";
			break;
		case 2:
			hanNum = "二";
			break;
		case 3:
			hanNum = "三";
			break;
		case 4:
			hanNum = "四";
			break;
		case 5:
			hanNum = "五";
			break;
		case 6:
			hanNum = "六";
			break;
		case 7:
			hanNum = "七";
			break;
		case 8:
			hanNum = "八";
			break;
		case 9:
			hanNum = "九";
			break;
		case 10:
			hanNum = "十";
			break;
		default:
			hanNum = "零";
			break;	
		}
		return hanNum;
	}

	/**
	 * 接口返回数据result 的可以在配置文件中是否存在
	 * @param apiConfig
	 * @param param
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  上午9:43:12
	 */
	public static boolean getCheckResultKey(JSONObject apiConfig, String param) {
		JSONObject resultObject = JSONObject.parseObject(apiConfig.get("result").toString());  
		if(null != param && !"".equals(param)){
			if(resultObject.size()>0){
				//如果接口文档返回值有多个，则返回多个参数 或者  返回明细，两个方法通用
				for(Entry<String,Object> entryResult : resultObject.entrySet()){
					if(entryResult.getValue().equals(param)){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**  
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1  
	 * @param String s 需要得到长度的字符串  
	 * @return int 得到的字符串长度  
	 * @author cxw
	 */   
	public static int length(String s) {  
		if (s == null)  
			return 0;  
		char[] c = s.toCharArray();  
		int len = 0;  
		for (int i = 0; i < c.length; i++) {  
			len++;  
			if (!isLetter(c[i])) {  
				len++;  
			}  
		}  
		return len;  
	}

	private static boolean isLetter(char c) {
		int k = 0x80;   
		return c / k == 0 ? true : false;
	}  

	
	
	
	
	/**
     * 文本输入，2-10个中英文字符、数字，不支持输入空格及特殊符号 
     *  
     * @param str 待匹配字符串 
     * @return true 匹配通过 false 匹配失败 
     */  
    public static boolean isValidName(String str)  
    {  
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次  
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次  
        boolean flag = false;  
        Pattern p = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]*");
        if (str != null)  
        {  
            Matcher match = p.matcher(str);  
            flag = match.matches();  
        }
        if(true==flag){
        	flag=str.length()>10||str.length()<2?false:true;
        }

        return flag;  
    }
    
  
    
    
    public static boolean isValidNameNull(String str)  
    {  
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次  
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次  
        boolean flag = false;  
        Pattern p = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]*");  
        if (str != null)  
        {  
            Matcher match = p.matcher(str);  
            flag = match.matches();  
        }else{
        	return true;
        }
        if(true==flag){
        	flag=str.length()>10||str.length()<2?false:true;
        }

        return flag;  
    }  
    
    /**
     * 名称，2-20个中英文字符、数字，不支持输入空格及特殊符号 
     * @param str
     * @return
     */
    public static boolean isValidNameNullTwo(String str)  
    {  
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次  
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次  
        boolean flag = false;  
        Pattern p = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]*");  
        if (str != null)  
        {  
            Matcher match = p.matcher(str);  
            flag = match.matches();  
        }else{
        	return true;
        }
        if(true==flag){
        	flag=str.length()>20||str.length()<2?false:true;
        }

        return flag;  
    }  
    
    
    public static boolean isValidDescNull(String str)  
    {  
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次  
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次  
        boolean flag = false;  
        Pattern p = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]*");  
        if (str != null)  
        {  
            Matcher match = p.matcher(str);  
            flag = match.matches();  
        }else{
        	return true;
        }
        if(true==flag){
        	flag=str.length()>30||str.length()<2?false:true;
        }

        return flag;  
    }  
    
    /**
     * 自定义判断长度
     * @param str 字符串
     * @param start 开始长度
     * @param end  结束长度
     * @return false:不在范围内      true:在范围内
     */
    public static boolean isValid(String str,int start,int end)  
    {  
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次  
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次  
        boolean flag = false;  
//        Pattern p = Pattern.compile("[a-zA-Z0-9\u4E00-\u9FA5]*");  
//        if (str != null)  
//        {  
//            Matcher match = p.matcher(str.trim());  
//            flag = match.matches();  
//        }else{
//        	return true;
//        }
//        if(true==flag){
        	flag=str.length()>end||str.length()<start?false:true;
//        }
        return flag;  
    }  
    
	/**
     * 校验长度
     *  
     * @param str 校验字符长度
     * @return true 
     */ 
    public static boolean isValidLength(String str,int len)  
    {  
        boolean flag = false;  
        if(null==str||str.length()<=len){
        	flag=true;
        }
        return flag;  
    }
    
    /**
     * 校验长度
     *  
     * @param str 校验字符长度
     * @return true 
     */ 
    public static boolean isLength(String str,int len)  
    {  
        boolean flag = false;  
        if(!"".equals(str)&&str.length()<=len){
        	flag=true;
        }
        return flag;  
    }
    /**
     * 校验身份证
     *  
     * @param str 校验字符长度
     * @return true 
     */ 
    public static boolean isValidLength(String idcard)  
    {  
    	IdcardValidator iv = new IdcardValidator();  
        return iv.isValidatedAllIdcard(idcard);  
    }

    /**
     * 获取图片地址
     * @param imgId 图片id
     * @return
     */
    
    /**
     * 获取图片地址
     * @param imgId 图片id
     * @param defaultImgType 默认
     * @return
     */
    public static  String getImgUrl(Integer imgId, Integer defaultImgType){
    	
    	String imgUrl = "";
    	String publicPictureRedis = "";
    	if(null != imgId && imgId != 0){
			//获取图片
			publicPictureRedis = RedisComponent.findRedisObject(imgId, PublicPicture.class);
			if("".equals(publicPictureRedis)){
	    		//获取默认图片
				publicPictureRedis = RedisComponent.findRedisObject(defaultImgType, PublicPicture.class);
	    	}
		}else{
			//获取默认图片
			publicPictureRedis = RedisComponent.findRedisObject(defaultImgType, PublicPicture.class);
		}
    	if(!StringUtil.isEmpty(publicPictureRedis)){
    		PublicPicture pp = JSONObject.parseObject(publicPictureRedis, PublicPicture.class);
    		if(null != pp && (!pp.equals(""))){
    			//imgUrl = pp.getDownloadUrl()+BusinessConstant.OSS_RATE;
    			/*
    			 * wangzx 2016-11-21
    			 */
    			imgUrl = pp.getDownloadUrl();
    		}
    	}
    	
    	return imgUrl;
    }
    
    /**
     * @param money
     */
    public static boolean isValidMoney(String money){
    	String  regex = "[1-9]{1}[0-9]*$";
    	return match(regex, money);
    }
    
    private static boolean match(String regex, String str) {
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(str);
    	return matcher.matches();
    	}
  //转化错误


  	
  	/**
  	 * 获取时间戳
  	 * @return
  	 */
  	public static int timePoke(){
  	//时间戳精确到秒
  			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  			String dates = sdf.format(new Date());
  		    Date parses = null;
  			try {
  				parses = sdf.parse(dates);
  			} catch (ParseException e) {
  			}
  		    int dateTime = (int)(parses.getTime()/1000);
			return dateTime;
  	}
}
