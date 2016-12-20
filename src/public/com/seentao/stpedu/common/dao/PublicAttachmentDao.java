package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.PublicAttachment;
import com.seentao.stpedu.common.sqlmap.PublicAttachmentMapper;


@Repository
public class PublicAttachmentDao {

	@Autowired
	private PublicAttachmentMapper publicAttachmentMapper;
	
	
	public int insertPublicAttachment(PublicAttachment publicAttachment){
	 publicAttachmentMapper .insertPublicAttachment(publicAttachment);
	 return publicAttachment.getAttaId();
	}
	
	public void deletePublicAttachment(PublicAttachment publicAttachment){
		publicAttachmentMapper .deletePublicAttachment(publicAttachment);
	}
	
	public void updatePublicAttachmentByKey(PublicAttachment publicAttachment){
		publicAttachmentMapper .updatePublicAttachmentByKey(publicAttachment);
	}
	
	public List<PublicAttachment> getOtherPublicAttachment(Integer courseId) {
		return publicAttachmentMapper.getOtherPublicAttachment1(courseId);
	}
	
	public List<PublicAttachment> getClubOtherPublicAttachment(Integer courseId) {
		return publicAttachmentMapper.getClubOtherPublicAttachment1(courseId);
	}
	
	public List<PublicAttachment> selectSinglePublicAttachment(PublicAttachment publicAttachment){
		return publicAttachmentMapper .selectSinglePublicAttachment(publicAttachment);
	}
	
	public PublicAttachment selectPublicAttachment(PublicAttachment publicAttachment){
		List<PublicAttachment> publicAttachmentList = publicAttachmentMapper .selectSinglePublicAttachment(publicAttachment);
		if(publicAttachmentList == null || publicAttachmentList .size() == 0){
			return null;
		}
		return publicAttachmentList .get(0);
	}
	
	public List<PublicAttachment> selectAllPublicAttachment(){
		return publicAttachmentMapper .selectAllPublicAttachment();
	}

	public List<PublicAttachment> getOtherPublicAttachment(Map<String, Object> attachmentids) {
		return publicAttachmentMapper.getOtherPublicAttachment(attachmentids);
	}
	
	/**
	 * redis根据主键查询数据
	 * @param paramMap
	 * @return
	 */
	public PublicAttachment getEntityForDB(Map<String, Object> paramMap){
		PublicAttachment tmp = new PublicAttachment();
		tmp.setAttaId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectPublicAttachment(tmp);
	}

	public List<PublicAttachment> getClubOtherPublicAttachment(Map<String, Object> attachmentids) {
		return publicAttachmentMapper.getClubOtherPublicAttachment(attachmentids);
	}

}