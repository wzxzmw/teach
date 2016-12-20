
package com.seentao.stpedu.club.service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.entity.CenterNews;
import com.seentao.stpedu.common.entity.CenterNewsStatus;
import com.seentao.stpedu.constants.GameConstants;

public class CommonParameter {
	/**组装参数
	 * @param centerNewsStatus
	 * @param centerNews 
	 * @param dynamicType  动态入口类型
	 * @param message 动态入口标题
	 * @param commonId 动态入口主体id
	 * @param hasNewMsg 是否有未读消息
	 * @return
	 */
	protected static JSONObject buildParameter(CenterNewsStatus centerNewsStatus,CenterNews centerNews,int dynamicType,String message,Integer commonId,Integer hasNewMsg){
		JSONObject obj = new JSONObject();
		/*
		 * 动态入口类型
		 */
		obj.put("dynamicEntryType", String.valueOf(dynamicType));
		/*
		 * 动态入口的标题
		 */
		obj.put("dynamicEntryTitle", message);
		/*
		 * 动态入口主体的id
		 */
		if(dynamicType == 1 || dynamicType == 6 || dynamicType ==8){
			/*
			 * 如果动态入口类型为1:消息，该字段返回空；
			 * 如果动态入口类型为6:推荐，该字段返回空
			 * 如果动态入口类型为8:人脉，该字段返回空;
			 *  如果动态入口类型为9:评选，该字段返回投票id；
			 */
			obj.put("dynamicEntryMainId", "");
		}if(dynamicType == 2 || dynamicType ==3 || dynamicType ==4 || dynamicType ==5 || dynamicType == 7 || dynamicType ==9){
			/*
			 * 如果动态入口类型为2:班级群(群组讨论)，该字段是教学班id；
			 * 如果动态入口类型为3:答疑，该字段是教学班id
			 * 如果动态入口类型为4:实训，该字段是教学班id;
			 * 如果动态入口类型为5:俱乐部，该字段是俱乐部id;
			 * 如果动态入口类型为7:竞猜，该字段是赛事id;
			 */
			obj.put("dynamicEntryMainId",commonId == null ? String.valueOf(0):String.valueOf(commonId) );
		}
		
		/*
		 * 1:私信；2:关注；3:邀请；4:比赛；5:系统；
			如果动态入口类型为1:消息，返回该字段；
		 */
		if(dynamicType == 1){
			if(centerNews == null){
				obj.put("messageType", String.valueOf(1));
				obj.put("hasNewMsg", centerNewsStatus == null ?0:centerNewsStatus.getIsNewPrivateMessage());
			}else{
				if(centerNewsStatus != null){
					int newsType = centerNews.getNewsType();
					if(newsType == 1){
						obj.put("messageType", GameConstants.GET_MESSAGE_TWO);
						obj.put("hasNewMsg", centerNewsStatus.getIsNewAttention());
					}else if(newsType == 2){
						obj.put("messageType", GameConstants.GET_MESSAGE_THREE);
						obj.put("hasNewMsg", centerNewsStatus.getIsNewQuestionInvite());
					}else if(newsType == 3){
						obj.put("messageType", GameConstants.GET_MESSAGE_FOUR);
						obj.put("hasNewMsg", centerNewsStatus.getIsNewGameInvite());
					}else if(newsType == 4){
						obj.put("messageType", GameConstants.GET_MESSAGE_FIVE);
						obj.put("hasNewMsg", centerNewsStatus.getIsNewSysNews());
					}
					
				}else{
					obj.put("hasNewMsg", 0);
				}
				
			}
			
		}
		
		/*
		 * 1:是；0:否；
			默认为否；
			页面的消息icon上判断是否显示红点时使用
		 */
		if(dynamicType != 1){
			obj.put("hasNewMsg", hasNewMsg);
		}
		return obj;
	}
}
