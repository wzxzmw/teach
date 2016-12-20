package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import com.seentao.stpedu.common.entity.BasePromptInfo;

public interface BasePromptInfoMapper {

	public abstract void insertBasePromptInfo(BasePromptInfo basePromptInfo);
	
	public abstract void deleteBasePromptInfo(BasePromptInfo basePromptInfo);
	
	public abstract void updateBasePromptInfoByKey(BasePromptInfo basePromptInfo);
	
	public abstract List<BasePromptInfo> selectSingleBasePromptInfo(BasePromptInfo basePromptInfo);
	
	public abstract List<BasePromptInfo> selectAllBasePromptInfo();
	
}