package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author orange
 * @create 2020-06-25 11:45 下午
 */
public class HttpResponse {
    //状态行相关信息
    //状态代码默认为200，因为大部分响应都能正确
    private int statusCode = 200;
    private String statusReson = "OK";
    //响应头相关信息

    //响应正文相关信息
    private File entity;
    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket){
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将当前响应对象内容以标准的HTTP响应格式发送给客户端
     */
    public void flush(){
        System.out.println("HttpResponse:开始发送响应...");
        /*
        1：发送状态行
        2：发送响应头
        3：发送响应正文
         */
        sendStatusLine();
        sendHeaders();
        sendContent();
        System.out.println("HttpResponse:发送响应完毕!");
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine(){
        System.out.println("HttpResponse:开始发送状态行...");
        try {
            String line = "HTTP1.1" + " " + statusCode + " " + statusReson;
            System.out.println("状态行:"+line);
            println(line);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpResponse:发送状态行完毕!");
    }

    /**
     * 发送响应头
     */
    private void sendHeaders(){
        System.out.println("HttpResponse:开始发送响应头...");
        try{
            //Content-Type: text/html
            String line = "Content-Type: text/html";
            System.out.println("响应头:"+line);
            println(line);
            //Content-Length: xxxx
            line = "Content-Length: "+entity.length();
            System.out.println("响应头："+line);
            println(line);
            //单独发送回车符换行符表示响应头部分发送完毕
            println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("HttpResponse:响应头发送完毕!");
    }
    /**
     * 发送响应正文
     */
    private void sendContent(){
        System.out.println("HttpResponse:开始发送响应正文...");
        try(
            FileInputStream fis = new FileInputStream(entity);
        ){
            int len = 0;
            byte[] buf = new byte[1024*10];
            while ((len = fis.read(buf))!=-1){
                out.write(buf,0,len);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpResponse:发送响应正文完毕!");
    }
    private void println(String str) throws IOException {
        byte[] data = str.getBytes("ISO8859-1");
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReson() {
        return statusReson;
    }

    public void setStatusReson(String statusReson) {
        this.statusReson = statusReson;
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
    }
}