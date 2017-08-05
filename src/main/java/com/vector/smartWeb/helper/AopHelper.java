package com.vector.smartWeb.helper;

import com.vector.smartWeb.annonation.Aspect;
import com.vector.smartWeb.proxy.AspectProxy;
import com.vector.smartWeb.proxy.Proxy;
import com.vector.smartWeb.proxy.ProxyManager;
import com.vector.smartWeb.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by vector01.yao on 2017/8/5.
 * AOP助手类:获取所有的目标类及其被拦截的切面类，并通过ProxyManager的createProxy方法来创建代理对象ProxyChain，
 * 最后将其放入BeanMap中
 */
public final class AopHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>,Set<Class<?>>> proxyMap=createProxyMap();
            Map<Class<?>,List<Proxy>> targetMap=getTargetMap(proxyMap);
            for (Map.Entry<Class<?>,List<Proxy>> entry:targetMap.entrySet()){
                Class<?> targetClass=entry.getKey();
                List<Proxy> proxyList=entry.getValue();
                Object proxyChain= ProxyManager.createProxy(targetClass,proxyList);
                BeanHelper.setBean(targetClass,proxyChain);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure!",e);
        }
    }


    /**
     * 获得所有目标类
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet=new HashSet<Class<?>>();//目标类集合
        Class<? extends Annotation> annotation=aspect.value();
        if (annotation!=null&&!annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassByAnnoation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类与目标类的映射关系
     * 一个代理类可能对应多个目标类，获取代理类和目标类的映射Map
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap=new HashMap<Class<?>, Set<Class<?>>>();

        //添加切面代理
        Set<Class<?>> proxyClassSet=ClassHelper.getClassSetBySuper(AspectProxy.class);//1.所有代理类都继承AspectProxy类
        for (Class<?> proxyClass:proxyClassSet){
            if (proxyClass.isAnnotationPresent(Aspect.class)){//2.代理类需要带有@Aspect注解
                Aspect aspect=proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet=createTargetClassSet(aspect);//获取该代理类的所有目标类
                proxyMap.put(proxyClass,targetClassSet);
            }
        }

        //添加事务代理
        Set<Class<?>> transactionTargetClasses=ClassHelper.getClassByAnnoation(Annotation.class);//所有带有@Transation注解的类
        proxyMap.put(TransactionProxy.class,transactionTargetClasses);

        return proxyMap;
    }

    /**
     * 获取目标类与其代理对象集合的映射关系
     * @param proxyMap map<代理类，目标类集合>
     * @return targetMap:<目标类，代理对象集合>
     */
    private static Map<Class<?>,List<Proxy>> getTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws IllegalAccessException, InstantiationException {
        Map<Class<?>,List<Proxy>> targetMap=new HashMap<Class<?>,List<Proxy>>();
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyMapEntry: proxyMap.entrySet()){
            Class<?> proxyClass=proxyMapEntry.getKey();
            Set<Class<?>> targetClassSet=proxyMapEntry.getValue();
            for (Class<?> targetClass:targetClassSet){
                Proxy proxy= (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList=new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
