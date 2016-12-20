package com.seentao.stpedu.common.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubRelRedPacketMemberDao;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;

@Service
public class ClubRelRedPacketMemberService{
	
	@Autowired
	private ClubRelRedPacketMemberDao clubRelRedPacketMemberDao;
	
	public String getClubRelRedPacketMember(Integer id) {
		ClubRelRedPacketMember clubRelRedPacketMember = new ClubRelRedPacketMember();
		clubRelRedPacketMember.setRelId(id);
		List<ClubRelRedPacketMember> clubRelRedPacketMemberList = clubRelRedPacketMemberDao .selectSingleClubRelRedPacketMember(clubRelRedPacketMember);
		if(clubRelRedPacketMemberList == null || clubRelRedPacketMemberList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(clubRelRedPacketMemberList);
	}

	public void batchInsertClubRelRedPacketMember(List<ClubRelRedPacketMember> clubRelRedPacketMemberList) {
		clubRelRedPacketMemberDao. batchInsertClubRelRedPacketMember(clubRelRedPacketMemberList);
	}

	public void insertClubRelRedPacketMember(ClubRelRedPacketMember clubRelRedPacketMember) {
		clubRelRedPacketMemberDao.insertClubRelRedPacketMember(clubRelRedPacketMember);
	}

	/** 
	* @Title: getClubRelRemindMemberByRedPackId 
	* @Description: 根据红包ID获得红包会员关系列表
	* @param redPacketId
	* @return List<ClubRelRedPacketMember>
	* @author liulin
	* @date 2016年7月13日 下午5:30:54
	*/
	public List<ClubRelRedPacketMember> getClubRelRemindMemberByRedPackId(Integer redPacketId) {
		ClubRelRedPacketMember clubRelRedPacketMember = new ClubRelRedPacketMember();
		clubRelRedPacketMember.setRpId(redPacketId);
		return clubRelRedPacketMemberDao.selectSingleClubRelRedPacketMember(clubRelRedPacketMember);
	}
}