package com.vector.smartWeb.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by vector01.yao on 2017/8/1.
 * 链式代理：将多个代理通过一个链串起来，一个个执行
 */
public class ProxyChain {
    private final Class<?> targetClass;//目标类
    private final Object targetObject;//目标对象
    private final Method targetMethod;//目标方法
    private final MethodProxy methodProxy;//方法代理
    private final Object[] methodParams;//方法参数
    private List<Proxy> proxyList;//代理列表
    private int proxyIndex=0;//代理索引

    public ProxyChain(Class<?> targetClass,Object targetObject,Method targetMethod,MethodProxy methodProxy
    ,Object[] methodParams,List<Proxy> proxyList){
        this.targetClass=targetClass;
        this.targetObject=targetObject;
        this.targetMethod=targetMethod;
        this.methodProxy=methodProxy;
        this.methodParams=methodParams;
        this.proxyList=proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public List<Proxy> getProxyList() {
        return proxyList;
    }

    public int getProxyIndex() {
        return proxyIndex;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex<proxyList.size()){//依次调用代理链中的代理方法
            methodResult=proxyList.get(proxyIndex++).doProxy(this);
        }else {
            methodResult=methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
