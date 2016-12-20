
package com.seentao.stpedu.common.entity; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月11日 上午2:42:11
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class VoteNumber {
		private Integer voteId;
		
		private Integer count;
		
		public VoteNumber(){
			
		}
	
		
		public VoteNumber(Integer voteId, Integer count) {
			super();
			this.voteId = voteId;
			this.count = count;
		}


		public Integer getVoteId() {
			return voteId;
		}
	
		public void setVoteId(Integer voteId) {
			this.voteId = voteId;
		}
	
		public Integer getCount() {
			return count;
		}
	
		public void setCount(Integer count) {
			this.count = count;
		}
}
