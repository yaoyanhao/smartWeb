package com.vector.smartWeb.proxy;

/**
 * Created by vector01.yao on 2017/8/1.
 * 代理接口
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
