package com.seentao.stpedu.common.sqlmap;

import com.seentao.stpedu.common.entity.ClubBackgroundColor;
import java.util.List;

public interface ClubBackgroundColorMapper {

	public abstract void insertClubBackgroundColor(ClubBackgroundColor clubBackgroundColor);
	
	public abstract void deleteClubBackgroundColor(ClubBackgroundColor clubBackgroundColor);
	
	public abstract void updateClubBackgroundColorByKey(ClubBackgroundColor clubBackgroundColor);
	
	public abstract List<ClubBackgroundColor> selectSingleClubBackgroundColor(ClubBackgroundColor clubBackgroundColor);
	
	public abstract List<ClubBackgroundColor> selectAllClubBackgroundColor();
	
}