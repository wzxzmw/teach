package com.seentao.stpedu.common.entity;


public class PublicAttachment {

	private Integer attaId;
	
	private Integer attaType;
	
	private String fileName;
	
	private String filePath;
	
	private String downloadUrl;
	
	private String suffixName;
	
	private Double size;
	
	private Integer timeLength;
	
	private Integer createTime;
	
	private Integer createUserId;
	
	
	//附件一级分类
	private Integer attachmentMajorType;

	public PublicAttachment(){
		super();
	}
	
	public PublicAttachment( int attaType,String fileName, String downloadUrl,String suffixName,int timeLength,int createTime, int createUserId){
		this.attaType=attaType;
		this.fileName=fileName;
		this.downloadUrl=downloadUrl;
		this.filePath=downloadUrl;
		this.suffixName=suffixName;
		this.timeLength=timeLength;
		this.createTime=createTime;
		this.createUserId=createUserId;
	}
	
	
	
	public Integer getAttachmentMajorType() {
		return attachmentMajorType;
	}

	public void setAttachmentMajorType(Integer attachmentMajorType) {
		this.attachmentMajorType = attachmentMajorType;
	}

	public Integer getAttaId() {
		return attaId;
	}

	public void setAttaId(Integer attaId) {
		this.attaId = attaId;
	}
	public Integer getAttaType() {
		return attaType;
	}

	public void setAttaType(Integer attaType) {
		this.attaType = attaType;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}
	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
	public Integer getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(Integer timeLength) {
		this.timeLength = timeLength;
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

}
