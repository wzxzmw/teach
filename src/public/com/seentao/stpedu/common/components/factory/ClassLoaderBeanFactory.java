package com.seentao.stpedu.common.components.factory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

/**
 * 类加载器
 * @author 	lw
 * @date	2016年6月19日  下午3:11:00
 *
 */
@Component
public class ClassLoaderBeanFactory implements ApplicationContextAware{

	//spring上下文
	private static ApplicationContext ctx;
	
	/** 缓存系统中所有通过反射获取的method 方法 */
	private static final Map<String, Method> CLASS_METHOD = new HashMap<String, Method>();
	/** 反射方法参数 Map.class */
	public static final Class<?>[] CLASS_ARR = {Map.class};
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ClassLoaderBeanFactory.ctx = applicationContext;
		
	}

	
	
	/**
	 * 反射类加载获取bean
	 * @param t			
	 * @return
	 * @author 			lw
	 * @date			2016年6月19日  下午3:22:24
	 */
	public static <T> T getBean(Class<T> t){
		try {
			return ctx.getBean(t);
		} catch (BeansException e) {
			System.out.println("类加载器加载错误类！");
			LogUtil.error("com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory", "executiveMethod", AppErrorCode.ERROR_REFLEX, e);
			return null;
		}
	}


	/**
	 * 获取对象方法
	 * @param t					对象
	 * @param methodName		方法名
	 * @param parameterTypes	参数数组
	 * @return
	 * @author 					lw
	 * @date					2016年6月20日  上午11:54:23
	 */
	public static  Method getClassMethod(Class<?> t, String methodName, Class<?>... parameterTypes){
		
		//获取对象静态锁：提高性能
		synchronized(t){
			
			String key = t.getName() + ":" + methodName + ":" + classArrToString(parameterTypes);
			if(CLASS_METHOD.containsKey(key)){
				return CLASS_METHOD.get(key);
			}
			try {
				Method declaredMethod = t.getDeclaredMethod(methodName, parameterTypes);
				CLASS_METHOD.put(key, declaredMethod);
				return declaredMethod;
			} catch (Exception e) {
				System.out.println("未实现 " + t.getName() + " 类 " + methodName + " ( " + classArrToString(parameterTypes) + " ) 方法");
				e.printStackTrace();
				LogUtil.error("com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory", "executiveMethod", AppErrorCode.ERROR_REFLEX, e);
				return null;
			}
		}
		
	}
	
	
	
	
	
	/**
	 * class 类名称转换成字符串名称
	 * @param parameterTypes	参数
	 * @return
	 * @author 					lw
	 * @date					2016年6月20日  上午11:54:14
	 */
	private static String classArrToString(Class<?>... parameterTypes){
		StringBuffer sb = new StringBuffer();
		for(Class<?> t : parameterTypes){
			sb.append(t.getSimpleName()).append(",");
		}
		String str = sb.toString();
		return str.substring(0, str.length()-1);
	}



	/**
	 * 反射执行某个方法
	 * @param beanClass			执行对象class
	 * @param methodName		执行对象class 方法名
	 * @param paramClassArr		执行对象class 方法参数
	 * @param paramObject		执行对象class 方法传入参数
	 * @return
	 * @author 					lw
	 * @date					2016年7月5日  下午8:39:32
	 */
	public static synchronized Object executiveMethod(Object bean, Class<?> beanClass, String methodName, Class<?>[] paramClassArr, Object... paramObject) {
		if(paramClassArr != null && paramClassArr.length > 0 && paramClassArr.length == paramObject.length){
			try {
				return getClassMethod(beanClass, methodName, paramClassArr).invoke(bean, paramObject);
			} catch (Exception e) {
				LogUtil.error("com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory", "executiveMethod", AppErrorCode.ERROR_REFLEX, e);
			}
		}
		return null;
	}


	
}
