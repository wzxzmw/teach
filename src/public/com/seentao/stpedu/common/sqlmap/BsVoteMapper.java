
package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.BsVote;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午2:04:47
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public interface BsVoteMapper {
    /*
     * 添加评选表对象
     */
	public abstract void insertBsVote(BsVote bsvote)throws InsertObjectException;
	/*
	 * 分页查询推荐评选
	 */
	public abstract List<BsVote> queryByPageBsVote(Map<String,Object> map);
	/*
	 * 查询推荐评选总数
	 */
	public abstract Integer queryCountByBsVote(Map<String,Object> map);
	/*
	 * 根据投票id获取唯一一条投票信息
	 */
	public abstract BsVote queryBsVoteByVoteId(@Param("voteId")Integer voteId);
	/*
	 * 修改评选数量
	 */
	public abstract int updateBsVoteByVoteIdVoteNum(@Param("voteId")Integer voteId,@Param("voteNum")Integer voteNum)throws UpdateObjectException;
	
	public abstract void batchUpdateVoteCount(List<BsVote> list);
	
}
