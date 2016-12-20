package com.seentao.stpedu.redpacket.controller;

/** 
* @author yy
* @date 2016年7月5日 下午9:40:54 
*/
public interface IRedPacketController {

	/**
	 * 发红包
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param redPacketContent 红包内容
	 * @param redPacketObject 赠送对象(1:全部会员；2:指定的人；3:俱乐部；)
	 * @param redPacketUserIds 赠送人员id 多个id以逗号分隔
	 * @param redPacketType 红包类型 1:一级货币；2:鲜花；
	 * @param perRedPacketCount 每人赠送的数量
	 */
	String submitRedPacket(Integer userType, String userName, String userId, String userToken, Integer redPacketObject,
			Integer redPacketType, Integer perRedPacketCount, String clubId, String redPacketContent,
			String redPacketUserIds);

	/**
	 * 获取红包类型信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param inquireType 查询类型
	 */
	String getRedPacketType(Integer userType, String userName, String userId, String userToken, Integer inquireType);

	/**
	 * 获取红包信息
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param redPacketShowType 红包显示分类(1：收到的红包；2:我发的红包；3:大家收到的红包排名；)
	 * @param start 开始
	 * @param limit 结束
	 */
	String getRedPackets(Integer userType, String userName, String userId, String userToken, String clubId,
			Integer redPacketShowType, Integer start, Integer limit);

}
