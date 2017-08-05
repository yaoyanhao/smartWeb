package com.vector.smartWeb;

import com.vector.smartWeb.helper.*;
import com.vector.smartWeb.util.ClassUtil;

/**
 * Created by vector01.yao on 2017/7/30.
 * 加载相应的Helper类
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList={
                ClassHelper.class, BeanHelper.class,AopHelper.class,IOCHelper.class, ControllerHelper.class
        };
        for (Class<?> clazz:classList){
            ClassUtil.loadClass(clazz.getName(),true);
        }
    }
}
