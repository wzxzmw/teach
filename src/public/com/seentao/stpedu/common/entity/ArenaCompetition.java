package com.seentao.stpedu.common.entity;

import com.alibaba.fastjson.JSONArray;

public class ArenaCompetition {

	private Integer compId;
	
	private String title;
	
	private Integer imgId;
	
	private Integer startTime;
	
	private Integer endTime;
	
	private Integer clubId;
	
	private String introduce;
	
	private Integer notStartNum;
	
	private Integer inProcessNum;
	
	private Integer totalSignNum;
	
	private Integer attenCompNum;
	
	private int hasBeenAttention;

	//赛事宣传图片链接地址
	private String gameEventLink;
	//赛事所属俱乐部logo链接地址
	private String clubLogoLink;
	//赛事所属俱乐部名称
	private String clubName;
	//内容需替换的超链接信息集合
	private JSONArray links;
	//内容需替换的图片信息集合
	private JSONArray imgs;
	

	public int getHasBeenAttention() {
		return hasBeenAttention;
	}

	public void setHasBeenAttention(int hasBeenAttention) {
		this.hasBeenAttention = hasBeenAttention;
	}

	public Integer getAttenCompNum() {
		return attenCompNum;
	}

	public void setAttenCompNum(Integer attenCompNum) {
		this.attenCompNum = attenCompNum;
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

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getImgId() {
		return imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Integer getNotStartNum() {
		return notStartNum;
	}

	public void setNotStartNum(Integer notStartNum) {
		this.notStartNum = notStartNum;
	}
	public Integer getInProcessNum() {
		return inProcessNum;
	}

	public void setInProcessNum(Integer inProcessNum) {
		this.inProcessNum = inProcessNum;
	}
	public Integer getTotalSignNum() {
		return totalSignNum;
	}

	public void setTotalSignNum(Integer totalSignNum) {
		this.totalSignNum = totalSignNum;
	}

	public String getGameEventLink() {
		return gameEventLink;
	}

	public void setGameEventLink(String gameEventLink) {
		this.gameEventLink = gameEventLink;
	}

	public String getClubLogoLink() {
		return clubLogoLink;
	}

	public void setClubLogoLink(String clubLogoLink) {
		this.clubLogoLink = clubLogoLink;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

}
