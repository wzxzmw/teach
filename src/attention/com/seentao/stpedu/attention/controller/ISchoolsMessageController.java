package com.seentao.stpedu.attention.controller;


public interface ISchoolsMessageController {

	/**
	 * getSchools(获取学校信息)
	 * @param userId 用户ID
	 * @param start 起始数
	 * @param limit 每页数量 
	 * @param provinceId 省份id
	 * @param cityId 城市ID
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param inquireType 1:教学模块，学校列表页调用   2:俱乐部模块，创建俱乐部页面的选择学校时调用
	 * @author ligs
	 * @date 2016年6月29日 下午3:57:42
	 * @return
	 */
	String getSchools(Integer userId, Integer start, Integer limit, Integer provinceId, Integer cityId,
			Integer sortType, Integer inquireType);

}