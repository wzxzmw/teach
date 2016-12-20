
package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.DeleteException;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;
import com.seentao.stpedu.common.sqlmap.BsRelVoteItemUserMapper;
import com.seentao.stpedu.utils.LogUtil;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午3:17:55
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
@Repository
public class BsRelVoteItemUserDao {

	@Autowired
	private BsRelVoteItemUserMapper bsRelVoteItemUserMapper;
	/*
	 * 删除BsRelVoteItemUser 全部数据
	 */
	public boolean deleteBsRelVoteItemUser()throws DeleteException{
		boolean result = false;
		try {
			int results = bsRelVoteItemUserMapper.deleteBsRelVoteItemUser();
			if(results>=1)
				result = true;
		} catch (DeleteException e) {
			LogUtil.error(BsRelVoteItemUserDao.class, "deleteBsRelVoteItemUser", "delete BsRelVoteItemUser is error");
			throw new DeleteException("delete BsRelVoteItemUser is error",e);
		}
		return result;
	}
	/*
	 * 添加评选项目用户关联表对象
	 */
	public void insertBsRelVoteItemUser(BsRelVoteItemUser bsRelVoteItemUser)throws InsertObjectException{
		try {
			bsRelVoteItemUserMapper.insertBsRelVoteItemUser(bsRelVoteItemUser);
		} catch (InsertObjectException e) {
			LogUtil.error(BsRelVoteItemUserDao.class, "insertBsRelVoteItemUser", "insert bsRelVoteItemUser is error");
			throw new InsertObjectException("insert bsRelVoteItemUser is error",e);
		}
	}
	/*
	 * 批量添加评选项目用户关联对象
	 */
	public void batchBsRelVoteItemUser(List<BsRelVoteItemUser> lists)throws InsertObjectException{
		try {
			bsRelVoteItemUserMapper.batchBsRelVoteItemUser(lists);
		} catch (InsertObjectException e) {
			LogUtil.error(this.getClass(), "batchBsRelVoteItemUser", "batch insert bsrelVoteItemUser is error");
			throw new InsertObjectException("batch insert bsrelVoteItemUser is error", e);
		}
	}
	/*
	 *根据评选人数排序查询评选voteIds 
	 */
	public List<Integer> queryBsVoteItemUserSomesVoteIds(){
		
		return bsRelVoteItemUserMapper.queryBsVoteItemUserSomesVoteIds();
	}
	/*
	 * 根据评选itemIds查询评选项
	 */
	public List<Integer> queryBsVoteItemUserSomesItemIds(Integer voteId){
		return bsRelVoteItemUserMapper.queryBsVoteItemUserSomesItemIds(voteId);
	}
	/*
	 * 根据投票选项查询投票选项次数
	 */
	public Integer queryBsVoteItemUserByItemId(Integer itemId,Integer userId,Integer voteId){
		return bsRelVoteItemUserMapper.queryBsVoteItemUserByItemId(itemId,userId,voteId);
	}
	/*
	 * 根据投票选项itemIds批量更新评选次数
	 */
	public void updateBsRelVoteItemUserByItemIds(List<BsRelVoteItemUser> lists)throws UpdateObjectException{
		try {
			bsRelVoteItemUserMapper.updateBsRelVoteItemUserByItemIds(lists);
		} catch (UpdateObjectException e) {
			LogUtil.error(BsRelVoteItemUserDao.class, "updateBsRelVoteItemUserByItemIds", "update BsRelVoteItem is error");
		     throw new 	UpdateObjectException("update BsRelVoteItem is error",e);
		}
	}
	/*
	 * 根据评选项itemIds查询所有的评选项关联
	 */
	public List<BsRelVoteItemUser> queryBsRelVoteItemUserItems(String itemIds,Integer voteId,Integer userId){
		return bsRelVoteItemUserMapper.queryBsRelVoteItemUserItems(itemIds,voteId,userId);
	}
	/*
	 * 根据voteId查询评选和评选项关联查询
	 */
	public List<BsRelVoteItemUser> queryBsRelVoteItemUserVoteId(Integer voteId){
		return bsRelVoteItemUserMapper.queryBsRelVoteItemUserVoteId(voteId);
	}
}
