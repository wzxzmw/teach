package com.seentao.stpedu.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.GetClubsService;
/**
 * @author cxw
 */
@Controller
public class GetClubsController implements IgetClubsService {

	@Autowired
	private GetClubsService getClubsService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.club.controller.IgetClubsService#getClubs(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "getClubs")
	public String getClubs(String userId,String memberId,String searchWord,String clubId,int hasConcernTheClub,int start,int limit,int inquireType,String gameEventId){
		
		return getClubsService.getClubsInfo(userId,memberId,searchWord,clubId,hasConcernTheClub,start,limit,inquireType,gameEventId);
	}
}
