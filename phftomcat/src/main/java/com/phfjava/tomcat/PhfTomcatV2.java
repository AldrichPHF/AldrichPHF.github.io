package com.phfjava.tomcat;

import com.phfjava.tomcat.handler.PhfRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 庞化峰
 * @version 1.0
 */
public class PhfTomcatV2 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("=======myTomcatV2 在8088端口监听======");
        while (!serverSocket.isClosed()) {
            //1.接收到浏览器的连接后，如果成功，就会得到socket
            //2.这个socket就是浏览器和服务器的数据通道
            Socket accept = serverSocket.accept();
            //3.创建一个线程对象，并把socket给该线程
            new Thread(new PhfRequestHandler(accept)).start();
        }


    }
}
