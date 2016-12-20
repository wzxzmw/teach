package com.seentao.stpedu.common.entity;

import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;

public class ArenaGuess {

	private Integer guessId;
	
	private String guessTitle;
	
	private Integer endTime;
	
	private Integer guessType;
	
	private Double sureAmount;
	
	private Double negativeAmount;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer joinUserNum;
	
	private Integer maxUserId;
	
	private Double maxAmount;
	
	private Integer bankerPosition;
	
	private Double bankerAmount;
	
	private Double odds;
	
	private Integer status;
	
	private Integer result;
	
	private Integer resultTime;
	
	private Integer topicId;
	
	//竞猜开始时间
	private Integer quizStartDate;
	//发布者昵称
	private String createrNickname;
	//发布者头像链接地址
	private String createrHeadLink;
	//当前登录者是否已投注
	private Integer hasBetting;
	//当前登录者投注哪方
	private Integer bettingObject;
	//当前登录者投注数量(虚拟物品的数量)
	private Double bettingCount;
	//非庄家方最大可投注数量(虚拟物品的数量)
	private Double otherCanBettingCount;
	//投注最多者昵称
	private String topBettingNickname;
	//是否是庄家
	private Integer isTheBanker;
	//当前登录者id
	private Integer userId;
	//竞猜话题id
	private Integer quizTopicId;
	//竞猜话题所属比赛id
	private Integer gameId;
	//竞猜话题所属比赛标题
	private String gameTitle;
	//竞猜话题的标题
	private String quizTopicTitle;
	//(YY修改)
	private String quizId;//竞猜id
	private String userIdString;//登录者id
	private String topBettingUserId;//投注者最多的人
	private String createrId;//发布者id
	
	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public String getUserIdString() {
		return userIdString;
	}

	public void setUserIdString(String userIdString) {
		this.userIdString = userIdString;
	}

	public String getTopBettingUserId() {
		return topBettingUserId;
	}

