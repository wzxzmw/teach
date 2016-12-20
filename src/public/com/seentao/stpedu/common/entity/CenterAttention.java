package com.seentao.stpedu.common.entity;

/**
 * 关注表
 * @author ligs
 */
public class CenterAttention {
	private Integer start;
	
	private Integer limit;

	private Integer atteId;
	
	private Integer createUserId;
	
	private Integer createTime;//创建时间
	
	private Integer relObjetType;
	
	private String relObjetId;

	
	//数据库查询字段
	private String relObjetIdIds;
	
	public CenterAttention(Integer createUserId,Integer relObjetType){
		this.createUserId = createUserId;
		this.relObjetType  = relObjetType ;
	}
	public CenterAttention(String relObjetId){
		this.relObjetId = relObjetId;
	}
	public CenterAttention(){
	}
	public CenterAttention(Integer createUserId,Integer relObjetType,String relObjetId){
		this.createUserId = createUserId;
		this.relObjetType  = relObjetType ;
		this.relObjetId = relObjetId;
	}
	public String getRelObjetIdIds() {
		return relObjetIdIds;
	}

	public void setRelObjetIdIds(String relObjetIdIds) {
		this.relObjetIdIds = relObjetIdIds;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getAtteId() {
		return atteId;
	}

	public void setAtteId(Integer atteId) {
		this.atteId = atteId;
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
	public Integer getRelObjetType() {
		return relObjetType;
	}

	public void setRelObjetType(Integer relObjetType) {
		this.relObjetType = relObjetType;
	}

	public String getRelObjetId() {
		return relObjetId;
	}

	public void setRelObjetId(String relObjetId) {
		this.relObjetId = relObjetId;
	}
	
	
}
