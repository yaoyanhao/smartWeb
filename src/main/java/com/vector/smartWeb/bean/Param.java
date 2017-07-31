package com.vector.smartWeb.bean;

import com.vector.smartWeb.util.CastUtil;

import java.util.Map;

/**
 * Created by vector01.yao on 2017/7/30.
 * 请求参数对象
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String,Object> paramMap){
        this.paramMap=paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }
}
