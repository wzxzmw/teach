package com.seentao.stpedu.common.components;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

/**
 * 分页组件
 * @author 	lw
 * @date	2016年6月19日  下午2:31:06
 *
 */
@Component
public class QueryPageComponent{
	
	

	/**
	 * 默认 分页组件查询
	 * 	每个Dao只能实现一种查询
	 * 	执行过程
	 * 	1.校验传入参数 limit 、 start 、 t 不为空
	 * 	2.通过类加载获取 t 类型的 Dao
	 * 	4.dao类 queryCount 方法 和 queryByPage 方法 做分页
	 * 	5.分页查询sql 在 queryCount 和 queryByPage xml中实现
	 * 
	 * @param limit		每页显示记录数量
	 * @param start		查询页
	 * @param paramMap	queryCount 和 queryByPage 实现方法传入参数
	 * @param t			查询某个t类型的 xml 和  Dao
	 * @return
	 * @author 			lw
	 * @date			2016年6月20日  上午9:18:05
	 */
	public static <T> QueryPage<T> queryPage(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t) {
		return queryPageExecute( limit,  start,  paramMap, t, null ,  null);
	}
	
	
	
	/**
	 * 加入redis分页
	 * 1.查询所有数据的count
	 * 2.查询数据库分页，获取分页数据id
	 * 3.根据分页数据id查询redis获取redis数据
	 * 4.对比redis数据，并把对比正切的id redis数据保存到返回数据集合
	 * 5.如果缺少数据库id，把这些ids查询数据库，并加入返回数据集合
	 * 
	 * 
	 * 
	 * @param limit						分页 每页显示数据
	 * @param start						分页查询起始数据
	 * @param paramMap					查询参数集合
	 * @param t							返回参数泛型
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  下午4:16:54
	 */
	public static <T> QueryPage<T> queryPageByRedisSimple(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t){
		return queryPageByRedisExecute(limit, start, paramMap, t, null, null, null);
	}
	
	
	
