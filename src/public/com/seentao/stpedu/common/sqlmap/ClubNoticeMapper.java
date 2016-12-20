package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubNotice;
import java.util.List;
import java.util.Map;

public interface ClubNoticeMapper {

	public abstract void insertClubNotice(ClubNotice clubNotice);
	
	public abstract void deleteClubNotice(ClubNotice clubNotice);
	
	public abstract void updateClubNoticeByKey(ClubNotice clubNotice);
	
	public abstract List<ClubNotice> selectSingleClubNotice(ClubNotice clubNotice);
	
	public abstract List<ClubNotice> selectAllClubNotice();

	public abstract Integer queryCount(Map<String, Object> paramMap);

	public abstract List<ClubNotice> selectClubNoticesInfo(Map<String, Object> initParam);

	public abstract void delClubNoticeAll(List<ClubNotice> delClubNotice);

	public abstract void updateClubNoticeByKeyAll(ClubNotice clubNotices);
	
}