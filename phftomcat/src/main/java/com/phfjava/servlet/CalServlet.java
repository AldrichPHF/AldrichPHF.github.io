package com.phfjava.servlet;

import com.phfjava.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 庞化峰
 * @version 1.0
 */
@WebServlet(name = "Ca;Servlet")
public class CalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收提交的数据进行计算
        String strNum1 = request.getParameter("num1");
        String strNum2 = request.getParameter("num2");
        //把strNum1和strNum2转成int
        int num1 = WebUtils.parseInt(strNum1, 0);
        int num2 = WebUtils.parseInt(strNum2, 0);
        int result = num1 + num2;
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        //writer的本质就是OutputStream
        writer.print("<h1>" + num1 + "+" + num2 + "=" + result + "<h1/>");
        writer.flush();
        writer.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
