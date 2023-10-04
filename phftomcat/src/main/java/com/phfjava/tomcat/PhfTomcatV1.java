package com.phfjava.tomcat;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 庞化峰
 * @version 1.0
 * 第一个版本的tomcat 可以完成 接收浏览器的请求，并返回相关信息
 */
public class PhfTomcatV1 {
    public static void main(String[] args) throws IOException {

        //1.创建ServerSocket ,在8088端口监听
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("=======myTomcat在8088端口监听======");
        while (!serverSocket.isClosed()) {
            //等待浏览器/客户端的连接
            //如果有链接过来，就创建一个socket
            //这个socket就是连接浏览器/客户端的通道
            Socket socket = serverSocket.accept();
//            PhfRequestHandler phfRequestHandler = new PhfRequestHandler(socket);

            //先接收浏览器发送的数据
            //inputStream字节流--> BufferedReader字符流（可以按行读取）
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String mes = null;
            System.out.println("接收到浏览器发送的数据");
            //循环读取
            while ((mes = bufferedReader.readLine()) != null) {

                if (mes.length() == 0) {
                    break;
                }
                //判断mes的长度是否为0
                System.out.println(mes);
            }

            // myTomcat回送http响应方式
            OutputStream outputStream = socket.getOutputStream();
            //构建一个HTTP响应头
            //  /r/n表示换行
            // http响应体需要前面有两个换行\r\n\r\n
            //"HTTP/1.1 200\r\n" +
            //                    "Content-Type: text/html;charset=utf-8\r\n\r\n" +
            //                    "Transfer-Encoding: chunked\n" +
            //                    "Date: Mon, 18 Sep 2023 14:28:34 GMT\r\n" +
            //                    "Keep-Alive: timeout=20\r\n" +
            //                    "Connection: keep-alive"
            String respHeader = "HTTP/1.1 200\r\n" +
                    "Content-Type: text/html;charset=utf-8\r\n\r\n";
            String resp = respHeader + "hi,庞化峰学java";

            System.out.println("===========myTomcat给浏览器回送的数据=========");
            System.out.println(resp);
            outputStream.write(resp.getBytes());


            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();

        }

    }
}
