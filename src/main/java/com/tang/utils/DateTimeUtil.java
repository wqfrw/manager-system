package com.tang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName DateTimeUtil
 * @Description 时间工具类
 * @author 芙蓉王
 * @Date Dec 24, 2019 1:51:33 PM
 * @version 1.0.0
 */
public class DateTimeUtil {

    public static final String DAY_BEGIN = " 00:00:00";
    public static final String DAY_END = " 23:59:59";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat sdfMoth = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    public static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdf_yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter formatterDate_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter formatterDate_yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * String转localDate
     * @param dateStr
     * @return
     */
    public static LocalDate string2LocalDate(String dateStr){
        return LocalDate.parse(dateStr, formatterDate);
    }

    //入参：2019-03-10 20:25:00 不能2019-3-10 20:25:00 or 2019-03-10  23:59:59
    public static long dateToUnixTime(String dateStr){
        return LocalDateTime.parse(dateStr,formatterDateTime).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long dateToUnixTimeOld(String dateStr) throws ParseException {
        return sdf.parse(dateStr).getTime();
    }

    public static String nowDateTime(){
        return LocalDateTime.now().format(formatterDateTime);
    }

    public static String nowDate(){
        return LocalDate.now().format(formatterDate);
    }

    public static String nowDate_yyyyMMdd(){
        return LocalDate.now().format(formatterDate_yyyyMMdd);
    }

    public static String localDateMinusDays(long days){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(days).toString();
    }

    public static String localDateMinusMonths(long months){
        LocalDate localDate = LocalDate.now();
        return localDate.minusMonths(months).toString();
    }

    public static String minusMinutes(long minutes){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusMinutes(minutes).format(formatterDateTime);
    }

    //返回时间戳
    public static long plusMinutesAndReturnUnixTime(long minutes){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(minutes).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    //返回时间戳
    public static long plusMonthsAndReturnUnixTime(long months){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMonths(months).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    //返回时间戳
    public static long plusDaysAndReturnUnixTime(long days){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusDays(days).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    //返回秒
    public static long plusMonthsAndReturnSecond(long months){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMonths(months).toEpochSecond(ZoneOffset.of("+8"));
    }

    //返回时间戳
    public static long minusMinutesAndReturnUnixTime(long minutes){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusMinutes(minutes).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    //LocalDate转Date
    public static Date toDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
    }

    //LocalDateTime转Date
    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
    }

    //Date转LocalDate
    public static LocalDate toLocalDate(Date date){
        return date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }

    //Date转LocalDateTime
    public static LocalDateTime toLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    //LocalDate(当天的开始时间)转时间戳
    public static long toTimestamp(LocalDate localDate){
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    //LocalDateTime转时间戳
    public static long toTimestamp(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    //时间戳转LocalDate
    public static LocalDate toLocalDate(long timestamp){
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }

    //时间戳转LocalDateTime
    public static LocalDateTime toLocalDateTime(long timestamp){
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }
    
    /**
     * 
     * @Description 字符串日期转Date日期
     * @param date
     * @return
     * @throws ParseException 
     */
    public static Date strToDate(String date,String formatStr) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.parse(date);
    }
    
    /**
     * 
     * @Description Date日期转字符串日期
     * @param date
     * @param formatStr
     * @return
     */
    public static String dateToStr(Date date,String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    
    /**
     * 
     * @Description 新增或扣除天数
     * @param month 天数
     * @return
     */
    public static Date addOrMinusDay(Date date,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return calendar.getTime();
    }
    
    /**
     * 
     * @Description 获取传入日期加减天数的开始时间
     * @param dateNum
     * @return
     */
    public static Date minusOrPlusDate(Date date, int dateNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateNum);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 
     * @Description 获取当前日期过去某一天的开始时间
     * @param dateNum
     * @return
     */
    public static Date getStatisBeginDate(int dateNum) {
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DATE, -dateNum);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 
     * @Description 获取当前日期过去某一天的结束时间
     * @param dateNum
     * @return
     */
    public static Date getStatisEndDate(int dateNum) {
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DATE, -dateNum);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /**
     * 
     * @Description 获取当前日期的一周起始时间
     * @return
     */
    public static Date getStatisWeekBeginDate() {
    	Calendar cal = Calendar.getInstance();
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();////当前日期所在周的礼拜一
    }
    
    //获取指定时区的日期
    public static String getDateFromZone(String dateFormat, String zone) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(zone));
        return localDateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }
    
    
}
