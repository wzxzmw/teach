package com.seentao.stpedu.persionalcenter.controller;


public interface IPersionalCenterController {

	/**
	 * 发心情
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部id
	 * @param moodContent		心情内容
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	String submitMood(String userName,String userId,String userType,String userToken,String clubId,String moodContent);

	/**
	 * 提示红点的操作
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部id
	 * @param actionType		操作类型:
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	String getRemindRedDot(String userName,String userId,String userType,String userToken,String actionType,String clubId);
	/**
	 * 提示红点的操作【手机版】
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param clubId		俱乐部id
	 * @param actionType		操作类型:
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	String getRemindRedDotForMobile(String userName,String userId,String userType,String userToken,String actionType,String clubId);
	
	

	/**
	 * 提交附件的OSS信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param attachmentName		附件名称
	 * @param attachmentLink		附件地址
	 * @param attachmentSeconds		附件时长
	 * @param attachmentMajorType		附件一级分类
	 * @param attachmentType		附件类型
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	String submitAttachmentOSS(String userName,String userId,String userType,String userToken,String attachmentName,String attachmentLink,String attachmentSeconds,String attachmentMajorType,String attachmentType);

	/**
	 * 提交登录用户的信息
	 * @param userName				用户名
	 * @param userId				用户id
	 * @param userType				用户类型
	 * @param userToken				用户唯一标识
	 * @param actionType		提交操作
	 * @param headLinkId		头像id
	 * @param nickname		昵称
	 * @param sex		性别
	 * @param phoneNumber		手机
	 * @param realName		姓名
	 * @param schoolId		学校id
	 * @param collegeId		学院id
	 * @param provinceId		所在省id
	 * @param cityId		所在市id
	 * @param idcard		身份证
	 * @param desc		个人签名
	 * @param birthday		生日(时间戳)
	 * @param profession		职业
	 * @param studentID		学籍号(学生)
	 * @param studentCard		学生证号(学生)
	 * @param teacherCard		教师证号(教师)
	 * @param positionalTitle		专业职称(教师)
	 * @param speciality		专业名称(学生 教师)
	 * @param grade		年级(学生 教师)
	 * @param educationLevel		教育程度
	 * @return
	 * @author  lijin
	 * @date    2016年6月28日 下午10:59:25
	 */
	String submitLoginUser(String userName,String userId,String userType,String userToken,String actionType,String headLinkId,String nickname,Integer sex,String realName,String phoneNumber,Integer schoolId,Integer collegeId,String provinceId,String cityId,String idcard,String desc,Integer birthday,Integer profession,String studentID,String studentCard,String teacherCard,String positionalTitle,String speciality,String grade,Integer educationLevel,String address,String userNameForEdit);

}