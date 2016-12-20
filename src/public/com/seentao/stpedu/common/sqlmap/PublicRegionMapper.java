package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.PublicRegion;
import java.util.List;

public interface PublicRegionMapper {

	public abstract void insertPublicRegion(PublicRegion publicRegion);
	
	public abstract void deletePublicRegion(PublicRegion publicRegion);
	
	public abstract void updatePublicRegionByKey(PublicRegion publicRegion);
	
	public abstract List<PublicRegion> selectSinglePublicRegion(PublicRegion publicRegion);
	
	public abstract List<PublicRegion> selectAllPublicRegion();

	public abstract List<PublicRegion> selectPublicRegionByCityId(PublicRegion publicRegion);
	
}