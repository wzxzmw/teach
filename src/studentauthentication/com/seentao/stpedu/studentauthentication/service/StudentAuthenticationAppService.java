package com.seentao.stpedu.studentauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.TeachRelTeacherClass;
import com.seentao.stpedu.common.service.TeachRelStudentClassService;
import com.seentao.stpedu.common.service.TeachRelTeacherClassService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class StudentAuthenticationAppService {

	
	@Autowired
	private TeachRelStudentClassService relStudentClassService;
	
	@Autowired
	private TeachRelTeacherClassService relTeacherClassService;
	
	/**
	 * 学生的证书认证操作
	 * @param userId			用户id
	 * @param certObjectId		认证对象id
	 * @param actionType		认证操作
	 * @return
	 * @author 					lw
	 * @date					2016年6月22日  下午3:10:12
	 */
	public String submitCertRequest(int userId, int certObjectId, int actionType) {
		
		//	1.认证用户查询
		String userRedis = RedisComponent.findRedisObject(certObjectId, CenterUser.class);
		if(StringUtil.isEmpty(userRedis)){
			LogUtil.error(this.getClass(), "submitCertRequest", String.valueOf(AppErrorCode.ERROR_ACTIONTYPE_REQUEST_PARAM));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ACTIONTYPE_REQUEST_PARAM).toJSONString();
		}
		CenterUser user = JSONObject.parseObject(userRedis, CenterUser.class);
		
		//	2.认证教师班级关系查询
		TeachRelTeacherClass teacherAndClass = new TeachRelTeacherClass();
		teacherAndClass.setClassId(user.getClassId());
		teacherAndClass.setTeacherId(userId);
		TeachRelTeacherClass selectDBBean = relTeacherClassService.getTeachRelTeacherClass(teacherAndClass);
		if(selectDBBean == null){
			LogUtil.error(this.getClass(), "submitCertRequest", String.valueOf(AppErrorCode.ERROR_ACTIONTYPE_TEACHER_AND_USER));
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ACTIONTYPE_TEACHER_AND_USER).toJSONString();
		}
		
		//	3.1 添加认证资格 
		if(actionType == GameConstants.STUDENT_ACTIONTYPE_ADD){
			return relStudentClassService.addAuthentication(selectDBBean.getClassId(),certObjectId).toJSONString();
			
		//	3.2 取消认证资格 
		}else if(actionType == GameConstants.STUDENT_ACTIONTYPE_DEL){
			return relStudentClassService.delAuthentication(selectDBBean.getClassId(),certObjectId).toJSONString();
			
		//	3.3 申请证书 
		}else if(actionType == GameConstants.STUDENT_ACTIONTYPE_VIEW){
			return relStudentClassService.applyAuthentication(selectDBBean.getClassId(),certObjectId).toJSONString();
		}
		
		return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ACTIONTYPE_TYPE).toJSONString();
	}

}
