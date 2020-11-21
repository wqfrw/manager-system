package com.tang.base.filter;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName SQLFilter
 * @Description sql过滤器
 * @author 芙蓉王
 * @Date Dec 13, 2019 11:50:31 AM
 * @version 1.0.0
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, " ", "");
        
        //转换成小写进行校验
        String validStr = str.toLowerCase();

        //去掉'|"|;|\字符
        validStr = StringUtils.replace(validStr, "\"", "");
        validStr = StringUtils.replace(validStr, "\\", "");

        //转换成小写

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "create", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(validStr.indexOf(keyword) != -1){
                throw new RuntimeException("包含非法字符");
            }
        }

        return str;
    }
}
