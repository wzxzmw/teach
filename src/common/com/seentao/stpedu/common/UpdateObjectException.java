
package com.seentao.stpedu.common; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月27日 下午11:31:47
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class UpdateObjectException extends Exception {

	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	public UpdateObjectException(){
		
	}
	public UpdateObjectException(String msg,Exception e){
		super(msg,e);
	}
	public UpdateObjectException(Exception e){
		super(e);
	}
	public UpdateObjectException(String msg){
		super(msg);
	}
}
