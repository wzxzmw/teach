package com.seentao.stpedu.teacher.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.teacher.service.GetClassesService;

/**
 * 获取班级信息
 * <pre>项目名称：stpedu    
 * GetClassesController    
 */
@Controller
public class GetClassesController implements IGetClassesController {

	@Autowired
	private GetClassesService getClassesService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.IGetClassesController#getClasses(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
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
	@Override
	@ResponseBody
	@RequestMapping("/getClasses")
	public String getClasses(Integer userId,Integer schoolId,Integer teacherId,Integer classId,String searchWord,Integer start,Integer limit,Integer inquireType,Integer clubId,Integer sortType,Integer classType,Integer memberId,Integer requestSide){
		return getClassesService.getClasses(userId, schoolId, teacherId, classId, searchWord, start, limit, inquireType ,clubId ,sortType,classType,memberId,requestSide).toJSONString();
	}
	

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
	 * @author ligs
	 * @date 2016年7月19日 上午9:51:42
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getClassesForMobile")
	public String getClassesForMobile(Integer userId, Integer schoolId, Integer teacherId, Integer classId,String searchWord, Integer start, Integer limit, Integer inquireType, Integer clubId, Integer sortType,Integer classType,Integer memberId) {
		return getClassesService.getClassesForMobile( userId,  schoolId,  teacherId,  classId, searchWord,  start,  limit,  inquireType,  clubId,  sortType, classType,memberId).toJSONString();
	}
}
