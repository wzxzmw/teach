package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import com.seentao.stpedu.common.entity.ClubMember;
import com.seentao.stpedu.common.sqlmap.ClubMemberMapper;
import com.seentao.stpedu.utils.LogUtil;


@Repository
public class ClubMemberDao {

	@Autowired
	private ClubMemberMapper clubMemberMapper;
	
	
	public int insertClubMember(ClubMember clubMember){
		clubMemberMapper .insertClubMember(clubMember);
		return clubMember.getMemberId();
	}
	
	public void deleteClubMember(ClubMember clubMember){
		clubMemberMapper .deleteClubMember(clubMember);
	}
	
	public void updateClubMemberByKey(ClubMember clubMember){
		clubMemberMapper .updateClubMemberByKey(clubMember);
	}
	/**
	 * 根据距主键ID修改
	 * @param 
	 * @return
	 */
	public void updateClubMemberById(Integer memberId,Integer memberStatus)throws RuntimeException{
		try {
			clubMemberMapper.updateClubMemberById(memberId, memberStatus);
		} catch (RuntimeException e) {
			LogUtil.error(this.getClass(), "updateClubMemberById", "更新失败");
			throw new RuntimeException(e);
		}
	}
	
	public List<ClubMember> selectSingleClubMember(ClubMember clubMember){
		return clubMemberMapper .selectSingleClubMember(clubMember);
	}
	
	public ClubMember selectClubMember(ClubMember clubMember){
		List<ClubMember> clubMemberList = clubMemberMapper .selectSingleClubMember(clubMember);
		if(clubMemberList == null || clubMemberList .size() == 0){
			return null;
		}
		return clubMemberList .get(0);
	}
	
	public ClubMember queryClubMemberSome(Map<String,Object> map){
		return clubMemberMapper.queryClubMemberSome(map);
	}
	/**
	 * 
	 * @return
	 */
	
	public ClubMember selectOnlyClubMember(Integer userId){
		return clubMemberMapper.selectOnlyClubMember(userId);
	}
	
	public List<ClubMember> selectAllClubMember(){
		return clubMemberMapper .selectAllClubMember();
	}

	public Integer queryCount(ClubMember c) {
		return clubMemberMapper.queryCount( c);
	}

	public ClubMember checkPresidentAndArenaCompetition(ClubMember member) {
		List<ClubMember> list = clubMemberMapper.checkPresidentAndArenaCompetition(member);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}
	
	public boolean validatePresidentAndArenaCompetition(Map<String,Object> conditionMap) {
	    int num= clubMemberMapper.validatePresidentAndArenaCompetition(conditionMap);
		if(num>0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * redis组件 反射调用获取单表数据
	 */
	public ClubMember getEntityForDB(Map<String, Object> paramMap){
		ClubMember tmp = new ClubMember();
		tmp.setMemberId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectClubMember(tmp);
	}

	public List<ClubMember> selectClubMemberByClubId(ClubMember member) {
		return clubMemberMapper.selectClubMemberByClubId(member);
	}
	
	/** 
	* @Description:判断是否有待审核的会员
	*/
	public int checkClubMemberStatuts(ClubMember clubMember){
		if(clubMemberMapper.queryCount(clubMember)>0){
			return 1;
		}else{
			return 0;
		}
	}

	public List<ClubMember> getClubMemberList(ClubMember param) {
		return clubMemberMapper.getClubMemberList(param) ;
	}

	public Integer getClubMemberCount(ClubMember param) {
		return clubMemberMapper.getClubMemberCount( param) ;
	}

	public void batchUpdateByUserIdAndClubId(List<ClubMember> list) {
		clubMemberMapper.batchUpdateByUserIdAndClubId(list);
	}

	public void updateByUserIdAndClubId(ClubMember club) {
		clubMemberMapper.updateByUserIdAndClubId(club);
	}

	public List<ClubMember> getClubMemberByStatus(ClubMember c) {
		return clubMemberMapper.getClubMemberByStatus(c);
	}

	public Integer queryCountByStatus(ClubMember c) {
		return clubMemberMapper.queryCountByStatus( c) ;
	}

	public void updateClubmemerIsremoind(ClubMember clubMember) {

		clubMemberMapper.updateClubmemerIsremoind(clubMember);
	}

	public List<ClubMember> selectClubMemberByUserIds(Map<String, Object> paramMap) {
		return clubMemberMapper.selectClubMemberByUserIds(paramMap);
	}

	public void updateIsNewRemindByUserIds(Map<String, Object> paramMap) {
		clubMemberMapper.updateIsNewRemindByUserIds(paramMap);		
	}

	public void updateIsNewRemindByMemberIds(Map<String, Object> paramMap) {
		clubMemberMapper.updateIsNewRemindByMemberIds(paramMap);	
	}

	public void updateIsNewNoticeByMemberIds(Map<String, Object> paramMap) {
		clubMemberMapper.updateIsNewNoticeByMemberIds(paramMap);	
	}

	// 根据俱乐部clubId 和用户id ,以及1,2状态查询
	public ClubMember queryClubMemberByClubIdAndUserId(Integer userId,Integer clubId) {
		return clubMemberMapper.queryClubMemberByClubIdAndUserId(userId,clubId);	
	}
}