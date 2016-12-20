package com.seentao.stpedu.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class SystemConfig extends PropertyPlaceholderConfigurer {

	private static Map<String, Object> ctxPropertiesMap;
//	private static Properties props = null;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}
	
//	static {
//		InputStream in = SystemConfig.class.getClassLoader().getResourceAsStream("config/rabbitmq.properties");
//		props = new Properties();
//		try {
//			props.load(in);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 取得配置文件中的String
	 * @param keys
	 * @return
	 */
	public static String getString(String key){
		String val = (String) ctxPropertiesMap.get(key);
		if(val != null && val.trim().equals("")){
			return null;
		}
		return val;
	}
	
	/**
	 * 取得配置文件中的Integer
	 * @param keys
	 * @return
	 */
	public static Integer getInteger(String key){
		String val = (String) ctxPropertiesMap.get(key);
		if(val == null || val.trim().equals("")){
			return null;
		}
		return Integer.valueOf(val);
	}
	
	/**
	 * 取得配置文件中的Boolean
	 * @param keys
	 * @return
	 */
	public static Boolean getBoolean(String key){
		String val = (String) ctxPropertiesMap.get(key);
		if(val != null && !val.trim().equals("")){
			return Boolean.valueOf(val);
		}
		return false;
	}
}
