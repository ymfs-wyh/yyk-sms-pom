package com.yyk333.sms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * @Title: DateUtil.java
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 下午2:24:26
 * @version V1.0
 */
public class DateUtil {
    private static final int HOUR_OF_00 = 00;
    private static final int HOUR_OF_12 = 12;
    private static final int HOUR_OF_23 = 23;
    private static final int MINUTE_OF_0 = 0;
    private static final int MINUTE_OF_59 = 59;
    private static final int SECOND_OF_0 = 0;
    private static final int MILLISECOND_OF_0 = 0;
    private static final int MILLISECOND_OF_999 = 999;
    private static final int SECOND_OF_59 = 59;
    private static final int DAY_OF_1 = 1;
    private static final int DAY_OF_31 = 31;
    private static final int DAY_OF_WEEK = 7;
    private static final int LAST_MONTH_DAYS = 30;

    /** 一分钟的豪妙数    */
    public static final long MILLISECS_PER_MINUTE = 60L * 1000;
    /**一小时的豪妙数    */
    public static final long MILLISECS_PER_HOUR = 60L * MILLISECS_PER_MINUTE;
    /** 一天的豪妙数    */
    public static final long MILLISECS_PER_DAY = 24L * MILLISECS_PER_HOUR;
    
    /**
     * 
     * dateToString(将时间转换成字符串)
     * 
     * @Title: dateToString
     * @param dataDate
     *            日期
     * @param format
     *            格式
     * @return String 返回类型
     * @throws
     */
    public static String dateToString(Date dataDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(dataDate);
    }

    /**
     * 
     * StringToDate(将字符串转换成日期)
     * 
     * @Title: StringToDate
     * @param dataDate
     *            日期
     * @param format
     *            格式
     * @return Date 返回类型
     * @throws Exception 异常
     */
    public static Date stringToDate(String dataDate, String format) throws Exception{
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.parse(dataDate);
        return date;
    }

    /**
     * 将日期的时间部分转换为 00:00:00
     * 
     * @param date
     *            要格式化的日期
     * @return 格式化后的日期
     */
    public static Date dateToStartTime(Date date) {
        return DateUtil.dateToDateTime(date, HOUR_OF_00, MINUTE_OF_0, SECOND_OF_0,
                MILLISECOND_OF_0);
    }
    
    
    /**
      * dateCutSecond(将日期的秒数去掉)
      *
      * @Title: dateCutSecond
      * @param date 日期
      * @return
      * @return Date    去除秒以后的日期
      */
    public static Date dateCutSecond(Date date){
        return dateCutSecond(date, SECOND_OF_0,  MILLISECOND_OF_0);
    }
      
