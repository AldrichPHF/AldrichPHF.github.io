package com.phfjava.tomcat.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author 庞化峰
 * @version 1.0
 */
public class WebUtils {

    //将一个字符串转成数字的方法
    public static int parseInt(String strNum, int defaultVal) {
        try {
            return Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            System.out.println(strNum + " 不能转成数字");
        }
        return defaultVal;
    }


    /**
     * 判断URI是不是html文件
     *
     * @param uri
     * @return
     */
    public static boolean isHtml(String uri) {
        //判断后缀是不是.html
        return uri.endsWith(".html");
    }

    /**
     * 根据文件名来读取该文件
     */
    public static String readHtml(String filename) {
        String path = WebUtils.class.getResource("/").getPath();//path = /E:/idea_javaweb_projects/phftomcat/target/classes/
        // StringBuilder是字符串处理 类
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(path + filename));
            String buff = "";
            while ((buff = bufferedReader.readLine()) != null) {
                stringBuilder.append(buff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
