
package com.seentao.stpedu.common; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月6日 上午7:32:33
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class DeleteException extends Exception {

	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	public DeleteException(){
			
		}
		public DeleteException(String msg,Exception e){
			super(msg,e);
		}
		public DeleteException(Exception e){
			super(e);
		}
		public DeleteException(String msg){
			super(msg);
		}
}
