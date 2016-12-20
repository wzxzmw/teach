package com.seentao.stpedu.club.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IGetClubsForMobileController {

	/***
	 * 获取俱乐部信息
	 * @param userId
	 * @param memberId 人员id
	 * @param searchWord 搜索词
	 * @param clubId     俱乐部id
	 * @param start      起始数
	 * @param limit      每页数量
	 * @param inquireType   查询类型
	 * @param recordingActivity 是否统计活跃度
	 * @return
	 */
	String getClubsForMobile(String userId, Integer userType, String memberId,int hasConcernTheClub,
			String searchWord, String clubId, int start, int limit, int recordingActivity, int inquireType);

}