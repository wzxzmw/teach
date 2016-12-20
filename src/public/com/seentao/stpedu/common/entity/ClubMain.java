package com.seentao.stpedu.common.entity;

/**
 * 俱乐部
 * @author ligs
 */
public class ClubMain {

	private Integer clubId;

	private String clubName;

	private Integer logoId;

	private String clubExplain;

	private Integer memberNum;

	private Integer status;

	private Integer createTime;

	private Integer createUserId;

	private Integer gameBannerId;

	private Integer teachBannerId;

	private Integer styleBannerId;

	private Integer bgColorId;

	private Integer addMemberType;

	private Double addAmount;

	private Integer isNotAudited;

	private Integer isBuyClubVip;

	private Integer isBuyCompetition;

	private int clubRole; //俱乐部模块角色1:会长；2:教练；3:会员；4:普通用户(未加入俱乐部)；

	private String clubLogoLink;//俱乐部logo链接地址

	private String filePath;//链接地址

	private Integer hasBeenAttention; //是否已关注1:是；0:否；默认为0

	private String nickName;

	private String realName;

	private Integer clubType;

	private Integer schoolId;
	
	private Integer regionId;

	public ClubMain(){
		super();
	}
	public ClubMain(Integer clubId){
		this.clubId = clubId;
	}
	
	public ClubMain(Integer clubId, String clubName, Integer logoId, String clubExplain, Integer memberNum,
			Integer status, Integer createTime, Integer createUserId, Integer gameBannerId, Integer teachBannerId,
			Integer styleBannerId, Integer bgColorId, Integer addMemberType, Double addAmount, Integer isNotAudited,
			Integer isBuyClubVip, Integer isBuyCompetition, int clubRole, String clubLogoLink, String filePath,
			Integer hasBeenAttention, String nickName, String realName, Integer clubType, Integer schoolId,
			Integer regionId) {
		this.clubId = clubId;
		this.clubName = clubName;
		this.logoId = logoId;
		this.clubExplain = clubExplain;
		this.memberNum = memberNum;
		this.status = status;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.gameBannerId = gameBannerId;
		this.teachBannerId = teachBannerId;
		this.styleBannerId = styleBannerId;
		this.bgColorId = bgColorId;
		this.addMemberType = addMemberType;
		this.addAmount = addAmount;
		this.isNotAudited = isNotAudited;
		this.isBuyClubVip = isBuyClubVip;
		this.isBuyCompetition = isBuyCompetition;
		this.clubRole = clubRole;
		this.clubLogoLink = clubLogoLink;
		this.filePath = filePath;
		this.hasBeenAttention = hasBeenAttention;
		this.nickName = nickName;
		this.realName = realName;
		this.clubType = clubType;
		this.schoolId = schoolId;
		this.regionId = regionId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public Integer getLogoId() {
		return logoId;
	}

	public void setLogoId(Integer logoId) {
		this.logoId = logoId;
	}
	public String getClubExplain() {
		return clubExplain;
	}

	public void setClubExplain(String clubExplain) {
		this.clubExplain = clubExplain;
	}
	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public int getClubRole() {
		return clubRole;
	}

	public void setClubRole(int clubRole) {
		this.clubRole = clubRole;
	}

	public String getClubLogoLink() {
		return clubLogoLink;
	}

	public void setClubLogoLink(String clubLogoLink) {
		this.clubLogoLink = clubLogoLink;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getGameBannerId() {
		return gameBannerId;
	}

	public void setGameBannerId(Integer gameBannerId) {
		this.gameBannerId = gameBannerId;
	}

	public Integer getTeachBannerId() {
		return teachBannerId;
	}

	public void setTeachBannerId(Integer teachBannerId) {
		this.teachBannerId = teachBannerId;
	}

	public Integer getStyleBannerId() {
		return styleBannerId;
	}

	public void setStyleBannerId(Integer styleBannerId) {
		this.styleBannerId = styleBannerId;
	}

	public Integer getBgColorId() {
		return bgColorId;
	}

	public void setBgColorId(Integer bgColorId) {
		this.bgColorId = bgColorId;
	}

	public Integer getAddMemberType() {
		return addMemberType;
	}

	public void setAddMemberType(Integer addMemberType) {
		this.addMemberType = addMemberType;
	}

	public Double getAddAmount() {
		return addAmount;
	}

	public void setAddAmount(Double addAmount) {
		this.addAmount = addAmount;
	}

	public Integer getIsNotAudited() {
		return isNotAudited;
	}

	public void setIsNotAudited(Integer isNotAudited) {
		this.isNotAudited = isNotAudited;
	}

	public Integer getIsBuyClubVip() {
		return isBuyClubVip;
	}

	public void setIsBuyClubVip(Integer isBuyClubVip) {
		this.isBuyClubVip = isBuyClubVip;
	}

	public Integer getIsBuyCompetition() {
		return isBuyCompetition;
	}

	public void setIsBuyCompetition(Integer isBuyCompetition) {
		this.isBuyCompetition = isBuyCompetition;
	}

	public Integer getHasBeenAttention() {
		return hasBeenAttention;
	}

	public void setHasBeenAttention(Integer hasBeenAttention) {
		this.hasBeenAttention = hasBeenAttention;
	}

	public Integer getClubType() {
		return clubType;
	}

	public void setClubType(Integer clubType) {
		this.clubType = clubType;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	
}
