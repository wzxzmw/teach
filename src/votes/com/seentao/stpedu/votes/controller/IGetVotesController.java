
package com.seentao.stpedu.votes.controller; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月31日 下午4:28:45
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public interface IGetVotesController {
	
	/**获取投票列表信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return getVoteOptions
	 */
	public String getVotes(Integer userId,Integer voteId ,Integer start ,Integer limit,Integer inquireType);
	/**获取投票选项信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return 
	 */
	public String getVoteOptions(Integer userId,Integer voteId ,Integer start ,Integer limit,Integer inquireType);
	/**获取投票列表信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return getVotesForMobile
	 * 
	 */
	public String getVotesForMobile(Integer userId,Integer start ,Integer limit,Integer inquireType,String userName,Integer userType,String userToken);
	
	/**提交投票选项信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param voteOptionIds 投票选项id集合
	 * @return
	 */
	public String submitVoteOption(Integer userId,String voteId,String voteOptionIds );
}
