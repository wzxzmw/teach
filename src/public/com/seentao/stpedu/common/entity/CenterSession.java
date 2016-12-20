package com.seentao.stpedu.common.entity;

public class CenterSession {

	private Integer sessionId;
	
	private Integer userId;
	
	private Integer userSource;
	
	private String sourceId;
	
	private Integer loginTime;
	
	private String pushToken;
	
	private String userToken;
	
	private Integer clientType;//登录端类型(新增)
	
	public CenterSession(){
		
	}
	public CenterSession(Integer loginTime,Integer userId, Integer userSource,String userToken,Integer clientType){
		this.loginTime = loginTime ;
		this.userId = userId;
		this.userSource = userSource;
		this.userToken = userToken ;
		this.clientType = clientType;
	}
	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserSource() {
		return userSource;
	}

	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public Integer getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Integer loginTime) {
		this.loginTime = loginTime;
	}
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
