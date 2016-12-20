
package com.seentao.stpedu.common; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年8月10日 下午3:24:22
	Email :wzxzmw@163.com
* 	类描述功能说明 :自定义更新异常信息
*/
public class CustomizeException extends Exception {

	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	
	public CustomizeException(){
		
	}
	public CustomizeException(String msg,Exception e){
		super(msg,e);
	}
	public CustomizeException(Exception e){
		super(e);
	}
	public CustomizeException(String msg){
		super(msg);
	}
}
