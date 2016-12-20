package com.seentao.stpedu.utils;

import java.math.BigDecimal;

/**
 * @ClassName: DoubleUtil
 * @Description: 双浮点的工具类
 * @author W.jx
 * @date 2016年6月1日 上午10:35:27
 * 
 */
public class DoubleUtil {

	/**  
	* 保留2位小数 
	* @param result 要处理的数据
	* @return   处理好的数据（只保留2位小数--四舍五入）
	* @author W.jx
	* @date 2016年6月1日 上午10:42:54 
	*/
	public static Double formatDouble(Double result) {
		BigDecimal bg = new BigDecimal(result);  
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	
	
}
