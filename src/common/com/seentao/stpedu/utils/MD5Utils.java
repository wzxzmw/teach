package com.seentao.stpedu.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
* @author yy
* @date 2016年6月26日 上午11:45:15 
*/
public class MD5Utils {
	/**
	 * 进行MD5加密
	 * @param info
	 *            要加密的信息
	 * @return String 加密后的字符串
	 */
	public static String encryptToMD5(String info) throws Exception {
		byte[] digesta = null;
		try {
			// 得到一个md5的消息摘要
			MessageDigest alga = MessageDigest.getInstance("MD5");
			// 添加要进行计算摘要的信息
			alga.update(info.getBytes());
			alga.update(info.getBytes("UTF-8"));
			// 得到该摘要
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		}
		// 将摘要转为字符串
		String rs = byteToHexString(digesta);
		return rs;
	}

	/** 
     * 将指定byte数组转换成16进制字符串 
     * @param b 
     * @return 
     */  
    public static String byteToHexString(byte[] b) {  
        StringBuffer hexString = new StringBuffer();  
        for (int i = 0; i < b.length; i++) {  
            String hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            hexString.append(hex.toUpperCase());  
        }  
        return hexString.toString();  
    } 
    
	/** 
     * MD5 加密  企业
     */  
    public static  String getMD5Str(String str) {  
    	str = str + "hr.seentao.com";
        MessageDigest messageDigest = null;  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
            messageDigest.reset();  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
        return md5StrBuff.toString();  
    }  
    public static void main(String[] args) throws Exception {
    	String s = getMD5Str("ent_id=c311dXh8fHN2dYB9gHN3gA&datestr=201610131905");
    	//System.out.println(s);
    	String s1 = encryptToMD5(s);
    	System.out.println("ent_id=c311dXh8fHN2dYB9gHN3gA&datestr=201610131905&sign="+s);
	}
    
}


