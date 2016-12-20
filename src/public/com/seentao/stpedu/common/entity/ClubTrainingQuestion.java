package com.seentao.stpedu.common.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

public class ClubTrainingQuestion {

	private Integer questionId;
	
	private Integer classId;
	
	private Integer createTime;
	
	private Integer createUserId;
	
	private Integer praiseNum;
	
	private Integer isDelete;
	
	private Integer isEssence;
	
	private Integer answerNum;
	
	private String title;
	
	private String content;
	
	//问题发布者名称
	private String qCreaterName;
	//问题发布者昵称
	private String qCreaterNickname;
	//问题所属章节名称
	private String chapterName;
	//问题发布者头像链接地址
	private String qCreaterHeadLink;
	//回答
	private List<ClubTrainingQuestionAnswer>  answers = new ArrayList<ClubTrainingQuestionAnswer>();
	//问题发布者模块
	private Integer questionModule;
	//章节id
	private Integer chapterId;
	//内容需替换的超链接信息集合
	private JSONArray links;
	//内容需替换的图片信息集合
	private JSONArray imgs;
	
	
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

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public Integer getQuestionModule() {
		return questionModule;
	}

	public void setQuestionModule(Integer questionModule) {
		this.questionModule = questionModule;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getIsEssence() {
		return isEssence;
	}

	public void setIsEssence(Integer isEssence) {
		this.isEssence = isEssence;
	}
	public Integer getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(Integer answerNum) {
		this.answerNum = answerNum;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getqCreaterName() {
		return qCreaterName;
	}

	public void setqCreaterName(String qCreaterName) {
		this.qCreaterName = qCreaterName;
	}

	public String getqCreaterNickname() {
		return qCreaterNickname;
	}

	public void setqCreaterNickname(String qCreaterNickname) {
		this.qCreaterNickname = qCreaterNickname;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getqCreaterHeadLink() {
		return qCreaterHeadLink;
	}

	public void setqCreaterHeadLink(String qCreaterHeadLink) {
		this.qCreaterHeadLink = qCreaterHeadLink;
	}

	public List<ClubTrainingQuestionAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<ClubTrainingQuestionAnswer> answers) {
		this.answers = answers;
	}


}
