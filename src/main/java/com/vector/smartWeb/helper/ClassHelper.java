package com.vector.smartWeb.helper;

import com.vector.smartWeb.annonation.Controller;
import com.vector.smartWeb.annonation.Service;
import com.vector.smartWeb.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vector01.yao on 2017/7/30.
 * 类操作助手类
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage=ConfigHelper.getAppBasePath();
        CLASS_SET= ClassUtil.getClassSet(basePackage);//项目基础包下的所有的类
    }

    /**
     * 获取应用包名下的所有类
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下的所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> controllerClassSet=new HashSet<Class<?>>();
        for (Class<?> clazz:CLASS_SET){
            if (clazz.isAnnotationPresent(Controller.class)){
                controllerClassSet.add(clazz);
            }
        }
        return controllerClassSet;
    }

    /**
     * 获取应用包名下的所有Service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> controllerClassSet=new HashSet<Class<?>>();
        for (Class<?> clazz:CLASS_SET){
            if (clazz.isAnnotationPresent(Service.class)){
                controllerClassSet.add(clazz);
            }
        }
        return controllerClassSet;
    }

    /**
     * 获取应用包名下的所有Bean,包括Controller和Service
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet=new HashSet<Class<?>>();
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getServiceClassSet());
        return beanClassSet;
    }
}
