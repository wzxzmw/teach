package com.seentao.stpedu.common.entity;

import java.util.ArrayList;
import java.util.List;

public class CenterPhoto {

	private Integer photoId;
	
	private Integer albumId;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer imageId;
	
	private Integer praiseNum;
	
	private Integer isDelete;
	
	private String filePath;
	
	private Integer createPhotoTime;

	private String albumName;
	
	private Integer lastUpdateTime;
	
	private Integer coverPhotoId;
	
	private Integer photoNum;
	
	private String albumExplain;
	

	public String getAlbumExplain() {
		return albumExplain;
	}

	public void setAlbumExplain(String albumExplain) {
		this.albumExplain = albumExplain;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public Integer getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getCoverPhotoId() {
		return coverPhotoId;
	}

	public void setCoverPhotoId(Integer coverPhotoId) {
		this.coverPhotoId = coverPhotoId;
	}

	public Integer getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(Integer photoNum) {
		this.photoNum = photoNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getCreatePhotoTime() {
		return createPhotoTime;
	}

	public void setCreatePhotoTime(Integer createPhotoTime) {
		this.createPhotoTime = createPhotoTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
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
	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

}
