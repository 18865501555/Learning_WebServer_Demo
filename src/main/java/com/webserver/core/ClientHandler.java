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
            InputStream in = socket.getInputStream();
            int d = 0;
            while ((d=in.read())!=-1){
                System.out.println((char)d);
            }
            System.out.println("ClientHandler:处理完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
