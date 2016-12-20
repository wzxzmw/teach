package com.seentao.stpedu.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
	
	/** 
	* @Fields startTime : 起始时间戳
	*/ 
	private static long startTime = -1;
	/** 
	* @Fields endTime : 结束时间戳
	*/ 
	private static long endTime = -1;
	/** 
	* 获取当前时间戳（单位：秒）
	* @author W.jx
	* @date 2016年6月12日 下午4:50:04 
	* @return 当前的秒
	* @throws 
	*/
	public static int getCurrentTimestamp(){
		int seconds = -1;
		seconds = (int) (System.currentTimeMillis() / 1000);
		return seconds;
	}
	
	public static long getCurrentTimes(){
		return System.currentTimeMillis() / 1000;
	}
	/** 
	* 获取两个时间之间相差多少天,多少小时,多少分钟,多少秒
	* @author yy
	* @param startTime 开始时间
	* @param endTime  结束时间
	*/
	public static Map<String,Long> getTimeDifference(Integer startTime,Integer endTime){
		Map<String,Long> resMap = new HashMap<String,Long>();
		String endTimeStr = String.valueOf(endTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String end = sdf.format(new Date(Long.valueOf(endTimeStr+"000")));  
		String startTimeStr = String.valueOf(startTime);
        String start = sdf.format(new Date(Long.valueOf(startTimeStr+"000")));  
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数long
		//获得两个时间的毫秒时间差异
		long diff = 0;
		try {
			diff = sdf.parse(end).getTime() - sdf.parse(start).getTime();
		} catch (ParseException e) {
			LogUtil.error(TimeUtil.class, "getTimeDifference", "时间差计算异常", e);
		}
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
		resMap.put("day", day);
		resMap.put("hour", hour);
		resMap.put("min", min);
		resMap.put("sec", sec);
		return resMap;
	}
	
	/** 
	* @Title: initTime 
	* @Description: 初始化时间戳
	* @param timeType  时间类型(1:今天；2:近三天；3:本周；4:本月；)
	* @return void
	* @author liulin
	* @date 2016年6月30日 上午9:16:17
	*/
	public static void initTime(int timeType){
		try{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//设置星期一为一周的开始
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			if(timeType == 1){
				startTime = timeToTimestamp(format.format(new Date())+" 00:00:00");
				endTime = timeToTimestamp(format.format(new Date())+" 23:59:59");
			}else if(timeType == 2){
				//当前时间往前推三天
				cal.add(Calendar.DAY_OF_MONTH, -3);
				startTime =cal.getTimeInMillis();
				endTime = System.currentTimeMillis();
			}else if(timeType == 3){
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				startTime = timeToTimestamp(format.format(cal.getTime())+" 00:00:00");
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				endTime = timeToTimestamp(format.format(cal.getTime())+" 23:59:59");
			}else if(timeType == 4){
				cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
				startTime = timeToTimestamp(format.format(cal.getTime())+" 00:00:00");
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
				endTime = timeToTimestamp(format.format(cal.getTime())+" 23:59:59");
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static long timeToTimestamp(String dateTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = -1;
		try{
			time = sdf.parse(dateTime).getTime();
		}catch(Exception e){
			e.printStackTrace();
		}
		return time;
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String getFormatTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		return sdf.format(new Date());
	}
	
	public static long getStartTime() {
		return startTime;
	}

	public static void setStartTime(long startTime) {
		TimeUtil.startTime = startTime;
	}

	public static long getEndTime() {
		return endTime;
	}

	public static void setEndTime(long endTime) {
		TimeUtil.endTime = endTime;
	}
	
	/** 
	* 格式化时间戳
	* yyyy-MM-dd HH:mm:ss
	*/
	public static String formatDate(Integer date){
		String timeStr = String.valueOf(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String end = sdf.format(new Date(Long.valueOf(timeStr+"000")));  
		return end;
	}
	
	/**
	 * 根据年月获取当月最大的一天(每月多少天)
	 * @param year 年
	 * @param month 月
	 * @return maxDay 每月多少天
	 */
	public static int getMaxDayByYearMonth(int year,int month) {
		int maxDay = 0;
		int day = 1;
		/**
		 * 与其他语言环境敏感类一样，Calendar 提供了一个类方法 getInstance，
		 * 以获得此类型的一个通用的对象。Calendar 的 getInstance 方法返回一
		 * 个 Calendar 对象，其日历字段已由当前日期和时间初始化： 
		 */
        Calendar calendar = Calendar.getInstance();
        /**
         * 实例化日历各个字段,这里的day为实例化使用
         */
        calendar.set(year,month - 1,day);
        /**
         * Calendar.Date:表示一个月中的某天
         * calendar.getActualMaximum(int field):返回指定日历字段可能拥有的最大值
         */
        maxDay = calendar.getActualMaximum(Calendar.DATE);
		return maxDay;
	}
	
	/**
	 * 获取指定时间的时间戳(时间类型为yyyyMMdd)
	 * @return
	 */
	public static long getSpecifiedTimeStamp(String time) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		long timestamp = 0l;
		try {
			Date date = df.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			timestamp = cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp/1000;
	}
	/* 获取当天的起始时间
	 * toDayStartTime
	 */
	public static long getToDayStartTime(){
		return getToDayTime(" 00:00:00");
		
	}
	/* 获取当天的最后时间
	 * toDayEndTime
	 */
	public static  long getToDayEndTime(){
		return getToDayTime(" 23:59:59");
	}
	
	protected static long getToDayTime(String dayTime){
		long time = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dates = sdf.format(time)+ dayTime;
		long result =0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result= df.parse(dates).getTime();
		} catch (ParseException e) {
			LogUtil.error(TimeUtil.class, "getToDayTime", "Parse time is error");
		}
		return result/1000;
	}
	public static Integer getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		long timeMillis = System.currentTimeMillis();
		Date date = new Date(timeMillis);
		return Integer.valueOf(sdf.format(date));
	}
	
}
