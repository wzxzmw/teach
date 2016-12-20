
package com.seentao.stpedu.common.entity; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月20日 下午10:56:51
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class RedisUserPhone {
	/**用户注册电话号码*/
	private String phone;
	/**用户的唯一标识*/
	private String userToken;
	/**用户是否已经登录 0、没有登录  1、已经登录 2、退出登录*/
	private int is_login;
	/**用户是否已经注册 0、默认 1、已经注册 2、没有注册*/
	private int is_check;
	
	public RedisUserPhone(){
		
	}
	public RedisUserPhone(String phone, String userToken, int is_login, int is_check) {
		this.phone = phone;
		this.userToken = userToken;
		this.is_login = is_login;
		this.is_check = is_check;
	}
	
	public RedisUserPhone(String phone, int is_login, int is_check){
		this.phone = phone;
		this.is_login = is_login;
		this.is_check = is_check;
	}
	public int getIs_check(){
		return is_check;
	}
	public void setIs_check(int is_check){
		this.is_check= is_check;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public int getIs_login() {
		return is_login;
	}
	public void setIs_login(int is_login) {
		this.is_login = is_login;
	}
	
}
