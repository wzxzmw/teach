
package com.seentao.stpedu.votes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.votes.service.GetVotesForMobileService;
import com.seentao.stpedu.votes.service.GetVotesService;
import com.seentao.stpedu.votes.service.IGetVoteOptionService;
import com.seentao.stpedu.votes.service.SubmitVoteOptionService;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月31日 下午4:32:41
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Controller
public class GetVotesController implements IGetVotesController {

	@Autowired
	private GetVotesService getVotesService;
	@Autowired 
	private IGetVoteOptionService getVoteOptionsService;
	@Autowired
	private GetVotesForMobileService getVotesForMobileService;
	@Autowired
	private SubmitVoteOptionService submitVoteOptionService;

	/**获取投票列表信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return 
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="/getVotes")
	public String getVotes(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType) {
		
		return getVotesService.getVotesSomes(userId, voteId, start, limit, inquireType);
	}
	/**获取投票选项信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return 
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="/getVoteOptions")
	public String getVoteOptions(Integer userId, Integer voteId, Integer start, Integer limit, Integer inquireType) {
		
		return getVoteOptionsService.getVoteOptions(userId, voteId, start, limit, inquireType);
	}
	/**获取投票列表信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param start 起始数
	 * @param limit 每页数量
	 * @param inquireType 查询类型
	 * @return getVotesForMobile
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="/getVotesForMobile")
	public String getVotesForMobile(Integer userId, Integer start, Integer limit, Integer inquireType,String userName,Integer userType,String userToken) {
		return getVotesForMobileService.getVotesSomes(userId, start, limit, inquireType,userName,userType,userToken);
	}
	/**提交投票选项信息
	 * @param userId 用户userId
	 * @param voteId 投票id
	 * @param voteOptionIds 投票选项id集合
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="/submitVoteOption")
	public String submitVoteOption(Integer userId, String voteId, String voteOptionIds) {
		return submitVoteOptionService.SubmitVoteRedisBySomeIds(userId, voteId, voteOptionIds);
	}

}
