package com.vector.smartWeb.helper;

import com.vector.smartWeb.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by vector01.yao on 2017/7/30.
 * Bean助手类：我们需要获取所有被框架管理的对象，即首先需要调用ClassHelper类的getBeanClassSet方法，
 * 随后需要循环调用ReflectionUtil的newInstance方法来实例化对象，并将每次创建的对象存放在一个静态的Map<Class,Object>中
 */
public final class BeanHelper {

    /**
     * 定义Bean映射（用于存放Bean类和Bean实例的映射关系）
     */
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet= ClassHelper.getBeanClassSet();
        for (Class<?> clazz:beanClassSet){
            Object object= ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz,object);
        }
    }

    /**
     * 获取bean映射
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     */
    public static <T> T getBean(Class<T> clazz){
        if (!BEAN_MAP.containsKey(clazz)){
            throw new RuntimeException("can not get bean by class:"+clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }
}
