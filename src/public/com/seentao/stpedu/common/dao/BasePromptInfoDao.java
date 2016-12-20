package com.seentao.stpedu.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.BasePromptInfo;
import com.seentao.stpedu.common.sqlmap.BasePromptInfoMapper;


@Repository
public class BasePromptInfoDao {

	@Autowired
	private BasePromptInfoMapper basePromptInfoMapper;
	
	
	public void insertBasePromptInfo(BasePromptInfo basePromptInfo){
		basePromptInfoMapper .insertBasePromptInfo(basePromptInfo);
	}
	
	public void deleteBasePromptInfo(BasePromptInfo basePromptInfo){
		basePromptInfoMapper .deleteBasePromptInfo(basePromptInfo);
	}
	
	public void updateBasePromptInfoByKey(BasePromptInfo basePromptInfo){
		basePromptInfoMapper .updateBasePromptInfoByKey(basePromptInfo);
	}
	
	public List<BasePromptInfo> selectSingleBasePromptInfo(BasePromptInfo basePromptInfo){
		return basePromptInfoMapper .selectSingleBasePromptInfo(basePromptInfo);
	}
	
	public BasePromptInfo selectBasePromptInfo(BasePromptInfo basePromptInfo){
		List<BasePromptInfo> basePromptInfoList = basePromptInfoMapper .selectSingleBasePromptInfo(basePromptInfo);
		if(basePromptInfoList == null || basePromptInfoList .size() == 0){
			return null;
		}
		return basePromptInfoList .get(0);
	}
	
	public List<BasePromptInfo> selectAllBasePromptInfo(){
		return basePromptInfoMapper .selectAllBasePromptInfo();
	}
}