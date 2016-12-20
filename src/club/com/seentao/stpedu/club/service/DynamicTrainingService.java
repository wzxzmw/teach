
package com.seentao.stpedu.club.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.TeachCourseCard;
import com.seentao.stpedu.common.interfaces.IGameInterfaceService;
import com.seentao.stpedu.common.service.TeachCourseCardService;
import com.seentao.stpedu.common.service.TeachCourseChapterService;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.hprose.HproseRPC;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class DynamicTrainingService {
	/** 
	* @Fields teachCourseChapterService : 课程章节服务
	*/ 
	@Autowired
	private TeachCourseChapterService teachCourseChapterService;
	/** 
	* @Fields teachCourseCardService : 课程卡服务
	*/ 
	@Autowired
	private TeachCourseCardService teachCourseCardService;
	
	protected JSONObject tellMePracticalTraining(Integer classId) {
			List<Integer> chapetIds = teachCourseChapterService.getAllChapetIdByClass(classId);
			if(CollectionUtils.isEmpty(chapetIds))
				return null;
			List<TeachCourseCard> cardList = teachCourseCardService.selectByChapterIds(StringUtil.ListToString(chapetIds, ","));
			if(CollectionUtils.isEmpty(cardList))
				return null;
				//游戏端需要通过所有课程卡IDS查询最近创建的实训课程
				StringBuffer sb = new StringBuffer();
				
				for(TeachCourseCard en : cardList){
					if(en.getCardType().intValue() == 4){
						sb.append(en.getCardId()).append(",");
					}
				}
				if(sb.length() == 0)
					return null ;
				
					String cardIds = sb.substring(0, sb.length()-1).toString();
					
					IGameInterfaceService game = HproseRPC.getRomoteClass(ActiveUrl.GAME_INTERFACE_URL, IGameInterfaceService.class);  
					/*
					 * 通过cardId远程调用游戏端获取教师最近一次创建的实训课程。
					 */
					String gameData = game.getTeacherCreateMatchInfoByCardIds(cardIds);
					if(StringUtils.isEmpty(gameData))
						return null;
						JSONObject gameJson = JSONObject.parseObject(gameData);
						if(gameJson.getInteger(GameConstants.CODE) != 0)
							return null;
							JSONObject match = gameJson.getJSONObject(GameConstants.RESULT);
							if(match.isEmpty())
								return null;
								String chapterId = null ;
								for(TeachCourseCard en :cardList){
									// 只能是实训作业
									if(en.getCardId().equals(match.getInteger("cardId"))){
										chapterId = String.valueOf(en.getChapterId());
									}
								}
								JSONObject obj = CommonParameter.buildParameter(null , null, 4, "实训："+match.getString("matchName"), classId, 0);
								obj.put("chapterId", chapterId);
								return obj;
	}
}
