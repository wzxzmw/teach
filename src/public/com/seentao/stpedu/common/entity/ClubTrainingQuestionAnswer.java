package com.seentao.stpedu.common.entity;

import com.alibaba.fastjson.JSONArray;

public class ClubTrainingQuestionAnswer {

	private Integer answerId;
	
	private Integer questionId;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer praiseNum;
	
	private String content;
	
	private Integer isDelete;

	private String aCreaterName;
	
	private String aCreaterHeadLink;
	
	private String aCreaterNickname;//yy修改

	//内容需替换的超链接信息集合
	private JSONArray links;
	//内容需替换的图片信息集合
	private JSONArray imgs;
	
	
	//数据库查询字段
	private String questionIds;
	
	//过滤了html标签的问题回复内容(yy修改)
	private String answerBodyFilterHtml;
	
	public String getaCreaterNickname() {
		return aCreaterNickname;
	}

	public void setaCreaterNickname(String aCreaterNickname) {
		this.aCreaterNickname = aCreaterNickname;
	}

	public String getAnswerBodyFilterHtml() {
		return answerBodyFilterHtml;
	}

	public void setAnswerBodyFilterHtml(String answerBodyFilterHtml) {
		this.answerBodyFilterHtml = answerBodyFilterHtml;
	}
	
	public String getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(String questionIds) {
		this.questionIds = questionIds;
	}

	public JSONArray getLinks() {
		return links;
	}

	public void setLinks(JSONArray links) {
		this.links = links;
	}

	public JSONArray getImgs() {
		return imgs;
	}

	public void setImgs(JSONArray imgs) {
		this.imgs = imgs;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}
	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
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
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getaCreaterName() {
		return aCreaterName;
	}

	public void setaCreaterName(String aCreaterName) {
		this.aCreaterName = aCreaterName;
	}

	public String getaCreaterHeadLink() {
		return aCreaterHeadLink;
	}

	public void setaCreaterHeadLink(String aCreaterHeadLink) {
		this.aCreaterHeadLink = aCreaterHeadLink;
	}

}
