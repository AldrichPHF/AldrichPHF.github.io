package com.phfsdy.qqserver.service;


import com.phfsdy.qqcommon.Message;
import com.phfsdy.qqcommon.MessageType;
import com.phfsdy.utils.Utility;
import sun.util.calendar.LocalGregorianCalendar;

import javax.print.attribute.standard.MediaName;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 庞化峰
 * @version 1.0
 */
public class sendNewsToAllService implements Runnable {

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的新闻(输入exit表示退出推送服务)：");
            String news = Utility.readString(100);
            if (news.equals("exit")){
                break;
            }
            //构建一个消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMessageType(MessageType.Message_TO_ALL_MES);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说：" + message.getContent());
            //遍历当前所有的通信线程，得到socket，并发送message
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                ServerConnectClientThread serverConnectClientThread = hm.get(onLineUserId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }


    }
}