    private static Date dateCutSecond(Date date,int second, int milliSecond){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliSecond);
        return c.getTime();
    }
    
    /**
     * 将日期的时间部分转换为 12:00:00
     * 
     * @param date
     *            要格式化的日期
     * @return 格式化后的日期
     */
    public static Date dateToDateTime(Date date) {
        return DateUtil.dateToDateTime(date, HOUR_OF_12, MINUTE_OF_0, SECOND_OF_0,
                MILLISECOND_OF_0);
    }

    /**
     * 将日期的时间部分转换为某个时间
     * 
     * @param date
     *            要格式化的日期
     * @param hour
     *            初始化的时
     * @param minute
     *            初始化的分
     * @param second
     *            初始化的秒
     * @param milliSecond
     *            初始化的毫秒
     * @return 格式化后的日期
     */
    public static Date dateToDateTime(Date date, int hour, 
            int minute, int second, int milliSecond) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milliSecond);
        return c.getTime();
    }

    /**
     * 将日期的时间部分转换为结束时间
     * 
     * @param date
     *            要格式化的日期
     * @return 格式化后的日期
     */
    public static Date dateToEndTime(Date date) {
        return DateUtil.dateToDateTime(date, HOUR_OF_23, MINUTE_OF_59, SECOND_OF_59,
                MILLISECOND_OF_999);
    }

    /**
     * getWeekOfYear(取得当前日期是多少周)
     * 
     * @Title: getWeekOfYear
     * @param date
     *            参数
     * @return int 返回类型
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setMinimalDaysInFirstWeek(DAY_OF_WEEK);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * getMaxWeekNumOfYear(得到某一年周的总数 )
     * 
     * @Title: getMaxWeekNumOfYear
     * @param year
     *            年份
     * @return int 返回类型
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, Calendar.DECEMBER, DAY_OF_31, HOUR_OF_23, MINUTE_OF_59, SECOND_OF_59);
        return getWeekOfYear(c.getTime());
    }

    /**
     * getLastDayOfWeek(得到某年某周的第一天)
     * 
     * @Title: getLastDayOfWeek
     * @param year
     *            年份
     * @param week
     *            某一周
     * @return Date 返回类型
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, DAY_OF_1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * DAY_OF_WEEK);
        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * getLastDayOfWeek(得到某年某周的最后一天)
     * 
     * @Title: getLastDayOfWeek
     * @param year
     *            年份
     * @param week
     *            某一周
     * @return Date 返回类型
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, DAY_OF_1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * DAY_OF_WEEK);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * getFirstDayOfWeek(取得当前日期所在周的第一天)
     * 
     * @Title: getFirstDayOfWeek
     * @param date
     *            日期
     * @return Date 返回类型
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * getLastDayOfWeek(取得当前日期所在周的最后一天)
     * 
     * @Title: getLastDayOfWeek
     * @param date
     *            日期
     * @return Date 返回类型
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }
    
    /**
      * 返回传入日期当月的第一天(忽略时间部分)
      * @param date date
      * @return 期望的日期
      */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    
    /**
      * 返回传入日期当月的最后一天(忽略时间部分)
      * @param date date
      * @return 期望的日期
      */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
    /**
     * getMintueOfOffset(取得当前日期前(后)几分钟的日期)
     * 
     * @Title: getMintueOfOffset
     * @param date
     *            日期
     * @param offset
     *            偏移量
     * @return Date 返回类型
     */
    public static Date getMintueOfOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + offset);
        return c.getTime();
    }
    
    /**
     * getHourOfOffset(取得当前日期前(后)几小时的日期)
     * 
     * @Title: getHourOfOffset
     * @param date
     *            日期
     * @param offset
     *            偏移量
     * @return Date 返回类型
     */
    public static Date getHourOfOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + offset);
        return c.getTime();
    }
    
    /**
     * getSecondOfOffset(取得当前日期前(后)几秒的日期)
     * 
     * @Title: getSecondOfOffset
     * @param date
     *            日期
     * @param offset
     *            偏移量
     * @return Date 返回类型
     */
    public static Date getSecondOfOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + offset);
        return c.getTime();
    }
    
    /**
     * getDayOfBefore(取得当前日期前(后)几天的日期)
     * 
     * @Title: getDayOfBefore
     * @param date
     *            日期
     * @param offset
     *            偏移量
     * @return Date 返回类型
     */
    public static Date getDayOfOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + offset);
        return c.getTime();
    }
    /**
     * 
      * getMonthOfOffset(取得当前日期前(后)几个月的日期)
      *
      * @Title: getMonthOfOffset
      * @param date 日期
      * @param offset 偏移量
      * @return Date    返回类型
     */
    public static Date getMonthOfOffset(Date date,int offset){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + offset);
        return c.getTime(); 
    }

    /**
     * 获取日期的年份
     * 
     * @param date
     *            日期
     * @return int 年份
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    
    /**
     * 获取日期的月份
     * 
     * @param date
     *            日期
     * @return int 月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH)+1;
    }
    
    /**
     * 获取日期的日期
     * 
     * @param date
     *            日期
     * @return int 日期
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取时间的小时
     * 
     * @param date
     *            日期
     * @return int 日期
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * 获取时间的小时
     * 
     * @param date 日期
     * @return int 分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }
    
    /**
     * 获取时间字符串。 日期字符串格式： yyyy-MM-dd hh:mm:ss 其中： yyyy 表示4位年。 MM 表示2位月。 dd 表示2位日。
     * 
     * @return String " yyyy-MM-dd hh:mm:ss"格式的时间字符串。
     */
    public static String getSysDate() {
        return dateToString(new Date(),"yyyy-MM-dd hh:mm:ss");
    }
    
    /**
     * 获取时间字符串-到微秒。 日期字符串格式：yyyyMMddHHmmssSSS 其中： yyyy 表示4位年。 MM 表示2位月。 dd 表示2位日。
     * 
     * @return String " yyyyMMddHHmmssSSS"格式的时间字符串。
     */
    public static String getSysMicrosecondsDate() {
        return dateToString(new Date(),"yyyyMMddHHmmssSSS");
    }

    /**
     * 获取日期字符串。 日期字符串格式： yyyyMMdd 其中： yyyy 表示4位年。 MM 表示2位月。 dd 表示2位日。
     * 
     * @param date
     *            需要转化的日期。
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    public static String getDate(Date date) {
        return dateToString(date,"yyyy-MM-dd");
    }

    /**
     * 获取日期字符串。 日期字符串格式： yyyyMMdd 其中： yyyy 表示4位年。 MM 表示2位月。 dd 表示2位日。
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    public static String getSysDate24() {
        return dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取日期字符串。 日期字符串格式： yyyyMM 其中： yyyy 表示4位年。 MM 表示2位月。
     * 
     * @return String "yyyyMM"格式的日期字符串。
     */
    public static String getSysDateYYYYMM() {
        return dateToString(new Date(),"yyyyMM");
    }

    /**
     * 获取日期字符串。 日期字符串格式： yyyyMMdd 其中： yyyy 表示4位年。 MM 表示2位月。 dd 表示2位日。
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    public static String getSysDateYYYYMMDD() {
        return dateToString(new Date(),"yyyyMMdd");
    }
    
    /**
     * 比较两个时间大小(不比较毫秒)
     * 
     * @param timeOne 时间1
     * @param timeTwo 时间2
     * @return int 1表示大于，0表示等于，-1表示小于
     */
    public static int compareTime(Date timeOne,Date timeTwo) {
        long t1=DateUtils.getFragmentInSeconds(timeOne, Calendar.DATE);
        long t2=DateUtils.getFragmentInSeconds(timeTwo, Calendar.DATE);
        return t1 > t2 ? 1 : t1 < t2 ? -1 : 0;
    }
    
    /**
     * 
      * getWeek(获取日期是星期几)
      *
      * @Title: getWeek
      * @param date 时间
      * @return int    返回类型
      * @throws
     */
    public static int getWeek(Date date){
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }
    
    /**
     * 
      * getStartTimeOfYear(获取某年的第一时刻)
      *
      * @Title: getStartTimeOfYear
      * @param date dateYear
      * @return Date    返回类型
      * @throws
     */
    public static Date getStartTimeOfYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, DAY_OF_1);
        c.set(Calendar.HOUR_OF_DAY, HOUR_OF_00);
        c.set(Calendar.MINUTE, MINUTE_OF_0);
        c.set(Calendar.SECOND, SECOND_OF_0);
        c.set(Calendar.MILLISECOND, MILLISECOND_OF_0);
        return c.getTime();
    }
    
    /**
     * 
      * getLastTimeOfYear(获取某年的最后一个时刻)
      *
      * @Title: getLastTimeOfYear
      * @param date dateyear
      * @return    设定文件
     */
    public static Date getLastTimeOfYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DAY_OF_MONTH, DAY_OF_31);
        c.set(Calendar.HOUR_OF_DAY, HOUR_OF_23);
        c.set(Calendar.MINUTE, MINUTE_OF_59);
        c.set(Calendar.SECOND, SECOND_OF_59);
        c.set(Calendar.MILLISECOND, MILLISECOND_OF_999);
        return c.getTime();
    }

    /**
      * @Title: is30DayBefore
      * @Description: 判断传入日期是否比系统时间大30天
      * @param date 传入日期
      * @return boolean    返回类型
      */
    public static boolean is30DayBefore(Date date) {
        long sub = System.currentTimeMillis() - date.getTime();
        return sub >= LAST_MONTH_DAYS * MILLISECS_PER_DAY;
    }
    
    /**
     * 获取时间戳
     * @return String 时间戳
     */
    public static String getTimeStamp(){
    	return Long.toString(new Date().getTime()/1000);
    }
    
    /**
     * java时间转php时间毫秒数
     * @param timeMillis Long
     * @return Long 10位数的时间毫秒
     */
    public static Long convertDateToPHP(Long timeMillis){
    	String curTimeStr = timeMillis.toString();
    	return Long.valueOf(curTimeStr.substring(0, curTimeStr.length()-3));
    }
    
    /**
     * getAddTimeZoneDate(根据时区获取php时间 加时区PhpToJava)
     * @param timeMillis Long
     * @return Long 10位数的时间毫秒
     */
    public static Date getAddTimeZoneDate(Long phpTime,int timeZone){
    	return DateUtil.getHourOfOffset(DateUtil.convertPHPDateToJava(phpTime),timeZone);
    }
    
    /**
     * getSubTimeZoneDateToPhp(根据时区获取php时间 减时区javaToPhp)
     * @param timeMillis Long
     * @return Long 10位数的时间毫秒
     */
    public static Long getSubTimeZoneDateToPhp(Date date,int timeZone){
    	return DateUtil.convertDateToPHP(DateUtil.getHourOfOffset(date,-timeZone).getTime());
    }
    
    /**
     * php时间毫秒数转java时间
     * @param timeMillis Long
     * @return Long 10位数的时间毫秒
     */
    public static Date convertPHPDateToJava(Long timeMillis){
    	String curTimeStr = timeMillis.toString()+"000";
    	return new Date(Long.valueOf(curTimeStr));
    }
    
    /**
     * 
      * calcSecondCountDown(计算出订单还剩下多少时间失效)
      *
      * @Title: calcSecondCountDown
      * @param second 订单失效秒数  单位为秒
      * @param timeZone 相差时区
      * @param payTimePHP 订单的支付时间 数据库字段
      * @return Long 剩下多少秒数
      * @throws
     */
    public static Long calcSecondCountDown(Integer second,Integer timeZone,Long payTimePHP){
    	Long curTime = System.currentTimeMillis();
    	Date phpToJavaTimePayTime = convertPHPDateToJava(payTimePHP);
		return (second-(curTime-getHourOfOffset(phpToJavaTimePayTime,timeZone).getTime())/1000);
    }
    
    /**
     * get Calendar of given year
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);      
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    /**
     * get start date of given week no of a year
     * 根据年号和第几周得到开始时间
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * get the end day of given week no of a year.
     * 根据年号和第几周得到结束时间
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);    
    }
    
    /**
     * 
     * 获取前面几天排除周六周日
     * @param date Date
     * @param num int
     * @return Date Date
     */
    public static Date addWorkDay(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mod = num % 5;
		int other = num / 5 * 7;
		for (int i = 0; i < mod;) {
			cal.add(Calendar.DATE, 1);
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
			case Calendar.SATURDAY:
				break;
			default:
				i++;
				break;
			}
		}
		if (other > 0){			
			cal.add(Calendar.DATE, other);
		}
		return cal.getTime();
	}
    
    // 获得上周星期一的日期
    public static Date getPreviousMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * -1);
