package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubRelRemindMember;
import com.seentao.stpedu.common.entity.ClubRemind;
import java.util.List;
import java.util.Map;

public interface ClubRemindMapper {

	public abstract void insertClubRemind(ClubRemind clubRemind);
	
	public abstract void deleteClubRemind(ClubRemind clubRemind);
	
	public abstract void updateClubRemindByKey(ClubRemind clubRemind);
	
	public abstract List<ClubRemind> selectSingleClubRemind(ClubRemind clubRemind);
	
	public abstract List<ClubRemind> selectAllClubRemind();

	public abstract Integer queryCount(Map<String, Object> paramMap);
}