package com.vector.smartWeb.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by vector01.yao on 2017/8/5.
 * 切面代理
 */
public abstract class AspectProxy implements Proxy{
    private static final Logger LOGGER= LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 模板方法
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result=null;

        Class<?> targetClass=proxyChain.getTargetClass();
        Method targetMethod=proxyChain.getTargetMethod();
        Object[] methodParams=proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(targetClass,targetMethod,methodParams)){//拦截到方法
                before(targetClass,targetMethod,methodParams);
                result=proxyChain.doProxyChain();
                after(targetClass,targetMethod,methodParams);
            }else {
                result=proxyChain.doProxyChain();
            }
        }catch (Exception ex){
            LOGGER.error("proxy failure",ex);
            error(targetClass,targetMethod,methodParams);
            throw ex;
        } finally {
            end();
        }
        return result;
    }


    public void begin(){

    }

    public boolean intercept(Class<?> targetClass,Method targetMethod,Object[] methodParams){
        return true;
    }

    /**
     * 钩子方法：前置增强
     */
    public void before(Class<?> targetClass,Method targetMethod,Object[] methodParams){

    }

    /**
     * 钩子方法：后置增强
     */
    public void after(Class<?> targetClass,Method targetMethod,Object[] methodParams){

    }

    /**
     * 钩子方法：错误提示
     */
    public void error(Class<?> targetClass,Method targetMethod,Object[] methodParams){

    }

    public void end(){

    }
}
