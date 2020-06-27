package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * @author orange
 * @create 2020-06-27 9:59 下午
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest request, HttpResponse response);
}
