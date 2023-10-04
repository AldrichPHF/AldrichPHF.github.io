package com.phfjava.tomcat.http;

/**
 * @author 庞化峰
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 模拟 HttpServletRequest
 * 1.PhfRequest 作用是封装HTTP请求的数据
 * 2.比如 method（get）、uri(/phfCalServlet)、还有参数列表（num1=10&num2=12）
 */
public class PhfRequest {
    private String method;
    private String uri;
    //存放参数列表 参数名-参数值
    private HashMap<String, String> parameterMapping = new HashMap<>();
    private InputStream inputStream = null;

    //构造器 -> 对http请求封装
//    inputStream 是和对应的http请求的socket关联
    public PhfRequest(InputStream inputStream) {
        //完成对HTTP请求数据的封装

      /*  将外部socket关联的inputStream直接给到类的属性
          如果直接将与socket关联的inputStream直接赋给方法的参数，
          以后就无法单独调用与socket关联的inputStream。
          将与socket关联的inputStream赋给类的属性后，也就不需要
          为init()方法传给inputStream参数
       */
        this.inputStream = inputStream;
        encapHttpRequest();

    }

    //封装http请求的相关数据，然后提供相关的方法进行获取
    private void encapHttpRequest() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            //读取第一行
            // GET - /phfCalServlet?num1=19&num2=12 - HTTP/1.1
            String requestLine = bufferedReader.readLine();
            String[] requestLineArr = requestLine.split(" ");
            //得到method值
            method = requestLineArr[0];
            //解析得到 /PhfCalServlet
            //1.先看uri 有没有参数
            int index = requestLineArr[1].indexOf("?");
            if (index == -1) {//说明没有参数列表
                uri = requestLineArr[1];
            } else {
                //[0,index)
                uri = requestLineArr[1].substring(0, index);

                //获取参数列表 -> parameters
                String parameters = requestLineArr[1].substring(index + 1);//?以后的部分
                //num1=10,num2=12,......
                String[] parametersPair = parameters.split("&");
                //parametersPair[] = {"num1=10","num2=12",......}
                if ((null != parametersPair) && (!"".equals(parametersPair))) {//防止用户不传参数
                    //再次进行分割
                    for (String parameterPair : parametersPair) {
                        String[] parameterVal = parameterPair.split("=");
                        if (parameterVal.length == 2) {//确认传递参数为两个
                            parameterMapping.put(parameterVal[0], parameterVal[1]);
                        }
                    }
                }
            }

            //不能在这里关闭 inputStream ,因为inputStream和socket关联
//            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //request有一个特别重要的方法
    public String getParameter(String name) {
        if (parameterMapping.containsKey(name)) {//如果parameterMapping 里面含有key为name
            return parameterMapping.get(name);
        } else {
            return "";
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "PhfRequest{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", parameterMapping=" + parameterMapping +
                '}';
    }
}
