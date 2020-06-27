package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author orange
 * @create 2020-06-27 2:55 下午
 */
public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:用户开始登陆...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username==null||password==null){
            response.setEntity(new File("./src/main/webapp/myweb/login_info_error.html"));
            return;
        }
        try(
            RandomAccessFile raf = new RandomAccessFile("user.dat","r");
        ){
            for (int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if (name.equals(username)){
                    raf.read(data);
                    String word = new String(data,"UTF-8").trim();
                    if (word.equals(password)){
                        response.setEntity(new File("./src/main/webapp/myweb/login_success.html"));
                        return;
                    }
                    break;
                }
            }
            response.setEntity(new File("./src/main/webapp/myweb/login_fail.html"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("LoginServlet:用户登陆完毕!");
    }
}
