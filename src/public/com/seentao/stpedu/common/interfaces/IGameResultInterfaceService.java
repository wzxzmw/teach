package com.seentao.stpedu.common.interfaces;

import com.alibaba.fastjson.JSONObject;

public interface IGameResultInterfaceService {

	String getGameResult(String gameId, String teamId, Integer year, Integer reportType);

	/**
	 * 报表审核：利润表
	 * @param gameId
	 * @param teamId
	 * @param year
	 * @param isParamsNormal
	 * @return
	 */
	JSONObject getGameResultProfit(Integer gameId, Integer teamId, Integer year, int isParamsNormal);

	/**
	 * 报表审核：费用表
	 * @param gameId
	 * @param teamId
	 * @param year
	 * @param isParamsNormal
	 * @return
	 */
	JSONObject getGameResultCost(Integer gameId, Integer teamId, Integer year, int isParamsNormal);

	/**
	 * 报表审核：资产负债表
	 * @param gameId
	 * @param teamId
	 * @param year
	 * @param isParamsNormal
	 * @return
	 */
	JSONObject getGameResultBalanceSheet(Integer gameId, Integer teamId, Integer year, int isParamsNormal);

	/**
	 * 报表审核：费用表
	 * @param gameId
	 * @param teamId
	 * @param year
	 * @param isParamsNormal
	 * @return
	 */
	JSONObject getGameResultSalesStatistics(Integer gameId, Integer teamId, Integer year, int isParamsNormal);

}