	public void setTopBettingUserId(String topBettingUserId) {
		this.topBettingUserId = topBettingUserId;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public Integer getQuizTopicId() {
		return quizTopicId;
	}

	public void setQuizTopicId(Integer quizTopicId) {
		this.quizTopicId = quizTopicId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getQuizTopicTitle() {
		return quizTopicTitle;
	}

	public void setQuizTopicTitle(String quizTopicTitle) {
		this.quizTopicTitle = quizTopicTitle;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTopBettingNickname() {
		return topBettingNickname;
	}

	public void setTopBettingNickname(String topBettingNickname) {
		this.topBettingNickname = topBettingNickname;
	}

	public Integer getGuessId() {
		return guessId;
	}

	public void setGuessId(Integer guessId) {
		this.guessId = guessId;
	}
	public String getGuessTitle() {
		return guessTitle;
	}

	public void setGuessTitle(String guessTitle) {
		this.guessTitle = guessTitle;
	}
	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Integer getGuessType() {
		return guessType;
	}

	public void setGuessType(Integer guessType) {
		this.guessType = guessType;
	}
	public Double getSureAmount() {
		return sureAmount;
	}

	public void setSureAmount(Double sureAmount) {
		this.sureAmount = sureAmount;
	}
	public Double getNegativeAmount() {
		return negativeAmount;
	}

	public void setNegativeAmount(Double negativeAmount) {
		this.negativeAmount = negativeAmount;
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
	public Integer getJoinUserNum() {
		return joinUserNum;
	}

	public void setJoinUserNum(Integer joinUserNum) {
		this.joinUserNum = joinUserNum;
	}
	public Integer getMaxUserId() {
		return maxUserId;
	}

	public void setMaxUserId(Integer maxUserId) {
		this.maxUserId = maxUserId;
	}
	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Integer getBankerPosition() {
		return bankerPosition;
	}

	public void setBankerPosition(Integer bankerPosition) {
		this.bankerPosition = bankerPosition;
	}
	public Double getBankerAmount() {
		return bankerAmount;
	}

	public void setBankerAmount(Double bankerAmount) {
		this.bankerAmount = bankerAmount;
	}
	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getResultTime() {
		return resultTime;
	}

	public void setResultTime(Integer resultTime) {
		this.resultTime = resultTime;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getQuizStartDate() {
		return quizStartDate;
	}

	public void setQuizStartDate(Integer quizStartDate) {
		this.quizStartDate = quizStartDate;
	}

	public String getCreaterNickname() {
		return createrNickname;
	}

	public void setCreaterNickname(String createrNickname) {
		this.createrNickname = createrNickname;
	}

	public String getCreaterHeadLink() {
		return createrHeadLink;
	}

	public void setCreaterHeadLink(String createrHeadLink) {
		this.createrHeadLink = createrHeadLink;
	}

	public Integer getHasBetting() {
		return hasBetting;
	}

	public void setHasBetting(Integer hasBetting) {
		this.hasBetting = hasBetting;
	}

	public Integer getBettingObject() {
		return bettingObject;
	}

	public void setBettingObject(Integer bettingObject) {
		this.bettingObject = bettingObject;
	}

	public Double getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Double bettingCount) {
		this.bettingCount = bettingCount;
	}

	public Double getOtherCanBettingCount() {
		
		return otherCanBettingCount;
	}

	public void setOtherCanBettingCount(Double otherCanBettingCount) {
		this.otherCanBettingCount = otherCanBettingCount;
	}
	
	public Integer getIsTheBanker() {
		return isTheBanker;
	}

	public void setIsTheBanker(Integer isTheBanker) {
		this.isTheBanker = isTheBanker;
	}

	/**
	 * 加入下注额
	 * @param bet_position		下注方向
	 * @param amount			下注额
	 * @author 					lw
	 * @date					2016年6月28日  下午5:29:59
	 */
	public String addAmount(int bet_position, double amount){
		
		/*
		 * 1.校验坐庄错误数据
		 * 方向不能为空
		 */
		if(GameConstants.GUESS_CAN == bet_position ||  GameConstants.GUESS_CANNOT == bet_position ){
			
			/*
			 * 2.坐庄竞猜判断
			 * 	a.投递方向	不能跟随庄家投递 
			 * 	b.金额校验  金额 = 最大可投金额 - 已投金额
			 */
			if(this.bankerPosition != null &&  this.guessType == GameConstants.GUESS_LANDLORD){
				
				if(this.bankerPosition == bet_position){
					return AppErrorCode.ERROR_DO_GUESS_GUEST_DIRECTION;
					
				}else if(amount > (bankerAmount/odds) - getAmount(bet_position)){
					return AppErrorCode.ERROR_DO_GUESS_OUT_MAX_ACCOUNT;
				}
				
			}
			
			//	3. 添加金钱
			if(GameConstants.GUESS_CAN == bet_position){
				this.setSureAmount(getSureAmount() + amount);
			}else if(GameConstants.GUESS_CANNOT == bet_position){
				this.setNegativeAmount(getNegativeAmount() + amount);
			}
			
			return null;
			
		}else{
			return AppErrorCode.ERROR_DO_GUESS_DIRECTION;
			
		}
	}

	
	
	/**
	 * 根据方向获取金钱 正反方总金额
	 * @param bet_position
	 * @return
	 * @author 			lw
	 * @date			2016年6月28日  下午5:46:02
	 */
	public Double getAmount(int bet_position) {
		if(GameConstants.GUESS_CAN == bet_position){
			return getSureAmount();
		}else if(GameConstants.GUESS_CANNOT == bet_position){
			return getNegativeAmount();
		}
		return 0d;
	}

	/**
	 * 初始化 正反方向金钱
	 * 
	 * @author 			lw
	 * @date			2016年6月28日  下午8:03:59
	 */
	public void initAmount() {
		if(getSureAmount() == null){
			this.setSureAmount(0d);
		}
		if(getNegativeAmount() == null){
			this.setNegativeAmount(0d);
		}
		if(getMaxAmount() == null){
			this.setMaxAmount(0d);
		}
		
	}

	
	/**
	 * 线程对象赋值
	 * 本来是来做线程参数传递的对象深度复制。
	 * 后来使用容器解决了这个问题用不到该方法。
	 * 该方法目前没有任何地方调用
	 * lw
	 * 2016-7-13
	 */
	@Deprecated
	public ArenaGuess clone(){
		ArenaGuess guess = new ArenaGuess();
		guess.setBankerAmount(this.bankerAmount);
		guess.setBankerPosition(this.bankerPosition);
		guess.setBettingCount(this.bettingCount);
		guess.setBettingObject(this.bettingObject);
		guess.setCreaterHeadLink(this.createrHeadLink);
		guess.setCreaterNickname(this.createrNickname);
		guess.setCreateTime(this.createTime);
		guess.setCreateUserId(this.createUserId);
		guess.setEndTime(this.endTime);
		guess.setGuessId(this.guessId);
		guess.setGuessTitle(this.guessTitle);
		guess.setGuessType(this.guessType);
		guess.setHasBetting(this.hasBetting);
		guess.setIsTheBanker(this.isTheBanker);
		guess.setJoinUserNum(this.joinUserNum);
		guess.setMaxAmount(this.maxAmount);
		guess.setMaxUserId(this.maxUserId);
		guess.setNegativeAmount(this.negativeAmount);
		guess.setOdds(this.odds);
		guess.setOtherCanBettingCount(this.otherCanBettingCount);
		guess.setQuizStartDate(this.quizStartDate);
		guess.setResult(this.result);
		guess.setResultTime(this.resultTime);
		guess.setStatus(this.status);
		guess.setSureAmount(this.sureAmount);
		guess.setTopBettingNickname(this.topBettingNickname);
		guess.setTopicId(this.topicId);
		guess.setUserId(this.userId);
		return guess;
	}
	
	
	

}
