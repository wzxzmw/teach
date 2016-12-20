package com.seentao.stpedu.common.entity;


public class ArenaGuessTopic {

	private Integer topicId;
	
	private Integer matchId;
	
	private String title;
	
	private String content;
	
	private Integer createTime;
	
	private Integer guessNum;
	
	private Integer status;
	
	private Integer compId;
	
	//比赛标题
	private String gameTitle;
	
	//竞猜话题参与人数
	private Integer joinNum;
	
	//最多投注者投注数量(虚拟物品的数量)
	private Double bettingCount;
	
	//竞猜对象
	private ArenaGuess guess;
	
	//投注最多者id
	private Integer topBettingUserId;
	
	//投注最多者昵称
	private String topBettingNickname;
	
	
	//通过guess 获取 最多投注者id 和 昵称
	public void findGuessParam(){
		if(guess != null){
			this.topBettingUserId = guess.getCreateUserId();
			this.topBettingNickname = guess.getCreaterNickname();
		}
	}
	
	
	public Integer getCompId() {
		return compId;
	}


	public void setCompId(Integer compId) {
		this.compId = compId;
	}


	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
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
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getGuessNum() {
		return guessNum;
	}

	public void setGuessNum(Integer guessNum) {
		this.guessNum = guessNum;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
	}

	public ArenaGuess getGuess() {
		return guess;
	}

	public void setGuess(ArenaGuess guess) {
		this.guess = guess;
	}

	public Integer getTopBettingUserId() {
		return topBettingUserId;
	}

	public void setTopBettingUserId(Integer topBettingUserId) {
		this.topBettingUserId = topBettingUserId;
	}

	public String getTopBettingNickname() {
		return topBettingNickname;
	}

	public void setTopBettingNickname(String topBettingNickname) {
		this.topBettingNickname = topBettingNickname;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public Double getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Double bettingCount) {
		this.bettingCount = bettingCount;
	}

}
