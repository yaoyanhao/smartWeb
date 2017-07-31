package com.vector.smartWeb.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by vector01.yao on 2017/7/30.
 * json工具类：基于fastjson
 */
public final class JsonUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 从json串转换成实体对象
     * @param jsonStr e.g. {'name':'get','dateAttr':'2009-11-12'}
     * @param clazz Person.class
     * @return
     */
    public static <T> T json2Object(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr,clazz);
    }


    /**
     * 将Json字符串转换成对象list集合
     * @param jsonStr json字符串
     * @param clazz 对象类
     * @return
     */
    public static <T> List<T> json2ObjectList(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr, clazz);
    }

    /**
     * 将Json字符串转换成对象list集合
     * @param jsonStr json字符串
     * @param clazz 对象类
     * @return
     */
    public static <T> Object[] json2Array(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr, clazz).toArray();
    }


    /**
     * 将对象转为Json字符串
     * @param object 对象
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }


}
