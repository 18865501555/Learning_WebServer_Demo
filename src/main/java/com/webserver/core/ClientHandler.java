package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;

import java.io.*;
import java.net.Socket;

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
            //测试读取客户端发送过来的请求的内容
            System.out.println("ClientHandler:解析请求");
            HttpRequest request = new HttpRequest(socket);

            //1解析请求

            //2处理请求

            //3响应客户端
            /*
            发送一个HTTP响应给客户端
            内容是：webapp/myweb/index.html
             */
            OutputStream out = socket.getOutputStream();
            File file = new File("./src/main/webapp/myweb/index.html");
            //3.1发送状态行
            //HTTP/1.1 200 OK
            String line = "HTTP/1.1 200 OK";
            byte[] data = line.getBytes("ISO8859-1");
            out.write(data);
            out.write(13);//单独发送回车符
            out.write(10);//单独发送换行符

            //3.2发送响应头
            //Content-Type: text/html
            line = "Content-Type: text/html";
            data = line.getBytes("ISO8859-1");
            out.write(data);
            out.write(13);
            out.write(10);
            //Content-Length: xxxx
            line = "Content_Length:" + file.length();
            data = line.getBytes("ISO8859-1");
            out.write(data);
            out.write(13);
            out.write(10);
            //单独发送回车符换行符表示响应头部分发送完毕
            out.write(13);
            out.write(10);

            //3.3发送消息正文
            FileInputStream fis = new FileInputStream(file);
            int len = 0;
            byte[] buf = new byte[1024 * 10];
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
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