	/**
	 * 加入redis分页
	 * 1.查询所有数据的count
	 * 2.查询数据库分页，获取分页数据id
	 * 3.根据分页数据id查询redis获取redis数据
	 * 4.对比redis数据，并把对比正切的id redis数据保存到返回数据集合
	 * 5.如果缺少数据库id，把这些ids查询数据库，并加入返回数据集合
	 * 
	 * 方法名：queryCount 			获取总 页数
	 * 		queryByPageids		获取当前页所有数据的id
	 * 		queryByPage			如果redis中没有数据，使用该方法获取数据，并保存到redis
	 * 
	 * 		当初在设计该方法使用到反射，原来是定义了约定接口.使用本方法需要实现指定类Dao中某些方法。但是遭到强烈反对。
	 * 		但是思前想后还是觉得不能这样，愿后来的维护者能够把该功能实现。
	 * 
	 * @param limit						分页 每页显示数据
	 * @param start						分页查询起始数据
	 * @param paramMap					查询参数集合
	 * @param t							返回参数泛型
	 * @param queryCountMethodName		查询count方法名
	 * @param queryByPageMethodName		查询page对象方法名
	 * @param queryByPageIdsMethodName	查询page ids 方法名
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  下午2:44:28
	 */
	@SuppressWarnings("unchecked")
	public static <T> QueryPage<T> queryPageByRedisExecute(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t,String queryCountMethodName , String queryByPageIdsMethodName, String queryByPageMethodName){
		QueryPage<T> page = new QueryPage<T>(limit,start,paramMap,t);
		
		//	1. 校验参数合法性
		if(page != null && page.isJumpOut()){
			page = new QueryPage<T>();
			page.error("参数格式不正确！");
			return page;
		}
		
		try {
			/*
			 * 	使用反射的方式调用方法查询说明： 反射方法灵活主要考虑如下拓展：	
			 * 			a.灵活的方法名使用：可能一个对象有多个分页查询。
			 * 			b.灵活的方法名使用：可能需要个性化分页查询。
			 */
			
			// 2. 获取指定包下的dao类
			Class<?> forName = Class.forName(GameConstants.QUERYPAGE_PPATH_PACKAGE + page.getClazzName() + "Dao");
				
			Object bean = ClassLoaderBeanFactory.getBean(forName);
			
			/*
			 * 	3. 执行方法
			 * 		3.1 默认方法名GameConstants.QUERYPAGE_QUERYCOUNT
			 * 		3.2传入方法名queryCountMethodName
			 * 		3.3 方法参数ClassLoaderBeanFactory.CLASS_ARR
			 * 		3.4 传入参数page.getParaMap()
			 */
			Object invoke = ClassLoaderBeanFactory.executiveMethod(bean,forName, queryCountMethodName == null ? GameConstants.QUERYPAGE_QUERYCOUNT : queryCountMethodName, ClassLoaderBeanFactory.CLASS_ARR, page.getParaMap());
			
			//	4. count 数据
			page.setCount(invoke == null ? 0 : Integer.valueOf(invoke.toString()));
			
			//	5. 校验count数据：是否要继续调用查询分页ids
			if(page.getCount() > 0 ){
				
				/*
				 * 	6.1 查询需要分页的ids
				 * 		说明：这里的ids是所有所有需要查询的redis的ids也是需要查询实体的所有的ids
				 * 			在这里需要全部的分页查询逻辑(关联逻辑)
				 * 			如果redis中数据没有或者不全，再次查询数据的实体的时候，不根据关联逻辑查询，直接使用ids查询
				 */
				invoke = ClassLoaderBeanFactory.executiveMethod(bean, forName, queryByPageIdsMethodName == null ? GameConstants.QUERYPAGE_QUERYBYPAGE_IDS : queryByPageIdsMethodName, ClassLoaderBeanFactory.CLASS_ARR, page.getInitParam());
				
				// 6.2  数据库查询分页 ids 查询对象
				List<Integer> dataBaseDB = (List<Integer>) invoke;
				
				// 7. 校验ids数据长度：是否要继续调用查询分页实体
				if(CollectionUtils.isEmpty(dataBaseDB)){
					page.success();
					return page;
				}
				
				/*
				 * 	8. 校验是否查询数据库
				 * 		8.1 根据ids查询redis中对象
				 * 		8.2 校验完成性：根据ids校验redis中的对象数据
				 * 		8.2如果数据不完成，查询数据库
				 */
				if(page.checkRedisObject(dataBaseDB,JedisCache.getBatchRedisVal(t.getSimpleName(),StringUtil.ListIntegerToStringArr(dataBaseDB) ))){
						
					//	9.dataBaseDB.size() 有值表示重新查询数据库
					if(dataBaseDB.size() > 0 ){
						
						//	10. 反射查询数据库：该方法可能被个性化、可能改dao下有多种查询方式
						invoke = ClassLoaderBeanFactory.executiveMethod(bean, forName, queryByPageMethodName == null ? GameConstants.QUERYPAGE_QUERYBYPAGE : queryByPageMethodName, ClassLoaderBeanFactory.CLASS_ARR, page.getParaMap());
						
						//	11. 保存查询结果
						if(invoke != null && !CollectionUtils.isEmpty((List<T>)invoke) ){
							List<T> list = (List<T>)invoke;
							page.setList(list);
							
							//	12.校验redis中没有的对象数据，并保存到redis中
							page.setRedisMap(list);
						}
					}
				}
			}
			
			//没有数据返回分页对象
			page.success();
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("QueryPageComponent：分页组件", "queryPageByRedisExecute：redis分页方法", "查询"+t.getSimpleName()+"类  传入方法："+queryCountMethodName + " -- "+ queryByPageIdsMethodName + " -- "+ queryByPageMethodName, e);
			page.error(AppErrorCode.QUERY_PAGE_XML_MAPPER);
			return page;
		}
	}
	
	
	
	
	/**
	 * 自定义 分页组件查询	
	 * 
	 * 		当初在设计该方法使用到反射，原来是定义了约定接口.使用本方法需要实现指定类Dao中某些方法。但是遭到强烈反对。
	 * 		但是思前想后还是觉得不能这样，愿后来的维护者能够把该功能实现。
	 * @param limit							每页显示记录数量
	 * @param start							查询页
	 * @param paramMap						queryCount 和 queryByPage 实现方法传入参数
	 * @param t
	 * @param queryCountMethodName			分页  count 		查询方法名
	 * @param queryByPageMethodName			分页  queryByPage 	实体查询方法名
	 * @return
	 * @author 								lw
	 * @date								2016年6月20日  下午5:26:25
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> QueryPage<T> queryPageExecute(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t,String queryCountMethodName , String queryByPageMethodName){
		
		QueryPage<T> page = new QueryPage(limit,start,paramMap,t);
		//如果传入参数不合法
		if(page != null && page.isJumpOut()){
			page = new QueryPage();
			page.error("参数格式不正确！");
			return page;
		}
		
		try {
			Class<?> forName = Class.forName(GameConstants.QUERYPAGE_PPATH_PACKAGE + page.getClazzName() + "Dao");
			//获取对象方法,执行queryCount方法，并保存到page.count中
			Object bean = ClassLoaderBeanFactory.getBean(forName);
			Object invoke = ClassLoaderBeanFactory.executiveMethod(bean, forName, queryCountMethodName == null ? GameConstants.QUERYPAGE_QUERYCOUNT : queryCountMethodName, ClassLoaderBeanFactory.CLASS_ARR, page.getParaMap());
			page.setCount(invoke == null ? 0 : Integer.valueOf(invoke.toString()));
			
			//查询数据超过总数的不查询
			if(page.getCount() > 0 && page.hasData()){
				invoke = ClassLoaderBeanFactory.executiveMethod(bean, forName, queryByPageMethodName == null ? GameConstants.QUERYPAGE_QUERYBYPAGE : queryByPageMethodName, ClassLoaderBeanFactory.CLASS_ARR, page.getInitParam());
				page.setList((List<T>) invoke);
				page.success();
				return page;
			}
			page.success();
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("QueryPageComponent：分页组件", "queryPageByRedisExecute：自定义分页方法", "查询"+t.getSimpleName()+"类  传入方法："+queryCountMethodName +  " -- "+ queryByPageMethodName, e);
			page.error(AppErrorCode.QUERY_PAGE_XML_MAPPER);
			return page;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 缺省的反射分页
	 * 	反射执行mybatis不会执行级联查询，如果需要级联查询请自己写QueryPage结果加入进来
	 * 
	 * 		当初在设计该方法使用到反射，原来是定义了约定接口.使用本方法需要实现指定类Dao中某些方法。但是遭到强烈反对。
	 * 		但是思前想后还是觉得不能这样，愿后来的维护者能够把该功能实现。
	 * 
	 * @param limit							每页显示记录数量
	 * @param start							查询页
	 * @param paramMap						queryCount 和 queryByPage 实现方法传入参数
	 * @param t
	 * @param queryCountMethodName			分页  count 		查询方法名
	 * @param queryByPageMethodName			分页  queryByPage 	实体查询方法名
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午6:38:44
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> QueryPage<T> queryPageDefaultExecute(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t,String queryCountMethodName){
		
		QueryPage<T> page = new QueryPage(limit,start,paramMap,t);
		//如果传入参数不合法
		if(page != null && page.isJumpOut()){
			page = new QueryPage();
			page.error("参数格式不正确！");
			return page;
		}
		
		
		try {
			Class<?> forName = Class.forName(GameConstants.QUERYPAGE_PPATH_PACKAGE + page.getClazz().getSimpleName() + "Dao");
			Object bean = ClassLoaderBeanFactory.getBean(forName);
			//获取对象方法
			Method declaredMethod = ClassLoaderBeanFactory.getClassMethod(forName,queryCountMethodName ==null ? GameConstants.QUERYPAGE_QUERYCOUNT : queryCountMethodName, Map.class);
			//执行queryCount方法，并保存到page.count中
			String string = declaredMethod.invoke(bean, page.getParaMap()).toString();
			page.setCount(string == null ? 0 : Integer.valueOf(string));
			page.success();
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("QueryPageComponent：分页组件", "queryPageByRedisExecute：缺省分页方法", "查询"+t.getSimpleName()+"类  传入方法："+queryCountMethodName , e);
			page.error(AppErrorCode.QUERY_PAGE_XML_MAPPER);
			return page;
		}
		
	}
	
	/**
	 * 简单缺省分页
	 * @param limit							每页显示记录数量
	 * @param start							查询页
	 * @param paramMap						queryCount 和 queryByPage 实现方法传入参数
	 * @param t
	 * @param t
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午6:41:39
	 */
	public static <T> QueryPage<T> queryPageSimpleDefault(Integer limit, Integer start, Map<String, Object> paramMap,Class<T> t){
		return queryPageDefaultExecute(limit, start, paramMap, t, null);
	}
	
}