//        Date monday = ;
//        DateFormat df = DateFormat.getDateInstance();
//        String preMonday = df.format(monday);
        return currentDate.getTime();
    }
    
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }
    
    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }
    
    /**  
     * 获取当前时间距离指定时间相差毫秒数
     * @param hour 初始化的时
     * @param minute 初始化的分
     * @param second 初始化的秒
     * @param milliSecond 初始化的毫秒
     * @return 相差毫秒数
     * @throws ParseException  
     */    
    public static long getMillisecondByNowToCustomDate(int hour, 
            int minute, int second, int milliSecond)    
    {    
        Date now = new Date();
        Date eightClock = DateUtil.dateToDateTime(now, hour, minute, second, milliSecond);
        Long millisecond = eightClock.getTime()-System.currentTimeMillis();
        millisecond = millisecond<0?2000l:millisecond;
        return millisecond;
    }
    
    /**
     * 
      * isSaleCanUpdate(判断商家是否可以修改折扣)
      *
      * @Title: isSaleCanUpdate
      * @param saleUpdateDate
      * @return boolean    返回类型
      * @throws Exception    设定文件
     */
    public static final boolean isCanUpdateSale(String saleUpdateDate) throws Exception{
        boolean isCanUpdateSale = true;
        if(StringUtils.isNotBlank(saleUpdateDate)){
            int month = DateUtil.getMonth(DateUtil.stringToDate(saleUpdateDate, "yyyy-MM-dd"));
            int curMonth = DateUtil.getMonth(new Date());
            if(month==curMonth){
                isCanUpdateSale=false;
            }
        }
        return isCanUpdateSale;
    }
    
    /**
     * main(这里用一句话描述这个方法的作用)
     * 
     * @Title: main
     * @param args
     *            参数
     */
    public static void main(String[] args) throws Exception{
//        Date date = DateUtil.stringToDate("2013-03-12", "yyyy-MM-dd");
//        Date date1 = DateUtil.dateToDateTime(date);
//        Date date2 = DateUtil.dateToDateTime(new Date());
        
       // Date date3 = DateUtil.stringToDate("2013-03-12 12:02:02", "yyyy-MM-dd HH:mm:ss");
       // Date date4 = DateUtil.stringToDate("2055-03-13 12:02:00", "yyyy-MM-dd HH:mm:ss");
       // int c=DateUtil.compareTime(date3,date4);
        //assert c==1;   //-enableassertions
//        String dateStr = DateUtil.dateToString(new Date(), "HH:mm:ss");
//        int m = DateUtil.getMonth(new Date());
//        int d = DateUtil.getDay(new Date());
//        int h = DateUtil.getHour(new Date());
//        int mm = DateUtil.getMinute(new Date());
    	/*Long s = System.currentTimeMillis();
    	Long a = convertDateToPHP(System.currentTimeMillis());
    	System.out.println(a);*/
//    	System.out.println(DateUtil.dateToString(convertPHPDateToJava(1431669227l), "yyyy-MM-dd HH:mm:ss"));
//    	System.out.println(DateUtil.convertDateToPHP(DateUtil.stringToDate("2015-6-17 13:04:00", "yyyy-MM-dd HH:mm:ss").getTime()));
//    	
//    	System.out.println("getWeekOfYear="+getWeekOfYear(DateUtil.stringToDate("2015-5-18 8:10:00", "yyyy-MM-dd HH:mm:ss")));
//    	System.out.println("getStartDayOfWeekNo="+getStartDayOfWeekNo(2015, 21));
//    	System.out.println("getEndDayOfWeekNo="+getEndDayOfWeekNo(2015, 21));
//    	System.out.println(getPreviousMonday());
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	Date d1=sdf.parse("2012-09-08 10:10:10");  
        Date d2=sdf.parse("2012-09-15 10:10:9");  
        System.out.println(daysBetween(d1,d2));  
    }
}
