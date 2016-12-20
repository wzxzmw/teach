package com.seentao.stpedu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengshx
 */
public class LogUtil {

	private static Logger logger = LoggerFactory.getLogger(LogUtil.class);
	
	/**
	 * @author chengshx
	 * @return
	 */
	public static Logger getLogger(){
		return logger;
	}
	
	/**
	 * 提示日志
	 * @author chengshx
	 * @param clazz 日志输出调用类
	 * @param methodName 日志输出调用方法名
	 * @param msg 日志输出信息描述
	 */
	public static void info(Class clazz, String methodName, String msg){
		logger.info(clazz.getName() + "-->" + methodName +  "-->" + msg);
	}
	
	/**
	 * 错误日志
	 * @author chengshx
	 * @param clazz 日志输出调用类
	 * @param methodName 日志输出调用方法名
	 * @param msg 日志输出信息描述
	 */
	public static void error(Class clazz, String methodName, String msg){
		logger.error(clazz.getName() + "-->" + methodName +  "-->" + msg);
	}

	/**
	 * 错误日志抛出异常
	 * @author chengshx
	 * @param clazz 日志输出调用类
	 * @param methodName 日志输出调用方法名
	 * @param msg 日志输出信息描述
	 * @param e 抛出异常
	 */
	public static void error(Class clazz, String methodName, String msg, Throwable e){
		logger.error(clazz.getName() + "-->" + methodName +  "-->" + msg, e);
	}
	
	/**
	 * 游戏错误日志
	 * @param message
	 * @param args
	 */
	public static void error(String message,Object... args){
		logger.error(message,args);
	}
	
}
