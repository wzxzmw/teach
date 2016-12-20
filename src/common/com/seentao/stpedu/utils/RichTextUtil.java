package com.seentao.stpedu.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RichTextUtil {
	
	/**
	 * 替换文中img/a标签
	 * @param content 需要解析的内容
	 * @return {"content":"替换过img标和a标签的内容","imgs":[{"ref":"<!--IMG#0-->","pixel":"1000*200","alt":"","src":"","photosetId":"0"}],"links":[{"ref":"<!--link#0-->","title":"","linkId":"0","type":"","href":"l"}]}
	 * @author cuijing
	 */
	public static JSONObject parseRichText(String content){
        JSONObject result = new JSONObject();
        JSONObject imgJson = parseImg(content);
        JSONObject linkJson = parseLink(imgJson.getString("content"));
    	result.put("content", linkJson.getString("content"));
    	result.put("imgs", imgJson.getJSONArray("imgs"));
    	result.put("links", linkJson.getJSONArray("links"));
    	return result;
    }	

	/**
	 * 替换文中img标签
	 * @param content
	 * @return {"content":"替换过img标签的内容","imgs":[{"ref":"<!--IMG#0-->","pixel":"1000*200","alt":"","src":"","photosetId":"0"}]}
	 * @author cuijing
	 */
	public static JSONObject parseImg(String content){
		//img
		String imtPattern = "<\\s*img\\s+([^>]*)\\s*>";
		Pattern objImgPattern = Pattern.compile(imtPattern);   
        Matcher imgMatcher = objImgPattern.matcher(content);
        //匹配src属性
        String srcPattern = "src=[\\'\"](\\S+)[\\'\"]";
        Pattern objSrcPattern = Pattern.compile(srcPattern);
        //匹配height和width属性
        String heightPattern = "height=[\\'\"](\\S+)[\\'\"]";
        Pattern objHeightPattern = Pattern.compile(heightPattern);
        String widthPattern = "width=[\\'\"](\\S+)[\\'\"]";
        Pattern objWidthPattern = Pattern.compile(widthPattern);
        //匹配alt属性
        String altPattern = "alt=[\\'\"](\\S+)[\\'\"]";
        Pattern objAltPattern = Pattern.compile(altPattern);

        JSONArray imgList = new JSONArray();
        
        Integer i = 0;
        String src = "";
        String height = "";
        String width = "";
        String alt = "";
        String pixel = "";
        
        while(imgMatcher.find()){
        	src = "";
        	height = "";
        	width = "";
        	alt = "";
        	pixel = "";
 		
        	//匹配并替换img标签
        	String imgTag = imgMatcher.group(0);
        	String imgReplacement = "<!--IMG#"+i+"-->";
        	content = imgMatcher.replaceFirst(imgReplacement);
        	//匹配src属性
        	Matcher srcMatcher = objSrcPattern.matcher(imgTag);
        	if(srcMatcher.find()) src = srcMatcher.group(1);
        	//匹配height和width属性
        	Matcher heightMatcher = objHeightPattern.matcher(imgTag);
        	if(heightMatcher.find()) height = heightMatcher.group(1);
        	Matcher widthMatcher = objWidthPattern.matcher(imgTag);
        	if(widthMatcher.find()) width = widthMatcher.group(1);
        	
        	//获取图片长度和宽度
        	if(!height.equals("") && !width.equals("")){
        		pixel = width+"*"+height;
        	}else{
//        		//TODO:将src改成绝对地址
        		BufferedInputStream bis = null;
                try {
                	/*
                	 * TODO 建议图片上传的时候获取图片长宽保存到数据库
                	 * 
                	 * 通过图片url访问图片获取到图片大小
                	 * lw
                	 */
                	URL url = new URL(src);  
                	HttpURLConnection  httpUrl = (HttpURLConnection) url.openConnection();  
                	httpUrl.connect();  
                	bis = new BufferedInputStream(httpUrl.getInputStream());  
                	BufferedImage sourceImg = ImageIO.read(bis);
                	width = String.valueOf(sourceImg.getWidth());
                	height = String.valueOf(sourceImg.getHeight());
					pixel = width + "*" + height;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if(bis != null){
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
        	}
        	//匹配alt属性
        	Matcher altMatcher = objAltPattern.matcher(imgTag);
        	if(altMatcher.find()) alt = altMatcher.group(1);

        	JSONObject imgJson = new JSONObject();
        	imgJson.put("ref", imgReplacement);
        	imgJson.put("pixel", pixel);
        	imgJson.put("alt", alt);
        	imgJson.put("src", src);
        	imgJson.put("photosetId", i.toString());
        	imgList.add(imgJson);
        	imgMatcher = objImgPattern.matcher(content);
        	i+=1;
        }
        JSONObject result = new JSONObject();
        result.put("content", content);
        result.put("imgs", imgList);
        return result;
	}
	
	
	
	/**
	 * 替换文中a标签
	 * @param content
	 * @return {"content":"替换过a标签的内容","links":[{"ref":"<!--link#0-->","title":"","linkId":"0","type":"","href":"l"}]}
	 * @author cuijing
	 */
	public static JSONObject parseLink(String content){
        //匹配link标签
        String linkPattern = "<a[^>]*>([^<]+)<\\/a>\\s*";
		Pattern objLinkPattern = Pattern.compile(linkPattern);
        Matcher linkMatcher = objLinkPattern.matcher(content);
        //匹配type属性
        String typePattern = "type=[\\'\"](\\S+)[\\'\"]";
        Pattern objTypePattern = Pattern.compile(typePattern);
        //匹配href属性
        String hrefPattern = "href=[\\'\"](\\S+)[\\'\"]";
        Pattern objHrefPattern = Pattern.compile(hrefPattern);
        JSONArray linkList = new JSONArray();
        Integer j = 0;
        while(linkMatcher.find()){
        	//匹配并替换link标签
        	String link = linkMatcher.group(0);
        	String linkId = j.toString();
        	String ref = "<!--link#"+j+"-->";
        	content = linkMatcher.replaceFirst(ref);
        	//title
        	String title = linkMatcher.group(1);
        	//匹配type
        	String type = "";
        	Matcher typeMatcher = objTypePattern.matcher(link);
        	if(typeMatcher.find()) type = typeMatcher.group(1);
        	//匹配href
        	String href = "";
        	Matcher hrefMatcher = objHrefPattern.matcher(link);
        	if(hrefMatcher.find()) href = hrefMatcher.group(1);
        	JSONObject linkJson = new JSONObject();
        	linkJson.put("linkId", linkId);
        	linkJson.put("ref", ref);
        	linkJson.put("title", title);
        	linkJson.put("type", type);
        	linkJson.put("href", href);
        	linkList.add(linkJson);
        	linkMatcher = objLinkPattern.matcher(content);
        	j += 1;
        }
        JSONObject result = new JSONObject();
        result.put("content", content);
        result.put("links", linkList);
        return result;
	}

	/**
	 * 过滤掉html标签(包含<script> <style>)
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr){ 
		if(StringUtil.isEmpty(htmlStr)){
			return "";
		}
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 
        if(htmlStr.equals("")){
        	htmlStr = "[图片消息]";
        }
        htmlStr.replaceAll("nbsp", "");
        htmlStr.replaceAll("nbsp;", "");
        htmlStr.replaceAll("&nbsp", "");
        htmlStr.replaceAll("&nbsp;", "");
        return htmlStr.trim().replaceAll("&nbsp;", ""); //返回文本字符串 
    }
	
	public static void main(String[] args){
		String pic1 = "图片一<img height='65' src='/images/branding/googlelogo/2x/googlelogop.png' width='272' alt='Google' id='hplogo'>图片一，";
		String pic2 = "图片二<img height='92' src='/images/branding/googlelogo/color_272x92dp.png' width='272' alt='Google' id='hplogo'>图片二，";
		String link1 = "连接一<a href='wwww.baidu.com' type='text/html'>连接一</a>连接一，";
		String link2 = "链接二<a href='www.ceshi.com'>链接二</a>链接二";
		String content = pic1 + pic2 + link1 + link2;
		JSONObject json = parseRichText(content);
		System.out.println(json.toJSONString());
	}
}
