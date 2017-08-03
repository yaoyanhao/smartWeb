package com.vector.smartWeb.proxy;

import com.vector.smartWeb.UserImpl;
import com.vector.smartWeb.UserInterface;

import java.lang.reflect.*;

/**
 * Created by vector01.yao on 2017/8/1.
 */
public class TestProxy implements InvocationHandler {
    private Object target;

    public TestProxy(Object target){
        this.target=target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object object=method.invoke(proxy,args);
        after();
        return object;
    }

    public <T> T getProxy(){
        return (T)java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    private void before(){

    }

    private void after(){

    }

    public static void main(String[] args) {
        UserInterface userInterface=new TestProxy(new UserImpl()).getProxy();
        userInterface.doSomeThing();

    }
}
