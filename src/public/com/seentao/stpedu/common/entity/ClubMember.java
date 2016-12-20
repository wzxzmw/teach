package com.seentao.stpedu.common.entity;


public class ClubMember {
	private String searchWord;//搜索词
	
	private Integer start;
	
	private Integer limit;

	private Integer memberId;
	
	private Integer clubId;
	
	private Integer userId;
	
	private Integer memberStatus;
	
	private Integer level;
	
	private Integer rpSendNum;
	
	private Integer rpReceiveNum;
	
	private Integer totalReceiveRp1;
	
	private Integer totalReceiveRp2;
	
	private String applyExplain;
	
	private Integer isNewRemind;
	
	private Integer isNewNotice;
	
	private Integer isNewRedPacket;
	
	
	public ClubMember(){
		
	}
	
	public ClubMember(Integer userId){
		this.userId = userId;
	}
	
	public ClubMember(Integer clubId ,Integer level,Integer memberStatus){
		this.clubId = clubId ;
		this.level = level;
		this.memberStatus = memberStatus;
	}
	
	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public Integer getRpSendNum() {
		return rpSendNum;
	}

	public void setRpSendNum(Integer rpSendNum) {
		this.rpSendNum = rpSendNum;
	}

	public Integer getRpReceiveNum() {
		return rpReceiveNum;
	}

	public void setRpReceiveNum(Integer rpReceiveNum) {
		this.rpReceiveNum = rpReceiveNum;
	}

	public Integer getTotalReceiveRp1() {
		return totalReceiveRp1;
	}

	public void setTotalReceiveRp1(Integer totalReceiveRp1) {
		this.totalReceiveRp1 = totalReceiveRp1;
	}

	public Integer getTotalReceiveRp2() {
		return totalReceiveRp2;
	}

	public void setTotalReceiveRp2(Integer totalReceiveRp2) {
		this.totalReceiveRp2 = totalReceiveRp2;
	}

	public String getApplyExplain() {
		return applyExplain;
	}

	public void setApplyExplain(String applyExplain) {
		this.applyExplain = applyExplain;
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

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Integer memberStatus) {
		this.memberStatus = memberStatus;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsNewRemind() {
		return isNewRemind;
	}

	public void setIsNewRemind(Integer isNewRemind) {
		this.isNewRemind = isNewRemind;
	}

	public Integer getIsNewNotice() {
		return isNewNotice;
	}

	public void setIsNewNotice(Integer isNewNotice) {
		this.isNewNotice = isNewNotice;
	}

	public Integer getIsNewRedPacket() {
		return isNewRedPacket;
	}

	public void setIsNewRedPacket(Integer isNewRedPacket) {
		this.isNewRedPacket = isNewRedPacket;
	}

	
}
