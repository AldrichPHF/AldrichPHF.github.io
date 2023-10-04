package com.phfjava.tomcat.servlet;

import com.phfjava.tomcat.http.PhfRequest;
import com.phfjava.tomcat.http.PhfResponse;
import com.phfjava.tomcat.utils.WebUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 庞化峰
 * @version 1.0
 * servlet 测试2 计算两个数的乘法
 */
public class PhfCalServlet02 extends PhfHttpServlet {

    @Override
    public void doGet(PhfRequest request, PhfResponse response) {
        //java 动态绑定机制
//        request.getParameter("num1");
        int num1 = WebUtils.parseInt(request.getParameter("num1"), 0);
        int num2 = WebUtils.parseInt(request.getParameter("num2"), 0);
        int sum = num1 * num2;
        //返回计算结果给浏览器
//        outputStream和 当前的socket关联
        OutputStream outputStream = response.getOutputStream();
        String resMes = PhfResponse.respHeader + "<h1>" + num1 + "X" + num2 + "=" + sum + "   </h1><br/><h2>PhfTomcatV3 -反射 + xml</h2>";

        try {
            outputStream.write(resMes.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(PhfRequest request, PhfResponse response) {
        doGet(request, response);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() {

    }
}
