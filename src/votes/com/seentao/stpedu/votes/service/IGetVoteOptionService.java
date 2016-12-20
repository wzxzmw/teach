
package com.seentao.stpedu.votes.service;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月9日 下午7:54:17
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public interface IGetVoteOptionService {

	/**获取投票选项信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return 
	 */
	String getVoteOptions(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType);

}