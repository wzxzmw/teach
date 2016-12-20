package com.seentao.stpedu.attention.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.attention.service.SchoolsMessageService;

/**
 * 学校信息
 * <pre>项目名称：stpedu    
 * 类名称：SchoolsMessageController    
 */
@Controller
public class SchoolsMessageController implements ISchoolsMessageController {
	
	@Autowired
	private SchoolsMessageService schoolsMessageService;
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.attention.controller.ISchoolsMessageController#getSchools(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	@Override
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
	@ResponseBody
	@RequestMapping("/getSchools")
	public String getSchools(Integer userId,Integer start,Integer limit,Integer provinceId,Integer cityId,Integer sortType,Integer inquireType){
		return schoolsMessageService.getSchools( userId, start, limit, provinceId, cityId, sortType ,inquireType).toJSONString();
	}
}
