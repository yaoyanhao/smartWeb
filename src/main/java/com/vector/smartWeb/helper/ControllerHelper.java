package com.vector.smartWeb.helper;

import com.vector.smartWeb.annonation.Action;
import com.vector.smartWeb.bean.Handler;
import com.vector.smartWeb.bean.Request;
import com.vector.smartWeb.util.ArrayUtil;
import com.vector.smartWeb.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by vector01.yao on 2017/7/30.
 * 控制器助手类
 */
public final class ControllerHelper {
    /**
     * ACTION_MAP：用于存放请求和处理器之间的映射关系
     */
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<Request, Handler>();

    static {
        //获取所有Controller类
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            for (Class<?> controllerClass:controllerClassSet){
                //获取Controller类中定义的所有方法
                Method[] methods=controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)){
                    for (Method method:methods){
                        //判断当前方法是否有Action注解
                        if (method.isAnnotationPresent(Action.class)){
                            //若有Action注解，则获取请求信息
                            Action action=method.getAnnotation(Action.class);
                            String mapping=action.value();
                            //验证请求格式是否合法
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array=mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据请求信息获取对应的handler方法
     * @param requestMethod 请求方法名
     * @param requestPath 请求路径
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
