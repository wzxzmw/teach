package com.seentao.stpedu.common.entity;


public class TeachRelHomeworkAttachment {

	private Integer relId;
	
	private Integer homeworkId;
	
	private Integer attaId;
	
	//数据库查询参数传递
	private String homeworkIds;
	

	public String getHomeworkIds() {
		return homeworkIds;
	}

	public void setHomeworkIds(String homeworkIds) {
		this.homeworkIds = homeworkIds;
	}

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public Integer getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Integer homeworkId) {
		this.homeworkId = homeworkId;
	}
	public Integer getAttaId() {
		return attaId;
	}

	public void setAttaId(Integer attaId) {
		this.attaId = attaId;
	}

}
