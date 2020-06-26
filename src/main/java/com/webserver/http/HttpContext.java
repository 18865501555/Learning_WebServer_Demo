package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放所有HTTP协议规定的内容
 * @author orange
 * @create 2020-06-26 5:11 下午
 */
public class HttpContext {
    /**
     * 保存所有资源后缀与对应的Content-Type的值
     */
    private static Map<String,String> mimeMapping = new HashMap<>();

    static {
        initMimeMapping();
    }
    public static void initMimeMapping(){
        mimeMapping.put("html","text/html");
        mimeMapping.put("css","text/css");
        mimeMapping.put("js","application/javascript");
        mimeMapping.put("png","image/png");
        mimeMapping.put("gif","image/gif");
        mimeMapping.put("jpg","image/jpeg");
    }
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }
}
