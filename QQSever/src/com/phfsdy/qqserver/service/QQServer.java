package com.phfsdy.qqserver.service;

import com.phfsdy.qqcommon.Message;
import com.phfsdy.qqcommon.MessageType;
import com.phfsdy.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 庞化峰
 * @version 1.0
 * 服务端，监听端口，等待客户端的连接，并保持通信
 */
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果是这些用户登录，就认为合法
    //这里也可以使用ConcurrentHashMap,可以处理并发的集合，没有线程安全
    //HashMap 没有处理线程安全，因此在多线程情况下不安全
    //ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {//在静态代码块中初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("庞化峰", new User("庞化峰", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("庞艳杰", new User("庞艳杰", "123456"));

    }

    //验证用户是否有效的方法
    private boolean checkUser(String userId, String passwd) {
        User user = validUsers.get(userId);
        //过关的验证方式
        if (user == null) {//说明userId没有存在validUser的key
            return false;
        }
        if (!user.getPasswd().equals(passwd)) {//userId正确，但是密码错误
            return false;
        }
        System.out.println("用户验证成功");
        return true;

    }

    public QQServer() {
        //端口可以写在配置文件中
        try {
            System.out.println("服务端在9999端口监听....");
            //启动推送新闻的线程
            new Thread(new sendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true) {//当和某个客户端连接后，会继续监听，因此，使用while
                Socket socket = ss.accept();//如果没有客户端连接，就会阻塞在这里
                //得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端发送的User对象
                //创建一个Message对象，准备回复客户端
                Message message = new Message();
                //验证
                if (checkUser(u.getUserId(), u.getPasswd())) {//合法
                    //登录通过
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message对象回复客户端
                    oos.writeObject(message);

                    //创建一个线程和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    //启动该线程
                    serverConnectClientThread.start();
                    //把线程对象放入集合，进行管理
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);

                } else {//登录失败
                    System.out.println("用户 Id:" + u.getUserId() + " 密码：" + u.getPasswd() + " 登录验证失败");
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭Socket
                    socket.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务器退出了while ，说明服务器端不在监听，因此关闭ServerSocket
            try {
                ss.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
