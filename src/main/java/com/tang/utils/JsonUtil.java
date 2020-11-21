package com.tang.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Description:JSON工具类
 * @auth:
 * @Date:2018/8/10 16:58
 * @return:
 */
public class JsonUtil {

    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


    }

    /**
     * @Description: 对象转JSON
     * @params: 实体类
     * @auth:  seeanknow
     * @Date:   2018/8/10 16:57
     * @return:
     */
    public static String parseJsonString(Object o){
        if (o==null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description:JSON转 T对象
     * @auth:seeanknow
     * @Date:2018/8/10 16:57
     * @return:
     */
    public static <T> T parseObject(String json,Class<T> cla) {
        if (json==null||json.equals(""))
            return null;
        try {
            return objectMapper.readValue(json, cla);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @Description:JSON转 List对象,第二个参数,定义泛型
     * @auth:seeanknow
     * @Date:2018/8/10 16:57
     * @return:
     */
    public static <T> List<T> parseObjectList(String json,Class<T> cla) {
        if (json==null||json.equals("")) {
            return null;
        }
        try {
            JavaType javaType = JsonUtil.objectMapper.getTypeFactory().constructParametricType(List.class, cla);
            List<T> list = JsonUtil.objectMapper.readValue(json, javaType);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static Map<String ,Object> parseObjectMap(String json) {
        if (json==null||json.equals("")) {
            return null;
        }
        try {
            JavaType javaType = JsonUtil.objectMapper.getTypeFactory().constructParametricType(List.class, Map.class);
            Map<String,Object> m = JsonUtil.objectMapper.readValue(json, javaType);
            return m;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
