package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示客户端发送过来的一个请求
 * HTTP协议规定一个请求包含三部分
 * 1：请求行（三部分：请求方式，抽象路径，协议版本）
 * 2：消息头（若干个，每个消息头包含名字和对应的措施）
 * 3：消息正文（请求可以没有这部分，大部分请求没有这部分）
 * 一个请求的具体格式参考：http_utf-8.txt文件
 * @author orange
 * @create 2020-06-25 1:06 下午
 */
public class HttpRequest {
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();
    //消息正文相关信息

    private Socket socket;

    /**
     * 实例化HttpRequest的同时就会根据给定的Socket
     * 获取输入流并读取客户端发送过来的请求内容来做初始化
     * 实例化完毕后，该对象即表示客户端发送过来的请求内容了
     * @param socket
     */
    public HttpRequest(Socket socket) throws EmptyRequestException {
        System.out.println("HttpRequest:开始解析请求...");
        this.socket = socket;
        /*
        解析一个请求分为三步：
        1：解析请求行
        2：解析消息头
        3：解析消息正文
         */
        parseRequestLine();
        parseHeaders();
        parseContent();
        System.out.println("HttpRequest:解析请求完毕!");
    }
    /**
     * 解析请求行
     */
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("HttpRequest:开始解析请求行...");
        /*
        通过socket获取的输入流读取客户端发送过来的请求内容中
        第一行字符串（第一行内容就是请求行内容），将请求行内容
        按照空格拆分为三部分，并分别赋值给对应的属性：
        method,uri,protocol
         */
        try {
            String line = readLine();
            //如果请求行第一行就是空字符串，说明是空请求
            if (line.isEmpty()){
                throw new EmptyRequestException();
            }
            System.out.println("请求行:" + line);

            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
        } catch (EmptyRequestException e){
            //如果是空请求异常，对外抛出给构造方法
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);

        System.out.println("HttpRequest:解析请求行完毕!");
    }
    private void parseHeaders(){
        System.out.println("HttpRequest:开始解析消息头...");
        try{
            /*
            循环调用readLine方法读取每一个消息头
            当readLine方法返回的字符串为空字符串：""
            则说明单独读到了CRLF（回车符换行符）
            此时应当停止读取

            每个读取到的消息头按照冒号空格拆分
            将消息头的名字作为key，将消息头的值作为
            value保存到headers这个属性上
             */
            while (true){
                String line = readLine();
                if ("".equals(line)){
                    break;
                }
                System.out.println("消息头:"+line);
                String[] data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("所有消息头:"+headers);
        System.out.println("HttpRequest:解析消息头完毕!");
    }
    /*
    解析消息正文
     */
    private void parseContent(){
        System.out.println("HttpRequest:开始解析消息正文...");

        System.out.println("HttpRequest:解析消息正文完毕!");
    }
    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        int d = 0;
        StringBuilder builder = new StringBuilder();
        while ((d=in.read())!=-1){
            char c = (char) d;
            //上一次读取若是回车符并且本次读取的是换行符就停止
            if (builder.length()!=0 && builder.charAt(builder.length()-1)==13 && c==10){
                break;
            }
            builder.append(c);
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
    public String getHeader(String name){
        return headers.get(name);
    }
}
