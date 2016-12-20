package com.seentao.stpedu.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class StringUtil {

	/**
	 * 判断字符串是否为null或者内容为空
	 * @param msg
	 * @return
	 */
	public static boolean isEmpty(String msg){
		if(msg == null || msg.equals("") || msg.equals("null")){
			return true;
		}
		return false;
	}
	
	/**
	 * 带逗号字符串转换成list
	 * @param strArr
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  下午9:43:48
	 */
	public static List<String> strToList(String strArr){
		if(StringUtils.hasText(strArr)){
			
		}
		String[] split = strArr.split(",");
		List<String> list = new ArrayList<String>();
		for(String str : split){
			list.add(str);
		}
		return list;
	}
	/**
	 * 集合转换成带逗号的字符串 
	 * @param coll
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  下午9:44:14
	 */
	public static String ListToString(@SuppressWarnings("rawtypes") Collection coll,String link){
		if(CollectionUtils.isEmpty(coll)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(Object en : coll){
			sb.append(en.toString()).append(link);
		}
		String str = sb.toString();
		return str.substring(0, str.length()-1);
	}
	/**
	 * 集合转换成带逗号的字符串 
	 * @param coll
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  下午9:44:14
	 */
	public static String ListWithColonToString(@SuppressWarnings("rawtypes") Collection coll,String link){
		if(CollectionUtils.isEmpty(coll)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(Object en : coll){
			sb.append("'").append(en.toString()).append("'").append(link);
		}
		String str = sb.toString();
		return str.substring(0, str.length()-1);
	}
	
	
	/**
	 * 把集合转换成String[] 类型
	 * @param coll
	 * @return
	 * @author 			lw
	 * @date			2016年6月29日  下午5:32:41
	 */
	public static String[] ListIntegerToStringArr(@SuppressWarnings("rawtypes") Collection coll){
		String[] arr = new String[coll.size()];
		int i = 0 ;
		for(Object en : coll){
			arr[i] = en.toString();
			++i;
		}
		return arr;
	}
	
	
	/**
	 * 把数组转变成链接分隔的字符串
	 * @param arr
	 * @param link
	 * @return
	 * @author 			lw
	 * @date			2016年7月18日  下午12:01:34
	 */
	public static <T> String arrToString(T[] arr, String link){
		
		if(arr == null || arr.length <= 0 || link == null){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for(T en : arr){
			sb.append(en).append(link);
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
		
	}
	
	/**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
    
    /**
     * 校验字符串只包含数字英文和字符
     * @param value 指定的字符串
     * @return 是否是规则的字符串
     */
    public static boolean regexString(String value) {
    	String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match=pattern.matcher(value);
        return match.matches();
    }
    
   /**
    * 多方位校验字符串
    * @param value 校验的字符串
    * @param length 限制多少长度
    * @param sign 是否校验字符串只包含数字字符中文
    * 0数据为空
    * 1字符串规则不正确
    * 2数据长度不正确
    * 3成功
    * @return
    */
    public static int StringVerification(String value,Integer length,boolean sign) {
    	if(isEmpty(value)){//校验是否为空
    		return 0;
    	}else{
    		if(sign){//校验字符串只包含数字字符中文
    			//if(regexString(value)){
    				if(length(value)>length){//限制多少长度
        				return 2;
        			}else{
        				return 3;
        			}
    			//}else{
    				//return 1;
    			//}
    		}else{
    			if(length(value)>=length){//限制多少长度
    				return 2;
    			}else{
    				return 3;
    			}
    		}
    	}
    }
    
    public static int nullToInt(Object o){
    	if(null == o){
    		return 0;
    	}else{
    		return (int) o;
    	}
    }
    
    
    /**
	 * 把数组转变成链接分隔的字符串
	 * @param arr
	 * @param link
	 * @return
	 * @author 			lw
	 * @date			2016年7月18日  下午12:01:34
	 */
	public static <T> String StringArrToIntegerString(T[] arr, String link){
		
		if(arr == null || arr.length <= 0 || link == null){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for(T en : arr){
			sb.append(Integer.parseInt(en.toString())).append(link);
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
		
	}




	/**
	 * 带逗号字符串转换成Arr
	 * @param strArr
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  下午9:43:48
	 */
	public static String[] strToArr(String strArr){
		if(StringUtils.hasLength(strArr)){
			return  strArr.split(",");
		}
		return null;
	}
}
