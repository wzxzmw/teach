package com.seentao.stpedu.teacher.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.teacher.service.GetMessageService;

/**
 * 获取教师信息
 * @author ligs
 *
 */
@Controller
public class GetMessageController implements IGetMessageController {
	
	@Autowired
	private GetMessageService getMessageService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.IGetMessageController#getTeachers(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	@Override
	/**
	 * getTeachers(获取教师信息) 
	 * @param userType 用户类型  
	 * @param classId 班级id 
	 * @param classType 班级类型
	 * @param teacherId 教师id
	 * @param start 分页的开始页(从0开始)
	 * @param limit 每页数量
	 * @param inquireType 查询类型 1：学生端未加入班级时获取教师列表，作为筛选班级的查询条件   2：通过教师id获取唯一一条教师信息   3：通过班级id获取唯一一条教师信息
	 * @author ligs
	 * @date 2016年6月17日 下午3:16:34
	 * @return 
	 */
	@ResponseBody
	@RequestMapping("/getTeachers")
	public String getTeachers(Integer classId, Integer teacherId, Integer start, Integer limit, Integer inquireType,Integer userId, Integer classType){
		return getMessageService.getTeachers(classId,teacherId,start,limit,inquireType,userId,classType).toJSONString();
	}
}
