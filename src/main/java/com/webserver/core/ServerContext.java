package com.webserver.core;

import com.webserver.servlet.HttpServlet;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author orange
 * @create 2020-06-27 9:56 下午
 */
public class ServerContext {
    private static Map<String,HttpServlet> servletMapping = new HashMap<>();
    static {
        initServletMapping();
    }

    /**
     * 初始化servletMapping
     */
    private static void initServletMapping(){
        /*
        通过解析conf/servlets.xml文件初始化
        读取servliets.xml根标签下所有的<servlet>标签并将属性path的值作为key
        className的值利用反射加载该类并实例化后作为value保存到servletMapping这个map中
         */
        try{
            List<Element> list = new SAXReader().read("./src/main/conf/servlets.xml").getRootElement().elements("servlet");
            for (Element e: list
                 ) {
                String key = e.attributeValue("path");
                String className = e.attributeValue("className");
                Object value = Class.forName(className).newInstance();
                servletMapping.put(key,(HttpServlet) value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据请求的路径获取对应的Servlet
     * @param path
     * @return
     */
    public static HttpServlet getServlet(String path){
        return servletMapping.get(path);
    }
}
