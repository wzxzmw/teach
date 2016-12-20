package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.BsVoteItem;
import com.seentao.stpedu.common.sqlmap.BsVoteItemMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class BsVoteItemDao {

	@Autowired
	private BsVoteItemMapper bsVoteItemMapper;
	/*
	 * 添加评选项对象
	 */
	public  void insertBsVoteItem(BsVoteItem bsVoteItem)throws InsertObjectException{
		try {
			bsVoteItemMapper.insertBsVoteItem(bsVoteItem);	
		} catch (InsertObjectException e) {
			LogUtil.error(BsVoteItemDao.class, "insertBsVoteItem", "insert bsVoteItem is error");
			throw new InsertObjectException("insert bsVoteItem is error",e);
		}
	}
	/*
	 * 根据评选项itemIds查询评选项 limit,start 分页
	 */
	public List<BsVoteItem> queryBsVoteItemsByItemIds(Map<String, Object> map){
		return bsVoteItemMapper.queryBsVoteItemsByItemIds(map);
	}
	/*
	 * 求评选下的评先选的总条数
	 */
	public Integer queryBsVoteItemsByCount(Map<String, Object> map){
		return bsVoteItemMapper.queryBsVoteItemsByCount(map);
	}
}