package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.PublicPicture;
import java.util.List;

public interface PublicPictureMapper {

	public abstract int insertPublicPicture(PublicPicture publicPicture);
	
	public abstract void deletePublicPicture(PublicPicture publicPicture);
	
	public abstract void updatePublicPictureByKey(PublicPicture publicPicture);
	
	public abstract List<PublicPicture> selectSinglePublicPicture(PublicPicture publicPicture);
	
	public abstract List<PublicPicture> selectAllPublicPicture();
	
}