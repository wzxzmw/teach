package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.PublicAttachment;
import java.util.List;
import java.util.Map;

public interface PublicAttachmentMapper {

	public abstract int insertPublicAttachment(PublicAttachment publicAttachment);
	
	public abstract void deletePublicAttachment(PublicAttachment publicAttachment);
	
	public abstract void updatePublicAttachmentByKey(PublicAttachment publicAttachment);
	
	public abstract List<PublicAttachment> selectSinglePublicAttachment(PublicAttachment publicAttachment);
	
	public abstract List<PublicAttachment> selectAllPublicAttachment();

	public abstract List<PublicAttachment> getOtherPublicAttachment(Map<String, Object> attachmentids);

	public abstract List<PublicAttachment> getClubOtherPublicAttachment(Map<String, Object> attachmentids);
	
	public abstract List<PublicAttachment> getOtherPublicAttachment1(Integer courseId);

	public abstract List<PublicAttachment> getClubOtherPublicAttachment1(Integer courseId);

	
}