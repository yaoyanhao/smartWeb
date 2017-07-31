package com.vector.smartWeb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vector01.yao on 2017/7/30.
 * 流操作工具类
 */
public final class StreamUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流获取字符串
     */
    public static String getString(InputStream is){
        StringBuilder sb=new StringBuilder();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get String failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
