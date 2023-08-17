package com.phfsdy.service;

import com.phfsdy.qqcommon.Message;
import com.phfsdy.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author 庞化峰
 * @version 1.0
 * 线程类
 */
public class ClientConnectServerThread extends Thread {
    //该线程要持有Socket
    private Socket socket;

    //构造器可以接受一个socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为Thread要在后台和服务器通信，因此使用while循环
        while (true) {
            try {
                System.out.println("客户端线程，等待从服务器端发送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送消息，线程会阻塞在这里
                Message ms = (Message) ois.readObject();
                //判断服务器发送的消息类型，然后做相应的业务处理

                //如果读取的是服务端返回的在线用户列表
                if (ms.getMessageType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息，并显示
                    String[] onlineUsers = ms.getContent().split(" ");
                    System.out.println("\n================当前在线用户列表======================");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }

                } else if (ms.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {//接收到普通聊天消息
                    //把从服务器转发的消息显示出来
                    System.out.println("\n\u001B[34m" + ms.getSender() + " 对 " + ms.getGetter() + " 说： " + ms.getContent() + " " + ms.getSendTime() + "\u001B[0m");
//                    System.out.println("\u001B[34m" + "这段文字将会以蓝色显示" + "\u001B[0m");

                } else if (ms.getMessageType().equals(MessageType.Message_TO_ALL_MES)) {
                    System.out.println("\n\u001B[34m" + ms.getSender() + "对所有人说：" + ms.getContent() + "  " + ms.getSendTime() + "\u001B[0m");
                } else if (ms.getMessageType().equals(MessageType.Message_FILE_MES)) {
                    System.out.println("\n\u001B[34m" + ms.getSender() + " 给 " + ms.getGetter() + "发送了文件" + ms.getSrc() + "到我的电脑目录的" + ms.getDest() + "\u001B[0m");
                    //取出message文件的字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(ms.getDest());
                    fileOutputStream.write(ms.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功");

                } else {
                    System.out.println("其他类型暂时不处理了");
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}
