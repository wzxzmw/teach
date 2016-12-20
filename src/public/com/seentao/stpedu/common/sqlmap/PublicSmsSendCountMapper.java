package com.seentao.stpedu.common.sqlmap;

import java.util.List;

import com.seentao.stpedu.common.entity.PublicSmsSendCount;

public interface PublicSmsSendCountMapper {

	public abstract void insertPublicSmsSendCount(PublicSmsSendCount publicSmsSendCount);
	
	public abstract void deletePublicSmsSendCount(PublicSmsSendCount publicSmsSendCount);
	
	public abstract void updatePublicSmsSendCountByKey(PublicSmsSendCount publicSmsSendCount);
	
	public abstract List<PublicSmsSendCount> selectSinglePublicSmsSendCount(PublicSmsSendCount publicSmsSendCount);
	
	public abstract List<PublicSmsSendCount> selectAllPublicSmsSendCount();

	public abstract List<PublicSmsSendCount> selectPublicSmsSendCountByPhone(PublicSmsSendCount publicSmsSendCount);
	
	public abstract void updatePublicSmsSendCountByPhone(PublicSmsSendCount publicSmsSendCount);
}