package com.seentao.stpedu.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.club.service.GetBackgroundColorService;
/**
 * @author cxw
 */
@Controller
public class GetBackgroundColorController implements IGetBackgroundColorController {

	@Autowired
	private GetBackgroundColorService getBackgroundColorController;
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.club.controller.IGetBackgroundColorController#getBackgroundColor(java.lang.String, int, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value="getBackgroundColor")
	public String getBackgroundColor(String userId){
		
		return getBackgroundColorController.getbackgroundcolors(userId);
	}
}
