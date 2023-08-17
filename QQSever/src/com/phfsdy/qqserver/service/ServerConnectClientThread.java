package com.phfsdy.qqserver.service;

import com.phfsdy.qqcommon.Message;
import com.phfsdy.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 庞化峰
 * @version 1.0
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;//连接服务端的客户Id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//这里线程处于run的状态，可以发送/接收消息
        while (true) {
            try {
                System.out.println("服务端和客户端" + userId + "保持通信，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                try {
                    Message message = (Message) ois.readObject();
                    //根据message的消息类型，做相应的业务处理
                    if (message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                        //客户端要在线用户列表
                        System.out.println(message.getSender() + "要在线用户列表");
                        String onlineUser = ManageClientThreads.getOnlineUser();
                        //返回message
                        //构建一个message对象返回给客户端
                        Message message2 = new Message();
                        message2.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                        message2.setContent(onlineUser);
                        message2.setGetter(message.getSender());
                        //返回给客户端
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message2);

                    } else if (message.getMessageType().equals(MessageType.Message_TO_ALL_MES)) {
                        //需要遍历管理线程的集合，得到所有线程的socket，然后把message进行转发
                        HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                        Iterator<String> iterator = hm.keySet().iterator();
                        while (iterator.hasNext()) {
                            //取出在线用户的Id
                            String onLineUserId = iterator.next().toString();
                            if (!onLineUserId.equals(message.getSender())) {//排除群发消息的用户
                                //进行转发
                                ObjectOutputStream oos =
                                        new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                                oos.writeObject(message);

                            }

                        }

                    } else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {
                        //根据message 获取getterId ，然后得到对应的线程
                        ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                        //得到对应的socket的对象输出流，将message对象转发给指定的客户端
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);//转发，如果用户不在线，可以保存到数据库，实现离线留言


                    } else if (message.getMessageType().equals(MessageType.Message_FILE_MES)) {
                        //根据getterId获取到对应的线程，将message进行转发
                        ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                        oos.writeObject(message);

                    } else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {//客户端退出
                        System.out.println(message.getSender() + " 退出");
                        //将客户端对应的线程从集合中删除
                        ManageClientThreads.removeServerConnectClientThread(message.getContent());
                        socket.close();//关闭连接
                        //退出线程
                        break;  //直接退出while循环，即退出线程，

                    } else {
                        System.out.println("其他类型的业务，暂时不处理");
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }


    }
}
