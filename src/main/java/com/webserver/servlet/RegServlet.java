package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author orange
 * @create 2020-06-27 12:59 下午
 */
public class RegServlet {
    public void service(HttpRequest request,HttpResponse response){
        System.out.println("RegServlet:开始用户注册...");
        /*
        1:通过request获取用户在注册页面上输入的注册信息
        2:将用户输入的注册信息写入文件user.dat文件
        3:设置response响应注册结果页面
         */
        /*
        获取信息时注意getParameters方法中传入的字符串要与注册页面上对应的
        输入框的名字一致！即<input name="xxx">中name属性的值
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        if (username==null||password==null||nickname==null||ageStr==null||!ageStr.matches("\\d+")){
            response.setEntity(new File("./src/main/webapp/myweb/reg_info_error.html"));
            return;
        }
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        System.out.println("nickname:"+nickname);
        System.out.println("ageStr:"+ageStr);

        int age = Integer.parseInt(ageStr);
        /*
        将该用户信息写入user.dat文件
        每个用户信息占用100字节，其中用户名，密码，昵称为String类型各占32字节
        年龄为int值占4字节
         */
        try(
            RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
        ){
            //判断是否为重复用户
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if (name.equals(username)){
                    response.setEntity(new File("./src/main/webapp/myweb/have_user.html"));
                    return;
                }
            }
            raf.seek(raf.length());
            byte[] data = username.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);
            data = password.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);
            data = nickname.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);
            raf.writeInt(age);

            //响应客户端注册成功页面
            File file = new File("./src/main/webapp/myweb/reg_success.html");
            response.setEntity(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RegServlet:用户注册完毕!");
    }
}
