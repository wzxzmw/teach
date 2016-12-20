
package com.seentao.stpedu.common.sqlmap;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.InsertObjectException;
import com.seentao.stpedu.common.entity.BsVoteItem;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午12:51:02
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public interface BsVoteItemMapper {
	/*
	 * 添加评选项对象
	 */
	public abstract void insertBsVoteItem(BsVoteItem bsVoteItem)throws InsertObjectException;
	/*
	 * 根据评选项itemIds查询评选项
	 */
	public abstract List<BsVoteItem> queryBsVoteItemsByItemIds(Map<String, Object> map);
	/*
	 * 求评选itemIds下的评先选的总条数
	 */
	public abstract Integer queryBsVoteItemsByCount(Map<String, Object> map);
}
