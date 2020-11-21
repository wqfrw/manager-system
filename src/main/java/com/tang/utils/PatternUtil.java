package com.tang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName PatternUtil
 * @Description 正则匹配工具类
 * @author 芙蓉王
 * @Date Dec 13, 2019 3:17:20 PM
 * @version 1.0.0
 */
public class PatternUtil {

    public static final String COMMONREGEX = "<([^<>]*)>|[a-zA-z]+://|<+|>+|//|\\?";
    //手机号正则
    public static final String PHONENOREGEX = "^1[2-9][0-9]{9}$";
    //用户名正则
    public static final String USERNAMEREGEX = "^[^0-9][\\w_]{3,11}$";
    //用户真实姓名正则
    public static final String REALNAMEREGEX = "^[\\u4e00-\\u9fa5a-zA-Z.·]{2,12}$";
    //ip正则
    public static final String IPREGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    //密码正则
    public static final String PASSWORDREGEX = "[a-zA-Z0-9]{6,20}";
    //是否为数字
    public static final String NUMBERREGEX = "^-?[0-9]+$";
    //小数或整数
    public static final String MONEYREGEX = "^[0-9]+([.]{1}[0-9]+){0,1}$";
    //特殊字符正则
    public static final String CHARACTERREGEX = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    //QQ号正则
    public static final String QQRREGEX = "[1-9]{1}[0-9]{4,11}";
    //邮箱地址正则
    public static final String EMAILREGEX = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    //微信正则
    public static final String WXREGEX = "^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$";
    //字母正则
    public static final String LETTERREGEX = "^[A-Za-z]{2,5}$";
    
    public static Boolean isMatch(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
