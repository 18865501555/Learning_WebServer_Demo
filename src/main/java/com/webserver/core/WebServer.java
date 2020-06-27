package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer是模拟Tomcat实现的一个Web容器
 *
 * Web容器是一个服务器，它的主要职责是：
 * 1：管理其中部署的所有网络应用
 * 2：维护与客户端进行的TCP连接，并基于HTTP协议允许客户端访问不同网络应用下的资源
 * @author orange
 * @create 2020-06-23 11:24 下午
 */
public class WebServer {
    private ServerSocket server;

    public WebServer() {
        try {
            System.out.println("正在启动服务端...");
            server = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                System.out.println("一个客户端连接了!");
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
