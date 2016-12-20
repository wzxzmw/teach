
package com.seentao.stpedu.common.interfaces; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月16日 下午3:50:41
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public interface IVoteInterfacesService {
	/*
	 * 运营后台管理调用投票
	 */
	
	String getVoteOptions(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType); 
	/*
	 * 平台动态调用getVotes
	 */
	String getVotes(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType);
	/*
	 * 定时器调用service
	 */
	
}
