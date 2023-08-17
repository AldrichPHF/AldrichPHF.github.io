package com.phfsdy.service;

import com.phfsdy.qqcommon.Message;
import com.phfsdy.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author 庞化峰
 * @version 1.0
 * 该类、对象，提供和消息相关的方法
 */

public class MessageClientService {




    public void sendMessageToAll(String content,String senderId){//群发
        //构建message
        Message message = new Message();
        message.setMessageType(MessageType.Message_TO_ALL_MES);//群发聊天消息
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println("\u001B[34m" +senderId + " 对所有人说 ：" +  content+"\u001B[0m");
        //发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
     * @param content  消息内容
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendMessageToOne(String content, String senderId, String getterId) {
        //构建message
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_COMM_MES);//普通的聊天消息
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println("\u001B[34m"+senderId + " 对 " + getterId + " 说 " + content+"\u001B[0m");
        //发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
