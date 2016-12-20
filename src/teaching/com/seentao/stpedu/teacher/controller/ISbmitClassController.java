package com.seentao.stpedu.teacher.controller;

import java.text.ParseException;


public interface ISbmitClassController {

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
	String submitClass(Integer classId, String className, Integer classLogoId, Integer classType, Integer userId,
			String classDesc, Integer needToPay, Integer fLevelAccount);

	/**
	 * submitDefaultClass(设置默认班级)   
	 * @param classId 班级ID
	 * @param userId 用户ID
	 * @author ligs
	 * @date 2016年6月22日 上午11:22:23
	 * @return
	 */
	String submitDefaultClass(Integer classId, Integer userId);

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
	String joinClass(Integer classId, Integer userId, Integer classType);

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
	String submitTeacherOperation(Integer userId, Integer classId, String studentId, Integer actionType,
			Integer classType);

}