package com.seentao.stpedu.teacher.controller;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.teacher.service.SubmitClassService;

/**
 * 提交班级信息
 * <pre>项目名称：stpedu    
 * 类名称：SbmitClassController    
 */
@Controller
public class SbmitClassController implements ISbmitClassController {
	@Autowired
	private SubmitClassService submitClassService;
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.ISbmitClassController#submitClass(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Double)    
	 */
	/**
	 * 
	 * submitClass(提交班级信息)   
	 * @param userId 用户ID
	 * @param classId 班级id
	 * @param className 班级名称
	 * @param classLogoId 班级logo的图片id
	 * @param classType 班级类型  1:教学班；2:俱乐部培训班；
	 * @param classDesc 班级介绍 
	 * @param needToPay 加入时是否需要收费   1:是；0:否；默认是0；
	 * @param fLevelAccount 收取的一级货币
	 * @author ligs
	 * @date 2016年6月22日 上午10:24:21
	 * @return
	 * @throws ParseException 
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitClass")
	public String submitClass(Integer classId,String className,Integer classLogoId,Integer classType,Integer userId,String classDesc,Integer needToPay,Integer fLevelAccount) {
		//校验创业宝
		if(classType == 2 && needToPay == 1){
				if(!Common.isValidMoney(String.valueOf(fLevelAccount)))
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.VERIFY_MONEY).toJSONString();
		}
		String[] str = {"\\","/","*","<","|","'",">","&","#","$","^"};
		for(String s:str){
			if(className.contains(s)){
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO,BusinessConstant.ERROR_TITLE_NOT_REGULAR).toJSONString();
			}
		}
		return submitClassService.submitClass( classId, className, classLogoId, classType,userId,classDesc,needToPay,StringUtils.isEmpty(String.valueOf(fLevelAccount))?0:Double.valueOf(fLevelAccount)).toJSONString();
	}
	
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.ISbmitClassController#submitDefaultClass(java.lang.Integer, java.lang.Integer)    
	 */
	@Override
	/**
	 * submitDefaultClass(设置默认班级)   
	 * @param classId 班级ID
	 * @param userId 用户ID
	 * @author ligs
	 * @date 2016年6月22日 上午11:22:23
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/submitDefaultClass")
	public String submitDefaultClass(Integer classId,Integer userId){
		return submitClassService.submitDefaultClass(classId,userId).toJSONString();
	}
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.ISbmitClassController#joinClass(java.lang.Integer, java.lang.Integer, java.lang.Integer)    
	 */
	/**
	 * joinClass(报名加入班级)   
	 * @param classId 班级ID
	 * @param userId 用户ID
	 * @param classType 班级类型 1：教学班 2：俱乐部培训班
	 * @author ligs
	 * @date 2016年6月22日 下午2:49:52
	 * @return
	 * @throws ParseException 
	 */
	@Override
	@ResponseBody
	@RequestMapping("/joinClass")
	public String joinClass(Integer classId,Integer userId,Integer classType) {
		if(( null == classId  || classId == 0) || ( null == userId  || userId == 0) || ( null == classType  || classType == 0)){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_ONE, AppErrorCode.REQUEST_PARAM_ERROR).toJSONString();
		}
		return submitClassService.joinClass(classId,userId,classType).toJSONString();
	}
	/* (non-Javadoc)    
	 * @see com.seentao.stpedu.teacher.controller.ISbmitClassController#submitTeacherOperation(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer)    
	 */
	/**
	 * submitTeacherOperation(教师邀请或踢出学生操作)   
	 * @param userId 用户ID
	 * @param classId 班级ID
	 * @param classType 班级类型 1：教学班  2：俱乐部培训班
	 * @param studentId 邀请或踢出的学生id 多个id用逗号分隔
	 * @param actionType 1:邀请加入；2:踢出班级；
	 * @author ligs
	 * @date 2016年6月22日 下午4:43:23
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping("/submitTeacherOperation")
	public String submitTeacherOperation(Integer userId,Integer classId,String studentId,Integer actionType,Integer classType){
		return submitClassService.submitTeacherOperation( userId, classId, studentId, actionType ,classType).toJSONString();
	}
}
