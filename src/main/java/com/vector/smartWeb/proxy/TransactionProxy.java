package com.vector.smartWeb.proxy;

import com.vector.smartWeb.annonation.Transaction;
import com.vector.smartWeb.helper.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by vector01.yao on 2017/8/5.
 * 事务代理
 */
public class TransactionProxy implements Proxy {
    private static final Logger LOGGER= LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };


    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag=FLAG.get();
        Method method=proxyChain.getTargetMethod();
        if (!flag&&method.isAnnotationPresent(Transaction.class)){//检查事务注解
            FLAG.set(true);
            try {
                DataBaseHelper.beginTransaction();
                LOGGER.info("begin transaction");
                result=proxyChain.doProxyChain();
                DataBaseHelper.commitTransaction();
                LOGGER.info("commit transaction");
            } catch (Exception e){
                DataBaseHelper.rollbackTransaction();
                LOGGER.error("rollback transaction");
                throw e;
            } finally {
                FLAG.remove();
            }
        }else {
            result=proxyChain.doProxyChain();
        }
        return result;
    }
}
