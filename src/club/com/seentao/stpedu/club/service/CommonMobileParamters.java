
package com.seentao.stpedu.club.service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.entity.CenterLive;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年11月25日 下午4:37:29
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class CommonMobileParamters {
	/*
	 * 组装参数
	 */
	protected static JSONObject buildMobileParameters(CenterLive centerLive,CenterUser centerUser,String dynamicMainContent,String dynamicMainTitle,Integer dmCreateDate,Integer dynamicMainId){
		JSONObject obj = new JSONObject();
		/*
		 * 动态id
		 */
		obj.put("dynamicId", String.valueOf(centerLive.getLiveId()));
		/*
		 * 动态类型
		 */
		obj.put("dynamicType", String.valueOf(centerLive.getLiveType()));
		/*
		 * 动态主体的发布者id 俱乐部话题
		 */
		obj.put("dmCreaterId", String.valueOf(centerUser.getUserId()));
		
		obj.put("dmCreaterName", centerUser.getRealName());
		/*
		 * 动态主体发布者昵称
		 */
		obj.put("dmCreaterNickname", centerUser.getNickName());
		/*
		 * 动态主体的发布者头像链接
		 */
		String _pic = RedisComponent.findRedisObjectPublicPicture(centerUser.getHeadImgId(), BusinessConstant.DEFAULT_IMG_USER);
		PublicPicture pic = JSONObject.parseObject(_pic, PublicPicture.class);
		obj.put("dmCreaterHeadLink", Common.checkPic(pic.getFilePath())== true?pic.getFilePath()+ActiveUrl.HEAD_MAP:pic.getFilePath());
		/*
		 * 动态主体的标题
		 */
		obj.put("dynamicMainTitle", dynamicMainTitle);
		/*
		 * 动态主体的内容
		 * 
		 */
		obj.put("dynamicMainContent", dynamicMainContent);
		/*
		 * 动态主体发布时间的时间戳
		 */
		obj.put("dmCreateDate", String.valueOf(dmCreateDate));

		/*
		 * 动态主体的id
		 */
		if(dynamicMainId != null)
		obj.put("dynamicMainId", String.valueOf(dynamicMainId));
		/*
		 * 是否置顶
		 */
		if(centerLive.getLiveType() == 1 || centerLive.getLiveType() ==5 ){
			obj.put("isTop", String.valueOf(centerLive.getIsTop()));
		}else{
			obj.put("isTop",0);
		}
		if(centerLive.getLiveType() != 11){
			obj.put("postCount", 0);
			obj.put("postTypeCount", 0);
			obj.put("postCount",0);
			obj.put("supportCount", 0);
			obj.put("hasBeenAttention", 0);
		}if(centerLive.getLiveType() != 1){
			obj.put("dmSupportCount",0);
			obj.put("dmCommentCount",0);
		}if(centerLive.getLiveType() != 7 || centerLive.getLiveType() != 8||  centerLive.getLiveType() != 9){
			obj.put("classType",0);
		}if(centerLive.getLiveType() != 3){
			obj.put("hasJoinClass",0);
			
		}if(centerLive.getLiveType() != 12 ||centerLive.getLiveType() != 14){
			obj.put("hasJoinClub", 0);
		}if(centerLive.getLiveType()  != 2 ||centerLive.getLiveType()  != 10 || centerLive.getLiveType()  !=15){
			obj.put("gameType", 0);
		}
		return obj;
	}
	/*
	 * 组装拼接组合参数
	 */
	protected static void buildCommonParameters(JSONObject obj ,String receiverIds,String receiverNames,String receiverNicknames){
		
		/*
		 * 动态主体的接收者id
		 */
		obj.put("dmReceiverId", receiverIds.substring(0, receiverIds.length()-1));
		/*
		 * 动态主体的接收者姓名
		 */
		obj.put("dmReceiverName", receiverNames.substring(0, receiverNames.length()-1));
		/*
		 *  动态主体的接收者昵称
		 */
		obj.put("dmReceiverNickname", receiverNicknames.substring(0, receiverNicknames.length()-1));
	}
}
