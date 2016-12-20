package com.seentao.stpedu.club.controller;

public interface IgetClubsService {

	/***
	 * 获取俱乐部信息
	 * @param userId
	 * @param memberId 人员id
	 * @param searchWord 搜索词
	 * @param clubId     俱乐部id
	 * @param hasConcernTheClub 是否已关注
	 * @param start      起始数
	 * @param limit      每页数量
	 * @param inquireType   查询类型
	 * @return
	 */
	String getClubs(String userId, String memberId, String searchWord,
			String clubId,int hasConcernTheClub, int start, int limit, int inquireType,String gameEventId);

}