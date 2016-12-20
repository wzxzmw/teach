
package com.seentao.stpedu.common; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月27日 下午11:30:40
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class InsertObjectException extends Exception{

	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	public InsertObjectException(){
		
	}
	public InsertObjectException(String msg,Exception e){
		super(msg,e);
	}
	public InsertObjectException(Exception e){
		super(e);
	}
	public InsertObjectException(String msg){
		super(msg);
	}
}
