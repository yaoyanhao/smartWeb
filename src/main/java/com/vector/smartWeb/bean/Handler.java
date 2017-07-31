package com.vector.smartWeb.bean;

import java.lang.reflect.Method;

/**
 * Created by vector01.yao on 2017/7/30.
 * 封装Action信息，处理器的位置：位于哪个Controller中的那个方法
 */
public class Handler {
    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * Action方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass,Method actionMethod){
        this.actionMethod=actionMethod;
        this.controllerClass=controllerClass;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
