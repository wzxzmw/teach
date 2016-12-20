package com.seentao.stpedu.teacher.controller;


public interface IGetClassesController {

	/**
	 * getClasses(获取班级信息)  
	 * @param userId 用户id 
	 * @param schoolId 学校id
	 * @param teacherId 教师id
	 * @param classId 班级id
	 * @param classType 班级类型 1：教学班 2：俱乐部培训班
	 * @param clubId 俱乐部id
	 * @param searchWord 搜素词
	 * @param start 分页的开始页
	 * @param limit 每页数量
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param inquireType 查询类型
	 * @author ligs
	 * @date 2016年6月20日 下午9:30:42
	 * @return
	 */
	String getClasses(Integer userId, Integer schoolId, Integer teacherId, Integer classId, String searchWord,
			Integer start, Integer limit, Integer inquireType, Integer clubId, Integer sortType, Integer classType,Integer memberId,Integer requestSide);

	/**
	 * getClassesForMobile(移动端：获取班级信息)  
	 * @param userId 用户id 
	 * @param schoolId 学校id
	 * @param teacherId 教师id
	 * @param classId 班级id
	 * @param classType 班级类型 1：教学班 2：俱乐部培训班
	 * @param clubId 俱乐部id
	 * @param searchWord 搜素词
	 * @param start 分页的开始页
	 * @param limit 每页数量
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param inquireType 查询类型
	 * @param memberId 用户id
	 * @author ligs
	 * @date 2016年6月20日 下午9:30:42
	 * @return
	 */
	String getClassesForMobile(Integer userId, Integer schoolId, Integer teacherId, Integer classId, String searchWord,
			Integer start, Integer limit, Integer inquireType, Integer clubId, Integer sortType, Integer classType,Integer memberId);

}