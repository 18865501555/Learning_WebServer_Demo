package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端处理器
 * 用于与客户端进行交互，主要工作三步：
 * 1：解析请求
 * 2：处理请求
 * 3：响应客户端
 * @author orange
 * @create 2020-06-23 11:22 下午
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println("ClientHandler:开始处理...");
            //1解析请求
            System.out.println("ClientHandler:解析请求");
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);

            //2处理请求
            /*
            通过请求对象获取抽象路径
             */
            String path = request.getUri();
            System.out.println("抽象路径:"+path);
            File file = new File("./src/main/webapp"+path);
            //判断用户请求的资源是否真实存在
            if (file.exists()){
                System.out.println("资源已找到!");
                response.setEntity(file);
                //根据用户请求的资源文件名字来获取后缀名
                String fileName = file.getName();
                //从文件名最后一个"."之后第一个字符开始截取到末尾
                String ext = fileName.substring(fileName.lastIndexOf(".")+1);
                //key:资源后缀名 value:Content-Type对应的值
                Map<String,String> mimeMapping = new HashMap<>();
                mimeMapping.put("html","text/html");
                mimeMapping.put("css","text/css");
                mimeMapping.put("js","application/javascript");
                mimeMapping.put("png","image/png");
                mimeMapping.put("gif","image/gif");
                mimeMapping.put("jpg","image/jpeg");

                String type = mimeMapping.get(ext);

                response.putHeader("Content-Type",type);
                response.putHeader("Content-Length",file.length()+"");

            } else {
                //不存在则响应404给客户端
                //设置状态代码为404
                System.out.println("资源不存在!");
                response.setStatusCode(404);
                response.setStatusReson("NotFound");
                File notFound = new File("./src/main/webapp/root/404.html");
                response.setEntity(notFound);
                response.putHeader("Content-Type","text/html");
                response.putHeader("Content-Length",notFound.length()+"");
            }
            //3响应客户端
            response.flush();

            System.out.println("响应发送完毕!");
            System.out.println("ClientHandler:处理完毕!");
        } catch (EmptyRequestException e){

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断开连接（HTTP1.0协议要求，一问一答后断开连接）
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
