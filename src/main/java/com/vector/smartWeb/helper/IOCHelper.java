package com.vector.smartWeb.helper;

import com.vector.smartWeb.annonation.Inject;
import com.vector.smartWeb.util.ArrayUtil;
import com.vector.smartWeb.util.CollectionUtil;
import com.vector.smartWeb.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by vector01.yao on 2017/7/30.
 * 依赖注入助手类
 */
public class IOCHelper {
    static {
        //获取所有Bean类和Bean实例之间的映射关系
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>,Object> entry:beanMap.entrySet()){
                Class<?> clazz=entry.getKey();//bean类
                Object beanInstance=entry.getValue();//bean类对应的对象实例

                //遍历Bean类的所有成员变量
                Field[] fields=clazz.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)){
                    for (Field field:fields){
                        //判断当前成员变量是否有Inject注解，若有则获取成员变量类，进而获取其实例
                        if (field.isAnnotationPresent(Inject.class)){
                            Class<?> fieldClass=field.getType();
                            Object fieldInstance=BeanHelper.getBean(fieldClass);

                            //将对象实例修改为当前成员变量的值
                            if(fieldInstance!=null){
                                ReflectionUtil.setField(beanInstance,field,fieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
