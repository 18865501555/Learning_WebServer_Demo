package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用thymeleaf将user.dat文件数据绑定到静态页面
 * userList.html上生成一个含有user.dat文件数据的页面
 * 并将其响应给客户端
 * @author orange
 * @create 2020-06-27 7:50 下午
 */
public class ShowAllUserServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet:开始工作...");
        /*
        1: 将user.dat文件中所有用户数据读取出来

        User 4个属性
        List<User> list

        Map<String,String> key 属性名 value 属性值
        List<Map> list
         */
        try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")){
            List<Map<String,String>> list = new ArrayList<>();
            //读取每一个用户
            for (int i = 0; i < raf.length() / 100; i++) {
                //每个用户信息都存入一个map
                Map<String,String> user = new HashMap<>();
                //读取用户名
                byte[] data = new byte[32];
                raf.read(data);
                String username = new String(data,"UTF-8").trim();
                //读取密码
                raf.read(data);
                String password = new String(data,"UTF-8").trim();
                //读取昵称
                raf.read(data);
                String nickname = new String(data,"UTF-8").trim();
                //读取年龄
                int age = raf.readInt();
                //将该用户数据存入map
                user.put("username",username);
                user.put("password",password);
                user.put("nickname",nickname);
                user.put("age",age+"");
                //将map存入集合
                list.add(user);
            }
            System.out.println("读取完毕!");
            System.out.println(list);
            //2:利用thymeleaf将数据绑定到静态页面
            //thymeleaf提供的类，保存需要在页面上显示的数据
            Context context = new Context();
            //将所有用户信息存入context
            context.setVariable("users",list);
            //实例化Thymeleaf引擎
            FileTemplateResolver tr = new FileTemplateResolver();
            //模版页面是html格式的
            tr.setTemplateMode("html");
            //模版页面字符集是UTF-8
            tr.setCharacterEncoding("UTF-8");
            //创建模版引擎，用来将数据绑定页面
            TemplateEngine te = new TemplateEngine();
            te.setTemplateResolver(tr);
            /*
            处理，这个方法就是将模版页面和context中的数据结合生成一个含有
            context中数据的页面的代码，返回值为String是生成后的html代码
             */
            String html = te.process("./src/main/webapp/myweb/userList.html",context);
            System.out.println(html);
            byte[] data = html.getBytes("UTF-8");
            response.setData(data);
            response.putHeader("Content-Type","text/html");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ShowAllUserServlet:工作完毕!");
    }
}
