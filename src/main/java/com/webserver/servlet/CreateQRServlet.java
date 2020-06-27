package com.webserver.servlet;

import com.webserver.http.HttpContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import qrcode.QRCodeUtil;

import java.io.ByteArrayOutputStream;

/**
 * @author orange
 * @create 2020-06-27 5:00 下午
 */
public class CreateQRServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("CreateQRServlet:开始生成二维码...");
        try(
            ByteArrayOutputStream o = new ByteArrayOutputStream();
        ){
            String content = request.getParameter("content");
            QRCodeUtil.encode(content,o);
            //通过ByteArrayOutputStream获取生成的二维码数据
            byte[] data = o.toByteArray();
            response.setData(data);
            response.putHeader("Content-Type", HttpContext.getMimeType("jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("CreatQRServlet:二维码生成完毕!");
    }
}
