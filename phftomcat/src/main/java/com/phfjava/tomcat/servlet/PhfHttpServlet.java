package com.phfjava.tomcat.servlet;

import com.phfjava.tomcat.http.PhfRequest;
import com.phfjava.tomcat.http.PhfResponse;

import javax.imageio.IIOException;

/**
 * @author 庞化峰
 * @version 1.0
 */
public abstract class PhfHttpServlet implements PhfServlet {
    @Override
    public void service(PhfRequest request, PhfResponse response) throws IIOException {
        //equalsIgnoreCase 比较两个字符串内容是否相同，并且不区分大小写
        if ("GET".equalsIgnoreCase(request.getMethod())) {

            //this 动态绑定 到doGet/doPost的真正运行类型去执行  -->子类PhfCalServlet
            this.doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            this.doPost(request, response);
        }
    }

    //这里使用了模板设计模式，
    //让PhfHttpServlet的子类 PhfCalServlet来实现
    public abstract void doGet(PhfRequest request, PhfResponse response);

    public abstract void doPost(PhfRequest request, PhfResponse response);
}
