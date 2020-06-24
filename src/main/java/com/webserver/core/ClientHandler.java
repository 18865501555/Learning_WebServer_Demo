package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
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
            String line = builder.toString().trim();
            System.out.println(line);

            //1解析请求

            //2处理请求

            //3响应客户端

            System.out.println("ClientHandler:处理完毕!");
        } catch (IOException e) {
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
