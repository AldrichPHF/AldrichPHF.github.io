package com.phfjava.tomcat.http;

/**
 * @author 庞化峰
 * @version 1.0
 */

import com.phfjava.tomcat.utils.WebUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 模拟 HttpServletResponse
 * PhfResponse 对象可以封装OutputStream(是socket关联）
 * 2.可以通过PhfResponse对象 返回HTTP响应给浏览器/客户端
 */
public class PhfResponse {
    private OutputStream outputStream = null;

    //写一个HTTP的响应头  ==> 先死后活
    public static final String respHeader = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html;charset=utf-8\r\n\r\n";

    //在创建PhfResponse 对象时，传入的outputStream是和socket关联的
    public PhfResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    //当需要给浏览器返回数据的时候，可以通过PhfResponse的输出流来完成
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public  void htmlResp(String uri, Socket socket) {
        //============新增业务逻辑===========
        //1.判断uri是什么资源
        //2.如果是静态资源就读取，并返回给浏览器；contentType text/html
        //   封装成方法到--工具类
        //3.因为自己做的不是标准的tomcat，不是一个真正的web项目，
        //   所以手动将静态资源放到target/classes/cal.html

        //过滤，拦截 权限。。。。。。
        if (WebUtils.isHtml(uri)) {//是静态页面
            //因为uti 前面有 / ，所以要截取 / 后面的内容
            String content = WebUtils.readHtml(uri.substring(1));
            content = PhfResponse.respHeader + content;
            System.out.println("content = " + content);
            //得到outputStream ,返回信息给浏览器
            try {
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();

                //如果确认访问的是静态页面，就会直接return，下面代码不会执行
                //所以要在这里关闭socket
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }
}
