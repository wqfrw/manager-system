package com.tang.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ParamSortUtil {

	/**
	 *
	 * @MehtodName asciiSortByKey
	 * @Description 按Map键 ascii码排序，键值都参与签名串拼接
	 * @param dataMap 源数据
	 * @param ignoreKey 忽略参与签名参数名
	 * @param endStr 结尾字符 & ！+
	 * @param isDeleteEnd 是否删除 &
	 * @param isEmpty 是否判断非空
	 * @return
	 */
	public static StringBuilder asciiSortByKey(Map<String, String> dataMap, String ignoreKey, String endStr, boolean isDeleteEnd, boolean isEmpty) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> map = new TreeMap<>(dataMap);
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String val = map.get(key);
			if(isEmpty) {
				if (StringUtils.isBlank(val)) {
					continue;
				}
			}
			if (StringUtils.isNotBlank(ignoreKey)){
				List<String> ignoreList = Arrays.asList(ignoreKey.split(","));
				if(ignoreList.contains(key)) {
					continue;
				}
			}
			sb.append(key).append("=").append(val).append(endStr);
		}
		if(isDeleteEnd) {
			sb.deleteCharAt(sb.length()-endStr.length());
		}
		return sb;
	}

	/**
	 *
	 * @MehtodName asciiSortByKey
	 * @Description 按Map键 ascii码排序，值参与签名串拼接
	 * @param dataMap 源数据
	 * @param ignoreKey 忽略参与签名参数名
	 * @param isEmpty 是否判断非空
	 * @return
	 */
	public static StringBuilder asciiSortByValue(Map<String, String> dataMap, String ignoreKey, String endStr, boolean isDeleteEnd, boolean isEmpty) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> map = new TreeMap<>(dataMap);
		for (String key : map.keySet()) {
			String val = map.get(key);
			if (isEmpty) {
				if (StringUtils.isBlank(val)) {
					continue;
				}
			}
			List<String> ignoreList = Arrays.asList(ignoreKey.split(","));
			if (ignoreList.contains(key)) {
				continue;
			}
			sb.append(val).append(endStr);
		}
		if(isDeleteEnd) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb;
	}

	/**
	 *
	 * @MehtodName sequenceSortByKey
	 * @Description  固定顺序排序
	 * @param dataMap 源数据
	 * @param sortArray 排序数组
	 * @param middleStr 连接字符 = []
	 * @param endStr 结尾字符 & ！+
	 * @param isDeleteEnd 是否删除结尾字符
	 * @return
	 */
	public static StringBuilder sequenceSortByKey(Map<String, String> dataMap, String[] sortArray, String middleStr, String endStr, boolean isDeleteEnd) {
		StringBuilder sb = new StringBuilder();
		for (String key : sortArray) {
			sb.append(key).append(middleStr).append(dataMap.get(key)).append(endStr);
		}
		if(isDeleteEnd) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb;
	}
	/**
	 *
	 * @MehtodName sequenceSort
	 * @Description  固定顺序排序
	 * @param dataMap 源数据
	 * @param sortArray 排序数组
	 * @param endStr 结尾字符 & ！+
	 * * @param isDeleteEnd 是否删除结尾字符
	 * @return
	 */
	public static StringBuilder sequenceSort(Map<String, String> dataMap, String[] sortArray, String endStr, boolean isDeleteEnd) {
		StringBuilder sb = new StringBuilder();
		for (String key : sortArray) {
			sb.append(dataMap.get(key)).append(endStr);
		}
		if(isDeleteEnd) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb;
	}
}
