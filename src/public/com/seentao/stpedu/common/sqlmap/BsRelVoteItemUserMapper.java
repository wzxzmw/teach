package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.seentao.stpedu.common.DeleteException;
import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.UpdateObjectException;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;

public interface BsRelVoteItemUserMapper {
	/*
	 * 删除BsRelVoteItemUser 全部数据
	 */
	public abstract int deleteBsRelVoteItemUser()throws DeleteException;
	/*
	 * 批量添加评选项目用户关联对象
	 */
	public void batchBsRelVoteItemUser(List<BsRelVoteItemUser> lists)throws InsertObjectException;
	/*
	 * 添加评选项用户管理
	 */
	public abstract void insertBsRelVoteItemUser(BsRelVoteItemUser bsRelVoteItemUser)throws InsertObjectException;
	/*
	 *根据评选人数排序查询评选voteIds 
	 */
	public abstract List<Integer> queryBsVoteItemUserSomesVoteIds();
	/*
	 * 根据评选itemIds查询评选项
	 */
	public abstract List<Integer> queryBsVoteItemUserSomesItemIds(@Param("voteId")Integer voteId);
	/*
	 * 根据投票选项查询投票选项次数
	 */
	public abstract Integer queryBsVoteItemUserByItemId(@Param("itemId")Integer itemId,@Param("userId") Integer userId,@Param("voteId")Integer voteId);
	/*
	 * 根据投票选项itemIds批量更新评选次数
	 */
	public abstract void updateBsRelVoteItemUserByItemIds(List<BsRelVoteItemUser> list)throws UpdateObjectException;
	/*
	 * 根据评选项itemIds查询所有的评选项关联
	 */
	public abstract List<BsRelVoteItemUser> queryBsRelVoteItemUserItems(@Param("itemIds")String itemIds,@Param("voteId") Integer voteId,@Param("userId")Integer userId);
	/*
	 * 当前登录者是否能对本次提交的这些投票选项继续进行投票（对提交的这些选项的投票机会还没有用完）
	 */
	public abstract List<BsRelVoteItemUser> queryBsVoteUserByUserIdAndItemId(@Param("userId")Integer userId,@Param("voteId")Integer voteId);
	/*
	 * 根据voteId查询评选和评选项关联查询
	 */
	public abstract List<BsRelVoteItemUser> queryBsRelVoteItemUserVoteId(@Param("voteId")Integer voteId);
}