package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seentao.stpedu.common.dao.ClubRelRemindMemberDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.entity.ClubRelRedPacketMember;
import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class ClubRelRemindMemberService{
	
	@Autowired
	private ClubRelRemindMemberDao clubRelRemindMemberDao;
	@Autowired
	private CenterUserService centerUserService;
	
	@Autowired
	private ClubMemberService clubMemberService;
	
	/** 
	* @Title: getClubRelRemindMemberByRemindId 
	* @Description: 根据提醒ID获得提醒会员关系列表
	* @param remindId
	* @return List<ClubRelRemindMember>
	* @author liulin
	* @date 2016年6月29日 下午2:27:22
	*/
	public List<ClubRelRemindMember> getClubRelRemindMemberByRemindId(Integer remindId) {
		ClubRelRemindMember clubRelRemindMember = new ClubRelRemindMember();
		clubRelRemindMember.setRemindId(remindId);
		return clubRelRemindMemberDao.selectClubRelRemindMember(clubRelRemindMember);
	}

	/** 
	* @Title: addRemindMember 
	* @Description: 添加一条提醒
	* @param id
	* @param remindObject	提醒对象
	* @param remindUserIds  提醒指定对象的ID
	* @return void
	* @author liulin
	* @date 2016年6月29日 下午2:28:32
	*/
	public void addRemindMember(Integer id, Integer userId, Integer clubId, Integer remindObject, String remindUserIds) {
		//批量添加列表
		List<ClubRelRemindMember> members = new ArrayList<ClubRelRemindMember>();
		//如果提醒的是全部对象
		if(remindObject.intValue() == GameConstants.REMIND_OBJECT_ALL){
			//根据clubId获得除userId以外的所有会员的是否有新提醒置为1.
			List<ClubMember> clubMembers = clubMemberService.selectClubMemberByClubId(userId, clubId);
			//clubMemberService.updateIsNewRemindByMemberId(clubMembers);
			String memberIds = "";
			for(ClubMember clubMember : clubMembers){
				memberIds = memberIds + clubMember.getMemberId() + ",";
				//拼装提醒会员关系列表
				ClubRelRemindMember member = new ClubRelRemindMember();
				member.setRemindId(id);
				member.setUserId(clubMember.getUserId());
				members.add(member);
				//删除缓存
				JedisCache.delRedisVal(ClubMember.class.getSimpleName(), String.valueOf(clubMember.getMemberId()));
			}
			//更改全部会员是否有新提醒置为1
			if(!StringUtil.isEmpty(memberIds)){
				memberIds = memberIds.substring(0, memberIds.length()-1);
				clubMemberService.updateIsNewRemindByMemberIds(memberIds);
			}
			//批量添加提醒会员关系列表
			clubRelRemindMemberDao.insertBatchClubRelRemindMember(members);
		}else if(remindObject.intValue() == GameConstants.REMIND_OBJECT_OTHER){
		//提醒的是指定的会员
			String [] ids = remindUserIds.split(",");
			for(int i = 0; i<ids.length; i++){
				ClubRelRemindMember member = new ClubRelRemindMember();
				member.setRemindId(id);
				member.setUserId(Integer.parseInt(ids[i]));
				members.add(member);
			}
			//批量添加提醒会员关系列表
			clubRelRemindMemberDao.insertBatchClubRelRemindMember(members);
			List<ClubMember> clubMembers = clubMemberService.selectClubMemberByUserIds(remindUserIds, clubId);
			//修改会员没有新提醒为1
			clubMemberService.updateIsNewRemindByUserIds(remindUserIds, clubId);
			//删除会员缓存
			for(ClubMember member : clubMembers){
				JedisCache.delRedisVal(ClubMember.class.getSimpleName(), String.valueOf(member.getMemberId()));
			}
		}
	}

}