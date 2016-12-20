
package com.seentao.stpedu.common.entity; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午12:33:37
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class BsVoteItem {
	
	/*
	 * 评选项ID
	 */
	private Integer itemId;
	/*
	 * 评选id
	 */
	private Integer voteId;
	/*
	 * 评选项主题
	 */
	private String itemName;
	/*
	 * 评选项描述
	 */
	private String itemDesc;
	/*
	 * 评选项主图id
	 */
	private Integer imgId;
	
	
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	
}

