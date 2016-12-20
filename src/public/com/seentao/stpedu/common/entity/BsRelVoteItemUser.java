
package com.seentao.stpedu.common.entity; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午2:58:22
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class BsRelVoteItemUser {

	/*
	 * 关联id 
	 */
	private Integer relId;
	/*
	 * 评选项id
	 */
	private Integer itemId;
	/*
	 * 用户id
	 */
	private Integer userId;
	/*
	 * 选项id
	 */
	private Integer voteId;
	/*
	 * 评选时间
	 */
	private Integer voteTime;
	/*
	 * 评选次数
	 */
	private Integer voteTimes;
	/*
	 * 组装生成hashKey
	 */
	public String hashKey;
	
	public BsRelVoteItemUser(){
		
	}
	
	public BsRelVoteItemUser(String hashKey,Integer itemId, Integer userId, Integer voteId, Integer voteTime, Integer voteTimes) {
		super();
		this.hashKey =hashKey; 
		this.itemId = itemId;
		this.userId = userId;
		this.voteId = voteId;
		this.voteTime = voteTime;
		this.voteTimes = voteTimes;
	}
	
	public String getHashKey(){
		return hashKey;
	}
	public void setHashKey(String hashKey){
		this.hashKey = hashKey;
	}
	public Integer getRelId() {
		return relId;
	}
	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	public Integer getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(Integer voteTime) {
		this.voteTime = voteTime;
	}
	public Integer getVoteTimes() {
		return voteTimes;
	}
	public void setVoteTimes(Integer voteTimes) {
		this.voteTimes = voteTimes;
	}
	
}
