package com.seentao.stpedu.common.entity;


public class CenterAlbum {

	private Integer albumId;

	private Integer createUserId;

	private Integer createTime;

	private String albumName;

	private String albumExplain;

	private Integer photoNum;

	private Integer lastUpdateTime;

	private Integer isDelete;

	private Integer coverPhotoId;

	private String filePath;//链接地址

	private Integer photoId; //相册图片的id

	private Integer createPhotoTime;//相册图片创建时间的时间戳

	private Integer praiseNum; //相册图片的点赞数量


	public  CenterAlbum(){
		super();
	}

	public CenterAlbum(Integer albumId, Integer createUserId, Integer createTime, String albumName, String albumExplain,
			Integer photoNum, Integer lastUpdateTime, Integer isDelete, Integer coverPhotoId, String filePath,
			Integer photoId, Integer createPhotoTime, Integer praiseNum) {

		this.albumId = albumId;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.albumName = albumName;
		this.albumExplain = albumExplain;
		this.photoNum = photoNum;
		this.lastUpdateTime = lastUpdateTime;
		this.isDelete = isDelete;
		this.coverPhotoId = coverPhotoId;
		this.filePath = filePath;
		this.photoId = photoId;
		this.createPhotoTime = createPhotoTime;
		this.praiseNum = praiseNum;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
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
	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getAlbumExplain() {
		return albumExplain;
	}

	public void setAlbumExplain(String albumExplain) {
		this.albumExplain = albumExplain;
	}
	public Integer getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(Integer photoNum) {
		this.photoNum = photoNum;
	}
	public Integer getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getCoverPhotoId() {
		return coverPhotoId;
	}

	public void setCoverPhotoId(Integer coverPhotoId) {
		this.coverPhotoId = coverPhotoId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public Integer getCreatePhotoTime() {
		return createPhotoTime;
	}

	public void setCreatePhotoTime(Integer createPhotoTime) {
		this.createPhotoTime = createPhotoTime;
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

}
