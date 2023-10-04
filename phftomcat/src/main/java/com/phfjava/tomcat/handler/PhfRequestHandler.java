package com.phfjava.tomcat.handler;

/**
 * @author 庞化峰
 * @version 1.0
 */

import com.phfjava.tomcat.PhfTomcatV3;
import com.phfjava.tomcat.http.PhfRequest;
import com.phfjava.tomcat.http.PhfResponse;
import com.phfjava.tomcat.servlet.PhfCalServlet;
import com.phfjava.tomcat.servlet.PhfHttpServlet;
import com.phfjava.tomcat.utils.WebUtils;

import java.io.*;
import java.net.Socket;

/**
 * 1.PhfRequestHandler 对象是一个线程对象
 * 2.用来处理http请求
 */
public class PhfRequestHandler implements Runnable {
    //定义socket
    private Socket socket = null;

    public PhfRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        //这里可以对客户端/浏览器进行IO编程/交互
        try {
            //先死后活
//            InputStream inputStream = socket.getInputStream();

            //将inputStream转成BufferedReader ==>方便对数据进行按行读取
//            BufferedReader bufferedReader =
//                    new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//            System.out.println("当前线程为："+Thread.currentThread().getName());
//            System.out.println("==========PhfTomcatV2 接收到的数据如下=============");
//            String mes = null;
//            while ((mes = bufferedReader.readLine()) != null) {
//                if (mes.length() == 0 || mes == null) {
//                    break;
//                }
//                System.out.println(mes);

            PhfRequest phfRequest = new PhfRequest(socket.getInputStream());
//            String num1 = phfRequest.getParameter("num1");
//            String num2 = phfRequest.getParameter("num2");
//            System.out.println("请求的参数 num1 = " + num1);
//            System.out.println("请求的参数 num2 = " + num2);
//            System.out.println(phfRequest);


            //通过PhfResponse 对象，返回数据给浏览器 ===>先死后活
            PhfResponse phfResponse = new PhfResponse(socket.getOutputStream());

            //创建PhfCalServlet 对象
//            PhfCalServlet phfCalServlet = new PhfCalServlet();
//            phfCalServlet.doGet(phfRequest, phfResponse);

            //1.得到uri ==>  servletUrlMapping 容器里面的url-pattern
            String uri = phfRequest.getUri();
            phfResponse.htmlResp(uri, socket);

            String servletName = PhfTomcatV3.servletUrlMapping.get(uri);//通过key 来获取value
            if (servletName == null) {
                servletName = "";
            }
            //2.进一步获取到servlet 实例
            //   真正的运行类型是其子类 PhfCalServlet
            PhfHttpServlet phfHttpServlet = PhfTomcatV3.servletMapping.get(servletName);

            if (phfHttpServlet != null) {
                //3.调用servlet方法，通过OOP动态绑定机制 又调用到运行类型的doGet或者doPost
                phfHttpServlet.service(phfRequest, phfResponse);
            } else {
                //没有这个servlet，返回404提示信息
                String resp = PhfResponse.respHeader + "<h1>404 Not Found Servlet</h1>";
                OutputStream outputStream = phfResponse.getOutputStream();
                outputStream.write(resp.getBytes());
                outputStream.flush();
                outputStream.close();

            }


//            String resp = PhfResponse.respHeader + "<h1>phfResponse 返回的信息 hi 你好</h1>";
//            OutputStream outputStream = phfResponse.getOutputStream();
//            outputStream.write(resp.getBytes());
//            outputStream.flush();
//            outputStream.close();
            //返回数据给浏览器 --> 封装成http响应
            //构建响应头
//            String respHeader = "HTTP/1.1 200 OK\r\n" +
//                    "Content-Type: text/html;charset=utf-8\r\n\r\n";
//            String resp = respHeader + "<h1>hi,庞化峰学java</h1>";
//            System.out.println("myTomcatV2 返回的数据 = " + resp);
//            OutputStream outputStream = socket.getOutputStream();
//            outputStream.write(resp.getBytes());
//
//
//            outputStream.flush();
//            outputStream.close();
//            socket.getInputStream().close();
            socket.close();//socket 关闭，与其关联的流也就没有了

        } catch (IOException e) {

        } finally {
            if (socket != null) {
                try {
                    //最后一定要确保socket关闭
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}