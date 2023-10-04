package com.phfjava.tomcat;

import com.phfjava.tomcat.handler.PhfRequestHandler;
import com.phfjava.tomcat.servlet.PhfHttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 庞化峰
 * @version 1.0
 * 第三版的tomcat，实现xml + 反射 来初始化容器
 */
public class PhfTomcatV3 {
    //1.容器1 servletMapping
    //2.使用ConcurrentHashMap或者HashMap;
    //   ConcurrentHashMap 不能存放 null值
    //3.     key             --value
    //     ServletName       对应的实例

    /**
     * Value 的类型是PhfHttpServlet
     * 因为PhfHttpServlet 是业务servlet 的父类，可以存放它所有子类的对象
     */
    public static final ConcurrentHashMap<String, PhfHttpServlet>
            servletMapping = new ConcurrentHashMap<>();


    //1.容器2 servletMapping
    //2.使用ConcurrentHashMap或者HashMap;
    //   ConcurrentHashMap 不能存放 null值
    //3.     key             --value
    //    url-pattern       ServletName
    public static final ConcurrentHashMap<String, String> servletUrlMapping = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        PhfTomcatV3 phfTomcatV3 = new PhfTomcatV3();
        phfTomcatV3.init();
        //启动PhfTomcatV3 容器
        phfTomcatV3.run();
    }


    public void run() {//不是线程类
        try {
            ServerSocket serverSocket = new ServerSocket(8088);
            System.out.println("===========PhfTomcatV3 在8088端口监听");
            while (!serverSocket.isClosed()) {
                Socket accept = serverSocket.accept();
                PhfRequestHandler phfRequestHandler = new PhfRequestHandler(accept);
                new Thread(phfRequestHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //对两个容器初始化
    public void init() {
        //读取web.xml ===>  dom4j
        //得到web.xml 文件的路径 ==>拷贝一份
        String path = PhfTomcatV3.class.getResource("/").getPath();
//        System.out.println("path = "+path);//path = /E:/idea_javaweb_projects/phftomcat/target/classes/
        //使用dom4j 技术，完成读取
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new File(path + "web.xml"));
            System.out.println("document = " + document);
//            获取根元素 即 <web-app>
            Element rootElement = document.getRootElement();
            //获取根元素 下面的所有元素
            List<Element> elements = rootElement.elements();
            //遍历并过滤到 servlet  servlet-mapping
            for (Element element : elements) {
                if (element.getName().equalsIgnoreCase("servlet")) {
                    //这是一个servlet 配置.
//                    System.out.println("发现 servlet");

                    //使用反射 将该servlet 实例放入servletMapping
                    Element servletName = element.element("servlet-name");
                    Element servletClass = element.element("servlet-class");
//              trim() 去掉字符串左右两端的空格
                    servletMapping.put(servletName.getText(),
                            (PhfHttpServlet) Class.forName(servletClass.getText().trim()).newInstance());
                } else if (element.getName().equalsIgnoreCase("servlet-mapping")) {
                    //这是一个servlet-mapping 配置
//                    System.out.println("发现 servlet-mapping");
                    Element servletName = element.element("servlet-name");
                    Element urlPatter = element.element("url-pattern");
                    servletUrlMapping.put(urlPatter.getText(), servletName.getText());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //验证 两个容器是否初始化成功
        System.out.println("servletMapping" + servletMapping);
        System.out.println("servletUrlMapping" + servletUrlMapping);

    }

}
