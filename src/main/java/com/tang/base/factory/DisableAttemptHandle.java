package com.tang.base.factory;


/**
 * <p> 防止恶意登录：对访问IP进行限制 </p>
 *
 * @description :
 * @author : 芙蓉王
 * @date : 2020.3.17
 */
public class DisableAttemptHandle {
    
//    private static final Logger log = LoggerFactory.getLogger(DisableAttemptHandle.class);
//
//    private static final int MAX_ATTEMPT = 10;
//
//    public static void loginSucceeded(String key) {
//        RedisUtil.delete(key);
//    }
//
//    public static void loginFailed(String key) {
//        int attempts = 0;
//        try {
//            attempts = Integer.valueOf(RedisUtil.get(key)==null?"0":RedisUtil.get(key).toString()) ;
//        } catch (Exception e) {
//            attempts = 0;
//        }
//        attempts++;
//        RedisUtil.set(key, attempts + "", 24*60*60*1000);//频繁访问登录并且登录失败设置一天的超时时间
//    }
//
//    public static boolean isBlocked(String key) {
//        Integer attempts = 0;
//        try {
//            attempts = Integer.valueOf(RedisUtil.get(key)==null?"0":RedisUtil.get(key).toString()) ;
//            return attempts >= MAX_ATTEMPT;
//        } catch (Exception e) {
//            log.error("当前key："+key+" 获取异常");
//            return false;
//        }
//
//    }
}
