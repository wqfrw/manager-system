package com.tang.base.consts;

import cn.hutool.core.date.format.FastDateFormat;

import java.util.TimeZone;

public interface DatePatternConsts {
	//-------------------------------------------------------------------------------------------------------------------------------- Normal
	/** 标准日期格式：yyyy-MM-dd */
	 String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/** 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd */
	 FastDateFormat NORM_DATE_FORMAT = FastDateFormat.getInstance(NORM_DATE_PATTERN);
	
	/** 标准时间格式：HH:mm:ss */
	 String NORM_TIME_PATTERN = "HH:mm:ss";
	/** 标准时间格式 {@link FastDateFormat}：HH:mm:ss */
	 FastDateFormat NORM_TIME_FORMAT = FastDateFormat.getInstance(NORM_TIME_PATTERN);

	/** 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm */
	 String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/** 标准日期时间格式，精确到分 {@link FastDateFormat}：yyyy-MM-dd HH:mm */
	 FastDateFormat NORM_DATETIME_MINUTE_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MINUTE_PATTERN);

	/** 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss */
	 String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss */
	 FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_PATTERN);

	/** 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS */
	 String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/** 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS */
	 FastDateFormat NORM_DATETIME_MS_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MS_PATTERN);
	
	/** 标准日期格式：yyyy年MM月dd日 */
	 String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/** 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日 */
	 FastDateFormat CHINESE_DATE_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_PATTERN);
	
	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/** 标准日期格式：yyyyMMdd */
	 String PURE_DATE_PATTERN = "yyyyMMdd";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMdd */
	 FastDateFormat PURE_DATE_FORMAT = FastDateFormat.getInstance(PURE_DATE_PATTERN);
	
	/** 标准日期格式：HHmmss */
	 String PURE_TIME_PATTERN = "HHmmss";
	/** 标准日期格式 {@link FastDateFormat}：HHmmss */
	 FastDateFormat PURE_TIME_FORMAT = FastDateFormat.getInstance(PURE_TIME_PATTERN);
	
	/** 标准日期格式：yyyyMMddHHmmss */
	 String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss */
	 FastDateFormat PURE_DATETIME_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_PATTERN);
	
	/** 标准日期格式：yyyyMMddHHmmssSSS */
	 String PURE_DATETIME_MS_PATTERN = "yyyyMMddHHmmssSSS";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS */
	 FastDateFormat PURE_DATETIME_MS_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_MS_PATTERN);
	
	//-------------------------------------------------------------------------------------------------------------------------------- Others
	/** HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z */
	 String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/** HTTP头中日期时间格式 {@link FastDateFormat}：EEE, dd MMM yyyy HH:mm:ss z */
	 FastDateFormat HTTP_DATETIME_FORMAT = FastDateFormat.getInstance(HTTP_DATETIME_PATTERN);

	/** JDK中日期时间格式：EEE MMM dd HH:mm:ss zzz yyyy */
	 String JDK_DATETIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
	/** JDK中日期时间格式 {@link FastDateFormat}：EEE MMM dd HH:mm:ss zzz yyyy */
	 FastDateFormat JDK_DATETIME_FORMAT = FastDateFormat.getInstance(JDK_DATETIME_PATTERN);
	
	/** UTC时间：yyyy-MM-dd'T'HH:mm:ss'Z' */
	 String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/** UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss'Z' */
	 FastDateFormat UTC_FORMAT = FastDateFormat.getInstance(UTC_PATTERN, TimeZone.getTimeZone("UTC"));
}
