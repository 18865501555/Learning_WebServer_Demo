package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
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
        /*
        通过解析conf/web.xml文件初始化mimeMapping将根标签下所有名为
        <mime-mapping>的子标签获取到，并将其子标签：
        <extension>中间的文本作为key
        <mime-type>中间的文本作为value
        保存到mimeMapping中
        初始化后mimeMapping中应当有1011个元素
         */
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./src/main/conf/web.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for (Element e: list) {
                String key = e.elementTextTrim("extension");
                String value = e.elementTextTrim("mime-type");
                mimeMapping.put(key,value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println(mimeMapping.size());
    }
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        String type = getMimeType("png");
        System.out.println(type);
    }
}
