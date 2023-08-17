package com.phfsdy.qqclient.view;

import com.phfsdy.qqclient.utils.Utility;
import com.phfsdy.qqcommon.Message;
import com.phfsdy.service.FileClientService;
import com.phfsdy.service.MessageClientService;
import com.phfsdy.service.UserClientService;

/**
 * @author 庞化峰
 * @version 1.0
 * 客户端的菜单界面
 */
public class QQView {

    private String key = "";//接收键盘输入
    private boolean loop = true;//控制是否显示菜单
    private UserClientService userClientService = new UserClientService();//对象是用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用于私聊/群聊
    private FileClientService fileClientService = new FileClientService();//用于文件传输

    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
        System.out.println("客户端退出系统");
    }


    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("============欢迎登录网络通信系统==========");
            System.out.println("\t\t1 登录系统");
            System.out.println("\t\t9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);//键盘输入，限制输入长度为1
            switch (key) {
                case "1":
                    System.out.println("登录系统");
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码：");
                    String passwd = Utility.readString(50);
                    if (userClientService.checkUser(userId, passwd)) {//判断输入的用户名和密码是否合法
                        System.out.println("===========欢迎用户" + userId + "登陆成功=============");
                        //进入二级菜单
                        while (loop) {
                            System.out.println("======网络通讯系统二级菜单  用户" + userId + "===========");
                            System.out.println("\t\t1 显示在线用户列表");
                            System.out.println("\t\t2 群发消息");
                            System.out.println("\t\t3 私聊消息");
                            System.out.println("\t\t4 发送文件");
                            System.out.println("\t\t9 退出系统");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    System.out.println("显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    System.out.println("请输入群发消息的内容：");
                                    String toAllContent = Utility.readString(100);
                                    //调用群发的方法，将message封装成对象发送给服务端
                                    messageClientService.sendMessageToAll(toAllContent, userId);


                                    break;
                                case "3":
                                    System.out.println("私聊消息");
                                    System.out.println("请输入要聊天的用户名（在线）：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入聊天内容：");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将私聊的消息发送给服务端
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    System.out.println("请输入接收用户（在线）：");
                                    String getter = Utility.readString(50);
                                    System.out.println("请输入要发送的文件路径（形式 d:\\xx.jpg）：");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入接收文件目录（形式 d:\\yy.jpg）：");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src, dest, userId, getter);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {//登录服务器失败
                        System.out.println("========登录失败================");
                    }
                    break;
                case "9":

                    loop = false;
                    break;
            }

        }
    }
}
