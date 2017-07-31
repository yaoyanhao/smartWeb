package com.vector.smartWeb.bean;

/**
 * Created by vector01.yao on 2017/7/30.
 * 封装请求信息
 */
public class Request {
    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    public Request(String requestMethod,String requestPath){
        this.requestMethod=requestMethod;
        this.requestPath=requestPath;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (requestPath != null ? !requestPath.equals(request.requestPath) : request.requestPath != null) return false;
        return requestMethod != null ? requestMethod.equals(request.requestMethod) : request.requestMethod == null;
    }

    @Override
    public int hashCode() {
        int result = requestPath != null ? requestPath.hashCode() : 0;
        result = 31 * result + (requestMethod != null ? requestMethod.hashCode() : 0);
        return result;
    }
}
