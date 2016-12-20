package com.seentao.stpedu.club.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.dao.ClubBackgroundColorDao;
import com.seentao.stpedu.common.entity.ClubBackgroundColor;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;
/**
 * @author cxw
 */
@Service
public class GetBackgroundColorService {

	@Autowired
	private ClubBackgroundColorDao backgroundColorDao;
	/**
	 *  获取自定义背景色
	 * @param userId
	 * @param userType
	 * @return 
	 */
	public String getbackgroundcolors(String userId) {
		 
		try {
			List<ClubBackgroundColor> list = backgroundColorDao.selectAllClubBackgroundColor();
			JSONArray jsonArray = (JSONArray) JSONArray.toJSON(list);
			 
			return Common.getReturn(AppErrorCode.SUCCESS, "获取自定义背景色成功", jsonArray ).toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "getbackgroundcolors", AppErrorCode.GETS_CUSTOM_BACKGROUND_COLOR_SUCCESS);
			return Common.getReturn(AppErrorCode.ERROR_TYPE_FIVE, AppErrorCode.GETS_CUSTOM_BACKGROUND_COLOR_FAILED).toJSONString();
		}
	}	
}
