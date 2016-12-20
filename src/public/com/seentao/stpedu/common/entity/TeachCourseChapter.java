package com.seentao.stpedu.common.entity;

import java.util.List;

public class TeachCourseChapter {

	private Integer chapterNo;
	
	private Integer chapterId;
	
	private String chapterName;
	
	private Integer classId;
	
	private Integer matchId;
	
	private Integer matchYear;
	
	private Integer isTheFirstYear;//比赛运行年是否为初始年

	public Integer getIsTheFirstYear() {
		return isTheFirstYear;
	}

	public void setIsTheFirstYear(Integer isTheFirstYear) {
		this.isTheFirstYear = isTheFirstYear;
	}
	
	private List<TeachCourseCard> courseCards;
	
	public List<TeachCourseCard> getCourseCards() {
		return courseCards;
	}

	public void setCourseCards(List<TeachCourseCard> courseCards) {
		this.courseCards = courseCards;
	}

	public Integer getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(Integer chapterNo) {
		this.chapterNo = chapterNo;
	}
	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}
	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public Integer getMatchYear() {
		return matchYear;
	}

	public void setMatchYear(Integer matchYear) {
		this.matchYear = matchYear;
	}
	
	

}
