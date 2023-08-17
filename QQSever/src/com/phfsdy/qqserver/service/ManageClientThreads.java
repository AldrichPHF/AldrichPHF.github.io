package com.phfsdy.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 庞化峰
 * @version 1.0
 * 该类用于管理和客户顿通信的线程
 */
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {//返回hm
        return hm;
    }

    //添加线程对象到hm集合
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //根据userId 返回 ServerConnectClientThread 线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //  从集合中移除某个线程对象的方法
    public static void removeServerConnectClientThread(String userId) {
        hm.remove(userId);
    }

    //编写方法，可以返回在线用户列表
    public static String getOnlineUser() {
        //集合遍历，遍历hashMap 的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }
}
