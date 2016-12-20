
package com.seentao.stpedu.common.entity; 
/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月1日 下午12:37:06
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class BsVote {
	/*
	 * 评选id
	 */
	private Integer voteId;
	/*
	 * 评选主题
	 */
	private String voteTitle;
	/*
	 * 评选说明
	 */
	private String voteDesc;
	/*
	 * 创建者id
	 */
	private Integer createUserId;
	/*
	 * 创建时间
	 */
	private Integer createTime;
	/*
	 * 评选主图id
	 */
	private Integer imgId;
	/*
	 * 开始时间
	 */
	private Integer beginTime;
	/*
	 * 结束时间
	 */
	private Integer endTime;
	/*
	 * 评选限制类型 1.次 2.次/天
	 */
	private Integer voteLimitType;
	/*
	 * 评选限制次数
	 */
	private Integer voteLimitNum;
	/*
	 * 评论数量
	 */
	private Integer voteNum;
	/*
	 * 选择模式 0 ，单选，大于1 表示多选
	 */
	private Integer choosePattern;
	/*
	 * 是否上线 0、不上线，1、上线
	 */
	private Integer isDisable;
	/*
	 *是否推荐 0、不推荐 1、推荐 
	 */
	private Integer isRecommend;
	/*
	 * 是否删除
	 */
	private Integer isDelete;
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	public String getVoteTitle() {
		return voteTitle;
	}
	public void setVoteTitle(String voteTitle) {
		this.voteTitle = voteTitle;
	}
	public String getVoteDesc() {
		return voteDesc;
	}
	public void setVoteDesc(String voteDesc) {
		this.voteDesc = voteDesc;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public Integer getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Integer beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Integer getVoteLimitType() {
		return voteLimitType;
	}
	public void setVoteLimitType(Integer voteLimitType) {
		this.voteLimitType = voteLimitType;
	}
	public Integer getVoteLimitNum() {
		return voteLimitNum;
	}
	public void setVoteLimitNum(Integer voteLimitNum) {
		this.voteLimitNum = voteLimitNum;
	}
	public Integer getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}
	public Integer getChoosePattern() {
		return choosePattern;
	}
	public void setChoosePattern(Integer choosePattern) {
		this.choosePattern = choosePattern;
	}
	public Integer getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}
	public Integer getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
}
