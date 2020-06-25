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
            //1解析请求
            System.out.println("ClientHandler:解析请求");
            HttpRequest request = new HttpRequest(socket);

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
                //存在则将该资源响应给客户端
                OutputStream out = socket.getOutputStream();
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

                //3.3发送响应正文
                FileInputStream fis = new FileInputStream(file);
                int len = 0;
                byte[] buf = new byte[1024 * 10];
                while ((len = fis.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            } else {
                //不存在则响应404
                System.out.println("资源不存在!");
                File notFound = new File("./src/main/webapp/root/404.html");
                OutputStream out = socket.getOutputStream();
                //发送状态行
                String line = "HTTP/1.1 404 NotFound";
                byte[] data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);
                out.write(10);

                line = "Content-Type: text/html";
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);
                out.write(10);
                line = "Content-Length:"+notFound.length();
                data = line.getBytes("ISO8859-1");
                out.write(data);
                out.write(13);
                out.write(10);
                out.write(13);
                out.write(10);
                FileInputStream fis = new FileInputStream(notFound);
                int len = 0;
                byte[] buf = new byte[1024*10];
                while ((len = fis.read(buf))!=-1){
                    out.write(buf,0,len);
                }
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
