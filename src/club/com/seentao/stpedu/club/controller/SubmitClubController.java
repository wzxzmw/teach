package com.seentao.stpedu.club.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.SubmitClubService;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
/**
 * @author cxw
 */
@Controller
public class SubmitClubController implements ISubmitClubController {

	@Autowired
	private SubmitClubService submitClubService;

	/**
	 * userId 当前登录者用户userId
	 * clubId 俱乐部id
	 * clubType 俱乐部类型
	 * clubName 俱乐部名称
	 * clubDesc 俱乐部介绍
	 * clubLogoId 俱乐部logo图片id
	 * provinceId 俱乐部所属省id 
	 * cityId 俱乐部所属城市id
	 * schoolId 俱乐部所属学校id
	 * addMemberType 会员加入方式
	 * fLevelAccount 收取一级货币
	 * gameBannerId 擂台海报id
	 * teachingBannerId 培训海报id
	 * styleBannerId 风格海报id
	 * backgroundColorId 背景色id
	 * actionType 操作类型
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="submitClub")
	public String submitClub(String userId,String clubId,int clubType,String clubName,String clubDesc,
			String clubLogoId,String provinceId,String cityId,String schoolId,int addMemberType,
			int fLevelAccount,String gameBannerId,String teachingBannerId,String styleBannerId,
			String backgroundColorId,int actionType){
		// 当clubId为空表示创建俱乐部
		if(StringUtils.isEmpty(clubId)){
			//校验俱乐部文本输入，2-10个中英文字符、数字，不支持输入空格及特殊符号 
			if(!Common.isValidName(clubName)){
				//俱乐部名称格式不对
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_CHAR_LEN).toJSONString();
			}
			if(submitClubService.userIsExist(clubName)){
				//俱乐部名称占用
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.SUBMIT_USER_NICKNAME_REPLACE).toJSONString();
			}
			boolean isNum = Common.isLength(clubDesc, 30);
			if(isNum != true){
				//2-30个字
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PLEASE_ENTER_TWO_THREE_WORD).toJSONString();
			}
			if(clubType == 1){
				if("".equals(cityId) || "".equals(schoolId)){
					//请选择地区或学校
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PLEASE_SELECT_AREA_OR_SCHOOL).toJSONString();
				}
			} 
			if(addMemberType==0){
				//请选择会员加入方式
				return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PLEASE_SELECT_MEMBER_JOIN_CLUB).toJSONString();
			}
			return submitClubService.submitClubInfo(userId,clubId,clubType,clubName,clubDesc,clubLogoId,provinceId,cityId,schoolId,addMemberType,fLevelAccount,
					gameBannerId,teachingBannerId,styleBannerId,backgroundColorId,actionType);

		}else{
			//否则表示修改俱乐部
			
			if(actionType!=4&&actionType!=5){
				//校验创业宝
				//if(StringUtils.isNotEmpty(fLevelAccount)){
					//if(!Common.isValidMoney(fLevelAccount)){
						//return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.VERIFY_MONEY).toJSONString();
						
					//}
				//}
				boolean isNum = Common.isLength(clubDesc, 30);
				if(isNum != true){
					//2-30个字
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PLEASE_ENTER_TWO_THREE_WORD).toJSONString();
				}
			}if(actionType == 3 && addMemberType == 3){
				if(StringUtils.isNotEmpty(String.valueOf(fLevelAccount))){
				if(!Common.isValidMoney(String.valueOf(fLevelAccount))){
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.VERIFY_MONEY).toJSONString();
				}
			}
			}
			if(clubType == 1){
				if("".equals(cityId) || "".equals(schoolId)){
					//请选择地区或学校
					return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.PLEASE_SELECT_AREA_OR_SCHOOL).toJSONString();
				}
			} 
			return submitClubService.submitClubInfo(userId,clubId,clubType,clubName,clubDesc,clubLogoId,provinceId,cityId,schoolId,addMemberType,fLevelAccount,
					gameBannerId,teachingBannerId,styleBannerId,backgroundColorId,actionType);
		}
	}
}
