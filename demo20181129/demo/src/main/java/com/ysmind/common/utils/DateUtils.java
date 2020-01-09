package com.ysmind.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.util.Assert;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author admin
 * @version 2013-3-15
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };
	public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";

	public static final String PATTERN_DATE = "yyyy-MM-dd";
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
    
	public static Date getDateStart(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getDateEnd(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String timestamp2String(Timestamp timestamp, String pattern) {
		if (timestamp == null) {
			throw new java.lang.IllegalArgumentException("timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timestamp.getTime()));
	}

	public static String date2String(java.util.Date date, String pattern) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
			;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Timestamp currentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public static String currentTimestamp2String(String pattern) {
		return timestamp2String(currentTimestamp(), pattern);
	}

	public static Timestamp string2Timestamp(String strDateTime, String pattern) {
		if (strDateTime == null || strDateTime.equals("")) {
			throw new java.lang.IllegalArgumentException("Date Time Null Illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(strDateTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return new Timestamp(date.getTime());
	}

	public static Date string2Date(String strDate, String pattern) {
		if (strDate == null || strDate.equals("")) {
			throw new RuntimeException("str date null");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = DateUtils.PATTERN_DATE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;

		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

	public static String stringToYear(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtils.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return String.valueOf(c.get(Calendar.YEAR));
	}

	public static String stringToMonth(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtils.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.MONTH));
		int month = c.get(Calendar.MONTH);
		month = month + 1;
		if (month < 10) {
			return "0" + month;
		}
		return String.valueOf(month);
	}

	public static String stringToDay(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtils.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			return "0" + day;
		}
		return "" + day;
	}

	public static Date getFirstDayOfMonth(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = 1;
		c.set(year, month, day, 0, 0, 0);
		return c.getTime();
	}

	public static Date getLastDayOfMonth(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = 1;
		if (month > 11) {
			month = 0;
			year = year + 1;
		}
		c.set(year, month, day - 1, 0, 0, 0);
		return c.getTime();
	}

	public static String date2GregorianCalendarString(Date date) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("Date is null");
		}
		long tmp = date.getTime();
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTimeInMillis(tmp);
		try {
			XMLGregorianCalendar t_XMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(ca);
			return t_XMLGregorianCalendar.normalize().toString();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException("Date is null");
		}

	}

	public static boolean compareDate(Date firstDate, Date secondDate) {
		if (firstDate == null || secondDate == null) {
			throw new java.lang.RuntimeException();
		}

		String strFirstDate = date2String(firstDate, "yyyy-MM-dd");
		String strSecondDate = date2String(secondDate, "yyyy-MM-dd");
		if (strFirstDate.equals(strSecondDate)) {
			return true;
		}
		return false;
	}
	
	public static Date getStartTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate,"yyyy-MM-dd") + " 00:00:00";
		return string2Date(strDateTime,"yyyy-MM-dd hh:mm:ss");
	}
	
	public static Date getEndTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate,"yyyy-MM-dd") + " 59:59:59";
		return string2Date(strDateTime,"yyyy-MM-dd hh:mm:ss");
	}
	/**
	 * 把数值型日期转换为字符类型的日期
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String format(long timeMillis) {
		return format(new Date(timeMillis));
	}

	/**
	 * 把date型日期转换为字符串
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String format(Date dateTime) {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM, Locale.CHINA).format(dateTime);
	}

	/**
	 * 把数值型日期转换为字符类型的日期
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String fmt(long timeMillis) {
		return fmt(new Date(timeMillis));
	}

	/**
	 * 把date型日期转换为字符串
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String fmt(Date date) {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA)
				.format(date);
	}

	/**
	 * 把数值型日期转换为指定格式的字符类型的日期
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String format(long timeMillis, String format) {
		return format(new Date(timeMillis), format);
	}

	/**
	 * 把date型日期转换为指定格式的字符串
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 把字符串转为日期
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date parse(String dateTime) {
		try {
			return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.MEDIUM, Locale.CHINA).parse(dateTime);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 把字符串转为日期
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date parseDate(String date) {
		try {
			return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA)
					.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 把字符串转为指定格式的日期
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date parse(String aDate, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(aDate);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 判断指定的日期是否在指定的起始时间内
	 * 
	 * @param aDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean between(Date aDate, Date startDate, Date endDate) {
		if (aDate == null) {
			return false;
		}
		if (startDate != null && startDate.getTime() > aDate.getTime()) {
			return false;
		}
		if (endDate != null && endDate.getTime() < aDate.getTime()) {
			return false;
		}
		return true;
	}

	/**
	 * 为日历设置初始值
	 * 
	 * @param calendar
	 * @param h
	 * @param m
	 * @param s
	 * @return
	 */
	public static Calendar setTime(Calendar calendar, int h, int m, int s) {
		if (calendar != null) {
			calendar.set(Calendar.HOUR_OF_DAY, h);
			calendar.set(Calendar.MINUTE, m);
			calendar.set(Calendar.SECOND, s);
		}
		return calendar;
	}

	/**
	 * 为指定的日期增加指定数据的时间
	 * 
	 * @param date
	 * @param calendarField
	 * @param amount
	 * @return
	 */
	static public Date add(Date date, int calendarField, int amount) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.add(calendarField, amount);
		return calendar.getTime();
	}

	/**
	 * 获得指定日期所属季度
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getQuarterValue(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH) + 1;
		int quarter = month / 3;
		return month % 3 == 0 ? quarter : quarter + 1;
	}

	/**
	 * 计算两个日期之间的日期差值
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getBetweenDays(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		return (endTime - startTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * Adjust the date by days
	 * 
	 * @param date
	 *            Date the date which you want to adjust
	 * @param days
	 *            int how many day will adjust.
	 * @return Date the day which have already been delayed.
	 */
	public static Date adjustDateByDay(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * 格式起始日期
	 */
	public static Date formatBeginDate(Date beginDate) {
		if (beginDate == null) {
			beginDate = new Date();
		}
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginDate);
		beginCal.set(Calendar.HOUR_OF_DAY, 0);
		beginCal.set(Calendar.MINUTE, 0);
		beginCal.set(Calendar.SECOND, 0);
		// 查询开始时间
		Date begin = beginCal.getTime();

		return begin;
	}

	/**
	 * 格式截止日期
	 */
	public static Date formatEndDate(Date endDate) {
		Calendar endCal = Calendar.getInstance();
		Date nowtime = new Date();
		if (endDate == null || nowtime.before(endDate)) {
			endCal.setTime(nowtime);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 59);
		} else {
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 59);
		}
		Date end = endCal.getTime();

		return end;
	}

	/**
	 * Adjust the date by years
	 * 
	 * @param date
	 *            Date the date which you want to adjust
	 * @param year
	 *            int how many years will adjust.
	 * @return Date the day which have already been delayed.
	 */
	public static Date adjustDateByYear(Date date, int years) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}

	/**
	 * 把日期转为指定的日期限格式
	 * 
	 * @param aMask
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		if (aDate == null) {
		} else if (aMask == null) {
			aMask = getDateTimePattern();
		}
		df = new SimpleDateFormat(aMask, Locale.getDefault());
		return df.format(aDate);
	}

	/**
	 * 获取日期格式
	 * 
	 * @return
	 */
	public static String getDateTimePattern() {
		return "yyyy-MM-dd HH:mm:ss";
	}

	/**
	 * 获取凌晨时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeeHour(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date resultDate = null;
		try {
			resultDate = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	/**
	 * 日期相加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 时间减去多少分
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date deffDate(Date date, String min) {
		if (date == null || min == null || "".equals(min))
			return null;
		try {
			int m = Integer.parseInt(min);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.setTimeInMillis(c.getTimeInMillis() - ((long) m) * 60 * 1000);
			return c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的分钟数
	 */
	public static int diffMinute(Date date, Date date1) {
		if (date == null || date1 == null)
			return 0;
		return (int) ((getMillis(date) - getMillis(date1)) / (60 * 1000));
	}

	/**
	 * 日期相减 减天数
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相减后的日期
	 */
	public static Date diffDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	public static int daysBetween(Date startDate, Date endDate) {
		return Days.daysBetween(new DateTime(startDate), new DateTime(endDate))
				.getDays();
	}
	
	public static void main(String[] args){
		/*String str1 = "2011-01-01";
		String str2 = "1988-09-09";
		Date date1 = DateUtils.string2Date(str1, "yyyy-MM-dd");
		Date date2 = DateUtils.string2Date(str2, "yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		c2.add(Calendar.YEAR, 4);
		if (c2.before(c1)) {
			System.out.println("illegal");
		}else {
			System.out.println("ok");
		}*/
		System.out.println(DateUtils.addDate(new Date(), -1));
		
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	/*public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}*/
}
