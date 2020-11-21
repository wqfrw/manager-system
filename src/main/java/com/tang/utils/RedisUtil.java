package com.tang.utils;

import com.alibaba.fastjson.JSON;
import com.tang.base.config.ApplicationConfig;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @ClassName RedisUtil
 * @Description Redis工具类
 * @author 芙蓉王
 * @Date Dec 13, 2019 12:10:08 PM
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedisUtil {
	
	/**
     * Redis客户端操作模板
     */
    private static RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 不可重复列表类型操作类
     */
    private static SetOperations<String, Object> setOperations;
    
    /**
     * 带权重的不可重复列表类型操作类
     */
    private static ZSetOperations<String, Object> zSetOperations;
    
    /**
     * 可重复列表类型操作类
     */
    private static ListOperations<String, Object> listOperations;
    
    /**
     * 通用对象类型操作类
     */
    private static ValueOperations<String, Object> valueOperations;
    
    /**
     * Hash表结构类型操作类
     */
    private static HashOperations<String, Object, Object> hashOperations;
    
	/**
     * 模糊匹配域脚本
     */
	private static final String HKEYS_LUA;
    
	/**
     * 分布式解锁脚本
     */
    private static final String UNLOCK_LUA;
    
    /**
     * Redis工具初始化
     */
    static {
    	StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
        
        StringBuilder hkeysBuilder = new StringBuilder();
        hkeysBuilder.append("local resultArr=redis.call('hkeys',KEYS[1]);");
        hkeysBuilder.append("local resultDict={};");
        hkeysBuilder.append("for index,value in pairs(resultArr) do ");
        hkeysBuilder.append("if(string.find(value,ARGV[1])) then ");
        hkeysBuilder.append("table.insert(resultDict,value);");
        hkeysBuilder.append("end;end;return resultDict");
        HKEYS_LUA=hkeysBuilder.toString();
        
        redisTemplate=ApplicationConfig.getBean("redisTemplate",RedisTemplate.class);
    	setOperations=redisTemplate.opsForSet();
    	listOperations=redisTemplate.opsForList();
    	hashOperations=redisTemplate.opsForHash();
    	zSetOperations=redisTemplate.opsForZSet();
    	valueOperations=redisTemplate.opsForValue();
    }
    
    /** 默认过期时长，单位：秒 (24小时) */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;

    /** 不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    
    public static void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public static void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public static <T> T get(String key, Class<T> clazz, long expire) {
        String value = (String) valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public static String get(String key, long expire) {
        String value = (String) valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public static String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description put一个map
     * @param key
     * @param data
     */
    public void hset(String key, Map<String, Object> map) {
        hset(key, map, DEFAULT_EXPIRE);
    }

    public void hset(String key, Map<String, Object> map, long expire) {
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public static void hmset(String key, Map<String, String> map) {
        hmset(key, map, DEFAULT_EXPIRE);
    }

    public static void hmset(String key, Map<String, String> map, long expire) {
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public static String hget(String key, String field) {
        String value = (String) hashOperations.get(key, field);
        return value;
    }

    /**
     * @Description zset
     * @param key
     * @param value
     */
    public static void sadd(String key, String value, long expire) {
        setOperations.add(key, value);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public static boolean isMember(String key, Object o) {
        return setOperations.isMember(key, o);
    }

    public static void sadd(String key, String value) {
        sadd(key, value);
    }

    public static void srem(String key, String value) {
        setOperations.remove(key, value);
    }

    public static Set<Object> smembers(String key) {
        return setOperations.members(key);
    }

    /**
     * hgetAll
     *
     * @param key
     * @return
     */
    public static Map<Object, Object> hgetAll(String key) {
        return hashOperations.entries(key);
    }

    /**
     * Object转成JSON数据
     */
    private static String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /** ===================================redis分布式锁操作方式=========================================== */
    /**
     * @Description 设置缓存分布式锁
     * @param lockKey
     *            锁key
     * @param uuid
     *            唯一标识符
     * @param expireTime
     *            过期时间
     * @return true 则没有锁 , false 则锁没有释放
     */
    public static boolean hasLock(String lockKey, String uuid, long expireTime) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.set(lockKey.getBytes(Charset.forName("UTF-8")),
                        uuid.getBytes(Charset.forName("UTF-8")),
                        Expiration.seconds(TimeUnit.SECONDS.toSeconds(expireTime)),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            boolean hasLock = redisTemplate.execute(callback);
            if(hasLock){
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * @Description 释放分布式锁
     * @param lockKey
     * @param uuid
     * @return
     */
    public static boolean releaseLock(String lockKey, String uuid) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1,
                    lockKey.getBytes(Charset.forName("UTF-8")), uuid.getBytes(Charset.forName("UTF-8")));
        };
        return (Boolean) redisTemplate.execute(callback);
    }
}
