package com.phfjava.tomcat.servlet;

import com.phfjava.tomcat.http.PhfRequest;
import com.phfjava.tomcat.http.PhfResponse;

import javax.imageio.IIOException;

/**
 * @author 庞化峰
 * @version 1.0
 * 只保留了三个核心的方法
 */
public interface PhfServlet {
    void init() throws Exception;

    void service(PhfRequest request, PhfResponse response) throws IIOException;

    void destroy();

}
